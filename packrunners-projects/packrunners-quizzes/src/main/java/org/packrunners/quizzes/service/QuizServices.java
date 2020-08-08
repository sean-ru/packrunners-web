package org.packrunners.quizzes.service;

import info.magnolia.cms.util.QueryUtil;
import info.magnolia.cms.util.SelectorUtil;
import info.magnolia.context.MgnlContext;
import info.magnolia.dam.api.Asset;
import info.magnolia.dam.templating.functions.DamTemplatingFunctions;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.jcr.wrapper.I18nNodeWrapper;
import info.magnolia.link.LinkException;
import info.magnolia.link.LinkTransformerManager;
import info.magnolia.link.LinkUtil;
import info.magnolia.module.categorization.functions.CategorizationTemplatingFunctions;
import info.magnolia.rendering.template.type.DefaultTemplateTypes;
import info.magnolia.rendering.template.type.TemplateTypeHelper;
import info.magnolia.templating.functions.TemplatingFunctions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.packrunners.categories.Category;
import org.packrunners.quizzes.QuizzesModule;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.jcr.*;
import javax.jcr.query.Query;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;


/**
 * Service class used by several model classes.
 */
@Singleton
@Slf4j
public class QuizServices {

    public static final String QUIZ_QUERY_PARAMETER = "quiz";
    public static final String DEFAULT_COURSE = "Honors-Algebra-2";

    private final QuizzesModule quizzesModule;
    private final TemplateTypeHelper templateTypeHelper;
    private final TemplatingFunctions templatingFunctions;
    private final CategorizationTemplatingFunctions categorizationTemplatingFunctions;
    private final DamTemplatingFunctions damFunctions;
    private final LinkTransformerManager linkTransformerManager;

    @Inject
    public QuizServices(QuizzesModule quizzesModule, TemplateTypeHelper templateTypeHelper,
                        TemplatingFunctions templatingFunctions,
                        CategorizationTemplatingFunctions categorizationTemplatingFunctions,
                        DamTemplatingFunctions damFunctions, LinkTransformerManager linkTransformerManager) {
        this.quizzesModule = quizzesModule;
        this.templateTypeHelper = templateTypeHelper;
        this.templatingFunctions = templatingFunctions;
        this.categorizationTemplatingFunctions = categorizationTemplatingFunctions;
        this.damFunctions = damFunctions;
        this.linkTransformerManager = linkTransformerManager;
    }

    /**
     * Tries to determine {@link Category} from passed URL selector (e.g.
     * <code>/page~category_name~.html</code>).
     */
    public Category getCategoryByUrl() {
        final String categoryName = StringUtils
                .defaultIfBlank(SelectorUtil.getSelector(0), DEFAULT_COURSE);
        return getCategoryByName(categoryName);
    }

    /**
     * Returns a {@link Category} object based on path or name of category.
     */
    public Category getCategoryByName(String categoryName) {
        final String categoryWorkspace = categorizationTemplatingFunctions
                .getCategorizationRepository();

        Category category = null;
        try {
            final Node categoryNode = getContentNodeByName(categoryName, categoryWorkspace);
            if (categoryNode != null) {
                category = marshallCategoryNode(categoryNode);
            }
        } catch (RepositoryException e) {
            log.debug("Could not find category with name [{}] in workspace [{}]", categoryName,
                    categoryWorkspace);
        }

        return category;
    }

    /**
     * Creates a {@link Category} object from a {@link Node}.
     */
    public Category marshallCategoryNode(Node categoryNodeRaw) {
        Category category = null;

        if (categoryNodeRaw != null) {
            try {
                final Node categoryNode = wrapForI18n(categoryNodeRaw);

                String name = categoryNode.getName();
                if (categoryNode.hasProperty(Category.PROPERTY_NAME_DISPLAY_NAME)) {
                    name = categoryNode.getProperty(Category.PROPERTY_NAME_DISPLAY_NAME).getString();
                }

                category = new Category(name, categoryNode.getIdentifier());

                // We always require a slug, here using the node name as the name might have a nicer display name
                category.setNodeName(categoryNode.getName());

                if (categoryNode.hasProperty(Category.PROPERTY_NAME_DESCRIPTION)) {
                    category.setDescription(
                            categoryNode.getProperty(Category.PROPERTY_NAME_DESCRIPTION).getString());
                }

                if (categoryNode.hasProperty(Category.PROPERTY_NAME_BODY)) {
                    category.setBody(categoryNode.getProperty(Category.PROPERTY_NAME_BODY).getString());
                }

                if (categoryNode.hasProperty(Category.PROPERTY_NAME_IMAGE)) {
                    Asset image = damFunctions
                            .getAsset(categoryNode.getProperty(Category.PROPERTY_NAME_IMAGE).getString());
                    category.setImage(image);
                }

                if (categoryNode.hasProperty(Category.PROPERTY_NAME_ICON)) {
                    Asset icon = damFunctions
                            .getAsset(categoryNode.getProperty(Category.PROPERTY_NAME_ICON).getString());
                    category.setIcon(icon);
                }
            } catch (RepositoryException e) {
                log.debug("Could not marshall category from node [{}]", categoryNodeRaw);
            }
        }

        return category;
    }

    /**
     * Returns a list of {@link Category}/ies.
     * <p>
     * Uses the passed {@link Node} from the website repository to find feature template sub type to
     * generate {@link Category} link.
     */
    public List<Category> marshallCategoryNodes(List<Node> categoryNodes, Node contentNode,
                                                String featureSubTypeName) {
        final List<Category> categories = new ArrayList<>();

        for (Node categoryNode : categoryNodes) {
            final Category category = marshallCategoryNode(categoryNode);
            if (category != null) {
                try {
                    final String link = getCategoryLink(contentNode, categoryNode.getName(),
                            featureSubTypeName);
                    category.setLink(link);
                } catch (RepositoryException e) {
                    log.error("Could not get node name of category node [{}]", categoryNode, e);
                }

                categories.add(category);
            }
        }

        return categories;
    }

    private Node wrapForI18n(Node node) {
        return NodeUtil.isWrappedWith(node, I18nNodeWrapper.class) ? node
                : templatingFunctions.wrapForI18n(node);
    }

    /**
     * Creates a {@link Quiz} from a {@link Node}.
     */
    public Quiz marshallQuizNode(Node quizNodeRaw) {
        Quiz quiz = null;

        if (quizNodeRaw != null) {
            final Node quizNode = wrapForI18n(quizNodeRaw);

            quiz = new Quiz();

            try {
                quiz.setIdentifier(quizNode.getIdentifier());

                quiz.setName(quizNode.getName());
                if (quizNode.hasProperty(Quiz.PROPERTY_NAME_DISPLAY_NAME)) {
                    quiz.setName(quizNode.getProperty(Quiz.PROPERTY_NAME_DISPLAY_NAME).getString());
                }

                if (quizNode.hasProperty(Quiz.PROPERTY_NAME_DESCRIPTION)) {
                    Property description = quizNode.getProperty(Quiz.PROPERTY_NAME_DESCRIPTION);
                    if (LinkUtil.UUID_PATTERN.matcher(description.getString()).find()) {
                        try {
                            String bodyWithResolvedLinks = LinkUtil
                                    .convertLinksFromUUIDPattern(description.getString(),
                                            linkTransformerManager.getBrowserLink(quizNode.getPath()));
                            quiz.setDescription(bodyWithResolvedLinks);
                        } catch (LinkException e) {
                            log.warn("Failed to parse links with from {}", description.getName(), e);
                        }
                    } else {
                        quiz.setDescription(description.getString());
                    }
                }

                if (quizNode.hasProperty(Quiz.PROPERTY_NAME_AUTHOR)) {
                    quiz.setAuthor(quizNode.getProperty(Quiz.PROPERTY_NAME_AUTHOR).getString());
                }

                if (quizNode.hasProperty(Quiz.PROPERTY_NAME_TAGS)) {
                    quiz.setTags(quizNode.getProperty(Quiz.PROPERTY_NAME_TAGS).getString());
                }

                if (quizNode.hasProperty(Quiz.PROPERTY_NAME_LAST_MODIFIED_DATE)) {
                    Calendar c = quizNode.getProperty(Quiz.PROPERTY_NAME_LAST_MODIFIED_DATE)
                            .getDate();
                    if (c != null) {
                        quiz.setLastModifiedDate(c.getTime());
                    }
                }

                if (quizNode.hasProperty(Quiz.PROPERTY_NAME_QUIZ_SHEET_URL)) {
                    quiz.setQuizSheetUrl(quizNode.getProperty(Quiz.PROPERTY_NAME_QUIZ_SHEET_URL).getString());
                }

                if (quizNode.hasProperty(Quiz.PROPERTY_NAME_COURSE_TYPES)) {
                    final List<Category> courseTypes = getCategories(quizNode,
                            Quiz.PROPERTY_NAME_COURSE_TYPES);
                    quiz.setCourseTypes(courseTypes);
                }

                if (quizNode.hasProperty(Quiz.PROPERTY_NAME_SCHOOLS)) {
                    final List<Category> schools = getCategories(quizNode,
                            Quiz.PROPERTY_NAME_SCHOOLS);
                    quiz.setSchools(schools);
                }

                if (quizNode.hasProperty(Quiz.PROPERTY_NAME_COURSE_NUMBERS)) {
                    final List<Category> courseNames = getCategories(quizNode,
                            Quiz.PROPERTY_NAME_COURSE_NUMBERS);
                    quiz.setCourseNumbers(courseNames);
                }

                final String quizLink = getQuizLink(quizNode);
                if (StringUtils.isNotBlank(quizLink)) {
                    quiz.setLink(quizLink);
                }

            } catch (RepositoryException e) {
                log.debug("Could not marshall quiz from node [{}]", quizNodeRaw);
            }
        }

        return quiz;
    }

    public Node getQuizNodeByParameter() throws RepositoryException {
        final String quizName = StringUtils
                .defaultIfBlank(MgnlContext.getParameter(QUIZ_QUERY_PARAMETER), "NotAvailable");
        return getContentNodeByName(quizName, QuizzesModule.QUIZZES_REPOSITORY_NAME);
    }

    /**
     * Get and marshall all categories of a {@link Node} stored under the given
     * <code>categoryPropertyName</code>.
     */
    private List<Category> getCategories(Node node, String categoryPropertyName) {
        final List<Category> categories = new ArrayList<>();

        final List<Node> nodes = categorizationTemplatingFunctions
                .getCategories(node, categoryPropertyName);
        for (Node n : nodes) {
            final Category category = marshallCategoryNode(n);
            if (category != null) {
                categories.add(category);
            }
        }

        return categories;
    }

    /**
     * Get the Link as String of the category of a specific page type If no category found, return
     * empty String.
     */
    public String getCategoryLink(Node content, String categoryName, String featureSubType) {
        try {
            Node siteRoot = templatingFunctions.siteRoot(content);
            Node courseTypeOverviewPage = categorizationTemplatingFunctions
                    .getContentByTemplateCategorySubCategory(siteRoot, DefaultTemplateTypes.FEATURE,
                            featureSubType);

            if (courseTypeOverviewPage != null) {
                return templatingFunctions.link(courseTypeOverviewPage).replace(".html",
                        SelectorUtil.SELECTOR_DELIMITER + categoryName + SelectorUtil.SELECTOR_DELIMITER
                                + ".html");
            }
        } catch (RepositoryException e) {
            log.warn("Can't get category page link [subType={}]", featureSubType, e);
        }

        return StringUtils.EMPTY;
    }

    private Node getContentNodeByName(final String pathOrName, final String workspace)
            throws RepositoryException {
        if (pathOrName.startsWith("/")) {
            return MgnlContext.getJCRSession(workspace)
                    .getNode(StringUtils.substringBefore(pathOrName, "?"));
        } else {
            final String sql = String
                    .format("SELECT * FROM [nt:base] AS content WHERE name(content)='%s'", pathOrName);
            final NodeIterator items = QueryUtil.search(workspace, sql, Query.JCR_SQL2, "mgnl:content");

            if (items != null && items.hasNext()) {
                return items.nextNode();
            }
        }

        log.warn("Could not find node from workspace [{}] based on slug [{}]", workspace, pathOrName);
        return null;
    }

    public List<Quiz> getQuizByCategory(String categoryPropertyName, String identifier) {

        final List<Quiz> quizzes = new LinkedList<>();

        try {
            final Session session = MgnlContext.getJCRSession(QuizzesModule.QUIZZES_REPOSITORY_NAME);
            String query = String.format("%s LIKE '%%%s%%'", categoryPropertyName, identifier);

            final List<Node> quizNodes = templateTypeHelper
                    .getContentListByTemplateIds(session.getRootNode(), null, Integer.MAX_VALUE, query, null);
            for (Node quizNode : quizNodes) {
                final Quiz quiz = marshallQuizNode(quizNode);
                quizzes.add(quiz);
            }

        } catch (RepositoryException e) {
            log.error("Could not get quizzes by school identifier [{}={}].",
                    categoryPropertyName,
                    identifier, e);
        }

        return quizzes;
    }

    /**
     * Create a link to a specific quiz.
     */
    public String getQuizLink(Node quizNode) {
        return templatingFunctions.link(quizNode);
    }

}

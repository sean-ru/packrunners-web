package org.packrunners.tutors.service;

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
import org.packrunners.tutors.TutorsModule;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.jcr.*;
import javax.jcr.query.Query;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Service class used by several model classes.
 */
@Singleton
@Slf4j
public class TutorServices {

    public static final String TUTOR_QUERY_PARAMETER = "tutor";
    public static final String DEFAULT_SCHOOL = "NNHS";

    private final TutorsModule tutorsModule;
    private final TemplateTypeHelper templateTypeHelper;
    private final TemplatingFunctions templatingFunctions;
    private final CategorizationTemplatingFunctions categorizationTemplatingFunctions;
    private final DamTemplatingFunctions damFunctions;
    private final LinkTransformerManager linkTransformerManager;

    @Inject
    public TutorServices(TutorsModule tutorsModule, TemplateTypeHelper templateTypeHelper,
                         TemplatingFunctions templatingFunctions,
                         CategorizationTemplatingFunctions categorizationTemplatingFunctions,
                         DamTemplatingFunctions damFunctions, LinkTransformerManager linkTransformerManager) {
        this.tutorsModule = tutorsModule;
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
                .defaultIfBlank(SelectorUtil.getSelector(0), DEFAULT_SCHOOL);
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
     * Creates a {@link Tutor} from a {@link Node}.
     */
    public Tutor marshallTutorNode(Node tutorNodeRaw) {
        Tutor tutor = null;

        if (tutorNodeRaw != null) {
            final Node tutorNode = wrapForI18n(tutorNodeRaw);

            tutor = new Tutor();

            try {
                tutor.setIdentifier(tutorNode.getIdentifier());

                tutor.setName(tutorNode.getName());
                if (tutorNode.hasProperty(Tutor.PROPERTY_NAME_DISPLAY_NAME)) {
                    tutor.setName(tutorNode.getProperty(Tutor.PROPERTY_NAME_DISPLAY_NAME).getString());
                }

                if (tutorNode.hasProperty(Tutor.PROPERTY_NAME_ID)) {
                    tutor.setId(tutorNode.getProperty(Tutor.PROPERTY_NAME_ID).getString());
                }

                if (tutorNode.hasProperty(Tutor.PROPERTY_NAME_PHOTO)) {
                    tutor.setPhoto(
                            damFunctions.getAsset(tutorNode.getProperty(Tutor.PROPERTY_NAME_PHOTO).getString()));
                }

                if (tutorNode.hasProperty(Tutor.PROPERTY_NAME_PROFILE)) {
                    Property content = tutorNode.getProperty(Tutor.PROPERTY_NAME_PROFILE);
                    if (LinkUtil.UUID_PATTERN.matcher(content.getString()).find()) {
                        try {
                            String bodyWithResolvedLinks = LinkUtil
                                    .convertLinksFromUUIDPattern(content.getString(),
                                            linkTransformerManager.getBrowserLink(tutorNode.getPath()));
                            tutor.setProfile(bodyWithResolvedLinks);
                        } catch (LinkException e) {
                            log.warn("Failed to parse links with from {}", content.getName(), e);
                        }
                    } else {
                        tutor.setProfile(content.getString());
                    }
                }

                if (tutorNode.hasProperty(Tutor.PROPERTY_NAME_EMAIL)) {
                    tutor.setEmail(tutorNode.getProperty(Tutor.PROPERTY_NAME_EMAIL).getString());
                }

                if (tutorNode.hasProperty(Tutor.PROPERTY_NAME_GRADE)) {
                    tutor.setGrade(tutorNode.getProperty(Tutor.PROPERTY_NAME_GRADE).getString());
                }

                final String tutorLink = getTutorLink(tutorNode);
                if (StringUtils.isNotBlank(tutorLink)) {
                    tutor.setLink(tutorLink);
                }
            } catch (RepositoryException e) {
                log.debug("Could not marshall tutor from node [{}]", tutorNodeRaw);
            }
        }

        return tutor;
    }

    public Node getTutorNodeByParameter() throws RepositoryException {
        final String tutorName = StringUtils
                .defaultIfBlank(MgnlContext.getParameter(TUTOR_QUERY_PARAMETER),
                        tutorsModule.getDefaultTutorName());
        return getContentNodeByName(tutorName, TutorsModule.TUTORS_REPOSITORY_NAME);
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
            log.warn("Can't get schoolOverviewPage page link [subType={}]", featureSubType, e);
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

    public List<Tutor> getTutorsBySchool(String categoryPropertyName, String identifier) {

        final List<Tutor> tutors = new LinkedList<>();

        try {
            final Session session = MgnlContext.getJCRSession(TutorsModule.TUTORS_REPOSITORY_NAME);
            String query = String.format("%s LIKE '%%%s%%'", categoryPropertyName, identifier);

            final List<Node> tutorNodes = templateTypeHelper
                    .getContentListByTemplateIds(session.getRootNode(), null, Integer.MAX_VALUE, query, null);
            for (Node tutorNode : tutorNodes) {
                final Tutor tutor = marshallTutorNode(tutorNode);
                tutors.add(tutor);
            }

        } catch (RepositoryException e) {
            log.error("Could not get tutors by school identifier [{}={}].",
                    categoryPropertyName,
                    identifier, e);
        }

        return tutors;
    }

    /**
     * Create a link to a specific tutor.
     */
    public String getTutorLink(Node tutorNode) {
        return templatingFunctions.link(tutorNode);
    }

}

package org.packrunners.courses.service;

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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.packrunners.courses.CoursesModule;


/**
 * Service class used by several model classes.
 */
@Singleton
@Slf4j
public class CourseServices {

  public static final String COURSE_QUERY_PARAMETER = "course";

  private final CoursesModule coursesModule;
  private final TemplateTypeHelper templateTypeHelper;
  private final TemplatingFunctions templatingFunctions;
  private final CategorizationTemplatingFunctions categorizationTemplatingFunctions;
  private final DamTemplatingFunctions damFunctions;
  private final LinkTransformerManager linkTransformerManager;

  @Inject
  public CourseServices(CoursesModule coursesModule, TemplateTypeHelper templateTypeHelper,
      TemplatingFunctions templatingFunctions,
      CategorizationTemplatingFunctions categorizationTemplatingFunctions,
      DamTemplatingFunctions damFunctions, LinkTransformerManager linkTransformerManager) {
    this.coursesModule = coursesModule;
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
    final String categoryName = StringUtils.defaultIfBlank(SelectorUtil.getSelector(0), "active");
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
   * Creates a {@link Course} from a {@link Node}.
   */
  public Course marshallCourseNode(Node courseNodeRaw) {
    Course course = null;

    if (courseNodeRaw != null) {
      final Node courseNode = wrapForI18n(courseNodeRaw);

      course = new Course();

      try {
        course.setIdentifier(courseNode.getIdentifier());

        course.setName(courseNode.getName());
        if (courseNode.hasProperty(Course.PROPERTY_NAME_DISPLAY_NAME)) {
          course.setName(courseNode.getProperty(Course.PROPERTY_NAME_DISPLAY_NAME).getString());
        }

        if (courseNode.hasProperty(Course.PROPERTY_NAME_DESCRIPTION)) {
          Property description = courseNode.getProperty(Course.PROPERTY_NAME_DESCRIPTION);
          if (LinkUtil.UUID_PATTERN.matcher(description.getString()).find()) {
            try {
              String bodyWithResolvedLinks = LinkUtil.convertLinksFromUUIDPattern(description.getString(),
                  linkTransformerManager.getBrowserLink(courseNode.getPath()));
              course.setDescription(bodyWithResolvedLinks);
            } catch (LinkException e) {
              log.warn("Failed to parse links with from {}", description.getName(), e);
            }
          } else {
            course.setDescription(description.getString());
          }
        }

        if (courseNode.hasProperty(Course.PROPERTY_NAME_COURSE_NUMBER)) {
          course.setCourseNumber(courseNode.getProperty(Course.PROPERTY_NAME_COURSE_NUMBER).getString());
        }

        if (courseNode.hasProperty(Course.PROPERTY_NAME_WEIGHTED)) {
          course.setWeighted(courseNode.getProperty(Course.PROPERTY_NAME_WEIGHTED).getBoolean());
        }

        if (courseNode.hasProperty(Course.PROPERTY_NAME_PREREQUISITE)) {
          course.setPrerequisite(courseNode.getProperty(Course.PROPERTY_NAME_PREREQUISITE).getString());
        }

        if (courseNode.hasProperty(Course.PROPERTY_NAME_DURATION)) {
          course.setDuration(courseNode.getProperty(Course.PROPERTY_NAME_DURATION).getString());
        }

        if (courseNode.hasProperty(Course.PROPERTY_NAME_LEVEL)) {
          course.setLevel(courseNode.getProperty(Course.PROPERTY_NAME_LEVEL).getString());
        }

        if (courseNode.hasProperty(Course.PROPERTY_NAME_CREDIT)) {
          course.setCredit(courseNode.getProperty(Course.PROPERTY_NAME_CREDIT).getString());
        }

        if (courseNode.hasProperty(Course.PROPERTY_NAME_TEACHERS)) {
          course.setTeachers(courseNode.getProperty(Course.PROPERTY_NAME_TEACHERS).getString());
        }

        if (courseNode.hasProperty(Course.PROPERTY_NAME_IMAGE)) {
          course.setImage(
              damFunctions.getAsset(courseNode.getProperty(Course.PROPERTY_NAME_IMAGE).getString()));
        }

        if (courseNode.hasProperty(Course.PROPERTY_NAME_SYLLABUS)) {
          course.setSyllabus(
              damFunctions.getAsset(courseNode.getProperty(Course.PROPERTY_NAME_SYLLABUS).getString()));
        }

        if (courseNode.hasProperty(Course.PROPERTY_NAME_COURSE_TYPE)) {
          final List<Category> courseTypes = getCategories(courseNode,
              Course.PROPERTY_NAME_COURSE_TYPE);
          course.setCourseTypes(courseTypes);
        }

        if (courseNode.hasProperty(Course.PROPERTY_NAME_SCHOOL)) {
          final List<Category> schools = getCategories(courseNode,
              Course.PROPERTY_NAME_SCHOOL);
          course.setSchools(schools);
        }

        final String courseLink = getCourseLink(courseNode);
        if (StringUtils.isNotBlank(courseLink)) {
          course.setLink(courseLink);
        }
      } catch (RepositoryException e) {
        log.debug("Could not marshall course from node [{}]", courseNodeRaw);
      }
    }

    return course;
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

  public Node getCourseNodeByParameter() throws RepositoryException {
    final String courseName = StringUtils
        .defaultIfBlank(MgnlContext.getParameter(COURSE_QUERY_PARAMETER),
            coursesModule.getDefaultCourseName());
    return getContentNodeByName(courseName, CoursesModule.COURSES_REPOSITORY_NAME);
  }

  /**
   * Get the Link as String of the category of a specific page type If no category found, return
   * empty String.
   */
  public String getCategoryLink(Node content, String categoryName, String featureSubType) {
    try {
      Node siteRoot = templatingFunctions.siteRoot(content);
      Node categoryOverviewPage = categorizationTemplatingFunctions
          .getContentByTemplateCategorySubCategory(siteRoot, DefaultTemplateTypes.FEATURE,
              featureSubType);

      if (categoryOverviewPage != null) {
        return templatingFunctions.link(categoryOverviewPage).replace(".html",
            SelectorUtil.SELECTOR_DELIMITER + categoryName + SelectorUtil.SELECTOR_DELIMITER + ".html");
      }
    } catch (RepositoryException e) {
      log.warn("Can't get categoryOverview page link [subType={}]", featureSubType, e);
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

  public List<Category> getRelatedCourseTypesByParameter() {
    final List<Category> categories = new LinkedList<>();

    try {
      final Node node = getCourseNodeByParameter();

      for (Node categoryNode : categorizationTemplatingFunctions
          .getCategories(node, Course.PROPERTY_NAME_COURSE_TYPE)) {
        final Category category = marshallCategoryNode(categoryNode);
        if (category != null) {
          categories.add(category);
        }
      }

    } catch (RepositoryException e) {
      log.error("Could not retrieve related categories by course parameter.", e);
    }

    return categories;
  }

  public List<Course> getCoursesByCategory(String categoryPropertyName, String identifier) {
    return getCoursesByCategory(categoryPropertyName, identifier, false);
  }

  public List<Course> getCoursesByCategory(String categoryPropertyName, String identifier,
      boolean featured) {
    final List<Course> courses = new LinkedList<>();

    try {
      final Session session = MgnlContext.getJCRSession(CoursesModule.COURSES_REPOSITORY_NAME);
      String query = String.format("%s LIKE '%%%s%%'", categoryPropertyName, identifier);
      if (featured) {
        query += " AND isFeatured = 'true'";
      }

      final List<Node> courseNodes = templateTypeHelper
          .getContentListByTemplateIds(session.getRootNode(), null, Integer.MAX_VALUE, query, null);
      for (Node courseNode : courseNodes) {
        final Course course = marshallCourseNode(courseNode);
        courses.add(course);
      }

    } catch (RepositoryException e) {
      log.error("Could not get related courses by category identifier [{}={}].", categoryPropertyName,
          identifier, e);
    }

    return courses;
  }

  /**
   * Create a link to a specific course.
   */
  public String getCourseLink(Node courseNode) {
    return templatingFunctions.link(courseNode);
  }

}

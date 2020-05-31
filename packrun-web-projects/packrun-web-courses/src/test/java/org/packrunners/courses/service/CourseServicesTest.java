package org.packrunners.courses.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import info.magnolia.cms.beans.config.URI2RepositoryManager;
import info.magnolia.cms.beans.config.URI2RepositoryMapping;
import info.magnolia.cms.core.AggregationState;
import info.magnolia.cms.i18n.DefaultI18nContentSupport;
import info.magnolia.cms.i18n.I18nContentSupport;
import info.magnolia.cms.i18n.LocaleDefinition;
import info.magnolia.context.MgnlContext;
import info.magnolia.dam.api.Asset;
import info.magnolia.dam.templating.functions.DamTemplatingFunctions;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.link.LinkTransformerManager;
import info.magnolia.module.categorization.functions.CategorizationTemplatingFunctions;
import info.magnolia.rendering.template.type.TemplateTypeHelper;
import info.magnolia.repository.RepositoryConstants;
import info.magnolia.templating.functions.TemplatingFunctions;
import info.magnolia.test.ComponentsTestUtil;
import info.magnolia.test.mock.MockWebContext;
import info.magnolia.test.mock.jcr.MockSession;
import java.util.Locale;
import javax.inject.Provider;
import javax.jcr.Node;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.packrunners.courses.CoursesModule;


/**
 * Tests for {@link CourseServices}.
 */
public class CourseServicesTest {

  private final static String CONTEXT_PATH = "/contextPath";
  private final static String URI_PREFIX = "/courses";

  private MockWebContext context;

  private MockSession session;
  private MockSession sessionCourses;
  private DamTemplatingFunctions damTemplatingFunctions;
  private CourseServices courseServices;
  private AggregationState aggregationState;
  private DefaultI18nContentSupport defaultI18nContentSupport;

  @Before
  public void setUp() {
    session = new MockSession(RepositoryConstants.WEBSITE);
    sessionCourses = new MockSession(CoursesModule.COURSES_REPOSITORY_NAME);

    aggregationState = new AggregationState();
    aggregationState.setLocale(Locale.ENGLISH);

    defaultI18nContentSupport = new DefaultI18nContentSupport();
    defaultI18nContentSupport.setEnabled(true);
    defaultI18nContentSupport.setDefaultLocale(Locale.ENGLISH);
    defaultI18nContentSupport.addLocale(LocaleDefinition.make("en", "", true));

    context = new MockWebContext();
    context.setContextPath(CONTEXT_PATH);
    context.setAggregationState(aggregationState);
    context.addSession(RepositoryConstants.WEBSITE, session);
    context.addSession(CoursesModule.COURSES_REPOSITORY_NAME, sessionCourses);

    MgnlContext.setInstance(context);

    final LinkTransformerManager linkTransformerManager = new LinkTransformerManager();
    linkTransformerManager.setAddContextPathToBrowserLinks(true);
    ComponentsTestUtil.setInstance(LinkTransformerManager.class, linkTransformerManager);
    ComponentsTestUtil.setInstance(I18nContentSupport.class, defaultI18nContentSupport);

    final URI2RepositoryMapping courseMapping = new URI2RepositoryMapping(URI_PREFIX,
        CoursesModule.COURSES_REPOSITORY_NAME, "");
    final URI2RepositoryManager uri2RepositoryManager = new URI2RepositoryManager();
    uri2RepositoryManager.addMapping(courseMapping);
    ComponentsTestUtil.setInstance(URI2RepositoryManager.class, uri2RepositoryManager);

    final CoursesModule coursesModule = new CoursesModule();
    final TemplateTypeHelper templateTypeHelper = mock(TemplateTypeHelper.class);
    final Provider<AggregationState> aggregationStateProvider = new Provider<AggregationState>() {
      @Override
      public AggregationState get() {
        return aggregationState;
      }
    };
    final TemplatingFunctions templatingFunctions = new TemplatingFunctions(
        aggregationStateProvider, templateTypeHelper);
    final CategorizationTemplatingFunctions categorizationTemplatingFunctions = mock(
        CategorizationTemplatingFunctions.class);

    damTemplatingFunctions = mock(DamTemplatingFunctions.class);

    courseServices = new CourseServices(coursesModule, templateTypeHelper, templatingFunctions,
        categorizationTemplatingFunctions, damTemplatingFunctions, linkTransformerManager);
  }

  @After
  public void tearDown() {
    ComponentsTestUtil.clear();
    MgnlContext.setInstance(null);
  }

  @Test
  public void marshallCategoryNode() throws Exception {
    // GIVEN
    final Node node = NodeUtil
        .createPath(session.getRootNode(), "test-node", NodeTypes.ContentNode.NAME);
    node.setProperty(Category.PROPERTY_NAME_DISPLAY_NAME, "displayName");
    node.setProperty(Category.PROPERTY_NAME_BODY, "bodyText");
    node.setProperty(Category.PROPERTY_NAME_DESCRIPTION, "description");
    node.setProperty(Category.PROPERTY_NAME_IMAGE, "jcr:cafebabe-cafe-babe-cafe-babecafebabe");
    final Asset asset = mock(Asset.class);

    when(damTemplatingFunctions.getAsset(anyString())).thenReturn(asset);

    // WHEN
    final Category category = courseServices.marshallCategoryNode(node);

    // THEN
    assertThat(category, not(nullValue()));
    assertThat(category.getName(), is("displayName"));
    assertThat(category.getBody(), is("bodyText"));
    assertThat(category.getDescription(), is("description"));
    assertThat(category.getImage(), is(asset));
    assertThat(category.getIdentifier(), is(node.getIdentifier()));
  }

  @Test
  public void marshallCategoryNodeI18nized() throws Exception {
    // GIVEN
    final Locale locale = Locale.FRENCH;
    aggregationState.setLocale(locale);

    final Node node = NodeUtil
        .createPath(session.getRootNode(), "test-node", NodeTypes.ContentNode.NAME);
    node.setProperty(Category.PROPERTY_NAME_DESCRIPTION, "description");
    node.setProperty(Category.PROPERTY_NAME_DESCRIPTION + "_" + locale.toString(),
        "description_FR");

    // WHEN
    final Category category = courseServices.marshallCategoryNode(node);

    // THEN
    assertThat(category.getDescription(), is("description_FR"));
  }

  @Test
  public void marshallCourseNode() throws Exception {
    // GIVEN
    final Node node = NodeUtil
        .createPath(session.getRootNode(), "test-node", NodeTypes.ContentNode.NAME);
    node.setProperty(Course.PROPERTY_NAME_DISPLAY_NAME, "courseDisplayName");
    node.setProperty(Course.PROPERTY_NAME_DESCRIPTION, "description");
    node.setProperty(Course.PROPERTY_NAME_IMAGE, "jcr:cafebabe-cafe-babe-cafe-babecafebabe");
    final Asset asset = mock(Asset.class);

    when(damTemplatingFunctions.getAsset(anyString())).thenReturn(asset);

    // WHEN
    final Course course = courseServices.marshallCourseNode(node);

    // THEN
    assertThat(course, not(nullValue()));
    assertThat(course.getName(), is("courseDisplayName"));
    assertThat(course.getDescription(), is("description"));
    assertThat(course.getImage(), is(asset));
    assertThat(course.getIdentifier(), is(node.getIdentifier()));
  }

  @Test
  public void marshallCourseNodeI18nized() throws Exception {
    // GIVEN
    final Locale locale = Locale.GERMAN;
    aggregationState.setLocale(locale);

    final Node node = NodeUtil
        .createPath(session.getRootNode(), "test-node", NodeTypes.ContentNode.NAME);
    node.setProperty(Course.PROPERTY_NAME_DESCRIPTION, "description");
    node.setProperty(Course.PROPERTY_NAME_DESCRIPTION + "_" + locale.toString(), "description_DE");

    // WHEN
    final Course course = courseServices.marshallCourseNode(node);

    // THEN
    assertThat(course.getDescription(), is("description_DE"));
  }

  @Test
  public void courseLinkReturnsProperLinkToCourse() throws Exception {
    // GIVEN
    final String pathToCourse = "/path/to/test-course";
    final Node courseNode = NodeUtil
        .createPath(sessionCourses.getRootNode(), pathToCourse, NodeTypes.ContentNode.NAME);

    // WHEN
    final String courseLink = courseServices.getCourseLink(courseNode);

    // THEN
    assertThat(courseLink, is(CONTEXT_PATH + URI_PREFIX + pathToCourse));
  }

  @Test
  public void marshallCourseNodeResolvesLinksInDescription() throws Exception {
    // GIVEN
    final String pathToCourse = "/path/to/test-course";
    final String pathToLinkedPage = "/courses/somepage";

    final Node node = NodeUtil
        .createPath(sessionCourses.getRootNode(), pathToCourse, NodeTypes.ContentNode.NAME);
    final Node linkNode = NodeUtil
        .createPath(session.getRootNode(), pathToLinkedPage, NodeTypes.ContentNode.NAME);

    node.setProperty(Course.PROPERTY_NAME_DESCRIPTION, String
        .format("${link:{uuid:{%s},repository:{website},path:{%s}}}", linkNode.getIdentifier(),
            pathToLinkedPage));

    // WHEN
    final Course course = courseServices.marshallCourseNode(node);

    // THEN
    assertThat(course.getDescription(), is(CONTEXT_PATH + pathToLinkedPage));
  }

}
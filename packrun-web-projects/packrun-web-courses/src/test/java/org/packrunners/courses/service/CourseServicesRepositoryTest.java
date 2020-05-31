package org.packrunners.courses.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import info.magnolia.cms.core.AggregationState;
import info.magnolia.cms.i18n.DefaultI18nContentSupport;
import info.magnolia.cms.i18n.I18nContentSupport;
import info.magnolia.context.MgnlContext;
import info.magnolia.dam.templating.functions.DamTemplatingFunctions;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.link.LinkTransformerManager;
import info.magnolia.module.categorization.functions.CategorizationTemplatingFunctions;
import info.magnolia.rendering.template.registry.TemplateDefinitionRegistry;
import info.magnolia.rendering.template.type.TemplateTypeHelper;
import info.magnolia.templating.functions.TemplatingFunctions;
import info.magnolia.test.ComponentsTestUtil;
import info.magnolia.test.RepositoryTestCase;
import info.magnolia.test.mock.MockWebContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Provider;
import javax.jcr.Node;
import javax.jcr.Session;
import org.junit.Before;
import org.junit.Test;
import org.packrunners.courses.CoursesModule;


/**
 * Repository tests for {@link CourseServices}.
 */
public class CourseServicesRepositoryTest extends RepositoryTestCase {

  private final String repositoryConfigFileName = "org/packrunners/courses/service/test-courses-repositories.xml";

  private CourseServices courseServices;

  private Session courseSession;

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();

    courseSession = MgnlContext.getJCRSession(CoursesModule.COURSES_REPOSITORY_NAME);

    final CoursesModule coursesModule = new CoursesModule();
    final TemplateDefinitionRegistry templateDefinitionRegistry = mock(
        TemplateDefinitionRegistry.class);
    final TemplateTypeHelper templateTypeHelper = new TemplateTypeHelper(
        templateDefinitionRegistry);
    final Provider<AggregationState> aggregationStateProvider = new Provider<AggregationState>() {
      @Override
      public AggregationState get() {
        return MgnlContext.getAggregationState();
      }
    };
    final TemplatingFunctions templatingFunctions = new TemplatingFunctions(
        aggregationStateProvider, templateTypeHelper);

    courseServices = new CourseServices(coursesModule, templateTypeHelper, templatingFunctions,
        mock(CategorizationTemplatingFunctions.class), mock(DamTemplatingFunctions.class),
        new LinkTransformerManager());

    ComponentsTestUtil.setImplementation(I18nContentSupport.class, DefaultI18nContentSupport.class);
  }

  @Override
  public String getRepositoryConfigFileName() {
    return this.repositoryConfigFileName;
  }

  @Test
  public void getCourseNodeByParameter() throws Exception {
    // GIVEN
    final Map<String, String> parameters = new HashMap<>();
    parameters.put(CourseServices.COURSE_QUERY_PARAMETER, "quz");

    ((MockWebContext) MgnlContext.getInstance()).setParameters(parameters);

    final Node node = NodeUtil
        .createPath(courseSession.getRootNode(), "/test/foo/bar/quz", NodeTypes.Content.NAME,
            true); // Need to save, running a query afterwards

    // WHEN
    final Node courseNode = courseServices.getCourseNodeByParameter();

    // THEN
    assertThat(courseNode.getIdentifier(), is(node.getIdentifier()));
  }

  @Test
  public void getRelatedCoursesByCategory() throws Exception {
    // GIVEN
    final Node referenceNode = NodeUtil
        .createPath(courseSession.getRootNode(), "/test/foo/bar/quz", NodeTypes.Content.NAME);
    final Node node = NodeUtil
        .createPath(courseSession.getRootNode(), "/another-test", NodeTypes.Content.NAME);
    node.setProperty("isFeatured", true);
    node.setProperty(Course.PROPERTY_NAME_COURSE_TYPE,
        new String[]{referenceNode.getIdentifier()});

    courseSession.save(); // Need to save, running a query afterwards

    // WHEN
    final List<Course> courses = courseServices
        .getCoursesByCategory(Course.PROPERTY_NAME_COURSE_TYPE,
            referenceNode.getIdentifier(), true);

    // THEN
    assertThat(courses, hasSize(1));
    assertThat(courses.get(0).getIdentifier(), is(node.getIdentifier()));
  }

}
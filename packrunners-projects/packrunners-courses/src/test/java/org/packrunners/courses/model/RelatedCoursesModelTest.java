package org.packrunners.courses.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.test.MgnlTestCase;
import java.util.ArrayList;
import java.util.List;
import javax.jcr.Node;
import org.junit.Before;
import org.junit.Test;
import org.packrunners.courses.model.definition.CourseCategoryTemplateDefinition;
import org.packrunners.courses.service.Course;
import org.packrunners.courses.service.CourseServices;


/**
 * Tests for {@link RelatedCoursesModel}.
 */
public class RelatedCoursesModelTest extends MgnlTestCase {

  private CourseServices courseServices;
  private RelatedCoursesModel model;
  private CourseCategoryTemplateDefinition templateDefinition;

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();
    this.courseServices = mock(CourseServices.class);
    templateDefinition = mock(CourseCategoryTemplateDefinition.class);

    this.model = new RelatedCoursesModel(mock(Node.class), templateDefinition,
        mock(RenderingModel.class),
        courseServices);
  }

  @Test
  public void testFilterCurrentCourse() throws Exception {
    // GIVEN
    final Node currentCourseNode = mock(Node.class);
    final Course currentCourse = new Course();
    final Course someCourse1 = new Course();
    final Course someCourse2 = new Course();

    String identifier = "bla-124-124sd";
    String categoryName = "some-category";

    currentCourse.setIdentifier(identifier);
    someCourse1.setIdentifier(identifier + "-1");
    someCourse2.setIdentifier(identifier + "-2");

    when(currentCourseNode.getIdentifier()).thenReturn(identifier);
    when(templateDefinition.getCategory()).thenReturn(categoryName);
    when(courseServices.getCourseNodeByParameter()).thenReturn(currentCourseNode);

    List<Course> courses = new ArrayList<Course>() {{
      add(0, currentCourse);
      add(1, someCourse1);
      add(1, someCourse2);
    }};
    when(courseServices.getCoursesByCategory(categoryName, identifier, true)).thenReturn(courses);

    // WHEN
    List<Course> relatedCourses = model.getRelatedCoursesByCategory(identifier);

    // THEN
    assertThat(relatedCourses, not(hasItem(currentCourse)));
    assertThat(relatedCourses, hasItem(someCourse1));
    assertThat(relatedCourses, hasItem(someCourse2));
  }

}

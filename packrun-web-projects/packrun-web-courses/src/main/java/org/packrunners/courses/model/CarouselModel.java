package org.packrunners.courses.model;

import info.magnolia.context.MgnlContext;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.model.RenderingModelImpl;
import info.magnolia.rendering.template.RenderableDefinition;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import lombok.extern.slf4j.Slf4j;
import org.packrunners.courses.CoursesModule;
import org.packrunners.courses.service.Course;
import org.packrunners.courses.service.CourseServices;


/**
 * Model class for the carousel. Gets the linked courses from current content node and creates
 * {@link Course} objects.
 *
 * @param <RD> The {@link RenderableDefinition} of the model.
 */
@Slf4j
public class CarouselModel<RD extends RenderableDefinition> extends RenderingModelImpl<RD> {

  public static final String PROPERTY_NAME_COURSES = "courses";

  private final CourseServices courseServices;

  @Inject
  public CarouselModel(Node content, RD definition, RenderingModel<?> parent,
      CourseServices courseServices) {
    super(content, definition, parent);
    this.courseServices = courseServices;
  }

  public List<Course> getCourses() {
    final List<Course> courses = new LinkedList<Course>();

    final Object object = PropertyUtil.getPropertyValueObject(content, PROPERTY_NAME_COURSES);
    if (object instanceof List) {
      final List<String> results = (List<String>) object;
      for (String identifier : results) {
        try {
          final Course course = getCourse(identifier);
          if (course != null) {
            courses.add(course);
          }
        } catch (RepositoryException e) {
          log.error("Could not retrieve linked course with identifier [{}].", identifier, e);
        }
      }
    }

    return courses;
  }

  private Course getCourse(String identifier) throws RepositoryException {
    final Session session = MgnlContext.getJCRSession(CoursesModule.COURSES_REPOSITORY_NAME);
    final Node courseNode = session.getNodeByIdentifier(identifier);

    return courseServices.marshallCourseNode(courseNode);
  }

}

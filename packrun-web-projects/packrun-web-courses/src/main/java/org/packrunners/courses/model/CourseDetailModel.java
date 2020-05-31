package org.packrunners.courses.model;

import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.model.RenderingModelImpl;
import info.magnolia.rendering.template.TemplateDefinition;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import lombok.extern.slf4j.Slf4j;
import org.packrunners.courses.service.Course;
import org.packrunners.courses.service.CourseServices;


/**
 * Model for displaying a course and its related courses by type or category.
 */
@Slf4j
public class CourseDetailModel extends RenderingModelImpl<TemplateDefinition> {

  private final CourseServices courseServices;

  @Inject
  public CourseDetailModel(Node content, TemplateDefinition definition, RenderingModel<?> parent,
      CourseServices courseServices) {
    super(content, definition, parent);
    this.courseServices = courseServices;
  }

  public Course getCourse() {
    try {
      final Node node = courseServices.getCourseNodeByParameter();
      return courseServices.marshallCourseNode(node);
    } catch (RepositoryException e) {
      log.error("Could not get course by course parameter.", e);
    }
    return null;
  }

}

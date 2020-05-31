package org.packrunners.courses.model;

import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.model.RenderingModelImpl;
import java.util.List;
import javax.inject.Inject;
import javax.jcr.Node;
import org.packrunners.courses.model.definition.CourseCategoryTemplateDefinition;
import org.packrunners.courses.service.Category;
import org.packrunners.courses.service.Course;
import org.packrunners.courses.service.CourseServices;


/**
 * Model for retrieving courses by type or category.
 *
 * @param <RD> The {@link CourseCategoryTemplateDefinition} of the model.
 */
public class CourseListModel<RD extends CourseCategoryTemplateDefinition> extends
    RenderingModelImpl<RD> {

  private final CourseServices courseServices;

  @Inject
  public CourseListModel(Node content, RD definition, RenderingModel<?> parent,
      CourseServices courseServices) {
    super(content, definition, parent);

    this.courseServices = courseServices;
  }

  public Category getCategoryByUrl() {
    return courseServices.getCategoryByUrl();
  }

  public Category getCategoryByName(String categoryName) {
    return courseServices.getCategoryByName(categoryName);
  }

  public List<Course> getCourseByCategory(String identifier) {
    return courseServices.getCoursesByCategory(definition.getCategory(), identifier);
  }

  protected CourseServices getCourseServices() {
    return courseServices;
  }
}

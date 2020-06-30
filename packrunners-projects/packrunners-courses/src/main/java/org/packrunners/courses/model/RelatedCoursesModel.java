package org.packrunners.courses.model;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import info.magnolia.rendering.model.RenderingModel;
import java.util.List;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import lombok.extern.slf4j.Slf4j;
import org.packrunners.courses.model.definition.CourseCategoryTemplateDefinition;
import org.packrunners.courses.service.Category;
import org.packrunners.courses.service.Course;
import org.packrunners.courses.service.CourseServices;


/**
 * Model for getting related Courses based on type- and destination-category.
 *
 * @param <RD> The {@link CourseCategoryTemplateDefinition} of the model.
 */
@Slf4j
public class RelatedCoursesModel<RD extends CourseCategoryTemplateDefinition> extends
    CourseListModel<RD> {

  @Inject
  public RelatedCoursesModel(Node content, RD definition, RenderingModel<?> parent,
      CourseServices courseServices) {
    super(content, definition, parent, courseServices);
  }

  public List<Category> getRelatedCategoriesByParameter() {
    return getCourseServices().getRelatedCourseTypesByParameter();
  }

  /**
   * Filters the current course from the category.
   */
  public List<Course> getRelatedCoursesByCategory(String identifier) {
    List<Course> relatedCourses = Lists.newArrayList();

    try {
      final String currentIdentifier = getCourseServices().getCourseNodeByParameter()
          .getIdentifier();
      List<Course> courses = getCourseServices()
          .getCoursesByCategory(definition.getCategory(), identifier, true);

      relatedCourses = Lists.newArrayList(Iterables.filter(courses, new Predicate<Course>() {
        @Override
        public boolean apply(Course course) {
          return !currentIdentifier.equals(course.getIdentifier());
        }
      }));
    } catch (RepositoryException e) {
      log.error("Could not retrieve identifier for the current course.", e);
    }

    return relatedCourses;
  }
}
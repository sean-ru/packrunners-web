package org.packrunners.courses.model;

import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.model.RenderingModelImpl;
import java.util.List;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import lombok.extern.slf4j.Slf4j;
import org.packrunners.courses.model.definition.CourseCategoryTemplateDefinition;
import org.packrunners.courses.service.Category;
import org.packrunners.courses.service.Course;
import org.packrunners.courses.service.CourseServices;
import org.packrunners.courses.service.StudyGuide;
import org.packrunners.courses.service.StudyGuideServices;
import org.packrunners.courses.service.VideoServices;


/**
 * Model for displaying a course and its related courses by type or category.
 */
@Slf4j
public class CourseDetailModel<RD extends CourseCategoryTemplateDefinition> extends
    RenderingModelImpl<RD> {

  private final CourseServices courseServices;
  private final StudyGuideServices studyGuideServices;
  private final VideoServices videoServices;

  @Inject
  public CourseDetailModel(Node content, RD definition, RenderingModel<?> parent,
      CourseServices courseServices, StudyGuideServices studyGuideServices,
      VideoServices videoServices
  ) {
    super(content, definition, parent);
    this.courseServices = courseServices;
    this.studyGuideServices = studyGuideServices;
    this.videoServices = videoServices;
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

  public List<StudyGuide> getStudyGuidesByCourseName(String courseName) {
    Category courseNameCategory = studyGuideServices.getCategoryByName(courseName);
    return studyGuideServices.getStudyGuidesByCategory(definition.getCategory(), courseNameCategory.getIdentifier());
  }
/*

  public List<Video> getVideos() {
    return courseServices.getCoursesByCategory(definition.getCategory(), identifier);
  }

  public List<Video> getQuizzes() {
    return courseServices.getCoursesByCategory(definition.getCategory(), identifier);
  }
*/

}

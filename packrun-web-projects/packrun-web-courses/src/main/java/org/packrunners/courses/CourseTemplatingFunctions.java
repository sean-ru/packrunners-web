package org.packrunners.courses;

import info.magnolia.jcr.util.ContentMap;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.jcr.Node;
import org.packrunners.courses.service.Category;
import org.packrunners.courses.service.Course;
import org.packrunners.courses.service.CourseServices;


/**
 * Useful functions for templating.
 */
@Singleton
public class CourseTemplatingFunctions {

  private final CourseServices courseServices;

  @Inject
  public CourseTemplatingFunctions(CourseServices courseServices) {
    this.courseServices = courseServices;
  }

  public Category getCategoryByUrl() {
    return courseServices.getCategoryByUrl();
  }

  /**
   * Returns the link to a course type.
   *
   * <p>Will use given {@link ContentMap} to find feature page of type
   * {@link CoursesModule#TEMPLATE_SUB_TYPE_COURSE_CATEGORY_OVERVIEW} to link to.</p>
   */
  public String getCourseTypeLink(ContentMap contentMap, String courseTypeName) {
    return getCourseTypeLink(contentMap.getJCRNode(), courseTypeName);
  }

  /**
   * Returns the link to a course type.
   *
   * <p>Will use given {@link Node} to find feature page of type
   * {@link CoursesModule#TEMPLATE_SUB_TYPE_COURSE_CATEGORY_OVERVIEW} to link to.</p>
   */
  public String getCourseTypeLink(Node content, String courseTypeName) {
    return courseServices
        .getCategoryLink(content, courseTypeName, CoursesModule.TEMPLATE_SUB_TYPE_COURSE_CATEGORY_OVERVIEW);
  }

  /**
   * Returns the link to a course type.
   *
   * <p>Will use given {@link ContentMap} to find feature page of type
   * {@link CoursesModule#TEMPLATE_SUB_TYPE_SCHOOL_OVERVIEW} to link to.</p>
   */
  public String getSchoolLink(ContentMap contentMap, String schoolName) {
    return getSchoolLink(contentMap.getJCRNode(), schoolName);
  }

  /**
   * Returns the link to a course type.
   *
   * <p>Will use given {@link Node} to find feature page of type
   * {@link CoursesModule#TEMPLATE_SUB_TYPE_SCHOOL_OVERVIEW} to link to.</p>
   */
  public String getSchoolLink(Node content, String schoolName) {
    return courseServices.getCategoryLink(content, schoolName,
        CoursesModule.TEMPLATE_SUB_TYPE_SCHOOL_OVERVIEW);
  }

  public String getCourseLink(ContentMap courseContentMap) {
    return getCourseLink(courseContentMap.getJCRNode());
  }

  public String getCourseLink(Node courseNode) {
    return courseServices.getCourseLink(courseNode);
  }

  /**
   * Allows marshalling of course node from templates. Can be useful when accessing {@link Category
   * categories}.
   */
  public Course marshallCourseNode(Node CourseNode) {
    return courseServices.marshallCourseNode(CourseNode);
  }

  public Course marshallCourseNode(ContentMap CourseContentMap) {
    return marshallCourseNode(CourseContentMap.getJCRNode());
  }
}

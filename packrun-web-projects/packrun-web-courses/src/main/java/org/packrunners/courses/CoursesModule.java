package org.packrunners.courses;

/**
 * This class is optional and represents the configuration for the courses module. By exposing
 * simple getter/setter/adder methods, this bean can be configured via content2bean using the
 * properties and node from <tt>config:/modules/courses</tt>. If you don't need this, simply remove
 * the reference to this class in the module descriptor xml.
 */
public class CoursesModule {

  public static final String COURSES_REPOSITORY_NAME = "courses";

  public final static String TEMPLATE_SUB_TYPE_SCHOOL_OVERVIEW = "schoolOverview";
  public final static String TEMPLATE_SUB_TYPE_COURSE_CATEGORY_OVERVIEW = "courseCategoryOverview";

  /**
   * Specifies the root node of schools.
   */
  private String schoolRootNode;

  /**
   * Specifies the root node of course categories.
   */
  private String courseTypeRootNode;

  /**
   * Specifies the default course node name (slug).
   */
  private String defaultCourseName;

  public String getSchoolRootNode() {
    return schoolRootNode;
  }

  public void setSchoolRootNode(String schoolRootNode) {
    this.schoolRootNode = schoolRootNode;
  }

  public String getCourseTypeRootNode() {
    return courseTypeRootNode;
  }

  public void setCourseTypeRootNode(String courseTypeRootNode) {
    this.courseTypeRootNode = courseTypeRootNode;
  }

  public String getDefaultCourseName() {
    return defaultCourseName;
  }

  public void setDefaultCourseName(String defaultCourseName) {
    this.defaultCourseName = defaultCourseName;
  }

}

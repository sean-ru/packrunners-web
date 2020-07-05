package org.packrunners.studyguides;

/**
 * This class is optional and represents the configuration for the courses module. By exposing
 * simple getter/setter/adder methods, this bean can be configured via content2bean using the
 * properties and node from <tt>config:/modules/courses</tt>. If you don't need this, simply remove
 * the reference to this class in the module descriptor xml.
 */
public class StudyGuidesModule {

  public static final String STUDY_GUIDES_REPOSITORY_NAME = "study-guides";

  /**
   * Specifies the root node of schools.
   */
  private String schoolRootNode;

  /**
   * Specifies the root node of courseType categories.
   */
  private String courseTypeRootNode;

  /**
   * Specifies the root node of resourceType categories.
   */
  private String resourceTypeRootNode;

  /**
   * Specifies the root node of courseName categories.
   */
  private String courseNumberRootNode;

  /**
   * Specifies the default course node name (slug).
   */
  private String defaultCourseName;

  /**
   * Specifies the default tutor node name.
   */
  private String defaultTutorName;

  /**
   * Specifies the default video node name (slug).
   */
  private String defaultVideoName;

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

  public String getResourceTypeRootNode() {
    return resourceTypeRootNode;
  }

  public void setResourceTypeRootNode(String resourceTypeRootNode) {
    this.resourceTypeRootNode = resourceTypeRootNode;
  }

  public String getDefaultCourseName() {
    return defaultCourseName;
  }

  public void setDefaultCourseName(String defaultCourseName) {
    this.defaultCourseName = defaultCourseName;
  }

  public String getDefaultTutorName() {
    return defaultTutorName;
  }

  public void setDefaultTutorName(String defaultTutorName) {
    this.defaultTutorName = defaultTutorName;
  }

  public String getDefaultVideoName() {
    return defaultVideoName;
  }

  public void setDefaultVideoName(String defaultVideoName) {
    this.defaultVideoName = defaultVideoName;
  }

  public String getCourseNumberRootNode() {
    return courseNumberRootNode;
  }

  public void setCourseNumberRootNode(String courseNumberRootNode) {
    this.courseNumberRootNode = courseNumberRootNode;
  }
}


package org.packrunners.courses.service;

import info.magnolia.dam.api.Asset;
import java.util.Date;
import java.util.List;


/**
 * Simple POJO for wrapping a video node.
 */
public class Video {

  public static final String PROPERTY_NAME_DISPLAY_NAME = "name";
  public static final String PROPERTY_NAME_AUTHOR = "author";
  public static final String PROPERTY_NAME_COURSE = "course";
  public static final String PROPERTY_NAME_LAST_MODIFIED_DATE = "date";
  public static final String PROPERTY_NAME_ATTACHMENTS = "video";
  public static final String PROPERTY_NAME_COURSE_TYPE = "courseType";
  public static final String PROPERTY_NAME_SCHOOL = "school";

  private String name;
  private String author;
  private String course;
  private Date lastModifiedDate;
  private Asset video;
  private List<Category> schools;
  private List<Category> courseTypes;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getCourse() {
    return course;
  }

  public void setCourse(String course) {
    this.course = course;
  }

  public Date getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(Date lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }

  public Asset getVideo() {
    return video;
  }

  public void setVideo(Asset video) {
    this.video = video;
  }

  public List<Category> getSchools() {
    return schools;
  }

  public void setSchools(List<Category> schools) {
    this.schools = schools;
  }

  public List<Category> getCourseTypes() {
    return courseTypes;
  }

  public void setCourseTypes(List<Category> courseTypes) {
    this.courseTypes = courseTypes;
  }
}
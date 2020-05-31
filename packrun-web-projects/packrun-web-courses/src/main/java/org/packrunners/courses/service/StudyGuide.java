package org.packrunners.courses.service;

import info.magnolia.dam.api.Asset;

import java.util.Date;
import java.util.List;


/**
 * Simple POJO for wrapping a course node.
 */
public class StudyGuide {

  public static final String PROPERTY_NAME_DISPLAY_NAME = "title";
  public static final String PROPERTY_NAME_CONTENT = "content";
  public static final String PROPERTY_NAME_AUTHOR = "author";
  public static final String PROPERTY_NAME_COURSE = "course";
  public static final String PROPERTY_NAME_LAST_MODIFIED_DATE = "date";
  public static final String PROPERTY_NAME_ATTACHMENTS = "attachments";
  public static final String PROPERTY_NAME_COURSE_TYPE = "courseType";
  public static final String PROPERTY_NAME_SCHOOL = "school";

  private String name;
  private String content;
  private String author;
  private Course course;
  private Date lastModifiedDate;
  private List<Asset> attachments;
  private List<Category> schools;
  private List<Category> courseTypes;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public Course getCourse() {
    return course;
  }

  public void setCourse(Course course) {
    this.course = course;
  }

  public Date getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(Date lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }

  public List<Asset> getAttachments() {
    return attachments;
  }

  public void setAttachments(List<Asset> attachments) {
    this.attachments = attachments;
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
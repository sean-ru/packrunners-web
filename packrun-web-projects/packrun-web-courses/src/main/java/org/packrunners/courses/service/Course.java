package org.packrunners.courses.service;

import info.magnolia.dam.api.Asset;
import java.util.List;


/**
 * Simple POJO for wrapping a course node.
 */
public class Course {

  public static final String PROPERTY_NAME_DISPLAY_NAME = "title";
  public static final String PROPERTY_NAME_DESCRIPTION = "description";
  public static final String PROPERTY_NAME_IMAGE = "image";
  public static final String PROPERTY_NAME_COURSE_NUMBER = "courseNumber";
  public static final String PROPERTY_NAME_WEIGHTED = "weighted";
  public static final String PROPERTY_NAME_PREREQUISITE = "prerequisite";
  public static final String PROPERTY_NAME_LEVEL = "level";
  public static final String PROPERTY_NAME_DURATION = "duration";
  public static final String PROPERTY_NAME_CREDIT = "credit";
  public static final String PROPERTY_NAME_TEACHERS = "teachers";
  public static final String PROPERTY_NAME_SYLLABUS = "syllabus";
  public static final String PROPERTY_NAME_COURSE_TYPE = "courseType";
  public static final String PROPERTY_NAME_SCHOOL = "school";

  private String name;
  private String description;
  private String courseNumber;
  private Boolean weighted;
  private String prerequisite;
  private String level;
  private String duration;
  private String credit;
  private String link;
  private String teachers;
  private Asset syllabus;
  private String identifier;
  private Asset image;
  private List<Category> schools;
  private List<Category> courseTypes;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getCourseNumber() {
    return courseNumber;
  }

  public void setCourseNumber(String courseNumber) {
    this.courseNumber = courseNumber;
  }

  public Boolean getWeighted() {
    return weighted;
  }

  public void setWeighted(Boolean weighted) {
    this.weighted = weighted;
  }

  public String getPrerequisite() {
    return prerequisite;
  }

  public void setPrerequisite(String prerequisite) {
    this.prerequisite = prerequisite;
  }

  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public String getDuration() {
    return duration;
  }

  public void setDuration(String duration) {
    this.duration = duration;
  }

  public String getCredit() {
    return credit;
  }

  public void setCredit(String credit) {
    this.credit = credit;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getTeachers() {
    return teachers;
  }

  public void setTeachers(String teachers) {
    this.teachers = teachers;
  }

  public Asset getSyllabus() {
    return syllabus;
  }

  public void setSyllabus(Asset syllabus) {
    this.syllabus = syllabus;
  }

  public Asset getImage() {
    return image;
  }

  public void setImage(Asset image) {
    this.image = image;
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

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

}
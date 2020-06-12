package org.packrunners.courses.service;

import info.magnolia.dam.api.Asset;
import java.util.Date;
import java.util.List;


/**
 * Simple POJO for wrapping a video node.
 */
public class Video {

  public static final String PROPERTY_NAME_DISPLAY_NAME = "name";
  public static final String PROPERTY_NAME_DESCRIPTION = "description";
  public static final String PROPERTY_NAME_AUTHOR = "author";
  public static final String PROPERTY_NAME_COURSES = "courses";
  public static final String PROPERTY_NAME_LAST_MODIFIED_DATE = "date";
  public static final String PROPERTY_NAME_VIDEO = "video";

  private String name;
  private String description;
  private String author;
  private Date lastModifiedDate;
  private Asset video;
  private String link;
  private String identifier;
  private List<Category> courses;

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

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<Category> getCourses() {
    return courses;
  }

  public void setCourses(List<Category> courses) {
    this.courses = courses;
  }
}
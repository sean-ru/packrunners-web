package org.packrunners.courses.service;

import info.magnolia.dam.api.Asset;


/**
 * Simple POJO for wrapping a video node.
 */
public class Tutor {

  public static final String PROPERTY_NAME_ID = "id";
  public static final String PROPERTY_NAME_NAME = "name";
  public static final String PROPERTY_NAME_PHOTO = "photo";
  public static final String PROPERTY_NAME_PROFILE = "profile";
  public static final String PROPERTY_NAME_EMAIL = "email";
  public static final String PROPERTY_NAME_GRADE = "grade";

  private String id;
  private String name;
  private Asset photo;
  private String profile;
  private String email;
  private String grade;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Asset getPhoto() {
    return photo;
  }

  public void setPhoto(Asset photo) {
    this.photo = photo;
  }

  public String getProfile() {
    return profile;
  }

  public void setProfile(String profile) {
    this.profile = profile;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getGrade() {
    return grade;
  }

  public void setGrade(String grade) {
    this.grade = grade;
  }
}
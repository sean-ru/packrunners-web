package org.packrunners.tutors.service;

import info.magnolia.dam.api.Asset;
import org.packrunners.categories.Category;

import java.util.List;


/**
 * Simple POJO for wrapping a tutor node.
 */
public class Tutor {

    public static final String PROPERTY_NAME_ID = "id";
    public static final String PROPERTY_NAME_DISPLAY_NAME = "name";
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
    private String link;
    private String identifier;
    private List<Category> schools;

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

    public List<Category> getSchools() {
        return schools;
    }

    public void setSchools(List<Category> schools) {
        this.schools = schools;
    }
}
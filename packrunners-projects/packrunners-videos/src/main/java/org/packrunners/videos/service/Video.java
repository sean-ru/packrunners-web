package org.packrunners.videos.service;

import org.packrunners.categories.Category;

import java.util.Date;
import java.util.List;


/**
 * Simple POJO for wrapping a video node.
 */
public class Video {

    public static final String PROPERTY_NAME_DISPLAY_NAME = "name";
    public static final String PROPERTY_NAME_DESCRIPTION = "description";
    public static final String PROPERTY_NAME_AUTHOR = "author";
    public static final String PROPERTY_NAME_LAST_MODIFIED_DATE = "lastModifiedDate";
    public static final String PROPERTY_NAME_VIDEO_URL = "videoUrl";
    public static final String PROPERTY_NAME_COURSE_TYPES = "courseTypes";
    public static final String PROPERTY_NAME_SCHOOLS = "schools";
    public static final String PROPERTY_NAME_COURSE_NUMBERS = "courseNumbers";

    private String name;
    private String description;
    private String author;
    private List<Category> courseNumbers;
    private List<Category> courseTypes;
    private List<Category> schools;
    private Date lastModifiedDate;
    private String videoUrl;
    private String link;
    private String identifier;

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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public List<Category> getSchools() {
        return schools;
    }

    public void setSchools(List<Category> schools) {
        this.schools = schools;
    }

    public List<Category> getCourseNumbers() {
        return courseNumbers;
    }

    public void setCourseNumbers(List<Category> courseNumbers) {
        this.courseNumbers = courseNumbers;
    }

    public List<Category> getCourseTypes() {
        return courseTypes;
    }

    public void setCourseTypes(List<Category> courseTypes) {
        this.courseTypes = courseTypes;
    }
}
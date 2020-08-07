package org.packrunners.studyguides.service;

import org.packrunners.categories.Category;

import java.util.Date;
import java.util.List;


/**
 * Simple POJO for wrapping a studyGuide node.
 */
public class StudyGuide {

    public static final String PROPERTY_NAME_DISPLAY_NAME = "name";
    public static final String PROPERTY_NAME_DESCRIPTION = "description";
    public static final String PROPERTY_NAME_AUTHOR = "author";
    public static final String PROPERTY_NAME_TAGS = "tags";
    public static final String PROPERTY_NAME_LAST_MODIFIED_DATE = "lastModifiedDate";
    public static final String PROPERTY_NAME_DOC_URL = "docUrl";
    public static final String PROPERTY_NAME_SCHOOLS = "schools";
    public static final String PROPERTY_NAME_COURSE_TYPES = "courseTypes";
    public static final String PROPERTY_NAME_COURSE_NUMBERS = "courseNumbers";

    private String name;
    private String description;
    private String author;
    private String tags;
    private String docUrl;
    private List<Category> courseNumbers;
    private List<Category> schools;
    private List<Category> courseTypes;
    private Date lastModifiedDate;
    private String identifier;
    private String link;

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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDocUrl() {
        return docUrl;
    }

    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl;
    }

    public List<Category> getCourseNumbers() {
        return courseNumbers;
    }

    public void setCourseNumbers(List<Category> courseNumbers) {
        this.courseNumbers = courseNumbers;
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

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
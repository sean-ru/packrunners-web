package org.packrunners.studyguides.service;

import info.magnolia.dam.api.Asset;
import org.packrunners.categories.Category;

import java.util.Date;
import java.util.List;


/**
 * Simple POJO for wrapping a studyGuide node.
 */
public class StudyGuide {

    public static final String PROPERTY_NAME_DISPLAY_NAME = "title";
    public static final String PROPERTY_NAME_CONTENT = "content";
    public static final String PROPERTY_NAME_AUTHOR = "author";
    public static final String PROPERTY_NAME_COURSE_NAMES = "courseNames";
    public static final String PROPERTY_NAME_LAST_MODIFIED_DATE = "lastModifiedDate";
    public static final String PROPERTY_NAME_COURSE_NUMBERS = "courseNumbers";
    public static final String PROPERTY_NAME_ATTACHMENTS = "attachments";
    public static final String PROPERTY_NAME_COURSE_TYPES = "courseTypes";
    public static final String PROPERTY_NAME_SCHOOLS = "schools";

    private String name;
    private String content;
    private String author;
    private List<Category> courseNumbers;
    private Date lastModifiedDate;
    private String identifier;
    private String link;
    private List<Asset> attachments;
    private List<Category> schools;
    private List<Category> courseTypes;
    private List<Category> courseNames;

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

    public List<Category> getCourseNames() {
        return courseNames;
    }

    public void setCourseNames(List<Category> courseNames) {
        this.courseNames = courseNames;
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

    public List<Category> getCourseNumbers() {
        return courseNumbers;
    }

    public void setCourseNumbers(List<Category> courseNumbers) {
        this.courseNumbers = courseNumbers;
    }
}
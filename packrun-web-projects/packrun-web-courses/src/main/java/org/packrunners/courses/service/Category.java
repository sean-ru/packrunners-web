package org.packrunners.courses.service;

import info.magnolia.dam.api.Asset;


/**
 * Simple POJO for wrapping categories.
 */
public class Category {

  public static final String PROPERTY_NAME_DISPLAY_NAME = "displayName";
  public static final String PROPERTY_NAME_DESCRIPTION = "description";
  public static final String PROPERTY_NAME_IMAGE = "image";
  public static final String PROPERTY_NAME_ICON = "icon";
  public static final String PROPERTY_NAME_BODY = "body";

  private String name;
  private String identifier;
  private String link;
  private Asset image;
  private Asset icon;
  private String description;
  private String body;
  private String nodeName;

  public Category() {
  }

  public Category(String name, String identifier) {
    this.name = name;
    this.identifier = identifier;
  }

  public String getName() {
    return name;
  }

  public String getIdentifier() {
    return identifier;
  }

  public String getLink() {
    return link;
  }

  public Asset getImage() {
    return image;
  }

  public Asset getIcon() {
    return icon;
  }

  public String getDescription() {
    return description;
  }

  public String getBody() {
    return body;
  }

  public String getNodeName() {
    return nodeName;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public void setImage(Asset image) {
    this.image = image;
  }

  public void setIcon(Asset icon) {
    this.icon = icon;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public void setNodeName(String nodeName) {
    this.nodeName = nodeName;
  }
}
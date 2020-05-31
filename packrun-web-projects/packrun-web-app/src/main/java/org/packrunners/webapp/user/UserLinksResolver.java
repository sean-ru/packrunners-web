package org.packrunners.webapp.user;

import javax.jcr.RepositoryException;


/**
 * Resolver of user (profile, registration pages) links.
 */
public interface UserLinksResolver {

  boolean useForCurrentPage() throws RepositoryException;

  String getUsername();

  String getProfilePageLink() throws RepositoryException;

  String getRegistrationPageLink() throws RepositoryException;

  String getLoginPageLink() throws RepositoryException;

  String getLogoutLink() throws RepositoryException;
}

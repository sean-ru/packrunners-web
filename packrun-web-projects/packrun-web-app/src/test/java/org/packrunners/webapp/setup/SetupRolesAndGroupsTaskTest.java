package org.packrunners.webapp.setup;

import static info.magnolia.test.hamcrest.NodeMatchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.packrunners.webapp.setup.SetupRolesAndGroupsTask.DAM_ACTIVATE_ACCESS_ROLES;
import static org.packrunners.webapp.setup.SetupRolesAndGroupsTask.DAM_PERMISSIONS_ROLES;
import static org.packrunners.webapp.setup.SetupRolesAndGroupsTask.EDITOR_ROLE;
import static org.packrunners.webapp.setup.SetupRolesAndGroupsTask.ENTERPRISE_MODULE;
import static org.packrunners.webapp.setup.SetupRolesAndGroupsTask.PAGES_ACTIVATE_ACCESS_ROLES;
import static org.packrunners.webapp.setup.SetupRolesAndGroupsTask.PAGES_PERMISSIONS_ROLES;
import static org.packrunners.webapp.setup.SetupRolesAndGroupsTask.PUBLISHERS_GROUP;
import static org.packrunners.webapp.setup.SetupRolesAndGroupsTask.PUBLISHER_ROLE;
import static org.packrunners.webapp.setup.SetupRolesAndGroupsTask.WORKFLOW_JBPM_MODULE;
import static org.packrunners.webapp.setup.SetupRolesAndGroupsTask.WORKFLOW_JBPM_PUBLISH_GROUPS;

import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.module.InstallContext;
import info.magnolia.repository.RepositoryConstants;
import info.magnolia.test.mock.jcr.MockSession;
import javax.jcr.Session;
import org.junit.Before;
import org.junit.Test;


public class SetupRolesAndGroupsTaskTest {

  private Session session;
  private InstallContext ctx;

  @Before
  public void setUp() throws Exception {
    session = new MockSession(RepositoryConstants.CONFIG);
    ctx = mock(InstallContext.class);
    when(ctx.getJCRSession(eq(RepositoryConstants.CONFIG))).thenReturn(session);
  }

  @Test
  public void publisherCanActivatePagesOnCEInstance() throws Exception {
    // GIVEN
    NodeUtil
        .createPath(session.getRootNode(), PAGES_ACTIVATE_ACCESS_ROLES, NodeTypes.ContentNode.NAME);

    // WHEN
    new SetupRolesAndGroupsTask().execute(ctx);

    // THEN
    assertThat(session.getNode(PAGES_ACTIVATE_ACCESS_ROLES),
        not(hasProperty(EDITOR_ROLE, EDITOR_ROLE)));
    assertThat(session.getNode(PAGES_ACTIVATE_ACCESS_ROLES),
        hasProperty(PUBLISHER_ROLE, PUBLISHER_ROLE));
  }

  @Test
  public void editorCanActivatePagesOnEEInstance() throws Exception {
    // GIVEN
    NodeUtil
        .createPath(session.getRootNode(), PAGES_ACTIVATE_ACCESS_ROLES, NodeTypes.ContentNode.NAME);
    when(ctx.isModuleRegistered(eq(ENTERPRISE_MODULE))).thenReturn(true);

    // WHEN
    new SetupRolesAndGroupsTask().execute(ctx);

    // THEN
    assertThat(session.getNode(PAGES_ACTIVATE_ACCESS_ROLES),
        hasProperty(EDITOR_ROLE, EDITOR_ROLE));
    assertThat(session.getNode(PAGES_ACTIVATE_ACCESS_ROLES),
        hasProperty(PUBLISHER_ROLE, PUBLISHER_ROLE));
  }

  @Test
  public void rolesCanActivateAssets() throws Exception {
    // GIVEN
    NodeUtil
        .createPath(session.getRootNode(), DAM_ACTIVATE_ACCESS_ROLES, NodeTypes.ContentNode.NAME);

    // WHEN
    new SetupRolesAndGroupsTask().execute(ctx);

    // THEN
    assertThat(session.getNode(DAM_ACTIVATE_ACCESS_ROLES),
        hasProperty(EDITOR_ROLE, EDITOR_ROLE));
    assertThat(session.getNode(DAM_ACTIVATE_ACCESS_ROLES),
        hasProperty(PUBLISHER_ROLE, PUBLISHER_ROLE));
  }

  @Test
  public void rolesCanAccessDamApp() throws Exception {
    // GIVEN
    NodeUtil.createPath(session.getRootNode(), DAM_PERMISSIONS_ROLES, NodeTypes.ContentNode.NAME);

    // WHEN
    new SetupRolesAndGroupsTask().execute(ctx);

    // THEN
    assertThat(session.getNode(DAM_PERMISSIONS_ROLES),
        hasProperty(EDITOR_ROLE, EDITOR_ROLE));
    assertThat(session.getNode(DAM_PERMISSIONS_ROLES),
        hasProperty(PUBLISHER_ROLE, PUBLISHER_ROLE));
  }

  @Test
  public void rolesCanAccessPagesApp() throws Exception {
    // GIVEN
    NodeUtil.createPath(session.getRootNode(), PAGES_PERMISSIONS_ROLES, NodeTypes.ContentNode.NAME);

    // WHEN
    new SetupRolesAndGroupsTask().execute(ctx);

    // THEN
    assertThat(session.getNode(PAGES_PERMISSIONS_ROLES),
        hasProperty(EDITOR_ROLE, EDITOR_ROLE));
    assertThat(session.getNode(PAGES_PERMISSIONS_ROLES),
        hasProperty(PUBLISHER_ROLE, PUBLISHER_ROLE));
  }

  @Test
  public void installTaskCreatesPathToAccessRolesIfNotExisting() throws Exception {
    // GIVEN

    // WHEN
    new SetupRolesAndGroupsTask().execute(ctx);

    // THEN
    assertThat(session.nodeExists(PAGES_ACTIVATE_ACCESS_ROLES), is(true));
    assertThat(session.nodeExists(DAM_ACTIVATE_ACCESS_ROLES), is(true));
  }

  @Test
  public void publisherCanAccessWorkflowItems() throws Exception {
    // GIVEN
    NodeUtil.createPath(session.getRootNode(), WORKFLOW_JBPM_PUBLISH_GROUPS,
        NodeTypes.ContentNode.NAME);
    when(ctx.isModuleRegistered(eq(WORKFLOW_JBPM_MODULE))).thenReturn(true);

    // WHEN
    new SetupRolesAndGroupsTask().execute(ctx);

    // THEN
    assertThat(session.getNode(WORKFLOW_JBPM_PUBLISH_GROUPS),
        hasProperty(PUBLISHERS_GROUP, PUBLISHERS_GROUP));
  }

  @Test
  public void doNotAddPublishersGroupToWorkflowIfWorkflowJbpmIsNotInstalled() throws Exception {
    // GIVEN
    when(ctx.isModuleRegistered(eq(WORKFLOW_JBPM_MODULE))).thenReturn(false);

    // WHEN
    new SetupRolesAndGroupsTask().execute(ctx);

    // THEN
    assertThat(
        session.propertyExists(WORKFLOW_JBPM_PUBLISH_GROUPS + "/" + PUBLISHERS_GROUP),
        is(false));
  }
}
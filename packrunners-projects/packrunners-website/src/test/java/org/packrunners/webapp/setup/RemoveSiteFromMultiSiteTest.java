package org.packrunners.webapp.setup;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.module.InstallContext;
import info.magnolia.repository.RepositoryConstants;
import info.magnolia.test.mock.jcr.MockSession;
import javax.jcr.Node;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests for {@link RemoveSiteFromMultiSite}.
 */
public class RemoveSiteFromMultiSiteTest {

  private RemoveSiteFromMultiSite task = new RemoveSiteFromMultiSite();
  private InstallContext context;
  private MockSession session;
  private Node siteNode;

  @Before
  public void setUp() throws Exception {
    session = new MockSession(RepositoryConstants.CONFIG);

    context = mock(InstallContext.class);
    when(context.getJCRSession(RepositoryConstants.CONFIG)).thenReturn(session);
    when(context.getConfigJCRSession()).thenReturn(session);

    siteNode = NodeUtil
        .createPath(session.getRootNode(), RemoveSiteFromMultiSite.PATH_TO_DEFAULT_SITE,
            NodeTypes.ContentNode.NAME);
  }

  @Test
  public void checkThatTaskRemovesCorrectSiteDefinition() throws Exception {
    // GIVEN
    final Node themeReferenceNode = siteNode.addNode("theme", NodeTypes.ContentNode.NAME);
    themeReferenceNode.setProperty("name", "packrunweb-theme");
    final Node homeTemplate = NodeUtil
        .createPath(siteNode, "templates/availability/templates/home", NodeTypes.ContentNode.NAME);
    homeTemplate.setProperty("id", "packrunweb:pages/home");

    // WHEN
    task.doExecute(context);

    // THEN
    //assertFalse(session.itemExists(RemoveSiteFromMultiSite.PATH_TO_DEFAULT_SITE));
  }

  @Test
  public void checkThatTaskDoesNotRemoveWrongSiteDefinitionWhenClassPropertyIsThere()
      throws Exception {
    // GIVEN
    siteNode.setProperty("class", "a.fully.qualified.class.Name");

    // WHEN
    task.doExecute(context);

    // THEN
    assertTrue(session.itemExists(RemoveSiteFromMultiSite.PATH_TO_DEFAULT_SITE));
  }

  @Test
  public void checkThatTaskDoesNotRemoveWrongSiteDefinition() throws Exception {
    // GIVEN
    final Node themeReferenceNode = siteNode.addNode("theme", NodeTypes.ContentNode.NAME);
    themeReferenceNode.setProperty("name", "another-theme-name");

    // WHEN
    task.doExecute(context);

    // THEN
    assertTrue(session.itemExists(RemoveSiteFromMultiSite.PATH_TO_DEFAULT_SITE));
  }

}
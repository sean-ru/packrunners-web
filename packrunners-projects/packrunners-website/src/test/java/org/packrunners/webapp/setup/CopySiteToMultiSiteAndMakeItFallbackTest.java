package org.packrunners.webapp.setup;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.packrunners.webapp.setup.CopySiteToMultiSiteAndMakeItFallback.MULTISITE_FALLBACK_SITE;
import static org.packrunners.webapp.setup.CopySiteToMultiSiteAndMakeItFallback.MULTISITE_PACKRUNNERS_SITE;
import static org.packrunners.webapp.setup.CopySiteToMultiSiteAndMakeItFallback.PACKRUNNERS_SITE;

import info.magnolia.context.MgnlContext;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.module.InstallContext;
import info.magnolia.repository.RepositoryConstants;
import info.magnolia.test.RepositoryTestCase;
import javax.jcr.Node;
import javax.jcr.Session;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests for {@link CopySiteToMultiSiteAndMakeItFallback}. Unfortunately we have to use the "heavy"
 * {@link RepositoryTestCase} because the underlying task uses {@link NodeUtil#copyInSession(Node,
 * String)} which does not work with mocked {@link Node}s.
 */
public class CopySiteToMultiSiteAndMakeItFallbackTest extends RepositoryTestCase {

  private CopySiteToMultiSiteAndMakeItFallback copySiteToMultiSiteAndMakeItFallback;
  private InstallContext installContext;
  private Session configSession;

  @Before
  public void setUp() throws Exception {
    super.setUp();

    configSession = MgnlContext.getJCRSession(RepositoryConstants.CONFIG);

    installContext = mock(InstallContext.class);
    when(installContext.getConfigJCRSession()).thenReturn(configSession);
    when(installContext.getJCRSession(RepositoryConstants.CONFIG)).thenReturn(configSession);

    copySiteToMultiSiteAndMakeItFallback = new CopySiteToMultiSiteAndMakeItFallback();

    // We require the multisite config to exist
    NodeUtil.createPath(configSession.getRootNode(), "/modules/multisite/config/sites",
        NodeTypes.ContentNode.NAME);
    NodeUtil.createPath(configSession.getRootNode(), MULTISITE_FALLBACK_SITE,
        NodeTypes.ContentNode.NAME);

    // Travel demo site
    NodeUtil.createPath(configSession.getRootNode(), PACKRUNNERS_SITE, NodeTypes.ContentNode.NAME);
  }

  @Test
  public void checkThatSiteIsCopied() throws Exception {
    // GIVEN
    final Node travelSiteNode = configSession.getNode(PACKRUNNERS_SITE);
    travelSiteNode.setProperty("test", "test-value");

    // WHEN
    copySiteToMultiSiteAndMakeItFallback.execute(installContext);

    // THEN
    assertTrue(configSession.itemExists(MULTISITE_PACKRUNNERS_SITE));
    assertTrue(configSession.propertyExists(MULTISITE_PACKRUNNERS_SITE + "/test"));
  }

  @Test
  public void checkThatFallbackIsSetWhenPropertyDoesNotExist() throws Exception {
    // GIVEN

    // WHEN
    copySiteToMultiSiteAndMakeItFallback.execute(installContext);

    // THEN
    assertTrue(configSession.propertyExists(MULTISITE_FALLBACK_SITE + "/extends"));
    assertThat(configSession.getProperty(MULTISITE_FALLBACK_SITE + "/extends").getString(),
        is("../packrunners"));
  }

  @Test
  public void checkThatFallbackIsSetWhenPropertyDoesExist() throws Exception {
    // GIVEN
    final Node fallBackNode = configSession.getNode(MULTISITE_FALLBACK_SITE);
    fallBackNode.setProperty("extends", "../default");

    // WHEN
    copySiteToMultiSiteAndMakeItFallback.execute(installContext);

    // THEN
    assertTrue(configSession.propertyExists(MULTISITE_FALLBACK_SITE + "/extends"));
    assertThat(configSession.getProperty(MULTISITE_FALLBACK_SITE + "/extends").getString(),
        is("../packrunners"));
  }
}
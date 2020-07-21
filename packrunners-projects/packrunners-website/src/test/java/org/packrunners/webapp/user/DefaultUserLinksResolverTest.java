package org.packrunners.webapp.user;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import info.magnolia.cms.i18n.I18nContentSupport;
import info.magnolia.cms.security.LogoutFilter;
import info.magnolia.context.MgnlContext;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.objectfactory.guice.GuiceUtils;
import info.magnolia.rendering.template.configured.ConfiguredTemplateDefinition;
import info.magnolia.rendering.template.registry.TemplateDefinitionRegistry;
import info.magnolia.rendering.template.type.DefaultTemplateTypes;
import info.magnolia.rendering.template.type.TemplateTypeHelper;
import info.magnolia.repository.RepositoryConstants;
import info.magnolia.templating.functions.TemplatingFunctions;
import info.magnolia.test.ComponentsTestUtil;
import info.magnolia.test.RepositoryTestCase;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;


/**
 * Tests for {@link DefaultUserLinksResolver}.
 */
public class DefaultUserLinksResolverTest extends RepositoryTestCase {

  private DefaultUserLinksResolver resolver;
  private Node siteRoot, loginPage, profilePage;

  @Before
  public void setUp() throws Exception {
    super.setUp();
    TemplateDefinitionRegistry registry = mock(TemplateDefinitionRegistry.class);

    ConfiguredTemplateDefinition rootPageDefinition = new ConfiguredTemplateDefinition(null);
    rootPageDefinition.setType(DefaultTemplateTypes.SITE_ROOT);
    when(registry.getTemplateDefinition(rootPageDefinition.getId())).thenReturn(rootPageDefinition);

    ConfiguredTemplateDefinition loginPageDefinition = new ConfiguredTemplateDefinition(null);
    loginPageDefinition.setId("loginPageTemplate");
    when(registry.getTemplateDefinition(loginPageDefinition.getId()))
        .thenReturn(loginPageDefinition);

    ConfiguredTemplateDefinition profilePageDefinition = new ConfiguredTemplateDefinition(null);
    profilePageDefinition.setId("profilePageDefinition");
    when(registry.getTemplateDefinition(profilePageDefinition.getId()))
        .thenReturn(profilePageDefinition);

    resolver = new DefaultUserLinksResolver(
        GuiceUtils.providerForInstance(MgnlContext.getWebContext()),
        GuiceUtils.providerForInstance(MgnlContext.getAggregationState()),
        new TemplatingFunctions(null, new TemplateTypeHelper(registry), null)
    );
    resolver.setLoginPageTemplateId(profilePageDefinition.getId());
    resolver.setRegistrationPageTemplateId("nonExistingTemplate");

    Session session = MgnlContext.getJCRSession(RepositoryConstants.WEBSITE);
    siteRoot = session.getRootNode().addNode("siteRoot", NodeTypes.Page.NAME);
    NodeTypes.Renderable.set(siteRoot, DefaultTemplateTypes.SITE_ROOT);

    loginPage = siteRoot.addNode("loginPage", NodeTypes.Page.NAME);
    NodeTypes.Renderable.set(loginPage, resolver.getLoginPageTemplateId());

    profilePage = siteRoot.addNode("profilePage", NodeTypes.Page.NAME);
    NodeTypes.Renderable.set(profilePage, resolver.getLoginPageTemplateId());

    session.save();

    I18nContentSupport i18nContentSupport = mock(I18nContentSupport.class);
    when(i18nContentSupport.toI18NURI(anyString())).thenAnswer(new Answer<String>() {
      @Override
      public String answer(InvocationOnMock invocation) throws Throwable {
        Object[] args = invocation.getArguments();
        return (String) args[0];
      }
    });
    ComponentsTestUtil.setInstance(I18nContentSupport.class, i18nContentSupport);
  }


  @Test
  public void useForCurrentPage() throws RepositoryException {
    // GIVEN
    MgnlContext.getAggregationState().setMainContentNode(loginPage);

    // WHEN
    boolean useForCurrentPage = resolver.useForCurrentPage();

    // THEN
    assertTrue(useForCurrentPage);
  }

  @Test
  public void getLoginPageLink() throws RepositoryException {
    // GIVEN
    MgnlContext.getAggregationState().setMainContentNode(siteRoot);

    // WHEN
    String link = resolver.getLoginPageLink();

    // THEN
    assertThat(link, equalTo(loginPage.getPath()));
  }

  @Test
  public void getLogoutLink() throws RepositoryException {
    // GIVEN
    MgnlContext.getAggregationState().setMainContentNode(loginPage);

    // WHEN
    String link = resolver.getLogoutLink();

    // THEN
    assertThat(link, equalTo(loginPage.getPath() + "?" + LogoutFilter.PARAMETER_LOGOUT + "=true"));
  }

  @Test
  public void getProfilePageLink() throws RepositoryException {
    // GIVEN
    MgnlContext.getAggregationState().setMainContentNode(siteRoot);

    // WHEN
    String link = resolver.getProfilePageLink();

    // THEN
    //assertEquals(link, loginPage.getPath());
  }

  @Test
  public void getRegistrationPageLinkWhenRegistrationPageDoesntExist() throws RepositoryException {
    // GIVEN
    MgnlContext.getAggregationState().setMainContentNode(loginPage);

    // WHEN
    String link = resolver.getRegistrationPageLink();

    // THEN
    assertNull(link);
  }

  @Test
  public void getUsernameForAnonymousUser() throws RepositoryException {
    // GIVEN

    // WHEN
    String username = resolver.getUsername();

    // THEN
    assertNull(username);
  }
}
package org.packrunners.webapp.model;

import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.model.RenderingModelImpl;
import info.magnolia.rendering.template.AreaDefinition;
import info.magnolia.rendering.template.type.DefaultTemplateTypes;
import info.magnolia.templating.functions.TemplatingFunctions;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.LocaleUtils;
import org.packrunners.webapp.definition.NavigationAreaDefinition;
import org.packrunners.webapp.user.UserLinksResolver;


@Slf4j
public class NavigationAreaModel extends RenderingModelImpl<AreaDefinition> {

  private static final String ABOUT_TEMPLATE_SUBTYPE = "packrunweb-about";
  private final TemplatingFunctions templatingFunctions;
  private UserLinksResolver userLinksResolver;

  @Inject
  public NavigationAreaModel(Node content, AreaDefinition definition, RenderingModel<?> parent,
      TemplatingFunctions templatingFunctions) {
    super(content, definition, parent);

    this.templatingFunctions = templatingFunctions;
  }

  public String getAboutDemoLink() {
    Node siteRoot = templatingFunctions.siteRoot(content);
    String link = null;
    try {
      List<Node> nodes = templatingFunctions
          .contentListByTemplateType(siteRoot, DefaultTemplateTypes.FEATURE,
              ABOUT_TEMPLATE_SUBTYPE);
      if (nodes.size() > 0) {
        link = templatingFunctions.link(nodes.get(0));
      }
    } catch (RepositoryException e) {
      log.error("Could not get the '{}' page.", ABOUT_TEMPLATE_SUBTYPE, e);
    }
    return link;
  }

  public String getUsername() throws RepositoryException {
    if (this.getUserLinksResolver() != null) {
      return this.getUserLinksResolver().getUsername();
    }
    return null;
  }

  public String getLogoutLink() throws RepositoryException {
    if (this.getUserLinksResolver() != null) {
      return this.getUserLinksResolver().getLogoutLink();
    }
    return null;
  }

  public String getLoginPageLink() throws RepositoryException {
    if (this.getUserLinksResolver() != null) {
      return this.getUserLinksResolver().getLoginPageLink();
    }
    return null;
  }

  public String getRegistrationPageLink() throws RepositoryException {
    if (this.getUserLinksResolver() != null) {
      return this.getUserLinksResolver().getRegistrationPageLink();
    }
    return null;
  }

  public String getProfilePageLink() throws RepositoryException {
    if (this.getUserLinksResolver() != null) {
      return this.getUserLinksResolver().getProfilePageLink();
    }
    return null;
  }

  private UserLinksResolver getUserLinksResolver() throws RepositoryException {
    if (userLinksResolver == null) {
      if (this.getDefinition() instanceof NavigationAreaDefinition) {
        for (UserLinksResolver userLinksResolver : ((NavigationAreaDefinition) this.getDefinition())
            .getUserLinksResolvers()) {
          if (userLinksResolver.useForCurrentPage()) {
            this.userLinksResolver = userLinksResolver;
            break;
          }
        }
      }
    }
    return userLinksResolver;
  }

  public Locale getLocale(String language) {
    return LocaleUtils.toLocale(language);
  }
}

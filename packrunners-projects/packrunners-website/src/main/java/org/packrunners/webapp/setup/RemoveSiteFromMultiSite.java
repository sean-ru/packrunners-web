package org.packrunners.webapp.setup;

import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.RemoveNodeTask;
import info.magnolia.module.delta.TaskExecutionException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;


public class RemoveSiteFromMultiSite extends RemoveNodeTask {

  protected static final String PATH_TO_DEFAULT_SITE = "/modules/multisite/config/sites/default";

  public RemoveSiteFromMultiSite() {
    super("Remove packrunners site definition from multi site module", PATH_TO_DEFAULT_SITE);
  }

  @Override
  protected void doExecute(InstallContext ctx) throws RepositoryException, TaskExecutionException {
    final Session session = ctx.getConfigJCRSession();
    if (session.nodeExists(PATH_TO_DEFAULT_SITE)) {
      final Node siteNode = session.getNode(PATH_TO_DEFAULT_SITE);
      if (!siteNode.hasProperty("class") &&
          "packrunweb-theme".equals(PropertyUtil.getString(siteNode, "theme/name")) &&
          "packrunweb:pages/home".equals(
              PropertyUtil.getString(siteNode, "templates/availability/templates/home/id"))) {
        super.doExecute(ctx);
      }
    }
  }

}

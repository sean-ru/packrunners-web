package org.packrunners.webapp.setup;

import info.magnolia.module.delta.ArrayDelegateTask;
import info.magnolia.module.delta.CheckAndModifyPropertyValueTask;
import info.magnolia.module.delta.CopyNodeTask;
import info.magnolia.module.delta.HasPropertyDelegateTask;
import info.magnolia.module.delta.SetPropertyTask;
import info.magnolia.repository.RepositoryConstants;


/**
 * Task that moves site definition to multi site module and tries to make it the fallback site.
 */
public class CopySiteToMultiSiteAndMakeItFallback extends ArrayDelegateTask {

  protected static final String PACKRUNNERS_SITE = "/modules/packrunweb/config/packrunners";
  protected static final String MULTISITE_PACKRUNNERS_SITE = "/modules/multisite/config/sites/packrunners";
  protected static final String MULTISITE_FALLBACK_SITE = "/modules/multisite/config/sites/fallback";

  public CopySiteToMultiSiteAndMakeItFallback() {
    this(false);
  }

  public CopySiteToMultiSiteAndMakeItFallback(final boolean override) {
    super("Copy site definition to multisite",
        "Copies site definition to multisite and makes it fallback site",
        new CopyNodeTask("Copy site definition to multisite", PACKRUNNERS_SITE,
            MULTISITE_PACKRUNNERS_SITE, override),
        new HasPropertyDelegateTask("Set packrunners site as fallback site if possible",
            MULTISITE_FALLBACK_SITE, "extends",
            new CheckAndModifyPropertyValueTask(MULTISITE_FALLBACK_SITE, "extends", "../default",
                "../packrunners"),
            new SetPropertyTask(RepositoryConstants.CONFIG, MULTISITE_FALLBACK_SITE, "extends",
                "../packrunners")));
  }

}

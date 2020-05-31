package org.packrunners.webapp.setup;

import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.BootstrapResourcesTask;
import javax.jcr.ImportUUIDBehavior;


/**
 * Task that re-bootstraps all files from specified folder, replacing any existing item.
 *
 * @see javax.jcr.ImportUUIDBehavior#IMPORT_UUID_COLLISION_REPLACE_EXISTING
 */
public class FolderBootstrapTask extends BootstrapResourcesTask {

  private final String folderName;

  public FolderBootstrapTask(final String folderName) {
    super("Bootstraps all files from specified folder",
        String.format("Bootstraps all files from folder '%s' (replacing existing elements).",
            folderName),
        ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING);

    this.folderName = folderName;
  }

  @Override
  protected boolean acceptResource(InstallContext ctx, String resourceName) {
    return resourceName.startsWith(folderName) && super.acceptResource(ctx, resourceName);
  }

}

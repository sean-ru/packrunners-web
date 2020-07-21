package org.packrunners.webapp.setup;

import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.module.delta.ArrayDelegateTask;
import info.magnolia.module.delta.CreateNodePathTask;
import info.magnolia.module.delta.SetPropertyTask;
import info.magnolia.repository.RepositoryConstants;


/**
 * Sets permissions for the web app users.
 */
public class AddPermissionTask extends ArrayDelegateTask {

  public AddPermissionTask(String path, String role) {
    super("Give [" + role + "] permissions to " + path);
    addTask(new CreateNodePathTask("", "", RepositoryConstants.CONFIG, path,
        NodeTypes.ContentNode.NAME));
    addTask(new SetPropertyTask(RepositoryConstants.CONFIG, path, role, role));
  }
}

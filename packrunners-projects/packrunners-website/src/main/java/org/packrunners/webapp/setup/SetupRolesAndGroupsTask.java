package org.packrunners.webapp.setup;

import info.magnolia.module.delta.ArrayDelegateTask;
import info.magnolia.module.delta.IsModuleInstalledOrRegistered;
import info.magnolia.module.delta.NewPropertyTask;


/**
 * Gives basic permissions to the roles and groups defined by this project.
 */
public class SetupRolesAndGroupsTask extends ArrayDelegateTask {

  public static final String PUBLISHER_ROLE = "packrunweb-publisher";
  public static final String EDITOR_ROLE = "packrunweb-editor";
  public static final String ADMINCENTRAL_ROLE = "packrunweb-admincentral";

  public static final String PUBLISHERS_GROUP = "packrunweb-publishers";
  public static final String PAGES_ACTIVATE_ACCESS_ROLES = "/modules/pages/apps/pages/subApps/browser/actions/activate/availability/access/roles";
  public static final String DAM_ACTIVATE_ACCESS_ROLES = "/modules/dam-app/apps/assets/subApps/browser/actions/activate/availability/access/roles";
  public static final String PAGES_PERMISSIONS_ROLES = "/modules/pages/apps/pages/permissions/roles";
  public static final String DAM_PERMISSIONS_ROLES = "/modules/dam-app/apps/assets/permissions/roles";
  public static final String WORKFLOW_JBPM_PUBLISH_GROUPS = "/modules/workflow-jbpm/tasks/publish/groups";
  public static final String ENTERPRISE_MODULE = "enterprise";
  public static final String WORKFLOW_JBPM_MODULE = "workflow-jbpm";

  public SetupRolesAndGroupsTask() {
    super("Set permissions for the packrunweb-editor(s) and packrunweb-publisher(s) roles and groups");

    addTask(new AddPermissionTask(PAGES_PERMISSIONS_ROLES, EDITOR_ROLE));
    addTask(new AddPermissionTask(PAGES_PERMISSIONS_ROLES, PUBLISHER_ROLE));

    addTask(new AddPermissionTask(DAM_PERMISSIONS_ROLES, EDITOR_ROLE));
    addTask(new AddPermissionTask(DAM_PERMISSIONS_ROLES, PUBLISHER_ROLE));

    addTask(new AddPermissionTask(PAGES_ACTIVATE_ACCESS_ROLES, PUBLISHER_ROLE));
    addTask(new IsModuleInstalledOrRegistered("If on EE, give editors publishing rights on website",
        ENTERPRISE_MODULE, new AddPermissionTask(PAGES_ACTIVATE_ACCESS_ROLES, EDITOR_ROLE)));

    addTask(new AddPermissionTask(DAM_ACTIVATE_ACCESS_ROLES, PUBLISHER_ROLE));
    addTask(new AddPermissionTask(DAM_ACTIVATE_ACCESS_ROLES, EDITOR_ROLE));

    addTask(new IsModuleInstalledOrRegistered(
        "If workflow-jbpm module is installed, add workflow permissions for " + PUBLISHERS_GROUP,
        WORKFLOW_JBPM_MODULE,
        new NewPropertyTask("", WORKFLOW_JBPM_PUBLISH_GROUPS, PUBLISHERS_GROUP, PUBLISHERS_GROUP)));
  }
}

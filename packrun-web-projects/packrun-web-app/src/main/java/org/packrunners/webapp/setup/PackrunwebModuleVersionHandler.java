package org.packrunners.webapp.setup;

import com.google.common.collect.Lists;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.module.DefaultModuleVersionHandler;
import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.ArrayDelegateTask;
import info.magnolia.module.delta.BootstrapSingleModuleResource;
import info.magnolia.module.delta.BootstrapSingleResource;
import info.magnolia.module.delta.BootstrapSingleResourceAndOrderAfter;
import info.magnolia.module.delta.CheckAndModifyPropertyValueTask;
import info.magnolia.module.delta.CopyNodeTask;
import info.magnolia.module.delta.CreateNodePathTask;
import info.magnolia.module.delta.CreateNodeTask;
import info.magnolia.module.delta.DeltaBuilder;
import info.magnolia.module.delta.FilterOrderingTask;
import info.magnolia.module.delta.HasPropertyDelegateTask;
import info.magnolia.module.delta.IsAdminInstanceDelegateTask;
import info.magnolia.module.delta.IsInstallSamplesTask;
import info.magnolia.module.delta.IsModuleInstalledOrRegistered;
import info.magnolia.module.delta.NodeExistsDelegateTask;
import info.magnolia.module.delta.RemoveNodeTask;
import info.magnolia.module.delta.SetPropertyTask;
import info.magnolia.module.delta.Task;
import info.magnolia.module.delta.ValueOfPropertyDelegateTask;
import info.magnolia.module.delta.WarnTask;
import info.magnolia.module.site.setup.DefaultSiteExistsDelegateTask;
import info.magnolia.repository.RepositoryConstants;
import java.util.ArrayList;
import java.util.List;
import javax.jcr.ImportUUIDBehavior;


public class PackrunwebModuleVersionHandler extends DefaultModuleVersionHandler {

  private static final String DEFAULT_URI_NODEPATH = "/modules/ui-admincentral/virtualUriMappings/default";
  private static final String DEFAULT_URI = "redirect:/packrunners.html";

  private final Task setDefaultUriOnPublicInstance = new ValueOfPropertyDelegateTask(
      "Set default URI to home page, when current site is packrunners site",
      "/modules/site/config/site", "extends", "/modules/packrunweb/config/packrunners", false,
      new IsAdminInstanceDelegateTask("Set default URI to home page",
          String.format("Set default URI to point to '%s'", DEFAULT_URI), null,
          new NodeExistsDelegateTask("", DEFAULT_URI_NODEPATH,
              new SetPropertyTask(RepositoryConstants.CONFIG, DEFAULT_URI_NODEPATH, "toUri",
                  DEFAULT_URI),
              new WarnTask("Set default URI to home page",
                  "Could not set default URI to home packrunners page, default mapping was not found."))));

  private final Task setupPackrunnersSiteAsActiveSite = new NodeExistsDelegateTask(
      "Set packrunners as an active site", "/modules/site/config/site",
      new HasPropertyDelegateTask("Check extends property and update or create it",
          "/modules/site/config/site", "extends",
          new CheckAndModifyPropertyValueTask("/modules/site/config/site", "extends",
              "/modules/standard-templating-kit/config/site", "/modules/packrunweb/config/packrunners"),
          new DefaultSiteExistsDelegateTask("", "",
              new SetPropertyTask(RepositoryConstants.CONFIG, "/modules/site/config/site",
                  "extends", "/modules/packrunweb/config/packrunners"))),
      new ArrayDelegateTask("",
          new CreateNodeTask("", "/modules/site/config", "site", NodeTypes.ContentNode.NAME),
          new SetPropertyTask(RepositoryConstants.CONFIG, "/modules/site/config/site", "extends",
              "/modules/packrunweb/config/packrunners")
      ));

  private final Task copySiteToMultiSiteAndMakeItFallback = new CopySiteToMultiSiteAndMakeItFallback();

  private final Task setupAccessPermissionsForDemoUsers = new SetupRoleBasedAccessPermissionsTask(
      "Deny access permissions to apps",
      "Deny access permissions to Contacts app, Web Dev group, Set Up group for packrunweb-admincentral role",
      Lists.newArrayList("packrunweb-admincentral"), false, "/modules/contacts/apps/contacts",
      "/modules/ui-admincentral/config/appLauncherLayout/groups/stk",
      "/modules/ui-admincentral/config/appLauncherLayout/groups/manage");

  private final Task setupTargetAppGroupAccessPermissions = new SetupRoleBasedAccessPermissionsTask(
      "Allow access to Target app group",
      "Allow access to Target app group only to packrunweb-editor and packrunweb-publisher roles",
      Lists.newArrayList("packrunweb-editor", "packrunweb-publisher"), true,
      "/modules/ui-admincentral/config/appLauncherLayout/groups/target");

  private final InstallPurSamplesTask installPurSamples = new InstallPurSamplesTask();

  public PackrunwebModuleVersionHandler() {
    register(DeltaBuilder.update("0.0.1", "")
        .addTask(new IsInstallSamplesTask("Re-Bootstrap website content for packrunners pages",
            "Re-bootstrap website content to account for all changes",
            new ArrayDelegateTask("",
                new BootstrapSingleResource("", "",
                    "/mgnl-bootstrap-samples/packrunweb/website.packrunners.yaml"),
                new BootstrapSingleResource("", "",
                    "/mgnl-bootstrap-samples/packrunweb/dam.packrunweb.xml",
                    ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING))))
        // We re-bootstrap twice because a simple (and single) re-bootstrap (using ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING) would NOT
        // "move" an existing site definition (which might actually exist from a previous version) in the site module
        .addTask(new BootstrapSingleModuleResource("config.modules.packrunweb.config.packrunners.xml",
            ImportUUIDBehavior.IMPORT_UUID_COLLISION_REMOVE_EXISTING))
        .addTask(new BootstrapSingleModuleResource("config.modules.packrunweb.config.packrunners.xml",
            ImportUUIDBehavior.IMPORT_UUID_COLLISION_THROW))

        .addTask(new NodeExistsDelegateTask("Remove packrunweb-theme configuration from JCR",
            "/modules/site/config/themes/packrunweb-theme",
            new RemoveNodeTask("", "/modules/site/config/themes/packrunweb-theme")))

        .addTask(setupPackrunnersSiteAsActiveSite)
        .addTask(setDefaultUriOnPublicInstance)

        .addTask(installPurSamples)

        .addTask(new IsModuleInstalledOrRegistered("Enable packrunners site in multisite configuration",
            "multisite",
            new NodeExistsDelegateTask("Check whether multisite can be enabled for packrunners web site",
                "/modules/packrunweb/config/packrunners",
                new NodeExistsDelegateTask(
                    "Check whether packrunners was already copied in a previous version",
                    "/modules/multisite/config/sites/default",
                    new ArrayDelegateTask("", "",
                        new RemoveSiteFromMultiSite(),
                        copySiteToMultiSiteAndMakeItFallback),
                    new NodeExistsDelegateTask(
                        "Check whether packrunners node in multisite does not exist.",
                        "/modules/multisite/config/sites/packrunners", null,
                        copySiteToMultiSiteAndMakeItFallback)))))

        .addTask(new NodeExistsDelegateTask("Configure permissions for access to Pages app",
            "/modules/pages/apps/pages",
            new ArrayDelegateTask("Configure permissions for access to Pages app",
                new CreateNodePathTask("", "", RepositoryConstants.CONFIG,
                    "/modules/pages/apps/pages/permissions/roles", NodeTypes.ContentNode.NAME),
                new SetPropertyTask(RepositoryConstants.CONFIG,
                    SetupRolesAndGroupsTask.PAGES_PERMISSIONS_ROLES,
                    SetupRolesAndGroupsTask.EDITOR_ROLE, SetupRolesAndGroupsTask.EDITOR_ROLE),
                new SetPropertyTask(RepositoryConstants.CONFIG,
                    SetupRolesAndGroupsTask.PAGES_PERMISSIONS_ROLES,
                    SetupRolesAndGroupsTask.PUBLISHER_ROLE,
                    SetupRolesAndGroupsTask.PUBLISHER_ROLE))))
        .addTask(setupAccessPermissionsForDemoUsers)

        .addTask(setupTargetAppGroupAccessPermissions)

        .addTask(new IsModuleInstalledOrRegistered(
            "Copy changes in site definition to multisite if multisite is installed", "multisite",
            new IsModuleInstalledOrRegistered("", "public-user-registration",
                new CopyNodeTask("",
                    "/modules/packrunweb/config/packrunners/templates/availability/templates/pur",
                    "/modules/multisite/config/sites/packrunners/templates/availability/templates/pur",
                    true))))
    );
    register(DeltaBuilder.update("0.0.2", "")
        .addTask(new BootstrapSingleResourceAndOrderAfter(
            "Bootstrap addCORSHeaders filter to be /.rest/* urls available via CORS",
            "Bootstrap addCORSHeaders filter to be /.rest/* urls available via CORS",
            "/mgnl-bootstrap/packrunweb/config.server.filters.addCORSHeaders.xml", "uriSecurity"))
    );
    register(DeltaBuilder.update("0.0.3", "")
        .addTask(new RemoveNodeTask(
            "Remove packrunners node from PUR module configuration in favor of YAML decoration",
            "/modules/public-user-registration/config/configurations/packrunners"))
    );
  }

  @Override
  protected List<Task> getExtraInstallTasks(InstallContext installContext) {
    final List<Task> tasks = new ArrayList<>();
    tasks.addAll(super.getExtraInstallTasks(installContext));
    tasks.add(setupPackrunnersSiteAsActiveSite);
    tasks.add(setDefaultUriOnPublicInstance);

    tasks.add(installPurSamples);

    tasks.add(new IsModuleInstalledOrRegistered("Enable packrunners site in multisite configuration",
        "multisite",
        new NodeExistsDelegateTask("Check whether multisite can be enabled for packrunners site",
            "/modules/packrunweb/config/packrunners",
            copySiteToMultiSiteAndMakeItFallback)));
    tasks.add(new SetupRolesAndGroupsTask());
    tasks.add(setupAccessPermissionsForDemoUsers);
    tasks.add(setupTargetAppGroupAccessPermissions);
    tasks.add(new FilterOrderingTask("addCORSHeaders", new String[]{"uriSecurity"}));
    return tasks;
  }

}

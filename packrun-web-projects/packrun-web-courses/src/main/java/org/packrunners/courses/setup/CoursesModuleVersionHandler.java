package org.packrunners.courses.setup;

import static info.magnolia.repository.RepositoryConstants.CONFIG;
import static info.magnolia.repository.RepositoryConstants.WEBSITE;
import static javax.jcr.ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING;

import com.google.common.collect.Lists;
import info.magnolia.module.DefaultModuleVersionHandler;
import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.AddRoleToUserTask;
import info.magnolia.module.delta.ArrayDelegateTask;
import info.magnolia.module.delta.BootstrapSingleModuleResource;
import info.magnolia.module.delta.BootstrapSingleResource;
import info.magnolia.module.delta.BootstrapSingleResourceAndOrderAfter;
import info.magnolia.module.delta.CheckAndModifyPropertyValueTask;
import info.magnolia.module.delta.CopyNodeTask;
import info.magnolia.module.delta.DeltaBuilder;
import info.magnolia.module.delta.IsInstallSamplesTask;
import info.magnolia.module.delta.IsModuleInstalledOrRegistered;
import info.magnolia.module.delta.NodeExistsDelegateTask;
import info.magnolia.module.delta.OrderNodeBeforeTask;
import info.magnolia.module.delta.RemoveNodeTask;
import info.magnolia.module.delta.RemovePropertiesTask;
import info.magnolia.module.delta.SetPropertyTask;
import info.magnolia.module.delta.Task;
import info.magnolia.module.delta.ValueOfPropertyDelegateTask;
import info.magnolia.rendering.module.setup.InstallRendererContextAttributeTask;
import info.magnolia.repository.RepositoryConstants;
import info.magnolia.ui.contentapp.ConfiguredContentAppDescriptor;
import info.magnolia.ui.contentapp.contenttypes.ConfiguredContentTypeAppDescriptor;
import java.util.ArrayList;
import java.util.List;
import org.packrunners.courses.CourseTemplatingFunctions;
import org.packrunners.webapp.setup.AddPermissionTask;
import org.packrunners.webapp.setup.CopySiteToMultiSiteAndMakeItFallback;
import org.packrunners.webapp.setup.FolderBootstrapTask;
import org.packrunners.webapp.setup.SetPageAsPublishedTask;


/**
 * {@link DefaultModuleVersionHandler} of the {@link org.packrunners.courses.CoursesModule}.
 */
public class CoursesModuleVersionHandler extends DefaultModuleVersionHandler {

  protected static final String TRAVEL_DEMO_TOUR_EDITOR_ROLE = "packrunweb-course-editor";
  protected static final String DAM_PERMISSIONS_ROLES = "/modules/dam-app/apps/assets/permissions/roles";

  private final Task orderPageNodes = new ArrayDelegateTask(
      "Order packrunners pages before the 'about' page", "",
      new OrderNodeBeforeTask("", "", WEBSITE, "/packrunners/course-type", "about"),
      new OrderNodeBeforeTask("", "", WEBSITE, "/packrunners/destination", "about"),
      new OrderNodeBeforeTask("", "", WEBSITE, "/packrunners/course", "about"));

  public CoursesModuleVersionHandler() {
    register(DeltaBuilder.update("0.0.1", "")
        .addTask(new FolderBootstrapTask("/mgnl-bootstrap/courses/packrunweb/"))
        .addTask(new IsInstallSamplesTask("Re-Bootstrap website content for packrunners pages",
            "Re-bootstrap website content to account for all changes",
            new ArrayDelegateTask("",
                new FolderBootstrapTask("/mgnl-bootstrap-samples/courses/website/"),
                new ArrayDelegateTask("Re-Bootstrap category content for packrunweb courses",
                    "Re-bootstrap category content to account for all changes",
                    new BootstrapSingleResource("", "",
                            "/mgnl-bootstrap-samples/courses/category.schools.xml"),
                    new BootstrapSingleResource("", "",
                        "/mgnl-bootstrap-samples/courses/category.course-types.xml")),
                new BootstrapSingleResource("Re bootstrap courses content", "",
                    "/mgnl-bootstrap-samples/courses/courses.NNHS-Courses.xml"))))
//                new FolderBootstrapTask("/mgnl-bootstrap-samples/courses/assets/"),
//                new NodeExistsDelegateTask("", "", WEBSITE, "/packrunners/about/careers/main/06",
//                    new OrderNodeBeforeTask("Order careers 05 node before 06", "", WEBSITE,
//                        "/packrunners/about/careers/main/05", "06")))))
        .addTask(new BootstrapSingleModuleResource("config.modules.courses.apps.courseCategories.xml",
            IMPORT_UUID_COLLISION_REPLACE_EXISTING))
        .addTask(new BootstrapSingleModuleResource("config.modules.courses.apps.courses.xml",
            IMPORT_UUID_COLLISION_REPLACE_EXISTING))
        .addTask(new IsModuleInstalledOrRegistered("Enable packrunweb site in multisite configuration",
            "multisite",
            new NodeExistsDelegateTask("Check whether multisite can be enabled for packrunweb demo",
                "/modules/packrunweb/config/packrunweb",
                new CopySiteToMultiSiteAndMakeItFallback(true))))
        .addTask(new NodeExistsDelegateTask("Add permission for access to Dam app",
            DAM_PERMISSIONS_ROLES,
            new SetPropertyTask(CONFIG, DAM_PERMISSIONS_ROLES, TRAVEL_DEMO_TOUR_EDITOR_ROLE,
                TRAVEL_DEMO_TOUR_EDITOR_ROLE)))

        .addTask(orderPageNodes)
        .addTask(new SetPageAsPublishedTask("/packrunweb", true))
        .addTask(
            new RemoveNodeTask("Cleanup deprecated virtualURIMapping location before re-install",
                "/modules/courses/virtualURIMapping"))
        .addTask(
            new BootstrapSingleResource("Re-Bootstrap virtual URI mapping for courses module.", "",
                "/mgnl-bootstrap/courses/config.modules.courses.virtualUriMappings.xml",
                IMPORT_UUID_COLLISION_REPLACE_EXISTING))
    );

    register(DeltaBuilder.update("0.0.2", "")
        .addTask(new RemovePropertiesTask("Remove obsolete i18nBasename properties",
            RepositoryConstants.CONFIG, Lists.newArrayList(
            "/modules/courses/apps/courses/subApps/detail/editor/form/tabs/course/fields/courseTypes/i18nBasename",
            "/modules/courses/apps/courses/subApps/detail/editor/form/tabs/course/fields/destination/i18nBasename"
        ), false))
    );

    register(DeltaBuilder.update("0.0.3", "")
        .addTask(
            new ValueOfPropertyDelegateTask("Re-Bootstrap virtual URI mapping for courses module.",
                "/modules/courses/virtualUriMappings/coursesMapping", "class",
                "info.magnolia.virtualuri.mapping.RegexpVirtualUriMapping", true,
                new BootstrapSingleResource("Re-Bootstrap virtual URI mapping for courses module.",
                    "Re-Bootstrap virtual URI mapping to avoid collision with resource files.",
                    "/mgnl-bootstrap/courses/config.modules.courses.virtualUriMappings.xml",
                    IMPORT_UUID_COLLISION_REPLACE_EXISTING)))
        .addTask(new BootstrapSingleResource("Change type of duration field to number",
            "Re-bootstrap the duration field in courses editor.",
            "/mgnl-bootstrap/courses/config.modules.courses.apps.courses.xml",
            "courses/subApps/detail/editor/form/tabs/course/fields/duration",
            IMPORT_UUID_COLLISION_REPLACE_EXISTING))
        .addTask(new BootstrapSingleResource("Re-bootstrap the courses workspace",
            "Re-bootstrap the courses workspace.",
            "/mgnl-bootstrap-samples/courses/courses.NNHS-Courses.xml",
            IMPORT_UUID_COLLISION_REPLACE_EXISTING))
        .addTask(new ArrayDelegateTask("Bootstrap Course Finder",
            "Extract files and modify the packrunweb demo.",
            new BootstrapSingleResource("", "",
                "/mgnl-bootstrap-samples/courses/website/website.packrunners.course-finder.yaml"),
            new BootstrapSingleResourceAndOrderAfter(
                "/mgnl-bootstrap-samples/courses/website/website.packrunners.main.01.yaml", "0")))
    );

    register(DeltaBuilder.update("0.0.5", "")
        .addTask(new FolderBootstrapTask("/mgnl-bootstrap-samples/courses/assets/"))
    );

    register(DeltaBuilder.update("0.0.6", "")
        .addTask(new FolderBootstrapTask("/mgnl-bootstrap-samples/courses/assets/"))
        .addTask(new NodeExistsDelegateTask("Change courses app definition to support Content Type",
            "/modules/courses/apps/courses",
            new ArrayDelegateTask("",
                new CheckAndModifyPropertyValueTask("/modules/courses/apps/courses", "class",
                    ConfiguredContentAppDescriptor.class.getName(),
                    ConfiguredContentTypeAppDescriptor.class.getName()),
                new SetPropertyTask(RepositoryConstants.CONFIG, "/modules/courses/apps/courses",
                    "contentType", "course")
            )))
        .addTask(new NodeExistsDelegateTask(
            "Change courseCategories app definition to support Content Type",
            "/modules/courses/apps/courseCategories",
            new ArrayDelegateTask("",
                new CheckAndModifyPropertyValueTask("/modules/courses/apps/courseCategories", "class",
                    ConfiguredContentAppDescriptor.class.getName(),
                    ConfiguredContentTypeAppDescriptor.class.getName()),
                new SetPropertyTask(RepositoryConstants.CONFIG,
                    "/modules/courses/apps/courseCategories", "contentType", "courseCategory")
            )))
    );
  }

  @Override
  protected List<Task> getExtraInstallTasks(InstallContext installContext) {
    final List<Task> tasks = new ArrayList<>();

    tasks.addAll(super.getExtraInstallTasks(installContext));

    tasks.add(new InstallRendererContextAttributeTask("rendering", "freemarker", "coursefn",
        CourseTemplatingFunctions.class.getName()));

    /* Order bootstrapped pages accordingly */
    tasks.add(orderPageNodes);
//    tasks.add(new OrderNodeBeforeTask("Order careers zeroFive node before zeroFix",
//        "Order careers zeroFive node before zeroFix", WEBSITE, "/packrunners/about/careers/main/05",
//        "06"));
    tasks.add(new OrderNodeBeforeTask("Place Course Finder component on packrunweb home page",
        "Place Course Finder component on the correct position on the home page.", WEBSITE,
        "/packrunners/main/01", "00"));

    /* Add packrunweb-base role to user anonymous */
    tasks.add(new AddRoleToUserTask("Adds role 'packrunweb-base' to user 'anonymous'", "anonymous",
        "packrunweb-base"));
    tasks.add(new AddPermissionTask(DAM_PERMISSIONS_ROLES, TRAVEL_DEMO_TOUR_EDITOR_ROLE));

    tasks.add(new IsModuleInstalledOrRegistered(
        "Copy template availability and navigation areas from site definition to multisite module",
        "multisite",
        new ArrayDelegateTask("",
            new CopyNodeTask("Copy course template",
                "/modules/packrunweb/config/packrunweb/templates/availability/templates/course",
                "/modules/multisite/config/sites/packrunweb/templates/availability/templates/course",
                false),
            new CopyNodeTask("Copy categoryOverview template",
                "/modules/packrunweb/config/packrunweb/templates/availability/templates/categoryOverview",
                "/modules/multisite/config/sites/packrunweb/templates/availability/templates/categoryOverview",
                false),
            new CopyNodeTask("Copy categoryOverview template",
                "/modules/packrunweb/config/packrunweb/templates/availability/templates/destinationCatOverview",
                "/modules/multisite/config/sites/packrunweb/templates/availability/templates/destinationCatOverview",
                false))));
    tasks.add(new SetPageAsPublishedTask("/packrunweb", true));
    return tasks;
  }

}

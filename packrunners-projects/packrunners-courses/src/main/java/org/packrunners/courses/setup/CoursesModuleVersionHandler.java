package org.packrunners.courses.setup;

import com.google.common.collect.Lists;
import info.magnolia.module.DefaultModuleVersionHandler;
import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.*;
import info.magnolia.rendering.module.setup.InstallRendererContextAttributeTask;
import info.magnolia.repository.RepositoryConstants;
import info.magnolia.ui.contentapp.ConfiguredContentAppDescriptor;
import info.magnolia.ui.contentapp.contenttypes.ConfiguredContentTypeAppDescriptor;
import org.packrunners.courses.CourseTemplatingFunctions;

import java.util.ArrayList;
import java.util.List;

import static info.magnolia.repository.RepositoryConstants.CONFIG;
import static info.magnolia.repository.RepositoryConstants.WEBSITE;
import static javax.jcr.ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING;


/**
 * {@link DefaultModuleVersionHandler} of the {@link org.packrunners.courses.CoursesModule}.
 */
public class CoursesModuleVersionHandler extends DefaultModuleVersionHandler {

    protected static final String PACKRUNNERS_COURSE_EDITOR_ROLE = "packrunners-course-editor";
    protected static final String PACKRUNNERS_BASE_ROLE = "packrunners-base";
    protected static final String DAM_PERMISSIONS_ROLES = "/modules/dam-app/apps/assets/permissions/roles";

    private final Task orderPageNodes = new ArrayDelegateTask(
            "Order packrunners pages before the 'about' page", "",
            new OrderNodeBeforeTask("", "", WEBSITE, "/packrunners/course-type", "about"),
            new OrderNodeBeforeTask("", "", WEBSITE, "/packrunners/course", "about"));

    public CoursesModuleVersionHandler() {

        register(DeltaBuilder.update("0.2.1", "")
                .addTask(new FolderBootstrapTask("/mgnl-bootstrap/courses/packrunners/"))
                .addTask(new IsInstallSamplesTask("Re-Bootstrap website content for packrunners pages",
                        "Re-bootstrap website content to account for all changes",
                        new ArrayDelegateTask("",
                                new FolderBootstrapTask("/mgnl-bootstrap-samples/courses/website/"),
                                new BootstrapSingleResource("Re bootstrap courses content", "",
                                        "/mgnl-bootstrap-samples/courses/courses.NNHS-Courses.xml")),
                        new FolderBootstrapTask("/mgnl-bootstrap-samples/courses/assets/")
                ))

                .addTask(new BootstrapSingleModuleResource("config.modules.courses.xml",
                        IMPORT_UUID_COLLISION_REPLACE_EXISTING))
                .addTask(new NodeExistsDelegateTask("Add permission for access to Dam app",
                        DAM_PERMISSIONS_ROLES,
                        new SetPropertyTask(CONFIG, DAM_PERMISSIONS_ROLES, PACKRUNNERS_COURSE_EDITOR_ROLE,
                                PACKRUNNERS_COURSE_EDITOR_ROLE)))

                .addTask(orderPageNodes)
                .addTask(new SetPageAsPublishedTask("/packrunners", true))


/*
                .addTask(
                        new RemoveNodeTask("Cleanup deprecated virtualURIMapping location before re-install",
                                "/modules/courses/virtualURIMapping"))
                .addTask(
                        new BootstrapSingleResource("Re-Bootstrap virtual URI mapping for courses module.", "",
                                "/mgnl-bootstrap/courses/config.modules.courses.virtualUriMappings.xml",
                                IMPORT_UUID_COLLISION_REPLACE_EXISTING))
*/
        );


        register(DeltaBuilder.update("0.2.2", "")
                .addTask(new RemovePropertiesTask("Remove obsolete i18nBasename properties",
                        RepositoryConstants.CONFIG, Lists.newArrayList(
                        "/modules/courses/apps/courses/subApps/detail/editor/form/tabs/course/fields/courseTypes/i18nBasename",
                        "/modules/courses/apps/courses/subApps/detail/editor/form/tabs/course/fields/schools/i18nBasename",
                        "/modules/courses/apps/courses/subApps/detail/editor/form/tabs/course/fields/courseNumbers/i18nBasename"
                ), false))
        );

        register(DeltaBuilder.update("0.2.3", "")
                .addTask(
                        new ValueOfPropertyDelegateTask("Re-Bootstrap virtual URI mapping for courses module.",
                                "/modules/courses/virtualUriMappings/coursesMapping", "class",
                                "info.magnolia.virtualuri.mapping.RegexpVirtualUriMapping", true,
                                new BootstrapSingleResource("Re-Bootstrap virtual URI mapping for courses module.",
                                        "Re-Bootstrap virtual URI mapping to avoid collision with resource files.",
                                        "/mgnl-bootstrap/courses/config.modules.courses.virtualUriMappings.xml",
                                        IMPORT_UUID_COLLISION_REPLACE_EXISTING)))
                .addTask(new BootstrapSingleResource("Re-bootstrap the courses workspace",
                        "Re-bootstrap the courses workspace.",
                        "/mgnl-bootstrap-samples/courses/courses.NNHS-Courses.xml",
                        IMPORT_UUID_COLLISION_REPLACE_EXISTING))
        );

        register(DeltaBuilder.update("0.2.4", "")
                .addTask(new FolderBootstrapTask("/mgnl-bootstrap-samples/courses/assets/"))
        );

        register(DeltaBuilder.update("0.2.5", "")
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

        /* Add packrunners-base role to user anonymous */
        tasks.add(new AddRoleToUserTask("Adds role 'packrunners-base' to user 'anonymous'", "anonymous",
                PACKRUNNERS_BASE_ROLE));
        tasks.add(new AddPermissionTask(DAM_PERMISSIONS_ROLES, PACKRUNNERS_COURSE_EDITOR_ROLE));

        tasks.add(new IsModuleInstalledOrRegistered(
                "Copy template availability and navigation areas from site definition to multisite module",
                "multisite",
                new ArrayDelegateTask("",
                        new CopyNodeTask("Copy course template",
                                "/modules/packrunners/config/packrunners/templates/availability/templates/course",
                                "/modules/multisite/config/sites/packrunners/templates/availability/templates/course",
                                false),
                        new CopyNodeTask("Copy courseTypeOverview template",
                                "/modules/packrunners/config/packrunners/templates/availability/templates/courseTypeOverview",
                                "/modules/multisite/config/sites/packrunners/templates/availability/templates/courseTypeOverview",
                                false)
                )));
        tasks.add(new SetPageAsPublishedTask("/packrunners", true));
        return tasks;
    }

}

package org.packrunners.videos.setup;

import info.magnolia.module.DefaultModuleVersionHandler;
import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.*;
import info.magnolia.repository.RepositoryConstants;
import info.magnolia.ui.contentapp.ConfiguredContentAppDescriptor;
import info.magnolia.ui.contentapp.contenttypes.ConfiguredContentTypeAppDescriptor;
import org.packrunners.videos.VideosModule;

import java.util.ArrayList;
import java.util.List;

import static info.magnolia.repository.RepositoryConstants.CONFIG;
import static javax.jcr.ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING;


/**
 * {@link DefaultModuleVersionHandler} of the {@link VideosModule}.
 */
public class VideosModuleVersionHandler extends DefaultModuleVersionHandler {

    protected static final String PACKRUNNERS_COURSE_EDITOR_ROLE = "packrunners-course-editor";
    protected static final String DAM_PERMISSIONS_ROLES = "/modules/dam-app/apps/assets/permissions/roles";

    public VideosModuleVersionHandler() {
        register(DeltaBuilder.update("0.3.1", "")
                .addTask(new IsInstallSamplesTask("Re-Bootstrap website content for packrunners pages",
                        "Re-bootstrap website content to account for all changes",
                        new ArrayDelegateTask("",
                                new BootstrapSingleResource("Re bootstrap courses content", "",
                                        "/mgnl-bootstrap-samples/videos/videos.Matthew.xml")),
                        new FolderBootstrapTask("/mgnl-bootstrap-samples/videos/assets/")
                ))

                .addTask(new BootstrapSingleModuleResource("config.modules.videos.apps.videos.xml",
                        IMPORT_UUID_COLLISION_REPLACE_EXISTING))
                .addTask(new NodeExistsDelegateTask("Add permission for access to Dam app",
                        DAM_PERMISSIONS_ROLES,
                        new SetPropertyTask(CONFIG, DAM_PERMISSIONS_ROLES, PACKRUNNERS_COURSE_EDITOR_ROLE,
                                PACKRUNNERS_COURSE_EDITOR_ROLE)))

                .addTask(new SetPageAsPublishedTask("/packrunners", true))
        );

        register(DeltaBuilder.update("0.3.2", "")
                .addTask(new BootstrapSingleResource("Re-bootstrap the videos workspace",
                        "Re-bootstrap the videos workspace.",
                        "/mgnl-bootstrap-samples/videos/videos.Matthew.xml",
                        IMPORT_UUID_COLLISION_REPLACE_EXISTING))
        );

        register(DeltaBuilder.update("0.3.3", "")
                .addTask(new FolderBootstrapTask("/mgnl-bootstrap-samples/videos/assets/"))
                .addTask(new NodeExistsDelegateTask("Change videos app definition to support Content Type",
                        "/modules/videos/apps/videos",
                        new ArrayDelegateTask("",
                                new CheckAndModifyPropertyValueTask("/modules/videos/apps/videos", "class",
                                        ConfiguredContentAppDescriptor.class.getName(),
                                        ConfiguredContentTypeAppDescriptor.class.getName()),
                                new SetPropertyTask(RepositoryConstants.CONFIG, "/modules/videos/apps/videos",
                                        "contentType", "video")
                        )))
        );


    }

    @Override
    protected List<Task> getExtraInstallTasks(InstallContext installContext) {
        final List<Task> tasks = new ArrayList<>();

        tasks.addAll(super.getExtraInstallTasks(installContext));

        tasks.add(new AddPermissionTask(DAM_PERMISSIONS_ROLES, PACKRUNNERS_COURSE_EDITOR_ROLE));

        tasks.add(new SetPageAsPublishedTask("/packrunners", true));

        return tasks;
    }

}

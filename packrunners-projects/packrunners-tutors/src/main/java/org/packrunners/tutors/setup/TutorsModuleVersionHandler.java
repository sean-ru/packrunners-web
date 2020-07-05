package org.packrunners.tutors.setup;

import info.magnolia.module.DefaultModuleVersionHandler;
import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.*;
import info.magnolia.rendering.module.setup.InstallRendererContextAttributeTask;
import info.magnolia.repository.RepositoryConstants;
import info.magnolia.ui.contentapp.ConfiguredContentAppDescriptor;
import info.magnolia.ui.contentapp.contenttypes.ConfiguredContentTypeAppDescriptor;
import org.packrunners.tutors.TutorTemplatingFunctions;
import org.packrunners.tutors.TutorsModule;

import java.util.ArrayList;
import java.util.List;

import static info.magnolia.repository.RepositoryConstants.CONFIG;
import static javax.jcr.ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING;


/**
 * {@link DefaultModuleVersionHandler} of the {@link TutorsModule}.
 */
public class TutorsModuleVersionHandler extends DefaultModuleVersionHandler {

    protected static final String PACKRUNNERS_ADMINCENTRAL_ROLE = "packrunners-admincentral";
    protected static final String DAM_PERMISSIONS_ROLES = "/modules/dam-app/apps/assets/permissions/roles";

    public TutorsModuleVersionHandler() {
        register(DeltaBuilder.update("0.5.1", "")
                .addTask(new IsInstallSamplesTask("Re-Bootstrap website content for packrunners pages",
                        "Re-bootstrap website content to account for all changes",
                        new ArrayDelegateTask("",
                                new BootstrapSingleResource("Re bootstrap tutors content", "",
                                        "/mgnl-bootstrap-samples/tutors/tutors.NNHS.xml")),
                        new FolderBootstrapTask("/mgnl-bootstrap-samples/tutors/assets/")))
                .addTask(new BootstrapSingleModuleResource("config.modules.tutors.xml",
                        IMPORT_UUID_COLLISION_REPLACE_EXISTING))
                .addTask(new NodeExistsDelegateTask("Add permission for access to Dam app",
                        DAM_PERMISSIONS_ROLES,
                        new SetPropertyTask(CONFIG, DAM_PERMISSIONS_ROLES, PACKRUNNERS_ADMINCENTRAL_ROLE,
                                PACKRUNNERS_ADMINCENTRAL_ROLE)))

                .addTask(new SetPageAsPublishedTask("/packrunners", true))

        );

        register(DeltaBuilder.update("0.5.2", "")
                .addTask(new BootstrapSingleResource("Re-bootstrap the tutors workspace",
                        "Re-bootstrap the tutors workspace.",
                        "/mgnl-bootstrap-samples/tutors/tutors.NNHS.xml",
                        IMPORT_UUID_COLLISION_REPLACE_EXISTING))
        );

        register(DeltaBuilder.update("0.5.3", "")
                .addTask(new FolderBootstrapTask("/mgnl-bootstrap-samples/tutors/assets/"))
                .addTask(new NodeExistsDelegateTask("Change tutors app definition to support Content Type",
                        "/modules/tutors/apps/tutors",
                        new ArrayDelegateTask("",
                                new CheckAndModifyPropertyValueTask("/modules/tutors/apps/tutors", "class",
                                        ConfiguredContentAppDescriptor.class.getName(),
                                        ConfiguredContentTypeAppDescriptor.class.getName()),
                                new SetPropertyTask(RepositoryConstants.CONFIG, "/modules/tutors/apps/tutors",
                                        "contentType", "tutor")
                        )))
        );

    }

    @Override
    protected List<Task> getExtraInstallTasks(InstallContext installContext) {
        final List<Task> tasks = new ArrayList<>();

        tasks.addAll(super.getExtraInstallTasks(installContext));

        tasks.add(new InstallRendererContextAttributeTask("rendering", "freemarker", "tutorfn",
                TutorTemplatingFunctions.class.getName()));

        return tasks;
    }

}

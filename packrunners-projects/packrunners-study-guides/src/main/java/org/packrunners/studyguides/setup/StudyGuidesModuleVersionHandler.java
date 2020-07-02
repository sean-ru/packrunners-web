package org.packrunners.studyguides.setup;

import info.magnolia.module.DefaultModuleVersionHandler;
import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.*;
import info.magnolia.repository.RepositoryConstants;
import info.magnolia.ui.contentapp.ConfiguredContentAppDescriptor;
import info.magnolia.ui.contentapp.contenttypes.ConfiguredContentTypeAppDescriptor;
import org.packrunners.studyguides.StudyGuidesModule;

import java.util.ArrayList;
import java.util.List;

import static info.magnolia.repository.RepositoryConstants.CONFIG;
import static javax.jcr.ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING;


/**
 * {@link DefaultModuleVersionHandler} of the {@link StudyGuidesModule}.
 */
public class StudyGuidesModuleVersionHandler extends DefaultModuleVersionHandler {

    protected static final String PACKRUNNERS_COURSE_EDITOR_ROLE = "packrunners-course-editor";
    protected static final String DAM_PERMISSIONS_ROLES = "/modules/dam-app/apps/assets/permissions/roles";

    public StudyGuidesModuleVersionHandler() {
        register(DeltaBuilder.update("0.4.1", "")
                .addTask(new IsInstallSamplesTask("Re-Bootstrap website content for packrunners pages",
                        "Re-bootstrap website content to account for all changes",
                        new ArrayDelegateTask("",
                                new BootstrapSingleResource("Re bootstrap courses content", "",
                                        "/mgnl-bootstrap-samples/studyguides/studyGuides.Matthew.xml")),
                        new FolderBootstrapTask("/mgnl-bootstrap-samples/studyguides/assets/")
                ))

                .addTask(new BootstrapSingleModuleResource("config.modules.studyguides.apps.studyGuides.xml",
                        IMPORT_UUID_COLLISION_REPLACE_EXISTING))
                .addTask(new NodeExistsDelegateTask("Add permission for access to Dam app",
                        DAM_PERMISSIONS_ROLES,
                        new SetPropertyTask(CONFIG, DAM_PERMISSIONS_ROLES, PACKRUNNERS_COURSE_EDITOR_ROLE,
                                PACKRUNNERS_COURSE_EDITOR_ROLE)))

                .addTask(new SetPageAsPublishedTask("/packrunners", true))
        );

        register(DeltaBuilder.update("0.4.2", "")
                .addTask(new BootstrapSingleResource("Re-bootstrap the studyGuides workspace",
                        "Re-bootstrap the video workspace.",
                        "/mgnl-bootstrap-samples/studyguides/studyguides.Matthew.xml",
                        IMPORT_UUID_COLLISION_REPLACE_EXISTING))
        );

        register(DeltaBuilder.update("0.4.3", "")
                .addTask(new FolderBootstrapTask("/mgnl-bootstrap-samples/studyguides/assets/"))
                .addTask(new NodeExistsDelegateTask("Change video app definition to support Content Type",
                        "/modules/studyguides/apps/studyGuides",
                        new ArrayDelegateTask("",
                                new CheckAndModifyPropertyValueTask("/modules/studyguides/apps/studyGuides", "class",
                                        ConfiguredContentAppDescriptor.class.getName(),
                                        ConfiguredContentTypeAppDescriptor.class.getName()),
                                new SetPropertyTask(RepositoryConstants.CONFIG, "/modules/studyguides/apps/studyGuides",
                                        "contentType", "studyGuide")
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

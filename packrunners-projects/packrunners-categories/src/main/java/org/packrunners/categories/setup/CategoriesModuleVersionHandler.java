package org.packrunners.categories.setup;

import info.magnolia.module.DefaultModuleVersionHandler;
import info.magnolia.module.delta.*;

import javax.jcr.ImportUUIDBehavior;

/**
 * {@link DefaultModuleVersionHandler} of the {@link org.packrunners.categories.CategoriesModule}.
 */
public class CategoriesModuleVersionHandler extends DefaultModuleVersionHandler {

    public CategoriesModuleVersionHandler() {
        register(DeltaBuilder.update("0.1.1", "")
                .addTask(new IsInstallSamplesTask("Re-Bootstrap categories content",
                        "Re-bootstrap categories content to account for all changes",
                        new ArrayDelegateTask("",
                                new BootstrapSingleResource("", "",
                                        "/mgnl-bootstrap-samples/categories/category.course-numbers.xml"),
                                new BootstrapSingleResource("", "",
                                        "/mgnl-bootstrap-samples/categories/category.course-types.xml"),
                                new BootstrapSingleResource("", "",
                                        "/mgnl-bootstrap-samples/categories/category.resource-types.xml"),
                                new BootstrapSingleResource("", "",
                                        "/mgnl-bootstrap-samples/categories/category.schools.xml")
                        )))
                // We re-bootstrap twice because a simple (and single) re-bootstrap (using ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING) would NOT
                // "move" an existing site definition (which might actually exist from a previous version) in the site module
                .addTask(new BootstrapSingleModuleResource("config.modules.categories.apps.categories.xml",
                        ImportUUIDBehavior.IMPORT_UUID_COLLISION_REMOVE_EXISTING))
                .addTask(new BootstrapSingleModuleResource("config.modules.categories.apps.categories.xml",
                        ImportUUIDBehavior.IMPORT_UUID_COLLISION_THROW))
        );

    }
}

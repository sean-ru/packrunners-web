package org.packrunners.webapp.setup;

import info.magnolia.cms.security.UserManager;
import info.magnolia.module.delta.AddURIPermissionTask;
import info.magnolia.module.delta.ArrayDelegateTask;
import info.magnolia.module.delta.BootstrapSingleModuleResource;
import info.magnolia.module.delta.BootstrapSingleResource;
import info.magnolia.module.delta.IsAdminInstanceDelegateTask;
import info.magnolia.module.delta.OrderNodeBeforeTask;
import info.magnolia.module.delta.Task;
import java.util.Arrays;
import java.util.List;


/**
 * {@link ArrayDelegateTask} which install samples for Public User Registration.
 */
public class InstallPurSamplesTask extends ArrayDelegateTask {

  protected static final String PUR_SAMPLE_ROOT_PAGE_NAME = "members";

  protected static final List<String> PROTECTED_PAGES_NAMES = Arrays.asList(
      "protected",
      "profile-update"
  );

  protected static final List<String> PROTECTED_PAGES_PATHS = Arrays.asList(
      "/packrunners/" + PUR_SAMPLE_ROOT_PAGE_NAME + "/" + PROTECTED_PAGES_NAMES.get(0) + "*",
      "/packrunners/" + PUR_SAMPLE_ROOT_PAGE_NAME + "/" + PROTECTED_PAGES_NAMES.get(1) + "*",
      "<packrunners>/" + PUR_SAMPLE_ROOT_PAGE_NAME + "/" + PROTECTED_PAGES_NAMES.get(0) + "*",
      "<packrunners>/" + PUR_SAMPLE_ROOT_PAGE_NAME + "/" + PROTECTED_PAGES_NAMES.get(1) + "*"
  );

  protected static final String PASSWORD_CHANGE_PAGE_PATH =
      "packrunners/" + PUR_SAMPLE_ROOT_PAGE_NAME + "/forgotten-password/password-change";

  public InstallPurSamplesTask() {
    super("Install PUR samples if public-user-registration module is installed");
    this.addTask(new BootstrapSingleModuleResource(
        "config.server.filters.securityCallback.clientCallbacks.packrunweb-pur.xml"));
    this.addTask(new BootstrapSingleResource("Install user role for PUR", "",
        "/mgnl-bootstrap-samples/packrunweb/userroles.packrunweb-pur.xml"));
    this.addTask(new BootstrapSingleResource("Install user group for PUR", "",
        "/mgnl-bootstrap-samples/packrunweb/usergroups.packrunweb-pur.xml"));
    this.addTask(new ArrayDelegateTask("",
        new IsAdminInstanceDelegateTask("", (Task) null, this.getPermissionTasks()),
        new OrderNodeBeforeTask("/server/filters/securityCallback/clientCallbacks/packrunweb-pur",
            "form")
    ));
  }

  private ArrayDelegateTask getPermissionTasks() {
    ArrayDelegateTask task = new ArrayDelegateTask("");
    for (String page : PROTECTED_PAGES_PATHS) {
      task.addTask(new AddURIPermissionTask("", UserManager.ANONYMOUS_USER, page,
          AddURIPermissionTask.DENY));
    }
    return task;
  }
}
package org.packrunners.courses.setup;

import static info.magnolia.test.hamcrest.NodeMatchers.hasNode;
import static info.magnolia.test.hamcrest.NodeMatchers.hasProperty;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import info.magnolia.context.MgnlContext;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.jcr.util.NodeTypes.Activatable;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.module.ModuleVersionHandler;
import info.magnolia.module.ModuleVersionHandlerTestCase;
import info.magnolia.module.model.Version;
import info.magnolia.repository.RepositoryConstants;
import info.magnolia.ui.contentapp.ConfiguredContentAppDescriptor;
import info.magnolia.ui.contentapp.contenttypes.ConfiguredContentTypeAppDescriptor;
import info.magnolia.ui.framework.action.ExportActionDefinition;
import info.magnolia.ui.framework.action.OpenExportDialogActionDefinition;
import info.magnolia.virtualuri.mapping.RegexpVirtualUriMapping;
import java.util.Arrays;
import java.util.List;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
/*

@Ignore
public class CoursesModuleVersionHandlerTest extends ModuleVersionHandlerTestCase {

  private Session configSession;
  private Session websiteSession;
  private Session damSession;

  @Override
  protected String getModuleDescriptorPath() {
    return "/META-INF/magnolia/courses.xml";
  }

  @Override
  protected ModuleVersionHandler newModuleVersionHandlerForTests() {
    return new CoursesModuleVersionHandler();
  }

  @Override
  protected List<String> getModuleDescriptorPathsForTests() {
    return Arrays.asList("/META-INF/magnolia/core.xml");
  }

  @Override
  protected String[] getExtraWorkspaces() {
    return new String[]{RepositoryConstants.WEBSITE, "courses", "category", "dam"};
  }

  @Override
  protected String getExtraNodeTypes() {
    return "/mgnl-nodetypes/test-course-nodetypes.xml";
  }

  @Override
  public String getRepositoryConfigFileName() {
    return "/org/packrunners/courses/service/test-courses-repositories.xml";
  }

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();

    configSession = MgnlContext.getJCRSession(RepositoryConstants.CONFIG);
    websiteSession = MgnlContext.getJCRSession(RepositoryConstants.WEBSITE);
    damSession = MgnlContext.getJCRSession("dam");

    addSupportForSetupModuleRepositoriesTask(null);
  }

  @Test
  public void demoRolesCanAccessTourCategoriesApp() throws Exception {
    // GIVEN
    setupBootstrapPages();
    setupConfigNode("/modules/courses/apps/tourCategories/permissions/roles");

    // WHEN
    executeUpdatesAsIfTheCurrentlyInstalledVersionWas(Version.parseVersion("0.7"));

    // THEN
    assertThat(configSession.getNode("/modules/courses/apps/tourCategories/permissions/roles"),
        hasProperty("packrunweb-editor", "packrunweb-editor"));
    assertThat(configSession.getNode("/modules/courses/apps/tourCategories/permissions/roles"),
        hasProperty("packrunweb-publisher", "packrunweb-publisher"));
  }

  @Test
  public void updateTo08SetsPagesAsPublished() throws Exception {
    // GIVEN
    setupBootstrapPages();
    websiteSession.getRootNode().addNode("packrunweb/foo", NodeTypes.Page.NAME);
    PropertyUtil.setProperty(websiteSession.getNode("/packrunweb/foo"), Activatable.ACTIVATION_STATUS,
        Long.valueOf(Activatable.ACTIVATION_STATUS_MODIFIED));

    // WHEN
    executeUpdatesAsIfTheCurrentlyInstalledVersionWas(Version.parseVersion("0.7"));

    // THEN
    int activationStatus = Activatable.getActivationStatus(websiteSession.getNode("/packrunweb/foo"));
    assertThat("We expect that /packrunweb/foo is activated", activationStatus,
        equalTo(Activatable.ACTIVATION_STATUS_ACTIVATED));
  }

  @Test
  public void cleanInstallSetsPagesAsPublished() throws Exception {
    // GIVEN
    setupBootstrapPages();
    PropertyUtil
        .setProperty(websiteSession.getNode("/packrunweb/tour-type"), Activatable.ACTIVATION_STATUS,
            Long.valueOf(Activatable.ACTIVATION_STATUS_MODIFIED));
    PropertyUtil
        .setProperty(websiteSession.getNode("/packrunweb/destination"), Activatable.ACTIVATION_STATUS,
            Long.valueOf(Activatable.ACTIVATION_STATUS_MODIFIED));
    PropertyUtil.setProperty(websiteSession.getNode("/packrunweb/tour"), Activatable.ACTIVATION_STATUS,
        Long.valueOf(Activatable.ACTIVATION_STATUS_MODIFIED));

    // WHEN
    executeUpdatesAsIfTheCurrentlyInstalledVersionWas(null);

    // THEN
    int activationStatus = Activatable
        .getActivationStatus(websiteSession.getNode("/packrunweb/tour-type"));
    assertThat("We expect that /packrunweb/tour-type node is activated", activationStatus,
        equalTo(Activatable.ACTIVATION_STATUS_ACTIVATED));

    activationStatus = Activatable
        .getActivationStatus(websiteSession.getNode("/packrunweb/destination"));
    assertThat("We expect that /packrunweb/destination node is activated", activationStatus,
        equalTo(Activatable.ACTIVATION_STATUS_ACTIVATED));

    activationStatus = Activatable.getActivationStatus(websiteSession.getNode("/packrunweb/tour"));
    assertThat("We expect that /packrunweb/tour node is activated", activationStatus,
        equalTo(Activatable.ACTIVATION_STATUS_ACTIVATED));
  }

  @Test
  public void demoRoleCanAccessDamApp() throws Exception {
    // GIVEN
    setupBootstrapPages();
    setupConfigNode(CoursesModuleVersionHandler.DAM_PERMISSIONS_ROLES);

    // WHEN
    executeUpdatesAsIfTheCurrentlyInstalledVersionWas(Version.parseVersion("0.7"));

    // THEN
    assertThat(configSession.getNode(CoursesModuleVersionHandler.DAM_PERMISSIONS_ROLES),
        hasProperty(CoursesModuleVersionHandler.TRAVEL_DEMO_TOUR_EDITOR_ROLE,
            CoursesModuleVersionHandler.TRAVEL_DEMO_TOUR_EDITOR_ROLE));
  }

  @Test
  public void updateFrom08AlsoReordersPages() throws Exception {
    // GIVEN
    setupBootstrapPages();

    // WHEN
    executeUpdatesAsIfTheCurrentlyInstalledVersionWas(Version.parseVersion("0.8"));

    // THEN
    final Node packrunwebPages = websiteSession.getNode("/packrunweb");
    final List<Node> pageNames = Lists
        .newArrayList(NodeUtil.getNodes(packrunwebPages, NodeTypes.Page.NAME));
    assertThat(Collections2.transform(pageNames, new ToNodeName()), contains(
        "tour-type",
        "destination",
        "tour",
        "about",
        "tour-finder"
    ));
  }

  @Test
  public void explicitlyBootstrappedCareersMain05NodeOrderedFreshInstall() throws Exception {
    // GIVEN
    setupBootstrapPages();
    Node careersMain = NodeUtil
        .createPath(websiteSession.getRootNode(), "/packrunweb/about/careers/main",
            NodeTypes.Component.NAME, true);

    // WHEN
    executeUpdatesAsIfTheCurrentlyInstalledVersionWas(null);

    // THEN
    assertThat(careersMain, hasNode("05"));
    List<Node> careerNodeList = Lists.newArrayList(careersMain.getNodes());
    assertThat(Collections2.transform(careerNodeList, new ToNodeName()), contains(
        "01",
        "05",
        "06"
    ));
  }

  @Test
  public void explicitlyBootstrappedCareersMain05NodeOrdered() throws Exception {
    // GIVEN
    setupBootstrapPages();
    Node careersMain = NodeUtil
        .createPath(websiteSession.getRootNode(), "/packrunweb/about/careers/main",
            NodeTypes.Component.NAME, true);

    // WHEN
    executeUpdatesAsIfTheCurrentlyInstalledVersionWas(Version.parseVersion("0.10"));

    // THEN
    assertThat(careersMain, hasNode("05"));
    List<Node> careerNodeList = Lists.newArrayList(careersMain.getNodes());
    assertThat(Collections2.transform(careerNodeList, new ToNodeName()), contains(
        "01",
        "05",
        "06"
    ));
  }

  @Test
  public void updateFrom113ReconfiguresExportAction() throws Exception {
    //GIVEN
    Node exportActionNode = NodeUtil.createPath(configSession.getRootNode(),
        "/modules/courses/apps/courses/subApps/browser/actions/export", NodeTypes.ContentNode.NAME,
        true);
    exportActionNode.setProperty("class", ExportActionDefinition.class.getName());
    exportActionNode.setProperty("command", "export");

    // WHEN
    executeUpdatesAsIfTheCurrentlyInstalledVersionWas(Version.parseVersion("1.1.3"));

    // THEN
    exportActionNode = configSession
        .getNode("/modules/courses/apps/courses/subApps/browser/actions/export");
    assertThat(exportActionNode,
        hasProperty("class", OpenExportDialogActionDefinition.class.getName()));
    assertThat(exportActionNode, hasProperty("dialogName", "ui-admincentral:export"));
    assertThat(exportActionNode, not(hasProperty("command")));
  }

  @Test
  public void updateFrom114ReinstallsUriMappings() throws Exception {
    // GIVEN
    Node coursesModule = NodeUtil
        .createPath(configSession.getRootNode(), "/modules/courses", NodeTypes.Content.NAME, true);
    coursesModule.addNode("virtualURIMapping", NodeTypes.Content.NAME);

    // WHEN
    executeUpdatesAsIfTheCurrentlyInstalledVersionWas(Version.parseVersion("1.1.4"));

    //THEN
    assertThat(coursesModule, not(hasNode("virtualURIMapping")));
    assertThat(coursesModule, hasNode("virtualUriMappings"));
    Node mappings = coursesModule.getNode("virtualUriMappings");
    assertThat(mappings, hasNode(allOf(
        hasProperty("class", RegexpVirtualUriMapping.class.getName()),
        hasProperty("fromUri", "^/courses(.*).html"),
        hasProperty("toUri", "forward:/packrunweb/tour?tour=$1"))));
  }

  @Test
  public void updateFrom() throws Exception {
    // GIVEN
    Node tourTypes = NodeUtil.createPath(configSession.getRootNode(),
        "/modules/courses/apps/courses/subApps/detail/editor/form/tabs/tour/fields/tourTypes",
        NodeTypes.Content.NAME, true);
    tourTypes.setProperty("i18nBasename", "info.magnolia.module.packrunweb-courses.messages");
    Node destination = NodeUtil.createPath(configSession.getRootNode(),
        "/modules/courses/apps/courses/subApps/detail/editor/form/tabs/tour/fields/destination",
        NodeTypes.Content.NAME, true);
    destination.setProperty("i18nBasename", "info.magnolia.module.packrunweb-courses.messages");

    // WHEN
    executeUpdatesAsIfTheCurrentlyInstalledVersionWas(Version.parseVersion("1.0.0-SNAPSHOT"));

    //THEN
    assertThat(tourTypes, not(hasProperty("i18nBasename")));
    assertThat(destination, not(hasProperty("i18nBasename")));
  }

  @Test
  public void updateFrom122InstallsTourFinder() throws Exception {
    // GIVEN
    Node packrunweb = NodeUtil
        .createPath(websiteSession.getRootNode(), "packrunweb", NodeTypes.Content.NAME);
    Node main = NodeUtil.createPath(packrunweb, "main", NodeTypes.Content.NAME);
    NodeUtil.createPath(main, "0", NodeTypes.ContentNode.NAME);
    NodeUtil.createPath(main, "00", NodeTypes.ContentNode.NAME);

    // WHEN
    executeUpdatesAsIfTheCurrentlyInstalledVersionWas(Version.parseVersion("1.2.2"));

    //THEN
    assertThat(packrunweb, hasNode("tour-finder"));
    List<Node> components = Lists.newArrayList(main.getNodes());
    assertThat(Collections2.transform(components, new ToNodeName()), contains(
        "0",
        "01",
        "00"
    ));
  }

  @Test
  public void updateFrom13Installs60DemoImages() throws Exception {
    // GIVEN
    NodeUtil.createPath(damSession.getRootNode(), "courses", NodeTypes.Folder.NAME);

    // WHEN
    executeUpdatesAsIfTheCurrentlyInstalledVersionWas(Version.parseVersion("1.3"));

    // THEN
    List<Node> coursesNodeList = Lists
        .newArrayList(damSession.getRootNode().getNode("courses").getNodes());
    assertThat(Collections2.transform(coursesNodeList, new ToNodeName()), hasItems(
        "ash-edmonds-441220-unsplash",
        "ruben-mishchuk-571314-unsplash",
        "simon-mumenthaler-199501-unsplash"
    ));
  }

  @Test
  public void updateFrom14InstallsNewImageMetadata() throws Exception {
    // GIVEN
    NodeUtil.createPath(damSession.getRootNode(), "courses", NodeTypes.Folder.NAME);

    // WHEN
    executeUpdatesAsIfTheCurrentlyInstalledVersionWas(Version.parseVersion("1.4"));

    // THEN
    Node ashEdmonds = damSession.getRootNode().getNode("courses/ash-edmonds-441220-unsplash");
    assertThat(ashEdmonds, hasProperty("coverage"));
  }

  @Test
  public void updateFrom14AndCheckContentTypeSupport() throws Exception {
    // GIVEN
    Node node = NodeUtil.createPath(configSession.getRootNode(), "/modules/courses/apps/courses",
        NodeTypes.ContentNode.NAME);
    node.setProperty("class", ConfiguredContentAppDescriptor.class.getName());

    Node nodeCategories = NodeUtil
        .createPath(configSession.getRootNode(), "/modules/courses/apps/tourCategories",
            NodeTypes.ContentNode.NAME);
    nodeCategories.setProperty("class", ConfiguredContentAppDescriptor.class.getName());

    // WHEN
    executeUpdatesAsIfTheCurrentlyInstalledVersionWas(Version.parseVersion("1.4"));

    // THEN
    MatcherAssert
        .assertThat(node, hasProperty("class", ConfiguredContentTypeAppDescriptor.class.getName()));
    MatcherAssert.assertThat(node, hasProperty("contentType", "tour"));
    MatcherAssert.assertThat(nodeCategories,
        hasProperty("class", ConfiguredContentTypeAppDescriptor.class.getName()));
    MatcherAssert.assertThat(nodeCategories, hasProperty("contentType", "tourCategory"));
  }

  private void setupBootstrapPages() throws RepositoryException {
    websiteSession.getRootNode().addNode("packrunweb", NodeTypes.Page.NAME);
    websiteSession.getRootNode().addNode("packrunweb/about", NodeTypes.Page.NAME);
    websiteSession.getRootNode().addNode("packrunweb/tour-type", NodeTypes.Page.NAME);
    websiteSession.getRootNode().addNode("packrunweb/destination", NodeTypes.Page.NAME);
    websiteSession.getRootNode().addNode("packrunweb/tour", NodeTypes.Page.NAME);
    websiteSession.getRootNode().addNode("packrunweb/about/careers", NodeTypes.Page.NAME);
    websiteSession.getRootNode().addNode("packrunweb/about/careers/main", NodeTypes.Area.NAME);
    websiteSession.getRootNode().addNode("packrunweb/about/careers/main/01", NodeTypes.Component.NAME);
    websiteSession.getRootNode().addNode("packrunweb/about/careers/main/06", NodeTypes.Component.NAME);
  }

  */
/**
   * This function is used to extract node name of a given node.
   *//*

  private static class ToNodeName implements Function<Node, String> {

    @Override
    public String apply(Node node) {
      try {
        return node.getName();
      } catch (RepositoryException e) {
        throw new RuntimeException(e);
      }
    }
  }

}
*/

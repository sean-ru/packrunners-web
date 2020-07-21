package org.packrunners.webapp.setup;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import info.magnolia.module.InstallContext;
import org.junit.Test;


/**
 * Tests for {@link FolderBootstrapTask}.
 */
public class FolderBootstrapTaskTest {

  private FolderBootstrapTask folderBootstrapTask;

  @Test
  public void makeSureOnlyFilesFromFolderAreAccepted() {
    // GIVEN
    final InstallContext installContext = mock(InstallContext.class);

    folderBootstrapTask = new FolderBootstrapTask("/test-folder");

    // WHEN
    // THEN
    assertThat(folderBootstrapTask.acceptResource(installContext, "/test-folder/some-resource.xml"),
        is(true));
    assertThat(
        folderBootstrapTask.acceptResource(installContext, "/other-folder/some-resource.xml"),
        is(false));

    // 'edge' cases
    assertThat(folderBootstrapTask.acceptResource(installContext, "/test-folder-resource.xml"),
        is(true));
    assertThat(folderBootstrapTask.acceptResource(installContext, "/test-folder-resource"),
        is(false));
  }

}
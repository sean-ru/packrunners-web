package org.packrunners.courses.model;

import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.model.RenderingModelImpl;
import java.util.List;
import javax.inject.Inject;
import javax.jcr.Node;
import org.packrunners.courses.model.definition.CourseCategoryTemplateDefinition;
import org.packrunners.courses.service.Category;
import org.packrunners.courses.service.Video;
import org.packrunners.courses.service.VideoServices;


/**
 * Model for retrieving videos by type or category.
 *
 * @param <RD> The {@link CourseCategoryTemplateDefinition} of the model.
 */
public class VideoListModel<RD extends CourseCategoryTemplateDefinition> extends
    RenderingModelImpl<RD> {

  private final VideoServices videoServices;

  @Inject
  public VideoListModel(Node content, RD definition, RenderingModel<?> parent,
      VideoServices videoServices) {
    super(content, definition, parent);

    this.videoServices = videoServices;
  }

  public Category getCategoryByName(String categoryName) {
    return videoServices.getCategoryByName(categoryName);
  }

  public List<Video> getVideosByCategory(String identifier) {
    return videoServices.getVideosByCategory(definition.getCategory(), identifier);
  }

  protected VideoServices getVideoServices() {
    return videoServices;
  }
}

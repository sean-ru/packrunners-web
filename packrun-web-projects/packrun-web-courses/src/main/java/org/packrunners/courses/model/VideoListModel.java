package org.packrunners.courses.model;

import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.model.RenderingModelImpl;
import org.packrunners.courses.model.definition.CourseCategoryTemplateDefinition;
import org.packrunners.courses.service.Category;
import org.packrunners.courses.service.Video;
import org.packrunners.courses.service.VideoServices;

import javax.inject.Inject;
import javax.jcr.Node;
import java.util.List;


/**
 * Model for retrieving courses by type or category.
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

  public Category getCategoryByUrl() {
    return videoServices.getCategoryByUrl();
  }

  public Category getCategoryByName(String categoryName) {
    return videoServices.getCategoryByName(categoryName);
  }

  public List<Video> getVideosByCourseName(String identifier) {
    return videoServices.getVideosByCourseName(definition.getCategory(), identifier);
  }

  protected VideoServices getVideoServices() {
    return videoServices;
  }
}

package org.packrunners.tutors.model;

import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.model.RenderingModelImpl;
import java.util.List;
import javax.inject.Inject;
import javax.jcr.Node;
import org.packrunners.tutors.service.Tutor;
import org.packrunners.tutors.service.TutorServices;
import org.packrunners.webapp.Category;
import org.packrunners.webapp.definition.CategoryTemplateDefinition;


/**
 * Model for retrieving courses by type or category.
 *
 * @param <RD> The {@link CategoryTemplateDefinition} of the model.
 */
public class TutorListModel<RD extends CategoryTemplateDefinition> extends
    RenderingModelImpl<RD> {

  private final TutorServices tutorServices;

  @Inject
  public TutorListModel(Node content, RD definition, RenderingModel<?> parent,
      TutorServices tutorServices) {
    super(content, definition, parent);

    this.tutorServices = tutorServices;
  }

  public Category getCategoryByUrl() {
    return tutorServices.getCategoryByUrl();
  }

  public Category getCategoryByName(String categoryName) {
    return tutorServices.getCategoryByName(categoryName);
  }

  public List<Tutor> getTutorsByCategory(String identifier) {
    return tutorServices.getTutorsBySchool(definition.getCategory(), identifier);
  }

  protected TutorServices getTutorServices() {
    return tutorServices;
  }
}
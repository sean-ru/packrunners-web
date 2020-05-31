package org.packrunners.courses.model;

import info.magnolia.context.MgnlContext;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.module.categorization.CategorizationModule;
import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.model.RenderingModelImpl;
import info.magnolia.rendering.template.RenderableDefinition;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import lombok.extern.slf4j.Slf4j;
import org.packrunners.courses.CoursesModule;
import org.packrunners.courses.service.Category;
import org.packrunners.courses.service.Course;
import org.packrunners.courses.service.CourseServices;


/**
 * Model for displaying the 'featured' courseTypes on the home page. reads the 'courseTypes'
 * property from the content and resolves the categories.
 *
 * @param <RD> Renderable definition.
 */
@Slf4j
public class CourseTeaserModel<RD extends RenderableDefinition> extends RenderingModelImpl<RD> {

  private final CourseServices courseServices;

  @Inject
  public CourseTeaserModel(Node content, RD definition, RenderingModel<?> parent,
      CourseServices courseServices) {
    super(content, definition, parent);
    this.courseServices = courseServices;
  }

  public List<Category> getCourses() {
    final List<Category> categories = new LinkedList<Category>();

    final Object object = PropertyUtil
        .getPropertyValueObject(content, Course.PROPERTY_NAME_COURSE_TYPE);
    if (object instanceof List) {
      List<String> results = (List<String>) object;
      for (String identifier : results) {
        try {
          final Category category = getCategory(identifier);
          if (category != null) {
            categories.add(category);
          }
        } catch (RepositoryException e) {
          log.error("Could not retrieve linked course.", e);
        }
      }
    }

    return categories;
  }

  private Category getCategory(String identifier) throws RepositoryException {
    final Session session = MgnlContext
        .getJCRSession(CategorizationModule.CATEGORIZATION_WORKSPACE);
    final Node categoryNode = session.getNodeByIdentifier(identifier);

    final Category category = courseServices.marshallCategoryNode(categoryNode);
    if (category != null) {
      String link = courseServices.getCategoryLink(content, categoryNode.getName(),
          CoursesModule.TEMPLATE_SUB_TYPE_COURSE_CATEGORY_OVERVIEW);
      category.setLink(link);
    }

    return category;
  }


}

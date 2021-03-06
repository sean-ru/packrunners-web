package org.packrunners.courses.model;

import info.magnolia.context.MgnlContext;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.module.categorization.CategorizationModule;
import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.model.RenderingModelImpl;
import info.magnolia.rendering.template.RenderableDefinition;
import lombok.extern.slf4j.Slf4j;
import org.packrunners.categories.Category;
import org.packrunners.courses.CoursesModule;
import org.packrunners.courses.service.CourseServices;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.LinkedList;
import java.util.List;


/**
 * Model for displaying the 'featured' resourceTypes on the home page. reads the 'resourceTypes'
 * property from the content and resolves the categories.
 *
 * @param <RD> Renderable definition.
 */
@Slf4j
public class ResourceTeaserModel<RD extends RenderableDefinition> extends RenderingModelImpl<RD> {

    private final CourseServices courseServices;

    @Inject
    public ResourceTeaserModel(Node content, RD definition, RenderingModel<?> parent,
                               CourseServices courseServices) {
        super(content, definition, parent);
        this.courseServices = courseServices;
    }

    public List<Category> getResourceTypes() {
        return getCategoriesByName(Category.PROPERTY_NAME_RESOURCE_TYPES);
    }

    private List<Category> getCategoriesByName(String categoryName) {
        final List<Category> categories = new LinkedList<Category>();

        final Object object = PropertyUtil
                .getPropertyValueObject(content, categoryName);
        if (object instanceof List) {
            List<String> results = (List<String>) object;
            for (String identifier : results) {
                try {
                    final Category category = getCategory(identifier);
                    if (category != null) {
                        categories.add(category);
                    }
                } catch (RepositoryException e) {
                    log.error("Could not retrieve categories.", e);
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
                    CoursesModule.TEMPLATE_SUB_TYPE_RESOURCE_TYPE_OVERVIEW);
            category.setLink(link);
        }

        return category;
    }


}

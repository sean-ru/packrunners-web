package org.packrunners.videos.model;

import info.magnolia.context.MgnlContext;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.module.categorization.CategorizationModule;
import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.model.RenderingModelImpl;
import info.magnolia.rendering.template.RenderableDefinition;
import lombok.extern.slf4j.Slf4j;
import org.packrunners.categories.Category;
import org.packrunners.videos.VideosModule;
import org.packrunners.videos.service.VideoServices;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.LinkedList;
import java.util.List;


/**
 * Model for displaying the 'featured' courseTypes on the home page. reads the 'courseTypes'
 * property from the content and resolves the categories.
 *
 * @param <RD> Renderable definition.
 */
@Slf4j
public class VideoTeaserModel<RD extends RenderableDefinition> extends RenderingModelImpl<RD> {

    private final VideoServices courseServices;

    @Inject
    public VideoTeaserModel(Node content, RD definition, RenderingModel<?> parent,
                            VideoServices courseServices) {
        super(content, definition, parent);
        this.courseServices = courseServices;
    }

    public List<Category> getCoursess() {
        return getCategoriesByName(Category.PROPERTY_NAME_COURSES);
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
                    VideosModule.TEMPLATE_SUB_TYPE_COURSE_TYPE_OVERVIEW);
            category.setLink(link);
        }

        return category;
    }

}

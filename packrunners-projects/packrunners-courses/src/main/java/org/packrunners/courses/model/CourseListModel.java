package org.packrunners.courses.model;

import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.model.RenderingModelImpl;
import org.packrunners.categories.Category;
import org.packrunners.categories.definition.CategoryTemplateDefinition;
import org.packrunners.courses.service.Course;
import org.packrunners.courses.service.CourseServices;

import javax.inject.Inject;
import javax.jcr.Node;
import java.util.List;


/**
 * Model for retrieving courses by type or category.
 *
 * @param <RD> The {@link CategoryTemplateDefinition} of the model.
 */
public class CourseListModel<RD extends CategoryTemplateDefinition> extends
        RenderingModelImpl<RD> {

    private final CourseServices courseServices;

    @Inject
    public CourseListModel(Node content, RD definition, RenderingModel<?> parent,
                           CourseServices courseServices) {
        super(content, definition, parent);

        this.courseServices = courseServices;
    }

    public Category getCategoryByUrl() {
        return courseServices.getCategoryByUrl();
    }

    public Category getCategoryByName(String categoryName) {
        return courseServices.getCategoryByName(categoryName);
    }

    public List<Course> getCoursesByCategory(String identifier) {
        return courseServices.getCoursesByCategory(definition.getCategory(), identifier);
    }

    protected CourseServices getCourseServices() {
        return courseServices;
    }
}

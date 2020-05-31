package org.packrunners.courses.model.definition;

import info.magnolia.rendering.template.configured.ConfiguredTemplateDefinition;
import lombok.Data;


/**
 * Template definition for configuring the course category, e.g. courseTypes.
 */
@Data
public class CourseCategoryTemplateDefinition extends ConfiguredTemplateDefinition {

    private String category;
}

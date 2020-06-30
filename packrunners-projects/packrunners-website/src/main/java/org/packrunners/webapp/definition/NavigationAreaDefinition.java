package org.packrunners.webapp.definition;

import info.magnolia.rendering.template.configured.ConfiguredAreaDefinition;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.packrunners.webapp.user.UserLinksResolver;


@Data
public class NavigationAreaDefinition extends ConfiguredAreaDefinition {

  private List<UserLinksResolver> userLinksResolvers = new ArrayList<>();

}

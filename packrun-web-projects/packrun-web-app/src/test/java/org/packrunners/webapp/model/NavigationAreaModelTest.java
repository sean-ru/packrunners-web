package org.packrunners.webapp.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.template.AreaDefinition;
import info.magnolia.templating.functions.TemplatingFunctions;
import javax.jcr.Node;
import org.junit.Test;


public class NavigationAreaModelTest {

  private final NavigationAreaModel navigationAreaModel = new NavigationAreaModel(mock(Node.class),
      mock(AreaDefinition.class), mock(RenderingModel.class), mock(TemplatingFunctions.class));

  @Test
  public void makeSureReturnedLocalesTakeCountryIntoAccount() {
    // GIVEN
    // WHEN
    // THEN
    assertThat(navigationAreaModel.getLocale("de").getLanguage(), is("de"));
    assertThat(navigationAreaModel.getLocale("de").getCountry(), emptyString());
    assertThat(navigationAreaModel.getLocale("de_CH").getLanguage(), is("de"));
    assertThat(navigationAreaModel.getLocale("de_CH").getCountry(), is("CH"));
  }
}
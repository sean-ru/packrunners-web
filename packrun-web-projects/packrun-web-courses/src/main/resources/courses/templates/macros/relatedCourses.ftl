[#-- Displays a row of featured courses. --]
[#macro relatedCourses categoryName courses]

    [#include "/packrunweb/templates/macros/imageResponsive.ftl"]
    [#include "/packrunweb/templates/macros/editorAlert.ftl" /]
    [#include "/courses/templates/macros/courseTypeIcon.ftl" /]

    [#if courses?has_content || cmsfn.editMode]
    <div class="container after-category-header">

        [#-- get(key, args[]) requires the second parameter to be a sequence --]
        <h2>${i18n.get('course.featured', [categoryName])}</h2>
        <div class="row featured-card-row">
            [#list courses as course]
                [#assign name = course.name!course.@name /]
                [#assign description = course.description!"" /]
                [#assign courseLink = course.link /]
                [#assign imageHtml][@responsiveImageTravel course.image "" "" "featured-image" "data-ratio='1.33'" true /][/#assign]

                <a class="featured-card-anchor" href="${courseLink!}">
                    <div class="col-md-4 featured-card card">
                        ${imageHtml}
                        <div class="featured-card-shader"></div>
                        <div class="featured-blaze"></div>

                        <div class="featured-blaze-text">${i18n['course.featured.card']}</div>

                        <h3>${name!}</h3>
                        <div class="category-icons">
                            [#list course.courseTypes as courseType]
                                <div class="category-icon">
                                    [@courseTypeIcon courseType.icon courseType.name "" /]
                                </div>
                            [/#list]
                        </div>

                        <div class="featured-card-content">
                            [#if description?has_content]
                                <p><span class="description">${description!}</span></p>
                            [/#if]
                        </div>
                        <div class="card-button">
                            <div class="btn btn-primary">${i18n['course.view']}</div>
                        </div>
                    </div>
                </a>
            [/#list]
        </div>

        [@editorAlert i18n.get('note.for.editors.featured', [categoryName!""]) /]

    </div>

    <script>
        jQuery(".featured-image").objectFitCoverSimple();
    </script>
    [/#if]

[/#macro]

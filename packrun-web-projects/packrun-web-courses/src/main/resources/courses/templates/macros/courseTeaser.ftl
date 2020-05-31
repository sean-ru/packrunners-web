[#macro courseTeaser course additionalWrapperClass="col-md-6"]

    [#include "/packrunweb/templates/macros/imageResponsive.ftl"]
    [#include "/courses/templates/macros/courseTypeIcon.ftl" /]
    [#assign imageHtml][@responsiveImageTravel course.image "" "" "course-card-image" "data-ratio='1.33'" true /][/#assign]

    <!-- Course Teaser -->
    <div class="${additionalWrapperClass} course-card card">
        <div class="course-card-background">
        ${imageHtml}
        </div>
        <a class="course-card-anchor" href="${course.link!}">
            <div class="course-card-content-shader"></div>
            <div class="course-card-content">
                <h3>${course.name!}</h3>
                <div class="category-icons">
                    [#list course.courseTypes as courseType]
                        <div class="category-icon absolute-center-container">
                            [@courseTypeIcon courseType.icon courseType.name "" /]
                        </div>
                    [/#list]
                </div>
                <div class="card-button">
                    <div class="btn btn-primary">${i18n['course.view']}</div>
                </div>
            </div>
        </a>
    </div>

[/#macro]

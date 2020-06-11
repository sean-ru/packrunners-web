[#macro tutorTeaser tutor additionalWrapperClass="col-md-6"]

    [#include "/packrunweb/templates/macros/imageResponsive.ftl"]
    [#include "/courses/templates/macros/courseTypeIcon.ftl" /]
    [#assign imageHtml][@responsiveImageMain tutor.photo "" "" "course-card-image" "data-ratio='0.67'" true /][/#assign]

    <!-- Tutor Teaser -->
    <div class="${additionalWrapperClass} course-card card">
        <div class="course-card-background">
        ${imageHtml}
        </div>
        <a class="course-card-anchor" href="${tutor.link!}">
            <div class="course-card-content-shader"></div>
            <div class="course-card-content">
                <h3>${tutor.name!}</h3>
                <div class="card-button">
                    <div class="btn btn-primary">${i18n['tutor.view']}</div>
                </div>
            </div>
        </a>
    </div>

[/#macro]

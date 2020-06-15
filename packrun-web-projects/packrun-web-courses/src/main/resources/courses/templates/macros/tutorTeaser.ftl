[#macro tutorTeaser tutor additionalWrapperClass="col-md-12"]

    [#include "/packrunweb/templates/macros/imageResponsive.ftl"]
    [#include "/courses/templates/macros/courseTypeIcon.ftl" /]
    [#assign imageHtml][@responsiveImageMain tutor.photo "" "" "course-card-image" "data-ratio='1.33'" true /][/#assign]

    <!-- Tutor Teaser -->
    <div class="${additionalWrapperClass}">

       <div class="col-md-6 course-card card">
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

       <div class="col-md-6">
          <div><h3>Personal Profile<h3></div>
          <div>
            ${tutor.profile!}
          </div>
       </div>

    </div>

[/#macro]

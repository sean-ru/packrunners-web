[#macro tutorTeaser tutor additionalWrapperClass="col-md-12"]

    [#include "/packrunweb/templates/macros/imageResponsive.ftl"]
    [#include "/courses/templates/macros/courseTypeIcon.ftl" /]
    [#assign imageHtml][@responsiveImagePortrait tutor.photo "" "" "course-card-image" "data-ratio='0.75'" true /][/#assign]

    <!-- Tutor Teaser -->
    <div class="${additionalWrapperClass}">

       <div class="col-md-4 course-card card">
          <div class="course-card-background">
              ${imageHtml}
          </div>
          <a class="course-card-anchor" href="${tutor.link!}">
              <div class="course-card-content">
                  <div class="card-button">
                      <div class="btn btn-primary">${i18n['tutor.view']}</div>
                  </div>
              </div>
          </a>
       </div>

       <div class="col-md-8">
          <div><h3>${tutor.name!}<h3></div>
          <div>
            [#if tutor.profile?has_content]
                ${tutor.profile!}
            [/#if]
          </div>
       </div>

    </div>

[/#macro]

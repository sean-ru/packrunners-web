[#-------------- ASSIGNMENTS --------------]
[#include "/packrunweb/templates/macros/imageResponsive.ftl"]
[#include "/courses/templates/macros/courseTypeIcon.ftl" /]

[#assign course = model.course]
[#assign asset = course.image!]
[#assign dc = damfn.getAssetMap(asset).metadata.dc!]
[#if asset?exists]
    [#assign assetCredit = dc.contributor?first!]
    [#assign imageHtml][@responsiveImageLandscape asset "" "" "header-image" "data-ratio='1.33'" true /][/#assign]
[/#if]

[#assign relatedCourseTypes = course.courseTypes!]
[#assign relatedSchools = course.schools!]
[#assign relatedCourseNumbers = course.courseNumbers!]
[#assign studyGuides = model.getStudyGuidesByCourseNumber(course.courseNumbers)]
[#assign videos = model.getVideosByCourseNumber(course.courseNumbers)]
[#assign quizzes = []]

[#-------------- RENDERING --------------]
<!-- Course Detail -->
<div class="product-header">
    <div class="navbar-spacer"></div>
    <div class="header-wrapper">
        ${imageHtml}
        <div class="lead-caption">
            <h1>${course.name}</h1>
            <div class="category-icons">
                [#list relatedCourseTypes as courseType]
                    <div class="category-icon absolute-center-container">
                        <a href="${coursefn.getCourseTypeLink(content, courseType.nodeName)!'#'}">
                            [@courseTypeIcon courseType.icon courseType.name "absolute-center" /]
                        </a>
                    </div>
                [/#list]
            </div>
        </div>
    </div>
</div>

<script>
    jQuery(".header-image").objectFitCoverSimple();
</script>

<div class="product-header-spacer"></div>
<div class="container after-product-header">

    <div class="row product-info product-summary">

        <div class="product-location">
            [#list relatedSchools as school]
                 <div class="category-icon absolute-center-container">
                        <a href="${coursefn.getSchoolLink(content, school.nodeName)!'#'}">
                            [@courseTypeIcon school.icon school.name "absolute-center" /]
                        </a>
                 </div>
            [/#list]
        </div>

        <div class="product-properties col-xs-10 col-xs-push-1">
            <div class="product-property">
                <div class="property-label">${i18n.get('course.property.name')}</div>
                <div class="property-value">${course.name!}</div>
            </div>
            <div class="product-property">
                <div class="property-label">${i18n.get('course.property.courseNumber')}</div>
                [#list relatedCourseNumbers as courseNumber]
                <div class="property-value">${courseNumber.name!}</div>
                [/#list]
            </div>
            <div class="product-property">
                <div class="property-label">${i18n.get('course.property.duration')}</div>
                <div class="property-value">${course.duration!} Semester(s)</div>
            </div>
            <div class="product-property">
                <div class="property-label">${i18n.get('course.property.credit')}</div>
                <div class="property-value">${course.credit!}</div>
            </div>
            <div class="product-property">
                <div class="property-label">${i18n.get('course.property.courseTypes')}</div>
                  <div class="property-value product-categories">
                    <div class="category-icons">
                        [#list relatedCourseTypes as courseType]
                            <a href="${coursefn.getCourseTypeLink(content, courseType.nodeName)!'#'}">
                                <div class="category-icon absolute-center-container">
                                    [@courseTypeIcon courseType.icon courseType.name  "absolute-center" /]
                                </div></a>
                        [/#list]
                    </div>
                </div>
            </div>
        </div>

        <div class="product-action">
        </div>
    </div>

    [#if studyGuides?has_content]
    <div class="row product-info">
        <div class="col-xs-10 col-xs-push-1 product-property">
            <p class="summary">${i18n.get('course.property.studyGuides')}</p>
            <hr style="margin-top:1px;"/>
            [#list studyGuides as studyGuide]
               <div class="body">${studyGuide.name}</div>
            [/#list]
        </div>
    </div>
    [/#if]

    [#if videos?has_content]
    <div class="row product-info">
        <div class="col-xs-10 col-xs-push-1 product-property">
            <p class="summary">${i18n.get('course.property.videos')}</p>
            <hr style="margin-top:1px;"/>
            [#list videos as video]
                <a href="${video.link!}">
                    <div class="body">${video.name}</div>
                </a>
            [/#list]
        </div>
    </div>
    [/#if]

    [#if quizzes?has_content]
    <div class="row product-info">
        <div class="col-xs-10 col-xs-push-1 product-property">
            <p class="summary">${i18n.get('course.property.quizzes')}</p>
            <hr style="margin-top:1px;"/>
            [#list quizzes as quiz]
                <div class="body">${quiz.name}}</div>
            [/#list]
        </div>
    </div>
    [/#if]

    [#if assetCredit?has_content]
        <div class="row product-info ">
            <div class="col-xs-10 col-xs-push-1 product-image-credit">
                <hr style="margin-top:0px;">
                <div class="body">${i18n['credit.leadImage']} ${assetCredit}
                    [#assign license=asset.copyright!]
                    [#if license?has_content]
                        &nbsp;<a target="_blank" href="https://creativecommons.org/licenses/${license}">${license}</a>
                    [/#if]
                </div>
            </div>
        </div>
    [/#if]

</div>

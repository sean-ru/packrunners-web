[#-------------- ASSIGNMENTS --------------]
[#include "/packrunweb/templates/macros/imageResponsive.ftl"]
[#include "/courses/templates/macros/courseTypeIcon.ftl" /]
[#include "/courses/templates/macros/sampleCourseText.ftl" /]

[#assign course = model.course]
[#assign asset = course.image!]
[#assign dc = damfn.getAssetMap(asset).metadata.dc!]
[#if asset?exists]
    [#assign assetCredit = dc.contributor?first!]
    [#assign imageHtml][@responsiveImageTravel asset "" "" "header-image" "data-ratio='1.33'" true /][/#assign]
[/#if]

[#if def.parameters.showSchools?? && def.parameters.showSchools == false]
    [#assign showSchools = false]
[#else]
    [#assign showSchools = true]
    [#assign relatedSchools = course.schools!]
[/#if]

[#if def.parameters.showCourseTypes?? && def.parameters.showCourseTypes == false]
    [#assign showCourseTypes = false]
[#else]
    [#assign showCourseTypes = true]
    [#assign relatedCourseTypes = course.courseTypes!]
[/#if]


[#-------------- RENDERING --------------]
<!-- CourseDetail -->
<div class="product-header">
    <div class="navbar-spacer"></div>
    <div class="header-wrapper">
        ${imageHtml}
        <div class="lead-caption">
            <h1>${course.name}</h1>

            [#if showCourseTypes]
                <div class="category-icons">
                    [#list relatedCourseTypes as courseType]
                        <div class="category-icon absolute-center-container">
                            <a href="${coursefn.getCourseTypeLink(content, courseType.nodeName)!'#'}">
                                [@courseTypeIcon courseType.icon courseType.name "absolute-center" /]
                            </a>
                        </div>
                    [/#list]
                </div>
            [/#if]
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
            [#if showDestinations]
                [#list relatedDestinations as destination]
                    <div class="category-icon absolute-center-container">
                        <a href="${coursefn.getDestinationLink(content, destination.nodeName)!'#'}">
                            [@courseTypeIcon destination.icon destination.name "absolute-center" /]
                        </a>
                    </div>
                [/#list]
            [/#if]
            </div>

        <div class="product-properties col-xs-10 col-xs-push-1">
            <div class="product-property">
                <div class="property-label">${i18n.get('course.property.startCity')}</div>
                <div class="property-value">${course.location!}</div>
            </div>
            <div class="product-property">
                <div class="property-label">${i18n.get('course.property.duration')}</div>
                <div class="property-value">${i18n.get('course.duration', [course.duration!])}</div>
            </div>
            <div class="product-property">
                <div class="property-label">${i18n.get('course.property.operator')}</div>
                <div class="property-value">${course.author!}</div>
            </div>
            <div class="product-property ">
            [#if showCourseTypes]
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
            [/#if]
            </div>

        </div>

        <div class="product-action">
        [#assign bookNode = cmsfn.contentByPath("/packrunweb/book-course")]
            <form action="${cmsfn.link(bookNode)}">
                <input type="hidden" name="location" value="${course.location!}">
                <input class="btn btn-primary btn-lg book-button" type="submit" value="${i18n['course.book']}">
            </form>
        </div>
    </div>

    <div class="row product-info">
        <div class="col-xs-10 col-xs-push-1 product-property">
            <p class="summary">${course.description}</p>

            [#if course.body?has_content]
                <div class="body">${course.body!}</div>
            [/#if]
            [@cms.area name="summary"/]
        </div>
    </div>

    <!-- Additional sample text for the purposes of demonstrating what a full page could look like -->
    <div class="row product-info">
        <div class="col-xs-10 col-xs-push-1 product-property">
            <hr style="margin-top:0px;"/>
            <div class="body">[@sampleCourseText /]</div>
        </div>
    </div>

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

<!-- Book Course Dialog -->
<div class="modal fade book-course-not-implemented">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="${i18n['course.book.notImplementedDialog.close']}"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">${i18n['course.book.notImplementedDialog.title']}</h4>
            </div>
            <div class="modal-body">
                <p>${i18n['course.book.notImplementedDialog.body']}</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">${i18n['course.book.notImplementedDialog.close']}</button>
            </div>
        </div>
    </div>
</div>

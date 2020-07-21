[#-------------- ASSIGNMENTS --------------]
[#include "/courses/templates/macros/image.ftl" /]
[#include "/courses/templates/macros/courseTypeIcon.ftl" /]

[#assign courses = model.courses]
[#assign showCourseTypes = content.showCourseTypes!true]

[#-------------- RENDERING --------------]

<!-- Course Carousel -->
<div id="myCarousel" class="carousel slide">
    <!-- Indicators -->
    <ol class="carousel-indicators">
    [#list courses as course]
        [#assign activeClass=""]
        [#if course_index == 0 ]
            [#assign activeClass="active"]
        [/#if]
        <li data-target="#myCarousel" data-slide-to="${course_index}" class="${activeClass}"></li>
    [/#list]
    </ol>

    <!-- Actual Carousel List -->
    <div class="carousel-inner" role="listbox">
    [#list courses as course]
        [#assign activeClass=""]
        [#if course_index == 0 ]
            [#assign activeClass="active"]
        [/#if]
        [#assign rendition = damfn.getRendition(course.image, "1366")!]
        <div class="item ${activeClass}"${backgroundImage(rendition)}>
            <div class="container">
                <a class="carousel-link" href="${course.link!}">
                    <div class="carousel-caption">
                        <h1>${course.name!}</h1>
                        [#if showCourseTypes]
                            <div class="category-icons">
                                [#list course.courseTypes as courseType]
                                    <div class="category-icon absolute-center-container">
                                        [@courseTypeIcon courseType.icon courseType.name "" /]
                                    </div>
                                [/#list]
                            </div>
                        [/#if]
                        <button class="btn btn-lg btn-primary">${i18n['course.view']}</button>
                    </div>
                </a>
            </div>
        </div>
    [/#list]
    </div>

    <!-- Carousel Controls -->
    <a class="left carousel-control" href="#myCarousel" data-slide="prev">
        <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
        <span class="sr-only">${i18n['course.previous']}</span>
    </a>
    <a class="right carousel-control" href="#myCarousel" data-slide="next">
        <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
        <span class="sr-only">${i18n['course.next']}</span>
    </a>
</div>

[#-------------- ASSIGNMENTS --------------]
[#include "/courses/templates/macros/courseTypeIcon.ftl" /]

[#assign courseTypes = model.courses]


[#-------------- RENDERING --------------]
<!-- CourseType Teaser Row -->
<div class="container category-card-row after-lead-image">
    <div class="row">

        <h2>${content.title!}</h2>
        <p>${content.body!}</p>

        [#list courseTypes as courseType]
            <a class="category-card-anchor" href="${courseType.link!'#'}">
                <div class="col-md-4 category-card">
                    <div class="category-icon absolute-center-container">
                        [@courseTypeIcon courseType.icon courseType.name "absolute-center" /]
                    </div>
                    <h3>${courseType.name!}</h3>
                    <div class="category-card-content">
                        <p>${courseType.description!}</p>
                    </div>
                </div>
            </a>
        [/#list]

    </div>
</div>

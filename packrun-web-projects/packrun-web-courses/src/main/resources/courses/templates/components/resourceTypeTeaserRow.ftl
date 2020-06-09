[#-------------- ASSIGNMENTS --------------]
[#include "/courses/templates/macros/courseTypeIcon.ftl" /]

[#assign resourceTypes = model.resourceTypes]


[#-------------- RENDERING --------------]
<!-- Resource Type Teaser Row -->
<div class="container category-card-row after-lead-image">
    <div class="row">

        <h2>${content.title!}</h2>
        <p>${content.body!}</p>

        [#list resourceTypes as resourceType]
            <a class="category-card-anchor" href="${resourceType.link!'#'}">
                <div class="col-md-4 category-card">
                    <div class="category-icon absolute-center-container">
                        [@courseTypeIcon resourceType.icon resourceType.name "absolute-center" /]
                    </div>
                    <h3>${resourceType.name!}</h3>
                    <div class="category-card-content">
                        <p>${resourceType.description!}</p>
                    </div>
                </div>
            </a>
        [/#list]

    </div>
</div>

[#-------------- ASSIGNMENTS --------------]
[#assign category = model.categoryByUrl!]


[#-------------- RENDERING --------------]
[#if category?has_content]
    <!-- Course List - Featured Row -->
    [#include "/courses/templates/macros/relatedCourses.ftl"]
    [#assign courses = model.getRelatedCoursesByCategory(category.identifier)]
    [@relatedCourses category.name courses /]

    <div class="container category-overview">
        [#if category.body?has_content]
            <div class="category-body">
                ${category.body}
            </div>
        [/#if]
    </div>
[/#if]

[#-------------- ASSIGNMENTS --------------]
[#include "/courses/templates/macros/relatedCourses.ftl"]

[#assign relatedCategories = model.relatedCategoriesByParameter]


[#-------------- RENDERING --------------]
<!-- Course Detail - Related Courses -->
[#list relatedCategories as category]

    [#assign courses = model.getRelatedCoursesByCategory(category.identifier)]
    [@relatedCourses category.name courses /]

[/#list]

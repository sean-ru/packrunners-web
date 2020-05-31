[#-------------- ASSIGNMENTS --------------]
[#include "/courses/templates/macros/courseTeaser.ftl"]
[#include "/packrunweb/templates/macros/editorAlert.ftl" /]

[#if def.parameters.courseType??]
    [#assign category = model.getCategoryByName(def.parameters.courseType)]
[#else]
    [#assign category = model.getCategoryByUrl()!]
[/#if]

[#assign courses = model.getCoursesByCategory(category.identifier)]
[#assign title = content.title!i18n.get('course.all.courses', [category.name!""])!]

[#-------------- RENDERING --------------]
<!-- Course List -->
<div class="container course-list">

    <h2>${title}</h2>

    <div class="row">
        [#list courses as course]
            [@courseTeaser course /]
        [/#list]
    </div>

    [@editorAlert i18n.get('note.for.editors.assign.category', [category.name!""]) /]
</div>

<script>
    jQuery(".course-card-image").objectFitCoverSimple();
</script>

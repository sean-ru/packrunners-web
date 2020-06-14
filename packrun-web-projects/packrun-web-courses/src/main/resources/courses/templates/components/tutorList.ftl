[#-------------- ASSIGNMENTS --------------]
[#include "/courses/templates/macros/tutorTeaser.ftl"]
[#include "/packrunweb/templates/macros/editorAlert.ftl" /]

[#if def.parameters.school??]
    [#assign category = model.getCategoryByName(def.parameters.school)]
[#else]
    [#assign category = model.getCategoryByUrl()!]
[/#if]

[#assign tutors = model.getTutorsByCategory(category.identifier)]
[#assign title = content.title!i18n.get('tutor.all.tutors', [category.name!""])!]

[#-------------- RENDERING --------------]
<!-- Tutor List -->
<div class="container course-list">

    <h2>${title}</h2>

    <div class="row">
        [#list tutors as tutor]
            [@tutorTeaser tutor /]
        [/#list]
    </div>

    [@editorAlert i18n.get('note.for.editors.assign.category', [category.name!""]) /]
</div>

<script>
    jQuery(".course-card-image").objectFitCoverSimple();
</script>

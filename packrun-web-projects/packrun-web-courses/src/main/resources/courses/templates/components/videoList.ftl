[#-------------- ASSIGNMENTS --------------]
[#include "/packrunweb/templates/macros/editorAlert.ftl" /]

[#if def.parameters.school??]
    [#assign category = model.getCategoryByName(def.parameters.courseName)]
[#else]
    [#assign category = model.getCategoryByUrl()!]
[/#if]

[#assign videos = model.getVideosByCourseName(category.identifier)]
[#assign title = content.title!i18n.get('video.all.videos', [category.name!""])!]

[#-------------- RENDERING --------------]
<!-- video List -->
<div class="container course-list">

    <h2>${title}</h2>

    <div class="row">
        [#list videos as video]
                ${video.name!}</h3>
            </a>
        [/#list]
    </div>

    [@editorAlert i18n.get('note.for.editors.assign.category', [category.name!""]) /]
</div>

<script>
    jQuery(".course-card-image").objectFitCoverSimple();
</script>

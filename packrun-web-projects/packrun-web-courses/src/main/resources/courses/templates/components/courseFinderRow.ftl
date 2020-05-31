[#-------------- ASSIGNMENTS --------------]
[#if content.courseFinder??]
    [#assign courseFinderLink = cmsfn.link(cmsfn.contentById(content.courseFinder))]
[/#if]


[#-------------- RENDERING --------------]
<!-- Course Finder -->
<div class="container">
    <div class="row">
        <div class="finder-home col-md-12">
            <form action="${courseFinderLink!'/'}" method="get"
                onsubmit="location.href=this.action+'#!/?q='+this.searchQuery.value+'&duration='+this.duration.value+'&courseTypes='+this.courseTypes.value; return false">
            <div class="finder-home-segment-icon"><img src="${ctx.contextPath}/.resources/courses/webresources/img/map_red.png"></div>
            <div class="finder-home-segment" style="flex:1; min-width:200px">
                <div>${i18n['courseFinder.title']}</div>
                <input class="finder-search-home" placeholder="${i18n['courseFinder.search.placeholder']}" type="text" name="searchQuery">
            </div>
            <div class="finder-home-segment">
                <div>${i18n['courseFinderRow.duration']}</div>
                <select name="duration">
                    <option value="">${i18n['courseFinderRow.duration.placeholder']}</option>
                    <option value="2">${i18n['courseFinder.duration.options.2-days']}</option>
                    <option value="7">${i18n['courseFinder.duration.options.7-days']}</option>
                    <option value="14">${i18n['courseFinder.duration.options.14-days']}</option>
                    <option value="21">${i18n['courseFinder.duration.options.21-days']}</option>
                </select>
            </div>
            <div class="finder-home-segment">
                <div>${i18n['courseFinderRow.type']}</div>
                <select name="courseTypes">
                    <option value="">${i18n['courseFinderRow.type.placeholder']}</option>
                    [#assign allCourseTypes = navfn.navItemsFromApp("category", "/course-types", "mgnl:category")]
                    [#list allCourseTypes as courseType]
                       <option value="${courseType.@uuid}">${courseType['displayName_' + cmsfn.language()]!courseType.displayName}</option>
                    [/#list]
                </select>
            </div>
            <div class="finder-home-segment-icon">
                <button type="submit" class="btn btn-primary">${i18n['courseFinderRow.submit']}</button>
            </div>
            </form>
        </div>
    </div>
</div>

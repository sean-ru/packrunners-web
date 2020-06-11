<div class="finder-background" style="background-image: url(${ctx.contextPath}/.resources/courses/webresources/img/course-finder-background-ross-parmly-25230.jpg);"></div>

<div class="finder-container" ng-app="CourseFinder">
    <div ng-view>
    </div>
</div>

<script type="text/javascript">

[#assign keys = ["courseFinder.search.placeholder",
                 "courseFinder.search.resultsFound",
                 "courseFinder.search.noResults1",
                 "courseFinder.search.noResults2",
                 "courseFinder.search.noResults3",
                 "courseFinder.filter.resource",
                 "courseFinder.filter.tutor",
                 "courseFinder.filter.type",
                 "courseFinder.resource.options.studyGuide",
                 "courseFinder.resource.options.video",
                 "courseFinder.resource.options.quiz",
                 "course.view",
                 "courseFinder.title"] ]

var translations = {
[#list keys as key]
    "${key}" : "${i18n[key]}",
[/#list]
}

CourseFinder.init({ contextPath: "${ctx.contextPath}", restBase: "${ctx.contextPath}/.rest/delivery", language: "${cmsfn.language()}", i18n: translations });

</script>

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
                 "courseFinder.filter.duration",
                 "courseFinder.filter.destination",
                 "courseFinder.filter.type",
                 "courseFinder.duration.options.2-days",
                 "courseFinder.duration.options.7-days",
                 "courseFinder.duration.options.14-days",
                 "courseFinder.duration.options.21-days",
                 "course.view",
                 "courseFinder.title"] ]

var translations = {
[#list keys as key]
    "${key}" : "${i18n[key]}",
[/#list]
}

CourseFinder.init({ contextPath: "${ctx.contextPath}", restBase: "${ctx.contextPath}/.rest/delivery", language: "${cmsfn.language()}", i18n: translations });

</script>

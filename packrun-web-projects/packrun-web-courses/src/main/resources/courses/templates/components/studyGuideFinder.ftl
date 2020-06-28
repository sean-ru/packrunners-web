<div class="finder-background" style="background-image: url(${ctx.contextPath}/.resources/courses/webresources/img/course-finder-background-ross-parmly-25230.jpg);"></div>

<div class="finder-container" ng-app="StudyGuideFinder">
    <div ng-view>
    </div>
</div>

<script type="text/javascript">

[#assign keys = ["studyGuideFinder.search.placeholder",
                 "studyGuideFinder.search.resultsFound",
                 "studyGuideFinder.search.noResults1",
                 "studyGuideFinder.search.noResults2",
                 "studyGuideFinder.search.noResults3",
                 "studyGuideFinder.filter.courseType",
                 "studyGuide.view",
                 "studyGuideFinder.title"] ]

var translations = {
[#list keys as key]
    "${key}" : "${i18n[key]}",
[/#list]
}

StudyGuideFinder.init({ contextPath: "${ctx.contextPath}", restBase: "${ctx.contextPath}/.rest/delivery", language: "${cmsfn.language()}", i18n: translations });

</script>

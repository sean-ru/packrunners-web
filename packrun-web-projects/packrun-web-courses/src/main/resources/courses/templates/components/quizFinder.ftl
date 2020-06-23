<div class="finder-background" style="background-image: url(${ctx.contextPath}/.resources/courses/webresources/img/course-finder-background-ross-parmly-25230.jpg);"></div>

<div class="finder-container" ng-app="StudyGuideFinder">
    <div ng-view>
    </div>
</div>

<script type="text/javascript">

[#assign keys = ["quizFinder.search.placeholder",
                 "quizFinder.search.resultsFound",
                 "quizFinder.search.noResults1",
                 "quizFinder.search.noResults2",
                 "quizFinder.search.noResults3",
                 "quizFinder.filter.courseType",
                 "quizFinder.view",
                 "quizFinder.title"] ]

var translations = {
[#list keys as key]
    "${key}" : "${i18n[key]}",
[/#list]
}

QuizFinder.init({ contextPath: "${ctx.contextPath}", restBase: "${ctx.contextPath}/.rest/delivery", language: "${cmsfn.language()}", i18n: translations });

</script>

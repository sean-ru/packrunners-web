<div class="finder-background" style="background-image: url(${ctx.contextPath}/.resources/videos/webresources/img/finder-background.jpg);"></div>

<div class="finder-container" ng-app="VideoFinder">
    <div ng-view>
    </div>
</div>

<script type="text/javascript">

[#assign keys = ["videoFinder.search.placeholder",
                 "videoFinder.search.resultsFound",
                 "videoFinder.search.noResults1",
                 "videoFinder.search.noResults2",
                 "videoFinder.search.noResults3",
                 "videoFinder.filter.courseType",
                 "videoFinder.view",
                 "video.view",
                 "videoFinder.title"] ]

var translations = {
[#list keys as key]
    "${key}" : "${i18n[key]}",
[/#list]
}

VideoFinder.init({ contextPath: "${ctx.contextPath}", restBase: "${ctx.contextPath}/.rest/delivery", language: "${cmsfn.language()}", i18n: translations });

</script>

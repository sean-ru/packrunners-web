[#-------------- ASSIGNMENTS --------------]
[#include "/courses/templates/macros/courseTypeIcon.ftl" /]

[#assign resourceTypes = model.resourceTypes]
[#if content.studyGuideFinder??]
    [#assign studyGuideFinderLink = cmsfn.link(cmsfn.contentById(content.studyGuideFinder))]
[/#if]

[#if content.videoFinder??]
    [#assign videoFinderLink = cmsfn.link(cmsfn.contentById(content.videoFinder))]
[/#if]

[#if content.quizFinder??]
    [#assign quizFinderLink = cmsfn.link(cmsfn.contentById(content.quizFinder))]
[/#if]

[#-------------- RENDERING --------------]
<!-- Resource Type Teaser Row -->
<div class="container category-card-row after-lead-image">
    <div class="row">

        <h2>${content.title!}</h2>
        <p>${content.body!}</p>

        [#list resourceTypes as resourceType]

            [#if resourceType.name == 'Study Guides']
                [#assign resourceFinderLink = studyGuideFinderLink]
                [#assign resourceFinderSubmitKey = "resourceTypeTeaserRow.studyGuide.submit"]
            [/#if]

            [#if resourceType.name == 'Videos']
                [#assign resourceFinderLink = videoFinderLink]
                [#assign resourceFinderSubmitKey = "resourceTypeTeaserRow.video.submit"]
            [/#if]

            [#if resourceType.name == 'Quizzes']
                [#assign resourceFinderLink = quizFinderLink]
                [#assign resourceFinderSubmitKey = "resourceTypeTeaserRow.quiz.submit"]
            [/#if]

            <form action="${resourceFinderLink!'/'}" method="get"
                        onsubmit="location.href=this.action+'#!/?q='+this.searchQuery.value+'&resourceTypes='+this.resourceTypes.value; return false">
                <div class="col-md-4 category-card">
                    <div class="category-icon absolute-center-container">
                        [@courseTypeIcon resourceType.icon resourceType.name "absolute-center" /]
                    </div>
                    <h3>${resourceType.name!}</h3>
                    <div class="category-card-content">
                        <p>${resourceType.description!}</p>
                    </div>
                    <div class="finder-home-segment-icon">
                      <button type="submit" class="btn btn-primary">${i18n[resourceFinderSubmitKey]}</button>
                    </div>
                </div>
            </form>
        [/#list]

    </div>
</div>

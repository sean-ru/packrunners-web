[#macro studyGuideTeaser studyGuide]

    <!-- Study Guide Teaser -->
    <li class="list-group-item list-group-item-action flex-column align-items-start d-flex justify-content-between align-items-center">
        <div class="d-flex w-100 justify-content-between">
           <h5 class="mb-1">${studyGuide.name!}</h5>
           <small>By ${studyGuide.author!}</small>
        </div>
        <p class="mb-1">${studyGuide.description!}</p>
        <span class="badge badge-primary badge-pill">${studyGuide.tags!}</span>
        <p>
           <a class="btn btn-primary btn-lg" href="${studyGuide.docUrl!}" target="_blank" role="button">${i18n['studyGuide.view']}</a>
        </p>
    </li>

[/#macro]

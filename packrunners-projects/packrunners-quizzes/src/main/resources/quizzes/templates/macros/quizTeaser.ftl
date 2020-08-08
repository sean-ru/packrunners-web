[#macro quizTeaser quiz]

    <!-- Quiz Teaser -->
    <li class="list-group-item list-group-item-action flex-column align-items-start d-flex justify-content-between align-items-center">
        <div class="d-flex w-100 justify-content-between">
           <h5 class="mb-1">${quiz.name!}</h5>
           <small>By ${quiz.author!}</small>
        </div>
        <p class="mb-1">${quiz.description!}</p>
        <span class="badge badge-primary badge-pill">${quiz.tags!}</span>
        <p>
           <a class="btn btn-primary btn-lg" href="${quiz.quizSheetUrl!}" target="_blank" role="button">${i18n['quiz.view']}</a>
        </p>
    </li>

[/#macro]

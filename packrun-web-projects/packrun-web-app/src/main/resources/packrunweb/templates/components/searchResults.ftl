[#-------------- ASSIGNMENTS --------------]
[#assign queryStr = ctx.getParameter('queryStr')!?html]

[#-------------- RENDERING --------------]
[#if content.headline?has_content]
    <h2>${content.headline}</h2>
[/#if]

[#if queryStr?has_content]
  [#assign searchResults = searchfn.searchPages(queryStr, '/packrunweb') /]
  [#assign recordsFound = searchResults?size /]

  <h3><em>${recordsFound}</em> ${i18n['search.pagesFoundFor']} "${queryStr}"</span></h3>

  <div class="list-group">
      [#if searchResults?has_content]
          [#list searchResults as item]
              <a href="${cmsfn.link(item)}" class="list-group-item">
                  <h4 class="list-group-item-heading">${item.title!}</h4>
                  <p class="list-group-item-text">${item.excerpt!}</p>
              </a>
          [/#list]
      [/#if]
  </div>

  [#assign searchResults = searchfn.searchContent('courses', queryStr, '/magnolia-packrunwebs', 'mgnl:content') /]
  [#assign recordsFound = searchResults?size /]

  <h3><em>${recordsFound}</em> ${i18n['search.coursesFoundFor']} "${queryStr}"</span></h3>

  <div class="list-group">
  [#if searchResults?has_content]
      [#list searchResults as item]
          <a href="${cmsfn.link(item)}" class="list-group-item search-result clearfix">
             <h4 class="list-group-item-heading">${item.description!}</h4>
             <img src="${damfn.getAssetLink(item.image)}" class="img-responsive" alt="${item.description!}"/>
             <p class="list-group-item-text">${item.excerpt!}</p>
          </a>
      [/#list]
  [/#if]
  </div>

[#else]
  [#if cmsfn.isEditMode()]
    ${i18n['search.editMode']}
  [/#if]
[/#if]

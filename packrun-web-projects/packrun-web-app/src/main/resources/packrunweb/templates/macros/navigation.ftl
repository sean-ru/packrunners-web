[#--
Customized /mtk/templates/macros/navigation.ftl macro for packrunners project.
Uncommented and adjusted section for resolving menu for course-types and schools from the category content app.
--]
[#macro navigation navParentItem depth=1 expandAll=false navClass="nav"]

    [#if navParentItem?has_content && depth > 0]
    <ul class="${navClass}">

        [#assign navItems = navfn.navItems(navParentItem)]
        [#list navItems as navItem]
            [#if navfn.hasTemplateSubtype(navItem, "courseTypeOverview")]
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                        ${navItem.navigationTitle!navItem.title!navItem.@name}
                        <span class="caret"></span>
                    </a>

                    <ul class="dropdown-menu" role="menu">
                        [#assign navContentItems = navfn.navItemsFromApp("category", "/course-types", "mgnl:category")]
                        [#list navContentItems as navContentItem]
                            <li><a href="${navfn.linkWithSelector(navItem, navContentItem)!"#"}">${navContentItem.displayName!navContentItem.@name}</a></li>
                        [/#list]
                    </ul>
                </li>
            [#elseif navfn.hasTemplateSubtype(navItem, "tutorOverview")]
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                        ${navItem.navigationTitle!navItem.title!navItem.@name}
                        <span class="caret"></span>
                    </a>

                    <ul class="dropdown-menu" role="menu">
                        [#assign navContentItems = navfn.navItemsFromApp("category", "/schools", "mgnl:category")]
                        [#list navContentItems as navContentItem]
                            <li><a href="${navfn.linkWithSelector(navItem, navContentItem)!"#"}">${navContentItem.displayName!navContentItem.@name}</a></li>
                        [/#list]
                    </ul>
                </li>
            [#elseif navfn.hasTemplateSubtype(navItem, "schoolOverview")]
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                            ${navItem.navigationTitle!navItem.title!navItem.@name}
                            <span class="caret"></span>
                        </a>

                        <ul class="dropdown-menu" role="menu">
                            [#assign navContentItems = navfn.navItemsFromApp("category", "/schools", "mgnl:category")]
                            [#list navContentItems as navContentItem]
                                <li><a href="${navfn.linkWithSelector(navItem, navContentItem)!"#"}">${navContentItem.displayName!navContentItem.@name}</a></li>
                            [/#list]
                        </ul>
                    </li>
            [#else]
                [#assign activeNavItem = navfn.isActive(content, navItem)] [#-- Active navigation item is the one which is same as current page--]
                [#assign openNavItem = navfn.isOpen(content, navItem)] [#-- Open navigation item is the one which is ancestor of current page--]
                [#if activeNavItem]
                    <li class="active">
                [#elseif openNavItem]
                    <li class="open">
                [#else]
                    <li>
                [/#if]
                        <a href="${navfn.link(navItem)!"#"}">${navItem.navigationTitle!navItem.title!navItem.@name}</a>
                        [#if expandAll || activeNavItem || openNavItem]
                            [@navigation navItem depth-1 /]
                        [/#if]
                    </li>
            [/#if]
        [/#list]
    </ul>
    [/#if]
[/#macro]
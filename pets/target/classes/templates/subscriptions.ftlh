<#import "parts/common.ftlh" as c>
<#import "parts/login.ftlh" as l>

<@c.page>
    <h3>${userChannel.username}</h3>
    <div>${type}</div>
    <ul class="list-group">
        <#list users as user>
        <li class="list-group-item">
            <a href="/user-pets/${user.id}">${user.getUsername()}</a>
        </li>
        </#list>
    </ul>


</@c.page>
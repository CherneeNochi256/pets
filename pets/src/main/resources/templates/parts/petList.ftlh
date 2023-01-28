<#include "security.ftlh">
<#import "pager.ftlh" as p>


<@p.pager url page/>
<div class="card-columns" id="pet-list">
    <#list page.content as pet>
        <div class="card my-3" data-id="${pet.id}">
            <div>
                <#if pet.filename??>
                    <img src="/img/${pet.filename}" class="card-img-top"/>
                </#if>
            </div>
            <div class="m-2">
                <span>${pet.text}</span><br/>
                <i>#${pet.tag}</i>
            </div>
            <div class="card-footer text-muted container">
                <div class="row">
                    <a class="col align-self-center"
                       href="/user-pets/${pet.author.id}"> ${pet.authorName}</a>
                    <a class="col align-self-center" href="/pets/${pet.id}/like">
                        <#if pet.meLiked>
                            <i class="fa fa-heart"></i>
                        <#else >
                            <i class="fa fa-heart-o"></i>
                        </#if>
                    ${pet.likes}
                    </a>
                    <#if pet.author.id == currentUserId>
                        <a class="col btn btn-primary" href="/user-pets/${pet.author.id}?pet=${pet.id}">
                            Edit
                        </a>
                    </#if>
                </div>
            </div>
        </div>
    <#else>
        No pet
    </#list>
</div>
<@p.pager url page/>

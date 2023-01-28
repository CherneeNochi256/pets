<a class="btn btn-primary" data-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false"
   aria-controls="collapseExample">
    Message editor
</a>
<div class="collapse <#if pet??>show</#if>" id="collapseExample">
    <div class="form-group mt-3">
        <form method="post" action="/main" enctype="multipart/form-data">

            <div class="form-group">
                <input type="text" name="text" class="form-control ${(textError??)?string('is-invalid', '')}"
                       value="<#if pet??>${pet.text}</#if>" placeholder="Enter your massage"/>
                <#if textError??>
                    <div class="invalid-feedback">
                        ${textError}
                    </div>
                </#if>
            </div>

            <div class="form-group">
                <input type="text" name="tag" value="<#if pet??>${pet.tag}</#if>"
                       class="form-control" placeholder="Enter tag"/>
                <#if tagError??>
                    <div class="invalid-feedback">
                        ${tagError}
                    </div>
                </#if>
            </div>

            <div class="form-group">
                <div class="custom-file">
                    <label class="custom-file-label" for="customFile">Choose file</label>
                    <input type="file" name="file" id="customFile"/>
                </div>
            </div>

            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <input type="hidden" name="id" value="<#if pet??>${pet.id}</#if>"/>
            <div class="form-group">
                <button type="submit" class="btn btn-primary">Save pet</button>
            </div>

        </form>
    </div>
</div>
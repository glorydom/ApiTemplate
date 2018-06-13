<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>


<div id="createDialog" class="crudDialog">
	
	<!-- 发布流程 -->
	<form enctype="multipart/form-data" method="POST" id="createForm">

		<div class="form-group">
			<label for="name">名称</label>
			<input id="name" type="text" class="form-control" name="name" maxlength="20">
		</div>
		
		<div class="form-group">
			<label for="description">工作流文件</label>
			<br/><br/><br/>
			<input type="file" name="file"/>
		</div>
		
		<div class="form-group text-right dialog-buttons">
			<input type="button" value="发布" class="waves-effect waves-button" onclick="createSubmit();"/>
			<input type="button" value="取消" class="waves-effect waves-button" onclick="createDialog.close();"/>
		</div>
		
	</form>
	
</div>
<script>
function createSubmit() {
    $.ajax({
        type: 'post',
        url: '${basePath}/chqs/workflow/deployment',
        data: new FormData($("#createForm")[0]),
     	// 告诉jQuery不要去处理发送的数据
        processData : false, 
        // 告诉jQuery不要去设置Content-Type请求头
        contentType : false,
        beforeSend: function() {
            if ($('#name').val() == '') {
                $('#name').focus();
                return false;
            }
        },
        success: function(result) {
			if (result.code != 1) {
				if (result.data instanceof Array) {
					$.each(result.data, function(index, value) {
						$.confirm({
							theme: 'dark',
							animation: 'rotateX',
							closeAnimation: 'rotateX',
							title: false,
							content: value.errorMsg,
							buttons: {
								confirm: {
									text: '确认',
									btnClass: 'waves-effect waves-button waves-light'
								}
							}
						});
					});
				} else {
						$.confirm({
							theme: 'dark',
							animation: 'rotateX',
							closeAnimation: 'rotateX',
							title: false,
							content: result.data.errorMsg,
							buttons: {
								confirm: {
									text: '确认',
									btnClass: 'waves-effect waves-button waves-light'
								}
							}
						});
				}
			} else {
				createDialog.close();
				$table.bootstrapTable('refresh');
			}
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
			$.confirm({
				theme: 'dark',
				animation: 'rotateX',
				closeAnimation: 'rotateX',
				title: false,
				content: textStatus,
				buttons: {
					confirm: {
						text: '确认',
						btnClass: 'waves-effect waves-button waves-light'
					}
				}
			});
        }
    });
}
</script>
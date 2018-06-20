<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>


<div id="completeDialog" class="crudDialog">
	
	<form  method="POST" id="createForm">
		<input type="hidden" name="taskIds" value="${taskIds}"/>
		<div class="form-group">
			<label for="comments">说明</label>
			<input id="comments" type="text" class="form-control" name="name" maxlength="20">
		</div>
		
		<div class="form-group text-right dialog-buttons">
			<input type="button" value="完成任务" class="waves-effect waves-button" onclick="createSubmit();"/>
			<input type="button" value="取消" class="waves-effect waves-button" onclick="completeDialog.close();"/>
		</div>
		
	</form>
	
</div>
<script>
function createSubmit() {
    $.ajax({
        type: 'post',
        url: '${basePath}/chqs/workflow/completeTask',
        data: {taskIds: "${taskIds}", comments: $('#comments').val() },
        beforeSend: function() {
            if ($('#comments').val() == '') {
                $('#comments').focus();
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
				completeDialog.close();
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
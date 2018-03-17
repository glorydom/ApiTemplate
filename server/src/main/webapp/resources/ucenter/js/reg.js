$(function() {


});

function registerWithEmail() {
	$.ajax({
		url: BASE_PATH + '/signup',
		type: 'POST',
		data: {
			username: $('#email').val(),
			password: $('#password').val(),
			backurl: BACK_URL
		},
		beforeSend: function() {
            alert(BACK_URL);
		},
		success: function(json){
			if (json.code == 1) {
				location.href = BASE_PATH + '/' + json.data;
			} else {
				alert(json.data);
				if (10101 == json.code) {
					$('#username').focus();
				}
				if (10102 == json.code) {
					$('#password').focus();
				}
			}
		},
		error: function(error){
			console.log(error);
		}
	});
}


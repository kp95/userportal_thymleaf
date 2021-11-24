/**
*Show image file, check size and modal Configuration 
 */

$(document).ready(function(){
	$("#buttonCancel").on("click", function(){
		window.location = moduleURL;
	});
	
	$("#fileImage").change(function(){
		console.log("File change");
		if(!checkFileSize(this)){
			return;
		}
		showImageThumbnail(this);
	});
})

function showImageThumbnail(fileInput){
	var file = fileInput.files[0];
	var reader = new FileReader();
	reader.onload = function(e){
		$("#thumbnail").attr("src",e.target.result);
	};
	reader.readAsDataURL(file);
	
}

function checkFileSize(fileInput){
	MAX_FILE_SIZE = 102400; // 100KB
	fileSize = fileInput.files[0].size;
	
	if(fileSize > MAX_FILE_SIZE){
		fileInput.setCustomValidity("Please file less than "+ MAX_FILE_SIZE + "bytes");
		fileInput.reportValidity();
		return false;
	}
	else{
		fileInput.setCustomValidity("");
		return true;
	}
	
}
function showModalDialog(title, message) {
	$("#modalTitle").text(title);
	$("#modalBody").text(message);
	$("#modalDialog").modal();
}

function showErrorModal(message) {
	showModalDialog("Error", message);
}

function showWarningModal(message) {
	showModalDialog("Warning", message);
}
function checkUniqueEmail(form){
	url = controllerURL;
	userEmail = $("#email").val();
	userId = $("#id").val();
	
	csrfValue = $("input[name='_csrf']").val();
	
	params = {id: userId, email: userEmail, _csrf: csrfValue} ;
	
	$.post(url,params,function(response){
		unique = response.unique;
		
		if(unique == true){
			form.submit();
		}
		else if (unique == false){
				showWarningModal("There is another user having the email " + userEmail);
		} 
		else {
			showErrorModal("Unknown response from server");
		}
		
	}).fail(function(){
		showErrorModal("Could not connect to the server");
	});
	return false;
}




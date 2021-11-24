/**
 * 
 */
 
$(document).ready(function(){

	$("#logoutLink").on("click",function(e){
		e.preventDefault();
		document.logoutForm.submit();
	});


	console.log("Loading Document navigation")
	customizeDropDownMenu();
	
})
function customizeDropDownMenu(){
	$(".navbar .dropdown").hover(function(){
			$(this).find('.dropdown-menu').first().stop(true,true).delay(250).slideDown();
		},
		function(){
			$(this).find(".dropdown-menu").first().stop(true,true).delay(100).slideUp();
		}
	);
}
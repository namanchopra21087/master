/* 
	author: istockphp.com
*/
jQuery(function($) {
	
	openMyPopup('dmsBCSearch');
	openMyPopup('popup');
	
	function openMyPopup(pmval){
	       $("."+pmval).click(function() {
	                     loading();
	                     setTimeout(function(){
	                           loadPopup(pmval); 
	                     }, 500);
	       return false;
	       });
	       }

	
	/* event for close the popup */
	$("div.closeOne").hover(
					function() {
						$('span.ecs_tooltipOne').show();
					},
					function () {
    					$('span.ecs_tooltipOne').hide();
  					}
				);
				
				
	$("div.closeTwo").hover(
					function() {
						$('span.ecs_tooltipTwo').show();
					},
					function () {
    					$('span.ecs_tooltipTwo').hide();
  					}
				);
				
	$("div.closeThree").hover(
					function() {
						$('span.ecs_tooltipThree').show();
					},
					function () {
    					$('span.ecs_tooltipThree').hide();
  					}
				);
				
	$("div.closeFour").hover(
					function() {
						$('span.ecs_tooltipFour').show();
					},
					function () {
    					$('span.ecs_tooltipFour').hide();
  					}
				);
				
	$("div.closeFive").hover(
					function() {
						$('span.ecs_tooltipFive').show();
					},
					function () {
    					$('span.ecs_tooltipFive').hide();
  					}
				);
	
	$("div.close").click(function() {
		disablePopup();  // function close pop up
	});
	
	$(this).keyup(function(event) {
		if (event.which == 27) {
			
		}  	
	});
	
	
	$("div.ecs_tooltipOne").click(function() {
		disablePopup("dmsBCSearch","popup");		
		});
	$("div#backgroundPopup").click(function() {
		disablePopup("dmsBCSearch","popup");
	});
	

	 /************** start: functions. **************/
	function loading() {
		$("#backgroundPopup").css("opacity", "0.7"); // css opacity, supports IE7, IE8
		$("#backgroundPopup").fadeIn(0001);
		$("div.loader").show();  
	}
	function closeloading() {
		$("div.loader").fadeOut('normal');  
	}
	
	var popupStatus = 0; // set value
	
	function loadPopup(val) { 
		if(popupStatus == 1){popupStatus = 0;}
		if(popupStatus == 0) { // if value is 0, show popup
			closeloading(); // fadeout loading
			
			for (var i=0; i < arguments.length; i++) {	
				$("#"+arguments[i]).fadeIn(0500);
    		}
			$("#backgroundPopup").css("opacity", "0.7"); // css opacity, supports IE7, IE8
			$("#backgroundPopup").fadeIn(0001); 
			popupStatus = 1; // and set value to 1
		}	
	}
		
	function disablePopup() {
		if(popupStatus == 1) { // if value is 1, close popup
			for (var i=0; i < arguments.length; i++) {				
				$("#"+arguments[i]).fadeOut("normal");
    		}
			$("#backgroundPopup").fadeOut("normal");  
			popupStatus = 0;  // and set value to 0
		}
	}
	/************** end: functions. **************/
}); // jQuery End
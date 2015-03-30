/**
 * 鼠标移动上去显示全路径
 */

	jQuery(function divHover(){
		var chooseText=jQuery(this).data("chooseText");
		viewMessageDiv=jQuery("<div id='viewMessage' class='viewMessage'></div>");
		jQuery("body").append(viewMessageDiv);
	});
	function setViewMessageDiv(jDivs){
		jDivs.hover(function(event){
			var chooseText=jQuery(this).data("chooseText");
			viewMessageDiv.css("left",event.pageX-1);
			viewMessageDiv.css("top",event.pageY-1);
			viewMessageDiv.html(chooseText);
			viewMessageDiv.show();
		}, function() {
			viewMessageDiv.hide();
		});
	}
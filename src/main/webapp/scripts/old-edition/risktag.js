function removeCheckedValues(tags, id) {
	remove(id);
	if(checkShowDiv(tags)){
		$("#check_"+tags).attr("value","");
	}
}

function checkShowDiv(tags) {
	tags = "#div"+tags;
	var inner = $(tags).html();
	return inner == "" ? true : false;
}

function removeCheckedRisk(tags) {
	clearSelectValue(tags);
	tags = "div"+tags;
	document.getElementById(tags).innerHTML = "";
}

function removeCheckedPositions(tags, id) {
	remove(id);
}

function removeCheckedPosition(tags) {
	tags = "div"+tags;
	document.getElementById(tags).innerHTML = "";
}

function removeCheckedRisks(tags, id) {
	remove(id);
}

function removeChecked(tags) {
	clearSelectValue(tags);
	tags = "div"+tags;
	document.getElementById(tags).innerHTML = "";
}

function remove(id) {
	var idObject = document.getElementById(id);
	if (idObject != null)
		idObject.parentNode.removeChild(idObject);
}

function clearSelectValue(tags){
	var selectValue = document.getElementsByName(tags);
	if(selectValue!=undefined && selectValue.length>0){
		for(var i=0;i<selectValue.length;i++){
			selectValue[i].value="";
		}
	}
}
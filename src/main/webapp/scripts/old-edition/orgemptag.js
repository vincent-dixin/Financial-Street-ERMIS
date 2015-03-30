function removeCheckedValues(tags, id) {
	remove(id);
}

function removeCheckedOrg(tags,orgName,posiName) {
	clearSelectEmpValue(tags,orgName,posiName);
	tags = "div"+tags;
	document.getElementById(tags).innerHTML = "";
}

function removeCheckedPositions(tags, id) {
	remove(id);
}

function removeCheckedPosition(tags) {
	clearSelectEmpValue(tags,orgName,posiName);
	tags = "div"+tags;
	document.getElementById(tags).innerHTML = "";
}

function removeCheckedOrgEmps(tags, id) {
	remove(id);
}

function removeCheckedOrgEmp(tags,orgName,posiName) {
	clearSelectEmpValue(tags,orgName,posiName);
	tags = "div"+tags;
	document.getElementById(tags).innerHTML = "";
}

function removeCheckedDicts(tags, id) {
	remove(id);
}

function removeCheckedDict(tags) {
	clearSelectEmpValue(tags,orgName,posiName);
	tags = "div"+tags;
	document.getElementById(tags).innerHTML = "";
}

function remove(id) {
	var idObject = document.getElementById(id);
	if (idObject != null){
		idObject.parentNode.removeChild(idObject);
	}
}

function clearSelectEmpValue(tags,orgName,posiName){
	var selectValue = document.getElementsByName(tags);
	var orgSelectValue = document.getElementsByName(orgName);
	var posiSelectValue = document.getElementsByName(posiName);
	if(selectValue!=undefined && selectValue.length>0){
		for(var i=0;i<selectValue.length;i++){
			selectValue[i].value="";
			orgSelectValue[i].value="";
			if(undefined != posiSelectValue && posiSelectValue.length>0){
				posiSelectValue[i].value="";
			}
		}
	}
}
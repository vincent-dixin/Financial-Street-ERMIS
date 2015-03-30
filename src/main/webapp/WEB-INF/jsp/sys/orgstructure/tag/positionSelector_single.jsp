<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<html>
<base target="_self">
<head>
<title><spring:message code="fhd.sys.orgstructure.org.org" /></title>
<style type="text/css">
.selectorTable{
	border-collapse:inherit;
	width:100%;
	padding: 0; 
    margin: 0; 
	border-left: 1px solid #C1DAD7; 
}

.selectorTable th { 
    font: bold 12px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif; 
    color: #666; 
    border-right: 1px solid #C1DAD7; 
    border-bottom: 1px solid #C1DAD7; 
    border-top: 1px solid #C1DAD7; 
    letter-spacing: 2px; 
    text-transform: uppercase; 
    padding: 6px 6px 6px 12px; 
    background: #CAE8EA url(images/bg_header.jpg) no-repeat; 
} 

.selectorTable td { 
    border-right: 1px solid #C1DAD7; 
    border-bottom: 1px solid #C1DAD7; 
    font-size:12px; 
    color: #666; 
}

.fhd_btn{
	height:20px;
	BORDER-RIGHT: #7b9ebd 1px solid; 
	PADDING-RIGHT: 5px; 
	BORDER-TOP: #7b9ebd 1px solid; 
	PADDING-LEFT: 5px; 
	FONT-SIZE: 12px; 
	FILTER: progid:DXImageTransform.Microsoft.Gradient(GradientType=0, StartColorStr=#ffffff, EndColorStr=#cecfde); 
	BORDER-LEFT: #7b9ebd 1px solid; 
	CURSOR: hand; COLOR: black; 
	PADDING-TOP: 2px; 
	BORDER-BOTTOM: #7b9ebd 1px solid;
}

</style>
</head>
<body style="overflow: hidden;">
<!--<fhdcore:tree rootText="${orgRoot.orgname}" 
el="org-tree" url="${ctx}/sys/orgstructure/org/loadPositionTree.do?leaf=position" 
height="368" width="265" rootId="${orgRoot.id}" checkNode="true" onlyLeafCheckable="true" checkModel="single">
	<fhdcore:treeNode nodeType="org" url="${ctx}/sys/orgstructure/org/loadPositionTree.do?leaf=position"></fhdcore:treeNode>
</fhdcore:tree>

--><fhd:positionTree rootId="${orgRoot.id}" rootName="${orgRoot.orgname}" height="368" width="265" rootCheck="false" allowRootLink="false" dataUrl="${ctx}/sys/orgstructure/org/loadPositionTree.do?leaf=position" checkNode="${checkNode}" onlyLeafCheckable="${onlyLeafCheckable}" checkModel="${checkModel}" showFreshBtn="false" id="tree"/>

<div style="float:left;width:310px;height:260px;">
<table class="selectorTable">
<tr><th  height="25px" colspan="2" align="center" valign="middle">选择结果</th></tr>
<tr bgcolor="#F7FDFD" >
<td bgcolor="#F7FDFD" height="25px" align="center" valign="middle">
<a href="javascript:clearHasCheckedValues()">清空</a>
</td>
<td width="275px" align="center" valign="middle">岗位列表</td></tr>
<tr><td height="260px" colspan="2" align="left" valign="top" id="chooseValues"></td></tr>
<tr bgcolor="#F7FDFD" ><td  height="40px" colspan="2" align="center" valign="middle">
<input type="button" value="确定" onclick="selectedPositionSure()" class="fhd_btn"/>&nbsp;
<input type="button" value="取消" onclick="closeDialogWindow()" class="fhd_btn"/></td></tr>
</table>
</div>

<script type="text/javascript">
	var chooseValues = document.getElementById("chooseValues");
	var jChooseValues=jQuery(chooseValues);
	// 注意！这行代码是每个内容页必须加的内容，否则无法正确显示。
	var P = parentWindow();
	var tags = '${tag}';
	var parentTag = 'div'+tags;

	function clearHasCheckedValues(){
		var chooseOrg = document.getElementsByName(tags);
		if(chooseOrg.length>0 && chooseOrg[0].value!='')
			unCheckTreeNode(chooseOrg[0].value);
		
		chooseValues.innerHTML="";
	}

	function removeChecked(tags,id){
		unCheckTreeNode(id);
		chooseValues.innerHTML="";
	}

	function unCheckTreeNode(nodeId){
		var node = tree.getNodeById(nodeId);
		if(node!=undefined){
			node.ui.toggleCheck(false);   
			node.attributes.checked = false;
		}
	}
	
	function selectedPositionSure(){
		var selectValues = $('#chooseValues').html();
		P.$("#"+parentTag).html(selectValues);
		if(selectValues.length==0)
			clearSelectValue();
		if(chooseValues.innerHTML == ""){
			P.$("#"+tags).attr("value","");
		}
		closeDialogWindow();
	}

	function clearSelectValue(){
		var selectValue = P.document.getElementsByName(tags);
		if(selectValue!=undefined && selectValue.length>0){
			for(var i=0;i<selectValue.length;i++){
				selectValue[i].value="";
			}
		}
	}

	function closeDialogWindow(){
		closeWindow();
	}
	
	tree.on('checkchange', function(node, checked) {
		var nodeId = node.attributes.id;
		var nodeText = node.attributes.text;
		
		if(node.attributes.checked){
			var checkPosition="<div id='checkValue'><input type='radio' checked='checked' onClick='javascript:removeChecked(\"" + tags + "\",\"" + nodeId + "\")' name='" + tags + "' value='"+ nodeId + "' /><span ext:qtip='"+fullpath+nodeText+"'>"+ nodeText + "</span></div>";
			jChooseValues.html(checkPosition);
		}else{
			chooseValues.innerHTML="";
		}	
	}, tree);


	
	var selectedPositions = P.document.getElementById(parentTag).innerHTML;
	if(selectedPositions!=''){
		//step 2:
		document.getElementById("chooseValues").innerHTML = selectedPositions;
	}
</script>
</body>
</html>
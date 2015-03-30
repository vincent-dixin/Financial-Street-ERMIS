<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<html>
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
<body>
	<!--<fhdcore:tree rootText="${orgRoot.orgname}"
	el="org-tree" url="${ctx}/sys/orgstructure/org/loadPositionTree.do?leaf=emp" 
	height="368" width="265" rootId="${orgRoot.id}" checkNode="true" onlyLeafCheckable="true" checkModel="single">
		<fhdcore:treeNode nodeType="org" url="${ctx}/sys/orgstructure/org/loadPositionTree.do?leaf=emp"></fhdcore:treeNode>
		<fhdcore:treeNode nodeType="posi" url="${ctx}/sys/orgstructure/posi/loadPosiTree.do"></fhdcore:treeNode>
	</fhdcore:tree>
	-->
	<fhd:empTree rootId="${orgRoot.id}" defaultOrg="${defaultOrg}" empfilter="${empfilter}" rootName="${orgRoot.orgname}"  height="368" width="265" rootCheck="false" allowRootLink="false" checkNode="${checkNode}" onlyLeafCheckable="${onlyLeafCheckable}" checkModel="${checkModel}" showFreshBtn="false" id="tree"/>
	<div style="float:left;width:310px;height:260px;">
		<table class="selectorTable" border="0" cellpadding="0" cellspacing="0">
			<tr><th height="25px" colspan="2" align="center" valign="middle">选择结果</th></tr>
			<tr bgcolor="#F7FDFD" >
				<td bgcolor="#F7FDFD" height="25px" align="center" valign="middle">
					<a href="javascript:clearHasCheckedValues()">清空</a>
				</td>
				<td width="275px" align="center" valign="middle">员工列表</td>
			</tr>
			<tr><td height="260px" colspan="2" align="left" valign="top" id="chooseValues"></td></tr>
			<tr bgcolor="#F7FDFD" >
				<td height="40px" colspan="2" align="center" valign="middle">
					<input type="button" value="确定" onclick="selectedEmpSure()" class="fhd_btn"/>&nbsp;
					<input type="button" value="取消" onclick="closeDialogWindow()" class="fhd_btn"/>
				</td>
			</tr>
		</table>
	</div>
	<script type="text/javascript">
		var chooseValues = document.getElementById("chooseValues");
		// 注意！这行代码是每个内容页必须加的内容，否则无法正确显示。
		var P = parentWindow();
		var tags = '${tag}';
		var orgName = '${orgName}';
		var posiName = '${posiName}';
		var parentTag = 'div'+tags;
	
		function clearHasCheckedValues(){
			var chooseOrg = document.getElementsByName(tags);
			if(chooseOrg.length>0 && chooseOrg[0].value!='')
				unCheckTreeNode(chooseOrg[0].value.split(',')[0]);
			
			chooseValues.innerHTML="";
		}
	
		function removeChecked(tags,orgName,posiName,id){
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
		
		function selectedEmpSure(){
			var selectValues = document.getElementById('chooseValues').innerHTML;
			P.document.getElementById(parentTag).innerHTML=selectValues;
			if(selectValues.length==0)
				clearSelectValue();
			
			closeWindow();
		}
	
		function clearSelectValue(){
			var selectValue = P.document.getElementsByName(tags);
			var orgSelectValue = P.document.getElementsByName(orgName);
			var posiSelectValue = P.document.getElementsByName(posiName);
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
	
		function closeDialogWindow(){
			selectedEmpSure();
			closeWindow();
		}
		
		tree.on('checkchange', function(node, checked) {
			var nodeId = node.attributes.id;
			var nodeText = node.attributes.text;
			if(node.attributes.checked){
				chooseValues.innerHTML = "<input type='radio' checked='checked' onClick='javascript:removeChecked(\"" + tags+"\",\""+orgName+"\",\""+posiName + "\",\"" + nodeId + "\")' name='" + tags + "_check' value='"+ nodeId + "' />";
				if('' != posiName){
					chooseValues.innerHTML += "<span ext:qtitle='' ext:qtip='" + node.attributes.orgName+"->"+node.attributes.posiName+"->"+nodeText + "'>"+nodeText + "</span>";
					P.document.getElementsByName(posiName)[0].value = node.attributes.posiId;
				}else{
					chooseValues.innerHTML += "<span ext:qtitle='' ext:qtip='" + node.attributes.orgName+"->"+nodeText + "'>"+nodeText + "</span>";
				}
				P.document.getElementsByName(tags)[0].value = nodeId;
				P.document.getElementsByName(orgName)[0].value = node.attributes.orgId;
			}else{
				chooseValues.innerHTML="";
				P.document.getElementsByName(tags)[0].value = '';
				alert('orgName='+orgName);
				P.document.getElementsByName(orgName)[0].value = '';
				if('' != posiName){
					P.document.getElementsByName(posiName)[0].value = '';
				}else{
					alert('posiName='+posiName);
				}
			}
		}, tree);
	
		var selected = P.document.getElementsByName(tags)[0].value;
		tree.on('load',function(node){
	    	node.eachChild(function(n){
				if(n.id == selected){
					n.ui.toggleCheck(true);   
					n.attributes.checked = true;
				}
	    	});
	    });
	
		var selectedEmps = P.document.getElementById(parentTag).innerHTML;
		if(selectedEmps!=''){
			//step 2:
			document.getElementById("chooseValues").innerHTML = selectedEmps;
		}
	</script>
</body>
</html>
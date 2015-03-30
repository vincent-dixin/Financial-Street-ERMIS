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
	el="org-tree" url="${ctx}/sys/orgstructure/org/loadPositionTree.do?leaf=emp&empfilter=${empfilter}" 
	height="368" width="265" rootId="${orgRoot.id}" checkNode="${checkNode}" onlyLeafCheckable="${onlyLeafCheckable}" checkModel="${checkModel}">
		<fhdcore:treeNode nodeType="org" url="${ctx}/sys/orgstructure/org/loadPositionTree.do?leaf=emp&empfilter=${empfilter}"></fhdcore:treeNode>
		<fhdcore:treeNode nodeType="posi" url="${ctx}/sys/orgstructure/posi/loadPosiTree.do?empfilter=${empfilter}"></fhdcore:treeNode>
	</fhdcore:tree>
	-->
	<fhd:empTree rootId="${orgRoot.id}" defaultOrg="${defaultOrg}" empfilter="${empfilter}" rootName="${orgRoot.orgname}" height="368" width="265" rootCheck="false" allowRootLink="false" checkNode="${checkNode}" onlyLeafCheckable="${onlyLeafCheckable}" checkModel="${checkModel}" showFreshBtn="false" id="tree"/>
	<div style="float:left;width:310px;height:260px;">
		<table class="selectorTable" border="0" cellpadding="0" cellspacing="0">
			<tr><th height="25px" colspan="2" align="center" valign="middle">选择结果</th></tr>
			<tr bgcolor="#F7FDFD" >
				<td bgcolor="#F7FDFD" height="25px" align="left" valign="middle">
					<a href="javascript:clearHasCheckedValues()">清空</a>
				</td>
				<td width="275px" align="center" valign="middle">员工列表</td>
			</tr>
			<tr><td height="260px" colspan="2" align="left" valign="top" id="chooseValues"></td></tr>
			<tr bgcolor="#F7FDFD">
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
	
		function remove(id) {
			var idObject = document.getElementById(id);
			if (idObject != null){
				idObject.parentNode.removeChild(idObject);				
			}
		}
		
		function clearHasCheckedValues(){
			var chooseOrgs = document.getElementsByName(tags);
			if(chooseOrgs.length>0){
				for(var i=0;i<chooseOrgs.length;i++)
					unCheckTreeNode(chooseOrgs[i].value);
			}
				
			chooseValues.innerHTML="";
		}
		
		function selectedEmpSure(){
			P.document.getElementById(parentTag).innerHTML=document.getElementById("chooseValues").innerHTML;
			closeDialogWindow();
		}
	
		function unCheckTreeNode(nodeId){
			var node = tree.getNodeById(nodeId);
			if(node!=undefined){
				node.ui.toggleCheck(false);   
				node.attributes.checked = false;
			}
		}
	
		function checkTreeNode(nodeId){
			var node = tree.getNodeById(nodeId);
			if(node!=undefined){
				node.ui.toggleCheck(true);   
				node.attributes.checked = true;
			}
		}
	
		function removeCheckedValues(tags, id) {
			unCheckTreeNode(id); 
			remove(id);
		}
	
		function closeDialogWindow(){
			closeWindow();
		}
	
		function handleSelect(node, show){
			var nodeId = node.attributes.id;
			var nodeText = node.attributes.text;
			if(node.attributes.checked){
				if(node.isLeaf() && node.attributes.checked  && show.indexOf(nodeId)<0){
					var a = '';
					a += "<div id='"+ nodeId +"'><input type='checkbox' checked='checked' onClick='javascript:removeCheckedValues(\"" + tags + "\",\"" + nodeId + "\")' name='" + tags + "_check' value='"+ nodeId +"' />";
					if('' != posiName){
						a += "<span ext:qtitle='' ext:qtip='" + node.attributes.orgName+"->"+node.attributes.posiName+"->"+nodeText + "'>"+nodeText + "</span>";
						a += "<input type='hidden' name='"+posiName+"' value='"+node.attributes.posiId+"'/>";
					}else{
						a += "<span ext:qtitle='' ext:qtip='" + node.attributes.orgName+"->"+nodeText + "'>"+nodeText + "</span>";
					}
					a += "<input type='hidden' name='"+tags+"' value='"+nodeId+"'/><input type='hidden' name='"+orgName+"' value='"+node.attributes.orgId+"'/>";
					a += "</div>";
					
					chooseValues.innerHTML += a;
				}
				/*
				node.eachChild(function(child) {
					handleSelect(child);      
			 	});
			 	*/
				return;  
			 }
		}
	
		function unSelect(node){
			var nodeId = node.attributes.id;
			if(node.attributes.checked==false){
				removeCheckedOrgEmps(tags,nodeId);
				if(!node.isLeaf()){
					node.eachChild(function(child) {unSelect(child);});
				}
			}
		}
	
		tree.on('checkchange', function(node, checked) {
			unSelect(node);
		}, tree);
	
		tree.on('check', function(node, checked) {
			var show = document.getElementById("chooseValues").innerHTML;
			handleSelect(node,show);
		}, tree);
	
		//step 1:
		var selectedEmps = P.document.getElementById(parentTag).innerHTML;
		if(selectedEmps!=''){
			//step 2:
			document.getElementById("chooseValues").innerHTML = selectedEmps;
		}
	</script>
</body>
</html>
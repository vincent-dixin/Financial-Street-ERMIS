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
<body style="overflow: hidden;">
	<!--<fhdcore:tree rootText="${orgRoot.orgname}" rootHref="javascript:void(0);"
	el="org-tree" url="${ctx}/sys/orgstructure/org/loadPositionTree.do?leaf=org" 
	height="368" width="265" rootId="${orgRoot.id}" checkNode="true" onlyLeafCheckable="false" checkModel="single">
		<fhdcore:treeNode nodeType="org" url="${ctx}/sys/orgstructure/org/loadPositionTree.do?leaf=org"></fhdcore:treeNode>
	</fhdcore:tree>
	
	--><fhd:orgTree rootId="${orgRoot.id}" rootName="${orgRoot.orgname}" height="368" width="265" rootCheck="false" allowRootLink="false" dataUrl="${ctx}/sys/orgstructure/org/loadPositionTree.do?leaf=org" checkNode="${checkNode}" onlyLeafCheckable="${onlyLeafCheckable}" checkModel="${checkModel}" showFreshBtn="false" id="tree"></fhd:orgTree>

	<div style="float:left;width:310px;height:260px;">
		<table class="selectorTable" cellpadding="0" cellspacing="0" border="0">
			<tr><th height="25px" colspan="2" align="center" valign="middle">选择结果</th></tr>
			<tr bgcolor="#F7FDFD" >
				<td bgcolor="#F7FDFD" height="25px" align="center" valign="middle"><a href="javascript:clearHasCheckedValues()">清空</a></td>
				<td width="275px" align="center" valign="middle">部门列表</td>
			</tr>
			<tr><td height="260px" colspan="2" align="left" valign="top" id="chooseValues"></td></tr>
			<tr bgcolor="#F7FDFD" >
				<td height="40px" colspan="2" align="center" valign="middle">
					<input type="button" value="确定" onclick="selectedOrgSure()" class="fhd_btn"/>
					&nbsp;
					<input type="button" value="取消" onclick="closeDialogWindow()" class="fhd_btn"/>
				</td>
			</tr>
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
				unCheckTreeNode(chooseOrg[0].value.split(',')[0]);
			var chs = chooseValues.getElementsByTagName('input');
			for(var i=0;i<chs.length;i++)
				unCheckTreeNode(chs[i].value);
			chooseValues.innerHTML="";
		}
	
		function removeChecked(tags,id){
			unCheckTreeNode(id.substring(0,32));
			chooseValues.innerHTML="";
		}
	
		function unCheckTreeNode(nodeId){
			var node = tree.getNodeById(nodeId);
			if(node!=undefined){
				node.ui.toggleCheck(false);   
				node.attributes.checked = false;
			}
		}
		
		function selectedOrgSure(){
			var selectValues = $('#chooseValues').html();
			P.$("#"+parentTag).html(selectValues);
			if(selectValues.length==0){
				clearSelectValue();
			}
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
			var fullpath = '';
			
			var fullnode=node.parentNode;
			if(fullnode.parentNode != null){
			while(fullnode.parentNode != null){
					fullpath = fullnode.text + '>>' + fullpath;
					fullnode = fullnode.parentNode;
			}
			}else {
				fullpath = fullnode.text + '>>' + fullpath;
			}
			if(node.attributes.checked){
				P.document.getElementsByName(tags)[0].value = nodeId;
				var checkOrg="<div id='checkValue' ><input type='radio' checked='checked' onClick='javascript:removeChecked(\"" + tags + "\",\"" + nodeId + RndNum(4) + "\")' name='" + tags + "_check' value='"+ nodeId + "' /><span ext:qtip='"+fullpath+nodeText+"'>" +FHD.util.StringUtil.shortString( nodeText,20) + "</span></div>";
				var nodeDiv=jQuery(checkOrg);
				jChooseValues.html(nodeDiv);
				
			}else{
				P.document.getElementsByName(tags)[0].value = '';
				chooseValues.innerHTML="";	
			}
			 
		}, tree);
		var selectedOrgs = P.document.getElementById(parentTag).innerHTML;
		var selected = P.document.getElementsByName(tags)[0].value;
		tree.on('load',function(node){
		    	node.eachChild(function(n){
					if(null != selected && selected.indexOf(n.id) > -1){
						n.ui.toggleCheck(true);   
						n.attributes.checked = true;
					}
		    	});
		    });
	
		
		
		if(selectedOrgs!=''){
			//step 2:
			document.getElementById("chooseValues").innerHTML = selectedOrgs;
		}
	</script>
</body>
</html>
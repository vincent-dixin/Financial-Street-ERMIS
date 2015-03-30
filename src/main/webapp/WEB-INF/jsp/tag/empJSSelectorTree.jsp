<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<html>
<head>
	<title><spring:message code="fhd.common.risk" /></title>
</head>
<body onload="loadMethod();">
	<fhdcore:tree rootText="${orgRoot.orgname}" 
	el="tree-div" url="${ctx}/sys/orgstructure/org/loadPositionTree.do?checkNode=true&leaf=emp&defaultOrg=${defaultOrg}" 
	height="document.body.offsetHeight" width="265" rootId="${root.id}" checkNode="true" onlyLeafCheckable="true" checkModel="multiple">
		<fhdcore:treeNode nodeType="org" url="${ctx}/sys/orgstructure/org/loadPositionTree.do?checkNode=true&leaf=emp&defaultOrg=${defaultOrg}"></fhdcore:treeNode>
		<fhdcore:treeNode nodeType="posi" url="${ctx}/sys/orgstructure/posi/loadPosiTree.do?checkNode=true"></fhdcore:treeNode>
	</fhdcore:tree>
	<div id="showResult" style="float:left;width:310px;height:document.body.offsetHeight">
		<table class="selectorTable" cellpadding="0" cellspacing="0" border="0">
			<tr><th height="25px" colspan="2" align="center" valign="middle">选择结果</th></tr>
			<tr bgcolor="#F7FDFD" >
				<td bgcolor="#F7FDFD" height="25px" align="center" valign="middle">
					<a href="javascript:clearHasCheckedValues()">清空</a>
				</td>
				<td width="275px" align="center" valign="middle">员工列表</td>
			</tr>
			<tr><td height="275px" colspan="2" align="left" valign="top" id="chooseValues"></td></tr>
			<tr bgcolor="#F7FDFD">
				<td height="40px" colspan="2" align="center" valign="middle">
					<input type="button" id="submits" value="确定" onclick="confirmSelect()" class="fhd_btn"/>&nbsp;
					<input type="button" value="取消" onclick="closeDialogWindow()" class="fhd_btn"/>
				</td>
			</tr>
		</table>
	</div>
	<script type="text/javascript">
		var selected='${selects}';
		var chooseValues = document.getElementById("chooseValues");
		// 注意！这行代码是每个内容页必须加的内容，否则无法正确显示。
		var P = parentWindow();
		var tags = 'treeSelectId';
		var parentTag = 'div'+tags;
	
		function remove(id) {
			var idObject = document.getElementById(id);
			if (idObject != null){
				idObject.parentNode.removeChild(idObject);
			}

			var chooseValues = document.getElementById("chooseValues").innerHTML;
			//清空后，确定按钮置灰
			if(null == chooseValues || "" == chooseValues){
				$("#submits").attr("disabled","disabled");
			}
		}
		
		function clearHasCheckedValues(){
			var chooseOrgs = document.getElementsByName(tags);
			if(chooseOrgs.length>0){
				for(var i=0;i<chooseOrgs.length;i++)
					unCheckTreeNode(chooseOrgs[i].value);
			}
				
			chooseValues.innerHTML="";
			//清空后，确定按钮置灰
			$("#submits").attr("disabled","disabled");
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
	
		function removeCheckedEmps(tags, id) {
			unCheckTreeNode(id); 
			remove(id);
		}
	
		function closeDialogWindow(){
			closeWindow();
		}
	
		function handleSelect(node, show){
			var nodeId = node.attributes.id;
			var nodeText = node.attributes.text;
			if(document.getElementById(nodeId+"") != null){
				return;
			}
			if(node.attributes.checked){
				if(node.isLeaf() && node.attributes.checked  && show.indexOf(nodeId)<0){
					chooseValues.innerHTML +="<div id='"+ nodeId +"'><input type='checkbox' checked='checked' onClick='javascript:removeCheckedEmps(\"" + tags + "\",\"" + nodeId + "\")' name='" + tags + "' value='"+ nodeId +"' text = '"+nodeText+"' />"+ nodeText + "</div>";
				}
				
				//node.eachChild(function(child) {
					//handleSelect(child);      
			 	//});
			 	$("#submits").removeAttr("disabled");      
				return;
			 }
		}
	
		function unSelect(node){
			var nodeId = node.attributes.id;
			if(node.attributes.checked==false){
				removeCheckedEmps(tags,nodeId);
				if(!node.isLeaf()){
					node.eachChild(function(child) {unSelect(child);});
				}
			}
		}
	
	    tree.on('checkchange', function(node, checked){
		    if(checked){
		    	handleSelect(node,document.getElementById("chooseValues").innerHTML);
		    }else{
				remove(node.id);	    
		    }
	    });
		tree.on('load',function(node){
	    	node.eachChild(function(n){
    			if(selected.indexOf(n.id)>-1){
			    	n.ui.toggleCheck(true);   
					n.attributes.checked = true;
				}
	    	});
	    });
		/*
		tree.on('check', function(node, checked) {
			var show = document.getElementById("chooseValues").innerHTML;
			handleSelect(node,show);
		}, tree);
		*/
	    function loadMethod(){
			if(null == selected || ''==selected){
				chooseValues.innerHTML +="";
			}else{
				var selecteds=eval('('+selected+')');
		    	if(selecteds != null && selecteds != ""){
			    	for(var i = 0 ; i < selecteds.length; i++){
			    		chooseValues.innerHTML +="<div id='"+ selecteds[i].id +"'><input type='checkbox' checked='checked' onClick='javascript:removeCheckedEmps(\"" + tags + "\",\"" + selecteds[i].id + "\")' name='" + tags + "' value='"+ selecteds[i].id +"' text = '"+selecteds[i].empname+"' />"+ selecteds[i].empname + "</div>";
			    	}
		    	}
			}
	    }
	    
		function confirmSelect(){
			var list = document.getElementById("chooseValues");
			var els = list.getElementsByTagName('input');
			var selects = new Array();
			for(var i=0;i<els.length;i++){
					var id = els[i].value;
					var text = els[i].text;
					var o ={id:id,text:text};
					selects[selects.length]=o;
			}
			if('${param.callback}' in parentWindow()){
				parentWindow().${param.callback}(selects);
			}
			closeWindow();
		}
		
		function deleteRecorder(id){
			if(document.getElementById('div'+id)==null)return;
		    document.getElementById('div'+id).outerHTML='';
		    var node = Ext.getCmp('selectTree').getNodeById(id);
		    node.ui.toggleCheck(false);   
			node.attributes.checked = false;
		}
	</script>
</body>
</html>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<html>
<head>
	<title><spring:message code="fhd.common.risk" /></title>
</head>
<body onload = "loadMethode();">
	<table width="100%" height="550">
		<tr>
            <td rowspan="2" valign="top" width="200">
				<fhdcore:tree rootText="${orgRoot.orgname}" 
				rootHref="${ctx}/components/org/empsEx.do?parentType=org&abName=${abName}&id=${orgRoot.id}" 
				
				el="tree-div" url="${ctx}/components/org/loadOrgTree.do?abName=${tag}" 
				height="550" width="200" rootId="${orgRoot.id}" rootIconCls="org-tree-icon" enableDD="true">
					<fhdcore:treeNode nodeType="org" url="${ctx}/components/org/loadOrgTree.do?abName=${tag}"></fhdcore:treeNode>
					<%--
					<fhdcore:treeNode nodeType="posi" url="${ctx}/sys/orgstructure/posi/loadPosiTree.do"></fhdcore:treeNode>
					 --%>
				</fhdcore:tree>
			</td>
            <td valign="top" align="left">
				<iframe src="" name="mainframe" width="100%" scrolling="no" height="" frameborder="0"  style="height: 350;margin: 0;border: 0;padding: 0;" ></iframe>
			</td>
        </tr>
        <tr>
            <td valign="top" align="right">
				<div style="float:left;">
					<table class="selectorTable">
					<tr><th  height="25px" colspan="2" align="center" valign="middle">选择结果</th></tr>
					<tr bgcolor="#F7FDFD" >
					<td bgcolor="#F7FDFD" height="25px" align="left" valign="middle">
					<a href="javascript:clearHasCheckedValues()">清空</a>
					</td>
					<td width="575px" align="center" valign="middle">员工列表</td></tr>
					<tr><td  height="110px" colspan="2" align="left" valign="top" id="chooseValues"></td></tr>
					<tr bgcolor="#F7FDFD" ><td  height="40px" colspan="2" align="center" valign="middle">
					<input type="button" value="确定" onclick="confirmSelect()" class="fhd_btn"/>&nbsp;
					<input type="button" value="取消" onclick="closeDialogWindow()" class="fhd_btn"/></td></tr>
					</table>
				</div>
			</td>
        </tr>
	</table>
	<script type="text/javascript">
		var selected='${selects}';
		var chooseValues = document.getElementById("chooseValues");
		var jChooseValues=jQuery(chooseValues);
		// 注意！这行代码是每个内容页必须加的内容，否则无法正确显示。
		var P = parentWindow();
		var tags = 'treeSelectId';
		var parentTag = 'div'+tags;
		var abName="${tag}";
	
		function remove(id) {
			var idObject = document.getElementById(id);
			if (idObject != null)
				idObject.parentNode.removeChild(idObject);
		}
		
		function clearHasCheckedValues(){
			var chooseOrgs = document.getElementsByName(tags);
			if(chooseOrgs.length>0){
				for(var i=0;i<chooseOrgs.length;i++)
					unCheckTreeNode(chooseOrgs[i].value);
			}
				
			chooseValues.innerHTML="";
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
	/*
		function removeCheckedEmps(tags, id) {
			unCheckTreeNode(id); 
			remove(id);
		}*/
		function removeCheckedEmps(tags, id) {
			unCheckRecord(id); 
			remove(id);
		}
		function unCheckRecord(id){//反选 mainframe 中的list表中的checkbox 如果还是在当前页面
		
			if(!mainframe.mv_grid){
				return;
			}
			var grid = mainframe.mv_grid.grid;
			var sm = grid.getSelectionModel();
			var store = grid.getStore();
			var view = grid.getView();
			for(var i = 0; i < view.getRows().length; i ++){
				var record = store.getAt(i);
				var userid = record.get("userid");
				if(id == userid){
					sm.deselectRow(i);
					return;
				}
			}
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
					var nodeDivStr="<div id='"+ nodeId +"'><input type='checkbox' checked='checked' onClick='javascript:removeCheckedEmps(\"" + tags + "\",\"" + nodeId + "\")' name='" + abName + "' value='"+ nodeId +"' text = '"+nodeText+"' /><span ext:qtip='"+nodeText+"'>"+ nodeText + "</span></div>";
					var nodeDiv=jQuery(nodeDivStr);
					jChooseValues.append(nodeDiv);
				}
				
				//node.eachChild(function(child) {
					//handleSelect(child);      
			 	//});
			 	
				return;  
			 }
		}
	
		function unSelect(node){
			var nodeId = node.attributes.id;
			if(node.attributes.checked==false){
				removeCheckedEmps(tags,nodeId);
				if(!node.isLeaf())
					node.eachChild(function(child) {unSelect(child);});
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
					}else{
					}
	    	})
	    });
		/**tree.on('check', function(node, checked) {
			var show = document.getElementById("chooseValues").innerHTML;
			handleSelect(node,show);
		}, tree);**/
		
	    function loadMethode(){
	    	var selecteds=eval('('+selected+')');
	    	if(selecteds != null && selecteds != ""){
		    	for(var i = 0 ; i < selecteds.length; i++){
		    		chooseValues.innerHTML +="<span id='"+ selecteds[i].id +"'><input type='checkbox' checked='checked' onClick='javascript:removeCheckedEmps(\"" + tags + "\",\"" + selecteds[i].id + "\")' name='" + abName + "' value='"+ selecteds[i].id +"' text = '"+selecteds[i].realname+"' />"+ selecteds[i].realname + "</span>";
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
			if('beforeHandler' in parentWindow(selects)){
				parentWindow().beforeHandler(selects);
			}
			parentWindow().document.getElementById('div'+abName).innerHTML=chooseValues.innerHTML;
			if('afterHandler' in parentWindow(selects)){
				parentWindow().afterHandler(selects);
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
		//FHD.util.UI.expand('mainframe');
	</script>
</body>
</html>
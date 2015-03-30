<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<html>
<base target="_self">
<head>
	<title><spring:message code="risk" /></title>
	<script type="text/javascript">
		var selected=',${param.selects},';
		function confirmSelect(){
			var list = document.getElementById('list');
			var els = list.getElementsByTagName('div');
			var selects = new Array();
			for(var i=0;i<els.length;i++)
				if(/^div.*$/.test(els[i].id)){
					var id = els[i].id.replace('div','');
					var text = els[i].getElementsByTagName('span')[0].innerText;
					var o ={id:id,text:text};
					selects[selects.length]=o;
				}
			parentWindow().${param.callback}(selects);
			closeWindow();
		}
		function deleteRecorder(id){
			if(document.getElementById('div'+id)==null)return;
		    document.getElementById('div'+id).outerHTML='';
		    var node = Ext.getCmp('selectTree').getNodeById(id);
		    node.ui.toggleCheck(false);   
			node.attributes.checked = false;
		}
		function checkedNode(node){
			var odiv = document.createElement('div');
			odiv.id='div'+node.id;
			var delBtn = document.createElement('a');delBtn.innerText='删除';delBtn.href='javascript:deleteRecorder("'+node.id+'")'
			var otext = document.createElement('span');otext.innerText='  '+node.text+'   ';
			odiv.appendChild(otext);
			odiv.appendChild(delBtn);
			document.getElementById('list').appendChild(odiv);
		}
		function selectAllDepartments(){
			clearAllDepartments();
			var childNodes = tree.getRootNode().childNodes;
			for(var i=0;i<childNodes.length;i++){
				var node = childNodes[i];
				node.ui.toggleCheck(true);   
				node.attributes.checked = true;
				checkedNode(childNodes[i]);
				/*
				var id = node.id;
				var text = node.text;
				setTimeout(function(){
					var odiv = document.createElement('div');
					odiv.id='div'+id;
					var delBtn = document.createElement('a');delBtn.innerText='删除';delBtn.href='javascript:deleteRecorder("'+id+'")'
					var otext = document.createElement('span');otext.innerText='  '+text+'   ';
					odiv.appendChild(otext);
					odiv.appendChild(delBtn);
					document.getElementById('list').appendChild(odiv);
				},1000);
				*/
			}
		}
		function clearAllDepartments(){
			var list = document.getElementById('list');
			var els = list.getElementsByTagName('div');
			var selects = new Array();
			for(var i=0;i<els.length;i++){
				if(/^div.*$/.test(els[i].id)){
					var id = els[i].id.replace('div','');
					selects[selects.length]=id;
				}
			}
			for(var j=0;j<selects.length;j++){
				deleteRecorder(selects[j]);
			}
		}
		var tree;
		Ext.onReady(function(){
		    var treeloader = new Ext.tree.TreeLoader({
		    	baseAttrs:{uiProvider:Ext.ux.TreeCheckNodeUI},
		    	dataUrl:'${ctx}/sys/orgstructure/org/loadPositionTree.do?leaf=org'
		    });
		    tree = new Ext.tree.TreePanel({
		    	id:'selectTree',
		    	el:'tree-div',autoScroll:true,height:330,width:200,animate:true,
		    	rootVisible:false,lines:null,hlColor:null,hlDrop:false,
		    	onlyLeafCheckable:false,containerScroll:true,
		    	loader:treeloader});
		    var root = new Ext.tree.AsyncTreeNode({id:'${root.id}'});
		    tree.setRootNode(root);tree.render();root.expand();
		    
		    tree.on('checkchange', function(node, checked){
			    if(checked){
			    	checkedNode(node);
			    }else{
					deleteRecorder(node.id);	    
			    }
		    });
		    tree.on('load',function(node){
		    	node.eachChild(function(n){
	    			//try{n.expand()}catch(e){};
	    			if(selected.indexOf(','+n.id+',')>-1){
				    	n.ui.toggleCheck(true);   
						n.attributes.checked = true;
						checkedNode(n);
					}
		    	});
		    });
		});
	</script>
</head>
<body>
	<table style='width:100%' border=0>
		<tr>
			<td style='width:200px'><div id='tree-div' ></div></td>
			<td style='vertical-align:top;text-align:center'>
				<table class='selectorTable'>
					<tr>
						<th>已选择<spring:message code="org" /></th>
					</tr>
					<tr>
						<td><div id='list' style='height:300px;overflow-y:auto'></div></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td colspan='2' style='text-align:right'>
				<!--
				<input type='button' value='全选' onclick="selectAllDepartments()" class="fhd_btn">
				-->
				<input type='button' value='确定' onclick="confirmSelect()" class="fhd_btn">
				<input type='button' value='关闭' onclick="closeWindow()" class="fhd_btn">
				<input type='button' value='清空' onclick="clearAllDepartments()" class="fhd_btn">
			</td>
		</tr>
	</table>
</body>
</html>
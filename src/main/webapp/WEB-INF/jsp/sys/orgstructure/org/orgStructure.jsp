<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="fhd.sys.orgstructure.org.org" /></title>
</head>
<body style="overflow:hidden">
<div id="west" >
	
<div id="org-tree" style="float:left;"></div>
    </div>
       <div id="center" > 
        	<iframe src="" id="mainframe" name="mainframe" width="100%" scrolling="no" height="0" frameborder="0"></iframe>
       </div>
  

<script type="text/javascript">
var tree;var treeloader;var root;
var Tree = Ext.tree;
var Treeheight = Ext.getBody().getHeight();
treeloader = new Tree.TreeLoader({dataUrl:'${ctx}/sys/orgstructure/org/loadOrgTree.do'});
tree = new Tree.TreePanel({
	  el:'org-tree',
	  height:Treeheight - 26,
	  width:200,
	  border:false,
	  useArrows: true,
	  autoScroll: true,
	  animate: true,
	  enableDD: true,
	  lines:null,
	  hlColor:null,
	  hlDrop:false,
	  containerScroll: true,
	  border: true,
	  loader:treeloader
});
var root = new Tree.AsyncTreeNode({
	text:'${orgRoot.orgname}',
	draggable:false,
	href:'${ctx}/sys/orgstructure/org/tabs.do?id=${orgRoot.id}',
	hrefTarget:'mainframe',
	iconCls:'icon-package-icon',
	cls:'root',
	dbid:'${orgRoot.id}',
	id:'${orgRoot.id}2345'
});
tree.setRootNode(root);
tree.render();
root.expand();

treeloader.on("beforeload", function(treeLoader, node) {
	var cls = node.attributes.cls;
	if('org' === cls)treeloader.dataUrl = '${ctx}/sys/orgstructure/org/loadOrgTree.do';
	if('posi' === cls)treeloader.dataUrl = '${ctx}/sys/orgstructure/posi/loadPosiTree.do';
});


	tree.on('check', function(node, checked) {
		/*
		              递归选择checkbox
		
		node.expand();   
		node.attributes.checked = checked;   
		node.eachChild(function(child) {   
		    child.ui.toggleCheck(checked);   
		    child.attributes.checked = checked;   
		    child.fireEvent('checkchange', child, checked);   
		});
		 */
	}, tree);
	
	tree.on('beforemovenode',function(tree,node,oldParent,newParent,index){
		if(!newParent.leaf && oldParent.id != newParent.id){
			var message;
			var ret = window.showModalDialog("${ctx}/sys/orgstructure/org/choose.do",window,"dialogWidth:200px;dialogHeight:20px,center:yes,resizable:no,status:no");
			if(ret == "move"){
				$.ajax({
					type: "GET",
					async: false,
					url: "${ctx}/sys/orgstructure/emp/move.do",
					data: "currentId="+node.id+"&pid="+oldParent.id+"&targetId="+newParent.id+"&ptype="+oldParent.attributes.cls+"&type="+newParent.attributes.cls,
					success: function(msg){
						if("true"==msg){
							window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", "<spring:message code='fhd.common.operateSuccess'/>");
							message = msg;
						}else{
							window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", "<spring:message code='fhd.common.operateFailure'/>");
							message = msg;
						}
					}
				});
				if("false"==message){
					return false;
				}
			}else if(ret == "copy"){
				$.ajax({
					type: "GET",
					async: false,
					url: "${ctx}/sys/orgstructure/emp/copy.do",
					data: "currentId="+node.id+"&targetId="+newParent.id+"&type="+newParent.attributes.cls,
					success: function(msg){
						if("true"==msg){
							window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", "<spring:message code='fhd.common.operateSuccess'/>");
							message = msg;
						}else{
							window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", "<spring:message code='fhd.common.operateFailure'/>");
							message = msg;
						}
					}
				});
				if("true"==message){
					if("root" === newParent.attributes.cls){
						tree.getRootNode().reload();
					}else{
						newParent.reload();
					}
					return false;
				}else{
					return false;
				}
			}else if(ret == "cancel"){
				return false;
			}else{
				return false;				
			}
		}else{
			return false;
		}
	});

	tree.on('contextmenu', onContextMenu);
	function onContextMenu(node, e) {
		var type = node.attributes.cls;
		//alert(type);
		var menu;
		if (!menu) {
			if (type === "root") {
				menu = new Ext.menu.Menu([
					{
						id : node.id + 'addorg'+ Math.random(),
						text : '<spring:message code="fhd.sys.orgstructure.org.addNextOrg"/>',
						iconCls : 'icon-add',
						handler : function() {
							FHD.openWindow("<spring:message code='fhd.sys.orgstructure.org.addorg'/>", 800,340, "${ctx}/sys/orgstructure/org/add.do?id="+node.attributes.dbid,'no');
							//selectNodeReload();
						}
					},
					'-',
					{

						id : node.id + 'addposi'+ Math.random(),
						text : '<spring:message code="fhd.sys.orgstructure.org.addNextPosi"/>',
						iconCls : 'icon-add',
						handler : function() {
							FHD.openWindow("<spring:message code='fhd.sys.orgstructure.posi.addposi'/>", 800,215, "${ctx}/sys/orgstructure/posi/add.do?id="+node.attributes.dbid,'no');
							//selectNodeReload();
						}
					},
					'-',
					{
						id : node.id + 'addemp'+ Math.random(),
						text : '<spring:message code="fhd.sys.orgstructure.org.addNextEmp"/>',
						iconCls : 'icon-add',
						handler : function() {
							FHD.openWindow("<spring:message code='fhd.sys.orgstructure.emp.addemp'/>", 800,466, "${ctx}/sys/orgstructure/emp/add.do?id="+node.attributes.dbid);
							selectNodeReload();
						}
					},
					'-',
					{
						id : node.id + 'refresh'+ Math.random(),
						text : '<spring:message code="fhd.common.refresh"/>',
						iconCls : 'icon-arrow-refresh-blue',
						handler : function() {
							tree.getRootNode().reload();
						}
					}
				]);
			}
			
			if(true == node.attributes.leaf){

				if (type === "org") {
					
					menu = new Ext.menu.Menu([
						{
							id : node.id + 'addorg'+ Math.random(),
							text : '<spring:message code="fhd.sys.orgstructure.org.addNextOrg"/>',
							iconCls : 'icon-add',
							handler : function() {
								FHD.openWindow("<spring:message code='fhd.sys.orgstructure.org.addorg'/>", 800,340, "${ctx}/sys/orgstructure/org/add.do?id="+node.attributes.dbid,'no');
								//tree.getSelectionModel().getSelectedNode().reload();
							}
						},
						'-',
						{
							id : node.id + 'addposi'+ Math.random(),
							text : '<spring:message code="fhd.sys.orgstructure.org.addNextPosi"/>',
							iconCls : 'icon-add',
							handler : function() {
								FHD.openWindow("<spring:message code='fhd.sys.orgstructure.posi.addposi'/>", 800,215, "${ctx}/sys/orgstructure/posi/add.do?id="+node.attributes.dbid,'no');
								//tree.getSelectionModel().getSelectedNode().reload();
							}
						},
						'-',
						{
							id : node.id + 'addemp'+ Math.random(),
							text : '<spring:message code="fhd.sys.orgstructure.org.addNextEmp"/>',
							iconCls : 'icon-add',
							handler : function() {
								FHD.openWindow("<spring:message code='fhd.sys.orgstructure.emp.addemp'/>", 800,467, "${ctx}/sys/orgstructure/emp/add.do?id="+node.attributes.dbid);
								//tree.getSelectionModel().getSelectedNode().reload();
							}
						},
						'-',
						{
							id : node.id + 'delorg'+ Math.random(),
							text : '<spring:message code="fhd.sys.orgstructure.org.delOrg"/>',
							iconCls : 'icon-del',
							handler : function() {
								Response = confirm('<spring:message code="fhd.common.delconfirm" />');
								if(Response == true){
									$.ajax({
										type: "GET",
										url: "${ctx}/sys/orgstructure/org/delete.do",
										data: "id="+node.attributes.dbid,
										success: function(msg){
											if("true"==msg){
												window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", "<spring:message code='fhd.common.operateSuccess'/>");
												node.parentNode.reload();
											}else{
												window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", "有子集不能删除");
											}
										}
									}); 
									node.parentNode.reload();
								}
							}
						}
					]);
				}
				
			}
			if(false == node.attributes.leaf){
				
				if (type === "org") {
					menu = new Ext.menu.Menu([
						{
							id : node.id + 'addorg'+ Math.random(),
							text : '<spring:message code="fhd.sys.orgstructure.org.addNextOrg"/>',
							iconCls : 'icon-add',
							handler : function() {
								FHD.openWindow('添加机构', 800,340, "${ctx}/sys/orgstructure/org/add.do?id="+node.attributes.dbid,'no');
								//tree.getSelectionModel().getSelectedNode().reload();
							}
						},
						'-',
						{
							id : node.id + 'addposi'+ Math.random(),
							text : '<spring:message code="fhd.sys.orgstructure.org.addNextPosi"/>',
							iconCls : 'icon-add',
							handler : function() {
								FHD.openWindow('添加岗位', 800,215, "${ctx}/sys/orgstructure/posi/add.do?id="+node.attributes.dbid,'no');
								//tree.getSelectionModel().getSelectedNode().reload();
							}
						},
						'-',
						{
							id : node.id + 'addemp'+ Math.random(),
							text : '<spring:message code="fhd.sys.orgstructure.org.addNextEmp"/>',
							iconCls : 'icon-add',
							handler : function() {
								FHD.openWindow('添加员工', 800,466, "${ctx}/sys/orgstructure/emp/add.do?id="+node.attributes.dbid);
								//tree.getSelectionModel().getSelectedNode().reload();
							}
						},
						'-',
						{
							id : node.id + 'delorg'+ Math.random(),
							text : '<spring:message code="fhd.sys.orgstructure.org.delOrg"/>',
							iconCls : 'icon-del',
							handler : function() {
								Response = confirm('<spring:message code="fhd.common.delconfirm" />');
								
								if(Response == true){
									$.ajax({
										type: "GET",
										url: "${ctx}/sys/orgstructure/org/delete.do",
										data: "id="+node.attributes.dbid,
										success: function(msg){
											if("true"==msg){
												window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", "<spring:message code='fhd.common.operateSuccess'/>");
											}else{
												window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", "有子集不能删除");
											}
										}
									}); 
									selectNodeReload();
									//tree.getSelectionModel().getSelectedNode().parentNode.reload();
								}
							}
						},
						'-', 
						{
							id : node.id + 'refresh'+ Math.random(),
							text : '<spring:message code="fhd.common.refresh"/>',
							iconCls : 'icon-arrow-refresh-blue',
							handler : function() {
								node.reload();
							}
						} 
					]);
				}

			}
			if (type === "posi") {
				menu = new Ext.menu.Menu([
					{
						id : node.id + 'addposi'+ Math.random(),
						text : '<spring:message code="fhd.sys.orgstructure.org.addNextEmp"/>',
						iconCls : 'icon-add',
						handler : function() {
							FHD.openWindow("<spring:message code='fhd.sys.orgstructure.emp.addemp'/>", 800,466, "${ctx}/sys/orgstructure/emp/fromPosiadd.do?id="+node.attributes.dbid);
							//FHD.openWindow('添加员工', 800,600, "${ctx}/sys/orgstructure/emp/queryEmployeeByOrg.do?id="+node.id);
							//selectNodeReload();
						}
					},
					'-',
					{
						id : node.id + 'delposi'+ Math.random(),
						text : '<spring:message code="fhd.sys.orgstructure.org.delPosi"/>',
						iconCls : 'icon-del',
						handler : function() {
							Response = confirm('<spring:message code="fhd.common.delconfirm" />');
							if(Response == true){
								$.ajax({
									type: "GET",
									url: "${ctx}/sys/orgstructure/posi/delete.do",
									data: "id="+node.attributes.dbid,
									success: function(msg){
										if("true"==msg){
											window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", "<spring:message code='fhd.common.operateSuccess'/>");
										}else{
											window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", "<spring:message code='fhd.common.operateFailure'/>");
										}
									}
								}); 
								node.parentNode.reload();
							}
						}
					}, '-', {
						id : node.id + 'refresh'+ Math.random(),
						text : '<spring:message code="fhd.common.refresh"/>',
						iconCls : 'icon-arrow-refresh-blue',
						handler : function() {
							node.reload();
						}
					}
				]);
			}

			if (type === "emp") {
				menu = new Ext.menu.Menu([
					{
						id : node.id + 'delemp'+ Math.random(),
						text : '<spring:message code="fhd.sys.orgstructure.org.delEmp"/>',
						iconCls : 'icon-del',
						handler : function() {
							Response = confirm('<spring:message code="fhd.common.delconfirm" />');
							if(Response == true){
								$.ajax({
									type: "GET",
									url: "${ctx}/sys/orgstructure/emp/delete.do",
									data: "eid="+node.attributes.dbid+"&pid="+node.parentNode.attributes.dbid+"&type="+node.parentNode.attributes.cls,
									success: function(msg){
										window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", "<spring:message code='fhd.common.operateSuccess'/>");
								    }
								}); 
								node.parentNode.reload();
							}
						}
					} 
				]);
			}
		}

		e.preventDefault();
		node.select();  
		menu.showAt(e.getPoint());			
	}
	
	// 选中结点刷新
	function selectNodeReload() {
		var selectNode  = tree.getSelectionModel().getSelectedNode();
		if(selectNode!=null){
		if(true == selectNode.attributes.leaf){
			selectNode.parentNode.reload();
		}else{
			selectNode.reload();
		}
		selectNode.select();
		}
	}

	// 选中结点父结点刷新后再选中该结点
	function parentNodeLoad(){
		var selectNode =  tree.getSelectionModel().getSelectedNode();
		selectNode.parentNode.reload();
		selectNode.select();
	}
	var height;
	
	Ext.onReady(function(){
		 height=Ext.getBody().getHeight();
		//document.getElementById("mainFrame").src="${ctx}/sys/orgstructure/org/tabs.do?id=${orgRoot.id}"; 
		 var viewport = new Ext.Panel({
	           layout: 'border',
	       	   id:'viewportPanel',
	           renderTo:document.body,
	           height:height,
	           items: [
	         {
	               region: 'west',
	               id: 'west-panel', 
	               title: '',
	               border:false,
	               collapsible: true,
	               split: true,
	               width: 200,
	               minSize: 200,
	               maxSize: 200,
	               contentEl: 'west' 
	           },
	           {
	               title: '',
	               contentEl: 'center',
	               border:false,
	               collapsible: false,
	               region:'center',
	               height:height,
	               margins: '0 0 0 0',
	       	       html:'<iframe id="mainframe" height="'+height+'" src="${ctx}/sys/orgstructure/org/tabs.do?id=${orgRoot.id}&nexduty=true"  name="mainframe" width="100%" scrolling="no" border="0"  frameborder="0"></iframe>'

	           }]
	       });
		 Ext.EventManager.onWindowResize(function(width ,height){
				Ext.getCmp("viewportPanel").setWidth(width);
				Ext.getCmp("viewportPanel").setHeight(height);
				
			});
	  });
</script>
</body>
</html>
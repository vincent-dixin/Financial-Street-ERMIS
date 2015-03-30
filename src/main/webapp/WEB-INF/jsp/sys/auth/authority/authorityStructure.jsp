<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<html>
<head>
	<title><spring:message code="fhd.sys.auth.authority.authority" /><spring:message code="fhd.common.manage"/></title>
</head>
<body style="overflow:hidden" >
<script type="text/javascript">
	var tree;
	Ext.onReady(function(){
		var height=Ext.getBody().getHeight();
		var width=Ext.getBody().getWidth();
		var loadDate = new Ext.tree.TreeLoader({
			dataUrl : '${ctx}/sys/auth/loadAuthorityTree.do'
		 });
		 var root=new Ext.tree.AsyncTreeNode({
		   id:'${orgRoot.id}',
		   text:'${orgRoot.authorityName}',
		   expanded:true,
		   href:'${ctx}/sys/auth/authority/tabs.do?id=${orgRoot.id}',
	       hrefTarget:'mainframe',
		   cls:'root',
		   iconCls:'authority-tree-icon'
		 });
		  tree=new Ext.tree.TreePanel({
		  id:"tree",
		  region: 'west',
		  width:'200',
		  height:height,
		  border:false,
		  useArrows: true,
		  autoScroll: true,
		  animate: true,
		  enableDD: true,
		  collapsible:true,
		  containerScroll: true,
		  border: true,
		  loader:loadDate,
		  root:root,
		  cls:'authority'
		});
		/*
		var field=new Ext.ux.form.SearchField({
		     store:store
		    });
		    
		var panel=new Ext.Panel({
			 region: 'west',
			 collapsible:true,
		    width:'200',
			 layout:{type:'vbox',
			 padding:'2',
			 align:'stretch'},
		    items: [
			   field,
		      tree
		    ]
		});
		*/
		var tabs = new Ext.Panel({
		     id:"tabs",
			 region:'center',
			 margins: '0 0 0 0',
			 id:'center-panel',
			 hideBorders:true,
			 height:height,
			 width:'auto',
			 border:false,
		    activeTab: 0,
		    // plain:true, 
		    items:[{
		        id:"iframe",
		        height:height,
		        width:'auto',
		        html:'<iframe id="mainframe" src="${ctx}/sys/auth/authority/tabs.do?id=${orgRoot.id}"  name="mainframe" width="100%" scrolling="no"   frameborder="0"></iframe>'
		    }]
		});
		var viewport=new Ext.Panel({
			id:'viewportPanel',
			renderTo :document.body,
		   layout:'border',
		   height:height,
		   items:[tree,tabs]
		});
		Ext.get('mainframe').setHeight(height); 
		Ext.EventManager.onWindowResize(function(width ,height){
			Ext.getCmp("viewportPanel").setWidth(width);
			Ext.getCmp("viewportPanel").setHeight(height);
			Ext.getCmp("center-panel").setHeight(height);
			Ext.getCmp("center-panel").setWidth(width);
			Ext.getCmp("viewportPanel").doLayout();
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
				var ret = window.showModalDialog("${ctx}/sys/auth/authority/choose.do",window,"dialogWidth:220px;dialogHeight:50px,center:yes,resizable:no,status:no");
				if(ret == "move"){
					$.ajax({
						type: "GET",
						async: false,
						url: "${ctx}/sys/auth/authority/move.do",
						data: "currentId="+node.id+"&pid="+oldParent.id+"&targetId="+newParent.id,
						success: function(msg){
							if("true"==msg){
								window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>",'<spring:message code="fhd.common.operateSuccess"/>');
								message = msg;
							}else{
								window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>",'<spring:message code="fhd.common.operateFailure"/>');
								message = msg;
							}
						}
					});
					if("false"==message){
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
					menu = new Ext.menu.Menu( [
							{
								id : node.id + 'addauthority'+ Math.random(),
								text : '<spring:message code="fhd.common.add"/><spring:message code="fhd.sys.auth.authority.nextAuthority"/>',
								iconCls : 'icon-add',
								handler : function() {
									FHD.openWindow('<spring:message code="fhd.common.add"/><spring:message code="fhd.sys.auth.authority.nextAuthority"/>', 480,228, "${ctx}/sys/auth/authority/add.do?id="+node.id+"&test="+"ca",'no');
									closeWindow();
									//var ret = window.showModalDialog("${ctx}/sys/auth/authority/add.do?id="+node.id,window,"dialogWidth:500px;dialogHeight:240px,center:yes,resizable:no,status:no");
									tree.getRootNode().reload();
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
							} ]);
				}
				
				if(true == node.attributes.leaf){
	
					if (type === "authority") {
						menu = new Ext.menu.Menu( [
						       				
								{
									
									id : node.id + 'addauthority'+ Math.random(),
									text : '<spring:message code="fhd.common.add"/><spring:message code="fhd.sys.auth.authority.nextAuthority"/>',
									iconCls : 'icon-add',
									handler : function() {
										//var ret = window.showModalDialog("${ctx}/sys/auth/authority/add.do?id="+node.id,window,"dialogWidth:500px;dialogHeight:240px,center:yes,resizable:no,status:no");
										FHD.openWindow('<spring:message code="fhd.common.add"/><spring:message code="fhd.sys.auth.authority.nextAuthority"/>',480,228, "${ctx}/sys/auth/authority/add.do?id="+node.id,'no');
									}
									
								},
								
								'-',
								
								{
									id : node.id + 'delauthority'+ Math.random(),
									text : '<spring:message code="fhd.common.delete"/><spring:message code="fhd.sys.auth.authority.authority"/>',
									iconCls : 'icon-del',
									handler : function() {
										Response = confirm('<spring:message code="fhd.common.makeSureDelete" />');
										if(Response == true){
											$.ajax({
												type: "GET",
												async: false,
												url: "${ctx}/sys/auth/authority/delete.do",
												data: "id="+node.id,
												success: function(msg){
													if("true"==msg){
														window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>",'<spring:message code="fhd.common.operateSuccess"/>');
													}else{
														window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>",'<spring:message code="fhd.common.operateFailure"/>'+msg);
													}
												}
											}); 
											if(null==tree.getSelectionModel().getSelectedNode().parentNode.parentNode){
												tree.getRootNode().reload();
											}else{
											tree.getSelectionModel().getSelectedNode().parentNode.parentNode.reload();
											}
										}
									}
								}
								
						 ]);
					}
					
				}
				if(false == node.attributes.leaf){
				
					if (type === "authority") {
						menu = new Ext.menu.Menu( [
								{
									id : node.id + 'addauthority'+ Math.random(),
									text : '<spring:message code="fhd.common.add"/><spring:message code="fhd.sys.auth.authority.nextAuthority"/>',
									iconCls : 'icon-add',
									handler : function() {
										FHD.openWindow('<spring:message code="fhd.common.add"/><spring:message code="fhd.sys.auth.authority.nextAuthority"/>', 480,228, "${ctx}/sys/auth/authority/add.do?id="+node.id,'no');
										//var ret = window.showModalDialog("${ctx}/sys/auth/authority/add.do?id="+node.id,window,"dialogWidth:500px;dialogHeight:240px,center:yes,resizable:no,status:no");
										tree.getSelectionModel().getSelectedNode().reload();
									}
								},
								'-',
								{
									id : node.id + 'delauthority'+ Math.random(),
									text : '<spring:message code="fhd.common.delete"/><spring:message code="fhd.sys.auth.authority.authority"/>',
									iconCls : 'icon-del',
									handler : function() {
										Response = confirm('<spring:message code="fhd.common.makeSureDelete" />');
										if(Response == true){
											$.ajax({
												type: "GET",
												async: false,
												url: "${ctx}/sys/auth/authority/delete.do",
												data: "id="+node.id,
												success: function(msg){
													if("true"==msg){
														window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>",'<spring:message code="fhd.common.operateSuccess"/>');
													}else{
														window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>",'<spring:message code="fhd.common.operateFailure"/>'+msg);
													}
												}
											}); 
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
			}
	
			e.preventDefault();
			node.select();  
			menu.showAt(e.getPoint());		
		}
	});
	function selectNodeReload() {
		if(null==tree.getSelectionModel().getSelectedNode().parentNode){
			tree.getSelectionModel().getSelectedNode().reload();
		}else{
		tree.getSelectionModel().getSelectedNode().parentNode.reload();
			}
	}
	
	
	function selectNodeReload2() {
		tree.getSelectionModel().getSelectedNode().parentNode.reload();
	}
	// 根据结点id刷新该结点
	function nodeReload(id){
		tree.getId(id).reload();
	}
	
</script>
</body>
</html>
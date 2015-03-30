<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<html>
<head>
	<title><spring:message code="menu" /><spring:message code="manage"/></title>
	<script type="text/javascript" src="${ctx}/scripts/ext-3.4.0/examples/ux/SearchField.js"></script>
</head>
<body style="overflow:hidden"> 
<script type="text/javascript">
	 var height=Ext.getBody().getHeight();
	 var width=Ext.getBody().getWidth();
	 var loadDate = new Ext.tree.TreeLoader({
		dataUrl : '${ctx}/sys/menu/loadMenuTree.do'
	  });
      var root=new Ext.tree.AsyncTreeNode({
	    id:'${orgRoot.id}',
        text:'${orgRoot.name}',
        expanded:true,
        href:'${ctx}/sys/menu/tabs.do?id=${orgRoot.id}',
        hrefTarget:'mainframe',
        cls:'root',
        iconCls:'menu-tree-icon'
	  });
     var tree=new Ext.tree.TreePanel({
       id:"tree",
       region: 'west',
	   width:'200',
	   border:false,
	   frame:false,
	   height:height,
       useArrows: true,
       autoScroll: true,
       animate: true,
       enableDD: true,
       collapsible:true,
       containerScroll: true,
       border: true,
       loader:loadDate,
       root:root,
       cls:'menu'
    });
    Ext.onReady(function(){
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
		 border:false,
		 hideBorders:true,
		 height:height,
		 width:'auto',
	     activeTab: 0,
	     // plain:true, 
         items:[{
             id:"iframe",
             height:height,
             width:'auto',
             html:'<iframe id="mainframe" src="${ctx}/sys/menu/tabs.do?id=${orgRoot.id}"  name="mainframe" width="100%" scrolling="no"   frameborder="0"></iframe>'
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
		alert(checked);
		alert(node.attributes.id);
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
	},tree);
	
	tree.on('beforemovenode',function(tree,node,oldParent,newParent,index){
		if(!newParent.leaf && oldParent.id != newParent.id){
			var message;
			var ret = window.showModalDialog("${ctx}/sys/menu/choose.do",window,"dialogWidth:200px;dialogHeight:20px,center:yes,resizable:no,status:no");
			if(ret == "move"){
				$.ajax({
					type: "GET",
					async: false,
					url: "${ctx}/sys/menu/move.do",
					data: "currentId="+node.id+"&pid="+oldParent.id+"&targetId="+newParent.id,
					success: function(msg){
						if("true"==msg){
							alert("<spring:message code="success"/>");
							message = msg;
						}else{
							alert("<spring:message code="fail"/>");
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
							//id : node.id + 'addmenu'+ Math.random(),
							text : '<spring:message code="add"/><spring:message code="nextMenu"/>',
							iconCls : 'icon-add',
							handler : function() {
							FHD.openWindow('<spring:message code="add"/><spring:message code="nextMenu"/>', 515,213, "${ctx}/sys/menu/add.do?id="+node.id,'no');
							
								//var ret = window.showModalDialog("${ctx}/sys/menu/add.do?id="+node.id,window,"dialogWidth:500px;dialogHeight:240px,center:yes,resizable:no,status:no");
								//if(ret == "refresh"){
									tree.getRootNode().reload();
								//}
							}
						},
						'-',
						{
							//id : node.id + 'refresh'+ Math.random(),
							text : '<spring:message code="refresh"/>',
							iconCls : 'icon-arrow-refresh-blue',
							handler : function() {
								tree.getRootNode().reload();
							}
						} ]);
			}
			
			if(true == node.attributes.leaf){
	
				if (type === "menu") {
					menu = new Ext.menu.Menu( [
							{
								//id : node.id + 'addmenu'+ Math.random(),
								text : '<spring:message code="add"/><spring:message code="nextMenu"/>',
								iconCls : 'icon-add',
								disabled :true,
								handler : function() {
								FHD.openWindow('<spring:message code="add"/><spring:message code="nextMenu"/>', 515,213, "${ctx}/sys/menu/add.do?id="+node.id,'no');
								
									//var ret = window.showModalDialog("${ctx}/sys/menu/add.do?id="+node.id,window,"dialogWidth:500px;dialogHeight:240px,center:yes,resizable:no,status:no");
									//if(ret == "refresh"){
										tree.getSelectionModel().getSelectedNode().reload();
									//}
								}
							},
							'-',
							{
								//id : node.id + 'delmenu'+ Math.random(),
								text : '<spring:message code="del"/><spring:message code="menu"/>',
								iconCls : 'icon-del',
								handler : function() {
								
									Response = confirm('<spring:message code="delconfirm" />');
									if(Response == true){
										$.ajax({
											type: "GET",
											async: false,
											url: "${ctx}/sys/menu/delete.do",
											data: "id="+node.id,
											success: function(msg){
												if("true"==msg){
													window.top.Ext.ux.Toast.msg('信息',"<spring:message code="success"/>");
													//alert("<spring:message code="success"/>");
												}else{
													window.top.Ext.ux.Toast.msg('信息',"<spring:message code="delfailure"/>");
													//alert("<spring:message code="delfailure"/>");
												}
											}
										}); 
										tree.getSelectionModel().getSelectedNode().parentNode.reload();
									}
										
								}
							}
					 ]);
				}
				
			}
			if(false == node.attributes.leaf){
			
				if (type === "menu") {
					menu = new Ext.menu.Menu( [
							{
								//id : node.id + 'addmenu'+ Math.random(),
								text : '<spring:message code="add"/><spring:message code="nextMenu"/>',
								iconCls : 'icon-add',
								handler : function() {
								FHD.openWindow('<spring:message code="add"/><spring:message code="nextMenu"/>', 515,213, "${ctx}/sys/menu/add.do?id="+node.id,'no');
								
								//	var ret = window.showModalDialog("${ctx}/sys/menu/add.do?id="+node.id,window,"dialogWidth:500px;dialogHeight:240px,center:yes,resizable:no,status:no");
									//if(ret == "refresh"){
										tree.getSelectionModel().getSelectedNode().reload();
									//}
								}
							},
							'-',
							{
								//id : node.id + 'delmenu'+ Math.random(),
								text : '<spring:message code="del"/><spring:message code="menu"/>',
								iconCls : 'icon-del',
								handler : function() {
								
									Response = confirm('<spring:message code="delconfirm" />');
									if(Response == true){
										$.ajax({
											type: "GET",
											async: false,
											url: "${ctx}/sys/menu/delete.do",
											data: "id="+node.id,
											success: function(msg){
												if("true"==msg){
												Ext.MessageBox.alert('信息提示',"<spring:message code="success"/>");
													//alert("<spring:message code="success"/>");
												}else{
												Ext.MessageBox.alert('信息提示',"<spring:message code="delfailure"/>");
													//alert("<spring:message code="delfailure"/>");
												}
											}
										}); 
										tree.getSelectionModel().getSelectedNode().parentNode.reload();
									}
										
								}
							},
							'-', 
							{
								//id : node.id + 'refresh',
								text : '<spring:message code="refresh"/>',
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
		tree.getSelectionModel().getSelectedNode().reload();
	}

	function parentNodeLoad(){
		tree.getSelectionModel().getSelectedNode().parentNode.reload();
	}
	
  
</script>
</body>
</html>
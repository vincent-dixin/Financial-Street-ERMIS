<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/include-tagsOnly.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>

<script type="text/javascript">
	/* 要显示的容器的右边内容  */
	Ext.define('rightGridPanel', {
		extend : 'Ext.panel.Panel',
		border : false,
		autoScroll : false,
		height : FHD.getCenterPanelHeight() - 3,
		region : 'center',
		autoLoad : {
			url : 'pages/process/processEdit.jsp',
			scripts : true
		}

	});

	/*定义容器类  */
	Ext
			.define(
					'process_processManage_panelAndMenu',
					{
						extend : 'Ext.container.Container',
						layout : 'border',
						defaults : {
							collapsible : true,
							split : true,
							animFloat : false,
							useSplitTips : true,
							collapseMode : 'mini'
						},
						height : FHD.getCenterPanelHeight(),
						//width : FHD.getCenterPanelWidth(),
						renderTo : "FHD.process.processManage.tree${param._dc}",
						items : new Array(),
						rightGridPanel : null,
						processTreeRightMenus : null,//右键菜单
						addNextLevel : {},//菜单添加项
						addNowLevel : {},//添加同级
						deleteRela : {},//删除关联
						refresh : {},//刷新
						setMenuByTreeNode : {},///*根据选中的树节点的类型不同，弹出的右键菜单的项也不同*/
						processTreeManage : null,//流程树
						treeAndListAndEditPanel : null,//容器
						treeRightNode : null,
						initComponent : function() {
							var me = this;
							me.rightGridPanel = Ext.create('rightGridPanel', {
								id : 'rightProcessGridPanel'
							});
							me.processTreeRightMenus = Ext.create(
									'Ext.menu.Menu', {
										margin : '0 0 10 0'
									});
							me.addNextLevel = {
								iconCls : 'icon-add',
								text : '添加下级',
								handler : function() {
									//右键选择树的节点
									var processNode = me.treeRightNode;
									//流程添加下级
									//if(processNode.data.id==processNode.parentNode.data.id){
										
									var url = 'pages/process/processEdit.jsp?processtext='+encodeURIComponent(processNode.data.text)+'&parentId='+processNode.data.id;
									me.setEditPage(url);
									//}
								}
							};
							me.addNowLevel = {
								iconCls : 'icon-add',
								text : '添加同级',
								handler : function() {
									//流程添加当前及（选中节点的同级）
									var processNode = me.treeRightNode;
									var url = 'pages/process/processEdit.jsp?processtext='+encodeURIComponent(processNode.parentNode.data.text)+'&parentId='+processNode.parentNode.data.id;
									me.setEditPage(url);
								}
							};

							/*删除*/
							me.deleteRela = {
								iconCls : 'icon-delete-icon',
								text : '删除',
								handler : function() {
									var processNode = me.treeRightNode;
							        
									Ext.MessageBox.show({
										title : FHD.locale.get('fhd.common.delete'),
										width : 260,
										msg : FHD.locale.get('fhd.common.makeSureDelete'),
										buttons : Ext.MessageBox.YESNO,
										icon : Ext.MessageBox.QUESTION,
										fn : function(btn) {
											if (btn == 'yes') {
												FHD.ajax({
												/*   params : {
												  "processID" : processNode.data.id
												   }, */
												  url : __ctxPath+ '/process/process/removeProcess.f?processID='+processNode.data.id,
												  callback : function(ret) {
													  me.processTreeManage.processTree.getStore().load();//刷新
												  }
												 });
											}
										}
									});
								}
							};
							/* ‘刷新’右键菜单*/
							me.refresh = {
								iconCls : 'icon-arrow-refresh',
								text :'刷新',
								handler : function() {
									 me.processTreeManage.processTree.getStore().load();
								}
							
							};

							/*根据选中的树节点的类型不同，弹出的右键菜单的项也不同*/
							me.setMenuByTreeNode = function(node, even) {
								me.processTreeRightMenus.removeAll();//清空菜单项
								if (node.data.leaf) {//叶子节点
									//添加下级
									me.processTreeRightMenus
											.add(me.addNextLevel);
									me.processTreeRightMenus.add('-');
									//添加同级
									me.processTreeRightMenus
											.add(me.addNowLevel);
									me.processTreeRightMenus.add('-');
									//删除选中
									me.processTreeRightMenus
											.add(me.deleteRela);
								} else {//非叶子节点
									//添加同级
									me.processTreeRightMenus
											.add(me.addNextLevel);
									me.processTreeRightMenus.add('-');
									//添加下级
									me.processTreeRightMenus
											.add(me.addNowLevel);
									me.processTreeRightMenus.add('-');
									//删除选中
									me.processTreeRightMenus
											.add(me.deleteRela);
									me.processTreeRightMenus.add('-');
									//刷新节点
									me.processTreeRightMenus.add(me.refresh);
								}
								//弹出菜单
								me.processTreeRightMenus.showAt(even.getXY());
							};
							//流程树
							me.processTreeManage = Ext.create(
									'FHD.ux.process.processTree', {
										height : FHD.getCenterPanelHeight(),
										 extraParams:{canChecked : false},
										width : 250,
										collapsible : true,
										maxWidth : 300,
										region : 'west',
										processTreeContextMenu : function(
												node, even) {
											if (node.data.id != 'root') {//非根节点才有菜单
												me.treeRightNode = node;
												me.setMenuByTreeNode(node, even);
											}
										},
										clickFunction:function(node){//点击叶子节点
											me.treeRightNode = node;
											if(node.data.id != 'root'){
												me.remove(me.rightGridPanel);
												me.rightGridPanel=Ext.create('rightGridPanel',{
													autoLoad : {
														url : 'pages/process/processEdit.jsp?processName='+encodeURIComponent(node.data.text)+'&processtext='+encodeURIComponent(node.parentNode.data.text)+'&parentId='+node.parentNode.data.id+'&processEdit=yes'+'&processEditID='+node.data.id,

														scripts : true
													}
												});
												me.add(me.rightGridPanel);
										 }
											
										}
									});
							          

							me.items = [ me.processTreeManage,
									me.rightGridPanel ];

							me.callParent(arguments);

						},
						setEditPage : function(url) {
							var me = this;
							me.remove(me.rightGridPanel);
							me.rightGridPanel = Ext.create('rightGridPanel', {
								region : 'center',
								collapsible : false,
								autoLoad : {
									url : url,
									scripts : true
								}
							});
							me.add(me.rightGridPanel);

						}
						

					});
	//var processTree_ManagerView;

	Ext.onReady(function() {

		process_ManagerView = Ext
				.create('process_processManage_panelAndMenu');
		 FHD.componentResize(process_ManagerView, 0, 0);


	});
</script>


</head>
<body>
	<div id="FHD.process.processManage.tree${param._dc}"></div>
</body>
</html>
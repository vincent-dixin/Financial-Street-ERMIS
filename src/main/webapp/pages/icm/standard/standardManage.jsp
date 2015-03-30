<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/include-tagsOnly.jsp"%>
<%@ include file="/pages/icm/standard/standardEdit.jsp"%>
<%@ include file="/pages/icm/standard/standardList.jsp"%>
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
			url : 'pages/icm/standard/standardList.jsp',
			scripts : true
		}

	});

	/*定义容器类  */
	Ext.define('standard_standardManage_panelAndMenu',{
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
						autoWidth:true,
						renderTo : "FHD.icm.standardManage.tree${param._dc}",
						items : new Array(),
						rightGridPanel : null,//grid列表
						standardTreeRightMenus : null,//右键菜单
						addNextLevel : {},//菜单添加项
						addNowLevel : {},//添加同级
						deleteRela : {},//删除关联
						refresh : {},//刷新
						addType:'',
						setMenuByTreeNode : {},///*根据选中的树节点的类型不同，弹出的右键菜单的项也不同*/
						icmStandardTreeManage : null,//内控树
						treeAndListAndEditPanel : null,//容器
						treeRightNode : null,
						northContiaier:null,
						tabEditPanel:null,
						centerContiaier:null,
						initComponent : function() {
							var me = this;
							
							me.standardTreeRightMenus = Ext.create(
									'Ext.menu.Menu', {
										margin : '0 0 10 0'
									});
							/*根据选中的树节点的类型不同，弹出的右键菜单的项也不同*/
							me.setMenuByTreeNode = function(node, even) {
								me.standardTreeRightMenus.removeAll();//清空菜单项
								if (node.data.leaf) {//叶子节点
									//添加下级
									me.standardTreeRightMenus.add(me.addNextLevel);
									me.standardTreeRightMenus.add('-');
									//添加同级
									me.standardTreeRightMenus.add(me.addNowLevel);
									me.standardTreeRightMenus.add('-');
									//删除选中
									me.standardTreeRightMenus.add(me.deleteRela);
									//刷新节点
									me.standardTreeRightMenus.add(me.refresh);
								} else {//非叶子节点
									//添加下级
									me.standardTreeRightMenus.add(me.addNextLevel);
									me.standardTreeRightMenus.add('-');
									//添加同级  
									me.standardTreeRightMenus.add(me.addNowLevel);
									me.standardTreeRightMenus.add('-');
									//删除选中
									me.standardTreeRightMenus.add(me.deleteRela);
									me.standardTreeRightMenus.add('-');
									//刷新节点
									me.standardTreeRightMenus.add(me.refresh);
								}
								//弹出菜单
								me.standardTreeRightMenus.showAt(even.getXY());
							};
							/* 添加下级 */
							me.addNextLevel = {
								iconCls : 'icon-add',
								text : '添加下级',
								handler : function() {
									var standardNode = me.treeRightNode;
									var sd=standardNode.data;
									me.tabEditPanel.setActiveTab(0);
									FHD_icm_standard_standardEdit.getForm().reset();
									FHD_icm_standard_standardEdit.getForm().setValues({'upName':sd.text});
									FHD_icm_standard_standardEdit.controlType='addTree';
									FHD_icm_standard_standardEdit.nodeId=sd.id;
									FHD_icm_standard_standardEdit.addType='addNext';
									FHD_icm_standard_standardEdit.idSeq=sd.idSeq;
									Ext.getCmp('standardCreateCodeButtonId').setDisabled(false);
								}
							};
							
							/* 添加同级 */
							me.addNowLevel = {
								iconCls : 'icon-add',
								text : FHD.locale
										.get('fhd.strategymap.strategymapmgr.sameLevel'),
								handler : function() {
									var standardNode = me.treeRightNode.parentNode;
									var sd=standardNode.data;
									me.tabEditPanel.setActiveTab(0);
									FHD_icm_standard_standardEdit.getForm().reset();
									FHD_icm_standard_standardEdit.getForm().setValues({'upName':sd.text});
									FHD_icm_standard_standardEdit.controlType='addTree';
									FHD_icm_standard_standardEdit.nodeId=sd.id;
									FHD_icm_standard_standardEdit.addType='addNow';
									FHD_icm_standard_standardEdit.idSeq=sd.idSeq;
									FHD_icm_standard_standardEdit.flagListOrTree='addTree';
									Ext.getCmp('standardCreateCodeButtonId').setDisabled(false);
								}
							};

							/*删除关联*/
							me.deleteRela = {
								iconCls : 'icon-delete-icon',
								text : FHD.locale
										.get('fhd.strategymap.strategymapmgr.delCascade'),
								handler : function() {
									var standardNode = me.treeRightNode;
									Ext.MessageBox.show({
										title : FHD.locale.get('fhd.common.delete'),
										width : 260,
										msg : FHD.locale.get('fhd.common.makeSureDelete'),
										buttons : Ext.MessageBox.YESNO,
										icon : Ext.MessageBox.QUESTION,
										fn : function(btn) {
									    if (btn == 'yes') {
										FHD.ajax({
										params : {
										"standardId" : standardNode.data.id
										},
										url : __ctxPath+ '/standard/standardTree/removeStandards.do',
										callback : function(ret) {
										me.icmStandardTreeManage.standardTree.getStore().load();//刷新
									    standard_standardGridPanel.store.load();
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
								text : FHD.locale.get('fhd.strategymap.strategymapmgr.refresh'),
								handler : function() {
									 var tree=me.icmStandardTreeManage.standardTree;
										var node = tree.getSelectionModel().getLastSelected();
								        var path = node.getPath('id');
								        tree.getStore().load({
								        	    scope   : this,
								        	    callback: function(records, operation, success) {
								        	    	 tree.expandPath(path);
								        	    }
								       });
								}
							};
							me.rightGridPanel = Ext.create('rightGridPanel', {
								id : 'rightStandardGridPanel',
								 title :'标准列表',
							     autoLoad : {
								 url : 'pages/icm/standard/standardList.jsp',
								 scripts : true
							     }
							});
							
							me.tabEditPanel=Ext.create('Ext.tab.Panel', {
                                bodyStyle: 'border-top: 0px',
                                activeTab: 1,
                                height : FHD.getCenterPanelHeight()-30,
                                plain: true,
							    region : 'center',
								items : [FHD_icm_standard_standardEdit,standard_standardGridPanel]
							   });
							me.tabEditPanel.getTabBar().insert(0, {
		                            xtype: 'tbfill'
		                        });
							
							//内控树
							me.icmStandardTreeManage = Ext.create(
									'FHD.ux.standard.StandardTree', {
										height : FHD.getCenterPanelHeight(),
										collapsible: true,
			                            width: 265,
			                            maxWidth: 300,
										region : 'west',
										standardTreeContextMenu : function(node, even) {
											if (node.data.id != '') {//非根节点才有菜单
												me.treeRightNode = node;
												me.setMenuByTreeNode(node, even);
											}
										},
										clickFunction:function(node){//点击叶子节点，展示内控列表（etype==0）
										 	if(node.data.id!=''){
												var form=FHD_icm_standard_standardEdit.getForm();
												form.reset();
												form.setValues({'upName':node.parentNode.data.text});
												FHD_icm_standard_standardEdit.controlType='listEdit';
												Ext.getCmp('standardCreateCodeButtonId').setDisabled(true);
												standard_standardGridPanel.standardId=node.data.id
												standard_standardGridPanel.idSeq=node.data.idSeq;
												standard_standardGridPanel.upName=node.data.text;
												
												form.load({
								                      url: __ctxPath + '/standard/standardTree/findStandardByIdToJson.do',
								                      params: {
								                    	  standardId:node.data.id
								                      },
								                      success: function (form, action) {
								                          return true;
								                      },
								                      failure: function (form, action) {
								                      }
								                  });
												//读取内控类型为0的数据
												 standard_standardGridPanel.store.proxy.url=__ctxPath+ '/standard/standardGrid/findStandardByPage.f?clickedNodeId='+node.data.id+'&isLeaf=1',//调用后台url
											     standard_standardGridPanel.store.load();
											}
										}
									});
							 me.northContiaier=Ext.create('Ext.container.Container', {
								region : 'north',
								height:30,
								autoLoad:{
									url:'pages/icm/standard/standardNavigation.jsp',
									scripts:true
								}
					        });
							me.centerContiaier=  Ext.create('Ext.container.Container', {
								region : 'center',
								height : FHD.getCenterPanelHeight(),
								items:[me.northContiaier,me.tabEditPanel]
			                  });
							
						 me.items = [me.icmStandardTreeManage,me.tabEditPanel];
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
	var standardTree_ManagerView;

	Ext.onReady(function() {
		standardTree_ManagerView = Ext.create('standard_standardManage_panelAndMenu');
		FHD.componentResize(standardTree_ManagerView,0,0);
	
	});
</script>


</head>
<body>
	<div id="FHD.icm.standardManage.tree${param._dc}"></div>
</body>
</html>
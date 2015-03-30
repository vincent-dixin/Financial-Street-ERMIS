/**
 * 菜单管理-左边的树
 * denggy
 */
Ext.define('FHD.view.sys.menu.MenuManagementTreePanel',{
	extend : 'FHD.ux.TreePanel',
	alias : 'widget.menumanagementtreepanel',
	//init method
	initComponent : function(){
		var me = this;
		me.queryUrl = 'menu/menuManagement/findMenuTreeSome.f';
		me.delUrl = 'menu/menuManagement/delMenuInfoById.f';//删除菜单
		me.findByIdUrl = 'menu/menuManagement/findMenuInfoById.f';//根据ID查询菜单实体
		me.delMsg = '';
		Ext.apply(me,{
    		rootVisible: false,
    		width:260,
    		split: true,
           	collapsible : true,
           	border:true,
           	region: 'west',
           	multiSelect: true,
           	rowLines:false,
          	singleExpand: false,
           	checked: false,
   			url: me.queryUrl,
   			listeners : {
	   			'itemclick' : function(view,re){		//左键点击改变右侧面板
	   				var formAddPanel = me.up('menumanagementmainpanel').rightPanel.tabPanel.menuAddPanel;
	   				formAddPanel.parentMenu.setValue(re.parentNode.data.text);
	   				formAddPanel.parentId = re.parentNode.data.id;
	   				formAddPanel.currentId = re.data.id	
	   				formAddPanel.loadData();
	   				me.currentNode = re;
		   				var tabgrid = formAddPanel.tadEditGrid;
			   				tabgrid.store.proxy.extraParams.id = re.data.id;
			   				tabgrid.currentId = re.data.id;
			   				tabgrid.currentName = re.data.text;
			   				tabgrid.reloadData();
		   				var btngrid = formAddPanel.btnEditGrid;
		   					btngrid.store.proxy.extraParams.id = '';
			   				btngrid.parentId = '';
			   				btngrid.parentName = '';
			   				btngrid.reloadData();
			   			tabgrid.down('#sys_menumanagement_tab_add').setDisabled(false);
			   			btngrid.down('#sys_menumanagement_btn_add').setDisabled(true);
	   				},
   				'itemcontextmenu':function(menutree, record, items, index, e){//右键菜单
   					
   		   			var formAddPanel = me.up('menumanagementmainpanel').rightPanel.tabPanel.menuAddPanel;
   		   			var tabgrid = formAddPanel.tadEditGrid;
		   			var btngrid = formAddPanel.btnEditGrid;
   		   			e.preventDefault();
   		           	e.stopEvent();
		   		           	me.nodemenu =new Ext.menu.Menu({
		   		                   floating:true,
		   		                   items:['root'==record.parentNode.data.id?null:{
		   		                	  // 	parentName:record.parentNode.data.text,
		   		                           text:"添加同级",
		   		                           iconCls : 'icon-add',
		   		                           handler:function(){
		   		                        	   	formAddPanel.getForm().reset();
			   		                        	formAddPanel.parentMenu.setValue(record.parentNode.data.text);
			   		                        	//新建同级节点  清空当前ID 
			   		     	   					formAddPanel.parentId = record.parentNode.data.id;
			   		     	   					formAddPanel.currentId = '';
			   		     	   					me.currentNode = record.parentNode;
			   		     	   					tabgrid.down('#sys_menumanagement_tab_add').setDisabled(true);
			   									btngrid.down('#sys_menumanagement_btn_add').setDisabled(true);
		   		                           }
		                   			},{
		   		                	 //  	parentName:record.data.text,
		   		                           text:"添加下级",
		   		                           iconCls : 'icon-add',
		   		                           handler:function(){
		   		                        	   	formAddPanel.getForm().reset();
		   		                        	   	formAddPanel.parentMenu.setValue(record.data.text);
			   		                        	//新建下级节点  清空当前ID 
			   		     	   					formAddPanel.parentId = record.data.id;
			   		     	   					formAddPanel.currentId = '';
			   		     	   					record.expand();
			   		     	   					me.currentNode = record;
			   		     	   					tabgrid.down('#sys_menumanagement_tab_add').setDisabled(true);
			   									btngrid.down('#sys_menumanagement_btn_add').setDisabled(true);
			   		     	   					
		   		                           }
		                   			},'root'==record.parentNode.data.id?null:{
			   		               		   text:"删除",
				                           iconCls : 'icon-del', 
				                           handler:function(){
				                        	   var selection = me.getSelectionModel().getSelection()[0];//获得选中的节点
				                        	   Ext.MessageBox.show({
				                           			title : '删除',
				                           			width : 260,                     
				                           			buttons : Ext.MessageBox.YESNO,
				                           			icon : Ext.MessageBox.QUESTION,
				                           			msg : '此节点下的子节点都会被删除，你确定删除吗？',
				                           			/*****删除选择开始****/
				                           			fn : function(btn) {
				                           				if (btn == 'yes') {//确认删除
				                           					
				                           					FHD.ajax({//ajax调用
				                           						url : me.delUrl + "?ids=" + selection.data.id,
				                           						callback : function(data) {
				                           							if (data) {//删除成功！
				                           								 Ext.ux.Toast.msg(FHD.locale
				                           										.get('fhd.common.prompt'), FHD.locale
				                           				 						.get('fhd.common.operateSuccess'));
				                           				 				var tempid = record.parentNode.data.id;
				                           				 				var temptx = record.parentNode.data.text;
				                           								formAddPanel.currentId = tempid;
				                           								formAddPanel.loadData();
				                           								me.currentNode = record.parentNode;
				                           								record.parentNode.removeChild(record);
				                           								me.getSelectionModel().select(me.currentNode);
				                           								//formAddPanel.getForm().reset();
			                           								var tabgrid = formAddPanel.tadEditGrid;
				                           				   				tabgrid.store.proxy.extraParams.id = tempid;
				                           				   				tabgrid.currentId = tempid;
				                           				   				tabgrid.currentName = temptx;
				                           				   				tabgrid.reloadData();
				                           			   				var btngrid = formAddPanel.btnEditGrid;
				                           			   					btngrid.store.proxy.extraParams.id = '';
				                           				   				btngrid.parentId = '';
				                           				   				btngrid.parentName = '';
				                           				   				btngrid.reloadData();
				                           							}
				                           						}
				                           					});
				                           				}
				                           			}
				                                   	/*******删除选择结束********/
				                                   })
				                           	}
		   		               		}, {
			   		                	   text:"刷新",
			   	                           iconCls : 'icon-arrow-refresh-small', 
			   	                           handler:function(){me.store.load();}	
		   		                   }]
		   		           });
   		           me.nodemenu.showAt(e.getXY());      
   	   		 
	   				}
   			}
		});
		me.callParent();
	}

});
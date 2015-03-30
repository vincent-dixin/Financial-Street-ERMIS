/**
 * 角色树
 * 
 * @author 翟辉
 */
Ext.define('FHD.view.sys.role.RoleTreePanel', {
    extend: 'FHD.ux.TreePanel',
    alias: 'widget.roleTreePanel',
  //var roleTabPanel = Ext.getCmp('roleTabPanel');//得到叶签
	//var rolePersonGridPanel = Ext.getCmp('rolePersonGridPanel');//得到grid
	//var roleBasicPanel = Ext.getCmp('roleBasicPanel');//得到角色基本信息form
	/*var authorityPanel = Ext.getCmp('authorityPanel');//授权panel
	authorityPanel.authorityTree.extraParams.id = re.data.id;
	authorityPanel.authorityShowTree.extraParams.id = re.data.id;
	authorityPanel.authorityTree.store.load();
	authorityPanel.authorityShowTree.store.load();
	authorityPanel.typeId = re.data.id;*/
	//var authorityTree = Ext.getCmp('authorityTree');//得到角色授权树(左)
	//var authorityShowTree = Ext.getCmp('authorityShowTree');//得到权限树展示树(右)
	//me.authorityTree.body.mask("保存中...","x-mask-loading");
	//me.authorityTree.store.proxy.url = 'sys/autho/authorityTreeLoader.f';
	//me.authorityShowTree.store.proxy.url = 'sys/autho/showTreeLoader.f';
	//me.authorityTree.body.unmask();
	//Ext.getCmp('deleteAssignAuthority').setDisabled(false);
	//Ext.getCmp('saveAssignAuthority').setDisabled(false);
	//var roleRightPanel = Ext.getCmp('roleRightPanel');//导航
    itemclickTree:function (re){
    	var me = this;
		me.authorityTree.roleTreeId= re.data.id;
		me.authorityShowTree.roleTreeId= re.data.id;
		me.authorityTree.extraParams.id = re.data.id;//角色树
		me.authorityShowTree.extraParams.id = re.data.id;//角色显示树
		me.authorityTree.store.load();
		me.authorityShowTree.store.load();
		me.rolePersonGridPanel.store.proxy.url = me.rolePersonGridPanel.roleQueryUrl + "?roleId=" + re.data.id;//角色对应人员列表url
		me.roleBasicPanel.roleTreeId = re.data.id;
		me.rolePersonGridPanel.roleTreeId = re.data.id;
		me.roleTreeId = re.data.id;
		me.roleBasicPanel.load();
		me.rolePersonGridPanel.store.load();
		me.rolePersonGridPanel.setstatus();//设置按钮可用状态
		me.roleBasicPanel.isAdd = true;//标志位修改为true添加为false
		me.roleRightPanel.navigationBar.renderHtml('rolerightPanelDiv', re.data.id, '', 'role');
	
    },
    selectedNodeClick: function(nodeid) {//选中树节点
    	var me = this;
    	var rootNode = me.getRootNode();
    	if (rootNode.childNodes.length > 0) {
    		 for(var i=0; i <rootNode.childNodes.length; i++){
    	           var node = rootNode.childNodes[i];
    	           if(node.data.id == nodeid){
    	        	   me.getSelectionModel().select(node);
    	           }
    		 }
    	}
    },
    // 初始化方法
    initComponent: function() {
    	var me = this;
    	me.isAdd = false;
    	me.id = 'roleTreePanel';
    	me.roleTreeId;
    	me.queryUrl = 'sys/autho/findDictTypeBySome.f';//树初始化和查询
    	me.delUrl = 'sys/autho/delRole.f';//删除角色
    	me.delMsg = FHD.locale.get('fhd.common.makeSureDelete');//删除弹出信息
    	me.nodemenu = null; 
    	me.roleTabPanel = Ext.getCmp('roleTabPanel');//得到叶签
		me.rolePersonGridPanel = Ext.getCmp('rolePersonGridPanel');//得到grid
		me.roleBasicPanel = Ext.getCmp('roleBasicPanel');//得到角色基本信息form
		me.authorityTree = Ext.getCmp('authorityTree');//得到角色授权树(左)
		me.authorityShowTree = Ext.getCmp('authorityShowTree');//得到权限树展示树(右)
		me.roleRightPanel = Ext.getCmp('roleRightPanel');//导航
		//me.authorityTreeUrl="sys/autho/authorityTreeLoader.f";//权限树  url
		//me.authorityShowTreeUrl = 'sys/autho/showTreeLoader.f';//展示树url
    	Ext.apply(me, {
    		//rootVisible: true,
    		width:260,
    		split: true,
           	collapsible : true,
           	border:true,
           	region: 'west',
           	multiSelect: true,
           	rowLines:false,
          	singleExpand: false,
           	checked: false,
   			url: me.queryUrl,//调用后台url
   			listeners : {
   				load: function (store, records) { //默认选择首节点
                    var rootNode = me.getRootNode();
                    if (rootNode.childNodes.length > 0) {
                        me.getSelectionModel().select(rootNode.firstChild);//默认选择首节点
                        me.itemclickTree(rootNode.firstChild);
                        //me.roleTreeId = rootNode.firstChild.data.id
                        //alert(rootNode.firstChild.data.id);
                    } 
                },
    		 itemclick :function(v,re){
    			 me.itemclickTree(re);
    		 },
	   		'itemcontextmenu':function(menutree, record, items, index, e){ //右键菜单
	   			e.preventDefault();
	           	e.stopEvent();     	
	          	if(me.nodemenu == null){
	           	me.nodemenu = new Ext.menu.Menu({
	                   floating:true,
	                   items:[{
	                           text:"新增",
	                           iconCls : 'icon-add',
	                           handler:function(){
	                        	   me.roleBasicPanel.getForm().reset();
	                        	   me.roleTabPanel.setActiveTab(2); 
	                        	   me.roleBasicPanel.isAdd = false;
	                           }
	                   	}, {
	                           text:"修改",
	                           iconCls : 'icon-edit', 
	                       	   handler:function(){
	                       		   me.roleBasicPanel.roleTreeId = me.getSelectionModel().getSelection()[0].data.id;//通过右键传过来的树Id
	                       		   me.roleTabPanel.setActiveTab(2);
	                       		   me.roleBasicPanel.load();
	                       		   me.roleBasicPanel.isAdd = true;
	                       	   }
	               		}, {
	                           text:"删除",
	                           iconCls : 'icon-del',
	                           handler:function(){
	                           	var selection = me.getSelectionModel().getSelection()[0];
	                                   Ext.MessageBox.show({
	                           			title : '删除',
	                           			width : 260,                     
	                           			buttons : Ext.MessageBox.YESNO,
	                           			icon : Ext.MessageBox.QUESTION,
	                           			msg : me.delMsg,
	                           			//*****删除选择开始****//
		                           			fn : function(btn) {
		                           				if (btn == 'yes') {//确认删除
		                           					FHD.ajax({//ajax调用
		                           						url : me.delUrl + "?roleId=" + selection.data.id,
		                           						callback : function(data) {
		                           							if (data) {//删除成功！
		                           								 Ext.ux.Toast.msg(FHD.locale
		                           										.get('fhd.common.prompt'), FHD.locale
		                           										.get('fhd.common.operateSuccess'));
		                           								 me.store.load();
		                           								 me.roleBasicPanel.getForm().reset();//清空基本信息
		                           								 me.authorityTree.store.load();
		                           								 me.authorityShowTree.store.load();
		                           								 //Ext.getCmp('deleteAssignAuthority').setDisabled(true);//删除权限按钮变灰
		                           				    			// Ext.getCmp('saveAssignAuthority').setDisabled(true);//添加权限按钮变灰
		                           				    			 Ext.getCmp('roleaddTop').setDisabled(true);
		                           							}
		                           						}
		                           					});
		                           				}
		                           			}
	                                   	//*******删除选择结束********//
	                                   })
	                           }
	                   },
	                   {
	                	   text:"刷新",
                           iconCls : 'icon-arrow-refresh-small', 
                           handler:function(){me.store.load();}	
	                   }]
	           });
	           }
	           me.nodemenu.showAt(e.getXY());      
	   		}
    		}
        });
    	me.on('collapse', function(p) {
    		me.authorityTree.setWidth(me.roleTabPanel.getWidth()/2);
		});
		me.on('expand', function(p) {
			me.authorityTree.setWidth(me.roleTabPanel.getWidth()/2);
		});
		me.on('collapse', function(p) {
			me.authorityShowTree.setWidth(me.roleTabPanel.getWidth()/2);
		});
		me.on('expand', function(p) {
			me.authorityShowTree.setWidth(me.roleTabPanel.getWidth()/2);
		});
		
		me.on('resize', function(p) {
			if (p.collapsed) {
				me.authorityTree.setWidth(me.roleTabPanel.getWidth()/2);
				me.authorityShowTree.setWidth(me.roleTabPanel.getWidth()/2);
			} else {
				me.authorityTree.setWidth(me.roleTabPanel.getWidth()/2);
				me.authorityShowTree.setWidth(me.roleTabPanel.getWidth()/2);
			}
		});
		
		
        me.callParent(arguments);
    }
});
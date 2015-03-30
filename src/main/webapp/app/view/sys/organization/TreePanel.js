/**
 * 机构树
 * 
 * @author 金鹏祥
 */
Ext.define('FHD.view.sys.organization.TreePanel', {
    extend: 'FHD.ux.TreePanel',
    alias: 'widget.treePanel',
    findRootNode:function(){
    	var me = this;
    	var root;
    	FHD.ajax({
       		async : false,
       		url :'sys/organization/findTreeRoot.f',
       		callback : function(objectMaps){
       			 root =objectMaps.organization;
       		}
    	});
    	return root;
    },
    currentNode:null,
    findNode:function(root,nodeid){
    	var me = this;
    	var childnodes = root.childNodes;//获取根节点的子节点
        for(var i=0; i < childnodes.length; i++){
           var node = childnodes[i];
           if(node.data.id == nodeid)
           {
             me.navNode = node;
           }
           if(node.hasChildNodes()){
        	 me.findNode(node,nodeid);//递归调用
           }
        }
        return me.navNode;
    },
    /**
     * 左键
     */
    itemclickTree : function(re){
    	var me = this;
    	me.currentNode = re;
		var tabPanel = Ext.getCmp('tabPanel');
		var orgGridPanel = Ext.getCmp('tabpanelorgGridPanel');
		var empGridPanel = Ext.getCmp('tabpanelempGridPanel');
		var orgEditPanel = Ext.getCmp('orgEditPanel');
		var cardPanel = Ext.getCmp('cardPanel');
		var rightPanel = Ext.getCmp('rightPanel');
		var positionTabPanel = Ext.getCmp('positionTabPanel');
		var positionGridPanel = Ext.getCmp('positionGridPanel');
		var positionEditPanel = Ext.getCmp('positionEditPanel');
		var posiOrgGrid = Ext.getCmp('positionorgGridPanel');
		var posiEmpGrid = Ext.getCmp('positionempGridPanel');
		if("jg" == re.data.type){
			cardPanel.layout.setActiveItem(tabPanel);
			if(tabPanel.getActiveTab()==tabPanel.items.items[0]){
				orgGridPanel.store.proxy.url = orgGridPanel.queryUrl;//动态赋给机构列表url
	  			orgGridPanel.store.proxy.extraParams.orgIds = me.currentNode.data.id;
	  			orgGridPanel.store.load();
			}else if(tabPanel.getActiveTab()==tabPanel.items.items[1]){
				positionGridPanel.store.proxy.url = positionGridPanel.queryUrl;
	  			positionGridPanel.store.proxy.extraParams.orgId = me.currentNode.data.id;
	  			positionGridPanel.store.load();
			}else if(tabPanel.getActiveTab()==tabPanel.items.items[2]){
				empGridPanel.store.proxy.url = empGridPanel.queryUrl;
  				empGridPanel.store.proxy.extraParams.orgIds = me.currentNode.data.id;
  				empGridPanel.store.load();
			}else if(tabPanel.getActiveTab()==tabPanel.items.items[3]){
				orgEditPanel.newFlag = 'treeClick';
				orgEditPanel.orgtreeId = me.currentNode.data.id;
				orgEditPanel.load();
				orgEditPanel.isAdd=false;
			}
			rightPanel.navigationBar.renderHtml('organizationNavDiv', re.data.id, '', 'org');
		}else{
			cardPanel.layout.setActiveItem(positionTabPanel);
			if(positionTabPanel.getActiveTab()==positionTabPanel.items.items[0]){
				posiEmpGrid.store.proxy.url = posiEmpGrid.queryUrl;
				posiEmpGrid.store.proxy.extraParams.positionIds = me.currentNode.data.id;
				posiEmpGrid.store.load();
				positionEditPanel.orgtreeId = me.currentNode.data.id;
			}else if(positionTabPanel.getActiveTab()==positionTabPanel.items.items[1]){
				positionEditPanel.orgtreeId = me.currentNode.data.id;
  				positionEditPanel.load();
  				positionEditPanel.isAdd=false;
			}
			/*positionGridPanel.store.proxy.url = positionGridPanel.queryUrl + "?positionIds=" + re.data.id;
			positionGridPanel.store.load();*/
			//导航
			rightPanel.navigationBar.renderHtml('organizationNavDiv', re.data.id, '', 'org');
			
		}
		
		},
		 /**
	     * 右键菜单
	     */
	   contextItemMenuFun: function (view, record, node, index, e) {
		   	var me = this;
  	    	me.currentNode = record;
  			var orgEditPanel = Ext.getCmp('orgEditPanel');
  			var orgGridPanel = Ext.getCmp('tabpanelorgGridPanel');
  			var positionGridPanel = Ext.getCmp('positionGridPanel');
  			
  			e.preventDefault();
          	e.stopEvent();
          	
          	orgEditPanel.orgtreeId = me.currentNode.data.id;
          	if("jg" == me.currentNode.data.type){
          		positionGridPanel.orgId = me.currentNode.data.id;
          	}else{
          		positionGridPanel.orgId = null;
          	}
	        var menu = Ext.create('Ext.menu.Menu', {
	            margin: '0 0 10 0',
	            items: []
	        });
	        if("gw"!=record.data.type){
	        	//添加下级菜单
		        var subLevel = {
			            iconCls: 'icon-add',
			            text:"添加下级机构",
			            //id:'treeOrgAdd_sameLevel',
			            handler: function () {
			            	 me.currentNode = record;
			                 if (!me.currentNode.isExpanded() && !me.currentNode.isLeaf()) {
			                     me.currentNode.expand();
			                 }
			            	orgGridPanel.edit('add' ,orgGridPanel, true, false)
			            }
			        };
		        	 menu.add(subLevel);
		        	 menu.add('-');
		      //添加同级菜单
		      if(me.getRootNode()!=me.currentNode){
		        var sameLevel = {
		                iconCls: 'icon-add',
		                text: "添加同级机构",
		                //id : 'treeOrgAdd_assist',
	                    handler:function(){
	                    	me.currentNode = record.parentNode;
	                        me.currentNode.expand();
	                    	orgGridPanel.edit('add' ,orgGridPanel, true, true)
	                    	}
		            };
		            menu.add(sameLevel);
			    menu.add('-');
		      }
		      //修改菜单
		    var modifymenu = {
		    		text:"修改机构",
                    iconCls : 'icon-edit', 
                	//id : 'treeOrgEdit',
                    handler:function(){orgGridPanel.edit('edit' ,orgGridPanel, true)}
	            };
	            menu.add(modifymenu);
		    menu.add('-');
		     //添加岗位
		    var addPosi = {
		    		text:"添加岗位",
                    iconCls : 'icon-add',
                    //id : 'treePosiAdd',
                    handler:function(){positionGridPanel.edit('add' ,positionGridPanel, true)}
	            };
	            menu.add(addPosi);
		    menu.add('-');
	        }
		    //删除菜单
	        var delmenu = {
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
                    			//*****删除选择开始****//*
                    			fn : function(btn) {
                    				if (btn == 'yes') {//确认删除
                    					if("jg"==selection.data.type){
                    						FHD.ajax({//ajax调用
                        						url : me.delUrl + "?ids=" + selection.data.id,
                        						callback : function(data) {
                        							if (data) {//删除成功！
                        								 Ext.ux.Toast.msg(FHD.locale
                        										.get('fhd.common.prompt'), FHD.locale
                        										.get('fhd.common.operateSuccess'));
                        								 me.store.load();
                        							}else{//该节点存在子集
                         							Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'有子集不能删除');
                         						}
                        						}
                        					});
                    					}else{
                    						FHD.ajax({//ajax调用
                        						url : me.delPosiUrl + "?ids=" + selection.data.id,
                        						callback : function(data) {
                        							if (data) {//删除成功！
                        								 Ext.ux.Toast.msg(FHD.locale
                        										.get('fhd.common.prompt'), FHD.locale
                        										.get('fhd.common.operateSuccess'));
                        								 me.store.load();
                        							}
                        						}
                        					});
                    					}
                    					
                    					
                    				}
                    			}
                            	//*******删除选择结束********//*
                            })
                    }
	            }
	            menu.add(delmenu);
	            menu.add('-');
		    //刷新菜单
		    var refresh = {
		    		 text:"刷新",
                     iconCls : 'icon-arrow-refresh-small', 
                     handler:function(){me.store.load();}	
		     }
		    menu.add(refresh);
		    return menu;
	    },
    
    // 初始化方法
    initComponent: function() {
    	var me = this;
    	var root = me.findRootNode();
    	var newNodeId;
    	
    	me.id = 'treePanel';
    	me.queryUrl = 'sys/organization/treeLoader.f';
    	me.nodemenu = null;
    	me.delMsg=FHD.locale.get('fhd.common.makeSureDelete');//删除提示信息
    	me.delUrl='sys/organization/removeOrgEntryById.f';//删除机构
    	me.delPosiUrl='sys/organization/removeposientrybyids.f';//删除岗位
    	
    	Ext.apply(me, {
    		rootVisible: true,
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
   			root:root,
   			listeners : {
   				load: function (store, records) { //默认选择首节点
   					var selectedNode;
   					
   					var nodeItems = me.getSelectionModel().selected.items;
   			        if (nodeItems.length > 1) {
   			        	selectedNode = nodeItems[0];
   			            me.currentNode = selectedNode;
   			           
   			        }else{
   			        	selectedNode = me.getRootNode();
   			        	me.currentNode = me.getRootNode();
   			        }
   			        me.getSelectionModel().select(selectedNode);//默认选择首节点
		            me.itemclickTree(selectedNode);
                    /*var rootNode = me.getRootNode();
                    if (rootNode.childNodes.length > 0) {
                        me.getSelectionModel().select(rootNode);//默认选择首节点
                        me.itemclickTree(rootNode);
                    } */
                },
    		itemclick :function(view,re){
    			 me.itemclickTree(re);
    		 }
    		},
    		 viewConfig: {
                 listeners: {
                     itemcontextmenu: function (view, rec, node, index, e) {//右键
                         e.stopEvent();
                         var menu = me.contextItemMenuFun(view, rec, node, index, e);
                         if (menu) {
                             menu.showAt(e.getPoint());
                         }
                         return false;
                     }
                 }
             }
        });
    	
        me.callParent(arguments);
    }
});
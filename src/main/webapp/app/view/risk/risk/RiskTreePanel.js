/**
 * 目标树
 * 
 * @author zhengjunxiang
 */
Ext.define('FHD.view.risk.risk.RiskTreePanel', {
    extend: 'FHD.ux.TreePanel',
    alias: 'widget.risktreepanel',
    root: {
        "id": "root",
        "text": "风险",
        "dbid": "sm_root",
        "leaf": false,
        "code": "sm",
        "type": "orgRisk",
        "expanded": false,
        'iconCls':'icon-ibm-icon-scorecards'	//样式
    },
    // 初始化方法
    initComponent: function() {
    	var me = this;
    	
    	//初始化参数
    	var extraParams = {};
    	extraParams.rbs = me.rbs || false;
    	extraParams.canChecked = me.canChecked || false;
    	
    	me.id = 'riskTreePanel';
    	me.queryUrl = __ctxPath + '/risk/riskTreeLoader';	//暂时不留作参数/component/riskTreeLoader
    	
    	Ext.apply(me, {
    		rootVisible: true,
           	multiSelect: true,
           	rowLines:false,
          	singleExpand: false,
           	checked: false,
           	extraParams:extraParams,
   			url: me.queryUrl,//调用后台url
   			listeners: {	
                load:function(){
                	var selectedNode = null;
                	var nodeItems = me.getSelectionModel().selected.items;
                    if (nodeItems.length > 0) {
                        selectedNode = nodeItems[0];
                    }
                    //没有选中节点，默认选中第一个节点
                    if (selectedNode == null) {
                    	//me.currentNodeClick();
                    }
                },
   				//树单击事件
                itemclick: me.onTreepanelItemClick,
                /**
                 * 右键监听事件
                 */
                itemcontextmenu: function (view, rec, node, index, e) {
                    e.stopEvent();
                    var menu = me.contextItemMenuFun(view, rec, node, index, e);
                    if (menu) {
                        menu.showAt(e.getPoint());
                    }
                    return false;
                }
            }
        });
    	
        me.callParent(arguments);
    },
    //树单击事件
    onTreepanelItemClick: function (tablepanel, record, item, index, e, options) {
    	var me = this;

    	var id = record.data.id;
        var name = record.data.text;
        
        //根节点没有操作
        if(id=="root"){
        	return;
        }
        
        /**
         * 将左侧树id,name保持到face的全局变量中，共tab页面点击时加载使用
         */
        me.face.nodeId = id;
        me.face.nodeName = name;
        
        /**
         * 右侧哪个tab激活，哪个tab页面加载数据
         */
        var tab = me.face.tabpanel;
        var activeTab = tab.getActiveTab();
        if(activeTab == me.face.riskBasicFormView){
        	//1 请求风险详细信息
        	FHD.ajax({
       			async:false,
       			params: {
                    riskId: id
                },
                url: __ctxPath + '/risk/findRiskById.f',
                callback: function (ret) {
                	//显示目标详细信息
                	me.face.riskBasicFormView.reLoadData(ret);
                }
            });
        }else if(activeTab == me.face.riskEventGridContainer){
        	//2 请求风险事件列表
        	me.face.riskEventGrid.reLoadData(id,name);
        }else if(activeTab == me.face.riskHistoryGridContainer){
        	//3 请求风险历史记录
        	me.face.riskHistoryGrid.reLoadData(id);
        }else if(activeTab == me.face.stepPanelContainer){
        	//4 请求风险编辑信息
        	FHD.ajax({
       			async:false,
       			params: {
                    riskId: id
                },
                url: __ctxPath + '/risk/findRiskEditInfoById.f',
                callback: function (ret) {
                	//显示目标详细信息
                	var riskEditFormView = me.face.riskEditBasicFormView;	//找到步骤导航中的formpanel
                	riskEditFormView.reLoadData(ret);
                	riskEditFormView.isEdit = true;
                	riskEditFormView.editId = id;
                }
            });
        }
    },
    /**
     * 当前节点点击函数,如果没有选中节点,默认选中首节点
     */
    currentNodeClick: function () {
        var me = this;

        var selectedNode;
        var nodeItems = me.getSelectionModel().selected.items;
        if (nodeItems.length > 0) {
            selectedNode = nodeItems[0];
        }

        //没有选中节点，默认选中第一个节点
        if (selectedNode == null) {	
            var firstNode = me.getRootNode().firstChild;
            if(null!=firstNode){
            	me.getSelectionModel().select(firstNode);
            	selectedNode = firstNode;
            }else{
            	return;
            }
        }

        var id = selectedNode.data.id;
        var name = selectedNode.data.text;
        
        /**
         * 将左侧树id,name保持到face的全局变量中，共tab页面点击时加载使用
         */
        me.face.nodeId = id;
        me.face.nodeName = name;
        
        //tab切换到基本信息标签
//        var tab = me.face.tabpanel;
//        tab.setActiveTab(3);
        
        /**
         * 右侧哪个tab激活，哪个tab页面加载数据
         */
        var tab = me.face.tabpanel;
        var activeTab = tab.getActiveTab();
        if(activeTab == me.face.riskBasicFormView){
        	//1 请求风险详细信息
        	FHD.ajax({
       			async:false,
       			params: {
                    riskId: id
                },
                url: __ctxPath + '/risk/findRiskById.f',
                callback: function (ret) {
                	//显示目标详细信息
                	me.face.riskBasicFormView.reLoadData(ret);
                }
            });
        }else if(activeTab == me.face.riskEventGridContainer){
        	//2 请求风险事件列表
        	me.face.riskEventGrid.reLoadData(id,name);
        }else if(activeTab == me.face.riskHistoryGridContainer){
        	//3 请求风险历史记录
        	me.face.riskHistoryGrid.reLoadData(id);
        }else if(activeTab == me.face.stepPanelContainer){
        	//4 请求风险编辑信息
        	FHD.ajax({
       			async:false,
       			params: {
                    riskId: id
                },
                url: __ctxPath + '/risk/findRiskEditInfoById.f',
                callback: function (ret) {
                	//显示目标详细信息
                	var riskEditFormView = me.face.riskEditBasicFormView;	//找到步骤导航中的formpanel
                	riskEditFormView.reLoadData(ret);
                	riskEditFormView.isEdit = true;
                	riskEditFormView.editId = id;
                }
            });
        }
    },
    /**
     * 添加右键菜单
     */
    contextItemMenuFun: function (view, rec, node, index, e) {
        var me = this;
        var id = rec.data.id;
        var name = rec.data.text;
        
        /**
         * 将左侧树id,name保持到face的全局变量中，共tab页面点击时加载使用
         */
        me.face.nodeId = id;
        me.face.nodeName = name;
        
        var menu = Ext.create('Ext.menu.Menu', {
            margin: '0 0 10 0',
            items: []
        });

        /*添加*/
        var add = {
            iconCls: 'icon-add',
            text: "添加",
            handler: function () { 
                me.currentNode = rec;
                if (!me.currentNode.isExpanded() && !me.currentNode.isLeaf()) {
                    me.currentNode.expand();
                }
                          
                //切换tab标签
                var tab = me.face.tabpanel;
                tab.setActiveTab(me.face.stepPanelContainer);	//？？步骤导航内部还有不同页面
            	var riskEditFormView = me.face.riskEditBasicFormView;
            	
                if(id=="root"){
                	var json = {parentId:''};
            		//继承父亲的值
            		json.isInherit = '0yn_y';
            		json.templeteId = '';
            		json.relativeTo = '';
            		json.formulaDefine = '';
            		json.alarmPlanId = '';
                	riskEditFormView.reSetData(json);
                	riskEditFormView.isEdit = false;
                	riskEditFormView.isRiskClass = 'rbs';
                }else{
                    //请求风险编辑信息,为新节点赋值
                	FHD.ajax({
               			async:false,
               			params: {
                            riskId: id
                        },
                        url: __ctxPath + '/risk/findRiskEditInfoById.f',
                        callback: function (ret) {
                         	//清空数据，并设置上级id和初始值
                        	var parentId = [];
                        	var obj = {};
                        	obj["id"] = me.currentNode.data.id;
                        	parentId.push(obj);
                    		var json = {parentId:Ext.encode(parentId)};
                    		//继承父亲的值
                    		json.isInherit = '0yn_y';
                    		json.templateId = ret.templateId;
                    		json.relativeTo = ret.relativeTo||'';
                    		json.formulaDefine = ret.formulaDefine;
                    		json.alarmPlanId = ret.alarmPlanId;
                        	riskEditFormView.reSetData(json);
                        	riskEditFormView.isEdit = false;
                        	riskEditFormView.isRiskClass = 'rbs';
                        }
                    });
                }
            }
        };
                
        /*添加同级*/
        var addSibling = {
            iconCls: 'icon-add',
            text: "添加同级",
            handler: function () { 
                me.currentNode = rec;
                if (!me.currentNode.isExpanded() && !me.currentNode.isLeaf()) {
                    me.currentNode.expand();
                }
                          
                //切换tab标签
                var tab = me.face.tabpanel;
                tab.setActiveTab(me.face.stepPanelContainer);	//？？步骤导航内部还有不同页面
            	var riskEditFormView = me.face.riskEditBasicFormView;
                
                var parentNode = me.currentNode.parentNode;
                //请求风险编辑信息,为新节点赋值
                if(parentNode.data.id != 'root'){	//父亲节点不是根节点，请求上级节点详细信息
                	FHD.ajax({
               			async:false,
               			params: {
                            riskId: parentNode.data.id
                        },
                        url: __ctxPath + '/risk/findRiskEditInfoById.f',
                        callback: function (ret) {
                        	//清空数据，并设置上级id和初始值
                        	var parentId = [];
                        	var obj = {};
                        	obj["id"] = parentNode.data.id;
                        	parentId.push(obj);
                    		var json = {parentId:Ext.encode(parentId)};
                    		//继承父亲的值
                    		json.isInherit = '0yn_y';
                    		json.templateId = ret.templateId;
                    		json.relativeTo = ret.relativeTo||'';
                    		json.formulaDefine = ret.formulaDefine;
                    		json.alarmPlanId = ret.alarmPlanId;
                        	riskEditFormView.reSetData(json);
                        	riskEditFormView.isEdit = false;
                        	riskEditFormView.isRiskClass = 'rbs';
                        }
                    });
                }else{
                	var json = {parentId:''};
            		//继承父亲的值
            		json.isInherit = '0yn_y';
            		json.templeteId = '';
            		json.relativeTo = '';
            		json.formulaDefine = '';
            		json.alarmPlanId = '';
                	riskEditFormView.reSetData(json);
                	riskEditFormView.isEdit = false;
                	riskEditFormView.isRiskClass = 'rbs';
                }
            }
        };
                
        /*编辑*/
        var edit = {
            iconCls: 'icon-edit',
            text: "编辑",
            handler: function () { 
                me.currentNode = rec;
                if (!me.currentNode.isExpanded() && !me.currentNode.isLeaf()) {
                    me.currentNode.expand();
                }
                          
                //切换tab标签
                var tab = me.face.tabpanel;
                tab.setActiveTab(me.face.stepPanelContainer);	//？？步骤导航内部还有不同页面
            	var riskEditFormView = me.face.riskEditBasicFormView;
            	
            	//请求风险编辑信息
            	FHD.ajax({
           			async:false,
           			params: {
                        riskId: id
                    },
                    url: __ctxPath + '/risk/findRiskEditInfoById.f',
                    callback: function (ret) {
                    	//显示目标详细信息
                    	riskEditFormView.reLoadData(ret);
                    	riskEditFormView.isEdit = true;
                    	riskEditFormView.editId = id;
                    }
                });
            	
            }
        };        
        
        /*删除*/
        var del = {
            iconCls: 'icon-del',
            text: "删除",
            handler: function () {
            	var delUrl = '/risk/risk/removeRiskById.f';
            	var selection = me.getSelectionModel().getSelection()[0];
 
            	//如果有叶子节点，直接返回
            	if(selection.childNodes.length>0){
            		Ext.MessageBox.show({
            			title:'操作错误',
            			msg:'该风险下有下级风险，不允许删除!'
            		});
            		return;
            	}
            	
                Ext.MessageBox.show({
        			title : '删除',
        			width : 260,                     
        			buttons : Ext.MessageBox.YESNO,
        			icon : Ext.MessageBox.QUESTION,
        			msg : FHD.locale.get('fhd.common.makeSureDelete'),
        			/*****删除选择开始****/
        			fn : function(btn) {
        				if (btn == 'yes') {//确认删除
        					FHD.ajax({//ajax调用
        						url : __ctxPath + delUrl + "?ids=" + selection.data.id,
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
                	/*******删除选择结束********/
                })
            }
        };        
        
        /*启用*/
        var start = {
            iconCls: 'icon-plan-start',
            text: "启用",
            handler: function () {
            	var startUrl = "/risk/enableRisk";
            	var selection = me.getSelectionModel().getSelection()[0];
            	FHD.ajax({//ajax调用
					url : __ctxPath + startUrl,
					params:{ids:selection.data.id,isUsed:'0yn_y'},
					callback : function(data) {
						if (data) {//删除成功！
							 me.store.load();
							 Ext.Msg.alert(FHD.locale.get('fhd.common.prompt'), '启用成功!');
						}
					}
				});
            }
        };
        
        /*停用*/
        var stop = {
            iconCls: 'icon-plan-stop',
            text: "停用",
            handler: function () {
            	var stopUrl = "/risk/enableRisk";
            	var selection = me.getSelectionModel().getSelection()[0];
            	FHD.ajax({//ajax调用
					url : __ctxPath + stopUrl,
					params:{ids:selection.data.id,isUsed:'0yn_n'},
					callback : function(data) {
						if (data) {//删除成功！
							 me.store.load();
							 Ext.Msg.alert(FHD.locale.get('fhd.common.prompt'), '停用成功!');
						}
					}
				});
            }
        };        
        
        /*刷新*/
        var refresh = {
            iconCls: 'icon-arrow-refresh-small',
            text: "刷新",
            handler: function () { 
            	me.store.load();
            }
        };
        
        //根节点只有添加和刷新操作
        if(id=="root"){
        	menu.add(add);
        	menu.add(refresh);
        }else{
        	menu.add(add);
        	menu.add(addSibling);
        	menu.add(edit);
        	menu.add(del);
        	menu.add(start);
        	menu.add(stop);
        	menu.add(refresh);
        }
        
        return menu;
    }
});
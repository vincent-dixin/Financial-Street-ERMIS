/**
 * 
 * 行业树表格
 */

Ext.define('FHD.view.industry.risk.RiskGridTree', {
    extend: 'FHD.ux.TreeGridPanel',
    alias: 'widget.riskGridTree',
    
    /***attribute end***/
    /***function start***/
    edit : function(me){//新增方法
    	debugger;
    	me.store;
////    	var selection = treegrid.getSelectionModel().getSelection()[0];//得到选中的记录
    	var formwindow = new Ext.Window({
    		layout:'fit',
    		iconCls: 'icon-edit',//标题前的图片
    		modal:true,//是否模态窗口
    		collapsible:true,
    		width:400,
    		height:240,
    		maximizable:true//（是否增加最大化，默认没有）
//    		autoLoad:{ url: 'pages/demo/treegrid/treegridedit.jsp',nocache:true,scripts:true}
    	});
    	formwindow.show();
////    	if(button.id=='add'){
////    		formwindow.setTitle(FHD.locale.get('fhd.common.add'));
////    		isAdd = true;
////    	}else{
////    		formwindow.setTitle(FHD.locale.get('fhd.common.modify'));
////    		formwindow.initialConfig.id = selection.get('id');
////    		isAdd = false;
////    	}
    },
//    
//    view : function(me){
////    	FHD.ajax({//ajax调用
////    		url : 'test/viewTestMvc.f',
////    		params : {
////    			id:id
////    		},
////    		callback : function(data){
////    			viewwindow = new Ext.Window({
////    				title:'查看',
////    		    	layout:'fit',
////    				iconCls: 'icon-report',//标题前的图片
////    				modal:true,//是否模态窗口
////    				collapsible:true,
////    				width:400,
////    				height:240,
////    				maximizable:true,//（是否增加最大化，默认没有）
////    				items:[Ext.create('Ext.panel.Panel',{
////                		width: 300,
////                        bodyStyle: "padding:5px;font-size:12px;"
////                	})],
////    				listeners: {
////    	                afterlayout: function() {
////    	                	var panel = this.down('panel');
////                            tpl = Ext.create('Ext.Template', 
////                                '<p><div style="float:left;width:15%">'+FHD.locale.get('fhd.pages.test.field.name')+'</div>: {name}</p>',
////                                '<p><div style="float:left;width:15%">'+FHD.locale.get('fhd.pages.test.field.title')+'</div>: {title}</p>',
////                                '<p><div style="float:left;width:15%">'+FHD.locale.get('fhd.pages.test.field.myLocale')+'</div>: {myLocaleName}</p>',
////                                '<p><div style="float:left;width:15%">NUMBER</div>: {num}</p>',
////                                '<p><div style="float:left;width:15%">'+FHD.locale.get('fhd.pages.test.field.parentName')+'</div>: {parentName}</p>',
////                                '<p><div style="float:left;width:15%">'+FHD.locale.get('fhd.pages.test.field.level')+'</div>: {level}</p>'
////                            );
////
////    	                    tpl.overwrite(panel.body, data);
////    	                }
////    	            },
////    	            buttons:[
////    	            	{itemId:'viewcancel',text: FHD.locale.get('fhd.common.cancel'),iconCls: 'icon-cancel',handler:close}
////    	            ]
////    		    }).show();
////    		}
////    	});
//    },
//    
//    close : function(){
////    	viewwindow.close();
//    },
//    
    del : function(){//删除方法
////    	var selection = treegrid.getSelectionModel().getSelection();//得到选中的记录
////    	Ext.MessageBox.show({
////    		title : FHD.locale.get('fhd.common.delete'),
////    		width : 260,
////    		msg : FHD.locale.get('fhd.common.makeSureDelete'),
////    		buttons : Ext.MessageBox.YESNO,
////    		icon : Ext.MessageBox.QUESTION,
////    		fn : function(btn) {
////    			if (btn == 'yes') {//确认删除
////    				var ids = [];
////    				for(var i=0;i<selection.length;i++){
////    					ids.push(selection[i].get('id'));
////    				}
////    				FHD.ajax({//ajax调用
////    					url : delUrl,
////    					params : {
////    						ids:ids.join(',')
////    					},
////    					callback : function(data){
////    						if(data){//删除成功！
////    							Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
////    							treeComboxStore.load();//重新加载treeCombox
////    							treegrid.store.load();
////    						}
////    					}
////    				});
////    			}
////    		}
////    	});
    },
//    
    setstatus : function(){//设置你按钮可用状态
////    	var length = treegrid.getSelectionModel().getSelection().length;
////    	treegrid.down('#edit').setDisabled(length == 0 || length>1);
////    	treegrid.down('#del').setDisabled(length == 0);
    },
//    /***function end***/
    
    
    views : function(name){
//    	debugger;
//    	if(Ext.getCmp('riskEditId').items.length != 0){
//    		Ext.getCmp('riskEditId').removeAll();
//    	}
//    	
//    	Ext.getCmp('riskEditId').setNumberValue('nain22344');
//    	Ext.getCmp('riskEditId').setParentValue('');
//    	Ext.getCmp('riskEditId').setNameValue(name);
//    	Ext.getCmp('riskEditId').setReValue(name + '描述');
//    	Ext.getCmp('riskEditId').setResponsibilityReValue('责任部门');
//    	Ext.getCmp('riskEditId').setRelatedReValue('相关部门');
//    	Ext.getCmp('riskEditId').setRelatedKpi();
//    	Ext.getCmp('riskEditId').setRelatedProcess();
//    	Ext.getCmp('riskEditId').doLayout();
    	
    	Ext.getCmp('industryCardId').riskInfoTab.riskEditContainer.items.items[0].load(name);
    	Ext.getCmp('industryCardId').showRiskInfoTab();
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
//            	me.views(firstNode.raw.name);
            }else{
            	return;
            }
        }

//    	//请求风险详细信息
//    	FHD.ajax({
//   			async:false,
//   			params: {
//                riskId: selectedNode.data.id
//            },
//            url: __ctxPath + '/risk/findRiskById.f',
//            callback: function (ret) {
//            	//显示目标详细信息
//            	Ext.fcache.obj.views["riskFormView"].reLoadData(ret);
//            }
//        });
 
    },
    
    initNodeClick : function(){
		var me = this;
		var selectedNode = null;
		var nodeItems = me.getSelectionModel().selected.items;
		
		if (nodeItems.length > 0) {
			selectedNode = nodeItems[0];
		}
		//没有选中节点，默认选中第一个节点
		if (selectedNode == null) {
			me.currentNodeClick();
		}
    },
    
    // 初始化方法
    initComponent: function() {
        var me = this;
        
        me.id = 'riskGridTreeId';
//        me.url = 'test/queryTestMvcTree.f';
        me.fieldstore = Ext.create('Ext.data.Store',{//myLocale的store
        	fields : ['id', 'name'],
        	data : [{'id' : 'zh-cn','name' : FHD.locale.get('fhd.pages.test.chinese')},{'id' : 'en','name' : FHD.locale.get('fhd.pages.test.english')}]
        });
        
        me.treegridColums =[
        	{
        		hidden:true,
        		text: 'ID',
        	    dataIndex: 'id'
        	},
        	{
        	    xtype: 'treecolumn',
        	    text: '风险名称',
        	    flex: 2,
        	    dataIndex: 'name',
        	    sortable: true,
        	    renderer:function(value,metaData,record,colIndex,store,view) {
        	    	debugger;
        		    return "<a href=\"javascript:void(0);\" onclick=\"Ext.getCmp('" + me.id + "').views('" + record.get('name') + "') \">"+value+"</a>";  
        		}
        	},
        	{
        	    text: '上级风险',
        	    flex: 1,
        	    dataIndex: 'parent',
        	    sortable: true
        	},
        	{
        	    text: '责任部门',
        	    flex: 1,
        	    dataIndex: 'responsibilityDe',
        	    sortable: true
        	},
        	{
        	    text: '相关部门',
        	    flex: 1,
        	    dataIndex: 'relatedDe',
        	    sortable: true
        	}
        ];
        
        me.tbars =[//菜单项
   			{text : '导入', iconCls: 'icon-ibm-move-left-disabled',id:'riskImportId',
   	    		handler:function(){me.edit(me);}, scope : this},
       		{text : '导出', iconCls: 'icon-ibm-move-right-disabled',id:'riskExportId',
   	    		handler:function(){me.edit(me);}, scope : this},
       		{text : '修改', iconCls: 'icon-edit',id:'riskEditId',
   	    		handler:function(){me.edit(me);}, scope : this},
       		{text : '删除', iconCls: 'icon-del',id:'riskDelId',
   	    		handler:function(){me.edit(me);}, scope : this}
   		];
        
        Ext.apply(me, {
        	useArrows: true,
            rootVisible: false,
            multiSelect: true,
            rowLines:true,
            singleExpand: false,
            checked: false,
            multiSelect:false,
            border:false,
            root : {
                expanded: true,
                children: [
                    {id: 'A', leaf: false, level : '1', myLocale : 'zh-cn', 
                    	name : 'A管理风险', 
                    	parent : '',
                    	expanded: true, 
                    	responsibilityDe : '风险责任部门',
                    	relatedDe : '风险相关部门',
                    	children  :[{
                    		id: 'A1', leaf: true, level : '2', myLocale : 'zh-cn', 
                    		name : 'A1物质风险', 
                    		parent : 'A管理风险',
                    		responsibilityDe : '风险责任部门',
                        	relatedDe : '风险相关部门',
                    		
                    	}]},
                    {id: 'B', leaf: true, level : '1', myLocale : 'zh-cn', 
                    		name : 'B业务风险', 
                    		parent : '', 
                    		responsibilityDe : '风险责任部门',
                        	relatedDe : '风险相关部门',}
                ]
            },
//    		renderTo: 'demo', //渲染到id为demo的div里
//    		url: me.url,//调用后台url
    		height:FHD.getCenterPanelHeight()-120,//高度为：获取center-panel的高度
    		cols:me.treegridColums,//cols:为需要显示的列
    		tbarItems:me.tbars
        });
        
        me.callParent(arguments);
        me.initNodeClick();
    }

});
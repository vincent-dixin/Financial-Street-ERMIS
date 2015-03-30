/**
 * 流程树
 * 
 * @author 宋佳
 */
Ext.define('FHD.view.icm.icsystem.FlowTree',{
	extend: 'FHD.ux.TreePanel',
    alias: 'widget.flowtree',
	url : __ctxPath + '/process/processTree/findrootProcessTreeLoader.f',
    
	root: {
        text: '流程库',
        dbid: 'process_root',
        leaf: false,
        code: 'category',
        type: '',
        expanded: true
    },
	autoScroll:true,
    animate: false,
    rootVisible: true,
    collapsible: true,
    border: false,
    singleExpand: false,
    checked: false,
    selectId : '',
	onTreepanelItemClick: function(store, records){
		var me = this;
		//me.up('panel').processeditpanel.removeAll(true);//删除
    	//me.up('panel').processeditpanel.addComponent();
		var floweditpanel = Ext.getCmp('floweditpanel');
		var flownotelist = Ext.getCmp('flownotelist');
//		var noteeditform = Ext.getCmp('noteeditform');
		var flowrisklist = Ext.getCmp('flowrisklist');
		floweditpanel.removeAll(true);
		floweditpanel.addComponent();
		var parentNode = records.parentNode;
		floweditpanel.reLoadData(store, records, parentNode);
		me.selectId = records.data.id;
		flownotelist.paramObj.processId = me.selectId;
//		noteeditform.paramObj.processId = me.selectId;
		flownotelist.reloadData();
		flownotelist.onchange();    //设置编辑和删除的属性
		flowrisklist.paramObj.processId = me.selectId;
		flowrisklist.reloadData();
		flowrisklist.onchange();    //设置编辑和删除的属性
	},
	refreshTree: function(){
		var me = this;
        me.getStore().load();
	},
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            listeners: {
                itemclick: me.onTreepanelItemClick
//                load: function (store, records) { //默认选择首节点
//                    var rootNode = me.getRootNode();
//                    if (rootNode.childNodes.length > 0) {
//                        me.getSelectionModel().select(rootNode.firstChild);//默认选择首节点
//                        Ext.getCmp('scorecardmainpanel').reLoadData(rootNode.firstChild);//加载首节点数据
//                    }
//                }
            },
            viewConfig: {
                listeners: {
                    itemcontextmenu: function (view, rec, node, index, e) {
                        e.stopEvent();
                        return false;
                    }
                }
            }

        });

        me.callParent(arguments);
    }

});
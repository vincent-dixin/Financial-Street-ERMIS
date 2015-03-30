Ext.define('FHD.view.kpi.myfolder.MyFolderTree', {
    extend: 'FHD.ux.TreePanel',
    alias: 'widget.myfoldertree',


    autoScroll: true,
    animate: false,
    rootVisible: true,
    collapsible: false,
    border: false,
    multiSelect: true,
    rowLines: false,
    singleExpand: false,
    checked: false,

    url: __ctxPath + "/kpi/myfolder/myfoldertreeloader.f", //调用后台url
    root: {
        id: "myfolder_root",
        text: FHD.locale.get('fhd.kpi.kpi.tree.myfolder'),
        dbid: "myfolder_root",
        leaf: false,
        type: "myfolder",
        expanded: true,
        iconCls:'icon-ibm-new-group-view'
    },


    initComponent: function () {
        var me = this;

        Ext.applyIf(me, {
            listeners: {
                itemclick: me.onTreepanelItemClick,
                load: function (store, records) {
                    //me.treeload(store,records);
                }

            }
        });

        me.callParent(arguments);
    },
    /**
     * 当前节点点击函数,如果没有选中节点,默认选中首节点
     */
    currentNodeClick:function(){
    	var me = this;
    	var selectedNode;
    	var allmetricmainpanel = Ext.getCmp('myfoldertab');
    	var nodeItems = me.getSelectionModel().selected.items;
        if (nodeItems.length > 0) {
            selectedNode = nodeItems[0];
        }
        if(selectedNode==null){
        	var firstNode = me.getRootNode().firstChild;
        	me.getSelectionModel().select(firstNode);
        	allmetricmainpanel.reLoadData(firstNode);//默认选中首节点
        }else{
        	allmetricmainpanel.reLoadData(selectedNode);//当前选中节点
        }
    },
    /**
     * 树节点点击函数
     */
    onTreepanelItemClick: function (tablepanel, record, item, index, e, options) {
    	if (record.parentNode == null) {
			 return;//如果是根节点直接返回
		}
    	var metrictreecardpanel = Ext.getCmp('metrictreecardpanel');
        var myfoldertab = Ext.getCmp('myfoldertab');
        var activeItem = metrictreecardpanel.getActiveItem();
        if (activeItem.id == 'myfoldertree') { //当点击添加或编辑指标时,在点击战略节点时,需要设置记分卡strategyobjectivemainpanel主面板在右侧
            Ext.getCmp('metriccentercardpanel').setActiveItem(myfoldertab);
        }
    	Ext.getCmp('myfoldertab').reLoadData();
    }

});
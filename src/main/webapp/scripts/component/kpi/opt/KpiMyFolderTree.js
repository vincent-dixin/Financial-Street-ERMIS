Ext.define('FHD.ux.kpi.opt.KpiMyFolderTree', {
	extend : 'Ext.Panel',
	alias : 'widget.kpimyfoldertree',
	border:true,
	

	clickFunction:function(node){
		
	},

	initComponent : function() {
		var me = this;
		
		me.tree = Ext.create('FHD.ux.TreePanel', {
            clickFunction: me.clickFunction,
            autoScroll:true,
            animate: false,
            rootVisible: false,
            width: 265,
            maxWidth: 300,
            split: true,
            collapsible: false,
            border: false,
            multiSelect: true,
            rowLines: false,
            singleExpand: false,
            checked: false,
            url: __ctxPath + "/kpi/myfolder/myfoldertreeloader.f", //调用后台url
            rootVisible: true,
            height: FHD.getCenterPanelHeight() - 5,
            root: {
                id: "myfolder_root",
                text: FHD.locale.get('fhd.kpi.kpi.tree.myfolder'),
                dbid: "myfolder_root",
                leaf: false,
                type: "myfolder"
                ,expanded: true,
                iconCls:'icon-ibm-new-group-view'
            },
            listeners: {
                itemclick: function (node, record, item) {
                    me.clickFunction(record);
                },
                load:function(store,records){
                	me.treeload(store,records);
                }
                
            }

		});
		
		
		
		Ext.applyIf(me, {
			layout : {
				type : 'fit'
			}
		});
		
		me.callParent(arguments);
		
		me.add(me.tree);
	}

});
Ext.define('FHD.view.riskinput.RiskInputMenuPanel', {
    extend: 'FHD.ux.MenuPanel',
    alias: 'widget.riskinputmenupanel',
    
    requires: [
    	'FHD.view.riskinput.RiskEventEditCardPanel',
    	'FHD.view.riskinput.EventEditCardPanel'
    ],
    // 初始化方法
    initComponent: function() {
        var me = this;
		
        Ext.applyIf(me, {
        	items:[{
		        text: '风险事件',
		        iconCls:'icon-btn-assessPlan',
		        scale: 'large',
				iconAlign: 'top',
				handler:function(){	
					var riskinputcenterpanel = me.up('panel').riskinputcenterpanel;
					riskinputcenterpanel.removeAll(true);
					var riskeventeditcardpanel = Ext.widget('riskeventeditcardpanel');
					riskinputcenterpanel.add(riskeventeditcardpanel);
					riskeventeditcardpanel.reloadData();
				}
		    },{
		        text: '事件',
		        iconCls:'icon-btn-assessPlan',
		        scale: 'large',
				iconAlign: 'top',
				handler:function(){	
					var riskinputcenterpanel = me.up('panel').riskinputcenterpanel;
					riskinputcenterpanel.removeAll(true);
					var eventeditcardpanel = Ext.widget('eventeditcardpanel');
					riskinputcenterpanel.add(eventeditcardpanel);
					eventeditcardpanel.reloadData();
				}
		    }]
        });

        me.callParent(arguments);
    }
});
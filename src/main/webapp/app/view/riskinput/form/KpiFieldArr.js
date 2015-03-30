/**
 * 
 * 风险上报标准
 * 使用card布局
 * 
 * 下级有两个组件 潜在风险上报标准、历史风险上报标准
 * 
 * @author 宋佳
 */
Ext.define('FHD.view.riskinput.form.KpiFieldArr', {
    extend: 'Ext.form.FieldContainer',
    alias: 'widget.kpifieldarr',
    requires: [
    ],
	autoHeight: true,
	layout: {
		type: 'hbox'
	},
	initComponent: function() {
		/*定性 qualitative  定量quantification*/
		var me = this;
		//定性
		//定量
		var kpi = Ext.widget('textfield', {
			name : 'kpi', 
			value: me.kpiname,
			margin: '7 10 0 105',
			width:300,
			colspan : 5
		});
		var kpivalue = Ext.widget('textfield', {
			margin: '7 0 0 20',
			name : 'kpivalue', 
			width:170,
			colspan : 5
		});
//        var delQuantification = Ext.widget('button',{
////        	iconCls:'icon-magnifier',   /*查询按钮图标*/
////        	iconCls :'icon-del-min',   /*查询按钮图标*/
//        	margin: '7 10 0 20',
//        	text : '删除',
//        	handler : me.delSelf,
//        	colspan : 2
//        });
        
        var delQuantification = Ext.widget('label',{
        	margin: '7 10 0 0',
			html : "<a href='javascript:void(0)' onclick='Ext.getCmp(\""+me.id+"\").delSelf()'>删除</a>"
        });

        Ext.apply(me, {
            items : [
            	kpi,
            	kpivalue,
            	{
                    xtype: 'hiddenfield',
                    width: 100,
                    fieldLabel: 'Label'
                },
            	delQuantification
            	]
        });
        me.callParent(arguments);
    },
    delSelf : function(){
    	var self = this;
    	upPanel = self.up('container');
    	upPanel.remove(self);
    }
    	
});
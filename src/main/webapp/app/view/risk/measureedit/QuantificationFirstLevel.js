/**
 * 
 * 风险上报标准
 * 使用card布局
 * 
 * 下级有两个组件 潜在风险上报标准、历史风险上报标准
 * 
 * @author 宋佳
 */
Ext.define('FHD.view.risk.measureedit.QuantificationFirstLevel', {
    extend: 'Ext.form.FieldContainer',
    alias: 'widget.quantificationfirstlevel',
    requires: [
    	'FHD.ux.kpi.KpiSelector'
    ],
    autoHeight: true,
    autoWidth : true,
	layout: {
        type: 'vbox',
        align : 'stretch'
    },
//    defaults :{
//    	margin: '7 30 3 30'
//    },
    kpiname : '',
	initComponent: function() {
		/*定性 qualitative  定量quantification*/
		var me = this;
		//定性
		//定量
		me.quantification = Ext.widget('kpiselector', {
			name : 'quantification', 
			value: me.kpiname,
			labelText : '指标',
			width:300,
			colspan : 2
		});
        me.delQuantification = Ext.widget('label',{
			html : "<a href='javascript:void(0)' onclick='Ext.getCmp(\""+me.id+"\").delSelf()'>删除</a>",
			colspan : 2
        });
        var fieldcontainer1 = Ext.widget('fieldcontainer',{
        	layout : {
        		type : 'hbox'
        	},
        	items : [
        	me.quantification,
        	{
        		xtype : 'tbspacer',
        		flex : 1
        	},
        	me.delQuantification]
        }
        );
        var redLabel = {
			margin: '7 30 3 30',
			xtype : 'label',
			text : '红',
			colspan : 1
        };  
        var yellowLabel = {
        	margin: '7 30 3 30',
			xtype : 'label',
			text : '黄',
			colspan : 1
        };  
        var greenLabel = {
        	margin: '7 30 3 30',
			xtype : 'label',
			text : '绿',
			colspan : 1
        };
        var formula1 = {
        	margin: '7 30 3 30',
        	xtype : 'label',
			text : '0<=x<1000',
			colspan : 1
        };
        var formula2 = {
        	margin: '7 30 3 30',
        	xtype : 'label',
			text : '1000 <= x < 2000',
			colspan : 1
        };
        var formula3 = {
        	margin: '7 30 3 30',
        	xtype : 'label',
			text : '2000 <= x < 5000',
			colspan : 1
        };
        me.addSetPlanBtn = Ext.widget('label',{
			html : "<a href='javascript:void(0)' onclick='Ext.getCmp(\""+me.id+"\").addQuantitative()'>设定预案/</a>"
        });
        me.addYellowSetPlanBtn = Ext.widget('label',{
			html : "<a href='javascript:void(0)' onclick='Ext.getCmp(\""+me.id+"\").addQuantitative()'>设定预案/</a>"
        });
        me.addGreenSetPlanBtn = Ext.widget('label',{
			html : "<a href='javascript:void(0)' onclick='Ext.getCmp(\""+me.id+"\").addQuantitative()'>设定预案/</a>"
        });
		me.addLookPlanBtn = Ext.widget('label',{
			html : "<a href='javascript:void(0)' onclick='Ext.getCmp(\""+me.id+"\").addQuantitative()'>查看预案</a>"
        });
		me.addYellowLookPlanBtn = Ext.widget('label',{
			html : "<a href='javascript:void(0)' onclick='Ext.getCmp(\""+me.id+"\").addQuantitative()'>查看预案</a>"
        });
		me.addGreenLookPlanBtn = Ext.widget('label',{
			html : "<a href='javascript:void(0)' onclick='Ext.getCmp(\""+me.id+"\").addQuantitative()'>查看预案</a>"
        });
		me.delMySelfBtn = Ext.widget('label',{
			html : "<a href='javascript:void(0)' onclick='Ext.getCmp(\""+me.id+"\").delMySelf()'>删除</a>"
        });
		me.delYellowMySelfBtn = Ext.widget('label',{
			html : "<a href='javascript:void(0)' onclick='Ext.getCmp(\""+me.id+"\").delMySelf()'>删除</a>"
        });
        var fieldcontainerRed = Ext.widget('fieldcontainer',{
        	layout : {
        		type : 'hbox'
        	},
        	items : [
        	{
                    xtype: 'hiddenfield',
                    width: 125,
                    fieldLabel: 'Label',
                    colspan : 1
            },redLabel,formula1,
        	{
        		xtype : 'tbspacer',
        		flex : 1
        	},
            me.addSetPlanBtn,
            me.addLookPlanBtn
        	]
        }
        );
        var fieldcontainerYellow = Ext.widget('fieldcontainer',{
        	layout : {
        		type : 'hbox'
        	},
        	items : [
        		{
                    xtype: 'hiddenfield',
                    width: 125,
                    fieldLabel: 'Label',
                    colspan : 1
                },
                yellowLabel,formula2,
                {
        		    xtype : 'tbspacer',
        		    flex : 1
        	    },
                me.addYellowSetPlanBtn,
                me.addYellowLookPlanBtn
        	]
        }
        );
        var fieldcontainerGreen = Ext.widget('fieldcontainer',{
        	layout : {
        		type : 'hbox'
        	},
        	items : [
        	{
                    xtype: 'hiddenfield',
                    width: 125,
                    fieldLabel: 'Label',
                    colspan : 1
                },
                greenLabel,formula3,
                {
        		    xtype : 'tbspacer',
        		    flex : 1
        	    },
                me.addGreenSetPlanBtn,
                me.addGreenLookPlanBtn]
        }
        );
        Ext.apply(me, {
            items : [
            	fieldcontainer1,
            	fieldcontainerRed,
            	fieldcontainerYellow,
                fieldcontainerGreen
            	]
        });
        me.callParent(arguments);
    },
    delSelf : function(){
    	var self = this.up('fieldcontainer');
    	upPanel = self.up('fieldset');
    	upPanel.remove(self);
    }
});
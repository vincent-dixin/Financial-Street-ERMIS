/**
 * 
 * 风险上报标准
 * 使用card布局
 * 
 * 下级有两个组件 潜在风险上报标准、历史风险上报标准
 * 
 * @author 宋佳
 */
Ext.define('FHD.view.risk.responseplan.Solutions', {
    extend: 'Ext.form.FieldContainer',
    alias: 'widget.solutions',
    requires: [
    ],
	autoHeight: true,
	layout: {
        type: 'column'
    },
    kpiname : '',
	initComponent: function() {
		//定量
		var me = this;
		me.desc = Ext.widget('textfield', {
			name : 'desc', 
			fieldLabel : '描述',
			value: '',
			columnWidth : .2
		});
		//责任人
		me.noteradio = Ext.create('FHD.ux.org.CommonSelector', {
			fieldLabel : '责任人',
			labelAlign : 'left',
			name:'meaSureempId',
			type : 'emp',
			allowBlank: false,
			multiSelect : false,
			value : '',
			columnWidth : .3
		});
		me.isSystem = Ext.widget('checkboxfield',{
                    boxLabel: '系统动作',
                    columnWidth : .2
                });
		me.isTrack = Ext.widget('checkboxfield',{
                    boxLabel: '是否风险管理部跟踪',
                    columnWidth : .2
                });
        me.delConfidence = Ext.widget('button',{
//        	iconCls:'icon-magnifier',   /*查询按钮图标*/
//        	iconCls :'icon-del-min',   /*查询按钮图标*/
        	text : '删除',
        	handler : me.delSelf,
        	columnWidth : .1
        });
        Ext.apply(me, {
            items: [
               me.desc,
               me.noteradio,
               me.isSystem,
               me.isTrack,
               me.delConfidence
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
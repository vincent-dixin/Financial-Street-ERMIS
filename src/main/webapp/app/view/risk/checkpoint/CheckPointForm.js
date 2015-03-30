/**
 * 风险预览页面，其中嵌套了风险控制矩阵
 * 
 * @author 宋佳
 */
Ext.define('FHD.view.risk.checkpoint.CheckPointForm', {
	extend: 'Ext.form.Panel',
	alias: 'widget.checkpointform',
    requires: [
    	'FHD.view.risk.checkpoint.CheckPointMeasureList',
    	'FHD.view.risk.checkpoint.SetingTimeForm'
    ],
    defaults : {
		margin: '7 5'
	},
    layout : {
    	type : 'hbox',
    	align : 'stretch'
    },
    frame: false,
    border : false,
	autoWidth : true,
   // 初始化方法
    initComponent: function() {
        var me = this;
		me.checkpointmeasurelist = Ext.widget('checkpointmeasurelist',{
			flex : 2,
			searchable:false,
			pagable : false
		});
		me.setingtime = Ext.widget('setingtimeform',{
			flex : 1
		});
        Ext.applyIf(me,{
        	items : [me.checkpointmeasurelist,me.setingtime]
        })
  		me.callParent(arguments);
       	}
});
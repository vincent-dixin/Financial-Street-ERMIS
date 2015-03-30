Ext.define('FHD.view.icm.rectify.form.ImproveViewForm', {
	extend : 'Ext.form.Panel',
	alias: 'widget.improveviewform',
	layout : {
		type : 'column'
	},
	bodyPadding:'0 3 3 3',
	requires:[
    	'FHD.view.icm.rectify.form.RectifyView',
    	'FHD.view.icm.rectify.component.ImprovePlanViewGrid'
    ],
	defaults:{
		columnWidth: 1
	},
	autoWidth: true,
	collapsed : false,
	autoScroll: false,
	fieldsetCollapsed: true,
	border: false,
	initComponent :function() {
		var me = this;
		me.rectifyView = Ext.widget('rectifyview');
    	me.improveplanviewgrid = Ext.widget('improveplanviewgrid');
    		
    	Ext.applyIf(me,{
			items:[
				me.rectifyView,
				{
					xtype : 'fieldset',
					layout : {
						type : 'column'
					},
					defaults:{
						columnWidth:1
					},
					collapsed : false,
					collapsible : false,
					title : '方案列表',
					items:[me.improveplanviewgrid]
				}
			]
    	});	
		me.callParent(arguments);
	},
	reloadData:function(){
		var me = this;
		if(me.improveId){
			me.rectifyView.loadData(me.improveId);
			me.improveplanviewgrid.loadData(me.improveId);
		}
	}
});
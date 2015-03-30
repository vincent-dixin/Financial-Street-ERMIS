Ext.define('FHD.view.icm.rectify.form.RectifyDefectSelectForm', {
	extend : 'Ext.form.Panel',
	alias: 'widget.rectifydefectselectform',
	requires: [
       'FHD.view.icm.rectify.form.RectifyView',
       'FHD.view.icm.defect.component.DefectRelaImproveGrid'
    ],
	layout : {
		type : 'column'
	},
	defaults:{
		columnWidth:1/1
	},
	bodyPadding:'0 3 3 3',
	autoScroll:true,
	initComponent : function() {
		var me=this;
		//预览
		me.rectifyview = Ext.widget('rectifyview');
		//缺陷选择列表
		me.defectrelaimprovegrid = Ext.widget('defectrelaimprovegrid',{border: false});
		//fieldSet
		var childItems={
			xtype : 'fieldset',
			defaults : {
				columnWidth: 1
			},
			layout : {
				type : 'column'
			},
			collapsed: false,
			collapsible : false,
			title : '整改范围',
			items : [me.defectrelaimprovegrid]
		};
		me.items=[me.rectifyview,childItems];
		me.callParent(arguments);
	},
	loadData: function(improveId){
		var me = this;
		me.improveId = improveId;
		me.reloadData();
	},
	reloadData:function(){
    	var me=this;
    	me.rectifyview.loadData(me.improveId);
		me.defectrelaimprovegrid.loadData(me.improveId);
	}
});
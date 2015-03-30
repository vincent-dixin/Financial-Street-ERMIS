/**
 * 评价计划编辑第二步：选择范围
 */
Ext.define('FHD.view.icm.icsystem.constructplan.form.ConstructPlanRangeFormForView',{
	extend:'Ext.form.Panel',
	alias: 'widget.constructplanrangeformforview',
	requires: [
       'FHD.view.icm.icsystem.constructplan.form.ConstructPlanPreviewForm',
       'FHD.view.icm.icsystem.constructplan.ConstructPlanRelaStandardViewGrid'
    ],
    initParam : function(paramObj){
         var me = this;
    	 me.paramObj = paramObj;
	},
	autoScroll:true,
	bodyPadding:'0 3 3 3',
	layout : {
		type : 'column'
	},
	defaults:{
		columnWidth:1/1
	},
	
	initComponent : function() {
		var me=this;
		//评价计划预览表单
		me.basicInfo=Ext.widget('constructplanpreviewform',{
			columnWidth:1/1,
			businessId:me.businessId
		});
		//评价计划可编辑列表
		me.constructPlanGrid=Ext.widget('constructplanrelastandardviewgrid',{
			columnWidth:1/1,
			searchable : false
		});
		//fieldSet
		me.childItems={
			xtype : 'fieldset',
			//margin: '7 10 0 30',
			layout : {
				type : 'column'
			},
			collapsed: false,
			columnWidth:1/1,
			collapsible : true,
			title : '建设范围',
			items : [me.constructPlanGrid]
		};
		me.items=[me.basicInfo,me.childItems];
		me.callParent(arguments);
	},
	reloadData:function(){
		var me=this;
		//基本信息和详细信息刷新
		me.basicInfo.initParam(me.paramObj);
		me.basicInfo.reloadData();
		//选择范围刷新
		me.constructPlanGrid.extraParams=me.paramObj;
    	me.constructPlanGrid.reloadData();
		/*
		if(editflag){
			//基本信息和详细信息刷新
			me.basicInfo.loadData(businessId);
			//选择范围刷新
			me.constructPlanGrid.extraParams.businessId = businessId;
			me.constructPlanGrid.businessId = businessId;
			
			me.constructPlanGrid.store.proxy.extraParams.assessPlanId = businessId;
        	me.constructPlanGrid.store.load();
		}else{
			//基本信息和详细信息刷新
			me.basicInfo.loadData(businessId);
			//选择范围刷新
			me.constructPlanGrid.extraParams.businessId = businessId;
			me.constructPlanGrid.businessId = businessId;
			
			me.constructPlanGrid.store.proxy.extraParams.assessPlanId = businessId;
        	me.constructPlanGrid.store.load();
		}
		*/
	}
});
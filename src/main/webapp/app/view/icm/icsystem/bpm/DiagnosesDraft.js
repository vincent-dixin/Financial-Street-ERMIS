Ext.define('FHD.view.icm.icsystem.bpm.DiagnosesDraft', {
	/*
 * 内控评价计划，审批页面
 * businessId:评价计划Id
 */
	extend:'Ext.panel.Panel',
	aligs:'widget.diagnosesdraft',
	requires: [
       'FHD.view.icm.icsystem.constructplan.PlanStandardDiagnosesEditGrid',
       'FHD.ux.icm.common.FlowTaskBar',
       'FHD.view.icm.icsystem.constructplan.form.ConstructPlanPreviewForm'
    ],
//    initParam : function(paramObj){
//         var me = this;
//    	 me.paramObj = paramObj;
//	},
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
		me.basicInfo=Ext.widget('constructplanpreviewform',{
			columnWidth:1/1,
			businessId:me.businessId,
			border:false
		});
		//评价计划可编辑列表
		me.diagnosesEditGrid=Ext.widget('planstandarddiagnoseseditgrid',{
			border : false,
			columnWidth:1/1,
			executionId : me.executionId,
			businessId : me.businessId,
			checked:false,
			pagable : false
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
			collapsible : false,
			title : '请填写诊断结果',
			items : [me.diagnosesEditGrid]
		};
		
		me.bbar={
			items: [
				'->',{
					text: '保存',
    				iconCls: 'icon-control-stop-blue',
    				handler: function () {
					    me.diagnosesEditGrid.saveData();
    				} 
				},{
       				text: '提交',
    				iconCls: 'icon-operator-submit',
    				handler: function () {
						if(!me.diagnosesEditGrid.saveSubmitData()){
							
						}else{
							var jsonArray=[];
							var rows = me.diagnosesEditGrid.store.data;
							Ext.each(rows.items,function(item){
								jsonArray.push(item.data);
							});
							if(jsonArray.length>0){
								 FHD.ajax({
					    		     url : __ctxPath+ '/icm/icsystem/diagnosessubmit.f',
					    		     params : {
					    		    	 modifiedRecord:Ext.encode(jsonArray),
					    		    	 constructPlanId : me.businessId,
					    				 executionId : me.executionId
					    			 },
					    			 callback : function(data) {
					    				if(me.winId){
											Ext.getCmp(me.winId).close();
										}
					    			 }
					    		});
							}
						}
    				} 
    			}
    		]
		};
		Ext.applyIf(me, {
        	layout:{
        		align: 'stretch',
        		type: 'vbox'
        	},
        	items:[Ext.widget('flowtaskbar',{
    		jsonArray:[
	    		{index: 1, context:'1.合规诊断',status:'current'},
	    		{index: 2, context:'2.缺陷反馈',status:'undo'},
	    		{index: 3, context:'3.缺陷整理',status:'undo'}
	    	]
    	}),me.basicInfo,me.childItems]
		});
		me.callParent(arguments);
	},
	reloadData:function(){
		var me=this;
		me.basicInfo.initParam({
    		businessId : me.businessId
    	});
    	me.basicInfo.reloadData();
    	me.diagnosesEditGrid.reloadData();
	}
});
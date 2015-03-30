/**
 * 评价执行
 */
Ext.define('FHD.view.icm.assess.form.AssessPlanDraftForm',{
	extend: 'Ext.form.Panel',
    alias: 'widget.assessplandraftform',
    
    autoScroll:true,
    bodyPadding:'0 3 3 3',
    border:false,
    
	initComponent : function() {
	    var me = this;

    	me.pTestStatGrid=Ext.create('FHD.view.icm.assess.component.PracticeTestDraftGrid',{
    		searchable:false,
    		pagable : false,
    		height:100,
    		columnWidth:1/1,
    		margin: '7 10 0 30',
    		showAssessDate:false,
    		executionId:me.executionId,
    		businessId:me.businessId
    	});
    	me.sTestStatGrid=Ext.create('FHD.view.icm.assess.component.SampleTestDraftGrid',{
    		searchable:false,
    		pagable : false,
    		height:100,
    		columnWidth:1/1,
    		margin: '7 10 0 30',
    		showAssessDate:false,
    		executionId:me.executionId,
    		businessId:me.businessId
    	});

    	me.pTestStatFieldset = {
			xtype : 'fieldset',
			//margin: '7 10 0 30',
			layout : {
				type : 'column'
			},
			collapsed : false,
			columnWidth:1/1,
			collapsible : true,
			title: '穿行测试底稿预览',
			items :[me.pTestStatGrid]
		};
    	me.sTestStatFieldset={
			xtype : 'fieldset',
			//margin: '7 10 0 30',
			layout : {
				type : 'column'
			},
			collapsed : false,
			columnWidth:1/1,
			collapsible : true,
			title: '抽样测试底稿预览',
			items :[me.sTestStatGrid]
		};
    	me.sampleAnalysisOfResults={
			xtype : 'fieldset',
			//margin: '7 10 0 30',
			layout : {
				type : 'column'
			},
			collapsed : false,
			columnWidth:1/1,
			collapsible : true,
			title: '结果分析',
			items :[
				{
					xtype     : 'textareafield',
					//grow      : true,
					name      : 'sampleAnalysisOfResults',
					columnWidth : 1 / 1,
					hideLabel : true,
					fieldLabel: '结果分析',
					labelAlign : 'left',
					margin: '7 10 0 30',
					labelWidth : 80,
					row : 5
				}
			]
		};

    	Ext.applyIf(me,{
    		layout:{
    	    	type : 'column'
    	    },
    	    items:[me.pTestStatFieldset,me.sTestStatFieldset,me.sampleAnalysisOfResults]
    	});

    	me.callParent(arguments);
	},
	saveResultAnalysis:function(){
		var me=this;

		//提交from表单
        var form = me.getForm();
        var vobj = form.getValues();
        
        if(vobj.sampleAnalysisOfResults != ''){
        	FHD.ajax({
    	        url: __ctxPath + '/icm/assess/saveAssessorResultAnalysis.f',
    	        async:false,
    	        params: {
    		        assessPlanId:me.businessId,
    		        executionId:me.executionId,
    		        resultAnalysis: vobj.sampleAnalysisOfResults
    	        },
    	        callback: function (data) {
    	        	if(data){
    	        		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
    	        	}
    	        }
        	});
        }
	},
	loadData:function(businessId,editflag){
		var me=this;
		
		me.businessId=businessId;
		me.editflag=editflag;
		
		//grid刷新
		me.pTestStatGrid.extraParams.assessPlanId=me.businessId;
		me.pTestStatGrid.store.load();
		
		me.sTestStatGrid.extraParams.assessPlanId=me.businessId;
		me.sTestStatGrid.store.load();
		//form刷新
		me.getForm().load({
	        url:__ctxPath + '/icm/assess/findResultAnalysisByAssessplanIdAndExecutionId.f?businessId='+me.businessId+"&executionId="+me.executionId,
	        success: function (form, action) {
	     	   return true;
	        },
	        failure: function (form, action) {
	     	   return false;
	        }
		});
	},
	reloadData:function(){
		var me=this;
		
	}
});
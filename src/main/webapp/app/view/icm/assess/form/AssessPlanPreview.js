/**
 * 评价计划:预览
 */
Ext.define('FHD.view.icm.assess.form.AssessPlanPreview',{
	extend:'Ext.panel.Panel',
	alias: 'widget.assessplanpreview',
	
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
		me.basicInfo=Ext.create('FHD.view.icm.assess.form.AssessPlanPreviewForm',{
			columnWidth:1/1,
			businessId:me.assessPlanId
		});
		//评价计划范围列表
		me.processGrid=Ext.create('FHD.view.icm.assess.component.AssessPlanRelaProcessGrid',{
			columnWidth:1/1,
			margin: '7 10 0 30',
			searchable:false,
			isShow:true,
			extraParams:{
				assessPlanId:me.assessPlanId
			}
		});
		me.processFiledset = {
			xtype : 'fieldset',
			layout: {
				type : 'column'
			},
			collapsed: false,
			columnWidth:1/1,
			collapsible: true,
			title : '评价范围',
			items : [me.processGrid]
		};
		//穿行测试信息
		me.pTestStatGrid=Ext.create('FHD.view.icm.assess.component.PracticeTestDraftGrid',{
    		searchable:false,
    		pagable : false,
    		columnWidth:1/1,
    		margin: '7 10 0 30',
    		showAssessDate:true,
    		businessId:me.assessPlanId
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
		//抽样测试信息
		me.sTestStatGrid=Ext.create('FHD.view.icm.assess.component.SampleTestDraftGrid',{
    		searchable:false,
    		pagable : false,
    		columnWidth:1/1,
    		margin: '7 10 0 30',
    		showAssessDate:true,
    		businessId:me.assessPlanId
    	});
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
		//缺陷信息
    	me.assessDefectGrid=Ext.create('FHD.view.icm.assess.component.AssessDefectFeedbackGrid',{
			margin: '7 10 0 30',
			searchable:false,
			defectsIsAvailable:false,
			feedbackIsAvailable:false,
			isOwnerOrg:'N',
			businessId:me.assessPlanId
		});
		me.assessDefectGridPanel={
			xtype : 'fieldset',
			//margin: '7 10 0 30',
			layout : {
				type : 'fit'
			},
			collapsed : false,
			//columnWidth:1/1,
			collapsible : true,
			title: '缺陷信息汇总',
			items :[me.assessDefectGrid]
		};
		
		me.callParent(arguments);
		
		//评价计划基本信息和详细信息
		me.add(me.basicInfo);
		//评价计划流程范围
		me.add(me.processFiledset);
		
		//me.loadData();
	},
	loadData:function(){
		var me=this;
		
		//基本信息和详细信息刷新
		//me.basicInfo.loadData(me.assessPlanId);
		
		FHD.ajax({
		     url : __ctxPath+ '/icm/assess/findAssessPlanForView.f',
		     params : {
		    	 assessPlanId:me.assessPlanId
			 },
			 callback : function(data) {
				 if (data) {
					 me.assessPlanMeasureId = data.data.assessMeasureId;
					 me.dealStatus = data.data.dealStatus;
					 if('ca_assessment_measure_0'== me.assessPlanMeasureId){
						 //穿行测试--隐藏抽样测试列
						 me.processGrid.down('[dataIndex=isPracticeTest]').show();
						 me.processGrid.down('[dataIndex=practiceNum]').show();
						 me.processGrid.down('[dataIndex=isSampleTest]').hide();
						 me.processGrid.down('[dataIndex=coverageRate]').hide();
						
						 //评价计划已完成或逾期
						 if(me.dealStatus == 'F' || me.dealStatus == 'A'){
							 //评价计划穿行测试信息
							 me.add(me.pTestStatFieldset);
						 }
					 }else if('ca_assessment_measure_1' == me.assessPlanMeasureId){
						 //抽样测试--隐藏穿行测试列
						 me.processGrid.down('[dataIndex=isPracticeTest]').hide();
						 me.processGrid.down('[dataIndex=practiceNum]').hide();
						 me.processGrid.down('[dataIndex=isSampleTest]').show();
						 me.processGrid.down('[dataIndex=coverageRate]').show();
						
						 //评价计划已完成或逾期
						 if(me.dealStatus == 'F' || me.dealStatus == 'A'){
							 //评价计划抽样测试信息
							 me.add(me.sTestStatFieldset);
						 }
					 }else if('ca_assessment_measure_2' == me.assessPlanMeasureId){
						 //全部：穿行测试和抽样测试
						 me.processGrid.down('[dataIndex=isPracticeTest]').show();
						 me.processGrid.down('[dataIndex=practiceNum]').show();
						 me.processGrid.down('[dataIndex=isSampleTest]').show();
						 me.processGrid.down('[dataIndex=coverageRate]').show();
						
						 //评价计划已完成或逾期
						 if(me.dealStatus == 'F' || me.dealStatus == 'A'){
							 //评价计划穿行测试信息
							 me.add(me.pTestStatFieldset);
							 //评价计划抽样测试信息
							 me.add(me.sTestStatFieldset);
						 }
					 }

					 //评价计划已完成或逾期
					 if(me.dealStatus == 'F' || me.dealStatus == 'A'){
						 //评价计划缺陷信息
						 me.add(me.assessDefectGridPanel);
					 }
				 }
			 }
		});
	},
	reloadData:function(){
		var me=this;
		
		me.loadData();
	}
});
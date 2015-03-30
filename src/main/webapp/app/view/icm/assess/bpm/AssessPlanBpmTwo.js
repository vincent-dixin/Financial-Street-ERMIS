/*
 * 内控评价计划，审批页面
 * businessId:评价计划Id
 */
Ext.define('FHD.view.icm.assess.bpm.AssessPlanBpmTwo',{
	extend:'Ext.panel.Panel',
	aligs:'widget.assessplanbpmtwo',
	
	autoScroll:false,
	border:false,
	//bodyPadding:'0 3 3 3',
	requires: [
       'FHD.ux.icm.common.FlowTaskBar'
    ],
	
	initComponent : function() {
		var me=this;
		
		//评价计划预览表单
		var basicInfo=Ext.create('FHD.view.icm.assess.form.AssessPlanPreviewForm',{
			columnWidth:1/1,
			bodyPadding:'0 3 3 3',
			isShowGroupPers:false,
			businessId:me.businessId
		});
		//评价计划可编辑列表
		me.assessPlanGrid=Ext.create('FHD.view.icm.assess.component.AssessPlanRelaProcessGrid',{
			margin:'7 10 0 30',
			columnWidth:1/1,
			checked:false,
			extraParams:{
				assessPlanId:me.businessId
			}
		});
		//fieldSet
		var childItems={
			xtype : 'fieldset',
			margin: '0 3 3 3',
			layout : {
				type : 'column'
			},
			collapsed : false,
			columnWidth:1/1,
			collapsible : true,
			title : '评价范围',
			items : [me.assessPlanGrid]
		};
		//审批意见
		me.ideaApproval=Ext.create('FHD.view.comm.bpm.ApprovalIdea',{
			columnWidth:1/1,
			executionId:me.executionId
		});
    	//fieldSet
		var fieldSet={
			xtype:'fieldset',
			margin:'0 3 3 3',
			title:'审批',
			collapsible : true,
			layout:{
				type:'column'
			},
			columnWidth:1/1,
			items:[me.ideaApproval]
		};
		
		me.bbar=[
		    '->',
		    {
				text:'提交',
				iconCls: 'icon-operator-submit',
				id:'assess_plan_bpm_two_submit_btn',
				handler: function () {
					//提交工作流
					me.submit(me.ideaApproval.isPass,me.ideaApproval.getValue());
	            }
			}/*,
			{
				text:'关闭',
				iconCls: 'icon-control-fastforward-blue',
				handler: function () {
					if(me.winId){
						Ext.getCmp(me.winId).close();
					}
	            }
			}
			*/
		];
		
		me.contentContainer = Ext.create('Ext.container.Container',{
			autoScroll:true,
			layout:'column',
			height:Ext.getBody().getHeight() * 0.8-50-30-30,//50是图片高度，30是window弹出穿口title和页面内容显示的bbar高度
        	items:[basicInfo,childItems,fieldSet]
		});
		
		Ext.applyIf(me, {
        	layout:{
        		align: 'stretch',
        		type: 'vbox'
        	},
        	items:[Ext.widget('flowtaskbar',{
        		jsonArray:[
		    		{index: 1, context:'1.计划制定',status:'done'},
		    		{index: 2, context:'2.计划审批',status:'current'},
		    		{index: 3, context:'3.任务分配',status:'undo'},
		    		{index: 4, context:'4.任务分配审批',status:'undo'},
		    		{index: 5, context:'5.计划发布',status:'undo'},
		    		{index: 6, context:'6.内控测试',status:'undo'},
		    		{index: 7, context:'7.测试结果复核',status:'undo'},
		    		{index: 8, context:'8.汇总整理',status:'undo'},
		    		{index: 9, context:'9.缺陷反馈',status:'undo'},
		    		{index: 10, context:'10.缺陷调整',status:'undo'},
		    		{index: 11, context:'11.缺陷确认',status:'undo'}
		    	]
	    	}),me.contentContainer]
		});
		
		me.callParent(arguments);
		
		me.load();
	},
	submit:function(isPass,examineApproveIdea){
		var me=this;
		
		//提交按钮不可用
		Ext.getCmp('assess_plan_bpm_two_submit_btn').setDisabled(true);
		
		FHD.ajax({//ajax调用
			url : __ctxPath+ '/icm/assess/assessPlanApproval.f',
		    params : {
		    	businessId:me.businessId,
		    	executionId:me.executionId,
		    	isPass:isPass,
		    	examineApproveIdea:examineApproveIdea
			},
			callback : function(data) {
				if(me.winId){
					Ext.getCmp(me.winId).close();
				}
			}
		});
	},
	operateColumn:function(){
		var me=this;
		
		if('ca_assessment_measure_0'== me.assessPlanMeasureId){
			//穿行测试--隐藏抽样测试列
			me.assessPlanGrid.down('[dataIndex=isPracticeTest]').show();
			me.assessPlanGrid.down('[dataIndex=practiceNum]').show();
			me.assessPlanGrid.down('[dataIndex=isSampleTest]').hide();
			me.assessPlanGrid.down('[dataIndex=coverageRate]').hide();
		}else if('ca_assessment_measure_1' == me.assessPlanMeasureId){
			//抽样测试--隐藏穿行测试列
			me.assessPlanGrid.down('[dataIndex=isPracticeTest]').hide();
			me.assessPlanGrid.down('[dataIndex=practiceNum]').hide();
			me.assessPlanGrid.down('[dataIndex=isSampleTest]').show();
			me.assessPlanGrid.down('[dataIndex=coverageRate]').show();
		}else if('ca_assessment_measure_2' == me.assessPlanMeasureId){
			//全部：穿行测试和抽样测试
			me.assessPlanGrid.down('[dataIndex=isPracticeTest]').show();
			me.assessPlanGrid.down('[dataIndex=practiceNum]').show();
			me.assessPlanGrid.down('[dataIndex=isSampleTest]').show();
			me.assessPlanGrid.down('[dataIndex=coverageRate]').show();
		}
	},
	load:function(){
		var me=this;

		FHD.ajax({
		     url : __ctxPath+ '/icm/assess/findAssessPlanForView.f',
		     params : {
		    	 assessPlanId:me.businessId,
			 },
			 callback : function(data) {
				 if (data) {
					 me.assessPlanMeasureId = data.data.assessMeasureId;
					 me.operateColumn();
				 }
			 }
		});
	},
	reloadData:function(){
		var me=this;
		
	}
});
/**
 *任务分配页面
 *assessPlanId：评价计划Id
 **/
Ext.define('FHD.view.icm.assess.bpm.AssessPlanBpmThree',{
	extend:'Ext.form.Panel',
	alias: 'widget.assessplanbpmthree',
	
	autoScroll:false,
	//bodyPadding:'0 3 3 3',
	border:false,
	requires: [
       'FHD.ux.icm.common.FlowTaskBar'
    ],
    
	initComponent : function() {
		var me=this;
		
		//评价计划预览表单
		var basicInfo=Ext.create('FHD.view.icm.assess.form.AssessPlanPreviewForm',{
			columnWidth:1/1,
			bodyPadding:'0 3 3 3',
			businessId:me.businessId
		});
		//任务分配列表
		me.assessPlanProcessRelaEmpEditGrid=Ext.create('FHD.view.icm.assess.component.AssessPlanProcessRelaEmpEditGrid',{
			margin:'7 10 0 30',
			columnWidth:1/1,
			businessId:me.businessId,
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
			items : [me.assessPlanProcessRelaEmpEditGrid]
		};
		//alert(me.executionId);
		me.approvalIdeaGrid = Ext.create('FHD.view.comm.bpm.ApprovalIdeaGrid',{
			executionId: me.executionId,
			title:'审批意见历史列表',
			margin:'7 10 0 30',
			columnWidth:1
		});
		//fieldSet
		var approvalIdeaFieldSet={
			xtype : 'fieldset',
			margin: '0 3 3 3',
			layout : {
				type : 'column'
			},
			collapsed : true,
			collapsible : true,
			columnWidth:1/1,
			collapsible : true,
			title : '审批意见列表',
			items : [me.approvalIdeaGrid]
		};
		
		me.save_btn = {
			xtype:'button',
			text: '保存',
			iconCls: 'icon-control-stop-blue',
			id:'assess_plan_bpm_three_save_btn',
			handler: function(){
				 me.save();
			}
		};
		me.submit_btn = {
			xtype:'button',
			text: '提交',
			iconCls: 'icon-operator-submit',
			id:'assess_plan_bpm_three_submit_btn',
			handler: function(){
				 me.submit();   
			}
		};
		me.close_btn = {
			xtype:'button',
			text: '关闭',
			iconCls: 'icon-control-stop-blue',
			id:'assess_plan_bpm_three_close_btn',
			handler: function(){
				if(me.winId){
					Ext.getCmp(me.winId).close();
				}
			}
		};
		
		me.contentContainer = Ext.create('Ext.container.Container',{
			autoScroll:true,
			layout:'column',
			height:Ext.getBody().getHeight() * 0.8-50-30-30,//50是图片高度，30是window弹出穿口title和页面内容显示的bbar高度
        	items:[basicInfo,childItems,approvalIdeaFieldSet]
		});
		
		Ext.applyIf(me, {
			layout : {
				align:'stretch',
				type : 'vbox'
			},
			bbar : [
    			'->',me.save_btn,me.submit_btn
    		],
    		items:[Ext.widget('flowtaskbar',{
        		jsonArray:[
   		    		{index: 1, context:'1.计划制定',status:'done'},
   		    		{index: 2, context:'2.计划审批',status:'done'},
   		    		{index: 3, context:'3.任务分配',status:'current'},
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
		
	},
	save:function(){
		var me=this;
		
		me.assessPlanProcessRelaEmpEditGrid.save();
	},
	submit:function(){
		var me=this;
		
		
    	//验证每个流程必须设置评价人和复核人
		var isvalidation = true;
		me.assessPlanProcessRelaEmpEditGrid.getStore().each(function(record) {
			//评价人handlerId和复核人reviewerId
		    if('' == record.get('handlerId') || '' == record.get('reviewerId')){
		    	isvalidation = false;
		    	return ;
		    }
		});
		if(!isvalidation){
			Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'评价人或复核人为空，请选择!');
			return false;
		}
		
		//验证每个流程的评价人和复核人不能相同
		var validateFlag = false;
		var count = me.assessPlanProcessRelaEmpEditGrid.store.getCount();
		for(var i=0;i<count;i++){
			var item = me.assessPlanProcessRelaEmpEditGrid.store.data.get(i);
 			if(item.get('handlerId')!='' && item.get('reviewerId')!='' && item.get('handlerId')==item.get('reviewerId')){
				validateFlag = true;
			}
		}
		if(validateFlag){
 			Ext.Msg.alert(FHD.locale.get('fhd.common.prompt'), '复核人不能和评价人相同!');
 			return false;
 		}
		
		//保存评价流程分配人
		var rows = me.assessPlanProcessRelaEmpEditGrid.store.getModifiedRecords();
		var jsonArray=[];
		Ext.each(rows,function(item){
			jsonArray.push(item.data);
		});
		FHD.ajax({
			url : __ctxPath + '/icm/assess/saveAssessorAndAssessResultBatch.f',
			params : {
				jsonString:Ext.encode(jsonArray),
				assessPlanId:me.businessId
			},
			callback : function(data){
				if(data){
					
					Ext.MessageBox.show({
			            title: '提示',
			            width: 260,
			            msg: '提交后将不能修改，您确定要提交么?',
			            buttons: Ext.MessageBox.YESNO,
			            icon: Ext.MessageBox.QUESTION,
			            fn: function (btn) {
			                if (btn == 'yes') {
			                	
            					//所有按钮不可用
            					Ext.getCmp('assess_plan_bpm_three_save_btn').setDisabled(true);
            					Ext.getCmp('assess_plan_bpm_three_submit_btn').setDisabled(true);
            					
            					//提交工作流
            					FHD.ajax({
            						url : __ctxPath+ '/icm/assess/assessPlanAllocation.f',
            					    params : {
            					    	businessId:me.businessId,
            					    	executionId:me.executionId
            						},
            						callback : function(data) {
            							if(me.winId){
            								Ext.getCmp(me.winId).close();
            							}
            						}
            					});
			                }
			            }
					});
				}
			}
		});
	},
	reloadData:function(){
		var me=this;
		
	}
});
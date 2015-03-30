Ext.define('FHD.view.icm.assess.bpm.AssessPlanBpmEleven', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.assessplanbpmeleven',
    
    autoScroll:false,
    //bodyPadding:'0 3 3 3',
    border:false,
    requires: [
       'FHD.ux.icm.common.FlowTaskBar'
    ],
    
	initComponent : function() {
	    var me = this;
	    
	    me.flowImage = Ext.widget('image',{
        	src : __ctxPath + '/images/icm/assess/steps66.jpg',
            width: 850
        });
	    
	    me.bbar=[
 		    '->',
 		    {
 				text:'提交',
 				iconCls: 'icon-operator-submit',
 				id:'assess_plan_bpm_eleven_submit_btn',
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
	    
		//评价计划预览表单
		var basicInfo=Ext.create('FHD.view.icm.assess.form.AssessPlanPreviewForm',{
			columnWidth:1/1,
			bodyPadding:'0 3 3 3',
			businessId:me.businessId
		});
		
		me.assessDefectGrid=Ext.create('FHD.view.icm.assess.component.AssessDefectFeedbackGrid',{
			margin: '7 10 0 30',
			defectsIsAvailable:false,
			feedbackIsAvailable:false,
			isOwnerOrg:'N',
			columnWidth:1/1,
			businessId:me.businessId
		});
		
		me.assessDefectGridPanel={
			xtype : 'fieldset',
			margin: '0 3 3 3',
			layout : {
				type : 'column'
			},
			collapsed : false,
			columnWidth:1/1,
			collapsible : true,
			title: '缺陷确认',
			items :[me.assessDefectGrid]
		};
		
		//审批意见组件
		me.ideaApproval=Ext.create('FHD.view.comm.bpm.ApprovalIdea',{
			columnWidth:1/1,
			executionId:me.executionId
		});
		//fieldSet审批意见
		var ideaSet={
			xtype : 'fieldset',
			margin: '0 3 3 3',
			layout : {
				type : 'column'
			},
			collapsed : false,
			columnWidth:1/1,
			collapsible : true,
			title : '审批',
			items : [me.ideaApproval]
		};
		
		me.contentContainer = Ext.create('Ext.container.Container',{
			autoScroll:true,
			layout:'column',
			height:Ext.getBody().getHeight() * 0.8-50-30-30,//50是图片高度，30是window弹出穿口title和页面内容显示的bbar高度
        	items:[basicInfo,me.assessDefectGridPanel,ideaSet]
		});
		
		me.items=[Ext.widget('flowtaskbar',{
    		jsonArray:[
	    		{index: 1, context:'1.计划制定',status:'done'},
	    		{index: 2, context:'2.计划审批',status:'done'},
	    		{index: 3, context:'3.任务分配',status:'done'},
	    		{index: 4, context:'4.任务分配审批',status:'done'},
	    		{index: 5, context:'5.计划发布',status:'done'},
	    		{index: 6, context:'6.内控测试',status:'done'},
	    		{index: 7, context:'7.测试结果复核',status:'done'},
	    		{index: 8, context:'8.汇总整理',status:'done'},
	    		{index: 9, context:'9.缺陷反馈',status:'done'},
	    		{index: 10, context:'10.缺陷调整',status:'done'},
	    		{index: 11, context:'11.缺陷确认',status:'current'}
	    	]
    	}),me.contentContainer];
		
		me.callParent(arguments);
	},
	submit:function(isPass,examineApproveIdea){
		var me=this;

		//提交按钮不可用
		Ext.getCmp('assess_plan_bpm_eleven_submit_btn').setDisabled(true);
		
		FHD.ajax({
			url : __ctxPath+ '/icm/assess/assessDefectRecognition.f',
			params : {
		    	businessId:me.businessId,
		    	executionId:me.executionId,
		    	isPass:isPass,
		    	examineApproveIdea:examineApproveIdea
			},
			callback : function(data) {
				if(data==true){
					if(me.winId){
						Ext.getCmp(me.winId).close();
					}
				}else{
					Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),data);
				}
			}
		});
	},
	reloadData : function(){
		var me=this;
		
	}
});
Ext.define('FHD.view.icm.assess.bpm.AssessPlanBpmTen', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.assessplanbpmten',
    
    autoScroll:false,
    //bodyPadding:'0 3 3 3',
    border:false,
    requires: [
       'FHD.ux.icm.common.FlowTaskBar'
    ],
    
	initComponent : function() {
	    var me = this;
	    
	    me.bbar=[
 		    '->',
 		    {
 				text: '保存',
 				id:'assess_plan_bpm_ten_save_btn',
 				iconCls: 'icon-control-stop-blue',
 				handler: function(){
 					 me.save();
 				}
 			},
 		    {
 				text:'提交',
 				id:'assess_plan_bpm_ten_submit_btn',
 				iconCls: 'icon-operator-submit',
 				handler: function () {
 					//提交工作流
 					me.submit();
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
		
		me.assessDefectGrid=Ext.create('FHD.view.icm.assess.component.AssessDefectGrid',{
			margin: '7 10 0 30',
			feedbackIsAvailable:true,
			businessId:me.businessId
		});
		
		me.assessDefectGridPanel={
			xtype : 'fieldset',
			margin: '0 3 3 3',
			layout : {
				type : 'fit'
			},
			collapsed : false,
			columnWidth:1/1,
			collapsible : true,
			title: '缺陷调整',
			items :[me.assessDefectGrid]
		};
		
		//审批意见组件
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
		
		me.contentContainer = Ext.create('Ext.container.Container',{
			autoScroll:true,
			layout:'column',
			height:Ext.getBody().getHeight() * 0.8-50-30-30,//50是图片高度，30是window弹出穿口title和页面内容显示的bbar高度
        	items:[basicInfo,me.assessDefectGridPanel,approvalIdeaFieldSet]
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
	    		{index: 10, context:'10.缺陷调整',status:'current'},
	    		{index: 11, context:'11.缺陷确认',status:'undo'}
	    	]
    	}),me.contentContainer];
		
		me.callParent(arguments);
	},
	save:function(){
		var me=this;
		
		var rows = me.assessDefectGrid.store.getModifiedRecords();
		var jsonArray=[];
		Ext.each(rows,function(item){
			jsonArray.push(item.data);
		});
		FHD.ajax({
			url : __ctxPath + '/icm/assess/mergeDefectBatch.f',
			params : {
				jsonString:Ext.encode(jsonArray)
			},
			callback : function(data){
				if(data){
					Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
				}
			}
		});
		me.assessDefectGrid.store.commitChanges(); 
	},
	submit:function(){
		var me=this;
		
		//验证每条缺陷必须选择整改责任部门、缺陷描述、缺陷级别、缺陷类型
		var validateFlag=false;
 		var message = '';
 		
 		var count = me.assessDefectGrid.store.getCount();
		for(var i=0;i<count;i++){
			var item = me.assessDefectGrid.store.data.get(i);
			if(item.get('orgId')=='' || item.get('orgId')==null || item.get('orgId')==undefined){
				message += "'整改责任部门'字段不能为空!<br/>";
				validateFlag=true;
			}
 			if(item.get('desc')=='' || item.get('desc')==null || item.get('desc')==undefined){
 				message += "'缺陷描述'字段不能为空!<br/>";
 				validateFlag=true;
 			}
			if(item.get('type')=='' || item.get('type')==null || item.get('type')==undefined){
				message += "'缺陷类型'字段不能为空!<br/>";
				validateFlag=true;
			}
 			if(item.get('level')=='' || item.get('level')==null || item.get('level')==undefined){
 				message += "'缺陷级别'字段不能为空!<br/>";
 				validateFlag=true;
 			}
 			
 			if(''!=message){
 				break;
 			}
 		}
 		
 		if(validateFlag){
 			Ext.Msg.alert(FHD.locale.get('fhd.common.prompt'), message);
 			return false;
 		}
		
		var rows = me.assessDefectGrid.store.getModifiedRecords();
		var jsonArray=[];
		Ext.each(rows,function(item){
			jsonArray.push(item.data);
		});
		FHD.ajax({
			url : __ctxPath + '/icm/assess/mergeDefectBatch.f',
			params : {
				jsonString:Ext.encode(jsonArray)
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
								Ext.getCmp('assess_plan_bpm_ten_save_btn').setDisabled(true);
								Ext.getCmp('assess_plan_bpm_ten_submit_btn').setDisabled(true);
								
								FHD.ajax({
									url : __ctxPath+ '/icm/assess/assessDefectRevise.f',
									params : {
										businessId:me.businessId,
										executionId:me.executionId
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
			                }
			            }
					});
				}
			}
		});
	},
	reloadData : function(){
		var me=this;
		
	}
});
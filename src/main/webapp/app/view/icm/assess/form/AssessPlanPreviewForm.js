Ext.define('FHD.view.icm.assess.form.AssessPlanPreviewForm',{
	extend : 'Ext.form.Panel',
	alias: 'widget.assessplanpreviewform',
	
	border:false,
	autoScroll:false,
	isShowGroupPers:true,
	layout : {
		type : 'column'
	},
	defaults : {
		columnWidth : 1 / 1
	},
	collapsed : false,
	
	initComponent : function() {
		var me = this;
		
		var idStroFile = {
			xtype : 'textfield',
			disabled : false,
			name : 'id',
			hidden : true
		};
		//所属公司
		var assessPlanCompany ={
			labelWidth : 80,
			xtype : 'displayfield',
			disabled : false,
			lblAlign : 'right',
			fieldLabel : '所属公司',
			value : '',
			name : 'companyName',
			margin : '7 10 0 30',
			maxLength : 200,
			columnWidth : .5
		};
		//编号
		var assessPlanCode = {
			labelWidth : 80,
			xtype : 'displayfield',
			lblAlign : 'right',
			fieldLabel : '计划编号',
			value : '',
			name : 'code',
			margin : '7 10 0 30',
			maxLength : 200,
			columnWidth : .5
		};
		//名称
		var assessPlanName = {
			xtype : 'displayfield',
			labelWidth : 80,
			fieldLabel : '计划名称',
			name : 'name',
			margin : '7 10 0 30',
			columnWidth : .6
		};
		//创建人
		var createEmpName = {
			xtype : 'displayfield',
			labelWidth : 80,
			fieldLabel : '创建人',
			name : 'createBy',
			margin : '7 10 0 30',
			columnWidth : .5
		};
		//创建时间
		var createTime = {
			xtype : 'displayfield',
			labelWidth : 80,
			fieldLabel : '创建时间',
			name : 'createTime',
			margin : '7 10 0 30',
			columnWidth : .5
		};
		//评价方式
		var assessPlanMeasure ={
			xtype : 'displayfield',
			labelWidth : 80,
			fieldLabel : '评价方式',
			name : 'assessMeasureName',
			margin : '7 10 0 30',
			columnWidth : .5
		};
		//评价类型
		var assessPlanType = {
			xtype : 'displayfield',
			labelWidth : 80,
			fieldLabel : '评价类型',
			name : 'type',
			margin : '7 10 0 30',
			columnWidth : .5
		};
		//评价方式
		var assessPlanScaleSetMeasure = {
			xtype : 'displayfield',
			labelWidth : 80,
			fieldLabel : '评价方式',
			name : 'scopeReq',
			margin : '7 10 0 30',
			columnWidth : .5
		};
		//评价期间
		var assessPlanDateAmongStart = {
			xtype: 'displayfield',
			name: 'targetStartDate',
			columnWidth : .3
		};
		var assessPlanDateAmongEnd = {
			xtype: 'displayfield',
			name: 'targetEndDate',
			columnWidth : .3
		};
		var labelPlanDisplay={
			xtype:'displayfield',
			width:82,
			value:'样本期间:'
		};
		var labelDisplay1={
			xtype:'displayfield',
			value:'~&nbsp;&nbsp;&nbsp;&nbsp;'
		};
	    var assessPlanTime=Ext.create('Ext.container.Container',{
     	    layout:{
     	    	type:'column'  
     	    },
     	    margin: '7 10 0 30',
     	    columnWidth : .5/*,
     	    items:[labelPlanDisplay,assessPlanDateAmongStart,labelDisplay1,assessPlanDateAmongEnd]*/
	    });
	    assessPlanTime.add(labelPlanDisplay);
	    if('' != assessPlanDateAmongStart && '' != assessPlanDateAmongEnd){
	    	assessPlanTime.add(assessPlanDateAmongStart);
	    	assessPlanTime.add(labelDisplay1);
	    	assessPlanTime.add(assessPlanDateAmongEnd);
	    }
		//计划评价时间
		var assessPlanTimeStart = {
			xtype: 'displayfield',
			name: 'planStartDate',
			columnWidth : .3
		};
		var assessPlanTimeEnd = {
			xtype: 'displayfield',
			name: 'planEndDate',
			columnWidth : .3
		};
		var labelPlanDisplay1={
			xtype:'displayfield',
			width:82,
			value:'计划起止日期:'
		};
		var assessPlanTime1=Ext.create('Ext.container.Container',{
     	    layout:{
     	    	type:'column'  
     	    },
     	    margin: '7 10 0 30',
     	    columnWidth : .5/*,
     	    items:[labelPlanDisplay1,assessPlanTimeStart,labelDisplay1,assessPlanTimeEnd]*/
	    });
		assessPlanTime1.add(labelPlanDisplay1);
	    if('' != assessPlanTimeStart && '' != assessPlanTimeEnd){
	    	assessPlanTime1.add(assessPlanTimeStart);
	    	assessPlanTime1.add(labelDisplay1);
	    	assessPlanTime1.add(assessPlanTimeEnd);
	    }
		//组长
		var assessPlanGroup = {
			labelWidth : 80,
			xtype : 'displayfield',
			lblAlign : 'right',
			fieldLabel : '组长',
			value : '',
			name : 'responsibilityEmp',
			margin : '7 10 0 30',
			maxLength : 200,
			columnWidth : .5
		};
		//组成员
		me.assessPlanGroupPers = {
			labelWidth : 80,
			xtype : 'displayfield',
			lblAlign : 'right',
			fieldLabel : '组成员',
			value : '',
			name : 'handlerEmp',
			margin : '7 10 0 30',
			maxLength : 200,
			columnWidth : .5
		};
		//评价目标
		var assessPlanTarget = {
			xtype : 'textareafield',
			labelAlign : 'left',
			margin: '7 10 0 30',
			name : 'assessTarget',
			readOnly:true,
			columnWidth : 1,
			labelWidth : 80,
			row : 5
		};
		//范围要求
		var assessPlanStandard = {
			xtype : 'textareafield',
			labelAlign : 'left',
			margin: '7 10 0 30',
			name : 'assessStandard',
			readOnly:true,
			columnWidth : 1,
			labelWidth : 80,
			row : 5
		};
		//评价依据
		var assessPlanRequirement= {
			xtype : 'textareafield',
			labelAlign : 'left',
			margin: '7 10 0 30',
			name : 'requirement',
			readOnly:true,
			columnWidth : 1,
			labelWidth : 80,
			row : 5
		};
		//考核要求
		var assessPlanDesception={
			xtype : 'textareafield',
			labelAlign : 'left',
			margin: '7 10 0 30',
			name : 'desc',
			readOnly:true,
			columnWidth : 1,
			labelWidth : 80,
			row : 5
		};
		var fieldSetInfo={//基本信息
			xtype : 'fieldset',
			//margin: '7 10 0 30',
			layout : {
				type : 'column'
			},
			columnWidth:1,
			collapsed : false,
			collapsible : false,
			title : '基本信息',
			items : [
			    assessPlanCompany,assessPlanCode,
			    assessPlanName,assessPlanType,
			    assessPlanMeasure,assessPlanTime,
			    assessPlanTime1,createEmpName,createTime
			]
		};
		
		var fieldSetChildTarger={//评价目标
			xtype : 'fieldset',
			margin: '7 10 0 30',
			layout : {
				type : 'column'
			},
			columnWidth:1,
			collapsed : false,
			collapsible : false,
			title : '评价目标',
			items : [assessPlanTarget]
		};
		var fieldSetChildRange={//范围要求
			xtype : 'fieldset',
			margin: '7 10 0 30',
			layout : {
				type : 'column'
			},
			columnWidth:1,
			title : '范围要求',
			items : [assessPlanStandard]
		};
		
		me.fieldSetChildMemberSelect = Ext.create('Ext.form.FieldSet',{//人员选择
			//xtype : 'fieldset',
			margin: '7 10 0 30',
			layout : {
				type : 'column'
			},
			columnWidth:1,
			title : '人员选择'/*,
			items : [assessPlanGroup,me.assessPlanGroupPers]*/
		});
		
		//组长
		me.fieldSetChildMemberSelect.add(assessPlanGroup);
		//组成员
		me.fieldSetChildMemberSelect.add(me.assessPlanGroupPers);
		
		var fieldSetChildAssessPlanRequirement={//评价依据
			xtype : 'fieldset',
			margin: '7 10 0 30',
			layout : {
				type : 'column'
			},
			columnWidth:1,
			title : '评价依据',
			items : [assessPlanRequirement]
		};
		
		var fieldSetChildAssessPlanDesception={//考核要求
			xtype : 'fieldset',
			margin: '7 10 0 30',
			layout : {
				type : 'column'
			},
			columnWidth:1,
			title : '考核要求',
			items : [assessPlanDesception]
		};
		
		var fieldSetXiangXiInfo={//详细信息
			xtype : 'fieldset',
			//margin: '7 10 0 30',
			layout : {
				type : 'column'
			},
			columnWidth:1,
			collapsed : true,
			collapsible : true,
			title : '详细信息',
			items : [
			    fieldSetChildTarger,fieldSetChildRange,
				me.fieldSetChildMemberSelect,fieldSetChildAssessPlanRequirement,
				fieldSetChildAssessPlanDesception
			]
		};
		
		me.items=[fieldSetInfo,fieldSetXiangXiInfo]
		me.callParent(arguments);
		
		me.loadData(me.businessId);
	},
	/*
	listeners:{
		afterrender:function(me){
			me.loadData();
		}
	},
	*/
	loadData:function(businessId){
		var me=this;

		me.businessId=businessId;
		me.getForm().load({
            url: __ctxPath+'/icm/assess/findAssessPlanForView.f?assessPlanId='+me.businessId,
            success: function (form, action) {
         	   return true;
            },
            failure: function (form, action) {
         	   return false;
            }
		});
		
		FHD.ajax({
		     url : __ctxPath+ '/icm/assess/findAssessPlanForView.f',
		     params : {
		    	 assessPlanId:me.businessId
			 },
			 callback : function(data) {
				 if (data) {
					me.assessPlanTypeId = data.data.typeId;
					//评价类型
					if('ca_assessment_etype_self' != me.assessPlanTypeId){
						//组成员显示
						me.down('[name=handlerEmp]').show();
					}else{
						if(!me.isShowGroupPers){
							me.down('[name=handlerEmp]').hide();
						}
					}
				 }
			 }
		});
	},
	reloadData:function(){
		var me=this;
		
	}
});

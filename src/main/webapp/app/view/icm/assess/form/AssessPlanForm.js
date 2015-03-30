/**
 * 评价计划第一步：编辑页面
 */
Ext.define('FHD.view.icm.assess.form.AssessPlanForm',{
	extend : 'Ext.form.Panel',
	alias: 'widget.assessplanform',
	
	autoScroll:true,
	border:false,
	bodyPadding:'0 3 3 3',
	
	initComponent : function() {
		var me = this;
		
		var idStroFile = {
			xtype : 'textfield',
			disabled : false,
			name : 'id',
			hidden : true
		};
		//所属公司
		var assessPlanCompany =Ext.create('FHD.ux.org.CompanySelectList',{
			fieldLabel : '所属公司'+ '<font color=red>*</font>',
			name : 'company',
			columnWidth : .5,
			labelWidth : 80,
			value:__user.companyId,
			multiSelect : false,
			margin : '7 10 0 30'
        }); 
		//编号
		var assessPlanCode = {
			labelWidth : 80,
			xtype : 'textfield',
			readOnly:true,
			disabled : false,
			lblAlign : 'rigth',
			fieldLabel : '计划编号'+ '<font color=red>*</font>',
			value : '',
			name : 'code',
			margin : '7 10 0 30',
			maxLength : 200,
			columnWidth : .5,
			allowBlank : false
		};
		//名称
		var assessPlanName = {
			xtype : 'textfield',
			labelWidth : 80,
			disabled : false,
			fieldLabel : '计划名称'+ '<font color=red>*</font>',
			name : 'name',
			margin : '7 10 0 30',
			columnWidth : .5,
			allowBlank : false
		};
		var spaceLabel =  {
			xtype : 'displayfield',
			columnWidth : .6
		};
		//评价方式
		me.assessPlanMeasure = Ext.create('FHD.ux.dict.DictSelectForEditGrid', {
			dictTypeId : 'ca_assessment_measure',
			margin : '7 10 0 30',
			labelWidth : 80,
			allowBlank : false,
			labelAlign : 'left',
			name : 'assessMeasure',
			multiSelect : false,
			columnWidth : .5,
			fieldLabel : '评价方式'+ '<font color=red>*</font>'
		});
		me.assessPlanMeasure.setValue('ca_assessment_measure_2');
		//评价类型
		me.assessPlanType = Ext.create('FHD.ux.dict.DictSelectForEditGrid', {
			dictTypeId : 'ca_assessment_etype',
			labelAlign : 'left',
			margin : '7 10 0 30',
			labelWidth : 80,
			value : "",
			name : 'type',
			columnWidth : .5,
			multiSelect : false,
			fieldLabel : '评价类型'+ '<font color=red>*</font>'
		});
		me.assessPlanType.setValue('ca_assessment_etype_self');
		
		/***选择范围 ***/
		//1.按部门选择
		var assessPlanDepart ={
		    xtype:'textfield',
			name : 'deptId',
			hidden:true
		};
		
		//2.按流程选择
		var assessPlanRelaProcess = {
		   xtype:'textfield',
		   name : 'processId',
		   hidden:true
		};
		
        me.assessPlanType.on('change',function(oldValue,newValue,ii){
			if(newValue=='ca_assessment_etype_self'){//自评
				me.assessPlanGroupPers.hide();
			}else if(newValue=='ca_assessment_etype_other'){//他评
				me.assessPlanGroupPers.show();
			}
		});
		//样本期间
		var assessPlanDateAmongStart = {
	        xtype: 'datefield',
	        name: 'targetStartDate',
	        columnWidth:.5,
	        format: 'Y-m-d'
		};
		var assessPlanDateAmongEnd = {
	        xtype: 'datefield',
	        name: 'targetEndDate',
	        columnWidth:.5,
	        format: 'Y-m-d'
		};
		var labelDisplay={
		    xtype:'displayfield',
		    width:82,
		    value:'样本期间&nbsp;&nbsp;:'
		};
		var labelDisplay1={
		    xtype:'displayfield',
		    value:'&nbsp;至'
		};
		var assessPlanDateAmongCont=Ext.create('Ext.container.Container',{
     	    layout:{
     	    	type:'column'  
     	    },
     	    margin: '7 10 0 30',
     	    columnWidth : .5,
     	    items:[labelDisplay,assessPlanDateAmongStart,labelDisplay1,assessPlanDateAmongEnd]
        });
		//计划评价时间
		var assessPlanTimeStart = {
			xtype: 'datefield',
		    name: 'planStartDate',
		    columnWidth:.5,
		    format: 'Y-m-d'
		};
		var assessPlanTimeEnd = {
			xtype: 'datefield',
		    name: 'planEndDate',
		    columnWidth:.5,
		    format: 'Y-m-d'
		};
		var labelPlanDisplay={
		    xtype:'displayfield',
		    width:82,
		    value:'计划期间&nbsp;&nbsp;:'
		};
		var assessPlanTime=Ext.create('Ext.container.Container',{
     	    layout:{
     	    	type:'column'  
     	    },
     	    margin: '7 10 0 30',
     	    columnWidth : .5,
     	    items:[labelPlanDisplay,assessPlanTimeStart,labelDisplay1,assessPlanTimeEnd]
		});
		//组长
		me.assessPlanGroup = Ext.create('FHD.ux.org.CommonSelector', {
			fieldLabel : '组&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;长'+ '<font color=red>*</font>',
			labelAlign : 'left',
			margin: '7 10 0 30',
			name : 'groupLeaderId',
			columnWidth : .5,
			value:'',
			//value:'[{"id":"'+__user.empId+'"}]',
			labelWidth : 80,
			type : 'emp',
			multiSelect : false
		});
		//组成员
		me.assessPlanGroupPers = Ext.create('FHD.ux.org.CommonSelector', {
			fieldLabel : '组成员'+ '<font color=red>*</font>',
			labelAlign : 'left',
			margin: '7 10 0 30',
			name : 'groupPersId',
			columnWidth : .5,
			labelWidth : 80,
			hidden:true,
			type : 'emp'
		});
		//评价目标
		var assessPlanTarget = {
			xtype : 'textareafield',
			labelAlign : 'left',
			margin: '7 10 0 30',
			name : 'assessTarget',
			//emptyText : '请输入评价目标',
			//submitEmptyText : false,
			columnWidth : 1,
			labelWidth : 80,
			row : 5
		};
		//基本要求
		var assessPlanStandard = {
			xtype : 'textareafield',
			labelAlign : 'left',
			margin: '7 10 0 30',
			row : 5,
			columnWidth : 1,
			//emptyText : '请输入基本要求',
			//submitEmptyText : false,
			name : 'assessStandard',
			labelWidth : 80
		};
		//评价依据
		var assessPlanRequirement = {
			xtype : 'textareafield',
			labelAlign : 'left',
			margin: '7 10 0 30',
			name : 'requirement',
			columnWidth : 1,
			//emptyText : '请输入评价依据',
			//submitEmptyText : false,
			labelWidth : 80,
			row : 5
		};
		//考核要求 
		var assessPlanDesception={
			xtype : 'textareafield',
			labelAlign : 'left',
			margin: '7 10 0 30',
			columnWidth : 1,
			name : 'desc',
			//emptyText : '请输入考核要求',
			//submitEmptyText : false,
			labelWidth : 80,
			row : 5
		};

		me.approvalIdeaGrid = Ext.create('FHD.view.comm.bpm.ApprovalIdeaGrid',{
			executionId: me.executionId,
			title:'审批意见历史列表',
			margin:'7 10 0 30',
			height:200,
			columnWidth:1
		});
		//fieldSet
		var approvalIdeaFieldSet={
			xtype : 'fieldset',
			//margin: '7 10 0 30',
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
		
		Ext.applyIf(me, {
			border:false,
			layout: {
				type : 'column'
			},
			defaults : {
				columnWidth : 1 / 1
			},
			collapsed : false,
        	items:[
       		    {
    				xtype : 'fieldset',
    				//margin: '7 10 0 30',
    				layout : {
    					type : 'column'
    				},
    				collapsed : false,
    				collapsible : false,
    				title : '基本信息',
    				items : [
    			         idStroFile,assessPlanCompany,assessPlanCode,assessPlanName,spaceLabel,
    			         me.assessPlanType,me.assessPlanMeasure,assessPlanDepart,
    			         assessPlanRelaProcess,assessPlanDateAmongCont,assessPlanTime
    				]
    			},
    			{
    				xtype : 'fieldset',
    				//margin: '7 10 0 30',
    				layout : {
    					type : 'column'
    				},
    				collapsed : false,
    				collapsible : false,
    				title: '评价目标',
    				items: [assessPlanTarget]
    			},
    			{
    				xtype : 'fieldset',
    				//margin: '7 10 0 30',
    				layout : {
    					type : 'column'
    				},
    				collapsed : false,
    				collapsible : false,
    				title: '范围要求',
    				items: [assessPlanStandard]
    			 },
    			 {
    				xtype : 'fieldset',
    				//margin: '7 10 0 30',
    				layout : {
    					type : 'column'
    				},
    				collapsed : false,
    				collapsible : false,
    				title : '人员选择',
    				items : [me.assessPlanGroup,me.assessPlanGroupPers]
    			 },
    			 {
    				xtype : 'fieldset',
    				//margin: '7 10 0 30',
    				layout : {
    					type : 'column'
    				},
    				collapsed : false,
    				collapsible : false,
    				title : '评价依据',
    				items : [assessPlanRequirement]
    			 },
    			 {
    				xtype : 'fieldset',
    				//margin: '7 10 0 30',
    				layout : {
    					type : 'column'
    				},
    				collapsed : false,
    				collapsible : false,
    				title : '考核要求',
    				items : [assessPlanDesception]
    			}							
    		]
		});
		
		me.callParent(arguments);
		
		if(me.executionId){
			me.add(approvalIdeaFieldSet);
		}
		
		me.loadData(me.businessId, me.editflag);
	},
	validateDate:function(date1,date2){//验证时间
	    var sd1=date1.split("-");
	    var sd2=date2.split("-");
	    var oldDate=new Date(sd1[0],sd1[1],sd1[2]);
	    var newDate=new Date(sd2[0],sd2[1],sd2[2]);
		if(oldDate<=newDate){
			return true;
		}else{
			return false;
		}
	 },
	 saveFunc:function(cardpanel){
		 var me=this;
		 if(!me.getForm().isDirty()){
			 //未修改任何数据，直接显示上一步
			 return true;
		 }else{
			 //已修改，保存返回成功后，再跳转
			 if(me.submitData(cardpanel)){
		    	 return true;
		     }else{
		    	 return false;
		     }
		 }
	 },
	 submitData:function(cardpanel){
		var me=this;

		//提交from表单
        var form = me.getForm();
        var vobj = form.getValues();
        if(!form.isValid()){
	        Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'存在未通过的验证!');
	        return ;
        }
        //alert("vobj.groupLeaderId="+vobj.groupLeaderId);
    	if(vobj.groupLeaderId == undefined || vobj.groupLeaderId =='' || vobj.groupLeaderId =='[]'){
    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'组长必选!');
    		return ;
    	}
        
        if(me.assessPlanType.getValue()=='ca_assessment_etype_other'){//他评
        	//alert('groupPers='+me.assessPlanGroupPers.getValue());
        	if(me.assessPlanGroupPers.getValue()==undefined || me.assessPlanGroupPers.getValue()=='' || me.assessPlanGroupPers.getValue()=='[]'){
        		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'组员必选!');
        		return ;
        	}
        }
        
        if(vobj.planStartDate!=''&&vobj.planEndDate!=''){
        	 if(!me.validateDate(vobj.planStartDate,vobj.planEndDate)){
             	Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'计划开始时间不能大于结束时间!');
             	return ;
             }
        }
        
        if(vobj.targetStartDate!=''&&vobj.targetEndDate!=''){
        	if(!me.validateDate(vobj.targetStartDate,vobj.targetEndDate)){
            	Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'评价开始时间不能大于结束时间!');
            	return ;
            }
        }
        
        //保存参数
		var assessplanmainpanel = me.up('assessplanmainpanel');
		if(assessplanmainpanel){
			//评价类型
			assessplanmainpanel.paramObj.assessMeasureId = me.assessPlanMeasure.getValue();
			//评价方式
			assessplanmainpanel.paramObj.assessTypeId = me.assessPlanType.getValue();
		}
        
        //验证form
		FHD.ajax({
	        url: __ctxPath + '/icm/assess/validateAssessPlanForm.f',
	        async:false,
	        params: {
		        assessPlanId:vobj.id,
		        name: vobj.name,
		        code: vobj.code
	        },
	        callback: function (data) {
	        	if (data.flagStr=='nameRepeat') {
	        		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.strategymap.strategymapmgr.prompt.nameRepeat'));
	        		return ;
	        	}
	        	if (data.flagStr=='codeRepeat') {
	        		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.strategymap.strategymapmgr.prompt.codeRepeat'));
	        		return ;
	        	}
	        	if (data.flagStr=='notRepeat') {
	        		//保存数据
			        FHD.submit({
				        form: form,
				        url: __ctxPath + '/icm/assess/mergeAssessPlanByForm.f',
				        callback: function (data) {
				        	if(data.success){
				        		//保存参数
				        		if(assessplanmainpanel){
				        			assessplanmainpanel.paramObj.businessId = data.assessPlanId;
				        			me.businessId = data.assessPlanId;
				        			me.editflag=true;
				        			me.loadData(data.assessPlanId, true);
				        		}
				        		if(cardpanel){
				        			cardpanel.businessId=data.assessPlanId;
				        			cardpanel.assessplanrangeform.loadData(data.assessPlanId,true);
				        		}
				        	}
				        }
			        });
	        	}
	        }
        });
		return true;
	},
	/*
	listeners:{
		afterrender:function(me){
			me.loadData();
		}
	},
	*/
	loadData:function(businessId,editflag){
		var me=this;
		//alert("one businessId="+businessId+"\t"+editflag);
		me.businessId=businessId;
		me.editflag=editflag;
		if(editflag){
			//修改：加载form数据
			me.getForm().load({
		    	 url: __ctxPath + '/icm/assess/findAssessPlan.f',
		    	 params: {
		    		 assessPlanId: me.businessId
		    	 },
		    	 success: function (form, action) {
		    		 return true;
		    	 },
		    	 failure: function (form, action) {
		    		 return false;
		    	 }
	        });
		}else{
			me.getForm().reset();
			
			//清除组长和组员控件值
			me.assessPlanGroup.clearValues();
			me.assessPlanGroupPers.clearValues();
			
			me.assessPlanMeasure.setValue('ca_assessment_measure_2');
			me.assessPlanType.setValue('ca_assessment_etype_self');
			//新增：创建编号
			FHD.ajax({
				url : __ctxPath+ '/icm/assess/createAssessPlanCode.f',
				callback : function(data) {
					me.getForm().setValues({'code': data.code});//给code表单赋值
				}
			});
		}
	},
	reloadData:function(){
		var me=this;
		
	}
 });
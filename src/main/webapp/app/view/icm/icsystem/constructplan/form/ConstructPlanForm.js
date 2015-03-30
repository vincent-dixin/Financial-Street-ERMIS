/**
 * 评价计划第一步：编辑页面
 */
Ext.define('FHD.view.icm.icsystem.constructplan.form.ConstructPlanForm',{
    extend : 'Ext.form.Panel',
    alias: 'widget.constructplanform',
    autoScroll:true,
    bodyPadding:'0 3 3 3',
    border:false,
    initParam : function(paramObj){
         var me = this;
         me.paramObj = paramObj;
    },
    initComponent : function() {
        var me = this;
        //编号
        var constructPlanId = { 
            xtype : 'textfield',
            value : '',
            name : 'id',
            hidden : true
        };
        var groupPersIdHid = {  
            xtype : 'textfield',
            value : '',
            name : 'groupPersIdHid',
            hidden : true
        };
        var groupLeaderIdHid = {    
            xtype : 'textfield',
            value : '',
            name : 'groupLeaderIdHid',
            hidden : true
        };
        var constructPlanCode = {   
            labelWidth : 80,
            xtype : 'textfield',
            readOnly:true,
            disabled : false,
            lblAlign : 'rigth',
            fieldLabel : '计划编号'+ '<font color=red>*</font>',
            value : '',
            name : 'code',
            maxLength : 200,
            allowBlank : false,
            hidden : true
        };
        //名称
        var constructPlanName = {
            xtype : 'textfield',
            disabled : false,
            fieldLabel : '计划名称'+ '<font color=red>*</font>',
            name : 'name',
            columnWidth: .5,
            allowBlank : false
        };
        //计划期间
		var constructPlanStart = {
	        xtype: 'datefield',
	        name: 'planStartDate',
	        columnWidth: .475,
	        format: 'Y-m-d'
		};
		var constructPlanEnd = {
	        xtype: 'datefield',
	        name: 'planEndDate',
	        columnWidth: .475,
	        format: 'Y-m-d'
		};
		var labelDisplay={
		    xtype:'displayfield',
		    width:82,
		    value:'计划期间&nbsp;&nbsp;:'
		};
		var labelDisplay1={
		    xtype:'displayfield',
		    value:'&nbsp;至',
		    columnWidth:.05
		};
		var constructPlanDateAmongCont=Ext.create('Ext.container.Container',{
     	    layout:{
     	    	type:'column'  
     	    },
     	    margin: '7 10 0 30',
     	    columnWidth : .5,
     	    items:[labelDisplay,constructPlanStart,labelDisplay1,constructPlanEnd]
        });
        //评价类型
        me.constructType = Ext.create('FHD.ux.dict.DictSelectForEditGrid', {
            dictTypeId : 'ic_reconstruct_plan_type',
            margin : '7 10 0 30',
            labelWidth : 80,
            allowBlank : false,
            labelAlign : 'left',
            name : 'type',
            multiSelect : false,
            columnWidth : .5,
            fieldLabel : '工作内容'+ '<font color=red>*</font>'
        });
        me.constructType.setValue('process');
        //组长
        me.constructPlanGroup = Ext.create('FHD.ux.org.CommonSelector', {
            fieldLabel : '组&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;长'+ '<font color=red>*</font>',
            labelAlign : 'left',
            labelWidth : 75,
            name : 'groupLeaderId',
            columnWidth : .5,
            allowBlank : false,
            value:'',
            type : 'emp',
            multiSelect : false
        });
        //组成员
        me.constructPlanGroupPers = Ext.create('FHD.ux.org.CommonSelector', {
            fieldLabel : '组&nbsp;成&nbsp;员' + '<font color=red>*</font>',
            labelAlign : 'left',
            labelWidth : 75,
            name : 'groupPersId',
            columnWidth : .5,
            allowBlank : false,
            hidden:false,
            type : 'emp'
        });
        //工作目标
        var workTarget = {
            xtype : 'textareafield',
            labelAlign : 'left',
            name : 'workTarget',
            columnWidth : 1,
            labelWidth : 80,
            margin: '7 10 0 30',
            row : 5
        };
        //基本要求
        var constructPlanStandard = {
            xtype : 'textareafield',
            labelAlign : 'left',
            margin: '7 10 0 30',
            row : 5,
            columnWidth : 1,
            name : 'requirement',
            labelWidth : 80
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
                    defaults : {
                        margin: '7 10 0 30',
                        columnWidth : .5,
                        labelWidth : 80
                    },
                    collapsed : false,
                    collapsible : false,
                    title : '基本信息',
                    items : [
                         constructPlanId,groupPersIdHid,groupLeaderIdHid,constructPlanCode,constructPlanName,constructPlanDateAmongCont,me.constructType,me.constructPlanGroup,me.constructPlanGroupPers
                    ]
                },
                {xtype : 'fieldset',
                    //margin: '7 10 0 30',
                    layout : {
                        type : 'column'
                    },
                    collapsed : true,
                    collapsible : true,
                    title: '更多信息',
                    defaults : {
                		columnWidth : 1 / 1
            		},
                    items: [
                    {
	                    xtype : 'fieldset',
	                    //margin: '7 10 0 30',
	                    layout : {
	                        type : 'column'
	                    },
	                    
	                    collapsed : false,
	                    collapsible : false,
	                    title: '工作目标',
	                    items: [workTarget]
                	},
               		{
	                    xtype : 'fieldset',
	                    //margin: '7 10 0 30',
	                    layout : {
	                        type : 'column'
	                    },
	                    collapsed : false,
	                    collapsible : false,
	                    title: '工作要求',
	                    items: [constructPlanStandard]
                 	}]
                }
            ]
        });
        
        me.callParent(arguments);
        
        if(me.executionId){
            me.add(approvalIdeaFieldSet);
        }
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
         me.constructPlanGroup.renderBlankColor(me.constructPlanGroup);
         me.constructPlanGroupPers.renderBlankColor(me.constructPlanGroupPers);
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
        	Ext.Msg.alert(FHD.locale.get('fhd.common.prompt'), '存在未通过的验证!');
            return ;
        }
        if(vobj.planStartDate!=''&&vobj.planEndDate!=''){
             if(!me.validateDate(vobj.planStartDate,vobj.planEndDate)){
                Ext.Msg.alert(FHD.locale.get('fhd.common.prompt'), '计划开始时间不能大于结束时间!');
                return ;
             }
        }
        //保存数据
        FHD.submit({
            form: form,
            url: __ctxPath + '/icm/icsystem/mergeconstructplanbyform.f',
            callback: function (data) {
                if(data.success){
                    //保存参数
                	me.editFlag = true;
                    if(cardpanel){
                        form.setValues({
                            'groupLeaderIdHid' : data.groupLeaderIdHid,
                            'groupPersId' : data.groupPersIdHid
                        });//给code表单赋值
                        if(me.executionId){
                        	me.initParam({
                        		businessId : data.constructPlanId,
                        		editFlag : true
                        	});
                        	cardpanel.constructplanrangeform.initParam({
                        		businessId : data.constructPlanId,
                        		editFlag : true
                        	});
                        }else{
                        	me.paramObj.editFlag = true;
                            me.paramObj.businessId = data.constructPlanId;
                        	me.reloadData();
                        	me.paramObj.businessId = data.constructPlanId;
	                        cardpanel.constructplanrangeform.initParam(me.paramObj);
                        }
                        cardpanel.constructplanrangeform.reloadData();
                    }
                }
            }
        });
        return true;
    },
    
    reloadData:function(){
        var me=this;
        if(me.executionId){
        	me.load({
                 url: __ctxPath + '/icm/icsystem/findconstructplan.f',
                 params: {
                     constructPlanId: me.businessId
                 },
                 success: function (form, action) {
                     return true;
                 },
                 failure: function (form, action) {
                     return false;
                 }
            });
        }
        else if(me.paramObj.editFlag){
            //修改：加载form数据
            me.load({
                 url: __ctxPath + '/icm/icsystem/findconstructplan.f',
                 params: {
                     constructPlanId: me.paramObj.businessId
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
            me.constructType.setValue('process');
            //新增：创建编号
            FHD.ajax({
                url : __ctxPath+ '/icm/icsystem/createconstructplancode.f',
                callback : function(data) {
                    me.getForm().setValues({'code': data.code});//给code表单赋值
                }
            });
        }
    }
 });
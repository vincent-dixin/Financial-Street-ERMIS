/**
 * 评价计划第一步：编辑页面
 */
Ext.define('FHD.view.riskinput.workflow.SchemeEndStateForm',{
    extend : 'Ext.form.Panel',
    alias: 'widget.schemeendstateform',
    requires: [
    	'FHD.view.riskinput.form.KpiFieldArr'
    ],
    autoScroll:true,
    layout: {
                type : 'column'
            },
    defaults : {
        columnWidth : 1 / 1
    },
    bodyPadding:'0 60 3 60',
    border:false,
    initParam : function(paramObj){
         var me = this;
         me.paramObj = paramObj;
    },
    initComponent : function() {
        var me = this;

        me.bbar=['->',{
        		iconCls : 'icon-control-play-blue',
        		text:'保存',
        		handler: function(){
//               	var riskeventeditcardpanel = me.up('riskeventeditcardpanel');
//		    		riskeventeditcardpanel.reloadData();
//    				riskeventeditcardpanel.navBtnHandler(0);
//    				me.close();
        			var riskeventeditcardpanel = me.up('riskeventeditcardpanel');
        			riskeventeditcardpanel.reloadData();
        			var schemeform = Ext.widget('schemeform');
        			riskeventeditcardpanel.setActiveItem(schemeform);
        			
                }
        	},'-',{
        		iconCls : 'icon-control-fastforward-blue',
        		text:'提交',
        		handler: me.onSubmit
        }]
        var finishtime = Ext.widget('datefield', {
            xtype: 'datefield',
            format: 'Y-m-d',
            name: 'startDateStr',
            margin: '7 10 10 20',
            fieldLabel: '实际完成时间', //开始日期
            width:400,
            allowBlank: false
        });
        var describe = {
            xtype : 'textareafield',
            fieldLabel:'工作开展情况描述',
            labelAlign : 'left',
        	margin: '7 10 10 20',
            row : 5,
            width: 1000,
            name : 'requirement',
            labelWidth : 100
        };
		 var realYields = Ext.create('Ext.form.RadioGroup', {
			fieldLabel: "实际收效评价",
	   		vertical: true,
	   		margin: '7 10 10 20',
	   		width: 400,
        	items: [
            { boxLabel: "很高", name: 'realYields', inputValue: '0',checked:true,id:'occurposs_0'},
            { boxLabel: "高", name: 'realYields', inputValue: '1',id:'occurposs_1'},
            { boxLabel: "一般", name: 'realYields', inputValue: '2',id:'occurposs_2'},
            { boxLabel: "低", name: 'realYields', inputValue: '3',id:'occurposs_3'},
            { boxLabel: "很低", name: 'realYields', inputValue: '4',id:'occurposs_4'}]
        });
        var addQuantification = Ext.widget('label',{
        	margin: '7 10 0 20',
			html : "<a href='javascript:void(0)' onclick='Ext.getCmp(\""+me.id+"\").addDl'>增加</a>"
        });
        var schemeFieldset = Ext.create('Ext.container.Container', {
           layout:{
     	    	type:'hbox'  
     	    },
           items : [realYields,addQuantification]
        });
        
        var kpifieldarr = Ext.widget('kpifieldarr');
        me.kpiFieldSet =  Ext.create('Ext.container.Container', {
           margin: '7 10 0 20',
           items : [kpifieldarr]
        });
		
        
        var kpiEtc = Ext.widget('textfield', {
            xtype: 'textfield',
            fieldLabel: '其它',
             margin: '7 10 10 120',
            value: '',
            maxLength: 255,
            width: 500
        });
        var realYieldsCon=Ext.create('Ext.container.Container',{
     	    layout:{
     	    	type:'vbox'  
     	    },
     	    width: 1000,
     	    items:[schemeFieldset,me.kpiFieldSet,kpiEtc]
        });
		
        var actualCost = Ext.create('Ext.form.RadioGroup', {
			fieldLabel: "实际成本",
	   		vertical: true,
	   		margin: '7 10 10 20',
	   		width: 400,
        	items: [
            { boxLabel: "很高", name: 'actualCost', inputValue: '0',checked:true,id:'actualCost_0'},
            { boxLabel: "高", name: 'actualCost', inputValue: '1',id:'actualCost_1'},
            { boxLabel: "一般", name: 'actualCost', inputValue: '2',id:'actualCost_2'},
            { boxLabel: "低", name: 'actualCost', inputValue: '3',id:'actualCost_3'},
            { boxLabel: "很低", name: 'actualCost', inputValue: '4',id:'actualCost_4'}]
        });
        
        
		var cost = Ext.widget('textfield', {
            xtype: 'textfield',
            fieldLabel: '直接经济成本',
            margin: '7 10 10 20',
            maxLength: 255,
            width: 500
        });
        var unit ={
        	 xtype:'displayfield',
        	 margin: '7 10 0 20',
    		 width:40,
    	     vertical: true,
    	     value:'万元'
        };
        
        var costCon=Ext.create('Ext.container.Container',{
        	margin: '20 10 0 0',
     	    layout:{
     	    	type:'column'  
     	    },
     	    width:1000,
     	    items:[cost,unit]
        });
        var costEtc = Ext.widget('textfield', {
            xtype: 'textfield',
            fieldLabel: '其它',
            margin: '7 10 10 20',
            value: '',
            maxLength: 255,
            width: 500
        });
        var costCon=Ext.create('Ext.container.Container',{
     	    layout:{
     	    	type:'vbox'  
     	    },
     	    width: 1000,
     	    items:[actualCost,costCon,costEtc]
        });

          var suggest = {
            xtype : 'textareafield',
            fieldLabel:'心得和建议',
            labelAlign : 'left',
            margin: '7 10 10 20',
            row : 5,
            width: 1000,
            name : 'requirement',
            labelWidth : 100
        };
        
       
        
       
        Ext.applyIf(me, {
            border:false,
            layout: {
                type : 'vbox'
            },
            collapsed : false,
            items:[
                {
                    xtype : 'fieldset',
                    collapsed : false,
                    collapsible : false,
                    title : '基本信息',
                    items : [
                         finishtime,describe,realYieldsCon,costCon,suggest]
                }
            ]
        },me.bbar);
        
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
//                        form.setValues({
//                            'groupLeaderIdHid' : data.groupLeaderIdHid,
//                            'groupPersId' : data.groupPersIdHid
//                        });//给code表单赋值
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
//            me.constructType.setValue('process');
            //新增：创建编号
            FHD.ajax({
                url : __ctxPath+ '/icm/icsystem/createconstructplancode.f',
                callback : function(data) {
//                    me.getForm().setValues({'code': data.code});//给code表单赋值
                }
            });
        }
    }
 });
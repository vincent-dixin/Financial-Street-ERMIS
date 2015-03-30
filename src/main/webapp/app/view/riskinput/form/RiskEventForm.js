/**
 * 评价计划第一步：编辑页面
 */
Ext.define('FHD.view.riskinput.form.RiskEventForm',{
    extend : 'Ext.form.Panel',
    alias: 'widget.riskeventform',
    requires: [
 	 	'FHD.view.riskinput.scheme.SchemeMainPanel'
    ],
    layout: {
                type : 'column'
            },
    defaults : {
        columnWidth : 1 / 1
    },
    bodyPadding:'0 60 3 60',
    autoScroll:true,
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
        			var schemeeditpanel=Ext.widget('schemeeditpanel');
        			riskeventeditcardpanel.setActiveItem(schemeeditpanel);
        			
                }
        	},'-',{
        		iconCls : 'icon-control-fastforward-blue',
        		text:'提交',
        		handler: me.onSubmit
        }]
        
        var describe = {
            xtype : 'textareafield',
            fieldLabel:'描述',
            labelAlign : 'left',
            margin: '7 10 10 20',
            row : 5,
            width:600,
            name : 'requirement',
            labelWidth : 100
        };
        var occurtime = Ext.create('Ext.form.RadioGroup', {
        		 fieldLabel: "风险发生时间",
//        		 columnWidth:.1,
        		 width:300,
        		 margin: '7 10 10 20',
        	     vertical: true,
        	     items: [
        	            { boxLabel: "一年内", name: 'lockstate', inputValue: '1',checked:true,id:'lockstateYes'},
        	            { boxLabel: "不清楚", name: 'lockstate', inputValue: '0',id:'lockstateNo'}]
        });
        

        
        
        var reportStandard = Ext.create('Ext.form.RadioGroup', {
         	 fieldLabel:'符合上报标准',
    		 columnWidth:1,
    		 margin: '7 10 0 20',
    		 layout:{
     	    	type:'vbox'  
     	     },
    	     vertical: true,
	         items: [
	            { boxLabel: "价格变动导致市场份额发生重大变化", name: 'reportStandard', inputValue: '0',checked:true, columnWidth:.5},
	            { boxLabel: "市场平均价格超过100元", name: 'reportStandard', inputValue: '1', columnWidth:.5}
	           ]
        });
        
        var reportStandardEtc = Ext.widget('textfield', {
            xtype: 'textfield',
            value: '',
            margin: '0 0 10 130',
            fieldLabel: "其它",
            maxLength: 255,
            width:500
        });
        var reportStandardCon=Ext.create('Ext.container.Container',{
        	margin: '20 10 0 0',
     	    layout:{
     	    	type:'vbox'  
     	    },
     	    width:800,
     	    items:[reportStandard,reportStandardEtc]
        });
        
        var occurposs = Ext.create('Ext.form.RadioGroup', {
		 fieldLabel: "发生可能性",
		 width:300,
		 margin: '7 10 10 20',
	     vertical: true,
         items: [
            { boxLabel: "高", name: 'occurposs', inputValue: '0',checked:true,id:'occurposs_high'},
            { boxLabel: "中", name: 'occurposs', inputValue: '1',id:'occurposs_mid'},
            { boxLabel: "低", name: 'occurposs', inputValue: '2',id:'occurposs_low'}]
        });
        var occurposs2 = Ext.widget('textfield', {
            xtype: 'textfield',
            value: '',
            margin: '0 0 10 130',
            fieldLabel: "概率",
            maxLength: 255,
            width:500
        });
        var occurpossCon=Ext.create('Ext.container.Container',{
        	margin: '20 10 0 0',
     	    layout:{
     	    	type:'column'  
     	    },
     	    width:600,
     	    items:[occurposs,occurposs2]
        });
        
        var labelDisplay2={
		    xtype:'displayfield',
		    margin: '0 60 10 20',
		    value:'影响程度:'
		};
		
        
        var property ={
        	 xtype:'displayfield',
    		 columnWidth:.1,
    	     vertical: true,
    	     value:'市场方面'
        };
        
        var occurPossSel = Ext.create('Ext.form.RadioGroup',{
	        vertical: true,
	        layout:'column',
	        columnWidth:.3,
	        items: [
	            { boxLabel: "<span data-qtip='竞争对手增加'>高</span>", name: 'occurPossSel', inputValue: '1', columnWidth:.3},
	            { boxLabel: "<span data-qtip='价格升高'>中</span>", name: 'occurPossSel', inputValue: '2', columnWidth:.3},
	            { boxLabel: "<span data-qtip='供货量不足'>低</span>", name: 'occurPossSel', inputValue: '3', columnWidth:.3}
	        ]
    	});
        var propertyCon=Ext.create('Ext.container.Container',{
        	layout:{
     	    	type:'column'  
     	    },
     	    columnWidth : .5,
     	    items:[property,occurPossSel]
        });
        
        var property2 = {
    		 xtype:'displayfield',
    		 columnWidth:.1,
    	     vertical: true,
    	     value:'收入'
        };
        var occurPossSel2 =Ext.create('Ext.form.RadioGroup',{
	        vertical: true,
	        layout:'column',
	        columnWidth : .3,
	        items: [
	            { boxLabel: "<span data-qtip='竞争对手增加'>高</span>", name: 'everyWeeksCheck', inputValue: '1', columnWidth:.3},
	            { boxLabel: "<span data-qtip='价格升高'>中</span>", name: 'everyWeeksCheck', inputValue: '2', columnWidth:.3},
	            { boxLabel: "<span data-qtip='供货量不足'>低</span>", name: 'everyWeeksCheck', inputValue: '3', columnWidth:.3}
	        ]
    	});
        var propertyCon2=Ext.create('Ext.container.Container',{
        	margin: '20 10 0 0',
     	    layout:{
     	    	type:'column'  
     	    },
     	    columnWidth : .7,
     	    items:[property2,occurPossSel2]
        });
        
//      --------------------------定量
        
        var percentage = {
    		 xtype:'displayfield',
    		 columnWidth:.1,
    	     vertical: true,
    	     value:'市场占有率'
        };
        var percentageData = Ext.widget('textfield', {
            xtype: 'textfield',
            value: '',
            margin: '0 20 10 20',
            maxLength: 255,
            columnWidth: .2
        });
        var occurPossSel = Ext.create('Ext.container.Container',{
	        vertical: true,
	        layout:'column',
	        columnWidth : .7,
	        items: [
	            { xtype:'displayfield',value:'红&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;70%~80%', columnWidth:.3},
	            { xtype:'displayfield',value:'黄&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;40%~70%', columnWidth:.3},
	            { xtype:'displayfield',value:'绿&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;0~40%', columnWidth:.3}
	        ]
    	});
        var percentageCon=Ext.create('Ext.container.Container',{
     	    layout:{
     	    	type:'column'  
     	    },
     	    margin: '20 10 0 0',
     	    columnWidth : 1,
     	    items:[percentage,percentageData]
        });
        
        
        
        
        var effectDegreeArr=Ext.create('Ext.container.Container',{
     	    columnWidth : 1,
     	    items:[propertyCon,propertyCon2,percentageCon]
        });
        
         var effectDegreeCon=Ext.create('Ext.container.Container',{
        	margin: '20 10 0 0',
     	    layout:{
     	    	type:'column'  
     	    },
     	    columnWidth : 1,
     	    items:[labelDisplay2,effectDegreeArr]
        });
        
        var reply = Ext.widget('dictradio', {
            xtype: 'dictradio',
            labelWidth: 100,
            width:300,
            name: 'monitorStr',
            margin: '20 10 0 20',
            dictTypeId: '0yn',
            fieldLabel: '是否触发应对', //是否监控
            defaultValue: '0yn_y',
            labelAlign: 'left'
        });
        
    	var approve = {
            xtype : 'textareafield',
            fieldLabel:'领导部门审批',
            labelAlign : 'left',
            margin: '7 10 0 20',
            row : 5,
            width:600,
            name : 'requirement',
            labelWidth : 100
        };
        
       
        Ext.applyIf(me, {
            items:[
                {xtype : 'fieldset',
               	 collapsed : false,
               	 collapsible : false,
               	 title : '基本信息',
               	 items : [
                     describe,occurtime,reportStandardCon]
                },
                {xtype : 'fieldset',
                 collapsed : true,
                 collapsible : true,
                 title: '评估信息',
                 items: [occurpossCon,effectDegreeCon,reply]
                },
                {xtype : 'fieldset',
                 collapsed : true,
                 collapsible : true,
                 title: '工作流',
                 items: [approve]
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
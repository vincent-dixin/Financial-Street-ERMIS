/**
 * 评价计划第一步：编辑页面
 */
Ext.define('FHD.view.riskinput.workflow.PlanEndStateForm',{
    extend : 'Ext.form.Panel',
    alias: 'widget.planendstateform',
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
        
        me.implementationGrid = Ext.create('FHD.ux.GridPanel',{
    		height:150,
    		url: __ctxPath + '',
    		extraParams:{
    			assessplanId:''
    		},
    		checked:false,
    		pagable:false,
    		searchable:false,
    		cols:[{
    			header:'控制措施',dataIndex:'parentProcessName',flex:3,
    			renderer : function(value, metaData, record, colIndex, store, view) {
					metaData.tdAttr = 'data-qtip="'+value+'"'; 
		 			return value;
				}
    		},{
    			header:'责任人',dataIndex:'processName',flex:3,
    			renderer : function(value, metaData, record, colIndex, store, view) {
					metaData.tdAttr = 'data-qtip="'+value+'"'; 
		 			return value;
				}
    		},{
    			header:'工作内容',dataIndex:'parentProcessName',flex:3,
    			renderer : function(value, metaData, record, colIndex, store, view) {
					metaData.tdAttr = 'data-qtip="'+value+'"'; 
		 			return value;
				}
    		},{
    			header:'工作类别',dataIndex:'parentProcessName',flex:3,
    			renderer : function(value, metaData, record, colIndex, store, view) {
					metaData.tdAttr = 'data-qtip="'+value+'"'; 
		 			return value;
				}
    		},{
    			header:'计划完成时间',dataIndex:'parentProcessName',flex:3,
    			renderer : function(value, metaData, record, colIndex, store, view) {
					metaData.tdAttr = 'data-qtip="'+value+'"'; 
		 			return value;
				}
    		}]
    	});
        var finishtime = Ext.widget('datefield', {
            xtype: 'datefield',
            format: 'Y-m-d',
            name: 'startDateStr',
            margin: '7 10 10 20',
            fieldLabel: '结束时间' + '<font color=red>*</font>', //开始日期
            width:400,
            allowBlank: false
        });
        var describe = {
            xtype : 'textareafield',
            fieldLabel:'执行情况说明',
            labelAlign : 'left',
            margin: '7 10 10 20',
            row : 5,
            width: 1000,
            name : 'requirement',
            labelWidth : 100
        };
		var executionState= Ext.create('Ext.form.RadioGroup',{
			fieldLabel: '执行状态',
			labelWidth : 95,
//			width: 400,
			margin: '7 10 10 20',
			layout:'vbox',
			columnWidth : 1,
			name:'isDesirableAdjust',
			vertical: true,
	        items: [
	            { boxLabel: "已按照要求执行预案", name: 'effectDegree', inputValue: '0',checked:true},
	            { boxLabel: "由于XX原因与预案不符", name: 'effectDegree', inputValue: '1'},
	            { xtype: 'textareafield',row : 5, width: 900}]
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
                type : 'column'
            },
            defaults : {
                columnWidth : 1 / 1
            },
            collapsed : false,
            items:[
                {
                    xtype : 'fieldset',
                    defaults : {
                        margin: '7 10 0 20',
                        columnWidth : 1,
                        labelWidth : 80
                    },
                    collapsed : false,
                    collapsible : false,
                    title : '预案信息',
                    items : [
                         me.implementationGrid]
                },{
                    xtype : 'fieldset',
                    defaults : {
                        margin: '7 10 0 20',
                        columnWidth : 1,
                        labelWidth : 80
                    },
                    collapsed : false,
                    collapsible : false,
                    title : '评价信息',
                    items : [executionState,describe,finishtime,suggest]
                }
            ]
        },me.bbar);
        
        me.callParent(arguments);
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
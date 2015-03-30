<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
	Ext.define('FHD.icm.icsystem.constructPlanEdit', {
		extend : 'Ext.form.Panel',
		renderTo:'FHD.icsystem.constructPlan.constructPlanEditForm${param._dc}',
		items:[],
		layout : {
			type : 'column'
		},
		defaults : {
			columnWidth : 1/1
		},
		autoWidth:true,
		collapsed : false,
		height : FHD.getCenterPanelHeight(),
		bbar: {},
		compSelect:{},
		constructPlanCode:{},
		constructPlanName:{},
		standardSelect:{},
		workType:{},
		empSelect:{},
		empsSelect:{},
		planStartDate:{},
		constructPlanDesc:{},
		planEndDate:{},
		initComponent :function() {
		  var me = this;
		  
			/*评价期间  */
			me.planStartDate = {
			        xtype: 'datefield',
			        fieldLabel:'计划时间',
			        name: 'planStartDate',
			        margin: '7 10 10 30',
			        columnWidth : .3,
			        format: 'Y-m-d',
			        labelWidth:80,
			};
			me.planEndDate = {
			        xtype: 'datefield',
			        name: 'planEndDate',
			        columnWidth : .2,
			        margin: '7 10 10 30',
			        format: 'Y-m-d',
			};
		  me.constructPlanDesc={
                  xtype: "textarea",
                  fieldLabel: "说明",
                  id: "memo",
                  name:"comment",
                  labelSepartor: "：",
                  value:""
                  };
		  me.empsSelect=Ext.create('FHD.ux.org.CommonSelector',{
          	fieldLabel: '组成员',
            type:'emp',
            multiSelect:true
        });
		  me.empSelect=Ext.create('FHD.ux.org.CommonSelector',{
          	fieldLabel: '组长',
            type:'emp',
            multiSelect:false
        });
		  me.compSelect=Ext.create('FHD.ux.org.CompanySelectList',{
          	id:'company',
        	name:'company',
        	maxHeight:300,
            fieldLabel: '所属公司'
        });
		  me.constructPlanCode={
				  xtype:"textfield",
                  fieldLabel:"编号"+'<font color=red>*</font>',
                  allowBlank:false,
                  name:"code",
                  value:''
                  };
			me.constructPlanName={
                    xtype:"textfield",
                    fieldLabel:"名称"+'<font color=red>*</font>',
                    allowBlank:false,
                    blankText:"不能为空，请填写",
                    name:"name",
                   // value:'${param.name}'
                    };
			
			me.workType={
                xtype:"textfield",
                fieldLabel:"工作类型"+'<font color=red>*</font>',
                allowBlank:false,
                blankText:"不能为空，请填写",
                name:"workType",
                readOnly:true,
               	value:'合规诊断',
                };
			
			me.standardSelect=Ext.create('FHD.ux.standard.StandardSelector',{
				name : 'standardName',
				multiSelect : false,//单选，都带复选框，选谁就是谁
				mytype:0,
				labelWidth:100,
				labelText :'更新依据',
         }),
        
			
			me.items= [{
				xtype : 'fieldset',
				defaults : {
					columnWidth : 1/2,
					margin:'7 10 10 30'
				},//每行显示一列，可设置多列
				layout : {
					type : 'column'
				},
				collapsed : false,
				//margin: '8 10 0 10',
				collapsible : false,
				title : '基本信息',
				items:[ me.compSelect,
				        me.constructPlanCode,
				        me.constructPlanName,
				        me.standardSelect,
				        me.workType,
				        me.empSelect,
				        me.empsSelect,
				        me.constructPlanDesc,
				        me.constructPlanTime,
				        me.planStartDate,
				        me.planEndDate]
	            }];
			me.bbar={
		               style: 'background-image:url() !important;background-color:rgb(250,250,250);',
		               items: [{
		                   xtype: 'tbtext',
		               }, '->',{
		                   text:'保存',
		                   width:100,
		                   handler: function () {
		                	   FHD.submit({
		                		   form: me.getForm(),
				                   url: __ctxPath+'/icsystem/saveConstructPlan.f',
				                   callback: function (data){
				                	  	constructPlan_ManagerView.remove(constructPlan_ManagerView.gridPanel);
					                	constructPlan_ManagerView.gridPanel = Ext.create('icsystem_constructPlanManage_panelAndMenu');
					               		constructPlan_ManagerView.add(constructPlan_ManagerView.gridPanel);
					                  
				                   }
		                		  });
		                   }
		                   },{
		                   text:'提交',
		                   width:100,
		                   handler: function () {}
		                   }, {
		                   text:'返  回',
		                   width:100,
		                   handler: function () {
		                	constructPlan_ManagerView.remove(constructPlan_ManagerView.gridPanel);
		                	constructPlan_ManagerView.gridPanel = Ext.create('icsystem_constructPlanManage_panelAndMenu');
		               		constructPlan_ManagerView.add(constructPlan_ManagerView.gridPanel);
		                   }
		               }]
		           };
			Ext.applyIf(me,{
				items:me.items
			});
			me.callParent(arguments);
			}
		});

	var FHD_icm_icsystem_constructPlanEdit;
	Ext.onReady(function(){
		FHD_icm_icsystem_constructPlanEdit=Ext.create('FHD.icm.icsystem.constructPlanEdit');
		FHD_icm_icsystem_constructPlanEdit.getForm().load({
            waitMsg: FHD.locale.get('fhd.kpi.kpi.prompt.waiting'),
            url: __ctxPath + '/icsystem/findConstructPlanById.f',
            params: {
            	constructPlanId:'${param.id}',
            },
            success: function (form, action) {
                return true;
            },
            failure: function (form, action) {
            }
        });
	});
	
	
</script>
</head>
<body>
	<div id='FHD.icsystem.constructPlan.constructPlanEditForm${param._dc}'></div>
</body>
</html>
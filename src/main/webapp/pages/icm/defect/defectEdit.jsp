<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
	Ext.define('FHD.icm.defect.defectEdit', {
		extend : 'Ext.form.Panel',
		renderTo:'FHD.defect.defect.defectEditForm${param._dc}',
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
		defectCode:{},//编号
		compSelect:{},//所属公司
		defectDesc:{},//缺陷描述
		defectLevel:{},//缺陷等级
		defectType:{},//缺陷类型
		designDefect:{},//设计缺陷
		executeDefect:{},//执行缺陷
		defectProcess:{},//涉及流程
		defectAnalyze:{},//缺陷分析
		compensationControl:{},//补偿控制
		isEffective:{},//控制效果
		improveAdivce:{},//整改建议
		checkDate:{},//下次观察时间
		checkResult:{},//观察结果
		initComponent :function() {
		  var me = this;
		  me.defectCode = {
				  xtype:"textfield",
                  fieldLabel:"编号"+'<font color=red>*</font>',
                  allowBlank:false,
                  name:"code",
                  value:''
                  };
		  
		  me.compSelect=Ext.create('FHD.ux.org.CompanySelectList',{
	          	fieldLabel : '所属公司'+'<font color=red>*</font>',
	          	margin : '7 5 5 30',
	        	name : 'company',
	            multiSelect : false
			});
		  me.defectDesc = {
					xtype : 'textfield',
					fieldLabel : '缺陷描述' + '<font color=red>*</font>',
					allowBlank : false,
					//width : 400,
					margin : '7 5 5 30',
					name : 'desc'
		  };
		  
		me.defectLevel = Ext.create('FHD.ux.dict.DictSelect',
	    		{
			margin : '7 5 5 30',
	    	name:'level',dictTypeId:'ca_defect_level',labelAlign:'left',fieldLabel : '缺陷等级'+ '<font color=red>*</font>',multiSelect:false
	    });
		me.defectType = Ext.create('FHD.ux.dict.DictRadio',
				{
			 margin : '7 5 5 30',
				name:'type',dictTypeId:'ca_defect_type',lableWidth:100,labelAlign:'left',fieldLabel : '缺陷类型'+ '<font color=red>*</font>'
			});	
		
		me.designDefect = Ext.create('FHD.ux.dict.DictSelect',
	    		{
	    	margin : '7 5 5 30',
	    	name:'designDefect',dictTypeId:'ca_design_defect',labelAlign:'left',fieldLabel : '设计缺陷'+ '<font color=red>*</font>',multiSelect:false
	    });
		
		me.executeDefect = Ext.create('FHD.ux.dict.DictSelect',
	    		{
			margin : '7 5 5 30',
	    	name:'executeDefect',dictTypeId:'ca_execute_defect',labelAlign:'left',fieldLabel : '执行缺陷'+ '<font color=red>*</font>',multiSelect:false
	    });
		me.defectProcess = Ext.create('FHD.ux.process.processSelector',{                   
	        name:'processId',
	        single : false,	 
	        margin : '7 5 5 30',
	        height : 80,
	        labelText: $locale('fhd.process.processselector.labeltext'),
	        labelWidth: 100
		});
		me.defectAnalyze = {
				xtype : 'textareafield',
				margin : '7 5 5 30',
				fieldLabel : '缺陷分析',
			    //width : 400,
				rows : 3,
				name : 'defectAnalyze'
		};
		me.compensationControl = {
				xtype : 'textareafield',
				fieldLabel : '补偿控制',
				margin : '7 5 5 30',
				//width : 400,
				name : 'compensationControl'
		};
		me.isEffective = Ext.create('FHD.ux.dict.DictSelect',
	    		{
			margin : '7 5 5 30',
	    	name:'isEffective',dictTypeId:'0yn',labelAlign:'left',fieldLabel : '控制效果',multiSelect:false
	    });
		me.improveAdivce = {
				xtype : 'textareafield',
				fieldLabel : '整改建议',
				margin : '7 5 5 30',
				//width : 400,
				rows : 3,
				name : 'improveAdivce'
		};
		me.checkDate = {
				xtype: 'datefield',
		        fieldLabel:'下次观察',
		        name: 'checkDate',
		        margin : '7 5 5 30',
		        //columnWidth : .3,
		        format: 'Y-m-d',
		        labelWidth:100,
		};
		me.checkResult = {
				xtype : 'textareafield',
				fieldLabel : '观察结果',
				margin : '7 5 5 30',
				//width : 400,
				rows : 3,
				name : 'checkResult'
		};
		
			
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
				items:[ me.defectCode,
				        me.compSelect,
				        me.defectDesc,
				        me.defectLevel,
				        me.defectType,
				        me.designDefect,
				        me.executeDefect,
				        me.defectProcess,
				        me.defectAnalyze,
				        me.compensationControl,
				        me.isEffective,
				        me.improveAdivce,
				        me.checkDate,
				        me.checkResult
				      ]
	            }];
			me.bbar={
		               style: 'background-image:url() !important;background-color:rgb(250,250,250);',
		               items: [{
		                   xtype: 'tbtext',
		               }, '->',{
		                   text:'保存',
		                   width:100,
		                   handler: function () {
		                	    if(!me.getForm().isValid()){
                                   Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'存在未通过的验证!');
                                   return;
                                  }; 
		                	   FHD.submit({
		                		   form: me.getForm(),		   
				                   url: __ctxPath+'/icm/defect/saveDefect.f',
				                   callback: function (data){
				                	  	/* defect_ManagerView.remove(defect_ManagerView.gridPanel);
					                	defect_ManagerView.gridPanel = Ext.create('defect_defectManage_panelAndMenu');
					               		defect_ManagerView.add(defect_ManagerView.gridPanel); */
					                  
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
		                	defect_ManagerView.remove(defect_ManagerView.gridPanel);
		                	defect_ManagerView.gridPanel = Ext.create('defect_defectManage_panelAndMenu');
		               		defect_ManagerView.add(defect_ManagerView.gridPanel);
		                   }
		               }]
		           };
			Ext.applyIf(me,{
				items:me.items
			});
			me.callParent(arguments);
			}
		});

	var FHD_icm_defect_defectEdit;
	Ext.onReady(function(){
		FHD_icm_defect_defectEdit=Ext.create('FHD.icm.defect.defectEdit');
		if(${param.type=='yes'}){
			FHD_icm_defect_defectEdit.getForm().load({
				url:'icm/defect/findDefectForForm.f?defectId=${param.defectId}',
					success: function (form, action) {
	                    
	             	   return true;
	                },
	                failure: function (form, action) {
	             	  
	             	   return true;
	                }
				
			});
			
		}
		
	});
	
	
</script>
</head>
<body>
	<div id='FHD.defect.defect.defectEditForm${param._dc}'></div>
</body>
</html>
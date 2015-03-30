<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
	Ext.define('FHD.icm.defect.defectFollowEdit', {
		extend : 'Ext.form.Panel',
		renderTo:'FHD.defect.defect.defectFollowEditForm${param._dc}',
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
		id:{},
		defect:{},//缺陷描述
		improve:{},//整改计划
		improveResult:{},//整改结果
		improveTest:{},//整改测试
		testAnalyze:{},//测试分析
		initComponent :function() {
		  var me = this;
		  me.id = {
				  xtype:"textfield",
				  hidden:true,
				  name:"id"
		  }
		  me.defect = {
				//  xtype:"textfield",
				  xtype : 'displayfield',
                  fieldLabel:"缺陷描述",
                  //allowBlank:false,
                  name:"defect",
                  value:''
                  };
		  me.improve = {
				//  xtype:"textfield",
				  xtype : 'displayfield',
                  fieldLabel:"整改计划",
                  //allowBlank:false,
                  name:"improve",
                  value:''
                  };
		  me.improveResult = {
				  xtype:"textfield",
                  fieldLabel:"整改结果",
                 // allowBlank:false,
                  name:"improveResult",
                  value:''
                  };
		  me.improveTest = {
				  xtype:"textfield",
                  fieldLabel:"整改测试",
                  //allowBlank:false,
                  name:"improveTest",
                  value:''
                  };
		  me.testAnalyze = {
				  xtype:"textfield",
                  fieldLabel:"测试分析",
                  //allowBlank:false,
                  name:"testAnalyze",
                  value:''
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
				items:[
                        me.id,
				        me.defect,
				        me.improve,
				        me.improveResult,
				        me.improveTest,
				        me.testAnalyze
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
		                	   FHD.submit({
		                		   form: me.getForm(),
				                   url: __ctxPath+'/icm/defect/saveDefectFollow.f',
				                   callback: function (data){
				                	  	defectFollow_ManagerView.remove(defectFollow_ManagerView.gridPanel);
					                	defectFollow_ManagerView.gridPanel = Ext.create('defect_defectFollowManage_panelAndMenu');
					               		defectFollow_ManagerView.add(defectFollow_ManagerView.gridPanel);
					                  
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
		                	defectFollow_ManagerView.remove(defectFollow_ManagerView.gridPanel);
		                	defectFollow_ManagerView.gridPanel = Ext.create('defect_defectFollowManage_panelAndMenu');
		               		defectFollow_ManagerView.add(defectFollow_ManagerView.gridPanel);
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
		FHD_icm_defect_defectEdit=Ext.create('FHD.icm.defect.defectFollowEdit');
		FHD_icm_defect_defectEdit.getForm().load({
			url:'icm/defect/findDefectFollow.f?defectRelaImproveId=${param.defectRelaImproveId}&defectId=79fac09f-3ec4-40e0-9c83-197158a6d60a&improveId=b24c10ab-2e5d-470f-923f-03cc6b321430',
				success: function (form, action) {
                    
             	   return true;
                },
                failure: function (form, action) {
             	  
             	   return true;
                }
			
		});
	});
	
	
</script>
</head>
<body>
	<div id='FHD.defect.defect.defectFollowEditForm${param._dc}'></div>
</body>
</html>
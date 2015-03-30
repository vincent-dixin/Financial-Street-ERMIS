<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>评价计划编辑</title>
<script type="text/javascript">
	Ext.define('FHD_icm_assess_assessPlanShow',{
						extend : 'Ext.form.Panel',
						renderTo:'FHD.icm.assess.assessPlanEdit${param._dc}',
						height : FHD.getCenterPanelHeight(),
						autoScroll:true,
						items : [],
						bbar : {},
						layout : {
						type : 'column'
						},
						defaults : {
						columnWidth : 1 / 1
						},
						collapsed : false,
						assessPlanRelaProcessGrid:{},
						basicInfo:null,
						initComponent : function() {
							var me = this;
							me.basicInfo=Ext.create('pages.icm.assess.assessPlanBasicInformation');

							/*范围*/
							me.assessPlanRelaProcessGrid = Ext.create('FHD.ux.GridPanel',{//创建可编辑的grid列表，注释同不可编辑的gird
								columnWidth : 1,
								multiSelect:false,
								checked:false,
								url: 'icm/assess/findAssessPlanRelaProcessListByPage.f?assessPlanId=${param.assessPlanId}',
								cols:[{dataIndex:'id',width:0},
								      {header:'一级流程', dataIndex: 'firstProcessName', sortable: false,flex:1},
								      {header:'二级流程', dataIndex: 'parentProcessName',sortable: false,flex:1},
								      {header:'末级流程', dataIndex: 'processName', sortable: false,flex:1},
								      {header: '穿行测试',
								    	  dataIndex: 'isPracticeTest',
								    	  renderer: function(value,p,record){
								    		  if(record.data.isPracticeTest)
								    			  return '是';
								    		  else
								    			  return '否';
								    	  }
								      },
								      {header:'穿行次数', dataIndex: 'practiceNum', sortable: false},
								      {header: '抽样测试',
								    	  dataIndex: 'isSampleTest',
								    	  renderer: function(value,p,record){
								    		  if(record.data.isSampleTest)
								    			  return '是';
								    		  else
								    			  return '否';
								    	  }
								      },
								      {header:'抽样次数', dataIndex: 'sampleNum',hidden:true, sortable: false},
								      {header:'抽取样本比例(%)', dataIndex: 'coverageRate', sortable: false,renderer:function(value){
								    	  if(value!=''&&value!=null){
								 				var newValue=value*100;
								 	 			return newValue;
								 			}else{
								 				return value;
								 			}
								      }}],
							});
					
							me.items = [me.basicInfo,{
										xtype : 'fieldset',
										margin: '7 10 0 30',
										layout : {
											type : 'column'
										},
										collapsed : false,
										collapsible : true,
										title : '范围',
										items : [me.assessPlanRelaProcessGrid]
									}
							];
							 this.callParent(arguments);
	},
	upStep:function(){//上一步计划
		 var assessMeasureId='${param.assessMeasureId}';
	     var assessPlanId='${param.assessPlanId}';
	     fhd_icm_assess_assessPlanPanelManager.remove(fhd_icm_assess_assessPlanPanelManager.centerContainer);
	     fhd_icm_assess_assessPlanPanelManager.setCenterContainer('pages/icm/assess/assessPlanListEdit.jsp?assessMeasureId='+assessMeasureId+'&assessPlanId='+assessPlanId);
	     fhd_icm_assess_assessPlanPanelManager.add(fhd_icm_assess_assessPlanPanelManager.centerContainer);
	},
	subMit:function(){//提交方法
		
		FHD.ajax({//ajax调用
		     url : __ctxPath+ '/icm/assess/saveAssessPlanRelaPointBatch.f',
		     params : {
		    	 assessPlanId:'${param.assessPlanId}'
			 },
			 callback : function(data) {
			 if (data) {//提交信息成功
			 Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
			 fhd_icm_assess_assessPlanContainer.remove(fhd_icm_assess_assessPlanContainer.centerContainer);
			 fhd_icm_assess_assessPlanContainer.setCenterContainer('pages/icm/assess/assessPlanList.jsp');
			 fhd_icm_assess_assessPlanContainer.add(fhd_icm_assess_assessPlanContainer.centerContainer);
		   }
		   }
		});
	}
					});
	var fhd_icm_assess_assessPlanShow=null;
	
	Ext.onReady(function() {
		fhd_icm_assess_assessPlanShow=Ext.create('FHD_icm_assess_assessPlanShow');
		FHD.componentResize(fhd_icm_assess_assessPlanShow,0,0);
		
			     fhd_icm_assess_assessPlanShow.getForm().load({
	                                   url:__ctxPath + '/assess/assessPlan/findAssessPlanForView.f?assessPlanId=${param.assessPlanId}',
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
	<div id='FHD.icm.assess.assessPlanEdit${param._dc}'></div>
</body>
</html>
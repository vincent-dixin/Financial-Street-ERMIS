<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>内控评价列表</title>
<script type="text/javascript">
	/***attribute start***/
 Ext.define('FHD_icm_assess_assessPlanExecuteList',{
    	extend:'FHD.ux.GridPanel',
    	renderTo:'FHD.icm.assess.assessPlanExecuteList${param._dc}',
    	cols:[],
    	tbar:[],
    	tbarItems:[],
    	standardId:'',
    	idSeq:'',
    	upName:'',
    	initComponent:function(){
    	var me=this;
    	me.cols=[{header : '编号',dataIndex : 'code',sortable : true,flex : 1}, 
 				 {header : '名称',dataIndex : 'name',sortable : true,flex : 1}, 
 				 {header : '评价方式',dataIndex :'assessMeasure',sortable : true}, 
 				 {header : '评价类型',dataIndex : 'type',sortable : true}, 
 				 {header : '评价期间',dataIndex : 'targetDate',sortable : true},
 				 {header : '状态',dataIndex :'dealStatus',sortable : true,renderer:function(value){
 					 if(value=='H'){
 						 value='处理中';
 					 } else if(value=='P'){
 						 value='已提交';
 					 }
 					 return value;
 				 }},
 				 {header : '更新时间',dataIndex :'createTime',sortable : true},
 				 {header : '操作',width:170,dataIndex :'id',renderer:function(value){
 					 
 					return "<a onclick='adminIntoFunction()' herf='javascript:void(0);' style='cursor:pointer'> 管理员进入  </a>|<a onclick='executeFunction();' herf='javascript:void(0);' style='cursor:pointer'> 执行  </a>|<a onclick='defectFunction();' a herf='javascript:void(0);' style='cursor:pointer'> 缺陷  </a>";
 				 }}];
    	me.callParent(arguments);
    	}
    });
    
    var id=adminIntoFunctionId='';
	var adminIntoFunction =function(){
		fhd_icm_assess_assessPlanExecutePanelManager.remove(fhd_icm_assess_assessPlanExecutePanelManager.centerContainer);
		fhd_icm_assess_assessPlanExecutePanelManager.setCenterContainer('pages/icm/assess/assessorBasic.jsp?assessPlanId='+adminIntoFunctionId);
		fhd_icm_assess_assessPlanExecutePanelManager.add(fhd_icm_assess_assessPlanExecutePanelManager.centerContainer);
	};
	var defectFunction =function(){
		 FHD.ajax({//ajax调用
	 		    url : __ctxPath+ '/assess/assessPlan/findAssessPlanRelaOperatorIdByAssessPlanIdAndEmpId.f',
	 		    params : {
	 		    	      assessPlanId:adminIntoFunctionId,
	 		    	      empId:__user.empId
	 			         },
	 			callback : function(data) {
	 			              if (data) {
	 			            	 fhd_icm_assess_assessPlanExecutePanelManager.remove(fhd_icm_assess_assessPlanExecutePanelManager.centerContainer);
	 			            	 fhd_icm_assess_assessPlanExecutePanelManager.setCenterContainer('pages/icm/assess/analysisOfResults.jsp?flag=1&assessorId='+data.id+'&assessPlanId='+adminIntoFunctionId);
	 			            	 fhd_icm_assess_assessPlanExecutePanelManager.add(fhd_icm_assess_assessPlanExecutePanelManager.centerContainer);
	 		                      }
	 		                   }
	 		               });
	};
	var executeFunction =function(){
		//通过，dang
		 FHD.ajax({//ajax调用
 		    url : __ctxPath+ '/assess/assessPlan/findAssessPlanRelaOperatorIdByAssessPlanIdAndEmpId.f',
 		    params : {
 		    	      assessPlanId:adminIntoFunctionId,
 		    	      empId:__user.empId
 			         },
 			callback : function(data) {
 			              if (data) {
 			            	 fhd_icm_assess_assessPlanExecutePanelManager.remove(fhd_icm_assess_assessPlanExecutePanelManager.centerContainer);
 			            	 fhd_icm_assess_assessPlanExecutePanelManager.setCenterContainer('pages/icm/assess/assessorRelaProcessList.jsp?flag=1&assessorId='+data.id+'&assessPlanId='+adminIntoFunctionId);
 			            	 fhd_icm_assess_assessPlanExecutePanelManager.add(fhd_icm_assess_assessPlanExecutePanelManager.centerContainer);
 		                      }
 		                   }
 		               });
	};
    var fhd_icm_assess_assessPlanList=null;
	Ext.onReady(function() {
		
		fhd_icm_assess_assessPlanList=Ext.create('FHD_icm_assess_assessPlanExecuteList',{
			url:__ctxPath+ '/assess/assessList/findAssessPlansByPage.f?dealStatus=H',
		    height : FHD.getCenterPanelHeight()//高度为：获取center-panel的高度
		});
		fhd_icm_assess_assessPlanList.on('select',function(){
			var selectionDate=fhd_icm_assess_assessPlanList.getSelectionModel().getSelection();
			if(null!=selectionDate[0].get('id')){
				adminIntoFunctionId=selectionDate[0].get('id');	
			}
		});
		FHD.componentResize(fhd_icm_assess_assessPlanList,0,0);
});
	/***Ext.onReady end***/
</script>
</head>
<body>
	<div id='FHD.icm.assess.assessPlanExecuteList${param._dc}'></div>
</body>
</html>
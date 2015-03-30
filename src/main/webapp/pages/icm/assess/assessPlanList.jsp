<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>内控评价列表</title>
<script type="text/javascript">
	/***attribute start***/
 Ext.define('FHD_icm_assess_assessPlanList',{
    	extend:'FHD.ux.GridPanel',
    	renderTo:'FHD.icm.assess.assessList${param._dc}',
    	cols:[],
    	tbar:[],
    	tbarItems:[],
    	standardId:'',
    	idSeq:'',
    	upName:'',
    	initComponent:function(){
    	var me=this;
    	me.tbarItems=[{iconCls : 'icon-add',id : 'assessPlanAdd${param._dc}',handler :me.addAssessPlan,scope : this},'-', 
    	    	      {iconCls : 'icon-del',id : 'assessPlanDel${param._dc}',handler :me.delAssessPlan,scope : this}];
    	me.cols=[{header : '编号',dataIndex : 'code',sortable : true,flex : 1}, 
 				 {header : '名称',dataIndex : 'name',sortable : true,flex : 1}, 
 				 {header : '评价方式',dataIndex :'assessMeasure',sortable : true,}, 
 				 {header : '评价类型',dataIndex : 'type',sortable : true}, 
 				 {header : '评价期间',dataIndex : 'targetDate',sortable : true},
 				{header : '状态',dataIndex :'dealStatus',sortable : true,renderer:function(value){
					 if(value=='S'){
						 value='已保存';
					 }else if(value=='P'){
						 value='已提交';
					 }else if(value=='H'){
						 value='处理中';
					 }
					 return value;
					 
				 }},
 				 {header : '更新时间',dataIndex :'createTime',sortable : true,},
 				{
  		            xtype:'actioncolumn',
  		            dataIndex : 'id',
  		            text:'操作',
  		            items: [{
  		                icon:__ctxPath+'/images/icons/edit.gif',
  		                tooltip: '编辑',
  		                handler: function(grid, rowIndex, colIndex) {
  		                	var row=grid.store.getAt(rowIndex);
  		                	var getEditFlag=row.get('dealStatus');
  		                	if(getEditFlag=='S'){
  		                		var row=grid.store.getAt(rowIndex)
  	 				    		var assessPlanId=row.get('id');
  	 				    		fhd_icm_assess_assessPlanContainer.remove(fhd_icm_assess_assessPlanContainer.centerContainer);
  	 				    		fhd_icm_assess_assessPlanContainer.setCenterContainer('pages/icm/assess/assessPlanPanelManager.jsp?initForm=1&assessPlanId='+assessPlanId);
  	 				    		fhd_icm_assess_assessPlanContainer.add(fhd_icm_assess_assessPlanContainer.centerContainer);
  		                	}
  		                	//选择编辑评价计划，load from表单
  		                }
  		            },
  		          {
  		                icon:__ctxPath+'/images/icons/add.gif',
  		                tooltip: '任务分配',
  		                handler: function(grid, rowIndex, colIndex) {
  		                	//选择编辑评价计划，load from表单
 				    		var row=grid.store.getAt(rowIndex)
 				    		var assessPlanId=row.get('id');
 				    		var url=__ctxPath + '/assess/assessPlan/findAssessPlan.f?assessPlanId='+assessPlanId;
 				    		fhd_icm_assess_assessPlanContainer.remove(fhd_icm_assess_assessPlanContainer.centerContainer);
 				    		fhd_icm_assess_assessPlanContainer.setCenterContainer('pages/icm/assess/assessPlanAllocation.jsp?assessPlanId='+assessPlanId);
 				    		fhd_icm_assess_assessPlanContainer.add(fhd_icm_assess_assessPlanContainer.centerContainer);
  		                }
  		            },
  		          {
  		                icon:__ctxPath+'/images/icons/add.gif',
  		                tooltip: '缺陷',
  		                handler: function(grid, rowIndex, colIndex) {
  		                	//选择编辑评价计划，load from表单
 				    		var row=grid.store.getAt(rowIndex)
 				    		var assessPlanId=row.get('id');
 				    		var url=__ctxPath + '/assess/assessPlan/findAssessPlan.f?assessPlanId='+assessPlanId;
 				    		fhd_icm_assess_assessPlanContainer.remove(fhd_icm_assess_assessPlanContainer.centerContainer);
 				    		fhd_icm_assess_assessPlanContainer.setCenterContainer('pages/icm/defect/defectManage.jsp?assessPlanId='+assessPlanId);
 				    		fhd_icm_assess_assessPlanContainer.add(fhd_icm_assess_assessPlanContainer.centerContainer);
  		                }
  		            }
  		            ]
  				 }
 				 ];
    	me.callParent(arguments);
    	},
    	//添加 type=0的standard
    	addAssessPlan:function(){
    		var me=this;
    		fhd_icm_assess_assessPlanContainer.remove(fhd_icm_assess_assessPlanContainer.centerContainer);
    		fhd_icm_assess_assessPlanContainer.setCenterContainer('pages/icm/assess/assessPlanPanelManager.jsp?initForm=0');
    		fhd_icm_assess_assessPlanContainer.add(fhd_icm_assess_assessPlanContainer.centerContainer);
    	},
    	//编辑
        editStandard:function(){
        	var me=this;
        	
        },
        //删除type=0的数据
        delAssessPlan:function(){
        	var me=this;
        	var selection = me.getSelectionModel().getSelection();//得到选中的记录
    		Ext.MessageBox.show({
    			title : FHD.locale.get('fhd.common.delete'),
    			width : 260,
    			msg : FHD.locale.get('fhd.common.makeSureDelete'),
    			buttons : Ext.MessageBox.YESNO,
    			icon : Ext.MessageBox.QUESTION,
    			fn : function(btn) {
    		    if (btn == 'yes') {//确认删除
    			 var ids = [];
    			for ( var i = 0; i < selection.length; i++) {
    			  ids.push(selection[i].get('id'));
    			 }
    			 FHD.ajax({//ajax调用
    		     url : __ctxPath+ '/assess/assessList/removeAssessPlans.f',
    		     params : {
    		     assessPlans : ids
    			 },
    			 callback : function(data) {
    			 if (data) {//删除成功！
    			 Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
    			 me.store.load();
    		   }
    		   }
    		});
    		}
    			}
    		});
        	
        },
    });
    
    var fhd_icm_assess_assessPlanList=null;
	Ext.onReady(function() {
		fhd_icm_assess_assessPlanList=Ext.create('FHD_icm_assess_assessPlanList',{
			url:__ctxPath+ '/assess/assessList/findAssessPlansByPage.f?dealStatus=S',
		    height : FHD.getCenterPanelHeight(),//高度为：获取center-panel的高度
			listeners : {
			}
		});
		fhd_icm_assess_assessPlanList.store.on("load",function(store){
  	    Ext.getCmp('assessPlanDel${param._dc}').setDisabled(true);
	 });
		fhd_icm_assess_assessPlanList.on('selectionchange',function(m) {
	var len=fhd_icm_assess_assessPlanList.getSelectionModel().getSelection().length; 
	 if (len== 0) {
	   Ext.getCmp('assessPlanDel${param._dc}').setDisabled(true);
	   } else if (len == 1) {
	   Ext.getCmp('assessPlanDel${param._dc}').setDisabled(false);//删除可用
	   } else if (len > 1) {
	   Ext.getCmp('assessPlanDel${param._dc}').setDisabled(false);//删除可用
	   }
	 });
		FHD.componentResize(fhd_icm_assess_assessPlanList,0,0);
	//初始化时候加载 数据
/* 	 fhd_icm_assess_assessPlanList.store.proxy.url=__ctxPath+ '/assess/assessList/findAssessPlansByPage.f?dealStatus=S',//调用后台url
	 fhd_icm_assess_assessPlanList.store.load(); */
	
});
	/***Ext.onReady end***/
</script>
</head>
<body>
	<div id='FHD.icm.assess.assessList${param._dc}'></div>
</body>
</html>
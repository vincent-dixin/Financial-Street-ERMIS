<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
var practiceAnalysisOfResults={
        xtype     : 'textareafield',
        grow      : true,
        name      : 'practiceAnalysisOfResults',
        fieldLabel: '结果分析',
        columnWidth:1/1
    }
var sampleAnalysisOfResults={
        xtype     : 'textareafield',
        grow      : true,
        name      : 'sampleAnalysisOfResults',
        fieldLabel: '结果分析',
        columnWidth:1/1
    }
var practiceGrid=Ext.create('FHD.ux.GridPanel',{//创建可编辑的grid列表，注释同不可编辑的gird
	//renderTo: 'assessResult${param._dc}', 
	multiSelect:false,
	pagable:false,
	checked:false,
	url: 'icm/assess/findpracticeAnalysis.f?assessorId=${param.assessorId}',
	height:FHD.getCenterPanelHeight()/4,
	columnWidth:1/1,
	cols:[{header:'末级流程ID',dataIndex:'id',hidden:true},
	      {header:'参评人ID',dataIndex:'assessorId',hidden:true},
	      {header:'流程分类', dataIndex: 'parentProcess', sortable: false,flex:1},
	      {header:'末级流程', dataIndex: 'name', sortable: false,flex:1},
	      {header:'穿行测试结果', dataIndex: 'presult',sortable: false,flex:1},
	      {header:'流程节点数', dataIndex: 'processPointNO',sortable: false,flex:1},
	      {header:'评价点数', dataIndex: 'allNumByPracticeTest',sortable: false,flex:1},
	      {header:'评价点通过率', dataIndex: 'qualifiedRateByPracticeTest',sortable: false,flex:1,
	    	  renderer:function(value){
	    		  var qualifiedRateByPracticeTest=value*100+'%';
	    		  return qualifiedRateByPracticeTest;
	    	  }
	      },
	      {header:'样本数', dataIndex: 'practiceTestSampleNum',sortable: false,flex:1},
	      {header:'样本合格率', dataIndex: 'qualifiedPracticeTestSample',sortable: false,flex:1,
	    	  renderer:function(value){
	    		  var qualifiedPracticeTestSample=value*100+'%';
	    		  return qualifiedPracticeTestSample;
	    	  }  
	      },
	      {header:'缺陷数', dataIndex: 'practiceHasDefectNum',sortable: false,flex:1}
	      ]
});
var sampleTestGrid=Ext.create('FHD.ux.GridPanel',{//创建可编辑的grid列表，注释同不可编辑的gird
	//renderTo: 'assessResult${param._dc}', 
	multiSelect:false,
	pagable:false,
	checked:false,
	url: 'icm/assess/findpracticeAnalysis.f?assessorId=${param.assessorId}',
	height:FHD.getCenterPanelHeight()/4,
	columnWidth:1/1,
	cols:[{header:'末级流程ID',dataIndex:'id',hidden:true},
	      {header:'参评人ID',dataIndex:'assessorId',hidden:true},
	      {header:'流程分类', dataIndex: 'parentProcess', sortable: false,flex:1},
	      {header:'末级流程', dataIndex: 'name', sortable: false,flex:1},
	      {header:'控制措施数', dataIndex: 'assessMeasureNO',sortable: false,flex:1},
	      {header:'评价点数', dataIndex: 'allNumBySampleTest',sortable: false,flex:1},
	      {header:'评价点通过率', dataIndex: 'qualifiedRateBySampleTest',sortable: false,flex:1,
	    	  renderer:function(value){
	    		  var qualifiedRateBySampleTest=value*100+'%';
	    		  return qualifiedRateBySampleTest;
	    	  }
	      },
	      {header:'样本数', dataIndex: 'sampleTestSampleNum',sortable: false,flex:1},
	      {header:'样本合格率', dataIndex: 'qualifiedSampleTestSample',sortable: false,flex:1,
	    	  renderer:function(value){
	    		  var qualifiedSampleTestSample=value*100+'%';
	    		  return qualifiedSampleTestSample;
	    	  }  
	      },
	      {header:'缺陷数', dataIndex: 'sampleHasDefectNum',sortable: false,flex:1}
	      ]
});
/***Ext.onReady start***/
Ext.onReady(function(){
	var form=Ext.create('Ext.form.Panel', {
	    layout: 'column',
	    height:FHD.getCenterPanelHeight()-8,
	    defaults:{
         	 columnWidth:1/1,
         	margin:'0 10 0 0'
	    },
	    items: [{
	    	xtype:'fieldset',
            title:'穿行测试底稿预览',
            layout:{
           	 type:'column'
            },
            defaults:{
             	 margin:'0 10 10 0'
            },
            items:[practiceGrid,practiceAnalysisOfResults]
	    },{
	    	xtype:'fieldset',
            title:'抽样测试底稿预览',
            layout:{
           	 type:'column'
            },
            defaults:{
             	 columnWidth:1/2,
             	 margin:'0 10 10 0'
            },
            items:[sampleTestGrid,sampleAnalysisOfResults]
	    }],
	    
	    tbar:{
						items: [{
							xtype: 'tbtext'
						}, '->',{
							text: '上一步',
							iconCls: 'icon-control-rewind-blue',
							handler: function(){
									fhd_icm_assess_assessPlanExecutePanelManager.remove(fhd_icm_assess_assessPlanExecutePanelManager.centerContainer);
									fhd_icm_assess_assessPlanExecutePanelManager.setCenterContainer('pages/icm/assess/assessPlanExecuteList.jsp?assessPlanId=${param.assessPlanId}');
									fhd_icm_assess_assessPlanExecutePanelManager.add(fhd_icm_assess_assessPlanExecutePanelManager.centerContainer);
							}
						},{
							text: '下一步',
							iconCls: 'icon-control-fastforward-blue',
							handler: function(){
									fhd_icm_assess_assessPlanExecutePanelManager.remove(fhd_icm_assess_assessPlanExecutePanelManager.centerContainer);
									fhd_icm_assess_assessPlanExecutePanelManager.setCenterContainer('pages/icm/assess/assessDefectList.jsp?assessPlanId=${param.assessPlanId}');
									fhd_icm_assess_assessPlanExecutePanelManager.add(fhd_icm_assess_assessPlanExecutePanelManager.centerContainer);
							}
						},{
							text: '返回',
							iconCls: 'icon-arrow-redo',
							handler: function(){
									fhd_icm_assess_assessPlanExecutePanelManager.remove(fhd_icm_assess_assessPlanExecutePanelManager.centerContainer);
									fhd_icm_assess_assessPlanExecutePanelManager.setCenterContainer('pages/icm/assess/assessPlanExecuteList.jsp?assessPlanId=${param.assessPlanId}');
									fhd_icm_assess_assessPlanExecutePanelManager.add(fhd_icm_assess_assessPlanExecutePanelManager.centerContainer);
							}
						},{
							text: '保存',
							iconCls: 'icon-control-stop-blue',
							handler:  function(){
								FHD.ajax({
									url:'',
                                    params: {
                                    },
                                    callback: function (data) {
                                    	if(data){
                            				Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
                                    	}	
                                    }
								});
							}
						},{
							text: '提交',
							iconCls: 'icon-control-stop-blue',
							handler:  function(){
								FHD.ajax({
									url:'',
                                    params: {
                                    },
                                    callback: function (data) {
                                    	if(data){
                            				Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
            									fhd_icm_assess_assessPlanExecutePanelManager.remove(fhd_icm_assess_assessPlanExecutePanelManager.centerContainer);
            									fhd_icm_assess_assessPlanExecutePanelManager.setCenterContainer('pages/icm/assess/assessPlanExecuteList.jsp?assessPlanId=${param.assessPlanId}');
            									fhd_icm_assess_assessPlanExecutePanelManager.add(fhd_icm_assess_assessPlanExecutePanelManager.centerContainer);
                            			}	
                                    }
								});
							}
						}]
				},
				
	    renderTo: 'analysisOfResults${param._dc}'
	});
	
})

/***Ext.onReady end***/
</script>
</head>
<body>
	<div id='analysisOfResults${param._dc}'></div>
</body>
</html>
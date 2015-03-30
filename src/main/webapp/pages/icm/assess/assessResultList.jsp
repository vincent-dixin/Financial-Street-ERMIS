<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
/***function start***/
 
/***function end***/
/***Ext.onReady start***/
Ext.onReady(function(){
	/***assessPlanRelaPointGrid start***/
	assessPlanRelaPointGrid = Ext.create('FHD.ux.GridPanel',{//创建可编辑的grid列表，注释同不可编辑的gird
		renderTo: 'assessResult${param._dc}', 
		multiSelect:false,
		checked:false,
		pagable:false,
		url: 'icm/assess/findAssessorListByPage.f?assessPlanId=${param.assessPlanId}',
		cols:[{dataIndex:'id',hidden:true},
		      {header:'参评人', dataIndex: 'empName', sortable: false,flex:1},
		      {header:'完成期限', dataIndex: 'deadLine',sortable: false,flex:1},
		      {header:'实际完成日期', dataIndex: 'actualFinishDate',sortable: false,flex:1},
		      {header:'穿行的评价点总数', dataIndex: 'allNumByPracticeTest',sortable: false,flex:1},
		      {header:'抽样的评价点总数', dataIndex: 'allNumBySampleTest',sortable: false,flex:1},
		      {header:'待穿行的评价点数', dataIndex: 'numByPracticeTest',sortable: false,flex:1},
		      {header:'待抽样的评价点数', dataIndex: 'numBySampleTest',sortable: false,flex:1},
		      {
					header: FHD.locale.get('fhd.common.operate'),dataIndex:'',width:60,hideable:false, 
					align: 'center',
					xtype:'actioncolumn',
					items: [{
						text:'计划列表',
						icon: __ctxPath+'/images/icons/ text_align_justify.png',  
						tooltip: '计划列表',
						handler: function(grid, rowIndex, colIndex) {
							var rec = grid.getStore().getAt(rowIndex);
							fhd_icm_assess_assessPlanExecutePanelManager.remove(fhd_icm_assess_assessPlanExecutePanelManager.centerContainer);
							fhd_icm_assess_assessPlanExecutePanelManager.setCenterContainer('pages/icm/assess/assessorRelaProcessList.jsp?assessPlanId=${param.assessPlanId}&assessorId='+rec.data.id);
							fhd_icm_assess_assessPlanExecutePanelManager.add(fhd_icm_assess_assessPlanExecutePanelManager.centerContainer);
						}
					}]
		      }],
	});
});
/***Ext.onReady end***/
</script>
</head>
<body>
	<div id='assessResult${param._dc}' ></div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
/***function start***/
  function returnAssessorRelaProcessList(){//返回
	 assessResuListView.remove(assessResuListView.gridPanel);
	 assessResuListView.gridPanel=Ext.create('Ext.container.Container',{
 		autoLoad : {
 			url : 'pages/icm/assess/assessorRelaProcessList.jsp?assessorId=${param.assessorId}',
 			scripts : true
 		}
 	});
	 assessResuListView.add(assessResuListView.gridPanel);
	}

/***function end***/
/***Ext.onReady start***/
Ext.onReady(function(){
	/***assessPlanRelaPointGrid start***/
	assessPlanRelaPointGrid = Ext.create('FHD.ux.GridPanel',{//创建可编辑的grid列表，注释同不可编辑的gird
		renderTo: 'assessResult${param._dc}', 
		multiSelect:false,
		pagable:false,
		checked:false,
		url: 'icm/assess/findAssessResultPageBySome.f?testType=ca_assessment_measure_1&assessorId=${param.assessorId}&processId=${param.processId}',
		height:FHD.getCenterPanelHeight(),
		cols:[{dataIndex:'id',hidden:true},
		      {header:'控制措施', dataIndex: 'measureName', sortable: false,flex:1},
		      {header:'评价点', dataIndex: 'assessPointDesc',sortable: false,flex:1},
		      {header:'实施证据', dataIndex: 'assessSampleName',sortable: false,flex:1},
		      {header:'抽取样本数', dataIndex: 'extractedAmount',sortable: false},
		      {header:'有效样本数', dataIndex: 'effectiveNumber',sortable: false},
		      {header:'无效样本数', dataIndex: 'defectNumber',sortable: false},
		      {header:'是否存在缺陷', dataIndex: 'hasDefect',sortable: false},
		      {header:'说明', dataIndex: 'hasDefect',sortable: false,flex:1},
		      {
					header: FHD.locale.get('fhd.common.operate'),dataIndex:'',width:60,hideable:false, 
					align: 'center',
					xtype:'actioncolumn',
					items: [{
						icon: __ctxPath+'/images/icons/edit.gif',  // Use a URL in the icon config
						tooltip: '编辑样本',
						handler: function(grid, rowIndex, colIndex) {
							grid.getSelectionModel().deselectAll();
							
							var rows=[grid.getStore().getAt(rowIndex)];
			 	    		grid.getSelectionModel().select(rows,true);
							var rec = grid.getStore().getAt(rowIndex);
							assessResuListView.remove(assessResuListView.gridPanel);
							assessResuListView.gridPanel=Ext.create('Ext.container.Container',{
								autoLoad : {
									url : 'pages/icm/assess/editSample.jsp?url=pages/icm/assess/assessResultSample.jsp&assessorId=${param.assessorId}&processId=${param.processId}',
			    					scripts : true,
								}
							});
							assessResuListView.add(assessResuListView.gridPanel);
						}
					}]
		      }],
		tbarItems:[{iconCls: 'icon-arrow-redo',id:'return${param._dc}',handler:returnAssessorRelaProcessList, disabled : false, scope : this},'-',
		           {labelWidth : 80,xtype : 'displayfield',disabled : false,lblAlign : 'rigth',fieldLabel : '计划名称',value : '',	name : 'planName'},'-',
	               {labelWidth : 80,xtype : 'displayfield',disabled : false,lblAlign : 'rigth',fieldLabel : '末级流程',value : '',	name : 'processName'}]
	});
});
/***Ext.onReady end***/
</script>
</head>
<body>
	<div id='assessResult${param._dc}'></div>
</body>
</html>
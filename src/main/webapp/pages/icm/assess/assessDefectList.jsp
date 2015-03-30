<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>评价计划编辑</title>
<script type="text/javascript">
Ext.define('FHD_icm_assess_assessDefect',{
	extend : 'Ext.form.Panel',
	renderTo:'FHD.icm.assess.assessDefect${param._dc}',
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
	initComponent : function() {
		var me = this;
		var basicInfo=Ext.create('pages.icm.assess.assessPlanBasicInformation');
		var gridPanel = Ext.create('FHD.ux.GridPanel',{//创建可编辑的grid列表，注释同不可编辑的gird
			//renderTo: 'assessResult${param._dc}', 
			multiSelect:false,
			pagable:false,
			checked:false,
			url: 'icm/defect/findAssessDefectListBypage.f?assessPlanId=${param.assessPlanId}',
			//height:FHD.getCenterPanelHeight()/4,
			height:400,
			columnWidth:1/1,
			cols:[{header:'末级流程ID',dataIndex:'processid',hidden:true},
			      {header:'参评人ID',dataIndex:'assessorId',hidden:true},
			      {header:'缺陷Id',dataIndex:'id',hidden:true},
			      {header:'流程分类', dataIndex: 'parentProcess', sortable: false,flex:1},
			      {header:'末级流程', dataIndex: 'name', sortable: false,flex:1},
			      {header:'评价点', dataIndex: 'allNumBySampleTest',sortable: false,flex:1},
			      {header:'评价人', dataIndex: 'qualifiedRateBySampleTest',sortable: false,flex:1},
			      {header:'缺陷描述', dataIndex: 'sampleTestSampleNum',sortable: false,flex:1},
			      {header:'缺陷级别', dataIndex: 'controlRequirement',sortable: false,flex:1,},
			      {header:'缺陷类型', dataIndex: 'type',sortable: false,flex:1},
			      {header:'整改责任部门', dataIndex: 'companyId',sortable: false,flex:1}
			      ]
		});
		me.items = [basicInfo,{
			xtype : 'fieldset',
			margin: '7 10 0 30',
			layout : {
				type : 'column'
			},
			collapsed : false,
			collapsible : true,
			title : '缺陷清单',
			items : [gridPanel]
		}
		];
		me.bbar={
				items: [{
					xtype: 'tbtext',
				}, '->',{
					text: '返回',
					iconCls: 'icon-arrow-redo',
					handler: function(){
					}
				}]
		}
		this.callParent(arguments);
	}
});
var FHD_icm_assess_assessDefect=null;
Ext.onReady(function() {
	FHD_icm_assess_assessDefect=Ext.create('FHD_icm_assess_assessDefect');
	FHD.componentResize(FHD_icm_assess_assessDefect,0,0);
	FHD_icm_assess_assessDefect.getForm().load({
		url:'assess/assessPlan/findAssessPlanForView.f?assessPlanId=${param.assessPlanId}',
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
	<div id='FHD.icm.assess.assessDefect${param._dc}'></div>
</body>
</html>
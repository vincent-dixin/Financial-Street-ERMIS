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
	basicInfo:null,
	gridPanel:{},
	initComponent : function() {
		var me = this;
		me.basicInfo=Ext.create('pages.icm.assess.assessPlanBasicInformation');
		me.gridPanel = Ext.create('Ext.container.Container', {
			region : 'center',
			columnWidth : 1,
			autoLoad : {
				url : 'pages/icm/assess/assessResultList.jsp?assessPlanId=${param.assessPlanId}',
						scripts : true
			}
		});
		me.items = [me.basicInfo,{
			xtype : 'fieldset',
			margin: '7 10 0 30',
			layout : {
				type : 'column'
			},
			collapsed : false,
			collapsible : true,
			title : '指定评价人',
			items : [me.gridPanel]
		}
		];
		me.bbar={
				items: [{
					xtype: 'tbtext',
				}, '->',{
					text: '返回',
					iconCls: 'icon-arrow-redo',
					handler: function(){
						fhd_icm_assess_assessPlanExecutePanelManager.remove(fhd_icm_assess_assessPlanExecutePanelManager.centerContainer);
						fhd_icm_assess_assessPlanExecutePanelManager.setCenterContainer('pages/icm/assess/assessPlanExecuteList.jsp');
						fhd_icm_assess_assessPlanExecutePanelManager.add(fhd_icm_assess_assessPlanExecutePanelManager.centerContainer);
					}
				}]
		}
		me.tbar={
				items: [{
					xtype: 'tbtext',
				}, '->',{
					text: '返回',
					iconCls: 'icon-arrow-redo',
					handler: function(){
						fhd_icm_assess_assessPlanExecutePanelManager.remove(fhd_icm_assess_assessPlanExecutePanelManager.centerContainer);
						fhd_icm_assess_assessPlanExecutePanelManager.setCenterContainer('pages/icm/assess/assessPlanExecuteList.jsp');
						fhd_icm_assess_assessPlanExecutePanelManager.add(fhd_icm_assess_assessPlanExecutePanelManager.centerContainer);
					}
				}]
		}
		this.callParent(arguments);
	}
});
var fhd_icm_assess_assessPlanShow=null;
Ext.onReady(function() {
	fhd_icm_assess_assessPlanShow=Ext.create('FHD_icm_assess_assessPlanShow');
	FHD.componentResize(fhd_icm_assess_assessPlanShow,0,0);
	fhd_icm_assess_assessPlanShow.getForm().load({
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
	<div id='FHD.icm.assess.assessPlanEdit${param._dc}'></div>
</body>
</html>
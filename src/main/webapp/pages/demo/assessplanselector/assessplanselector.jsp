<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>内控评价计划选择组件</title>
	<script type="text/javascript">
		Ext.onReady(function(){
			var assessplanselector = Ext.create('FHD.ux.icm.assess.AssessPlanSelector',{
            	fieldLabel : '评价计划',
            	name: 'assessplanId'
            });
			
			//表单panel
			var form = Ext.create("Ext.form.Panel",{
				renderTo: 'assessplandemo',
				autoScroll: true,
		        border: false,
		        bodyPadding: "5 5 5 5",
		        items: [{
		            xtype: 'fieldset',//基本信息fieldset
		            collapsible: false,
		            defaults: {
		            	margin: '7 30 3 30',
		            	columnWidth:.5
		            },
		            layout: {
		                type: 'column'
		            },
		            title: '评价计划选择',
		            items:[assessplanselector]
		        }]
			});
		});
	</script>
</head>
<body>
	<div id='assessplandemo'></div>
</body>
</html>
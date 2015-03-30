<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>内控缺陷选择组件</title>
	<script type="text/javascript">
		Ext.onReady(function(){
			var defectselector = Ext.create('FHD.ux.icm.defect.DefectSelector',{
            	fieldLabel : '缺陷',
            	name: 'defectId'
            });
			
			//表单panel
			var form = Ext.create("Ext.form.Panel",{
				renderTo: 'defectdemo',
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
		            title: '缺陷选择',
		            items:[defectselector]
		        }]
			});
		});
	</script>
</head>
<body>
	<div id='defectdemo'></div>
</body>
</html>
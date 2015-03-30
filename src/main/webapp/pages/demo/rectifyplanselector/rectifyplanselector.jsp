<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>内控整改计划选择组件</title>
	<script type="text/javascript">
		Ext.onReady(function(){
			var rectifyplanselector = Ext.create('FHD.ux.icm.rectify.ImproveSelector',{
            	fieldLabel : '整改计划',
            	name: 'rectifyplanId'
            });
			
			//表单panel
			var form = Ext.create("Ext.form.Panel",{
				renderTo: 'rectifyplandemo',
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
		            title: '整改计划选择',
		            items:[rectifyplanselector]
		        }]
			});
		});
	</script>
</head>
<body>
	<div id='rectifyplandemo'></div>
</body>
</html>
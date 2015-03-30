<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程选择</title>
<script type="text/javascript">
	Ext.onReady(function() {
		var selector = Ext.create('FHD.ux.standard.StandardSelector',{
    	    columnWidth : 1,
			name : 'standardName1',
			multiSelect : true,//多选，都带复选框，包括，父节点
			myType:'standard',
			height : 100,
			labelText : $locale('standardSelector.labelText'),
			labelAlign : 'right',
			labelWidth : 80
	     });
		var panel = Ext.create('Ext.form.Panel', {
			id : 'formdemo',
			bodyPadding : 25,
			renderTo : 'standard${param._dc}',
			autoScroll : true,
			//layout:'fit',
			height : FHD.getCenterPanelHeight(),
			items : [ {
				xtype : 'fieldset',
				defaults : {
					columnWidth : 1/1
				},//每行显示一列，可设置多列
				layout : {
					type : 'column'
				},
				bodyPadding : 5,
				collapsed : false,
				collapsible : false,
				title : '内控标准选择',
				items : [selector]
			} ]
		});
		FHD.componentResize(panel, 200, 0);
	});
</script>
</head>
<body>
	<div id='standard${param._dc}'>
		<div id="l1"></div>
	</div>
</body>
</html>
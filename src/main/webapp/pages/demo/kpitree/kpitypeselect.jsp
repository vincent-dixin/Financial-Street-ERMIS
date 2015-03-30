<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>指标类型选择</title>
<script type="text/javascript">
	Ext.onReady(function(){

		var panel = Ext.create('Ext.form.Panel',{
			id:'formdemo',
			bodyPadding: 25,
			renderTo:'demos',
			autoScroll:true,
			height:FHD.getCenterPanelHeight(),
			items: [
					{
						xtype: 'fieldset',
						defaults: {columnWidth: 1/1},//每行显示一列，可设置多列
						layout: {type: 'column'},
						bodyPadding: 5,
						collapsed: false,
						collapsible: false,
						title: '指标类型选择',
						items:[
						 {
							xtype:'kpitypeselect',
							labelWidth:70,
							btnHeight:23,
						}
					]
					}
					
			      ]
		});
		FHD.componentResize(panel,200,0); 
});
</script>
</head>
<body>
	<div id='demos'>
		<div id="l1"></div>
	</div>
</body>
</html>
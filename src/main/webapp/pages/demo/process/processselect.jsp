<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程选择</title>
<script type="text/javascript">
	Ext.onReady(function(){
		var panel = Ext.create('Ext.form.Panel',{
			id:'formdemo',
			bodyPadding: 25,
			renderTo:'demos',
			autoScroll:true,
			//layout:'fit',
			height:FHD.getCenterPanelHeight(),
			items: [
					{xtype: 'fieldset',
						defaults: {columnWidth: 1/1},//每行显示一列，可设置多列
						layout: {type: 'column'},
						bodyPadding: 5,
						collapsed: false,
						collapsible: false,
						title: $locale('fhd.process.processselector.title'),
						items:[
                        	Ext.create('FHD.ux.process.processSelector',{
                        		columnWidth:1,
                                name:'kpiIds',
                                single : false,	 
                                parent : false,
                                height :100,
                                fieldLabel: '流程选择',
                                labelAlign: 'left',
                                labelWidth: 80
                        	}),Ext.create('FHD.ux.process.processSelector',{
                        		columnWidth:1,
                                name:'kpiIds',                                           
                                multiSelect : false,	
                                height :30,
                                fieldLabel: '流程选择',
                                labelAlign: 'left',
                                labelWidth: 80
                        	})
							]
					}
			      ]
		});
		FHD.componentResize(panel,200,0); 
});
</script>
</head>
<body>
	<div id='demos'><div id="l1"></div></div>
</body>
</html>
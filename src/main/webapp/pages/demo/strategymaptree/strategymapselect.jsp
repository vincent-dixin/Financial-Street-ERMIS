<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    
<title>目标选择</title>

<script type="text/javascript">
	Ext.onReady(function(){
		var panel = Ext.create('Ext.form.Panel',{
			bodyPadding: 5,
			renderTo:'demos',
			height:FHD.getCenterPanelHeight(),
			items: [
					{xtype: 'fieldset',
						defaults: {columnWidth: 1/1},//每行显示一列，可设置多列
						layout: {type: 'column'},
						bodyPadding: 5,
						collapsed: false,
						collapsible: false,
						title: '目标选择',
						items:[
						 {
                        	columnWidth:1,
                            xtype: 'kpistrategymapselector',
                            name:'kpiStrategyMapIds',
                            extraParams:{smIconType:'display',canChecked:true},
                            single:false,
                            height :100,
                            //value:'3,4',
                            labelText: $locale('kpistrategymapselector.labeltext'),
                            labelAlign: 'right',
                            labelWidth: 80
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
	<div id='demos'><div id="l1"></div></div>
</body>
</html>
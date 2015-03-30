<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>时间组件</title>
<script type="text/javascript">
/***attribute start***/
/***attribute end***/

/***Ext.onReady start***/
Ext.onReady(function(){
	
	Ext.create('Ext.panel.Panel',{
			renderTo: 'timeDemo',
			border:false,
    		items: [{
    			xtype: 'button', 
    			text: '时间控件',
    				handler:function(){
    					Ext.create('FHD.ux.timestamp.TimestampWindow',{
							onSubmit:function(values){
								var valuesStr = values.split(',');
								var yearId = valuesStr[0];
								var quarterId = valuesStr[1];
								var monthId = valuesStr[2];
								var weekId = valuesStr[3];
								alert(yearId);
								alert(quarterId);
								alert(monthId);
								alert(weekId);
							}
						}).show();
    				}
    		}]
	});
});

/***Ext.onReady end***/

</script>
</head>
<body>
	<div id='timeDemo'></div>
</body>
</html>
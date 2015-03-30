<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>风险选择</title>
<script type="text/javascript">
/***attribute start***/
/***attribute end***/

/***Ext.onReady start***/
Ext.onReady(function(){
	
	Ext.widget('riskstrategymapselector', {
		renderTo: 'riskSelectDemo',
		name:'kpiStrategyMapIds',
	    single:false,
	    height :100,
	    value:'3,4',
	    labelText: $locale('riskstrategymapselector.labeltext'),
	    labelAlign: 'right',
	    labelWidth: 80
	});
});

/***Ext.onReady end***/

</script>
</head>
<body>
	<div id='riskSelectDemo'></div>
</body>
</html>
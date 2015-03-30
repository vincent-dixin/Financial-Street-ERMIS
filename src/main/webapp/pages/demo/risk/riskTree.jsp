<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>风险树</title>
<script type="text/javascript">
/***attribute start***/
/***attribute end***/

/***Ext.onReady start***/
Ext.onReady(function(){
	var extraParams = {
		canChecked : false
	};
	Ext.widget('riskstrategymaptree', {
		renderTo: 'riskTreeDemo',
		width : 220,
		height : 400,
		myRiskTreeVisible : false,
		OrgRiskTreeVisible : false,
		riskTreeVisible : true,
		rbsVisible : false,
		extraParams : extraParams,
		riskClickFunction:function(node){
			alert(node);
		}
	});
});

/***Ext.onReady end***/

</script>
</head>
<body>
	<div id='riskTreeDemo'></div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>公司选择</title>
<script type="text/javascript">
	Ext.onReady(function() {
		var panel = Ext.create('FHD.ux.kpi.KpiStrategyMapTree', {
			id : 'formdemo',
			width : 300,
			height : 500,
			//renderTo : 'demos',
			extraParams : {
				canChecked : false
			},
			clickFunction : function(node) {
			},
			orgSmTreeContextMenuFc : function(tree, node) {
				var type = node.data.type;
				var menu;
				if (type === 'sm') {
					menu = Ext.create('Ext.menu.Menu', {
						items : [ {
							text : '111'
						}, {
							text : '222'
						} ]
					});
				}
				return menu;
			}
		});
		
		/* var categoryTreePanel = Ext.create('FHD.ux.category.CategoryStrategyMapTree', {
			width : 300,
			height : 500,
			//renderTo : 'demos',
			extraParams : {
				canChecked : false
			},
			
			myCategoryClickFunction : function(node) {alert(node);},
			categoryClickFunction:function(node){alert(node);},
			orgCategoryClickFunction : function(node){alert(node);},
		});
		
		var kpiTypeTreePanel = Ext.create('FHD.ux.kpi.KpiTypeTree', {
			width : 300,
			height : 500,
			//renderTo : 'demos',
			extraParams : {
				canChecked : false
			},
			
			orgCategoryClickFunction : function(node){alert(node);},
		});
		 */
		
		var main = Ext.create('Ext.panel.Panel', {
			renderTo : 'demos',
			defaults:{margin: '10 10 10 10'},
			layout:{type: 'column'},
			items : [panel /*, categoryTreePanel, kpiTypeTreePanel*/]	
		});
		
		FHD.componentResize(main, 0, 0);
	});
</script>
</head>
<body>
	<div id='demos'></div>
</body>
</html>
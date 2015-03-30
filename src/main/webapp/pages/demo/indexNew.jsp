<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<script type="text/javascript">
var tree,panel;
Ext.define('demopanel',{
	extend:'Ext.panel.Panel',
	border:false,
	autoScroll:false,
	height:FHD.getCenterPanelHeight()-3,
	region: 'center'
});
function initDemoPanel(url){
	var panelDemopanel;
	if(url!='暂无'){
		panelDemopanel=Ext.create('demopanel',{autoLoad :{ url: url,scripts: true}});
	}else{
		panelDemopanel=Ext.create('demopanel',{title:'暂无'});
	}
	panel.demopanel=panelDemopanel;
	panel.add(panelDemopanel);
}
Ext.onReady(function () {
	tree = Ext.create('Ext.tree.Panel', {
	    region: 'west',
	    split: true,
        collapsible : true,
        border:true,
        maxWidth:300,
        height:FHD.getCenterPanelHeight()-3,
	    width:220,
	    root: {
	        text: '开发帮助',
	        iconCls:'icon-help',
	        expanded: true,
	        autoLoad:true,
	        children:[
	        	{
	        		text:'公共控件',
	        		iconCls:'icon-folder',
	        		expanded: true,
	        		children:[
						{
						    text: '树组件',
						    iconCls:'icon-information',
						    leaf: true
						},
						{
						    text: 'grid组件',
						    iconCls:'icon-information',
						    leaf: true
						}
	        		]
	        	}
	        ]
	        
	    },
	    listeners : {
	    	itemclick:function(view,node){
	    		panel.remove(panel.demopanel);
	    		switch (node.data.text){
		    		
		    		case '树组件':
		    			initDemoPanel('pages/demo/common/tree.jsp');
	    				break;
		    		case 'grid组件':
		    			initDemoPanel('pages/demo/common/grid.jsp');
	    				break;
		    		
	    			default:
	    				initDemoPanel('pages/risk/dimension/dimension.jsp');
	    		}
	    	}
	    }
	});
	var panelDemopanel=Ext.create('demopanel',{title:'暂无'});
	panel = Ext.create('Ext.container.Container',{
	    renderTo: 'demotree', 
	    autoScroll:true,
	    layout: {
	        type: 'border'
	    },
	    defaults: {
            border:true
        },
	    demopanel:panelDemopanel,
        height:FHD.getCenterPanelHeight()-3,
	    items:[tree,panelDemopanel]
	});
	FHD.componentResize(panel,0,0); 
});

</script>


</head>
<body>
	<div id='demotree'></div>
</body>

</html>
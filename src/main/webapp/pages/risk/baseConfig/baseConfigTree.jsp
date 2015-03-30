<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<script type="text/javascript">
var baseConfigTree,baseConfigMainPanel;
Ext.define('demopanel',{
	extend:'Ext.panel.Panel',
	border:false,
	autoScroll:false,
	height:FHD.getCenterPanelHeight(),
	region: 'center'
});
function initDemoPanel(url){
	var panelDemopanel;
	if(url!='暂无'){
		panelDemopanel=Ext.create('demopanel',{autoLoad :{ url: url,scripts: true}});
	}else{
		panelDemopanel=Ext.create('demopanel',{title:'暂无'});
	}
	baseConfigMainPanel.demopanel=panelDemopanel;
	baseConfigMainPanel.add(panelDemopanel);
}
Ext.onReady(function () {
	baseConfigTree = Ext.create('Ext.tree.Panel', {
	    region: 'west',
	    title:FHD.locale.get('fhd.risk.baseconfig.option'),
	    useArrows: true,
	    rowLines:true,
	    split: true,
        collapsible : true,
        border:true,
        rootVisible : false,
        maxWidth:300,
        height:FHD.getCenterPanelHeight(),
	    width:220,
	    root: {
	        text: '',
	        iconCls:'icon-help',
	        expanded: true,
	        autoLoad:true,
	        children:[
				{
				    text: FHD.locale.get('fhd.risk.baseconfig.dimensionConfig'),
				  	iconCls:'icon-asterisk-orange',
				    leaf: true
				},
				{
				    text: FHD.locale.get('fhd.risk.baseconfig.templateConfig'),
				  	iconCls:'icon-xhtml',
				    leaf: true
				},
				{
				    text: FHD.locale.get('fhd.risk.baseconfig.riskMappingConfig'),
				  	iconCls:'icon-tupu',
				    leaf: true
				},
				{
				    text: FHD.locale.get('fhd.risk.baseconfig.weightConfig'),
				  	iconCls:'icon-chart-pie',
				    leaf: true
				}
	        ]
	        
	    },
	    listeners : {
	    	itemclick:function(view,node){
	    		baseConfigMainPanel.remove(baseConfigMainPanel.demopanel);
	    		switch (node.data.text){
		    		case FHD.locale.get('fhd.risk.baseconfig.dimensionConfig'):
		    			initDemoPanel('pages/risk/baseConfig/dimension.jsp');
	    				break;
		    		case FHD.locale.get('fhd.risk.baseconfig.templateConfig'):
		    			initDemoPanel('pages/risk/baseConfig/template.jsp?riskId=');
	    				break;	
	    			default:
	    				initDemoPanel('暂无');
	    		}
	    	}
	    }
	});
	var panelDemopanel=Ext.create('demopanel',{autoLoad :{ url: 'pages/risk/baseConfig/template.jsp',scripts: true}});
	baseConfigMainPanel = Ext.create('Ext.container.Container',{
	    renderTo: 'baseConfigMainPanelDiv${param._dc}', 
	    autoScroll:true,
	    layout: {
	        type: 'border'
	    },
	    defaults: {
            border:true
        },
	    demopanel:panelDemopanel,
        height:FHD.getCenterPanelHeight(),
	    items:[baseConfigTree,panelDemopanel]
	});
	FHD.componentResize(baseConfigMainPanel,0,0); 
});

</script>


</head>
<body>
	<div id='baseConfigMainPanelDiv${param._dc}'></div>
</body>

</html>
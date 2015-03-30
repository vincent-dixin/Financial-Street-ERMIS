Ext.define('FHD.demo.IndexMainPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.indexmainpanel',

    treepanel:null,		//左侧组件树导航
    mainpanel:null,		//右侧主面板，里面放着demepanel
    demopanel:null,		//右侧组件demo面板
    initDemoPanel:function(componentName){
    	var me = this;
    	me.mainpanel.remove(me.demopanel);
    	if(componentName!=''){
    		me.demopanel = Ext.create(componentName,{
	    		height:FHD.getCenterPanelHeight()-3
	    	});
    	}else{	//点击目录展示空面板
    		me.demopanel = Ext.create("Ext.panel.Panel",{
    			title:'暂无',
	    		height:FHD.getCenterPanelHeight()-3
	    	});
    	}
    	
		me.mainpanel.add(me.demopanel);
    },
    
    /**
     * 初始化页面组件
     */
    initComponent: function () {
        var me = this;
  
        me.tree = Ext.create('Ext.tree.Panel', {
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
		        		text:'UI控件',
		        		iconCls:'icon-folder',
		        		expanded: true,
		        		children:[
		        			{
							    text: 'grid组件',
							    iconCls:'icon-information',
							    leaf: true
							},
							{
							    text: '树组件',
							    iconCls:'icon-information',
							    leaf: true
							},
							{
							    text: '树选择控件',
							    iconCls:'icon-information',
							    leaf: true
							},
							{
							    text: '树列表选择控件',
							    iconCls:'icon-information',
							    leaf: true
							},
							{
							    text: '列表选择控件',
							    iconCls:'icon-information',
							    leaf: true
							},
							{
							    text: '图表控件',
							    iconCls:'icon-information',
							    leaf: true
							},
							{
							    text: '风险事件选择弹窗控件',
							    iconCls:'icon-information',
							    leaf: true
							},
							{
							    text: '按风险事件过滤得风险组合树控件',
							    iconCls:'icon-information',
							    leaf: true
							}
		        		]
		        	},{
		        		text:'布局控件',
		        		iconCls:'icon-folder',
		        		expanded: true,
		        		children:[
							{
							    text: '步骤导航布局控件',
							    iconCls:'icon-information',
							    leaf: true
							},
							{
							    text: '折叠树布局控件',
							    iconCls:'icon-information',
							    leaf: true
							},
		        			{
							    text: '基础管理布局控件',
							    iconCls:'icon-information',
							    leaf: true
							}
															
		        		]
		        	}
		        ]
		        
		    },
		    listeners : {
		    	itemclick:function(view,node){
		    		switch (node.data.text){
			    		
			    		case '树组件':
			    			me.initDemoPanel('FHD.demo.layout.TreePanelDemo');
		    				break;
			    		case 'grid组件':
			    			me.initDemoPanel('FHD.demo.layout.GridPanelDemo');
		    				break;
		    			case '树选择控件':
			    			me.initDemoPanel('FHD.demo.layout.TreeSelectorDemo');
		    				break;
		    			case '树列表选择控件':
			    			me.initDemoPanel('FHD.demo.layout.TreeListSelectorDemo');
		    				break;
		    			case '列表选择控件':
			    			me.initDemoPanel('FHD.demo.layout.ListSelectorDemo');
		    				break;
		    			case '步骤导航布局控件':
			    			me.initDemoPanel('FHD.demo.layout.StepNavigatorDemo');
		    				break;
			    		case '基础管理布局控件':
			    			me.initDemoPanel('FHD.demo.layout.SingleLayoutDemo');
		    				break;
		    			case '多表管理布局控件':
			    			me.initDemoPanel('FHD.demo.layout.MultiLayoutDemo');
		    				break;
		    			case '折叠树布局控件':
			    			me.initDemoPanel('FHD.demo.layout.AccordionTreeDemo');
		    				break;	
		    			case '图表控件':
		    				me.initDemoPanel('FHD.demo.chart.ChartDashboardDemo');
		    				break;
		    			case '风险事件选择弹窗控件':
		    				me.initDemoPanel('FHD.demo.riskEvent.RiskEventSelectWindowDemo');
		    				break;
		    			case '按风险事件过滤得风险组合树控件':
		    				me.initDemoPanel('FHD.demo.riskEvent.RiskEventFilterTreeDemo');
		    				break;
		    			default:
		    				me.initDemoPanel('');
		    		}
		    	}
		    }
		});
		me.mainpanel=Ext.create('Ext.panel.Panel',{
			border:false,
			autoScroll:true,
			height:FHD.getCenterPanelHeight()-3,
			region: 'center'
		});	
        
        Ext.apply(me, {
            autoScroll:false,
		    layout: {
		        type: 'border'
		    },
		    defaults: {
	            border:true
	        },
	        height:FHD.getCenterPanelHeight()-3,
		    items:[me.tree,me.mainpanel]
        });
        
        me.callParent(arguments);      
    }
});
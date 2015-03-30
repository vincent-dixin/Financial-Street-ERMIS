/**
 * 机构管理主面板
 * 
 * @author 金鹏祥
 */
Ext.define('FHD.view.sys.organization.ManPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.manPanel',
    
    requires: [
           'FHD.view.sys.organization.TreePanel',
           'FHD.view.sys.organization.RightPanel'
    ],
    
    // 初始化方法
    initComponent: function() {
    	var me = this;
    	var variable;
//    	debugger;
//    	var mainPanel;
//    	var panel;//页签
    	//var orgtreeId;
//    	var loadable = true;//解决重复load冲突的问题
//    	var orgTree, queryUrl = 'sys/organization/findOrgTreeBySome.f';
//    	var isAdd = true;
    	
    	//右侧面板
    	me.rightPanel = Ext.widget('rightPanel');
    	
    	//机构树
    	me.treePanel = Ext.widget('treePanel');
    	
//    	me.containerRight = Ext.create('Ext.container.Container', {
//    		region:'center',
//        	layout:{
//                align: 'stretch',
//                type: 'vbox'
//    		},
//    		items:[
//    		    {
//	    			xtype:'box',
//	    			//height:20,
//	    			style : 'border-left: 1px  #99bce8 solid;',
//	    			html:'<div id="kpimaincontainer">1231232123</div>',
//		            listeners : {
//		            	afterrender: function(){
//		            		var navigationBar = Ext.create('Ext.scripts.component.NavigationBars',{
//					        	type: 'sc',
//					        	id : 'a5ab2b43-8605-44a5-b019-43da9dc034a0'
//					        });
//		            		navigationBar.renderHtml('kpimaincontainer', 'a5ab2b43-8605-44a5-b019-43da9dc034a0', '', 'sc');
//		            	}
//	            }
//    		},me.demoTestCardPanel]
//        });
    	
    	Ext.apply(me, {
    		//demopanel:me.panelDemopanel,
//            height:FHD.getCenterPanelHeight(),
            border:false,
     		layout: {
     	        type: 'border',
     	        padding: '0 0 5	0'
     	    },
     	    items:[me.treePanel, me.rightPanel]
        });
    	
        me.callParent(arguments);
        
        me.rightPanel.on('resize',function(p){
    		me.rightPanel.setHeight(FHD.getCenterPanelHeight() );
    	});
    }
});
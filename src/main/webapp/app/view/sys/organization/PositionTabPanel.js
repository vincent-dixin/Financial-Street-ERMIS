/**
 * 岗位管理TAB面板
 * 
 * @author 
 */
Ext.define('FHD.view.sys.organization.PositionTabPanel', {
    extend: 'Ext.tab.Panel',
    alias: 'widget.positionTabPanel',
    
    requires: [
           'FHD.view.sys.organization.org.OrgGridPanel',
           'FHD.view.sys.organization.emp.EmpGridPanel',
          // 'FHD.view.sys.organization.positiion.PositionGridPanel',
           'FHD.view.sys.organization.positiion.PositionEditPanel'
    ],
    
    listeners: {
	  	tabchange:function(tabPanel, newCard, oldCard, eOpts){
	  		var me = tabPanel;
	  		var treepanel = Ext.getCmp('treePanel');
	  		var cardid = newCard.id;
	  		if('positionempGridPanel'==cardid){	
	  			me.empGridPanel.store.proxy.url = me.empGridPanel.queryUrl;
  				me.empGridPanel.store.proxy.extraParams.positionIds = treepanel.currentNode.data.id;
  				me.empGridPanel.store.load();
  			}else if('positionEditPanel'==cardid){
  				if("jg"!=treepanel.currentNode.data.type){
  					me.positionEditPanel.orgtreeId = treepanel.currentNode.data.id;
  	  				me.positionEditPanel.load();
  	  				me.positionEditPanel.isAdd=false;
  				}
	  		}
	  	}
    },
    
    
    // 初始化方法
    initComponent: function() {
    	var me = this;
    	variable = 'position';
    	
    	me.id = 'positionTabPanel';
    	//机构GRID
    	me.orgGridPanel = Ext.widget('orgGridPanel');
    	//岗位GRID
    	//me.positionGridPanel = Ext.widget('positionGridPanel');
    	//人员GRID
    	me.empGridPanel = Ext.widget('empGridPanel');
    	//岗位基本信息
    	me.positionEditPanel = Ext.widget('positionEditPanel');
    	//设置员工列表菜单文本
    	var empGridTbar = Ext.getCmp('empGridTbar'+variable);
    	empGridTbar.items.items[4].setText("取消关联");
    	empGridTbar.items.items[4].iconCls='icon-plugin-delete';//设置关联图标
    	
    	Ext.apply(me, {
//        	width:1000,
//		    height:500,
            //region: 'center', // a center region is ALWAYS required for border layout
            deferredRender: false,
            //bodyStyle: 'border-bottom: 1px solid #bec0c0 !important;',
            activeTab: 0,     // first tab initially active
            items: [ /*me.positionGridPanel,*/ me.empGridPanel, me.positionEditPanel],
//            region:'center',
            plain: true
//            layout:{
//                //align: 'stretch',
//                type: 'border'
//	        }
        });
    	
    	me.orgGridPanel.on('resize',function(p){
    		me.orgGridPanel.setHeight(FHD.getCenterPanelHeight() - 51);
    	});
    	
    /*	me.positionGridPanel.on('resize',function(p){
    		me.positionGridPanel.setHeight(FHD.getCenterPanelHeight() - 51);
    	});*/
    	
    	me.empGridPanel.on('resize',function(p){
    		me.empGridPanel.setHeight(FHD.getCenterPanelHeight() - 51);
    	});
    	
    	me.positionEditPanel.on('resize',function(p){
    		me.positionEditPanel.setHeight(FHD.getCenterPanelHeight() - 51);
    	});
    	
        me.callParent(arguments);
    }
});
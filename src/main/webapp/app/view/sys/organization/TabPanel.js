/**
 * 机构管理TAB面板
 * 
 * @author 金鹏祥
 */
Ext.define('FHD.view.sys.organization.TabPanel', {
    extend: 'Ext.tab.Panel',
    alias: 'widget.tabPanel',
    
    requires: [
           'FHD.view.sys.organization.org.OrgGridPanel',
           'FHD.view.sys.organization.emp.EmpGridPanel',
           'FHD.view.sys.organization.org.OrgEditPanel',
           'FHD.view.sys.organization.positiion.PositionGridPanel'
    ],
    listeners: {
	  	tabchange:function(tabPanel, newCard, oldCard, eOpts){
	  		var me = tabPanel;
	  		var treepanel = Ext.getCmp('treePanel');
	  		var cardid = newCard.id;
	  		if('tabpanelorgGridPanel'==cardid){	
	  			me.orgGridPanel.store.proxy.url = me.orgGridPanel.queryUrl;//动态赋给机构列表url
	  			me.orgGridPanel.store.proxy.extraParams.orgIds = treepanel.currentNode.data.id;
	  			me.orgGridPanel.store.load();
  			}else if('tabpanelempGridPanel'==cardid){
  				me.empGridPanel.store.proxy.url = me.empGridPanel.queryUrl;
  				me.empGridPanel.store.proxy.extraParams.orgIds = treepanel.currentNode.data.id;
  				me.empGridPanel.store.load();
	  		}else if('orgEditPanel'==cardid){
	  			if("addOrg"!=me.orgEditPanel.newFlag){//修改
	  				me.orgEditPanel.orgtreeId = treepanel.currentNode.data.id;
		  			me.orgEditPanel.load();
		  			me.orgEditPanel.isAdd=false;
	  			}else{//新增
	  				me.orgEditPanel.orgtreeId = treepanel.currentNode.data.id;//将节点id传给edit页面
	  				me.orgEditPanel.parentOrgLoad();//右键添加机构时，显示‘上级机构'
	  			}
	  		}else if('positionGridPanel'==cardid){
	  			me.positionGridPanel.store.proxy.url = me.positionGridPanel.queryUrl;
	  			me.positionGridPanel.store.proxy.extraParams.orgId = treepanel.currentNode.data.id;
	  			me.positionGridPanel.store.load();
	  		}
	  	}
    },
    
    
    // 初始化方法
    initComponent: function() {
    	var me = this;
    	
    	me.id = 'tabPanel';
    	variable = 'tabpanel';
    	//机构GRID
    	me.orgGridPanel = Ext.widget('orgGridPanel');
    	//人员GRID
    	me.empGridPanel = Ext.widget('empGridPanel');
//    	//机构基本信息
    	me.orgEditPanel = Ext.widget('orgEditPanel');
    	//岗位GRID
    	me.positionGridPanel = Ext.widget('positionGridPanel');
    	
    	Ext.apply(me, {
//        	width:1000,
//		    height:500,
            //region: 'center', // a center region is ALWAYS required for border layout
            deferredRender: false,
            //bodyStyle: 'border-bottom: 1px solid #bec0c0 !important;',
            activeTab: 0,     // first tab initially active
            items: [me.orgGridPanel, me.positionGridPanel, me.empGridPanel, me.orgEditPanel],//me.orgGridPanel, me.empGridPanel],
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
    	
    	me.positionGridPanel.on('resize',function(p){
    		me.positionGridPanel.setHeight(FHD.getCenterPanelHeight() - 51);
    	});
    	
    	me.empGridPanel.on('resize',function(p){
    		me.empGridPanel.setHeight(FHD.getCenterPanelHeight() - 51);
    	});
    	
    	me.orgEditPanel.on('resize',function(p){
    		me.orgEditPanel.setHeight(FHD.getCenterPanelHeight() - 51);
    	});
    	
        me.callParent(arguments);
    }
});
/**
 * 机构管理右面板
 * 
 * @author 金鹏祥
 */
Ext.define('FHD.view.sys.organization.RightPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.rightPanel',
    
    requires: [
			'FHD.view.sys.organization.CardPanel'
    ],
    
    // 初始化方法
    initComponent: function() {
    	var me = this;
    	me.id = 'rightPanel';
//    	var empEditPanel;
//    	//var tabPanel;//页签
//    	//var cardPanel;
//    	//var orgtreeId ;
//
//    	var empgridId;
//    	var loadable=true;//解决重复load冲突的问题
//    	
//    	me.gridqueryUrl = 'sys/organization/queryOrgPage.f';//查询所有机构
//    	me.empgridUrl = 'sys/organization/queryemppage.f';//员工查询列表Url
//    	var empaddUrl = 'pages/sys/orgstructure/empGridEdit.jsp';//新增、更新员工Url
//    	
//    	var delMsg=FHD.locale.get('fhd.common.makeSureDelete');	//删除提示信息
    	
    	//图片面板
    	me.cardPanel = Ext.widget('cardPanel');
    	//me.navigationBar = Ext.create('Ext.scripts.component.NavigationBars');
    	me.navigationBar = Ext.create('Ext.scripts.component.NavigationBars',{
        	type: 'org'
        });
    	
        Ext.apply(me, {
        	region:'center',
        	border:false,
        	layout:{
                align: 'stretch',
                type: 'vbox'
    		},
    		items:[
    		    {
	    			xtype:'box',
	    			height:20,
	    			style : 'border-left: 1px  #99bce8 solid;',
	    			html:'<div id="organizationNavDiv" class="navigation"></div>',
		        /* listeners : {
		            	afterrender: function(){
		            			//me.navigationBar.renderHtml('organizationNavDiv', '', '', 'org');
		            		var navigationBar = Ext.create('Ext.scripts.component.NavigationBars',{
					        	type: 'role'
					        	//,id : 'organizationNavDiv'
					        });
		            		navigationBar.renderHtml('organizationNavDiv', '', '', 'role');
		            	}
	            }*/
    		},me.cardPanel]
        });

        me.callParent(arguments);
    }
});
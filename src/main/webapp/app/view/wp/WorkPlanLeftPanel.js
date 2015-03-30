/**
 * 
 * 工作计划左侧功能树
 * 
 * @author 胡迪新
 */
Ext.define('FHD.view.wp.WorkPlanLeftPanel', {
    extend: 'FHD.ux.MenuPanel',
    alias: 'widget.workplanleftpanel',
    
    requires: [
    	'FHD.view.wp.WorkPlanForm',
    	'FHD.view.wp.WorkPlanList',
    	'FHD.view.wp.WorkPlanDashboard'
    ],
    
	
	
    // 初始化方法
    initComponent: function() {
        var me = this;
       
		
        Ext.applyIf(me, {
        	items:[{
		        text: '驾驶舱',
		        iconCls:'icon-btn-home',
		        scale: 'large',
				iconAlign: 'top',
				handler:function(){	
					var workplancenterpanel = me.up('panel').workplancenterpanel;
				    workplancenterpanel.removeAll(true);
				    workplancenterpanel.add(Ext.widget('workplandashboard'));
				}
		    },{
		        text: '计划管理',
		        iconCls:'icon-btn-plan',
		        scale: 'large',
				iconAlign: 'top',
				handler:function(){	
					var workplancenterpanel = me.up('panel').workplancenterpanel;
				    workplancenterpanel.removeAll(true);
				    workplancenterpanel.add(Ext.widget('workplanlist'));
				}
		    },{
		        text: '基础设置',
		        iconCls:'icon-btn-set',
		        scale: 'large',
				iconAlign: 'top',
				handler:function(){
					
				}
		    }]
        });

        me.callParent(arguments);
    }

});
/**
 * 
 * 风险整理树面板
 */

Ext.define('FHD.view.risk.assess.utils.AssessTree', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.assessTree',

    requires : ['FHD.view.risk.assess.utils.RiskTidyFoldTreeUtil',
                'FHD.view.risk.assess.utils.KpiTree',
                'FHD.ux.layout.AccordionTree',
                'FHD.view.risk.assess.utils.FlowTree',
                'FHD.view.risk.assess.utils.DepartmentTree'
                ],
    
    // 初始化方法
    initComponent: function() {
        var me = this;
        
        debugger;
        me.kpiTree = Ext.widget('kpiTree',{title : '目标', treeTitle : '目标'});
        me.flowTree = Ext.widget('flowTree', {title : '流程', treeTitle : '流程'})
        me.departmentTree = Ext.widget('departmentTree', {title : '部门', treeTitle : '部门'})
        
        me.accordionTree = Ext.widget("fhdaccordiontree",{
        	title: '树1',
            iconCls: 'icon-strategy',
            width : 240,
    		height : 520,
        	treeArr:[me.kpiTree, me.flowTree, me.departmentTree]
        });
        
        Ext.apply(me, {
			border:false,
    		split: true,
    		collapsible : true,
			region: 'west',
			items : [me.accordionTree]
        });

        me.callParent(arguments);
        
        me.on('resize',function(p){
        	me.accordionTree.setHeight(FHD.getCenterPanelHeight() - 28);
    	});
    }

});
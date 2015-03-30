Ext.define('FHD.demo.layout.AccordionTreeDemo', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.accordiontreedemo',
    
    /**
     * 初始化页面组件
     */
    initComponent: function () {
        var me = this;
        
        var queryUrl = __ctxPath + '/pages/demo/layout/tree.json';	//暂时不留作参数
        var tree1 = Ext.create('FHD.ux.TreePanel', {
        	treeTitle:'树1',
        	treeIconCls : 'icon-orgsub',
			url : queryUrl
		});
        var tree2 = Ext.create('FHD.ux.TreePanel', {
        	treeTitle:'树2',
        	treeIconCls : 'icon-org',
			url : queryUrl
		});
        var tree3 = Ext.create('FHD.ux.TreePanel', {
        	treeTitle:'树3',
        	treeIconCls : 'icon-orgsub',
			url : queryUrl
		});
        var tree4 = Ext.create('FHD.ux.TreePanel', {
        	treeTitle:'树4',
        	treeIconCls : 'icon-org',
			url : queryUrl
		});

        var accordionTree = Ext.create("FHD.ux.layout.AccordionTree",{
        	title: '树1',
            iconCls: 'icon-strategy',
        	treeArr:[tree1,tree2,tree3,tree4],
        	width:300,
        	height:500
        });
        
        Ext.applyIf(me, {
            autoScroll: true,
            border: false,
            bodyPadding: "5 5 5 5",
            items: [accordionTree]
        });
        
        me.callParent(arguments);
      
    }
});
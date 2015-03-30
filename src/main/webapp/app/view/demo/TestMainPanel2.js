Ext.define('FHD.view.demo.TestMainPanel', {
    extend: 'FHD.view.demo.TreePanel',// FHD.ux.TreePanel
    alias: 'widget.selectortreepanel',
   
    title: '树标题', 
    url:'',
    checkable:true,	// 是否有复选框
    chooseId:'',
    checked: false,
    myexpand:false,
    checkModel:'cascade', 
    initComponent: function() {
        var me = this;

        Ext.apply(me, {
        	autoScroll:true,
        	url: __ctxPath + '/demo/findFristtreeTree.f',// '/demo/findFristtreeTree.f' /app/view/demo/tree.json
    	    extraParams: {
	        	checkable: me.checkable,
	        	chooseId:me.chooseId
	        },
        	root: {
    	        text: "first部门树",
    	        id: "root",
    	        expanded: true
    	    }
        });
        me.callParent(arguments);
    }
});
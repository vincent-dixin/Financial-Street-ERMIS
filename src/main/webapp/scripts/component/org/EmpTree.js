
Ext.define('FHD.ux.org.EmpTree', {
    extend: 'FHD.ux.TreePanel',
    alias: 'widget.emptreepanel',
    title:$locale('emptreepanel.title'), 
    checkable:true,	// 是否有复选框
    chooseId:'',
    checked: false,
    myexpand:false,
    subCompany: false,// 是否显示子公司
    initComponent: function() {
        var me = this;
        if(__user.companyId == 'null') {
        	__user.companyId = 'root';
        	__user.companyName = $locale('depttreepanel.__user.companyName');
        }
        Ext.apply(me, {
        	autoScroll:true,
        	url: __ctxPath + '/sys/emp/cmp/treeloader',
        	extraParams: {
    	        	posiVisible: true,
    	        	empVisible: false,
    	        	subCompany: me.subCompany
    	    },
    	    root: {
    	        text: __user.companyName,
    	        id: __user.companyId,
    	        expanded: true
    	    }
        });
        me.callParent(arguments);
    }
});

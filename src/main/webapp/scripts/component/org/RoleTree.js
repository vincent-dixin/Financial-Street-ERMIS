
Ext.define('FHD.ux.org.RoleTree', {
    extend: 'FHD.ux.TreePanel',
    alias: 'widget.roletreepanel',
   
    title: $locale('depttreepanel.title'), 
    
    checkable:true,	// 是否有复选框
    chooseId:'',
    checked: false,
    myexpand:false,
    initComponent: function() {
        var me = this;
        if(__user.companyId == 'null') {
        	__user.companyId = 'root';
        	__user.companyName = $locale('depttreepanel.__user.companyName');
        }
        Ext.apply(me, {
        	autoScroll:true,
        	url: __ctxPath + '/sys/auth/role/treeloader',
    	    root: {
    	        text: __user.companyName,
    	        id: __user.companyId,
    	        expanded: true
    	    }
        });
        me.callParent(arguments);
        
    }
    
    

});

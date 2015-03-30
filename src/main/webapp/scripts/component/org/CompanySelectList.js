
Ext.define('FHD.ux.org.CompanySelectList', { 
	extend: 'FHD.ux.TreeCombox', 
	alias: 'widget.companyselectlist',
	
	
	fieldLabel:$locale('fhd.company'),
	
	initComponent:function(){
		var me = this;
		if(__user.companyId == 'null') {
        	__user.companyId = 'root';
        	__user.companyName = $locale('depttreepanel.__user.companyName');
        }
		
		var store = Ext.create('Ext.data.TreeStore', {
			fields : ['text', 'id', 'dbid','code','leaf','iconCls','cls'],
    	    proxy: {
    	    	url: __ctxPath + '/sys/org/cmp/companytreeloader.f',
    	        type: 'ajax',
    	        reader: {
    	            type: 'json'
    	        },
    	        extraParams: {}
    	    },
    	    root: {
    	        text: __user.companyName,
    	        id: __user.companyId,
    	        expanded: true
    	    }
    	});
		
		Ext.apply(me, {
			store:store,
			displayField:'text'
		});
		
		me.callParent(arguments);
		
	}
});
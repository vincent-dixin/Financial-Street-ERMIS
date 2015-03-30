
Ext.define('FHD.ux.fileupload.FileTypeTree', {
	extend: 'Ext.tree.Panel',
	alias: 'widget.FileTypeTree',
    canChecked:false,	// 是否有复选框
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
        	store: Ext.create('Ext.data.TreeStore', {
        	    proxy: {
        	    	url: __ctxPath + '/fileupload/typeTreeLoader',
        	        type: 'ajax',
        	        reader: {
        	            type: 'json'
        	        },
        	        extraParams: {
        	        	canChecked:me.canChecked
        	        }
        	    },
        	    root: {
        	        text: $locale('filetypetree.root'),
        	        id: null,
        	        canChecked:false
        	    },
        	    nodeParam:'id'
        	})
        });
        
        me.callParent(arguments);
        childNodes=me.getStore().getRootNode().expand();
    }
});
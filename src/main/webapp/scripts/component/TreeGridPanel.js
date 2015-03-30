/**
 * 
 */
Ext.define('FHD.ux.TreeGridPanel',{
	extend: 'Ext.tree.Panel',
	alias: 'widget.fhdtreegrid',
	
	url:'',
	extraParams:{},
	
	checked:true,
	searchable:true,
	searchParamName:'query',
	
	
	cols:[],
	tbarItems:[],
	
	
	initComponent:function(){
		var me = this;
		var fields = new Array();
		
		fields.push('id');
		for(i in me.cols) {
			fields.push(me.cols[i].dataIndex);
		}
		
		var cols = new Array();
		cols = cols.concat(me.cols);
		
		var store = Ext.create('Ext.data.TreeStore', {
			fields:fields,
    	    proxy: {
    	    	url: me.url,
    	        type: 'ajax',
    	        reader: {
    	            type: 'json'
    	        },
    	        extraParams: me.extraParams
    	    },
    	    autoLoad:false
    	})
		
		var toolbar = Ext.create('Ext.toolbar.Toolbar',{
			dock:'top'
		});
		Ext.applyIf(me,{
			columns:cols,
			store:store,
			dockedItems:[toolbar]
		});
		
		if(me.checked){
			Ext.applyIf(me,{
				selModel:Ext.create('Ext.selection.CheckboxModel')
			});
		}
		var searchField = Ext.create('Ext.ux.form.SearchField', {
				width : 150,
				paramName:me.searchParamName,
				store:store,
				emptyText : FHD.locale.get('searchField.emptyText')
		});
		toolbar.add(me.tbarItems);
		if(me.searchable){
			toolbar.add('->',searchField);
		}
		me.callParent(arguments);
	}
});
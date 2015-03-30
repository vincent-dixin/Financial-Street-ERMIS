
Ext.define('FHD.ux.layout.EditorGridPanel',{
	extend: 'FHD.ux.layout.GridPanel',
	alias: 'widget.fhdeditorgridpanel',
	clicksToEdit:Ext.emptyFn(),
	initComponent: function(){
		var me = this;
		
		var cellEditing = Ext.create('Ext.grid.plugin.CellEditing', {
	        clicksToEdit: me.clicksToEdit
	    });
		
		Ext.applyIf(me,{
			plugins: [cellEditing]
		})
		
		me.callParent(arguments);
	}
	
	
});

Ext.define('FHD.view.component.EditorGridPanel',{
	extend: 'FHD.view.component.GridPanel',
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
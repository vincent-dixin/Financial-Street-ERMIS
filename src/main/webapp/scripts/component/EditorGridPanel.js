
Ext.define('FHD.ux.EditorGridPanel',{
	extend: 'FHD.ux.GridPanel',
	alias: 'widget.fhdeditorgrid',
	clicksToEdit:1,
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
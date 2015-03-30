Ext.define('FHD.ux.org.DeptSelect', {
	extend : 'Ext.form.field.ComboBox',
	alias : 'widget.deptselect',
	
	fieldLabel : $locale('fhd.dept'),
	labelAlign : 'right',
	anchor : '100%',
	queryMode: 'local',
	valueField : 'id',
	displayField : 'name',
	checked:true,
	listConfig: {  
       getInnerTpl: function() {  
            var tmp = '<div data-qtip="{name}">{name}</div>';
            return tmp;
        }  
	},
	
	initComponent : function() {
		var me = this;
		me.callParent(arguments);
		me.bindStore(Ext.create('Ext.data.Store', {
		    fields: [
		         {name: 'id', type: 'string'},
		         {name: 'name',  type: 'string'}
		    ],
			proxy : {
				type : 'ajax',
				url : __ctxPath + '/sys/org/cmp/deptListByCompanyId.f',
				reader : {
					type : 'json',
					root : 'deptList'
				}
			},    
			autoLoad : true
		}), true);		
		me.select(me.defaultValue);
	}
});
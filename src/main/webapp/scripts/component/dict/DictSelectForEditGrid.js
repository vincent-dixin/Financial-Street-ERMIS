/**
 * @author 胡迪新
 * @class FHD.ux.dict.DictSelect
 * @extends DictSelectForEditGrid
 * 
 * 数据字典下拉列表控件
 * 
 */

Ext.define('FHD.ux.dict.DictSelectForEditGrid', {
	extend : 'Ext.form.field.ComboBox',
	alias : 'widget.dictselectforeditgrid',
	fieldLabel : $locale('dictcheckbox.fieldLabel'),
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
	       } ,
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
				url : __ctxPath + '/sys/dic/findDictEntryByTypeId.f?typeId=' + me.dictTypeId,
				reader : {
					type : 'json',
					root : 'dictEnties'
				}
			},    
			autoLoad : true
		}), true);		
		me.select(me.defaultValue);
	}

});
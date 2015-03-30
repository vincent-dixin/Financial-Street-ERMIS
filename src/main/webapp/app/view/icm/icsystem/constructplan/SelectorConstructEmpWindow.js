Ext.define('FHD.view.icm.icsystem.constructplan.SelectorConstructEmpWindow', {
	extend : 'Ext.window.Window',
	alias : 'widget.selectorconstructempwindow',
	constrain : true,
	height : 200,
	width : 400,
	modal : true,
	//collapsible : true,
	maximizable : true,
	bodyStyle: 'background:#fffff; padding:10px',
	layout : {
		type : 'column'
	},
	title :'选择建设责任人',
	initComponent : function() {
		var me = this;
		me.empSelStore=Ext.create('Ext.data.Store',{
			fields : ['id', 'name'],
			proxy : {
				type : 'ajax',
				url : __ctxPath + '/icm/icsystem/findallempbyconstructplanid.f'
			}
		});
		me.empSelStore.proxy.extraParams.constructPlanId = me.constructPlanId;
		me.empSelStore.load();
		me.combobox = Ext.create('Ext.form.ComboBox',{
		    fieldLabel: '选择人员',
			labelWidth : 120,
			store :me.empSelStore,
			emptyText:'请选择',
			valueField : 'id',
			margin: '7 10 0 30',
			columnWidth : 1,
			name:'isDesirableAdjust',
			displayField : 'name',
			selectOnTab: true,
			lazyRender: true,
			typeAhead: true,
			editable : false});
		me.buttons = [{
					xtype : 'button',
					text : $locale('fhd.common.confirm'),
					handler : function() {
						me.onSubmit(me.combobox.getValue());
						me.close();
					}
				}, {
					xtype : 'button',
					text : $locale('fhd.common.close'),
					style : {
						marginLeft : '10px'
					},
					handler : function() {
						me.close();
					}
				}];

		Ext.applyIf(me, {
					items : [me.combobox, me.buttons]
				});
		me.callParent(arguments);
	}

});
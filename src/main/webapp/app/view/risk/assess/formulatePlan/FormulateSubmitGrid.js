
Ext.define('FHD.view.risk.assess.formulatePlan.FormulateSubmitGrid',{
	extend:'FHD.ux.EditorGridPanel',
	alias: 'widget.formulatesubmitgrid',
	requires: [
       
    ],
	url: __ctxPath + '/access/formulateplan/queryscoredeptandempgrid.f',
	
	pagable:false,
    searchable:false,
    rowNumberer:false,
    autoLoad:false,
	//可编辑列表为只读属性
	readOnly : false,
	border: false,
	region:'north',
	
	initComponent:function(){
		var me=this;
		me.cols=[
//		    {header: 'id',  dataIndex: 'id', hidden: true},
        	{header: '部门名称', dataIndex: 'deptName', sortable : true, flex: 1 },
        	{header:'承办人<font color=red>*</font>',dataIndex:'empId',flex:1,hidden : true,width:0},
        	{header:'承办人<font color=red>*</font>',dataIndex:'empName',flex:1,
				editor:Ext.create('Ext.form.field.ComboBox',{
					store :Ext.create('Ext.data.Store',{
						autoLoad : false,
						fields : ['id', 'name'],
						proxy : {
							type : 'ajax',
							url : __ctxPath + '/access/formulateplan/findempsbydeptids.f'
						}
					}),
					valueField : 'id',
					displayField : 'name',
					allowBlank : false,
					editable : false,
					listeners:{
							expand:function(){
									var selection = me.getSelectionModel().getSelection();
									var length = selection.length;
									if (length > 0) {
										var deptId = selection[0].get('id');
										this.store.load({params:{deptId: deptId}});
									}
							}
					}}),
					renderer:function(value,metaData,record,rowIndex ,colIndex,store,view){
						var v = this.columns[3].getEditor(record).store.findRecord('id',value);
						if(v){
							record.data.empId = v.data.id;
							return v.data.name;
						}
						return value;
					}
			}
		];
		me.callParent(arguments);
	}
	
});
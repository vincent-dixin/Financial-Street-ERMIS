/**
 * 按风险分配列表
 */
Ext.define('FHD.view.risk.assess.kpiSet.KpiSetRiskTaskGrid',{
	extend:'FHD.ux.EditorGridPanel',
	alias: 'widget.kpisetrisktaskgrid',
	requires: [
       
    ],
	pagable:false,
    searchable:false,
    rowNumberer:false,
    autoLoad:false,
	//可编辑列表为只读属性
	readOnly : false,
	border: false,
	url: __ctxPath + '/access/kpiSet/queryassesstaskbyriskid.f',
	
	initComponent:function(){
		var me=this;
		me.empStore=Ext.create('Ext.data.Store',{
			fields : ['id', 'name'],
						proxy : {
							type : 'ajax',
							url : __ctxPath + '/access/kpiSet/findempsbyuserdeptId.f'
						}
		});
		
		me.cols=[
		    {header: 'riskId',  dataIndex: 'riskId', hidden: true},
        	{header: '风险名称', dataIndex: 'parRiskName', sortable : true, flex: 1 },
        	//{header:'承办人<font color=red>*</font>',dataIndex:'empId',flex:1,hidden : true,width:0},
        	{header:'评估人<font color=red>*</font>',dataIndex:'empId',flex:1,emptyCellText:'<font color="#808080">请选择</font>',
				editor:Ext.create('Ext.form.field.ComboBox',{
					checkField: 'checked',//多选
    				//multiSelect : true,
    				separator: ',',
					store :me.empStore,
					valueField : 'id',
					displayField : 'name',
					allowBlank : false,
					editable : false
					}),
					renderer:function(value,metaData,record,rowIndex ,colIndex,store,view){
						metaData.tdAttr = 'style="background-color:#FFFBE6"';
						var index = me.empStore.find('id',value);
						var record = me.empStore.getAt(index);
						if(record){
							return record.data.name;
						}else{
							return '';
						}
					}
			}
		];
		me.callParent(arguments);
	}
	
});
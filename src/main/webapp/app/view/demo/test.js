var region = Ext.create('FHD.ux.listselector.ListSelector',{
   	title:'选择员工',
   	noteField:'userid',
	columns: [{dataIndex: 'userid',header: '员工编号'},{dataIndex: 'realname',header: '姓名',isPrimaryName:true}],
	entityName:'com.fhd.sys.entity.orgstructure.SysEmployee',
	foreignKey:'sysOrganization.id',
	queryKey:'realname',
   	fieldLabel : '员工',
   	labelAlign: 'left',
    multiSelect : true,
    value:'[{id:"chenjie"},{id:"hanwei"}]',
    name: 'emp'
});

var tbar =['->',{text : "设置值",handler:setData, scope : me},'-',{text : "得到值",handler:save, scope : me}];
function setData(){
	region.clearValues();
	region.setHiddenValue([{id:"chenjie"}]);
	region.initValue();
}
function save(){
	alert(region.getValue());
}

var plan = Ext.create('FHD.ux.icm.rectify.ImproveSelector',{
	name:'rectifyplanId',
	fieldLabel : '整改计划'
})
var formPanal = Ext.create("Ext.form.Panel", {
	renderTo:Ext.getBody(),
    autoScroll: true,
    border: false,
    bodyPadding: "5 5 5 5",
    tbar:tbar,
    items: [{
        xtype: 'fieldset',//基本信息fieldset
        collapsible: true,
        defaults: {
        	margin: '7 30 3 30',
        	columnWidth:.5
        },
        layout: {
            type: 'column'
        },
        title: "基础信息",
        items:[plan]
    }]
});
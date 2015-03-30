<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>树选择布局组件</title>
<script type="text/javascript">
/***attribute start***/

/***attribute end***/

/***Ext.onReady start***/
Ext.onReady(function(){
	//树选择布局表单
	var emp = Ext.create('FHD.ux.listselector.ListSelector',{
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
	
	//表单panel
	var form = Ext.create("Ext.form.Panel",{
		renderTo: 'treedemo',
		autoScroll: true,
        border: false,
        bodyPadding: "5 5 5 5",
        //tbar:tbar,
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
            items:[emp]
        }]
	});
});

/***Ext.onReady end***/

</script>
</head>
<body>
	<div id='treedemo'></div>
</body>
</html>
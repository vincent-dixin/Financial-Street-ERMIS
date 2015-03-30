<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>树选择布局组件</title>
<script type="text/javascript">
/***attribute start***/
var treeUrl = '/pages/demo/treeselector/data.jsp?fun=treeLoader';//树查询url
var initUrl = '/pages/demo/treeselector/data.jsp?fun=findByIds'; //初始化url
/***attribute end***/

/***Ext.onReady start***/
Ext.onReady(function(){
	//树选择布局表单
	/*
	var region = Ext.create('FHD.ux.treeselector.TreeSelector',{
    	title:'请您选择地区',
    	noteField:'title',
		columns: [{dataIndex: 'title',header: '拼音'},{dataIndex: 'name',header: '名称',isPrimaryName:true}],
		treeUrl: treeUrl,
		initUrl: initUrl,
    	fieldLabel : '地区',
    	labelAlign: 'left',
        multiSelect : true,
        value:'[{id:2}]',
        name: 'deptFirst'
    });*/
	var region = Ext.create('FHD.ux.treeselector.TreeSelector',{
		title:'请您选择部门',
    	noteField:'orgcode',
		columns: [{dataIndex: 'orgcode',header: '部门编号'},{dataIndex: 'orgname',header: '部门名称',isPrimaryName:true}],
		entityName:'com.fhd.sys.entity.orgstructure.SysOrganization',
		parentKey:'parentOrg.id',
		relationKey:'orgseq',
		value:'[{id:"XD00"},{id:"eda8ffeab0da4159be0ff924108e3883"}]',
    	fieldLabel : '部门',
    	labelAlign: 'left',
        multiSelect : true,
        checkable:true,
        name: 'deptFirst'
    });
	
	//表单panel
	var form = Ext.create("Ext.form.Panel",{
		renderTo: 'treedemo',
		autoScroll: true,
        border: false,
        bodyPadding: "5 5 5 5",
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
            items:[region]
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
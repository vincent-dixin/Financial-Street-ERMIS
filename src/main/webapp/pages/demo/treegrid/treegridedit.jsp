<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>树treegrid列表</title>
<script type="text/javascript">
/***attribute start***/
var param = formwindow.initialConfig;
var formButtons = [//表单按钮
	{text: FHD.locale.get('fhd.common.save'),handler:save},
    {text: FHD.locale.get('fhd.common.cancel'),handler:cancel}
];
/***attribute end***/
/***function start***/
function save(){//保存方法
    var form = formPanel.getForm();
    if (form.isValid()) {
		if (isAdd) {//新增
			FHD.submit({form:form,url:addUrl,callback:function(data){treegrid.store.load()}});//参数依次为form，url，store
		} else {//更新
			FHD.submit({form:form,url:updateUrl,callback:function(data){treegrid.store.load()}});
		}
		formwindow.close();
	}
    
}
function cancel(){//取消方法
	formwindow.close();
}
/***function end***/
/***form start***/ 
var formColums = [//form表单的列
   	{xtype : 'hidden',name : 'id', id:'id'}
   	,{xtype : 'treecombox',
   		name : 'parentId',
   		fieldLabel : FHD.locale.get('fhd.pages.test.field.parentName'),
   		valueField : 'id',
   		displayField : 'text',
   		vtype:'treeNode',
   		maxPickerHeight : 200,
   		maxPickerWidth : 250,
   		store : treeComboxStore}
   	,{xtype : 'textfield',
   		fieldLabel : FHD.locale.get('fhd.pages.test.field.title')+'<font color=red>*</font>',
   		name : 'title', vtype:'uniqueTitle',
   		allowBlank : false}
   	, {xtype : 'textfield',
   		fieldLabel : FHD.locale.get('fhd.pages.test.field.name')+'<font color=red>*</font>',
   		name : 'name',
   		allowBlank : false}
   	, {xtype : 'numberfield',
   		fieldLabel : 'NUMBER',
   		name : 'num',  
   		maxValue: 92,  
   		minValue: 0,  
   		allowDecimals: true, // 允许小数点 
   		nanText:'请输入数字',  
   		step:0.5,  
   		allowBlank : true}
   	,{xtype : 'combobox',
   		fieldLabel : FHD.locale.get('fhd.pages.test.field.myLocale')+'<font color=red>*</font>',
   		store : fieldstore,
   		displayField : 'name',
   		valueField : 'id',
   		name: 'myLocale',
   		allowBlank : false,
   		editable : false
   	}
];
var formPanel = Ext.create('Ext.form.Panel',{
	bodyPadding: 5,
	renderTo:'demos',
	layout:'fit',
	items: [
		{xtype: 'fieldset',
			defaults: {columnWidth: 1/1},//每行显示一列，可设置多列
			layout: {type: 'column'},
			bodyPadding: 5,
			collapsed: false,
			collapsible: false,
			title: FHD.locale.get('fhd.common.baseInfo'),
			items: formColums}
	      ],
	buttons:formButtons
});
/***form end***/ 
/***CustomValidator start***/
Ext.apply(Ext.form.field.VTypes, {
    //验证title不重复
    uniqueTitle:  function(val,field) {
    	var id = field.up("form").down('#id');
        var flag=false;
        FHD.ajax({
        	url: 'test/checkTitle.f',
            async:false,//这一项必须加上，否则后台返回true,flag的值也为false
            params : {
					title:val,
					id:id.value
				},
			callback : function(data){
				if(data){
					flag = true;
				}
			}
        })
        return flag;
    },
    uniqueTitleText: FHD.locale.get('fhd.pages.test.uniqueMsg'),
    //验证parentId的合法性
    treeNode: function(val,field){
    	var parentId = field.up("form").down('[name=parentId]').value;
    	var id = field.up("form").down('#id').value;
    	var flag=false;
    	if(parentId==null || parentId == ''){
    		flag = true;
    	}else if(parentId==id){
    		flag = false;
    	}else{
    		FHD.ajax({
	        	url: 'test/checkParent.f',
	            async:false,//这一项必须加上，否则后台返回true,flag的值也为false
	            params : {
						parentId:parentId,
						id:id
					},
				callback : function(data){
					if(data){
						flag = true;
					}
				}
	        })
    	}
        return flag;
    },
    treeNodeText: FHD.locale.get('fhd.pages.test.treeNodeMsg')
});
/***CustomValidator end***/

formwindow.on('resize',function(me){
	formPanel.setWidth(me.getWidth()-10);
	
})
if(typeof(param.id) != "undefined") {
	formPanel.form.load( {
        url : 'test/getTestMvc.f',
        params: {id: param.id},
        failure : function(form,action) {
            MessageBox.error('页面初始化错误');
        }
    });
}
</script>
</head>
<body>
	<div id='demos'></div>
</body>
</html>
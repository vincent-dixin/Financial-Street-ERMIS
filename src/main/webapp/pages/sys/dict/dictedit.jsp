<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>树grid列表</title>
<script type="text/javascript">
/***attribute start***/
var i18delUrl='sys/dict/removeDictEntryI18.f';
var i18saveUrl='sys/dict/mergeDictEntryI18.f?entryId='+curEntryId;

var i18QueryUrl='';
var updatei18url;
var param = formwindow.initialConfig;

var formButtons = [//表单按钮
	{text: FHD.locale.get('fhd.common.save'),handler:save},
    {text: FHD.locale.get('fhd.common.cancel'),handler:cancel}
];
var formColums2;
//国际化编辑列表
var i18list;
var formPanel;
/***attribute end***/
/***function start***/
function save(){//保存方法
	debugger;
    var form = formPanel.getForm();
    if (form.isValid() && validI18(i18list.store.getModifiedRecords())) {
		if (isAdd) {//新增
			FHD.submit({form:form,url:addUrl,callback:function(data){
				debugger;
				curEntryId=Ext.getCmp('newnum').value;i18saveUrl='sys/dict/mergeDictEntryI18.f?entryId='+curEntryId;
				i18save();
				//mgrid.store.load();
				}});//参数依次为form，url，callback
		} else {//更新
			FHD.submit({form:form,url:updateUrl,callback:function(data){
				i18save();
				//mgrid.store.load()
			}});//参数依次为form，url，callback
		}
		
		formwindow.close();
		dictTreeComboxStore.load();
	}
    
}

function validI18(rows)
{
var result=true;
	Ext.each(rows,function(item){
		if(item.data.myLocale=='')
		{
			result= false;
		}
		
		if(item.data.name=='')
		{
			
			result = false;
		}else
		{
			if( item.data.name.length>255)
			{
				result=false;
			}
		}
	});
	if(!result)
	{
	alert(FHD.locale.get('fhd.sys.dic.i18err'));
	}
	return result;
}
function cancel(){//取消方法
	formwindow.close();
}
/***function end***/
/***form start***/ 
var formColums = [//form表单的列
   	{xtype : 'hidden',hidden:true,name : 'id', id:'id'},
   	{xtype : 'hidden',hidden:true,name : 'dictTypeId',value:curTypeId},
   	{xtype : 'treecombox',
   		name : 'parent',
   		fieldLabel : FHD.locale.get('fhd.sys.dic.parent'),
   		valueField : 'id',
   		displayField : 'text', 		
   		maxPickerHeight : 200,
   		maxPickerWidth : 250,  		
   		store : dictTreeComboxStore}
   	,{xtype : 'textfield',
   		fieldLabel : FHD.locale.get('fhd.sys.dic.num')+'<font color=red>*</font>',
   		name : 'num',id:'newnum', vtype:'uniqueTitle',maxLength:100,readOnly:!isAdd,
   		allowBlank : false}
   	, {xtype : 'textfield',
   		fieldLabel : FHD.locale.get('fhd.sys.dic.name')+'<font color=red>*</font>',
   		name : 'name',maxLength:255,
   		allowBlank : false},
   	{xtype : 'textfield',
   		fieldLabel : FHD.locale.get('fhd.sys.dic.value'),
   		name : 'value',  maxLength:255,
   		allowBlank : true},
   	{xtype : 'textfield',
   		fieldLabel : FHD.locale.get('fhd.sys.dic.desc'),
   		name : 'edesc',  
   		allowBlank : true}
  
];
//国际化store
 Ext.define('Locale', {
     extend: 'Ext.data.Model',
     fields: [
         {name: 'id', type: 'string'},
         {name: 'name',  type: 'string'}
     ]
 });

//国际化列
var i18cols=[
             //{dataIndex:'id',id:'id',width:80},
{dataIndex:'myLocale',checked:false,header:FHD.locale.get('fhd.sys.dic.myLocale'),width:100,editor:
				new FHD.ux.dict.DictSelectForEditGrid({
				    dictTypeId:'0locale',fieldLabel:''
				}),
				renderer:function(value, metadata, record, rowIndex, colIndex, store){
  
				    var curModel = i18store.findRecord("id",value);
				    if(curModel!=null)
					{
					return curModel.raw.name;
					}
				}},
{dataIndex:'name',header:FHD.locale.get('fhd.sys.dic.name'),width:100,flex:1,editor: {allowBlank: false,maxLength:255}}
];

//国际化对象
Ext.define('I18Info', {//定义model
	    extend: 'Ext.data.Model',
	   	fields: [{name: 'myLocale', type: 'string'},
		        {name: 'name', type: 'string'}]
	});
//国际化顶部按钮
var tbarItemsList=[
			{text : FHD.locale.get('fhd.common.add'),iconCls: 'icon-add',handler:i18add, scope : this},{xtype : 'tbspacer'}
			,{text : FHD.locale.get('fhd.common.delete'),iconCls: 'icon-del',id:'i18del${param._dc}', handler:i18del, disabled : true, scope : this}  
		]
/***form end***/ 

Ext.onReady(function(){

 if(!isAdd)
 {
 updatei18url='sys/dic/findDictEntryI18ByEntryId.f?entryId='+curEntryId;
 
 }else
 {
     
 updatei18url='';
 }
 
i18list=Ext.create('FHD.ux.EditorGridPanel',
{
url:updatei18url,
searchable:false,
cols:i18cols,
height:180,
pagable:false,
tbarItems:tbarItemsList
});
 

i18list.store.on('load',i18onchange);//执行store.load()时改变按钮可用状态
i18list.on('selectionchange',i18onchange);//选择记录发生改变时改变按钮可用状态
i18list.store.on('update',i18onchange);//修改时改变按钮可用状态
	
formColums2=[i18list];

formPanel = Ext.create('Ext.form.Panel',{
	renderTo:'dictedit${param._dc}',
	bodyPadding: 5,
	items: [ 
		{xtype: 'fieldset',
			flex:1,
			defaults: {
				columnWidth: 1/1,
				labelWidth:46,
				margin: '3 3 3 3'},//每行显示一列，可设置多列
			layout: {type: 'column'},
			title: FHD.locale.get('fhd.common.baseInfo'),
			items: formColums}
	       ,

		{xtype: 'fieldset',
	    	   flex:1,
			collapsed: false,
			overflow:'auto',
			collapsible: false,
			title: FHD.locale.get('fhd.sys.dic.i18info'),
			items: formColums2}
	      ],
	     
	buttons:formButtons
	});
formwindow.on('resize',function(me){
	formPanel.setWidth(me.getWidth()-10);
	formPanel.setHeight(me.getHeight()-40);
})
});
/***CustomValidator start***/
Ext.apply(Ext.form.field.VTypes, {
    //验证title不重复
    uniqueTitle:  function(val,field) {
        var flag=false;
        
        FHD.ajax({
        	url: 'sys/dic/checkDictEntryByNum.f',
            async:false,//这一项必须加上，否则后台返回true,flag的值也为false
            params : {
					num:val,
					orgnum:curEntryId,
					mode:isAdd
				},
			callback : function(data){
				if(data){
					flag = true;
				}
			}
        })
        return flag;
    },
    treeNodeText: FHD.locale.get('fhd.pages.test.treeNodeMsg')
});
/***CustomValidator end***/

if(typeof(param.id) != "undefined") {
	formPanel.form.load( {
        url : 'sys/dic/findDictEntryByNum.f',
        params: {num: param.id},
        failure : function(form,action) {
            alert("err 155");
        }
    });
}

//国际化部分的function
function i18add(){//新增方法
	var r = Ext.create('I18Info');
    i18list.store.insert(0, r);
    i18list.editingPlugin.startEditByPosition({row:0,column:0}); 
} 

function i18onchange(){//设置你按钮可用状态
	i18list.down('#i18del${param._dc}').setDisabled(i18list.getSelectionModel().getSelection().length === 0);
}

function i18save(){//保存方法

	var rows = i18list.store.getModifiedRecords();
	var jsonArray=[];
	Ext.each(rows,function(item){
		jsonArray.push(item.data);
	});
	FHD.ajax({
		url : i18saveUrl,
		params : {
			modifiedRecord:Ext.encode(jsonArray)
		},
		callback : function(data){
			if(data){
				debugger;
				//Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
				dictGrid.store.load();
			}
		}
	})
	//i18list.store.commitChanges(); 
}
function i18del(){//删除方法
	var selection = i18list.getSelectionModel().getSelection();
	Ext.MessageBox.show({
		title : FHD.locale.get('fhd.common.delete'),
		width : 260,
		msg : FHD.locale.get('fhd.common.makeSureDelete'),
		buttons : Ext.MessageBox.YESNO,
		icon : Ext.MessageBox.QUESTION,
		fn : function(btn) {
			if (btn == 'yes') {
				var ids = [];
				for(var i=0;i<selection.length;i++){
					ids.push(selection[i].get('id'));
				}
				FHD.ajax({
					url : i18delUrl,
					params : {
						ids:ids.join(',')
					},
					callback : function(data){
						if(data){
							Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
							
							i18list.store.load();
							
						}
					}
				});
			}
		}
	});
}

</script>
</head>
<body>
	<div id='dictedit${param._dc}'></div>
</body>
</html>
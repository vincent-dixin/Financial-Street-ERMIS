<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>字典管理</title>
<%
String typeId="";
if(request.getParameter("typeId")!=null)
{
typeId=request.getParameter("typeId");
}
%>
<script type="text/javascript">
/***attribute start***/
var typeId='${param.typeId}';
var mainPanel,dictGrid;
var curTypeId;//当前选中字典项行ID
var curEntryIsSystem;//当前选择字典项是否为系统字典
var curEntryId;
var addUrl = 'sys/dic/saveDictEntry.f';//新增保存url
var updateUrl = 'sys/dic//mergeDictEntry.f';//更新保存url
var i18DictEntryUrl='sys/dic/findDictEntryBySome.f?typeId=0locale';
var loadable=true;//解决重复load冲突的问题
var dictTree, queryUrl = 'sys/dic/findDictTypeBySome.f';
var delUrl='sys/dic/removeDictEntry.f';
var gridqueryUrl = 'sys/dic/findDictEntryBySome.f';
var i18store;
var delMsg=FHD.locale.get('fhd.common.makeSureDelete');

var gridColums =[
	{
		hidden:true,
		text: 'ID',
	    dataIndex: 'id'
	},
	{
		hidden:true,
		text: 'sys',
	    dataIndex: 'sys'
	},
		{
		hidden:true,
	    dataIndex: 'linked'
	},
	
	{
	    xtype: 'treecolumn', //this is so we know which column will show the dictTree
	    text: FHD.locale.get('fhd.pages.test.field.name'),
	    flex: 2,
	    dataIndex: 'name',
	    sortable: true,
	    renderer:function(value,metaData,record,colIndex,store,view) { 
			//
			var issys=record.get('sys');
			metaData.tdAttr = 'data-qtip="'+value+'"';  
		    if(issys=="1")
		{
			return "<span style='color:#C0C0C0' class=''>"+value+"</span><span style='color:red'>*</span>";
		}else
		{
			return "<span style='' class=''>"+value+"</span>";
		} 
		}
	},
	{
	    text: FHD.locale.get('fhd.sys.dic.num'),
	    flex: 1,
	    dataIndex: 'num',
	    sortable: true,
	    renderer:function(value,metaData,record,colIndex,store,view) { 
		metaData.tdAttr = 'data-qtip="'+ value +'"';  
		var issys=record.get('sys');
		var num=record.get('num');
		
		if(issys=="1")
		{
			return "<div style='color:#C0C0C0' class=''>"+num+"</div>";
		}else
		{
			return "<div style='' class=''>"+num+"</div>";
		}
	      
		}
	},
		{
	    text: FHD.locale.get('fhd.sys.dic.value'),
	    flex: 1,
	    dataIndex: 'value',
	    sortable: true,
	     renderer:function(value,metaData,record,colIndex,store,view) {
	    	 if(value==null)value="";
		metaData.tdAttr = 'data-qtip="'+value+'"';  
		var issys=record.get('sys');
		
		if(issys=="1")
		{
			return "<div style='color:#C0C0C0' class=''>"+value+"</div>";
		}else
		{
			return value;
		}
	      
		}
	}
	
];

var tbar =[//菜单项
			{text : FHD.locale.get('fhd.common.add'),iconCls: 'icon-add',id:'dictadd${param._dc}',handler:edit, scope : this,disabled : true},'-'
			,{text : FHD.locale.get('fhd.common.edit'),iconCls: 'icon-edit',id:'dictedit${param._dc}', handler:edit, disabled : true, scope : this} ,'-'
			,{text : FHD.locale.get('fhd.common.delete'),iconCls: 'icon-del',id:'dictdel${param._dc}', handler:del, disabled : true, scope : this}  
		];
var dictTreeComboxStore = Ext.create('Ext.data.TreeStore', {//dictTreeCombox的store
	//fields : ['text', 'id'],
	root : {text : FHD.locale.get('fhd.common.select'),id:'',expanded : true},
	proxy : {type : 'ajax',url : 'sys/dic/findDictEntryBySome.f'/*加载dictTree的url*/},
	autoload:false
});		
/***attribute end***/

/***function start***/
function edit(button){//新增方法
	var selection = dictGrid.getSelectionModel().getSelection()[0];//得到选中的记录
	if(button.id=='dictadd${param._dc}'){
	    dictTreeComboxStore.proxy.url=dictGrid.store.proxy.url+'?typeId='+curTypeId;
	}else
	    {
	    dictTreeComboxStore.proxy.url=dictGrid.store.proxy.url+'?typeId='+curTypeId+'&entryId='+curEntryId;
	    }
	
	//dictTreeComboxStore.proxy=dictGrid.store.proxy;
	dictTreeComboxStore.load();
	formwindow = new Ext.Window({
		iconCls: 'icon-edit',//标题前的图片
		modal:true,//是否模态窗口
		collapsible:true,
		scroll:'auto',
		width:450,
		height:520,
		maximizable:true,//（是否增加最大化，默认没有）
		autoLoad:{ url: 'pages/sys/dict/dictedit.jsp',nocache:true,scripts:true}
	});
	formwindow.show();
	if(button.id=='dictadd${param._dc}'){
		formwindow.setTitle(FHD.locale.get('fhd.common.add'));
		isAdd = true;
	}else{
		formwindow.setTitle(FHD.locale.get('fhd.common.modify'));
		formwindow.initialConfig.id = selection.get('id');
		isAdd = false;
	}
} 

function del(){//删除方法
	var selection = dictGrid.getSelectionModel().getSelection();//得到选中的记录
	for(var i=0;i<selection.length;i++){
	
				if(selection[i].get('linked')!='')
				{
				delMsg=FHD.locale.get('fhd.sys.dic.linked');
				}
		}
	Ext.MessageBox.show({
		title : FHD.locale.get('fhd.common.delete'),
		width : 260,
		msg : delMsg,
		buttons : Ext.MessageBox.YESNO,
		icon : Ext.MessageBox.QUESTION,
		fn : function(btn) {
			if (btn == 'yes') {//确认删除
				var ids = [];
				for(var i=0;i<selection.length;i++){
					ids.push(selection[i].get('id'));
				}
				FHD.ajax({//ajax调用
					url : delUrl,
					params : {
						ids:ids.join(',')
					},
					callback : function(data){
						if(data){//删除成功！
							Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
							dictTreeComboxStore.load();//重新加载dictTreeCombox
							dictGrid.store.load();
						}
					}
				});
			}
		}
	});
}
function setstatus(){//设置你按钮可用状态
	var length = dictGrid.getSelectionModel().getSelection().length;
	var dictTreeLength = dictTree.getSelectionModel().getSelection().length;
	

	dictGrid.down('#dictedit${param._dc}').setDisabled(length == 0 || length>1 || curEntryIsSystem==1);
	dictGrid.down('#dictdel${param._dc}').setDisabled(length ==0 ||curEntryIsSystem==1);
	dictGrid.down('#dictadd${param._dc}').setDisabled(dictTreeLength==0);
	
	if(typeId!='')
	    {
	    dictGrid.down('#dictadd${param._dc}').setDisabled(false);
	    }
}
//解决重复load不同步的问题
function doLoad(records, operation, success)
{
	loadable=true;
}
/***function end***/


/***Ext.onReady start***/
Ext.onReady(function(){
    
    i18store=Ext.create('Ext.data.Store', {//dictTreeCombox的store
    fields : ['id', 'name'],
	proxy : {type : 'ajax',url : i18DictEntryUrl,reader:{type:'json',root:'children'}},
	autoload:false
});
    
i18store.load();

	dictTree = Ext.create('FHD.ux.TreePanel',{//实例化一个grid列表
        rootVisible: false,
        width:220,
        maxWidth:350,
        split: true,
        collapsible : true,
        border:true,
        region: 'west',
        multiSelect: true,
        rowLines:false,
        singleExpand: false,
        checked: false,
		url: queryUrl,//调用后台url
		height:FHD.getCenterPanelHeight()-5,
		listeners : {
 		 'itemclick' : function(view,re){
 		 curTypeId=re.data.id;
 		 dictGrid.getSelectionModel().deselectAll();
 		 dictGrid.store.proxy.extraParams=[{'typeId':re.data.id}];
  		 dictGrid.store.proxy.url=gridqueryUrl;
  		 dictGrid.store.on('beforeload', function (store, options) {
    	    var new_params = { typeId: re.data.id };
    	    Ext.apply(store.proxy.extraParams,null);
	        Ext.apply(store.proxy.extraParams, new_params);
    	});
  		dictGrid.down('searchfield').url=gridqueryUrl;
  		dictGrid.down('searchfield').setValue("");
  		dictGrid.down('searchfield').triggerCell.item(0).setDisplayed(false);
  		 if(loadable)
			 {
				 dictGrid.store.load({callback:doLoad});
				 setstatus();
				 loadable=false;
			 }
  		 dictGrid.on('afterload',function(){
  		   loadable=true;
  		 })
  		}
 		}
 		
 		
	});

	 dictGrid=Ext.create('FHD.ux.TreeGridPanel',{//实例化一个grid列表
        useArrows: true,
        rootVisible: false,
        multiSelect: true,
        border:true,
        rowLines:true,
        singleExpand: false,
        checked: false,
        autoScroll:true,
        width:FHD.getCenterPanelWidth()-225,
		height:FHD.getCenterPanelHeight()-3,//高度为：获取center-panel的高度
		cols:gridColums,//cols:为需要显示的列
		tbarItems:tbar,
		listeners:{
		'itemclick':function(view,record,item,index,e){
				curEntryId=record.raw.id;
				curEntryIsSystem=record.raw.sys;
				setstatus();
			}
		}
	});
	dictGrid.store.remoteSort=true;
	mainPanel = Ext.create('Ext.container.Container',{
	    renderTo: 'dictDiv${param._dc}', 
	    border:false,
        height:FHD.getCenterPanelHeight()-3,
		layout: {
	        type: 'border'
	    },
	    items:[dictTree,dictGrid]
	});
	dictTree.on('collapse',function(p){
		dictGrid.setWidth(mainPanel.getWidth()-26-5);
	});
	dictTree.on('expand',function(p){
		dictGrid.setWidth(mainPanel.getWidth()-p.getWidth()-5);
	});
	mainPanel.on('resize',function(p){
		dictGrid.setHeight(p.getHeight()-3);
		if(dictTree.collapsed){
			dictGrid.setWidth(p.getWidth()-26-5);
		}else{
			dictGrid.setWidth(p.getWidth()-dictTree.getWidth()-5);
		}
	});
	dictTree.on('resize',function(p){
		if(p.collapsed){
			dictGrid.setWidth(mainPanel.getWidth()-26-5);
		}else{
			dictGrid.setWidth(mainPanel.getWidth()-p.getWidth()-5);
		}
	});
	FHD.componentResize(mainPanel,0,0);//参数为：需要自适应的对象，需要减去的宽度，需要减去的高度
	if(typeId!=null && typeId!='')
	{
	    FHD.componentResize(dictGrid,0,0);

	curTypeId=typeId;
	setstatus();
	 dictGrid.store.proxy.extraParams=[{'typeId':typeId}];
	 dictGrid.store.proxy.url=gridqueryUrl;
	 dictGrid.store.on('beforeload', function (store, options) {
	var new_params = { typeId: typeId };
	Ext.apply(store.proxy.extraParams,null);
	Ext.apply(store.proxy.extraParams, new_params);
	});
	 dictGrid.down('searchfield').url=gridqueryUrl;
	 dictGrid.down('searchfield').store.proxy.url=gridqueryUrl;
	 dictGrid.down('searchfield').onTrigger1Click();
	 dictGrid.store.load();
	 
	dictTree.hide();
	}
});


/***Ext.onReady end***/

</script>
</head>
<body>
	<div id='dictDiv${param._dc}'></div>
</body>
</html>
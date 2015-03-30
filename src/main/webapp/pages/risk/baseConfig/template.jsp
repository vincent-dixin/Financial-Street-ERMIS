<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
/***attribute start***/
var riskId = '${param.riskId}',
	riskIdIsNull = !(riskId!=null && riskId!=''),
	templateGrid,
	templateGridTbarItems,
	templateGridColumn,
	templateRelaDimensionTreeGrid,
	dimensionForSelectGrid,
	templateRelaDimensionEditWindow,
	typeStore,
	typeStoreURL = 'sys/dic/findDictEntryBySome.f?typeId=dim_template_type',//查询模板类型
	calculateMethodStore,
	calculateMethodStoreURL = 'sys/dic/findDictEntryBySome.f?typeId=dim_calculate_method',//查询计算方法
	saveTemplateByIdURL = 'risk/saveTemplateById.f',//拷贝模板
	saveTemplateRelaDimensionURL = 'risk/saveTemplateRelaDimension.f',//从待选维度列表新增模板关联维度
	mergeTemplateBatchURL = 'risk/mergeTemplateBatch.f',//保存模板
	removeTemplateByIdURL = 'risk/removeTemplateById.f',//删除模板
	removeTemplateRelaDimensionByIdURL = 'risk/removeTemplateRelaDimensionById.f',//删除模板关联维度
	templateRelaDimensionTreeLoaderURL = 'risk/templateRelaDimensionTreeLoader.f',//加载模板树
	findDimensionForSelectListURL = 'risk/findDimensionForSelectList.f?deleteStatus=1',//查询有效的维度
	findTemplateListURL = 'risk/findTemplateList.f?riskId='+riskId;//查询模板
/***attribute end***/	

/***function start***/
function addTemplate(){//新增模板方法
	var count = templateGrid.getStore().getCount();// 获得总行数
    var maxSort;
    if("0" != count){
    	maxSort = templateGrid.getStore().getAt(count-1).get("sort");
    }else{
    	maxSort=0;
    }
	var rec = Ext.create('Template',{
		name : FHD.locale.get('fhd.risk.baseconfig.newName'),
		editable:true,
		type : 'dim_template_type_self',
		sort : maxSort+1
	});
	templateGrid.store.insert(count, rec);
	templateGrid.editingPlugin.startEditByPosition({row:count,column:0}); 
} 
function saveTemplateById(){//拷贝模板方法
	var templateRows = templateGrid.store.getModifiedRecords();
	var selection = templateGrid.getSelectionModel().getSelection()[0];
	if(templateRows != null && templateRows.length){
		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.risk.baseconfig.saveFirst'));
		return;
	}
	FHD.ajax({
		url : saveTemplateByIdURL,
		params : {
			templateId: selection.get('id'),
			riskId: riskId
		},
		callback : function(data){
			if(data){
				Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
				templateGrid.store.load();
			}
		}
	});
}
function mergeTemplateBatch(){//保存模板方法
	var rows = templateGrid.store.getModifiedRecords();
	var jsonArray=[];
	Ext.each(rows,function(item){
		if(item.data.type &&　item.data.name && item.data.editable){
			jsonArray.push(item.data);
		}
	});
	FHD.ajax({
		url : mergeTemplateBatchURL,
		params : {
			jsonString:Ext.encode(jsonArray),
			riskId: riskId
		},
		callback : function(data){
			if(data){
				Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
				templateGrid.store.load();
			}
		}
	})
	templateGrid.store.commitChanges(); 
}
function openMergeTemplateRelaDimensionWindow(){
	var selection = templateRelaDimensionTreeGrid.getSelectionModel().getSelection()[0];//得到选中的记录
	templateRelaDimensionEditWindow = new Ext.Window({
		iconCls: 'icon-edit',//标题前的图片
		title: FHD.locale.get('fhd.common.edit'),
		modal:true,//是否模态窗口
		collapsible:true,
		width:500,
		height:500,
		maximizable:true,//（是否增加最大化，默认没有）
		autoLoad:{ url: 'pages/risk/baseConfig/templateRelaDimensionEdit.jsp',nocache:true,scripts:true}
	});
	templateRelaDimensionEditWindow.show();
	templateRelaDimensionEditWindow.initialConfig.id = selection.get('id');
}
function removeTemplateById(){//删除模板方法
	var templateRows = templateGrid.store.getModifiedRecords();
	var selection = templateGrid.getSelectionModel().getSelection()[0];
	if(templateRows != null && templateRows.length){
		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.risk.baseconfig.saveFirst'));
		return;
	}
	if(selection && 'dim_template_type_sys'== selection.get('type')){
		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.risk.baseconfig.notAllowDelete'));
		return;
	}
	if(selection && !selection.get('editable')){
		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.risk.baseconfig.noAuthority'));
		return;
	}
	if(selection && selection.get('used')){
		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.risk.baseconfig.usedTemplateWithNotAllowDelete'));
		return;
	}
	Ext.MessageBox.show({
		title : FHD.locale.get('fhd.common.delete'),
		width : 260,
		msg : FHD.locale.get('fhd.common.makeSureDelete'),
		buttons : Ext.MessageBox.YESNO,
		icon : Ext.MessageBox.QUESTION,
		fn : function(btn) {
			if (btn == 'yes') {
				FHD.ajax({
					url : removeTemplateByIdURL,
					params : {
						templateId:selection.get('id'),
						riskId: riskId
					},
					callback : function(data){
						if(data){
							Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
							templateGrid.store.load();
							templateRelaDimensionTreeGrid.store.load();
							dimensionForSelectGrid.store.load();
						}
					}
				});
			}
		}
	});
}
function removeTemplateRelaDimensionById(){//删除模板关联维度节点
	var selection = templateRelaDimensionTreeGrid.getSelectionModel().getSelection()[0];
	Ext.MessageBox.show({
		title : FHD.locale.get('fhd.common.delete'),
		width : 260,
		msg : FHD.locale.get('fhd.risk.baseconfig.deleteDimensionConfirm'),
		buttons : Ext.MessageBox.YESNO,
		icon : Ext.MessageBox.QUESTION,
		fn : function(btn) {
			if (btn == 'yes') {
				FHD.ajax({
					url : removeTemplateRelaDimensionByIdURL,
					params : {
						templateRelaDimensionId:selection.get('id')
					},
					callback : function(data){
						if(data){
							Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
							templateRelaDimensionTreeGrid.getSelectionModel().deselectAll();
							templateRelaDimensionTreeGrid.store.load();
							dimensionForSelectGrid.store.load();
						}
					}
				});
			}
		}
	});
}
function selectDimension(){//选择待选的维度
	var templateGridselection = templateGrid.getSelectionModel().getSelection()[0],
		templateRelaDimensionTreeGridselection = templateRelaDimensionTreeGrid.getSelectionModel().getSelection()[0],
		dimensionForSelectGridselection = dimensionForSelectGrid.getSelectionModel().getSelection()[0],
		templateId,
		dimensionId,
		parentId;
	if(templateGridselection && dimensionForSelectGridselection){
		templateId = templateGridselection.get('id');
		dimensionId = dimensionForSelectGridselection.get('id');
	}
	if(templateRelaDimensionTreeGridselection){
		parentId = templateRelaDimensionTreeGridselection.get('id');
	}
	FHD.ajax({
		url : saveTemplateRelaDimensionURL,
		params : {
			templateId: templateId,
			dimensionId: dimensionId,
			parentId: parentId==null?'':parentId
		},
		callback : function(data){
			if(data){
				Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
				templateRelaDimensionTreeGrid.getSelectionModel().deselectAll();
				templateRelaDimensionTreeGrid.store.load();
				dimensionForSelectGrid.store.load();
			}
		}
	});
}

function uploadTemplateFile(){
	var templateGridselection = templateGrid.getSelectionModel().getSelection()[0];
	Ext.create('FHD.ux.fileupload.FileUploadWindow',{
		multiSelect: false,
		callBack:function(value){
			if(value!=null&&value.length>0){
				FHD.ajax({
					url:__ctxPath+'/risk/mergeTemplateFile.f',
					params:{fileId:value, templateId: templateGridselection.get('id')},
					callback:function(data){
						if(data){
							Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
							templateGrid.store.load();
							templateRelaDimensionTreeGrid.store.load();
							dimensionForSelectGrid.store.load();
						}
					}
				}); 
			}
		}
	}).show();
}
function onTemplatechange(){//设置维度按钮可用状态
	var selection = templateGrid.getSelectionModel().getSelection();
	if(selection[0] && selection[0].get('editable') && !selection[0].get('used')){
		templateGrid.down('#delTemplate${param._dc}').setDisabled(selection.length=== 0);
		templateGrid.down('#uploadTemplateFile${param._dc}').setDisabled(selection.length === 0);
	}else{
		templateGrid.down('#delTemplate${param._dc}').setDisabled(true);
		templateGrid.down('#uploadTemplateFile${param._dc}').setDisabled(true);
	}
	templateGrid.down('#addTemplate${param._dc}').setDisabled(selection.length === 0);
	
	templateGrid.down('#saveTemplate${param._dc}').setDisabled(templateGrid.getStore().getModifiedRecords().length === 0);
}
function onDimensionForSelectchange(){//设置维度按钮可用状态
	var templateSelection = templateGrid.getSelectionModel().getSelection();
	var dimensionForSelectLength = dimensionForSelectGrid.getSelectionModel().getSelection().length;
	if(templateSelection[0] && templateSelection[0].get('editable') && !templateSelection[0].get('used')){
		dimensionForSelectGrid.down('#selectDimension${param._dc}').setDisabled(templateSelection.length === 0 || dimensionForSelectLength ===0);
	}else{
		dimensionForSelectGrid.down('#selectDimension${param._dc}').setDisabled(true);
	}
	
}
function onTemplateRelaDimensionTreechange(){//设置维度按钮可用状态
	var templateSelection = templateGrid.getSelectionModel().getSelection();
	var selectionLength = templateRelaDimensionTreeGrid.getSelectionModel().getSelection().length;
	if(templateSelection[0] && templateSelection[0].get('editable') && !templateSelection[0].get('used')){
		templateRelaDimensionTreeGrid.down('#delDimension${param._dc}').setDisabled(selectionLength === 0);
		templateRelaDimensionTreeGrid.down('#editDimension${param._dc}').setDisabled(selectionLength === 0);
	}else{
		templateRelaDimensionTreeGrid.down('#delDimension${param._dc}').setDisabled(true);
		templateRelaDimensionTreeGrid.down('#editDimension${param._dc}').setDisabled(true);
	}
	
}
/***function end***/
/***Ext.onReady start***/
Ext.onReady(function(){
	/***templateGrid start***/
	Ext.define('Template', {//定义model
	    extend: 'Ext.data.Model',
	   	fields: [{name: 'id', type: 'string'},
		        {name: 'type', type: 'string'},
		        {name: 'name', type:'string'},
		        {name: 'editable'},
		        {name: 'desc', type:'string'},
		        {name: 'sort',type: 'int'}]
	});
	typeStore=Ext.create('Ext.data.Store', {//dictTreeCombox的store
	    fields : ['id', 'name'],
		proxy : {type : 'ajax',url : typeStoreURL,reader:{type:'json',root:'children'}},
		autoload:false
	});
	typeStore.load();
	calculateMethodStore=Ext.create('Ext.data.Store', {//dictTreeCombox的store
	    fields : ['id', 'name'],
		proxy : {type : 'ajax',url : calculateMethodStoreURL,reader:{type:'json',root:'children'}},
		autoload:false
	});
	calculateMethodStore.load();
	templateGridTbarItems = [FHD.locale.get('fhd.risk.baseconfig.templateList'),'-'
		,{text : FHD.locale.get('fhd.common.add'),iconCls: 'icon-add',handler:addTemplate, scope : this},'-'
		,{text : FHD.locale.get('fhd.common.delete'),iconCls: 'icon-del',id:'delTemplate${param._dc}', handler:removeTemplateById, disabled : true, scope : this},'-'
		,{text : FHD.locale.get('fhd.common.save'),iconCls: 'icon-save',id:'saveTemplate${param._dc}',handler:mergeTemplateBatch, disabled : true, scope : this},'-'
		,{text : FHD.locale.get('fhd.common.operate'),iconCls: 'icon-cog',
			menu:{items:[{text : FHD.locale.get('fhd.common.copyEntity'),iconCls: 'icon-other-copy',id:'addTemplate${param._dc}',handler:saveTemplateById, disabled : true, scope : this},
	             {text : FHD.locale.get('fhd.common.upload'),iconCls: 'icon-folder-upload',id:'uploadTemplateFile${param._dc}',handler:uploadTemplateFile, disabled : false, scope : this}
			]}}
	];
	templateGridColumn = [{dataIndex:'id',width:0},
		{header: FHD.locale.get('fhd.risk.baseconfig.name')+'<font color=red>*</font>', dataIndex: 'name', hideable:false, sortable: false, flex : 1,editor: {allowBlank: false},
			renderer:function(value,metaData,record,colIndex,store,view) { 
				metaData.tdAttr = 'data-qtip="'+ value +'"';  
				return value;
			}
	    },
		{header: FHD.locale.get('fhd.risk.baseconfig.type')+'<font color=red>*</font>', dataIndex: 'type', hideable:false, sortable: false, flex : 1,
			renderer:function(value, metadata, record, rowIndex, colIndex, store){
		 		var typeRecord = typeStore.findRecord('id',value);
				if(typeRecord!=null){
					return typeRecord.raw.name;
				}else{
					return '';
				} 
			}
		},
		{header: FHD.locale.get('fhd.risk.baseconfig.desc'), dataIndex: 'desc', sortable: false, flex : 3,editor: {},
			renderer:function(value,metaData,record,colIndex,store,view) { 
				metaData.tdAttr = 'data-qtip="'+ value +'"';  
				return value;
			}
	    },
	    {header: FHD.locale.get('fhd.risk.baseconfig.allowEdit'), dataIndex: 'editable', hidden:riskIdIsNull?true:false,sortable: false, width: 80,
		    renderer:function(value,metaData,record,colIndex,store,view) { 
		    	if(value==true){
		    		return FHD.locale.get('fhd.common.true');
		    	}else if(value==false){
		    		return FHD.locale.get('fhd.common.false');
		    	}else{
		    		return '';
		    	}
			}
		},
		{header: FHD.locale.get('fhd.risk.baseconfig.used'), dataIndex: 'used', sortable: false, width: 60,
		    renderer:function(value,metaData,record,colIndex,store,view) { 
		    	if(value){
		    		return FHD.locale.get('fhd.common.true');
		    	}else{
		    		return FHD.locale.get('fhd.common.false');
		    	}
			}
		},
		{header: FHD.locale.get('fhd.common.download'), dataIndex: 'fileId', sortable: false, width: 60,
			align: 'center',
		    renderer:function(value,metaData,record,colIndex,store,view) { 
		    	if(value){
		    		return "<a href=\"javascript:void(0);\" onclick=\"downloadFile('" + value + "')\"><span class=\"icon-download-min\" style=\"cursor:pointer;\">&nbsp;&nbsp;&nbsp;&nbsp;</span></a>";  
		    	}else{
		    		return FHD.locale.get('fhd.common.nothing');
		    	}
			}
		},
		{header: FHD.locale.get('fhd.risk.baseconfig.sort'),dataIndex: 'sort', sortable: false, width: 60,editor:{
				xtype:'numberfield',
				minValue: 1,  
				allowDecimals: false, // 允许小数点 
				nanText: FHD.locale.get('fhd.risk.baseconfig.inputInteger'),
				//hideTrigger: true,  //隐藏上下递增箭头
				keyNavEnabled: true,  //键盘导航
				mouseWheelEnabled: true,  //鼠标滚轮
				step:1
			}
		},
		{
			header: FHD.locale.get('fhd.common.operate'),dataIndex:'',width:60,hideable:false, 
			align: 'center',
			xtype:'actioncolumn',
			items: [{
				icon: __ctxPath+'/images/icons/edit.gif',  // Use a URL in the icon config
				tooltip: FHD.locale.get('fhd.common.edit'),
				handler: function(grid, rowIndex, colIndex) {
					//点击编辑按钮时，自动选中行
					grid.getSelectionModel().deselectAll();
					
					var rows=[grid.getStore().getAt(rowIndex)];
	 	    		grid.getSelectionModel().select(rows,true);
					var rec = grid.getStore().getAt(rowIndex);
					templateRelaDimensionTreeGrid.getSelectionModel().deselectAll();
					templateRelaDimensionTreeGrid.down('searchfield').url = templateRelaDimensionTreeLoaderURL + '?templateId='+rec.data.id;
					//刷新模板关联维度列表
					templateRelaDimensionTreeGrid.getStore().proxy.url = templateRelaDimensionTreeLoaderURL + '?templateId='+rec.data.id;
					templateRelaDimensionTreeGrid.getStore().load();
					//刷新待选维度列表
					dimensionForSelectGrid.getStore().on('beforeload', function (store, options) {
					var new_params = { templateId: rec.data.id};
	              	    Ext.apply(store.proxy.extraParams,null);
	          	        Ext.apply(store.proxy.extraParams, new_params);
	              	});
					dimensionForSelectGrid.getStore().load();
				}
			}]
		}
	];
	templateGrid = Ext.create('FHD.ux.EditorGridPanel',{//创建可编辑的grid列表，注释同不可编辑的gird
		renderTo: 'templateDIV${param._dc}', 
		multiSelect:false,
		checked:false,
		url: findTemplateListURL,
		height:FHD.getCenterPanelHeight()*0.4,
		pagable:false,
		cols:templateGridColumn,
		tbarItems:templateGridTbarItems
	});
	templateGrid.store.on('load',onTemplatechange);//执行store.load()时改变按钮可用状态
	templateGrid.on('selectionchange',onTemplatechange);//选择记录发生改变时改变按钮可用状态
	templateGrid.store.on('update',onTemplatechange);//修改时改变按钮可用状态
	/***templateGrid end***/
	/***dimensionForSelectGrid start***/
	dimensionForSelectGrid = Ext.create('FHD.ux.GridPanel',{//创建可编辑的grid列表，注释同不可编辑的gird
		renderTo: 'dimensionForSelectDIV${param._dc}', 
		multiSelect:false,
		checked:false,
		stripeRows : true,
		padding:'0 1 0 0',
		url: findDimensionForSelectListURL,
		height:FHD.getCenterPanelHeight()*0.6,
		pagable:false,
		cols:[
		    {dataIndex:'id',width:0},
			{header: FHD.locale.get('fhd.risk.baseconfig.code'), dataIndex: 'code', sortable: false, flex : 1,
				renderer:function(value,metaData,record,colIndex,store,view) { 
					metaData.tdAttr = 'data-qtip="'+ value +'"';  
					return value;
				}
		    },
			{header: FHD.locale.get('fhd.risk.baseconfig.name'), dataIndex: 'name', hideable:false, sortable: false, flex : 2,
				renderer:function(value,metaData,record,colIndex,store,view) { 
					metaData.tdAttr = 'data-qtip="'+ value +'"';  
					return value;
				}
		    },
			{header: FHD.locale.get('fhd.risk.baseconfig.desc'), dataIndex: 'desc', sortable: false, flex : 4,
				renderer:function(value,metaData,record,colIndex,store,view) { 
					metaData.tdAttr = 'data-qtip="'+ value +'"';  
					return value;
				}
		    },
			{header: FHD.locale.get('fhd.risk.baseconfig.sort'),dataIndex: 'sort', sortable: false,hidden:true, width: 60}
		],
		tbarItems:[FHD.locale.get('fhd.risk.baseconfig.dimensionListForSelect'),'-'
			,{text : FHD.locale.get('fhd.common.select'),iconCls: 'icon-tick',id:'selectDimension${param._dc}',handler:selectDimension, disabled : true, scope : this}
		]
	});
	dimensionForSelectGrid.store.on('load',onDimensionForSelectchange);//执行store.load()时改变按钮可用状态
	dimensionForSelectGrid.on('selectionchange',onDimensionForSelectchange);//选择记录发生改变时改变按钮可用状态
	/***dimensionForSelectGrid end***/
	/***templateRelaDimensionTreeGrid start***/
	templateRelaDimensionTreeGrid = Ext.create('FHD.ux.TreeGridPanel',{
        useArrows: true,
        rootVisible: false,
        multiSelect: false,
        rowLines:true,
        stripeRows : true,
        singleExpand: false,
        checked: false,
        padding:'1 1 0 0',
		renderTo: 'templateRelaDimensionDIV${param._dc}', 
		url: templateRelaDimensionTreeLoaderURL,
		height:FHD.getCenterPanelHeight()*0.6,
		cols:[
			{dataIndex: 'id',width:0}
			,{text: FHD.locale.get('fhd.risk.baseconfig.name'),dataIndex: 'name',xtype: 'treecolumn',flex: 2,hideable:false,sortable: false,
			    renderer:function(value,metaData,record,colIndex,store,view) { 
					metaData.tdAttr = 'data-qtip="'+value+'"';  
				    return value;
					//return "<a href=\"javascript:void(0);\" onclick=\"view('" + record.get('id') + "')\">"+value+"</a>";  
				}
			}
			,{text: FHD.locale.get('fhd.risk.baseconfig.desc'),dataIndex: 'desc',flex: 2,hideable:false,sortable: false,
			    renderer:function(value,metaData,record,colIndex,store,view) { 
					metaData.tdAttr = 'data-qtip="'+value+'"';  
				    return value;
				}
			}
			,{text: FHD.locale.get('fhd.risk.baseconfig.isCalculate'),dataIndex: 'isCalculate',sortable: false,flex: 1,
			    renderer:function(value,metaData,record,colIndex,store,view) { 
			    	if(value){
			    		return FHD.locale.get('fhd.common.true');
			    	}else{
			    		return FHD.locale.get('fhd.common.false');
			    	}
				}
			}
			,{text: FHD.locale.get('fhd.risk.baseconfig.calculateMethod'),dataIndex: 'calculateMethodId',sortable: false,flex: 1,editor:
				new FHD.ux.dict.DictSelectForEditGrid({
				    dictTypeId:'dim_calculate_method',hideLabel : true
				}),
				renderer:function(value, metadata, record, rowIndex, colIndex, store){
				    var curModel = calculateMethodStore.findRecord("id",value);
				    if(curModel!=null)
					{
					return curModel.raw.name;
					}
				}
			}
		],
		tbarItems:[//菜单项
		    FHD.locale.get('fhd.risk.baseconfig.templateRelaDimensionList')
			,'-'
			,{text : FHD.locale.get('fhd.common.edit'),iconCls: 'icon-edit', id:'editDimension${param._dc}',handler:openMergeTemplateRelaDimensionWindow, disabled : true} 
			,'-'
			,{text : FHD.locale.get('fhd.common.delete'),iconCls: 'icon-del',id:'delDimension${param._dc}', handler:removeTemplateRelaDimensionById, disabled : true, scope : this}  
		]
	});
	templateRelaDimensionTreeGrid.store.on('load',onTemplateRelaDimensionTreechange);//执行store.load()时改变按钮可用状态
	templateRelaDimensionTreeGrid.on('selectionchange',onTemplateRelaDimensionTreechange);//选择记录发生改变时改变按钮可用状态
	/***templateRelaDimensionTreeGrid end***/
	//设置自适应窗口
	baseConfigTree.on('collapse',function(p){
		templateGrid.setWidth((baseConfigMainPanel.getWidth()-26)-5);
		templateRelaDimensionTreeGrid.setWidth((baseConfigMainPanel.getWidth()-26)/2-2);
		dimensionForSelectGrid.setWidth((baseConfigMainPanel.getWidth()-26)/2-2);
	});
	baseConfigTree.on('expand',function(p){
		templateGrid.setWidth((baseConfigMainPanel.getWidth()-p.getWidth())-5);
		templateRelaDimensionTreeGrid.setWidth((baseConfigMainPanel.getWidth()-p.getWidth())/2-2);
		dimensionForSelectGrid.setWidth((baseConfigMainPanel.getWidth()-p.getWidth())/2-2);
	});
	baseConfigMainPanel.on('resize',function(p){
		templateGrid.setHeight(p.getHeight()*0.4);
		templateRelaDimensionTreeGrid.setHeight(p.getHeight()*0.6);
		dimensionForSelectGrid.setHeight(p.getHeight()*0.6);
		if(baseConfigTree.collapsed){
			templateGrid.setWidth((p.getWidth()-26)-5);
			templateRelaDimensionTreeGrid.setWidth((p.getWidth()-26)/2-2);
			dimensionForSelectGrid.setWidth((p.getWidth()-26)/2-2);
		}else{
			templateGrid.setWidth((p.getWidth()-baseConfigTree.getWidth())-5);
			templateRelaDimensionTreeGrid.setWidth((p.getWidth()-baseConfigTree.getWidth())/2-2);
			dimensionForSelectGrid.setWidth((p.getWidth()-baseConfigTree.getWidth())/2-2);
		}
	});
	baseConfigTree.on('resize',function(p){
		if(p.collapsed){
			templateGrid.setWidth((baseConfigMainPanel.getWidth()-26)-5);
			templateRelaDimensionTreeGrid.setWidth((baseConfigMainPanel.getWidth()-26)/2-2);
			dimensionForSelectGrid.setWidth((baseConfigMainPanel.getWidth()-26)/2-2);
		}else{
			templateGrid.setWidth((baseConfigMainPanel.getWidth()-p.getWidth())-5);
			templateRelaDimensionTreeGrid.setWidth((baseConfigMainPanel.getWidth()-p.getWidth())/2-2);
			dimensionForSelectGrid.setWidth((baseConfigMainPanel.getWidth()-p.getWidth())/2-2);
		}
	});
});
/***Ext.onReady end***/

</script>


</head>
<body>
	<div id='templateDIV${param._dc}' style="height:40%;"></div>
	<div id='templateRelaDimensionDIV${param._dc}' style="height:60%;width:50%;float:left"></div>
	<div id='dimensionForSelectDIV${param._dc}' style="height:60%;width:50%;float:right"></div>
</body>

</html>

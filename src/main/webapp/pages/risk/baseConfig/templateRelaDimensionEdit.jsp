<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
/***attribute start***/
var param = templateRelaDimensionEditWindow.initialConfig,//参数
	formPanel,//表单
	formColums,//表单列
	formButtons = [//表单按钮
		{text: FHD.locale.get('fhd.common.save'),handler:mergeTemplateRelaDimension},
	    {text: FHD.locale.get('fhd.common.cancel'),handler:cancel}
	],
	scoreInstanceGrid,//模板关联维度的分值grid
	mergeTemplateRelaDimensionURL = 'risk/mergeTemplateRelaDimension.f',//保存模板关联维度的URL;
	mergeScoreInstanceBatchURL = 'risk/mergeScoreInstanceBatch.f',//保存模板关联维度下的分值数据的URL;
	findScoreInstanceListURL = 'risk/findScoreInstanceList.f?templateRelaDimensionId='+param.id;//查找模板关联维度下的分值数据的URL。
/***attribute end***/
/***function start***/
function mergeTemplateRelaDimension(){//保存模板关联维度
    var form = formPanel.getForm();
    if (form.isValid()) {
    	FHD.submit({form:form,url:mergeTemplateRelaDimensionURL,callback:function(data){templateRelaDimensionTreeGrid.store.load()}});
    	mergeScoreInstanceBatch();
	}
}
function mergeScoreInstanceBatch(){//保存模板关联维度下的分值数据
	var rows = scoreInstanceGrid.store.getModifiedRecords();
	var jsonArray=[];
	Ext.each(rows,function(item){
		if(item.data.name){
			jsonArray.push(item.data);
		}
	});
	if(jsonArray.length>0){
		FHD.ajax({
			url : mergeScoreInstanceBatchURL,
			params : {
				jsonString:Ext.encode(jsonArray)
			},
			callback : function(data){
				if(data){
					Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
					scoreInstanceGrid.getStore().load();
					templateRelaDimensionEditWindow.close();
				}
			}
		})
	}else{
		templateRelaDimensionEditWindow.close();
	}
}
function cancel(){//取消方法
	templateRelaDimensionEditWindow.close();
}
/***function end***/
/***form start***/ 
formColums = [//form表单的列
   	{xtype : 'hidden',hidden:true,name : 'id', id:'id'}
   	,{xtype : 'textfield',fieldLabel : FHD.locale.get('fhd.risk.baseconfig.name'), name : 'name', labelAlign:'left',readOnly:true}
   	,Ext.create('FHD.ux.dict.DictSelectForEditGrid',{ padding:'1 0 0 0',fieldLabel : FHD.locale.get('fhd.risk.baseconfig.calculateMethod'),name:'calculateMethodId', labelAlign:'left',multiSelect:false,columns:5, dictTypeId:'dim_calculate_method' })
   	,{xtype : 'numberfield', fieldLabel : FHD.locale.get('fhd.risk.baseconfig.weight'), name : 'weight', labelAlign:'left',maxValue: 100, minValue: 0, allowDecimals: true, nanText:FHD.locale.get('fhd.risk.baseconfig.inputNumber'), step:1}
   	,{xtype : 'textareafield', fieldLabel : FHD.locale.get('fhd.risk.baseconfig.desc'), name : 'desc', labelAlign:'left'}
];
scoreInstanceGrid = Ext.create('FHD.ux.EditorGridPanel',{
	url:findScoreInstanceListURL,
	searchable: false,
	checked: false,
	height: 160,
	pagable: false,
	cols:[
	    {dataIndex:'id',width:0}
		,{header: FHD.locale.get('fhd.risk.baseconfig.name'), dataIndex: 'name', sortable: false,hideable:false, flex : 2,editor: {allowBlank: false},
			renderer:function(value,metaData,record,colIndex,store,view) { 
				metaData.tdAttr = 'data-qtip="'+ value +'"';  
				return value;
			}
	    }
		,{header: FHD.locale.get('fhd.risk.baseconfig.value'), dataIndex: 'value', sortable: false,hideable:false, flex : 1}
		,{header: FHD.locale.get('fhd.risk.baseconfig.desc'), dataIndex: 'desc', sortable: false, flex : 6,editor: {},
			renderer:function(value,metaData,record,colIndex,store,view) { 
				metaData.tdAttr = 'data-qtip="'+ value +'"';  
				return value;
			}
	    }
	]
});
formPanel = Ext.create('Ext.form.Panel',{
	renderTo:'templateRelaDimensionFormDiv${param._dc}',
	bodyPadding: 5,
	items: [
		{xtype: 'fieldset',
			defaults: {columnWidth: 1},//每行显示一列，可设置多列
			flex:1,
			layout: {type: 'column'},
			collapsed: false,
			collapsible: false,
			title: FHD.locale.get('fhd.common.baseInfo'),
			items: formColums},
		{xtype: 'fieldset',
			flex:1,
			collapsed: false,
			collapsible: false,
			title: FHD.locale.get('fhd.risk.baseconfig.dimensionValue'),
			items: scoreInstanceGrid}
	      ],
	buttons:formButtons
});
/***form end***/ 
templateRelaDimensionEditWindow.on('resize',function(win){
	formPanel.setWidth(win.getWidth()-10);
	formPanel.setHeight(win.getHeight()-40);
})
if(typeof(param.id) != "undefined") {
	
	formPanel.form.load( {
        url : 'risk/findTemplateRelaDimension.f',
        params: {templateRelaDimensionId: param.id},
        failure : function(form,action) {
            MessageBox.error(FHD.locale.get('fhd.common.initError'));
        }
    });
}
</script>
</head>
<body>
	<div id='templateRelaDimensionFormDiv${param._dc}'></div>
</body>
</html>


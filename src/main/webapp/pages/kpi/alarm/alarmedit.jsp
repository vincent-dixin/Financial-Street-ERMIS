<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>告警编辑</title>
<script type="text/javascript">
/** *attribute start** */
var url=__ctxPath + "/kpi/alarm/findalarmplanregions.f";
var addUrl = __ctxPath + "/kpi/alarm/savealarmplan.f";
var updateUrl = __ctxPath + "/kpi/alarm/mergealarmplan.f";
var loadUrl = __ctxPath + "/kpi/alarm/findalarmplanbyid.f";
var validateUrl = __ctxPath + "/kpi/alarm/validate.f";
var param = formwindow.initialConfig;
var rangeColums;
var rangelist;
var formPanel;
var validateItems ;
var formulaTwoValue;
var formulaOneValue;
var rangeObjectList = [];
var winsub;
var formButtons = [// 表单按钮
	{text: FHD.locale.get('fhd.common.save'),handler:function(){
		if(formPanel){
			 var form = formPanel.getForm();
			    if (form.isValid()&&validate()) {
			    	var rangeArray = getStoreValue();
					form.setValues({
						range:Ext.JSON.encode(rangeArray),
						memo:Ext.getCmp('descs${param._dc}').value
						});
					if (isAdd) {// 新增
						// 参数依次为form，url，callback
						FHD.submit({form:form,url:addUrl,callback:function(data){
							alarm_mgr_grid.store.load();
						}});
					} else {// 更新
						FHD.submit({form:form,url:updateUrl,callback:function(data){
							alarm_mgr_grid.store.load();
						}});
					}
					formwindow.close();
				}
		}
	}},
    {text: FHD.locale.get('fhd.common.cancel'),handler:cancel}
];
/** *attribute end** */
/** *function start** */

function getStoreValue(){
	var rangeArray = [];
	if(rangeObjectList){
		for(var i=0;i<rangeObjectList.length;i++){
			var rangeObj = {};
			var obj = rangeObjectList[i];
			rangeObj.level = getLevel(obj.id);
			rangeObj.formulaMax = obj.formulaTwo;
			rangeObj.formulaMin = obj.formulaOne;
			rangeObj.maxSign = obj.signTwoId;
			rangeObj.minSign = obj.signOneId;
			rangeArray.push(rangeObj);
		}
	}
	return rangeArray;
}

function getLevel(id){
	var level = "";
	var storeItems = rangelist.store.data.items;
	Ext.Array.each(storeItems, function(object) {
		if(object.data.id==id){
			level = object.data.level;
		}
	});
	return level;
}




function validate() {
	//var form = formPanel.getForm();
	//vform = form.getValues();
	var alarmname = Ext.getCmp('alarm_name${param._dc}').value;
	validateItems = {"name":alarmname};
    var flag=false;
    FHD.ajax({
    	url: validateUrl,
        async:false,// 这一项必须加上，否则后台返回true,flag的值也为false
        params : {
				mode:isAdd,
				id:param.id,
				name:alarmname,
				validateItems:Ext.JSON.encode(validateItems)
			},
		callback : function(data){
			if(data&&data.success){
				flag = true;
			}else{
				if(data.error=="nameRepeat"){
					Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.alarmplan.prompt.nameRepeat'));
				}
			}
		}
    })
    return flag;
}


function cancel(){// 取消方法
	formwindow.close();
}
/** *function end** */
/** *form start** */ 

var alarm_formColums = [// form表单的列
   	{xtype : 'hidden',name : 'id',value:param.id},
   	{xtype : 'hidden',name : 'range'},// 提交表单时拼接成json串,赋值给range属性,传递给后台[{level:'',formulaMax:'',formulaMin:'',maxSign:'',minSign:''}.....]
   	{xtype : 'textfield',labelAlign:'left',fieldLabel : FHD.locale.get('fhd.alarmplan.form.name')+'<font color=red>*</font>',id:'alarm_name${param._dc}',name : 'name',allowBlank : false,margin: '3 3 3 3'},
    Ext.create('widget.dictselectforeditgrid',{
   		fieldLabel: FHD.locale.get('fhd.alarmplan.form.types')+'<font color=red>*</font>',
   	    margin: '3 3 3 3',
   	 	labelWidth:76,
   	    name:'types',
   	    labelAlign:'left',
   	    allowBlank : false,
   	    editable : false,
   	    multiSelect:false,
   		dictTypeId:'0alarm_type'
    }),
   	{xtype : 'textareafield',rows:5,margin: '3 3 3 3',labelAlign: 'left',fieldLabel : FHD.locale.get('fhd.sys.dic.desc'),id:'descs${param._dc}',name : 'descs',allowBlank : true}
	
];


 Ext.define('rangeModel', {
	 extend: 'Ext.data.Model',
     fields: [
         {name: 'id', type: 'string'},
         {name: 'level',  type: 'string'},
         {name: 'range',  type: 'string'}
     ]
 });

function rangeSave(){
	var formulaTwoValue = Ext.getCmp('formulaTwo${param._dc}').value;
	if(formulaTwoValue==null||formulaTwoValue==undefined){
		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'区间值不能为空');
		return ;
	}
	var formulaOneValue = Ext.getCmp('formulaOne${param._dc}').value;
	if(formulaOneValue==null||formulaOneValue==undefined){
		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'区间值不能为空');
		return ;
	}
	var signOneId = Ext.getCmp('signOne${param._dc}').value;
	if(signOneId==null||signOneId==""||signOneId==undefined){
		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'区间符号不能为空');
		return ;
	}
	var signOneValue = signOneId.substr(16);
	
	if(signOneValue=="<"){
		signOneValue = "&lt;";
	}
	var signTwoId = Ext.getCmp('signTwo${param._dc}').value;
	if(signTwoId==null||signTwoId==""||signTwoId==undefined){
		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'区间符号不能为空');
		return ;
	}
	var signTwoValue = signTwoId.substr(16);
	
	
	var selection = rangelist.getSelectionModel().getSelection();
	for(var i=0;i<selection.length;i++){
		var id = selection[i].get('id');
		for(var j=0;j<rangeObjectList.length;j++){
			if(id==rangeObjectList[j].id){
				rangeObjectList[j].formulaOne = formulaOneValue;
				rangeObjectList[j].formulaTwo = formulaTwoValue;
				rangeObjectList[j].signOneId = signOneId;
				rangeObjectList[j].signOneValue = signOneValue;
				rangeObjectList[j].signTwoId = signTwoId;
				rangeObjectList[j].signTwoValue = signTwoValue;
			}
		}
	}
	var exp = formulaOneValue+signOneValue+"X"+signTwoValue+formulaTwoValue;
	for(var k=0;k<selection.length;k++){
		selection[k].set({'range':exp});
	}
	winsub.close();
}

function setValues(){
	var selection = rangelist.getSelectionModel().getSelection();
	for(var i=0;i<selection.length;i++){
		var id = selection[i].get('id');
		for(var j=0;j<rangeObjectList.length;j++){
			if(id==rangeObjectList[j].id){
				Ext.getCmp('formulaOne${param._dc}').setValue(rangeObjectList[j].formulaOne);
				Ext.getCmp('formulaTwo${param._dc}').setValue(rangeObjectList[j].formulaTwo);
				Ext.getCmp('signOne${param._dc}').setValue(rangeObjectList[j].signOneId);
				Ext.getCmp('signTwo${param._dc}').setValue(rangeObjectList[j].signTwoId);
			}
		}
	}
}



var gridColums =[
	             	{header: FHD.locale.get("fhd.alarmplan.form.level"), dataIndex: 'level', sortable: true, width: 60,flex : 1,editor: new FHD.ux.dict.DictSelectForEditGrid({
					    dictTypeId:'0alarm_startus',fieldLabel:''
					}),
					renderer:function(v){
						var color = "";
						var text = "";
						var icon = "";
						switch(v){
							case '0alarm_startus_h':/* 对应数据字典中的主键 */
								color='symbol_4_sm';
								icon = "symbol-4-sm";
								text=FHD.locale.get("fhd.alarmplan.form.hight");
								break;
							case '0alarm_startus_m':
								color='symbol_5_sm';
								icon = "symbol-5-sm";
								text=FHD.locale.get("fhd.alarmplan.form.min");
								break;
							case '0alarm_startus_l':
								color='symbol_6_sm';
								icon = "symbol-6-sm";
								text=FHD.locale.get("fhd.alarmplan.form.low");
								break;
						}
						  return color!=""?'<img src="'+__ctxPath+'/images/icons/' + color+ '.gif">':"";
	             	  }
	             	
	             	},
	             	{header: FHD.locale.get("fhd.alarmplan.form.range"), dataIndex: 'range', sortable: true, width: 60,flex : 1,
	             		renderer:function(v){
	             			var rangestr = v;
	             			if(v.indexOf("<")!=-1){
	             				rangestr = v.replace("<","&lt;");
	             			}
	             			return rangestr;
	             		},
	             		editor:{
						xtype:'trigger',
						editable:false,
						onTriggerClick:function(){
	             		var me = this;
						winsub = new Ext.Window({
							constrain:true,
							width:440,
							height:100,
							layout:'hbox',
							layoutConfig: {
								pack:'center',
								align:'middle'
							},
							defaults:{margins:'0 5 0 0'},
							modal:true,
							title:FHD.locale.get('fhd.alarmplan.form.rangeformula'),
							items:[{
								xtype: 'numberfield',
                                step: 1,
								id:'formulaOne${param._dc}',
								width:150
							},
							new FHD.ux.dict.DictSelectForEditGrid({
							    dictTypeId:'0_compare_symbol',fieldLabel:'',width:50,id:'signOne${param._dc}',multiSelect:false
							})
							,{
								xtype:'label',
								text:'X'
							},
							new FHD.ux.dict.DictSelectForEditGrid({
							    dictTypeId:'0_compare_symbol',fieldLabel:'',width:50,id:'signTwo${param._dc}',multiSelect:false
							})
							,{
								xtype: 'numberfield',
                                step: 1,
								id:'formulaTwo${param._dc}',
								width:150
							}],buttonAlign:'center',
							buttons: [{
								text: FHD.locale.get('fhd.common.save'),
								handler:function(){
									rangeSave();
									
								}
							},{
								text: FHD.locale.get('fhd.common.cancel'),
								handler:function(){winsub.close();}
							}]
							
						});
						winsub.show(me);
						// 给公式赋值
						setValues();
						}
					}
					}
                ];



/** *form end** */ 



Ext.onReady(function(){
	if(!isAdd){
		url = url+"?id="+param.id;
	}else{
		url = "";
	}
	rangelist=Ext.create('FHD.ux.EditorGridPanel',
	{
		url:url,
		searchable:false,
		cols:gridColums,
		height:180,
		pagable:false,
		tbarItems:[{iconCls:'icon-add',handler:rangeAdd},'-',{iconCls:'icon-del',handler:rangeDel,id:'delId'}]
	});

	rangelist.store.on('load',onchange);// 执行store.load()时改变按钮可用状态
	rangelist.on('selectionchange',onchange);// 选择记录发生改变时改变按钮可用状态
	rangelist.store.on('update',onchange);// 修改时改变按钮可用状态
	
rangeColums=[rangelist];

formPanel = Ext.create('Ext.form.Panel',{
	renderTo:'alarmedit${param._dc}',
	border:false,
	bodyPadding: 5,
	items: [ 
			{
			    xtype: 'hidden',
			    hidden: true,
			    name: 'memo',
			    id: 'memo'
			},
	        {xtype: 'fieldset',
			flex:1,
			defaults: {columnWidth: 1/1,labelWidth:76,margin: '3 3 3 3'},// 每行显示一列，可设置多列
			layout: {type: 'column'},
			title: FHD.locale.get('fhd.common.baseInfo'),
			items: alarm_formColums
		}
	    ,
		{xtype: 'fieldset',
	    	flex:1,
			collapsed: false,
			overflow:'auto',
			//collapsible: true,
			title: FHD.locale.get('fhd.alarmplan.form.rangeset'),
			items: rangeColums
		}
	   ],
	buttons:formButtons
	});


if(typeof(param.id) != "undefined") {
	formPanel.form.load( {
        url : loadUrl,
        params: {id: param.id},
        success:function(form,action){
        	rangeObjectList = Ext.JSON.decode(action.result.regions);
        },
        failure : function(form,action) {
            alert("err load");
        }
    });
}

formwindow.on('resize',function(me){
	formPanel.setWidth(me.getWidth()-10);
	formPanel.setHeight(me.getHeight()-40);
})
});


function createRndNum(n){
	var rnd="";
	for(var i=0;i<n;i++)
	rnd+=Math.floor(Math.random()*10);
	return rnd;
}

function rangeDel(){
	var k = 0;
	var tempList = [];
	var selection = rangelist.getSelectionModel().getSelection();
	for(var i=0;i<selection.length;i++){
		for(var j=0;j<rangeObjectList.length;j++){
			if(rangeObjectList[j].id!=selection[i].get('id')){
				tempList[k++] = rangeObjectList[j];
			}
		}
		rangelist.store.remove(selection[i]);
	}
	rangeObjectList = tempList;
}



function rangeAdd(){// 新增方法
	var r = Ext.create('rangeModel');
	var randNum = createRndNum(6);
	r.set({id:randNum});
	rangelist.store.add(r);
	rangelist.editingPlugin.startEditByPosition({row:0,column:0}); 
	
	var rangeObj = {};
	rangeObj.id = randNum;
	rangeObjectList.push(rangeObj);
} 

function onchange(){// 设置你按钮可用状态
	rangelist.down('#delId').setDisabled(rangelist.getSelectionModel().getSelection().length === 0);
}
/** *CustomValidator start** */
Ext.apply(Ext.form.field.VTypes, {
	
});
/** *CustomValidator end** */
</script>
</head>
<body>
	<div id='alarmedit${param._dc}'></div>
</body>
</html>
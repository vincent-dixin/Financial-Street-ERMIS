<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>任务计划模版</title>
<script type="text/javascript">
/***attribute start***/
var dictEntryId='';
var editor;
var loadable=true;//解决重复load冲突的问题
var taskTree, queryUrl = 'sys/st/findDictEntryBySome.f?dictEntryId=st_temp_category';
var tempGridqueryUrl = 'sys/st/findDictEntryByType.f';
var formUrl = 'sys/st/findTempByCategory.f';
var tempMan_saveUrl = 'sys/st/saveTemp.f';
var i18store;
var delMsg=FHD.locale.get('fhd.common.makeSureDelete');
var tempFormPanel;
var tempManPanel;
var tempGrid;

Ext.define('demopanel',{
	extend:'Ext.panel.Panel',
	border:false,
	autoScroll:false,
	height:FHD.getCenterPanelHeight(),
	region: 'center',
	width:100
});
/***attribute end***/

/***function start***/
//解决重复load不同步的问题
function doLoad(records, operation, success){
	loadable=true;
}

function clickFunction(data){
	var selection = tempGrid.getSelectionModel().getSelection()[0];//得到选中的记录
	editor.html(editor.html() + selection.data.parameter);
}

function loadForm(){
	if(typeof(dictEntryId) != 'undefined') {
		tempFormPanel.form.load( {
	        url:formUrl,
	        params:{dictEntryId:dictEntryId},
	        failure:function(form,action) {
	            alert("err 155");
	        },
	        success:function(form,action){
		        if(form.getValues().content != ''){
		        	editor.html(form.getValues().content);
			    }else{
			    	editor.html('');
				}
		        
	        }
	    });
	}
}
/***function end***/

/***Ext.onReady start***/
Ext.onReady(function(){
	taskTree = Ext.create('FHD.ux.TreePanel',{
        useArrows: true,
        rootVisible: false,
        split: true,
        width:220,
        collapsible : true,
        region: 'west',
        multiSelect: true,
        rowLines:false,
        singleExpand: false,
        checked: false,
		url: queryUrl,//调用后台url
		height:FHD.getCenterPanelHeight(),
		listeners : {
 			'itemclick' : function(view,re){
				var form = tempFormPanel.getForm();
				editor.html(form.items[1].value);
					dictEntryId = re.data.id;
					tempGrid.store.proxy.url = tempGridqueryUrl + '?dictEntryId=' + dictEntryId;
					tempGrid.store.load();
					loadForm();
  			}
 		}
	});
	
	taskTree.store.on('beforeload', function (store, options) {
    	    var new_params = {dictEntryId:dictEntryId};
    	    Ext.apply(store.proxy.extraParams, null);
	        Ext.apply(store.proxy.extraParams, new_params);
    });

	var gridColums =[
    	{header:FHD.locale.get('fhd.sys.tempMan.parameter'),dataIndex:'parameter', flex:1},
    	{header:FHD.locale.get('fhd.sys.tempMan.describe'),dataIndex:'describe', flex:1}
    ];
    
	tempGrid = Ext.create('FHD.ux.GridPanel',{//实例化一个grid列表
		cols:gridColums,//cols:为需要显示的列
		checked:false,
		searchable:false,
		pagable:false,
		listeners:{itemdblclick:clickFunction}//双击执行修改方法
	});

	var formText = [//form表单的列
       	{
           	xtype:'textfield',
           	fieldLabel:FHD.locale.get('fhd.sys.tempMan.name')+'<font color=red>*</font>',
           	name:'name',
           	allowBlank:false
        },			
		{
        	xtype:'textarea',
	        fieldLabel:FHD.locale.get('fhd.sys.tempMan.content')+'<font color=red>*</font>',
	        name:'content',
	        id:'content'
        },
		{xtype:'hidden', value:'', name:'state',labelHiden:true},
		{xtype:'hidden', name:'id',labelHiden:true},
		{xtype:'hidden', name:'dictEntryId',labelHiden:true}
    ];

    var formGrid = [
    	tempGrid
    ];

    var formButtons = [
    	{
  			text:FHD.locale.get('fhd.common.save'),
  			id:"btnEditContentCn",
  			handler:function(){
  				var form = tempFormPanel.getForm();
  				if(form.isValid()){
  	  				debugger;
  					//保存
  					FHD.submit({
  						form:form,
  						url:tempMan_saveUrl + '?contentEdit=' + encodeURI(editor.html()),
  						callback:function(data){
	  						if(data){
	  							loadForm();
	  	  					}
  						}
      				});
  						
  				}				
  			}
  		}
     	,
  		{
			text:FHD.locale.get('fhd.common.cancel'),
			handler:function(){
     			loadForm();
			}
		}
    ];
	
    tempFormPanel = Ext.create('Ext.form.Panel',{
    	bodyPadding:5,
    	region:'center',
		autoScroll:true,
		listeners:{
        	render:function(){
		        setTimeout(function(){
		        	editor = KindEditor.create('#' + (Ext.getCmp("content").getEl().query('textarea')[0]).id);
		        	editor.resizeType = 1;
		        });
	        }  
		},
    	items: [ 
    		{
        		xtype: 'fieldset',
    			defaults:{columnWidth: 1/1,labelWidth:46,margin: '3 3 3 3'},
    			layout:{type: 'column'},
    			title:FHD.locale.get('fhd.common.baseInfo'),
    			items:formText
    		},
    		{
        		xtype: 'fieldset',
        		defaults:{columnWidth: 1/1,labelWidth:46,margin: '3 3 3 3'},
    			layout:{type: 'column'},
    			title:FHD.locale.get('fhd.sys.tempMan.gridTitle'),
    			items:formGrid
    		}
    	],
    	buttons:formButtons
	});
	
	var panelDemopanel=Ext.create('demopanel',{
		items:[tempFormPanel]
	});
	
	tempManPanel = Ext.create('Ext.panel.Panel',{
	    renderTo: 'tempMan${param._dc}', 
	    demopanel:panelDemopanel,
        height:FHD.getCenterPanelHeight(),
        border:false,
		layout: {
	        type: 'border',
	        padding: '0 0 5	0'
	    },
	    defaults: {
            border:true
        },
	    items:[taskTree,tempFormPanel]
	});

	
});

FHD.componentResize(tempManPanel,0,0);//参数为：需要自适应的对象，需要减去的宽度，需要减去的高度

/***Ext.onReady end***/

</script>
</head>
<body>
	<div id='tempMan${param._dc}'></div>
</body>
</html>
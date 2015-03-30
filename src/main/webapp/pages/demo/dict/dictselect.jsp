<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>公司选择</title>
<script type="text/javascript">
	Ext.onReady(function(){
	  var b1 =Ext.create('FHD.ux.dict.DictRadio',
		{
	name:'b1',dictTypeId:'0locale',labelAlign:'right',columns:5,fieldLabel : '单选框'
});

    var b2 =Ext.create('FHD.ux.dict.DictCheckbox',
		{
	name:'b2',dictTypeId:'0locale',labelAlign:'right',columns:5,fieldLabel : '复选框'
});

    
    var b3 =Ext.create('FHD.ux.dict.DictSelect',
		{
	name:'b3',dictTypeId:'0locale',labelAlign:'right', fieldLabel : '下拉多选',maxHeight:80
});	
    var b4 =Ext.create('FHD.ux.dict.DictSelect',
    		{
    	name:'b4',dictTypeId:'0locale',labelAlign:'right',fieldLabel : '下拉单选',multiSelect:false
    });
    var b5 =Ext.create('FHD.ux.dict.DictSelectForEditGrid',
    		{
    	name:'b5',dictTypeId:'0locale',labelAlign:'right',fieldLabel : '可编辑列表下拉多选',multiSelect:true
    });
    var b6 =Ext.create('FHD.ux.dict.DictSelectForEditGrid',
    		{
    	name:'b6',dictTypeId:'0locale',labelAlign:'right',fieldLabel : '可编辑列表下拉单选'
    });

    var writes =Ext.create('Ext.Button',
		{
	text:'设置值',
	width:150,
	//iconCls:'icon-collapse-all',
	listeners:{
		beforerender:function(thiz){
			
		}
	},
	handler:function(){
	    b1.setValue('zh-cn');
	    b2.setValue('en,zh-cn');
		b3.setValue(['en','zh-cn']);
		b4.setValue(['en']);
		b5.setValue(['en','zh-cn']);
		b6.setValue(['en']);
	}
});
    
  /*   var read =Ext.create('Ext.Button',
		{
	text:'获得值',
	width:150,
	//iconCls:'icon-collapse-all',
	listeners:{
		beforerender:function(thiz){
			
		}
	},
	handler:function(){
	    
	    alert(b1.getValue());
	    alert(b2.getValue());
	    alert(b3.getValue());
		 
	}
}); */

		var dictpanel = Ext.create('Ext.form.Panel',{
			bodyPadding: 5,
			renderTo:'demos',
			height:FHD.getCenterPanelHeight(),
			items: [
					{xtype: 'fieldset',
						defaults: {columnWidth: 1/1,margin:'5 5 5 5'},
						layout: {type: 'column'},
						bodyPadding: 5,
						collapsed: false,
						collapsible: false,
						title: '以数据字典“语言”为例',
						items:[
					b1,b2,b3,b4,b5,b6
						]
					}
					,writes//,read
			      ]
		});
		tree.on('collapse',function(p){
			dictpanel.setWidth(panel.getWidth()-26-5);
		});
		tree.on('expand',function(p){
			dictpanel.setWidth(panel.getWidth()-p.getWidth()-5);
		});
		panel.on('resize',function(p){
			dictpanel.setHeight(p.getHeight()-5);
			if(tree.collapsed){
				dictpanel.setWidth(p.getWidth()-26-5);
			}else{
				dictpanel.setWidth(p.getWidth()-tree.getWidth()-5);
			}
		});
});
</script>
</head>
<body>
	<div id='demos'></div>
</body>
</html>
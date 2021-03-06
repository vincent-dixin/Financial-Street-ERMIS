<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

</head>
<body>
	<div >
	<div id="ftlgrid"></div>
	<div id="button-container"></div>
	</div>
</body>
<script type="text/javascript">	
var mv_grid;
	//var datas=${datas};
	/*我的ext-json-grid.js代码*/  
	Ext.onReady(function() {   
  	
  	var toolbar = new Ext.Toolbar( {
			height : 30,
			hideBorders : true,
			buttons : [{text : '保存',handler : setUserRemind,iconCls:'icon-add'},
						{text : '刷新项目',handler : resetUserRemind,iconCls:'refresh'}
			]
		});
		
		var filterSelModel = new Ext.grid.CheckboxSelectionModel();
        
    	
    
    // 定义reader   
        var reader = new Ext.data.JsonReader( 
        {id : 'id'},
        [{name : 'id'}, 
        {name : 'remindItem'}, 
        {name : 'remindftl'},
        {name : 'remindEmail'}, 
        {name : 'remindMobile'}])   
        // 构建Store   
        var proxy = new Ext.data.HttpProxy( {   
        	url : '${ctx}/sys/sysapp/queryRemind.do'  
    	}); 
    	 
    
        var store = new Ext.data.Store( {   
            proxy : proxy,  
            reader : reader   
        });   
        // 载入   
        store.load();   
        // create the grid  
        var grid = new Ext.grid.GridPanel({
    	   	autoScroll:true, //滚动条
    	  	loadMask:{msg:"数据加载中..."},
    	  	region: 'center',
    	  	frame : true,
    	  	tbar:toolbar,
    	  	store: store,
    	  	//sm:filterSelModel,//checkbox 多选
    	  	columns : [ 
			new Ext.grid.RowNumberer({header:'序',width:20}),
    	  	//filterSelModel,//checkbox 显示
    	  	{header : "id",width : 0,dataIndex : 'id'},
    	  	{header : "提醒项目",width : 400,dataIndex : 'remindItem',sortable : true},
    	  	{header : "消息模板",width : 200,dataIndex : 'remindftl',sortable : true},
    	  	{header : "邮件",width : 100,dataIndex : 'remindEmail',sortable : true,
    	  	 	renderer:function(val, metaData, record)
    	  	 	{var ischk = (val==1)?"checked":" ";
    	  	 	var id=record.get('id');
				return "<input type='checkbox' name='item' value='"+ id +"_email' "+ ischk +">"
				}
    	  	}, 
    	  	{header : "短信",width : 100,dataIndex : 'remindMobile',sortable : true,
    	  	 	renderer:function(val, metaData, record)
    	  	 	{var ischk = (val==1)?"checked":" ";
    	  	 	var id=record.get('id');
				return "<input type='checkbox' name='item' value='"+ id +"_mobile' "+ ischk +">"
				}
    	  	}
    	  	],    
    	  	columnLines: true, //数据间竖线
    	  	trackMouseOver : true, //鼠标特效  
    	  	stripeRows : true, //斑马线   
    	  	width:1050,
    		height:235, 
	       	title:'消息提醒设置',
    	  	viewConfig: {
    	  		scrollOffset:0,
    	  		autoFit: true,
    	  		sortAscText: '升序',
	    		columnsText: '显示列',
	    		sortDescText: '降序'
	    	},
	       	renderTo: 'ftlgrid', 
	       	clicksToEdit:'1',
	       	margins: '0 5 5 5',
	        forceFit:true, //Ture表示自动扩展或缩小列的宽度以适应grid的宽度,从而避免出现水平滚动条.   
	       	frame:true
	    });
	    
	   mv_grid=grid;
    });  
	
	
//保存
	function setUserRemind(){
	
	/*
		var rows =mv_grid.getSelectionModel().getSelections();
		if(rows.length==0){
			Ext.MessageBox.alert('信息提示', '<spring:message code="removeTip"/>'); 
			return;
		}
		*/
		
		Ext.MessageBox.confirm('提示','你确定更新设置吗？',function(btn){
			if(btn=='yes')
			{
				var chboxs = document.getElementsByName("item");
				var bakstr="";
				for(var i=0;i<chboxs.length;i++){
					if(chboxs[i].checked==true){
						bakstr=bakstr+","+chboxs[i].value;
					}
				}
				FHD.ajaxReq('${ctx}/sys/sysapp/setUserRemind.do',{items:bakstr},function(data){
					if(data!='true')
						Ext.MessageBox.alert('信息提示','操作失败');
					else 
						Ext.MessageBox.alert('信息提示','操作成功');
						//mv_grid.store.reload();
				});
			}
			
		});
						
		
		                                  
	}
	
	//数据初始化 所有设置为空
	function resetUserRemind(){
		var resetproxy = new Ext.data.HttpProxy( {   
	        	url : '${ctx}/sys/sysapp/queryNewRemind.do'  
	    	});
		mv_grid.store.proxy=resetproxy;
		mv_grid.store.reload();
	}
	
	
	setTimeout(function(){
 		
       }, 2000);
	</script>
</html>
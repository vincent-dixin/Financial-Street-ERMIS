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
			buttons : [{
				text : '添加',
				handler : addfile,
				iconCls:'icon-add'
			},{
				text : '删除',
				handler : removeFile,
				iconCls:'icon-del'
			}]
		});
		
		var filterSelModel = new Ext.grid.CheckboxSelectionModel();
        
    	
    
    // 定义reader   
        var reader = new Ext.data.JsonReader( {   
            id : 'filename'  
        }, [ {   
            name : 'filename'  
        }, {   
            name : 'leng'  
        }, {   
            name : 'datadefine'  
        }, {   
            name : 'filepixname'  
        }])   
        // 构建Store   
        var proxy = new Ext.data.HttpProxy( {   
        	url : '${ctx}/sys/sysapp/getftljsonlist.do'  
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
    	  	sm:filterSelModel,
    	  	columns : [ 
			new Ext.grid.RowNumberer({header:'序',width:20}),
    	  	filterSelModel,
    	  	{
    	  		header : "id",   
                width : 0,   
                dataIndex : 'filename',   
                sortable : true 
    	  	},
    	  	{   
    	  	
                header : "模板文件名",   
                width : 100,   
                dataIndex : 'filename',   
                sortable : true  
            }, {   
                header : "模板大小",   
                width : 100,   
                dataIndex : 'leng',   
                sortable : true  
            }, {   
                header : "数据源定义",   
                width : 100,   
                dataIndex : 'datadefine',   
                sortable : true  
            }, {   
                header : "操作",   
                width : 200,   
                sortable : true,
                dataIndex : 'filepixname',
                renderer:function(val){
			return "<a href='javascript:void(0)' onclick='javascript:viewFtl(\""+val+"\");'>查看模板</a>"
			+"&nbsp;||&nbsp;"
			+"<a href='javascript:void(0)' onclick='javascript:viewDataDefine(\""+val+"\");'>查看数据定义</a>"
		}
            }],    
    	  	columnLines: true, //数据间竖线
    	  	trackMouseOver : true, //鼠标特效  
    	  	stripeRows : true, //斑马线   
    	  	width:1050,
    		height:235, 
	       	title:'计划值',
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
	    /*
        var grid = new Ext.grid.GridPanel( {   
            store : store,   
            columns : [ {   
                header : "模板文件名",   
                width : 110,   
                dataIndex : 'filename',   
                sortable : true  
            }, {   
                header : "模板大小",   
                width : 120,   
                dataIndex : 'leng',   
                sortable : true  
            }, {   
                header : "数据源定义",   
                width : 120,   
                dataIndex : 'datadefine',   
                sortable : true  
            }],   
            renderTo : 'ftlgrid',   
            width : 540,   
            height : 200,
            columnLines: true   
        });   
  		*/
    });  
	
	function viewFtl(val){
		//alert(val);
		FHD.openWindow('模板查看', 650,530, "${ctx}/sys/sysapp/viewfile.do?type=ftl&filename="+val);
	}
	function viewDataDefine(val){
		//alert(val)
		FHD.openWindow('数据定义查看', 650,530, "${ctx}/sys/sysapp/viewfile.do?type=properties&filename="+val);
	}
	function addfile(){
		openFileUploadWindow(500,300,'uploadcallback');
	}
	function uploadcallback(val){
		FHD.ajaxReq('${ctx}/sys/sysapp/transferfile.do',{fileid:val},function(data){
					if(data!='true')
						Ext.MessageBox.alert('信息提示','操作失败');
					else 
						Ext.MessageBox.alert('信息提示','操作成功');
						mv_grid.store.reload();
				});
	}
//删除
	function removeFile(){
	
	
		var rows =mv_grid.getSelectionModel().getSelections();
		if(rows.length==0){
			Ext.MessageBox.alert('信息提示', '<spring:message code="removeTip"/>'); 
			return;
		}
		
		Ext.MessageBox.confirm('提示','你确定要删除？',function(btn){
			if(btn=='yes')
			{
				var ids = '';
				for(var i=0;i<rows.length;i++)
					ids+=rows[i].get('filepixname')+',';
				FHD.ajaxReq('${ctx}/sys/sysapp/delfile.do',{ids:ids},function(data){
					if(data!='true')
						Ext.MessageBox.alert('信息提示','操作失败');
					else 
						Ext.MessageBox.alert('信息提示','操作成功');
						mv_grid.store.reload();
				});
			}
			else
			{
				return;
			}
		});
						
		
		                                  
	}
	
	</script>
</html>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>index</title>
</head>
<body style="overflow: hidden;"> 
	<div id="hisGridId" style="overflow:auto;width:100%;"></div>
	<script type="text/javascript">
		var hisDs;
		Ext.onReady(function(){
			Ext.QuickTips.init();
			
			var hisCol = new Ext.grid.ColumnModel([ 
			  	new Ext.grid.RowNumberer({
					header: "序号",
				  	width: 40,
				  	renderer:function(value,metadata,record,rowIndex){
				   		return record.store.lastOptions.params.start + 1 + rowIndex;
				  	}
				}), 
			   	{id:'gatherBy',header: "员工", dataIndex: 'gatherBy', sortable: true, width: 100},
			   	/* {id:'ea_Operate',header: "操作", dataIndex: 'ea_Operate', sortable: true, width: 120}, */
			   	{id:'ea_Content',header: "意见", dataIndex: 'ea_Content', sortable: true, width: 300,
		        renderer:function(value, cellmeta, record, rowIndex, columnIndex, store){
					return '<div ext:qtitle="" ext:qtip="' + value + '">'+ value +'</div>'; 
		        }},
			   	{id:'ea_Date',header: "时间", dataIndex: 'ea_Date', sortable: true, width: 120}
			]); 
			var hisProxy = new Ext.data.HttpProxy({
				method:'POST',
				url:'${ctx}/jbpm/getEAI.do?pid=${param.pid}'
			});
			var hisReader = new Ext.data.JsonReader({
			 	idProperty:'id',
				root:'datas',
				totalProperty:'totalCount',
				fields: ['ea_Content','ea_Operate','ea_Date','gatherBy','index']
			});
			hisDs = new Ext.data.Store({
			 	autoDestroy: true,
		    	proxy: hisProxy,
		        reader: hisReader,
		        sortInfo:{field:'index', direction:'DESC'}    // 排序信息 
			}); 
				 
			var grid = new Ext.grid.GridPanel({
			 	id:'hisEventGridId',
				el:'hisGridId',viewConfig: {forceFit:true},
				height:jQuery(window).height(),
				width:'auto',
				stripeRows:true,
				ds:hisDs,
				autoscroll:false,
				loadMask:true,
				cm:hisCol,
				bbar: new Ext.PagingToolbar({
		            pageSize: 20,
		            store: hisDs,
		            displayInfo: true, 
		            displayMsg: '显示{0} - {1}条 共{2}条记录', 
		            emptyMsg: "无记录显示"
		        })			
			 }); 
			 grid.render();
			
		 	 // 数据重新加载
		  	hisDs.load({
			  	params:{
			   		start:0,
			   		limit:20		    		
			   	}
			});
		});
		
		function reflush(){
			window.location.reload();
		}
		
		function viewPic(){
			var rows =Ext.getCmp('hisEventGridId').getSelectionModel().getSelections(); 
			if(rows.length==0){
				Ext.MessageBox.alert('警告', '最少选择一条信息，进行修改!'); 
				return;
			}
			var link2=rows[0].get("link2");
			//alert(link2);
			eval(link2);
		}
	    
	    function reloadStore(){
	    	hisDs.reload();
	    }
    </script>
</body>
</html>




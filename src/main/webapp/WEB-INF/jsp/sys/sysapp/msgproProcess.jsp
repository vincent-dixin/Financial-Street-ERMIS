<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

</head>
<body>

	<div style="text-align: center">
	<input type="hidden" value="${ftltype}" id="ftltype" name="ftltype"/>
	<div id="prop-grid"></div>
	<div id="button-container"></div>
	</div>
</body>
<script type="text/javascript">	
var ftltype=document.getElementById("ftltype").value;

	Ext.onReady(function(){
		var keyArray=new Array();
		var valueArray=new Array();
	    var json=${source};
	    
	     var propsGrid = new Ext.grid.PropertyGrid({
	        renderTo: 'prop-grid',
	        width: 600,
	        autoHeight: true,
	        propertyNames: {
	            tested: 'QA',
	            borderWidth: 'Border Width'
	        },
	        source: json,
	        viewConfig : {
	            forceFit: true,
	            scrollOffset: 2 // the grid will never have scrollbars
	        }
	    });
	    
	    
	    // simulate updating the grid data via a button click
	    new Ext.Button({
	        renderTo: 'button-container',
	        text: '确认修改',
	        handler: function(){
	        	
	        	
	        	var store = propsGrid.getStore();
				for(var i = 0; i < store.getTotalCount(); i ++){
					var record = store.getAt(i);
					var key = record.get("name");
					var value = record.get("value");
					
					keyArray.push(key);
					valueArray.push(value);
					
				}
	        	
	        	propsGrid.stopEditing();
	        	
	        	FHD.ajaxReq('${ctx}/sys/sysapp/process.do',
	        	{keyArray:keyArray.toString(),
				valueArray:valueArray.toString(),
				ftltype:ftltype},
	        	function(data){
					if(data=='false'){
						Ext.MessageBox.alert('信息提示','操作失败');
						valueArray=new Array();
						keyArray=new Array();
					}
					else 
					{
						parentWindow().mailcontent.innerText=data;
						
	         			parent.Ext.MessageBox.alert('提示', '操作成功.');
	         			closeWindow();//关闭窗口
						
					}
				});
				
	        	
	        }
	    });
	    
	    
	});
	
	
	
	</script>
</html>
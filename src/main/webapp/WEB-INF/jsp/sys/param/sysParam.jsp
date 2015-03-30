<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body >
	
	<div id="prop-grid"></div>
	<div id="button-container" style="text-align:center;"></div>
</body>
<script type="text/javascript">	
	Ext.onReady(function(){
		var keyArray=new Array();
		var valueArray=new Array();
	    var json=${source};
	    
	    var propsGrid = new Ext.grid.PropertyGrid({
	        renderTo: 'prop-grid',
	        width: document.body.offsetWidth,
	        height:450,
	        //autoHeight: true,
	        
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
	    
	    
	    propsGrid.on('afteredit',afterEdit);
		function afterEdit(e)
		{
			//alert(e.record.get('name')+" is modified:"+e.value);
			keyArray.push(e.record.get('name'));
			//alert(keyArray.length);
			valueArray.push(e.value);
			//alert(valueArray.length);
		}
	    // simulate updating the grid data via a button click
	    new Ext.Button({
	        renderTo: 'button-container',
	        text: '确认修改',
	        handler: function(){
	        	if(keyArray.length==0)
	        	{
	        		Ext.Msg.alert('提示','没有修改任何数据！');
	        		return;
	        	}
	        	propsGrid.stopEditing();
				Ext.Ajax.request({
					url:'${ctx}/sys/param/modifySysParam.do',
					success:function(response){
						if('true'==response.responseText)
						{
							Ext.Msg.alert('提示','修改成功！');
						}
						else
						{
							Ext.Msg.alert('提示','修改失败！');
						}
						keyArray=new Array();
						valueArray=new Array();
					},
					failure:function(){
						Ext.Msg.alert('提示','修改失败！！');
						keyArray=new Array();
						valueArray=new Array();
					},
					params:{
						keyArray:keyArray.toString(),
						valueArray:valueArray.toString()
					}
				});
	        }
	    });
	});
	</script>
</html>
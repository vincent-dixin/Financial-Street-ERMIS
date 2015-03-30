<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<html>
<base target="_self">
<head>
	<title>选择预警方案</title>
	<style type="text/css">
		.selectorTable{
			border-collapse:inherit;
			width:100%;
			padding: 0; 
		    margin: 0; 
			border-left: 1px solid #C1DAD7; 
		}
		
		.selectorTable th { 
		    font: bold 12px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif; 
		    color: #666; 
		    border-right: 1px solid #C1DAD7; 
		    border-bottom: 1px solid #C1DAD7; 
		    border-top: 1px solid #C1DAD7; 
		    letter-spacing: 2px; 
		    text-transform: uppercase; 
		    padding: 6px 6px 6px 12px; 
		    background: #CAE8EA url(images/bg_header.jpg) no-repeat; 
		} 
		
		.selectorTable td { 
		    border-right: 1px solid #C1DAD7; 
		    border-bottom: 1px solid #C1DAD7; 
		    font-size:12px; 
		    color: #666; 
		}
		
		.fhd_btn{
			height:20px;
			BORDER-RIGHT: #7b9ebd 1px solid; 
			PADDING-RIGHT: 5px; 
			BORDER-TOP: #7b9ebd 1px solid; 
			PADDING-LEFT: 5px; 
			FONT-SIZE: 12px; 
			FILTER: progid:DXImageTransform.Microsoft.Gradient(GradientType=0, StartColorStr=#ffffff, EndColorStr=#cecfde); 
			BORDER-LEFT: #7b9ebd 1px solid; 
			CURSOR: hand; COLOR: black; 
			PADDING-TOP: 2px; 
			BORDER-BOTTOM: #7b9ebd 1px solid;
		}
	</style>
	<script type="text/javascript">
		var selected=',${param.selects},';
	    var hisDs;
	    var grid;
	    var listGrid;
	    var tags = '${tag}';
		var parentTag = 'div'+tags;
	    function confirmSelect(){
	   		var P = parentWindow();
	   		P.document.getElementById(parentTag).innerHTML="";
			P.document.getElementById(parentTag).innerHTML=document.getElementById('list').innerHTML;
			closeWindow();
		}
		function checkedNode(id,name){
			list.innerHTML="&nbsp;<input type='radio' checked='checked' onClick='javascript:removeCheckedRisk(\""+tags+"\");' name='" + tags + "' value='"+ id + "' />"+ FHD.util.StringUtil.shortString(name,20) + "<br/>";
		}
		function removeCheckedRisk(tag){
			if(document.getElementById(tag) == null){
				list.innerHTML="";
			}
		}
	    var sm=new Ext.grid.CheckboxSelectionModel({singleSelect:true});
	    sm.singleSelect= true;	
		Ext.onReady(function(){
			var headers = [new Ext.grid.RowNumberer({
				  header : "序",
				  width : 40,
				  renderer:function(value,metadata,record,rowIndex){
				  	return record.store.lastOptions.params.start + 1 + rowIndex;
				  }
			   }),{header:'预警区间名称',dataIndex:'warningRegionName',width: 150,renderer: formatQtip},{header:'最小值',dataIndex:'minValue',width: 60},{header:'包含最小值',dataIndex:'isContainMinName', width:80},{header:'最大值',dataIndex:'maxValue',width: 60},{header:'包含最大值',dataIndex:'isContainMaxName',width:80},{header:'描述',dataIndex:'warningRegionDesc',renderer: formatQtip},{header:'预警状态',dataIndex:'warningStatus', width:60,renderer:function(value){
				    return getColor(value);
	        }}];
			function formatQtip(data,metadata){ 
			    var title ="";
			    var tip =data; 
			    metadata.attr = 'ext:qtitle="' + title + '"' + ' ext:qtip="' + tip + '"';  
			    return data;  
		    }  
			//listGrid = new FHD.ext.Grid('gridList','',headers,null,'预警区间列表',640,250,'${ctx}/kpi/target/showWarningRegion.do?',true,false,null).getGrid();

			var col = new Ext.grid.ColumnModel([ 
   				new Ext.grid.RowNumberer({
   					header : "序",
   				  	width : 20,
   				  	renderer:function(value,metadata,record,rowIndex){
   				  		return 1 + rowIndex;
   				  	}
   			   	}),
	   			{header:'预警区间名称',dataIndex:'warningRegionName',width: 150,renderer: formatQtip},
	   			{header:'最小值',dataIndex:'minValue',width: 60},
	   			{header:'包含最小值',dataIndex:'isContainMinName', width:80},
	   			{header:'最大值',dataIndex:'maxValue',width: 60},
	   			{header:'包含最大值',dataIndex:'isContainMaxName',width:80},
	   			{header:'描述',dataIndex:'warningRegionDesc',renderer: formatQtip},
	   			{header:'预警状态',dataIndex:'warningStatus', width:60,renderer:function(value){
	   				return getColor(value);
	 	        }}
   		   	]); 
   			var proxy = new Ext.data.HttpProxy({
   				 method:'POST',
   				 url:'${ctx}/kpi/target/showWarningRegion.do'
   			});
   			var reader = new Ext.data.JsonReader({
   				idProperty:'id',
   				root:'datas',
   				totalProperty:'totalCount',
   				fields: ['id','warningRegionName','minValue','isContainMinName','maxValue','isContainMaxName','warningRegionDesc','warningStatus','sort']
   			});
   			store = new Ext.data.Store({
   			 	autoDestroy: true,
   		    	proxy: proxy,
   		        reader: reader,
   		        sortInfo:{field:'minValue', direction:'ASC'}    // 排序信息 
   			}); 
   			listGrid = new Ext.grid.GridPanel({
   				el:'gridList',
   				height:250,
   				width:640,
   				stripeRows:true,
   				ds:store,
   				cm:col,
   				title:'预警区间列表',
   				loadMask:true		
   			}); 
   			listGrid.render();
			/*******************************预警方案******************************/
			Ext.QuickTips.init();
			var hisCol = new Ext.grid.ColumnModel([ 
			   new Ext.grid.RowNumberer({
				  header : "序",
				  width : 20,
				  renderer:function(value,metadata,record,rowIndex){
				  	return record.store.lastOptions.params.start + 1 + rowIndex;
				  }
			   }),
			   sm,
			   {id:'warningName',header: "方案名称", dataIndex: 'warningName', sortable: true, width: 100,renderer: formatQtip},
			   {id:'warningDesc',header: "方案说明", dataIndex: 'warningDesc', sortable: true, width: 100,renderer: formatQtip},
			   {id:'dataType',header: "数据类型", dataIndex: 'dataType', sortable: true, width: 70, renderer:function(value){
		   			var dataTypeName = "完成值";//0=完成值；1=评估值；2=差值；3=累积值；4=预期值；5=当期完成率；6=累积完成率
		   			if(value != null && value =="1"){
		   				dataTypeName = "评估值";
		   			}else if(value != null && value =="2"){
		   				dataTypeName = "差值";
		   			}else if(value != null && value =="3"){
		   				dataTypeName = "累积值";
		   			}else if(value != null && value =="4"){
		   				dataTypeName = "预期值";
		   			}else if(value != null && value =="5"){
		   				dataTypeName = "当期完成率";
		   			}else if(value != null && value =="6"){
		   				dataTypeName = "累积完成率";
		   			}else if(value != null && value =="7"){
		   				dataTypeName = "累积差异";
		   			}
		        	return dataTypeName;
		        }}
		   	]); 
			var hisProxy = new Ext.data.HttpProxy({
				 method:'POST',
				 url:'${ctx}/kpi/target/getWarningPlanDatas.do?id=${targetId}&dataType=${dataType}'
			});
			var hisReader = new Ext.data.JsonReader({
				idProperty:'id',
				root:'datas',
				totalProperty:'totalCount',
				fields: ['id','warningName','warningDesc','dataType','programTypeName','startDate','endDate','programDesc','createBy']
			});
			hisDs = new Ext.data.Store({
			 	autoDestroy: true,
		    	proxy: hisProxy,
		        reader: hisReader,
		        sortInfo:{field:'createBy', direction:'DESC'}    // 排序信息 
			}); 
			grid = new Ext.grid.GridPanel({
			 	id:'hisEventGridId',
				el:'hisGridId',
				height:570,
				width:340,
				stripeRows:true,
				ds:hisDs,
				cm:hisCol,
				sm:sm,
				loadMask:true		
			}); 
		 	grid.render();
		    // 数据重新加载
		    hisDs.load({
		    	params:{
		    		start:0,
		    		limit:20			    		
		    	}
			});
	    	grid.getSelectionModel().on('rowselect',function(model,index,record){
	    		listGrid.getStore().load({params:{id:record.get('id')}});
	    		checkedNode(record.get('id'),record.get('warningName'));
	    	});
	    	grid.getSelectionModel().on('rowdeselect',function(model,index,record){removeCheckedRisk(record.get('id'));});
	    	grid.getStore().on('load',function(store,records,op){
	   			var rows=new Array();
	   			for(var i=0;i<records.length;i++){
	   				var strArray = document.getElementById(tags).value;
	   				if(strArray == records[i].get('id')){
	   					rows[rows.length]=records[i];
	   				}
	   			}
	   			grid.getSelectionModel().selectRecords(rows,true);
	   		});
		});
	</script>
</head>
<body>
	<div id="hisGridId" style= "float:left;height:590px;width:400px"></div>
	<div style="float:left;width:570px%">
		<table class="selectorTable">
			<tr><td colspan="2" ><div id='gridList'></div></td></tr>
			<tr><th  height="25px" colspan="2" align="center" valign="middle">选择结果</th></tr>
			<tr bgcolor="#F7FDFD" >
				<td bgcolor="#F7FDFD" height="25px" align="center" valign="middle">
					<a href="javascript:removeCheckedRisk('');">清空</a>
				</td>
				<td width="475px" align="center" valign="middle">预警方案列表</td>
			</tr>
			<tr>
				<td height="200px" colspan="2" align="left" valign="top" id="chooseValues">
					<div id='list' style='height:200px;overflow-y:auto'>
						<c:if test="${param.warnName!=null && param.warnName!=''}">
							<input type='radio' checked='checked' onClick='javascript:removeCheckedRisk('');' name='${tag}' value='${choosedWarnIds}' />${warnName}
						</c:if>  
					</div>
				</td>
			</tr>
			<tr bgcolor="#F7FDFD">
				<td height="52px" colspan="2" align="center" valign="middle">
					<input type="button" value="确定" onclick="confirmSelect()" class="fhd_btn"/>&nbsp;
					<input type="button" value="取消" onclick="closeWindow()" class="fhd_btn"/>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>

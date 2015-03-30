<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css"> 
/* CSS Document */ 

body { 
font: normal 11px auto "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif; 
color: #4f6b72; 
background: #E6EAE9; 
} 

a { 
color: #c75f3e; 
} 

#mytable { 
padding: 0; 
margin: 0; 
} 

caption { 
padding: 0 0 5px 0; 
width: 700px; 
font: italic 11px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif; 
text-align: right; 
} 

th { 
font: bold 11px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif; 
color: #4f6b72; 
border-right: 1px solid #99BBE8; 
border-bottom: 1px solid #99BBE8; 
border-top: 1px solid #99BBE8; 
letter-spacing: 2px; 
text-transform: uppercase; 
text-align: center; 
padding: 6px 6px 6px 12px; 
background: #C6D4E4  no-repeat; 
} 
/*power by www.winshell.cn*/ 
th.nobg { 
border-top: 0; 
border-left: 0; 
border-right: 1px solid #C1DAD7; 
background: none; 
} 

td { 
border-right: 1px solid #99BBE8; 
border-bottom: 1px solid #99BBE8; 
background: #fff; 
font-size:11px; 
padding: 6px 6px 6px 12px; 
color: #4f6b72; 
} 
/*power by www.winshell.cn*/ 

td.alt { 
background: #F5FAFA; 
color: #797268; 
} 

th.spec { 
border-left: 1px solid #C1DAD7; 
border-top: 0; 
background: #fff no-repeat; 
font: bold 10px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif; 
} 

th.specalt { 
border-left: 1px solid #C1DAD7; 
border-top: 0; 
background: #f5fafa no-repeat; 
font: bold 10px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif; 
color: #797268; 
}
tr.over td {     
	background: #D5D5D9;  
}
tr.error td{
	background: #F8AC9E;
}
/*---------for IE 5.x bug*/ 
html>body td{ font-size:11px;} 
body,td,th { 
font-family: 宋体, Arial; 
font-size: 12px; 
} 
</style> 
<script>
	var errorRecordsNum = '${fn:length(risks)}';
    window.top.Ext.MessageBox.confirm = function(title, msg, fn) {  
        this.show({  
            title : title,  
            msg : msg,  
            buttons:{yes:'我要继续导入',no:'取消'},  
            fn : fn,  
            icon : this.QUESTION  
        });  
        return this;  
    };

    $(document).ready(function(){  
        $("tr").mouseover(function(){  
            $(this).addClass("over");  
        }).mouseout(function(){  
            $(this).removeClass("over");  
        });

        FHD.parentWindow().closeTip();

      	//数据导入事件
        $('#import').click(function(){
        	var errorNums = ${errorNum};
        	var msg = "数据检验未通过,无法继续导入！";
        	/*if(errorRecordsNum == errorNums){
        		window.top.Ext.Msg.alert('错误',msg);
				return false;
        	}*/
        	msg = "在进行数据导入时，我们可能会对将要导入的数据进行一些计算,<br>这可能会消耗较长的时间,是否继续?";
            if(errorNums>0){
            	msg = "在对数据的完整行和合法性进行扫描时,<br>我们发现了<b><font color='red'> "+errorNums+" 条错误数据</font></b>(红色部分),<br>若您还要继续导入,错误的数据(红色部分)将不被处理.<br>是否继续?";
            }
            Ext.MessageBox.confirm('数据导入确认', msg, importDatas);
        });

        //进行数据导入操作
        function importDatas(btn){
        	if(btn=="yes"){
        		var msgTip = opWait("","","正在导入数据，请稍候...");
        		// 后台操作更新数据
        		ajaxReq('${ctx}/sys/orgstructure/org/importData.do',{importType:'5'},function(response){
        			msgTip.hide();
        	      	if(response == "true"){
        	      		window.top.Ext.ux.Toast.msg('提示','数据导入成功!<br>请手工刷新页面来查看导入结果.');
        	      		closeWindow();//关闭窗口
        		    }else{
        		    	window.top.Ext.ux.Toast.msg('提示','数据导入失败!');
        		    }
        		});
        	}
        }

        //退出事件
    	$('#cancel').click(function(){
    		closeWindow();//关闭窗口
        });
    });
</script> 
</head>
<body>
<table width="100%" id="mytable" cellspacing="0">
		<caption> </caption>
		<tr align="center">
			<td colspan="3" style="font:30">数据预览</td>
		</tr>
		<tr>
			<th align="center" scope="col">工作组编号</th>
			<th align="center" scope="col">工作组名称</th>
			<th align="center" scope="col">上级工作组名称</th>
		</tr>
		<c:forEach items="${datas}" var="group">
		<tr>
				<td class="row" align="left" title="${group.groupCode}">
				&nbsp;${group.groupCode}
				</td>
				<td class="row" align="left" title="${group.groupName}">
				&nbsp;${group.groupName}
				</td>
				<td class="row" align="left" title="${group.groupDesc}">
				&nbsp;${group.groupDesc}
				</td>
		</tr>
		</c:forEach>
		<c:forEach items="${edatas}" var="group">
		<tr class="error">
				<td class="row" align="left" title="${group.impErrorInfo}">
				&nbsp;${group.groupCode}
				</td>
				<td class="row" align="left" title="${group.impErrorInfo}">
				&nbsp;${group.groupName}
				</td>
				<td class="row" align="left" title="${group.impErrorInfo}">
				&nbsp;${group.groupDesc}
				</td>
			</tr>
		</c:forEach>
</table>
<div align="center">
	<br>
	<input type="button" id="import" value="继续导入" class="fhd_btn"/>
	&nbsp;&nbsp;
	<input type="button" id="cancel" value="取消导入" class="fhd_btn"/>
	<br><br><br>
</div>
</body>
</html>
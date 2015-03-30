<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>设置调度时间</title>
</head>
<body style="overflow-y:hidden;overflow-x:hidden">
	<form:form commandName="scheduledTaskForm">
		<table width="100%" style="margin:5 5 5 5" border="0" cellpadding="2" cellspacing="0">
			<tr>
				<td colspan="5">
					<input type="radio" name="frequency" value="0" id="frequency1" <c:if test="${frequency eq '0' }">checked="checked""</c:if> />单次提醒时间
					<input type="radio" name="frequency" value="1" id="frequency2" <c:if test="${frequency eq '1' }">checked="checked""</c:if> />周期提醒时间
				</td>
			</tr>
			<tr>
				<td colspan="5">
					<hr style="width:95%;height:1px;"></hr>
				</td>
			</tr>
		</table>
		<div id="div1" style="display:block;">
			<table width="100%" style="margin:5 5 5 5" border="0" cellpadding="2" cellspacing="0">
				<tr>
					<td>
						&nbsp;&nbsp;&nbsp;&nbsp;标题：<input style="width:90%;" type="text" id="singleTitle" name="singleTitle" value=""/> 
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>
						&nbsp;&nbsp;&nbsp;&nbsp;提醒时间：
						<form:input path="startTime" onclick="WdatePicker();" cssClass="Wdate" cssStyle="width:123px" onkeydown="return false"/>
						<select id="showTime1" name="showTime" style="width:80px;">
							<option value="00:00:00">00:00:00</option>
							<option value="00:30:00">00:30:00</option>
							<option value="01:00:00">01:00:00</option>
							<option value="01:30:00">01:30:00</option>
							<option value="02:00:00">02:00:00</option>
							<option value="02:30:00">02:30:00</option>
							<option value="03:00:00">03:00:00</option>
							<option value="03:30:00">03:30:00</option>
							<option value="04:00:00">04:00:00</option>
							<option value="04:30:00">04:30:00</option>
							<option value="05:00:00">05:00:00</option>
							<option value="05:30:00">05:30:00</option>
							<option value="06:00:00">06:00:00</option>
							<option value="06:30:00">06:30:00</option>
							<option value="07:00:00">07:00:00</option>
							<option value="07:30:00">07:30:00</option>
							<option value="08:00:00">08:00:00</option>
							<option value="08:30:00">08:30:00</option>
							<option value="09:00:00">09:00:00</option>
							<option value="09:30:00">09:30:00</option>
							<option value="10:00:00">10:00:00</option>
							<option value="10:30:00">10:30:00</option>
							<option value="11:00:00">11:00:00</option>
							<option value="11:30:00">11:30:00</option>
							<option value="12:00:00">12:00:00</option>
							<option value="12:30:00">12:30:00</option>
							<option value="13:00:00">13:00:00</option>
							<option value="13:30:00">13:30:00</option>
							<option value="14:00:00">14:00:00</option>
							<option value="14:30:00">14:30:00</option>
							<option value="15:00:00">15:00:00</option>
							<option value="15:30:00">15:30:00</option>
							<option value="16:00:00">16:00:00</option>
							<option value="16:30:00">16:30:00</option>
							<option value="17:00:00">17:00:00</option>
							<option value="17:30:00">17:30:00</option>
							<option value="18:00:00">18:00:00</option>
							<option value="18:30:00">18:30:00</option>
							<option value="19:00:00">19:00:00</option>
							<option value="19:30:00">19:30:00</option>
							<option value="20:00:00">20:00:00</option>
							<option value="20:30:00">20:30:00</option>
							<option value="21:00:00">21:00:00</option>
							<option value="21:30:00">21:30:00</option>
							<option value="22:00:00">22:00:00</option>
							<option value="22:30:00">22:30:00</option>
							<option value="23:00:00">23:00:00</option>
							<option value="23:30:00">23:30:00</option>
						</select>
					</td>
				</tr>
			</table>
		</div>
		<div id="div2" style="display:none;">
			<table width="100%" style="margin:5 5 5 5" border="0" cellpadding="2" cellspacing="0">
				<tr>
					<td>
						&nbsp;&nbsp;&nbsp;&nbsp;标题：<input style="width:90%;" type="text" id="cycleTitle" name="cycleTitle" value=""/> 
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>
						&nbsp;&nbsp;&nbsp;&nbsp;提醒时间：
						<select id="showTime2" name="showTime"  style="width:80;">
							<option value="00:00:00">00:00:00</option>
							<option value="00:30:00">00:30:00</option>
							<option value="01:00:00">01:00:00</option>
							<option value="01:30:00">01:30:00</option>
							<option value="02:00:00">02:00:00</option>
							<option value="02:30:00">02:30:00</option>
							<option value="03:00:00">03:00:00</option>
							<option value="03:30:00">03:30:00</option>
							<option value="04:00:00">04:00:00</option>
							<option value="04:30:00">04:30:00</option>
							<option value="05:00:00">05:00:00</option>
							<option value="05:30:00">05:30:00</option>
							<option value="06:00:00">06:00:00</option>
							<option value="06:30:00">06:30:00</option>
							<option value="07:00:00">07:00:00</option>
							<option value="07:30:00">07:30:00</option>
							<option value="08:00:00">08:00:00</option>
							<option value="08:30:00">08:30:00</option>
							<option value="09:00:00">09:00:00</option>
							<option value="09:30:00">09:30:00</option>
							<option value="10:00:00">10:00:00</option>
							<option value="10:30:00">10:30:00</option>
							<option value="11:00:00">11:00:00</option>
							<option value="11:30:00">11:30:00</option>
							<option value="12:00:00">12:00:00</option>
							<option value="12:30:00">12:30:00</option>
							<option value="13:00:00">13:00:00</option>
							<option value="13:30:00">13:30:00</option>
							<option value="14:00:00">14:00:00</option>
							<option value="14:30:00">14:30:00</option>
							<option value="15:00:00">15:00:00</option>
							<option value="15:30:00">15:30:00</option>
							<option value="16:00:00">16:00:00</option>
							<option value="16:30:00">16:30:00</option>
							<option value="17:00:00">17:00:00</option>
							<option value="17:30:00">17:30:00</option>
							<option value="18:00:00">18:00:00</option>
							<option value="18:30:00">18:30:00</option>
							<option value="19:00:00">19:00:00</option>
							<option value="19:30:00">19:30:00</option>
							<option value="20:00:00">20:00:00</option>
							<option value="20:30:00">20:30:00</option>
							<option value="21:00:00">21:00:00</option>
							<option value="21:30:00">21:30:00</option>
							<option value="22:00:00">22:00:00</option>
							<option value="22:30:00">22:30:00</option>
							<option value="23:00:00">23:00:00</option>
							<option value="23:30:00">23:30:00</option>
						</select>
					</td>
				</tr>
			</table>
			<fieldset style="margin:5 3 5 5; width: 98%">
				<legend>定期模式</legend>
				<table width="100%" border="0" cellpadding="0" style="margin:5 5 5 5" cellspacing="0">
					<tr>
						<td width="15%" style="vertical-align:top;"><input type="radio" name="mode" value="0" id="mode0" <c:if test="${mode eq '0' }">checked="checked""</c:if> />按小时</td>
					    <td rowspan="5" style="vertical-align:top;"><hr style="width:2px;height:100px; "></hr></td>
					    <td rowspan="5" width="80%" style="vertical-align:top;">
					    	<table id="table1" width="100%" border="0" cellpadding="0" cellspacing="0" style="margin:5 5 5 5;display:block;vertical-align:top;">
					      		<tr>
							        <td colspan="2">
							        	&nbsp;每<input type="text" id="everyHour" name="everyHour" value="${param1}" style="width:40;"/>小时
							        </td>
					      		</tr>
					    	</table>
					    	<table id="table2" width="100%" border="0" cellpadding="0" cellspacing="0" style="margin:5 5 5 5;display:none;vertical-align:top;">
					      		<tr>
							        <td>
							        	<input type="radio" name="everyDay" value="0" id="everyDay1" <c:if test="${param1 eq '0' }">checked="checked""</c:if> />
							        	每<input type="text" id="everyDayParam" name="everyDayParam" value="${param2}" style="width:40;"/>天
							        </td>
					      		</tr>
					      		<tr>
							        <td>
							        	<input type="radio" name="everyDay" value="1" id="everyDay2" <c:if test="${param1 eq '1' }">checked="checked""</c:if> />
							        	每个工作日
							        </td>
					      		</tr>
					    	</table>
					    	<table id="table3" width="100%" border="0" cellpadding="0" cellspacing="0" style="margin:5 5 5 5;display:none;vertical-align:top;">
					      		<tr>
							        <td>
							        	&nbsp;每<input type="text" id="everyWeek" name="everyWeek" value="${param1}" style="width:40;"/>周的:
							        </td>
					      		</tr>
					      		<tr>
							        <td>
							        	<input type="checkbox" name="week" value="SUN" id="SUN" <c:if test="${param2 eq 'SUN' }">checked="checked""</c:if> />星期天
							        	<input type="checkbox" name="week" value="MON" id="MON" <c:if test="${param2 eq 'MON' }">checked="checked""</c:if> />星期一
							        	<input type="checkbox" name="week" value="TUE" id="TUE" <c:if test="${param2 eq 'TUE' }">checked="checked""</c:if> />星期二
							        	<input type="checkbox" name="week" value="WED" id="WED" <c:if test="${param2 eq 'WED' }">checked="checked""</c:if> />星期三
							        	<input type="checkbox" name="week" value="THU" id="THU" <c:if test="${param2 eq 'THU' }">checked="checked""</c:if> />星期四
							        	<input type="checkbox" name="week" value="FRI" id="FRI" <c:if test="${param2 eq 'FRI' }">checked="checked""</c:if> />星期五
							        	<input type="checkbox" name="week" value="SAT" id="SAT" <c:if test="${param2 eq 'SAT' }">checked="checked""</c:if> />星期六
							        </td>
					      		</tr>
					    	</table>
					    	<table id="table4" width="100%" border="0" cellpadding="0" cellspacing="0" style="margin:5 5 5 5;display:none;vertical-align:top;">
					      		<tr>
							        <td>
							        	<input type="radio" name="everyMonth" value="0" id="everyMonth1" <c:if test="${param1 eq '0' }">checked="checked""</c:if> />
							        	每<input type="text" id="everyMonthParam1" name="everyMonthParam" value="${param2}" style="width:40;"/>个月的
							        	<select id="daysParam" name="daysParam" style="width:80;">
							        		<option value="1">第1</option>
							        		<option value="2">第2</option>
							        		<option value="3">第3</option>
							        		<option value="4">第4</option>
							        		<option value="5">第5</option>
							        		<option value="6">第6</option>
							        		<option value="7">第7</option>
							        		<option value="8">第8</option>
							        		<option value="9">第9</option>
							        		<option value="10">第10</option>
							        		<option value="11">第11</option>
							        		<option value="12">第12</option>
							        		<option value="13">第13</option>
							        		<option value="14">第14</option>
							        		<option value="15">第15</option>
							        		<option value="16">第16</option>
							        		<option value="17">第17</option>
							        		<option value="18">第18</option>
							        		<option value="19">第19</option>
							        		<option value="20">第20</option>
							        		<option value="21">第21</option>
							        		<option value="22">第22</option>
							        		<option value="23">第23</option>
							        		<option value="24">第24</option>
							        		<option value="25">第25</option>
							        		<option value="26">第26</option>
							        		<option value="27">第27</option>
							        		<option value="28">第28</option>
							        		<option value="29">第29</option>
							        		<option value="30">第30</option>
							        		<option value="31">第31</option>
							        		<option value="最后一">最后一</option>
							        	</select>
							        	天
							        </td>
					      		</tr>
					      		<tr>
							        <td>
							        	<input type="radio" name="everyMonth" value="1" id="everyMonth2" <c:if test="${param1 eq '0' }">checked="checked""</c:if> />
							        	每<input type="text" id="everyMonthParam2" name="everyMonthParam" value="${param2}" style="width:40;"/>个月的
							        	<select id="severalParam" name="severalParam" style="width:80;">
							        		<option value="第1个">第一个</option>
							        		<option value="第2个">第二个</option>
							        		<option value="第3个">第三个</option>
							        		<option value="第4个">第四个</option>
							        		<option value="最后一个">最后一个</option>
							        	</select>
							        	<select id="weekParam" name="weekParam" style="width:65;">
							        		<option value="SUN">星期天</option>
							        		<option value="MON">星期一</option>
							        		<option value="TUE">星期二</option>
							        		<option value="WED">星期三</option>
							        		<option value="THU">星期四</option>
							        		<option value="FRI">星期五</option>
							        		<option value="SAT">星期六</option>
							        	</select>
							        </td>
					      		</tr>
					    	</table>
					    	<table id="table5" width="100%" border="0" cellpadding="0" cellspacing="0" style="margin:5 5 5 5;display:none;vertical-align:top;">
					      		<tr>
							        <td>
							        	<input type="radio" name="everyYear" value="0" id="everyYear1" <c:if test="${param1 eq '0' }">checked="checked""</c:if> />
							        	每年
							        	<select id="monthParam1" name="monthParam1" style="width:65;">
							        		<option value="1">1月</option>
							        		<option value="2">2月</option>
							        		<option value="3">3月</option>
							        		<option value="4">4月</option>
							        		<option value="5">5月</option>
							        		<option value="6">6月</option>
							        		<option value="7">7月</option>
							        		<option value="8">8月</option>
							        		<option value="9">9月</option>
							        		<option value="10">10月</option>
							        		<option value="11">11月</option>
							        		<option value="12">12月</option>
							        	</select>
							        	<select id="yearDaysParam" name="yearDaysParam" style="width:80;">
							        		<option value="1">1</option>
							        		<option value="2">2</option>
							        		<option value="3">3</option>
							        		<option value="4">4</option>
							        		<option value="5">5</option>
							        		<option value="6">6</option>
							        		<option value="7">7</option>
							        		<option value="8">8</option>
							        		<option value="9">9</option>
							        		<option value="10">10</option>
							        		<option value="11">11</option>
							        		<option value="12">12</option>
							        		<option value="13">13</option>
							        		<option value="14">14</option>
							        		<option value="15">15</option>
							        		<option value="16">16</option>
							        		<option value="17">17</option>
							        		<option value="18">18</option>
							        		<option value="19">19</option>
							        		<option value="20">20</option>
							        		<option value="21">21</option>
							        		<option value="22">22</option>
							        		<option value="23">23</option>
							        		<option value="24">24</option>
							        		<option value="25">25</option>
							        		<option value="26">26</option>
							        		<option value="27">27</option>
							        		<option value="28">28</option>
							        		<option value="29">29</option>
							        		<option value="30">30</option>
							        		<option value="31">31</option>
							        		<option value="最后一">最后一</option>
							        	</select>
							        	日
							        </td>
					      		</tr>
					      		<tr>
							        <td>
							        	<input type="radio" name="everyYear" value="1" id="everyYear2" <c:if test="${param1 eq '0' }">checked="checked""</c:if> />
							        	每年
							        	<select id="monthParam2" name="monthParam2" style="width:65;">
							        		<option value="1">1月</option>
							        		<option value="2">2月</option>
							        		<option value="3">3月</option>
							        		<option value="4">4月</option>
							        		<option value="5">5月</option>
							        		<option value="6">6月</option>
							        		<option value="7">7月</option>
							        		<option value="8">8月</option>
							        		<option value="9">9月</option>
							        		<option value="10">10月</option>
							        		<option value="11">11月</option>
							        		<option value="12">12月</option>
							        	</select>
							        	<select id="yearSeveralParam" name="yearSeveralParam" style="width:80;">
							        		<option value="第1个">第一个</option>
							        		<option value="第2个">第二个</option>
							        		<option value="第3个">第三个</option>
							        		<option value="第4个">第四个</option>
							        		<option value="最后一个">最后一个</option>
							        	</select>
							        	<select id="yearWeekParam" name="yearWeekParam" style="width:65;">
							        		<option value="SUN">星期天</option>
							        		<option value="MON">星期一</option>
							        		<option value="TUE">星期二</option>
							        		<option value="WED">星期三</option>
							        		<option value="THU">星期四</option>
							        		<option value="FRI">星期五</option>
							        		<option value="SAT">星期六</option>
							        	</select>
							        </td>
					      		</tr>
					    	</table>
					    </td>
					</tr>
					<tr>
					    <td><input type="radio" name="mode" value="1" id="mode1" <c:if test="${mode eq '1' }">checked="checked""</c:if> />按天</td>
					</tr>
					<tr>
					    <td><input type="radio" name="mode" value="2" id="mode2" <c:if test="${mode eq '2' }">checked="checked""</c:if> />按周</td>
					</tr>
					<tr>
					    <td><input type="radio" name="mode" value="3" id="mode3" <c:if test="${mode eq '3' }">checked="checked""</c:if> />按月</td>
					</tr>
					<tr>
						<td><input type="radio" name="mode" value="4" id="mode4" <c:if test="${mode eq '4' }">checked="checked""</c:if> />按年</td>
					</tr>
				</table>
			</fieldset>
			<fieldset style="margin:5 5 5 5; width: 100%">
				<legend>重复范围</legend>
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
					    <td>开始日期：</td>
					    <td>
					    	<input type="radio" name="range" value="0" id="range1" <c:if test="${range eq '0' }">checked="checked""</c:if> />永不过期
					    </td>
					 </tr>
					 <tr>
					    <td>
					    	<form:input path="startTime" onclick="WdatePicker();" cssClass="Wdate" cssStyle="width:123px" onchange="isBeginTimeLegal();" onkeydown="return false"/>
					    </td>
					    <td>
					    	<input type="radio" name="range" value="1" id="range2" <c:if test="${range eq '1' }">checked="checked""</c:if> />结束日期：<form:input path="endTime" onclick="WdatePicker();" cssClass="Wdate" cssStyle="width:123px" onchange="islegal();" onkeydown="return false"/>
					    </td>
					 </tr>
				</table>
			</fieldset>
		</div>
		<table width="100%" style="margin:0 5 5 5" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td colspan="5">
					<hr style="width:95%;height:1px;"></hr>
				</td>
			</tr>
			<tr>
				<td colspan="5" align="center">
					<input type="button" id="submits" value="确定" class="fhd_btn"/>
					<input type="button" id="close" value="取消" class="fhd_btn"/>
				</td>
			</tr>
		</table>
	</form:form>
	<script type="text/javascript">
		var P = parentWindow();
		var myDate = new Date(); 
		//获取完整的年份(4位)
		var year = myDate.getFullYear();
		//获取当前月份(0-11,0代表1月)
		var month = myDate.getMonth()+1;
		//获取当前日(1-31) 
		var day = myDate.getDate();
		//获取当前星期X(0-6,0代表星期天) 
		var week = myDate.getDay();
		//获取当前日期所在月的第几周
		var c = weekOfMonth(myDate);
		
		//某日期是本月的第几周
		function weekOfMonth(today) {
			//得到当前日期   
		    //var today = new Date();   
		       
		    var currentDate =  today.getDate();   
		    //得到当月第一天   
		    var firstdayofmonth = new Date();   
		    firstdayofmonth.setTime(today.getTime() - 1000 * 3600 * 24 * (currentDate - 1));   
		       
		    var firstDay = firstdayofmonth.getDay();//第一天的星期数   
		    var firstDate = firstdayofmonth.getDate();//第一天的日期数   
		       
		    var result;   
		    if((currentDate-firstDate) < (6-firstDay)){   
		        result = 1;   
		    }else{   
		        result = 2 + Math.floor((currentDate - firstDate + firstDay - 6)/7);   
		    }
		    return result;
		} 

		//判断开始时间合法性
		function isBeginTimeLegal(){
			var startDate = document.getElementById("startTime").value;
			var endDate = document.getElementById("endTime").value;			
			if(null != startDate && startDate!='' && null != endDate && endDate!=''){
				if(!checkDate(startDate,endDate)){
					Ext.MessageBox.alert("提示", "请选择结束日期大于开始日期!");
					document.getElementById("startTime").value='';
					return false;			
				}
			}
			return true;
		}
		//判断结束时间合法性
		function islegal(){
			var startDate = document.getElementById("startTime").value;
			var endDate = document.getElementById("endTime").value;			
			if(null != startDate && startDate!='' && null != endDate && endDate!=''){
				if(!checkDate(startDate,endDate)){
					Ext.MessageBox.alert("提示", "请选择结束日期大于开始日期!");
					document.getElementById("endTime").value='';
					return false;			
				}
			}
			return true;
		}
		//判断开始时间和结束结束时间
		function checkDate(startDate,endDate){
			var regS = new RegExp("-","gi");
			date1=startDate.replace(regS,"/");
			date2=endDate.replace(regS,"/");	
			var bd =new Date(Date.parse(date1));
			var ed =new Date(Date.parse(date2));
		    if(bd < ed){
				return true;
			}
			return false;
		}
		//周期频率选择
		$('[name=frequency]:radio').change(function(){
			var frequency = $("input[name='frequency'][checked]").val();
			if('0'== frequency){//单次
				document.getElementById("div1").style.display="block";
				document.getElementById("div2").style.display="none";
			}else if('1'== frequency){//周期
				document.getElementById("div1").style.display="none";
				document.getElementById("div2").style.display="block";
				
				//定期模式默认选择
				var mode = $("input[name='mode'][checked]").val();
				if(typeof(mode)=="undefined" || mode==''){
					//默认选择按小时
					jQuery("#mode0").attr("checked","checked");
					jQuery("#everyHour").attr("value","1");
				}else if(mode=='0'){
					jQuery("#mode0").attr("checked","checked");
					jQuery("#everyHour").attr("value","1");
				}
			}
		});
		//定期模式选择
		$('[name=mode]:radio').change(function(){
			var mode = $("input[name='mode'][checked]").val();
			if(mode=="0"){
				document.getElementById("table1").style.display="block";
				document.getElementById("table2").style.display="none";
				document.getElementById("table3").style.display="none";
				document.getElementById("table4").style.display="none";
				document.getElementById("table5").style.display="none";
				//默认每小时的输入框值为1
				var everyHour = jQuery("#everyHour").val();
				if(typeof(everyHour)=="undefined" || everyHour==''){
					jQuery("#everyHour").attr("value","1");
				}
			}else if(mode=="1"){
				document.getElementById("table1").style.display="none";
				document.getElementById("table2").style.display="block";
				document.getElementById("table3").style.display="none";
				document.getElementById("table4").style.display="none";
				document.getElementById("table5").style.display="none";
				//默认选中每几天，输入框值为1
				var everyDay = $("input[name='everyDay'][checked]").val();
				if(typeof(everyDay)=="undefined" || everyDay==''){
					jQuery("#everyDay1").attr("checked","checked");
				}
				var everyDayParam = jQuery("#everyDayParam").val();
				if(typeof(everyDayParam)=="undefined" || everyDayParam==''){
					jQuery("#everyDayParam").attr("value","1");
				}
			}else if(mode=="2"){
				document.getElementById("table1").style.display="none";
				document.getElementById("table2").style.display="none";
				document.getElementById("table3").style.display="block";
				document.getElementById("table4").style.display="none";
				document.getElementById("table5").style.display="none";
				//默认每几周的输入框值为1，选中当天的星期
				var everyWeek = jQuery("#everyWeek").val();
				if(typeof(everyWeek)=="undefined" || everyWeek==''){
					jQuery("#everyWeek").attr("value","1");
				}
				if(week=='0'){
					week="SUN";
				}else if(week=='1'){
					week="MON";
				}else if(week=='2'){
					week="TUE";
				}else if(week=='3'){
					week="WED";
				}else if(week=='4'){
					week="THU";
				}else if(week=='5'){
					week="FRI";
				}else if(week=='6'){
					week="SAT";
				}
				var everyDay1 = jQuery("#everyDay1").val();
				if(typeof(everyDay1)=="undefined" || everyDay1==''){
					jQuery("#"+week).attr("checked","checked");
				}else{
					jQuery("#"+week).attr("checked","checked");
				}
			}else if(mode=="3"){
				document.getElementById("table1").style.display="none";
				document.getElementById("table2").style.display="none";
				document.getElementById("table3").style.display="none";
				document.getElementById("table4").style.display="block";
				document.getElementById("table5").style.display="none";
				//默认选中每几个月的第几天，每几个月的输入框值为1，第几天为当天
				var everyMonth = $("input[name='everyMonth'][checked]").val();
				if(typeof(everyMonth)=="undefined" || everyMonth==''){
					jQuery("#everyMonth1").attr("checked","checked");
					jQuery("#everyMonthParam1").attr("value","1");
					$("#daysParam option[value='"+day+"']").attr("selected",true);
					jQuery("#everyMonthParam2").attr("value","1");
					if(c==1){
						c="第1个";
					}else if(c==2){
						c="第2个";
					}else if(c==3){
						c="第3个";
					}else if(c==4){
						c="第4个";
					}
					$("#severalParam option[value='"+c+"']").attr("selected",true);
					if(week=='0'){
						week = 'SUN';
					}else if(week=='1'){
						week = 'MON';
					}else if(week=='2'){
						week = 'TUE';
					}else if(week=='3'){
						week = 'WED';
					}else if(week=='4'){
						week = 'THU';
					}else if(week=='5'){
						week = 'FRI';
					}else if(week=='6'){
						week = 'SAT';
					}
					$("#weekParam option[value='"+week+"']").attr("selected",true);
				}else if(everyMonth=='0'){
					var everyMonthParam1 = jQuery("#everyMonthParam1").val();
					if(typeof(everyMonthParam1)=="undefined" || everyMonthParam1==''){
						jQuery("#everyMonthParam1").attr("value","1");
					}
					$("#daysParam option[value='"+day+"']").attr("selected",true);
				}else if(everyMonth=='1'){
					var everyMonthParam2 = jQuery("#everyMonthParam2").val();
					if(typeof(everyMonthParam2)=="undefined" || everyMonthParam2==''){
						jQuery("#everyMonthParam2").attr("value","1");
					}
					if(c==1){
						c="第1个";
					}else if(c==2){
						c="第2个";
					}else if(c==3){
						c="第3个";
					}else if(c==4){
						c="第4个";
					}
					$("#severalParam option[value='"+c+"']").attr("selected",true);
					if(week=='0'){
						week = 'SUN';
					}else if(week=='1'){
						week = 'MON';
					}else if(week=='2'){
						week = 'TUE';
					}else if(week=='3'){
						week = 'WED';
					}else if(week=='4'){
						week = 'THU';
					}else if(week=='5'){
						week = 'FRI';
					}else if(week=='6'){
						week = 'SAT';
					}
					$("#weekParam option[value='"+week+"']").attr("selected",true);
				}
			}else if(mode=="4"){
				document.getElementById("table1").style.display="none";
				document.getElementById("table2").style.display="none";
				document.getElementById("table3").style.display="none";
				document.getElementById("table4").style.display="none";
				document.getElementById("table5").style.display="block";
				//默认选中每几个月的第几个星期，每几个月的输入框值为1，第几个星期为当天所属，选中当天的星期
				var everyYear = $("input[name='everyYear'][checked]").val();
				if(typeof(everyYear)=="undefined" || everyYear==''){
					jQuery("#everyYear1").attr("checked","checked");
					$("#monthParam1 option[value='"+month+"']").attr("selected",true);
					$("#yearDaysParam option[value='"+day+"']").attr("selected",true);
					$("#monthParam2 option[value='"+month+"']").attr("selected",true);
					if(c==1){
						c="第1个";
					}else if(c==2){
						c="第2个";
					}else if(c==3){
						c="第3个";
					}else if(c==4){
						c="第4个";
					}
					$("#yearSeveralParam option[value='"+c+"']").attr("selected",true);
					if(week=='0'){
						week = 'SUN';
					}else if(week=='1'){
						week = 'MON';
					}else if(week=='2'){
						week = 'TUE';
					}else if(week=='3'){
						week = 'WED';
					}else if(week=='4'){
						week = 'THU';
					}else if(week=='5'){
						week = 'FRI';
					}else if(week=='6'){
						week = 'SAT';
					}
					$("#yearWeekParam option[value='"+week+"']").attr("selected",true);
				}else if(everyYear=='0'){
					$("#monthParam1 option[value='"+month+"']").attr("selected",true);
					$("#yearDaysParam option[value='"+day+"']").attr("selected",true);
				}else if(everyMonth=='1'){
					$("#monthParam2 option[value='"+month+"']").attr("selected",true);
					if(c==1){
						c="第1个";
					}else if(c==2){
						c="第2个";
					}else if(c==3){
						c="第3个";
					}else if(c==4){
						c="第4个";
					}
					$("#yearSeveralParam option[value='"+c+"']").attr("selected",true);
					if(week=='0'){
						week = 'SUN';
					}else if(week=='1'){
						week = 'MON';
					}else if(week=='2'){
						week = 'TUE';
					}else if(week=='3'){
						week = 'WED';
					}else if(week=='4'){
						week = 'THU';
					}else if(week=='5'){
						week = 'FRI';
					}else if(week=='6'){
						week = 'SAT';
					}
					$("#yearWeekParam option[value='"+week+"']").attr("selected",true);
				}
			}
		});
		//按天
		$('[name=everyDay]:radio').change(function(){
			var everyDay = $("input[name='everyDay'][checked]").val();
			if(typeof(everyDay)=="undefined" || everyDay==''){
				jQuery("#everyDay").attr("checked","checked");
				jQuery("#everyDayParam").attr("value","1");
				
			}else if(everyDay=='0'){
				var everyDayParam = jQuery("#everyDayParam").val();
				if(typeof(everyMonthParam1)=="undefined" || everyMonthParam1==''){
					jQuery("#everyDayParam").attr("value","1");
				}
			}
		});
		//按月
		$('[name=everyMonth]:radio').change(function(){
			var everyMonth = $("input[name='everyMonth'][checked]").val();
			if(typeof(everyMonth)=="undefined" || everyMonth==''){
				jQuery("#everyMonth1").attr("checked","checked");
			}else if(everyMonth=='0'){
				var everyMonthParam1 = jQuery("#everyMonthParam1").val();
				if(typeof(everyMonthParam1)=="undefined" || everyMonthParam1==''){
					jQuery("#everyMonthParam1").attr("value","1");
				}
				$("#daysParam option[value='"+day+"']").attr("selected",true);
			}else if(everyMonth=='1'){
				var everyMonthParam2 = jQuery("#everyMonthParam2").val();
				if(typeof(everyMonthParam2)=="undefined" || everyMonthParam2==''){
					jQuery("#everyMonthParam2").attr("value","1");
				}
				if(c==1){
					c="第1个";
				}else if(c==2){
					c="第2个";
				}else if(c==3){
					c="第3个";
				}else if(c==4){
					c="第4个";
				}
				$("#severalParam option[value='"+c+"']").attr("selected",true);
				if(week=='0'){
					week = 'SUN';
				}else if(week=='1'){
					week = 'MON';
				}else if(week=='2'){
					week = 'TUE';
				}else if(week=='3'){
					week = 'WED';
				}else if(week=='4'){
					week = 'THU';
				}else if(week=='5'){
					week = 'FRI';
				}else if(week=='6'){
					week = 'SAT';
				}
				$("#weekParam option[value='"+week+"']").attr("selected",true);
			}
		});
		//按年
		$('[name=everyYear]:radio').change(function(){
			var everyYear = $("input[name='everyYear'][checked]").val();
			if(typeof(everyYear)=="undefined" || everyYear==''){
				jQuery("#everyYear1").attr("checked","checked");
			}else if(everyYear=='0'){
				$("#monthParam1 option[value='"+month+"']").attr("selected",true);
				$("#yearDaysParam option[value='"+day+"']").attr("selected",true);
			}else if(everyYear=='1'){
				$("#monthParam2 option[value='"+month+"']").attr("selected",true);
				if(c==1){
					c="第1个";
				}else if(c==2){
					c="第2个";
				}else if(c==3){
					c="第3个";
				}else if(c==4){
					c="第4个";
				}
				$("#yearSeveralParam option[value='"+c+"']").attr("selected",true);
				if(week=='0'){
					week = 'SUN';
				}else if(week=='1'){
					week = 'MON';
				}else if(week=='2'){
					week = 'TUE';
				}else if(week=='3'){
					week = 'WED';
				}else if(week=='4'){
					week = 'THU';
				}else if(week=='5'){
					week = 'FRI';
				}else if(week=='6'){
					week = 'SAT';
				}
				$("#yearWeekParam option[value='"+week+"']").attr("selected",true);
			}
		});
		//重复范围选择
		$('[name=range]:radio').click(function(){
			var ranges = document.getElementsByName('range');
            for(var j=0;j<ranges.length;j++){
				if(ranges[j].checked){
					if(ranges[j].value=="0"){
						document.getElementById("endTime").disabled=true;
						//$("#endTime").addClass();
					}else if(ranges[j].value=="1"){
						document.getElementById("endTime").disabled=false;
					}
				}
			}
		});
		//确定
		$('#submits').click(function(){
			//取值
			var hiddenTime = "";
			var displayTime = "";
			
			var frequency = $("input[name='frequency'][checked]").val();
			var showDate='';
			var showTime='';
			if(frequency=='0'){
				showDate=jQuery("#div1 input[name='startTime']").val();
				showTime=jQuery("#div1 select[name='showTime']").val();
			}else if(frequency=='1'){
				showDate=jQuery("#div2 input[name='startTime']").val();
				showTime=jQuery("#div2 select[name='showTime']").val();
			}
			var mode = $("input[name='mode'][checked]").val();
			var range = $("input[name='range'][checked]").val();
			var endTime = jQuery("#endTime").val();
			
			if(frequency=="0"){//单次
				//标题
				P.document.getElementById("planTitle").value=jQuery("#singleTitle").val();
				//生成隐藏时间格式
				hiddenTime = generateHiddenTime(frequency,showDate,showTime);
				//生成显示时间格式
				displayTime = generateDisplayTime(frequency,showDate,showTime);
			}else if(frequency=="1"){//周期
				//标题
				P.document.getElementById("planTitle").value=jQuery("#cycleTitle").val();
				//定期模式:0-按小时;1-按天;2-按周;3-按月;4-按年;
				if(mode=="0"){
					var param1 = jQuery("#everyHour").val();
					if(param1==''){
						Ext.MessageBox.alert("提示","请输入参数!");
						return false;
					}else if(param1=='0'){
						Ext.MessageBox.alert("提示","参数输入有误，请重新输入参数(正整数)!");
						return false;
					}
					
					//生成隐藏时间格式
					hiddenTime = generateHiddenTime(frequency,showDate,showTime,mode,range,endTime,'',param1);
					//生成显示时间格式
					displayTime = generateDisplayTime(frequency,showDate,showTime,mode,range,endTime,'',param1);
				}else if(mode=="1"){
					var everyDay = $("input[name='everyDay'][checked]").val();
					var param2;
					if(typeof(everyDay)=="undefined" || everyDay==''){
						Ext.MessageBox.alert("提示","请选择每几天或每个工作日!");
						return false;
					}else if(everyDay=='0'){
						param2 = jQuery("#everyDayParam").val();
						if(param2==''){
							Ext.MessageBox.alert("提示","请输入参数!");
							return false;
						}else if(param2=='0'){
							Ext.MessageBox.alert("提示","参数输入有误，请重新输入参数(正整数)!");
							return false;
						}
					}
					
					//生成隐藏时间格式
					hiddenTime = generateHiddenTime(frequency,showDate,showTime,mode,range,endTime,everyDay,'',param2);
					//生成显示时间格式
					displayTime = generateDisplayTime(frequency,showDate,showTime,mode,range,endTime,everyDay,'',param2);
				}else if(mode=="2"){
					var param1 = jQuery("#everyWeek").val();
					if(param1==''){
						Ext.MessageBox.alert("提示","请输入参数!");
						return false;
					}else if(param1=='0'){
						Ext.MessageBox.alert("提示","参数输入有误，请重新输入参数(正整数)!");
						return false;
					}
					var everyWeek='';
					$("input[name='week'][checked]").each(function(){
						everyWeek += jQuery(this).val()+",";
					});
					if(typeof(everyWeek)=="undefined" || everyWeek==''){
						Ext.MessageBox.alert("提示","请选择星期!");
						return false;
					}
					
					//生成隐藏时间格式
					hiddenTime = generateHiddenTime(frequency,showDate,showTime,mode,range,endTime,'',param1,everyWeek);
					//生成显示时间格式
					displayTime = generateDisplayTime(frequency,showDate,showTime,mode,range,endTime,'',param1,everyWeek);
				}else if(mode=="3"){
					var everyMonth = $("input[name='everyMonth'][checked]").val();
					var param1;
					var param2;
					var param3;
					if(typeof(everyMonth)=="undefined" || everyMonth==''){
						Ext.MessageBox.alert("提示","请选择每几个月的第几天或每第几个月第几个星期!");
						return false;
					}else if(everyMonth=='0'){
						//每几个月的第几天
						param1 = jQuery("#everyMonthParam1").val();
						if(param1==''){
							Ext.MessageBox.alert("提示","请输入参数!");
							return false;
						}else if(param1=='0'){
							Ext.MessageBox.alert("提示","参数输入有误，请重新输入参数(正整数)!");
							return false;
						}
						param2 = $("select[name='daysParam']").val();
					}else if(everyMonth=='1'){
						//每几个月的第几个星期
						param1 = jQuery("#everyMonthParam2").val();
						if(param1==''){
							Ext.MessageBox.alert("提示","请输入参数!");
							return false;
						}else if(param1=='0'){
							Ext.MessageBox.alert("提示","参数输入有误，请重新输入参数(正整数)!");
							return false;
						}
						param2 = $("select[name='severalParam']").val();
						param3 = $("select[name='weekParam']").val();
					}
					
					//生成隐藏时间格式
					hiddenTime = generateHiddenTime(frequency,showDate,showTime,mode,range,endTime,everyMonth,param1,param2,param3);
					//生成显示时间格式
					displayTime = generateDisplayTime(frequency,showDate,showTime,mode,range,endTime,everyMonth,param1,param2,param3);
				}else if(mode=="4"){
					var everyYear = $("input[name='everyYear'][checked]").val();
					var param1;
					var param2;
					var param3;
					if(typeof(everyYear)=="undefined" || everyYear==''){
						Ext.MessageBox.alert("提示","请选择每年某月某日或每年某月第几个星期!");
						return false;
					}else if(everyYear=='0'){
						//每年某月某日
						param1 = $("select[name='monthParam1']").val();
						param2 = $("select[name='yearDaysParam']").val();
					}else if(everyYear=='1'){
						//每年某月第几个星期
						param1 = $("select[name='monthParam2']").val();
						param2 = $("select[name='yearSeveralParam']").val();
						param3 = $("select[name='yearWeekParam']").val();
					}
					
					//生成隐藏时间格式
					hiddenTime = generateHiddenTime(frequency,showDate,showTime,mode,range,endTime,everyYear,param1,param2,param3);
					//生成显示时间格式
					displayTime = generateDisplayTime(frequency,showDate,showTime,mode,range,endTime,everyYear,param1,param2,param3);
				}
				showDate = document.getElementById("startTime").value;
				showTime = document.getElementById("showTime").value;
			}
			
            alert("hiddenTime="+hiddenTime+"\t"+"displayTime="+displayTime);
          	//设置参数值
        	P.document.getElementById("${hiddenTime}").value=hiddenTime;
			P.document.getElementById("${displayTime}").value=displayTime;
			P.document.getElementById("frequency").value=frequency;
			P.document.getElementById("mode").value=mode;
			P.document.getElementById("range").value=range;
			if(range=='1'){
				P.document.getElementById("endTime").value=endTime;
			}
			P.document.getElementById("startTime").value=showDate;	
			P.document.getElementById("showTime").value=showTime;
			
			//设置参数值
			if(mode=='0'){
				//按小时
				P.document.getElementById("param1").value=jQuery("#everyHour").val();
			}else if(mode=='1'){
				//按天
				var everyDay = $("input[name='everyDay'][checked]").val();
				P.document.getElementById("param1").value=everyDay;
				if(everyDay=='0'){
					P.document.getElementById("param2").value=jQuery("#everyDayParam").val();
				}
			}else if(mode=='2'){
				//按周
				P.document.getElementById("param1").value=jQuery("#everyWeek").val();
				var everyWeek='';
				$("input[name='week'][checked]").each(function(){
					everyWeek += jQuery(this).val()+",";
				});
				P.document.getElementById("param2").value=everyWeek.substring(0,everyWeek.length-1);
			}else if(mode=='3'){
				//按月
				
			}else if(mode=='4'){
				
			}
			/*
			P.document.getElementById("param1").value="";
			P.document.getElementById("param2").value="";
			P.document.getElementById("param3").value="";
			P.document.getElementById("param4").value="";
			*/
			closeWindow();
		});
		//生成隐藏时间格式
		function generateHiddenTime(frequency,beginTime,showTime,mode,range,endTime,everyTimes,param1,param2,param3){
			//隐藏时间字符串
			var hiddenTime;
			
			var dateArray = beginTime.split("-");
			var timeArray = showTime.split(":");
			if(frequency=="0"){
				//秒 分 时 日 月 周 年
				//8 42 14 21 5 * 2012
				//使用~~可以去掉字符串开头的0
				//~(求反) 反转操作数的每一位。
				//~~就回到原数了
				hiddenTime = ~~timeArray[2]+" "+~~timeArray[1]+" "+~~timeArray[0]+" "+~~dateArray[2]+" "+~~dateArray[1]+" "+"*"+" "+dateArray[0];
			}else if(frequency=="1"){
				if(mode=="0"){
					//按小时
					hiddenTime = ~~timeArray[2]+" "+~~timeArray[1]+" "+~~timeArray[0]+" */"+param1+" * * * *";
				}else if(mode=="1"){
					//按天
					if(everyTimes=='0'){
						//每几天
						hiddenTime = ~~timeArray[2]+" "+~~timeArray[1]+" "+~~timeArray[0]+" * */"+param2+" * *";
					}else if(everyTimes=='1'){
						//每个工作日
						hiddenTime = ~~timeArray[2]+" "+~~timeArray[1]+" "+~~timeArray[0]+" * * MON-FRI"+" *";
					}
				}else if(mode=="2"){
					//按周
					hiddenTime = ~~timeArray[2]+" "+~~timeArray[1]+" "+~~timeArray[0]+" * "+param1+" "+param2.substring(0,param2.length-1);
				}else if(mode=="3"){
					//按月
					if(everyTimes=='0'){
						//每几个月的第几天
						if(param2=='最后一'){
							param2 = "L";
						}
						hiddenTime = ~~timeArray[2]+" "+~~timeArray[1]+" "+~~timeArray[0]+" "+param2+" */"+param1+" * *";
					}else if(everyTimes=='1'){
						//每几个月的第几个星期
						if(typeof(param3)!="undefined" || param3!=''){
							if(param3=='SUN'){
								param3 = "1";
							}else if(param3=='MON'){
								param3 = "2";
							}else if(param3=='TUE'){
								param3 = "3";
							}else if(param2=='WED'){
								param3 = "4";
							}else if(param3=='THU'){
								param3 = "5";
							}else if(param3=='FRI'){
								param3 = "6";
							}else if(param3=='SAT'){
								param3 = "0";
							}
						}
						if(param2=='第1个'){
							param3 += "#1";
						}else if(param2=='第2个'){
							param3 += "#2";
						}else if(param2=='第3个'){
							param3 += "#3";
						}else if(param2=='第4个'){
							param3 += "#4";
						}else if(param2=='最后一个'){
							param3 += "L";
						}
						hiddenTime = ~~timeArray[2]+" "+~~timeArray[1]+" "+~~timeArray[0]+" * */"+param1+" "+param3+" *";
					}
				}else if(mode=="4"){
					//按年
					if(everyTimes=='0'){
						//每年某月第几个星期
						if(param2=='最后一'){
							param2 = "L";
						}
						hiddenTime = ~~timeArray[2]+" "+~~timeArray[1]+" "+~~timeArray[0]+" "+param2+" "+param1+" ? *";
					}else if(everyTimes=='1'){
						//每年某月某日
						if(typeof(param3)!="undefined" || param3!=''){
							if(param3=='SUN'){
								param3 = "1";
							}else if(param3=='MON'){
								param3 = "2";
							}else if(param3=='TUE'){
								param3 = "3";
							}else if(param2=='WED'){
								param3 = "4";
							}else if(param3=='THU'){
								param3 = "5";
							}else if(param3=='FRI'){
								param3 = "6";
							}else if(param3=='SAT'){
								param3 = "0";
							}
						}
						if(param2=='第1个'){
							param3 += "#1";
						}else if(param2=='第2个'){
							param3 += "#2";
						}else if(param2=='第3个'){
							param3 += "#3";
						}else if(param2=='第4个'){
							param3 += "#4";
						}else if(param2=='最后一个'){
							param3 += "L";
						}
						hiddenTime = ~~timeArray[2]+" "+~~timeArray[1]+" "+~~timeArray[0]+" * "+param1+" "+param3+" *";
					}
				}
			}
			return hiddenTime;
		}
		//生成显示时间格式
		function generateDisplayTime(frequency,beginTime,showTime,mode,range,endTime,everyTimes,param1,param2,param3){
			//显示时间字符串
			var displayTime;
			//alert(frequency+"\t"+beginTime+"\t"+showTime+"\t"+mode+"\t"+param1+"\t"+range);
			if(frequency=="0"){
				displayTime = "生效时间："+beginTime+","+showTime;
			}else if(frequency=="1"){
				if(mode == "0"){
					//按小时
					if(range=="0"){
						//开始日期--永不过期
						displayTime = "启动时间:每"+param1+"小时，生效时间："+beginTime+","+showTime;
					}else if(range=="1"){
						//开始日期--结束日期
						displayTime = "启动时间:每"+param1+"小时，生效时间："+beginTime+" 到 "+endTime+","+showTime;
					}
				}else if(mode == "1"){
					//每天
					if(range=="0"){
						//开始日期--永不过期
						if(param2=='1'){
							if(everyTimes=='0'){
								displayTime = "启动时间:每天，生效时间："+beginTime+","+showTime;
							}else if(everyTimes=='1'){
								displayTime = "启动时间:每个工作日，生效时间："+beginTime+","+showTime;
							}
						}else{
							if(everyTimes=='0'){
								displayTime = "启动时间:每"+param2+"天，生效时间："+beginTime+","+showTime;
							}else if(everyTimes=='1'){
								displayTime = "启动时间:每个工作日，生效时间："+beginTime+","+showTime;
							}
						}
					}else if(range=="1"){
						//开始日期--结束日期
						if(param2=='1'){
							if(everyTimes=='0'){
								displayTime = "启动时间:每天，生效时间："+beginTime+" 到 "+endTime+","+showTime;
							}else if(everyTimes=='1'){
								displayTime = "启动时间:每个工作日，生效时间："+beginTime+" 到 "+endTime+","+showTime;
							}
						}else{
							if(everyTimes=='0'){
								displayTime = "启动时间:每"+param2+"天，生效时间："+beginTime+" 到 "+endTime+","+showTime;
							}else if(everyTimes=='1'){
								displayTime = "启动时间:每个工作日，生效时间："+beginTime+" 到 "+endTime+","+showTime;
							}
						}
					}
				}else if(mode == "2"){
					//按周
					if(range=="0"){
						//开始日期--永不过期
						displayTime = "启动时间:每周 "+param2.substring(0,param2.length-1).replace(/,/g,"、")+"，生效时间："+beginTime+","+showTime;
					}else if(range=="1"){
						//开始日期--结束日期
						displayTime = "启动时间:每周 "+param2.substring(0,param2.length-1).replace(/,/g,"、")+"，生效时间："+beginTime+" 到 "+endTime+","+showTime;
					}
				}else if(mode == "3"){
					if(typeof(param3)!="undefined" || param3!=''){
						if(param3=='SUN'){
							param3 = "星期天";
						}else if(param3=='MON'){
							param3 = "星期一";
						}else if(param3=='TUE'){
							param3 = "星期二";
						}else if(param3=='WED'){
							param3 = "星期三";
						}else if(param3=='THU'){
							param3 = "星期四";
						}else if(param3=='FRI'){
							param3 = "星期五";
						}else if(param3=='SAT'){
							param3 = "星期六";
						}
					}
					//按月
					if(range=="0"){
						//开始日期--永不过期
						if(everyTimes=='0'){
							//每几个月的第几天
							displayTime = "启动时间:每"+param1+"个月 第"+param2+"天，生效时间："+beginTime+","+showTime;
						}else if(everyTimes=='1'){
							//每几个月的第几个星期
							displayTime = "启动时间:每"+param1+"个月 "+param2+" "+param3+"，生效时间："+beginTime+","+showTime;
						}
					}else if(range=="1"){
						//开始日期--结束日期
						if(everyTimes=='0'){
							//每几个月的第几天
							displayTime = "启动时间:每"+param1+"个月 第"+param2+"天，生效时间："+beginTime+" 到 "+endTime+","+showTime;
						}else if(everyTimes=='1'){
							//每几个月的第几个星期
							displayTime = "启动时间:每"+param1+"个月 "+param2+" "+param3+"，生效时间："+beginTime+" 到 "+endTime+","+showTime;
						}
					}
				}else if(mode == "4"){
					if(typeof(param3)!="undefined" || param3!=''){
						if(param3=='SUN'){
							param3 = "星期天";
						}else if(param3=='MON'){
							param3 = "星期一";
						}else if(param3=='TUE'){
							param3 = "星期二";
						}else if(param3=='WED'){
							param3 = "星期三";
						}else if(param3=='THU'){
							param3 = "星期四";
						}else if(param3=='FRI'){
							param3 = "星期五";
						}else if(param3=='SAT'){
							param3 = "星期六";
						}
					}
					//按年
					if(range=="0"){
						//开始日期--永不过期
						if(everyTimes=='0'){
							//每年某月某日
							displayTime = "启动时间:每年"+param1+"月 第"+param2+"天，生效时间："+beginTime+","+showTime;
						}else if(everyTimes=='1'){
							//每年某月第几个星期
							displayTime = "启动时间:每年"+param1+"月 "+param2+" "+param3+"，生效时间："+beginTime+","+showTime;
						}
					}else if(range=="1"){
						//开始日期--结束日期
						if(everyTimes=='0'){
							//每年某月某日
							displayTime = "启动时间:每年"+param1+"月 第"+param2+"天，生效时间："+beginTime+" 到 "+endTime+","+showTime;
						}else if(everyTimes=='1'){
							//每年某月第几个星期
							displayTime = "启动时间:每年"+param1+"月 "+param2+" "+param3+"，生效时间："+beginTime+" 到 "+endTime+","+showTime;
						}
					}
				}
			}
			return displayTime;
		}
		//取消
		$('#close').click(function(){
			closeWindow();
		});
	</script>
</body>
</html>
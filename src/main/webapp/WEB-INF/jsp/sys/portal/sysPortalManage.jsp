<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><spring:message code="fhd.sys.portal.portal"/></title>
	<script type="text/javascript">
		var columnStyle="padding:10px 10px 10px 10px";
		var portalItemNo=${portal.col};
		var portalTools = [{
			id : 'refresh',
			handler : function(e, target, portlet) {
				var iframeSrc=jQuery("#iframe"+portlet.id).attr("src");
				jQuery("#iframe"+portlet.id).attr("src", iframeSrc);
			}
		},{
			id : 'close',
			handler : function(e, target, portlet) {
				portletRemove(portlet);
			}
		}];
	    var portal = new Ext.ux.Portal({
	    	id : '${portal.id}',
	    	layout : 'column',
			xtype:'portal',
			el:"portal_div",
			autoScroll:false,
	        border: false,
			items:[
				<c:forEach begin="1" end="4">
					{
						columnWidth:${1/portal.col},
						style:columnStyle
					},
				</c:forEach>
				{
					columnWidth:${1/portal.col},
					style:columnStyle
				}
			]
	    });
		var usePortlets=[
			<c:if test="${portal.subPortalPortlet!=null}">
				<c:forEach items="${portal.subPortalPortlet }" var="portalPortlet">
					{
						rowNo:${portalPortlet.rowNo},
						colNo:${portalPortlet.colNo},
						id:'${portalPortlet.portlet.id}',
						title: '${portalPortlet.portlet.title}',
						tools: portalTools,
						html:"<iframe id='iframe${portalPortlet.portlet.id}' frameborder='0' width='100%' height='100%' scrolling='auto' noresize='noresize' src='${ctx}${portalPortlet.portlet.url}' />",
						height:${portalPortlet.portlet.height}
					},
				</c:forEach>
			</c:if>
			{}
		];
		var portlets=[
			<c:forEach items="${portlets }" var="portlet">
				{
					id:'${portlet.id}',
					title: '${portlet.title}',
					tools: portalTools,
					html:"<iframe id='iframe${portlet.id}' frameborder='0' width='100%' height='100%' scrolling='auto' noresize='noresize' src='${ctx}${portlet.url}' />",
					height:${portlet.height}
				},
			</c:forEach>
			{}
		];

		function portletRemove(portlet){
			jQuery("#checkbox"+portlet.id).attr('checked',false);
			portlet.ownerCt.remove(portlet, true);
		}
		function portletAdd(portalItem,portlet){
			jQuery("#checkbox"+portlet.id).attr('checked',true);
			portalItem.add(portlet);
		}
		function checkboxClick(ele){
			var portletId=jQuery(ele).attr("id").substr(8);
  			var portlet;
			if(jQuery(ele).attr('checked')){
				$.each(portlets, function(m, portletTemp){
					if(portletTemp.id==portletId){
						portlet=portletTemp;
					}
	  			});
				var portalItem=portal.items.get(0);
	  			portletAdd(portalItem,portlet);
			}else{
				portlet=Ext.getCmp(portletId);
				portletRemove(portlet);
			}
			portal.doLayout();
		}
		function save(){
			var items=portal.items;
			var portalPortlets=[];
			var portalsJson=[];
			var col=0;
			for(var i=0;i<items.getCount();i++){
				var c=items.get(i);
				var j=0;
				c.items.each(function(portlet){
					var portalPortletObj={portalId:portal.id,portletId:portlet.id,colNo:i,rowNo:j++};
					portalPortlets.push(portalPortletObj);
					if(col<j){
						col=j;
					}
				});
			}
			portalsJson.push({id:portal.id,col:portalItemNo,row:col+1});
			Ext.Ajax.request({
				type: "POST",
				url: "${ctx}/sys/portal/editSys.do",
				params:'portalPortletsJson='+encodeURIComponent(Ext.encode(portalPortlets))+"&portalsJson="+encodeURIComponent(Ext.encode(portalsJson)),
				success: function(response) {
					Ext.Msg.alert('成功', "保存成功");
				},
				failure: function(response) {
					Ext.Msg.alert('失败', "保存失败");
				}
			});
		}
		function formatPortal(){
			Ext.Ajax.request({
				type: "POST",
				url: "${ctx}/sys/portal/UserPortalRemove.do",
				success: function(response) {
					Ext.Msg.alert('成功', "初始化成功");
				},
				failure: function(response) {
					Ext.Msg.alert('失败', "初始化失败");
				}
			});
		}
		function changeCol(n){
			for(var i=1;i<=5;i++){
				jQuery("#changeCol"+i).attr("style","");
			}
			jQuery("#changeCol"+n).attr("style","border-style:solid ;border-color: #000000;border-width: 4;");
			portalItemNo=n;
			var items=portal.items;
			var length=items.getCount();
			for(var i=0;i<length;i++){
				if(i<n){
					items.get(i).columnWidth=1/n;
				}
			}
			for(var i=length-1; i>=n;i--){
				items.get(i).items.each(function(portlet){
					var portletId=portlet.id;
					portletRemove(portlet);
					$.each(portlets, function(m, portletTemp){
						if(portletTemp.id==portletId){
							portlet=portletTemp;
						}
		  			});
					var portalItem=portal.items.get(i-1);
		  			portletAdd(portalItem,portlet);
				});
			}
			portal.doLayout();
		}
		function showAndHide(){
			var divShows = jQuery("#questSetDiv");
			if(divShows.is(":hidden")){
				divShows.show();
			}else{
				divShows.hide();
			}
		}
		Ext.onReady(function(){
			for(var j=0;j<${portal.row};j++){
				for(var i=0;i<${portal.col};i++){
					$.each(usePortlets, function(m, portlet){
						if(i==portlet.colNo&&j==portlet.rowNo){
							var portalItem=portal.items.get(i);
							portletAdd(portalItem,portlet);
						}
		  			});
				}
			}
			portal.height=portlets.length*500;
			portal.render();
			changeCol(portalItemNo);
		});
	</script>
</head>
<body>
	<div id="questSetDiv" style="display: none;" >
		<table width="100%" border="0" cellspacing="0" cellpadding="0" onclick="showAndHide();">
			<tr>
				<td class="fhd_query_title">&nbsp;<img src="${ctx}/images/plus.gif" width="15" height="15" />布局</td>
			</tr>
		</table>
		<table id="hisEventTable" border="0" width="100%" cellpadding="0" cellspacing="0" class="fhd_query_table">
			<tr>
				<td>
					<div align="center" style="width:100%;border:1px none #3366cc;border-bottom-style:solid;top: 10px;">
						<table>
							<tr>
								<td style="width: 300px;border:none;" align="center"><img id="changeCol1" src="${ctx}/images/home/col1.jpg"  alt="两列" onclick="changeCol(1)"/></td>
								<td style="width: 300px;border:none;" align="center"><img id="changeCol2" src="${ctx}/images/home/col2.jpg"  alt="两列" onclick="changeCol(2)"/></td>
								<td style="width: 300px;border:none;" align="center"><img id="changeCol3" src="${ctx}/images/home/col3.jpg"  alt="三列" onclick="changeCol(3)"/></td>
								<td style="width: 300px;border:none;" align="center"><img id="changeCol4" src="${ctx}/images/home/col4.jpg"  alt="四列" onclick="changeCol(4)"/></td>
								<td style="width: 300px;border:none;" align="center"><img id="changeCol5" src="${ctx}/images/home/col5.jpg"  alt="五列" onclick="changeCol(5)"/></td>
							</tr>
						</table>
					</div>
					<div style="width: 100%">
						<table align="center">
							<tr>
								<c:forEach items="${portlets }" var="portlet" varStatus="status">
									<td style="width: 100px;border:1px solid #89C7EE;"><input id="checkbox${portlet.id}" type="checkbox" onclick="checkboxClick(this)"/>${portlet.title}</td>
									<c:if test="${status.count%10==0}">
										</tr><tr>
									</c:if>
								</c:forEach>
							</tr>
						</table>
						<input id="save" type="button" value="保存" onclick="save()" class="fhd_btn" style="float:right"/>
						<input id="formate" type="button" value="初始化所有用户面板布局" onclick="formatPortal(2)" class="fhd_btn" style="float:right"/>
					</div>
				</td>
			</tr>
		</table>
	</div>
<div class="x-panel-tbar">
<div class="x-toolbar x-small-editor x-toolbar-layout-ct" style="height: 25px;">
<table cellspacing="0" class="x-toolbar-ct">
	<tbody>
		<tr>
			<td align="left" class="x-toolbar-left">
			<table cellspacing="0">
				<tbody>
					<tr class="x-toolbar-left-row">
						<td class="x-toolbar-cell">
						<table cellspacing="0" class="x-btn  x-btn-text-icon"
							 style="width: auto;">
							<tbody class="x-btn-small x-btn-icon-small-left">
								<tr>
									<td class="x-btn-tl"><i>&nbsp;</i></td>
									<td class="x-btn-tc"></td>
									<td class="x-btn-tr"><i>&nbsp;</i></td>
								</tr>
								<tr>
									<td class="x-btn-ml"><i>&nbsp;</i></td>
									<td class="x-btn-mc"><em unselectable="on" class="">
									<button type="button"
										class=" x-btn-text adquery" onclick="showAndHide();">布局</button>
									</em></td>
									<td class="x-btn-mr"><i>&nbsp;</i></td>
								</tr>
								<tr>
									<td class="x-btn-bl"><i>&nbsp;</i></td>
									<td class="x-btn-bc"></td>
									<td class="x-btn-br"><i>&nbsp;</i></td>
								</tr>
							</tbody>
						</table>
						</td>
					</tr>
				</tbody>
			</table>
			</td>
			<td align="right" class="x-toolbar-right">
			<table cellspacing="0" class="x-toolbar-right-ct">
				<tbody>
					<tr>
						<td>
						<table cellspacing="0">
							<tbody>
								<tr class="x-toolbar-right-row"></tr>
							</tbody>
						</table>
						</td>
						<td>
						<table cellspacing="0">
							<tbody>
								<tr class="x-toolbar-extras-row"></tr>
							</tbody>
						</table>
						</td>
					</tr>
				</tbody>
			</table>
			</td>
		</tr>
	</tbody>
</table>
</div>
</div>
	<div id="portal_div" style="height: 100%;width: 100%;overflow: auto;"></div>
</body>
</html>
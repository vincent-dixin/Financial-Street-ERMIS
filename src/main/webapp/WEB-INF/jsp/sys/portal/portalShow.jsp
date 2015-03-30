<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/commons/include-tags.jsp"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><spring:message code="fhd.sys.portal.portal"/></title>

	<script type="text/javascript">
		var columnStyle="padding:10px 0 10px 10px";
		var portalTools = [{
			id : 'refresh',
			handler : function(e, target, portlet) {
				var iframeSrc=jQuery("#iframe"+portlet.id).attr("src");
				jQuery("#iframe"+portlet.id).attr("src", iframeSrc);
			}}
		];
	    var portal = new Ext.ux.Portal({
	    	id : '${portal.id}',
	    	layout : 'column',
			xtype:'portal',
			el:"portal_div",
			autoScroll:true,
	        border: false,
	        width: parent.Ext.getCmp('home').width,
	        height: Ext.getBody().getHeight() ,
			items:[
				
				{
					columnWidth:${1/portal.col},
					style:columnStyle
				}
			]
	    });
		var usePortlets=[
			<c:if test="${portal.subPortalPortlet!=null}">
				<c:forEach items="${portal.subPortalPortlet}" var="portalPortlet">
					{
						rowNo:${portalPortlet.rowNo},
						colNo:${portalPortlet.colNo},
						id:'${portalPortlet.portlet.id}',
						title: '${portalPortlet.portlet.title}',
						tools: portalTools,
						html:"<iframe id='iframe${portalPortlet.portlet.id}' frameborder='0' width='100%' height='100%' scrolling='auto' noresize='noresize' src='${ctx}${portalPortlet.portlet.url}' />",
						height:${portalPortlet.portlet.height},
						collapsible : true,
						draggable : false
					},
				</c:forEach>
			</c:if>
			{}
		];
		function portletAdd(portalItem,portlet){
			portalItem.add(portlet);
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
			portal.render();
		});
	</script>
</head>

<body>
	<div id="portal_div" style="height: 100%"></div>
</body>
</html>
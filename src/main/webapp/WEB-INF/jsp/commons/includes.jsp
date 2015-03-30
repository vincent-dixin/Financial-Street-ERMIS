<%@ page errorPage="/WEB-INF/jsp/commons/errorpage.jsp"  pageEncoding="UTF-8"%>
<%@ page buffer="48kb"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %>
<%@ taglib uri="fhd-tag-core" prefix="fhdcore" %>
<%@ taglib uri="fhd-dic-tag" prefix="d" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" /> 
<c:set var="req" value="${pageContext.request}" /> 

<link rel="stylesheet" id="ext" type="text/css" href="${ctx}/scripts/ext-3.4.0/resources/css/ext-all.css" />
<link rel="stylesheet" id="fhd" type="text/css" href="${ctx}/css/FHDstyle.css" />
<link rel="stylesheet" id="fhd" type="text/css" href="${ctx}/css/icon.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/scripts/ext-3.4.0/examples/ux/css/Portal.css"/>
<link rel='stylesheet' type='text/css' href='${ctx}/scripts/ext-3.4.0/examples/ux/treegrid/treegrid.css' />
<link rel='stylesheet' type='text/css' href='${ctx}/scripts/ext-3.4.0/examples/ux/css/GroupSummary.css' />
<link rel='stylesheet' type='text/css' href='${ctx}/scripts/ext-3.4.0/examples/ux/css/Spinner.css' />
<link rel='stylesheet' type='text/css' href='${ctx}/scripts/ext-3.4.0/examples/ux/css/RowEditor.css' />
<link rel='stylesheet' type="text/css" href="${ctx}/scripts/ext-3.4.0/examples/ux/treegrideditor/treegrideditor.css" />

<link rel="stylesheet" href="${ctx}/scripts/kindeditor-4.1.1/themes/default/default.css" />
<script charset="utf-8" src="${ctx}/scripts/kindeditor-4.1.1/kindeditor.js"></script>
<script charset="utf-8" src="${ctx}/scripts/kindeditor-4.1.1/lang/zh_CN.js"></script>

<script src="${ctx}/scripts/old-edition/calendar/WdatePicker.js" type="text/javascript"></script>
<script src="${ctx}/scripts/jquery-1.4.2.min.js" type="text/javascript"></script>
<script src="${ctx}/scripts/jquery-ui-1.8.2.custom.min.js" type="text/javascript"></script>
<script src="${ctx}/scripts/jquery.validate.js" type="text/javascript"></script>
<script src="${ctx}/scripts/jquery.validate.method.js" type="text/javascript"></script>
<script src="${ctx}/scripts/jquery.form.js" type="text/javascript"></script>
<script src="${ctx}/scripts/messages_cn.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/scripts/old-edition/selectTreeShowText.js"></script>
<script type="text/javascript" src="${ctx}/scripts/old-edition/SpryTabbedPanels.js"></script>

<script type="text/javascript" src="${ctx}/scripts/ext-3.4.0/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="${ctx}/scripts/ext-3.4.0/ext-all.js"></script>
<script type="text/javascript" src="${ctx}/scripts/ext-3.4.0/examples/ux/TabCloseMenu.js"></script>
<script type="text/javascript" src="${ctx}/scripts/ext-3.4.0/examples/ux/ProgressBarPager.js"></script>
<script type="text/javascript" src="${ctx}/scripts/old-edition/TreeCheckNodeUI.js"></script>
<script type="text/javascript" src="${ctx}/scripts/ext-3.4.0/examples/ux/Portal.js"></script>
<script type="text/javascript" src="${ctx}/scripts/ext-3.4.0/examples/ux/PortalColumn.js"></script>
<script type="text/javascript" src="${ctx}/scripts/ext-3.4.0/examples/ux/Portlet.js"></script>
<script type='text/javascript' src='${ctx}/scripts/ext-3.4.0/examples/ux/treegrid/TreeGridSorter.js'></script>
<script type='text/javascript' src='${ctx}/scripts/ext-3.4.0/examples/ux/treegrid/TreeGridColumnResizer.js'></script>
<script type='text/javascript' src='${ctx}/scripts/ext-3.4.0/examples/ux/treegrid/TreeGridNodeUI.js'></script>
<script type='text/javascript' src='${ctx}/scripts/ext-3.4.0/examples/ux/treegrid/TreeGridLoader.js'></script>
<script type='text/javascript' src='${ctx}/scripts/ext-3.4.0/examples/ux/treegrid/TreeGridColumns.js'></script>
<script type='text/javascript' src='${ctx}/scripts/ext-3.4.0/examples/ux/RowExpander.js'></script>
<script type='text/javascript' src='${ctx}/scripts/ext-3.4.0/examples/ux/RowEditor.js'></script>
<script type='text/javascript' src='${ctx}/scripts/ext-3.4.0/examples/ux/GroupSummary.js'></script>
<script type='text/javascript' src='${ctx}/scripts/ext-3.4.0/examples/ux/treegrid/TreeGrid.js'></script>
<script type="text/javascript" src="${ctx}/scripts/ext-3.4.0/examples/ux/treegrideditor/TreeGridEditor.js"></script>
<script type="text/javascript" src="${ctx}/scripts/ext-3.4.0/examples/ux/treegrideditor/TreeGridEditorEventModel.js"></script>
<script type="text/javascript" src="${ctx}/scripts/ext-3.4.0/examples/ux/treegrideditor/TreeGridEditorNode.js"></script>
<script type="text/javascript" src="${ctx}/scripts/ext-3.4.0/examples/ux/treegrideditor/TreeGridEditorNodeUI.js"></script>
<script type="text/javascript" src="${ctx}/scripts/ext-3.4.0/examples/ux/treegrideditor/TreeGridEditorNodeReader.js"></script>
<script type="text/javascript" src="${ctx}/scripts/ext-3.4.0/examples/ux/treegrideditor/TreeGridEditorNodeWriter.js"></script>
<script type="text/javascript" src="${ctx}/scripts/ext-3.4.0/examples/ux/treegrideditor/TreeGridEditorLoader.js"></script>
<script type="text/javascript" src="${ctx}/scripts/ext-3.4.0/examples/ux/treegrideditor/TreeGridEditorDragZone.js"></script>
<script type="text/javascript" src="${ctx}/scripts/ext-3.4.0/examples/ux/treegrideditor/TreeGridEditorSelectionModel.js"></script>
<script type="text/javascript" src="${ctx}/scripts/ext-3.4.0/examples/ux/treegrideditor/plugins/TreeNodeChecked.js"></script>
<script type="text/javascript" src="${ctx}/scripts/ext-3.4.0/examples/ux/GroupSummary.js"></script>
<script type="text/javascript" src="${ctx}/scripts/ext-3.4.0/examples/ux/Spinner.js"></script>
<script type="text/javascript" src="${ctx}/scripts/ext-3.4.0/examples/ux/PanelResizer.js"></script>
<script type="text/javascript" src="${ctx}/scripts/ext-3.4.0/examples/ux/PagingMemoryProxy.js"></script>
<script type="text/javascript" src="${ctx}/scripts/ext-3.4.0/examples/ux/Toast.js"></script>
<script type="text/javascript" src="${ctx}/scripts/ext-3.4.0/src/locale/ext-lang-zh_CN.js"></script>


<script>
	var contextPath = "${ctx}";	
	var contextHttp = "${req.scheme}://${req.serverName}:${req.serverPort}${ctx}/";
	
	// sets default ExtJS blank image
	Ext.BLANK_IMAGE_URL = "${ctx}/scripts/ext-3.4.0/resources/images/default/s.gif";
	
	
	/**
	 *	在其他iframe页面中点击隐藏菜单
	 *
	 */
	$(document).click(function(event){
		try{//altered by David on 2012-02-23
			Ext.each(top.menu.items.items,function(m){
				if(m.text){
					m.menu.hide();
				}
			});
		}catch(err){}
	});
	// 初始化提示
	Ext.QuickTips.init();
</script>

<script type="text/javascript" src="${ctx}/scripts/old-edition/fhd.js"></script>
<script type="text/javascript" src="${ctx}/scripts/old-edition/chart/Charts/FusionCharts.js"></script>
<script type="text/javascript" src="${ctx}/scripts/old-edition/ComboboxGrid.js"></script>
<style type="text/css">
.x-grid3-row-over .x-grid3-cell-inner {
    font-weight: bold;
}
.icon-expand-all {
    background-image: url(${ctx}/images/expand-all.gif) !important;
}
.icon-collapse-all {
    background-image: url(${ctx}/images/collapse-all.gif) !important;
}

body {
	overflow-y: auto;
}
</style>

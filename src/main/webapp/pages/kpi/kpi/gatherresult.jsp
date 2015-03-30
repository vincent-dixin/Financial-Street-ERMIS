<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>

<%@ include file="/WEB-INF/jsp/commons/include-tagsOnly.jsp" %>
    <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
    <html>
        
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>指标结果采集入录</title>
            <style type="text/css">
			</style>
			
            <script type="text/javascript" >
            Ext.Loader.setPath({
            	'FHD.ux' : 'scripts/component'
            });
            Ext.require(['FHD.ux.FusionChartPanel']);
            </script>
        </head>
        
        <body>
        <div id='kpigatherresult' ></div>
        <!--   <iframe id='resultIFrameId' noresize='noresize' src='${ctx}/pages/kpi/kpi/gatherresulttableinput.jsp?kpiname=${param.kpiname}&frequecy=${param.frequecy}&parentid=${param.parentid}&editflag=${param.editflag}&id=${param.id}&parentname=${param.parentname}'++ frameborder='0' height="100%" width="100%"></iframe>-->
        <iframe id='resultIFrameId' noresize='noresize' src='${ctx}/pages/kpi/kpi/gatherresulttable.jsp?kpiname=${param.kpiname}&frequecy=${param.frequecy}&parentid=${param.parentid}&editflag=false&id=${param.id}&parentname=${param.parentname}&yearId=${param.yearId}&timeId=${param.timeId}'++ frameborder='0' height="100%" width="100%"></iframe>
        </body>
    
    </html>
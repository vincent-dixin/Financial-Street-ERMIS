<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <html>
            <title>风险指标监控驾驶舱</title>
            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <style type="text/css">
				</style>
                <c:set var="ctx" value="${pageContext.request.contextPath}" />
                <script src="${ctx}/scripts/jquery-1.4.2.min.js" type="text/javascript"></script>
                <script>
                    
                </script>
            </head>
            
            <body>
                <table id="jrjindex" width="100%" height="100%" border="0">
                    <tr>
                        <td height="220" colspan="2" width="1058">
                            <div>
                                <iframe src="${ctx}/jrj/angulargauge.do?scid=root" height="210" id="headiframe" name="headiframe" width="1058" frameborder="0" marginwidth="0" marginheight="0" scrolling="auto"></iframe>
                            </div>
                        </td>
                        <tr>
                            <tr>
                                <td width="530">
                                    <div>
                                        <iframe src="${ctx}/jrj/kpilist.do" height="430" id="leftiframe" name="leftiframe" width="530" frameborder="0" marginwidth="0" marginheight="0" sytle="overflow-y:hidden"></iframe>
                                    </div>
                                </td>
                                <td >
                                    <div>
                                        <iframe src="${ctx}/jrj/riskanalysis.do" height="433" id="rightiframe" name="rightiframe" width="530" frameborder="0" marginwidth="0" marginheight="0" scrolling="auto"></iframe>
                                    </div>
                                </td>
                            </tr>
                </table>
            </body>
        
        </html>
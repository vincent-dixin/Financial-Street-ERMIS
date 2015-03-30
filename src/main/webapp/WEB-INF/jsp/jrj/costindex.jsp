<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <html>
            
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
    <table id="jrjindex2" width="100%" height="100%" border="0">
      <tr>
       <td>
         <div >
           	<iframe src="${ctx}/jrj/costpage1.do" height="550" id="leftiframe" name="leftiframe" width="550" frameborder="0" marginwidth="0" marginheight="0" sytle="overflow-y:hidden"></iframe>
         </div>
       </td>
       <td>
          <div>
          	<iframe src="${ctx}/jrj/costpage2.do" height="550" id="rightiframe" name="rightiframe" width="550" frameborder="0" marginwidth="0" marginheight="0" sytle="overflow-y:hidden"></iframe>	
          </div>
       </td>
      </tr>
    </table>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
    <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
    <html>
        
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>目标树</title>
            <script type="text/javascript">
                    
                    
	            Ext.Loader.setPath({
	                'Ext.kpi': 'pages/kpi'
	            });
	           	Ext.require(['Ext.kpi.strategyMap.smtree']);
                    
                    

				var fhd_kpi_sm_tree_view ;
                Ext.onReady(function () {
                	fhd_kpi_sm_tree_view = Ext.create('Ext.kpi.strategyMap.smtree',{
                		renderTo:'FHD.kpi.smtree.view${param._dc}'
                	});
                })
            </script>
        </head>
        
        <body>
            <div id='FHD.kpi.smtree.view${param._dc}'></div>
        </body>
    
    </html>
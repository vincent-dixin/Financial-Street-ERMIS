<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
    <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
    <html>
        
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>记分卡树</title>
            <script type="text/javascript">
                    
                    
	            Ext.Loader.setPath({
	                'Ext.kpi': 'pages/kpi'
	            });
	           	Ext.require(['Ext.kpi.kpi.opt.kpicategorytree']);
                    
                    

				var kpi_category_tree_view ;
                Ext.onReady(function () {
                	kpi_category_tree_view = Ext.create('Ext.kpi.kpi.opt.kpicategorytree',{
                		renderTo:'FHD.kpi.kpicategorytree.view${param._dc}'
                	});
                })
            </script>
        </head>
        
        <body>
            <div id='FHD.kpi.kpicategorytree.view${param._dc}'></div>
        </body>
    
    </html>
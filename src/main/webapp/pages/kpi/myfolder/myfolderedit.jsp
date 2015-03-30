<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
 <%@ include file="/WEB-INF/jsp/commons/include-tagsOnly.jsp" %>
    <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
    <html>
        
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>所有度量指标</title>
            <script type="text/javascript">
           		
            	
            	Ext.Loader.setPath({
	                'Ext.kpi': 'pages/kpi'
	            });
	           	Ext.require(['Ext.kpi.myfolder.myfoldertabpanel']);
	         	
				var myfoldertab;
				
				//获取当前年份
                function getYear(){
                	var myDate = new Date();
                	var year = myDate.getFullYear();
                	return year;
                } 
				
				function myfolder_gatherResultFun(v) {
					if(FHD.data.yearId == ''){
                		FHD.data.yearId = this.getYear();
                	}
                	FHD.data.kpiName = v;
                    var rightUrl = __ctxPath + "/pages/kpi/kpi/gatherresulttable.jsp?kpiname=" + encodeURI(v) + "&editflag=true"  + "&yearId=" + FHD.data.yearId +"&type=myfolder";
                    fhd_kpi_kpiaccordion_view.initRightPanel(rightUrl);
                }

                Ext.onReady(function () {
                	myfoldertab = Ext.create('Ext.kpi.myfolder.myfoldertabpanel',{
                		renderTo:"FHD.kpi.myfolder.view${param._dc}",
                		kpiListUrl:__ctxPath+'/kpi/myfolder/findallkpirelaresult.f' +"?year="+FHD.data.yearId+"&month="+FHD.data.monthId+"&quarter="+FHD.data.quarterId+"&week="+FHD.data.weekId+"&eType="+FHD.data.eType+"&isNewValue="+FHD.data.isNewValue
                	});
                	
                	myfoldertab.getTabBar().insert(0, {
                        xtype: 'tbfill'
                    });
                	
                });

                Ext.apply(Ext.form.field.VTypes, {
                	

                });
            </script>
        </head>
        
        <body>
            <div id='FHD.kpi.myfolder.view${param._dc}'></div>
        </body>
    
    </html>
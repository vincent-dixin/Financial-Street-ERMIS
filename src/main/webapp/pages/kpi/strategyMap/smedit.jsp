<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
 <%@ include file="/WEB-INF/jsp/commons/include-tagsOnly.jsp" %>
    <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
    <html>
        
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>目标管理</title>
            <script type="text/javascript">
            
           		var kpicategory_paramdc = "${param._dc}";
           		var sm_paramdc = "${param._dc}";
           		var sm_editflag = "${param.editflag}";
           		var smid = "${param.id}";
           		var smparentid = "${param.parentid}";
           		var smname = "${param.smname}";
                var smparentname = "${param.parentname}";
                var chartIds = '';
                
           		Ext.Loader.setPath({
   	                'Ext.kpi': 'pages/kpi'
   	            });
           		
   	           	Ext.require(['Ext.kpi.strategyMap.smMainContainer']);
   	         	Ext.require(['Ext.kpi.strategyMap.smBasicPanel']);
   	         	Ext.require(['Ext.kpi.strategyMap.smKpisetContainer']);
   	         	Ext.require(['Ext.kpi.strategyMap.smWarningContainer']);
           
                var smcontainer;
				var smBasicPanel;
				var smKpisetContainer;
				var smWarningContainer;
				
				//获取当前年份
                function getYear(){
                	var myDate = new Date();
                	var year = myDate.getFullYear();
                	return year;
                }
              
                function sm_gatherResultFun(v) {
                	if(FHD.data.yearId == ''){
                		FHD.data.yearId = this.getYear();
                	}
                	FHD.data.kpiName = v;
                    var rightUrl = __ctxPath + "/pages/kpi/kpi/gatherresulttable.jsp?kpiname=" + encodeURI(v) + "&editflag=true" + "&yearId=" + FHD.data.yearId+"&smparentid="+smparentid+"&smid="+smid+"&smname="+smname+"&smparentname="+smparentname+"&type=sm" ;
                    fhd_kpi_kpiaccordion_view.initRightPanel(rightUrl);
                }
                
                Ext.onReady(function () {
                	smBasicPanel = Ext.create('Ext.kpi.strategyMap.smBasicPanel',{
                		smid:smid,
                		editflag:sm_editflag,
                		smparentid:smparentid,
                		smparentname:smparentname
                	});
                	smKpisetContainer = Ext.create('Ext.kpi.strategyMap.smKpisetContainer',{
                		smid:smid
                	});
                	smWarningContainer = Ext.create('Ext.kpi.strategyMap.smWarningContainer',{
                		smid:smid,
                		warningUrl:__ctxPath + '/kpi/kpistrategymap/findsmrelaalarmbysome.f?currentSmId=' + smid+"&editflag="+sm_editflag
                	});
                	
                	//根据记分卡ID查询图表类型信息
                    FHD.ajax({
                        url: __ctxPath + '/kpi/strategyMap/findStrategyMapById.f',
                        params: {
                            id: smid
                        },
                        callback: function (data) {
                        	chartIds = data.chartType==null?'':data.chartType;
                        	
                        	//仪表板url
                    		var angulargaugeUrl = __ctxPath + '/kpi/category/findcategoryrelakpi.f?id='+smid+"&year="+FHD.data.yearId+"&month="+FHD.data.monthId+"&quarter="+FHD.data.quarterId+"&week="+FHD.data.weekId+"&eType="+FHD.data.eType+"&isNewValue="+FHD.data.isNewValue + "&tableType=sm";
                            
                            //仪表板
                        	var angularGaugePanel = Ext.create('pages.chart.kpi.AngularGaugePanel', {
                         		targetId:smid,
                         		chartShowType:'0com_catalog_chart_type_1',
                         		typeTitle : '战略目标',
                         		dataType : 'str',
                         		url:angulargaugeUrl
                            });
                            
                        	analysisChartMainPanel = Ext.create('pages.chart.kpi.SMAnalysisChartMainPanel', {
                         		chartIds: chartIds,
                                cardItems: [
                                	angularGaugePanel
                                ]
                         	});
                            
                        	smcontainer = Ext.create('Ext.kpi.strategyMap.smTabPanel',{
                        		editflag:sm_editflag,
                        		destoryflag:'true',
                        		renderTo:"FHD.kpi.smedit.view${param._dc}",
                        		kpiListUrl:__ctxPath + "/kpi/kpistrategymap/findsmrelakpiresult.f?id=" + smid
                                +"&year="+FHD.data.yearId+"&month="+FHD.data.monthId+"&quarter="+FHD.data.quarterId+"&week="+FHD.data.weekId+"&eType="+FHD.data.eType+"&isNewValue="+FHD.data.isNewValue,
                        		cardItems:[
        	                		       smBasicPanel,
        	                		       smKpisetContainer,
        	                		       smWarningContainer
                        		          ],
                        	 	analysisChartMainPanel : analysisChartMainPanel
                        	});
                        	
                        	smcontainer.getTabBar().insert(0, {
                                 xtype: 'tbfill'
                             });
                        	
                        	
                        	if (sm_editflag == "" || sm_editflag == "true") {
                        		smcontainer.setActiveTab(0);
                            } else {
                            	smcontainer.setActiveTab(2);
                            }
                        	
                           /*smcontainer = Ext.create('Ext.kpi.strategyMap.smMainContainer',{
                        		editflag:sm_editflag,
                        		renderTo:"FHD.kpi.smedit.view${param._dc}",
                        		cardItems:[
        	                		       smBasicPanel,
        	                		       smKpisetContainer,
        	                		       smWarningContainer
                        		          ]
                        		
                        	}); */
                        	
                        	 var navigationBars = new Ext.scripts.component.NavigationBars();
                             navigationBars.renderHtml('SMNavigationBarsDiv', smid, '', 'sm');
                        	
                        }
                    });
                	
                	
                });

                Ext.apply(Ext.form.field.VTypes, {

                });
            </script>
        </head>
        
        <body>
        	<!-- <div style="padding-left:10px; border-left: 1px solid #99bce8;border-right: 1px solid #99bce8;">战略目标
                    <img src="${ctx}/images/icons/icon_rarrow.gif">
            </div>
            <div id='FHD.kpi.smedit.view${param._dc}'></div> -->
            
            <div style="width: 100%; height: 20px">
            		<div id="SMNavigationBarsDiv"></div>
            </div>
            <div id='FHD.kpi.smedit.view${param._dc}'></div>
        </body>
    
    </html>
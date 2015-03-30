<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
    <%@ include file="/WEB-INF/jsp/commons/include-tagsOnly.jsp" %>
        <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
        <html>
            
            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <title>记分卡编辑</title>
                <script type="text/javascript">
                	var kpicategory_paramdc = "${param._dc}";
                    var categoryid = "${param.categoryid}";
                    if("undefined"==categoryid){
                    	categoryid = "";
                    }
                    var categoryname = "${param.categoryname}";
                    var categoryparentid = "${param.categoryparentid}";
                    var editflag = "${param.editflag}";
                    var categoryparentname = "${param.categoryparentname}";
                    var category_editUrl = __ctxPath + "/pages/kpi/kpi/opt/kpicategoryedit.jsp?";
                    
                    var kpicategoryBasicPanel;
					var kpicategoryMainPanel;
					var kpicategoryWarningPanel;
					
					//chart 变量
					var analysisChartMainPanel;
			        var angularGaugePanel;
					var multiDimComparePanel;
					var trendPanel;
					var structuralAnalysisPanel;
					var chartIds='';
					
                    //仪表板url
            		var angulargaugeUrl = __ctxPath + '/kpi/category/findcategoryrelakpi.f?id='+categoryid+"&year="+FHD.data.yearId+"&month="+FHD.data.monthId+"&quarter="+FHD.data.quarterId+"&week="+FHD.data.weekId+"&eType="+FHD.data.eType+"&isNewValue="+FHD.data.isNewValue + "&tableType=sc";
            		//多维对比分析url
            		var multiDimCompareUrl = __ctxPath + '/kpi/category/findcategoryrelakpi.f?id='+categoryid+"&year="+FHD.data.yearId+"&month="+FHD.data.monthId+"&quarter="+FHD.data.quarterId+"&week="+FHD.data.weekId+"&eType="+FHD.data.eType+"&isNewValue="+FHD.data.isNewValue + "&tableType=sc";
            		//趋势分析url
            		var trendUrl = __ctxPath + '/kpi/category/findcategoryrelakpihistorydatas.f?id='+categoryid+"&year="+FHD.data.yearId+"&month="+FHD.data.monthId+"&quarter="+FHD.data.quarterId+"&week="+FHD.data.weekId+"&eType="+FHD.data.eType+"&isNewValue="+FHD.data.isNewValue;
					//结构化分析url
					var structuralAnalysisUrl = __ctxPath + '/kpi/category/findcategoryrelakpidata.f?id='+categoryid+"&year="+FHD.data.yearId+"&month="+FHD.data.monthId+"&quarter="+FHD.data.quarterId+"&week="+FHD.data.weekId+"&eType="+FHD.data.eType+"&isNewValue="+FHD.data.isNewValue;
            		
                    Ext.Loader.setPath({
                        'Ext.kpi': 'pages/kpi'
                    });
                    Ext.require(['Ext.kpi.kpi.opt.kpicategoryMainPanel']);
                    Ext.require(['Ext.kpi.kpi.opt.kpicategoryBasicPanel']);
                    Ext.require(['Ext.kpi.kpi.opt.kpicategoryWarningPanel']);
                    
                    //chart
                    Ext.require(['pages.chart.kpi.AngularGaugePanel']);
                    Ext.require(['pages.chart.kpi.MultiDimComparePanel']);
                    Ext.require(['pages.chart.kpi.TrendPanel']);
                    Ext.require(['pages.chart.kpi.StructuralAnalysisPanel']);
                   
                    //获取当前年份
                    function getYear(){
                    	var myDate = new Date();
                    	var year = myDate.getFullYear();
                    	return year;
                    } 

                    function category_gatherResultFun(v) {
                    	if(FHD.data.yearId == ''){
                    		FHD.data.yearId = this.getYear();
                    	}
                    	FHD.data.kpiName = v;
                        var rightUrl = __ctxPath + "/pages/kpi/kpi/gatherresulttable.jsp?kpiname=" + encodeURI(v) + "&parentid=${param.categoryparentid}" + "&editflag=true" + "&id=${param.categoryid}" + "&parentname=" + encodeURIComponent(categoryparentname) + "&yearId=" + FHD.data.yearId + "&categoryname=" + categoryname;
                        fhd_kpi_kpiaccordion_view.initRightPanel(rightUrl);
                    }
                    
                    //根据记分卡ID查询图表类型信息
                    FHD.ajax({
                        url: __ctxPath + '/kpi/category/findcharttypebyid.f',
                        params: {
                            id: categoryid
                        },
                        callback: function (data) {
                        	chartIds = data.chartType==null?'':data.chartType;
                        }
                    });

				
                    Ext.onReady(function () {
                    	//记分卡基本信息panel
                    	kpicategoryBasicPanel = Ext.create('Ext.kpi.kpi.opt.kpicategoryBasicPanel', {
                    		categoryid: categoryid,
                            editflag: editflag,
                            categoryparentid: categoryparentid,
                            categoryparentname: categoryparentname

                        });
                    	//记分卡告警信息
                    	kpicategoryWarningPanel = Ext.create('Ext.kpi.kpi.opt.kpicategoryWarningPanel', {
                             warningUrl:__ctxPath + "/kpi/category/findcategoryrelaalarmbysome.f?id=" + categoryid+"&editflag="+editflag
                        });
                    	 
                    	//仪表板
                    	angularGaugePanel = Ext.create('pages.chart.kpi.AngularGaugePanel', {
                     		targetId:categoryid,
                     		chartShowType:'0com_catalog_chart_type_1',
                     		typeTitle : '记分卡',
                     		dataType : 'sc',
                     		url:angulargaugeUrl
                        });
                     	//多维对比分析
                     	multiDimComparePanel = Ext.create('pages.chart.kpi.MultiDimComparePanel', {
                     		targetId:categoryid,
                     		chartShowType:'0com_catalog_chart_type_3',
                     		url:multiDimCompareUrl
                        });
                     	//趋势分析
                     	trendPanel = Ext.create('pages.chart.kpi.TrendPanel', {
                     		targetId:categoryid,
                     		chartShowType:'0com_catalog_chart_type_4',
                     		url:trendUrl
                        });
                     	//结构化分析
                     	 structuralAnalysisPanel = Ext.create('pages.chart.kpi.StructuralAnalysisPanel', {
                     		targetId:categoryid,
                     		chartShowType:'0com_catalog_chart_type_5',
                     		url:structuralAnalysisUrl
                        }); 
                     	
                     	//图表分析主面板
                     	//if('' != categoryid && 'category_root' != categoryid){
                     		analysisChartMainPanel = Ext.create('pages.chart.kpi.AnalysisChartMainPanel', {
                         		chartIds: chartIds,
                                cardItems: [
                                	angularGaugePanel, multiDimComparePanel, trendPanel, structuralAnalysisPanel
                                ]
                         	});
                     	//}
                     	
                    	//记分卡主面板
                    	kpicategoryMainPanel = Ext.create('Ext.kpi.kpi.opt.kpicategoryMainPanel', {
                    		destoryflag:'true',
                    		categoryid: categoryid,
                            editflag: editflag,
                            categoryparentid: categoryparentid,
                            categoryparentname: categoryparentname,
                            categoryname:categoryname,
                            kpiListUrl: __ctxPath + "/kpi/category/findcategoryrelakpiresult.f?id=" + categoryid
                            	+"&year="+FHD.data.yearId+"&month="+FHD.data.monthId+"&quarter="+FHD.data.quarterId+"&week="+FHD.data.weekId+"&eType="+FHD.data.eType+"&isNewValue="+FHD.data.isNewValue,
                            renderTo: "FHD.kpi.kpicategoryedit.view${param._dc}",
                            cardItems: [
                                kpicategoryBasicPanel, kpicategoryWarningPanel
                                /* , {
                                	title: FHD.locale.get("fhd.sys.auth.authority.authority"),border:false,last:function(cardPanel,finishflag){
                                	var layout = cardPanel.getLayout();
                                    if(finishflag){
                                    	cardPanel.getLayout().setActiveItem(0);
                                    }else{
                                    	cardPanel.pageMove("next");
                                    }
                                    Ext.getCmp('kpicategory_move-prev'+kpicategory_paramdc).setDisabled(!layout.getPrev());
                                    Ext.getCmp('kpicategory_move-next'+kpicategory_paramdc).setDisabled(!layout.getNext());
                                    Ext.getCmp('kpicategory_move-prev_top'+kpicategory_paramdc).setDisabled(!layout.getPrev());
                                    Ext.getCmp('kpicategory_move-next_top'+kpicategory_paramdc).setDisabled(!layout.getNext());
                                }
                              } */
                                
                            ],
                            analysisChartMainPanel:analysisChartMainPanel
                        });
                    	
                    	//FHD.componentResize(kpicategoryMainPanel, 265, 16);

                        kpicategoryMainPanel.getTabBar().insert(0, {
                            xtype: 'tbfill'
                        });
                        
                        if (editflag == "false") {
                        	kpicategoryMainPanel.setActiveTab(2);
                        } else {
                        	kpicategoryMainPanel.setActiveTab(0);
                        }

                        var navigationBars = new Ext.scripts.component.NavigationBars();
                        navigationBars.renderHtml('SCNavigationBarsDiv', categoryid, '', 'sc');
                    })
                </script>
            </head>
            
            <body>
            	<div style="width: 100%; height: 20px">
            		<div id="SCNavigationBarsDiv"></div>
            	</div>
                <div id='FHD.kpi.kpicategoryedit.view${param._dc}'></div>
            </body>
        
        </html>
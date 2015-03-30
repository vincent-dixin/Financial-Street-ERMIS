<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
    <%@ include file="/WEB-INF/jsp/commons/include-tagsOnly.jsp" %>
        <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
        <html>
            
            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <title>指标类型编辑</title>
                <script type="text/javascript">
	                
	                var param_dc ="${param._dc}"; 
                    var editflag = "${param.editflag}";
                    var currentId = "${param.id}";
                    var kpitypename = "${param.name}";

                    Ext.Loader.setPath({
                        'Ext.kpi': 'pages/kpi'
                    });
                   	Ext.require(['Ext.kpi.kpi.opt.kpiTypeMainPanel']);
                    Ext.require(['Ext.kpi.kpi.opt.kpiTypeBasicPanel']);
                    Ext.require(['Ext.kpi.kpi.opt.kpiTypeGatherPanel']);
                    Ext.require(['Ext.kpi.kpi.opt.kpiTypeWarningPanel']);

                  //获取当前年份
                    function getYear(){
                    	var myDate = new Date();
                    	var year = myDate.getFullYear();
                    	return year;
                    }
                  
                    function kpitype_gatherResultFun(v) {
                    	if(FHD.data.yearId == ''){
                    		FHD.data.yearId = this.getYear();
                    	}
                    	FHD.data.kpiName = v;
                        var rightUrl = __ctxPath + "/pages/kpi/kpi/gatherresulttable.jsp?kpiname=" + encodeURI(v) + "&editflag=true"  + "&yearId=" + FHD.data.yearId +"&kpitypeId="+currentId+"&kpitypeName="+kpitypename+"&type=kpitype";
                        fhd_kpi_kpiaccordion_view.initRightPanel(rightUrl);
                    }


                    function kpitypeValueToFormula(id, name) {
                        Ext.getCmp('kpityperesultFormulaSelector'+param_dc).setTargetId(id);
                        Ext.getCmp('kpityperesultFormulaSelector'+param_dc).setTargetName(name);
                        Ext.getCmp('kpitypetargetFormulaSelector'+param_dc).setTargetId(id);
                        Ext.getCmp('kpitypetargetFormulaSelector'+param_dc).setTargetName(name);
                        Ext.getCmp('kpitypeassessmentFormulaSelector'+param_dc).setTargetId(id);
                        Ext.getCmp('kpitypeassessmentFormulaSelector'+param_dc).setTargetName(name);
                    }

                    var kpiTypeMainPanel;
                    var kpitype_container;
                    
                    Ext.onReady(function () {
                    	
                        var kpitypebasicPanel = Ext.create('Ext.kpi.kpi.opt.kpiTypeBasicPanel', {
                            currentId: currentId,
                            editflag: editflag
                        });

                        var kpiTypeGatherPanel = Ext.create('Ext.kpi.kpi.opt.kpiTypeGatherPanel', {
                            currentId: currentId,
                            kpitypename: kpitypename,
                            editflag: editflag
                        });

                        var kpiTypeWarningPanel = Ext.create('Ext.kpi.kpi.opt.kpiTypeWarningPanel', {
                            warningUrl: __ctxPath + "/kpi/kpi/findkpirelaalarmbysome.f?id=" + currentId+"&editflag="+editflag
                        });
                        kpiTypeMainPanel = Ext.create('Ext.kpi.kpi.opt.kpiTypeMainPanel', {
                        	destoryflag:'true',
                            editflag: editflag,
                            kpiListUrl: __ctxPath + "/kpi/kpi/findkpityperelaresult.f?id=" + currentId
                            +"&year="+FHD.data.yearId+"&month="+FHD.data.monthId+"&quarter="+FHD.data.quarterId+"&week="+FHD.data.weekId+"&eType="+FHD.data.eType+"&isNewValue="+FHD.data.isNewValue,
                            cardItems: [kpitypebasicPanel, kpiTypeGatherPanel, kpiTypeWarningPanel
                                        
                           /* , {
                                title: FHD.locale.get("fhd.sys.auth.authority.authority"),border:false,last:function(cardPanel, finishflag){
                                	var layout = cardPanel.getLayout();
                                     if (finishflag) {
                                        kpiTypeMainPanel._gotopage();
                                    } else {
                                        cardPanel.pageMove("next");
                                        kpiTypeMainPanel._navBtnState(cardPanel);
                                    } 
                                }
                            } */
                            
                            ]
                        });
                        
                        

                        kpiTypeMainPanel.getTabBar().insert(0, {
                            xtype: 'tbfill'
                        });
                        
                        kpitype_container = Ext.create('Ext.container.Container',{
                    		items:[kpiTypeMainPanel],
                    		layout: "fit",
                    		renderTo: "FHD.kpi.kpitypeedit.view${param._dc}"
                    	}); 
            			
            			//FHD.componentResize(kpitype_container, 265, 16);
                        
                        if (editflag == "" || editflag == "true") {
                        	kpiTypeMainPanel.setActiveTab(0);
                        } else {
                        	kpiTypeMainPanel.setActiveTab(1);
                        }
                        
                        var navigationBars = new Ext.scripts.component.NavigationBars();
                        navigationBars.renderHtml('kpiNavigationBarsDiv', currentId, '', 'kpi');
                    })


                     Ext.apply(Ext.form.field.VTypes, {

                    });
                </script>
            </head>
            
            <body>
                <!-- <div style="padding-left:10px; border-left: 1px solid #99bce8;border-right: 1px solid #99bce8;">指标类型
                    <img src="${ctx}/images/icons/icon_rarrow.gif">
                </div>
                <div id='FHD.kpi.kpitypeedit.view${param._dc}'></div> -->
                
                <div style="width: 100%; height: 20px">
            		<div id="kpiNavigationBarsDiv"></div>
            	</div>
                <div id='FHD.kpi.kpitypeedit.view${param._dc}'></div>
            </body>
        
        </html>
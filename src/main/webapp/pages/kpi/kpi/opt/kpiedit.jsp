<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
    <%@ include file="/WEB-INF/jsp/commons/include-tagsOnly.jsp" %>
        <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
        <html>
            
            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <title>指标添加编辑页面</title>
                <script type="text/javascript">
                    var parentKpiId;
                    var kpi_kpi_paramdc = "${param._dc}";
                    var editflag = "${param.editflag}";
                    var categoryid = "${param.categoryid}";
                    var categoryname = "${param.categoryname}";
                    var categoryparentid = "${param.categoryparentid}";
                    var categoryparentname = "${param.categoryparentname}";
                    var kpiname = "${param.kpiname}";
                    var kpiId = "${param.kpiId}";
                    var kpi_kpi_opflag = "edit";
                    if (editflag == "false") {
                        kpi_kpi_opflag = "add";
                    }

                    /*给公式赋值,公式编辑器需要设置它的targetid和targetname
                      targetid为指标ID,targetname为指标名称
                    */
                    function kpiValueToFormula(id, name) {
                        var resultformula = Ext.getCmp('kpi_kpi_kpityperesultFormulaSelector' + kpi_kpi_paramdc);
                        var targetformula = Ext.getCmp('kpi_kpi_kpitypetargetFormulaSelector' + kpi_kpi_paramdc);
                        var assessmentformula = Ext.getCmp('kpi_kpi_kpitypeassessmentFormulaSelector' + kpi_kpi_paramdc);
                        resultformula.setTargetId(id);
                        resultformula.setTargetName(name);
                        targetformula.setTargetId(id);
                        targetformula.setTargetName(name);
                        assessmentformula.setTargetId(id);
                        assessmentformula.setTargetName(name);
                    }


                    Ext.Loader.setPath({
                        'Ext.kpi': 'pages/kpi'
                    });
                    Ext.require(['Ext.kpi.kpi.opt.kpiMainPanel']);
                    Ext.require(['Ext.kpi.kpi.opt.kpiBasicPanel']);
                    Ext.require(['Ext.kpi.kpi.opt.KpiNameSelector']);
                    var kpiMainPanel;
                    var kpiBasicPanel;
                    var kpiGatherPanel;
                    var kpiWarningPanel;




                    Ext.onReady(function () {

                        kpiBasicPanel = Ext.create('Ext.kpi.kpi.opt.kpiBasicPanel', {
                            kpiId: kpiId,
                            editflag: editflag
                        });

                        kpiGatherPanel = Ext.create('Ext.kpi.kpi.opt.kpiGatherPanel', {
                            kpiId: kpiId,
                            editflag: editflag,
                            kpiname: kpiname
                        });

                        kpiWarningPanel = Ext.create('Ext.kpi.kpi.opt.kpiWarningPanel', {
//                            warningUrl: __ctxPath + "/kpi/kpi/findkpirelaalarmbysome.f?id=" + kpiId+"&editflag="+editflag
                            warningUrl: __ctxPath + "/kpi/kpi/findkpirelaalarmbysome.f",
                            kpiId:kpiId,
                            editflag:editflag
                        });

                        kpiMainPanel = Ext.create('Ext.kpi.kpi.opt.kpiMainPanel', {
                            destoryflag: 'true',
                            editflag: editflag,
                            categoryid: categoryid,
                            categoryname: categoryname,
                            categoryparentid: categoryparentid,
                            categoryparentname: categoryparentname,
                            renderTo: "FHD.kpi.kpikpiedit.view${param._dc}",
                            cardItems: [
                            kpiBasicPanel,
                            kpiGatherPanel,
                            kpiWarningPanel
]
                        });



                        var navigationBars = new Ext.scripts.component.NavigationBars();
                        navigationBars.renderHtml('kpieditNavDiv', categoryid, '', 'sc');


                    })


                     Ext.apply(Ext.form.field.VTypes, {

                    });
                </script>
            </head>
            
            <body>
                <div style="width: 100%; height: 35px">
                    <div id="kpieditNavDiv"></div>
                </div>
                <div id='FHD.kpi.kpikpiedit.view${param._dc}'></div>
            </body>
        
        </html>
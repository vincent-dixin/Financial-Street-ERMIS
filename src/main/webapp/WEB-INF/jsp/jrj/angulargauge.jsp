<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <html>
            
            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <style type="text/css">
				</style>
                <c:set var="ctx" value="${pageContext.request.contextPath}" />
                <script src="${ctx}/scripts/jquery-1.4.2.min.js" type="text/javascript"></script>
                <script type="text/javascript" src="${ctx}/scripts/old-edition/chart/Charts/FusionCharts.js"></script>
                <script>
                    $(function () {
                        init();
                    });

                    function init() {
                        loadSCData();
                    }
                     //加载记分卡数据

                    function loadSCData() {
                        $.ajax({
                                type: "POST",
                                url: "${ctx}/jrj/createangulargaugechart.f",
                                data: "scid=${param.scid}",
                                success: function (jsObj) {
                                    var jsObjs = $.parseJSON(jsObj)
                                    createAngulargauge(jsObjs.chartArray);
                                    if ("1" == jsObjs.level) {
                                        $("#risktitle").text("金融街集团关注的重点风险指标");
                                    } else if ("2" == jsObjs.level && jsObjs.riskName) {
                                        $("#risktitle").text(jsObjs.riskName);
                                    }
                                }
                            });
                    }

                    function createAngulargauge(chartArray) {
                        var url = "#";
                        var table = $("<table>");     
                        table.appendTo($("#createtable"));   
                        var tr = $("<tr></tr>");    
                        if ("${param.scid}" != 'root') {
                            $("#toolbarhref").attr({
                                    href: "${ctx}/jrj/angulargauge.do?scid=" + chartArray[0].parentscid
                                });
                        }
                        tr.appendTo(table);        
                        for (var j = 0; j < chartArray.length; j++) {
                            var chartObj = chartArray[j];
                            if (chartObj.hasChild == "false") {
                                $("#backtool").show();
                            }
                            var td = $("<td align='left' >");
                            td.appendTo(tr);
                            if (j != 0) {
                                var columndiv = $("<div style='border: 2px solid #C6C2C2;margin-left: 15px;'>");
                            } else {
                                var columndiv = $("<div style='border: 2px solid #C6C2C2;margin-left: 0px;'>");
                            }

                            columndiv.appendTo(td);
                            var cname = chartObj.name;
                            if (chartObj.small == "true") {
                                var titleDiv = $("<div  align='left' style='margin-top:5px'><span style='font-family:Microsoft YaHei,微软雅黑;font-size: 12px;font-weight: 800;color:#1960ad;margin:5 0 0 5'>" + cname + "</span></div>");
                            } else {
                                var titleDiv = $("<div  align='left' style='margin-top:5px'><span style='font-family:Microsoft YaHei,微软雅黑;font-size: 15px;font-weight: 800;color:#1960ad;margin:5 0 0 5'>" + cname + "</span></div>");
                            }
                            titleDiv.appendTo(columndiv);
                            var chartid = 'chartId' + j;
                            var chartdiv = $("<div  align='center' style='margin: 0;' id='" + chartid + "'  ></div>");
                            chartdiv.appendTo(columndiv);
                            var btnDiv = $("<div  align='left' style='margin-top:2px'><span style='font-family:Microsoft YaHei,微软雅黑;font-size: 12px;font-weight: 800;color:#1960ad;margin:5 0 0 5'>风险包含" + chartObj.kpicount + "指标</span></div>");
                            btnDiv.appendTo(columndiv);
                            var dataString1 = chartObj.xml;
                            if (chartObj.small == "true") {
                                var chart1 = new FusionCharts('${ctx}/images/chart/' + "AngularGauge.swf", chartid + '-chart', "190", "122");
                            } else {
                                var chart1 = new FusionCharts('${ctx}/images/chart/' + "AngularGauge.swf", chartid + '-chart', "242", "113");
                            }
                            chart1.setXMLData(dataString1);
                            chart1.render(chartid);
                            columndiv.append("</div>");
                            td.append("</td>");
                        }
                        $("#createtable").append("</table>");
                        $("#createtable").css("margin-top", "10px");
                    }
                </script>
            </head>
            
            <body>
                <div>
                    <table style="width: 100%">
                        <tr id='toolbar' style="background-image: url('${ctx}/images/jrj-portal-nav.gif');">
                            <td width="100%"><a id='toolbarhref' href='#' target='headiframe' style="text-decoration: none;font-family:Microsoft YaHei,微软雅黑;"><span style="margin: 0 0 0 8;"><font style="font-size: 15px" id="risktitle"></font></span><span id="backtool" style="display: none; margin: 0 0 0 915;"><span></a>

                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div id='createtable'></div>
                            </td>
                        </tr>
                    </table>
                </div>
            </body>
        
        </html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
    <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
    <html>
        
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>指标结果采集入录</title>
            <script type="text/javascript">
                var kpigatherresult_view = (function () {
                    var kpiname = "${param.kpiname}";
                    var gatherUrl = __ctxPath+"/kpi/kpi/findkpigatherresultbyname.f?name="+encodeURIComponent(kpiname);
                    var gather_grid = Ext.create('FHD.ux.EditorGridPanel', {
                        border: false,
                        pagable: false,
                        checked: false,
                        url: gatherUrl,
                        height: FHD.getCenterPanelHeight() - 150,
                        cols: [
						{
						    dataIndex: 'id',
						    id: 'id',
						    width: 0
						},       
                        {
                            header: "时间",
                            flex: 1,
                            dataIndex: 'name',
                            sortable: true,
                            flex: 1
                        },  {
                            header: "<span><div data-qtitle='' data-qtip='" + FHD.locale.get("fhd.sys.planEdit.status") + "'>" + '<img src="' + __ctxPath + '/images/icons/' + 'icon_statushead' + '.gif">' + "</div></span>",
                            dataIndex: 'assessmentStatus',
                            sortable: true,
                            flex: 0.2,
                            renderer: function (v) {
                                var color = "";
                                var display = "";
                                if (v == "icon-ibm-symbol-4-sm") {
                                    color = "symbol_4_sm";
                                    display = FHD.locale.get("fhd.alarmplan.form.low");
                                } else if (v == "icon-ibm-symbol-6-sm") {
                                    color = "symbol_6_sm";
                                    display = FHD.locale.get("fhd.alarmplan.form.hight");
                                } else if (v == "icon-ibm-symbol-5-sm") {
                                    color = "symbol_5_sm";
                                    display = FHD.locale.get("fhd.alarmplan.form.min");
                                }
                                var text = color != "" ? '<img src="' + __ctxPath + '/images/icons/' + color + '.gif">' : "";
                                return "<div data-qtitle='' data-qtip='" + display + "'>" + text + "</div>";
                            }
                        }, {
                            header: "<span><div data-qtitle='' data-qtip='" + FHD.locale.get("fhd.kpi.kpi.form.directionto") + "'>" + '<img src="' + __ctxPath + '/images/icons/' + 'icon_trendhead' + '.gif">' + "</div></span>",
                            dataIndex: 'directionstr',
                            sortable: true,
                            flex: 0.2,
                            renderer: function (v) {
                                var color = "";
                                var display = "";
                                if (v == "icon-ibm-icon-trend-rising-positive") {
                                    color = "icon_trend_rising_positive";
                                    display = FHD.locale.get("fhd.kpi.kpi.prompt.positiv");
                                } else if (v == "icon-ibm-icon-trend-neutral-null") {
                                    color = "icon_trend_neutral_null";
                                    display = FHD.locale.get("fhd.kpi.kpi.prompt.flat");
                                } else if (v == "icon-ibm-icon-trend-falling-negative") {
                                    color = "icon_trend_falling_negative";
                                    display = FHD.locale.get("fhd.kpi.kpi.prompt.negative");
                                }
                                var text = color != "" ? '<img src="' + __ctxPath + '/images/icons/' + color + '.gif">' : "";
                                return "<div data-qtitle='' data-qtip='" + display + "'>" + text + "</div>";
                            }
                        }, {
                            header: FHD.locale.get('fhd.kpi.kpi.form.finishValue'),
                            dataIndex: 'finishValue',
                            sortable: true,
                            flex: 0.8,
                            align: 'right'
                        }, {
                            header: FHD.locale.get('fhd.kpi.kpi.form.targetValue'),
                            dataIndex: 'targetValue',
                            sortable: true,
                            flex: 0.8,
                            align: 'right'
                        }, {
                            header: FHD.locale.get('fhd.kpi.kpi.form.assessmentValue'),
                            dataIndex: 'assessmentValue',
                            sortable: true,
                            flex: 0.8,
                            align: 'right'
                        }],
                        tbarItems: [{
                            tooltip: FHD.locale.get('fhd.strategymap.strategymapmgr.form.set'),
                            xtype: 'button',
                            iconCls: 'icon-cog',
                            columnWidth: 0.1,
                            handler: function () {

                            }
                        }]
                    });
                    Ext.define('kpigatherresult.view', {
                        gatherpanel: null,
                        init: function () {
                            this.gatherpanel = Ext.create('Ext.panel.Panel', {
                                renderTo: 'kpigatherresult${param._dc}',
                                border: false,
                                layout: "fit",
                                height: FHD.getCenterPanelHeight(),
                                items: [gather_grid]
                            });

                        }

                    });

                    var kpigatherresult_view = Ext.create('kpigatherresult.view');
                    return kpigatherresult_view;

                }

                )();

                Ext.onReady(function () {
                    kpigatherresult_view.init();
                    FHD.componentResize(kpigatherresult_view.container, 0, 0);
                })
            </script>
        </head>
        
        <body>
            <div id='kpigatherresult${param._dc}'></div>
        </body>
    
    </html>
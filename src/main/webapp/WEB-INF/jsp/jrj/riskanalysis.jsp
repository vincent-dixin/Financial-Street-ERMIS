<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <html>

        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <style type="text/css">
                table.imagetables {
                    font-family: verdana, arial, sans-serif;
                    font-size: 12px;
                    color: #333333;
                    border-width: 1px;
                    border-color: #999999;
                    border-collapse: collapse;
                }
                table.imagetables th {
                    background-image: url('images/white-top-bottom.gif');
                    border-width: 1px;
                    padding: 8 0 8 0;
                    text-align: center;
                    border-style: solid;
                    border-color: #999999;
                }
                table.imagetables td {
                    background-color: white;
                    border-width: 1px;
                    padding: 8 0 8 0;
                    text-align: center;
                    border-style: solid;
                    border-color: #999999;
                }
            </style>
            <c:set var="ctx" value="${pageContext.request.contextPath}" />
            <script src="${ctx}/scripts/jquery-1.7.2.min.js" type="text/javascript"></script>
            <script type="text/javascript" src="${ctx}/scripts/old-edition/chart/Charts/FusionCharts.js"></script>
            <script>
                $(function () {
                    init();
                });

                function init() {
                    var id = '${param.id}';
                    var chartType = '';
                    var frequecy = "";
                    if ("zichijiudian" == id) {
                    	//20.自持酒店
                        frequecy = "0frequecy_month";
                        chartType = 'MSLine';
                        createSingleColumnLineChart(id, frequecy, chartType); 

                    }  
                    else if ("fangdichan" == id || "baoxian" == id || "wenhua" == id || "wuye" == id) {
                    	//14 客户满意度-房地产      15 客户满意度-保险   16 客户满意度-物业    17 客户满意度-文化 
                        frequecy = "0frequecy_year";
                        chartType = 'MSLine';
                        createSingleColumnLineChart(id, frequecy, chartType);
                    }
                    
                    else if("yingye_shouru_zhengzhanglv"==id//5.营业收入增长率
                    		|| "yingye_shouru_jihua_wangchenglv" == id//6.营业收入计划完成率
                    		|| "jinglirun_zhengzhanglv_mugongshi" == id//7.净利润(归属于母公司)增长率
                    		|| "jinglirun_jihua_wanchenglv_mugongshi" == id //8.净利润(归属于母公司)计划完成率
                    		|| "zongzhichan_zhouzhuanlv" == id//9.总资产周转率
                    		|| "chengben_feiyong_lirenlv" == id //10.成本费用利润率
                    		|| "maolilv" == id //11.毛利率
                    		)
                    {
                    	 frequecy = "0frequecy_month";
                         chartType = 'ScrollLine2D';
                         createAlarmLineChart(id, frequecy, chartType);
                    }

                    //else if (
                    		/*"zongzhichan_zhouzhuanlv" == id*/ //9.总资产周转率
                    	  /*|| "jingying_huodong_xianjin_liuliang_jinger" == id*///新需求已经去掉该指标 经营活动产生的现金流量净额 
                    		/*|| "yingye_shouru_zhengzhanglv" == id*/ //5.营业收入增长率
                    		/*|| "yingye_shouru_jihua_wangchenglv" == id*/ //6.营业收入计划完成率
                    		/*|| "jinglirun_zhengzhanglv_mugongshi" == id*/ //7.净利润(归属于母公司)增长率
                    		/*|| "jinglirun_jihua_wanchenglv_mugongshi" == id *///8.净利润(归属于母公司)计划完成率
                    		/* "chengben_feiyong_lirenlv" == id ||*///10.成本费用利润率
                    		/* "maolilv" == id *///11.毛利率
                    //		) {
                    	
                    //    frequecy = "0frequecy_month";
                    //    chartType = 'Line';
                    //    createAlarmLineChart(id, frequecy, chartType);
                    //}

                    else if ('3_xianjin_baozhang_nengli' == id//1.现金/3个月内刚性支出
                    		||'6_xianjin_baozhang_nengli'==id//1.现金/6个月内刚性支出
                    		||"zhichan_fuzhailv" == id//2.资产负债率
                    		||"youxi_fuzhailv" == id//3.有息负债率
                    		|| "jingji_zhengjiazhi_eva_shichang" == id//4.经济增加值(EVA)-市场
                    		|| "guanjian_wangwei_kongquelv" == id //13.关键岗位空岗率
                    		|| "zonghe_shouyi_jihua_wanchenglv" == id //21.(本年度累计完成净利润+同期可供出售公允价值变动)/当年度综合收益目标
                    		) {
                        frequecy = "0frequecy_quarter";
                        chartType = 'ScrollLine2D';
                        //chartType = 'Line';
                        createAlarmLineChart(id, frequecy, chartType);
                    } 
                    //else if (
                    		/*"zhichan_fuzhailv" == id ||*/ //2.资产负债率
                    		/* "youxi_fuzhailv" == id ||*///3.有息负债率
                    		/* "guanjian_wangwei_kongquelv" == id *///13.关键岗位空岗率
                    		/*|| "6_xianjin_baozhang_nengli" == id *///1.现金/6个月内刚性支出
                    	  /*|| 'jingji_zhengjiazhi_eva' == id*///新需求去掉了该指标  经济增加值(EVA) 
                    		/*|| "jingji_zhengjiazhi_eva_shichang" == id*/ //4.经济增加值(EVA)-市场
                    	  /*|| 'jingji_zhengjiazhi_eva_zhengfu' == id *///新需求去掉了该指标 经济增加值(EVA)-政府
                    	  /*|| "biaozhun_baofei" == id*/ //新需求去掉了该指标  标准保费
                    		// "zonghe_shouyi_jihua_wanchenglv" == id//21.(本年度累计完成净利润+同期可供出售公允价值变动)/当年度综合收益目标
                    //		) {
                    //    frequecy = "0frequecy_quarter";
                    //    chartType = 'Line';
                    //    createAlarmLineChart(id, frequecy, chartType);
                    //} 
                    else if ("hengtai_zhengquan_yingye_shouru" == id//22.（恒泰证券营业收入-竞争组别平均值）/竞争组别平均值 
                    		|| "hengtai_zhengquan_zhichan_shouyi" == id //22.（恒泰证券净资产收益率-竞争组别平均值）/竞争组别平均值
                    		) {
                        frequecy = "0frequecy_halfyear";
                        chartType = 'Line';
                        createAlarmLineChart(id, frequecy, chartType);
                    } 
                    else if ("china_fangdichan_qiye_paiming" == id) {
                    	//19.金融街控股公司  本年度房地产百强综合实力排名-上年度综合实力排名
                        frequecy = "0frequecy_year";
                        chartType = 'InverseMSLine';
                        createAlarmLineChart(id, frequecy, chartType);
                    } else if ("quanguo_yingyuan_paiming" == id) {
                    	//23.全国影院票房排名
                        frequecy = "0frequecy_month";
                        chartType = 'InverseMSLine';
                        createAlarmLineChart(id, frequecy, chartType);
                    } 
                    else if(
                    		"renli_chengben_lirenlv" == id //	 //12.人力成本利润率
                    		||"qiye_neihan_jiazhi" == id//21.原指标名称为企业内含价值,新需求改名称为新业务价值
                    ){
                    	frequecy = "0frequecy_year";
                        chartType = 'ScrollLine2D';
                        createAlarmLineChart(id, frequecy, chartType);
                    }
                    //else if (
                    		/*"renli_chengben_lirenlv" == id||*/ //12.人力成本利润率
                    	   /*"qiye_neihan_jiazhi" == id*///21.原指标名称为企业内含价值,新需求改名称为新业务价值
                  //  		) {
                  //      frequecy = "0frequecy_year";
                  //      chartType = 'Line';
                   //     createAlarmLineChart(id, frequecy, chartType);
                    //} 
                    else if ("redstatus" == id) {
                        getStatus();
                    } else if ("jituan_neibu_chufa" == id) {
                    	//25.集团内部处罚同比变动情况
                        createJituanNeibuChufaChart();
                    } else if ("waibu_xingzheng_chufa" == id) {
                    	//24.外部行政监管处罚
                        createWaibuXingzhengChufaChart();
                    } else if ("anquan_zhishu" == id) {
                    	//18.安全指数
                        createAnquanZhishuChart();
                    }

                }

                function getStatus() {
                    //1.发送ajax向后台取灯状态
                    $.ajax({
                        type: "POST",
                        url: "${ctx}/jrj/findredstatus.f",
                        data: {},
                        success: function (redStatus) {
                            $("#kpiname").text("红   色   预  警  列  表");
                            var status = $.parseJSON(redStatus);
                            topten(status);

                        }
                    });
                }

                function topten(redStatus) {
                    var kpiname = '';
                    var aurl = '';
                    var countTable = '<tr><td colspan="2" ><div style="margin: 0 0 0 20"><table width="480" class="imagetables" ><tr>' + '<th width=80 style="font-size:12px;">指标总个数</th>' + '<th width=80 style="font-size:12px;background:red">红色预警项</th>' + '<th width=80 style="font-size:12px;background:#FE9F06">橙色预警项</th>' + '<th width=80 style="font-size:12px;background:#FAFA06" >黄色预警项</th>' + '<th width=80 style="font-size:12px;background:#3EF90F">绿色无风险项</th>' + '</tr>' + '<tr>' + '<td id="totlecounts">25</td>' + '<td style="background:red" id="redcounts"></td>' + '<td style="background:#FE9F06" id="orangecounts"></td>' + '<td style="background:#FAFA06" id="yellowcounts"></td>' + '<td style="background:#3EF90F" id="greencounts"></td>' + '</tr></table></div></td></tr>';
                    $("#head_tr").append(countTable);
                    $("#redcounts").text(redStatus.redcounts);
                    $("#orangecounts").text(redStatus.orangecounts);
                    $("#yellowcounts").text(redStatus.yellowcounts);
                    $("#greencounts").text(redStatus.greencounts);

                    for (var key in redStatus) {
                        kpiname = '';
                        aurl = '';
                        if (key == "3_xianjin_baozhang_nengli") {
                            kpiname = "1.现金/3个月内刚性支出" ;
                        } else if (key == "6_xianjin_baozhang_nengli") {
                            kpiname = "1.现金/6个月内刚性支出" ;
                        } else if (key == "zhichan_fuzhailv") {
                            kpiname = "2.资产负债率";
                        } else if (key == "youxi_fuzhailv") {
                            kpiname = "3.有息负债率" ;
                        }
                        else if (key == "jingji_zhengjiazhi_eva_shichang") {
                            kpiname = "4.经济增加值(EVA)-市场" ;
                        }
                        ////新需求去掉了该指标
                        /* else if (key == "jingji_zhengjiazhi_eva") {
                                kpiname = "4.经济增加值(EVA)";
                            }  */
                        /* else if (key == "jingji_zhengjiazhi_eva_zhengfu") {
                                kpiname = "6.经济增加值(EVA)-政府";
                            } */
                        /* else if (key == "jingying_huodong_xianjin_liuliang_jinger") {
                                kpiname = "7.经营活动产生的现金流量净额";
                            }  */
                        //新加指标综合收益计划完成率
                        else if (key == "zonghe_shouyi_jihua_wanchenglv") {
                            kpiname = "21.长城保险公司—综合收益计划完成率" ;
                        } else if (key == "yingye_shouru_zhengzhanglv") {
                            kpiname = "5.营业收入增长率" ;
                        } else if (key == "yingye_shouru_jihua_wangchenglv") {
                            kpiname = "6.营业收入计划完成率" ;
                        } else if (key == "jinglirun_zhengzhanglv_mugongshi") {
                            kpiname = "7.净利润（归属于母公司）增长率";
                        } else if (key == "jinglirun_jihua_wanchenglv_mugongshi") {
                            kpiname = "8.净利润（归属于母公司）计划完成率" ;
                        } else if (key == "zongzhichan_zhouzhuanlv") {
                            kpiname = "9.总资产周转率" ;
                        } else if (key == "chengben_feiyong_lirenlv") {
                            kpiname = "10.成本费用利润率";
                        } else if (key == "maolilv") {
                            kpiname = "11.毛利率" ;
                        } else if (key == "renli_chengben_lirenlv") {
                            kpiname = "12.人力成本利润率" ;
                        } else if (key == "guanjian_wangwei_kongquelv") {
                            kpiname = "13.关键岗位空岗率" ;
                        } else if (key == "fangdichan") {
                            kpiname = "14.客户满意度-房地产";
                        } else if (key == "baoxian") {
                            kpiname = "15.客户满意度-保险" ;
                        } else if (key == "wenhua") {
                            kpiname = "16.客户满意度-文化" ;
                        } else if (key == "wuye") {
                            kpiname = "17.客户满意度-物业" ;
                        } else if (key == "anquan_zhishu") {
                            kpiname = "18.安全指数";
                        } else if (key == "china_fangdichan_qiye_paiming") {
                            kpiname = "19.金融街控股公司" ;
                        } else if (key == "zichijiudian") {
                            kpiname = "20.自持酒店   " ;
                        } else if (key == "biaozhun_baofei") {
                            kpiname = "21.标准保费" ;
                        } else if (key == "qiye_neihan_jiazhi") {
                            kpiname = "21.长城保险公司—新业务价值" ;
                        } else if (key == "hengtai_zhengquan_zhichan_shouyi") {
                            kpiname = "22.恒泰证劵公司—同组别竞争企业净资产收益率比较" ;
                        } else if (key == "hengtai_zhengquan_yingye_shouru") {
                            kpiname = "22.恒泰证劵公司—同组别竞争企业营业收入比较" ;
                        } else if (key == "quanguo_yingyuan_paiming") {
                            kpiname = "23.首都电影院西单影院" ;
                        } else if (key == "waibu_xingzheng_chufa") {
                            kpiname = "24.外部行政监管处罚 " ;
                        } else if (key == "jituan_neibu_chufa") {
                            kpiname = "25.集团内部处罚";
                        }
						
                        if (kpiname.length > 0) {
                        	kpiname+= "(" + redStatus[key] + ")";
                            var tr = '<tr><td width="10%" align="center" ><image id="red" src="${ctx}/images/icons/symbol_jrj_r_sm.gif"/></td><td style=color:"#000000" ><span style=font-weight:bold>' + kpiname + '</span></td></tr>';
                            var blanktr = '<tr><td colspan="2" width="10%" align="center" ></tr>'
                            $("#head_tr").append(tr);
                        }

                        $("#head_tr").append(blanktr);
                    }

                }


                function createAnquanZhishuChart() {
                    var id = "anquan_zhishu";
                    var chartType = "Scatter";
                    var urlobj = {
                        id: id,
                        frequecy: "0frequecy_month"
                    };
                    $.ajax({
                        type: "POST",
                        url: "${ctx}/jrj/alarmlinechart.f",
                        data: urlobj,
                        success: function (xmljson) {
                            var xmlobj = $.parseJSON(xmljson);
                            if ("noData" == xmlobj.xml) {
                                createChart(id, document.getElementById('anquan_zhishu_xml').innerHTML, chartType, "安全指数");
                            } else {
                                createChart(id, xmlobj.xml, chartType, "安全指数");
                            }
                        }
                    });
                }

                function createJituanNeibuChufaChart() {
                    var id = "jituan_neibu_chufa";
                    var chartType = "MSLine";
                    //横向滚动条
                    //var chartType = "ScrollLine2D";
                    var urlobj = {
                        id: id,
                        frequecy: "0frequecy_year"
                    };
                    $.ajax({
                        type: "POST",
                        url: "${ctx}/jrj/alarmlinechart.f",
                        data: urlobj,
                        success: function (xmljson) {
                            var xmlobj = $.parseJSON(xmljson);
                            createChart(id, xmlobj.xml, chartType, "集团内部处罚");
                        }
                    });

                }

                function createWaibuXingzhengChufaChart() {
                    var id = "waibu_xingzheng_chufa";
                    var chartType = "Scatter";
                    var urlobj = {
                        id: id,
                        frequecy: "0frequecy_month"
                    };
                    $.ajax({
                        type: "POST",
                        url: "${ctx}/jrj/alarmlinechart.f",
                        data: urlobj,
                        success: function (xmljson) {
                            var xmlobj = $.parseJSON(xmljson);
                            if ("noData" == xmlobj.xml) {
                                createChart(id, document.getElementById('waibu_xingzheng_chufa_xml').innerHTML, chartType, "外部行政监管处罚");
                            } else {
                                createChart(id, xmlobj.xml, chartType, "外部行政监管处罚");
                            }
                        }
                    });
                }


                function createAlarmLineChart(id, frequecy, chartType) {
                    var urlobj = {
                        id: id,
                        frequecy: frequecy
                    };
                    $.ajax({
                        type: "POST",
                        url: "${ctx}/jrj/alarmlinechart.f",
                        data: urlobj,
                        success: function (xmljson) {
                            var xmlobj = $.parseJSON(xmljson);
                            createChart(id, xmlobj.xml, chartType, xmlobj.kpiname);
                        }
                    });
                }

                function createSingleColumnLineChart(id, frequecy, chartType) {
                    var urlobj = {
                        id: id,
                        frequecy: frequecy
                    };
                    $.ajax({
                        type: "POST",
                        url: "${ctx}/jrj/singlecolumnchart.f",
                        data: urlobj,
                        success: function (xmljson) {
                            var xmlobj = $.parseJSON(xmljson)
                            createChart(id, xmlobj.xml, chartType, xmlobj.kpiname);
                        }
                    });
                }

                function createChart(id, xmldata, type, kpiname) {
                    $("#kpiname").text(convertkpiname(kpiname));
                    var chart = new FusionCharts('${ctx}/images/chart/' + type + ".swf", id + '-chart', "500", "380");
                    chart.setXMLData(xmldata);
                    chart.render("chartdiv");
                }

                function convertkpiname(kpiname) {

                    var name = kpiname;
                    if (kpiname == "经济增加值EVA市场") {
                        name = "经济增加值（EVA）-市场"
                    } 
                    /*
                    else if (kpiname == "经济增加值EVA") {
                        name = "经济增加值（EVA）"
                    } 
                    else if (kpiname == "经济增加值EVA政府") {
                        name = "经济增加值（EVA）-政府"
                    }*/
                    else if (kpiname == "净利润增长率归属于母公司") {
                        name = "净利润（归属于母公司）增长率"
                    } else if (kpiname == "净利润计划完成率归属于母公司") {
                        name = "净利润（归属于母公司）计划完成率"
                    } else if (kpiname == "房地产") {
                        name = "客户满意度-房地产"
                    } else if (kpiname == "保险") {
                        name = "客户满意度-保险"
                    } else if (kpiname == "物业") {
                        name = "客户满意度-物业"
                    } else if (kpiname == "文化") {
                        name = "客户满意度-文化"
                    }
                    return name;
                }
            </script>
        </head>

        <body>
            <xmp id="jituan_neibu_chufa_xml" style="display:none;">
                <chart showValues='1' numberSuffix='%' alternateHGridAlpha='5' canvasBorderColor='F7F2EA' canvasBorderThickness='0' numDivlines='-1' bgcolor='FFFFFF' baseFontColor='666666' baseFontSize='12' lineColor='666666' showAlternateVGridColor='1' showYAxisValues='0'
                showBorder='0' canvasBorderColor='F7F2EA' palette='2' xAxisMaxValue='100' xAxisMinValue='10' yAxisMinValue='-100' yAxisMaxValue='100'>
                    <categories verticalLineColor='666666' verticalLineThickness='1'>
                        <category label='2012' x='25' showVerticalLine='0' />
                        <category label='2013' x='50' showVerticalLine='0' />
                        <category label='2014' x='75' showVerticalLine='0' />
                        <category label='2015' x='100' showVerticalLine='0' />
                    </categories>
                    <dataset seriesName='处罚数量同比' color='009900' anchorSides='4' anchorRadius='4' anchorBgColor='D5FFD5' anchorBorderColor='009900'>
                        <!-- 数量同比 -->
                        <set y='-89.47' x='17' toolText='-89.47%' />
                    </dataset>
                    <dataset seriesName='处罚金额同比' color='0000FF' anchorSides='4' anchorRadius='4' anchorBgColor='C6C6FF' anchorBorderColor='0000FF'>
                        <!-- 金额同比 -->
                        <set y='-83.3' x='25' toolText='-83.3%' />
                    </dataset>
                    <trendlines>
                        <line startValue='0' displayValue='0' />
                        <line startValue='0' endValue='100' color='FF654F' isTrendZone='1' showOnTop='0' displayValue='危险' lineThickness='2' valueOnRight='1' dashed='1' dashGap='5' />
                        <line startValue='0' endValue='-100' isTrendZone='1' showOnTop='0' displayValue='安全' lineThickness='2' color='8BBA00' valueOnRight='1' dashed='1' dashGap='5' />
                    </trendlines>
                </chart>
            </xmp>
            <xmp id="waibu_xingzheng_chufa_xml" style="display:none;">
                <chart caption='截止目前,未发生需要预警的外部行政处罚' alternateHGridAlpha='5' canvasBorderColor='F7F2EA' canvasBorderThickness='0' numDivlines='-1' bgcolor='FFFFFF' baseFontColor='666666' baseFontSize='12' lineColor='666666' numVDivlines='7' showAlternateVGridColor='1'
                showYAxisValues='0' showBorder='0' canvasBorderColor='F7F2EA' palette='2' xAxisMaxValue='120' xAxisMinValue='0' yAxisMinValue='-20' yAxisMaxValue='100'>
                    <categories verticalLineColor='666666' verticalLineThickness='1'>
                        <category label='1月' x='1' showVerticalLine='0' />
                        <category label='2月' x='10' showVerticalLine='0' />
                        <category label='3月' x='20' showVerticalLine='0' />
                        <category label='4月' x='30' showVerticalLine='0' />
                        <category label='5月' x='40' showVerticalLine='0' />
                        <category label='6月' x='50' showVerticalLine='0' />
                        <category label='7月' x='60' showVerticalLine='0' />
                        <category label='8月' x='70' showVerticalLine='0' />
                        <category label='9月' x='80' showVerticalLine='0' />
                        <category label='10月' x='90' showVerticalLine='0' />
                        <category label='11月' x='100' showVerticalLine='0' />
                        <category label='12月' x='110' showVerticalLine='0' />
                    </categories>
                    <dataset seriesName='处罚金额      单位:万元' color='009900' anchorSides='0' anchorRadius='0' anchorBgColor='D5FFD5' anchorBorderColor='009900'>
                        <set y='15' x='24' toolText='24万' />
                        <set y='0.1' x='32' toolText='32万' />
                        <set y='86.47' x='72' toolText='72万' />
                    </dataset>
                    <trendlines>
                        <line startValue='10' displayValue='10' />
                        <line startValue='10' endValue='100' color='FF654F' isTrendZone='1' showOnTop='0' displayValue='危险' lineThickness='2' valueOnRight='1' dashed='1' dashGap='5' />
                        <line startValue='10' endValue='-20' isTrendZone='1' showOnTop='0' displayValue='安全' lineThickness='2' color='8BBA00' valueOnRight='1' dashed='1' dashGap='5' />
                    </trendlines>
                </chart>

            </xmp>
            <xmp id="anquan_zhishu_xml" style="display:none;">
                <chart caption='截止目前，未发生需预警的突发安全事件' alternateHGridAlpha='5' canvasBorderColor='F7F2EA' canvasBorderThickness='0' numDivlines='-1' bgcolor='FFFFFF' baseFontColor='666666' baseFontSize='12' lineColor='666666' numVDivlines='7' showAlternateVGridColor='1'
                showYAxisValues='0' showBorder='0' canvasBorderColor='F7F2EA' palette='2' xAxisMaxValue='120' xAxisMinValue='0' yAxisMinValue='1' yAxisMaxValue='6'>
                    <categories verticalLineColor='666666' verticalLineThickness='1'>
                        <category label='1月' x='1' showVerticalLine='0' />
                        <category label='2月' x='10' showVerticalLine='0' />
                        <category label='3月' x='20' showVerticalLine='0' />
                        <category label='4月' x='30' showVerticalLine='0' />
                        <category label='5月' x='40' showVerticalLine='0' />
                        <category label='6月' x='50' showVerticalLine='0' />
                        <category label='7月' x='60' showVerticalLine='0' />
                        <category label='8月' x='70' showVerticalLine='0' />
                        <category label='9月' x='80' showVerticalLine='0' />
                        <category label='10月' x='90' showVerticalLine='0' />
                        <category label='11月' x='100' showVerticalLine='0' />
                        <category label='12月' x='110' showVerticalLine='0' />
                    </categories>
                    <dataset color='009900' anchorSides='0' anchorRadius='0' anchorBgColor='D5FFD5' anchorBorderColor='009900'>
                        <set y='2.4' x='21' />
                    </dataset>
                    <dataset color='0000FF' anchorSides='0' anchorRadius='0' anchorBgColor='C6C6FF' anchorBorderColor='0000FF'>
                        <set y='1.4' x='23' />
                    </dataset>
                    <trendlines>
                        <line startValue='5' displayValue='5' />
                        <line startValue='4' displayValue='4' />
                        <line startValue='3' displayValue='3' />　
                        <line startValue='5' endValue='6' color='FF654F' isTrendZone='1' showOnTop='0' displayValue='危险' lineThickness='2' valueOnRight='1' dashed='1' dashGap='5' />　
                        <line startValue='4' endValue='5' color='F59630' isTrendZone='1' showOnTop='0' displayValue='风险' lineThickness='2' valueOnRight='1' dashed='1' dashGap='5' />　
                        <line startValue='3' endValue='4' color='ffe600' isTrendZone='1' showOnTop='0' displayValue='警告' lineThickness='2' valueOnRight='1' dashed='1' dashGap='5' />
                        <line startValue='1' endValue='3' isTrendZone='1' showOnTop='0' displayValue='安全' lineThickness='2' color='8BBA00' valueOnRight='1' dashed='1' dashGap='5' />
                    </trendlines>
                </chart>
            </xmp>
            <div id='mainDiv'>
                <div id='titleDiv' style='font-family:Microsoft YaHei,微软雅黑;border:1px solid #999999;height:40px;background-image: url("${ctx}/images/jrj-portal-nav.gif");text-align: center;border-bottom: none;'><span style='font-size: 15px;font-weight: bold;color:#000000;margin: 10 0 0 135;
float: left;width:255' id='kpiname'></span>

                </div>
                <div id="chartdiv" style='border: 1px solid #999999;font-family:Microsoft YaHei,微软雅黑;height:390px;'>
                    <table width="500" height="100%">
                        <tr id="head_tr"></tr>
                    </table>
                </div>
            </div>
        </body>

        </html>
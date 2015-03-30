<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <html>

        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <link rel="stylesheet" type="text/css" href="<c:url value='/css/Weaver.css'/>" />
            <link rel="stylesheet" type="text/css" href="<c:url value='/css/FHDstyle.css'/>" />
            <style type="text/css">
            </style>
            <c:set var="ctx" value="${pageContext.request.contextPath}" />
            <script src="${ctx}/scripts/jquery-1.4.2.min.js" type="text/javascript"></script>
            <script>
                var statusRed;
                $(function () {
                    init();
                    getStatus();
                });

                function getStatus() {
                    //1.发送ajax向后台取灯状态
                    $.ajax({
                        type: "POST",
                        url: "${ctx}/jrj/findredstatus.f",
                        data: {},
                        success: function (redStatus) {
                            statusRed = $.parseJSON(redStatus);
                        }
                    });
                }

                function init() {
                    //1.发送ajax向后台取灯状态
                    $.ajax({
                        type: "POST",
                        url: "${ctx}/jrj/findallkpistatus.f",
                        success: function (allKpiStatus) {
                            //2.给image的src属性赋值
                            initStatus($.parseJSON(allKpiStatus));
                            parent.rightiframe.location = "${ctx}/jrj/riskanalysis.do?id=redstatus";
                        }
                    });

                }

                function initRight(id) {

                    parent.rightiframe.location = "${ctx}/jrj/riskanalysis.do?id=" + id;
                }

                function initStatus(allKpiStatus) {
                    for (var key in allKpiStatus) {
                    	var k = key;
                        $("#" + k).attr({
                            src: "${ctx}" + allKpiStatus[key]
                        });
                    }
                }


                function showColor(v) {
                    var objs = $(".showColor");
                    var objs2 = $("." + v);
                    for (var i = 0; i < objs.length; i++) {
                        $(objs[i]).css("background-color", "");
                    }
                    for (var i = 0; i < objs2.length; i++) {
                        $(objs2[i]).css("background-color", "#E6F0F0");
                    }
                }


                colors2 = new Array(6);
                colors2[0] = "#FFFFFF";
                colors2[1] = "#FFFFFF";
                colors2[0] = "#000000";
                colors2[1] = "#000000";
                colors2[4] = "#ffffff";
                colors2[5] = "#FFFFFF";
                var i = 0;

                function shan() {
                    if (i < 6) {
                        for (var key in statusRed) {
                            if (key == "3_xianjin_baozhang_nengli") {
                                document.getElementById("n1").style.color = colors2[i];
                                document.getElementById("x1").style.color = colors2[i];
                                document.getElementById("m31").style.color = colors2[i];
                                document.getElementById("j31").style.color = colors2[i];
                                document.getElementById("n1").style.fontWeight = '800';
                                document.getElementById("x1").style.fontWeight = '800';
                                document.getElementById("m31").style.fontWeight = '800';
                                document.getElementById("j31").style.fontWeight = '800';
                                document.getElementById("n1").style.background = '#FF3546';
                                document.getElementById("x1").style.background = '#FF3546';
                                document.getElementById("m31").style.background = '#FF3546';
                                document.getElementById("j31").style.background = '#FF3546';
                            } else if (key == "6_xianjin_baozhang_nengli") {
                                document.getElementById("n1").style.color = colors2[i];
                                document.getElementById("x1").style.color = colors2[i];
                                document.getElementById("m22").style.color = colors2[i];
                                document.getElementById("j22").style.color = colors2[i];
                                document.getElementById("n1").style.fontWeight = '800';
                                document.getElementById("x1").style.fontWeight = '800';
                                document.getElementById("m22").style.fontWeight = '800';
                                document.getElementById("j22").style.fontWeight = '800';
                                document.getElementById("n1").style.background = '#FF3546';
                                document.getElementById("x1").style.background = '#FF3546';
                                document.getElementById("m22").style.background = '#FF3546';
                                document.getElementById("j22").style.background = '#FF3546';
                            } else if (key == "zhichan_fuzhailv") {
                                document.getElementById("n2").style.fontWeight = '800';
                                document.getElementById("x2").style.fontWeight = '800';
                                document.getElementById("m2").style.fontWeight = '800';
                                document.getElementById("j2").style.fontWeight = '800';
                                document.getElementById("n2").style.background = '#FF3546';
                                document.getElementById("x2").style.background = '#FF3546';
                                document.getElementById("m2").style.background = '#FF3546';
                                document.getElementById("j2").style.background = '#FF3546';
                                document.getElementById("n2").style.color = colors2[i];
                                document.getElementById("x2").style.color = colors2[i];
                                document.getElementById("m2").style.color = colors2[i];
                                document.getElementById("j2").style.color = colors2[i];
                            } else if (key == "youxi_fuzhailv") {
                                document.getElementById("n3").style.color = colors2[i];
                                document.getElementById("x3").style.color = colors2[i];
                                document.getElementById("m3").style.color = colors2[i];
                                document.getElementById("j3").style.color = colors2[i];
                                document.getElementById("n3").style.fontWeight = '800';
                                document.getElementById("x3").style.fontWeight = '800';
                                document.getElementById("m3").style.fontWeight = '800';
                                document.getElementById("j3").style.fontWeight = '800';
                                document.getElementById("n3").style.background = '#FF3546';
                                document.getElementById("x3").style.background = '#FF3546';
                                document.getElementById("m3").style.background = '#FF3546';
                                document.getElementById("j3").style.background = '#FF3546';
                            }
                            //金融街需求变更,停用了三个指标 经济增加值EVA,经济增加值EVA-政府,经营活动产生的现金流量净额
                            /* else if (key == "jingji_zhengjiazhi_eva") {
		                            	document.getElementById("n4").style.color = colors2[i];
		        				    	document.getElementById("x4").style.color = colors2[i];
		        				    	document.getElementById("m4").style.color = colors2[i];
		        				    	document.getElementById("j4").style.color = colors2[i];
		        				    	document.getElementById("n4").style.fontWeight='800';
		        				    	document.getElementById("x4").style.fontWeight='800';
		        				    	document.getElementById("m4").style.fontWeight='800';
		        				    	document.getElementById("j4").style.fontWeight='800';
		                            	document.getElementById("n4").style.background ='#FF3546';
		        				    	document.getElementById("x4").style.background ='#FF3546';
		        				    	document.getElementById("m4").style.background ='#FF3546';
		        				    	document.getElementById("j4").style.background ='#FF3546';
		                            }  */
                            else if (key == "jingji_zhengjiazhi_eva_shichang") {
                                document.getElementById("n5").style.color = colors2[i];
                                document.getElementById("x5").style.color = colors2[i];
                                document.getElementById("m5").style.color = colors2[i];
                                document.getElementById("j5").style.color = colors2[i];
                                document.getElementById("n5").style.fontWeight = '800';
                                document.getElementById("x5").style.fontWeight = '800';
                                document.getElementById("m5").style.fontWeight = '800';
                                document.getElementById("j5").style.fontWeight = '800';
                                document.getElementById("n5").style.background = '#FF3546';
                                document.getElementById("x5").style.background = '#FF3546';
                                document.getElementById("m5").style.background = '#FF3546';
                                document.getElementById("j5").style.background = '#FF3546';
                            }
                            //金融街需求变更,停用了三个指标 经济增加值EVA,经济增加值EVA-政府,经营活动产生的现金流量净额
                            /* else if (key == "jingji_zhengjiazhi_eva_zhengfu") {
		                            	document.getElementById("n6").style.color = colors2[i];
		        				    	document.getElementById("x6").style.color = colors2[i];
		        				    	document.getElementById("m6").style.color = colors2[i];
		        				    	document.getElementById("j6").style.color = colors2[i];
		        				    	document.getElementById("n6").style.fontWeight='800';
		        				    	document.getElementById("x6").style.fontWeight='800';
		        				    	document.getElementById("m6").style.fontWeight='800';
		        				    	document.getElementById("j6").style.fontWeight='800';
		                            	document.getElementById("n6").style.background ='#FF3546';
		        				    	document.getElementById("x6").style.background ='#FF3546';
		        				    	document.getElementById("m6").style.background ='#FF3546';
		        				    	document.getElementById("j6").style.background ='#FF3546';
		                            } */
                            //金融街需求变更,停用了三个指标 经济增加值EVA,经济增加值EVA-政府,经营活动产生的现金流量净额
                            /* else if (key == "jingying_huodong_xianjin_liuliang_jinger") {
		                            	document.getElementById("n7").style.color = colors2[i];
		        				    	document.getElementById("x7").style.color = colors2[i];
		        				    	document.getElementById("m7").style.color = colors2[i];
		        				    	document.getElementById("j7").style.color = colors2[i];
		        				    	document.getElementById("n7").style.fontWeight='800';
		        				    	document.getElementById("x7").style.fontWeight='800';
		        				    	document.getElementById("m7").style.fontWeight='800';
		        				    	document.getElementById("j7").style.fontWeight='800';
		                            	document.getElementById("n7").style.background ='#FF3546';
		        				    	document.getElementById("x7").style.background ='#FF3546';
		        				    	document.getElementById("m7").style.background ='#FF3546';
		        				    	document.getElementById("j7").style.background ='#FF3546';
		                            } */
                            else if (key == "yingye_shouru_zhengzhanglv") {
                                document.getElementById("n8").style.color = colors2[i];
                                document.getElementById("x8").style.color = colors2[i];
                                document.getElementById("m8").style.color = colors2[i];
                                document.getElementById("j8").style.color = colors2[i];
                                document.getElementById("n8").style.fontWeight = '800';
                                document.getElementById("x8").style.fontWeight = '800';
                                document.getElementById("m8").style.fontWeight = '800';
                                document.getElementById("j8").style.fontWeight = '800';
                                document.getElementById("n8").style.background = '#FF3546';
                                document.getElementById("x8").style.background = '#FF3546';
                                document.getElementById("m8").style.background = '#FF3546';
                                document.getElementById("j8").style.background = '#FF3546';
                            } else if (key == "yingye_shouru_jihua_wangchenglv") {
                                document.getElementById("n9").style.color = colors2[i];
                                document.getElementById("x9").style.color = colors2[i];
                                document.getElementById("m9").style.color = colors2[i];
                                document.getElementById("j9").style.color = colors2[i];
                                document.getElementById("n9").style.fontWeight = '800';
                                document.getElementById("x9").style.fontWeight = '800';
                                document.getElementById("m9").style.fontWeight = '800';
                                document.getElementById("j9").style.fontWeight = '800';
                                document.getElementById("n9").style.background = '#FF3546';
                                document.getElementById("x9").style.background = '#FF3546';
                                document.getElementById("m9").style.background = '#FF3546';
                                document.getElementById("j9").style.background = '#FF3546';
                            } else if (key == "jinglirun_zhengzhanglv_mugongshi") {
                                document.getElementById("n10").style.color = colors2[i];
                                document.getElementById("x10").style.color = colors2[i];
                                document.getElementById("m10").style.color = colors2[i];
                                document.getElementById("j10").style.color = colors2[i];
                                document.getElementById("n10").style.fontWeight = '800';
                                document.getElementById("x10").style.fontWeight = '800';
                                document.getElementById("m10").style.fontWeight = '800';
                                document.getElementById("j10").style.fontWeight = '800';
                                document.getElementById("n10").style.background = '#FF3546';
                                document.getElementById("x10").style.background = '#FF3546';
                                document.getElementById("m10").style.background = '#FF3546';
                                document.getElementById("j10").style.background = '#FF3546';
                            } else if (key == "jinglirun_jihua_wanchenglv_mugongshi") {
                                document.getElementById("n11").style.color = colors2[i];
                                document.getElementById("x11").style.color = colors2[i];
                                document.getElementById("m11").style.color = colors2[i];
                                document.getElementById("j11").style.color = colors2[i];
                                document.getElementById("n11").style.fontWeight = '800';
                                document.getElementById("x11").style.fontWeight = '800';
                                document.getElementById("m11").style.fontWeight = '800';
                                document.getElementById("j11").style.fontWeight = '800';
                                document.getElementById("n11").style.background = '#FF3546';
                                document.getElementById("x11").style.background = '#FF3546';
                                document.getElementById("m11").style.background = '#FF3546';
                                document.getElementById("j11").style.background = '#FF3546';
                            } else if (key == "zongzhichan_zhouzhuanlv") {
                                document.getElementById("n12").style.color = colors2[i];
                                document.getElementById("x12").style.color = colors2[i];
                                document.getElementById("m12").style.color = colors2[i];
                                document.getElementById("j12").style.color = colors2[i];
                                document.getElementById("n12").style.fontWeight = '800';
                                document.getElementById("x12").style.fontWeight = '800';
                                document.getElementById("m12").style.fontWeight = '800';
                                document.getElementById("j12").style.fontWeight = '800';
                                document.getElementById("n12").style.background = '#FF3546';
                                document.getElementById("x12").style.background = '#FF3546';
                                document.getElementById("m12").style.background = '#FF3546';
                                document.getElementById("j12").style.background = '#FF3546';
                            } else if (key == "chengben_feiyong_lirenlv") {
                                document.getElementById("n13").style.color = colors2[i];
                                document.getElementById("x13").style.color = colors2[i];
                                document.getElementById("m13").style.color = colors2[i];
                                document.getElementById("j13").style.color = colors2[i];
                                document.getElementById("n13").style.fontWeight = '800';
                                document.getElementById("x13").style.fontWeight = '800';
                                document.getElementById("m13").style.fontWeight = '800';
                                document.getElementById("j13").style.fontWeight = '800';
                                document.getElementById("n13").style.background = '#FF3546';
                                document.getElementById("x13").style.background = '#FF3546';
                                document.getElementById("m13").style.background = '#FF3546';
                                document.getElementById("j13").style.background = '#FF3546';
                            } else if (key == "maolilv") {
                                document.getElementById("n14").style.color = colors2[i];
                                document.getElementById("x14").style.color = colors2[i];
                                document.getElementById("m14").style.color = colors2[i];
                                document.getElementById("j14").style.color = colors2[i];
                                document.getElementById("n14").style.fontWeight = '800';
                                document.getElementById("x14").style.fontWeight = '800';
                                document.getElementById("m14").style.fontWeight = '800';
                                document.getElementById("j14").style.fontWeight = '800';
                                document.getElementById("n14").style.background = '#FF3546';
                                document.getElementById("x14").style.background = '#FF3546';
                                document.getElementById("m14").style.background = '#FF3546';
                                document.getElementById("j14").style.background = '#FF3546';
                            } else if (key == "renli_chengben_lirenlv") {
                                document.getElementById("n15").style.color = colors2[i];
                                document.getElementById("x15").style.color = colors2[i];
                                document.getElementById("m15").style.color = colors2[i];
                                document.getElementById("j15").style.color = colors2[i];
                                document.getElementById("n15").style.fontWeight = '800';
                                document.getElementById("x15").style.fontWeight = '800';
                                document.getElementById("m15").style.fontWeight = '800';
                                document.getElementById("j15").style.fontWeight = '800';
                                document.getElementById("n15").style.background = '#FF3546';
                                document.getElementById("x15").style.background = '#FF3546';
                                document.getElementById("m15").style.background = '#FF3546';
                                document.getElementById("j15").style.background = '#FF3546';
                            } else if (key == "guanjian_wangwei_kongquelv") {
                                document.getElementById("n16").style.color = colors2[i];
                                document.getElementById("x16").style.color = colors2[i];
                                document.getElementById("m16").style.color = colors2[i];
                                document.getElementById("j16").style.color = colors2[i];
                                document.getElementById("n16").style.fontWeight = '800';
                                document.getElementById("x16").style.fontWeight = '800';
                                document.getElementById("m16").style.fontWeight = '800';
                                document.getElementById("j16").style.fontWeight = '800';
                                document.getElementById("n16").style.background = '#FF3546';
                                document.getElementById("x16").style.background = '#FF3546';
                                document.getElementById("m16").style.background = '#FF3546';
                                document.getElementById("j16").style.background = '#FF3546';
                            } else if (key == "fangdichan") {
                                document.getElementById("n17").style.color = colors2[i];
                                document.getElementById("x17").style.color = colors2[i];
                                document.getElementById("m17").style.color = colors2[i];
                                document.getElementById("j17").style.color = colors2[i];
                                document.getElementById("n17").style.fontWeight = '800';
                                document.getElementById("x17").style.fontWeight = '800';
                                document.getElementById("m17").style.fontWeight = '800';
                                document.getElementById("j17").style.fontWeight = '800';
                                document.getElementById("n17").style.background = '#FF3546';
                                document.getElementById("x17").style.background = '#FF3546';
                                document.getElementById("m17").style.background = '#FF3546';
                                document.getElementById("j17").style.background = '#FF3546';
                            } else if (key == "baoxian") {
                                document.getElementById("n18").style.color = colors2[i];
                                document.getElementById("x18").style.color = colors2[i];
                                document.getElementById("m18").style.color = colors2[i];
                                document.getElementById("j18").style.color = colors2[i];
                                document.getElementById("n18").style.fontWeight = '800';
                                document.getElementById("x18").style.fontWeight = '800';
                                document.getElementById("m18").style.fontWeight = '800';
                                document.getElementById("j18").style.fontWeight = '800';
                                document.getElementById("n18").style.background = '#FF3546';
                                document.getElementById("x18").style.background = '#FF3546';
                                document.getElementById("m18").style.background = '#FF3546';
                                document.getElementById("j18").style.background = '#FF3546';
                            } else if (key == "wenhua") {
                                document.getElementById("n19").style.color = colors2[i];
                                document.getElementById("x19").style.color = colors2[i];
                                document.getElementById("m19").style.color = colors2[i];
                                document.getElementById("j19").style.color = colors2[i];
                                document.getElementById("n19").style.fontWeight = '800';
                                document.getElementById("x19").style.fontWeight = '800';
                                document.getElementById("m19").style.fontWeight = '800';
                                document.getElementById("j19").style.fontWeight = '800';
                                document.getElementById("n19").style.background = '#FF3546';
                                document.getElementById("x19").style.background = '#FF3546';
                                document.getElementById("m19").style.background = '#FF3546';
                                document.getElementById("j19").style.background = '#FF3546';
                            } else if (key == "wuye") {
                                document.getElementById("n20").style.color = colors2[i];
                                document.getElementById("x20").style.color = colors2[i];
                                document.getElementById("m20").style.color = colors2[i];
                                document.getElementById("j20").style.color = colors2[i];
                                document.getElementById("n20").style.fontWeight = '800';
                                document.getElementById("x20").style.fontWeight = '800';
                                document.getElementById("m20").style.fontWeight = '800';
                                document.getElementById("j20").style.fontWeight = '800';
                                document.getElementById("n20").style.background = '#FF3546';
                                document.getElementById("x20").style.background = '#FF3546';
                                document.getElementById("m20").style.background = '#FF3546';
                                document.getElementById("j20").style.background = '#FF3546';
                            } else if (key == "anquan_zhishu") {
                                document.getElementById("n21").style.color = colors2[i];
                                document.getElementById("x21").style.color = colors2[i];
                                document.getElementById("m21").style.color = colors2[i];
                                document.getElementById("j21").style.color = colors2[i];
                                document.getElementById("n21").style.fontWeight = '800';
                                document.getElementById("x21").style.fontWeight = '800';
                                document.getElementById("m21").style.fontWeight = '800';
                                document.getElementById("j21").style.fontWeight = '800';
                                document.getElementById("n21").style.background = '#FF3546';
                                document.getElementById("x21").style.background = '#FF3546';
                                document.getElementById("m21").style.background = '#FF3546';
                                document.getElementById("j21").style.background = '#FF3546';
                            } else if (key == "china_fangdichan_qiye_paiming") {
                                document.getElementById("n22").style.color = colors2[i];
                                document.getElementById("x22").style.color = colors2[i];
                                document.getElementById("m22").style.color = colors2[i];
                                document.getElementById("j22").style.color = colors2[i];
                                document.getElementById("n22").style.fontWeight = '800';
                                document.getElementById("x22").style.fontWeight = '800';
                                document.getElementById("m22").style.fontWeight = '800';
                                document.getElementById("j22").style.fontWeight = '800';
                                document.getElementById("n22").style.background = '#FF3546';
                                document.getElementById("x22").style.background = '#FF3546';
                                document.getElementById("m22").style.background = '#FF3546';
                                document.getElementById("j22").style.background = '#FF3546';
                            } else if (key == "zichijiudian") {
                                document.getElementById("n23").style.color = colors2[i];
                                document.getElementById("x23").style.color = colors2[i];
                                document.getElementById("m23").style.color = colors2[i];
                                document.getElementById("j23").style.color = colors2[i];
                                document.getElementById("n23").style.fontWeight = '800';
                                document.getElementById("x23").style.fontWeight = '800';
                                document.getElementById("m23").style.fontWeight = '800';
                                document.getElementById("j23").style.fontWeight = '800';
                                document.getElementById("n23").style.background = '#FF3546';
                                document.getElementById("x23").style.background = '#FF3546';
                                document.getElementById("m23").style.background = '#FF3546';
                                document.getElementById("j23").style.background = '#FF3546';
                            } else if (key == "zonghe_shouyi_jihua_wanchenglv") {
                                document.getElementById("n24").style.color = colors2[i];
                                document.getElementById("x24").style.color = colors2[i];
                                document.getElementById("m241").style.color = colors2[i];
                                document.getElementById("j241").style.color = colors2[i];
                                document.getElementById("n24").style.fontWeight = '800';
                                document.getElementById("x24").style.fontWeight = '800';
                                document.getElementById("m241").style.fontWeight = '800';
                                document.getElementById("j241").style.fontWeight = '800';
                                document.getElementById("n24").style.background = '#FF3546';
                                document.getElementById("x24").style.background = '#FF3546';
                                document.getElementById("m241").style.background = '#FF3546';
                                document.getElementById("j241").style.background = '#FF3546';
                            } else if (key == "qiye_neihan_jiazhi") {
                                document.getElementById("n24").style.color = colors2[i];
                                document.getElementById("x24").style.color = colors2[i];
                                document.getElementById("m242").style.color = colors2[i];
                                document.getElementById("j242").style.color = colors2[i];
                                document.getElementById("n24").style.fontWeight = '800';
                                document.getElementById("x24").style.fontWeight = '800';
                                document.getElementById("m242").style.fontWeight = '800';
                                document.getElementById("j242").style.fontWeight = '800';
                                document.getElementById("n24").style.background = '#FF3546';
                                document.getElementById("x24").style.background = '#FF3546';
                                document.getElementById("m242").style.background = '#FF3546';
                                document.getElementById("j242").style.background = '#FF3546';

                            } else if (key == "hengtai_zhengquan_yingye_shouru") {
                                document.getElementById("n25").style.color = colors2[i];
                                document.getElementById("x25").style.color = colors2[i];
                                document.getElementById("m251").style.color = colors2[i];
                                document.getElementById("j251").style.color = colors2[i];
                                document.getElementById("n25").style.fontWeight = '800';
                                document.getElementById("x25").style.fontWeight = '800';
                                document.getElementById("m251").style.fontWeight = '800';
                                document.getElementById("j251").style.fontWeight = '800';
                                document.getElementById("n25").style.background = '#FF3546';
                                document.getElementById("x25").style.background = '#FF3546';
                                document.getElementById("m251").style.background = '#FF3546';
                                document.getElementById("j251").style.background = '#FF3546';
                            } else if (key == "hengtai_zhengquan_zhichan_shouyi") {
                                document.getElementById("n25").style.color = colors2[i];
                                document.getElementById("x25").style.color = colors2[i];
                                document.getElementById("m252").style.color = colors2[i];
                                document.getElementById("j252").style.color = colors2[i];
                                document.getElementById("n25").style.fontWeight = '800';
                                document.getElementById("x25").style.fontWeight = '800';
                                document.getElementById("m252").style.fontWeight = '800';
                                document.getElementById("j252").style.fontWeight = '800';
                                document.getElementById("n25").style.background = '#FF3546';
                                document.getElementById("x25").style.background = '#FF3546';
                                document.getElementById("m252").style.background = '#FF3546';
                                document.getElementById("j252").style.background = '#FF3546';
                            } else if (key == "quanguo_yingyuan_paiming") {
                                document.getElementById("n26").style.color = colors2[i];
                                document.getElementById("x26").style.color = colors2[i];
                                document.getElementById("m26").style.color = colors2[i];
                                document.getElementById("j26").style.color = colors2[i];
                                document.getElementById("n26").style.fontWeight = '800';
                                document.getElementById("x26").style.fontWeight = '800';
                                document.getElementById("m26").style.fontWeight = '800';
                                document.getElementById("j26").style.fontWeight = '800';
                                document.getElementById("n26").style.background = '#FF3546';
                                document.getElementById("x26").style.background = '#FF3546';
                                document.getElementById("m26").style.background = '#FF3546';
                                document.getElementById("j26").style.background = '#FF3546';
                            } else if (key == "waibu_xingzheng_chufa") {
                                document.getElementById("n27").style.color = colors2[i];
                                document.getElementById("x27").style.color = colors2[i];
                                document.getElementById("m27").style.color = colors2[i];
                                document.getElementById("j27").style.color = colors2[i];
                                document.getElementById("n27").style.fontWeight = '800';
                                document.getElementById("x27").style.fontWeight = '800';
                                document.getElementById("m27").style.fontWeight = '800';
                                document.getElementById("j27").style.fontWeight = '800';
                                document.getElementById("n27").style.background = '#FF3546';
                                document.getElementById("x27").style.background = '#FF3546';
                                document.getElementById("m27").style.background = '#FF3546';
                                document.getElementById("j27").style.background = '#FF3546';
                            } else if (key == "jituan_neibu_chufa") {
                                document.getElementById("n28").style.color = colors2[i];
                                document.getElementById("x28").style.color = colors2[i];
                                document.getElementById("m28").style.color = colors2[i];
                                document.getElementById("j28").style.color = colors2[i];
                                document.getElementById("n28").style.fontWeight = '800';
                                document.getElementById("x28").style.fontWeight = '800';
                                document.getElementById("m28").style.fontWeight = '800';
                                document.getElementById("j28").style.fontWeight = '800';
                                document.getElementById("n28").style.background = '#FF3546';
                                document.getElementById("x28").style.background = '#FF3546';
                                document.getElementById("m28").style.background = '#FF3546';
                                document.getElementById("j28").style.background = '#FF3546';
                            }
                        }

                        i++;
                        setTimeout("shan()", 100);
                    } else {
                        i = 0;
                        setTimeout("shan()", 100);
                    }
                }
                setTimeout("shan()", 100);
            </script>
            <style type="text/css">
                table.laochen td {
                    background: #FFFFFF;
                    font-family: "Microsoft YaHei", 微软雅黑, "Microsoft JhengHei", 华文细黑, STHeiti, MingLiu;
                    font-size: 14px;
                }
            </style>
        </head>

        <body style="overflow-y:hidden">
            <div style="width: 515px;">
                <table class="imagetable" width="497" align="left">
                    <tr height="40" bgcolor='#Fffffff'>
                        <th width="100%" colspan="7" style="background-image: url('${ctx}/images/jrj-portal-nav.gif');"><span style="font-family:Microsoft YaHei,微软雅黑;font-size: 15px;font-weight: 800;">风&nbsp;险&nbsp;指&nbsp;标</span>
                        </th>
                    </tr>
                    <tr style="background-image: url('${ctx}/images/jrj-portal-nav.gif');">
                        <th width="12%" style="border-bottom: none;background-image: url('${ctx}/images/jrj-portal-nav.gif');"><span style="font-family:Microsoft YaHei,微软雅黑;font-size:12px;">一级风险</span>
                        </th>
                        <th width="12%" style="border-bottom: none;background-image: url('${ctx}/images/jrj-portal-nav.gif');"><span style="font-family:Microsoft YaHei,微软雅黑;font-size:12px;">二级风险</span>
                        </th>
                        <th width="8%" style="border-bottom: none;background-image: url('${ctx}/images/jrj-portal-nav.gif');"><span style="font-family:Microsoft YaHei,微软雅黑;font-size:12px;">序号</span>
                        </th>
                        <th width="25%" style="border-bottom: none;background-image: url('${ctx}/images/jrj-portal-nav.gif');"><span style="font-family:Microsoft YaHei,微软雅黑;font-size:12px;">指标名称</span>
                        </th>
                        <th width="25%" style="border-bottom: none;background-image: url('${ctx}/images/jrj-portal-nav.gif');"><span style="font-family:Microsoft YaHei,微软雅黑;font-size:12px;">指标描述</span>
                        </th>
                        <th width="8%" style="border-bottom: none;background-image: url('${ctx}/images/jrj-portal-nav.gif');"><span style="font-family:Microsoft YaHei,微软雅黑;font-size:12px;">等级</span>
                        </th>
                        <th width="10%" style="border-bottom: none;background-image:url('${ctx}/images/jrj-portal-nav.gif');"><span style="font-family:Microsoft YaHei,微软雅黑;font-size:12px;">监控</br>周期</span>
                        </th>
                    </tr>
                </table>
            </div>
            <div style="container:positioned; position=relative;overflow: auto;height: 335px; width: 515px;">
                <table id="mytable" class="imagetable laochen" width="497" align="left">
                    <!-- 财务风险 -->
                    <tr>
                        <td rowspan="4">财
                            <br>务
                            <br>风
                            <br>险</td>
                        <td rowspan="2">现
                            <br>金
                            <br>流
                            <br>安
                            <br>全
                            <br>风
                            <br>险</td>
                        <td id="x1" rowspan="2" style="text-align: center">1</td>
                        <td id="n1" rowspan="2" style="text-align: left">现金保障能力</td>
                        <td id="m31" class="showColor k1" style="text-align: left"><span style="font-size: 14px;">现金/3个月内刚性支出</span>
                        </td>
                        <td class="showColor k1">
                            <a href="${ctx}/jrj/riskanalysis.do?id=3_xianjin_baozhang_nengli" target="rightiframe">
                                <image id='3_xianjin_baozhang_nengli' onclick="showColor('k1');" src='' border="0px" />
                            </a>
                        </td>
                        <td id="j31" class="showColor k1">季度</td>
                    </tr>
                    <tr>
                        <td id="m22" class="showColor k2" style="text-align: left"><span style="font-size: 14px;">现金/6个月内刚性支出</span>
                        </td>
                        <td class="showColor k2">
                            <a href="${ctx}/jrj/riskanalysis.do?id=6_xianjin_baozhang_nengli" target="rightiframe">
                                <image id='6_xianjin_baozhang_nengli' onclick="showColor('k2');" src='' border="0px" />
                            </a>
                        </td>
                        <td id="j22" class="showColor k2">季度</td>
                    </tr>
                    <tr>
                        <td rowspan="2">财
                            <br>务
                            <br>结
                            <br>构
                            <br>风
                            <br>险</td>
                        <td id="x2" class="showColor k3" width="5%" style="text-align: center">2</td>
                        <td id="n2" class="showColor k3" style="text-align: left"><span style="font-size: 14px;">资产负债率<span</td>
                            <td id="m2" class="showColor k3" style="text-align: left"><span style="font-size: 14px;">总负债/资产总额</span>
                        </td>
                        <td class="showColor k3">
                            <a href="${ctx}/jrj/riskanalysis.do?id=zhichan_fuzhailv" target="rightiframe">
                                <image id='zhichan_fuzhailv' onclick="showColor('k3');" src='' border="0px" />
                            </a>
                        </td>
                        <td id="j2" class="showColor k3" style="text-align: center">季度</td>
                    </tr>
                    <tr>
                        <td id="x3" class="showColor k4" width="5%" style="text-align: center">3</td>
                        <td id="n3" class="showColor k4" style="text-align: left">有息负债率</td>
                        <td id="m3" class="showColor k4" style="text-align: left"><span style="font-size: 14px;">有息负债/资产总额</span>
                        </td>
                        <td class="showColor k4">
                            <a href="${ctx}/jrj/riskanalysis.do?id=youxi_fuzhailv" target="rightiframe">
                                <image id='youxi_fuzhailv' onclick="showColor('k4');" src='' border="0px" />
                            </a>
                        </td>
                        <td id="j3" class="showColor k4">季度</td>
                    </tr>
                    <!-- 运营风险 -->
                    <!--  //金融街需求变更,停用了三个指标 经济增加值EVA,经济增加值EVA-政府,经营活动产生的现金流量净额 -->
                    <!-- tr>
                            <td rowspan="18">运
                                <br>营
                                <br>风
                                <br>险</td>
                            <td rowspan="11">运
                                <br>营
                                <br>计
                                <br>划
                                <br>风
                                <br>险</td>
                            <td id="x4" class="showColor k5" width="5%" style="text-align: center">4</td>
                            <td id="n4" class="showColor k5" style="text-align: left">经济增加值(EVA)  <br>（经济增加值=税后营业净利润-资本总成本）</td>
                            <td id="m4" class="showColor k5" style="text-align: left"><span style="font-size: 14px;">（截至到本季度累计数-上年同期累计数）/上年同期累计数（取绝对值）</span>
                            </td>
                            <td class="showColor k5">
                                <a href="${ctx}/jrj/riskanalysis.do?id=jingji_zhengjiazhi_eva" target="rightiframe">
                                    <image id='jingji_zhengjiazhi_eva' onclick="showColor('k5');" src='' border="0px" />
                                </a>
                            </td>
                            
                            <td  id="j4" class="showColor k5">季度</td>
                        </tr-->
                    <tr>
                        <td rowspan="15">运
                            <br>营
                            <br>风
                            <br>险</td>
                        <td rowspan="8">运
                            <br>营
                            <br>计
                            <br>划
                            <br>风
                            <br>险</td>
                        <td id="x5" class="showColor k6" width="5%" style="text-align: center">4</td>
                        <td id="n5" class="showColor k6" style="text-align: left">经济增加值(EVA)-市场</td>
                        <td id="m5" class="showColor k6" style="text-align: left"><span style="font-size: 14px;">（截至到本季度累计数-上年同期累计数)/上年同期累计数（取绝对值）</span>
                        </td>
                        <td class="showColor k6">
                            <a href="${ctx}/jrj/riskanalysis.do?id=jingji_zhengjiazhi_eva_shichang" target="rightiframe">
                                <image id='jingji_zhengjiazhi_eva_shichang' onclick="showColor('k6');" src='' border="0px" />
                            </a>
                        </td>
                        <td id="j5" class="showColor k6">季度</td>
                    </tr>
                    <!--  //金融街需求变更,停用了三个指标 经济增加值EVA,经济增加值EVA-政府,经营活动产生的现金流量净额 -->
                    <!-- tr>
                            <td id="x6" class="showColor k7" width="5%" style="text-align: center">6</td>
                            <td id=""n6 class="showColor k7" style="text-align: left">经济增加值(EVA)-政府</td>
                            <td id="m6" class="showColor k7" style="text-align: left"><span style="font-size: 14px;">（截至到本季度累计数-上年同期累计数)/上年同期累计数（取绝对值）</span>
                            </td>
                            <td class="showColor k7">
                                <a href="${ctx}/jrj/riskanalysis.do?id=jingji_zhengjiazhi_eva_zhengfu" target="rightiframe">
                                    <image id='jingji_zhengjiazhi_eva_zhengfu'onclick="showColor('k7');" src='' border="0px" />
                                </a>
                            </td>
                            <td id="j6" class="showColor k7">季度</td>
                        </tr-->
                    <!-- tr>
                            <td id="x7" class="showColor k8" width="5%" style="text-align: center">7</td>
                            <td id="n7" class="showColor k8" style="text-align: left">经营活动产生的现金流量净额</td>
                            <td id="m7" class="showColor k8" style="text-align: left"><span style="font-size: 14px;">经营活动现金流入小计 -经营活动现金流出小计</span>
                            </td>
                            <td class="showColor k8">
                                <a href="${ctx}/jrj/riskanalysis.do?id=jingying_huodong_xianjin_liuliang_jinger" target="rightiframe">
                                    <image id='jingying_huodong_xianjin_liuliang_jinger' onclick="showColor('k8');" src='' border="0px" />
                                </a>
                            </td>
                            <td id="j7" class="showColor k8">月度</td>
                        </tr-->
                    <tr>
                        <td id="x8" class="showColor k9" width="5%" style="text-align: center">5</td>
                        <td id="n8" class="showColor k9" style="text-align: left">营业收入增长率</td>
                        <td id="m8" class="showColor k9" style="text-align: left"><span style="font-size: 14px;">(本期收入-上期收入)/上期收入</span>
                        </td>
                        <td class="showColor k9">
                            <a href="${ctx}/jrj/riskanalysis.do?id=yingye_shouru_zhengzhanglv" target="rightiframe">
                                <image id='yingye_shouru_zhengzhanglv' onclick="showColor('k9');" src='' border="0px" />
                            </a>
                        </td>
                        <td id="j8" class="showColor k9">月度</td>
                    </tr>
                    <tr>
                        <td id="x9" class="showColor k10" width="5%" style="text-align: center">6</td>
                        <td id="n9" class="showColor k10" style="text-align: left">营业收入计划完成率</td>
                        <td id="m9" class="showColor k10" style="text-align: left"><span style="font-size: 14px;">已完成营业收入/计划营业收入</span>
                        </td>
                        <td class="showColor k10">
                            <a href="${ctx}/jrj/riskanalysis.do?id=yingye_shouru_jihua_wangchenglv" target="rightiframe">
                                <image id='yingye_shouru_jihua_wangchenglv' onclick="showColor('k10');" src='' border="0px" />
                            </a>
                        </td>
                        <td id="j9" class="showColor k10">月度</td>
                    </tr>
                    <tr>
                        <td id="x10" class="showColor k11" width="5%" style="text-align: center">7</td>
                        <td id="n10" class="showColor k11" style="text-align: left">净利润(归属于母公司)增长率</td>
                        <td id="m10" class="showColor k11" style="text-align: left"><span style="font-size: 14px;">(本期净利润-上期净利润)/上期净利润</span>
                        </td>
                        <td class="showColor k11">
                            <a href="${ctx}/jrj/riskanalysis.do?id=jinglirun_zhengzhanglv_mugongshi" target="rightiframe">
                                <image id='jinglirun_zhengzhanglv_mugongshi' onclick="showColor('k11');" src='' border="0px" />
                            </a>
                        </td>
                        <td id="j10" class="showColor k11">月度</td>
                    </tr>
                    <tr>
                        <td id="x11" class="showColor k12" width="5%" style="text-align: center">8</td>
                        <td id="n11" class="showColor k12" style="text-align: left">净利润(归属于母公司)计划完成率</td>
                        <td id="m11" class="showColor k12" style="text-align: left"><span style="font-size: 14px;">已完成净利润/计划净利润</span>
                        </td>
                        <td class="showColor k12">
                            <a href="${ctx}/jrj/riskanalysis.do?id=jinglirun_jihua_wanchenglv_mugongshi" target="rightiframe">
                                <image id='jinglirun_jihua_wanchenglv_mugongshi' onclick="showColor('k12');" src='' border="0px" />
                            </a>
                        </td>
                        <td id="j11" class="showColor k12">月度</td>
                    </tr>
                    <tr>
                        <td id="x12" class="showColor k13" width="5%" style="text-align: center">9</td>
                        <td id="n12" class="showColor k13" style="text-align: left">总资产周转率</td>
                        <td id="m12" class="showColor k13" style="text-align: left"><span style="font-size: 14px;">营业收入净额/平均资产总额</span>
                        </td>
                        <td class="showColor k13">
                            <a href="${ctx}/jrj/riskanalysis.do?id=zongzhichan_zhouzhuanlv" target="rightiframe">
                                <image id='zongzhichan_zhouzhuanlv' onclick="showColor('k13');" src='' border="0px" />
                            </a>
                        </td>
                        <td id="j12" class="showColor k13">年度</td>
                    </tr>
                    <tr>
                        <td id="x13" class="showColor k14" width="5%" style="text-align: center">10</td>
                        <td id="n13" class="showColor k14" style="text-align: left">成本费用利润率</td>
                        <td id="m13" class="showColor k14" style="text-align: left"><span style="font-size: 14px;">利润总额/成本费用总额</span>
                        </td>
                        <td class="showColor k14">
                            <a href="${ctx}/jrj/riskanalysis.do?id=chengben_feiyong_lirenlv" target="rightiframe">
                                <image id='chengben_feiyong_lirenlv' onclick="showColor('k14');" src='' border="0px" />
                            </a>
                        </td>
                        <td id="j13" class="showColor k14">月度</td>
                    </tr>
                    <tr>
                        <td id="x14" class="showColor k15" width="5%" style="text-align: center">11</td>
                        <td id="n14" class="showColor k15" style="text-align: left">毛利率</td>
                        <td id="m14" class="showColor k15" style="text-align: left"><span style="font-size: 14px;">(营业收入－营业成本)/营业收入</span>
                        </td>
                        <td class="showColor k15">
                            <a href="${ctx}/jrj/riskanalysis.do?id=maolilv" target="rightiframe">
                                <image id='maolilv' onclick="showColor('k15');" src='' border="0px" />
                            </a>
                        </td>
                        <td id="j14" class="showColor k15">月度</td>
                    </tr>
                    <tr>
                        <td rowspan="2">人
                            <br>力
                            <br>资
                            <br>源
                            <br>风
                            <br>险</td>
                        <td id="x15" class="showColor k16" width="5%" style="text-align: center">12</td>
                        <td id="n15" class="showColor k16" style="text-align: left">人力成本利润率
                            <br>（利润总额/人力成本总额）</td>
                        <td id="m15" class="showColor k16" style="text-align: left"><span style="font-size: 14px;">（本年人力成本利润率-上年人力成本利润率）/上年人力成本利润率</span>
                        </td>
                        <td class="showColor k16">
                            <a href="${ctx}/jrj/riskanalysis.do?id=renli_chengben_lirenlv" target="rightiframe">
                                <image id='renli_chengben_lirenlv' onclick="showColor('k16');" src='' border="0px" />
                            </a>
                        </td>
                        <td id="j15" class="showColor k16">年度</td>
                    </tr>
                    <tr>
                        <td id="x16" class="showColor k17" width="5%" style="text-align: center">13</td>
                        <td id="n16" class="showColor k17" style="text-align: left">关键岗位空岗率</td>
                        <td id="m16" class="showColor k17" style="text-align: left"><span style="font-size: 14px;">关键岗位员工缺失数/关键岗位数</span>
                        </td>
                        <td class="showColor k17">
                            <a href="${ctx}/jrj/riskanalysis.do?id=guanjian_wangwei_kongquelv" target="rightiframe">
                                <image id='guanjian_wangwei_kongquelv' onclick="showColor('k17');" src='' border="0px" />
                            </a>
                        </td>
                        <td id="j16" class="showColor k17">季度</td>
                    </tr>
                    <tr>
                        <td rowspan="4">品
                            <br>牌
                            <br>风
                            <br>险</td>
                        <td id="x17" class="showColor k18" width="5%" style="text-align: center">14</td>
                        <td id="n17" class="showColor k18" style="text-align: left">客户满意度-房地产</td>
                        <td id="m17" class="showColor k18" style="text-align: left"><span style="font-size: 14px;">本年客户满意度实际得分与计划比较</span>
                        </td>
                        <td class="showColor k18">
                            <!-- a href="${ctx}/jrj/riskanalysis.do?id=fangdichan" target="rightiframe"-->
                            <a href="${ctx}/jrj/fangdichan.do?id=fangdichan&frequecy=0frequecy_year&type=Line" target="rightiframe">
                                <image id='fangdichan' onclick="showColor('k18');" src='${ctx}/images/icons/kongquan.jpg' border="0px" />
                            </a>
                        </td>
                        <td id="j17" class="showColor k18">年度</td>
                    </tr>
                    <tr>
                        <td id="x18" class="showColor k19" width="5%" style="text-align: center">15</td>
                        <td id="n18" class="showColor k19" style="text-align: left">客户满意度-保险</td>
                        <td id="m18" class="showColor k19" style="text-align: left"><span style="font-size: 14px;">本年客户满意度实际得分与计划比较</span>
                        </td>
                        <td class="showColor k19">
                            <!-- a href="${ctx}/jrj/riskanalysis.do?id=baoxian" target="rightiframe"-->
                            <a href="${ctx}/jrj/fangdichan.do?id=baoxian&frequecy=0frequecy_year&type=Line" target="rightiframe">
                                <image id='baoxian' onclick="showColor('k19');" src='${ctx}/images/icons/kongquan.jpg' border="0px" />
                            </a>
                        </td>
                        <td id="j18" class="showColor k19">年度</td>
                    </tr>
                    <tr>
                        <td id="x19" class="showColor k20" width="5%" style="text-align: center">16</td>
                        <td id="n19" class="showColor k20" style="text-align: left">客户满意度-物业</td>
                        <td id="m19" class="showColor k20" style="text-align: left"><span style="font-size: 14px;">本年客户满意度实际得分与计划比较</span>
                        </td>
                        <td class="showColor k20">
                            <!-- a href="${ctx}/jrj/riskanalysis.do?id=wuye" target="rightiframe"-->
                            <a href="${ctx}/jrj/fangdichan.do?id=wuye&frequecy=0frequecy_year&type=Line" target="rightiframe">
                                <image id='wuye' onclick="showColor('k20');" src='${ctx}/images/icons/kongquan.jpg' border="0px" />
                            </a>
                        </td>
                        <td id="j19" class="showColor k20">年度</td>
                    </tr>
                    <tr>
                        <td id="x20" class="showColor k21" width="5%" style="text-align: center">17</td>
                        <td id="n20" class="showColor k21" style="text-align: left">客户满意度-文化</td>
                        <td id="m20" class="showColor k21" style="text-align: left"><span style="font-size: 14px;">本年客户满意度实际得分与计划比较</span>
                        </td>
                        <td class="showColor k21">
                            <!-- a href="${ctx}/jrj/riskanalysis.do?id=wenhua" target="rightiframe"-->
                            <a href="${ctx}/jrj/fangdichan.do?id=wenhua&frequecy=0frequecy_year&type=Line" target="rightiframe">
                                <image id='wenhua' onclick="showColor('k21');" src='${ctx}/images/icons/kongquan.jpg' border="0px" />
                            </a>
                        </td>
                        <td id="j20" class="showColor k21">年度</td>
                    </tr>
                    <tr>
                        <td rowspan="1">安
                            <br>全
                            <br>风
                            <br>险</td>
                        <td id="x21" class="showColor k22" width="5%" style="text-align: center">18</td>
                        <td id="n21" class="showColor k22" style="text-align: left">安全指数</td>
                        <td id="m21" class="showColor k22" style="text-align: left"><span style="font-size: 14px;">按照《金融街集团突发事件应急响应及分级管理办法（试行版）》,发生即预警</span>
                        </td>
                        <td class="showColor k22">
                            <a href="${ctx}/jrj/riskanalysis.do?id=anquan_zhishu" target="rightiframe">
                                <image id='anquan_zhishu' onclick="showColor('k22');" src='' border="0px" />
                            </a>
                        </td>
                        <td id="j21" class="showColor k22">实时</br>更新</td>
                    </tr>
                    <!-- 市场风险 -->
                    <tr>
                        <td width="12%" rowspan="7">市
                            <br>场
                            <br>风
                            <br>险</td>
                        <td width="12%" rowspan="7">市
                            <br>场
                            <br>竞
                            <br>争
                            <br>风
                            <br>险</td>
                        <td id="x22" class="showColor k23" width="8%" style="text-align: center">19</td>
                        <td id="n22" class="showColor k23" width="25%" style="text-align: left">金融街控股公司</td>
                        <td id="m22" class="showColor k23" width="25%" style="text-align: left"><span style="font-size: 14px;">本年度房地产百强综合实力排名-上年度综合实力排名</span>
                        </td>
                        <td class="showColor k23" width="8%">
                            <a href="${ctx}/jrj/riskanalysis.do?id=china_fangdichan_qiye_paiming" target="rightiframe">
                                <image id='china_fangdichan_qiye_paiming' onclick="showColor('k23');" src='' border="0px" />
                            </a>
                        </td>
                        <td id="j22" class="showColor k23" width="10%">年度</td>
                    </tr>
                    <tr>
                        <td id="x23" class="showColor k24" width="5%" style="text-align: center">20</td>
                        <td id="n23" class="showColor k24" style="text-align: left">自持酒店</td>
                        <td id="m23" class="showColor k24" style="text-align: left">
                            <span style="font-size: 14px;">本月竞争组别每房收益排名-上月竞争组别每房收益排名</span>
                        </td>
                        <td class="showColor k24">
                            <a href="${ctx}/jrj/zcjd.do?id=zichijiudian" target="rightiframe">
                                <image id='zichijiudian' onclick="showColor('k24');" src='' border="0px" />
                            </a>
                        </td>
                        <td id="j23" class="showColor k24">月度</td>
                    </tr>
                    <tr>
                        <td id="x24" rowspan="2" width="5%" style="text-align: center">21</td>
                        <td id="n24" rowspan="2" style="text-align: left">长城保险公司</td>
                        <td id="m241" class="showColor k25" style="text-align: left"><span style="font-size: 14px;">(本年度累计完成净利润+同期可供出售公允价值变动)/当年度综合收益目标</span>
                        </td>
                        <td class="showColor k25">
                            <a href="${ctx}/jrj/riskanalysis.do?id=zonghe_shouyi_jihua_wanchenglv" target="rightiframe">
                                <image id='zonghe_shouyi_jihua_wanchenglv' onclick="showColor('k25');" src='' border="0px" />
                            </a>
                            <!-- a href="${ctx}/jrj/riskanalysis.do?id=biaozhun_baofei" target="rightiframe">
                                    <image id='biaozhun_baofei' onclick="showColor('k25');" src='' border="0px" />
                                </a-->
                        </td>
                        <td id="j241" class="showColor k25">季度</td>
                    </tr>
                    <tr>
                        <td id="m242" class="showColor k26" style="text-align: left"><font>
                            <!-- 新需求,更改名称 -->
                            <!-- span style="font-size: 14px;">（本年度内含价值-上年度内含价值）/上年度内含价值</span-->
                            <span style="font-size: 14px;">新业务价值（本年度新业务价值-上年新业务价值）÷上年新业务价值</span>
                            
                            </font>

                        </td>
                        <td class="showColor k26">
                            <a href="${ctx}/jrj/riskanalysis.do?id=qiye_neihan_jiazhi" target="rightiframe">
                                <image id='qiye_neihan_jiazhi' onclick="showColor('k26');" src='' border="0px" />
                            </a>
                        </td>
                        <td id="j242" class="showColor k26">年度</td>
                    </tr>
                    <tr>
                        <td id="x25" rowspan="2" width="5%" style="text-align: center">22</td>
                        <td id="n25" rowspan="2" style="text-align: left">恒泰证券公司</td>
                        <td id="m251" class="showColor k27" style="text-align: left"><span style="font-size: 14px;">（恒泰证券营业收入-竞争组别平均值）/竞争组别平均值</span>
                        </td>
                        <td class="showColor k27">
                            <!-- a href="${ctx}/jrj/riskanalysis.do?id=hengtai_zhengquan_yingye_shouru" target="rightiframe"-->
                            <a href="${ctx}/jrj/onediffregion.do?id=hengtai_zhengquan_yingye_shouru&frequecy=0frequecy_halfyear&type=Line" target="rightiframe">
                                <image id='hengtai_zhengquan_yingye_shouru' onclick="showColor('k27');" src='' border="0px" />
                            </a>
                        </td>
                        <!-- td id="j251" class="showColor k27">半年度</td-->
                        <td id="j251" class="showColor k27">年度</td><!-- 新需求 -->
                    </tr>
                    <tr>
                        <td id="m252" class="showColor k28" style="text-align: left"><span style="font-size: 14px;">（恒泰证券净资产收益率-竞争组别平均值）/竞争组别平均值</span>
                        </td>
                        <td class="showColor k28">
                            <!-- a href="${ctx}/jrj/riskanalysis.do?id=hengtai_zhengquan_zhichan_shouyi" target="rightiframe"-->
                            <a href="${ctx}/jrj/onediffregion.do?id=hengtai_zhengquan_zhichan_shouyi&frequecy=0frequecy_halfyear&type=Line" target="rightiframe">
                                <image id='hengtai_zhengquan_zhichan_shouyi' onclick="showColor('k28');" src='' border="0px" />
                            </a>
                        </td>
                        <!-- td id="j252" class="showColor k28">半年度</td-->
                        <td id="j252" class="showColor k28">年度</td><!-- 新需求 -->
                    </tr>
                    <tr>
                        <td id="x26" class="showColor k29" width="5%" style="text-align: center">23</td>
                        <td id="n26" class="showColor k29" style="text-align: left">首都电影院西单影院</td>
                        <td id="m26" class="showColor k29" style="text-align: left"><span style="font-size: 14px;">全国影院票房排名</span>
                        </td>
                        <td class="showColor k29">
                            <!-- a href="${ctx}/jrj/riskanalysis.do?id=quanguo_yingyuan_paiming" target="rightiframe"-->
                            <a href="${ctx}/jrj/onediffregion.do?id=quanguo_yingyuan_paiming&frequecy=0frequecy_month&type=InverseMSLine" target="rightiframe">
                                <image id='quanguo_yingyuan_paiming' onclick="showColor('k29');" src='' border="0px" />
                            </a>
                        </td>
                        <td id="j26" class="showColor k29">月度</td>
                    </tr>
                    <!-- 法律风险 -->
                    <tr>
                        <td rowspan="2">法
                            <br>律
                            <br>风
                            <br>险</td>
                        <td rowspan="2">合
                            <br>规
                            <br>风
                            <br>险</td>
                        <td id="x27" class="showColor k30" width="5%" style="text-align: center">24</td>
                        <td id="n27" class="showColor k30" style="text-align: left"><span style="font-size: 14px;">外部行政监管处罚</td>
                            <td id="m27" class="showColor k30" style="text-align: left"><span style="font-size: 14px;">政府部门、监管部门行政处罚的数量和金额</span>
                        </td>
                        <td class="showColor k30">
                            <a href="${ctx}/jrj/riskanalysis.do?id=waibu_xingzheng_chufa" target="rightiframe">
                                <image id='waibu_xingzheng_chufa' onclick="showColor('k30');" src='' border="0px" />
                            </a>
                        </td>
                        <td id="j27" class="showColor k30">实时</br>更新</td>
                    </tr>
                    <tr>
                        <td id="x28" class="showColor k31" width="5%" style="text-align: center">25</span>
                        </td>
                        <td id="n28" class="showColor k31" style="text-align: left">集团内部处罚同比变动情况</td>
                        <td id="m28" class="showColor k31" style="text-align: left"><span style="font-size: 14px;">（本年处罚件数或金额-上年处罚件数或金额）/上年处罚件数或金额</span>
                        </td>
                        <td class="showColor k31">
                            <a href="${ctx}/jrj/riskanalysis.do?id=jituan_neibu_chufa" target="rightiframe">
                                <image id='jituan_neibu_chufa' onclick="showColor('k31');" src='' border="0px" />
                            </a>
                        </td>
                        <td id="j28" class="showColor k31">年度</td>
                    </tr>
                </table>
            </div>
        </body>

        </html>
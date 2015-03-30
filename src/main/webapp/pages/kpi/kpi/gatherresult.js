 var kpigatherresult_view = (function () {
                		var mergeCells = function(grid,cols){  
                	    var arrayTr=document.getElementById(grid.getId()+"-body").firstChild.firstChild.firstChild.getElementsByTagName('tr');    
                	    var trCount = arrayTr.length;  
                	    var arrayTd;  
                	    var td;  
                	    var merge = function(rowspanObj,removeObjs){ //定义合并函数  
                	        if(rowspanObj.rowspan != 1){  
                	            arrayTd =arrayTr[rowspanObj.tr].getElementsByTagName("td"); //合并行  
                	            td=arrayTd[rowspanObj.td-1];  
                	            td.rowSpan=rowspanObj.rowspan;  
                	            td.vAlign="middle";               
                	            Ext.each(removeObjs,function(obj){ //隐身被合并的单元格  
                	                arrayTd =arrayTr[obj.tr].getElementsByTagName("td");  
                	                arrayTd[obj.td-1].style.display='none';                           
                	            });  
                	        }     
                	    };    
                	    var rowspanObj = {}; //要进行跨列操作的td对象{tr:1,td:2,rowspan:5}      
                	    var removeObjs = []; //要进行删除的td对象[{tr:2,td:2},{tr:3,td:2}]  
                	    var col;  
                	    Ext.each(cols,function(colIndex){ //逐列去操作tr  
                	        var rowspan = 1;  
                	        var divHtml = null;//单元格内的数值          
                	        for(var i=1;i<trCount;i++){  //i=0表示表头等没用的行  
                	            arrayTd = arrayTr[i].getElementsByTagName("td");  
                	            var cold=0;  
//                	          Ext.each(arrayTd,function(Td){ //获取RowNumber列和check列  
//                	              if(Td.getAttribute("class").indexOf("x-grid-cell-special") != -1)  
//                	                  cold++;                               
//                	          });  
                	            col=colIndex+cold;//跳过RowNumber列和check列  
                	            if(!divHtml){  
                	                divHtml = arrayTd[col-1].innerHTML;  
                	                rowspanObj = {tr:i,td:col,rowspan:rowspan}  
                	            }else{  
                	                var cellText = arrayTd[col-1].innerHTML;  
                	                var addf=function(){   
                	                    rowspanObj["rowspan"] = rowspanObj["rowspan"]+1;  
                	                    removeObjs.push({tr:i,td:col});  
                	                    if(i==trCount-1)  
                	                        merge(rowspanObj,removeObjs);//执行合并函数  
                	                };  
                	                var mergef=function(){  
                	                    merge(rowspanObj,removeObjs);//执行合并函数  
                	                    divHtml = cellText;  
                	                    rowspanObj = {tr:i,td:col,rowspan:rowspan}  
                	                    removeObjs = [];  
                	                };  
                	                if(cellText == divHtml){  
                	                    if(colIndex!=cols[0]){   
                	                        var leftDisplay=arrayTd[col-2].style.display;//判断左边单元格值是否已display  
                	                        if(leftDisplay=='none')  
                	                            addf();   
                	                        else  
                	                            mergef();                             
                	                    }else  
                	                        addf();                                           
                	                }else  
                	                    mergef();             
                	            }  
                	        }  
                	    });   
                	};  
                    var kpiname = "${param.kpiname}";
                    var gatherUrl = __ctxPath+"/kpi/kpi/findkpigatherresultbyname.f?name="+encodeURIComponent(kpiname);
                  	var gather_grid = Ext.create('FHD.ux.EditorGridPanel', {
                        border: false,
                        pagable: false,
                        checked: false,
                        url: gatherUrl,
                        cls:'rowspan-grid',
                        //view:Ext.create('FHD.ux.RowspanView',{}),
                        viewConfig:{
                  			doRender:function(obj,eOpts){
              					alert(obj);
              					alert(eOpts);
              				}
                        },
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
                            dataIndex: 'dateRange',
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
                  	gather_grid.on()
                  	gather_grid.getStore().on('load',function(){  
        				mergeCells(gather_grid,[1]);  
    				});  
                    Ext.define('kpigatherresult.view', {
                        gatherpanel: null,
                        init: function () {
                            this.gatherpanel = Ext.create('Ext.panel.Panel', {
                                renderTo: 'kpigatherresult',
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
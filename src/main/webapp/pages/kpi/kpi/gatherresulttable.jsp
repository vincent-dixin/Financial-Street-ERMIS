<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
    <%@ include file="/WEB-INF/jsp/commons/include-tagsOnly.jsp" %>
        <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
        <html>
            
            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <title>指标结果采集入录</title>
                <script type="text/javascript">
                var currentId = "${param.id}";
                var parentid = "${param.parentid}";
                var editflag = "${param.editflag}";
                var parentname = "${param.parentname}";
                var kpiname = "${param.kpiname}";
                var yearId = "${param.yearId}";
                var timeId = "${param.timeId}";
                var eType = "${param.eType}";
                var categoryname = "${param.categoryname}"
                var kpiid = "";
                var frequence = "";
                var paraobj = {};
                var kpiid = "";
                var frequenceTemp = "";
                
                FHD.data.currentId = currentId;
                FHD.data.parentid = parentid;
                FHD.data.editflag = editflag;
                FHD.data.parentname = parentname;
                
                kpiActivePanelflag = '';
                
                function charts(panel, id){
                	panel.layout.setActiveItem(id);
                	Ext.getCmp('tbarChartsButtonId${param._dc}').toggle(true);
                	Ext.getCmp('tbarListButtonId${param._dc}').toggle(false);
                	Ext.getCmp('tableEditId').hide();
                }
                
                function list(panel, id){
                	panel.layout.setActiveItem(id);
                	Ext.getCmp('tbarChartsButtonId${param._dc}').toggle(false);
                	Ext.getCmp('tbarListButtonId${param._dc}').toggle(true);
                	Ext.getCmp('tableEditId').show();
                }
                
                function componentResize(c,width,height){
        			var me = this;
        			FHD.getCenterPanel().on('resize',function(t,w,h){
        				c.setWidth(w - fhd_kpi_kpiaccordion_view.leftPanel.getWidth() - 7); 
        				c.setHeight(h - FHD.getCenterPanel().getTabBar().getHeight()-1-height - 33);
        			});
        		}
                
				function createQuarterTable(){
					//调用后台查询指标的采集频率
					debugger;
					var me = this;
					paraobj.eType = FHD.data.eType;
		            paraobj.kpiname = kpiname;
		            paraobj.timeId = timeId;
		            paraobj.year = FHD.data.yearId;
		            paraobj.isNewValue = FHD.data.isNewValue;
		            FHD.data.edit = false;
	                FHD.ajax({
	                    url: '${ctx}/kpi/kpi/createtable.f?edit=' + FHD.data.edit,
	                    params: {
	                    	condItem: Ext.JSON.encode(paraobj)
	                    },
	                    callback: function (data) {
	                        if (data && data.success) {
	                        	var funsionChartPanel = Ext.create('FHD.ux.FusionChartPanel',{
	                        		id : 'funsionChartPanelId${param._dc}',
	                        		border:false,
	                        		width : 50,
	                        		height : 50,
	                    			chartType:'MSColumnLine3D',
	                    			xmlData : data.xml
	                    		});
	                        	
	                        	var tablePanel = Ext.create('Ext.panel.Panel', {
	                        		id : 'tablePanelId${param._dc}',
                                    html : data.tableHtml,
                                    border:false,
                                    autoScroll: true,
                                    bodyStyle: 'border-bottom: 1px solid #bec0c0 !important;',
                                });
	                        	
	                        	var arr = '-';
	                        	me.basicPanel = Ext.create('FHD.ux.CardPanel', {
	                                xtype: 'cardpanel',
	                                border:false,
	                                activeItem : 0,
	                                items:[
	                                  funsionChartPanel,
	                                  tablePanel
	                                ],
	                                tbar: {
	                                    items: [
	                                    {
	                                    	id : 'tbarChartsButtonId${param._dc}',
	                                        text: '图表',
	                                        iconCls: 'icon-chart-bar',
	                                        handler: function () {
	                                        	me.charts(me.basicPanel, me.basicPanel.items.items[0].id);
	                                        }
	                                    },
	                                    arr, {
	                                    	id : 'tbarListButtonId${param._dc}',
	                                        text: '列表',
	                                        iconCls: 'icon-ibm-icon-allmetrics-list',
	                                        handler: function () {
	                                        	me.list(me.basicPanel, me.basicPanel.items.items[1].id);
	                                        }
	                                    },
	                                    '->', 
	                                    {
	                                    	id:'gatherresulttableinputsave',
                                            text: '保存',
                                            hidden : true,
                                            iconCls: 'icon-page-save',
                                            handler: function () {
                                            	me.save();
                                            }
                                        },
	                                    {
	                                    	id:'tableEditId',
                                            text: '修改',
                                            iconCls: 'icon-application-form-edit',
                                            hidden : true,
                                            handler: function () {
                                            	me.inputGatherResult();
                                            }
                                        },
                                        {
                                            text: '返回',
                                            iconCls: 'icon-arrow-undo',
                                            handler: function () {
                                            	me.goback();
                                            }
                                        }
	                                    ]
	                                }
	                            });
	                        	
	                        	Ext.getCmp('tbarChartsButtonId${param._dc}').toggle(true);
	                        	if(FHD.pram.edit == 1){
	                        		FHD.pram.edit = 0;
	                        		me.list(me.basicPanel, me.basicPanel.items.items[1].id);
	                        	}if(FHD.pram.save == 1){
	                        		FHD.pram.save = 0;
	                        		me.list(me.basicPanel, me.basicPanel.items.items[1].id);
	                        	}
	                        }
	                        
	                        FHD.panel.basicPanel = Ext.create('Ext.panel.Panel',{
	                        	renderTo : 'gatherresulttableDiv',
	        					height:FHD.getCenterPanelHeight() - 33,
	        					width: document.body.clientWidth - fhd_kpi_kpiaccordion_view.leftPanel.getWidth() - 12,
	                    	    border:true,
	                    	    autoScroll: true,
	                    		layout: {
	                    	        type: 'fit'
	                    	    },
	                    	    items:[me.basicPanel]
	                    	});
	                        me.componentResize(FHD.panel.basicPanel, 0, 0);//参数为：需要自适应的对象，需要减去的宽度，需要减去的高度
	                    }
	                });
				}
				
				//获取当前年份
                function getYear(){
                	var myDate = new Date();
                	var year = myDate.getFullYear();
                	return year;
                }
				
                //单点编辑
                function oneInput(timeId, yearId){
                	paraobj.timeId = timeId;
                	paraobj.yearId = yearId;
                	FHD.panel.basicPanel.body.mask("Loading...","x-mask-loading"); 
                	FHD.ajax({
	                    url: '${ctx}/kpi/kpi/createtable.f?edit=false',
	                    params: {
	                    	condItem: Ext.JSON.encode(paraobj)
	                    },
	                    callback: function (data) {
	                        if (data && data.success) {
	                        	FHD.panel.basicPanel.items.items[0].items.items[1].body.update(data.tableHtml);
	                        	FHD.panel.basicPanel.body.unmask();
	                        }
	                    }
	                });
                	FHD.pram.edit = 1;
                }
                
                //单点保存
                function oneSave(yearId, kpiId, timeId, realityValue, targetValue, assessValue){
                	var paraarr = [];
                	var value = {};
                	value[timeId] = realityValue + ',' + targetValue + ',' + assessValue;
                	paraarr.push(value);
                	FHD.panel.basicPanel.body.mask("Loading...","x-mask-loading"); 
                	FHD.ajax({
	                    url: '${ctx}/kpi/kpi/savekpigatherresultquarter.f?kpiid='+kpiId,
	                    params: {
	                    	params: Ext.JSON.encode(paraarr)
	                    },
	                    callback: function (data) {
	                        if (data && data.success) {
	        		        	paraobj.timeId = '';
	        		        	FHD.ajax({
	        	                    url: '${ctx}/kpi/kpi/createtable.f?edit=false',
	        	                    params: {
	        	                    	condItem: Ext.JSON.encode(paraobj)
	        	                    },
	        	                    callback: function (data) {
	        	                        if (data && data.success) {
	        	                        	FHD.panel.basicPanel.items.items[0].items.items[1].body.update(data.tableHtml);
	        	                        	FHD.panel.basicPanel.body.unmask();
	        	                        }
	        	                    }
	        	                });
	                        }
	                    }
	                });
                	FHD.pram.save = 1;
                }
                
              //返回
		        function goback(){
		        	if("sm"==type){
		        		smGoback();
		        	}else if("kpitype"==type){
		        		kpitypeGoback();
		        	}else if("myfolder"==type){
		        		myfolderGoback();
		        	}else{
		        		categoryGoBack();
		        	}
			    }
                
                var kpitypeId = "${param.kpitypeId}";
                var kpitypeName = "${param.kpitypeName}";
                var smparentid = "${param.smparentid}";
                var smid = "${param.smid}";
                var smparentname = "${param.smparentname}";
                var smname = "${param.smname}";
                var type = "${param.type}";
                
                
                function categoryGoBack(){
                	var rightUrl = "${ctx}/pages/kpi/kpi/opt/kpicategoryedit.jsp?"+"categoryparentid=" + parentid + "&categoryid=" + currentId + "&categoryparentname=" + encodeURIComponent(parentname) + "&categoryname="+encodeURIComponent(categoryname)+"&editflag=true";
		        	top.fhd_kpi_kpiaccordion_view.initRightPanel(rightUrl);
                }
                
                //我的文件夹返回事件
                function myfolderGoback(){
                	var rightUrl = __ctxPath + "/pages/kpi/myfolder/myfolderedit.jsp?";
                	fhd_kpi_kpiaccordion_view.initRightPanel(rightUrl);
                }
                //指标类型返回事件
                function kpitypeGoback(){
                	var rightUrl = __ctxPath + "/pages/kpi/kpi/opt/kpitypeedit.jsp?"+ "editflag=true" + "&id=" + kpitypeId + "&name=" + encodeURIComponent(kpitypeName);
                	fhd_kpi_kpiaccordion_view.initRightPanel(rightUrl);
                }
                //战略目标返回事件
                function smGoback(){
                	var rightUrl =  __ctxPath + "/pages/kpi/strategyMap/smedit.jsp?" + "parentid=" + smparentid + "&editflag=true" + "&id=" + smid + "&parentname=" + encodeURIComponent(smparentname) + "&smname=" + encodeURIComponent(smname);
                	fhd_kpi_kpiaccordion_view.initRightPanel(rightUrl);
                }

                //全部修改UI
				function inputGatherResult(){
					FHD.panel.basicPanel.body.mask("Loading...","x-mask-loading"); 
					FHD.ajax({
	                    url: '${ctx}/kpi/kpi/findkpiinfobyname.f?name='+encodeURIComponent(kpiname),
	                    callback: function (data) {
	                        if (data && data.success) {
	                         	kpiid = data.kpiid;
	                         	frequenceTemp = data.frequence;
	                        }
	                    }
	                });
                	
					FHD.ajax({
	                    url: '${ctx}/kpi/kpi/createtable.f?edit=true',
	                    params: {
	                    	condItem: Ext.JSON.encode(paraobj)
	                    },
	                    callback: function (data) {
	                        if (data && data.success) {
	                        	FHD.panel.basicPanel.items.items[0].items.items[1].body.update(data.tableHtml);
	                        	Ext.getCmp('gatherresulttableinputsave').show();
	        					Ext.getCmp('tableEditId').hide();
	        					FHD.panel.basicPanel.body.unmask();
	                        }
	                    }
	                });
				}
			   
			   //全部修改保存
			   function save(){
				   FHD.panel.basicPanel.body.mask("Loading...","x-mask-loading"); 
		        	if(frequenceTemp=="0frequecy_month"){
		        		saveMonth();
		        	}else if(frequenceTemp=="0frequecy_quarter"){
		        		saveQuarter();
		        	}else if(frequenceTemp=="0frequecy_year"){
		        		saveYear();
		        	}else if(frequenceTemp=="0frequecy_week"){
		        		saveWeek();
		        	}else if(frequenceTemp=="0frequecy_halfyear"){
		        		saveHalfYear();
		        	}
		        }
		        
		        function saveYear(){
					var paraarr = [];
	        		var realityYear = null;
	        		var targetYear = null;
	        		var assessYear = null;
	        		
	        		//年
       				realityYear = document.getElementById('realityYearId' + FHD.data.yearId + 'reality');
       				targetYear = document.getElementById('targetYearId' + FHD.data.yearId + 'target');
       				assessYear = document.getElementById('assessYearId' + FHD.data.yearId + 'assess');
        			if(realityYear != null && targetYear != null && assessYear != null){
        				var value = {};
        				value[FHD.data.yearId + ''] = realityYear.value + ',' + targetYear.value + ',' + assessYear.value;
        				paraarr.push(value);
        			}
        			
	        		FHD.ajax({
	                    url: '${ctx}/kpi/kpi/savekpigatherresultquarter.f?kpiid='+kpiid,
	                    params: {
	                    	params: Ext.JSON.encode(paraarr)
	                    },
	                    callback: function (data) {
	                        if (data && data.success) {
	                        	//var rightUrl = "${ctx}/pages/kpi/kpi/opt/kpicategoryedit.jsp?"+"categoryparentid=" + parentid + "&editflag=true" + "&categoryid=" + currentId + "&categoryparentname=" + encodeURIComponent(parentname);
	        		        	//top.fhd_kpi_kpiaccordion_view.initRightPanel(rightUrl);
	                        	refresh();
	                        }
	                    }
	                });
		        }
		        
		        function getNumber(str){
		        	if(parseInt(str) < 10){
		        		str = '0' + str;
		        	}
		        	
		        	return str;
		        }
		        
		        function saveWeek(){
					var paraarr = [];
					
	        		var week = 0;
	        		var month = 0;
	        		var realityWeek = null;
	        		var targetWeek = null;
	        		var assessWeek = null;
	        		
	        		var realityMonth = null;
	        		var targetMonth = null;
	        		var assessMonth = null;
	        		
	        		var realityYear = null;
	        		var targetYear = null;
	        		var assessYear = null;
	        		
	        		//周
        			for(var j = 1; j < 55 + 1; j++){
        				week = j;
        				realityWeek = document.getElementById(FHD.data.yearId + 'w' + week + 'reality');
        				targetWeek = document.getElementById(FHD.data.yearId + 'w' + week + 'target');
        				assessWeek = document.getElementById(FHD.data.yearId + 'w' + week + 'assess');
	        			if(realityWeek != null && targetWeek != null && assessWeek != null){
	        				var value = {};
	        				value[FHD.data.yearId + 'w' + week] = realityWeek.value + ',' + targetWeek.value + ',' + assessWeek.value;
	        				paraarr.push(value);
	        			}
	        		}
	        		
	        		//月
        			for(var j = 1; j < 12 + 1; j++){
        				month = j;
        				realityMonth = document.getElementById('realityMonthId' + month + 'reality');
        				targetMonth = document.getElementById('targetMonthId' + month + 'target');
        				assessMonth = document.getElementById('assessMonthId' + month + 'assess');
	        			if(realityMonth != null && targetMonth != null && assessMonth != null){
	        				var value = {};
	        				value[year + 'mm' + this.getNumber(j)] = realityMonth.value + ',' + targetMonth.value + ',' + assessMonth.value;
	        				paraarr.push(value);
	        			}
	        		}
	        		
	        		//年
       				realityYear = document.getElementById('realityYearId' + FHD.data.yearId + 'reality');
       				targetYear = document.getElementById('targetYearId' + FHD.data.yearId + 'target');
       				assessYear = document.getElementById('assessYearId' + FHD.data.yearId + 'assess');
        			if(realityYear != null && targetYear != null && assessYear != null){
        				var value = {};
        				value[FHD.data.yearId + ''] = realityYear.value + ',' + targetYear.value + ',' + assessYear.value;
        				paraarr.push(value);
        			}
	        		
	        		FHD.ajax({
	                    url: '${ctx}/kpi/kpi/savekpigatherresultquarter.f?kpiid='+kpiid,
	                    params: {
	                    	params: Ext.JSON.encode(paraarr)
	                    },
	                    callback: function (data) {
	                        if (data && data.success) {
	                        	refresh();
	                        }
	                    }
	                });
		        }
		        
		        function saveMonth(){
		        	debugger;
					var paraarr = [];
	        		var quarter = 0;
	        		var month = 0;
	        		var realityQuarter = null;
	        		var targetQuarter = null;
	        		var assessQuarter = null;
	        		
	        		var realityMonth = null;
	        		var targetMonth = null;
	        		var assessMonth = null;
	        		
	        		var realityYear = null;
	        		var targetYear = null;
	        		var assessYear = null;
	        		
	        		//季度
        			for(var j = 1; j < 4 + 1; j++){
        				quarter = j;
        				realityQuarter = document.getElementById(FHD.data.yearId + 'Q' + quarter + 'reality');
        				targetQuarter = document.getElementById(FHD.data.yearId + 'Q' + quarter + 'target');
        				assessQuarter = document.getElementById(FHD.data.yearId + 'Q' + quarter + 'assess');
	        			if(realityQuarter != null && targetQuarter != null && assessQuarter != null){
	        				var value = {};
	        				value[FHD.data.yearId + 'Q' + quarter] = realityQuarter.value + ',' + targetQuarter.value + ',' + assessQuarter.value;
	        				paraarr.push(value);
	        			}
	        		}
	        		
	        		//月
        			for(var j = 1; j < 12 + 1; j++){
        				month = j;
        				realityMonth = document.getElementById(FHD.data.yearId + 'mm' + this.getNumber(month) + 'reality');
        				targetMonth = document.getElementById(FHD.data.yearId + 'mm' +  this.getNumber(month) + 'target');
        				assessMonth = document.getElementById(FHD.data.yearId + 'mm' +  this.getNumber(month) + 'assess');
	        			if(realityMonth != null && targetMonth != null && assessMonth != null){
	        				var value = {};
	        				value[FHD.data.yearId + 'mm' + this.getNumber(j)] = realityMonth.value + ',' + targetMonth.value + ',' + assessMonth.value;
	        				paraarr.push(value);
	        			}
	        		}
	        		
	        		//年
       				realityYear = document.getElementById('realityYearId' + FHD.data.yearId + 'reality');
       				targetYear = document.getElementById('targetYearId' + FHD.data.yearId + 'target');
       				assessYear = document.getElementById('assessYearId' + FHD.data.yearId + 'assess');
        			if(realityYear != null && targetYear != null && assessYear != null){
        				var value = {};
        				value[FHD.data.yearId + ''] = realityYear.value + ',' + targetYear.value + ',' + assessYear.value;
        				paraarr.push(value);
        			}
	        		
	        		FHD.ajax({
	                    url: '${ctx}/kpi/kpi/savekpigatherresultquarter.f?kpiid='+kpiid,
	                    params: {
	                    	params: Ext.JSON.encode(paraarr)
	                    },
	                    callback: function (data) {
	                        if (data && data.success) {
	                        	refresh();
	                        }
	                    }
	                });
		        }
		        
		        function saveQuarter(){
					var paraarr = [];
	        		var quarter = 0;
	        		var month = 0;
	        		var realityQuarter = null;
	        		var targetQuarter = null;
	        		var assessQuarter = null;
	        		
	        		var realityYear = null;
	        		var targetYear = null;
	        		var assessYear = null;
	        		
	        		//季度
        			for(var j = 1; j < 4 + 1; j++){
        				quarter = j;
        				realityQuarter = document.getElementById(FHD.data.yearId + 'Q' + quarter + 'reality');
        				targetQuarter = document.getElementById(FHD.data.yearId + 'Q' + quarter + 'target');
        				assessQuarter = document.getElementById(FHD.data.yearId + 'Q' + quarter + 'assess');
	        			if(realityQuarter != null && targetQuarter != null && assessQuarter != null){
	        				var value = {};
	        				value[FHD.data.yearId + 'Q' + quarter] = realityQuarter.value + ',' + targetQuarter.value + ',' + assessQuarter.value;
	        				paraarr.push(value);
	        			}
	        		}
	        		
	        		//年
       				realityYear = document.getElementById('realityYearId' + FHD.data.yearId + 'reality');
       				targetYear = document.getElementById('targetYearId' + FHD.data.yearId + 'target');
       				assessYear = document.getElementById('assessYearId' + FHD.data.yearId + 'assess');
        			if(realityYear != null && targetYear != null && assessYear != null){
        				var value = {};
        				value[FHD.data.yearId + ''] = realityYear.value + ',' + targetYear.value + ',' + assessYear.value;
        				paraarr.push(value);
        			}
	        		FHD.ajax({
	                    url: '${ctx}/kpi/kpi/savekpigatherresultquarter.f?kpiid='+kpiid,
	                    params: {
	                    	params: Ext.JSON.encode(paraarr)
	                    },
	                    callback: function (data) {
	                        if (data && data.success) {
	                        	refresh();
	                        }
	                    }
	                });
		        }
		        
		        function saveHalfYear(){
					var paraarr = [];
	        		var halfYear = 0;
	        		var month = 0;
	        		var realityHalfYear = null;
	        		var targetHalfYear = null;
	        		var assessHalfYear = null;
	        		
	        		var realityYear = null;
	        		var targetYear = null;
	        		var assessYear = null;
	        		
	        		//半年
        			for(var j = 0; j < 2; j++){
        				halfYear = j;
        				realityHalfYear = document.getElementById(FHD.data.yearId + 'hf' + halfYear + 'reality');
        				targetHalfYear = document.getElementById(FHD.data.yearId + 'hf' + halfYear + 'target');
        				assessHalfYear = document.getElementById(FHD.data.yearId + 'hf' + halfYear + 'assess');
	        			if(realityHalfYear != null && targetHalfYear != null && assessHalfYear != null){
	        				var value = {};
	        				value[FHD.data.yearId + 'hf' + halfYear] = realityHalfYear.value + ',' + targetHalfYear.value + ',' + assessHalfYear.value;
	        				paraarr.push(value);
	        			}
	        		}
	        		
	        		//年
       				realityYear = document.getElementById('realityYearId' + FHD.data.yearId + 'reality');
       				targetYear = document.getElementById('targetYearId' + FHD.data.yearId + 'target');
       				assessYear = document.getElementById('assessYearId' + FHD.data.yearId + 'assess');
        			if(realityYear != null && targetYear != null && assessYear != null){
        				var value = {};
        				value[FHD.data.yearId + ''] = realityYear.value + ',' + targetYear.value + ',' + assessYear.value;
        				paraarr.push(value);
        			}
        			
	        		FHD.ajax({
	                    url: '${ctx}/kpi/kpi/savekpigatherresultquarter.f?kpiid='+kpiid,
	                    params: {
	                    	params: Ext.JSON.encode(paraarr)
	                    },
	                    callback: function (data) {
	                        if (data && data.success) {
	                        	refresh();
	                        }
	                    }
	                });
		        }
		        
		        function refresh(){
		        	FHD.ajax({
	                    url: '${ctx}/kpi/kpi/createtable.f?edit=false',
	                    params: {
	                    	condItem: Ext.JSON.encode(paraobj)
	                    },
	                    callback: function (data) {
	                        if (data && data.success) {
	                        	FHD.panel.basicPanel.items.items[0].items.items[1].body.update(data.tableHtml);
	                        	Ext.getCmp('gatherresulttableinputsave').hide();
	        					Ext.getCmp('tableEditId').show();
	        					FHD.panel.basicPanel.body.unmask();
	                        }
	                    }
	                });
		        }
			   
		        Ext.onReady(function(){
		        	createQuarterTable();
		        	var navigationBars = new Ext.scripts.component.NavigationBars();
		        	
		        	if("sm"==type){
		        		navigationBars.renderHtml('gatherresulttableNavDiv', smid, FHD.data.kpiName, 'sm');
		        	}else if("kpitype"==type){
		        		navigationBars.renderHtml('gatherresulttableNavDiv', kpitypeId, FHD.data.kpiName, 'kpi');
		        	}else if("myfolder"==type){
		        	}else{
		        		navigationBars.renderHtml('gatherresulttableNavDiv', currentId, FHD.data.kpiName, 'sc');
		        	}
		        	
                    
		        });   
		        
                </script>
            </head>
            
            <body>
            	<div id="gatherresulttableNavDiv" style="width: 100%; height: 30px"></div>
            	<div id='gatherresulttableDiv'></div>
            </body>
        
        </html>
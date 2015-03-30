<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
    <%@ include file="/WEB-INF/jsp/commons/include-tagsOnly.jsp" %>
        <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
        <html>
            
            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <title>指标结果采集入录</title>
                <script type="text/javascript">
                var kpiname = "${param.kpiname}";
                var currentId = "${param.categoryid}";
                var parentid = "${param.categoryparentid}";
                var editflag = "${param.editflag}";
                var parentname = "${param.categoryparentname}";
                var kpiname = "${param.kpiname}";
                var kpiid = "${param.kpiid}";
                var frequence = "${param.frequence}";
                var year = "${param.yearId}";
                var basicInputPanel = null;
                
                function charts(panel, id){
                	panel.layout.setActiveItem(id);
                	Ext.getCmp('tbarChartsButtonId${param._dc}').toggle(true);
                	Ext.getCmp('tbarListButtonId${param._dc}').toggle(false);
                	Ext.getCmp('gatherresulttableinputsave').hide();
                }
                
                function list(panel, id){
                	panel.layout.setActiveItem(id);
                	Ext.getCmp('tbarChartsButtonId${param._dc}').toggle(false);
                	Ext.getCmp('tbarListButtonId${param._dc}').toggle(true);
                	Ext.getCmp('gatherresulttableinputsave').show();
                }
                
                function componentResize(c,width,height){//c要被设置的对象，width：需要减去的宽度,height:需要减去的高度
        			var me = this;
        			FHD.getCenterPanel().on('resize',function(t,w,h){
        				c.setWidth(w - fhd_kpi_kpiaccordion_view.accordion.getWidth() - 7); 
        				c.setHeight(h - FHD.getCenterPanel().getTabBar().getHeight()-1-height - 25);
        			});
        		}
                
				function createQuarterTable(){
					 //调用后台查询指标的采集频率
					var paraobj = {};
					var me = this;
		            paraobj.kpiname = kpiname;
		            paraobj.year = FHD.data.yearId;
		            FHD.data.edit = true;
	                FHD.ajax({
	                    url: '${ctx}/kpi/kpi/createtable.f?edit=true',
	                    params: {
	                    	condItem: Ext.JSON.encode(paraobj)
	                    },
	                    callback: function (data) {
	                        if (data && data.success) {
	                        	var funsionChartPanel = Ext.create('FHD.ux.FusionChartPanel',{
	                        		id : 'funsionChartPanelId${param._dc}',
	                    			chartType:'FCF_MSLine',
	                    			region:'center',
	                    			xmlData:data.xml
	                    		});
	                        	
	                        	var tablePanel = Ext.create('Ext.panel.Panel', {
	                        		id : 'tablePanelId${param._dc}',
                                    html : data.tableHtml,
                                    autoScroll: true,
                                    bodyStyle: 'border-bottom: 1px solid #bec0c0 !important;'
                                });
	                        	
	                        	var arr = '-';
	                        	me.basicInputPanel = Ext.create('FHD.ux.CardPanel', {
	                                xtype: 'cardpanel',
	                                activeItem: 0,
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
	                                        	me.charts(me.basicInputPanel, me.basicInputPanel.items.items[0].id);
	                                        }
	                                    },
	                                    arr, {
	                                    	id : 'tbarListButtonId${param._dc}',
	                                        text: '列表',
	                                        iconCls: 'icon-ibm-icon-allmetrics-list',
	                                        handler: function () {
	                                        	me.list(me.basicInputPanel, me.basicInputPanel.items.items[1].id);
	                                        }
	                                    }, '->' ,{
	                                    	id:'gatherresulttableinputsave',
                                            text: '保存',
                                            iconCls: 'icon-page-save',
                                            handler: function () {
                                            	me.save();
                                            }
                                        },{
                                            text: '返回',
                                            iconCls: 'icon-arrow-undo',
                                            handler: function () {
                                            	var rightUrl = "${ctx}/pages/kpi/kpi/opt/kpicategoryedit.jsp?"+"categoryparentid=" + parentid + "&categoryid=" + currentId + "&categoryparentname=" + encodeURIComponent(parentname);
                            		        	top.fhd_kpi_kpiaccordion_view.initRightPanel(rightUrl);
                                            }
                                        }]
	                                }
	                            });
	                        	
	                        	Ext.getCmp('tbarListButtonId${param._dc}').toggle(true);
	                        	
	                        	FHD.panel.basicInputPanel = Ext.create('Ext.panel.Panel',{
		                        	renderTo : 'gatherresulttableInputDiv',
		                        	height:FHD.getCenterPanelHeight() - 25,
		        					width: document.body.clientWidth - fhd_kpi_kpiaccordion_view.accordion.getWidth() - 12,
		                    	    border:false,
		                    	    autoScroll: true,
		                    		layout: {
		                    	        type: 'fit'
		                    	    },
		                    	    items:[me.basicInputPanel]
		                    	});
                                
	                        	me.list(me.basicInputPanel, me.basicInputPanel.items.items[1].id);
	                        }
	                        me.componentResize(FHD.panel.basicInputPanel, 0, 0);//参数为：需要自适应的对象，需要减去的宽度，需要减去的高度
	                    }
	                });
				}

		        function go(){
		        	var rightUrl = "${ctx}/pages/kpi/kpi/opt/kpicategoryedit.jsp";
		        	top.fhd_kpi_kpiaccordion_view.initRightPanel(rightUrl);
			    }
			    
		        function save(){
		        	if(frequence=="0frequecy_month"){
		        		saveMonth();
		        	}else if(frequence=="0frequecy_quarter"){
		        		saveQuarter();
		        	}else if(frequence=="0frequecy_year"){
		        		saveYear();
		        	}else if(frequence=="0frequecy_week"){
		        		saveWeek();
		        	}else if(frequence=="0frequecy_halfyear"){
		        		saveHalfYear();
		        	}
		        }
		        
		        function saveYear(){
					var paraarr = [];
	        		var realityYear = null;
	        		var targetYear = null;
	        		var assessYear = null;
	        		
	        		//年
       				realityYear = document.getElementById('realityYearId' + year + 'reality');
       				targetYear = document.getElementById('targetYearId' + year + 'target');
       				assessYear = document.getElementById('assessYearId' + year + 'assess');
        			if(realityYear != null && targetYear != null && assessYear != null){
        				var value = {};
        				value[year + ''] = realityYear.value + ',' + targetYear.value + ',' + assessYear.value;
        				paraarr.push(value);
        			}
        			
	        		FHD.ajax({
	                    url: '${ctx}/kpi/kpi/savekpigatherresultquarter.f?kpiid='+kpiid,
	                    params: {
	                    	params: Ext.JSON.encode(paraarr)
	                    },
	                    callback: function (data) {
	                        if (data && data.success) {
	                        	var rightUrl = "${ctx}/pages/kpi/kpi/opt/kpicategoryedit.jsp?"+"categoryparentid=" + parentid + "&editflag=true" + "&categoryid=" + currentId + "&categoryparentname=" + encodeURIComponent(parentname);
	                        	
	        		        	top.fhd_kpi_kpiaccordion_view.initRightPanel(rightUrl); 	
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
        				realityWeek = document.getElementById(year + 'w' + week + 'reality');
        				targetWeek = document.getElementById(year + 'w' + week + 'target');
        				assessWeek = document.getElementById(year + 'w' + week + 'assess');
	        			if(realityWeek != null && targetWeek != null && assessWeek != null){
	        				var value = {};
	        				value[year + 'w' + week] = realityWeek.value + ',' + targetWeek.value + ',' + assessWeek.value;
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
       				realityYear = document.getElementById('realityYearId' + year + 'reality');
       				targetYear = document.getElementById('targetYearId' + year + 'target');
       				assessYear = document.getElementById('assessYearId' + year + 'assess');
        			if(realityYear != null && targetYear != null && assessYear != null){
        				var value = {};
        				value[year + ''] = realityYear.value + ',' + targetYear.value + ',' + assessYear.value;
        				paraarr.push(value);
        			}
	        		
	        		FHD.ajax({
	                    url: '${ctx}/kpi/kpi/savekpigatherresultquarter.f?kpiid='+kpiid,
	                    params: {
	                    	params: Ext.JSON.encode(paraarr)
	                    },
	                    callback: function (data) {
	                        if (data && data.success) {
	                        	var rightUrl = "${ctx}/pages/kpi/kpi/opt/kpicategoryedit.jsp?"+"categoryparentid=" + parentid + "&editflag=true" + "&categoryid=" + currentId + "&categoryparentname=" + encodeURIComponent(parentname);
	        		        	top.fhd_kpi_kpiaccordion_view.initRightPanel(rightUrl);
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
        				realityQuarter = document.getElementById(year + 'Q' + quarter + 'reality');
        				targetQuarter = document.getElementById(year + 'Q' + quarter + 'target');
        				assessQuarter = document.getElementById(year + 'Q' + quarter + 'assess');
	        			if(realityQuarter != null && targetQuarter != null && assessQuarter != null){
	        				var value = {};
	        				value[year + 'Q' + quarter] = realityQuarter.value + ',' + targetQuarter.value + ',' + assessQuarter.value;
	        				paraarr.push(value);
	        			}
	        		}
	        		
	        		//月
        			for(var j = 1; j < 12 + 1; j++){
        				month = j;
        				realityMonth = document.getElementById(year + 'mm' + this.getNumber(month) + 'reality');
        				targetMonth = document.getElementById(year + 'mm' +  this.getNumber(month) + 'target');
        				assessMonth = document.getElementById(year + 'mm' +  this.getNumber(month) + 'assess');
	        			if(realityMonth != null && targetMonth != null && assessMonth != null){
	        				var value = {};
	        				value[year + 'mm' + this.getNumber(j)] = realityMonth.value + ',' + targetMonth.value + ',' + assessMonth.value;
	        				paraarr.push(value);
	        			}
	        		}
	        		
	        		//年
       				realityYear = document.getElementById('realityYearId' + year + 'reality');
       				targetYear = document.getElementById('targetYearId' + year + 'target');
       				assessYear = document.getElementById('assessYearId' + year + 'assess');
        			if(realityYear != null && targetYear != null && assessYear != null){
        				var value = {};
        				value[year + ''] = realityYear.value + ',' + targetYear.value + ',' + assessYear.value;
        				paraarr.push(value);
        			}
	        		
	        		FHD.ajax({
	                    url: '${ctx}/kpi/kpi/savekpigatherresultquarter.f?kpiid='+kpiid,
	                    params: {
	                    	params: Ext.JSON.encode(paraarr)
	                    },
	                    callback: function (data) {
	                        if (data && data.success) {
	                        	var rightUrl = "${ctx}/pages/kpi/kpi/opt/kpicategoryedit.jsp?"+"categoryparentid=" + parentid + "&editflag=true" + "&categoryid=" + currentId + "&categoryparentname=" + encodeURIComponent(parentname);
	                        	
	        		        	top.fhd_kpi_kpiaccordion_view.initRightPanel(rightUrl); 	
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
        				realityQuarter = document.getElementById(year + 'Q' + quarter + 'reality');
        				targetQuarter = document.getElementById(year + 'Q' + quarter + 'target');
        				assessQuarter = document.getElementById(year + 'Q' + quarter + 'assess');
	        			if(realityQuarter != null && targetQuarter != null && assessQuarter != null){
	        				var value = {};
	        				value[year + 'Q' + quarter] = realityQuarter.value + ',' + targetQuarter.value + ',' + assessQuarter.value;
	        				paraarr.push(value);
	        			}
	        		}
	        		
	        		//年
       				realityYear = document.getElementById('realityYearId' + year + 'reality');
       				targetYear = document.getElementById('targetYearId' + year + 'target');
       				assessYear = document.getElementById('assessYearId' + year + 'assess');
        			if(realityYear != null && targetYear != null && assessYear != null){
        				var value = {};
        				value[year + ''] = realityYear.value + ',' + targetYear.value + ',' + assessYear.value;
        				paraarr.push(value);
        			}
	        		FHD.ajax({
	                    url: '${ctx}/kpi/kpi/savekpigatherresultquarter.f?kpiid='+kpiid,
	                    params: {
	                    	params: Ext.JSON.encode(paraarr)
	                    },
	                    callback: function (data) {
	                        if (data && data.success) {
	                        	var rightUrl = "${ctx}/pages/kpi/kpi/opt/kpicategoryedit.jsp?"+"categoryparentid=" + parentid + "&editflag=true" + "&categoryid=" + currentId + "&categoryparentname=" + encodeURIComponent(parentname);
	                        	
	        		        	top.fhd_kpi_kpiaccordion_view.initRightPanel(rightUrl); 	
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
        				realityHalfYear = document.getElementById(year + 'hf' + halfYear + 'reality');
        				targetHalfYear = document.getElementById(year + 'hf' + halfYear + 'target');
        				assessHalfYear = document.getElementById(year + 'hf' + halfYear + 'assess');
	        			if(realityHalfYear != null && targetHalfYear != null && assessHalfYear != null){
	        				var value = {};
	        				value[year + 'hf' + halfYear] = realityHalfYear.value + ',' + targetHalfYear.value + ',' + assessHalfYear.value;
	        				paraarr.push(value);
	        			}
	        		}
	        		
	        		//年
       				realityYear = document.getElementById('realityYearId' + year + 'reality');
       				targetYear = document.getElementById('targetYearId' + year + 'target');
       				assessYear = document.getElementById('assessYearId' + year + 'assess');
        			if(realityYear != null && targetYear != null && assessYear != null){
        				var value = {};
        				value[year + ''] = realityYear.value + ',' + targetYear.value + ',' + assessYear.value;
        				paraarr.push(value);
        			}
        			
	        		FHD.ajax({
	                    url: '${ctx}/kpi/kpi/savekpigatherresultquarter.f?kpiid='+kpiid,
	                    params: {
	                    	params: Ext.JSON.encode(paraarr)
	                    },
	                    callback: function (data) {
	                        if (data && data.success) {
	                        	var rightUrl = "${ctx}/pages/kpi/kpi/opt/kpicategoryedit.jsp?"+"categoryparentid=" + parentid + "&editflag=true" + "&categoryid=" + currentId + "&categoryparentname=" + encodeURIComponent(parentname);
	                        	
	        		        	top.fhd_kpi_kpiaccordion_view.initRightPanel(rightUrl); 	
	                        }
	                    }
	                });
		        }
		        
		        
			    function confirm(){
			    	save();
				}
			    
			    function canel(){
			    	var rightUrl = "${ctx}/pages/kpi/kpi/opt/kpicategoryedit.jsp?"+"categoryparentid=" + parentid + "&editflag=true" + "&categoryid=" + currentId + "&categoryparentname=" + encodeURIComponent(parentname);
		        	top.fhd_kpi_kpiaccordion_view.initRightPanel(rightUrl);
			    }
			    
			    function getKpiinfo(){
	            	FHD.ajax({
	                    url: '${ctx}/kpi/kpi/findkpiinfobyname.f?name='+encodeURI(kpiname),
	                    callback: function (data) {
	                        if (data && data.success) {
	                         	kpiid = data.kpiid;
	                         	frequence = data.frequence;
	                         	createQuarterTable();
	                        }
	                    }
	                });
			    	
			    }
			    
			    Ext.onReady(function(){
			    	getKpiinfo();
			    	var navigationBars = new Ext.scripts.component.NavigationBars();
                    navigationBars.renderHtml('gatherresulttableinputNavDiv', currentId, FHD.data.kpiName, 'sc');
		        });
			   
                </script>
            </head>
            
            <body>
            <div id="gatherresulttableinputNavDiv"></div>
            <div id="gatherresulttableInputDiv"></div>
            </body>
        </html>
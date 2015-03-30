Ext.define('FHD.view.icm.assess.AssessPlanDashboard', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.assessplandashboard',
    
    layout: {
        align: 'stretch',
        type: 'vbox'
    },
    bodyPadding: '3 3 3 3',
    border : false,
    
    initComponent: function() {
        var me = this;
        
        me.finishRateChart = Ext.create('FHD.ux.FusionChartPanel',{
    		id:'assessplan_finish_rate',
    		chartType:'AngularGauge',
			flex:1,
			width:105,
			height:108,
			border:false,
			//title:'计划完成率',
    		xmlData:me.finishRateXml
		});
        
        me.systemDefectRateChart = Ext.create('FHD.ux.FusionChartPanel',{
    		id:'assessplan_systemdefectrate_rate',
    		chartType:'AngularGauge',
			flex:1,
			width:105,
			height:108,
			border:false,
			//title:'制度缺陷率',
    		xmlData:me.systemDefectRateXml
		});
        
        me.performErrorRateChart = Ext.create('FHD.ux.FusionChartPanel',{
    		id:'assessplan_performerror_rate',
    		chartType:'AngularGauge',
			flex:1,
			width:105,
			height:108,
			border:false,
			//title:'执行疏失率',
        	xmlData:me.performErrorRateXml
        });
        
        var tbspacer = {
            xtype:'tbspacer',
            flex:1
        };
        
        var finishRateTitle;
        if(me.finishRate<50){
        	finishRateTitle = '计划完成率&nbsp;&nbsp;<font color="FF6600" size="5px">'+me.finishRate+'%</font>';
        }else if(me.finishRate>=50 && me.finishRate<75){
        	finishRateTitle = '计划完成率&nbsp;&nbsp;<font color="CC9900" size="5px">'+me.finishRate+'%</font>';
        }else{
        	finishRateTitle = '计划完成率&nbsp;&nbsp;<font color="339900" size="5px">'+me.finishRate+'%</font>';
        }
        var systemDefectRateTitle;
        if(me.systemDefectRate<50){
        	systemDefectRateTitle = '制度缺陷率&nbsp;&nbsp;<font color="339900" size="5px">'+me.systemDefectRate+'%</font>';
        }else if(me.systemDefectRate>=50 && me.systemDefectRate<75){
        	systemDefectRateTitle = '制度缺陷率&nbsp;&nbsp;<font color="CC9900" size="5px">'+me.systemDefectRate+'%</font>';
        }else{
        	systemDefectRateTitle = '制度缺陷率&nbsp;&nbsp;<font color="FF6600" size="5px">'+me.systemDefectRate+'%</font>';
        }
        var performErrorRateTitle;
        if(me.performErrorRate<50){
        	performErrorRateTitle = '执行疏失率&nbsp;&nbsp;<font color="339900" size="5px">'+me.performErrorRate+'%</font>';
        }else if(me.performErrorRate>=50 && me.performErrorRate<75){
        	performErrorRateTitle = '执行疏失率&nbsp;&nbsp;<font color="CC9900" size="5px">'+me.performErrorRate+'%</font>';
        }else{
        	performErrorRateTitle = '执行疏失率&nbsp;&nbsp;<font color="FF6600" size="5px">'+me.performErrorRate+'%</font>';
        }
        
        me.upRegion = Ext.widget('panel',{
        	layout: {
		        //align: 'stretch',
		        type: 'vbox'
		    },
		    overflowX: 'hidden',
		    overflowY: 'auto',
		    margin: '1 1 1 1',
		    border:false,
    		width:348,
        	items:[
        		tbspacer,
        	    me.encapsulateChart(me.finishRateChart,finishRateTitle,'反映当前评价计划评价完成情况的指标'),
        	    tbspacer,
        	    me.encapsulateChart(me.systemDefectRateChart,systemDefectRateTitle,'反映当前评价计划设计有效性情况的指标'),
        	    tbspacer,
        	    me.encapsulateChart(me.performErrorRateChart,performErrorRateTitle,'反映当前评价计划执行有效性情况的指标'),
        	    tbspacer
        	]
        });
        
        me.defectLevelChart = Ext.create('FHD.ux.FusionChartPanel',{
    		id:'assessplan_defect_level_chart',
    		chartType:'Column2D',
			flex:4,
			border:false,
			//title:'缺陷等级分布图',
			margin: '1 1 1 1',
    		xmlData:me.defectLevelXml
		});
        
        me.orgDefectChart = Ext.create('FHD.ux.FusionChartPanel',{
    		id:'assessplan_org_defect_chart',
    		chartType:'Column2D',
			flex:4,
			border:false,
			//title:'部门缺陷分布图',
			margin: '1 1 1 1',
    		xmlData:me.orgDefectXml
		});
        
        // 计划进度列表
        me.assessPlanGrid = Ext.widget('fhdgrid',{
        	url: __ctxPath + '/icm/assess/findAssessPlanListByParams.f?companyId='+__user.companyId+'&dealStatus=H,F,A',
        	checked:false,
			searchable:true,
			pagable : true,
			flex:7,
			height:300,
			border:true,
			margin: '1 1 1 1',
			cols : [
			    {dataIndex: 'id',hidden:true},
			    {
				 	header: '操作',
				    xtype:'actioncolumn',
				    dataIndex: '',
				    align: 'center',
				    width: 70,
				    items: [{
				        iconCls: 'icon-page-word',
				        tooltip: '生成评价计划的测试报告',
				        handler: function(grid, rowIndex, colIndex) {
		                    var rec = grid.getStore().getAt(rowIndex);
		                    if('A' == rec.get('dealStatus') || 'F' == rec.get('dealStatus')){
		                    	me.generateTestReport(rec.get('id'));
		                    }
		                }
				    },'-',{
				    	iconCls: 'icon-chart-bar',
				        tooltip: '评价统计',
				        handler: function(grid, rowIndex, colIndex) {
		                    var rec = grid.getStore().getAt(rowIndex);
		                    me.showAssessResultStatics(rec.get('name'),rec.get('id'));
		                }
				    }/*,'-',{
				    	icon: __ctxPath + '/images/icons/icon_browse_dis.gif',
				    	tooltip: '',
				        handler: me.showSampleTestList
				    },'-',{
				        icon: __ctxPath + '/images/icons/icon_browse_r.gif',
				        tooltip: '',
				        handler: me.showSampleTestAllList
				    }*/]
				},
				{header:'计划编号', sortable: false,dataIndex: 'code',flex:1, hidden:true},
				{header:'计划名称', sortable: false,dataIndex: 'name',flex:3,
					renderer:function(value,metaData,record,colIndex,store,view) { 
						return "<a href='javascript:void(0)' onclick=\"Ext.getCmp('"+me.id+"').showPlanViewList('" + record.data.id + "','" + record.data.dealStatus +"')\" >" + value + "</a>"; 
					}
				},
				{header:'计划进度', sortable: false,dataIndex: 'schedule',flex:1,
					renderer:function(value,metaData,record,colIndex,store,view) {
						if(value){
							value = value +'%';
						}
						return value;
					}
				},
				{header:'实际进度', sortable: false,dataIndex: 'actualProgress',flex:1,
					renderer:function(value,metaData,record,colIndex,store,view) {
						if(record.data.dealStatus!='N'){
							return "<a href='javascript:void(0)' onclick=\"Ext.getCmp('"+me.id+"').showProcessViewList('" + record.data.id +"')\" >" + value +'%' + "</a>"; 
						}
						return value;
					}
				},
				{header:'执行状态', sortable: false,dataIndex: 'dealStatus',flex:1,
					renderer:function(value,metaData,record,colIndex,store,view) {
						if(value == 'N'){
							return "未开始";
						}else if(value == 'H'){
							return "处理中";
						}else if(value == 'F'){
							return "已完成";
						}else if(value == 'A'){
							return "逾期";
						}
						return value;
					}
				},
				{header:'创建日期', sortable: false,dataIndex: 'createTime',flex:2},
				{header:'操作', dataIndex: 'operate',sortable: false,flex:1,
				    renderer : function(value, metaData, record, colIndex, store, view) { 
						if('A' == record.data.dealStatus || 'F' == record.data.dealStatus){
							metaData.tdAttr = 'data-qtip="生成评价计划的测试报告"'; 
				    		return "<a href='javascript:void(0)' onclick=\"Ext.getCmp('"+me.id+"').generateTestReport('" + record.data.id + "')\" >"+ "生成测试报告" +" </a>";
				    	}
				    	//metaData.tdAttr = 'data-qtip="只有已完成或逾期的评价计划才能生成测试报告!"'; 
						return "";
				    }
				}
			]
        });
        
        me.assessPlanGrid.on('select',function(t,record,index,e){
        	FHD.ajax({
                url: __ctxPath + '/icm/assess/findAssessPlanChartXmlByAssessPlanId.f',
                params: {
                	assessPlanId: record.data.id
                },
                callback: function (data) {
                    if (data){
                    	me.generateChartXml(data,record.data.id);
                    }
                }
        	});
        });
        /*
        me.assessPlanGrid.store.on('load',function(t,records,success,e){
        	if(records){
        		me.assessPlanGrid.getSelectionModel().select(0);
        	}
        });
        */
        me.rightChart = Ext.create('Ext.container.Container',{
        	layout: {
				type: 'hbox',
	        	align:'stretch'
	        },
	        border:false,
	        flex:8,
        	items:[
	        	me.defectLevelChart,
	        	me.orgDefectChart
        	]
		});
        
        me.downRegion = Ext.create('Ext.container.Container',{
        	layout: {
				type: 'hbox',
	        	align:'stretch'
	        },
	        border:false,
            flex:4,
        	items:[
	        	me.assessPlanGrid
        	]
		});
        
        Ext.applyIf(me, {
        	layout: {
				type: 'hbox',
	        	align:'stretch'
	        },
        	items:[
        		Ext.create('Ext.container.Container',{
		        	layout: {
						type: 'hbox',
			        	align:'stretch'
			        },
			        border:false,
		            flex:4,
		        	items:[
			        	me.upRegion,
			        	me.rightChart
		        	]
				}),
        		me.downRegion]
        });

        me.callParent(arguments);
    },
    encapsulateChart:function(singleChart,title,subtitle){
    	var me=this;
    	var encapsulateChart = Ext.widget('panel',{
			width: 348,
			height: 80,
			layout: {
                type: 'table',
                columns: 2
            },
            border:false,
            defaults: {frame:true},
			items:[
				singleChart,
				Ext.create('Ext.container.Container',{
					layout:'column',
					defaults:{
						columnWidth: 1
					},
					height: 80,
					items:[
						Ext.widget('label',{
							margin : '3 0 0 10',
							html: '<font size="3">'+title+'</font>'
						}),
						Ext.widget('label',{
							margin : '5 0 0 10',
							html: '<font color="#7E8877">'+subtitle+'</font>'
						})
					]
				})
			]
		});
    	
    	return encapsulateChart;
    },
    removeItems: function(){
    	var me=this;
    		//1.先删除
    	me.upRegion.removeAll();
    	me.rightChart.removeAll();
    	
    	if(FusionCharts("assessplan_finish_rate-chart") != undefined){
 		   	FusionCharts("assessplan_finish_rate-chart").dispose();
     	}
    	
    	if(FusionCharts("assessplan_systemdefectrate_rate-chart") != undefined){
  		   	FusionCharts("assessplan_systemdefectrate_rate-chart").dispose();
      	}
    	
    	if(FusionCharts("assessplan_performerror_rate-chart") != undefined){
  		   	FusionCharts("assessplan_performerror_rate-chart").dispose();
      	}
    },
    generateChartXml:function(data,id){
    	var me=this;

    	me.removeItems();
		//2.再创建
    	me.finishRateChart = Ext.create('FHD.ux.FusionChartPanel',{
    		id:'assessplan_finish_rate',
    		chartType:'AngularGauge',
			width:105,
			height:108,
			border:false,
			//title:'计划完成率',
    		xmlData:data.finishRateXml
		});
    	
    	me.systemDefectRateChart = Ext.create('FHD.ux.FusionChartPanel',{
    		id:'assessplan_systemdefectrate_rate',
    		chartType:'AngularGauge',
			width:105,
			height:108,
			border:false,
			//title:'制度缺陷率',
    		xmlData:data.systemDefectRateXml
		});
    	
    	me.performErrorRateChart = Ext.create('FHD.ux.FusionChartPanel',{
    		id:'assessplan_performerror_rate',
    		chartType:'AngularGauge',
			width:105,
			height:108,
			border:false,
			//title:'执行疏失率',
    		xmlData:data.performErrorRateXml
		});
        /*
        me.defectLevelChart = Ext.create('FHD.ux.FusionChartPanel',{
    		id:'assessplan_defect_level_chart',
    		chartType:'Column2D',
			flex:4,
			border:true,
			//title:'缺陷等级分布图',
			margin: '1 1 1 1',
    		xmlData:data.defectLevelXml
		});
        
        me.orgDefectChart = Ext.create('FHD.ux.FusionChartPanel',{
    		id:'assessplan_org_defect_chart',
    		chartType:'Column2D',
			flex:4,
			border:true,
			//title:'部门缺陷分布图',
			margin: '1 1 1 1',
    		xmlData:data.orgDefectXml
		});
        */
    	me.defectLevelChart = Ext.create('FHD.view.icm.statics.AssessResultCountChart',{
    		toolRegion:'north',
    		flex:4,
    		extraParams:{
    			planId:id
    		}
    	});
    	
    	me.orgDefectChart = Ext.create('FHD.view.icm.statics.DefectCountChart',{
    		toolRegion:'north',
    		flex:4,
    		extraParams:{
    			planId:id
    		}
    	});
        var tbspacer = {
            xtype:'tbspacer',
            flex:1
        };
        
        var finishRateTitle;
        if(data.finishRate<50){
        	finishRateTitle = '计划完成率&nbsp;&nbsp;<font color="FF6600" size="5px">'+data.finishRate+'%</font>';
        }else if(data.finishRate>=50 && data.finishRate<75){
        	finishRateTitle = '计划完成率&nbsp;&nbsp;<font color="CC9900" size="5px">'+data.finishRate+'%</font>';
        }else{
        	finishRateTitle = '计划完成率&nbsp;&nbsp;<font color="339900" size="5px">'+data.finishRate+'%</font>';
        }
        var systemDefectRateTitle;
        if(data.systemDefectRate<50){
        	systemDefectRateTitle = '制度缺陷率&nbsp;&nbsp;<font color="339900" size="5px">'+data.systemDefectRate+'%</font>';
        }else if(data.systemDefectRate>=50 && data.systemDefectRate<75){
        	systemDefectRateTitle = '制度缺陷率&nbsp;&nbsp;<font color="CC9900" size="5px">'+data.systemDefectRate+'%</font>';
        }else{
        	systemDefectRateTitle = '制度缺陷率&nbsp;&nbsp;<font color="FF6600" size="5px">'+data.systemDefectRate+'%</font>';
        }
        var performErrorRateTitle;
        if(data.performErrorRate<50){
        	performErrorRateTitle = '执行疏失率&nbsp;&nbsp;<font color="339900" size="5px">'+data.performErrorRate+'%</font>';
        }else if(data.performErrorRate>=50 && data.performErrorRate<75){
        	performErrorRateTitle = '执行疏失率&nbsp;&nbsp;<font color="CC9900" size="5px">'+data.performErrorRate+'%</font>';
        }else{
        	performErrorRateTitle = '执行疏失率&nbsp;&nbsp;<font color="FF6600" size="5px">'+data.performErrorRate+'%</font>';
        }
        
        me.upRegion.add(tbspacer);
        me.upRegion.add(me.encapsulateChart(me.finishRateChart,finishRateTitle,'反映当前评价计划评价完成情况的指标'));
        me.upRegion.add(tbspacer);
        me.upRegion.add(me.encapsulateChart(me.systemDefectRateChart,systemDefectRateTitle,'反映当前评价计划设计有效性情况的指标'));
        me.upRegion.add(tbspacer);
        me.upRegion.add(me.encapsulateChart(me.performErrorRateChart,performErrorRateTitle,'反映当前评价计划执行有效性情况的指标'));
        me.upRegion.add(tbspacer);
        
		me.rightChart.add(me.defectLevelChart);
		me.rightChart.add(me.orgDefectChart);
    },
    showProcessViewList:function(id){
    	var me=this;
    	//判断此条记录是否发起工作流
    	FHD.ajax({
			url : __ctxPath + '/jbpm/processInstance/findJbpmHistProcinstIfExistByBusinessId.f',//判断是否发起工作流
			params : {
				businessId : id
			},
			callback: function (data) {
               	if(!data.success){
               		return Ext.Msg.alert('提示','此条记录并未发起工作流!');
               	}
               	me.assessPlanDetailPanel = Ext.create('FHD.view.icm.assess.AssessPlanDetailsPopContainer',{
					businessId:id
				});
    	
	    		var popWin = Ext.create('FHD.ux.Window',{
					title:'评价计划进度信息',
					//modal:true,//是否模态窗口
					collapsible:false,
					maximizable:true,//（是否增加最大化，默认没有）
					items:[me.assessPlanDetailPanel]
	    		}).show();
            }
		});
    },
    showPlanViewList:function(id,dealStatus){
    	var me=this;
    	
    	me.assessPlanPanel=Ext.create('FHD.view.icm.assess.form.AssessPlanPreview',{
    		assessPlanId:id
		});
		me.assessPlanPanel.reloadData();
		var win = Ext.create('FHD.ux.Window',{
			title:'评价计划详细信息',
			//modal:true,//是否模态窗口
			collapsible:false,
			maximizable:true,//（是否增加最大化，默认没有）
			items:[me.assessPlanPanel]
    	}).show();
    },
    generateTestReport:function(id){
    	var me=this;
    	
    	//后台生成报告
    	FHD.ajax({
	        url: __ctxPath + '/comm/report/saveTestReport.f',
	        async:false,
	        params: {
	        	assessplanId: id
	        },
	        callback: function (data) {
	        	if(data){
	        		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
	        	}
	        }
    	});
    },
    showAssessResultStatics: function(planName,planId){
    	var me=this;
		var centerPanel = Ext.getCmp('center-panel');
		var tab = centerPanel.getComponent('FHD.view.comm.chart.ChartDashboardPanel'+planId);
		if(tab){
			centerPanel.setActiveTab(tab);
		}else{
			
			var p = centerPanel.add(Ext.create('Ext.panel.Panel',{
				id:'FHD.view.icm.statics.AssessResultCountChart'+planId,
				title:planName+'-评价统计',
				flex:1,
				closable:true,
				layout:'fit',
				items:[
					Ext.create('FHD.view.icm.statics.AssessResultCountChart',{
			    		//grid列表url参数--可选
			    		extraParams:{planId: planId}
			    	})
				]
			}));
			centerPanel.setActiveTab(p);
		}
    },
    reloadData:function(){
    	var me=this;
    	
    }
});
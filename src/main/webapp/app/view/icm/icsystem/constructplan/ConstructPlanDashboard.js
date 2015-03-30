Ext.define('FHD.view.icm.icsystem.constructplan.ConstructPlanDashboard', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.constructplandashboard',
    layout: {
        align: 'stretch',
        type: 'vbox'
    },
    bodyPadding: '3 3 3 3',
    border : false,
    initComponent: function() {
        var me = this;
       		me.finishRateChart = Ext.create('FHD.ux.FusionChartPanel',{
    		id:'improve_finish_rate',
    		chartType:'AngularGauge',
			width:105,
			height:108,
			border:false,
			//title:'计划完成率',
    		xmlData:me.finishRateXml
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
		me.upRegion = Ext.widget('panel',{
        	layout: {
		        type: 'vbox'
		    },
		    overflowX: 'hidden',
		    overflowY: 'auto',
		    margin: '1 1 1 1',
		    border:false,
    		flex:4,
        	items:[
        		tbspacer,
        	    me.encapsulateChart(me.finishRateChart,finishRateTitle,'反映当前体系建设情况的指标'),
        	    tbspacer
        	]
        });
		
		me.processChart = Ext.create('FHD.ux.FusionChartPanel',{
    		id:'assessplan_defect_level_chart',
    		chartType:'Column2D',
			flex:4,
			border:false,
			//title:'流程分布图',
			margin: '1 1 1 1',
    		xmlData:me.processXml
		});
        
        me.diagnosisChart = Ext.create('FHD.ux.FusionChartPanel',{
    		id:'assessplan_org_defect_chart',
    		chartType:'Column2D',
			flex:4,
			border:false,
			//title:'合规诊断分布图',
			margin: '1 1 1 1',
    		xmlData:me.diagnosisXml
		});
        
        // 计划进度列表
        me.constructPlanGrid = Ext.widget('fhdgrid',{
        	url: __ctxPath + '/icm/icsystem/constructplan/findconstructplansbypage.f',
        	checked:false,
			searchable:true,
			pagable : true,
			flex:4,
			border:true,
			margin: '1 1 1 1',
			extraParams : {
				status : 'P,D'
			},
			cols : [
			    {dataIndex: 'id',hidden:true},
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
				{header:'创建日期', sortable: false,dataIndex: 'createTime',flex:2}
			]
        });
        
        me.constructPlanGrid.on('select',function(t,record,index,e){
        	FHD.ajax({
                url : __ctxPath+ '/icm/icsystem/findconstructplanchartxmlbycomanyid.f',
                params: {
                	constructPlanId: record.data.id
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
	        	me.processChart,
	        	me.diagnosisChart
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
	        	me.constructPlanGrid
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
    	
    	if(FusionCharts("constructpaln_finish_rate-chart") != undefined){
 		   	FusionCharts("constructpaln_finish_rate-chart").dispose();
     	}
    },
    generateChartXml:function(data,id){
    	var me=this;

    	me.removeItems();
    	
		//2.再创建
    	me.finishRateChart = Ext.create('FHD.ux.FusionChartPanel',{
    		id:'constructpaln_finish_rate',
    		chartType:'AngularGauge',
			width:105,
			height:108,
			border:false,
			//title:'计划完成率',
    		xmlData:data.finishRateXml
		});
    	
    	/*
    	me.processChart = Ext.create('FHD.ux.FusionChartPanel',{
    		id:'assessplan_defect_level_chart',
    		chartType:'Column2D',
			flex:4,
			border:true,
			//title:'流程分布图',
			margin: '1 1 1 1',
    		xmlData:data.processXml
		});
        
        me.diagnosisChart = Ext.create('FHD.ux.FusionChartPanel',{
    		id:'assessplan_org_defect_chart',
    		chartType:'Column2D',
			flex:4,
			border:true,
			//title:'合规诊断分布图',
			margin: '1 1 1 1',
    		xmlData:data.diagnosisXml
		});
    	*/
    	me.processChart = Ext.create('FHD.view.icm.statics.AssessResultCountChart',{
    		toolRegion:'north',
    		flex:4,
    		extraParams:{
    			iPlanId:id
    		}
    	});
    	
    	me.diagnosisChart = Ext.create('FHD.view.icm.statics.DefectCountChart',{
    		toolRegion:'north',
    		flex:4,
    		extraParams:{
    			iPlanId:id
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
        
        me.upRegion.add(tbspacer);
        me.upRegion.add(me.encapsulateChart(me.finishRateChart,finishRateTitle,'反映当前体系建设情况的指标'));
        me.upRegion.add(tbspacer);
        
        me.rightChart.add(me.processChart);
		me.rightChart.add(me.diagnosisChart);
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
    	me.constructPlanPanel=Ext.create('FHD.view.icm.icsystem.constructplan.form.ConstructPlanRangeFormForView',{
    		businessId:id,
			dealStatus:dealStatus
		});
		me.constructPlanPanel.initParam({
			businessId : id,
			dealStatus:dealStatus
		});
		me.constructPlanPanel.reloadData();
		var win = Ext.create('FHD.ux.Window',{
			title:'建设计划详细信息',
			//modal:true,//是否模态窗口
			collapsible:false,
			maximizable:true,//（是否增加最大化，默认没有）
			items:[me.constructPlanPanel]
    	}).show();
    },
    reloadData:function(){
    	var me=this;
    	
    }
});
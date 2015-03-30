Ext.define('FHD.view.comm.chart.ChartDashboard', {
    extend: 'Ext.container.Container',
    alias: 'widget.chartdashboard',
    
    border:false,
	//url: __ctxPath + '/icm/statics/finddefectcountbysome.f',
    url: __ctxPath + '/app/view/comm/chart/data/baseData.json',
	extraParams:{
		id:'123',
		name:'456'
	},
	
	initComponent: function() {
    	var me = this;
    	/*
    	me.singleColumns =[
      	    {dataIndex:'id',hidden:true},
          	{
          		header: '部门', 
          		dataIndex: 'name', 
          		sortable: true, 
          		flex : 1,
          		renderer:function(value,metaData,record,colIndex,store,view) { 
  		      		metaData.tdAttr = 'data-qtip="'+value+'"'; 
  		      	    return value;
  		      	}
          	},
  	      	{
          		header: '缺陷数量', 
          		dataIndex: 'value',
          		sortable: true, 
          		flex : 1,
          		renderer:function(value,metaData,record,colIndex,store,view) { 
  		      		metaData.tdAttr = 'data-qtip="'+value+'"'; 
  		      	    return value;  
  		      	}
          	}
    	];
    	*/
    	me.multiColumns =[
    	    {dataIndex:'id',hidden:true},
    	    {dataIndex:'orgId',hidden:true},
        	{
        		header: '部门', 
        		dataIndex: 'orgName', 
        		sortable: true, 
        		flex : 1,
        		renderer:function(value,metaData,record,colIndex,store,view) { 
		      		metaData.tdAttr = 'data-qtip="'+value+'"'; 
		      	    return value;
		      	}
        	},
        	{dataIndex:'planId',hidden:true},
        	{
        		header: '评价计划', 
        		dataIndex: 'planName', 
        		sortable: true, 
        		flex : 1,
        		renderer:function(value,metaData,record,colIndex,store,view) { 
		      		metaData.tdAttr = 'data-qtip="'+value+'"'; 
		      	    return value;
		      	}
        	},
        	{dataIndex:'processId',hidden:true},
        	{
        		header: '流程', 
        		dataIndex: 'processName', 
        		sortable: true, 
        		flex : 1,
        		renderer:function(value,metaData,record,colIndex,store,view) { 
		      		metaData.tdAttr = 'data-qtip="'+value+'"'; 
		      	    return value;
		      	}
        	},
        	{dataIndex:'processPointId',hidden:true},
        	{
        		header: '流程节点', 
        		dataIndex: 'processPointName', 
        		sortable: true, 
        		flex : 1,
        		renderer:function(value,metaData,record,colIndex,store,view) { 
		      		metaData.tdAttr = 'data-qtip="'+value+'"'; 
		      	    return value;
		      	}
        	},
        	{dataIndex:'measureId',hidden:true},
        	{
        		header: '控制措施', 
        		dataIndex: 'measureName', 
        		sortable: true, 
        		flex : 1,
        		renderer:function(value,metaData,record,colIndex,store,view) { 
		      		metaData.tdAttr = 'data-qtip="'+value+'"'; 
		      	    return value;
		      	}
        	},
        	{dataIndex:'assessPointId',hidden:true},
        	{
        		header: '评价点', 
        		dataIndex: 'assessPointName', 
        		sortable: true, 
        		flex : 1,
        		renderer:function(value,metaData,record,colIndex,store,view) { 
		      		metaData.tdAttr = 'data-qtip="'+value+'"'; 
		      	    return value;
		      	}
        	},
	      	{
        		header: '整改状态', 
        		dataIndex: 'dealStatus',
        		sortable: true, 
        		flex : 1,
        		renderer:function(value,metaData,record,colIndex,store,view) { 
		      		metaData.tdAttr = 'data-qtip="'+value+'"'; 
		      	    return value;  
		      	}
        	},
	      	{
        		header: '缺陷等级', 
        		dataIndex: 'defectLevel',
        		sortable: true, 
        		flex : 1,
        		renderer:function(value,metaData,record,colIndex,store,view) { 
		      		metaData.tdAttr = 'data-qtip="'+value+'"'; 
		      	    return value;  
		      	}
        	},
	      	{
        		header: '缺陷类型', 
        		dataIndex: 'defectType',
        		sortable: true, 
        		flex : 1,
        		renderer:function(value,metaData,record,colIndex,store,view) { 
		      		metaData.tdAttr = 'data-qtip="'+value+'"'; 
		      	    return value;  
		      	}
        	},
	      	{
        		header: '缺陷数量', 
        		dataIndex: 'defectCount',
        		sortable: true, 
        		flex : 1,
        		renderer:function(value,metaData,record,colIndex,store,view) { 
		      		metaData.tdAttr = 'data-qtip="'+value+'"'; 
		      	    return value;  
		      	}
        	}
      	];
    	
    	me.chartPanel = Ext.create('FHD.view.comm.chart.ChartDashboardPanel',{
    		//图表标题--可选
    		caption:'部门缺陷数据',
    		//查询条件自定义位置--可选
    		toolRegion:'west',
    		//数据类型--必选
    		dataType:'baseData',
    		flex:1,
    		//系列与图例下拉选项--必选
    		storeItem:[
	    	    {'dataIndex':'', 'header':'请选择'},
	    	    {'dataIndex':'orgName', 'header':'部门'},
	          	{'dataIndex':'planName', 'header':'评价计划'},
	          	{'dataIndex':'processName', 'header':'流程'},
	          	{'dataIndex':'processPointName', 'header':'流程节点'},
	          	{'dataIndex':'measureName', 'header':'控制措施'},
	          	{'dataIndex':'assessPointName', 'header':'评价点'},
	      	    {'dataIndex':'dealStatus', 'header':'整改状态'},
	      	    {'dataIndex':'defectType', 'header':'缺陷类型'},
	          	{'dataIndex':'defectLevel', 'header':'缺陷等级'}
        	],
        	//默认显示图表类型--可选
        	type:'msColumnChart',
        	//默认系列与图例选项--可选
        	xCol:{header:'缺陷',dataIndex:'defectLevel'},
    		yCol:{header:'部门',dataIndex:'orgName'},
    		//算法默认选项--可选
            methodType:'add',
    		//值选项--必选
        	valueCol:{header:'缺陷数量',dataIndex:'defectCount'},
        	//可选项，排序，例如：me.cols.yCol.values=["yi","er","san+"]
        	//grid列表columns--必选
    		multiColumns:me.multiColumns,
    		//grid列表url--必选
    		url:me.url,
    		//grid列表url参数--可选
    		extraParams:me.extraParams
    	});
    	me.chartPanel1 = Ext.create('FHD.view.comm.chart.ChartDashboardPanel',{
    		caption:'部门缺陷数据',
    		toolRegion:'east',
    		//cols:{xCol:{header:'部门',dataIndex:'orgName'},yCol:{header:'缺陷等级',dataIndex:'defectLevel'},valueCol:{header:'缺陷数量',dataIndex:'defectCount'}},
    		dataType:'baseData',
    		flex:1,
    		storeItem:[
	    	    {'dataIndex':'', 'header':'请选择'},
	      	    {'dataIndex':'dealStatus', 'header':'整改状态'},
	      	    {'dataIndex':'defectType', 'header':'缺陷类型'},
	          	{'dataIndex':'orgName', 'header':'部门'},
	          	{'dataIndex':'defectLevel', 'header':'缺陷等级'}
        	],
        	//默认显示多维柱图
        	type:'msLineChart',
        	xCol:{header:'缺陷等级',dataIndex:'defectLevel'},
    		yCol:{header:'部门',dataIndex:'orgName'},
    		
        	valueCol:{header:'缺陷数量',dataIndex:'defectCount'},
        	//可选项，排序，例如：me.cols.yCol.values=["yi","er","san+"]
    		//me.cols.yCol.values:
    		multiColumns:me.multiColumns,
    		
    		//dataJson:[],
    		url:me.url,
    		extraParams:me.extraParams
    	});
    	me.chartPanel2 = Ext.create('FHD.view.comm.chart.ChartDashboardPanel',{
    		caption:'部门缺陷数据',
    		
    		//cols:{xCol:{header:'部门',dataIndex:'orgName'},yCol:{header:'缺陷等级',dataIndex:'defectLevel'},valueCol:{header:'缺陷数量',dataIndex:'defectCount'}},
    		dataType:'baseData',
    		flex:1,
    		storeItem:[
	    	    {'dataIndex':'', 'header':'请选择'},
	      	    {'dataIndex':'dealStatus', 'header':'整改状态'},
	      	    {'dataIndex':'defectType', 'header':'缺陷类型'},
	          	{'dataIndex':'orgName', 'header':'部门'},
	          	{'dataIndex':'defectLevel', 'header':'缺陷等级'}
        	],
        	
        	//默认显示折线图
        	type:'lineChart',
    		xCol:{header:'部门',dataIndex:'orgName'},
    		
        	valueCol:{header:'缺陷数量',dataIndex:'defectCount'},
        	//可选项，排序，例如：me.cols.yCol.values=["yi","er","san+"]
    		//me.cols.yCol.values:
    		multiColumns:me.multiColumns,
    		
    		//dataJson:[],
    		url:me.url,
    		extraParams:me.extraParams
    	});
    	me.chartPanel3 = Ext.create('FHD.view.comm.chart.ChartDashboardPanel',{
    		caption:'部门缺陷数据',
    		
    		//cols:{xCol:{header:'部门',dataIndex:'orgName'},yCol:{header:'缺陷等级',dataIndex:'defectLevel'},valueCol:{header:'缺陷数量',dataIndex:'defectCount'}},
    		dataType:'baseData',
    		flex:1,
    		storeItem:[
	    	    {'dataIndex':'', 'header':'请选择'},
	      	    {'dataIndex':'dealStatus', 'header':'整改状态'},
	      	    {'dataIndex':'defectType', 'header':'缺陷类型'},
	          	{'dataIndex':'orgName', 'header':'部门'},
	          	{'dataIndex':'defectLevel', 'header':'缺陷等级'}
        	],
        	//默认显示饼图
        	type:'pieChart',
    		xCol:{header:'部门',dataIndex:'orgName'},
    		
        	valueCol:{header:'缺陷数量',dataIndex:'defectCount'},
        	//可选项，排序，例如：me.cols.yCol.values=["yi","er","san+"]
    		//me.cols.yCol.values:
    		multiColumns:me.multiColumns,
    		
    		//dataJson:[],
    		url:me.url,
    		extraParams:me.extraParams
    	});
    	me.chartPanel4 = Ext.create('FHD.view.comm.chart.ChartDashboardPanel',{
    		caption:'部门缺陷数据',
    		toolRegion:'east',
    		//cols:{xCol:{header:'部门',dataIndex:'orgName'},yCol:{header:'缺陷等级',dataIndex:'defectLevel'},valueCol:{header:'缺陷数量',dataIndex:'defectCount'}},
    		dataType:'baseData',
    		
    		flex:1,
    		storeItem:[
	    	    {'dataIndex':'', 'header':'请选择'},
	      	    {'dataIndex':'dealStatus', 'header':'整改状态'},
	      	    {'dataIndex':'defectType', 'header':'缺陷类型'},
	          	{'dataIndex':'orgName', 'header':'部门'},
	          	{'dataIndex':'defectLevel', 'header':'缺陷等级'}
        	],
        	//默认显示柱图
        	type:'columnChart',
        	xCol:{header:'缺陷等级',dataIndex:'defectLevel'},
        	
        	
        	valueCol:{header:'缺陷数量',dataIndex:'defectCount'},
        	//可选项，排序，例如：me.cols.yCol.values=["yi","er","san+"]
    		//me.cols.yCol.values:
    		
    		multiColumns:me.multiColumns,
    		//可选项，与url两个二选一即可，此参数说明是静态数据时使用,
    		//dataJson:[],
    		url:me.url,
    		extraParams:me.extraParams
    	});
    	me.chartPanel5 = Ext.create('FHD.view.comm.chart.ChartDashboardPanel',{
    		caption:'部门缺陷数据',
    		toolRegion:'west',
    		//cols:{xCol:{header:'部门',dataIndex:'orgName'},yCol:{header:'缺陷等级',dataIndex:'defectLevel'},valueCol:{header:'缺陷数量',dataIndex:'defectCount'}},
    		dataType:'baseData',
    		flex:1,
    		storeItem:[
	    	    {'dataIndex':'', 'header':'请选择'},
	      	    {'dataIndex':'dealStatus', 'header':'整改状态'},
	      	    {'dataIndex':'defectType', 'header':'缺陷类型'},
	          	{'dataIndex':'orgName', 'header':'部门'},
	          	{'dataIndex':'defectLevel', 'header':'缺陷等级'}
        	],
        	//默认显示仪表盘
        	type:'angularGaugeChart',
        	
        	valueCol:{header:'缺陷数量',dataIndex:'defectCount'},
        	//可选项，排序，例如：me.cols.yCol.values=["yi","er","san+"]
    		//me.cols.yCol.values:
    		multiColumns:me.multiColumns,
    		
    		//dataJson:[],
    		url:me.url,
    		extraParams:me.extraParams
    	});
    	me.chartPanel6 = Ext.create('FHD.view.comm.chart.ChartDashboardPanel',{
    		caption:'部门缺陷数据',
    		toolRegion:'west',
    		//cols:{xCol:{header:'部门',dataIndex:'orgName'},yCol:{header:'缺陷等级',dataIndex:'defectLevel'},valueCol:{header:'缺陷数量',dataIndex:'defectCount'}},
    		dataType:'baseData',
    		flex:1,
    		storeItem:[
	    	    {'dataIndex':'', 'header':'请选择'},
	      	    {'dataIndex':'dealStatus', 'header':'整改状态'},
	      	    {'dataIndex':'defectType', 'header':'缺陷类型'},
	          	{'dataIndex':'orgName', 'header':'部门'},
	          	{'dataIndex':'defectLevel', 'header':'缺陷等级'}
        	],
        	//默认显示环形图
        	type:'circleChart',
        	
        	valueCol:{header:'缺陷数量',dataIndex:'defectCount'},
        	//可选项，排序，例如：me.cols.yCol.values=["yi","er","san+"]
    		//me.cols.yCol.values:
    		multiColumns:me.multiColumns,
    		
    		//dataJson:[],
    		url:me.url,
    		extraParams:me.extraParams
    	});
    	
    	me.chartPanel7 = Ext.create('FHD.view.comm.chart.ChartDashboardPanel',{
    		caption:'部门缺陷数据',
    		toolRegion:'west',
    		//cols:{xCol:{header:'部门',dataIndex:'orgName'},yCol:{header:'缺陷等级',dataIndex:'defectLevel'},valueCol:{header:'缺陷数量',dataIndex:'defectCount'}},
    		dataType:'baseData',
    		flex:1,
    		storeItem:[
	    	    {'dataIndex':'', 'header':'请选择'},
	      	    {'dataIndex':'dealStatus', 'header':'整改状态'},
	      	    {'dataIndex':'defectType', 'header':'缺陷类型'},
	          	{'dataIndex':'orgName', 'header':'部门'},
	          	{'dataIndex':'defectLevel', 'header':'缺陷等级'}
        	],
        	//默认显示多维柱图
        	type:'grid',
        	xCol:{header:'缺陷等级',dataIndex:'defectLevel'},
    		yCol:{header:'部门',dataIndex:'orgName'},
    		
        	valueCol:{header:'缺陷数量',dataIndex:'defectCount'},
        	//可选项，排序，例如：me.cols.yCol.values=["yi","er","san+"]
    		//me.cols.yCol.values:
    		multiColumns:me.multiColumns,
    		
    		//dataJson:[],
    		url:me.url,
    		extraParams:me.extraParams
    	});
    	me.chartPanel8 = Ext.create('FHD.view.comm.chart.ChartDashboardPanel',{
    		caption:'部门缺陷数据',
    		toolRegion:'east',
    		//cols:{xCol:{header:'部门',dataIndex:'orgName'},yCol:{header:'缺陷等级',dataIndex:'defectLevel'},valueCol:{header:'缺陷数量',dataIndex:'defectCount'}},
    		dataType:'baseData',
    		flex:1,
    		storeItem:[
	    	    {'dataIndex':'', 'header':'请选择'},
	      	    {'dataIndex':'dealStatus', 'header':'整改状态'},
	      	    {'dataIndex':'defectType', 'header':'缺陷类型'},
	          	{'dataIndex':'orgName', 'header':'部门'},
	          	{'dataIndex':'defectLevel', 'header':'缺陷等级'}
        	],
        	//默认显示交叉表
        	type:'crossGrid',
        	xCol:{header:'缺陷类型',dataIndex:'defectType'},
    		yCol:{header:'部门',dataIndex:'orgName'},
    		
        	valueCol:{header:'缺陷数量',dataIndex:'defectCount'},
        	//可选项，排序，例如：me.cols.yCol.values=["yi","er","san+"]
    		//me.cols.yCol.values:
    		multiColumns:me.multiColumns,
    		
    		url:me.url,
    		extraParams:me.extraParams
    	});
    	
    	Ext.applyIf(me, {
        	layout: {
				type: 'vbox',
	        	align:'stretch'
	        },
        	items:[
        		Ext.create('Ext.container.Container',{
	        		layout: {
						type: 'hbox',
			        	align:'stretch'
			        },
			        flex:1,
			        items:[
			        	Ext.create('Ext.container.Container',{
			        		layout: {
								type: 'vbox',
					        	align:'stretch'
					        },
					        flex:0.5,
					        items:[
					        	me.chartPanel5,//仪表盘
					        	me.chartPanel6//环形图
					        ]
		        		}),
			        	me.chartPanel2,//折线图
			        	me.chartPanel3,//饼图
			        	me.chartPanel4//柱图
			        ]
        		}),
        		Ext.create('Ext.container.Container',{
        			layout: {
						type: 'hbox',
			        	align:'stretch'
			        },
			        flex:2,
			        items:[
			        	Ext.create('Ext.container.Container',{
			        		layout: {
								type: 'vbox',
					        	align:'stretch'
					        },
					        flex:0.5,
					        items:[
					        	me.chartPanel,//多维柱图
					        	me.chartPanel7//列表
					        ]
		        		}),
			        	Ext.create('Ext.container.Container',{
			        		layout: {
								type: 'vbox',
					        	align:'stretch'
					        },
					        flex:0.5,
					        items:[
					        	me.chartPanel1,//多维折线图
					        	me.chartPanel8//交叉表
					        ]
		        		})
			        ]
        		})
        	]
		});
        
    	me.callParent(arguments);
	},
	//重新加载数据
    reloadData: function() {
    	var me = this;
    	
    }
});
Ext.define('FHD.view.icm.statics.DefectCountChart', {
    extend: 'Ext.container.Container',
    alias: 'widget.defectcountchart',
    border:false,
    toolRegion:'west',
	url: __ctxPath + '/icm/statics/finddefectcountbysome.f',
	extraParams:{
		orgId:'',
		planId:'',
		iPlanId:'',
		processId:'',
		processPointId:'',
		measureId:''
	},
   	//默认显示图表类型--可选
	myType:'msColumnChart',
	//默认系列与图例选项--可选
	myXCol:{header:'年份',dataIndex:'createYear'},
	myYCol:{header:'缺陷等级',dataIndex:'defectLevel'},
	initComponent: function() {
    	var me = this;
    	me.multiColumns =[
    	    {dataIndex:'id',hidden:true},
    	    {dataIndex:'orgId',hidden:true},
        	{
        		header: '年份', 
        		dataIndex: 'createYear', 
        		sortable: true, 
        		flex : 1,
        		renderer:function(value,metaData,record,colIndex,store,view) { 
		      		metaData.tdAttr = 'data-qtip="'+value+'"'; 
		      	    return value;
		      	}
        	},	
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
        	{
        		header: '整改计划', 
        		dataIndex: 'iPlanName', 
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
    		caption:'缺陷统计',
    		//查询条件自定义位置--可选
    		toolRegion:me.toolRegion,
    		//数据类型--必选
    		dataType:'baseData',
    		flex:1,
    		//系列与图例下拉选项--必选
    		storeItem:[
	    	    {'dataIndex':'', 'header':'请选择'},
	    	    {'dataIndex':'createYear', 'header':'年份'},
	    	    {'dataIndex':'orgName', 'header':'部门'},
	          	{'dataIndex':'planName', 'header':'评价计划'},
	          	{'dataIndex':'iPlanName', 'header':'整改计划'},
	          	{'dataIndex':'processName', 'header':'流程'},
	          	/*{'dataIndex':'processPointName', 'header':'流程节点'},
	          	{'dataIndex':'measureName', 'header':'控制措施'},
	          	{'dataIndex':'assessPointName', 'header':'评价点'},*/
	      	    {'dataIndex':'dealStatus', 'header':'整改状态'},
	      	    {'dataIndex':'defectType', 'header':'缺陷类型'},
	          	{'dataIndex':'defectLevel', 'header':'缺陷等级'}
        	],
        	//默认显示图表类型--可选
        	type:me.myType,
        	//默认系列与图例选项--可选
        	xCol:me.myXCol,
    		yCol:me.myYCol,
    		//算法默认选项--可选
            methodType:'add',
    		//值选项--必选
        	valueCol:{header:'缺陷数量',dataIndex:'defectCount'},
        	//可选项，排序，例如：me.cols.yCol.values=["yi","er","san+"]
        	//后缀
    		numberSuffix:'个',
        	//grid列表columns--必选
    		multiColumns:me.multiColumns,
    		//grid列表url--必选
    		url:me.url,
    		//grid列表url参数--可选
    		extraParams:me.extraParams
    	});
    	Ext.applyIf(me, {
        	layout: {
				type: 'vbox',
	        	align:'stretch'
	        },
        	items:[
        	    me.chartPanel
        	]
		});
    	me.callParent(arguments);
	},
	//重新加载数据
    reloadData: function() {
    	var me = this;
    	me.chartPanel.reloadData();
    }
});
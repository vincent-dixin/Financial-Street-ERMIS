Ext.define('FHD.view.icm.statics.AssessResultCountChart', {
    extend: 'Ext.container.Container',
    alias: 'widget.assessresultcountchart',
    border:false,
	url: __ctxPath + '/icm/statics/findassessresultcountbysome.f',
	toolRegion:'west',
	extraParams:{
		orgId:'',
		planId:'',
		empId:'', 
		processId:'',
		processPointId:'',
		measureId:'',
		assessPointId:''
	},
	//默认显示图表类型--可选
	myType:'msColumnChart',
	//默认系列与图例选项--可选
	myXCol:{header:'评价人',dataIndex:'empName'},
	myYCol:{header:'评价完成状态',dataIndex:'isFinished'},
	initComponent: function() {
    	var me = this;
    	me.multiColumns =[
    	    {dataIndex:'id',hidden:true},
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
        	{dataIndex:'empId',hidden:true},
        	{
        		header: '评价人', 
        		dataIndex: 'empName', 
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
        	/*{dataIndex:'processPointId',hidden:true},
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
        	*/
	      	{
        		header: '评价方式', 
        		dataIndex: 'assessMeasure',
        		sortable: true, 
        		flex : 1,
        		renderer:function(value,metaData,record,colIndex,store,view) { 
		      		metaData.tdAttr = 'data-qtip="'+value+'"'; 
		      	    return value;  
		      	}
        	},
	      	{
        		header: '样本是否合格', 
        		dataIndex: 'isValid',
        		sortable: true, 
        		flex : 1,
        		renderer:function(value,metaData,record,colIndex,store,view) { 
		      		metaData.tdAttr = 'data-qtip="'+value+'"'; 
		      	    return value;  
		      	}
        	},
        	{
        		header: '是否完成', 
        		dataIndex: 'isFinished',
        		sortable: true, 
        		flex : 1,
        		renderer:function(value,metaData,record,colIndex,store,view) { 
		      		metaData.tdAttr = 'data-qtip="'+value+'"'; 
		      	    return value;  
		      	}
        	},
	      	{
        		header: '占比(%)', 
        		dataIndex: 'assessResultRate',
        		sortable: true, 
        		flex : 1,
        		renderer:function(value,metaData,record,colIndex,store,view) { 
		      		metaData.tdAttr = 'data-qtip="'+value+'"'; 
		      		return value
		      	}
        	}
      	];
    	
    	me.chartPanel = Ext.create('FHD.view.comm.chart.ChartDashboardPanel',{
    		//图表标题--可选
    		caption:'评价统计',
    		//查询条件自定义位置--可选
    		toolRegion:me.toolRegion,
    		//数据类型--必选
    		dataType:'baseData',
    		flex:1,
    		//系列与图例下拉选项--必选
    		storeItem:[
	    	    {'dataIndex':'', 'header':'请选择'},
	          	{'dataIndex':'planName', 'header':'评价计划'},
	          	{'dataIndex':'empName', 'header':'评价人'},
	          	{'dataIndex':'processName', 'header':'流程'},
	          	//{'dataIndex':'processPointName', 'header':'流程节点'},
	          	//{'dataIndex':'measureName', 'header':'控制措施'},
	          	//{'dataIndex':'assessPointName', 'header':'评价点'},
	      	    {'dataIndex':'assessMeasure', 'header':'评价方式'},
	      	    {'dataIndex':'isValid', 'header':'样本合格状态'},
	      	    {'dataIndex':'isFinished', 'header':'评价完成状态'}
        	],
        	//默认显示图表类型--可选
        	type:me.myType,
        	//默认系列与图例选项--可选
        	xCol:me.myXCol,
    		yCol:me.myYCol,
    		//算法默认选项--可选
            methodType:'add',
    		//值选项--必选
        	valueCol:{header:'占比(%)',dataIndex:'assessResultRate'},
        	//可选项，排序，例如：me.cols.yCol.values=["yi","er","san+"]
        	//grid列表columns--必选
    		multiColumns:me.multiColumns,
    		//后缀
    		numberSuffix:'%',
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
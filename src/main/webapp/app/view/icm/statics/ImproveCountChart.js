Ext.define('FHD.view.icm.statics.ImproveCountChart', {
    extend: 'Ext.container.Container',
    alias: 'widget.improvecountchart',
    border:false,
	url: __ctxPath + '/icm/statics/findimprovecountbysome.f',
	toolRegion:'west',
	extraParams:{
		orgId:''
	},
	initComponent: function() {
    	var me = this;
    	me.multiColumns =[
    	    {dataIndex:'id',hidden:true},
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
        		header: '处理状态', 
        		dataIndex: 'dealStatus', 
        		sortable: true, 
        		flex : 1,
        		renderer:function(value,metaData,record,colIndex,store,view) { 
		      		metaData.tdAttr = 'data-qtip="'+value+'"'; 
		      	    return value;
		      	}
        	},
	      	{
        		header: '整改计划数量', 
        		dataIndex: 'planCount',
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
    		caption:'整改计划统计',
    		//查询条件自定义位置--可选
    		toolRegion:me.toolRegion,
    		//数据类型--必选
    		dataType:'baseData',
    		flex:1,
    		//系列与图例下拉选项--必选
    		storeItem:[
	    	    {'dataIndex':'', 'header':'请选择'},
	    	    {'dataIndex':'createYear', 'header':'年份'},
	          	{'dataIndex':'dealStatus', 'header':'处理状态'}
        	],
        	//默认显示图表类型--可选
        	type:'msColumnChart',
        	//默认系列与图例选项--可选
        	xCol:{header:'年份',dataIndex:'createYear'},
    		yCol:{header:'处理状态',dataIndex:'dealStatus'},
    		//算法默认选项--可选
            methodType:'add',
    		//值选项--必选
        	valueCol:{header:'整改计划数量',dataIndex:'planCount'},
        	//可选项，排序，例如：me.cols.yCol.values=["yi","er","san+"]
        	//grid列表columns--必选
    		multiColumns:me.multiColumns,
    		//后缀
    		numberSuffix:'个',
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
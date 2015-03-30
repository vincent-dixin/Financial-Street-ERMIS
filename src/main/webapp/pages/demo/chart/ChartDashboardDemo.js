Ext.define('FHD.demo.chart.ChartDashboardDemo', {
    extend: 'Ext.container.Container',
    alias: 'widget.chartdashboarddemo',
    
    border:false,
	//url: __ctxPath + '/icm/statics/finddefectcountbysome.f',
    url: __ctxPath + '/app/view/comm/chart/data/dataJson.json',
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
          		header: '部门名称', 
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
        		header: '部门名称', 
        		dataIndex: 'orgName', 
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
    		numberPrefix:'$',
    		numberSuffix:"%",
    		exportFileName:'test',
    		flex:1,
    		//查询条件自定义位置--可选
    		toolRegion:'west',
    		//图表标题--可选
    		caption:'部门缺陷数据',
    		//系列与图例下拉选项--必选
    		storeItem:[
	    	    {'dataIndex':'', 'header':'请选择'},
	      	    {'dataIndex':'dealStatus', 'header':'整改状态'},
	      	    {'dataIndex':'defectType', 'header':'缺陷类型'},
	          	{'dataIndex':'orgName', 'header':'部门'},
	          	{'dataIndex':'defectLevel', 'header':'缺陷等级'}
        	],
        	//数据类型--必选
        	dataType:'baseData',
        	//默认显示图表类型--可选
        	type:'angularGaugeChart',
        	//默认系列与图例选项--可选
        	xCol:{header:'缺陷等级',dataIndex:'defectLevel'},
    		yCol:{header:'部门',dataIndex:'orgName'},
    		//值选项--必选
        	valueCol:{header:'缺陷数量',dataIndex:'defectCount', sumMethod:'add'},
        	//排序--可选，例如：me.cols.yCol.values:["first","second","third"]
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
    	
    }
});
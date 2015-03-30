Ext.define('FHD.view.icm.statics.StandardCountChart', {
    extend: 'Ext.container.Container',
    alias: 'widget.standardcountchart',
    border:false,
	url: __ctxPath + '/icm/statics/findstandardcountbysome.f',
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
	initComponent: function() {
    	var me = this;
    	me.multiColumns =[
    	    {dataIndex:'id',hidden:true},
        	{dataIndex:'orgId',hidden:true},
        	{
        		header: '责任部门', 
        		dataIndex: 'orgName', 
        		sortable: true, 
        		flex : 1,
        		renderer:function(value,metaData,record,colIndex,store,view) { 
		      		metaData.tdAttr = 'data-qtip="'+value+'"'; 
		      	    return value;
		      	}
        	},
        	{dataIndex:'parentId',hidden:true},
        	{
        		header: '内控标准', 
        		dataIndex: 'parentName', 
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
        		header: '内控要素', 
        		dataIndex: 'controlPoint',
        		sortable: true, 
        		flex : 1,
        		renderer:function(value,metaData,record,colIndex,store,view) { 
		      		metaData.tdAttr = 'data-qtip="'+value+'"'; 
		      	    return value;  
		      	}
        	},
	      	{
        		header: '控制层级', 
        		dataIndex: 'controlLevel',
        		sortable: true, 
        		flex : 1,
        		renderer:function(value,metaData,record,colIndex,store,view) { 
		      		metaData.tdAttr = 'data-qtip="'+value+'"'; 
		      	    return value;  
		      	}
        	},
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
        		header: '内控要求数量', 
        		dataIndex: 'standardCount',
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
    		caption:'内控要求统计',
    		//查询条件自定义位置--可选
    		toolRegion:me.toolRegion,
    		//数据类型--必选
    		dataType:'baseData',
    		flex:1,
    		//系列与图例下拉选项--必选
    		storeItem:[
	    	    {'dataIndex':'', 'header':'请选择'},
	    	    {'dataIndex':'createYear', 'header':'年份'},
	          	{'dataIndex':'orgName', 'header':'责任部门'},
	          	{'dataIndex':'parentName', 'header':'内控标准'},
	          	{'dataIndex':'dealStatus', 'header':'处理状态'},
	      	    {'dataIndex':'controlPoint', 'header':'内控要素'},
	      	    {'dataIndex':'controlLevel', 'header':'控制层级'}
        	],
        	//默认显示图表类型--可选
        	type:'msColumnChart',
        	//默认系列与图例选项--可选
        	xCol:{header:'年份',dataIndex:'createYear'},
    		yCol:{header:'内控标准',dataIndex:'parentName'},
    		//算法默认选项--可选
            methodType:'add',
    		//值选项--必选
        	valueCol:{header:'内控要求数量',dataIndex:'standardCount'},
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
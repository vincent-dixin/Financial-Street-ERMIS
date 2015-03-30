Ext.define('FHD.view.comm.theme.AnalysisLayoutPanel2', {
	extend : 'Ext.container.Container',
    alias: 'widget.analysislayoutpanel2',
    requires: [
        'FHD.view.comm.theme.LayoutDataPanel'
       ],
    
    
  initComponent: function() {
	  var me = this;
  	 

		//左上
		me.upleftChart = Ext.create('FHD.ux.FusionChartPanel',{
			border:true,
			style:'padding:5px 5px 0px 5px',
//    			chartType:'AngularGauge',
			//id : 'upleftChart',
			title:'',
			flex:0.5,
			//width:300,
			xmlData:'',
			tools: [{
				        itemId: 'gear',
				        type: 'gear',
				        handler: function(){
				            Ext.getCmp('analysislayoutpanel2').editnewInfo('2.1');
				        }
				    }]
		});

		//右上
    	me.uprightChart = Ext.create('FHD.ux.FusionChartPanel',{
    		border:true,
			style:'padding:5px 5px 0px 0px',
//    			chartType:'Bar2D',
			//id : 'uprightChart',
			title:'',
//    			width:300,
			flex:0.5,
			xmlData:'',
			tools: [{
				        itemId: 'gear',
				        type: 'gear',
				        handler: function(){
				            Ext.getCmp('analysislayoutpanel2').editnewInfo('2.2');
				        }
				    }]
		});
    	me.upRegion = Ext.create('Ext.container.Container',{
        	flex:2,
        	//id:'layoutupRegion',
        	layout: {
				type: 'hbox',
	        	align:'stretch'
	        },
        	items:[
	        	me.upleftChart,
	        	me.uprightChart
        	]
		});
		
		//左下
		me.downleftChart = Ext.create('FHD.ux.FusionChartPanel',{
			border:true,
			style:'padding:5px 5px 0px 5px',
//    			chartType:'MSLine',
			//id:'downChart',
			title:'',
			flex:0.5,
			//width:600,//legendPosition="RIGHT"
			xmlData:'',
			tools: [{
				        itemId: 'gear',
				        type: 'gear',
				        handler: function(){
				            Ext.getCmp('analysislayoutpanel2').editnewInfo('2.3');
				        }
				    }]
		});
//		右下
		me.downrightChart = Ext.create('FHD.ux.FusionChartPanel',{
			border:true,
			style:'padding:5px 5px 0px 0px',
//    		chartType:'MSLine',
			//id:'downChart',
			title:'',
			flex:0.5,
			//width:600,//legendPosition="RIGHT"
			xmlData:'',
			tools: [{
				        itemId: 'gear',
				        type: 'gear',
				        handler: function(){
				            Ext.getCmp('analysislayoutpanel2').editnewInfo('2.4');
				        }
				    }]
		});
		
		me.downRegion = Ext.create('Ext.container.Container',{
        	flex:2,
        	//id:'layoutupRegion',
        	layout: {
				type: 'hbox',
	        	align:'stretch'
	        },
        	items:[
	        	me.downleftChart,
	        	me.downrightChart
        	]
		});
    	
    	Ext.applyIf(me,{
    		layout: {
				type: 'vbox',
	        	align:'stretch'
	        },
	        items:[
		        	me.upRegion,
		        	me.downRegion
	        	]
		});
		me.callParent(arguments);
    },
    
    //上部分图表
    categoryPanel : function(data){
   
    	var typeTitle = this.typeTitle;
    	var me = this;
    	var xml_1='';
    	var xml_2='';
    	var xml_3='';
    	var xml_4='';
    	var id_1='';
    	var id_2='';
    	var id_3='';
    	var id_4='';
    	var chartType_1='';
    	var chartType_2='';
    	var chartType_3='';
    	var chartType_4='';
    	var name_1='';
    	var name_2='';
    	var name_3='';
    	var name_4='';
    	
    	if(data!=null){
		for(var i = 0;i<data.length;i++){
    		if(data[i].positionName=='2.1'){
    			xml_1 = data[i].xmlMap;
    			id_1 = data[i].dataSource;
    			chartType_1 = data[i].chartType;
    			name_1=data[i].dataSourceName;
    		}if(data[i].positionName=='2.2'){
    			xml_2 = data[i].xmlMap;
    			id_2 = data[i].dataSource;
    			chartType_2 = data[i].chartType;
    			name_2=data[i].dataSourceName;
    		}if(data[i].positionName=='2.3'){
    			xml_3 = data[i].xmlMap;
    			id_3 = data[i].dataSource;
    			chartType_3 = data[i].chartType;
    			name_3=data[i].dataSourceName;
    		}if(data[i].positionName=='2.4'){
    			xml_4 = data[i].xmlMap;
    			id_4 = data[i].dataSource;
    			chartType_4 = data[i].chartType;
    			name_4=data[i].dataSourceName;
    		}
			}
    	}
        	
        me.removeAll();
        		
	    var upleftChart = Ext.create('FHD.ux.FusionChartPanel',{
			border:true,
			style:'padding:5px 5px 0px 5px',
			chartType:chartType_1,
			flex:0.5,
			//id : 'upleftChart',
			title:'',
			width:300,
			xmlData:xml_1,
			tools: [{
		        itemId: 'gear',
		        type: 'gear',
		        handler: function(){
		            Ext.getCmp('analysislayoutpanel2').editInfo(2.1,id_1,chartType_1,name_1);
		        }
		    }]
		});

    		
    	
    	var uprightChart = Ext.create('FHD.ux.FusionChartPanel',{
    		border:true,
			style:'padding:5px 5px 0px 0px',
			chartType:chartType_2,
			//id : 'uprightChart',
			title:'',
			width:300,
			flex:0.5,
			xmlData:xml_2,
			 tools: [{
		        itemId: 'gear',
		        type: 'gear',
		        handler: function(){
		            Ext.getCmp('analysislayoutpanel2').editInfo(2.2,id_2,chartType_2,name_2);
		        }
		    }]
		}); 
	
    	var downleftChart = Ext.create('FHD.ux.FusionChartPanel',{
			border:true,
			style:'padding:5px 5px 0px 5px',
			chartType:chartType_3,
			flex:0.5,
			//id : 'down',
			title:'',
			width:600,
			xmlData:xml_3,
			tools: [{
		        itemId: 'gear',
		        type: 'gear',
		        handler: function(){
		            Ext.getCmp('analysislayoutpanel2').editInfo(2.3,id_3,chartType_3,name_3);
		        }
		    }]
		});
		
		var downrightChart = Ext.create('FHD.ux.FusionChartPanel',{
			border:true,
			style:'padding:5px 5px 0px 0px',
			chartType:chartType_4,
			flex:0.5,
			//id : 'down',
			title:'',
			width:600,
			xmlData:xml_4,
			tools: [{
		        itemId: 'gear',
		        type: 'gear',
		        handler: function(){
		            Ext.getCmp('analysislayoutpanel2').editInfo(2.4,id_4,chartType_4,name_4);
		        }
		    }]
		});
    		
    	var upRegion = Ext.create('Ext.container.Container',{
        	flex:2,
        	//id:'layoutupRegion',
        	layout: {
				type: 'hbox',
	        	align:'stretch'
	        },
        	items:[
	        	upleftChart,
	        	uprightChart
        	]
		});
		
		var downRegion = Ext.create('Ext.container.Container',{
        	flex:2,
        	//id:'layoutupRegion',
        	layout: {
				type: 'hbox',
	        	align:'stretch'
	        },
        	items:[
	        	downleftChart,
	        	downrightChart
        	]
		});

            	
            Ext.getCmp('analysislayoutpanel2').add(upRegion);
            Ext.getCmp('analysislayoutpanel2').add(downRegion);
            Ext.getCmp('analysislayoutpanel2').doLayout();
    },
    

	//获取当前年份
    getYear: function(){
    	var myDate = new Date();
    	var year = myDate.getFullYear();
    	return year;
    },
    //重新加载数据
    reloadData: function(){
    	var me = this;
    	if(Ext.getCmp('thememainpanel').paramObj!=undefined){
    		me.gridPanel.store.proxy.extraParams.id = Ext.getCmp('thememainpanel').paramObj.categoryid;
        	me.gridPanel.store.load();
    	}
    },
    
    
        /**
     * 选择指标类型的按钮事件
     */
    editInfo: function (position,kpiid,chartType,name) {
        var me = this;
        
        if(Ext.getCmp('layoutdatapanel')){
      	  var layoutdatapanel = Ext.getCmp('layoutdatapanel');
        }else{
          var layoutdatapanel = Ext.widget('layoutdatapanel',{id:'layoutdatapanel'});
        }
        
        layoutdatapanel.position = position;
        if(chartType=="MSColumnLine3D"){
        	chartType=0
        }else if(chartType=="Doughnut3D"){
        	chartType=1
        }else if(chartType=="MSLine"){
        	chartType=2
        }
        layoutdatapanel.initGridStore(kpiid,chartType,name);
	    me.formwindow = Ext.create('Ext.Window',{
	        constrain: true,
	        layout: 'fit',
	        resizable:true,
//	        iconCls: 'icon-edit', //标题前的图片
	        title:'详细设置',
	        modal: true, //是否模态窗口
	        collapsible: true,
	        scroll: 'auto',
	        closeAction:'destory',
	        width: 500,
	        height: 450,
	        maximizable:true,
	        items: [layoutdatapanel]
        });
        me.formwindow.show();

    },
    
            /**
     * 选择指标类型的按钮事件
     */
    editnewInfo: function (position) {
        var me = this;
        var layoutdatapanel = Ext.widget('layoutdatapanel');
        layoutdatapanel.position = position;
	    me.formwindow = new Ext.Window({
	        constrain: true,
	        layout: 'fit',
	        iconCls: 'iwidgetcon-edit', //标题前的图片
	        modal: true, //是否模态窗口
	        collapsible: true,
	        scroll: 'auto',
	        closeAction:'destroy',
	        width: 400,
	        height: 400,
	        maximizable: true, //是否增加最大化，默认没有
	        items: [layoutdatapanel]
        });
        me.formwindow.show();

    }
    
});
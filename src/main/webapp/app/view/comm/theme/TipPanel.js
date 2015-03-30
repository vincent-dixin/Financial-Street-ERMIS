Ext.define('FHD.view.comm.theme.TipPanel', {
	extend : 'Ext.container.Container',
    alias: 'widget.tippanel',
    style:'padding:0px 0px 5px 0px',   
  initComponent: function() {
	  var me = this;
//	   Ext.apply(me, {
//            
//        });
	  me.callParent(arguments);
  },
    
    //上部分图表
  categoryPanel : function(data,type){
   	var me = this;
   	me.removeAll();
  	if(data!=null){
   		if(type=='1'){
   			
   			var xml_1='';
	    	var xml_2='';
	    	var xml_3='';
	    	var chartType_1='';
	    	var chartType_2='';
	    	var chartType_3='';
	    	for(var i = 0;i<data.length;i++){
	    		if(data[i].positionName=='1.1'){
	    			xml_1 = data[i].xmlMap;
	    			chartType_1 = data[i].chartType;
	    		}if(data[i].positionName=='1.2'){
	    			xml_2 = data[i].xmlMap;
	    			chartType_2 = data[i].chartType;
	    		}if(data[i].positionName=='1.3'){
	    			xml_3 = data[i].xmlMap;
	    			chartType_3 = data[i].chartType;
	    		}
	    	}
	    	
	        	
	        		
		    var upleftChart = Ext.create('FHD.ux.FusionChartPanel',{
				border:true,
				style:'padding:5px 5px 0px 5px',
				chartType:chartType_1,
				flex:0.3,
				//id : 'upleftChart',
				title:'',
				width:300,
				xmlData:xml_1
			});
	    	
	    	var uprightChart = Ext.create('FHD.ux.FusionChartPanel',{
	    		border:true,
				style:'padding:5px 5px 0px 0px',
				chartType:chartType_2,
				//id : 'uprightChart',
				title:'',
				width:300,
				flex:0.7,
				xmlData:xml_2
			}); 
		
	    	var down = Ext.create('FHD.ux.FusionChartPanel',{
				border:true,
				style:'padding:5px 5px 5px 5px',
				chartType:chartType_3,
				flex:2,
				//id : 'down',
				title:'',
				width:600,
				xmlData:xml_3
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
		        	down
	        	]
			});
	
            me.add(upRegion);
            me.add(downRegion);
            me.doLayout();
   		}else if(type=='2'){
   
	    	var xml_1='';
	    	var xml_2='';
	    	var xml_3='';
	    	var xml_4='';
	    	var chartType_1='';
	    	var chartType_2='';
	    	var chartType_3='';
	    	var chartType_4='';
	    	if(data!=null){
			for(var i = 0;i<data.length;i++){
	    		if(data[i].positionName=='2.1'){
	    			xml_1 = data[i].xmlMap;
	    			chartType_1 = data[i].chartType;
	    		}if(data[i].positionName=='2.2'){
	    			xml_2 = data[i].xmlMap;
	    			chartType_2 = data[i].chartType;
	    		}if(data[i].positionName=='2.3'){
	    			xml_3 = data[i].xmlMap;
	    			chartType_3 = data[i].chartType;
	    		}if(data[i].positionName=='2.4'){
	    			xml_4 = data[i].xmlMap;
	    			chartType_4 = data[i].chartType;
	    		}
				}
	    	}
	        		
		    var upleftChart = Ext.create('FHD.ux.FusionChartPanel',{
				border:true,
				style:'padding:5px 5px 0px 5px',
				chartType:chartType_1,
				flex:0.5,
				//id : 'upleftChart',
				title:'',
				width:300,
				xmlData:xml_1
			});
	
	    		
	    	
	    	var uprightChart = Ext.create('FHD.ux.FusionChartPanel',{
	    		border:true,
				style:'padding:5px 5px 0px 0px',
				chartType:chartType_2,
				//id : 'uprightChart',
				title:'',
				width:300,
				flex:0.5,
				xmlData:xml_2
			}); 
		
	    	var downleftChart = Ext.create('FHD.ux.FusionChartPanel',{
				border:true,
				style:'padding:5px 5px 0px 5px',
				chartType:chartType_3,
				flex:0.5,
				//id : 'down',
				title:'',
				width:600,
				xmlData:xml_3
			});
			
			var downrightChart = Ext.create('FHD.ux.FusionChartPanel',{
				border:true,
				style:'padding:5px 5px 0px 0px',
				chartType:chartType_4,
				flex:0.5,
				//id : 'down',
				title:'',
				width:600,
				xmlData:xml_4
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
			me.add(upRegion);
            me.add(downRegion);
            me.doLayout();

	    }else if(type=='3'){
	   
	    	var xml_1='';
	    	var xml_2='';
	    	var xml_3='';
	    	var chartType_1='';
	    	var chartType_2='';
	    	var chartType_3='';
	    	
	    	if(data!=null){
		    	for(var i = 0;i<data.length;i++){
		    		if(data[i].positionName=='3.1'){
		    			xml_1 = data[i].xmlMap;
		    			chartType_1 = data[i].chartType;
		    		}if(data[i].positionName=='3.2'){
		    			xml_2 = data[i].xmlMap;
		    			chartType_2 = data[i].chartType;
		    		}if(data[i].positionName=='3.3'){
		    			xml_3 = data[i].xmlMap;
		    			chartType_3 = data[i].chartType;
		    		}
		    	}
	    	}
	        	
		    var upChart = Ext.create('FHD.ux.FusionChartPanel',{
				border:true,
				style:'padding:5px 5px 0px 0px',
				chartType:chartType_2,
				flex:0.5,
				//id : 'upleftChart',
				title:'',
				width:300,
				xmlData:xml_2
			});
	
	    		
	    	
	    	var downChart = Ext.create('FHD.ux.FusionChartPanel',{
	    		border:true,
				style:'padding:5px 5px 5px 0px',
				chartType:chartType_3,
				//id : 'uprightChart',
				title:'',
				width:300,
				flex:0.5,
				xmlData:xml_3
			}); 
		
	    	var leftRegion = Ext.create('FHD.ux.FusionChartPanel',{
				border:true,
				style:'padding:5px 5px 5px 5px',
				chartType:chartType_1,
				flex:2,
				//id : 'down',
				title:'',
				width:600,
				height : 640,
				xmlData:xml_1
			});
	    		
	    	var rightRegion = Ext.create('Ext.container.Container',{
	        	flex:2,
	        	//id:'layoutupRegion',
	        	layout: {
					type: 'vbox',
		        	align:'stretch'
		        },
	        	items:[
		        	upChart,
		        	downChart
	        	]
			});
			var all= Ext.create('Ext.container.Container',{
//	        	flex:2,
	        	//id:'layoutupRegion',
	        	layout: {
					type: 'hbox',
		        	align:'stretch'
		        },
	        	items:[
		        	leftRegion,
		        	rightRegion
	        	]
			});
	            	
//	        me.add(leftRegion);
//	        me.add(rightRegion);
			me.add(all);
	        me.doLayout();
	
	  	  }
	   		
   	}

    }
    
});
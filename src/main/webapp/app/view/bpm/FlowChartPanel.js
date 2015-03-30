Ext.define('FHD.view.bpm.FlowChartPanel', {
    extend: 'Ext.panel.Panel',
	alias: 'widget.FlowChartPanel',
	
	flowChartImg:null,
	jbpmHistProcinstId:"",
	processInstanceId:"",
    autoScroll:true,
    toDoTaskACInit:function(){
		var me=this;
		var body=jQuery("#"+me.id + '-body');
		body.css("position","inherit");
        FHD.ajax({
            url: __ctxPath + "/jbpm/toDoTaskAC.f",
            params: {processInstanceId:me.processInstanceId},
            callback: function (activityCoordinatesList) {
            	var n=activityCoordinatesList.length;
				for (var i = 0; i < n; i++) {
					var activityCoordinates=activityCoordinatesList[i];
					var height=activityCoordinates.height;
					var width=activityCoordinates.width;
					var left=activityCoordinates.x;
					var top=activityCoordinates.y;
					var div=jQuery("<div style='z-index:1;position:absolute;border-style:solid;border-color:red;height:"+height+"px;width:"+width+"px;left:"+left+"px;top:"+top+"px;'></div>");
					body.append(div);
				}
            }
    	});
    },
    initComponent: function() {
        var me = this;
        me.flowChartImg=Ext.create("Ext.Img",{
        	src:__ctxPath + "/jbpm/flowChart.f?jbpmHistProcinstId="+me.jbpmHistProcinstId
        });
        Ext.applyIf(me, {
            items: [
            	me.flowChartImg
            ],
            listeners:{
				afterrender : function() {
					me.toDoTaskACInit();
				}
			}
        });
        me.callParent(arguments);
		
    }
});
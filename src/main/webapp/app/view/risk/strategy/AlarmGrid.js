Ext.define("FHD.view.risk.strategy.AlarmGrid",{
	extend:'Ext.grid.Panel',//FHD.ux.GridPanel
	alias:'widget.riskAlarmGrid',
	initComponent:function(){
		var me = this;
		
		// 数据源
		me.url = "kpi/kpistrategymap/findNewestAlarmById.f";
		var fields = ["level","range"];
		me.store = Ext.create('Ext.data.Store',{
        	fields:fields,
        	remoteSort:true,
        	proxy: {
		        type: 'ajax',
		        url:me.url,
		        reader: {
		            type : 'json',
		            root : 'datas'
		        }
		    }
		});
		
		//列表列
    	me.gridColums =[
    		{header: '等级' ,	dataIndex: 'level',sortable: true,flex : 1,
	            renderer: function (v) {
	                var color = "";
	                var text = "";
	                var icon = "";
	                switch (v) {
	                    case '0alarm_startus_h':
	                        /* 对应数据字典中的主键 */
	                        color = 'symbol_4_sm';
	                        icon = "symbol-4-sm";
	                        text = FHD.locale.get("fhd.alarmplan.form.hight");
	                        break;
	                    case '0alarm_startus_m':
	                        color = 'symbol_5_sm';
	                        icon = "symbol-5-sm";
	                        text = FHD.locale.get("fhd.alarmplan.form.min");
	                        break;
	                    case '0alarm_startus_l':
	                        color = 'symbol_6_sm';
	                        icon = "symbol-6-sm";
	                        text = FHD.locale.get("fhd.alarmplan.form.low");
	                        break;
	                }
	                return color != "" ? '<img src="' + __ctxPath + '/images/icons/' + color + '.gif">' : "";
	            }
    		},
    		{header: "区间" ,	dataIndex: 'range',sortable: true,flex : 1,
    			renderer: function (v) {
                    var rangestr = v;
                    if (v.indexOf("<") != -1) {
                        rangestr = v.replace("<", "&lt;");
                    }
                    return rangestr;
                }
    		}
    	];
    	
		Ext.apply(me, {
			region:'south',
            border:true,
            rowLines:true,//显示横向表格线
            checked: false, //复选框
            autoScroll:true,
            store:me.store,
            columns:me.gridColums
        });
		me.callParent(arguments);

	}
});
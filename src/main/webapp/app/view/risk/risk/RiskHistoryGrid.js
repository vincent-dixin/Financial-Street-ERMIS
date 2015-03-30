Ext.define('FHD.view.risk.risk.RiskHistoryGrid', {
    extend: 'FHD.ux.layout.GridPanel',//FHD.ux.GridPanel
    alias: 'widget.riskhistorygrid',
    //title:'历史记录',
    url: '',
    // kpi列表Url地址
    cols: [],
    // kpi列表显示的列
    tbarItems: [],
    // kpi列表上方工具条
    border: true,
    // 默认不显示border
    checked: true,
    // 是否可以选中
    initComponent: function() {
        var me = this;
        Ext.apply(me, {
        	url : __ctxPath + "/risk/findRiskAdjustHistoryById.f",
            extraParams:{
            	riskId:me.riskId
            }
        });
        me.cols = [
		{
			dataIndex:'id',
			hidden:true
		},
        {
            //cls: 'grid-icon-column-header grid-statushead-column-header',
            header: "状态",//"<span data-qtitle='' data-qtip='状态'>&nbsp&nbsp&nbsp&nbsp" + "</span>",
            dataIndex: 'assessementStatus',
            sortable: true,
            width: 40,
            renderer: function(v) {
                var color = "";
                var display = "";
                if (v == "icon-ibm-symbol-4-sm") {
                    color = "symbol_4_sm";
                    display = FHD.locale.get("fhd.alarmplan.form.hight");
                } else if (v == "icon-ibm-symbol-6-sm") {
                    color = "symbol_6_sm";
                    display = FHD.locale.get("fhd.alarmplan.form.low");
                } else if (v == "icon-ibm-symbol-5-sm") {
                    color = "symbol_5_sm";
                    display = FHD.locale.get("fhd.alarmplan.form.min");
                } else {
                    v = "icon-ibm-underconstruction-small";
                    display = "无";
                }
                return "<div style='width: 32px; height: 19px; background-repeat: no-repeat;" + "background-position: center top;' data-qtitle='' " + "class='" + v + "'  data-qtip='" + display + "'>&nbsp</div>";
            }
        },
        {
            //cls: 'grid-icon-column-header grid-trendhead-column-header',
            header: "趋势",//"<span data-qtitle='' data-qtip='趋势'>&nbsp&nbsp&nbsp&nbsp" + "</span>",
            dataIndex: 'etrend',
            sortable: true,
            width:40,
            renderer: function (v) {
            	var color = "";
                var display = "";
                if (v == "icon-ibm-icon-trend-rising-positive") {
                    color = "icon_trend_rising_positive";
                    display = FHD.locale.get("fhd.kpi.kpi.prompt.positiv");
                } else if (v == "icon-ibm-icon-trend-neutral-null") {
                    color = "icon_trend_neutral_null";
                    display = FHD.locale.get("fhd.kpi.kpi.prompt.flat");
                } else if (v == "icon-ibm-icon-trend-falling-negative") {
                    color = "icon_trend_falling_negative";
                    display = FHD.locale.get("fhd.kpi.kpi.prompt.negative");
                }
                return "<div style='width: 32px; height: 19px; background-repeat: no-repeat;" +
                    "background-position: center top;' data-qtitle='' " +
                    "class='" + v + "'  data-qtip='" + display + "'></div>";
            }
        },
        {
            header: '时间段',
            dataIndex: 'dateRange',
            //sortable: true,
            align: 'right',
            flex: 1
        },
        {
            header: '发生可能性',
            dataIndex: 'probability',
            sortable: true,
            align: 'right',
            flex: 1
        },
        {
            header: '影响程度',
            dataIndex: 'impacts',
            sortable: true,
            align: 'right',
            flex: 1
        }
        ];
        me.callParent(arguments);
    },
    reLoadData:function(riskId){
    	var me = this;
    	me.riskId = riskId;
    	me.store.proxy.extraParams.riskId = riskId;
		me.store.load();
    }

});
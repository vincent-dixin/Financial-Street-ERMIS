Ext.define('pages.chart.kpi.StructuralAnalysisGrid', {
    extend: 'FHD.ux.GridPanel',
    alias: 'widget.structuralanalysisgrid',
    url: '', // kpi列表Url地址
    cols: [], // kpi列表显示的列
    //isDisplayPreResult: false,
    //tbarItems: [], // kpi列表上方工具条
    border: false, // 默认不显示border
    checked: false, // 是否可以选中
    nameLink: true,
    pagable:false,
    type: '',

    initComponent: function () {
        var me = this;
        
        var store = Ext.create('Ext.data.JsonStore', {
            fields: ['name', 'data1', 'data2', 'data3', 'data4', 'data5'],
            data: [{
                'name': 'one',
                'data1': 10,
                'data2': 12,
                'data3': 14,
                'data4': 8,
                'data5': 13
            }, {
                'name': 'two',
                'data1': 7,
                'data2': 8,
                'data3': 16,
                'data4': 10,
                'data5': 3
            }, {
                'name': 'three',
                'data1': 5,
                'data2': 2,
                'data3': 14,
                'data4': 12,
                'data5': 7
            }, {
                'name': 'four',
                'data1': 2,
                'data2': 14,
                'data3': 6,
                'data4': 1,
                'data5': 23
            }, {
                'name': 'five',
                'data1': 4,
                'data2': 4,
                'data3': 36,
                'data4': 13,
                'data5': 33
            }]
        });

        var lineChart = Ext.create('Ext.chart.Chart', {
            animate: false,
            store: store,
            axes: [{
                type: 'Numeric',
                position: 'left',
                fields: ['data1', 'data2'],
                label: {
                    renderer: Ext.util.Format.numberRenderer('0,0')
                },
                grid: true,
                minimum: 0
            }, {
                type: 'Category',
                position: 'bottom',
                fields: ['name']
            }],
            series: [{
                type: 'line',
                highlight: {
                    size: 7,
                    radius: 7
                },
                axis: 'left',
                xField: 'name',
                yField: 'data1',
                markerConfig: {
                    type: 'cross',
                    size: 4,
                    radius: 4,
                    'stroke-width': 0
                }
            }, {
                type: 'line',
                highlight: {
                    size: 7,
                    radius: 7
                },
                axis: 'left',
                fill: true,
                xField: 'name',
                yField: 'data2',
                markerConfig: {
                    type: 'circle',
                    size: 4,
                    radius: 4,
                    'stroke-width': 0
                }
            }]
        });
        
        me.cols = [
			{
			    cls: 'grid-icon-column-header grid-statushead-column-header',
			    header: "<span data-qtitle='' data-qtip='" + FHD.locale.get("fhd.sys.planEdit.status") + "'>&nbsp&nbsp&nbsp&nbsp" + "</span>",
			    dataIndex: 'assessmentStatus',
			    sortable: true,
			    width:40,
			    renderer: function (v) {
			        var color = "";
			        var display = "";
			        if (v == "icon-ibm-symbol-4-sm") {
			            color = "symbol_4_sm";
			            display = FHD.locale.get("fhd.alarmplan.form.low");
			        } else if (v == "icon-ibm-symbol-6-sm") {
			            color = "symbol_6_sm";
			            display = FHD.locale.get("fhd.alarmplan.form.hight");
			        } else if (v == "icon-ibm-symbol-5-sm") {
			            color = "symbol_5_sm";
			            display = FHD.locale.get("fhd.alarmplan.form.min");
			        } else {
			            v = "icon-ibm-underconstruction-small";
			            display = "无";
			        }
			        return "<div style='width: 32px; height: 19px; background-repeat: no-repeat;" +
			            "background-position: center top;' data-qtitle='' " +
			            "class='" + v + "'  data-qtip='" + display + "'>&nbsp</div>";
			    }
			}, {
			    cls: 'grid-icon-column-header grid-trendhead-column-header',
			    header: "<span data-qtitle='' data-qtip='" + FHD.locale.get("fhd.kpi.kpi.form.directionto") + "'>&nbsp&nbsp&nbsp&nbsp" + "</span>",
			    dataIndex: 'directionstr',
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
	            header: FHD.locale.get('fhd.kpi.kpi.form.name'),
	            xtype: 'tipcolumn',
	            tips: {
	                width: 600,
	                height: 200,
	                layout: 'fit',
	                items: [lineChart],
	                renderer: function (c, rowIndex, cellIndex, tooltip) {
	                    var data = [{
	                        'name': 'one',
	                        'data2': 12,
	                        'data1': 14,
	                        'data4': 8,
	                        'data5': 13
	                    }, {
	                        'name': 'two',
	                        'data2': 8,
	                        'data1': 16,
	                        'data4': 10,
	                        'data5': 3
	                    }, {
	                        'name': 'three',
	                        'data2': 2,
	                        'data1': 14,
	                        'data4': 12,
	                        'data5': 7
	                    }, {
	                        'name': 'four',
	                        'data2': 14,
	                        'data1': 6,
	                        'data4': 1,
	                        'data5': 23
	                    }, {
	                        'name': 'five',
	                        'data2': 4,
	                        'data1': 36,
	                        'data4': 13,
	                        'data5': 33
	                    }]
	                    store.loadData(data);
	                }
	            },
	            dataIndex: 'name',
	            sortable: true,
	            flex: 2,
	            renderer: function (v) {
	                var type = me.type;
	                if(type){
	                	return "<a href='javascript:void(0)' onclick=\"" + type + "_gatherResultFun('" + v + "')\" >" + v + "</a>";
	                }else{
	                	return v;
	                }
            	}
            },
            {
                header: '实际值',
                dataIndex: 'finishValue',
                sortable: false,
                flex: 0.8,
                align: 'right'
            },
            {
            	dataIndex:'kid',
            	hidden:true
            },
            {
            	dataIndex:'id',
            	hidden:true
            }
        ];

        Ext.applyIf(me, {
            cols: me.cols,
            url: me.url,
            tbarItems: me.tbarItems,
            border: me.border,
            checked: me.checked
        });

        me.callParent(arguments);
    }
});

Ext.define('FHD.ux.kpi.KpiGridPanel', {
    extend: 'FHD.ux.GridPanel',
    alias: 'widget.fhdkpigrid',
    url: '', // kpi列表Url地址
    cols: [], // kpi列表显示的列
    isDisplayPreResult: false,
    tbarItems: [], // kpi列表上方工具条
    border: true, // 默认不显示border
    checked: true, // 是否可以选中
    nameLink: true,
    type: '',
    destoryflag:'true',
    xmlMap : null, //针对不同指标的效果图map类型(KEY:指标ID,VALUE:指标效果图)
    
    _insert: function (index, item) {
        if (index < 0) return;
        if (index > this.cols.length) return;
        for (var i = this.cols.length - 1; i >= index; i--) {
            this.cols[i + 1] = this.cols[i];
        }
        this.cols[index] = item;
    },
    
    //获取当前年份
    getYear : function(){
    	var myDate = new Date();
    	var year = myDate.getFullYear();
    	return year;
    },
    
    initComponent: function () {
        var me = this;
        
        var chartPanel = Ext.create('FHD.ux.FusionChartPanel',{
    		border:false,
    		width: 300,
            height: 220,
			chartType:'MSColumnLine3D',
			xmlData : ''
		});
        
        me.cols = [{
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
                    display = FHD.locale.get("fhd.alarmplan.form.hight");
                } else if (v == "icon-ibm-symbol-6-sm") {
                    color = "symbol_6_sm";
                    display = FHD.locale.get("fhd.alarmplan.form.low");
                } else if (v == "icon-ibm-symbol-5-sm") {
                    color = "symbol_5_sm";
                    display = FHD.locale.get("fhd.alarmplan.form.min");
                } else if(v=="icon-ibm-symbol-safe-sm"){
                	 display = "安全";
                } 
                else {
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
        }, {
            header: FHD.locale.get('fhd.kpi.kpi.form.name'),
            xtype: 'tipcolumn',
            tips: {
                width: 300,
                height: 200,
                //layout: 'fit',
                items : [chartPanel],
                renderer: function (c, rowIndex, cellIndex, tooltip) {
                	me.cols[2].tips.items[0].loadXMLData(me.xmlMap[me.items.items[0].store.data.items[rowIndex].data.id]);
                }
            },
            dataIndex: 'name',
            sortable: true,
            flex: 3,
            renderer: function (v) {
	                var type = me.type;
	                if(type){
	                	return "<a href='javascript:void(0)' onclick=\"Ext.getCmp('" + type + "').gatherResultFun('" + v + "')\" >" + v + "</a>";
	                }
	                else{
	                	return v;
	                }
            	}
            },
            {
                header: FHD.locale.get('fhd.kpi.kpi.form.finishValue'),
                dataIndex: 'finishValue',
                sortable: true,
                flex: 0.8,
                align: 'right'
            },
            {
                header: FHD.locale.get('fhd.kpi.kpi.form.targetValue'),
                dataIndex: 'targetValue',
                sortable: true,
                flex: 0.8,
                align: 'right'
            },
            {
                header: FHD.locale.get('fhd.kpi.kpi.form.assessmentValue'),
                dataIndex: 'assessmentValue',
                sortable: true,
                flex: 0.8,
                align: 'right'
            },
            {
                header: FHD.locale.get('fhd.kpi.kpi.form.dateRange'),
                dataIndex: 'dateRange',
                sortable: true,
                flex:2,
                renderer: function (v) {
                    return "<div data-qtitle='' data-qtip='" + v + "'>" + v + "</div>";
                }
            },
            {
                header: FHD.locale.get('fhd.sys.planMan.start'),
                dataIndex: 'kpistatus',
                sortable: false,
                flex: 0.5,
                renderer: function (v) {
                	if("0yn_y"==v){
                		return "<image src='images/icons/state_ok.gif' title='"+FHD.locale.get('fhd.sys.planMan.start')+"'/>";
                	}
                	if("0yn_n"==v){
                		return "<image src='images/icons/state_error.gif' title='"+FHD.locale.get('fhd.sys.planMan.stop')+"'/>";
                	}
                }
            }
            
            ];

        if (me.isDisplayPreResult) {
            var preCol = {
                header:  FHD.locale.get('fhd.kpi.kpi.form.prefinishValue'),
                dataIndex: 'preFinishValue',
                sortable: true,
                flex: 0.8,
                align: 'right'
            };
            var preYearCol = {
                header: FHD.locale.get('fhd.kpi.kpi.form.preYearfinishValue'),
                dataIndex: 'preYearFinishValue',
                sortable: true,
                flex: 0.8,
                align: 'right'
            };
            me._insert(4, preCol);
            me._insert(5, preYearCol);
        }
        
        if(!me.isDisplayPreResult&&me.type=="scorecardkpigrid"){
        	var belongKpiCol = {
    			header:FHD.locale.get('fhd.kpi.grid.belongKpi'),
                dataIndex: 'belongKpi',
                sortable: false,
                flex: 1,
                renderer: function (v) {
                	var text = "";
                    if("1"==v){
	                	text = FHD.locale.get("fhd.kpi.grid.createkpi");
	                }
	                else if("0"==v){
	                	text = FHD.locale.get("fhd.kpi.grid.relakpi");
	                }
                    return "<div data-qtitle='' data-qtip='" + text + "'>" + text + "</div>";
                }
        	}
        	me._insert(7,belongKpiCol);
        }
        

        Ext.apply(me, {
            cols: me.cols,
            url: me.url,
            tbarItems: me.tbarItems,
            border: me.border,
            checked: me.checked
        });
        
        me.callParent(arguments);
        me.store.on('refresh', function (store, options){
        	if(store.data.length != 0){
        		if(me.body != undefined){
        			me.body.mask("读取中...","x-mask-loading");
        		}
        	}
        	
        	var kpiId = "";
        	var yearId = "";
        	var yearIdTemp = new Array();
        	for(var i = 0; i < store.data.length; i++){
        		kpiId += "'" + store.data.items[i].data.id + "',";
        	}
        	
        	var paraobj = {};
        	paraobj.eType = '0frequecy_all';
            paraobj.kpiId = kpiId + '!@#$';
            paraobj.isNewValue = FHD.data.isNewValue
            if(FHD.data.yearId == ''){
            	paraobj.year = me.getYear();
            }else{
            	paraobj.year = FHD.data.yearId;
            }
            
            FHD.ajax({
                url: __ctxPath + '/kpi/kpi/createtable.f?edit=false',
                params: {
                	condItem: Ext.JSON.encode(paraobj)
                },
                callback: function (data) {
                    if (data && data.success) {
                    	me.xmlMap = data.xmlMap;
                    	if(me.store.data.length != 0){
                    		if(me.body != undefined){
                    			me.body.unmask();
                    		}
        	        	}
                    	
                    }
                }
            });
        });  
        }
    });
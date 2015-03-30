Ext.define('FHD.view.kpi.chart.TrendGrid', {
    extend: 'FHD.ux.GridPanel',
    alias: 'widget.trendgrid',
    
    url : __ctxPath + "/kpi/category/findcategoryrelakpihistorydatas.f",
    extraParams:{
    	id : '',
    	year : FHD.data.yearId,
    	month : FHD.data.monthId,
    	quarter : FHD.data.quarterId,
    	week : FHD.data.weekId,
    	eType : FHD.data.eType,
    	isNewValue : FHD.data.isNewValue
    },
    cols: [], // kpi列表显示的列
    isDisplayPreResult: false,
    tbarItems: [], // kpi列表上方工具条
    border: false, // 默认不显示border
    checked: false, // 是否可以选中
    nameLink: true,
    pagable:false,
    type: '',
    destoryflag:'true',
    xmlMap : null, //针对不同指标的效果图map类型(KEY:指标ID,VALUE:指标效果图)

    initComponent: function () {
        var me = this;
        
        var chartPanel = Ext.create('FHD.ux.FusionChartPanel',{
    		border:false,
    		width: 300,
            height: 200,
			chartType:'MSColumnLine3D',
			xmlData : ''
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
			            display = FHD.locale.get('fhd.common.none');
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
	                width: 300,
	                height: 200,
	                items : [chartPanel],
	                renderer: function (c, rowIndex, cellIndex, tooltip) {
	                	me.cols[2].tips.items[0].loadXMLData(me.xmlMap[me.items.items[0].store.data.items[rowIndex].data.kid]);
	                }
	            },
	            dataIndex: 'name',
	            sortable: true,
	            flex: 2,
	            renderer: function (v) {
	                var type = me.type;
	                if(type){
	                	return "<a href='javascript:void(0)' onclick=\"Ext.getCmp('" + type + "').gatherResultFun('" + v + "')\" >" + v + "</a>";
	                }else{
	                	return v;
	                }
            	}
            },
            {
                header: '12'+FHD.locale.get('fhd.sys.planEdit.month'),
                dataIndex: 'decemberValue',
                sortable: true,
                flex: 0.8,
                align: 'right'
            },
            {
                header: '11'+FHD.locale.get('fhd.sys.planEdit.month'),
                dataIndex: 'novemberValue',
                sortable: true,
                flex: 0.8,
                align: 'right'
            },
            {
                header: '10'+FHD.locale.get('fhd.sys.planEdit.month'),
                dataIndex: 'octoberValue',
                sortable: true,
                flex: 0.8,
                align: 'right'
            },
            {
                header: '9'+FHD.locale.get('fhd.sys.planEdit.month'),
                dataIndex: 'septemberValue',
                sortable: true,
                flex: 0.8,
                align: 'right'
            },
            {
                header: '8'+FHD.locale.get('fhd.sys.planEdit.month'),
                dataIndex: 'aguestValue',
                sortable: true,
                flex: 0.8,
                align: 'right'
            },
            {
                header: '7'+FHD.locale.get('fhd.sys.planEdit.month'),
                dataIndex: 'julyValue',
                sortable: true,
                flex: 0.8,
                align: 'right'
            },
            {
                header: '6'+FHD.locale.get('fhd.sys.planEdit.month'),
                dataIndex: 'juneValue',
                sortable: true,
                flex: 0.8,
                align: 'right'
            },
            {
                header: '5'+FHD.locale.get('fhd.sys.planEdit.month'),
                dataIndex: 'mayValue',
                sortable: true,
                flex: 0.8,
                align: 'right'
            },
            {
                header: '4'+FHD.locale.get('fhd.sys.planEdit.month'),
                dataIndex: 'aprilValue',
                sortable: true,
                flex: 0.8,
                align: 'right'
            },
            {
                header: '3'+FHD.locale.get('fhd.sys.planEdit.month'),
                dataIndex: 'marchValue',
                sortable: true,
                flex: 0.8,
                align: 'right'
            },
            {
                header: '2'+FHD.locale.get('fhd.sys.planEdit.month'),
                dataIndex: 'februaryValue',
                sortable: true,
                flex: 0.8,
                align: 'right'
            },
            {
                header: '1'+FHD.locale.get('fhd.sys.planEdit.month'),
                dataIndex: 'januaryValue',
                sortable: true,
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
            tbarItems: me.tbarItems,
            border: me.border,
            checked: me.checked
        });

        me.callParent(arguments);
        
        me.store.on('refresh', function (store, options){
        	if(store.data.length != 0){
        		if(me.body != undefined){
        			me.body.mask(FHD.locale.get('fhd.common.opWait'),"x-mask-loading");
        		}
        	}
        	
        	var kpiId = "";
        	var yearId = "";
        	var yearIdTemp = new Array();
        	for(var i = 0; i < store.data.length; i++){
        		kpiId += "'" + store.data.items[i].data.kid + "',";
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
    },
    //获取当前年份
    getYear : function(){
    	var myDate = new Date();
    	var year = myDate.getFullYear();
    	return year;
    }
});
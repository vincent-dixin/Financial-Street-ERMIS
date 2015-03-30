Ext.define('FHD.view.kpi.scorecard.ScorecardHistoryGrid', {
    extend: 'FHD.ux.EditorGridPanel',
    alias: 'widget.scorecardhistorygrid',
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
        	url : __ctxPath + "/category/findrelaassessresultsbysome.f",
            extraParams:{
            	objectId:me.objectId,
            	type:me.type
            },
            tbarItems:[
       				{text : FHD.locale.get('fhd.common.save'),id:'historySaveId',iconCls: 'icon-save',handler:function(){me.save(me)}, disabled : true, scope : this}
       			]
        });
        me.cols = [
		{
			dataIndex:'id',
			hidden:true
		},
        {
            cls: 'grid-icon-column-header grid-statushead-column-header',
            header: "<span data-qtitle='' data-qtip='" + FHD.locale.get("fhd.sys.planEdit.status") + "'>&nbsp&nbsp&nbsp&nbsp" + "</span>",
            dataIndex: 'assessmentStatusStr',
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
            header: FHD.locale.get('fhd.kpi.kpi.form.assessmentValue'),
            dataIndex: 'assessmentValue',
            sortable: true,
            flex:1,
            align: 'right',
            editor: {
                xtype: 'numberfield',
                maxValue: 100,
                allowDecimals: true,
                nanText: FHD.locale.get('fhd.strategymap.strategymapmgr.prompt.inputNum'),
                step: 0.5
            }
        },
        {
            header: FHD.locale.get('fhd.kpi.kpi.form.dateRange'),
            dataIndex: 'dateRange',
            sortable: true,
            align: 'right',
            flex: 1,
            renderer: function(v) {
                return "<div data-qtitle='' data-qtip='" + v + "'>" + v + "</div>";
            }
        }
        ];
        me.callParent(arguments);
        me.on('selectionchange',function(){me.onchange(me)});//选择记录发生改变时改变按钮可用状态
    },
    save:function(me){
    	var rows = me.store.getModifiedRecords();
    	var jsobj = {type:me.type,objectId:me.objectId};
    	jsobj.datas=[];
		Ext.each(rows,function(item){
			jsobj.datas.push(item.data);
		});
		if(jsobj.datas.length>0){
			FHD.ajax({
				url : __ctxPath + "/category/modifiedrelaassessresult.f",
				params : {
					modifiedRecord:Ext.encode(jsobj)
				},
				callback : function(data){
					if(data){
						Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
						me.store.load();
					}
				}
			});
		}
		me.store.commitChanges();
    },
    onchange:function(me){
    	me.down('#historySaveId').setDisabled(me.getSelectionModel().getSelection().length === 0);
    },
    reLoadData:function(objectId,type){
    	var me = this;
    	me.objectId = objectId;
    	me.type = type;
    	me.store.proxy.extraParams.objectId = objectId;
    	me.store.proxy.extraParams.type = type;
		me.store.load();
    }

});
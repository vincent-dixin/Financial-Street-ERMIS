Ext.define('FHD.view.comm.formula.FormulaLog', {
    extend: 'FHD.ux.GroupGridPanel',
    alias: 'widget.formulalog',
    
	url: __ctxPath + '/formula/findFormulaLogList.f',
	
	storeGroupField: 'objectColumn',
    
	initComponent: function() {
    	var me = this;
    	
    	//Ext.QuickTips.init();
    	
    	/*var store = Ext.create('Ext.data.JsonStore', {
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
        });*/
    	
    	
    	Ext.apply(me,{
    		tbarItems: [
                {
	                tooltip: FHD.locale.get('fhd.formula.batchDeleteFormulaLog'),
	                iconCls: 'icon-del',
	                text:FHD.locale.get('fhd.common.del'),
	                //id: 'formulalog_delete' + formulalog_paramdc,
	                handler: function(){
	                	me.delBatchFun(me);
	                },
	                scope: this
	            }
            ],
    		cols: [
				{
				    cls: 'grid-icon-column-header grid-statushead-column-header',
				    header: "<span data-qtitle='' data-qtip='" + FHD.locale.get('fhd.formula.calculate')+FHD.locale.get("fhd.sys.planEdit.status") + "'>&nbsp;&nbsp;&nbsp;&nbsp;" + "</span>",
				    dataIndex: 'failureType',
				    sortable: true,
				    flex:1,
				    renderer: function (v) {
				        var classStyle = "";
				        var display = "";
				        if('success' == v){
				        	classStyle = "icon-accept";
				            display = FHD.locale.get('fhd.formula.calculate')+FHD.locale.get("fhd.common.success");
				        }else if('failure' == v){
				        	classStyle = "icon-cancel";
				            display = FHD.locale.get('fhd.formula.calculate')+FHD.locale.get("fhd.common.failure");
				        }
				        
				        return "<div style='width: 32px; height: 19px; background-repeat: no-repeat;" +
				            "background-position: center top;' data-qtitle='' " +
				            "class='" + classStyle + "'  data-qtip='" + display + "'>&nbsp</div>";
				    }
				},
				{
				    header: FHD.locale.get('fhd.formula.objectName'),
				    /*xtype: 'tipcolumn',
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
				    },*/
				    dataIndex: 'objectName',
				    sortable: true,
				    flex: 3
				},
				{
				    header: FHD.locale.get('fhd.formula.formula'),
				    dataIndex: 'formulaContent',
				    sortable: true,
				    flex: 10,
				    renderer:function(value,metaData,record,colIndex,store,view) { 
						metaData.tdAttr = 'data-qtip="'+value+'"';
						return value;  
					}
				},
				{
				    header: FHD.locale.get('fhd.formula.formulaType'),
				    dataIndex: 'objectColumn',
				    sortable: true,
				    flex: 1,
				    renderer: function (v) {
				    	if('targetValueFormula' == v){
				    		return FHD.locale.get('fhd.formula.kpi')+FHD.locale.get('fhd.formula.targetValue')+FHD.locale.get('fhd.formula.formula');
				    	}else if('resultValueFormula' == v){
				    		return FHD.locale.get('fhd.formula.kpi')+FHD.locale.get('fhd.formula.resultValue')+FHD.locale.get('fhd.formula.formula');
				    	}else if('assessmentValueFormula' == v){
				    		return FHD.locale.get('fhd.formula.kpi')+FHD.locale.get('fhd.formula.assessmentValue')+FHD.locale.get('fhd.formula.formula');
				    	}else if('impactsFormula' == v){
				    		return FHD.locale.get('fhd.formula.risk')+FHD.locale.get('fhd.formula.impacts')+FHD.locale.get('fhd.formula.formula');
				    	}else if('probabilityFormula' == v){
				    		return FHD.locale.get('fhd.formula.risk')+FHD.locale.get('fhd.formula.probability')+FHD.locale.get('fhd.formula.formula');
				    	}else if('sm_assessmentValueFormula' == v){
				    		return FHD.locale.get("fhd.strategymap.strategymapmgr.target")+FHD.locale.get('fhd.formula.assessmentValue')+FHD.locale.get('fhd.formula.formula');
				    	}else if('sc_assessmentValueFormula' == v){
				    		return FHD.locale.get("fhd.kpi.categoryroot")+FHD.locale.get('fhd.formula.assessmentValue')+FHD.locale.get('fhd.formula.formula');
				    	}
				    	return v;
				    }
				},
				{
				    header: FHD.locale.get('fhd.formula.failureReason'),
				    dataIndex: 'failureReason',
				    sortable: true,
				    flex: 10,
				    renderer:function(value,metaData,record,colIndex,store,view) { 
						metaData.tdAttr = 'data-qtip="'+value+'"';
						return value;  
					}
				},
				{
				    header: FHD.locale.get('fhd.formula.calculateDate'),
				    dataIndex: 'calculateDate',
				    sortable: true,
				    flex: 4
				},
				{
					header: FHD.locale.get('fhd.formula.owner'),
				    dataIndex: 'empName',
				    sortable: true,
				    flex: 3
				},
				{
				 	header: FHD.locale.get('fhd.formula.operate'),
				    xtype:'actioncolumn',
				    dataIndex: 'id',
				    flex: 2,
				    items: [{
				    	icon: __ctxPath + '/images/icons/delete_icon.gif',
				        tooltip: FHD.locale.get('fhd.formula.singleDeleteFormulaLog'),
				        handler: me.delSingleFun
				    },{
				        icon: __ctxPath + '/images/icons/calculator.png',
				        tooltip: FHD.locale.get('fhd.formula.recalculate'),
				        handler: me.reCalculateFun
				    }]
				},
				{
					dataIndex:'objectType',
					hidden:true
				},
				{
					dataIndex:'objectId',
					hidden:true
				},
		        {
		        	dataIndex:'timePeriodId',
		        	hidden:true
		        }
    		]
    	});
        
    	me.callParent(arguments);
	},
	//批量删除
	delBatchFun: function(grid) {
		var me = this;
		
    	var selections = grid.getSelectionModel().getSelection();
    	if(0 == selections.length){
    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.msgDel'));
    	}else{
    		Ext.MessageBox.show({
                title: FHD.locale.get('fhd.common.delete'),
                width: 260,
                msg: FHD.locale.get('fhd.common.makeSureDelete'),
                buttons: Ext.MessageBox.YESNO,
                icon: Ext.MessageBox.QUESTION,
                fn: function (btn) {
                    if (btn == 'yes') {
                    	var ids='';
                        
                        Ext.Array.each(selections, function (item) {
                            ids += item.get("id") + ",";
                        });

    					if(ids.length>0){
    						ids = ids.substring(0,ids.length-1);
    					}
                        FHD.ajax({
                            url: __ctxPath + '/formula/removeFormulaLogByIds.f',
                            params: {
                                ids: ids
                            },
                            callback: function (data) {
                                if (data) {
                                    me.store.load();
                                    Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
                                }else{
                                	Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateFailure'));
                                }
                            }
                        });
                    }
                }
            });
    	}
    },
    //单条删除
    delSingleFun: function(grid, rowIndex, colIndex) {
    	var me = this;
    	
    	var rec = grid.getStore().getAt(rowIndex);
        var id = rec.get('id');
         
        Ext.MessageBox.show({
            title: FHD.locale.get('fhd.common.delete'),
            width: 260,
            msg: FHD.locale.get('fhd.common.makeSureDelete'),
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.QUESTION,
            fn: function (btn) {
                if (btn == 'yes') {
                    FHD.ajax({
                        url: __ctxPath + '/formula/removeFormulaLogById.f',
                        params: {
                            id: id
                        },
                        callback: function (data) {
                            if (data) {
                            	grid.store.load();
                                Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
                            }else{
                            	Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateFailure'));
                            }
                        }
                    });
                }
            }
        });
    },
    //重新计算
    reCalculateFun: function(grid, rowIndex, colIndex) {
    	var me = this;
    	
    	var rec = grid.getStore().getAt(rowIndex);
        var id = rec.get('id');
        var objectId = rec.get('objectId');
        var objectType = rec.get('objectType');
        var objectColumn = rec.get('objectColumn');
        var timePeriodId = rec.get('timePeriodId');
        var formula = rec.get('formulaContent');
        
        FHD.ajax({
            url: __ctxPath + '/formula/reCalculateFormula.f',
            params: {
            	id: id,
            	objectId: objectId,
                objectType: objectType,
                objectColumn: objectColumn,
                timePeriodId: timePeriodId,
                formula: formula
            },
            callback: function (data) {
                if (data) {
                	grid.store.load();
			      	Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
                }else{
                	Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateFailure'));
                }
            }
        });
    },
    //重新加载数据
    reloadData: function() {
    	var me = this;
    	
    	me.store.load();
    }
});
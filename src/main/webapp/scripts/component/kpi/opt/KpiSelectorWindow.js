Ext.define('FHD.ux.kpi.opt.KpiSelectorWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.kpioptselectorwindow',
    
    width: 800,
    height: 600,
    layout: {
        type: 'border'
    },
    border: true,
    constrain: true,
    modal: true,
    collapsible: true,
    maximizable: true,
    title:  $locale('kpiselectorwindow.title'),
    selectedvalues:[],
	
    setSelectedValue:function(selectedvalues){
    	var me = this;
    	if(selectedvalues.data){
    		for(var i=0;i<selectedvalues.data.length;i++){
        		var item = selectedvalues.data.items[i].data;
        		var insertobj = {};
        		insertobj.id = item.id;
        		insertobj.name = item.name;
        		me.selectedgrid.store.insert(me.selectedgrid.store.count(),insertobj);
        	}
    	}
    },
    
    resetSelectGrid:function(){
    	var me = this;
    	me.selectedgrid.store.removeAll();
    },

    initComponent: function () {
        var me = this;
        
		me.valueTypeStore=Ext.create('Ext.data.Store',{
			fields : ['id', 'name'],
			data : [
			    /*
			    {'id' : 'targetValue','name' : '目标值'},
			    {'id' : 'finishValue','name' : '实际值'},
			    {'id' : 'assessmentValue','name' : '评估值'},
			    {'id' : 'modelValue','name' : '标杆值'},
			    {'id' : 'sameValue','name' : '同比值'},
			    {'id' : 'ratioValue','name' : '环比值'},
			    {'id' : 'assessmentStatus','name' : '告警状态'}
			    */
			    {'id' : '目标值','name' : '目标值'},
			    {'id' : '实际值','name' : '实际值'},
			    {'id' : '评估值','name' : '评估值'},
			    {'id' : '标杆值','name' : '标杆值'},
			    {'id' : '同比值','name' : '同比值'},
			    {'id' : '环比值','name' : '环比值'}
			]
		});
		
        me.tree = Ext.create('FHD.ux.kpi.opt.KpiSelectTree', {
            width: 265,
            region: 'west'
        });
        
        me.kpigrid = Ext.create('FHD.ux.GridPanel', {
        	checked:false,
            border: me.border,
            pagable: true,
            loadMask: true,
            url:  __ctxPath + '/kpi/kpi/findkpibysome.f',
            height: 300,
            cols: [{
                dataIndex: 'id',
                id: 'id',
                width: 0
            }, {
                header: $locale("fhd.pages.test.field.name"),
                dataIndex: 'name',
                sortable: true,
                flex: 1
            }],
            listeners:{
            	select:function(rowModel,record, index){
            		var insertObj = {};
            		var selectedRow  = record.data;
	            	insertObj.id = selectedRow.id;
	            	insertObj.name = selectedRow.name;
	            	//给已选列表中的'值类型'字段设置默认值
	            	//insertObj.valueType = '实际值';
	            	if(!me.selectedgrid.store.getById(selectedRow.id)){
	            		if(!me.multiSelect){
	            			me.selectedgrid.store.removeAll();
	            		}
	            		me.selectedgrid.store.insert(me.selectedgrid.store.count(),insertObj);
	            	}
            	}
        
        		/*,
            	deselect:function(rowModel,record, index){
        			var selectedRow  = record.data;
            		var row = me.selectedgrid.store.getById(selectedRow.id);
        			me.selectedgrid.store.remove(row);
            	}*/
            	
            }
        });
        
        var cellEditing = Ext.create('Ext.grid.plugin.CellEditing', {
	        clicksToEdit: 1
	    });

        me.selectedgrid = Ext.create('Ext.grid.Panel',{
        	loadMask: true,
        	flex: 1,
        	border:me.border,
        	plugins: [cellEditing],
            store: Ext.create('Ext.data.Store', {
                idProperty: 'id',
                fields: ['id','name', 'valueType']
            }),
            columns: [{
                xtype: 'gridcolumn',
                hidden: true,
                dataIndex: 'id'
            }, {
                xtype: 'gridcolumn',
                dataIndex: 'name',
                flex: 3,
                text: $locale("fhd.pages.test.field.name"),
                renderer: function (value, metaData, record, rowIndex, colIndex, store) {
                    return "<div data-qtitle='' data-qtip='" + value + "'>" + value + "</div>";
                }
            }, {
				xtype: 'gridcolumn',
				text:'值类型',
				dataIndex: 'valueType',
				flex:1,
				editor: new Ext.form.ComboBox({
					store :me.valueTypeStore,
					valueField : 'id',
					displayField : 'name',
					selectOnTab: true,
					lazyRender: true,
					typeAhead: true,
					allowBlank : true,
					editable : false
				}),
				renderer:function(value,metaData,record,colIndex,store,view) {
					//metaData.tdAttr = 'style="background-color:#FFFBE6"';
			    	metaData.tdAttr = 'data-qtip="值类型表示指标的基本类型值，包括：目标值/实际值/评估值/标杆值/同比值/环比值等，可按业务需要进行调整" style="background-color:#FFFBE6"';
					var index = me.valueTypeStore.find('id',value);
					var record = me.valueTypeStore.getAt(index);
					if(record){
						return record.data.name;
					}else{
						if(value){
		    				return value;
		    			}else{
		    				metaData.tdAttr = 'data-qtip="值类型表示指标的基本类型值，包括：目标值/实际值/评估值/标杆值/同比值/环比值等，可按业务需要进行调整" style="background-color:#FFFBE6"';
						}
					}
				}
			}, {
                xtype: 'templatecolumn',
                flex: 0.5,
                text: $locale('fhd.common.delete'),
                align: 'center',
                tpl: '<font class="icon-del-min" style="cursor:pointer;">&nbsp&nbsp&nbsp&nbsp</font>',
                listeners: {
                    click: {
                        fn: function (grid, d, i) {
                        	var row = grid.store.getAt(i);
                        	grid.store.removeAt(i);
                        	/*var selections = me.kpigrid.getSelectionModel();
                        	selections.deselect(me.kpigrid.store.getById(row.data.id));*/
                        }
                    }
                }
            }]
        });

        me.centerpanel = Ext.create('Ext.container.Container', {
            flex: 2,
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            region: 'center',
            items: [
			            me.kpigrid,
			            me.selectedgrid
			       ]
        });

        me.buttons = [{
            xtype: 'button',
            text: $locale('fhd.common.confirm'),
            handler: function () {
            	me.onSubmit(me.selectedgrid.store);
                me.close();
            }
        }, {
            xtype: 'button',
            text: $locale('fhd.common.close'),
            style: {
                marginLeft: '10px'
            },
            handler: function () {
                me.close();
            }
        }];

        Ext.applyIf(me, {
            items: [	
            	me.tree,
                {
                	xtype:'container',
                	border:false,
                	layout: {
                        type: 'border'
                    },
                    activeItem: 0,
                	region: 'center',
                	items:[me.centerpanel]
                }
            ]
        });

        me.callParent(arguments);
        me.setSelectedValue(me.selectedvalues);
    }
});
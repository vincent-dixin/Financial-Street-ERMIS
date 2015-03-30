Ext.define('FHD.view.comm.theme.LayoutDataPanel', {
    extend: 'Ext.form.Panel',
    alias: 'widget.layoutdatapanel',
    /**
     * 标示对齐方式
     */
    labelAlign: 'left',

    /**
     * 标示宽度
     */
    labelWidth: 50,

    /**
     * 指标grid
     */
    kpitypeGrid: null,

    /**
     * field 名称
     */
    name:'',
    
    /**
     * field 值
     */
    fieldValue:'',
    
    /**
     * 是否多选
     */
    multiSelect:true,
    
    field:null,
    
    layoutInfoId: null,
    
    type : null,
    
    bodyPadding:'0 3 3 3',
    
    reload:function(){
    	var me = this;
    	me.kpitypeGrid.store.load();
    },
    
    save: function () {
    	var me = this;
    	var analysislayoutform = Ext.getCmp('analysislayoutform');
    	var id = analysislayoutform.layoutInfoId;
    	if(id==null){
    		id = layoutInfoId;
    	}
    	var layoutType = analysislayoutform.layoutType;
    	if(layoutType==null){
    		 layoutType =type;
    	}
    	var position = me.position;
    	me.save_url =__ctxPath+'/ic/theme/savelayoutdetailedset.f';//保存url
    	var form = me.getForm();
    	var selection = me.kpitypeGrid.store;
    	
    	var dataSource = "";
        	for(var i = 0; i < selection.data.length; i++){
        		dataSource += selection.data.items[i].data.id + ",";
        	}
        
        var form = me.getForm();
        var chartType = form.getValues().chartType;
        var length = selection.data.length;
//        if(chartType!=0&&length>1){
//        	Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.kpi.kpi.prompt.editone'));
//            return;
//        }
  	
		FHD.submit({	//修改保存
			form:form,
			url:me.save_url,
			params: {
                dataSource: dataSource,
                position:position,
                layoutInfoId : id,
                layoutType : layoutType
            },
			callback:function(data){
				if(layoutType=='1'){
					Ext.getCmp('analysislayoutpanel').formwindow.close();
					me.kpitypeGrid.store.data.removeAll();
				}if(layoutType=='2'){
					Ext.getCmp('analysislayoutpanel2').formwindow.close();
					me.kpitypeGrid.store.data.removeAll();
				}if(layoutType=='3'){
					Ext.getCmp('analysislayoutpanel3').formwindow.close();
					me.kpitypeGrid.store.data.removeAll();
				}
				Ext.getCmp('analysislayoutform').loadFormById(id,layoutType);
			}
		});
		
    		
    },

    // 初始化方法
    initComponent: function () {
        var me = this;
        var formButtons = [ //表单按钮
            {
                text: FHD.locale.get('fhd.common.confirm'),
                handler: function () {
                    me.save();
                }
            }, {
                text: FHD.locale.get('fhd.common.cancel'),
                handler: function () {
                	if(Ext.getCmp('analysislayoutpanel').formwindow){
               	    	Ext.getCmp('analysislayoutpanel').formwindow.close();
                	}if(Ext.getCmp('analysislayoutpanel2').formwindow){
               	 	    Ext.getCmp('analysislayoutpanel2').formwindow.close();
                	}if(Ext.getCmp('analysislayoutpanel3').formwindow){
               	 		Ext.getCmp('analysislayoutpanel3').formwindow.close();
                	}
                    
                }
            }
        ];
        me.chartType = Ext.widget('combo', {
	            editable: false,
	            id:'themecharttype',
	            labelWidth: 100,
	            multiSelect: false,
	            allowBlank: false,
	            name: 'chartType',
	            fieldLabel:'图表形式', //图表形式
	            labelAlign: 'left',
	            queryMode: 'local',
	            store : [['0', '柱状图'],['1', '饼状图'],['2', '曲线图']],
	            value :'0'
	        });
        //
        var basicfieldSet = Ext.widget('fieldset', {
            xtype: 'fieldset', 
            collapsible: true,
            autoHeight: true,
            autoWidth: true,
//            defaults: {
//                margin: '7 10 10 30',
//                labelWidth: 105
//            },
            layout: {
                type: 'column'
            },
            title: FHD.locale.get('fhd.kpi.kpi.form.basicinfo'),
            items: [me.chartType]
        });

        Ext.create('Ext.data.Store', {
		    storeId:'simpsonsStore',
		    fields:['id', 'name'],
		    proxy: {
		        type: 'memory',
		        reader: {
		            type: 'json',
		            root: 'items'
		        }
		    }
		});

		 me.kpitypeGrid =Ext.create('Ext.grid.Panel', {
		 	selModel:Ext.create('Ext.selection.CheckboxModel',{mode:"SIMPLE"}),
		    store: Ext.data.StoreManager.lookup('simpsonsStore'),
		    columns: [
		        { header: 'id',  dataIndex: 'id', width: 0 },
		        { header: FHD.locale.get('fhd.kpi.kpi.form.name'), dataIndex: 'name', flex: 1 }
		    ],
		    height: 200,
		    width: '*',
		    tbar: [
					{ xtype: 'button' ,
					  iconCls: 'icon-add',
					  handler:function(){
			  			me.addFun();
						}
					},'-',{ xtype: 'button' ,
					  iconCls: 'icon-del',
					  handler:function(){
			  			me.delFun();
						}
					}
			]
		
		});
		
		 var gridfieldSet = Ext.widget('fieldset', {
            xtype: 'fieldset', 
            collapsible: true,
            autoHeight: true,
            autoWidth: true,
//            defaults: {
//                margin: '7 10 10 30',
//                labelWidth: 105
//            },
            layout: {
                type: 'fit'
            },
            title: FHD.locale.get('fhd.kpi.kpi.form.basicinfo'),
            items: [me.kpitypeGrid]
        });
        
		//
        Ext.applyIf(me, {
            buttons: formButtons,
            items : [basicfieldSet, gridfieldSet]
        });
        me.callParent(arguments);
        me.addListerner();
        
    },
   	/*方法 根据指标ID获取指标放入grid中*/
	initGridStore:function(value,chartType,name){
		//如果传入的指标ID为空,直接返回.
		
		var me=this;
		me.getForm().reset();
		me.kpitypeGrid.store.removeAll();
		if(chartType==0&&value!=""&&name!=undefined){
			me.chartType.value='0';
			if("kpi_root"!=value){
//			FHD.ajax({
//	        	url: __ctxPath + '/kpi/Kpi/findKpiByid',
//	        	params:{ids:value.split(",")},
//	        	callback:function(kpis){
//	        		Ext.Array.each(kpis,function(kpi){
//	        			var insertobj = {};
//	        			insertobj.id = kpi.id;
//	        			insertobj.name = kpi.text;
//		        		me.kpitypeGrid.store.insert(me.kpitypeGrid.store.count(),insertobj);
//	        		})
//	        	}
//	        });
			var insertobj = {};
			insertobj.id = value;
			insertobj.name = name;
    		me.kpitypeGrid.store.insert(me.kpitypeGrid.store.count(),insertobj);	
			}
			else{
				//传入的值为根节点
				var insertobj = {};
				insertobj.id = 'kpi_root';
				insertobj.name = FHD.locale.get('fhd.kpi.kpitree.kpis');
	    		me.kpitypeGrid.store.insert(me.kpitypeGrid.store.count(),insertobj);
	    		me.setFieldValue('kpi_root');
			}
		}else if(chartType==1&&value!=""&&name!=undefined){
			FHD.ajax({
	        	url: __ctxPath + '/kpi/Kpi/findKpiByid',
	        	params:{ids:value.split(",")},
	        	callback:function(kpis){
	        		Ext.Array.each(kpis,function(kpi){
	        			var insertobj = {};
	        			insertobj.id = kpi.id;
	        			insertobj.name = kpi.text;
		        		me.kpitypeGrid.store.insert(me.kpitypeGrid.store.count(),insertobj);
	        		})
	        	}
	        });
			me.chartType.value='1';
		}else if(chartType==2&&value!=""&&name!=undefined){
			me.chartType.value='2';
			var insertobj = {};
			insertobj.id = value;
			insertobj.name = name;
    		me.kpitypeGrid.store.insert(me.kpitypeGrid.store.count(),insertobj);
		}
		me.chartType.onLoad();
		if(!value) return ;

	},
    
    
    getGridStore:function(){
    	var me = this;
    	return me.kpitypeGrid.store;
    },
    
    setFieldValue:function(value){
    	var me = this;
    	me.fieldValue = value;
    	me.field.setValue(value);
    },

        /**
     * 设定当前值
     * @param {} value设定值
     */
    setValue:function(value){
    	var me = this;
    	me.value = value;
		me.field.setValue(value);
    },
     /**
     * 获得当前值
     * @return {当前值}
     */
    getValue:function(){
    	var me = this;
		me.value = me.field.getValue();
    	return me.value;
    },
      //列表监听事件
    addListerner: function () {
    	var me = this.kpitypeGrid;
        me.on('selectionchange', function () {
            if (me.down('#datadel' )) {
                me.down('#datadel' ).setDisabled(me.getSelectionModel().getSelection().length === 0);
            }
        }); 
    },
    
    addFun:function(){
    	var me = this;
    	if(me.chartType.value=='0'){
	    	me.kpiselectwindow =  Ext.create('FHD.ux.kpi.opt.KpiSelectorWindow',{
			    multiSelect:false,
			    selectedvalues:me.getGridStore(),
			    onSubmit:function(store){
				    me.kpitypeGrid.store.removeAll();
				    var kpis = store.data.items;
			    	Ext.Array.each(kpis,function(kpi){
						var insertobj = {};
						var item =kpi.data;
						insertobj.id = item.id;
						insertobj.name = item.name;
						var c = me.kpitypeGrid.store.count();
						me.kpitypeGrid.store.insert(c,insertobj);
						})
				    }
		    });
		    me.kpiselectwindow.show();
    	}
    	if(me.chartType.value=='1'){
    		me.selectorWindow1= Ext.create('FHD.ux.kpi.opt.KpiSelectorWindow',{
			    multiSelect:true,
			    selectedvalues:me.getGridStore(),
			    onSubmit:function(store){
				    me.kpitypeGrid.store.removeAll();
				    var kpis = store.data.items;
				    me.kpitypeGrid.store.removeAll();
			    	Ext.Array.each(kpis,function(kpi){
						var insertobj = {};
						var item =kpi.data;
						insertobj.id = item.id;
						insertobj.name = item.name;
						var c = me.kpitypeGrid.store.count();
						me.kpitypeGrid.store.insert(c,insertobj);
						})
				    }
		    });
 			me.selectorWindow1.show();
    	}
    	if(me.chartType.value=='2'){
    		var insertobj = {};
    		if(me.getGridStore().data.size>0){
    			var item =me.getGridStore().data.items[0].data;
				var valueStore=Ext.create('Ext.data.Store', {
				fields : ['id', 'text'],
				data : [
				    {'id' : item.id,'text' : item.name}
					]
				});
    		}
    	
    		me.selectorWindow2= Ext.create('FHD.ux.kpi.KpiStrategyMapSelectorWindow',{
				extraParams:{smIconType:'display',canChecked:true},
				single:true,
				values:valueStore,
				OrgSmTreeVisible : false,
				smTreeVisible : true,
				mineSmTreeVisible : false,
				//设置目标树图标
				smTreeIcon :'icon-flag-red',
				onSubmit:function(values){
					me.kpitypeGrid.store.removeAll();
					var insertobj = {};
					var item =values.data.items[0].data;
					insertobj.id = item.id;
					insertobj.name = item.text;
					var c = me.kpitypeGrid.store.count();
					me.kpitypeGrid.store.insert(c,insertobj);
				}
			});
 			me.selectorWindow2.show();
    	}
    	
    },
    
    delFun:function(){
    	var me = Ext.getCmp('layoutdatapanel').kpitypeGrid;
   		var selection = me.getSelectionModel().getSelection();//得到选中的记录
   		me.store.remove(selection);
    }
    

})
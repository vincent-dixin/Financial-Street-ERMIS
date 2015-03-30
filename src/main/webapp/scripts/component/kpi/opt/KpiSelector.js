Ext.define('FHD.ux.kpi.opt.KpiSelector', {
    extend: 'Ext.container.Container',
    alias: 'widget.kpioptselector',
    layout: {
        type: 'column'
    },

    /**
     * 标示名称
     */
    labelText: FHD.locale.get('kpiselector.labelText'),

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
    grid: null,

    /**
     * grid默认高度
     */
    gridHeight: 80,

    /**
     * label高度
     */
    labelHeight: 22,

    /**
     * 按钮的高度
     */
    btnHeight: 30,
    
    /**
     * 按钮的宽度
     */
    btnWidth:30,
    
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
    
    
	/*方法 根据指标ID获取指标放入grid中*/
	initGridStore:function(value){
		//如果传入的指标ID为空,直接返回.
		var me=this;
		//先清除grid中的数据
		me.grid.store.removeAll();
		
		if(!value) return ;
		if("kpi_root"!=value){
			FHD.ajax({
	        	url: __ctxPath + '/kpi/Kpi/findKpiByid',
	        	params:{ids:value.split(",")},
	        	callback:function(kpis){
	 				var idArr=[];
	        		me.grid.store.removeAll();
	        		Ext.Array.each(kpis,function(kpi){
	        			var insertobj = {};
	        			insertobj.id = kpi.id;
	        			insertobj.name = kpi.text;
	        			idArr.push(insertobj.id);
		        		me.grid.store.insert(me.grid.store.count(),insertobj);
	        		})
	        		me.setFieldValue(idArr.join(","));
	        	}
	        });
		}
		else{
			//传入的值为根节点
			var insertobj = {};
			insertobj.id = 'kpi_root';
			insertobj.name = FHD.locale.get('fhd.kpi.kpitree.kpis');
    		me.grid.store.insert(me.grid.store.count(),insertobj);
    		me.setFieldValue('kpi_root');
		}
	},
    
    
    getGridStroe:function(){
    	var me = this;
    	return me.grid.store;
    },
    
    setFieldValue:function(value){
    	var me = this;
    	me.fieldValue = value;
    	me.field.setValue(value);
    },
    
    getFieldValue:function(){
    	var me = this;
    	return me.field.getValue();
    },

    initComponent: function () {
        var me = this;
        
        me.field = Ext.create('Ext.form.field.Hidden',{
        	name:me.name,
        	value:me.fieldValue
        });

        me.label = Ext.create('Ext.form.Label', {
            xtype: 'label',
            width: me.labelWidth,
            html: me.labelText + ':',	//将text改成html,满足添加星号的需求 郑军祥修改
            height: me.labelHeight,
            style: {
                marginTop: '3px',
                marginRight: '5px',
                textAlign: me.labelAlign
            }

        });

        me.grid = Ext.create('Ext.grid.Panel', {
            hideHeaders: true,
            height: me.gridHeight,
            columnWidth: 1,
            columns: [{
                xtype: 'gridcolumn',
                dataIndex: 'name',
                flex: 2,
                renderer: function (value, metaData, record, rowIndex, colIndex, store) {
                    return "<div data-qtitle='' data-qtip='" + value + "'>" + value + "</div>";
                }
            }
            /*,{
                xtype: 'templatecolumn',
                tpl: '<font class="icon-del-min" style="cursor:pointer;">&nbsp&nbsp&nbsp&nbsp</font>',
                width: 35,
                align: 'center',
                listeners: {
                    click: {
                        fn: function (t, d, i) {
                            me.grid.store.removeAt(i);
                        }
                    }
                }
            }*/
            ],
            store: Ext.create('Ext.data.Store', {
                idProperty: 'id',
                fields: ['id', 'name']
            })
        });

        Ext.applyIf(me, {

            items: [
            me.label,
            me.grid, {
                xtype: 'button',
                width:me.btnWidth,
                height: me.btnHeight,
                //iconCls: Ext.baseCSSPrefix + 'form-search-trigger',
                iconCls:'icon-magnifier',
                handler: function () {
                    me.kpiselectwindow =  Ext.create('FHD.ux.kpi.opt.KpiSelectorWindow',{
                    	multiSelect:me.multiSelect,
                    	selectedvalues:me.getGridStroe(),
                    	onSubmit:function(store){
                    		me.grid.store.removeAll();
                    		var idArray = [];
                    		var items = store.data.items;
                    		Ext.Array.each(items,function(item){
                        		me.grid.store.insert(me.grid.store.count(),item);
                        		idArray.push(item.data.id);
                        	});
                    		me.field.setValue(idArray.join(','));
                    	}
                    });
                    me.kpiselectwindow.show();
                    
                }
            }]

        });


        me.callParent(arguments);
        me.initGridStore(me.fieldValue);
    }

});
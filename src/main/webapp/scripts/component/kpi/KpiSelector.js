Ext.define('FHD.ux.kpi.KpiSelector', {
	extend : 'Ext.container.Container',
	alias : 'widget.kpiselector',
	requires:['FHD.ux.kpi.KpiSelectorWindow'],
	layout: {
        type: 'column'
    },
	
    /*属性*/
    /**
     * 是否单选
     * @type Boolean
     */
	single:true,
	/**
	 * 高度
	 * @type Number
	 */
	height: 25,
	/**
	 * 当前值
	 * @type String
	 */
	value:'',
	/**
	 * 标示名称
	 * @type String
	 */
	labelText: $locale('kpiselector.labeltext'),
	/**
	 * 标示对齐方式
	 * @type String
	 */
	labelAlign:'left',
	/**
	 * 标示宽度
	 * @type Number
	 */
	labelWidth:50,
	
	/*成员*/
	grid:null,
	field:null,
	/**
	 * 弹出窗口：默认未开启
	 * @type 
	 */
	selectorWindow:null,
	
	//是否显示指标树
	kpiTreeVisible : true,
	//是否机构指标树
	kpiOrgTreeVisible : true,
	//是否显示目标指标树
	kpiSmTreeVisible : true,
	//是否显示我的指标树
	kpiMineTreeVisible : true,

	//设置指标树图标
	kpiTreeIcon : 'icon-flag-red',
	//设置机构指标树图标
	kpiOrgTreelIcon : 'icon-org',
	//设置目标指标树图标
	kpiSmTreeIcon : 'icon-flag-red',
	//设置我的指标树图标
	kpiMineTreeIcon : 'icon-orgsub',
	
	extraParams : {},
	
	/*方法 根据指标ID获取指标放入grid中*/
	initValue:function(value){
		//如果传入的指标ID为空,直接返回.
		if(!value) return ;
		var me=this;
		if("kpi_root"!=value){
			var ids=value.split(",");
			FHD.ajax({
	        	url: __ctxPath + '/kpi/Kpi/findKpiByid',
	        	params:{ids:ids},
	        	callback:function(kpis){
	 				var ids=[];
	        		me.grid.store.removeAll();
	        		Ext.Array.each(kpis,function(kpi){
	        			var kpiTemp= new Kpi({
			    			id:kpi.id,
			    			dbid:kpi.dbid,
			    			text:kpi.text,
			    			type:kpi.type
			    		});
						ids.push(kpiTemp.data.id);
		        		me.grid.store.insert(me.grid.store.count(),kpiTemp);
	        		})
					var value=ids.join(",");
	        		me.setValue(value);
	        	}
	        });
		}
		else{
			//传入的值为根节点
			var ids = [];
			var kpiTemp= new Kpi({
    			id:"kpi_root",
    			dbid:"kpi_root",
    			text:FHD.locale.get('fhd.kpi.kpitree.kpis'),
    			type:"kpi"
    		});
			ids.push(kpiTemp.data.id);
    		me.grid.store.insert(me.grid.store.count(),kpiTemp);
		}
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
    
    setValues:function(values){
    	var me = this;
    	var ids=new Array();
		me.grid.store.removeAll();
    	values.each(function(value){
    		ids.push(value.data.id);
    		me.grid.store.insert(me.grid.store.count(),value);
    	});
    	var value=ids.join(",");
		this.setValue(value);
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
	getValues:function(){
    	var me = this;
		return me.grid.store;
    },
	/**
	 * 初始化方法
	 */
    initComponent: function() {
		Ext.define('Kpi', {
		    extend: 'Ext.data.Model',
		    fields:['id', 'code', 'text', 'dbid','type']
		});
    	
        var me = this;
		
        if(me.single){
        	me.height=25;
        }
        
		me.field=Ext.widget('textfield',{
			hidden:true,
	        name:me.name,
	        value:me.value
        });
		
		me.grid=Ext.widget('grid',{
        	hideHeaders:true,
            height: me.height,
            columnWidth: 0.9,
        	columns:[{
        		xtype: 'gridcolumn',
                dataIndex: 'text',
                flex:2,
                renderer:function(value, metaData, record, rowIndex, colIndex, store){
					return "<div data-qtitle='' data-qtip='" + value + "'>" + value + "</div>";					
			    }
        	},{
        		xtype:'templatecolumn',
            	tpl:'<font class="icon-del-min" style="cursor:pointer;">&nbsp&nbsp&nbsp&nbsp</font>',
            	width:35,
            	align:'center',
            	listeners:{
            		click:{
            			fn:function(t,d,i){
							me.grid.store.removeAt(i);
							var ids=new Array();
                        	me.grid.store.each(function(r){
                        		ids.push(r.data.id);
                        	});
                        	me.setValue(ids.join(","));
            			}
            		}
            	}
        	}],
        	store:Ext.create('Ext.data.Store',{
        		idProperty: 'id',
			    fields:['id', 'code', 'text','dbid','type']
        	})
        });

        Ext.applyIf(me, {
            items: [
            	{
            		xtype:'label',
            		width:me.labelWidth,
            		text:me.labelText + ':',
            		height: 22,
            		style:{
            			marginTop: '3px',
            			marginRight: '5px',
            			textAlign: me.labelAlign
            		}
            	},
            	me.grid,
            	{
                    xtype: 'button',
                    height: me.height,
                    iconCls:'icon-kpistrategymap-add',
                    columnWidth: 0.1,
                    handler:function(){
						me.selectorWindow= Ext.create('FHD.ux.kpi.KpiSelectorWindow',{
							single:me.single,
							values:me.getValues(),
							extraParams:me.extraParams,
							//是否显示指标树
							kpiTreeVisible : me.kpiTreeVisible,
							//是否机构指标树
							kpiOrgTreeVisible : me.kpiOrgTreeVisible,
							//是否显示目标指标树
							kpiSmTreeVisible : me.kpiSmTreeVisible,
							//是否显示我的指标树
							kpiMineTreeVisible : me.kpiMineTreeVisible,

							//设置指标树图标
							kpiTreeIcon : me.kpiTreeIcon,
							//设置机构指标树图标
							kpiOrgTreelIcon : me.kpiOrgTreelIcon,
							//设置目标指标树图标
							kpiSmTreeIcon : me.kpiSmTreeIcon,
							//设置我的指标树图标
							kpiMineTreeIcon : me.kpiMineTreeIcon,
							onSubmit:function(values){
								me.setValues(values);
							}
						}).show();
					}
                },
                me.field
            ]
        });
		me.callParent(arguments);
		me.initValue(me.value);
    }

});
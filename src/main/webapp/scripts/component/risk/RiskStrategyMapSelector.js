Ext.define('FHD.ux.risk.RiskStrategyMapSelector', {
	extend : 'Ext.container.Container',
	alias : 'widget.riskstrategymapselector',
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
	height: 22,
	/**
	 * 当前值
	 * @type String
	 */
	value:'',
	/**
	 * 标示名称
	 * @type String
	 */
	labelText: '1111111111111111',
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
	
//	//是否显示组织目标树
//	OrgSmTreeVisible : true,
//	//是否显示目标树
//	smTreeVisible : true,
//	//是否显示我的目标树
//	mineSmTreeVisible : true,
//	//设置机构目标树图标
//	orgSmTreeIcon : 'icon-org',
//	//设置目标树图标
//	smTreeIcon : 'icon-flag-red',
//	//设置我的目标树图标
//	mineSmTreeIcon : 'icon-orgsub',
	
	/*方法*/
	initValue:function(value){
//		var me=this;
//		var ids=value.split(",");
//		FHD.ajax({
//        	url: __ctxPath + '/kpi/KpiStrategyMap/findSmById',
//        	params:{ids:ids},
//        	callback:function(kpiStrategyMaps){
// 				var ids=new Array();
//        		me.grid.store.removeAll();
//        		Ext.Array.each(kpiStrategyMaps,function(kpiStrategyMap){
//        			var kpiStrategyMapTemp= new KpiStrategyMap({
//		    			id:kpiStrategyMap.id,
//		    			dbid:kpiStrategyMap.dbid,
//		    			text:kpiStrategyMap.text,
//		    			type:kpiStrategyMap.type
//		    		});
//					ids.push(kpiStrategyMapTemp.data.id);
//	        		me.grid.store.insert(me.grid.store.count(),kpiStrategyMapTemp);
//        		})
//				var value=ids.join(",");
//        		//this.setValue(value);
//        		me.value = value;
//        		me.field.setValue(value);
//        	}
//        });
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
    	//this.setValue(value);
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
	getValues:function(){
    	var me = this;
		return me.grid.store;
    },
	/**
	 * 初始化方法
	 */
    initComponent: function() {
		Ext.define('KpiStrategyMap', {
		    extend: 'Ext.data.Model',
		    fields:['id', 'code', 'text','dbid','type']
		});
    	
        var me = this;
		
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
                        	//me.setValue(ids.join(","));
                        	var value = ids.join(",");
                        	me.value = value;
                    		me.field.setValue(value);
            			}
            		}
            	}
        	}]
		
//			,
//        	store:Ext.create('Ext.data.Store',{
//        		idProperty: 'id',
//        		proxy : {
//					type : 'ajax',
//					url : __ctxPath + '/kpi/Kpi/listMap',
//					reader : {
//						type : 'json',
//						root : 'users'
//					}
//				},
//			    fields:['id', 'code', 'text','dbid','type']
//        	})
        });

        Ext.applyIf(me, {
            items: [
            	{
            		xtype:'label',
            		width:me.labelWidth,
            		text:me.labelText + ':',
            		height: me.height,
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
						me.selectorWindow= Ext.create('FHD.ux.risk.RiskStrategyMapSelectorWindow',{
							single:me.single,
							values:me.getValues(),
//							OrgSmTreeVisible : me.OrgSmTreeVisible,
//							smTreeVisible : me.smTreeVisible,
//							mineSmTreeVisible : me.mineSmTreeVisible,
							//设置机构目标树	图标
//							orgSmTreeIcon : me.orgSmTreeIcon,
							//设置目标树图标
//							smTreeIcon : me.smTreeIcon,
							//设置我的目标树图标
//							mineSmTreeIcon : me.mineSmTreeIcon,
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
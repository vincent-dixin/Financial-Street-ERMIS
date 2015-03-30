Ext.define('FHD.ux.process.processSelector', {
	extend : 'Ext.container.Container',
	alias : 'widget.processselector',
	
	requires : ['FHD.ux.process.processSelectorWindow'],
	layout : {
		type : 'column'
	},
	labelWidth:95,
	/**
	 * 是否单选
	 * 
	 * @type Boolean
	 */
	single : true,
	/**
	 * 当前值
	 * 
	 * @type String
	 */
	value : '',
	parent:true,
	/**
	 * 标示名称
	 * 
	 * @type String
	 */
	labelText : $locale('kpistrategymapselector.labeltext'),
	/**
	 * 标示对齐方式
	 * 
	 * @type String
	 */
	labelAlign : 'left',
	autoWidth: true,
	/**
	 * 标示宽度
	 * 
	 * @type Number
	 */
	/**
	 * 弹出窗口：默认未开启
	 * 
	 * @type
	 */
	extraParams : {},

	/* 方法 */
	initValue : function(value) {
		var me=this;
		if(value==null||value==""){
			value=me.field.getValue();
			return ;
		}
		var ids=value.split(",");
		FHD.ajax({
        	url: __ctxPath + '/process/process/findProcessByIds.f',
        	params:{
        		processIds:ids
        	},
        	callback:function(fileUploadEntitys){
        		var ids=new Array();
        		me.grid.store.removeAll();
        		Ext.Array.each(fileUploadEntitys,function(fileUploadEntity){
        			var file=new Array();
        			file.push({
		    			id:fileUploadEntity.id,
		    			code:fileUploadEntity.code,
		    			text:fileUploadEntity.text,
		    			dbid:fileUploadEntity.dbid,
		    			type:fileUploadEntity.type
		    		});
	        		me.grid.store.insert(me.grid.store.count(),file);
        		});
				me.value = value;
				me.field.setValue(value);
        	}
        });
	},
	/**
	 * 设定当前值
	 * @param {} value设定值
	 */
	setValue : function(value) {
		var me = this;
		me.value = value;
		me.field.setValue(value);
	},

	setValues : function(values) {
		var me = this;
		var ids = new Array();
		me.grid.store.removeAll();
		values.each(function(value) {
			ids.push(value.data.id);
			me.grid.store.insert(me.grid.store.count(), value);
		});
		var value = ids.join(",");
		me.value = value;
		me.field.setValue(value);
	},
	/**
	 * 获得当前值
	 * 
	 * @return {当前值}
	 */
	getValue : function() {
		var me = this;
		me.value = me.field.getValue();
		return me.value;
	},
	getValues : function() {
		var me = this;
		return me.grid.store;
	},
	/**
	 * 初始化方法
	 */
	initComponent : function() {
		
		Ext.define('KpiStrategyMap', {
			extend : 'Ext.data.Model',
			fields : ['id', 'code', 'text', 'dbid', 'type']
		});

		var me = this;
        if(me.multiSelect){
			if(me.height){
				me.height = me.height;
			}else{
				me.height = 80;				
			}
		}else{
			me.height = 23;
		}
		me.label=Ext.widget('label',{
    		width: me.labelWidth,
    		html: '<span style="float:'+me.labelAlign+'">'+me.fieldLabel + ':</span>',
    		height: 22,
    		style: {
    			marginRight: '10px'
    		}
    	});
		me.field = Ext.widget('textfield', {
			hidden : true,
			name : me.name,
			value : me.value,
			listeners:{
				change:function (field,newValue,oldValue,eOpts ){
					me.initValue(newValue);
				}
		    }
		});
		me.grid = Ext.widget('grid', {
			hideHeaders : true,
			height : me.height,
			columnWidth : 1,
			columns : [{
				xtype : 'gridcolumn',
				dataIndex : 'text',
				flex : 2,
				renderer : function(value, metaData, record, rowIndex,
						colIndex, store) {
					return "<div data-qtitle='' data-qtip='" + value + "'>"
							+ value + "</div>";
				}
			}, {
				xtype : 'templatecolumn',
				tpl : '<font class="icon-del-min" style="cursor:pointer;">&nbsp&nbsp&nbsp&nbsp</font>',
				width : 35,
				align : 'center',
				listeners : {
					click : {
						fn : function(t, d, i) {
							me.grid.store.removeAt(i);
							var ids = new Array();
							me.grid.store.each(function(r) {
										ids.push(r.data.id);
									});
							var value = ids.join(",");
							me.value = value;
							me.field.setValue(value);
						}
					}
				}
			}],
			store : Ext.create('Ext.data.Store', {
				idProperty : 'id',
				proxy : {
					type : 'ajax',
					url : __ctxPath + '/kpi/Kpi/listMap',
					reader : {
						type : 'json',
						root : 'users'
					}
				},
				fields : ['id', 'code', 'text', 'dbid', 'type']
			})
		});

		Ext.applyIf(me, {
			items : [
			    me.label,
			    me.grid, 
			    {
					xtype : 'button',
					height : 22,
					iconCls : 'icon-magnifier',
					width : 22,
					handler : function() {
						me.selectorWindow = Ext.create('FHD.ux.process.processSelectorWindow', {
							extraParams : {smIconType:'display',canChecked:true},
							single : me.single,
							parent:me.parent,
							values : me.getValues(),
							onSubmit : function(values) {
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
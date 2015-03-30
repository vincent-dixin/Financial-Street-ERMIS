/**
 * 右列待选列表和已选列表组件
 * @author 郑军祥
 * @param multiSelect：默认为true，支持单选；设置为true，支持多选		
 * @param allowBlank: 默认为false，可以为空；设置为true，不可为空，提示语句需要单独写     
 * */
Ext.define('FHD.ux.listselector.ListSelector', {
	extend : 'Ext.container.Container',
	alias : 'widget.listselector',
	layout: {
        type: 'column'
    },
	autoWidth: true,
	multiSelect: true,
	allowBlank: true,
	labelWidth:95,
	disabled:false,
	initUrl: '/components/initListByIds',
	listUrl:'/components/findListBySome',
	entityName:'NoEntityName',	//后台反射使用
	queryKey:'name',	//查询的属性名称
	initComponent : function() {
		var me = this;
		
		me.gridColumns = [];		//列表项
        me.modelFields = ['id'];	//列表实体定义
  
        //1. 构建grid的columns
        for(var i = 0; i < me.columns.length; i++) {
        	var obj = {};
        	obj['xtype'] = 'gridcolumn';
        	obj['dataIndex'] = me.columns[i]['dataIndex'];
        	obj['text'] = me.columns[i]['header'];
        	obj['hidden'] = true;
        	if(me.columns[i]['isPrimaryName']){
        		obj['hidden'] = false;
        		obj['flex'] = 2;
        		obj['renderer'] = function(value, metaData, record, rowIndex, colIndex, store){//动态
					console.log(record);
        			return "<div data-qtitle='' data-qtip='" + value+" ( "+record.get(me.noteField)+" ) " + "'>" + value + " ( "+record.get(me.noteField)+" ) " + "</div>";					
			    }
        	}
        	me.gridColumns.push(obj);
        	
        	//构建modelField
        	me.modelFields.push(me.columns[i]['dataIndex']);
        }
        me.gridColumns.push({
    		xtype:'templatecolumn',
        	tpl:'<font class="icon-close" style="cursor:pointer;">&nbsp;&nbsp;&nbsp;&nbsp;</font>',
        	width:35,
        	align:'center',
        	readonly:true,
        	listeners:{
        		click:{
        			fn:function(grid,d,i){
        				var select=grid.store.getAt(i);
        				me.removeFromStore(select);
        			}
        		}
        	}
    	});
        
        
		if(me.multiSelect){
			if(me.height){
				me.height = me.height;
			}else{
				me.height = 80;				
			}
		}else{
			me.height = 23;
		}
    	me.field=Ext.widget('textfield',{
			hidden:true,
	        name:me.name,
	        value: me.value,
	        allowBlank:me.allowBlank,
	        listeners:{
				change:function (field,newValue,oldValue,eOpts ){
					me.initValue(newValue);
				}
		    }
        });
        me.label=Ext.widget('label',{
    		width: me.labelWidth,
    		html: '<span style="float:'+me.labelAlign+'">'+me.fieldLabel + ':</span>',
    		height: 22,
    		style: {
    			marginRight: '10px'
    		}
    	});
    
        /**grid数据源*/
	    me.valueStore = Ext.create('Ext.data.Store',{
    		idProperty: 'id',
    		queryMode: 'local',
		    fields:me.modelFields
        });

		/**grid*/
		me.grid=Ext.widget('grid',{	//动态
        	hideHeaders: true,
        	autoScroll: true,
            height: me.height,
            columnWidth: 1,
        	columns:me.gridColumns,
        	store:me.valueStore
        });
        me.button=Ext.widget('button',{
        	iconCls:'icon-magnifier',
            height: 22,
            width: 22,
            disabled: me.disabled,
            handler:function(){
            	var selects = new Array();
            	me.getGridStore().each(function(r){
            		selects.push(r);
            	});
		    	me.window = Ext.create('FHD.ux.listselector.ListSelectorWindow',{
		    		title:me.title,
		    		entityName:me.entityName,
		    		queryKey:me.queryKey,
		    		columns:me.columns,
		    		listUrl:me.listUrl,
					multiSelect:me.multiSelect,
					onSubmit:function(win){
						me.grid.store.removeAll();
						me.setValueFromStore(win.selectedgrid.store);
					}
				}).show();
				me.window.setValue(selects);
		    }
    	}); 

        Ext.applyIf(me, {
            items: [
            	me.label,
            	me.grid,
            	me.button,
                me.field
            ]
        });
        me.callParent(arguments);
        me.initValue(me.value);
	},
    /**
	 * private 为隐藏域和显示的grid赋值
	 */
    setValueFromStore:function(values){
    	var me = this;
		var hiddenValue=new Array();
    	values.each(function(r){
    		me.grid.store.insert(0,r);
    	});
    	var hiddenValue = me.getHiddenValue();
		me.setHiddenValue(hiddenValue);
    },
    /**
     * private 获得当前要设置的值的数组
     * @return 隐藏域的值的数组
     */
    getHiddenValue: function(){
    	var me = this;
    	var values=me.getGridStore();
    	var hiddenValue=new Array();
    	values.each(function(value){
    		var ids = {};
    		ids['id'] = value.data.id;
    		hiddenValue.push(ids);
    	});
    	return hiddenValue;
    },
    /**
     * private 获得当前grid的store
     * @return 当前grid的store
     */
	getGridStore:function(){
    	var me = this;
		return me.grid.store;
    },
	/**
	 * public 为隐藏域赋值,完成初始化工作
	 *
	 */
	setHiddenValue:function(valueArray){
    	var me = this;
    	if(valueArray.length>0){
    		var value = Ext.JSON.encode(valueArray);
    		me.value = value;
			me.field.setValue(value);
    	}else{
    		me.value = null;
			me.field.setValue(null);
    	}
    },
    /**
     * private 从当前grid的store中删除
     */
	removeFromStore:function(value){
    	var me = this;
    	me.grid.store.remove(value);
    	var hiddenValue = me.getHiddenValue();
    	me.setHiddenValue(hiddenValue);
    },
    /**
     * public 设置初始值
     */
	initValue: function(value){
		var me=this;
		if(value==null||value==""){
			value=me.field.getValue();
		}
		var ids=value.split(",");
		if(!me.allowBlank){
			if(value && value !='[]'){
				me.grid.bodyStyle = 'background:#FFFFFF';
			}else{
				me.grid.bodyStyle = 'background:#FFEDE9';
			}
		}
		if(value && value !='[]'){
			if(me.initUrl){
				FHD.ajax({//ajax调用
					url : __ctxPath + me.initUrl, //'/demo/findFristtreeByIds.f'
					params : {
						entityName:me.entityName,
						ids:value
					},
					callback : function(data){
						var records = data.data;
		    			Ext.Array.each(records,function(r,i){
			        		me.grid.store.removeAt(i);
				            me.grid.store.insert(i, r);

			        	});
					}
				});
			}else{	//本地数据，不初始化
				
			}
		}
	},
    /**
     * public 获得当前值
     * @return {当前值}
     */
    getValue:function(){
    	var me = this;
		me.value = me.field.getValue();
    	return me.value;
    },
    /**
     * public 清空值
     */
    clearValues : function(){
        var me = this;
        me.field.setValue(null);
        me.grid.store.removeAll();
    }
});
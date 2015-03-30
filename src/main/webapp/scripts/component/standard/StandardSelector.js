Ext.define('FHD.ux.standard.StandardSelector', {
	extend : 'Ext.container.Container',
	alias : 'widget.standardselector',
	
	requires:['FHD.ux.standard.StandardSelectorWindow'],
	layout: {
        type: 'column'
    },
	
    /**
     * 是否单选
     * @type Boolean
     */
    multiSelect : false,
	/**
	 * 高度
	 * @type Number
	 */
	//height: 23,
	/**
	 * 当前值
	 * @type String
	 */
	value:'',
	/**
	 * 标示名称
	 * @type String
	 */
	labelText:'内控标准',
	/**
	 * 标示对齐方式
	 * @type String
	 */
	labelAlign:'left',
	/**
	 * 标示宽度
	 * @type Number
	 */
	labelWidth:95,
	
	extraParams : {},
	
	/*方法*/
	initValue:function(value){
		var me=this;
		if(value==null||value==""){
			value=me.field.getValue();
			return;
		}
		var ids=value.split(",");
		FHD.ajax({
        	url: __ctxPath + '/standard/standardTree/findStandardByIds.f',
        	params:{standardIds:ids},
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
        		})
				me.value = value;
				me.field.setValue(value);
        	}
        });
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
        var me = this;
        
        //var dataMap = {'standard':'1','required':'0'};
        //me.myType = dataMap[me.myType];
        
		me.field=Ext.widget('textfield',{
			hidden:true,
	        name:me.name,
	        value:me.value,
	        listeners:{
				change:function (field,newValue,oldValue,eOpts ){
					me.initValue(newValue);
				}
		    }
        });
		
		me.grid={
			xtype: 'gridpanel',
        	hideHeaders:true,
        	autoScroll: true,
            height : me.multiSelect?(me.height?me.height:80):23,
            columnWidth: 1,
            
        	columns:[{
        		xtype: 'gridcolumn',
                dataIndex: 'text',
                flex:2,
                renderer:function(value, metaData, record, rowIndex, colIndex, store){
					return "<div data-qtitle='' data-qtip='" + value + "'>" + value + "</div>";					
			    }
        	},{
        		xtype:'templatecolumn',
            	tpl:'<font class="icon-close" style="cursor:pointer;">&nbsp&nbsp&nbsp&nbsp</font>',
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
                        	var value = ids.join(",");
                        	me.value = value;
                    		me.field.setValue(value);
            			}
            		}
            	}
        	}],
        	store:Ext.create('Ext.data.Store',{
        		idProperty: 'id',
			    fields:['id', 'code', 'text','dbid','type','controlPoint']
        	})
        };
		
        Ext.apply(me, {
            items: [
            	{
            		xtype:'label',
            		width:me.labelWidth,
            		text:me.labelText + ':',
            		height: 23,
            		style: {
    						marginRight: '10px'
    				}
            	},
            	me.grid,
            	{
                    xtype: 'button',
                    height: 23 ,
                    iconCls:'icon-magnifier',
	            	width: 22,
                    handler:function(){
						me.selectorWindow= Ext.create('FHD.ux.standard.StandardSelectorWindow',{
							myType:me.myType,
							multiSelect:me.multiSelect,
							values:me.getValues(),
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
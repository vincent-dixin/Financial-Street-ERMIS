Ext.define('FHD.ux.rule.RuleSelector', {
	extend : 'Ext.container.Container',
	alias : 'widget.ruleselector',
	layout: {
        type: 'column'
    },
    autoWidth: true,
	allowBlank: true,
	labelWidth:95,
	fieldLabel : '制度选择',
	fieldAlgin : 'left',
	disabled:false,
	/**
	 * 初始化方法
	 */
    initComponent : function() {
		Ext.define('Rule', {
		    extend: 'Ext.data.Model',
		    fields:['id', 'code', 'text', 'dbid','type']
		});
		debugger;
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
        debugger;
		me.label=Ext.widget('label',{
    		width: me.labelWidth,
    		html: '<span style="float:'+me.labelAlign+'">'+me.fieldLabel + ':</span>',
    		height: 22,
    		style: {
    			marginRight: '10px'
    		}
    	});
		me.grid=Ext.widget('grid',{
        	hideHeaders: true,
        	autoScroll: true,
            height: me.height,
            autoWidth : true,
            columnWidth: 1,
        	columns:[{
	        		xtype: 'gridcolumn',
	                dataIndex: 'id',
	                hidden:true
		        },{
	        		xtype: 'gridcolumn',
	                dataIndex: 'code',
	                hidden:true
	        	},{
        		xtype: 'gridcolumn',
                dataIndex: 'text',
                flex:2,
                renderer:function(value, metaData, record, rowIndex, colIndex, store){
					debugger;
					return "<div data-qtitle='' data-qtip='" + value + "'>" + value + " ( "+record.get('code')+" ) " +  "</div>";					
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
                        	me.setValue(ids);
            			}
            		}
            	}
        	}],
        	store:Ext.create('Ext.data.Store',{
        		idProperty: 'id',
			    fields:['id', 'code', 'text','dbid','type']
        	})
        });
		me.button=Ext.widget('button',{
                    xtype: 'button',
                    iconCls:'icon-magnifier',
	            	height: 22,
	            	width: 22,
                    handler:function(){
						me.selectorWindow= Ext.create('FHD.ux.rule.RuleSelectorWindow',{
							multiSelect:me.multiSelect,
							values:me.getValues(),
							extraParams:me.extraParams,
							//是否显示指标树
							ruleTreeVisible : me.ruleTreeVisible,

							//设置指标树图标
							ruleTreeIcon : me.ruleTreeIcon,
							
							onSubmit:function(values){
								me.setValues(values);
							}
						}).show();
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
/*方法 根据指标ID获取指标放入grid中*/
	initValue:function(value){
		
		debugger;
		var me=this;
		if(value==null||value==""){
			value=me.field.getValue();
		}
		
		var ids=value.split(",");
		if(!me.allowBlank){
			debugger;
			if(value && value !='[]'){
				me.grid.bodyStyle = 'background:#FFFFFF';
			}else{
				me.grid.bodyStyle = 'background:#FFEDE9';
			}
		}
		if(value && value !='[]'){
			FHD.ajax({
	        	url: __ctxPath + '/icm/rule/findRuleByIds.f',
	        	params:{ids:ids},
	        	callback:function(rules){
	 				var ids=new Array();
	        		me.grid.store.removeAll();
	        		console.log(rules);
	        		Ext.Array.each(rules,function(rule){
	        			var ruleTemp= new Rule({
			    			id:rule.id,
			    			dbid:rule.dbid,
			    			text:rule.text,
			    			code:rule.code
			    		});
						ids.push(ruleTemp.data.id);
		        		me.grid.store.insert(me.grid.store.count(),ruleTemp);
	        		})
	        		me.setValue(ids);
	        	}
	        });
		}
		
	},
    /**
     * 设定当前值
     * @param {} value设定值
     */
    setValue:function(valueArray){
    	var me = this;
    	if(valueArray.length>0){
    		var value = valueArray.join(",");
    		me.value = value;
			me.field.setValue(value);
    	}else{
    		me.value = null;
			me.field.setValue(null);
    	}
    },
    
    setValues:function(values){
    	var me = this;
    	var ids=new Array();
		me.grid.store.removeAll();
    	values.each(function(value){
    		ids.push(value.data.id);
    		me.grid.store.insert(me.grid.store.count(),value);
    	});
    	me.setValue(ids);
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
    }
});
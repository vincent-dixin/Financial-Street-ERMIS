Ext.define('FHD.ux.icm.assess.AssessPlanSelector',{
	extend :'Ext.form.FieldContainer',
	alias : 'widget.assessplanselector',
	
	requires:[
		'FHD.ux.icm.assess.AssessPlanSelectorWindow'
	],
	
	height : 100,
	
	initComponent: function() {
		var me = this;
		
		me.field=Ext.widget('hiddenfield',{
			hidden:true,
	        name:me.name,
	        listeners:{
				change:me.onChange
		    }
        });
		
		me.grid = Ext.widget('grid',{
			hideHeaders:true,
	        height: me.height,
	        columns:[{
	        	dataIndex:'name',flex:1
	        },{
            	xtype:'templatecolumn',
            	tpl:'<font class="icon-del-min" style="cursor:pointer;">&nbsp&nbsp&nbsp&nbsp</font>',
            	width:35,
            	align:'center',
            	listeners:{
            		click:{
            			fn:function(grid,d,i){
            				var select = grid.store.getAt(i);
            				me.storeRemove(select.data.id);
            			}
            		}
            	}
            }],
			flex:1,
        	store:Ext.create('Ext.data.Store',{
        		idProperty: 'id',
			    fields:['id', 'name']
        	})
		});
		me.btn = Ext.widget('button',{
			iconCls:'icon-magnifier',
            width: 23,
            handler:function(){
				me.selectorWindow= Ext.create('FHD.ux.icm.assess.AssessPlanSelectorWindow',{
					multiSelect:me.multiSelect,
					modal: true,
					onSubmit:function(win){
						var assessPlanArray = new Array();
		            	win.selectedGrid.store.each(function(r){
		            		assessPlanArray.push(r);
		            	});
		            	me.setValue(assessPlanArray);
					}
				}).show();
		    }
		});
		
		Ext.applyIf(me,{
			layout: {
	            type: 'hbox',
	            align:'top'
	        },
	        items :[me.field,me.grid,me.btn]
		});
		
		me.callParent(arguments);
	},
	
	
	storeRemove : function(value){
		var me = this;
    	me.grid.store.removeAt(me.grid.store.find('id',value));
    	var ids = Ext.Array.remove(me.field.getValue().split(','),value)
    	var v=ids.join(",");
		me.value = v;
		me.field.setValue(v);
	},
	
	
	setValue : function(datas) {
		var me = this,value;
		me.grid.store.removeAll();
		var ids=new Array();
		if(Ext.isString(datas)){
			value = ids;
		}else {
	    	Ext.each(datas,function(v){
	    		ids.push(v.data.id);
	    		me.grid.store.insert(0,v);
	    	});
	    	value=ids.join(",");
		}
		me.value = value;
		me.field.setValue(value);
	},
	
	getValue : function() {
		return me.field.getValue();
	}	
})
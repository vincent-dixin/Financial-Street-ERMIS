Ext.define('FHD.ux.kpi.ScorecardSelectorWindow', {
	extend : 'Ext.window.Window',
	alias : 'widget.scorecardselectorWindow',
	
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
    title : '记分卡选择',
    selectedvalues:[],
	
    setSelectedValue:function(selectedvalues){
    	var me = this;
    	if(selectedvalues.data){
    		for(var i=0;i<selectedvalues.data.length;i++){
        		var item = selectedvalues.data.items[i].data;
        		var insertobj = {};
        		insertobj.id = item.id;
        		insertobj.text = item.text;
        		me.grid.store.insert(me.grid.store.count(),insertobj);
        	}
    	}
    },
	
    resetSelectGrid:function(){
    	var me = this;
    	me.grid.store.removeAll();
    },
    
	reloadStroe:function(node){
		if (node.parentNode == null) return;//根节点直接返回
		var me = this;
		var data = node.data;
	},
	
	getValues:function(){
    	var me = this;
		return me.grid.store;
    },
    
    setTreeValue:function(values){
    	var me = this;
    	if(me.tree){
    		me.tree.values=values;
    		me.tree.setTreeValues(true);
    	}
    },
	initComponent : function() {
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
			    
			    {'id' : '目标值','name' : '目标值'},
			    {'id' : '实际值','name' : '实际值'},
			    {'id' : '评估值','name' : '评估值'},
			    {'id' : '标杆值','name' : '标杆值'},
			    {'id' : '同比值','name' : '同比值'},
			    {'id' : '环比值','name' : '环比值'}
			    */
			    {'id' : '评估值','name' : '评估值'}
			]
		});
		
		var cellEditing = Ext.create('Ext.grid.plugin.CellEditing', {
	        clicksToEdit: 1
	    });
		
		me.grid = Ext.create('Ext.grid.Panel',{
			loadMask: true,
        	border:me.border,
			region : 'center',
			tbar : new Ext.Toolbar({
				height : 25,
				items : [{
					xtype : "tbtext",
					text : $locale('kpiselectorwindow.selectgrid.title')
				}]
			}),
			plugins: [cellEditing],
			store: Ext.create('Ext.data.Store', {
                idProperty: 'id',
                fields: ['id','name', 'valueType']
            }),
			columns : [
			    {
				xtype : 'gridcolumn',
				hidden : true,
				dataIndex : 'id'
			}, {
				xtype : 'gridcolumn',
				dataIndex : 'text',
				flex : 3,
				text : $locale("fhd.pages.test.field.name"),
				renderer : function(value, metaData, record, rowIndex, colIndex, store) {
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
			    	metaData.tdAttr = 'data-qtip="值类型表示记分卡的基本类型值，包括：评估值等，可按业务需要进行调整" style="background-color:#FFFBE6"';
					var index = me.valueTypeStore.find('id',value);
					var record = me.valueTypeStore.getAt(index);
					if(record){
						return record.data.name;
					}else{
						if(value){
		    				return value;
		    			}else{
		    				metaData.tdAttr = 'data-qtip="值类型表示记分卡的基本类型值，包括：评估值等，可按业务需要进行调整" style="background-color:#FFFBE6"';
						}
					}
				}
			}, {
				xtype : 'templatecolumn',
				text : $locale('fhd.common.delete'),
				align : 'center',
				tpl : '<font class="icon-del-min" style="cursor:pointer;">&nbsp&nbsp&nbsp&nbsp</font>',
				listeners : {
					click : {
						fn : function(grid, d, i) {
							Ext.Array.remove(me.tree.values,grid.store.getAt(i).data.dbid);
							me.tree.setTreeValues(false);
							grid.store.removeAt(i);
						}
					}
				}
			}]
		});
		me.tree = Ext.create('FHD.ux.category.CategoryStrategyMapTree', {
			title:FHD.locale.get('fhd.kpi.categoryroot'),
			border:true,
			region : 'west',
			iconCls: 'icon-ibm-icon-scorecards',
			width : 300,
			height : 500,
			oneVisible:true,
			checked : true,
			myCategoryTreeVisible:false,
			orgCategoryTreeVisible:false,
			categoryClickFunction:function(node){
				me.reloadStroe(node);
				if(!node.data.root){
					if(!me.grid.store.getById(node.data.id)){
						if(!me.multiSelect){
	            			me.grid.store.removeAll();
	            		}
            			me.grid.store.insert(me.grid.store.count(), node);
            		}
				}
			}
		});
		
		me.buttons = [{
			xtype : 'button',
			text : $locale('fhd.common.confirm'),
			handler : function() {
				me.onSubmit(me.grid.store);
				me.close();
			}
		}, {
			xtype : 'button',
			text : $locale('fhd.common.close'),
			style : {
				marginLeft : '10px'
			},
			handler : function() {
				me.close();
			}
		}];
		Ext.applyIf(me, {
			items : [me.tree,me.grid,me.buttons]
		});

		me.callParent(arguments);
		me.setSelectedValue(me.selectedvalues);
	}
});
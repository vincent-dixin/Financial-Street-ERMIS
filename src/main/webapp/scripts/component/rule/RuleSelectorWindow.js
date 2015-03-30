Ext.define('FHD.ux.rule.RuleSelectorWindow', {
	extend : 'Ext.window.Window',
	alias : 'widget.ruleselectorwindow',
	constrain:true,
	height : 500,
	width : 720,
	modal : true,
	collapsible : true,
	maximizable : true,
	layout : {
		type : 'border'
	},

	title : $locale('fhd.icm.rule.ruletitle'),

	// 多选部门
	multiSelect : false,
	checkModel : 'multiple',

	values : new Array(),
	tree : null,
	grid : null,
	buttons : null,
	//是否显示指标树
	ruleTreeVisible : true,


	
	
	extraParams : {},
	
	//赋值给grid,参数类型为store
	setValue:function(selecteds){
    	var me = this;
    	var value = new Array();
    	if(me.grid){
    		if(Ext.typeOf(selecteds)=='array'){
    			Ext.Array.each(selecteds,function(selected){
                	me.grid.store.insert(me.grid.store.count(),selected);
                	value.push(selected.dbid);
                });
    		}
    		if(Ext.typeOf(selecteds)=='object'){
    			selecteds.each(function(selected){
                	me.grid.store.insert(me.grid.store.count(),selected);
                	value.push(selected.data.dbid);
                });
    		}
    	}
    	me.values=value;
    	this.setTreeValue(value);
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

		if (me.multiSelect) {
			me.checkModel = 'cascade';
		}else{
		    me.checkModel = 'single';
		}
		me.tree = Ext.widget('ruletree', {
					split:true,
					maxWidth:300,
					region : 'west',
					width : 220,
					checkModel : me.checkModel,
					canChecked : true,
					extraParams:me.extraParams,

					multiSelect : me.multiSelect,
					//是否显示指标树
					ruleTreeVisible : me.ruleTreeVisible,
				

					//设置指标树图标
					ruleTreeIcon : me.ruleTreeIcon,
				
					
					onCheckchange : function(node, checked) {
						var value = {
							id : node.data.dbid,
							dbid:node.data.dbid,
							code : node.data.code,
							text : node.data.text
						};
						if (checked) {
							if (!me.multiSelect) {
								me.grid.store.removeAll();
							}
							var id = node.data.id;
							if(id.indexOf("_")!=-1){
								var ids = id.split("_");
								if(ids.length>1)
								{
									id = ids[1];
								}
							}
							if(!me.grid.store.getById(id)&&node.data.type=='rule'){
								me.grid.store.insert(me.grid.store.count(), value);
							}
						} else {
							me.grid.store.remove(me.grid.store.getById(node.data.dbid));
						}
						me.tree.values=new Array();
						me.grid.store.each(function(value) {
							me.tree.values.push(value.data.id);
						})
						me.tree.setTreeValues(checked);
					}
				});
		me.grid = Ext.widget('grid', {
			region : 'center',
			tbar : new Ext.Toolbar({
				height : 25,
				items : [{
							xtype : "tbtext",
							text : $locale('fhd.icm.rule.ruleSelectorwindowTitle')
						}, '-']
			}),
			store : Ext.create('Ext.data.Store', {
						proxy : {
							type : 'ajax',
							reader : {
								type : 'json',
								root : 'users'
							}
						},
						idProperty : 'id',
						fields : ['id', 'code', 'text', 'dbid']
					}),
			columns : [{
						xtype : 'gridcolumn',
						hidden : true,
						dataIndex : 'id'
					}, {
						xtype : 'gridcolumn',
						dataIndex : 'text',
						flex : 1,
						text : $locale("fhd.pages.test.field.name"),
						renderer : function(value, metaData, record, rowIndex,
								colIndex, store) {
							return "<div data-qtitle='' data-qtip='" + value
									+ "'>" + value + "</div>";
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
					items : [me.tree, me.grid]
				});

		me.callParent(arguments);
		me.setValue(me.values);
	}

});
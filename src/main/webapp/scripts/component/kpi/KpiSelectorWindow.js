Ext.define('FHD.ux.kpi.KpiSelectorWindow', {
	extend : 'Ext.window.Window',
	alias : 'widget.kpiselectorwindow',
	constrain:true,
	height : 500,
	width : 720,
	modal : true,
	collapsible : true,
	maximizable : true,
	layout : {
		type : 'border'
	},

	title : $locale('kpiselectorwindow.title'),

	// 单选部门
	single : true,
	checkModel : 'multiple',

	values : new Array(),
	tree : null,
	grid : null,
	buttons : null,
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

		if (me.single) {
			me.checkModel = 'single';
		}
		me.tree = Ext.widget('kpitree', {
					split:true,
					maxWidth:300,
					region : 'west',
					width : 220,
					checkModel : me.checkModel,
					canChecked : true,
					extraParams:me.extraParams,
//					extraParams:{
//						canChecked : false,
//					},
					single : me.single,
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
					
					onCheckchange : function(node, checked) {
						var value = {
							id : node.data.dbid,
							dbid:node.data.dbid,
							code : node.data.code,
							text : node.data.text
						};
						if (checked) {
							if (me.single) {
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
							if(!me.grid.store.getById(id)&&node.data.type=='kpi'){//add by chenxiaozhe 当选择机构时不添加到grid中.
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
							text : $locale('kpiselectorwindow.selectgrid.title')
						}, '-']
			}),
			store : Ext.create('Ext.data.Store', {
						proxy : {
							type : 'ajax',
//							url : __ctxPath + '/kpi/Kpi/listMap',
							reader : {
								type : 'json',
								root : 'users'
							}
						},
						idProperty : 'id',
						fields : ['id', 'code', 'text', 'dbid']
//						autoLoad : true
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
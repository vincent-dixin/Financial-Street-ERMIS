Ext.define('FHD.ux.standard.StandardSelectorWindow',{
	extend : 'Ext.window.Window',
	alias : 'widget.standardselectorwindow',
	
	constrain : true,
	height : 500,
	width : 720,
	modal : true,
	collapsible : true,
	maximizable : true,
	layout : {
		type : 'border'
	},
	title : $locale('standardselectorwindow.title'),
	multiSelect : true,
	checkModel : 'multiple',
	values : new Array(),
	// 是否显示指标树
	standardTreeVisible : true,
	// 设置指标树图标
	standardTreeIcon : 'icon-flag-red',
	extraParams : {},
	
	// 赋值给grid,参数类型为store
	setValue : function(selecteds) {
		var me = this;
		var value = new Array();
		if (me.grid) {
			if (Ext.typeOf(selecteds) == 'array') {
				Ext.Array.each(selecteds, function(selected) {
					me.grid.store.insert(me.grid.store.count(),selected);
					value.push(selected.dbid);
				});
			}
			if (Ext.typeOf(selecteds) == 'object') {
				selecteds.each(function(selected) {
					me.grid.store.insert(me.grid.store.count(),selected);
					value.push(selected.data.dbid);
				});
			}
		}
		me.values = value;
		this.setTreeValue(value);
	},
	setTreeValue : function(values) {
		var me = this;
		if (me.stadardtree) {
			me.stadardtree.values = values;
			me.stadardtree.setTreeValues(true);
		}
	},
	initComponent : function() {
		var me = this;
		
		me.stadardtree = Ext.create('FHD.ux.standard.StandardTree',{
			border:false,
			split : true,
			maxWidth : 300,
			region : 'west',
			width : 220,
			canChecked : true,
			checkable:true,
			extraParams : me.extraParams,
			myType:me.myType,
			multiSelect : me.multiSelect,
			// 是否显示指标树
			standardTreeVisible : me.standardTreeVisible,
			// 设置指标树图标
			standardTreeIcon : me.standardTreeIcon,
		
			onCheckchange : function(node,checked) {
				var value = {
					id : node.data.id,
					dbid : node.data.dbid,
					code : node.data.code,
					text : node.data.text,
					controlPoint :node.data.controlPoint
				};
				var gridStore=null;
			    gridStore=me.grid.store;
			    if(me.myType=='standard'){
					if (checked) {
						if (!me.multiSelect) {
							gridStore.removeAll();
						}
						var id = node.data.id;
						var flag = true;//标识是否有重复Id
						gridStore.each(function(value) {
							if (value.data.id == id) {
								flag = false;
							}
						});
						if (flag) {//grid中没有重复id才向其插入
							gridStore.insert(gridStore.getCount(),value);
						}
					} else {
						//如果是取消选中树节点的状态，则将grid中id匹配的数据移除
						gridStore.remove(gridStore.getById(node.data.dbid));
					}		
					me.stadardtree.values = new Array();
					gridStore.each(function(value) {
						me.stadardtree.values.push(value.data.id);
					});
					me.stadardtree.setTreeValues(checked);
				}
			},
	
			//左键点击事件
			clickFunction:function(node){
				if(me.myType=='standard') return;
				var clickedNodeIds = [];
				clickedNodeIds.push(node.data.id);
				if(clickedNodeIds.length){
					var temp = clickedNodeIds.toString();
					standardStore.proxy.url= __ctxPath+'/standard/standardGrid/findStandardByPage.f?clickedNodeId='+temp;
					standardStore.load(); 
					clickedNodeIds = null;
				}else{
					standardStore.removeAll();
				}
			}
		});
						
		var standardStore = Ext.create('Ext.data.Store',{
        	//storeId:'standardStores',
        	pageSize: FHD.pageSize,
        	idProperty: 'idd',
        	autoLoad: false,
        	fields:['id', 'text','code','controlPoint'],
        	proxy: {
		        type: 'ajax',
		        url: __ctxPath+'/standard/standardGrid/findStandardByPage.f',
		        extraParams: { start: 0, limit: 20},
		        reader: {
		            type : 'json',
		            root : 'datas',
		            totalProperty :'totalCount'
		        }
		    }
        });
			        
		//存放选中树节点的数据
		me.standardgrild=Ext.create('Ext.grid.Panel',{
            flex: 1,
            loadMask: true,
            store : standardStore,
            features: [{
            	ftype: 'filters',
				autoReload: false, //don't reload automatically
				local: true, //only filter locally
				fields:['id', 'text']
            }],
            columns: [
				{
					xtype : 'gridcolumn',
					hidden : true,
					dataIndex : 'id'
				},
				{
					xtype : 'gridcolumn',
					dataIndex : 'code',
					flex : 1,
					text : '编号',
					renderer : function(value,metaData,record,rowIndex,colIndex, store) {
						return "<div data-qtitle='' data-qtip='"+ value+ "'>"+ value+ "</div>";
					}
				},
				{
					xtype : 'gridcolumn',
					dataIndex : 'text',
					flex : 3,
					text : $locale("fhd.pages.test.field.name"),
					renderer : function(value,metaData,record,rowIndex,colIndex, store) {
						return "<div data-qtitle='' data-qtip='"+ value+ "'>"+ value+ "</div>";
					}
				},
				{
					xtype : 'gridcolumn',
					dataIndex : 'controlPoint',
					flex : 1,
					text : '内控要素',
					renderer : function(value,metaData,record,rowIndex,colIndex, store) {
						return "<div data-qtitle='' data-qtip='"+ value+ "'>"+ value+ "</div>";
					}
				}
			],
            tbar : ['可选列表']
        });
		
		me.standardgrild.on('select',function(t,r,i,o){
			var gridStore=me.grid.store;
			var thisId=r.data.id
			var flag=true;
			gridStore.each(function(n){
				if(thisId==n.data.id){
					flag=false;
				}
			});
			if(!me.multiSelect && me.myType=='required'){
				gridStore.removeAll();   
			}
			if(flag){
				gridStore.add(r);   
			}
		});
					
		me.grid = Ext.widget('grid',{
			region : 'center',
			flex:1,
			tbar: [FHD.locale.get('standardselectorwindow.selectgrid.title')],
			store : me.myType=='standard'?standardStore:null,
			columns : [
				{
					xtype : 'gridcolumn',
					hidden : true,
					dataIndex : 'id'
				},
				{
					xtype : 'gridcolumn',
					dataIndex : 'code',
					flex : 1,
					text : '编号',
					renderer : function(value,metaData,record,rowIndex,colIndex, store) {
						return "<div data-qtitle='' data-qtip='"+ value+ "'>"+ value+ "</div>";
					}
				},
				{
					xtype : 'gridcolumn',
					dataIndex : 'text',
					flex : 3,
					text : $locale("fhd.pages.test.field.name"),
					renderer : function(value,metaData,record,rowIndex,colIndex, store) {
						return "<div data-qtitle='' data-qtip='"+ value+ "'>"+ value+ "</div>";
					}
				},
				{
					xtype : 'gridcolumn',
					dataIndex : 'controlPoint',
					flex : 1,
					text : '内控要素',
					hidden:false,
					renderer : function(value,metaData,record,rowIndex,colIndex, store) {
						return "<div data-qtitle='' data-qtip='"+ value+ "'>"+ value+ "</div>";
					}
				},
				{
					xtype : 'templatecolumn',
					text : $locale('fhd.common.delete'),
					flex : 1,
					align : 'center',
					tpl : '<font class="icon-del-min" style="cursor:pointer;">&nbsp&nbsp&nbsp&nbsp</font>',
					listeners : {
						click : {
							fn : function(grid,d, i) {
								var nodes = me.stadardtree.standardTree.getChecked();
								if(me.myType=='standard'){
									Ext.each(nodes,function(n) {
										if (n.data.id == grid.store.getAt(i).data.id) {
											n.set("checked",false);
								        }
							        });
								}
									
								grid.store.removeAt(i);
							}
						}
					}
				}
			]
		});
		me.buttons = [{
			xtype : 'button',
			text : $locale('fhd.common.confirm'),
			handler : function() {
				me.onSubmit(me.grid.store);
				me.grid.store.removeAll();
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
		
		me.list = Ext.create('Ext.container.Container',{
			layout: {
                align: 'stretch',
                type: 'vbox'
            },
            region: 'center'
		});
		
		Ext.applyIf(me, {
			layout: {
                type: 'border'
            }
		});
			
		me.callParent(arguments);
		
		//me.list.removeAll();
		if(me.myType=='standard'){
			me.grid.down('[dataIndex=controlPoint]').hide();
			me.list.add(me.grid);
		}else if(me.myType=='required'){
			me.grid.down('[dataIndex=controlPoint]').show();
			me.list.add(me.standardgrild);
			me.list.add(me.grid);
		}
		
		me.add(me.stadardtree);
		me.add(me.list);
		//me.setValue(me.values);
	}
});
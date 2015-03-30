Ext
		.define(
				'pages.icm.defect.defectLevelSelectorWindow',
				{
					extend : 'Ext.window.Window',
					alias : 'widget.defectLevelselectorwindow',
					constrain : true,
					height : 500,
					width : 720,
					modal : true,
					collapsible : true,
					maximizable : true,
					layout : {
						type : 'border'
					},

					title :'缺陷选择',

					// 单选部门
					multiSelect : true,
					mytype:1,
					checkModel : 'multiple',

					values : new Array(),
					tree : null,
					grid : null,
					standardgrild:null,
					buttons : null,
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
						me.stadardtree = Ext.create('pages.icm.defect.defectLevelTree',
										{
											split : true,
											maxWidth : 300,
											region : 'west',
											width : 220,
											canChecked : true,
											extraParams : me.extraParams,
											mytype:me.mytype,
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
														text : node.data.text
													};
											var gridStore=null;
										    gridStore=me.grid.store;
									if (checked) {
										  if (!me.multiSelect) {
											  me.grid.store.removeAll();
											}
											 var id = node.data.id;
											 var flag = true;//标识是否有重复Id
											 gridStore.each(function(value) {
											   if (value.data.id == id) {
													flag = false;
												  }
												})
											   if (flag) {//grid中没有重复id才向其插入
													gridStore.insert(gridStore.count(),value);
											    }
										} else {
											//如果是取消选中树节点的状态，则将grid中id匹配的数据移除
											gridStore.remove(gridStore.getById(node.data.dbid));
										}		
											me.stadardtree.values = new Array();
											gridStore.each(function(value) {
												me.stadardtree.values.push(value.data.id);
											})
												me.stadardtree.setTreeValues(checked);
											},
											clickFunction:function(node){
												if(me.extraParams.myType=='1'){
													var isLeaf="0";
													if(node.isLeaf())
													{
														isLeaf="1";
													}
													var clickedNodeId=node.data.id;
													 if(clickedNodeId!=''&&me.mytype==1&&clickedNodeId!=1){
														 //重构grid加载器的url并且触发让其读取数据
														 standardStore.proxy.url= __ctxPath+'/defect/defectTree/findrootDefectTreeLoader.f?clickedNodeId='+clickedNodeId+'&isLeaf='+isLeaf;
														 standardStore.load(); 
													 }
												}
											}
										});
						
						var standardStore = Ext.create('Ext.data.Store',{
				        	storeId:'standardStores',
				        	pageSize: FHD.pageSize,
				        	idProperty: 'idd',
				        	autoLoad: false,
				        	fields:['id', 'text'],
				        	proxy: {
						        type: 'ajax',
						        url:'',
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
				            store:standardStore,
				            features: [{
				            	ftype: 'filters',
								autoReload: false, //don't reload automatically
								local: true, //only filter locally
								fields:['id', 'text'],
				            }],
				            columns: [
										{
											xtype : 'gridcolumn',
											hidden : true,
											dataIndex : 'id'
										},
										{
											xtype : 'gridcolumn',
											dataIndex : 'text',
											flex : 1,
											text : $locale("fhd.pages.test.field.name"),
											renderer : function(
													value,
													metaData,
													record,
													rowIndex,
													colIndex, store) {
												return "<div data-qtitle='' data-qtip='"+ value+ "'>"+ value+ "</div>";
											},
											listeners : {
												click : {
													fn : function(standardgrild,d, i) {
													  var gridStore=me.grid.store;
													  var thisId=standardgrild.store.getAt(i).data.id
													  var flag=true;
													  gridStore.each(function(n){
														  if(thisId==n.data.id){
															  flag=false;
														  }
													  });
													  if(!me.multiSelect&&me.mytype==1){
														  gridStore.removeAll();   
													  }
													  if(flag){
														  gridStore.add(standardgrild.store.getAt(i));   
													  }
													}
												}
											}
										}
				            ],
				            tbar : ['<b>' + FHD.locale.get('standardCanSelectedList.title') + '</b>', '->',

							],
				            viewConfig: {

				            },

				        });
						
						me.grid = Ext.widget('grid',{
											region : 'center',
											flex:1,
											tbar : new Ext.Toolbar({
														height : 25,
														items : [
															    {
																	xtype : "tbtext",
																	text : $locale('standardselectorwindow.selectgrid.title')
																}, '-' ]
													}),
											store : Ext.create(
													'Ext.data.Store', {
														proxy : {
															type : 'ajax',
															reader : {
																type : 'json',
																root : 'users'
															}
														},
														idProperty : 'id',
														fields : [ 'id',
																'code', 'text',
																'dbid' ]
													// autoLoad : true
													}),
											columns : [
													{
														xtype : 'gridcolumn',
														hidden : true,
														dataIndex : 'id'
													},
													{
														xtype : 'gridcolumn',
														dataIndex : 'text',
														flex : 1,
														text : $locale("fhd.pages.test.field.name"),
														renderer : function(
																value,
																metaData,
																record,
																rowIndex,
																colIndex, store) {
															return "<div data-qtitle='' data-qtip='"+ value+ "'>"+ value+ "</div>";
														}
													},
													{
														xtype : 'templatecolumn',
														text : $locale('fhd.common.delete'),
														align : 'center',
														tpl : '<font class="icon-del-min" style="cursor:pointer;">&nbsp&nbsp&nbsp&nbsp</font>',
														listeners : {
															click : {
																fn : function(grid,d, i) {
																		
																		var nodes = me.stadardtree.standardTree
																		.getChecked();
																		if(me.mytype==0){
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
													} ]
										});
						me.buttons = [ {
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
						} ];
						var itemContainer=[me.standardgrild,me.grid];
						if(me.mytype==0){
							itemContainer=[me.grid];
						}
						Ext.applyIf(me, {
							items : [ me.stadardtree,{
			                    xtype: 'container',
			                    activeItem: 0,
			                    layout: {
			                        type: 'border'
			                    },
			                    region: 'center',
			                    items: [
			                        {
			                            xtype: 'container',
			                            layout: {
			                                align: 'stretch',
			                                type: 'vbox'
			                            },
			                            region: 'center',
			                            items: itemContainer
			                        }
			                    ]
							}]
						});
						me.callParent(arguments);
						me.setValue(me.values);
					}

				});
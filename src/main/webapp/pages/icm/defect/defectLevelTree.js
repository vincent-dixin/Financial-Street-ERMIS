Ext
		.define(
				'pages.icm.defect.defectLevelTree',
				{
					extend : 'Ext.Panel',
					alias : 'widget.defectLeveltree',
					requires : [ 'Ext.tree.Panel' ],
					standardTreeVisible : true,
					animate : false,
					mytype:'',
					multiSelect :true,
					standardTreeRoot : {},

					standardTreeIcon : 'icon-flag-red',
                    mytype:1,
					//standardTreeTitle :FHD.locale.get('standardtreeorgstandard.title'),

					standardTree : null,

					extraParams : {
						canChecked : true,
						type:1
					},
					items : new Array(),
					values : new Array(),
					onCheckchange : function() {
					},
					/* 树节点点击事件 */
					clickFunction : function() {
					},
					checkModel : 'cascade',

					standardTreeContextMenu : function() {
					}, // 指标树右键菜单

					/* 方法 */
					initValue : function() {
						var me = this;
						if (me.standardTreeVisible) {// 内控标准树是否可用
											me.standardTreeRoot = {
									    	        text: '缺陷等级',
									    	        id: '',
									    	        expanded: true
									    	    };
											if(me.multiSelect){
												me.checkModel='cascade';
											}
											else if(me.multiSelect==false){
												me.checkModel='single';
											}
											me.standardTree = me
													.createTree({
														'standardTreeVisible' : me.standardTreeVisible,
														'onCheckchange' : me.onCheckchange,
														'extraParams' : me.extraParams,
														'standardUrl' : __ctxPath
																+ '/defect/defectTree/findrootDefectLevelTreeLoader.f?companyId='+__user.companyId,
														'titleIcon' : me.standardTreeIcon,
														'title' : me.standardTreeTitle,
														'clickFunction' : me.clickFunction,
														'values' : me.values,
														'checkModel' : me.checkModel,
														'root' :me.standardTreeRoot,
														'contextItemMenuFc' : me.standardTreeContextMenu,
														'animate' : me.animate
													});
											me.add(me.standardTree);
						}
					},

					setTreeValues : function(checked) {
						if (this.standardTree) {
							console.log(this.standardTree.getRootNode());
							this.standardTree.values = this.values;
							this.standardTree.setChecked(this.standardTree.getRootNode(), checked);
						}
					},

					createTree : function(o) {
						return Ext.create('FHD.ux.TreePanel', {
							iconCls : o.titleIcon,
							title : o.title,
							extraParams : o.extraParams,
							onCheckchange : o.onCheckchange,
							url : o.standardUrl,
							rootVisible : true,
							myexpand : false,
							canSelect : 'standard',
							checkModel : o.checkModel,
							useSplitTips : true,
							searchParamName : 'query',
							values : o.values,
							animate : o.animate,
							viewConfig : {
								//stripeRows : true,
								listeners : {
									itemcontextmenu : function(view, rec, node,index, e) {
										e.stopEvent();
										var menu = o.contextItemMenuFc(rec, e);
										if (menu) {
											menu.showAt(e.getPoint());
										}
										return false;
									}
								}
							},
							listeners : {
								itemclick : function(node, record, item) {
									o.clickFunction(record);
								},
								beforeLoad : function(node) {

								}
							},
							root : o.root,
							check : function(thiz, item, check) {
								this.setNodeChecked(item.data.dbid, check);
								this.onCheckchange(item, check);
							}
						})
					},
					initComponent : function() {
						// Ext.define('ObjMap', {
						// extend: 'Ext.data.Model',
						// fields:['standard', 'sm', 'org']
						// });
						var me = this;
						Ext.applyIf(me, {
							layout : {
								type : 'card'
							},
							items : me.items,
							//title : '内控标准树'//FHD.locale.get('standardtree.title')
						});
						me.callParent(arguments);
						me.initValue();
					}
				});
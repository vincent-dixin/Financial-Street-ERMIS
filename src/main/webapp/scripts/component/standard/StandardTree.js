Ext.define('FHD.ux.standard.StandardTree',{
					extend : 'Ext.Panel',
					alias : 'widget.standardtree',
					requires : [ 'Ext.tree.Panel' ],
					standardTreeVisible : true,
					animate : false,
					multiSelect :true,
					standardTreeRoot : {},

					standardTreeIcon : 'icon-flag-red',
					//standardTreeTitle :FHD.locale.get('standardtreeorgstandard.title'),

					standardTree : null,

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
									    	        text: '内控标准库',
									    	        id: '',
									    	        expanded: true
									    	    };
											if(me.multiSelect){
												me.checkModel='cascade';
											}
											else if(me.multiSelect==false){
												me.checkModel='single';
											}
											me.standardTree = me.createTree({
														'standardTreeVisible' : me.standardTreeVisible,
														'onCheckchange' : me.onCheckchange,
														'extraParams' : me.extraParams,
														'standardUrl' : __ctxPath + '/standard/standardTree/findStandardTreeLoader.do?companyId='+__user.companyId,
														'titleIcon' : me.standardTreeIcon,
														'title' : me.standardTreeTitle,
														'clickFunction' : me.clickFunction,
														'multiSelect':me.multiSelect,
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
							multiSelect : o.multiSelect,
							url : o.standardUrl,
							rootVisible : true,
							myexpand : false,
							canSelect : 'standard',
							checkModel : o.checkModel,
							checkable:true,
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
					me.extraParams = {
						canChecked : true,
						myType:me.myType
					},
						Ext.applyIf(me, {
							layout : {
								type : 'card'
							},
							items : me.items,
							extraParams : me.extraParams
							//title : '内控标准树'//FHD.locale.get('standardtree.title')
						});
						me.callParent(arguments);
						me.initValue();
					}
				});
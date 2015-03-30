Ext.define('FHD.ux.process.processTree', {
	extend : 'Ext.Panel',
	alias : 'widget.processTree',
	requires : ['Ext.tree.Panel'],
	// collapsible : true,
	processTreeVisible : true,
	animate : false,

	myexpand : true,	//默认是不是展开树节点 郑军祥添加
	
	processTreeRoot : {},

	processTreeIcon : 'icon-flag-red',

	processTreeTitle :'',// FHD.locale.get('fhd.process.processtree.title'),

	processTree : null,

	extraParams : {
		canChecked : true
	},
	items : new Array(),
	values : new Array(),
	onCheckchange : function() {
	},
	/* 树节点点击事件 */
	clickFunction : function() {
	},
	checkModel : 'cascade',

	processcontextmenu : function() {
	}, // 指标树右键菜单
	standardOrgTreelContextMenu : function() {
	}, // 机构指标树右键菜单
	standardSmTreeContextMenu : function() {
	}, // 目标指标树右键菜单
	standardMineTreeContextMenu : function() {
	}, // 我的指标树右键菜单

	/* 方法 */
	initValue : function() {
		var me = this;

		if (me.single) {
			me.checkModel = 'single';
		} else {
			me.checkModel = 'cascade';
		}

		if (me.processTreeVisible) {// 流程树是否可用
			
			me.processTreeRoot = {
		        text: '流程库',
		        dbid: 'process_root',
		        leaf: false,
		        code: 'category',
		        type: '',
		        expanded: me.myexpand
			};
			me.processTree = me.createTree({
						'processTreeVisible' : me.processTreeVisible,
						'onCheckchange' : me.onCheckchange,
						'extraParams' : me.extraParams,
						'processUrl' : __ctxPath
								+ '/process/processTree/findrootProcessTreeLoader.f',
						'titleIcon' : me.processTreeIcon,
						'title' : me.processTreeTitle,
						'clickFunction' : me.clickFunction,
						'values' : me.values,
						'checkModel' : me.checkModel,
						'root' : me.processTreeRoot,
						'contextItemMenuFc' : me.processTreeContextMenu,
						'animate' : me.animate
					});
			me.add(me.processTree);
		}
	},

	setTreeValues : function(checked) {
		if (this.processTree) {
			this.processTree.values = this.values;
			this.processTree
					.setChecked(this.processTree.getRootNode(), checked);
		}

	},

	createTree : function(o) {
		return Ext.create('FHD.ux.TreePanel', {
					iconCls : o.titleIcon,
					title : o.title,
					extraParams : o.extraParams,
					onCheckchange : o.onCheckchange,
					url : o.processUrl,
					rootVisible : true,
					myexpand : false,
					canSelect : 'standard',
					checkModel : o.checkModel,
					searchParamName : 'query',
					values : o.values,
					animate : o.animate,
					viewConfig : {
						stripeRows : true,
						listeners : {
							itemcontextmenu : function(view, rec, node, index,
									e) {
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
			}
				// items : me.items,
				// title : '流程树222'//FHD.locale.get('processtree.title')
			});
		me.callParent(arguments);
		me.initValue();
	}
});
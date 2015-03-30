Ext.define('FHD.view.monitor.MainContainer', {
	extend : 'FHD.view.monitor.TreeCardFace',
	alias : 'widget.monitormaincontainer',

	/**
	 * 右侧card布局panel重新设置激活面板并布局
	 */
	reRightLayout : function(c) {
		var me = this;
		me.rightContainer.setActiveItem(c);
		me.rightContainer.doLayout();

	},

	/**
	 * 创建左侧记分卡树
	 */
	createScTree : function() {
		var me = this;
		me.scTree = Ext.create('FHD.view.monitor.sc.ScTree', {
			id : 'sctree',
			treeTitle : '记分卡',
			treeIconCls : 'icon-ibm-icon-scorecards',
			monitorContainer:me,
			onClick : function() {
				me.createScMainContainer();
			}
		});
	},

	/**
	 * 创建右侧记分卡主容器
	 */
	createScMainContainer : function() {
		var me = this;
		if (!me.scMainContainer) {
			me.scMainContainer = Ext.create('FHD.view.monitor.sc.scMainContainer', {
						id : 'scorecardmainpanel',
						navid:'scorecardtabcontainer',
						sctab:Ext.create('FHD.view.monitor.sc.scTab',{
							flex:1,id:'scorecardtab',
							monitorContainer:me
						})
					});
			me.rightContainer.items.add(me.scMainContainer);
		}
		me.reRightLayout(me.scMainContainer);
	},

	/**
	 * 创建右侧战略目标主面板
	 */

	createStrMainContainer : function(me) {
		if (!me.strMainContainer) {
			me.strMainContainer = Ext.create('Ext.panel.Panel', {
				title : 'strMainContainer'
			});
			me.rightContainer.items.add(me.strMainContainer);
		}
		me.reRightLayout(me.strMainContainer);
	},

	/**
	 * 创建战略目标树容器
	 */
	createStrTreeContainer : function() {
		var me = this;
		me.strTreeContainer = Ext.create('Ext.container.Container', {
			treeTitle : '目标',
			treeIconCls : 'icon-strategy',
			layout : 'fit',
			onClick : function() {
				me.createStrTree(me, this);// 创建左侧树

				me.createStrMainContainer(me);// 创建右侧战略目标主容器

			}
		});

	},

	/**
	 * 创建战略目标树
	 */

	createStrTree : function(me, c) {
		if (!me.strTree) {
			me.strTree = Ext.create('FHD.view.monitor.str.StrTree', {
				id : 'strtree'
			});
			c.add(me.strTree);
			c.doLayout();
		}
	},
	/**
	 * 获得当前激活的树ID
	 */
	getActiveTreeId:function(){
		var me = this;
		return me.getActiveTree().id;
	},
	
	/**
	 * 获得当前激活的树
	 */
	getActiveTree:function(){
		var me = this;
		return me.accordionTree.accordionCard.getActiveItem();
	},

	initComponent : function() {
		var me = this;

		// 记分卡树
		me.createScTree();

		// 目标树
		me.createStrTreeContainer();

		me.accordionTree = Ext.create("FHD.ux.layout.AccordionTree", {
			title : '记分卡',
			iconCls : 'icon-ibm-icon-scorecards',
			width : 250,
			treeArr : [ me.scTree, me.strTreeContainer ]
		});

		Ext.apply(me, {
			tree : me.accordionTree
		});

		me.callParent(arguments);

		me.createScMainContainer();

	}

})
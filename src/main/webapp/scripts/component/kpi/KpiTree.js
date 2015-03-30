Ext.define('FHD.ux.kpi.KpiTree', {
	extend : 'Ext.Panel',
	alias : 'widget.kpitree',
	requires : ['Ext.tree.Panel'],
	collapsible : true,
	kpiTreeVisible : true,
	kpiOrgTreeVisible : false,
	kpiSmTreeVisible : true,
	kpiMineTreeVisible : true,
	animate:false,

	kpiTreeRoot : {},
	kpiOrgTreeRoot : {},
	kpiSmTreeRoot : {},
	kpiMineTreeRoot : {},

	kpiTreeIcon : 'icon-flag-red',
	kpiOrgTreelIcon : 'icon-org',
	kpiSmTreeIcon : 'icon-flag-red',
	kpiMineTreeIcon : 'icon-orgsub',

	kpiTreeTitle : FHD.locale.get('kpitreekpi.title'),
	kpiOrgTreelTitle : FHD.locale.get('kpitreeorgkpi.title'),
	kpiSmTreeTitle : FHD.locale.get('kpitreestrategykpi.title'),
	kpiMineTreeTitle : FHD.locale.get('kpitreemykpi.title'),

	kpiTree : null,
	kpiOrgTreel : null,
	kpiSmTree : null,
	kpiMineTree : null,

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

	kpiTreeContextMenu : function() {
	}, // 指标树右键菜单
	kpiOrgTreelContextMenu : function() {
	}, // 机构指标树右键菜单
	kpiSmTreeContextMenu : function() {
	}, // 目标指标树右键菜单
	kpiMineTreeContextMenu : function() {
	}, // 我的指标树右键菜单

	/* 方法 */
	initValue : function() {
		var me = this;
		FHD.ajax( {
			url : __ctxPath + '/kpi/KpiTree/findrootbycompany',
			callback : function(objectMaps) {
				Ext.Array.each(objectMaps, function(object) {
					//me.kpiTreeRoot = eval(object.kpi);
					/*指标树添加假根*/
					me.kpiTreeRoot = {
                            "id": "kpi_root",
                            "text": FHD.locale.get('fhd.kpi.kpitree.kpis'),
                            "dbid": "kpi_root",
                            "leaf": false,
                            "code": "kpi",
                            "type": "kpi",
                            "expanded":false
                        };
					//me.kpiSmTreeRoot = eval(object.sm);
					me.kpiSmTreeRoot = {
                            "id": "sm_root",
                            "text": FHD.locale.get('fhd.sm.strategymaps'),
                            "dbid": "sm_root",
                            "leaf": false,
                            "code": "sm",
                            "type": "sm",
                            "expanded":false
                        };
					me.kpiOrgTreeRoot = eval(object.org);
					me.kpiMineTreeRoot = {
                            "id": "kpi_root",
                            "text": FHD.locale.get('fhd.kpi.kpitree.kpis'),
                            "dbid": "kpi_root",
                            "leaf": false,
                            "code": "kpi",
                            "type": "kpi",
                            "expanded":false
                        };
				});
				if (me.single) {
					me.checkModel = 'single';
				} else {
					me.checkModel = 'cascade';
				}
				if (me.kpiMineTreeVisible) {
					me.kpiMineTree = me.createTree( {
						'kpiTreeVisible' : me.kpiMineTreeVisible,
						'onCheckchange' : me.onCheckchange,
						'extraParams' : me.extraParams,
						'kpiUrl' : __ctxPath + '/kpi/KpiTree/minetreeloader',
						'titleIcon' : me.kpiMineTreeIcon,
						'title' : me.kpiMineTreeTitle,
						'clickFunction' : me.clickFunction,
						'values' : me.values,
						'checkModel' : me.checkModel,
						'root' : me.kpiMineTreeRoot,
						'contextItemMenuFc' : me.kpiMineTreeContextMenu,
						'animate':me.animate
					});
					me.add(me.kpiMineTree);
				}
				if (me.kpiOrgTreeVisible) {
					me.kpiOrgTree = me.createTree( {
						'kpiTreeVisible' : me.kpiOrgTreeVisible,
						'onCheckchange' : me.onCheckchange,
						'extraParams' : me.extraParams,
						'kpiUrl' : __ctxPath + '/kpi/KpiTree/orgtreeloader',
						'titleIcon' : me.kpiOrgTreelIcon,
						'title' : me.kpiOrgTreelTitle,
						'clickFunction' : me.clickFunction,
						'values' : me.values,
						'checkModel' : me.checkModel,
						'root' : me.kpiOrgTreeRoot,
						'contextItemMenuFc' : me.kpiOrgTreelContextMenu,
						'animate':me.animate
					});
					me.add(me.kpiOrgTree);
				}
				if (me.kpiSmTreeVisible) {
					me.kpiSmTree = me.createTree( {
						'kpiTreeVisible' : me.kpiSmTreeVisible,
						'onCheckchange' : me.onCheckchange,
						'extraParams' : me.extraParams,
						'kpiUrl' : __ctxPath
								+ '/kpi/KpiTree/strategyMapTreeLoader',
						'titleIcon' : me.kpiSmTreeIcon,
						'title' : me.kpiSmTreeTitle,
						'clickFunction' : me.clickFunction,
						'values' : me.values,
						'root' : me.kpiSmTreeRoot,
						'checkModel' : me.checkModel,
						'contextItemMenuFc' : me.kpiSmTreeContextMenu,
						'animate':me.animate
					});
					me.add(me.kpiSmTree);
				}
				if (me.kpiTreeVisible) {
					me.kpiTree = me.createTree( {
						'kpiTreeVisible' : me.kpiTreeVisible,
						'onCheckchange' : me.onCheckchange,
						'extraParams' : me.extraParams,
						'kpiUrl' : __ctxPath + '/kpi/KpiTree/kpitreeloader',
						'titleIcon' : me.kpiTreeIcon,
						'title' : me.kpiTreeTitle,
						'clickFunction' : me.clickFunction,
						'values' : me.values,
						'checkModel' : me.checkModel,
						'root' : me.kpiTreeRoot,
						'contextItemMenuFc' : me.kpiTreeContextMenu,
						'animate':me.animate
					});
					me.add(me.kpiTree);
				}
			}
		});
	},

	setTreeValues : function(checked) {
		if (this.kpiTree) {
			this.kpiTree.values = this.values;
			this.kpiTree.setChecked(this.kpiTree.getRootNode(), checked);
		}
		if (this.kpiOrgTree) {
			this.kpiOrgTree.values = this.values;
			this.kpiOrgTree.setChecked(this.kpiOrgTree.getRootNode(), checked);
		}
		if (this.kpiSmTree) {
			this.kpiSmTree.values = this.values;
			this.kpiSmTree.setChecked(this.kpiSmTree.getRootNode(), checked);
		}
		if (this.kpiMineTree) {
			this.kpiMineTree.values = this.values;
			this.kpiMineTree
					.setChecked(this.kpiMineTree.getRootNode(), checked);
		}
	},

	createTree : function(o) {
		return Ext.create('FHD.ux.TreePanel', {
			iconCls : o.titleIcon,
			title : o.title,
			extraParams : o.extraParams,
			onCheckchange : o.onCheckchange,
			url : o.kpiUrl,
			rootVisible : true,
			myexpand : false,
			canSelect : 'kpi',
			checkModel : o.checkModel,
			values : o.values,
			animate:o.animate,
			viewConfig : {
				stripeRows : true,
				listeners : {
					itemcontextmenu : function(view, rec, node, index, e) {
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
//		Ext.define('ObjMap', {
//		    extend: 'Ext.data.Model',
//		    fields:['kpi', 'sm', 'org']
//		});
		var me = this;
		Ext.applyIf(me, {
			layout : {
				type : 'accordion'
			},
			items : me.items,
			title : FHD.locale.get('kpitree.title')
		});
		me.callParent(arguments);
		me.initValue();
	}
});
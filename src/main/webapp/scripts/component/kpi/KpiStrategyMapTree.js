Ext.define('FHD.ux.kpi.KpiStrategyMapTree', {
	extend : 'Ext.Panel',
	border:false,
	alias : 'widget.kpistrategymaptree',
	requires : [ 'Ext.tree.Panel' ],

	OrgSmTreeVisible : true,
	smTreeVisible : true,
	mineSmTreeVisible : true,
	collapsible : true,
	OrgSmTreeRoot : {},
	smTreeRoot : {},
	mineSmTreeRoot : {},
	animate:false,

	orgSmTreeIcon : 'icon-org',
	smTreeIcon : 'icon-flag-red',
	mineSmTreeIcon : 'icon-orgsub',

	orgSmTreeTitle : FHD.locale.get('orgkpistrategymaptree.title'),
	smTreeTitle : FHD.locale.get('strategymaptree.title'),
	mineSmTreeTitle : FHD.locale.get('mykpistrategymaptree.title'),

	orgSmTree : null,
	smTree : null,
	mineSmTree : null,
	dateFieldable:false,

	extraParams : {
		canChecked : true,
		smIcon:''//目标显示的图标
	},
	items : new Array(),
	values : new Array(),
	onCheckchange : function() {
	},

	mineClickFunction : function() {
	},
	smClickFunction:function(){
		
	},
	orgClickFunction : function(){
	},
	checkModel : 'cascade',

	orgSmTreeContextMenuFc : function(tree, node) {
	},
	smTreeContextMenuFc : function(tree, node) {
	},
	mineSmTreeContextMenuFc : function(tree, node) {
	},
	monthClick:function(menu,item,e,eOpts){
	},

	/* 方法 */
	initValue : function() {
		var me = this;
		FHD.ajax( {
			url : __ctxPath + '/kpi/KpiTree/findrootbycompany',
			callback : function(objectMaps) {
				Ext.Array.each(objectMaps, function(object) {
					me.OrgSmTreeRoot = eval(object.org);
					me.mineSmTreeRoot = {
                            "id": "sm_root",
                            "text": FHD.locale.get('fhd.sm.strategymaps'),
                            "dbid": "sm_root",
                            "leaf": false,
                            "code": "sm",
                            "type": "sm",
                            "expanded":false
                        };
					//me.mineSmTreeRoot = eval(object.sm);
					//me.smTreeRoot = eval(object.sm);
					/*目标树添加假根*/
					me.smTreeRoot = {
                            "id": "sm_root",
                            "text": FHD.locale.get('fhd.sm.strategymaps'),
                            "dbid": "sm_root",
                            "leaf": false,
                            "code": "sm",
                            "type": "sm",
                            "expanded":false
                        };
				});
				if (me.single) {
					me.checkModel = 'single';
				} else {
					me.checkModel = 'cascade';
				}
				if (me.mineSmTreeVisible) {
					me.mineSmTree = me.createTree( {
						'onCheckchange' : me.onCheckchange,
						'extraParams' : me.extraParams,
						'kpiUrl' : __ctxPath
								+ '/kpi/KpiStrategyMapTree/minetreeloader',
						'titleIcon' : me.mineSmTreeIcon,
						'title' : me.mineSmTreeTitle,
						'clickFunction' : me.mineClickFunction,
						'values' : me.values,
						'checkModel' : me.checkModel,
						'root' : me.mineSmTreeRoot,
						'contextItemMenuFc' : me.mineSmTreeContextMenuFc
						,'monthClick':me.monthClick
						,'dateFieldable':me.dateFieldable
						,'animate':me.animate
					});
					me.add(me.mineSmTree);
				}
				if (me.OrgSmTreeVisible) {
					me.orgSmTree = me.createTree( {
						'onCheckchange' : me.onCheckchange,
						'extraParams' : me.extraParams,
						'kpiUrl' : __ctxPath
								+ '/kpi/KpiStrategyMapTree/orgtreeloader',
						'titleIcon' : me.orgSmTreeIcon,
						'title' : me.orgSmTreeTitle,
						//'clickFunction' : me.clickFunction,
						'clickFunction' : me.orgClickFunction,
						'values' : me.values,
						'checkModel' : me.checkModel,
						'root' : me.OrgSmTreeRoot,
						'contextItemMenuFc' : me.orgSmTreeContextMenuFc
						,'dateFieldable':me.dateFieldable
						,'animate':me.animate
					});
					me.add(me.orgSmTree);
				}
				if (me.smTreeVisible) {
					me.smTree = me.createTree( {
						'onCheckchange' : me.onCheckchange,
						'extraParams' : me.extraParams,
						'kpiUrl' : __ctxPath
								+ '/kpi/KpiStrategyMapTree/treeloader',
						'titleIcon' : me.smTreeIcon,
						'title' : me.smTreeTitle,
						'clickFunction' : me.smClickFunction,
						'values' : me.values,
						'checkModel' : me.checkModel,
						'root' : me.smTreeRoot,
						'contextItemMenuFc' : me.smTreeContextMenuFc
						,'dateFieldable':me.dateFieldable
						,'animate':me.animate
					});
					me.add(me.smTree);
				}
			}
		});
	},
	setTreeValues : function(checked) {
		if (this.mineSmTree) {
			this.mineSmTree.values = this.values;
			this.mineSmTree.setChecked(this.mineSmTree.getRootNode(), checked);
		}
		if (this.orgSmTree) {
			this.orgSmTree.values = this.values;
			this.orgSmTree.setChecked(this.orgSmTree.getRootNode(), checked);
		}
		if (this.smTree) {
			this.smTree.values = this.values;
			this.smTree.setChecked(this.smTree.getRootNode(), checked);
		}
	},
	createTree : function(o) {
		return Ext.create('FHD.ux.TreePanel', {
			border:false,
			animate:o.animate,
			rowLines: false,
			dateFieldable:o.dateFieldable,
			id:o.id,
			iconCls : o.titleIcon,
			title : o.title,
			extraParams : o.extraParams,
			onCheckchange : o.onCheckchange,
			url : o.kpiUrl,
			rootVisible : true,
			myexpand : false,
			canSelect : 'sm',
			checkModel : o.checkModel,
			values : o.values,
			monthClick:o.monthClick,
			viewConfig : {
				//stripeRows : true,
				listeners : {
					itemcontextmenu : function(view, rec, node, index, e) {
						e.stopEvent();
						var menu = o.contextItemMenuFc(view, rec, node, index, e);
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
		// Ext.define('ObjMap', {
		// extend: 'Ext.data.Model',
		// fields:['kpi', 'sm', 'org']
		// });
		var me = this;
		Ext.applyIf(me, {
			layout : {
				type : 'accordion'
			},
			height : Ext.getBody().getHeight(),
			//title : FHD.locale.get('fhd.strategymap.strategymapmgr.tree.title'),
			border:false
		});
		me.callParent(arguments);
		me.initValue();
	}
});
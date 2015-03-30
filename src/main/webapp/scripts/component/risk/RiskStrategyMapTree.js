Ext.define('FHD.ux.risk.RiskStrategyMapTree', {
	extend : 'Ext.Panel',
	alias : 'widget.riskstrategymaptree',
	requires : [ 'Ext.tree.Panel' ],

	rbsVisible : false,	
	
	collapsible : true,
	split: true,
	OrgRiskTreeVisible : true,
	riskTreeVisible : true,
	myRiskTreeVisible : true,
	
	OrgRiskTreeRoot : {},
	riskTreeRoot : {},
	myRiskTreeRoot : {},

	orgRiskTreeIcon : 'icon-org',
	riskTreeIcon : 'icon-flag-red',
	myRiskTreeIcon : 'icon-orgsub',

	orgRiskTreeTitle : FHD.locale.get('orgRiskStrategyMapTree.title'),
	riskTreeTitle : FHD.locale.get('riskStrategyMapTree.title'),
	myRiskTreeTitle : FHD.locale.get('myRiskStrategyMapTree.title'),
	
	orgRiskTree : null,
	riskTree : null,
	myRiskTree : null,
	
	dateFieldable:false,

	extraParams : {
		canChecked : true
	},
	items : new Array(),
	values : new Array(),
	onCheckchange : function() {},

	myRiskClickFunction : function() {},
	riskClickFunction:function(){},
	orgRiskClickFunction : function(){},
	
	checkModel : 'cascade',

	orgRiskTreeContextMenuFc : function(tree, node) {},
	riskTreeContextMenuFc : function(tree, node) {},
	myRiskTreeContextMenuFc : function(tree, node) {},
	monthClick:function(menu,item,e,eOpts){},

	/* 方法 */
	initValue : function() {
		var me = this;
		FHD.ajax( {
			url : __ctxPath + '/risk/findRootByCompanyId.f',
			callback : function(objectMaps) {
				Ext.Array.each(objectMaps, function(object) {
					me.OrgRiskTreeRoot = eval(object.orgRisk);
					me.riskTreeRoot = eval(object.risk);
					me.myRiskTreeRoot = eval(object.myRisk);
				});
				if (me.single) {
					me.checkModel = 'single';
				} else {
					me.checkModel = 'cascade';
				}
				
				if (me.myRiskTreeVisible) {
					me.myRiskTree = me.createTree( {
						'onCheckchange' : me.onCheckchange,
						'extraParams' : me.extraParams,
						'riskUrl' : __ctxPath + '/risk/myRiskTree.f?rbs=' + me.rbsVisible,
						'titleIcon' : me.myRiskTreeIcon,
						'title' : me.myRiskTreeTitle,
						'clickFunction' : me.myRiskClickFunction,
						'values' : me.values,
						'checkModel' : me.checkModel,
						'root' : me.myRiskTreeRoot,
						'contextItemMenuFc' : me.myRiskTreeContextMenuFc
						,'monthClick':me.monthClick
						,'dateFieldable':me.dateFieldable
					});
					me.add(me.myRiskTree);
				}
				if (me.OrgRiskTreeVisible) {
					me.orgRiskTree = me.createTree( {
						'onCheckchange' : me.onCheckchange,
						'extraParams' : me.extraParams,
						'riskUrl' : __ctxPath + '/risk/orgRiskTree.f?rbs=' + me.rbsVisible,
						'titleIcon' : me.orgRiskTreeIcon,
						'title' : me.orgRiskTreeTitle,
						//'clickFunction' : me.clickFunction,
						'clickFunction' : me.orgRiskClickFunction,
						'values' : me.values,
						'checkModel' : me.checkModel,
						'root' : me.OrgRiskTreeRoot,
						'contextItemMenuFc' : me.orgRiskTreeContextMenuFc
						,'dateFieldable':me.dateFieldable
					});
					me.add(me.orgRiskTree);
				}
				if (me.riskTreeVisible) {
					me.riskTree = me.createTree( {
						'onCheckchange' : me.onCheckchange,
						'extraParams' : me.extraParams,
						'riskUrl' : __ctxPath + '/risk/riskTree.f?rbs=' + me.rbsVisible,
						'titleIcon' : me.riskTreeIcon,
						'title' : me.riskTreeTitle,
						'clickFunction' : me.riskClickFunction,
						'values' : me.values,
						'checkModel' : me.checkModel,
						'root' : me.riskTreeRoot,
						'contextItemMenuFc' : me.riskTreeContextMenuFc
						,'dateFieldable':me.dateFieldable
					});
					me.add(me.riskTree);
				}
			}
		});
	},
	setTreeValues : function(checked) {
		if (this.myRiskTree) {
			this.myRiskTree.values = this.values;
			this.myRiskTree.setChecked(this.myRiskTree.getRootNode(), checked);
		}if (this.orgRiskTree) {
			this.orgRiskTree.values = this.values;
			this.orgRiskTree.setChecked(this.orgRiskTree.getRootNode(), checked);
		}if (this.riskTree) {
			this.riskTree.values = this.values;
			this.riskTree.setChecked(this.riskTree.getRootNode(), checked);
		}
	},
	createTree : function(o) {
		return Ext.create('FHD.ux.TreePanel', {
			dateFieldable:o.dateFieldable,
			id:o.id,
			iconCls : o.titleIcon,
			title : o.title,
			extraParams : o.extraParams,
			onCheckchange : o.onCheckchange,
			url : o.riskUrl,
			rootVisible : true,
			myexpand : false,
			canSelect : 'risk',
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
			title : FHD.locale.get('fhd.strategymap.strategymapmgr.tree.title')
		});
		me.callParent(arguments);
		me.initValue();
	}
});
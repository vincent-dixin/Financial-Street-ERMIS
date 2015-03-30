
Ext.define('FHD.ux.rule.RuleTree', {
	extend : 'Ext.Panel',
	alias : 'widget.ruletree',
	requires : ['Ext.tree.Panel'],
	collapsible : false,
	ruleTreeVisible : true,
	animate:false,

	ruleTreeRoot : {text:'规章制度',
    	        id: __user.companyId,
    	        expanded: true},

	ruleTreeIcon : 'icon-flag-red',


	//ruleTreeTitle : FHD.locale.get('ruletreerule.title'),

	ruleTree : null,


	extraParams : {
		canChecked : true
	},
	items : new Array(),
	values : new Array(),
	onCheckchange : function() {
	},
	clickFunction : function() {
	},
	checkModel : 'cascade',

	ruleTreeContextMenu : function() {
	}, 

	initValue : function() {
		var me = this;
		FHD.ajax( {
			url : __ctxPath ,
			callback : function(objectMaps) {
				if (me.multiSelect) {
					me.checkModel = 'cascade';
				} else {
					me.checkModel = 'single';
					
				}
					me.ruleTree = me.createTree( {
						'ruleTreeVisible' : me.ruleTreeVisible,
						'onCheckchange' : me.onCheckchange,
						'extraParams' : me.extraParams,
						'ruleUrl' : __ctxPath + '/rule/ruleTree/ruletreeloader.f',
						'titleIcon' : me.ruleTreeIcon,
						'title' : me.ruleTreeTitle,
						'clickFunction' : me.clickFunction,
						'values' : me.values,
						'checkModel' : me.checkModel,
						'root' : me.ruleTreeRoot,
						'contextItemMenuFc' : me.ruleTreeContextMenu,
						'animate':me.animate
					});
					me.add(me.ruleTree);
			}
		});
	},

	setTreeValues : function(checked) {
		if (this.ruleTree) {
			this.ruleTree.values = this.values;
			this.ruleTree.setChecked(this.ruleTree.getRootNode(), checked);
		}
		
	},

	createTree : function(o) {
		return Ext.create('FHD.ux.TreePanel', {
			iconCls : o.titleIcon,
			title : o.title,
			extraParams : o.extraParams,
			onCheckchange : o.onCheckchange,
			url : o.ruleUrl,
			rootVisible : true,
			myexpand : false,
			canSelect : 'rule',
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

		var me = this;
		Ext.applyIf(me, {
			layout : {
				type : 'card'
			}
		});
		me.callParent(arguments);
		me.initValue();
	}
});
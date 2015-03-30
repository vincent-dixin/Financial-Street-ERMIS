Ext.define('FHD.ux.kpi.opt.StrategymapTree', {
	extend : 'Ext.Panel',
	alias : 'widget.strategymaptree',
	border:true,
	smTreeVisible : true,
	smTree : null,
	extraParams : {
		canChecked : true
	},
	items : new Array(),
	values : new Array(),
	onCheckchange : function() {},
	clickFunction : function(){},
	checkModel : 'cascade',
	treeContextMenuFc : function(tree, node) {},

	/* 方法 */
	initValue : function() {
		var me = this;
		if (me.single) {
			me.checkModel = 'single';
		} else {
			me.checkModel = 'cascade';
		}
		
		if (me.smTreeVisible) {
			me.smTree = me.createTree({
				'onCheckchange' : me.onCheckchange,
				'extraParams' : me.extraParams,
				'url' :  __ctxPath + '/kpi/KpiStrategyMapTree/treeloader',
				'clickFunction' : me.clickFunction,
				'values' : me.values,
				'checkModel' : me.checkModel,
				'root' :{
                    "id": "sm_root",
                    "text": FHD.locale.get('fhd.sm.strategymaps'),
                    "dbid": "sm_root",
                    "leaf": false,
                    "code": "sm",
                    "type": "sm",
                    "expanded":false,
                    'iconCls':'icon-strategy'
                },
				'contextItemMenuFc' : me.treeContextMenuFc
			});
			me.add(me.smTree);
		}
	},
	createTree : function(o) {
		return Ext.create('FHD.ux.TreePanel', {
			autoScroll:true,
			iconCls : o.titleIcon,
			title : o.title,
			extraParams : o.extraParams,
			onCheckchange : o.onCheckchange,
			url : o.url,
			rootVisible : true,
			myexpand : false,
			//canSelect : 'category',
			checkModel : o.checkModel,
			values : o.values,
			viewConfig : {
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
		var me = this;
		
		Ext.applyIf(me, {
			layout : {
				type : 'fit'
			}
		});
		
		me.callParent(arguments);
		me.initValue();
	}
});
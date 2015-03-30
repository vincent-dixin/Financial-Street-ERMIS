Ext.define('FHD.ux.kpi.KpiTypeTree', {
	extend : 'Ext.Panel',
	alias : 'widget.kpiTypeTree',
	requires : ['Ext.tree.Panel'],
	
	kpiTypeTreeVisible : true,
	kpiTypeTreeRoot : {},
	kpiTypeTree : null,
	dateFieldable:false,
	extraParams : {
		canChecked : true
	},
	items : new Array(),
	values : new Array(),
	onCheckchange : function() {},
	kpiTypeClickFunction : function(){},
	checkModel : 'cascade',
	kpiTypeTreeContextMenuFc : function(tree, node) {},
	monthClick:function(menu, item, e ,eOpts){},

	/* 方法 */
	initValue : function() {
		var me = this;
		if (me.single) {
			me.checkModel = 'single';
		} else {
			me.checkModel = 'cascade';
		}
		
		if (me.kpiTypeTreeVisible) {
			me.kpiTypeTree = me.createTree({
				'onCheckchange' : me.onCheckchange,
				'extraParams' : me.extraParams,
				'categoryUrl' : __ctxPath + "/kpi/kpi/kpitypetreeloader.f",
				'clickFunction' : me.kpiTypeClickFunction,
				'values' : me.values,
				'checkModel' : me.checkModel,
				'root' : {
                    "id": "type_1",
                    "text": FHD.locale.get('fhd.kpi.kpi.form.etype'),
                    "dbid": "type_1",
                    "leaf": false,
                    "code": "zblx",
                    "type": "kpi_type",
                    'iconCls':'icon-ibm-icon-metrictypes'
                    //,"expanded": true
                },
				'contextItemMenuFc' : me.kpiTypeTreeContextMenuFc
				,'dateFieldable':me.dateFieldable
			});
			me.add(me.kpiTypeTree);
		}
	},
	
	setTreeValues : function(checked) {
		if (this.kpiTypeTree) {
			this.kpiTypeTree.values = this.values;
			this.kpiTypeTree.setChecked(this.kpiTypeTree.getRootNode(), checked);
		}
	},
	createTree : function(o) {
		return Ext.create('FHD.ux.TreePanel', {
			dateFieldable:o.dateFieldable,
			iconCls : o.titleIcon,
			title : o.title,
			extraParams : o.extraParams,
			onCheckchange : o.onCheckchange,
			url : o.categoryUrl,
			rootVisible : true,
			myexpand : false,
			canSelect : 'category',
			checkModel : o.checkModel,
			values : o.values,
			monthClick:o.monthClick,
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
			},
			height : Ext.getBody().getHeight()
		});
		
		me.callParent(arguments);
		me.initValue();
	}
});
Ext.define('FHD.ux.category.CategoryStrategyMapTree', {
	extend : 'Ext.Panel',
	alias : 'widget.categoryStrategyMapTree',
	requires : ['Ext.tree.Panel'],
	oneVisible : false,
	collapsible : true,
	split: true,
	orgCategoryTreeVisible : true,
	categoryTreeVisible : true,
	myCategoryTreeVisible : true,
	
	orgCategoryTreeRoot : {},
	categoryTreeRoot : {},
	myCategoryTreeRoot : {},

	orgCategoryTreeIcon : 'icon-org',
	categoryTreeIcon : 'icon-flag-red',
	myCategoryTreeIcon : 'icon-orgsub',

	orgCategoryTreeTitle : '',
	categoryTreeTitle : '',
	myCategoryTreeTitle : '',
	
	orgCategoryTree : null,
	categoryTree : null,
	myCategoryTree : null,
	
	dateFieldable:false,

	extraParams : {
		canChecked : true
	},
	items : new Array(),
	values : new Array(),
	onCheckchange : function() {},

	myCategoryClickFunction : function() {},
	categoryClickFunction:function(){},
	orgCategoryClickFunction : function(){},
	
	checkModel : 'cascade',

	orgCategoryTreeContextMenuFc : function(tree, node) {},
	categoryTreeContextMenuFc : function(tree, node) {},
	myCategoryTreeContextMenuFc : function(tree, node) {},
	monthClick:function(menu,item,e,eOpts){},

	/* 方法 */
	initValue : function() {
		var me = this;
		FHD.ajax( {
			url : __ctxPath + '/category/findRootBO.f',
			callback : function(objectMaps) {
				Ext.Array.each(objectMaps, function(object) {
					me.myCategoryTreeRoot = eval(object.myCategory);
					me.orgCategoryTreeRoot = eval(object.orgCategory);
					me.categoryTreeRoot = eval(object.category);
				});
				
				if (me.single) {
					me.checkModel = 'single';
				} else {
					me.checkModel = 'cascade';
				}
				
				if (me.myCategoryTreeVisible) {
					me.myCategoryTree = me.createTree( {
						'onCheckchange' : me.onCheckchange,
						'extraParams' : me.extraParams,
						'categoryUrl' : __ctxPath + '/category/findMyCategoryTree.f',
						'titleIcon' : me.myCategoryTreeIcon,
						'title' : me.myCategoryTreeTitle,
						'clickFunction' : me.myCategoryClickFunction,
						'values' : me.values,
						'checkModel' : me.checkModel,
						'root' : me.myCategoryTreeRoot,
						'contextItemMenuFc' : me.myCategoryTreeContextMenuFc
						,'monthClick':me.monthClick
						,'dateFieldable':me.dateFieldable
					});
					me.add(me.myCategoryTree);
				}
				if (me.orgCategoryTreeVisible) {
					me.orgCategoryTree = me.createTree( {
						'onCheckchange' : me.onCheckchange,
						'extraParams' : me.extraParams,
						'categoryUrl' : __ctxPath + '/category/findOrgCategoryTree.f',
						'titleIcon' : me.orgCategoryTreeIcon,
						'title' : me.orgCategoryTreeTitle,
						//'clickFunction' : me.clickFunction,
						'clickFunction' : me.orgCategoryClickFunction,
						'values' : me.values,
						'checkModel' : me.checkModel,
						'root' : me.orgCategoryTreeRoot,
						'contextItemMenuFc' : me.orgCategoryTreeContextMenuFc
						,'dateFieldable':me.dateFieldable
					});
					me.add(me.orgCategoryTree);
				}
				
				if (me.categoryTreeVisible) {
					me.categoryTree = me.createTree( {
						'onCheckchange' : me.onCheckchange,
						'extraParams' : me.extraParams,
						'categoryUrl' : __ctxPath + '/category/findCategoryTree.f',
						'titleIcon' : me.categoryTreeIcon,
						'title' : me.categoryTreeTitle,
						'clickFunction' : me.categoryClickFunction,
						'values' : me.values,
						'checkModel' : me.checkModel,
						'root' : me.categoryTreeRoot,
						'contextItemMenuFc' : me.categoryTreeContextMenuFc,
						'dateFieldable':me.dateFieldable
					});
					me.add(me.categoryTree);
				}
			}
		});
	},
	
	setTreeValues : function(checked) {
		if (this.myCategoryTree) {
			this.myCategoryTree.values = this.values;
			this.myCategoryTree.setChecked(this.myCategoryTree.getRootNode(), checked);
		}if (this.orgCategoryTree) {
			this.orgCategoryTree.values = this.values;
			this.orgCategoryTree.setChecked(this.orgCategoryTree.getRootNode(), checked);
		}if (this.categoryTree) {
			this.categoryTree.values = this.values;
			this.categoryTree.setChecked(this.categoryTree.getRootNode(), checked);
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
			url : o.categoryUrl,
			rootVisible : true,
			myexpand : false,
			canSelect : 'category',
			checkModel : o.checkModel,
			values : o.values,
			monthClick:o.monthClick,
			viewConfig : {
				//stripeRows : true,
				listeners : {
					itemcontextmenu : function(view, rec, node, index, e) {
						alert('aa');
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
		
		if(me.oneVisible){
			me.collapsible = false;
			me.split = false;
			
			Ext.applyIf(me, {
				layout : {
					type : 'fit'
				},
				height : Ext.getBody().getHeight()
			});
		}else{
			Ext.applyIf(me, {
				layout : {
					type : 'accordion'
				},
				height : Ext.getBody().getHeight(),
				title : FHD.locale.get('fhd.categoryTree.title')
			});
			if(me.orgCategoryTreeVisible){
				me.orgCategoryTreeTitle = FHD.locale.get('fhd.categoryTree.orgTitle');
			}
			if(me.categoryTreeVisible){
				me.categoryTreeTitle  = FHD.locale.get('fhd.categoryTree.title');
			}
			if(me.myCategoryTreeTitle){
				me.myCategoryTreeTitle = FHD.locale.get('fhd.categoryTree.myTitle');
			}
		}
		
		me.callParent(arguments);
		me.initValue();
	}
});
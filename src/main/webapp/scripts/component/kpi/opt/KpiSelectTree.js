Ext.define('FHD.ux.kpi.opt.KpiSelectTree', {
	extend : 'Ext.container.Container',
	alias : 'widget.kpiselecttree',
	border:true,
	split: true,
    collapsible: false,
    width: 265,
    maxWidth: 300,
	layout : {
		type : 'accordion',
		titleCollapse: true,
        animate: false,
        collapseFirst: true,
        activeOnTop: false
			
	},
	/**
	 * 指标树是否显示
	 */
	smtreevisable:true,
	
	/**
	 * 记分卡树是否显示
	 */
	categorytreevisable:true,

	
	/**
	 * 指标类型树是否显示
	 */
	kpitypetreevisable:true,
	
	/**
	 * 我的文件夹是否显示
	 */
	myfoldertreevisable:true,
	
	reloadStroe:function(node){
		if (node.parentNode == null) return;//根节点直接返回
		var me = this;
		var data = node.data;
		me.findParentBy(function(container, component){
			var extraParams = {type:data.type,id:data.id};
			//获得指标列表的grid
			var kpigrid = container.kpigrid;
			//参数赋值
			kpigrid.store.proxy.extraParams = extraParams; 
			//重新加载grid中的数据
			kpigrid.store.load();
			
		});
		
	},
	init:function(){
		var me = this;
		
		/**
		 * 加载我的文件夹树
		 */
		if(me.myfoldertreevisable){
			me.foldertree = Ext.create('FHD.ux.kpi.opt.KpiMyFolderTree',{
				border:true,
				width : 300,
				height : 500,
				title:FHD.locale.get('fhd.kpi.kpi.tree.myfolder'),
				iconCls: 'icon-ibm-new-group-view',
				clickFunction:function(node){
					me.reloadStroe(node);
				},
				treeload:function(store,records){
					var tree = me.foldertree.tree;
                	var rootNode = tree.getRootNode();
                	if(rootNode.childNodes.length>0){
                		var selectedNode = rootNode.firstChild;
                		tree.getSelectionModel().select(selectedNode);
                		me.foldertree.clickFunction(selectedNode);
                	}
				}
			});
			me.add(me.foldertree);
		}
		
		
		/**
		 * 加载目标树
		 */
		if(me.smtreevisable){
			
			me.smtree = Ext.create('FHD.ux.kpi.opt.StrategymapTree', {
				border:true,
				width : 300,
				height : 500,
				title:FHD.locale.get('fhd.strategymap.strategymapmgr.target'),
				iconCls: 'icon-strategy',
				extraParams : {
					canChecked : false,
					smIconType:'display'
				},
				clickFunction:function(node){
					me.reloadStroe(node);
				}
			});
			me.add(me.smtree);
		}
		
		
		/**
		 * 加载记分卡树
		 */
		if(me.categorytreevisable){
			me.categorytree = Ext.create('FHD.ux.category.CategoryStrategyMapTree', {
				title:FHD.locale.get('fhd.kpi.categoryroot'),
				border:true,
				iconCls: 'icon-ibm-icon-scorecards',
				width : 300,
				height : 500,
				oneVisible:true,
				categoryTreeVisible:true,
				myCategoryTreeVisible:false,
				orgCategoryTreeVisible:false,
				extraParams : {
					canChecked : false
				},
				categoryClickFunction:function(node){
					me.reloadStroe(node);
				}
			});
			me.add(me.categorytree);
		}
		
		
		/**
		 * 加载指标类型树 
		 */
		if(me.kpitypetreevisable){
			me.kpitypetree =  Ext.create('FHD.ux.kpi.KpiTypeTree', {
				border:true,
				width : 300,
				height : 500,
				title:FHD.locale.get('fhd.kpi.kpi.form.etype'),
				iconCls: ' icon-ibm-icon-metrictypes',
				extraParams : {
					canChecked : false
				},
				kpiTypeClickFunction:function(node){
					me.reloadStroe(node);
				}
			});
			me.add(me.kpitypetree);
		}
		
		
	},
	
	initComponent : function() {
		
		var me = this;
		Ext.applyIf(me, {
			
		});
		
		
		me.callParent(arguments);
		me.init();
	}
	



});
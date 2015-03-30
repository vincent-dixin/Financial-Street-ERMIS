Ext.define('FHD.ux.riskEvent.RiskEventSelectTree', {
	extend : 'Ext.container.Container',
	alias : 'widget.riskeventselecttree',
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
	 * 风险树是否显示
	 */
	risktreevisable:true,
	risktreeUrl:'/potentialRiskEvent/findPotentialRiskEventByRiskId',
	
	/**
	 * 组织树是否显示
	 */
	orgtreevisable:true,
	orgtreeUrl:'/potentialRiskEvent/findPotentialRiskEventByOrgId',
	
	/**
	 * 指标树是否显示
	 */
	kpitreevisable:true,
	kpitreeUrl:'/potentialRiskEvent/findPotentialRiskEventByKpiId',
	
	/**
	 * 流程树是否显示
	 */
	processtreevisable:true,
	processtreeUrl:'/potentialRiskEvent/findPotentialRiskEventByProcessId',
	
	reloadStore:function(url,extraParams){
		var me = this;
		//alert(extraParams.id);
		me.findParentBy(function(container, component){
			//获得指标列表的grid
			var candidategrid = container.candidategrid;
			//参数赋值
			candidategrid.store.proxy.url = url;
			candidategrid.store.proxy.extraParams = extraParams; 
			//重新加载grid中的数据
			candidategrid.store.load();
			
		});
		
	},
	init:function(){
		var me = this;

		/**
		 * 加载组织树
		 */
		if(me.orgtreevisable){
			
			me.orgtree = Ext.create('FHD.ux.org.DeptTree',{
	        	title:'组织',
	        	iconCls : 'icon-ibm-new-group-view',
	        	subCompany: true,
	        	companyOnly: false,
	        	checkable:false,
	        	border:false,
	        	rootVisible: true,
	        	myexpand:false,	//默认不展开树，多个签展开出错
	        	listeners: {
	   				select: function(t,r,i,o){
	   					if (r.parentNode == null) return;//根节点直接返回
	                	var id = r.data.id;
	                	var url = __ctxPath + me.orgtreeUrl;
	        			var extraParams = {id:id};
	                	me.reloadStore(url,extraParams);
	                }
	            }
		    });
			me.add(me.orgtree);
		}
		
		
		/**
		 * 加载指标树
		 */
		if(me.kpitreevisable){
			me.kpitree = Ext.create('FHD.ux.TreePanel', {
				iconCls : 'icon-flag-red',
				title : '指标',
				extraParams : {
					canChecked : false
				},
				url : __ctxPath + '/kpi/KpiTree/kpitreeloader',
				rootVisible : true,
				root : {
                    "id": "kpi_root",
                    "text": '指标',
                    "dbid": "kpi_root",
                    "leaf": false,
                    "code": "kpi",
                    "type": "kpi",
                    "expanded":false
                },
				listeners: {
	   				select: function(t,r,i,o){
	   					if (r.parentNode == null) return;//根节点直接返回
	                	var id = r.data.id;
	                	var url = __ctxPath + me.kpitreeUrl;
	        			var extraParams = {id:id};
	                	me.reloadStore(url,extraParams);
	                }
	            }
			});
			me.add(me.kpitree);
		}
		
		/**
		 * 加载流程树 
		 */
		if(me.processtreevisable){
			me.processtree = Ext.create('FHD.ux.process.processTree', {
	        	border:false,
	        	title:'流程',
	        	iconCls : 'icon-ibm-icon-metrictypes',
	        	myexpand : false,
				extraParams : {canChecked : false},
				listeners: {
	   				select: function(t,r,i,o){
	   					if (r.parentNode == null) return;//根节点直接返回
	                	var id = r.data.id;
	                	var url = __ctxPath + me.processtreeUrl;
	        			var extraParams = {id:id};
	                	me.reloadStore(url,extraParams);
	                }
	            }
			});
			me.add(me.processtree);
		}
		
		/**
		 * 加载风险树
		 */
		if(me.risktreevisable){
			
			//初始化参数
	    	var extraParams = {};
	    	extraParams.rbs = me.rbs || false;
	    	extraParams.canChecked = me.canChecked || false;
	    	var queryUrl = __ctxPath + '/risk/riskTreeLoader';
	    	me.risktree = Ext.create('FHD.ux.TreePanel', {
	    		root:{
	    	        "id": "root",
	    	        "text": "风险",
	    	        "dbid": "sm_root",
	    	        "leaf": false,
	    	        "code": "sm",
	    	        "type": "orgRisk",
	    	        "expanded": false,
	    	        'iconCls':'icon-ibm-icon-scorecards'	//样式
	    	    },
	    		rootVisible: true,
	    		title:'风险',
				iconCls: 'icon-ibm-icon-scorecards',
	           	extraParams:extraParams,
	   			url: queryUrl,//调用后台url
	   			listeners: {
	   				select: function(t,r,i,o){
	   					if (r.parentNode == null) return;//根节点直接返回
	                	var id = r.data.id;
	                	var url = __ctxPath + me.risktreeUrl;
	        			var extraParams = {id:id};
	                	me.reloadStore(url,extraParams);
	                }
	            }
	        });
			me.add(me.risktree);
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
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
<%@ include file="/WEB-INF/jsp/commons/include-tagsOnly.jsp" %>

var fhd_strategymap_tree_view = (function () {
	
	
	Ext.define('FHD.strategymap.tree.view', {
		
		init:function(){
			var me = this;
			me.tree = Ext.create('Ext.tree.Panel',{
				index: 1,
				border:false,
			    //title: '战略地图',
			    //iconCls:'icon-ibm-icon-reports',
			    autoScroll: true,
			    root : {
			    	text : '第一会达',
			        iconCls : 'icon-ibm-icon-report',
			        expanded : true,
			        children : [{
			        	text: '系统研发部',
			        	expanded : true,
			        	iconCls : 'icon-ibm-icon-report',
			        	leaf : true
			        }]
			    },
			    initrightPanel:function(){
			    	var rightUrl = __ctxPath + "/pages/kpi/test.jsp";
		    	    fhd_kpi_kpiaccordion_view.container.remove(fhd_kpi_kpiaccordion_view.container.rightpanel,true);
		    	    var rightpanel = Ext.create('Ext.panel.Panel',{
		    	   		layout: 'fit',
                        border: false,
                        autoScroll: false,
                        region: 'center',
                        html:"<iframe frameborder='0' height='100%' width='100%' noresize  src='"+rightUrl+"'/>"
		    	   	
		    	   });
		    	   fhd_kpi_kpiaccordion_view.container.rightpanel = rightpanel;
		    	   fhd_kpi_kpiaccordion_view.container.add(rightpanel);
			    },
			    listeners: {
			    	itemclick: function (node, record, item) {
			    	   fhd_strategymap_tree_view.tree.initrightPanel();
	                },
	                /**
	                 * 战略面板展开前的事件
	                 */
	                beforeexpand: function (p) {
		                     fhd_kpi_kpiaccordion_view.accordion.remove(p, false);
		                     fhd_kpi_kpiaccordion_view.accordion.insert(0, p);
		                     panelSort(p);
		                     fhd_strategymap_tree_view.tree.initrightPanel();
                 	 }
			    }
			});
			
		}
		
	});
	
	
	
	
	
	
	var fhd_strategymap_tree_view = Ext.create('FHD.strategymap.tree.view');
	return fhd_strategymap_tree_view;
	
	})();
	
	
	fhd_strategymap_tree_view.init();
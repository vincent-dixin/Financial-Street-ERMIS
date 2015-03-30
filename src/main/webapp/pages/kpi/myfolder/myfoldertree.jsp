<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
<%@ include file="/WEB-INF/jsp/commons/include-tagsOnly.jsp" %>

var fhd_myfolder_tree_view = (function () { 
	
	Ext.define('FHD.kpi.myfolder.tree.view', {
		
		clickNode:function(node){
			var me = this;
			if (node.parentNode == null) return;//根节点直接返回
			var data = node.data;
			fhd_kpi_kpiaccordion_view.initRightPanel(me.editurl);
		},
		
		getMeTree : function(){
			if(this.meTree == null){
				this.meTree = Ext.create('FHD.ux.kpi.opt.KpiMyFolderTree',{
					border:false,
					width: 265,
		            maxWidth: 300,
					clickFunction:function(node){
						fhd_myfolder_tree_view.clickNode(node);
					},
					treeload:function(store,records){
						var tree = fhd_myfolder_tree_view.tree.tree;
	                	var rootNode = tree.getRootNode();
	                	if(rootNode.childNodes.length>0){
	                		var selectedNode = rootNode.firstChild;
	                		tree.getSelectionModel().select(selectedNode);
	                		fhd_myfolder_tree_view.clickNode(selectedNode);
	                	}
					},
		            clicked:function(){
		            	kpiActivePanelflag = 'myfolder';
				    	var selectedNode;
				    	var tree = fhd_myfolder_tree_view.tree.tree;
				    	var nodeItems = tree.getSelectionModel().selected.items;
				        if (nodeItems.length > 0) {
				            selectedNode = nodeItems[0];
				        }
				        if(selectedNode==null){
				        }else{
				        	fhd_myfolder_tree_view.clickNode(selectedNode);
				        }
					}
				});
			}
			
			return this.meTree;
		},
		
		
		init:function(){
			var me = this;
			me.editurl = __ctxPath + "/pages/kpi/myfolder/myfolderedit.jsp?"
			me.tree = me.getMeTree();
		}
		
	});
	
	var fhd_myfolder_tree_view = Ext.create('FHD.kpi.myfolder.tree.view');
    return fhd_myfolder_tree_view;
	
	
})();

fhd_myfolder_tree_view.init();
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="fhd.sys.duty.duty" /></title>
</head>
<body>
<!-- 
 <div id="west" >
       	<fhdcore:tree rootText="${orgRoot.orgname}" 
rootHref="${ctx}/sys/duty/tabs.do?id=id&nexduty=true"
el="duty-tree" url="${ctx}/sys/duty/loadOrgTree.do" 
height="document.body.offsetHeight - 6" width="200" rootId="${orgRoot.id}2345" rootIconCls="org-tree-icon" enableDD="true">
	<fhdcore:treeNode nodeType="org" url="${ctx}/sys/duty/loadOrgTree.do"></fhdcore:treeNode>
    
	
</fhdcore:tree>



	<fhdcore:tree rootText="${rootName}"  
	rootHref="${ctx}/sys/duty/tabs.do?id=${rootId}&nexduty=true"
	el="duty-tree" url="${ctx}/sys/duty/loadDutyTree.do" 
	height="document.body.offsetHeight" width="200" rootId="${rootId}2345" rootIconCls="org-tree-icon" enableDD="true">
	   	<fhdcore:treeNode nodeType="org" url="${ctx}/sys/duty/loadDutyTree.do"></fhdcore:treeNode>
	   	
	</fhdcore:tree>
	
 </div>
 <div id="center" >
    <iframe src="" id="mainFrame" name="mainframe" width="100%" scrolling="no"  frameborder="0"></iframe>
  </div>    
   -->
<script type="text/javascript">
var height=Ext.getBody().getHeight();
var width=Ext.getBody().getWidth();

var loadDate = new Ext.tree.TreeLoader({
	dataUrl : '${ctx}/sys/duty/loadOrgTree.do'
 });
 var root=new Ext.tree.AsyncTreeNode({
   id:'${orgRoot.id}2345',
   text:'${orgRoot.orgname}',
   href:'${ctx}/sys/duty/tabs.do?id=${orgRoot.id}&nexduty=true',
   hrefTarget:'mainframe',
   cls:'root',
   iconCls:'icon-package-icon',
   expanded:true
 });
var tree=new Ext.tree.TreePanel({
  id:"tree",
  region: 'west',
  width:'200',
  split: true,
  minSize: 200,
  //maxSize: 200,
  border:false,
  frame:false,
  height:height,
  //useArrows: true,
  autoScroll: true,
  animate: true,
  enableDD: true,
  collapsible:true,
  containerScroll: true,
  lines:null,
  hlColor:null,
  hlDrop:false,
  border: true,
  loader:loadDate,
  root:root,
  cls:'org'
});


/*
var field=new Ext.ux.form.SearchField({
     store:store
    });
    
var panel=new Ext.Panel({
	 region: 'west',
	 collapsible:true,
    width:'200',
	 layout:{type:'vbox',
	 padding:'2',
	 align:'stretch'},
    items: [
	   field,
      tree
    ]
});
*/

Ext.onReady(function(){
	var tabs = new Ext.Panel({
	    id:"tabs",
		 region:'center',
		 margins: '0 0 0 0',
		 id:'center-panel',
		 border:false,
		 hideBorders:true,
		 height:height,
		 width:'auto',
	     activeTab: 0,
	    // plain:true, 
	    items:[{
	        id:"iframe",
	        height:height,
	        width:'auto',
	        html:'<iframe id="mainframe" src="${ctx}/sys/duty/tabs.do?id=${orgRoot.id}&nexduty=true"  name="mainframe" width="100%" scrolling="no"   frameborder="0"></iframe>'
	    }]
	});
	var viewport=new Ext.Panel({
	    id:'viewportPanel',
	    renderTo :document.body,
	   layout:'border',
	   height:height,
	   items:[tree,tabs]
	});
	Ext.get('mainframe').setHeight(height); 
	Ext.EventManager.onWindowResize(function(width ,height){
		Ext.getCmp("viewportPanel").setWidth(width);
		Ext.getCmp("viewportPanel").setHeight(height);
		
	});






	tree.on('click',function(node,e){
		var url = "${ctx}/sys/duty/tabs.do?id=" + node.id;
		if(node.attributes.cls == 'org'){url += '&nexduty=true';}
		if(node.attributes.cls == 'duty'){url += '&nexpeople=true';}
		//document.getElementById('mainframe').src=url;
		});
	tree.on('contextmenu', onContextMenu);

	function onContextMenu(node, e) {
		var type = node.attributes.cls;
		var menu;
		if(!menu){
			if (type == "root" || type == "org") {
				menu = new Ext.menu.Menu({ id:'root'+ Math.random(),items:[
						{
							id : node.id + 'addposi' + Math.random(),
							text : '<spring:message code="fhd.common.add"/><spring:message code="fhd.sys.duty.nextduty"/>',
							iconCls : 'icon-add',
							handler : function() {
								FHD.openWindow($locale('fhd.common.prompt'), 800,192, "${ctx}/sys/duty/addPage.do?node=" + tree.getSelectionModel().getSelectedNode().id,'no');
								
								//window.showModalDialog("${ctx}/sys/duty/addPage.do?node=" + tree.getSelectionModel().getSelectedNode().id,window,"dialogWidth:800px;dialogHeight:258px,center:yes,resizable:no,status:no");
								//selectNodeReload();
							}
						},
						'-',
						{
							id : node.id + 'refresh' + Math.random(),
							text : '<spring:message code="fhd.common.refresh"/>',
							iconCls : 'icon-arrow-refresh-blue',
							handler : function() {
							//	 selectNodeReload();
							}
						}]});
			}
			else if (type === "duty") {
				menu = new Ext.menu.Menu({ id:'root'+Math.random(),items: [
						{
							id : node.id + 'editduty' + Math.random(),
							text : '<spring:message code="fhd.sys.duty.dutyedit"/>',
							iconCls : 'icon-edit',
							handler : function() {
								FHD.openWindow('<spring:message code="fhd.common.edit" />', 800,192, "${ctx}/sys/duty/editPage.do?id="+tree.getSelectionModel().getSelectedNode().id,'no');
								//window.showModalDialog("${ctx}/sys/duty/editPage.do?node="+tree.getSelectionModel().getSelectedNode().id,window,"dialogWidth:750px;dialogHeight:378px,center:yes,resizable:no,status:no");
								selectNodeReload();
							}
						},'-',
						{
							id : node.id + 'addposi' + Math.random(),
							text : '<spring:message code="fhd.sys.duty.dutydel"/>',
							iconCls : 'icon-del',
							handler : function() {
									//alert(tree.getSelectionModel().getSelectedNode().id);
									Response = confirm('<spring:message code="fhd.common.delconfirm" />');
									if(Response == true){
										$.ajax({
											type: "GET",
											url: "${ctx}/sys/duty/delete.do",
											data: "ids="+tree.getSelectionModel().getSelectedNode().id,
											success: function(msg){
												
												if("true"==msg){
												}else{
												}
											}
										}); 
								//		tree.getSelectionModel().getSelectedNode().parentNode.reload();
									}
									
							}
						}]});
			}
		}
		e.preventDefault();
		node.select();
		menu.showAt(e.getPoint());
		
	}
});
	function selectNodeReload() {
		tree.getSelectionModel().getSelectedNode().reload();
	}
	function selectNodeParentReoload(){
		tree.getSelectionModel().getSelectedNode().parentNode.reload();
	}

	/*
	var height=Ext.getBody().getHeight();
	Ext.get('mainframe').setHeight(height);
	Ext.onReady(function(){
		//document.getElementById("mainFrame").src="${ctx}/sys/duty/tabs.do?id=${rootId}&nexduty=true"; 
		 var viewport = new Ext.Panel({
	           layout: 'border',
	           renderTo:document.body,
	           height:height,
	           items: [
	         {
	               region: 'west',
	               id: 'west-panel', 
	               title: '',
	               border:false,
	               height:height-5,
	               collapsible: true,
	               split: true,
	               width: 200,
	               minSize: 200,
	               maxSize: 200,
	               contentEl: 'west'
	              
	            
	           },
	           {
	               title: '',
	               contentEl: 'center',
	               border:false,
	               collapsible: false,
	               region:'center',
	               margins: '0 0 0 0'
	           }]
	       });
	  });
		*/
</script>
</body>
</html>
























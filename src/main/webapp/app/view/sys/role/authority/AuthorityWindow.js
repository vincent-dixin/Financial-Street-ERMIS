/**
 * 为角色添加人员,基本信息面板
 * 继承于Ext.form.Panel
 * 
 * @author 翟辉
 */
Ext.define('FHD.view.sys.role.authority.AuthorityWindow', {
    extend: 'Ext.Window',
    alias: 'widget.authorityWindow',
    height: 500,
    width: 600,
    collapsible:true,
	resizable:false,
    typeId:null,
    type:null,
    findRootNode:function(){
    	var me = this;
    	var root;
    	FHD.ajax({
       		async : false,
       		url :'sys/autho/findTreeRoot.f',
       		callback : function(objectMaps){
       			 root =objectMaps.authorty;
       		}
    	});
    	return root;
    },
  //保存权限
 	assignAuthority:function(){
 		var me = this;
 		if(this.typeId!=null){
 			var nodes = me.authoritytree.getChecked();
 			var roleArr = [];
 			for ( var i = 0; i < nodes.length; i++) {
 				roleArr.push({checkId:nodes[i].data.id});
 			}
 			if(roleArr == null || roleArr == []){
 				window.top.Ext.ux.Toast.msg("操作不成功","权限不能为空");
 				return false;
 			}else{
 				FHD.ajax({
 					async : false,
 					url: me.assignAuthorityUrl, //+ '?id=' + param.id + 'selectIds=' + selectIds,
 					params : {
 						id:this.typeId,
 						roleItem: Ext.JSON.encode(roleArr),
 						authorityType:this.type
 					},
 					callback : function(object){
 						//var authorityShowTree = Ext.getCmp('authorityShowTree');//得到权限树展示树(右)
 						me.authorityShowTree.store.load();	
 						me.authoritytree.store.load();
 						if(object.resultOk == 'ok'){
 							window.top.Ext.ux.Toast.msg("操作成功","权限添加成功");
 						}else{
 							window.top.Ext.ux.Toast.msg("操作不成功","权限添加失败");
 						}
 					}
 			     });
 			}
 		}else{
 			window.top.Ext.ux.Toast.msg("操作不成功","你没有选中指定的角色");
 		}
 	},
 	 //删除权限
 	assignAuthorityShow:function(){
 		var me = this;
 		if(this.typeId!=null){
 			var nodes = me.authorityShowTree.getChecked();
 			var roleArr = [];
 			for ( var i = 0; i < nodes.length; i++) {
 				roleArr.push({checkId:nodes[i].data.id});
 			}
 			if(roleArr == null || roleArr == []){
 				window.top.Ext.ux.Toast.msg("操作不成功","权限不能为空");
 				return false;
 			}else{
 				FHD.ajax({
 					async : false,
 					url: me.assignAuthorityShowUrl,
 					params : {
 						id:this.typeId,
 						roleItem: Ext.JSON.encode(roleArr),
 						authorityType:this.type
 					},
 					callback : function(object){
 						me.authorityShowTree.store.load();	
 						me.authoritytree.store.load();
 						if(object.resultOk == 'ok'){
 							window.top.Ext.ux.Toast.msg("操作成功","权限删除成功");
 						}else{
 							window.top.Ext.ux.Toast.msg("操作不成功","权限删除失败");
 						}
 					}
 			     });
 			}
 		}else{
 			window.top.Ext.ux.Toast.msg("操作不成功","你没有选中指定的角色");
 		}
 	},
 	//树节点操作
 	//清空操作
 	emptyTree:function (){
 		var me = this;
 			var rootnodes = me.getRootNode();
 			 findchildnode(rootnodes);
 			function findchildnode(node){
 				var childnodes = node.childNodes;
 				 for(var i=0;i<childnodes.length;i++){ 	 
 						 childnodes[i].set("checked",false);
 					 if(childnodes[i].childNodes.length>0){
 						 findchildnode(childnodes[i]);
 					 }
 				 }
 			}
 		},
 	//递归选中权限树节点
 	checkTreeNode:function(nodeId){
 			var me = this;
 			var rootnodes = me.getRootNode();
 			findchildnode(rootnodes);
 			function findchildnode(node){
 				var childnodes = node.childNodes;
 				 for(var i=0;i<childnodes.length;i++){ 
 					 if(childnodes[i].data.id==nodeId){
 						 childnodes[i].set("checked",true);
 					 }
 					 if(childnodes[i].childNodes.length>0){
 						 findchildnode(childnodes[i]); 
 					 }
 				 }
 			}
 		},	
 		/** 
    	 * 取得一个节点的所有父节点 
    	 * 
    	 */
 		getAllParentNodes: function (node){ 
 			var me = this;
    	 	var parentNodes=[]; 
    	 	parentNodes.push(node); 
    	 		if(node.parentNode){ 
    	 			parentNodes = parentNodes.concat(me.getAllParentNodes(node.parentNode)); 
    	 		} 
    	 	return parentNodes; 
    	 },
    	/** 
    	 * 当点击子节点时 
    	 * 将父节点选中 
    	 */ 
    	 checkedParentNodes: function (node,checked){ 
    	 //取得本节点的所有父节点,父节点中包括其自己 
    		 var me = this;
    	 	var allParentNodes=me.getAllParentNodes(node); 
    	 	if(allParentNodes.length>1){ 
    	 		for(var i=0;i<allParentNodes.length-1;i++){
    	 			if(allParentNodes[i].data.checked != true){ 
    	 				allParentNodes[i].set("checked",checked);
    	 			} 
    	 		} 
    	 	} 
    	 }, 
    	/** 
    	 * 当当前子节点的父节点的所有子节点中 
    	 * 不存在checked=true的子节点时,父节点不被选中 
    	 */
    	 unCheckedParentNode: function (currentChildNode){ 
    		var me = this;
    	 	if(currentChildNode.parentNode){ 
    	 		var parentNode=currentChildNode.parentNode; 
    	 		//取得本父节点下所有被选中的子节点 
    	 		//包括本父节点本身 
    	 		var allCheckedChildrenNodes=me.getCheckedNodes(parentNode); 
    	 		//alert(allCheckedChildrenNodes);
    	 		if(allCheckedChildrenNodes.length === 1){ 
    	 			parentNode.set("checked",false);
    	 		} 
    	 		if(parentNode.parentNode){ 
    	 			me.unCheckedParentNode(parentNode); 
    	 		} 
    	 	} 
    	 } ,  
    	 /** 
    	  * 取得所有子节点中checked为true的节点(TreeNode) 
    	  * 包括本节点 
    	  */ 
    	 getCheckedNodes: function (node){ 
    		var me = this;
    	 	 var checked = []; 
    	 			 if( node.data.checked == true ) { 
    	 			 checked.push(node); 
    	 			 if( !node.isLeaf() ) { 
    	 			 for(var i = 0; i < node.childNodes.length; i++ ) { 
    	 			 checked = checked.concat( me.getCheckedNodes(node.childNodes[i])); 
    	 			 } 
    	 		 } 
    	 	 } 
    	 	 return checked; 
    	  },
    initComponent: function () {
        var me = this;
        me.id = 'authorityWindow';
        me.assignAuthorityUrl="sys/autho/roleAssignAuthority.f";//保存权限
        me.assignAuthorityShowUrl="sys/autho/deleteRoleAssignAuthority.f";//删除权限
    	me.treeLoaderUrl="sys/autho/authorityTreeLoader.f";//权限树        
    	me.roleTreePanel = Ext.getCmp('roleTreePanel');//得到叶签
    	var root = me.findRootNode(); 	
    	//授权树
        me.authoritytree = Ext.create('FHD.ux.TreePanel',{
        	// var me = this;
        	autoScroll : true,
			rootVisible : true,		
			autoWidth:true,	
			checkModel : 'cascade',
			url : me.treeLoaderUrl, 
			root:root,
			extraParams:{
				id:this.typeId,//me.roleTreePanel.roleTreeId,
				authorityType:this.type
			},
			tbar:new Ext.Toolbar({
				height:25,
				items : [
						{iconCls: 'icon-save',text:'添加权限',scope: this,id:'ssaveAssignAuthority',
 	    		           disabled : false,			
	                       handler:me.assignAuthority
						}
	                ]}),
			check: function(authoritytree,node,checked){
				me.checkedParentNodes(node,checked);//点击子节点选中父节点
				me.unCheckedParentNode(node);//没有子节点父节点不被选中
				
			}
        });
        //授权显示树
        me.authorityShowTree = Ext.create('FHD.ux.TreePanel',{
            //me.id = 'authorityShowTree';    	
        		autoScroll : true,
    			autoWidth:true,	
    			rootVisible : true,		
    			checkModel : 'cascade',
    			url:'sys/autho/showTreeLoader.f',
    			check: function(authorityShowTree,node,checked){
    				me.checkedParentNodes(node,checked);//点击子节点选中父节点
    				me.unCheckedParentNode(node);//没有子节点父节点不被选中
    			},
    			extraParams:{
    				id:this.typeId,
    				authorityType:this.type
    			},
    			tbar:new Ext.Toolbar({
    				height:25,
    				items : [
    						{iconCls: 'icon-del',text:'删除权限',scope: this,
    	 	    		       disabled : false,			
    	                       handler:me.assignAuthorityShow
    						}
    	            ]
    			}),
    			root:root,
        });
        Ext.applyIf(me, {
        	border:false,
        	modal:true,//是否模态窗口
        	
    		scroll:'auto',
        	title:"用户授权",
     	    layout: 'fit',
     	    items: [{
               xtype: 'panel',
               layout: {
            	   type: 'table',
                   columns: 2
               },
               defaults: {width:300, height:500},
               items:[ me.authoritytree,me.authorityShowTree],
           }]
        });
        me.callParent(arguments);
    }
});
/**
 * 为角色添加人员,基本信息面板
 * 继承于Ext.form.Panel
 * 
 * @author 翟辉
 */
Ext.define('FHD.view.sys.role.authority.AuthorityTree', {
    extend: 'FHD.ux.TreePanel',
    alias: 'widget.authorityTree',
   
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
 		if(me.roleTreeId!=undefined){
 			var nodes = me.getChecked();
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
 						id:me.roleTreeId,
 						roleItem: Ext.JSON.encode(roleArr),
 						authorityType:'role'
 					},
 					callback : function(object){
 						var authorityShowTree = Ext.getCmp('authorityShowTree');//得到权限树展示树(右)
 						authorityShowTree.store.load();
 						me.store.load();
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
    	 
    initComponent: function() {	
    	var me = this;
    	var root = me.findRootNode(); 	
    	var roleArrJs = [];
    	me.assignAuthorityUrl="sys/autho/roleAssignAuthority.f";//保存权限
    	me.treeLoaderUrl="sys/autho/authorityTreeLoader.f";//权限树  
    	me.id = 'authorityTree';
    	
    	Ext.apply(me, {
    		//autoScroll : true,
			//rootVisible : true,		
			autoWidth:true,	
			checkModel : 'cascade',
			url : me.treeLoaderUrl, 
			//dockedItems:[],
			root:root,
			extraParams:{
				id:me.roleTreeId,
				authorityType:'role'
			},
			tbar:new Ext.Toolbar({
				height:25,
				items : [
						{iconCls: 'icon-save',text:'添加权限',scope: this,
 	    		           disabled : false,	
	                       handler:me.assignAuthority
						}
	                ]}),
			check: function(me,node,checked){
				me.checkedParentNodes(node,checked);//点击子节点选中父节点
				me.unCheckedParentNode(node);//没有子节点父节点不被选中
				
			},
			/*listeners : {
				beforeload:function (store, records) {
					me.roleTreePanel.body.mask("保存中...","x-mask-loading");
				},
   				load: function (store, records) { //默认选择首节点
   					me.roleTreePanel.body.unmask();
                } 
           },*/
				//me.roleTreePanel.body.mask("保存中...","x-mask-loading");
				//me.roleTreePanel.body.unmask();
        });
        me.callParent(arguments);
        me.store.on('beforeload',function(){
        	var roleTreePanel = Ext.getCmp('roleTreePanel');//得到角色树
        	roleTreePanel.body.mask("权限树加载中...","x-mask-loading");
		});
        me.store.on('load',function(){
        	var roleTreePanel = Ext.getCmp('roleTreePanel');//得到角色树
        	roleTreePanel.body.unmask();
		});
     
       // var new_params = {'abc':me.roleTreeId};
	    //Ext.apply(searchField.store.proxy.extraParams, new_params);
    },
    //后台数据权限树id串
 	treeCheck:function(data){
 		//清空权限树
 		var me = this;
 		//me.emptyTree();	
 		//拆分字符串
 		var idmany = data.aid
 		var ids = idmany.substring(0,idmany.length-1);
 		var ida = ids.split(',');
 		for(var i=0;i<ida.length;i++){
 			me.checkTreeNode(ida[i]);
 		}
 	},
});

/**
 * 为角色添加人员,基本信息面板
 * 继承于Ext.form.Panel
 * 
 * @author 翟辉
 */
Ext.define('FHD.view.sys.role.authority.AuthorityShowTree', {
	extend: 'FHD.ux.TreePanel',
    alias: 'widget.authorityShowTree',
    findRootNode:function(){
    	var me = this;
    	var rootshow;
    	FHD.ajax({
       		async : false,
       		url :'sys/autho/findTreeRoot.f',
       		callback : function(objectMaps){
       			rootshow =objectMaps.authorty;
       		}
    	});
    	return rootshow;
    },
    
  //删除权限
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
 					url: me.assignAuthorityUrl,
 					params : {
 						id:me.roleTreeId,
 						roleItem: Ext.JSON.encode(roleArr),
 						authorityType:'role'
 					},
 					callback : function(object){
 						me.authorityTree.store.load();
 						me.store.load();
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
 	
    initComponent: function () {
        var me = this;
    	var rootshow = me.findRootNode();
    	me.authorityTree = Ext.getCmp('authorityTree');//得到权限树
    	me.assignAuthorityUrl="sys/autho/deleteRoleAssignAuthority.f";//保存权限
    	//var authorityTree = Ext.getCmp('authorityTree');//得到权限树
        me.id = 'authorityShowTree'; 
        
        Ext.apply(me, {
    		autoScroll : true,
			//animate : true,
			autoWidth:true,	
			checkModel : 'cascade',
			//dockedItems:[],
			//rootVisible : true,
			url:'sys/autho/showTreeLoader.f',
			check: function(me,node,checked){
				me.authorityTree.checkedParentNodes(node,checked);//点击子节点选中父节点
				me.authorityTree.unCheckedParentNode(node);//没有子节点父节点不被选中
			},
			extraParams:{
				id:me.roleTreeId,
				authorityType:'role'
			},
			tbar:new Ext.Toolbar({
				height:25,
				items : [
						{iconCls: 'icon-del',text:'删除权限',scope: this,
	 	    		       disabled : false,			
	                       handler:me.assignAuthority
						}
	            ]
			}),
			root:rootshow,
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
    }
});
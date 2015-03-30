/**
 * 人员GRID
 * 
 * @author 翟辉
 */
Ext.define('FHD.view.sys.role.RolePersonGridPanel', {
    extend: 'FHD.ux.GridPanel',
    alias: 'widget.rolePersonGridPanel',
     setstatus:function () {//设置你按钮可用状态
		var me = this;
		var gridLength = me.getSelectionModel().getSelection().length;
		//var dictTreeLength = dictTree.getSelectionModel().getSelection().length;
		var roleTreePanel = Ext.getCmp('roleTreePanel');
		var roleTreeLength = roleTreePanel.getSelectionModel().getSelection().length;
		Ext.getCmp('roleaddTop').setDisabled(roleTreeLength == 0);
		Ext.getCmp('roledeleteTop').setDisabled(gridLength == 0);
     },
     delrolePerson:function (){
		var me = this;
		var selection = me.getSelectionModel().getSelection();//得到选中的记录
		var roleArr = [];
		for ( var i = 0; i < selection.length; i++) {
			roleArr.push({roleId:selection[i].get('roleId'),userId:selection[i].get('usernameId')});
		}
		Ext.MessageBox.show({
   			title : '删除',
   			width : 260,                     
   			buttons : Ext.MessageBox.YESNO,
   			icon : Ext.MessageBox.QUESTION,
   			msg : me.delMsg,
   			//*****删除选择开始****//
       			fn : function(btn) {
       				if (btn == 'yes') {//确认删除
       					FHD.ajax({//ajax调用
       						url : 'sys/autho/delUserAndRole.f',
       						params: {
       				        	roleItem: Ext.JSON.encode(roleArr)
       						},
       						callback : function(data) {
       							me.store.load();	
       						}
       					});
       				}
       			}
           	//*******删除选择结束********//
           })
 	},
    addrolePerson:function (){
    	
    	var me = this;
    	if(me.roleTreeId!=undefined){
	    	var window = Ext.create('FHD.ux.org.EmpSelectorWindow',{
	    		 type:'emp',
	             multiSelect:true,
	             onSubmit:function(win){
	             	var hiddenValue = new Array();
	             	//me.valueStore.removeAll();
	             	win.selectedgrid.store.each(function(r){
	             		//me.valueStore.insert(0,r);
	             		hiddenValue.push(r.data);             		      		
	             	});
	             	me.value = Ext.JSON.encode(hiddenValue);//得到选中人员的JSON字符串
	             	FHD.ajax({//ajax调用
	        			url : 'sys/autho/addUserAndRole.f',
	        			params: {
	        				id:me.roleTreeId,
	        	        	roleItem: me.value
	        			},
	        			callback : function(data) {
	        				me.store.load();	
	        			}
	        		}); 	
	 			}
	    	});
	        window.show();
    	}
    },
    // 初始化方法
    initComponent: function() {
    	var me = this;
    	me.delMsg = FHD.locale.get('fhd.common.makeSureDelete');//删除弹出信息
    	me.id = 'rolePersonGridPanel';
    	me.roleQueryUrl = 'sys/autho/findrolePage.f';//角色信息列表（所有角色下的人员）
    	//员工列表项
    	me.rolePersonGridColums =[
    	    {
				text : '角色Id',
				dataIndex : 'roleId',
				hidden : true,
				flex : 1
			}, 
			{	
				text : '用户Id',
				dataIndex : 'usernameId',
				hidden : true,
				flex : 1
			},
			{
				header : '用户名',//用户名fhd.pages.test1.username
				flex : 1,
				dataIndex : 'username',
				sortable : true
			},{
				header : '员工姓名',//用户名fhd.pages.test1.username
				flex : 1,
				dataIndex : 'empname',
				sortable : true	
			},
			{
				text : '部门',//部门fhd.pages.test1.department
				flex : 1,
				dataIndex : 'organizationName',
				sortable : true
			},
			{
				text : '岗位',//岗位fhd.pages.test1.post
				flex : 1,
				dataIndex : 'post',
				sortable : true
			},
			{
				text : '公司',//岗位fhd.pages.test1.post
				flex : 1,
				dataIndex : 'companyname',
				sortable : true
			}
    	];
    	
    	me.rolePersonTbar =[//员工菜单项
    	    	           {text : FHD.locale.get('fhd.common.add'),
    	    		           iconCls: 'icon-add',id:'roleaddTop',
    	    		           disabled : true,
    	    		           handler:function(){me.addrolePerson();},
    	    		        },'-',
    	    			   {text : FHD.locale.get('fhd.common.delete'),
    	    	  			   iconCls: 'icon-del',id:'roledeleteTop',
    	    	  			   disabled : true,
    	    	  			   handler:function(){me.delrolePerson();}
    	    			   }
    	    			  
    	];
    	
    	Ext.apply(me, {
	       multiSelect: true,
	       border:true,
	       rowLines:true,//显示横向表格线
	       checked: true, //复选框
	       autoScroll:true,
	       title:"人员列表",
	       cols:me.rolePersonGridColums,//cols:为需要显示的列
	       tbarItems:me.rolePersonTbar
	       //url:me.queryUrl
        });
    	
    	me.on('selectionchange',function(){me.setstatus(me)});
        me.callParent(arguments);
    }, 
   
});
/**
 * 职务GRID
 * 
 * @author 
 */
Ext.define('FHD.view.sys.organization.positiion.PositionGridPanel', {
    extend: 'FHD.ux.GridPanel',
    alias: 'widget.positionGridPanel',
    
    // 初始化方法
    initComponent: function() {
    	var me = this;
    	me.id = 'positionGridPanel';
    	var orgId;//机构id
    	var gridFlag;
    	
    	me.delUrl='sys/organization/removeposientrybyids.f';	//删除岗位
    	me.queryUrl = 'sys/organization/queryPositionAll.f';//查询岗位
    	//岗位列表项
    	me.gridColums =[
    		{header: 'id' ,
    			dataIndex: 'id',sortable: true,flex : 1,hidden : true},
    		{header: FHD.locale.get('fhd.sys.orgstructure.posi.posiname') ,
				dataIndex: 'posiname',sortable: true,flex : 1},//岗位名称
    		{header: FHD.locale.get('fhd.sys.orgstructure.posi.posicode') ,
				dataIndex: 'posicode',sortable: true,flex : 1},//岗位编号
			{header: FHD.locale.get('fhd.sys.planEdit.startTime') ,
				dataIndex: 'posiStartDateStr',sortable: true,flex : 1,hidden : true},//开始时间    		
    		{header: FHD.locale.get('fhd.sys.planEdit.entTime') ,
				dataIndex: 'posiEndDateStr',sortable: true,flex : 1,hidden : true},//结束时间
    		{header: '岗位状态' ,
				dataIndex: 'posiStatus',sortable: true,flex : 1,
    			renderer:function(dataIndex) { 
    				  if(dataIndex == "1"){
    					  return "正常";
    				  }else {
    					  return "注销";
    				  }
    		}},//岗位状态
    		{header: '排列顺序' ,
    			dataIndex: 'posiSnStr',sortable: true,flex : 1,hidden : true},//排列顺序
    		{header: FHD.locale.get('fhd.sys.duty.dutyremark') ,
				dataIndex: 'remark',sortable: true,flex : 1,hidden : true}//备注
    	];
    	
    	me.tbar =[//岗位菜单项
    	           {text : FHD.locale.get('fhd.common.add'),
    	        	   iconCls: 'icon-add',id:'positionadd${param._dc}', handler:function(){me.edit('add', me, false);}, scope : this},'-',
    			   {text : FHD.locale.get('fhd.common.modify'),
	        		   iconCls: 'icon-edit',id:'positionedit${param._dc}', handler:function(){me.edit('edit', me, false);},
	        		   disabled : true, scope : this} ,'-',
    			   {text : FHD.locale.get('fhd.common.delete'),
        			   iconCls: 'icon-del',id:'positiondel${param._dc}', handler:function(){me.del(me);}, disabled : true, scope : this},'-',
        		   /*{text : '员工关联',
            		   iconCls: 'icon-plugin-add',id:'yggl', handler:function(){me.addEmp();}, disabled : true, scope : this}*/
        			{text : '操作',id:'yggl',iconCls: 'icon-group-go',disabled : true,menu:[
        			        {text : '分配用户',iconCls: 'icon-plugin-add',id:'fpyh',handler:function(){me.addEmp();}, scope : this},'-',
        			        {text : '查看用户',iconCls: 'icon-zoom',id:'ckyh',handler:function(){me.goPosiEmpGrid();}, scope : this}
        			 ]}
    	];	
    	
    	Ext.apply(me, {
    		multiSelect: true,
            border:true,
            rowLines:true,//显示横向表格线
            checked: true, //复选框
            autoScroll:true,
            title:"下级岗位",
    		cols:me.gridColums,//cols:为需要显示的列
    		tbarItems:me.tbar
    		//,url:me.queryUrl
        });
    	
    	me.on('selectionchange',function(){me.setstatus(me)});
    	
        me.callParent(arguments);
    },
    //分配员工
    addEmp:function (){
    	var me = this;
    	var selection = me.getSelectionModel().getSelection()[0];//得到选中的记录
    	var cardPanel = Ext.getCmp('cardPanel');
    	var positionEditPanel = Ext.getCmp('positionEditPanel');
    	var positionTabPanel = Ext.getCmp('positionTabPanel');
    	var positionempGridPanel = Ext.getCmp('positionempGridPanel');
    	var treepanel = Ext.getCmp('treePanel');
    	//me.orgtreeId = positionEditPanel.orgtreeId;
    	//alert(me.orgtreeId);
    	if(selection!=undefined){
	    	var window = Ext.create('FHD.ux.org.EmpSelectorWindow',{
	    		 type:'emp',
	             multiSelect:true,
	             onSubmit:function(win){
	             	var hiddenValue = new Array();
	             	win.selectedgrid.store.each(function(r){
	             		hiddenValue.push(r.data);             		      		
	             	});
	             	me.value = Ext.JSON.encode(hiddenValue);//得到选中人员的JSON字符串
	             	FHD.ajax({//ajax调用
	        			url : 'sys/organization/addempunderposi.f',
	        			params: {
	        				posiId:selection.get('id'),
	        	        	empItem: me.value
	        			},
	        			callback : function(data) {
	        				var rootNode = treepanel.getRootNode();//得到根节点
	        				var selectNode = treepanel.findNode(rootNode,selection.get('id'));
	        				treepanel.getSelectionModel().select(selectNode);//选中节点
	        				treepanel.currentNode = selectNode;//将选中的节点传给树
	        				treepanel.itemclickTree(treepanel.currentNode);
	        		    	cardPanel.getLayout().setActiveItem(positionTabPanel);
	        		    	if('positionempGridPanel'==positionTabPanel.getActiveTab().id){//避免二次请求
	        		    		positionempGridPanel.store.proxy.url = positionempGridPanel.queryUrl;
	        		    		positionempGridPanel.store.proxy.extraParams.positionIds = selection.get('id');
	        		    		positionempGridPanel.store.load();
	        		    	}else{
	        		    		positionTabPanel.setActiveTab(0);//跳转岗位员工列表
	        		    	}
	        		    	var positionEditPanel = Ext.getCmp('positionEditPanel');
	        		    	//跳转到岗位员工tab，把岗位id传过去
	        		    	positionEditPanel.orgtreeId = selection.get('id');
	        		    	
	        			}
	        		}); 	
	 			}
	    	});
	        window.show();
    	}
    },

    
    edit : function(id, me, isMenu){//岗位新增方法
    	var cardPanel = Ext.getCmp('cardPanel');
    	var positionTabPanel = Ext.getCmp('positionTabPanel');
    	var positionEditPanel = Ext.getCmp('positionEditPanel');
    	if(id=='add'){//新增
    		cardPanel.getLayout().setActiveItem(positionTabPanel);
        	positionTabPanel.setActiveTab(1);
    		positionEditPanel.getForm().reset();
    		me.isAdd = true;
    	}else{
    		if(id == 'edit'){//修改
    			if(!isMenu){
    				var selections = me.getSelectionModel().getSelection();//得到选中的记录
    				var length = selections.length;
   	    	     if (length >= 2) {//判断是否多选
   	    	          Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.updateTip'));//提示
   	    	          return;
   	    	      }else{
   	    	    	cardPanel.getLayout().setActiveItem(positionTabPanel);
    		    	positionTabPanel.setActiveTab(1);
    		    	var selection = selections[0];
    				positionEditPanel.orgtreeId = selection.data.id;
    				
    				var treepanel = Ext.getCmp('treePanel');
    				var rootNode = treepanel.getRootNode();//得到根节点
    				var selectNode = treepanel.findNode(rootNode,selection.get('id'));
    				treepanel.getSelectionModel().select(selectNode);//选中节点
    				treepanel.currentNode = selectNode;//将选中的节点传给树
    				//treepanel.itemclickTree(treepanel.currentNode);
   	    	      }
    			}
    			/*cardPanel.getLayout().setActiveItem(positionTabPanel);
    	    	positionTabPanel.setActiveTab(1);*/
    		}else{
    			if(!isMenu){
    				positionEditPanel.orgtreeId = orgtreeId;
    			}
    		}
    		
    		
    		positionEditPanel.load();
    		me.isAdd = false;
    	}
    },
    
    //查看用户
    goPosiEmpGrid : function(me){
    	var me = this;
    	var selection = me.getSelectionModel().getSelection()[0];//得到选中的记录
    	var cardPanel = Ext.getCmp('cardPanel');
    	var positionTabPanel = Ext.getCmp('positionTabPanel');
    	var posiEmpGrid = Ext.getCmp('positionempGridPanel');
    	var treepanel = Ext.getCmp('treePanel');
    	//var positionEditPanel = Ext.getCmp('positionEditPanel');
    	cardPanel.getLayout().setActiveItem(positionTabPanel);
    	positionTabPanel.setActiveTab(0);
    	//跳转到岗位员工tab，把岗位id传过去
    	positionEditPanel.orgtreeId = selection.get('id');
    	posiEmpGrid.store.proxy.url = posiEmpGrid.queryUrl;
		posiEmpGrid.store.proxy.extraParams.positionIds = selection.get('id');
		posiEmpGrid.store.load();
    	var rootNode = treepanel.getRootNode();//得到根节点
		var selectNode = treepanel.findNode(rootNode,selection.get('id'));
		treepanel.getSelectionModel().select(selectNode);//选中节点
		treepanel.currentNode = selectNode;//将选中的节点传给树
		treepanel.itemclickTree(treepanel.currentNode);
    },
    
    setstatus : function(me){//设置按钮可用状态
    	Ext.getCmp('positiondel${param._dc}').setDisabled(me.getSelectionModel().getSelection().length === 0);
    	Ext.getCmp('positionedit${param._dc}').setDisabled(me.getSelectionModel().getSelection().length === 0);
    	Ext.getCmp('yggl').setDisabled(me.getSelectionModel().getSelection().length === 0);
    },
    
    del : function(me){//岗位删除方法
    	var selection = me.getSelectionModel().getSelection();//得到选中的记录
    	Ext.MessageBox.show({
    		title : FHD.locale.get('fhd.common.delete'),
    		width : 260,
    		msg : FHD.locale.get('fhd.common.makeSureDelete'),
    		buttons : Ext.MessageBox.YESNO,
    		icon : Ext.MessageBox.QUESTION,
    		fn : function(btn) {
    			if (btn == 'yes') {//确认删除
    				var ids = [];
    				for(var i=0;i<selection.length;i++){
    					ids.push(selection[i].get('id'));
    				}
    				FHD.ajax({//ajax调用
    					url : me.delUrl,
    					params : {
    						ids:ids.join(',')
    					},
    					callback : function(data){
    						if(data){//删除成功！
    							Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
    							me.store.load();
    						}
    					}
    				});
    			}
    		}
    	});
    }
});
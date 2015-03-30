/**
 * 机构GRID
 * 
 * @author 金鹏祥
 */
Ext.define('FHD.view.sys.organization.org.OrgGridPanel', {
    extend: 'FHD.ux.GridPanel',
    alias: 'widget.orgGridPanel',
    
    // 初始化方法
    initComponent: function() {
    	var me = this;
    	
    	me.id = variable + 'orgGridPanel';
    	me.delUrl='sys/organization/removeOrgEntryById.f';	//删除机构
    	me.queryUrl = 'sys/organization/queryOrgPage.f';//查询所有机构
    	//机构列表项
    	me.gridColums =[
    		{header: 'id' ,
    			dataIndex: 'id',sortable: true,flex : 1,hidden : true},
    		{header: FHD.locale.get('fhd.sys.orgstructure.org.orgcode') ,
    			dataIndex: 'orgcode',sortable: true,flex : 1, hidden : true},//机构编号
    		{header: FHD.locale.get('fhd.sys.orgstructure.org.orgname') ,
				dataIndex: 'orgname',sortable: true,flex : 1},//机构名称
    		{header: FHD.locale.get('fhd.sys.orgstructure.org.parentorgname') ,
				dataIndex: 'parentOrgStr',sortable: true,flex : 1},//上级机构
    		{header: '机构层级' ,
				dataIndex: 'orgLevel',sortable: true,flex : 1,hidden : true},//机构层级
    		{header: FHD.locale.get('fhd.sys.orgstructure.org.orgtype') ,
    			dataIndex: 'orgType',sortable: true,flex : 1, //机构类型
    			renderer:function(dataIndex) { 
    				  if(dataIndex == "0orgtype_c"){
    					  return "总公司";
    				  }else if(dataIndex == "0orgtype_d"){
    					  return "总公司部门";
    				  }else if(dataIndex == "0orgtype_sc"){
    					  return "分公司";
    				  }else if(dataIndex == "0orgtype_sd"){
    					  return "分公司部门"; 
    				  }
    		}},
    		{header: FHD.locale.get('fhd.sys.orgstructure.org.forum') ,dataIndex: 'forum',sortable: true,flex : 1,hidden : true, //业务板块
    			renderer:function(dataIndex) { 
    				  if(dataIndex == "0forum_dl"){
    					  return "电力";
    				  }else if(dataIndex == "0forum_fl"){
    					  return "风力";
    				  }else if(dataIndex == "0forum_sl"){
    					  return "水力";
    				  }
    		}},
    		{header: FHD.locale.get('fhd.sys.orgstructure.org.region') ,
    			dataIndex: 'region',sortable: true,flex : 1,hidden : true},//区域
    		{header: FHD.locale.get('fhd.sys.orgstructure.org.orgaddress') ,
				dataIndex: 'address',sortable: true,flex : 1,hidden : true},//机构地址
    		{header: FHD.locale.get('fhd.sys.orgstructure.org.orgzipcode') ,
				dataIndex: 'zipcode',sortable: true,flex : 1,hidden : true},//邮政编码
    		{header: FHD.locale.get('fhd.sys.orgstructure.org.linktel') ,
				dataIndex: 'linkTel',sortable: true,flex : 1,hidden : true},//联系电话
    		{header: FHD.locale.get('fhd.sys.orgstructure.org.linkMan') ,
				dataIndex: 'linkMan',sortable: true,flex : 1,hidden : true},//联系人
    		{header: FHD.locale.get('fhd.sys.orgstructure.org.email') ,
				dataIndex: 'email',sortable: true,flex : 1,hidden : true},//电子邮件
    		{header: FHD.locale.get('fhd.sys.orgstructure.org.weburl') ,
				dataIndex: 'weburl',sortable: true,flex : 1,hidden : true},//网站地址
    		{header: FHD.locale.get('fhd.sys.planEdit.startTime') ,
				dataIndex: 'startDataStr',sortable: true,flex : 1,hidden : true},//开始时间
    		{header: FHD.locale.get('fhd.sys.planEdit.entTime') ,
				dataIndex: 'endDataStr',sortable: true,flex : 1,hidden : true},//结束时间
    		{header: FHD.locale.get('fhd.sys.orgstructure.org.orgstatus') ,
				dataIndex: 'orgStatus',sortable: true,flex : 1,
    			renderer:function(dataIndex) { 
    				  if(dataIndex == "1"){
    					  return "正常";
    				  }else {
    					  return "注销";
    				  }
    		}},//机构状态
    		{header: '排列顺序' ,
    			dataIndex: 'snStr',sortable: true,flex : 1,hidden : true},//排列顺序
    		{header: FHD.locale.get('fhd.sys.duty.dutyremark') ,
				dataIndex: 'remark',sortable: true,flex : 1,hidden : true}//备注
    	];
    	
    	me.tbar =[//机构菜单项
    	           {text : FHD.locale.get('fhd.common.add'),
    	        	   iconCls: 'icon-add',id:'orgadd'+variable, handler:function(){me.edit('add', me, false,true);}, scope : this},'-',
    			   {text : FHD.locale.get('fhd.common.modify'),
	        		   iconCls: 'icon-edit',id:'orgedit'+variable, handler:function(){me.edit('edit', me, false);},
	        		   disabled : true, scope : this} ,'-',
    			   {text : FHD.locale.get('fhd.common.delete'),
        			   iconCls: 'icon-del',id:'orgdel'+variable, handler:function(){me.del(me);}, disabled : true, scope : this}
    	];	
    	
    	Ext.apply(me, {
    		multiSelect: true,
            border:true,
            rowLines:true,//显示横向表格线
            checked: true, //复选框
            autoScroll:true,
            title:"下级机构",
    		cols:me.gridColums,//cols:为需要显示的列
    		tbarItems:me.tbar
    		//,url:me.queryUrl
        });
    	
    	me.on('selectionchange',function(){me.setstatus(me)});
    	
        me.callParent(arguments);
    },
    
    edit : function(id, me, isMenu,isSameLevelAdd){//机构新增方法
    	var cardPanel = Ext.getCmp('cardPanel');
    	var tabPanel = Ext.getCmp('tabPanel');
    	var treepanel = Ext.getCmp('treePanel');
    	var orgEditPanel = Ext.getCmp('orgEditPanel');
    	var orgEditPanel_commenSel = Ext.getCmp('parentOrgStrId');//上级机构
    	if(id=='add'|| id=='orgAdd'){//新增机构
    		orgEditPanel_commenSel.clearValues();//清空上级机构组件缓存
    		orgEditPanel.newFlag = 'addOrg';
    		orgEditPanel.isSameLevel = isSameLevelAdd;//是否添加同级
    		cardPanel.getLayout().setActiveItem(tabPanel);
    		if('orgEditPanel'==tabPanel.getActiveTab().id){//活动页签为editPanel，需要手动调用parentOrgLoad()方法
    			orgEditPanel.orgtreeId = treepanel.currentNode.data.id;//将节点id传给edit页面
    			orgEditPanel.parentOrgLoad();
    		}else{
    			tabPanel.setActiveTab(3);
    		}
    		orgEditPanel.getForm().reset(); 
    		me.isAdd = true;
    	}else{
    		orgEditPanel.newFlag = 'editOrg';
    		if(id == 'edit'){
    			if(!isMenu){
    				var selections = me.getSelectionModel().getSelection();//得到选中的记录
    				 var length = selections.length;
    	    	     if (length >= 2) {//判断是否多选
    	    	          Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.updateTip'));//提示
    	    	          return;
    	    	      }else{
    	    	    	  cardPanel.getLayout().setActiveItem(tabPanel);
    	    	    	  tabPanel.setActiveTab(3);
    	    	    	  var selection = selections[0];
    	    	    	  orgEditPanel.orgtreeId = selection.data.id;
    	    	      }
    			}
    			cardPanel.getLayout().setActiveItem(tabPanel);
		    	tabPanel.setActiveTab(3);
    		}else{
    			if(!isMenu){
    				orgEditPanel.orgtreeId = orgtreeId;
    			}
    		}
    		orgEditPanel.load();
    		me.isAdd = false;
    	}
    },
    
    setstatus : function(me){//设置按钮可用状态
    	Ext.getCmp('orgeditposition').setDisabled(me.getSelectionModel().getSelection().length === 0);
    	Ext.getCmp('orgdelposition').setDisabled(me.getSelectionModel().getSelection().length === 0);
    	Ext.getCmp('orgedittabpanel').setDisabled(me.getSelectionModel().getSelection().length === 0);
    	Ext.getCmp('orgdeltabpanel').setDisabled(me.getSelectionModel().getSelection().length === 0);
    },
    
    del : function(me){//机构删除方法
    	var selection = me.getSelectionModel().getSelection();//得到选中的记录
    	var treepanel = Ext.getCmp('treePanel');
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
    							treepanel.store.load();
    						}else{//该节点存在子集
    							Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'有子集不能删除');
    						}
    					}
    				});
    			}
    		}
    	});
    }
});
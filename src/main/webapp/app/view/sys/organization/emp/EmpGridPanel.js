/**
 * 人员GRID
 * 
 * @author 王再冉
 */
Ext.define('FHD.view.sys.organization.emp.EmpGridPanel', {
    extend: 'FHD.ux.GridPanel',
    alias: 'widget.empGridPanel',
    
    requires: [
               'FHD.view.sys.organization.emp.EmpRoleGridPanel'
    ],
    
    // 初始化方法
    initComponent: function() {
    	var me = this;
    	me.id = variable + 'empGridPanel';
    	
    	me.empRoleGridPanel = Ext.widget('empRoleGridPanel');
    
    	me.rolewin = new Ext.Window({
    		id:'rolewin',
    	    title: '用户授权',
    	    closeAction:'hide',
    	    height: 500,
    	    width: 600,
    	    layout: 'fit',
    	    items: [me.empRoleGridPanel]
    	});
    	
    	me.queryUrl = 'sys/organization/queryemppage.f';//员工查询列表Url
    	me.empdelUrl = 'sys/organization/removeempentrybyid.f';//删除员工url
    	me.empPosidelUrl = 'sys/organization/removeempposientrybysome.f';
    	me.resetPasswordUrl = 'sys/organization/resetuserpassword.f';//重置密码
//    	me.gridqueryUrl = 'sys/organization/queryOrgPage.f';//查询所有机构
    	//员工列表项
    	me.gridColums =[
    	             	{header: 'id' ,
    	             		dataIndex: 'id',sortable: true,flex : 1,hidden : true},
    	             	{header: 'userid' ,
        	             	dataIndex: 'userid',sortable: true,flex : 1,hidden : true},//用户id
    	             	{header: FHD.locale.get('fhd.sys.orgstructure.emp.empcode') ,
    	             		dataIndex: 'empcode',sortable: true,flex : 1,hidden : true},//员工编号
    	             	{header: FHD.locale.get('fhd.common.username') ,
	             			dataIndex: 'username',sortable: true,flex : 1},//用户名
    	             	{header: FHD.locale.get('fhd.sys.orgstructure.emp.empname') ,
    	             		dataIndex: 'empname',sortable: true,flex : 1},//员工姓名
    	             	{header: '角色' ,
            	          	dataIndex: 'roleName',sortable: true,flex : 1},//角色
    	             	{header: FHD.locale.get('fhd.sys.auth.role.realName') ,
    	             		dataIndex: 'realname',sortable: true,flex : 1,hidden : true},//真实姓名
    	             	{header: FHD.locale.get('fhd.sys.orgstructure.emp.gender') ,
    	             		dataIndex: 'gender',sortable: true,flex : 1,hidden : true,
    	             		renderer:function(dataIndex) { 
    	          			  if(dataIndex == "0gender_m"){
    	          				  return "男";
    	          			  }else if(dataIndex == "0gender_f"){
    	          				  return "女";
    	          			  }
    	             	}},//性别
    	             	{header: FHD.locale.get('fhd.sys.orgstructure.emp.birthday') ,
    	             		dataIndex: 'birthDateStr',sortable: true,flex : 1,hidden : true},//生日
    	             	{header: '主部门' ,
    	          			dataIndex: 'orgStrMain',sortable: true,flex : 1},//部门
    	          		{header: '辅部门' ,
        	          		dataIndex: 'orgStrs',sortable: true,flex : 1},//部门
    	          		{header: '公司' ,
        	          		dataIndex: 'companyOrg',sortable: true,flex : 1},//公司
    	          		
    	          		{header: '职务' ,
    	             		dataIndex: 'dutyStr',sortable: true,flex : 1,hidden : true},//职务
    	             	{header: FHD.locale.get('fhd.common.status') ,
	             			dataIndex: 'empStatus',sortable: true,flex : 1,//状态
    	             		renderer:function(dataIndex) { 
    	          			  if(dataIndex == "1"){
    	          				  return "正常";
    	          			  }else {
    	          				  return "注销";
    	          			  }
    	             	}},
    	             	{header: FHD.locale.get('fhd.sys.orgstructure.emp.otel') ,
    	             		dataIndex: 'otel',sortable: true,flex : 1,hidden : true},//公司电话
    	             	{header: FHD.locale.get('fhd.sys.orgstructure.emp.oaddress') ,
	             			dataIndex: 'oaddress',sortable: true,flex : 1,hidden : true},//公司地址
    	             	{header: FHD.locale.get('fhd.sys.orgstructure.emp.ozipcode') ,
             				dataIndex: 'ozipcode',sortable: true,flex : 1, hidden : true},//公司邮编
    	             	{header: FHD.locale.get('fhd.sys.orgstructure.emp.oemail') ,
         					dataIndex: 'oemail',sortable: true,flex : 1,hidden : true},//公司邮箱
    	             	{header: FHD.locale.get('fhd.sys.orgstructure.emp.faxno') ,
     						dataIndex: 'faxno',sortable: true,flex : 1,hidden : true},//传真号码
    	             	{header: FHD.locale.get('fhd.sys.orgstructure.emp.cardtype') ,
 							dataIndex: 'cardtype',sortable: true,flex : 1,hidden : true},//证件类型
    	             	{header: FHD.locale.get('fhd.sys.orgstructure.emp.cardno') ,
 							dataIndex: 'cardno',sortable: true,flex : 1,hidden : true},//证件号码
    	             	{header: FHD.locale.get('fhd.sys.orgstructure.emp.mobikeno') ,
							dataIndex: 'mobikeno',sortable: true,flex : 1,hidden : true},//手机号
    	             	{header: 'MSN' ,
							dataIndex: 'msn',sortable: true,flex : 1,hidden : true},//MSN
    	             	{header: FHD.locale.get('fhd.sys.orgstructure.emp.htel') ,
    	             		dataIndex: 'htel',sortable: true,flex : 1,hidden : true},//家庭电话
    	             	{header: FHD.locale.get('fhd.sys.orgstructure.emp.haddress') ,
	             			dataIndex: 'haddress',sortable: true,flex : 1, hidden : true},//家庭地址
    	             	{header: FHD.locale.get('fhd.sys.orgstructure.emp.hzipcode') ,
             				dataIndex: 'hzipcode',sortable: true,flex : 1,hidden : true},//家庭邮编
    	             	{header: FHD.locale.get('fhd.sys.orgstructure.emp.pemail') ,
         					dataIndex: 'pemail',sortable: true,flex : 1,hidden : true},//个人邮箱
    	             	{header: FHD.locale.get('fhd.sys.orgstructure.emp.party') ,
     						dataIndex: 'party',sortable: true,flex : 1, hidden : true},//政治面貌
    	             	{header: FHD.locale.get('fhd.sys.orgstructure.emp.degree') ,
 							dataIndex: 'degree',sortable: true,flex : 1, hidden : true},//学位
    	             	{header: FHD.locale.get('fhd.sys.orgstructure.emp.major') ,
							dataIndex: 'major',sortable: true,flex : 1, hidden : true},//专业
    	             	{header: FHD.locale.get('fhd.sys.orgstructure.emp.specialty') ,
							dataIndex: 'specialty',sortable: true,flex : 1, hidden : true},//特长
    	             	{header: FHD.locale.get('fhd.common.regdate') ,
							dataIndex: 'regdateStr',sortable: true,flex : 1, hidden : true},//注册日期
    	             	{header: FHD.locale.get('fhd.sys.duty.dutyremark') ,
							dataIndex: 'remark',sortable: true,flex : 1,hidden : true},//备注
    	             	{header: FHD.locale.get('fhd.sys.auth.user.password') ,
							dataIndex: 'userPassword',sortable: true,flex : 1, hidden : true},//密码
    	                {header: "密码失效日期" ,
							dataIndex: 'userCredentialsexpiryDateStr',sortable: true,flex : 1, hidden : true},//密码失效日期
    	             	{header: "最后登录时间" ,
							dataIndex: 'userLastLoginTimeStr',sortable: true,flex : 1, hidden : true},//最后登录时间
    	             	{header: "用户状态" ,
							dataIndex: 'userState',sortable: true,flex : 1, hidden : true,//用户状态
    	             		renderer:function(dataIndex) { 
    	            			  if(dataIndex == "1"){
    	            				  return "正常";
    	            			  }else {
    	            				  return "注销";
    	            			  }
    	               	}},
    	             	{header: "锁定状态" ,
    	               		dataIndex: 'lockstate',sortable: true,flex : 1, hidden : true},//锁定状态
    	             	{header: "是否启用" ,
	               			dataIndex: 'enable',sortable: true,flex : 1,renderer:function(dataIndex) { 
  	            			  if(dataIndex == "1"||dataIndex == "true"){
	            				  return "是";
	            			  }else {
	            				  return "否";
	            			  }
	               	}}//是否启用
    	];
    	
    	/*me.tbar =[//员工菜单项
       	           {text : FHD.locale.get('fhd.common.add'),
       	        	   iconCls: 'icon-add',id:'empadd'+variable, handler:function(){me.empedit('add', me);}, scope : this},'-',
       			   {text : FHD.locale.get('fhd.common.modify'),
   	        		   iconCls: 'icon-edit',id:'empedit'+variable, handler:function(){me.empedit('edit', me);},
   	        		   disabled : true, scope : this} ,'-',
       			   {text : FHD.locale.get('fhd.common.delete'),
           			   iconCls: 'icon-del',id:'empdel'+variable, handler:function(){me.empdel(me);}, disabled : true, scope : this},'-',
           		   {text : '密码重置',
                   	   iconCls: 'icon-wrench',id:'resetpassword'+variable, handler:function(){me.resetPassword(me);}, disabled : true, scope : this},'-',
           		   {text : '分配角色',
               		   iconCls: 'icon-user-suit',id:'emprole'+variable, handler:function(){me.emprole(me);}, disabled : true, scope : this},'-',
               	   {text : '用户授权',
                   	   iconCls: 'icon-computer-go',id:'yhsq'+variable, handler:function(){}, disabled : true, scope : this}
       	];	*/
    	//列表菜单
    	me.tbar = Ext.create('Ext.toolbar.Toolbar', {
    		id:'empGridTbar'+variable,
    		border:'0 0 0 0',//设置菜单边框
    	    items: [//员工菜单项
    	            {text : FHD.locale.get('fhd.common.add'),
    	            	iconCls: 'icon-add',id:'empadd'+variable, handler:function(){me.empedit('add', me);}, scope : this},'-',
    	            {text : FHD.locale.get('fhd.common.modify'),
    	            	iconCls: 'icon-edit',id:'empedit'+variable, handler:function(){me.empedit('edit', me);},
    	            	disabled : true, scope : this} ,'-',
    	            {text : FHD.locale.get('fhd.common.delete'),
    	            	iconCls: 'icon-del',id:'empdel'+variable, handler:function(){me.empdel(me);}, disabled : true, scope : this},'-',
    	            {text : '密码重置',
    	            	iconCls: 'icon-wrench',id:'resetpassword'+variable, handler:function(){me.resetPassword(me);}, disabled : true, scope : this},'-',
    	            {text : '分配角色',
    	            	iconCls: 'icon-user-suit',id:'emprole'+variable, handler:function(){me.emprole(me);}, disabled : true, scope : this},'-',
    	            {text : '用户授权',
    	            	iconCls: 'icon-computer-go',id:'yhsq'+variable, handler:function(){me.userAuthrity(me)}, disabled : true, scope : this}
    	    ]
    	});
    		

    	Ext.apply(me, {
	       multiSelect: true,
	       border:true,
	       rowLines:true,//显示横向表格线
	       checked: true, //复选框
	       autoScroll:true,
	       title:"下级员工",
	       cols:me.gridColums,//cols:为需要显示的列
	       tbarItems:me.tbar
	       //,url:me.queryUrl
        });
    	
    	me.on('selectionchange',function(){me.setstatus(me)});
    	
        me.callParent(arguments);
    },
    
    userAuthrity : function(me){//用户授权方法
    	var selections = me.getSelectionModel().getSelection();//得到选中的记录
    	var length = selections.length;
	     if (length >= 2) {//判断是否多选
	          Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), '只能同时给一位用户授权');//提示
	          return;
	      }else{
	    	  var selection = selections[0];
	    	  var window = Ext.create('FHD.view.sys.role.authority.AuthorityWindow',{
	      		//typeId:'baiaimei',
	      		typeId:selection.get('userid'),
	      	    type:'user',
	      	 });
	      	window.show();
	      }
    	
    },

    
    empedit : function(id, me){//员工新增方法
    	var selections = me.getSelectionModel().getSelection();//得到选中的记录
    	var cardPanel = Ext.getCmp('cardPanel');
    	var empEditPanel = Ext.getCmp('empEditPanel');
    	var rightPanel = Ext.getCmp('rightPanel');
    	var empEditPanel_mainorg = Ext.getCmp('mainorgId');
    	var empEditPanel_assistorg = Ext.getCmp('assistorgId');
		rightPanel.items.items[0].setHeight(40);//设置empEditPanel的高度
    	if(id=='add'){
    		cardPanel.getLayout().setActiveItem(empEditPanel);
    		empEditPanel_mainorg.clearValues();//清空主责部门组件缓存
    		empEditPanel_assistorg.clearValues();//清空辅助部门缓存
    		empEditPanel.getForm().reset();
    		me.isAdd = true;
    	}else{
    		 var length = selections.length;
    	     if (length >= 2) {//判断是否多选
    	          Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.updateTip'));//提示
    	          return;
    	      }else{
    	    	  var selection = selections[0];
    	    	  cardPanel.getLayout().setActiveItem(empEditPanel);
    	    	  empEditPanel.empgridId = selection.data.id;
    	    	  empEditPanel.load();
    	    	  me.isAdd = false;
    	      }
    	}
    },
    
    setstatus : function(me){//设置按钮可用状态
    	Ext.getCmp('empeditposition').setDisabled(me.getSelectionModel().getSelection().length === 0);
    	Ext.getCmp('empdelposition').setDisabled(me.getSelectionModel().getSelection().length === 0);
    	Ext.getCmp('empedittabpanel').setDisabled(me.getSelectionModel().getSelection().length === 0);
    	Ext.getCmp('empdeltabpanel').setDisabled(me.getSelectionModel().getSelection().length === 0);
    	Ext.getCmp('emproletabpanel').setDisabled(me.getSelectionModel().getSelection().length === 0);//分配角色
    	Ext.getCmp('emproleposition').setDisabled(me.getSelectionModel().getSelection().length === 0);
    	Ext.getCmp('yhsqtabpanel').setDisabled(me.getSelectionModel().getSelection().length === 0);//用户授权
    	Ext.getCmp('yhsqposition').setDisabled(me.getSelectionModel().getSelection().length === 0);
    	Ext.getCmp('resetpasswordtabpanel').setDisabled(me.getSelectionModel().getSelection().length === 0);//密码重置
    	Ext.getCmp('resetpasswordposition').setDisabled(me.getSelectionModel().getSelection().length === 0);
    },
    
    emprole : function(me){//分配角色方法
    	var selection = me.getSelectionModel().getSelection();//得到选中的记录
    	var ids = [];
		for(var i=0;i<selection.length;i++){
			ids.push(selection[i].get('id'));//得到选中员工id
		}
    	me.empRoleGridPanel.empIds = ids;
    	me.rolewin.show();
    },
    
    resetPassword : function(me){//密码重置方法
    	var selection = me.getSelectionModel().getSelection();//得到选中的记录
    	Ext.MessageBox.show({
    		title : FHD.locale.get('fhd.common.prompt'),
    		width : 260,
    		msg : '确定重置密码吗？',
    		buttons : Ext.MessageBox.YESNO,
    		icon : Ext.MessageBox.QUESTION,
    		fn : function(btn) {
    			if (btn == 'yes') {//确认重置
    					var ids = [];
        				for(var i=0;i<selection.length;i++){
        					ids.push(selection[i].get('userid'));
        				}
        				FHD.ajax({//ajax调用
        					url : me.resetPasswordUrl,//删除岗位员工实体
        					params : {
        						userIds:ids.join(',')
        					},
        					callback : function(data){
        						if(data){//重置成功！
        							Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'重置成功');
        						}
        					}
        				});
    				}
    			}
    	});
    },
    
    empdel : function(me){//员工删除方法
    	var positionEditPanel = Ext.getCmp('positionEditPanel');
    	var selection = me.getSelectionModel().getSelection();//得到选中的记录
    	me.flag = me.id;//判断员工列表,variable='position'是岗位下的员工列表
    	me.posiId = positionEditPanel.orgtreeId;
    	Ext.MessageBox.show({
    		title : FHD.locale.get('fhd.common.delete'),
    		width : 260,
    		msg : FHD.locale.get('fhd.common.makeSureDelete'),
    		buttons : Ext.MessageBox.YESNO,
    		icon : Ext.MessageBox.QUESTION,
    		fn : function(btn) {
    			if (btn == 'yes') {//确认删除
    				if("positionempGridPanel" == me.flag){//岗位tabPanel的员工列表
    					var ids = [];
        				for(var i=0;i<selection.length;i++){
        					ids.push(selection[i].get('id'));
        				}
        				FHD.ajax({//ajax调用
        					url : me.empPosidelUrl,//删除岗位员工实体
        					params : {
        						ids:ids.join(','),
        						posiId:me.posiId	//当前岗位id
        					},
        					callback : function(data){
        						if(data){//删除成功！
        							Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
        							me.store.load();
        						}
        					}
        				});
    				}else{
    					var ids = [];
        				for(var i=0;i<selection.length;i++){
        					ids.push(selection[i].get('id'));
        				}
        				FHD.ajax({//ajax调用
        					url : me.empdelUrl,//删除员工实体
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
    		}
    	});
    }
});
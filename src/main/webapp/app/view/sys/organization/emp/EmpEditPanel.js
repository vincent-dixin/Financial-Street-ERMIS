/**
 * 人员基本信息FORM面板
 * 
 * @author 金鹏祥
 */
Ext.define('FHD.view.sys.organization.emp.EmpEditPanel', {
    extend: 'Ext.form.Panel',
    alias: 'widget.empEditPanel',
    
    /**
     * 加载form数据
     */
    formLoad : function(){
    	var me = this;
    	var empEditPanel = Ext.getCmp('empEditPanel');
    	
    	if(typeof(me.empgridId) != 'undefined') {
    		empEditPanel.form.load({
    	        url:'sys/organization/findempbyid.f',
    	        params:{id:me.empgridId},
    	        failure:function(form,action) {
    	            alert("err 155");
    	        },
    	        success:function(form,action){
    	        	var formValue = form.getValues();
    	        	//用户锁定状态
    	        	if(formValue.lockstate == '1'){
    	        		Ext.getCmp('lockId').items.items[0].checked = true;
    	        		Ext.getCmp('lockId').items.items[0].setValue(true);
    	        	}else{
    	        		Ext.getCmp('lockId').items.items[1].checked = true;
    	        		Ext.getCmp('lockId').items.items[1].setValue(true);
    	        	}
    	        	//是否启用
    	        	if(formValue.enable == '1'){
    	        		Ext.getCmp('enableId').items.items[0].checked = true;
    	        		Ext.getCmp('enableId').items.items[0].setValue(true);
    	        	}else{
    	        		Ext.getCmp('enableId').items.items[1].checked = true;
    	        		Ext.getCmp('enableId').items.items[1].setValue(true);
    	        	}
    				
    	        }
    	    });
    	}
    },
    /**
     * 初始化页面组件
     */
    initComponent: function () {
        var me = this;
        
        me.id = 'empEditPanel';
        var queryUrl = 'sys/st/findPlanById.f';//查询分页
        var empEdit_saveUrl = 'sys/organization/saveEmpInfo.f';//保存
        var querygenderUrl = 'sys/organization/findempgender.f';//性别下拉菜单
        var querypartyUrl = 'sys/organization/findempparty.f';//政治面貌下拉菜单
        var querydegreeUrl = 'sys/organization/findempdegree.f';//学位下拉菜单
        var querycardUrl = 'sys/organization/findempcardtype.f';//证件类型下拉菜单
        var querycompanyUrl = 'sys/organization/findemporganization.f';//所属机构下拉菜单
        var querydutyUrl = 'sys/organization/findempduty.f';//职务Url
        var querystatusUrl = 'sys/organization/findempstates.f';//状态下拉列表

        /***attribute start***/
        	var genderStore = Ext.create('Ext.data.Store', {//性别store
        	    fields: ['id', 'text'],
        	    proxy: {
        	         type: 'ajax',
        	         url: querygenderUrl,
        	         reader: {
        	             type: 'json',
        	             root: 'datas'
        	         }
        	     }, 
        	    autoLoad:true
        	});
        	
        	var partyStore = Ext.create('Ext.data.Store', {//政治面貌store
        	    fields: ['id', 'text'],
        	    proxy: {
        	         type: 'ajax',
        	         url: querypartyUrl,
        	         reader: {
        	             type: 'json',
        	             root: 'datas'
        	         }
        	     }, 
        	    autoLoad:true
        	});
        	
        	var degreeStore = Ext.create('Ext.data.Store', {//学位store
        	    fields: ['id', 'text'],
        	    proxy: {
        	         type: 'ajax',
        	         url: querydegreeUrl,
        	         reader: {
        	             type: 'json',
        	             root: 'datas'
        	         }
        	     }, 
        	    autoLoad:true
        	});
        	
        	var statusStore = Ext.create('Ext.data.Store', {//状态store
        	    fields: ['id', 'text'],
        	    proxy: {
        	         type: 'ajax',
        	         url: querystatusUrl,
        	         reader: {
        	             type: 'json',
        	             root: 'datas'
        	         }
        	     }, 
        	    autoLoad:true
        	});
        	
        	var cardStore = Ext.create('Ext.data.Store', {//证件类型store
        	    fields: ['id', 'text'],
        	    proxy: {
        	         type: 'ajax',
        	         url: querycardUrl,
        	         reader: {
        	             type: 'json',
        	             root: 'datas'
        	         }
        	     }, 
        	    autoLoad:true
        	});
        	
        	var companyStore = Ext.create('Ext.data.Store', {//所属机构store
        	    fields: ['id', 'text'],
        	    proxy: {
        	         type: 'ajax',
        	         url: querycompanyUrl,
        	         reader: {
        	             type: 'json',
        	             root: 'datas'
        	         }
        	     }, 
        	    autoLoad:true
        	});
        	
        	var dutyStore = Ext.create('Ext.data.Store', {//职务
        	    fields: ['id', 'text'],
        	    proxy: {
        	         type: 'ajax',
        	         url: querydutyUrl,
        	         reader: {
        	             type: 'json',
        	             root: 'datas'
        	         }
        	     }, 
        	    autoLoad:true
        	});
        	//员工编号
        	var empCode=Ext.create('Ext.form.TextField', {
        	    fieldLabel: FHD.locale.get('fhd.sys.orgstructure.emp.empcode')+'<font color=red>*</font>',
        	    allowBlank:false,//不允许为空
        	    margin: '7 30 3 30',
        	    name:'empcode',
        	    id:'empcode',
        	    columnWidth:.5
        	});
        	//用户名
        	var userName=Ext.create('Ext.form.TextField', {
        	    fieldLabel: FHD.locale.get('fhd.common.username')+'<font color=red>*</font>',
        	    allowBlank:false,//不允许为空
        	    margin: '7 30 3 30',
        	    name:'username',
        	    id:'username',
        	    columnWidth:.5
        	});
        	//员工姓名
        	var empName=Ext.create('Ext.form.TextField', {
        	    fieldLabel: FHD.locale.get('fhd.sys.orgstructure.emp.empname')+'<font color=red>*</font>',
        	    allowBlank:false,//不允许为空
        	    margin: '7 30 3 30',
        	    name:'empname',
        	    id:'empname',
        	    columnWidth:.5
        	});
        	//真实姓名
        	var realName=Ext.create('Ext.form.TextField', {
        	    fieldLabel: FHD.locale.get('fhd.sys.auth.role.realName')+'<font color=red>*</font>',
        	    allowBlank:false,//不允许为空
        	    margin: '7 30 3 30',
        	    name:'realname',
        	    id:'realnameId',
        	    columnWidth:.5
        	});
        	//性别
        	var gender = Ext.create('Ext.form.field.ComboBox', {
        		    store: genderStore,
        		    fieldLabel: FHD.locale.get('fhd.sys.orgstructure.emp.gender'),
        		    editable:false,
        		    emptyText:FHD.locale.get('fhd.common.pleaseSelect'),//默认为空时的提示 
        		    margin: '7 30 3 30',
        		    queryMode: 'local',
        		    name:'gender',
        		    displayField: 'text',
        		    valueField: 'id',
        		    triggerAction :'all',
        		    columnWidth:.5
        		});
        	//生日
        	var BirthDate=Ext.create('Ext.form.field.Date', {
        	    fieldLabel: FHD.locale.get('fhd.sys.orgstructure.emp.birthday'),
        	    //allowBlank:false,//不允许为空
        	    margin: '7 30 3 30',
        	    name:'birthDateStr',
        	    format:"Y-m-d",
        	    columnWidth:.5
        	});
        	//主责部门
        	var oSysOrgMain = Ext.create('FHD.ux.org.CommonSelector',{
            			 fieldLabel: '主部门'+'<font color=red>*</font>',
            			 allowBlank:false,//不允许为空
            			 id : 'mainorgId',
            		     type : 'dept',
            		     multiSelect : false,
            		     name:'orgStrMain',
            		     margin: '7 30 3 30',
            		     columnWidth:.5
            		  });
        	//辅助部门
        	var oSysOrgs = Ext.create('FHD.ux.org.CommonSelector',{
   			 	fieldLabel: '辅部门',
   			 	type : 'dept',
   			 	id : 'assistorgId',
   			 	multiSelect : true,
   			 	name:'orgStrs',
   			 	margin: '7 30 3 30',
   			 	columnWidth:.5
   		  });
        	//公司
//        	var oCompanyOrg=Ext.create('Ext.form.TextField', {
//        	    fieldLabel: '公司',
//        	    margin: '7 30 3 30',
//        	    name:'companyOrg',
//        	    id:'companyOrgId',
//        	    columnWidth:.5
//        	});
        	//职务
        	var empDuty = Ext.create('Ext.form.field.ComboBox', {
        	    store: dutyStore,
        	    fieldLabel: '职务',
        	    editable:false,
        	    emptyText:FHD.locale.get('fhd.common.pleaseSelect'),//默认为空时的提示 
        	    margin: '7 30 3 30',
        	    queryMode: 'local',
        	    name:'dutyStr',
        	    displayField: 'text',
        	    valueField: 'id',
        	    triggerAction :'all',
        	    columnWidth:.5
        	});
        	//证件类型
        	var cardType = Ext.create('Ext.form.field.ComboBox', {
        	    store: cardStore,
        	    fieldLabel: FHD.locale.get('fhd.sys.orgstructure.emp.cardtype'),
        	    editable:false,
        	    emptyText:FHD.locale.get('fhd.common.pleaseSelect'),//默认为空时的提示 
        	    margin: '7 30 3 30',
        	    queryMode: 'local',
        	    name:'cardtype',
        	    displayField: 'text',
        	    valueField: 'id',
        	    triggerAction :'all',
        	    columnWidth:.5
        	});
        	//证件号码
        	var cardNum = Ext.create('Ext.form.TextField', {
        		    fieldLabel: FHD.locale.get('fhd.sys.orgstructure.emp.cardno'),
        		    margin: '7 30 3 30',
        		    name:'cardno',
        		    id:'cardnoId',
        		    columnWidth:.5
        		});
        	//状态
        	 var empStatus = Ext.create('Ext.form.field.ComboBox', {
        		    store: statusStore,
        		    fieldLabel: FHD.locale.get('fhd.common.status'),
        		    //allowBlank:false,//不允许为空
        		    emptyText:FHD.locale.get('fhd.common.pleaseSelect'),//默认为空时的提示 
        		    editable:false,
        		    margin: '7 30 3 30',
        		    queryMode: 'local',
        		    name:'empStatus',
        		    displayField: 'text',
        		    valueField: 'id',
        		    triggerAction :'all',
        		    columnWidth:.5
        		});
        	//公司地址
        	/*var empAddress=Ext.create('Ext.form.TextField', {
        		    fieldLabel: FHD.locale.get('fhd.sys.orgstructure.emp.oaddress'),
        		    margin: '7 30 3 30',
        		    name:'oaddress',
        		    id:'oaddressId',
        		    columnWidth:.5
        		});*/
        	//公司电话
        	var oTel=Ext.create('Ext.form.TextField', {
        		    fieldLabel: FHD.locale.get('fhd.sys.orgstructure.emp.otel'),
        		    margin: '7 30 3 30',
        		    name:'otel',
        		    id:'otelId',
        		    columnWidth:.5
        		});
        	//公司邮编
        	var ozipCode=Ext.create('Ext.form.TextField', {
        		    fieldLabel: FHD.locale.get('fhd.sys.orgstructure.emp.ozipcode'),
        		    margin: '7 30 3 30',
        		    name:'ozipcode',
        		    id:'empOzipcodeId',
        		    columnWidth:.5
        		});
        	//公司邮箱
        	var oEmail=Ext.create('Ext.form.TextField', {
        		    fieldLabel: FHD.locale.get('fhd.sys.orgstructure.emp.oemail'),
        		    margin: '7 30 3 30',
        		    name:'oemail',
        		    id:'empOemail',
        		    columnWidth:.5
        		});
        	//传真号码
        	var faxNo=Ext.create('Ext.form.TextField', {
        		    fieldLabel: FHD.locale.get('fhd.sys.orgstructure.emp.faxno'),
        		    margin: '7 30 3 30',
        		    name:'faxno',
        		    id:'faxnoId',
        		    columnWidth:.5
        		});
        	//手机号
        	var mobikenNo=Ext.create('Ext.form.TextField', {
        		    fieldLabel: FHD.locale.get('fhd.sys.orgstructure.emp.mobikeno'),
        		    margin: '7 30 3 30',
        		    name:'mobikeno',
        		    id:'mobikenoId',
        		    columnWidth:.5
        		});
        	//MSN
        	var empMSN=Ext.create('Ext.form.TextField', {
        		    fieldLabel: 'MSN',
        		    margin: '7 30 3 30',
        		    name:'msn',
        		    id:'msn',
        		    columnWidth:.5
        		});
        	//家庭电话
        	var hTel=Ext.create('Ext.form.TextField', {
        		    fieldLabel: FHD.locale.get('fhd.sys.orgstructure.emp.htel'),
        		    margin: '7 30 3 30',
        		    name:'htel',
        		    id:'htelId',
        		    columnWidth:.5
        		});
        	//家庭地址
        	var hAddress=Ext.create('Ext.form.TextField', {
        		    fieldLabel: FHD.locale.get('fhd.sys.orgstructure.emp.haddress'),
        		    margin: '7 30 3 30',
        		    name:'haddress',
        		    id:'haddress',
        		    columnWidth:.5
        		});
        	//家庭邮编
        	var hzipCode=Ext.create('Ext.form.TextField', {
        		    fieldLabel: FHD.locale.get('fhd.sys.orgstructure.emp.hzipcode'),
        		    margin: '7 30 3 30',
        		    name:'hzipcode',
        		    id:'hzipcode',
        		    columnWidth:.5
        		});
        	//个人邮箱
        	var empEmail=Ext.create('Ext.form.TextField', {
        		    fieldLabel: FHD.locale.get('fhd.sys.orgstructure.emp.pemail'),
        		    margin: '7 30 3 30',
        		    name:'pemail',
        		    id:'pemail',
        		    columnWidth:.5
        		});
        	//政治面貌
        	var party = Ext.create('Ext.form.field.ComboBox', {
        			 store: partyStore,
        			 fieldLabel: FHD.locale.get('fhd.sys.orgstructure.emp.party'),
        			 emptyText:FHD.locale.get('fhd.common.pleaseSelect'),//默认为空时的提示  
        			 editable:true,
        		     margin: '7 30 3 30',
        		     queryMode: 'local',
        			 name:'party',
        			 displayField: 'text',
        			 valueField: 'id',
        			 triggerAction :'all',
        			 columnWidth:.5
        		});
        	//学位
        	var empDegree = Ext.create('Ext.form.field.ComboBox', {
        			 store: degreeStore,
        			 fieldLabel: FHD.locale.get('fhd.sys.orgstructure.emp.degree'),
        			 emptyText:FHD.locale.get('fhd.common.pleaseSelect'),//默认为空时的提示  
        			 editable:true,
        		     margin: '7 30 3 30',
        		     queryMode: 'local',
        			 name:'degree',
        			 displayField: 'text',
        			 valueField: 'id',
        			 triggerAction :'all',
        			 columnWidth:.5
        		});
        	//专业
        	var empMajor=Ext.create('Ext.form.TextField', {
        			 fieldLabel: FHD.locale.get('fhd.sys.orgstructure.emp.major'),
        			 margin: '7 30 3 30',
        			 name:'major',
        			 id:'major ',
        			 columnWidth:.5
        		});
        	//特长
        	var empSpecialty=Ext.create('Ext.form.TextField', {
        			 fieldLabel: FHD.locale.get('fhd.sys.orgstructure.emp.specialty'),
        			 margin: '7 30 3 30',
        			 name:'specialty',
        			 id:'specialty',
        			 columnWidth:.5
        		});
        	//注册时间
        	var regDate=Ext.create('Ext.form.field.Date', {
        	    fieldLabel: FHD.locale.get('fhd.common.regdate'),
        	    margin: '7 30 3 30',
        	    name:'regdateStr',
        	    format:"Y-m-d",
        	    columnWidth:.5
        	});
        	//备注
        	var dutyRemark=Ext.create('Ext.form.field.TextArea', {
        	    fieldLabel: FHD.locale.get('fhd.sys.duty.dutyremark'),
        	    margin: '7 30 3 30',
        	    name:'remark',
        	    id:'empRemarkId',
        	    columnWidth:.5
        	});
        	
        	//密码
        	var password=Ext.create('Ext.form.TextField', {
        	    fieldLabel: FHD.locale.get('fhd.sys.auth.user.password'),
        	    inputType:'password',
        	    margin: '7 30 3 30',
        	    name:'userPassword',
        	    id:'passwordId',
        	    columnWidth:.5
        	});
        	//密码过期日期
        	var credentialsexpiryDate=Ext.create('Ext.form.field.Date', {
        	    fieldLabel: "密码过期日期",
        	    margin: '7 30 3 30',
        	    name:'userCredentialsexpiryDateStr',
        	    format:"Y-m-d",
        	    columnWidth:.5
        	});
        	//最后登录时间
        	var lastLoginTime=Ext.create('Ext.form.field.Date', {
        	    fieldLabel: "最后登录时间",
        	    margin: '7 30 3 30',
        	    name:'userLastLoginTimeStr',
        	    format:"Y-m-d",
        	    columnWidth:.5
        	});
        	//用户状态
        	 var userStatus = Ext.create('Ext.form.field.ComboBox', {
        		    store: statusStore,
        		    fieldLabel: "用户状态",
        		    emptyText:FHD.locale.get('fhd.common.pleaseSelect'),//默认为空时的提示   
        		    editable:false,
        		    margin: '7 30 3 30',
        		    queryMode: 'local',
        		    name:'userState',
        		    displayField: 'text',
        		    valueField: 'id',
        		    triggerAction :'all',
        		    columnWidth:.5
        		});
        	//锁定状态
        	 var lockState = Ext.create('Ext.form.RadioGroup', {
        		 id : 'lockId',
        		 fieldLabel: "锁定状态",
        		 columnWidth:.5,
        		 margin: '7 30 3 30',
        	        vertical: true,
        	        items: [
        	            { boxLabel: "锁定", name: 'lockstate', inputValue: '1',checked:true,id:'lockstateYes'},
        	            { boxLabel: "未锁定", name: 'lockstate', inputValue: '0',id:'lockstateNo'}]
        	  });
        	
        	//是否启用
        	  var enable = Ext.create('Ext.form.RadioGroup', {
        		id : 'enableId',
        	    fieldLabel: "是否启用",
        	    columnWidth:.5,
        	    margin: '7 30 3 30',
        	        vertical: true,
        	        items: [
        	            { boxLabel: "是", name: 'enable', inputValue: '1',checked:true,id:'enableYes'},
        	            { boxLabel: "否", name: 'enable', inputValue: '0',id:'enableNo'}]
        	  });
        //保存
    	function save(){
    		var empEditPanel = Ext.getCmp('empEditPanel');
    		var form = empEditPanel.getForm();
    		var empGridPanel = Ext.getCmp('tabpanelempGridPanel');
    		var posiempGridPanel = Ext.getCmp('positionempGridPanel');
    		var cardPanel = Ext.getCmp('cardPanel');
    		var tabPanel = Ext.getCmp('tabPanel');
    		var rightPanel = Ext.getCmp('rightPanel');
    		var treepanel = Ext.getCmp('treePanel');
    		rightPanel.items.items[0].setHeight(20);
    		if(form.isValid()){
    			if("tabpanelempGridPanel"==tabPanel.getActiveTab().id){//tabPanel的员工列表
    				if(empGridPanel.isAdd){//新增保存
	    			FHD.submit({
	    				form:form,
	    				url:empEdit_saveUrl,
	    				callback:function(data){
	    					empGridPanel.store.load();
	    					cardPanel.getLayout().setActiveItem(tabPanel);
	    					if("gw" == treepanel.currentNode.data.type){
	    						treepanel.getSelectionModel().select(treepanel.getRootNode());//选中根节点
		    					treepanel.itemclickTree(treepanel.getRootNode());
	    					}
	    				}
	    			});	
	    		}else{
	    			FHD.submit({
	    				form:form,
	    				url:empEdit_saveUrl + '?id=' + me.empgridId,
	    				callback:function(data){
	    					empGridPanel.store.load();
	    					cardPanel.getLayout().setActiveItem(tabPanel);
	    					if("gw" == treepanel.currentNode.data.type){
	    						treepanel.getSelectionModel().select(treepanel.getRootNode());//选中根节点
		    					treepanel.itemclickTree(treepanel.getRootNode());
	    					}
	    				}
	    			});	
	    		}
    			}else{//岗位员工列表的添加
    				if(posiempGridPanel.isAdd){//新增保存
	    			FHD.submit({
	    				form:form,
	    				url:empEdit_saveUrl,
	    				callback:function(data){
	    					posiempGridPanel.store.load();
	    					cardPanel.getLayout().setActiveItem(tabPanel);
	    					if("gw" == treepanel.currentNode.data.type){
	    						treepanel.getSelectionModel().select(treepanel.getRootNode());//选中根节点
		    					treepanel.itemclickTree(treepanel.getRootNode());
	    					}
	    				}
	    			});	
	    		}else{
	    			FHD.submit({
	    				form:form,
	    				url:empEdit_saveUrl + '?id=' + me.empgridId,
	    				callback:function(data){
	    					posiempGridPanel.store.load();
	    					cardPanel.getLayout().setActiveItem(tabPanel);
	    					if("gw" == treepanel.currentNode.data.type){
	    						treepanel.getSelectionModel().select(treepanel.getRootNode());//选中根节点
		    					treepanel.itemclickTree(treepanel.getRootNode());
	    					}
	    				}
	    			});	
	    		}
    			}
    		}
    	}
    	//返回
    	function returnTab(){
    		var cardPanel = Ext.getCmp('cardPanel');
    		var tabPanel = Ext.getCmp('tabPanel');
    		var rightPanel = Ext.getCmp('rightPanel');
    		rightPanel.items.items[0].setHeight(20);
    		cardPanel.getLayout().setActiveItem(tabPanel);
    		
    	}
    	
    	var tbar =[//菜单项
    	           '->',
    	           {text : "返回",iconCls: 'icon-arrow-undo',id:'return1', handler:returnTab},
    	           {text : "保存",iconCls: 'icon-save',id:'empsave1${param._dc}', handler:save, scope : this}];
    	var bbar =[//菜单项
    	           '->',
    	           {text : "返回",iconCls: 'icon-arrow-undo',id:'return2', handler:returnTab},
    	           {text : "保存",iconCls: 'icon-save',id:'empsave2${param._dc}', handler:save, scope : this}];
    	
        Ext.applyIf(me, {
            autoScroll: true,
            border: false,
            layout: 'column',
            width: FHD.getCenterPanelWidth() - 258,
            bodyPadding: "5 5 5 5",
            tbar:tbar,
    		bbar:bbar,
            items: [{	
            	 xtype: 'fieldset',//基本信息fieldset
                 autoHeight: true,
                 autoWidth: true,
                 collapsible: true,
                 defaults: {
                     margin: '3 30 3 30',
                     labelWidth: 100
                 },
                 layout: {
                     type: 'column'
                 },
				title: '员工信息',
				items:[empCode, empName, oSysOrgMain, oSysOrgs, empDuty,oTel, empStatus,
				         ozipCode, oEmail
				]
			},
			{	
				 xtype: 'fieldset',//基本信息fieldset
	             autoHeight: true,
	             autoWidth: true,
	             collapsible: true,
	             defaults: {
	                    margin: '3 30 3 30',
	                    labelWidth: 100
	            },
	            layout: {
	                    type: 'column'
	            },
				title: '用户信息',
				items:[userName, realName, password, userStatus, lastLoginTime,
				        credentialsexpiryDate, lockState, enable
				]
			},
			{	
				 xtype: 'fieldset',//基本信息fieldset
	             autoHeight: true,
	             autoWidth: true,
	             collapsible: true,
	             defaults: {
	                    margin: '3 30 3 30',
	                    labelWidth: 100
	                },
	             layout: {
	                    type: 'column'
	                },
				title: '其他信息',
				items:[BirthDate, cardType, mobikenNo, empMSN, hAddress, empEmail, empDegree, empSpecialty,
				       gender, faxNo, cardNum, hTel, hzipCode, party, empMajor, regDate, dutyRemark
				]
			}]
            
        });
        
        me.callParent(arguments);
        
    },
    
    load:function(){
    	var me = this;
    	if(typeof(me.empgridId) != 'undefined'){
        	//编辑时加载form数据
            me.formLoad();
    	}
    }
});
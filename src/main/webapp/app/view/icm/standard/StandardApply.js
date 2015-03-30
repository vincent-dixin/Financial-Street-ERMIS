/**
 *    @description 录入内控标准FORM 第一步
 *    
 *    @author 元杰
 *    @since 2013-3-5
 */
Ext.define('FHD.view.icm.standard.StandardApply', {
	extend : 'Ext.form.Panel',
	alias : 'widget.standardapply',
    requires: [
    	'FHD.view.icm.standard.StandardControlEdit',
    	'FHD.ux.standard.StandardSelector'
    ],
	//title:'申请更新',
	items:[],
	layout : {
		type : 'column'
	},
	defaults : {
		columnWidth : 1/1
	},
	bodyPadding:'0 3 3 3',
	standardcontroledit : [],
	collapsed : false,
	autoScroll : true,
	//传递的参数对象
	paramObj:{
		standardControlIds : ''
	},
	/**
     * 关闭窗口 
     * 
     */
	closeWin: function(step){
    	var me = this;
    	var standardPanel = null;
    	if('1' == step){
    		standardPanel = me.up('standardbpmone');
    	}
    	if(standardPanel){
    		if(standardPanel.winId){
    			Ext.getCmp(standardPanel.winId).close();
    		}
    	}
    },
    save: function() {
		var me = this.up('standardapply');
		var standardApplyForm = me.getForm();
		if(!standardApplyForm.isValid()){
	        Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'存在未通过的验证!');
	        return ;
      	}
		var jsonArray = [];
		for(var i = 0;i<me.standardcontroledit.length;i++){
			//手动判断非空
			var inferiorValue = me.standardcontroledit[i].inferiorRadio.getValue().inferior;
			var dept = me.standardcontroledit[i].standardDepart.getValue();
			var company = me.standardcontroledit[i].standardSubCompany.getValue()
			if(inferiorValue=='0yn_n'){//否，不适用于下级机构
				if(dept == "" || null == dept || dept == undefined){
					Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'存在未通过的验证!');
					return;
				}
			}else if(inferiorValue=='0yn_y'){//是
				if(company == "" || null == company || company == undefined){
					Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'存在未通过的验证!');
					return;
				}
			}
			//将form放到jsonArray，传到后台
		  	jsonArray.push(me.standardcontroledit[i].getForm().getValues(false,false,true));
		}
		var jsonStr = Ext.encode(jsonArray);
		standardApplyForm.setValues({
			standardControlFormsStr : jsonStr
		}); 
		
		FHD.submit({
			form : standardApplyForm,
			url : __ctxPath + '/icm/standard/saveStandardData.f', //保存数据
			params : {
			   businessId : me.businessId,
			   executionId: me.executionId
			},
			callback: function (data) {
				var standardplancenterpanel = me.up('standardplancenterpanel');
				if(null != standardplancenterpanel){
					standardplancenterpanel.removeAll();
					standardplancenterpanel.add(Ext.widget('standardbpmlist'));
				}
				me.closeWin(me.step);
			}
		});
    },
	submit: function() {
		var me = this.up('standardapply');
		var standardApplyForm = me.getForm();
		if(!standardApplyForm.isValid()){
	        Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'存在未通过的验证!');
	        return ;
        }
		var jsonArray = [];
		for(var i = 0;i<me.standardcontroledit.length;i++){
			//手动判断非空
			var inferiorValue = me.standardcontroledit[i].inferiorRadio.getValue().inferior;
			var dept = me.standardcontroledit[i].standardDepart.getValue();
			var company = me.standardcontroledit[i].standardSubCompany.getValue()
			if(inferiorValue=='0yn_n'){//否，不适用于下级机构
				if(dept == "" || null == dept || dept == undefined){
					Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'存在未通过的验证!');
					return;
				}
			}else if(inferiorValue=='0yn_y'){//是
				if(company == "" || null == company || company == undefined){
					Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'存在未通过的验证!');
					return;
				}
			}
			//将form放到jsonArray，传到后台
		  	jsonArray.push(me.standardcontroledit[i].getForm().getValues(false,false,true));
		}
		var jsonStr = Ext.encode(jsonArray);
		standardApplyForm.setValues({
			standardControlFormsStr : jsonStr
		}); 
		
		FHD.submit({
			form : standardApplyForm,
			url : __ctxPath + '/icm/standard/saveStandard.f',
			params : {
			   businessId : me.businessId,
			   executionId: me.executionId
			},
			callback: function (data) {
				var standardplancenterpanel = me.up('standardplancenterpanel');
				if(null != standardplancenterpanel){
					standardplancenterpanel.removeAll();
					standardplancenterpanel.add(Ext.widget('standardbpmlist'));
				}
				me.closeWin(me.step);
			}
		});
    },
    loadData: function(businessId, executionId){
    	var me = this;
    	me.businessId = businessId;
    	me.executionId = executionId;
    	me.reloadData(businessId,executionId);
    },
    reloadData: function(businessId,executionId) {
       var me = this;
       me.load({
           waitMsg: FHD.locale.get('fhd.kpi.kpi.prompt.waiting'),
           url: __ctxPath + '/icm/standard/findStandardById.f',
           params: {
               standardId : businessId,
               executionId : executionId,
               step : me.step
           },
           success: function (form, action){
           		if(null != me.standardControlIds.getValue() && "" != me.standardControlIds.getValue()){
	           		 me.paramObj.standardControlIds = me.standardControlIds.getValue().split(',');
//	           		 me.standardcontroledit = new Array();
	             	 for(var i = 0;i<me.paramObj.standardControlIds.length;i++){
	                 	 me.standardControlEdit = Ext.widget('standardcontroledit',{});
	   	    			 me.standardcontroledit.push(me.standardControlEdit);
	   	    			 me.standardControlEdit.initParam({
	   	    			 	standardControlId : me.paramObj.standardControlIds[i]
	   	    			 });
				 		 me.standardcontroledit[i].reloadData();
				 		 me.add(me.standardcontroledit[i]);
				 		 //设置提交按钮可用
				 		 Ext.getCmp('icm_standard_submit').setDisabled(false);
	             	 }
	           		 return true;
           	 	}
           }
        });
    },
	initParam:function(paramObj){
		var me = this;
		me.paramObj = paramObj;
	},
	//添加要求
    addStandard:function(){
   	   var me = this;
   	   var standardControlEdit = Ext.widget('standardcontroledit',{});
   	   me.standardcontroledit.push(standardControlEdit);
   	   me.add(standardControlEdit);
   	   Ext.getCmp('icm_standard_submit').setDisabled(false);
	},
	initComponent :function() {
	 	var me = this;
	 	me.standardcontroledit = new Array();
	 	//标准ID，隐藏域
	 	me.standardId = {
	 		xtype : 'hidden',
	 		name : 'id',
	 		value : ''
	 	},
	 	// 要求表单 隐藏域
	    me.standardControlFormsStr = Ext.widget('textfield', {
    		name: 'standardControlFormsStr',
    		hidden : true
	    });
	    // 要求ID 隐藏域
	    me.standardControlIds = Ext.widget('textfield', {
    		name: 'standardControlIds',
    		hidden : true
	    });
	  	me.time ={
			xtype : 'textfield',
			labelWidth : 80,
			disabled : true,
			fieldLabel : '创建日期',
			name : 'time',
			value : '',
			margin: '7 10 10 30'
		};
		me.deptName ={
			xtype : 'textfield',
			labelWidth : 80,
			disabled : true,
			fieldLabel : '提交单位 ',
			value : '',
			name : 'deptName',
			margin: '7 10 10 30',
			allowBlank : false
		};
		//主责部门id
		me.deptId ={
			xtype : 'hidden',
			name : 'deptId'
		};
		/*标准名称 */
		me.standardName={
	         xtype: 'textareafield',
             margin: '7 10 10 30',
             labelWidth : 80,
             name:'name',
             rows: 5,
             maxLength : 200,
             allowBlank : false,
             fieldLabel:'标准名称'+ '<font color=red>*</font>'
             };
        //标准编号
        me.standardCode = {
			labelWidth :80,
			xtype : 'textfield',
			disabled : false,
			lblAlign:'rigth',
			fieldLabel : '编　　号' + '<font color=red>*</font>',
			value : '',
			name : 'code',
			margin: '7 10 10 30',
			maxLength : 200,
			columnWidth:.4,
			allowBlank : false
		};
		//标准编号自动生成按钮
		me.standardCreateCodeButton = {
            xtype: 'button',
            margin: '7 10 0 0',
            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.autoCode'),
            handler: function(){
       			FHD.ajax({
	            	url:__ctxPath+'/standard/standardTree/createStandardCode.f',
	            	params: {
	                	nodeId: me.nodeId
                 	},
	                callback: function (data) {
	                 	me.getForm().setValues({'code':data.code});//给code表单赋值
	                }
                });
            },
            columnWidth: .1
        };
	    /*所属分类  */
		me.parentStandard =Ext.widget('standardselector',{
			labelAlign:'right',
			name : 'parent',
			myType : 'standard',
			labelText:'分　　类 ',
			height : 25,
			labelWidth : 80,
			margin: '7 10 10 30'
		}),
		/*来源 */
		me.standardControlLevel=Ext.create('FHD.ux.dict.DictSelect',{
		     name:'controlLevel',
		     dictTypeId:'ic_control_level',
		     margin: '7 10 10 30',
		     labelWidth : 80,
		     labelAlign : 'left',
		     value:'',
		     multiSelect:false,
		     fieldLabel : '控制层级 '
		     });
		/*更新期限  */
		me.updateDeadline={
			 name: 'updateDeadline', 
			 xtype: 'datefield', 
			 format: 'Y-m-d',
//			 allowBlank : false,
             margin: '7 10 10 30',
             labelWidth : 80,
             fieldLabel:'更新期限'
             };
		             
		me.items= [{
			xtype : 'fieldset',
			defaults : {
				columnWidth : 1/2
			},//每行显示一列，可设置多列
			layout : {
				type : 'column'
			},
			collapsed : false,
//			margin: '0 3 3 3',
			collapsible : false,
			title : '基本信息 ',
			items:[me.standardId,me.standardControlFormsStr,me.standardControlIds,
				   me.time,me.deptName,me.deptId,me.standardName,me.standardCode, me.standardCreateCodeButton,
				   me.parentStandard,me.standardControlLevel,me.updateDeadline]
            }];
		me.bbar={
           style: 'background-image:url() !important;background-color:rgb(250,250,250);',
           items: [{
               xtype: 'tbtext'
              // text: '<font color="#3980F4"><b>' + FHD.locale.get('fhd.strategymap.strategymapmgr.form.basicinfo') + '</b>&nbsp;&rArr;&rArr;&nbsp;</font><font color="#cccccc"><b>' + FHD.locale.get('fhd.strategymap.strategymapmgr.form.kpiset') + '</b>&nbsp;&rArr;&rArr;&nbsp;</font><font color="#cccccc"><b>' + FHD.locale.get('fhd.strategymap.strategymapmgr.form.alarmset') + '</b></font>'
           }, '->',
           	{
                name: 'icm_defect_undo_btn' ,
	            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.undo'),//返回按钮
	            iconCls: 'icon-operator-home',
            	handler: function () {
           			var standardplancenterpanel = me.up('standardplancenterpanel');
					if(null != standardplancenterpanel){
						standardplancenterpanel.removeAll();
						standardplancenterpanel.add(Ext.widget('standardbpmlist'));
					}
					me.closeWin(me.step);
            	}
            },{
		    	iconCls : 'icon-add',
		    	name : 'icm_add_standard_control_btn',
		    	text:'新增要求',
		    	handler :me.addStandard,
		    	scope : this
		    },{
	            text: FHD.locale.get("fhd.common.save"),//保存按钮
	            name: 'icm_standard_save_btn' ,
	            iconCls: 'icon-control-stop-blue',
           		handler: me.save
      		 },{
      		 	id : 'icm_standard_submit',
	            text: FHD.locale.get("fhd.common.submit"),//提交按钮
	            name: 'icm_standard_submit_btn' ,
	            disabled : true,
	            iconCls: 'icon-operator-submit',
           		handler: me.submit
      		 }]
       };
		Ext.applyIf(me,{
			items:me.items
		});
		me.callParent(arguments);
		}
	});
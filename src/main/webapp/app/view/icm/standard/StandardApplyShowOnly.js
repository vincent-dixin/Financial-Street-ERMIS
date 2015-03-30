/**
 *    @description 录入内控标准FORM 第二步
 *    
 *    @author 元杰
 *    @since 2013-3-5
 */
Ext.define('FHD.view.icm.standard.StandardApplyShowOnly', {
	extend : 'Ext.form.Panel',
	alias : 'widget.standardapplyshowonly',
    requires: [
    	'FHD.view.icm.standard.StandardControlEdit',
    	'FHD.view.icm.standard.StandardControlShowOnly'	
    ],
	//title:'申请更新',
	items:[],
	step:'',//五步中的第几步
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
	initParam:function(paramObj){
		var me = this;
		me.paramObj = paramObj;
	},
	/**
     * 关闭窗口 
     * 
     */
    closeWin: function(step){
    	var me = this;
    	var standardPanel = null;
    	if('3' == step){
    		standardPanel = me.up('standardbpmthree');
    	}else if('4' == step){
    		standardPanel = me.up('standardbpmfour');
    	}else if('5' == step){
    		standardPanel = me.up('standardbpmfive');
    	}
    	if(standardPanel){
    		if(standardPanel.winId){
    			Ext.getCmp(standardPanel.winId).close();
    		}
    	}
    },
	save: function() {
		var me = this.up('standardapplyshowonly');
		var standardApplyForm = me.getForm();
		if(!standardApplyForm.isValid()){
	        Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'存在未通过的验证!');
	        return ;
        }
		var jsonArray = [];
		for(var i = 0;i<me.standardcontroledit.length;i++){
			if('1' == me.step){
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
			}
			//将form放到jsonArray，传到后台
		  	jsonArray.push(me.standardcontroledit[i].getForm().getValues(false,false,true));
		}
		var jsonStr = Ext.encode(jsonArray);
		standardApplyForm.setValues({
			standardControlFormsStr : jsonStr
		}); //editGridJson
		FHD.submit({
			form : standardApplyForm,
			url : __ctxPath + '/icm/standard/saveStandard.f',
			params : {
				step : me.step,
				businessId : me.businessId,
				executionId: me.executionId
			},
			callback: function (data) {
				me.closeWin(me.step);
			}
		});
    },
    loadData: function(businessId, executionId){
    	var me = this;
    	me.businessId = businessId;
    	me.executionId = executionId;
    	me.reloadData();
    },
    reloadData: function(businessId, executionId) {
       var me = this;
//       alert(me.businessId + '---');
       me.load({
           waitMsg: FHD.locale.get('fhd.kpi.kpi.prompt.waiting'),
           url: __ctxPath + '/icm/standard/findStandardById.f',
           params: {
//               standardId: me.standardId.value,
           	   standardId : me.businessId,
			   executionId: me.executionId,
			   step :　me.step
           },
           success: function (form, action){
           		if(null != me.standardControlIds.getValue() && "" != me.standardControlIds.getValue()){
//           			console.log(me.standardControlIds);
	           		 me.paramObj.standardControlIds = me.standardControlIds.getValue().split(',');
	           		 var standardcontroleditArray = new Array();
	             	 for(var i = 0;i<me.paramObj.standardControlIds.length;i++){
	             	 	 if('4' == me.step){
	             	 	 	me.standardControlEdit = Ext.widget('standardcontroledit',{step:me.step});
	             	 	 }else{
	             	 	 	me.standardControlEdit = Ext.widget('standardcontrolshowonly',{step:me.step});
	             	 	 }
	   	    			 standardcontroleditArray.push(me.standardControlEdit);
	   	    			 me.standardControlEdit.initParam({
	   	    			 	standardControlId : me.paramObj.standardControlIds[i]
	   	    			 });
				 		 me.standardControlEdit.reloadData();
				 		 me.add(me.standardControlEdit);
	             	 }
	             	 me.standardcontroledit = standardcontroleditArray;
	             	 me.add(me.standardAdvice);
	           		 return true;
           	 	}
           }
        });
    },
	initComponent :function() {
	 	var me = this;
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
			xtype : 'displayfield',
			labelWidth : 80,
			name : 'time',
			fieldLabel : '创建日期 ',
			value : '',
			margin: '7 10 10 30'
		};
		me.company ={
			xtype : 'displayfield',
			labelWidth : 80,
			fieldLabel : '提交单位 ',
			value : '',
			name : 'deptName',
			margin: '7 10 10 30'
		};
		//主责部门id
		me.deptId ={
			xtype : 'hidden',
			name : 'deptId'
		};
		/*标准名称 */
		me.standardName={
			 xtype : 'displayfield',
	         margin: '7 10 10 30',
	         labelWidth : 80,
	         name:'name',
	         maxLength : 200,
	         fieldLabel:'标准名称 '
         };
		/*分类 */
		me.parentStandard={
			 xtype : 'displayfield',
	         margin: '7 10 10 30',
	         labelWidth : 80,
	         name:'parentStandardName',
	         maxLength : 200,
	         fieldLabel:'分　　类'
         };
		/*适用范围  */
		me.standardDepart={
			xtype : 'displayfield',
         	fieldLabel : '适用范围 ',
         	name:'companyName',
         	margin: '7 10 10 30',
         	labelWidth : 80
         };		             
		/*来源 */
		me.standardControlLevel={
			 xtype : 'displayfield',
		     name:'controlLevelName',
		     margin: '7 10 10 30',
		     labelWidth : 80,
		     value:'',
		     fieldLabel : '控制层级 '
	     };
		/*更新期限  */
		me.updateTime={
			 xtype : 'displayfield',
			 name: 'updateDeadline', 
			 format: 'Y-m-d',
             margin: '7 10 10 30',
             labelWidth : 80,
             fieldLabel:'更新期限 '
        };
        
//   	    me.standardControlShowOnly = Ext.widget('standardcontrolshowonly',{step:me.step});
		
		me.approvalidea = Ext.widget('approvalidea',{executionId:me.executionId,bodyPadding: '0 3 3 3'}); 
        
   	   	//内控标准的审批意见fieldset
        me.standardAdvice = Ext.widget('fieldset',{
			defaults : {
				columnWidth : 1/1
			},//每行显示一列，可设置多列
			layout : {
				type : 'column'
			},
			name : 'advicefieldset',
			collapsed : false,
//			margin: '8 10 0 10',
			collapsible : true,
			title : '审批',
			items:[me.approvalidea]
        });
        
		me.items= [{
				xtype : 'fieldset',
				defaults : {
					columnWidth : 1/2
				},//每行显示一列，可设置多列
				layout : {
					type : 'column'
				},
				collapsed : false,
//				margin: '8 10 0 10',
				collapsible : true,
				title : '基本信息',
				items:[me.standardId, me.time,me.company,me.standardControlIds,me.deptId,
				       me.standardName,me.parentStandard,me.standardControlLevel,me.standardControlFormsStr,
					   /*me.standardDepart,*/me.updateTime]
            }, 
           		me.standardAdvice
            ];
		me.bbar={
	               style: 'background-image:url() !important;background-color:rgb(250,250,250);',
	               items: [{
	                   xtype: 'tbtext'
	                  // text: '<font color="#3980F4"><b>' + FHD.locale.get('fhd.strategymap.strategymapmgr.form.basicinfo') + '</b>&nbsp;&rArr;&rArr;&nbsp;</font><font color="#cccccc"><b>' + FHD.locale.get('fhd.strategymap.strategymapmgr.form.kpiset') + '</b>&nbsp;&rArr;&rArr;&nbsp;</font><font color="#cccccc"><b>' + FHD.locale.get('fhd.strategymap.strategymapmgr.form.alarmset') + '</b></font>'
	               }, '->',
	               	{
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
			            text: FHD.locale.get("fhd.common.submit"),//提交按钮
			            iconCls: 'icon-operator-submit',
	               		handler: me.save
              		 }]
	           };
		if('3' == me.step || '4' == me.step){
			me.standardAdvice.hide();
		}else if('2' == me.step || '5' == me.step){
			me.standardAdvice.show();
		}
	           
		Ext.applyIf(me,{
			items:me.items
		});
		me.callParent(arguments);
		}
	});
/**
 *    @description 录入内控标准FORM 第二步
 *    
 *    @author 元杰
 *    @since 2013-3-5
 */
Ext.define('FHD.view.icm.standard.form.StandardPreview', {
	extend : 'Ext.form.Panel',
	alias : 'widget.standardpreview',
    requires: [
    	'FHD.view.icm.standard.form.StandardControlPreview'
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
	             	 	 me.standardcontrolpreview = Ext.widget('standardcontrolpreview');
	   	    			 standardcontroleditArray.push(me.standardcontrolpreview);
	   	    			 me.standardcontrolpreview.initParam({
	   	    			 	standardControlId : me.paramObj.standardControlIds[i]
	   	    			 });
				 		 standardcontroleditArray[i].reloadData();
				 		 me.add(standardcontroleditArray[i]);
	             	 }
	             	 me.standardcontroledit = standardcontroleditArray;
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
			fieldLabel : '创建日期',
			value : '',
			margin: '7 10 10 30'
		};
		me.company ={
			xtype : 'displayfield',
			labelWidth : 80,
			fieldLabel : '提交单位',
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
	         fieldLabel:'标准名称'
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
		/*适用范围  
		me.standardDepart={
			xtype : 'displayfield',
         	fieldLabel : '适用范围',
         	name:'companyName',
         	margin: '7 10 10 30',
         	labelWidth : 80
         };		
         */             
		/*来源 */
		me.standardControlLevel={
			 xtype : 'displayfield',
		     name:'controlLevelName',
		     margin: '7 10 10 30',
		     labelWidth : 80,
		     value:'',
		     fieldLabel : '控制层级'
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
					   me.standardDepart,me.updateTime]
            }
            ];
	           
		Ext.applyIf(me,{
			items:me.items
		});
		me.callParent(arguments);
		}
	});
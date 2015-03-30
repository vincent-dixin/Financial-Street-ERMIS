/**
 * 流程基本信息编辑页面
 * 
 * @author 宋佳
 */
Ext.define('FHD.view.icm.icsystem.constructplan.form.DiagnosesDefectForm', {
	   extend: 'Ext.form.Panel',
	   alias: 'widget.diagnosesdefectform',
       requires: [
    	
       ],
       frame: false,
       border : false,
       paramObj : {
       	   measureId : ""
       },
//       autoHeight : true,
//       autoWidth : true,
//       autoScroll : false,
       initParam:function(paramObj){
         var me = this;
    	 me.paramObj = paramObj;
		},
       addComponent: function () {
	    	var me = this;
	    	//基本信息fieldset
	        me.basicinfofieldset = Ext.widget('fieldset', {
	            flex:1,
	            collapsible: false,
	            autoHeight: true,
	            autoWidth: true,
	            defaults: {
	                columnWidth : 1 / 2,
	                margin: '7 30 3 30',
	                labelWidth: 95
	            },
	            layout: {
	                type: 'column'
	            },
	            title: '缺陷'
	        });
	        // 建设计划id隐藏域
	       me.diagnosesDefectId = Ext.widget('textfield', {name: 'diagnosesDefectId',hidden : true });
	        
			// 标准名称
			me.standardName = Ext.widget('displayfield', {
	            name : 'standardName',
	            fieldLabel : '标准名称',
	            value: '',
	            columnWidth: .5
	        });
	        me.basicinfofieldset.add(me.diagnosesDefectId,me.standardName);
			// 责任部门
			me.standardRelaOrg = Ext.widget('displayfield', {
	            name : 'standardRelaOrg',
	            fieldLabel : '责任部门',
	            value: '',
	            columnWidth: .5
	        });
	        me.basicinfofieldset.add(me.standardRelaOrg);
			// 内控要求
			me.controlRequirement = Ext.widget('textareafield', {
	            name : 'controlRequirement',
	            fieldLabel : '内控要求',
	            readOnly : true,
	            value: '',
	            columnWidth: .5
	        });
	        me.basicinfofieldset.add(me.controlRequirement);
			// 内控要求
			me.diagnosis = Ext.widget('displayfield', {
	            name : 'diagnosis',
	            fieldLabel : '诊断结果',
	            value: '',
	            columnWidth: .5
	        });
	        me.basicinfofieldset.add(me.diagnosis);
			// 内控要求
			me.proof = Ext.widget('displayfield', {
	            name : 'proof',
	            fieldLabel : '实施证据',
	            value: '',
	            columnWidth: .5
	        });
	        me.basicinfofieldset.add(me.proof);
			// 内控要求
			me.description = Ext.widget('displayfield', {
	            name : 'description',
	            fieldLabel : '缺陷描述',
	            value: '',
	            columnWidth: .5
	        });
	        me.basicinfofieldset.add(me.description);
			// 内控要求
			me.controldesc = Ext.widget('displayfield', {
	            name : 'controldesc',
	            fieldLabel : '控制描述',
	            value: '',
	            columnWidth: .5
	        });
	        me.basicinfofieldset.add(me.controldesc);
	        me.feedbackoptions = Ext.widget('textfield', {
	            name : 'feedbackoptions',
	            fieldLabel : '反馈意见' + '<font color=red>*</font>' ,
	            columnWidth: .5
	        });
	        
			/* 是否认同 */
			me.isAgree = Ext.create('FHD.ux.dict.DictRadio',
				{
					columnWidth : .5,
					lableWidth:100,labelAlign:'left',
					fieldLabel : '是否同意'+ '<font color=red>*</font>',
					dictTypeId:'0yn',
					name : 'isAgree',
					listeners: {
						change: function(t,newValue, oldValue,op){
							//var me = this.up('fieldset');
							if(newValue.isAgree=='0yn_n'){//同意审批
								me.feedbackoptions.show();
							}
							if(newValue.isAgree == '0yn_y'){
								me.feedbackoptions.hide();
								me.feedbackoptions.setValue("");    //
							}
						}
				}
			});
			me.basicinfofieldset.add(me.isAgree);
	        //控制描述
			
	        me.basicinfofieldset.add(me.feedbackoptions);
	        me.add(me.basicinfofieldset);
	    },
	    // 初始化方法
       initComponent: function() {
           var me = this;
           Ext.applyIf(me);
           me.callParent(arguments);
           //向form表单中添加控件
		   me.addComponent();
       },
	   reloadData: function() {
	       var me = this;
	       me.load({
	           waitMsg: FHD.locale.get('fhd.kpi.kpi.prompt.waiting'),
	           url: __ctxPath + '/icm/icsystem/loaddiagnosesdefectformdata.f',
	           params: {
	               defectId : me.paramObj.defectId
	           },
	           success: function (form, action) {
	               return true;
	           }
	        });
	        
	    }
});
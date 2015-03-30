/**
 *    @description 内控标准FORM显示用 
 *    
 *    @author 元杰
 *    @since 2013-3-5
 */
Ext.define('FHD.view.icm.standard.StandardControlShowOnly', {
	extend : 'Ext.form.Panel',
	alias : 'widget.standardcontrolshowonly',
	items:[],
	frame: false,
    border : false,
    step:'',//五步中的第几步
	layout : {
		type : 'column'
	},
	defaults : {
		columnWidth : 1/1
	},
	bodyPadding:'0 3 3 3',
	autoScroll : true,
	collapsed : false,
	//传递的参数对象
	paramObj:{
		standardControlId : ''
	},
	initParam:function(paramObj){
		var me = this;
		me.paramObj = paramObj;
	},
	reloadData: function() {
       var me = this;
       me.load({
           waitMsg: FHD.locale.get('fhd.kpi.kpi.prompt.waiting'),
           url: __ctxPath + '/icm/standard/findstandardControlById.f',
           params: {
               standardControlId: me.paramObj.standardControlId
           },
           success: function (form, action) {
               	return true;
           }
        });
    },
	initComponent :function() {
	  var me = this;
	  
	  //ID
	  me.sandardId = {
			xtype : 'hidden',
			name : 'cid'
	  };
	  //内部控制要求
	  me.standardCode = {
			labelWidth :80,
			xtype : 'displayfield',
			lblAlign:'rigth',
			fieldLabel : '内部控制要求',
			value : '',
			columnWidth : 1,
			name : 'cname',
			margin: '7 10 10 30',
			maxLength : 200,
			allowBlank : false
		};
       
		/*是-适用范围  */
		me.standardSubCompany= {
			xtype : 'displayfield',
         	fieldLabel : '适用范围',
         	name:'csubCompanyName',
         	margin: '7 10 10 30',
         	type : 'dept',
         	labelWidth : 80,
            multiSelect : false
         };
		
        /*否-责任部门  */
		me.standardDepart={
			xtype : 'displayfield',
         	fieldLabel : '责任部门',
         	name:'cdeptName',
         	margin: '7 10 10 30',
         	type : 'dept',
         	labelWidth : 80,
            multiSelect : false
         };
         //是否适用于下级单位Radio 此处只用来保存反馈意见
		me.inferiorRadio = {
			xtype:'hidden',
			value :'adviceOnly',
			name : 'inferior'
		};
       	/*对应流程  */
		me.standardRelaProcess= {
			xtype : 'displayfield',
         	fieldLabel : '对应流程',
         	name:'cprocessName',
         	margin: '7 10 10 30',
         	type : 'dept',
         	labelWidth : 80,
            multiSelect : false
         };
		
        /*内控要素 */
		me.standardControlPoint={
			xtype : 'displayfield',
		     name:'cstandardControlPoint',
		     margin: '7 10 10 30',
		     labelWidth : 80,
		     multiSelect:false,
		     fieldLabel : '内控要素'
	     };
	     
	     //处理状态-第5步显示
		 me.standardStatus=Ext.widget('displayfield',{
		     name:'statusId',
		     margin: '7 10 10 30',
		     labelWidth : 80,
		     columnWidth:.5,
		     fieldLabel : '处理状态',
		     hidden:true,
		     renderer : function(value, metaData, record, colIndex, store, view) { 
				if('N' == value){
					return '未开始';
				}else if('H' == value){
					return '处理中';
				}else if('ic_control_standard_estatus_1' == value){
					return '待更新';
				}else if('ic_control_standard_estatus_2' == value){
					return '体系建设计划阶段';
				}else if('ic_control_standard_estatus_3' == value){
					return '体系更新阶段';
				}else if('ic_control_standard_estatus_4' == value){
					return '缺陷整改阶段';
				}else if('ic_control_standard_estatus_5' == value){
					return '已纳入内控手册运转';
				}
				return value;
			}
	     });
	     
		/* 反馈意见*/
		me.feedback=Ext.widget('textarea',{
		     name:'cfeedback',
		     margin: '7 10 10 30',
		     labelWidth : 80,
		     multiSelect:false,
		     hidden:true,
		     columnWidth: 1,
		     fieldLabel : '反馈意见'
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
//			margin: '8 10 0 10',
			collapsible : true,
			title : '要求',
			items:[me.sandardId, me.standardCode, me.standardSubCompany,me.inferiorRadio,
					me.standardDepart,me.standardRelaProcess,
					me.standardControlPoint,me.standardStatus, me.feedback]
            }];
		
		if('2' == me.step){
			me.feedback.hide();
		}else if('5' == me.step){
			me.feedback.hide();
			me.standardStatus.show();
		}else if('3' == me.step){
			me.feedback.show();
			me.feedback.enable();
		}
            
		Ext.applyIf(me,{
			items:me.items
		});
		me.callParent(arguments);
		}
	});
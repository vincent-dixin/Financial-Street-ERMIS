/**
 *    @description 录入内控标准FORM 
 *    
 *    @author 元杰
 *    @since 2013-3-5
 */
Ext.define('FHD.view.icm.standard.StandardControlEdit', {
	extend : 'Ext.form.Panel',
	alias : 'widget.standardcontroledit',
	items:[],
	frame: false,
    border : false,
	requires: [
    	'FHD.view.process.ProcessMainPanel'	
    ],
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
           		var values = Ext.getCmp('cstandardControlPointHidden').getValue().split(',');
           		me.standardControlPoint.setValue(values[0]);
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
	  me.standardName = {
			labelWidth :80,
			xtype : 'textarea',
			disabled : false,
			lblAlign:'rigth',
			fieldLabel : '内控要求' + '<font color=red>*</font>',
			value : '',
			name : 'cname',
			margin: '7 10 10 30',
			maxLength : 200,
			columnWidth:1,
			allowBlank : false
		};
		/*是-适用范围  */
		me.standardSubCompany= Ext.create('FHD.ux.org.CommonSelector',{
         	fieldLabel : '适用范围' + '<font color=red>*</font>',
         	companyOnly : true,
         	rootVisible : false,
         	multiSelect : true,
         	height : 60,
         	type : 'dept',
         	name:'csubCompanyId',
         	margin: '7 10 10 30',
         	labelWidth : 80,
         	hidden : true,
         	columnWidth:.5
         });
       
        /*否-责任部门  */
		me.standardDepart=Ext.create('FHD.ux.org.CommonSelector',{
         	fieldLabel : '责任部门' + '<font color=red>*</font>',
         	name:'cdeptId',
         	margin: '7 10 10 30',
         	type : 'dept',
         	labelWidth : 80,
         	hidden : false,
         	columnWidth:.5,
            multiSelect : false
         });
             
       	/*否-对应流程  */
		me.standardRelaProcess = Ext.create('FHD.ux.process.processSelector',{
		    name:'cprocessId',
		    labelWidth : 80,
		    value:'',
		    columnWidth:.4,
		   	margin: '0 0 0 0',
         	height : 25,
		    fieldLabel:'选择流程 ',
         	hidden : false,
            multiSelect : false
         });
         
        //编辑流程按钮
		me.editProcessureButton = {
	        xtype:'button',
	        width: 30,
	        name : 'editProcessureButton',
	        text:'流程编辑 ',
	        columnWidth:.1,
	        height: 22,
	        handler:function(){
	        	me.processMainPanel = Ext.widget('processmainpanel', {});
				me.win = Ext.create('FHD.ux.Window', {
					title : '流程编辑',
					closable : true,
					maximizable: true,
					items : [me.processMainPanel]  //ITEMS里面是弹出窗体所包含的PANEL
				}).show();
//				me.win.setVisible(true); //设置可见
	        }
		};
		
        /*否-内控要素 */
		me.standardControlPoint=Ext.create('FHD.ux.dict.DictSelect',{
		     name:'cstandardControlPoint',
		     dictTypeId:'ic_control_point',
		     margin: '7 10 10 30',
		     labelWidth : 80,
		     labelAlign:'left',
		     multiSelect:false,
         	 hidden : false,
         	 columnWidth:.5,
         	 value:'',
		     fieldLabel : '内控要素 '
	     });
	     //处理状态-第4步显示
		 me.standardStatus=Ext.create('FHD.ux.dict.DictSelectForEditGrid',{
		     name:'statusId',
		     dictTypeId:'ic_control_standard_estatus',
		     margin: '7 10 10 30',
		     labelWidth : 80,
		     columnWidth:.5,
		     labelAlign:'left',
		     value:'',
		     varlue:'请选择',
		     multiSelect:false,
		     fieldLabel : '处理状态'
	     });
	     /*内控要素 隐藏域*/
	     me.cstandardControlPointHidden={
	     	xtype:'hidden',
	     	id:'cstandardControlPointHidden'
	     }
	     //是否适用于下级单位Radio
		me.inferiorRadio = Ext.create('FHD.ux.dict.DictRadio', {
		    margin: '7 10 10 30',
			labelWidth:80,
			labelAlign:'left',
			fieldLabel:'适用下级单位 ',
			dictTypeId:'0yn',
			columnWidth:.5,
			defaultValue :'0yn_n',
			name : 'inferior'
		});
		
		//内部要求反馈意见
	 	me.standardControlAdvice = {
			labelWidth :80,
			xtype : 'displayfield',
			labelAlign:'left',
			fieldLabel : '反馈意见 ',
			name : 'cstandardControlAdvice',
			columnWidth:1
		};
		
   	    //内控标准的审批意见fieldset
        me.standardControlAdviceField = Ext.widget('fieldset',{
			defaults : {
				columnWidth : 1/2
			},//每行显示一列，可设置多列
			layout : {
				type : 'column'
			},
			name : 'advicefieldset',
			border: 0,
			margin: '7 10 10 20',
			items:[me.standardControlAdvice]
            });
		
		me.processFieldset = {
			xtype : 'fieldset',
			defaults : {
				columnWidth : 1/2
			},//每行显示一列，可设置多列
			layout : {
				type : 'column'
			},
			margin: '7 10 10 20',
			border : 0,
			items:[me.standardRelaProcess,me.editProcessureButton]
        };
         
		me.items= [{
			xtype : 'fieldset',
			defaults : {
				columnWidth : 1/1
			},//每行显示一列，可设置多列
			layout : {
				type : 'column'
			},
			collapsed : false,
//			margin: '8 10 0 10',
			collapsible : false,
			title : '要求',
			items:[me.sandardId,me.standardName,me.cstandardControlPointHidden,
					me.inferiorRadio,me.standardSubCompany,me.standardDepart,me.standardControlPoint,
					me.processFieldset, me.standardStatus, me.standardControlAdviceField
			]
            }];
		
		if('4' == me.step){
			me.standardControlAdviceField.show();
			me.inferiorRadio.hide();
			me.standardStatus.show();//标准提交前最后一个可编辑步骤，第4步，显示
		}else{
			me.standardControlAdviceField.hide();
			me.inferiorRadio.show();
			me.standardStatus.hide();
		}
		
		Ext.applyIf(me,{
			items:me.items
		});
		me.callParent(arguments);
		me.inferiorRadio.on('change',function(t,newValue, oldValue,op){
			//console.log(this.up('fieldset').down('[name=controlPoint]'));
			var me = this.up('fieldset');
			if(newValue.inferior=='0yn_n'){//否
				me.down('[name=csubCompanyId]').hide();
//				me.down('[name=csubCompanyId]').allowBlank = true;
				me.down('[name=cdeptId]').show();
//				me.down('[name=cdeptId]').allowBlank = false;
				me.down('[name=cprocessId]').show();
				me.down('[name=cstandardControlPoint]').show();
				me.down('[name=editProcessureButton]').show();
			}else if(newValue.inferior=='0yn_y'){//是
				me.down('[name=csubCompanyId]').show();
//				me.down('[name=csubCompanyId]').allowBlank = false;
				me.down('[name=cdeptId]').hide();
//				me.down('[name=cdeptId]').allowBlank = true;
				me.down('[name=cprocessId]').hide();
				me.down('[name=cstandardControlPoint]').hide();
				me.down('[name=editProcessureButton]').hide();
			}
		});
	}
});
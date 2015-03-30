/*整改计划添加编辑页面 */
Ext.define('FHD.view.icm.rectify.form.RectifyImproveForm', {
	extend : 'Ext.form.Panel',
	alias: 'widget.rectifyimproveform',
	layout : {
		type : 'column'
	},
	defaults:{
		columnWidth: 1
	},
	bodyPadding: '0 3 3 3',
	autoWidth:true,
	collapsed : false,
	autoScroll:true,
	initComponent :function() {
		var me = this;
		Ext.applyIf(me,{
			items:[{
				xtype : 'fieldset',
				defaults : {
					margin: '7 10 3 30',
					labelWidth: 95,
					labelAlign: 'left',
					columnWidth: .5
				},
				layout : {
					type : 'column'
				},
				flex: 1,
				collapsed : false,
				collapsible : false,
				title : '基本信息',
				items:[{name : 'id', xtype : 'textfield', hidden: true},
					{name:"code", xtype:"textfield",fieldLabel:"编&nbsp;&nbsp;&nbsp;&nbsp;号"+'<font color=red>*</font>', allowBlank:false},
			    	{name : 'name', xtype : 'textfield', fieldLabel : '名&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称' + '<font color=red>*</font>', allowBlank : false},
				    Ext.create('Ext.container.Container',{
	             	    layout:{
	             		 type:'hbox'  
	             	    },
	             	    items:[{xtype:'displayfield', width:95, value:'计划日期:'},
							{name: 'planStartDate', xtype: 'datefield', format: 'Y-m-d'},
							{xtype:'displayfield', value:'至'},
							{name: 'planEndDate', xtype: 'datefield', format: 'Y-m-d'}
						]
			         }),
			         Ext.create('FHD.ux.dict.DictCheckbox',{
						name:'improvementSource',
						dictTypeId:'ref_improvement_source',
						labelAlign:'left',
						hidden:true,
						allowBlank:true,
						columns:3,
						fieldLabel : '改进原因'+ '<font color=red>*</font>'
					})
				]
	        },{
				xtype : 'fieldset',
				layout : {
					type : 'column'
				},
				defaults : {
					columnWidth : 1
				},//每行显示一列，可设置多列
				collapsed : false,
				collapsible : false,
				title : '改进目标',
				items:[
					{xtype : 'textareafield', name : 'improvementTarget', hideLabel: true, rows : 3}
				]
	        },{
				xtype : 'fieldset',
				layout : {
					type : 'column'
				},
				defaults : {
					columnWidth : 1
				},//每行显示一列，可设置多列
				collapsed : false,
				collapsible : false,
				title : '具体原因',
				items:[
					{xtype : 'textareafield', name : 'reasonDetail',  hideLabel: true, rows : 3}
				]
	        }]
		});
		me.callParent(arguments);
	},
	validateDate:function(date1,date2){//验证时间
	    var sd1=date1.split("-");
	    var sd2=date2.split("-");
	    var oldDate=new Date(sd1[0],sd1[1],sd1[2]);
	    var newDate=new Date(sd2[0],sd2[1],sd2[2]);
		if(oldDate<=newDate){
			return true;
		}else{
			return false;
		}
	},
	saveFunc:function(cardpanel){
		var me=this;
		var formValues = me.getForm().getValues();
        if(!me.getForm().isValid()){
	        Ext.Msg.alert(FHD.locale.get('fhd.common.prompt'),'存在未通过的验证!');
	        return ;
        }
		if(formValues.planStartDate!=''&&formValues.planEndDate!=''){
			if(!me.validateDate(formValues.planStartDate,formValues.planEndDate)){
				Ext.Msg.alert(FHD.locale.get('fhd.common.prompt'),'计划开始时间不能大于结束时间!');
             	return ;
			}
		}
		FHD.submit({
			form: me.getForm(),
			async: false,
			url: __ctxPath+'/icm/improve/mergeImprove.f',
			callback: function (data){
				if(data && data.success){
					if(cardpanel){//保存后重新加载数据
						cardpanel.rectifydefectselectform.loadData(data.id);
						cardpanel.improveId = data.id;
					}
					me.improveId = data.id;
					me.reloadData();
				}
			}
		});
		return true;
    },
    loadData: function(improveId,executionId){
    	var me = this;
    	me.improveId = improveId;
    	me.executionId = executionId;
    	me.reloadData();
    },
	reloadData:function(){
		var me = this;
		if(me.improveId && me.improveId !=''){
    		me.getForm().load({
				url:__ctxPath + '/icm/improve/findImproveAdviceForForm.f?improveId='+me.improveId,
				success: function (form, action) {
					return true;
				},
				failure: function (form, action) {
					return false;
				}
			});
    	}else{
    		me.getForm().reset();
    		//创建编号
			FHD.ajax({
				url : __ctxPath+ '/rectify/rectify/createRectifyCode.f',
				callback : function(data) {
					me.getForm().setValues({
						'code' : data.code
					});//给code表单赋值
				}
			});
    	}
    	if(me.executionId){
    		if(me.approvalIdeaGrid){
    			me.remove(me.approvalIdeaGrid);
    		}
			var approvalIdeaGrid = Ext.create('Ext.form.FieldSet',{
				layout : {
					type : 'column'
				},
				collapsed : true,
				collapsible : true,
				columnWidth:1/1,
				collapsible : true,
				title : '审批意见列表',
				items : [Ext.create('FHD.view.comm.bpm.ApprovalIdeaGrid',{
					executionId: me.executionId,
					title:'审批意见历史列表',
					margin:'5 3 5 3',
					height:200,
					columnWidth:1
				})]
			});
			me.approvalIdeaGrid = approvalIdeaGrid;
			me.add(me.approvalIdeaGrid);
		}
	}
});


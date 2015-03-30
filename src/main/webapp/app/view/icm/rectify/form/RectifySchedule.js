/*整改进度表单*/
Ext.define('FHD.view.icm.rectify.form.RectifySchedule', {
	extend : 'Ext.form.Panel',
	alias: 'widget.rectifyschedule',
	layout : {
		type : 'column'
	},
	defaults : {
		columnWidth : 1/1
	},
	requires: [
       'FHD.view.icm.rectify.form.ImprovePlanViewForm'
    ],
	bodyPadding: '0 3 3 3',
    autoWidth:true,
	collapsed : false,
	autoScroll:true,
	border: false,
	initComponent :function() {
		var me = this;
		me.improveplanviewform = Ext.widget('improveplanviewform',{fieldsetcollapsed: false});
		Ext.applyIf(me,{
			items:[me.improveplanviewform,{
				xtype : 'fieldset',
				defaults : {
					margin:'7 10 10 0'
				},//每行显示一列，可设置多列
				layout : {
					type : 'column'
				},
				collapsed : false,
				collapsible : false,
				title : '整改情况 <font color=red>*</font>',
				items:[
					{xtype: 'numberfield', fieldLabel: '进度&nbsp;(%)<font color=red>*</font>', columnWidth : 0.2, allowBlank:false, name : 'finishRate', maxValue: 100, minValue: 0},
					{xtype: 'textareafield', hideLabel:true, fieldLabel: '整改情况', allowBlank:false, name : 'improveResult',columnWidth : 1}
				]
			}]
		});
		me.callParent(arguments);
	},
	rectifyScheduleSave:function () {
		var me=this;
		if(me.getForm().isValid()){
			FHD.submit({
				form: me.getForm(),
				url: __ctxPath+'/icm/rectify/saveRectifySchedule.f',
				callback: function (data){ 
				}
			});
		}
		
	},
	loadData: function(improveId,executionId){
		var me = this;
		me.improveId = improveId;
		me.executionId = executionId;
		me.reloadData();
	},
	reloadData:function(){
		var me=this;
		if(me.improveId){
			me.getForm().load({
				url:__ctxPath + '/icm/improve/findimproveplanFormbyimproveid.f?improveId='+me.improveId+'&executionId='+me.executionId,
				success: function (form, action) {
					return true;
				},
				failure: function (form, action) {
					return false;
				}
			});
		}
	}
});


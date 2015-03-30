Ext.define('FHD.view.bpm.processinstance.JbpmHistProcinstForm', {
    extend: 'Ext.form.Panel',
	alias: 'widget.JbpmHistProcinstForm',
	
	border:false,
    model:"show",
    jbpmHistProcinstId:"",
	layout : {
		type : 'column'
	},
	reloadData:function(){
		var me=this;
		me.getForm().load({
            url: __ctxPath+'/jbpm/processinstance/jbpmHistProcinstForm.f',
            params: {
				jbpmHistProcinstId : me.jbpmHistProcinstId
	        },
            success: function (form, action) {
				return true;
            },
            failure: function (form, action) {
				return false;
            }
		});
	},
    
    initComponent: function() {
        var me = this;
        var businessNameDisplay=Ext.create('Ext.form.field.Display',{
			fieldLabel:'流程名称',
			value : '',
			name : 'businessName'
		});
        var endactivityDisplay=Ext.create('Ext.form.field.Display',{
			fieldLabel:'流程状态',
			value : '',
			name : 'endactivity'
		});
        var realnameDisplay=Ext.create('Ext.form.field.Display',{
			fieldLabel:'流程发起者',
			value : '',
			name : 'realname'
		});
        var orgnamesDisplay=Ext.create('Ext.form.field.Display',{
			fieldLabel:'发起者部门',
			value : '',
			name : 'orgnames'
		});
        var createTimeDisplay=Ext.create('Ext.form.field.Display',{
			fieldLabel:'发起时间',
			value : '',
			name : 'createTime'
		});
        var updateTimeDisplay=Ext.create('Ext.form.field.Display',{
			fieldLabel:'更新时间',
			value : '',
			name : 'updateTime'
		});
        Ext.applyIf(me, {
    		defaults: {
    			labelWidth : 80,
        		lblAlign : 'right',
        		maxLength : 200,
    			columnWidth: 1/2,
    			margin:'5 50 5 50'
    		},
            items: [
            	businessNameDisplay,
            	endactivityDisplay,
            	realnameDisplay,
            	orgnamesDisplay,
            	createTimeDisplay,
            	updateTimeDisplay
            ]
        });
        me.callParent(arguments);
        me.reloadData();
    }

});
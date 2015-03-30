/**
 * 流程转办弹出面板
 * 
 * @author 邓广义
 */
Ext.define('FHD.view.icm.assess.AssessPlanDetailsTurnToDealwith', {
    extend: 'Ext.form.Panel',
    alias: 'widget.assessplandetailsturntodealwith',
    /**
     * 初始化页面组件
     */
    initComponent: function () {
    	var me = this;
        me.queryUrl = __ctxPath + '/jbpm/processInstance/processInstanceInfoPersonSel.f';//转办
        me.savePersionUrl = __ctxPath  +'/jbpm/processInstance/savePerson.f' //保存承办人
        me.tbar =[//按钮
    	           '->',
    	           {text : "保存",iconCls: 'icon-save', handler:me.save, scope : this}];
    	me.undertaker = Ext.create('FHD.ux.org.CommonSelector', {
							fieldLabel : '承办人'+ '<font color=red>*</font>',
							labelAlign : 'left',
							name:'empId',
							type : 'emp',
							multiSelect : false,
							allowBlank:false
							});
        Ext.applyIf(me, {
            autoScroll: true,
            border: false,
            bodyPadding: "5 5 5 5",
            autoScroll:true,
            url:me.queryUrl,
            tbar:me.tbar,
            items: [{
                xtype: 'fieldset',//基本信息fieldset
                collapsible: false,
                title: "基本信息",
                defaults: {
                    columnWidth : 1 / 2,
                    margin: '7 30 3 30',
                    labelWidth: 95
                },
                layout: {
                    type: 'column'
                },
                items:[
                       	{xtype:'displayfield', fieldLabel:'流程实例ID', name:'processInstanceId',hidden:true},
                        {xtype:'displayfield', fieldLabel:'任务名称', name:'activityName'},
						{xtype:'displayfield', fieldLabel:'状态', name : 'dbversion'},
						{xtype:'displayfield', fieldLabel : '到达时间', name : 'startStr'},
						{xtype:'displayfield', fieldLabel : '承办单位', name : 'assigneeCompanyName'},
						me.undertaker,
						{xtype:'displayfield', fieldLabel : '审批意见', name : 'ea_Contents'}
						//{xtype:'displayfield', fieldLabel : '更新时间', name : 'updateTime'}
				]
            }]
        });
      
        me.callParent(arguments);
      	me.loadData();
    },
    loadData : function(){
    	var me = this;
    	me.load({
		        url:me.queryUrl,
	   	        params:{
	            	id:me.recid,
	            	processInstanceId:me.processInstanceId
          	    },
	   	        failure:function(form,action) {
	   	            alert("err 155");
	   	        },
	   	        success:function(form,action){
	   	        	var result = me.getForm().getFieldValues();
	   	        }
   	    });
    },
    save:function(){
    	var me = this;
    	//var editPerson = 
    	var empid = me.undertaker.getValue() && eval(me.undertaker.getValue())[0].id;
    	var id = me.recid;
    	var processInstanceId = me.processInstanceId;
    	var form = me.getForm();
    		if(form.isValid()){
    			FHD.submit({
    				form:form,
    				url:me.savePersionUrl + '?id=' + id +'&editPerson=' + empid +'&processInstanceId='+ processInstanceId,
    				callback:function(data){
    					data && Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
    				}
    			});	
    		}
    	
    }

    
});
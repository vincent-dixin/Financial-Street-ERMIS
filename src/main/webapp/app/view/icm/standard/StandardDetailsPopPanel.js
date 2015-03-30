/**
 * 流程详细查看弹出面板
 * 
 * @author 邓广义
 */
Ext.define('FHD.view.icm.standard.StandardDetailsPopPanel', {
    extend: 'Ext.form.Panel',
    alias: 'widget.standarddetailspoppanel',
    requires: [
               'FHD.view.icm.standard.StandardDetailsPopGrid'
              ],
    /**
     * 初始化页面组件
     */
    initComponent: function () {
    	var me = this;
        me.queryUrl = __ctxPath + '/jbpm/processInstance/findJbpmHistProcinstByBusinessId.f';//通过businessId查询实体
        Ext.applyIf(me, {
            autoScroll: true,
            border: false,
            bodyPadding: "5 5 5 5",
            autoScroll:true,
            url:me.queryUrl,
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
                        {xtype:'displayfield', fieldLabel:'流程名称', name:'businessName'},
						{xtype:'displayfield', fieldLabel:'流程进度百分比', name : 'processPercent'},
						{xtype:'displayfield', fieldLabel : '流程状态', name : 'endactivity'},
						{xtype:'displayfield', fieldLabel : '流程发起者', name : 'createByRealname'},
						{xtype:'displayfield', fieldLabel : '发起者部门', name : 'createOrgName'},
						{xtype:'displayfield', fieldLabel : '发起时间', name : 'createTime'},
						{xtype:'displayfield', fieldLabel : '更新时间', name : 'updateTime'}
				]
            }]
        });
        me.callParent(arguments);
      
    },
    reloadData : function(ass_container){
	    	var me = this;
	    	me.load({
		        url:me.queryUrl,
	   	        params:{businessId : me.businessId},
	   	        failure:function(form,action) {
	   	            alert("err 155");
	   	        },
	   	        success:function(form,action){
	   	        	var endact = me.getForm().getFieldValues().endactivity;
	   	        	me.getForm().setValues({
	   	        		endactivity : 'end1'==endact?'完成':'进行中',// TODO 对这个地方的实现有疑问
	   	        		processPercent : me.getForm().getFieldValues().processPercent && me.getForm().getFieldValues().processPercent+'%'
	   	        	});
	   	        	var id = action.result.data.id;
	   	        	ass_container.standarddetailspopgrid.reload_id = id;
	   	        	ass_container.standarddetailspopgrid.reloadData();
	   	  
	   	        }
   	    });
    }
    
});
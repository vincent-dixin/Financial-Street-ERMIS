/**
 * 
 * 计划制定表单(审批)
 */

Ext.define('FHD.view.risk.assess.formulatePlan.FormulateApproverSubmitMain', {
    extend: 'Ext.form.Panel',
    alias: 'widget.formulateApproverSubmitMain',
    requires: [
               ''
	],
   
    // 初始化方法
    initComponent: function() {
        var me = this;
        
        me.fieldSet = {
            xtype:'fieldset',
            title: '基础信息',
            collapsible: true,
            collapsed : true,//初始化收缩
            margin: '5 5 0 5',
            defaults: {
                    columnWidth : 1 / 2,
                    margin: '7 30 3 30',
                    labelWidth: 95
                },
            layout: {
     	        type: 'column'
     	    },
     	    items : [ 	{xtype:'displayfield', fieldLabel : '计划名称', name:'planName'},
						{xtype:'displayfield', fieldLabel : '起止日期', name : 'createByRealname'},
						{xtype:'displayfield', fieldLabel : '联系人', name : 'createTime'},
						{xtype:'displayfield', fieldLabel : '负责人', name : 'updateTime'}]
        };
        
        me.downgrid = Ext.create('FHD.view.risk.assess.formulatePlan.FormulateApproverSubmitGridPanel',{flex:1,margin:2,columnWidth :1});

        var fieldSet3 = Ext.create('Ext.form.FieldSet',{
				layout:{
         	        type: 'column'
         	    },
				  title:'评估范围',
				  collapsible: true,
				  margin: '5 5 0 5',
				  items:[me.downgrid]
	  	});
        
        Ext.apply(me, {
        	autoScroll:true,
        	border:false,
            items : [me.fieldSet,fieldSet3],
            tbar : {
//				items : []
			}
        });

        me.callParent(arguments);

        me.downgrid.on('resize',function(p){
    		me.downgrid.setHeight(FHD.getCenterPanelHeight()-120);
		});
    }

});
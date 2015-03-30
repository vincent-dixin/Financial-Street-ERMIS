/**
 * 
 * 计划制定预览
 */

Ext.define('FHD.view.risk.assess.formulatePlan.FormulatePlanPreview', {
    extend: 'Ext.form.Panel',
    alias: 'widget.formulateplanpreview',
    
    load : function(id){
    	var me = this;
    },
    
    // 初始化方法
    initComponent: function() {
        var me = this;
        var label1 = Ext.widget('textareafield', {
            xtype: 'textareafield',
            fieldLabel: '计划名称',
            margin: '7 30 3 30',
            name: 'kpiLevel',
            columnWidth: .5
        });
        
        
        
        var label2 = Ext.widget('textareafield', {
            xtype: 'textareafield',
            rows: 3,
            fieldLabel: '工作目标',
            margin: '7 30 3 30', 
            name: 'name',
            columnWidth: .5
        });
        
        var states = Ext.create('Ext.data.Store', {
            fields: ['abbr', 'name'],
            data : [
                {"abbr":"AL", "name":"因果分析"},
                {"abbr":"AK", "name":"层次分析"}
            ]
        });
        var states1 = Ext.create('Ext.data.Store', {
            fields: ['type', 'name'],
            data : [
                {"type":"dept", "name":"部门"},
                {"type":"risk", "name":"风险"},
                {"type":"kpi", "name":"目标"}
            ]
        });
        var label3 = Ext.create('Ext.form.ComboBox', {
            fieldLabel: '工作类型',
            store: states,
            queryMode: 'local',
            displayField: 'name',
            valueField: 'abbr',
            margin: '7 30 3 30', 
            columnWidth: .5
        });
        
//        var label4 = Ext.create('FHD.ux.org.CommonSelector',{
//        	fieldLabel : '评估范围',
//        	type : 'dept',
//        	isdisplayCompany:true,
//        	value : '[{"id":"13c2667cdfe444d99c0625cbec215375"},{"id":"0e5254f249e74d63be576c8b8076c4ca"}]',
//            multiSelect : true,
//            rows: 1,
//            margin: '7 30 3 30', 
//            columnWidth: .5
//        });
        
        var label5 = Ext.widget('textfield', {
            xtype: 'textfield',
            rows: 1,
            fieldLabel: '计划编号',
            margin: '7 30 3 30', 
            name: 'name',
            columnWidth: .5
        });
        
        var label6 = Ext.create('FHD.ux.org.CommonSelector',{
        	fieldLabel: '负责人',
            type:'emp',
            multiSelect:false,
            margin: '7 30 3 30', 
            columnWidth: .5
        });
        
        var label7 = Ext.create('FHD.ux.org.CommonSelector',{
        	fieldLabel: '联系人',
//        	labelAlign: 'right',
            type:'emp',
            multiSelect:false,
            margin: '7 30 3 30', 
            columnWidth: .5
        });
        
        var label8 = Ext.widget('collectionSelector', {
    	    name:'collections',
    	    labelText : '采集频率',
    	    single:false,
    	    value:'',
    	    margin: '7 30 3 30', 
    	    columnWidth: .5
    	});
        
        var label9 = Ext.widget('textfield', {
            xtype: 'textfield',
            rows: 1,
            fieldLabel: '开始时间',
            margin: '7 30 3 30', 
            name: 'name',
            columnWidth: .5
        });
        
        var label10 = Ext.widget('textfield', {
            xtype: 'textfield',
            rows: 1,
            fieldLabel: '结束时间',
            margin: '7 30 3 30', 
            name: 'name',
            columnWidth: .5
        });
        var label11 = Ext.create('Ext.form.ComboBox', {
            fieldLabel: '评估范围类型',
            store: states1,
            queryMode: 'local',
            displayField: 'name',
            valueField: 'abbr',
            margin: '7 30 3 30', 
            columnWidth: .5
        });
        
        var fieldSet = {
            xtype:'fieldset',
            title: '基础信息',
            collapsible: true,
            margin: '5 5 0 5',
            defaults: {
                    columnWidth : 1 / 2,
                    margin: '7 30 3 30',
                    labelWidth: 95
                },
            layout: {
     	        type: 'column'
     	    },
     	    items : [ 	{xtype:'displayfield', fieldLabel:'计划名称', name:'planName'},
						{xtype:'displayfield', fieldLabel:'计划编号', name : 'processPercent'},
						{xtype:'displayfield', fieldLabel : '工作类型', name : 'endactivity'},
						{xtype:'displayfield', fieldLabel : '起止日期', name : 'createByRealname'},
						{xtype:'displayfield', fieldLabel : '联系人', name : 'createTime'},
						{xtype:'displayfield', fieldLabel : '负责人', name : 'updateTime'},
						{xtype:'displayfield', fieldLabel : '工作目标', name : 'updateTime'},
						{xtype:'displayfield', fieldLabel : '评估类型', name : 'updateTime'}]
        };
        var fieldSet2 = {
                xtype:'fieldset',
                title: '评估要求',
                collapsible: true,
                collapsed:true,
                margin: '5 5 0 5',
                defaults: {
                    columnWidth : 1 / 2,
                    margin: '7 30 3 30',
                    labelWidth: 95
                },
                layout: {
         	        type: 'column'
         	    },
         	    items : [ {xtype:'displayfield', fieldLabel : '评估要求', name : 'updateTime'}]
            };
        var fieldSet3 = {
                xtype:'fieldset',
                title: '描述',
                collapsible: true,
                collapsed:true,
                margin: '5 5 0 5',
                defaults: {
                    columnWidth : 1 / 2,
                    margin: '7 30 3 30',
                    labelWidth: 95
                },
                layout: {
         	        type: 'column'
         	    },
         	    items : [ {xtype:'displayfield', fieldLabel : '描述', name : 'updateTime'}]
            };
        var measureGrid = Ext.create('FHD.view.risk.assess.formulatePlan.FormulatePlanPreviewGrid');
//        var measureGrid = Ext.create('FHD.view.risk.assess.formulatePlan.FormulatePlanRangGrid');
	  	 var fieldSet4 = Ext.create('Ext.form.FieldSet',{
				defaults: {
					columnWidth : 1,
					labelWidth: 95
							 },
				layout: {
						type: 'column'
							},
				  title:'评估范围',
				  collapsible: true,
				  margin: '5 5 0 5',
				  items:[measureGrid]
	  	});
        
        Ext.apply(me, {
        	autoScroll:true,
        	border:false,
            items : [fieldSet,fieldSet2,fieldSet3,fieldSet4],
            tbar : {
//				items : []
			}
        });

        me.callParent(arguments);
    	me.on('resize',function(p){
			me.setHeight(FHD.getCenterPanelHeight()-30);
		});
    }

});
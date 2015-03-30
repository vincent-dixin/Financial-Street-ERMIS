/**
 * 评价计划第一步：编辑页面
 */
Ext.define('FHD.view.riskinput.form.SchemeForm',{
    extend : 'Ext.form.FieldContainer',
    alias: 'widget.schemeform',
    requires: [
    	'FHD.view.riskinput.form.KpiFieldArr'
    ],
    layout : {
                type: 'vbox'
            },
    border : true,
    margin: '7 10 0 20',
	autoHeight: true,
	kpiItems : [],
	initComponent: function() {
		/*定性 qualitative  定量quantification*/
		var me = this;
		var kpifieldarr = Ext.widget('kpifieldarr');
		
        var responsibleDept = Ext.create('FHD.ux.org.CommonSelector', {
        	margin: '7 10 0 20',
            fieldLabel: '责任人', //采集部门
            labelAlign: 'left',
            width: 400,
            name: 'gatherDept',
            multiSelect: false,
            type: 'dept_emp',
            labelWidth: 95
        });
        var delQuantification = Ext.widget('label',{
        	width: 40,
        	margin: '7 10 0 600',
			html : "<a href='javascript:void(0)' onclick='Ext.getCmp(\""+me.id+"\").delDl()'>删除</a>"
        });
        
        var responsibleDeptCon = Ext.create('Ext.container.Container', {
//           width:1000,
           layout:{
     	    	type:'hbox'  
     	   },
           items : [responsibleDept,delQuantification]
        });
        
         var jobContent = {
            xtype : 'textareafield',
            fieldLabel:'工作内容',
            labelAlign : 'left',
            margin: '7 10 0 20',
            row : 5,
            width: 600,
            name : 'requirement',
            labelWidth : 100
        };
        
		var jobTypeStore = Ext.create('Ext.data.Store',{
			fields : ['id', 'name'],
			data : [
				{'id' : 0,'name' : '加强检查'},
				{'id' : 1,'name' : '职责分离'},
				{'id' : 2,'name' : '流程优化'}
			]
		});
		var jobType= Ext.create('Ext.form.ComboBox',{
			fieldLabel: '工作类别',
			labelWidth : 100,
			width: 400,
			store :jobTypeStore,
			emptyText:'请选择',
			valueField : 'id',
			margin: '7 10 0 20',
			columnWidth : 1 / 2,
			name:'isDesirableAdjust',
			displayField : 'name',
			selectOnTab: true,
			lazyRender: true,
			typeAhead: true,
			editable : false
		});
		
		var finishtime = Ext.widget('datefield', {
            xtype: 'datefield',
            format: 'Y-m-d',
            name: 'startDateStr',
            margin: '7 10 10 20',
            fieldLabel: '计划完成时间', //开始日期
            width:400,
            allowBlank: false
        });
		var effectType= Ext.create('Ext.form.RadioGroup',{
		 fieldLabel: "预计收效",
		 width: 400,
		 margin: '7 10 0 20',
	     vertical: true,
         items: [
            { boxLabel: "很高", name: 'effectType', inputValue: '0',checked:true},
            { boxLabel: "高", name: 'effectType', inputValue: '1'},
            { boxLabel: "一般", name: 'effectType', inputValue: '2'},
            { boxLabel: "低", name: 'effectType', inputValue: '3'},
            { boxLabel: "很低", name: 'effectType', inputValue: '4'}]
        });
		
		var addQuantification = Ext.widget('label',{
        	margin: '7 10 0 295',
			html : "<a href='javascript:void(0)' onclick='Ext.getCmp(\""+me.id+"\").addDl()'>增加</a>"
        });
        
        var schemeFieldset = Ext.create('Ext.container.Container', {
           layout:{
     	    	type:'hbox'  
     	    },
           items : [effectType,addQuantification]
        });
		
		me.kpiFieldSet =  Ext.create('Ext.container.Container', {
           margin: '7 10 0 20',
           items : [kpifieldarr]
        });
        var kpiEtc =  Ext.widget('textfield', {
			name : 'kpi', 
			fieldLabel: "其它",
			margin: '0 10 0 125',
			value: me.kpiname,
			width:500,
			colspan : 5
		});
		
		
		var actualCost= Ext.create('Ext.form.RadioGroup',{
		 fieldLabel: "实际成本",
		 width: 400,
		 margin: '7 10 0 20',
	     vertical: true,
         items: [
            { boxLabel: "很高", name: 'actualCost', inputValue: '0',checked:true},
            { boxLabel: "高", name: 'actualCost', inputValue: '1'},
            { boxLabel: "一般", name: 'actualCost', inputValue: '2'},
            { boxLabel: "低", name: 'actualCost', inputValue: '3'},
            { boxLabel: "很低", name: 'actualCost', inputValue: '4'}]
        });
		
		var cost = Ext.widget('textfield', {
            xtype: 'textfield',
            fieldLabel: '直接经济成本',
            margin: '7 10 0 120',
            value: '',
            maxLength: 255,
            width: 500
        });
        var unit ={
        	 xtype:'displayfield',
        	 margin: '7 10 0 20',
    		 width:40,
    	     vertical: true,
    	     value:'万元'
        };
        
        var costCon=Ext.create('Ext.container.Container',{
        	margin: '20 10 0 0',
     	    layout:{
     	    	type:'column'  
     	    },
     	    width:1000,
     	    items:[cost,unit]
        });
        var costEtc = Ext.widget('textfield', {
            xtype: 'textfield',
            fieldLabel: '其它',
            margin: '7 10 0 120',
            value: '',
            maxLength: 255,
            width: 500
        });
        var costCon=Ext.create('Ext.container.Container',{
     	    layout:{
     	    	type:'vbox'  
     	    },
     	    width: 1000,
     	    items:[actualCost,costCon,costEtc]
        });
        
        Ext.apply(me, {
            items: [ {
                    xtype : 'fieldset',
                    collapsed : false,
                    collapsible : false,
                    title : '应对措施',
                    items : [
                          responsibleDeptCon,jobContent,jobType,finishtime,schemeFieldset,me.kpiFieldSet,kpiEtc,costCon]
                }
             
            ]
        });

        me.callParent(arguments);
    },
    addDl : function(){
    	var me = this;
    	me.kpiItems = [];
    	var kpiFieldSet = me.kpiFieldSet;   /*定量的fieldset*/
    	kpiFieldSet.removeAll();
    	var insertobj = {};
    	me.kpiselectwindow =  Ext.create('FHD.ux.kpi.opt.KpiSelectorWindow',{
			    multiSelect:true,
			    onSubmit:function(store){
			    	var kpis = store.data.items;
					Ext.Array.each(kpis,function(kpi){
						var item =kpi.data;
						insertobj = {
							id : item.id,
							text : item.text
							};
						Ext.Array.push(me.kpiItems,insertobj);
						kpiFieldSet.add(Ext.widget('kpifieldarr',{kpiname : item.name}));
						})
				    }
		});
		me.kpiselectwindow.show();
    },
    delDl : function(){
    	var self = this;
    	upPanel = self.up('container');
    	upPanel.remove(self);}
 });
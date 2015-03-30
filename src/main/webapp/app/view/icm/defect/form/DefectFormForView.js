/*缺陷添加编辑页面*/
Ext.define('FHD.view.icm.defect.form.DefectFormForView', {
	extend : 'Ext.form.Panel',
	alias: 'widget.defectformforview',
	
	layout : {
		type : 'column'
	},
	defaults : {
		columnWidth : 1/1
	},
	autoWidth:true,
	collapsed : false,
	
	initComponent :function() {
		var me = this;
		var defectId = {
			xtype:"textfield",
			name:"defectId",
			margin: '7 10 3 30',
			hidden:true,
			value:''
		};
	  
		var defectCode = {
			xtype:"textfield",
			margin: '7 10 3 30',
			fieldLabel:"编　　号",
			hidden:true,
			name:"code",
			value:''
		};
	  	//所属公司ID
		var compSelect={
			xtype:"textfield",
			name : 'companyId',
			margin: '7 10 3 30',
			hidden:true,
			multiSelect : false
		};
		var defectDesc = {
			xtype : 'displayfield',
			margin: '7 10 3 30',
			fieldLabel : '缺陷描述',
			name : 'desc'
		};
		var org = {
			xtype : 'displayfield',
			margin: '7 10 3 30',
			fieldLabel : '责任部门',
			name : 'orgName'
		};
		var defectLevel = {
			xtype : 'displayfield',
			margin: '7 10 3 30',
			fieldLabel : '缺陷级别',
			name : 'level'
		};
		var defectType = {
			xtype : 'displayfield',
			margin: '7 10 3 30',
			fieldLabel : '缺陷类型',
			name : 'type'
		};
		var designDefect = {
			xtype : 'displayfield',
			margin: '7 10 3 30',
			fieldLabel : '设计缺陷',
			name : 'designDefect'
		};
		var executeDefect = {
			xtype : 'displayfield',
			margin: '7 10 3 30',
			fieldLabel : '执行缺陷',
			name : 'designDefect'
		};
		var defectAnalyze = {
			xtype : 'textareafield',
			rows : 5,
			margin: '7 10 3 30',
			name : 'defectAnalyze'
		};
		var improveAdivce = {
			xtype : 'textareafield',
			rows : 5,
			margin: '7 10 3 30',
			name : 'improveAdivce'
		};
			
		me.items= [{
			xtype : 'fieldset',
            defaults: {
                columnWidth : 1 / 2,
                labelWidth: 100
            },
			layout : {
				type : 'column'
			},
			margin: '0 3 3 3',
			collapsed : false,
			collapsible : false,
			title : '基本信息',
			items:[ 
		        defectId,
				defectCode,
		        compSelect,
		        defectDesc,
		        defectLevel,
		        org,
		        defectType,
		        designDefect,
		        executeDefect
			]
        },
        {
			xtype : 'fieldset',
            defaults: {
                columnWidth : 1 / 1,
                labelWidth: 105
            },
			layout : {
				type : 'column'
			},
			margin: '0 3 3 3',
			collapsed : false,
			collapsible : false,
			title : '缺陷分析',
			items:[defectAnalyze]
        },{
			xtype : 'fieldset',
            defaults: {
                columnWidth : 1 / 1,
                labelWidth: 105
            },
			layout : {
				type : 'column'
			},
			margin: '0 3 3 3',
			collapsed : false,
			collapsible : false,
			title : '整改建议',
			items:[improveAdivce]
        }];
            
		Ext.applyIf(me,{
			autoScroll: true,
			items:me.items
		});
		me.callParent(arguments);
	},
	setCenterContainer:function(compent){
    	this.removeAll(true);
    	this.add(compent);
    },
    loadData: function(defectId){
    	var me = this;
    	me.defectId = defectId;
    	me.reloadData();
    },
	reloadData:function(){
	     var me = this;	
	     if(me.defectId){
	    	 me.getForm().load({
		    	 url:__ctxPath + '/icm/defect/findDefectById.f',
		    	 params:{
		    	 	defectId:me.defectId
		    	 },
		    	 success: function (form, action) {
		    		 return true;
		    	 },
		    	 failure: function (form, action) {
		    		 return true;
		    	 }
	        });
	     }
	},
	listeners:{
		afterrender:function(me){
			me.reloadData();
		}
	}
});
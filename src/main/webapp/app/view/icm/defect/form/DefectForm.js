/*缺陷添加编辑页面*/
Ext.define('FHD.view.icm.defect.form.DefectForm', {
	extend : 'Ext.form.Panel',
	alias: 'widget.defectform',
	
	items:[],
	layout : {
		type : 'column'
	},
	defaults : {
		columnWidth : 1/1
	},
	autoWidth:true,
	collapsed : false,
	height : FHD.getCenterPanelHeight(),
	bbar: {},
	
	initComponent :function() {
		var me = this;
		var defectId = {
			xtype:"textfield",
			name:"defectId",
			hidden:true,
			value:''
		};
	  
		var defectCode = {
			xtype:"textfield",
			fieldLabel:"编　　号"+'<font color=red>*</font>',
			hidden:true,
			name:"code",
			value:''
		};
	  	//所属公司ID
		var compSelect={
			xtype:"textfield",
			name : 'companyId',
			hidden:true,
			multiSelect : false
		};
		var defectDesc = {
			xtype : 'textfield',
			fieldLabel : '缺陷描述' + '<font color=red>*</font>',
			allowBlank : false,
			name : 'desc'
		};
		var org = Ext.create('FHD.ux.org.CommonSelector', {
			fieldLabel : '责任部门'+ '<font color=red>*</font>',
			name:'org',
			type : 'dept',
			allowBlank : false,
			multiSelect : false
		});
		var defectLevel = Ext.create('FHD.ux.dict.DictRadio',{
	    	name:'level',
	    	dictTypeId:'ca_defect_level',
	    	labelAlign:'left',
	    	fieldLabel : '缺陷级别'+ '<font color=red>*</font>',
	    	editable:false,
	    	columns:4,
	    	multiSelect:false
	    });
		var defectType = Ext.create('FHD.ux.dict.DictRadio',{
			name:'type',
			dictTypeId:'ca_defect_type',
			lableWidth:100,
			labelAlign:'left',
			allowBlank : false,
			fieldLabel : '缺陷类型'+ '<font color=red>*</font>'
		});	
		
		var designDefect = Ext.create('FHD.ux.dict.DictRadio',{
	    	name:'designDefect',
	    	dictTypeId:'ca_design_defect',
	    	labelAlign:'left',
	    	fieldLabel : '设计缺陷',
	    	multiSelect:false
	    });
		
		var executeDefect = Ext.create('FHD.ux.dict.DictRadio',{
	    	name:'executeDefect',
	    	dictTypeId:'ca_execute_defect',
	    	labelAlign:'left',
	    	fieldLabel : '执行缺陷',
	    	multiSelect:false
	    });
		var defectAnalyze = {
			xtype : 'textareafield',
			rows : 3,
			name : 'defectAnalyze'
		};
		var improveAdivce = {
			xtype : 'textareafield',
			rows : 3,
			name : 'improveAdivce'
		};
			
		me.items= [{
			xtype : 'fieldset',
            defaults: {
                columnWidth : 1 / 2,
                margin: '7 30 3 30',
                labelWidth: 100
            },
			layout : {
				type : 'column'
			},
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
                margin: '7 30 3 30',
                labelWidth: 105
            },
			layout : {
				type : 'column'
			},
			collapsed : false,
			collapsible : false,
			title : '缺陷分析',
			items:[defectAnalyze]
        },{
			xtype : 'fieldset',
            defaults: {
                columnWidth : 1 / 1,
                margin: '7 30 3 30',
                labelWidth: 105
            },
			layout : {
				type : 'column'
			},
			collapsed : false,
			collapsible : false,
			title : '整改建议',
			items:[improveAdivce]
        }];
            
		me.bbar={
           id: 'icm_rectifyimprove_card_bbar',
           items: ['->',{
                   id: 'icm_defect_undo_btn' ,
		           text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.undo'),//返回按钮
		           iconCls: 'icon-arrow-undo',
                   handler: function () {
                   		var rectifyImproveCenterPanel = me.up('panel').up('panel').rectifyImproveCenterPanel;
						rectifyImproveCenterPanel.removeAll(true);
						rectifyImproveCenterPanel.add(Ext.widget('defectlist'));
                   		//me.setCenterContainer(Ext.create('FHD.view.icm.defect.DefectList'));
                   }
           },{
			   id: 'icm_defect_finish_btn' ,//保存按钮
           	   text: FHD.locale.get("fhd.common.save"),
	           iconCls: 'icon-control-stop-blue',
               handler: function () {
            	    if(!me.getForm().isValid()){
                       Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'存在未通过的验证!');
                       return;
                      }; 
            		   FHD.submit({
	            		   form: me.getForm(),		   
		                   url: __ctxPath+'/icm/defect/saveDefect.f',
		                   params:{submittype:'save'},
		                   callback: function (data){
		                   		me.loadData(data.defectId);
	                  	   }
            		  });
               }
               },{
		            text: FHD.locale.get("fhd.common.submit"),//提交按钮
		            id: 'icm_defect_submit_btn' ,
		            iconCls: 'icon-control-stop-blue',
               		handler: function () {
	            	    if(!me.getForm().isValid()){
	                       Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'存在未通过的验证!');
	                       return;
	                      }; 
	            	    FHD.submit({
	            		   form: me.getForm(),		   
		                   url: __ctxPath+'/icm/defect/saveDefect.f',
		                   params:{submittype:'submit'},
		                   callback: function (data){
		                   		me.up('container').setCenterContainer(Ext.create('FHD.view.icm.defect.DefectList'));
		                   }
	            		  });
	               		}
              	 }]
       };
		Ext.applyIf(me,{
			autoScroll: true,
			bodyPadding: "0 3 3 3",
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
		    	 url:__ctxPath + '/icm/defect/findDefectForForm.f',
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
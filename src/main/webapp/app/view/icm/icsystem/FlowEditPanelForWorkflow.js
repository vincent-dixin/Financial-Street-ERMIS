/**
 * 流程基本信息编辑页面
 * 
 * @author 宋佳
 */
Ext.define('FHD.view.icm.icsystem.FlowEditPanelForWorkflow', {
	    extend: 'Ext.form.Panel',
	    alias: 'widget.floweditpanelforworkflow',
	    frame: false,
        border : false,
        readOnly : false,
        autoScroll : false,
        url: __ctxPath + '/process/process/saveProcess.f',
        paramObj:[],
        initParam:function(paramObj){
	    	var me = this;
	    	me.paramObj = paramObj;
		},
        addComponent: function () {
	    	var me = this;
	    	//基本信息fieldset
	        me.basicinfofieldset = Ext.widget('fieldset', {
	            flex:1,
	            collapsible: false,
	            autoHeight: true,
	            autoWidth: true,
	            defaults: {
	                columnWidth : 1 / 2,
	                margin: '7 30 3 30',
	                labelWidth: 105
	            },
	            layout: {
	                type: 'column'
	            },
	            title: '基本信息'
	        });
	        me.add(me.basicinfofieldset);
	        //附件信息fieldset
			me.attachmentfieldset = Ext.widget('fieldset', {
	            flex:1,
	            collapsible: true,
	            autoHeight: true,
	            
	            autoWidth: true,
	            defaults: {
	               	columnWidth : .5,
	               	margin: '7 30 3 30',
	                labelWidth: 95
	            },
	            layout: {
	                type: 'column'
	            },
	            title: '更多信息'
	
	        });
	        
	        me.add(me.attachmentfieldset);
	        //上级流程
	        me.parentprocess = Ext.widget('textfield', {
	            name: 'parentprocess',
	            fieldLabel: '流程分类', //上级流程
	            readOnly : true,
	            columnWidth: .5
	        });
	        me.basicinfofieldset.add(me.parentprocess);
	       //当前流程id
			me.processId=Ext.widget('textfield', {
	                value: '',
	                name:"id",
	                hidden : true
			});
			me.basicinfofieldset.add(me.processId);
	        //流程编号
	        me.code = Ext.widget('textfield', {
	            name : 'code',
	            fieldLabel : '流程编号' + '<font color=red>*</font>',
	            value: '',
	            allowBlank: false,
	            columnWidth: .5
	        });
	        me.basicinfofieldset.add(me.code);
	        
	        //名称
	        me.processname = Ext.widget('textfield', {
				fieldLabel : '流程名称' + '<font color=red>*</font>',
				allowBlank : false,
				value : '',
				name : 'name'
	        });
	        me.basicinfofieldset.add(me.processname);
	        
	        
	        /*责任部门  */
			me.processDepart = Ext.create('FHD.ux.org.CommonSelector', {
				fieldLabel : '责任部门' + '<font color=red>*</font>',
				name:'orgId',
				type : 'dept',
				allowBlank : false,
				multiSelect : false
			});
	        me.basicinfofieldset.add(me.processDepart);
	        /*员工单选 */
			me.processradio = Ext.create('FHD.ux.org.CommonSelector', {
				fieldLabel : '责&nbsp;&nbsp;任&nbsp;人'+ '<font color=red>*</font>',
				labelAlign : 'left',
				name:'empId',
				type : 'emp',
				allowBlank : false,
				multiSelect : false
			});
	        me.basicinfofieldset.add(me.processradio);
			/*流程发生频率*/
			me.frequency = Ext.create('FHD.ux.dict.DictSelect', {
				name : 'frequency',
				dictTypeId : 'ic_control_frequency',
				multiSelect : false,
				allowBlank : false,
				labelAlign : 'left',
				fieldLabel : '发生频率' + '<font color=red>*</font>'
			});
			me.basicinfofieldset.add(me.frequency);
	        //描述
	        me.desc = Ext.widget('textareafield', {
				height:60,
				rows : 3,
				fieldLabel : '流程描述',
				allowBlank : true,
				value : '',
				name : 'desc'
	        });
	        me.attachmentfieldset.add(me.desc);
	        //控制目标
	        me.controlTarget = Ext.widget('textfield', {
				height:60,
				rows : 3,
				fieldLabel : '控制目标',
				allowBlank : true,
				value : '',
				name : 'controlTarget'
	        });
	        me.attachmentfieldset.add(me.controlTarget);
	        
	        /*相关部门  */
			me.relaProcessDepart = Ext.create('FHD.ux.org.CommonSelector', {
				fieldLabel : '相关部门',
				name:'relaOrgId',
				type : 'dept',
				multiSelect : true
				//allowBlank : false,
			});
	        me.attachmentfieldset.add(me.relaProcessDepart);
	        //制度选择
			me.ruleselector = Ext.widget('ruleselector', {
				extraParams : {
					smIconType : 'display',
					canChecked : true
				},
				name : 'ruleId',
				value:'',
				multiSelect:true,
				fieldLabel : $locale('fhd.icm.rule.ruleSelectorLabelText')
			});
			me.attachmentfieldset.add(me.ruleselector);
	        /*重要性*/
			me.importance = Ext.create('FHD.ux.dict.DictSelect', {
				name : 'controlLevelId',
				dictTypeId : 'ic_processure_importance',
				multiSelect : false,
				labelAlign : 'left',
				fieldLabel : '重&nbsp;&nbsp;要&nbsp;&nbsp;性'
			});
			me.attachmentfieldset.add(me.importance);
			/* 影响财报科目 */
			me.relaSubject = Ext.create('FHD.ux.dict.DictSelectForEditGrid', {
				name : 'relaSubject',
				dictTypeId : 'ic_rela_subject',
				fieldLabel : '影响财报科目',
				labelAlign : 'left',
				columnWidth: .5,
				multiSelect : true
			});
			me.attachmentfieldset.add(me.relaSubject);   
			
			//排序
			me.sort = Ext.widget('numberfield', {
				fieldLabel : '排&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;序',
				name : 'sort'
			});
			me.attachmentfieldset.add(me.sort);
			//附件
	        me.attachment = Ext.widget('FileUpload', {
	        	margin: '7 10 10 30',
				labelAlign : 'left',
				labelText : '附件',
				labelWidth : 100,
				columnWidth: 1,
				name : 'fileId',
				height: 50,
				showModel : 'base'
			});    
	        me.attachmentfieldset.add(me.attachment);
	        
	    },
	    reloadData: function() {
	    	var me = this;
	        me.load({
	        	waitMsg: FHD.locale.get('fhd.kpi.kpi.prompt.waiting'),
	            url: __ctxPath + '/process/process/constructplaneditProcess.f',
	            params: {
	                processEditID: me.paramObj.processId
	            },
	            success: function (form, action) {
	                    return true;
	            }
	        });
	    },
       
       // 初始化方法
       initComponent: function() {
           var me = this;
           Ext.applyIf(me, {
	           autoScroll: true,
	           border: me.border,
	           layout: 'column',
	           width: FHD.getCenterPanelWidth() - 258,
	           bodyPadding: "0 3 3 3"
			});
		   if(me.readOnly){
		   	 me.bbar = {};
		   }else{
		   		me.bbar={
				        id: 'process_bbar',
				        items: [ '->',{
				            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.save"), //保存按钮
				            iconCls: 'icon-control-stop-blue',
				            handler: function () {
				               var me = this.up('panel');
				               me.save();
			            }
			        }
			        ]
	   		};
		   }
           me.callParent(arguments);
           //向form表单中添加控件
		   me.addComponent();
		  
       },
       
	save: function() {
	   	var me = this;
	   	var processForm = me.getForm();
    	me.processDepart.renderBlankColor(me.processDepart);
        me.processradio.renderBlankColor(me.processradio);
    	if(processForm.isValid()) {
    		FHD.submit({
				form : processForm,
				url : __ctxPath + '/process/process/saveProcess.f',
				callback: function (data) {
				}
			});
		}
	   },
	   clearFormData:function(){
   	   		var me = this;
        	me.getForm().reset();
       }
});
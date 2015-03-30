/**
 * 流程基本信息编辑页面
 * 
 * @author 宋佳
 */
Ext.define('FHD.view.icm.icsystem.FlowEditPanelForView', {
    extend: 'Ext.form.Panel',
    alias: 'widget.floweditpanelforview',
    frame: false,
    border : false,
    readOnly : false,
    autoScroll : false,
    url: '',
    paramObj:[],
    initParam:function(paramObj){
    	var me = this;
    	me.paramObj = paramObj;
	},
    addComponent: function () {
    	var me = this;
	//基本信息fieldset
		me.basicinfofieldset = Ext.widget('fieldset', {
			title: '基本信息',
			flex:1,
			collapsible: false,
			autoHeight: true,
			autoWidth: true,
			defaults: {
			    columnWidth : 1 / 2,
			    margin : '7 10 0 30',
			    labelWidth: 105
			},
			layout: {
			    type: 'column'
			}
		});
	    me.add(me.basicinfofieldset);
        // 详细信息
		me.attachmentfieldset = Ext.widget('fieldset', {
			title: '更多信息',
			flex:1,
			collapsible: true,
			autoHeight: true,
			autoWidth: true,
			defaults: {
			   	columnWidth : .5,
			   	margin : '7 10 0 30',
			    labelWidth: 95
			},
			layout: {
			    type: 'column'
			}
	    });
        //上级流程
        me.parentprocess = Ext.widget('displayfield', {
            name: 'parentprocess',
            fieldLabel: '流程分类' //上级流程
        });
        me.basicinfofieldset.add(me.parentprocess);
        //流程编号
        me.code = Ext.widget('displayfield', {
            name : 'code',
            fieldLabel : '流程编号'
        });
        me.basicinfofieldset.add(me.code);
        //名称
        me.processname = Ext.widget('displayfield', {
			fieldLabel : '流程名称',
			name : 'name'
        });
        me.basicinfofieldset.add(me.processname);
        /*责任部门  */
		me.processDepart = Ext.widget('displayfield', {
			fieldLabel : '责任部门',
			name:'orgName'
		});
        me.basicinfofieldset.add(me.processDepart);
        /*员工单选 */
		me.processradio = Ext.widget('displayfield', {
			fieldLabel : '责&nbsp;&nbsp;任&nbsp;人',
			name:'empName'
		});
        me.basicinfofieldset.add(me.processradio);
		/*流程发生频率*/
		me.frequency = Ext.widget('displayfield', {
			name : 'frequency',
			dictTypeId : 'ic_control_frequency',
			fieldLabel : '发生频率'
		});
		me.basicinfofieldset.add(me.frequency);
        //描述
        me.desc = Ext.widget('textareafield', {
			fieldLabel : '流程描述',
			height : 50,
			readOnly : true,
			name : 'desc',
			listeners : {
				focus : function(obj,eOpts){
    				obj.blur();
    			}
			}
        });
        me.attachmentfieldset.add(me.desc);
        //控制目标
        me.controlTarget = Ext.widget('textareafield', {
			fieldLabel : '控制目标',
			height : 50,
			readOnly : true,
			name : 'controlTarget',
			listeners : {
				focus : function(obj,eOpts){
    				obj.blur();
    			}
			}
        });
        me.attachmentfieldset.add(me.controlTarget);
        /*相关部门  */
		me.relaProcessDepart = Ext.widget('displayfield', {
			fieldLabel : '相关部门',
			name:'relaOrgName'
		});
        me.attachmentfieldset.add(me.relaProcessDepart);
        //制度选择
		me.ruleselector = Ext.widget('displayfield', {
			name : 'ruleName',
			fieldLabel : $locale('fhd.icm.rule.ruleSelectorLabelText')
		});
		me.attachmentfieldset.add(me.ruleselector);
        /*重要性*/
		me.importance = Ext.widget('displayfield', {
			name : 'controlLevelId',
			fieldLabel : '重&nbsp;&nbsp;要&nbsp;&nbsp;性'
		});
		me.attachmentfieldset.add(me.importance);
		/* 影响财报科目 */
		me.relaSubject = Ext.widget('displayfield', {
			name : 'relaSubject',
			labelAlign : 'left',
			fieldLabel : '影响财报科目',
			columnWidth: .5
		});
		me.attachmentfieldset.add(me.relaSubject);   
		//排序
		me.sort = Ext.widget('displayfield', {
			fieldLabel : '排&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;序',
			name : 'sort'
		});
		me.attachmentfieldset.add(me.sort);
		//附件
        me.attachment = Ext.widget('displayfield', {
			name : 'fileId',
			fieldLabel: '附件',
			columnWidth: .8
		});    
        me.attachmentfieldset.add(me.attachment);
        me.add(me.attachmentfieldset);
	},
    reloadData: function() {
    	var me = this;
        me.load({
        	waitMsg: FHD.locale.get('fhd.kpi.kpi.prompt.waiting'),
            url: __ctxPath + '/process/process/findconstructprocessformforview.f',
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
       me.callParent(arguments);
       //向form表单中添加控件
	   me.addComponent();
   },
   downloadFile : function(fileId){
        if(fileId != ''){
        	window.location.href=__ctxPath+"/sys/file/download.do?id="+fileId;
        }else{
        	Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'该样本没有上传附件!');
        }
   }
});
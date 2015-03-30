/**
 * 流程基本信息编辑页面
 * 
 * @author 元杰
 */
Ext.define('FHD.view.process.ProcessEditPanel', {
	    extend: 'Ext.form.Panel',
	    alias: 'widget.processeditpanel',
	    
       frame: false,
       border : false,
       url: __ctxPath + '/process/process/saveProcess.f',
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
	                margin: '7 10 3 30',
	                labelWidth: 105
	            },
	            layout: {
	                type: 'column'
	            },
	            title: '流程信息'
	
	        });
	        
	        me.add(me.basicinfofieldset);
	        
	        
	        //当前流程id
			me.processId=Ext.widget('hiddenfield', {
	                value: '',
	                name:"id"
			});
			me.basicinfofieldset.add(me.processId);
			//父级流程id
			me.parentprocessId=Ext.widget('hiddenfield', {
	                value: '',
	                name:"parentid"
			});
			me.basicinfofieldset.add(me.parentprocessId);
			
	        //上级流程
	        me.parentprocess = Ext.widget('displayfield', {
	            name: 'parentprocess',
	            fieldLabel: '流程分类', //上级流程
	            single: false,
	            //value: me.parentProcessName,
	            maxLength: 200,
	            columnWidth: 1
	        });
	        me.basicinfofieldset.add(me.parentprocess);
	       
	        //流程编号
	        me.code = Ext.widget('textfield', {
	            name : 'code',
	            fieldLabel : '流程编号' + '<font color=red>*</font>',
	            allowBlank: false,
	            columnWidth: .4
	        });
	        me.basicinfofieldset.add(me.code);
	        me.codeButton = {
	            xtype: 'button',
	            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.autoCode'),
	            margin: '7 10 0 0',
	            handler: function(){
	       			FHD.ajax({
		            	url:__ctxPath+'/standard/standardTree/createStandardCode.f',
		            	params: {
		                	nodeId: me.nodeId
	                 	},
		                callback: function (data) {
		                 	me.getForm().setValues({'code':data.code});//给code表单赋值
		                }
	                });
	            },
	            columnWidth: .1
	        };
	        me.basicinfofieldset.add(me.codeButton);
	        
	        //名称
	        me.processname = Ext.widget('textfield', {
				fieldLabel : '流程名称' + '<font color=red>*</font>',
				allowBlank : false,
				value : '',
				name : 'name'
	        });
	        me.basicinfofieldset.add(me.processname);
	        
			
			//描述
	        me.desc = Ext.widget('textareafield', {
				rows : 3,
				fieldLabel : '流程描述',
				allowBlank : true,
				value : '',
				name : 'desc'
	        });
	        me.basicinfofieldset.add(me.desc);

	        //排序
			me.sort = Ext.widget('numberfield', {
				fieldLabel : '排&nbsp;&nbsp;&nbsp;&nbsp;序',
				name : 'sort'
			});
			me.basicinfofieldset.add(me.sort);
			
	    },
	    reLoadData: function(store, record, parentNode) {
	    	var me = this;
			if (record.parentNode == null) {
				 return;//如果是根节点直接返回
			}
	    	var id = record.data.id;
	        var name = record.data.text;
	        var parentId = parentNode.data.id;
	        var parentName = parentNode.data.text;
	        
	        me.parentprocessId.setValue(parentId);
	        me.parentprocess.setValue(parentName);
	        me.processname.setValue(name);
	        me.load({
	        	waitMsg: FHD.locale.get('fhd.kpi.kpi.prompt.waiting'),
	            url: __ctxPath + '/process/process/editProcess.f',
	            params: {
	                processEditID: id
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
       bbar: {
        id: 'process_bbar',
        items: [ '->',{
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.save"), //保存按钮
            id: 'process_finish_btn_top',
            iconCls: 'icon-control-stop-blue',
            handler: function () {
               var me = this.up('panel');
               me.save();
//               Ext.getCmp('processeditpanel').save();
            }
        }
        ]
	   },
	   save: function() {
	   	var me = this;
	   	var processForm = me.getForm();
    	if(processForm.isValid()) {
    		FHD.submit({
				form : processForm,
				params : {
					parentId: me.parentprocessId.getValue()
				},
				url : __ctxPath + '/process/process/saveProcess.f',
				callback: function (data) {
					me.up('processmainpanel').processtree.refreshTree();
				}
			});
		}
	   },
	   clearFormData:function(){
   	   		var me = this;
        	me.getForm().reset();
       }
});
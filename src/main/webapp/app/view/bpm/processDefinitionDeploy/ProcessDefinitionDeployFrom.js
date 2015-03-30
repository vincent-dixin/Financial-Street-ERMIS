
Ext.define('FHD.view.bpm.processDefinitionDeploy.ProcessDefinitionDeployFrom',{
    extend: 'Ext.form.Panel',
    alias: 'widget.ProcessDefinitionDeployFrom',
    winId:'',
    processDefinitionDeployId:null,
    
	autoScroll:true,
	border:false,
	bodyPadding:'0 3 3 3',
    idTextfield:null,
    disNameTextfield:null,
    urlTextfield:null,
    fileUploadEntityFileUpload:null,
    reloadData:function(){
    	var me=this;
    	if(me.processDefinitionDeployId){
			me.getForm().load({
				url: __ctxPath + '/jbpm/processDefinitionDeploy/findProcessDefinitionDeployById.f',
				params: {
					processDefinitionDeployId: me.processDefinitionDeployId
				},
				success: function (form, action) {
					return true;
				},
				failure: function (form, action) {
					return false;
				}
	        });
    	}
    },
	submitData:function(cardpanel){
		var me=this;

		//提交from表单
        var form = me.getForm();
        var vobj = form.getValues();
        if(!form.isValid()){
	        Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'存在未通过的验证!');
	        return ;
        }
        if(me.idTextfield.getValue()){
	        FHD.submit({
		        form: form,
		        url: __ctxPath + '/jbpm/processDefinitionDeploy/updateProcessDefinitionDeploy.f',
		        callback: function (flag) {
		        	Ext.getCmp(me.winId).close();
		        }
	        });
        }else{
	        FHD.submit({
		        form: form,
		        url: __ctxPath + '/jbpm/processDefinitionDeploy/mergeDeployProcessDefinitionDeploy.f',
		        callback: function (flag) {
		        	Ext.getCmp(me.winId).close();
		        }
	        });
        }
	},
    initComponent : function() {
    	var me = this;
    	var bbarItems=new Array();
		bbarItems.push('->');
    	if(me.processDefinitionDeployId){
    		bbarItems.push({
				xtype:'button',
				iconCls : "icon-edit",
            	text:$locale("fhd.common.edit"),
				handler : function(){
					me.submitData();
				}
			});
    	}else{
    		bbarItems.push({
				xtype:'button',
				iconCls : "icon-add",
            	text:$locale("fhd.common.add"),
				handler : function(){
					me.submitData();
				}
			});
    		
    	}
    	me.bbar = Ext.create('Ext.Toolbar', {
			items:bbarItems
        });
    	
    	me.idTextfield = Ext.create("Ext.form.field.Text",{
    		labelWidth : 95,
			disabled : false,
			name : 'id',
			hidden : true
		});
    	me.deleteStatusTextfield = Ext.create("Ext.form.field.Text",{
    		labelWidth : 95,
			disabled : false,
			name : 'deleteStatus',
			value:"1",
			hidden : true
		});
    	me.disNameTextfield = Ext.create("Ext.form.field.Text",{
    		labelWidth : 95,
			disabled : false,
			fieldLabel : '流程定义名称'+ '<font color=red>*</font>',
			name : 'disName',
			allowBlank : false,
			labelAlign: 'left'
		});
    	me.urlTextfield = Ext.create("Ext.form.field.Text",{
    		labelWidth : 95,
			disabled : false,
			fieldLabel : '业务查看',
			name : 'url',
			labelAlign: 'left'
		});
		var fileUploadEntityFileUploadReadonly=false;
		if(me.processDefinitionDeployId){
			fileUploadEntityFileUploadReadonly=true;
		}
    	me.fileUploadEntityFileUpload = Ext.create("FHD.ux.fileupload.FileUpload",{
    		readonly:fileUploadEntityFileUploadReadonly,
            labelWidth : 95,
            name:'fileUploadEntityId',//名称
            showModel:'base',//显示模式
            multiSelect:false,
            labelText: $locale('fileupdate.labeltext')+ '<font color=red>*</font>',//标题名称
            allowBlank : false,
            labelAlign: 'left'//标题对齐方式
		});
		
    	Ext.applyIf(me, {
			border:false,
			layout:'fit',
			collapsed : false,
			bbar:me.bbar,
        	items:[
        		Ext.create('Ext.form.FieldSet',{
        			layout:'column',
					defaults:{
						columnWidth:1,
						margin : '7 10 0 30'
					},
        			title:'基础信息',
					items:[
						me.idTextfield,
		       		    me.deleteStatusTextfield,
		       		    me.disNameTextfield,
		       		    me.urlTextfield,
		       		    me.fileUploadEntityFileUpload
					]
        		})
       		    
    		]
		});
        me.callParent(arguments);
        me.reloadData();
    }
    
    
});

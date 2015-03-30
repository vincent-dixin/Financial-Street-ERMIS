/**
 * 规章制度基本信息编辑页面
 * 
 * @author 元杰
 */
Ext.define('FHD.view.icm.rule.RuleEditPanel', {
	    extend: 'Ext.form.Panel',
	    alias: 'widget.ruleeditpanel',
	    
       frame: false,
       border : false,
       addComponent: function () {
	    	var me = this;
	    	
	    	//基本信息fieldset
	        me.basicinfofieldset = Ext.widget('fieldset', {
	            flex:1,
	            collapsible: true,
	            autoHeight: true,
	            autoWidth: true,
	            defaults: {
	                columnWidth : 1 / 2,
	                margin: '7 30 3 30',
	                labelWidth: 95
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
	               	columnWidth : 1 / 1,
	               	margin: '7 30 3 30',
	                labelWidth: 95
	            },
	            layout: {
	                type: 'column'
	            },
	            title: '附件信息'
	
	        });
	        me.add(me.attachmentfieldset);
	        
			//制度分类Id,隐藏域
	    	me.parentID=Ext.widget('hiddenfield',{
	            name:"parent.id",
	            value:''
	        });
	        me.basicinfofieldset.add(me.parentID);
	    	
	    	//制度Id，隐藏域
	    	me.ruleID=Ext.widget('hiddenfield',{
	            name:"id",
	            value:''
	        });
	        me.basicinfofieldset.add(me.ruleID);
	        
	    	//制度分类名称
	    	me.parentRule=Ext.widget('textfield', {
	            fieldLabel:"分类"+'<font color=red>*</font>',
	            //allowBlank:false,
	            readOnly:true,
	            blankText:"不能为空，请填写",
	            name:"parent.name",
	            // value:'${param.ruletext}'
	           	value:''
			});
	    	me.basicinfofieldset.add(me.parentRule);
	
	    	
	    	//编号
	    	me.ruleCode=Ext.widget('textfield', {
	            fieldLabel:"编号"+'<font color=red>*</font>',
	            readOnly:true,
	            allowBlank:false,
	            name:"code",
	            value:''
	        });
	    	me.basicinfofieldset.add(me.ruleCode);
	    	
	    	//制度名称
	    	me.ruleName=Ext.widget('textfield', {
	            fieldLabel:"名称"+'<font color=red>*</font>',
	            allowBlank:false,
	            blankText:"不能为空，请填写",
	            name:"name"
	        });
	    	me.basicinfofieldset.add(me.ruleName);
	    	
	        //调用部门选择控件
	    	me.deptSelect=new Ext.create('FHD.ux.org.CommonSelector',{
	    		name:'orgid',
	        	fieldLabel : '责任部门'+'<font color=red>*</font>',
	        	type : 'dept',
	            multiSelect : false
	        });
	        me.basicinfofieldset.add(me.deptSelect);
	        
	    	//制度排序
	    	me.ruleSort=Ext.widget('numberfield', {
	            fieldLabel:"排序",
	            minValue:0,  
	            name:"sort",
	            value:""
	        });
	    	me.basicinfofieldset.add(me.ruleSort);
	    	
	    	//制度描述
	    	me.ruleDesc=Ext.widget('textarea', {
	            fieldLabel: "描述",
	            id: "memo",
	            name:"desc",
	            labelSepartor: "：",
	            value:""
	        });
	        me.basicinfofieldset.add(me.ruleDesc);
	        
			//附件
	        me.attachment = Ext.widget('FileUpload', {
				margin: '7 10 10 30',
				labelAlign : 'left',
				fieldLabel : '附件',
				labelWidth : 80,
				height: 50,
				name : 'fileIds',
				showModel : 'base'
			});    
	        me.attachmentfieldset.add(me.attachment);
	        
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
           
           me.addComponent();
       },
       bbar: {
        id: 'rule_bbar',
        items: [ '->',{
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.save"), //保存按钮
            id: 'rule_finish_btn_top',
            iconCls: 'icon-control-stop-blue',
            handler: function () {
               var me = this.up('panel');
			   me.save();
            }
        }
        ]
	   },
	   save: function() {
		   var me = this;
		   var ruleForm = me.getForm();
		   FHD.submit({
		   	form: ruleForm,
		   	url: __ctxPath + '/icm/rule/saveRule.f',
	           params: {
	           },
	           callback: function (data) {
	           }
		   });
		   },
	   clearFormData:function(){
   	   		var me = this;
        	me.getForm().reset();
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
	        
	        me.ruleID.setValue(id);
	        me.ruleName.setValue(name);
	        me.parentRule.setValue(parentName);
	        me.parentID.setValue(parentId);
	        me.load({
	        	waitMsg: FHD.locale.get('fhd.kpi.kpi.prompt.waiting'),
	            url: __ctxPath + '/icm/rule/editRule.f',
	            params: {
	                ruleid: id,
	                ruleparentid: parentId
	            },
	            success: function (form, action) {
	                    return true;
	            }
	        });
	    	
	//    	me.paramObj = paramObj;
	//    	me.initParam(paramObj);
	//    	me.reLoadData();
	    	//me.navigationBar.renderHtml('scorecardtabcontainer', me.paramObj.categoryid , '', 'sc');
	    },
	    addNextLevel: function(id, parentId){
	    	var me = this;
	    	me.load({
	        	waitMsg: FHD.locale.get('fhd.kpi.kpi.prompt.waiting'),
	            url: __ctxPath + '/icm/rule/findRuleCode.f',
	            params: {
	                ruleid: id,
	                ruleparentid: parentId
	            },
	            success: function (form, action) {
	                    return true;
	            }
	        });
	    }
});
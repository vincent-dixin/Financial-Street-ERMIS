/**
 * 规章制度基本信息预览页面
 * 
 * @author 宋佳
 */
Ext.define('FHD.view.icm.rule.RuleEditPanelForView', {
    extend: 'Ext.form.Panel',
    alias: 'widget.ruleeditpanelforview',
   	frame: false,
   	border : false,
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
    	//制度分类名称
    	me.parentRule=Ext.widget('displayfield', {
            fieldLabel:"分类",
            name:"parent.name",
           	value:''
		});
    	me.basicinfofieldset.add(me.parentRule);
    	//编号
    	me.ruleCode=Ext.widget('displayfield', {
            fieldLabel:"编号",
            name:"code"
        });
    	me.basicinfofieldset.add(me.ruleCode);
    	//制度名称
    	me.ruleName=Ext.widget('displayfield', {
            fieldLabel:"名称",
            name:"name"
        });
    	me.basicinfofieldset.add(me.ruleName);
        //调用部门选择控件
    	me.deptSelect=Ext.widget('displayfield', {
    		name:'orgid',
        	fieldLabel : '责任部门'
        });
        me.basicinfofieldset.add(me.deptSelect);
    	//制度排序
    	me.ruleSort=Ext.widget('displayfield', {
            fieldLabel:"排序",
            name:"sort"
        });
    	me.basicinfofieldset.add(me.ruleSort);
    	//制度描述
    	me.ruleDesc=Ext.widget('textarea', {
            fieldLabel: "描述",
            name:"desc",
            readOnly : true
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
   	reloadData: function() {
   		var me = this;
   		me.load({
        	waitMsg: FHD.locale.get('fhd.kpi.kpi.prompt.waiting'),
            url: __ctxPath + '/icm/rule/loadruleforview.f',
            params: {
                ruleId: me.paramObj.ruleId,
                ruleParentid: me.paramObj.ruleParentId
            },
            success: function (form, action) {
                return true;
            }
	    });
   	}
});
/**
 *    @description 内控标准数据主页面，框架包括了 tree 和 列表
 *    @author 宋佳
 *    @since 2013-3-5
 */
Ext.define('FHD.view.risk.checkpoint.CheckPointCheckManage',{
	extend : 'Ext.form.Panel',
    alias: 'widget.checkpointcheckmanage',
    margin: '7 10',
    autoHeight : true,
    autoScroll : true,
    border : false,
	initComponent : function() {
		var me = this;
       //附件
        me.uploadFile = Ext.widget('FileUpload', {
			labelAlign : 'left',
			labelText : '实施证据上传',
			labelWidth : 100,
			name : 'fileId',
			height: 50,
			width : 438,
			showModel : 'base'
		});
		me.desc = Ext.widget('textareafield',{
			fieldLabel: '检查过程结果描述',
			width : 400
		});
		/**
		 * 检查意见
		 */
		me.checkOptions = Ext.widget('fieldcontainer',{
			layout : {
				type : 'table',
				columns : 6
				},
			autoWidth : true,
			items : [
				{
                    xtype: 'radiogroup',
                    fieldLabel: '检查意见',
                    colSpan : 4,
                    width : 400,
                    items: [
                        {
                            xtype: 'radiofield',
                            boxLabel: '正常'
                        },
                        {
                            xtype: 'radiofield',
                            boxLabel: '未执行'
                        },
                        {
                            xtype: 'radiofield',
                            boxLabel: '进度落后'
                        }
                    ]
                },{
                    xtype: 'checkboxgroup',
                    colSpan : 2,	
                    items: [
                        {
                            xtype: 'checkboxfield',
                            boxLabel: '方案需调整'
                        },
                        {
                            xtype: 'checkboxfield',
                            boxLabel: '其他'
                        },
                        {
                            xtype: 'textfield'
                        }
                    ]
                }
			]
		});
    	Ext.applyIf(me,{
    		items : [me.desc,me.uploadFile,me.checkOptions]
    	})
	 	me.callParent(arguments);
	}
});

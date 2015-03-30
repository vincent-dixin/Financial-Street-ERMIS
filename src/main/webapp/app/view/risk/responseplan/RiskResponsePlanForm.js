/**
 * 风险预览页面，其中嵌套了风险控制矩阵
 * 
 * @author 宋佳
 */
Ext.define('FHD.view.risk.responseplan.RiskResponsePlanForm', {
	extend: 'Ext.form.Panel',
	alias: 'widget.riskresponseplanform',
    requires: [
    	'FHD.view.risk.responseplan.ResponseMeasureList'
       ],
    frame: false,
    border : false,
	autoHeight : true,
	autoWidth : true,
	defaults : {
    	margin: '7 30 3 30'
    },
   // 初始化方法
    initComponent: function() {
        var me = this;
        me.name = Ext.widget('textfield',{
        	fieldLabel: '名称',
        	width : 400,
        	value : ''
        });
       //附件
        me.file = Ext.widget('FileUpload', {
			labelAlign : 'left',
			labelText : '选择文件',
			labelWidth : 100,
			name : 'fileId',
			height: 50,
			width : 438,
			showModel : 'base'
		});
		me.label = Ext.widget('label',{
			text : '控制措施列表:'
		});
		me.responsemeasurelist = Ext.widget('responsemeasurelist');
//		me.solutionsFieldSet = Ext.widget('fieldset',{
//			autoHeight : true,
//            border : false,
//            items : [me.responsemeasurelist]
//        });
        Ext.applyIf(me,{
        	items : [me.name,me.file,me.label,me.responsemeasurelist]
        })
  		me.callParent(arguments);
       	}
});
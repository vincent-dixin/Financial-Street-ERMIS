Ext.define('FHD.demo.layout.SingleLayoutDemo', {
    extend: 'FHD.ux.layout.singlelayout.SingleLayout',
    alias: 'widget.singlelayoutdemo',
    requires: [
               'FHD.view.risk.risk.RiskTreePanel',
               'FHD.view.risk.risk.RiskBasicFormView',
               'FHD.view.risk.risk.RiskEventGrid',
               'FHD.view.risk.risk.RiskHistoryGrid'
    ],
    
    /**
     * 初始化页面组件
     */
    initComponent: function () {
        var me = this;

        //风险树
        var tree = Ext.widget('risktreepanel',{
        	rbs:true        	
        }); 
        //基本信息
        var riskBasicFormView =  Ext.widget('riskBasicFormView');
        //风险事件列表
        var riskEventGrid =  Ext.widget('riskeventgrid',{
        	border:false,
        	height:FHD.getCenterPanelHeight()-47
        });
        //历史信息
        var riskHistoryGrid =  Ext.widget('riskhistorygrid',{
        	border:false,
        	height:FHD.getCenterPanelHeight()-47
        });

        Ext.apply(me,{
        	tree:tree,
        	tabs:[riskBasicFormView,riskEventGrid,riskHistoryGrid]
        });
        
        me.callParent(arguments);
    }
});
Ext.define('FHD.demo.layout.MultiLayoutDemo', {
    extend: 'FHD.ux.layout.multilayout.MultiLayout',
    alias: 'widget.multilayoutdemo',
    requires: [
               'FHD.view.risk.risk.RiskTreePanel',
               'FHD.view.risk.risk.RiskBasicFormView',
               'FHD.view.risk.risk.RiskEventGrid',
               'FHD.view.risk.risk.RiskHistoryGrid',
               'FHD.view.risk.risk.RiskEditBasicFormView',
               'FHD.view.risk.strategy.StrategyTreePanel',
               'FHD.view.risk.strategy.StrategyBasicFormView'
    ],
    
    /**
     * 初始化页面组件
     */
    initComponent: function () {
        var me = this;

        //参数传入
        var page1 = {};
        page1.tree = Ext.widget('risktreepanel',{
        	face:me,
        	rbs:true        	
        }); 
        //基本信息
        var riskBasicFormView =  Ext.widget('riskBasicFormView',{
        	border:false,
        	height:FHD.getCenterPanelHeight()-47
        });
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
        var riskEditBasicFormView =  Ext.widget('riskEditBasicFormView',{
        	title:'基本信息'
        });
        page1.treeTitle = '风险';
        page1.treeIconCls = 'icon-ibm-icon-scorecards';
        page1.tabs = [riskBasicFormView,riskEventGrid,riskHistoryGrid,riskEditBasicFormView];
        
        var page2 = {};
        //基本信息
        page2.tree = Ext.widget('strategytreepanel',{id:'strategyTreePanel'});
        page2.treeTitle = '目标';
        page2.treeIconCls = 'icon-strategy';
        var strategybasicformshow =  Ext.widget('strategybasicformview',{id:'strategybasicformshow'});
        page2.tabs = [strategybasicformshow];

        Ext.apply(me,{
        	pages:[page1,page2]
        });
        
        me.callParent(arguments);
    }
});
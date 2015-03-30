/**
 * 
 * 度量标准面板
 * 使用border布局
 * 
 * 左侧是MetricTreeCardPanel,右侧是MetricCenterCardPanel
 * 
 * @author 胡迪新
 */
Ext.define('FHD.view.kpi.Metric', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.metric',

    requires: [
    	'FHD.view.kpi.MetricCenterCardPanel',
        'FHD.view.kpi.MetricTreeCardPanel'
    ],
	
    
    frame: false,
    
    // metriccentercardpanel 中部的面板
    
    // leftpanel 左部的面板
    
    // metrictreecardpanel 左侧面板中的树卡片布局面板
    
    // 布局
    layout: {
        type: 'border'
    },

    // 初始化方法
    initComponent: function() {
        var me = this;
        
        
        me.metriccentercardpanel = Ext.widget('metriccentercardpanel',{
        	border : false,
            region: 'center',
            id:'metriccentercardpanel'
        });
        
        me.metrictreecardpanel = Ext.widget('metrictreecardpanel',{
        	border : false,
        	flex: 1,
        	id:'metrictreecardpanel'
        });
        
        // 使用的是vbox布局，一个是metrictreecardpanel,其他是按钮，更改了按钮的样式，使它看见起来想手风琴布局
        me.leftpanel = Ext.widget('panel',{
        	// 右边框样式
        	style:'border-right: 1px  #99bce8 solid !important;',
        	border : false,
            xtype: 'panel',
            region: 'west',
            split:true,
            width: 210,
            defaults: {
                height: 30,
                textAlign: 'left',
                style:'border-top: 1px  #f3f7fb solid !important;border-bottom: 1px  #99bce8 solid !important;',
              	cls:'aaa-btn'
            },
            layout: {
                align: 'stretch',
                type: 'vbox'
            },
            collapsible: true,
            title: '我的文件夹',
            items: [me.metrictreecardpanel,
                {
                	id:'groupViewBtn',
                    xtype: 'button',
                    iconCls: 'icon-ibm-new-group-view',
                  	style:'border-bottom: 1px  #99bce8 solid !important;',
                  	cls:'aaa-btn aaa-selected-btn',
                    text: '我的文件夹',
                    // 点击事件，添加了选中的样式，移除了其他按钮选中样式，并切换metrictreecardpanel、metriccentercardpanel
					handler:function(){
						this.addCls('aaa-selected-btn');
						Ext.getCmp('scorecardsTreeBtn').removeCls('aaa-selected-btn');
						Ext.getCmp('strategyTreeBtn').removeCls('aaa-selected-btn');
						Ext.getCmp('kpitypeTreeBtn').removeCls('aaa-selected-btn');
//						Ext.getCmp('strategyMapTreeBtn').removeCls('aaa-selected-btn');
						me.metrictreecardpanel.setActiveItem(Ext.getCmp('myfoldertree'));
						me.metriccentercardpanel.setActiveItem(Ext.getCmp('myfoldertab'));
						//默认选择如果没有选中节点,默认选中首节点
						Ext.getCmp('myfoldertree').currentNodeClick();
						me.leftpanel.setTitle(this.text);
						me.leftpanel.setIconCls(this.iconCls);
					}
                    
                },
                /*{
                	id:'strategyMapTreeBtn',
                    xtype: 'button',
                    iconCls: 'icon-ibm-icon-reports',
                    text: '战略地图',
					handler:function(){
						this.addCls('aaa-selected-btn');
						Ext.getCmp('scorecardsTreeBtn').removeCls('aaa-selected-btn');
						Ext.getCmp('strategyTreeBtn').removeCls('aaa-selected-btn');
						Ext.getCmp('kpitypeTreeBtn').removeCls('aaa-selected-btn');
						Ext.getCmp('groupViewBtn').removeCls('aaa-selected-btn');
						me.metrictreecardpanel.setActiveItem(Ext.getCmp('strategymaptree'));
						me.metriccentercardpanel.setActiveItem(Ext.getCmp('strategymapmainpanel'));
						me.leftpanel.setTitle(this.text);
						me.leftpanel.setIconCls(this.iconCls);
					}
                },*/
                {
                	id:'strategyTreeBtn',
                    xtype: 'button',
                    iconCls: 'icon-strategy',
                    height: 30,
                    text: '战略目标',
					handler:function(){
						this.addCls('aaa-selected-btn');
						Ext.getCmp('groupViewBtn').removeCls('aaa-selected-btn');
						Ext.getCmp('scorecardsTreeBtn').removeCls('aaa-selected-btn');
						Ext.getCmp('kpitypeTreeBtn').removeCls('aaa-selected-btn');
						//Ext.getCmp('strategyMapTreeBtn').removeCls('aaa-selected-btn');
						me.metrictreecardpanel.setActiveItem(Ext.getCmp('strategyobjectivetree'));
						me.metriccentercardpanel.setActiveItem(Ext.getCmp('strategyobjectivemainpanel'));
						//默认选择如果没有选中节点,默认选中首节点
						Ext.getCmp('strategyobjectivetree').currentNodeClick();
						me.leftpanel.setTitle(this.text);
						me.leftpanel.setIconCls(this.iconCls);
					}
                },
                {
                	id : 'kpitypeTreeBtn',
                    xtype: 'button',
                    iconCls: 'icon-ibm-icon-metrictypes',
                    height: 30,
                    text: '指标类型',
					handler:function(){
						this.addCls('aaa-selected-btn');
						Ext.getCmp('groupViewBtn').removeCls('aaa-selected-btn');
						Ext.getCmp('strategyTreeBtn').removeCls('aaa-selected-btn');
						Ext.getCmp('scorecardsTreeBtn').removeCls('aaa-selected-btn');
//						Ext.getCmp('strategyMapTreeBtn').removeCls('aaa-selected-btn');
						me.metrictreecardpanel.setActiveItem(Ext.getCmp('kpitypetree'));
						me.metriccentercardpanel.setActiveItem(Ext.getCmp('kpitypemainpanel'));
						//默认选择如果没有选中节点,默认选中首节点
						Ext.getCmp('kpitypetree').currentNodeClick();
						me.leftpanel.setTitle(this.text);
						me.leftpanel.setIconCls(this.iconCls);
					}
                },
                {
                	id:'scorecardsTreeBtn',
                    xtype: 'button',
                    iconCls: 'icon-ibm-icon-scorecards',
                    height: 30,
                    style:'border-top: 1px  #f3f7fb solid !important;',
                    text: '记分卡',
					handler:function(){
						this.addCls('aaa-selected-btn');
						Ext.getCmp('groupViewBtn').removeCls('aaa-selected-btn');
						Ext.getCmp('strategyTreeBtn').removeCls('aaa-selected-btn');
						Ext.getCmp('kpitypeTreeBtn').removeCls('aaa-selected-btn');
//						Ext.getCmp('strategyMapTreeBtn').removeCls('aaa-selected-btn');
						me.metrictreecardpanel.setActiveItem(Ext.getCmp('scorecardtree'));
						me.metriccentercardpanel.setActiveItem(Ext.getCmp('scorecardmainpanel'));
						//默认选择如果没有选中节点,默认选中首节点
						Ext.getCmp('scorecardtree').currentNodeClick();
						me.leftpanel.setTitle(this.text);
						me.leftpanel.setIconCls(this.iconCls);
					}
                }
            ]
        });
        
        
        
        Ext.applyIf(me, {
            items: [me.metriccentercardpanel,me.leftpanel]
        });

        me.callParent(arguments);
    }

});
/**
 * 
 * 所有度量指标标签
 * 使用Tab Panel
 * 
 * 显示全部度量指标列表
 * 
 * @author 胡迪新
 */
Ext.define('FHD.view.kpi.myfolder.AllMetricTab', {
    extend: 'Ext.tab.Panel',
    alias: 'widget.allmetrictab',

    plain: true,
    border: true,

    requires: [
        'FHD.view.kpi.myfolder.MyFolderKpiGrid'],

    // 初始化方法
    initComponent: function () {
        var me = this;
        me.myfolderkpigrid = Ext.widget('myfolderkpigrid', {
            id: 'myfolderkpigrid'
        });

        Ext.applyIf(me, {
            tabBar: {
                style: 'border-left: 1px  #99bce8 solid;'
            },
            plain: me.plain,
            border: me.border,
            items: [
            me.myfolderkpigrid]
        });

        me.callParent(arguments);

        me.getTabBar().insert(0, {
            xtype: 'tbfill'
        });

    },
    /**
     * 重新加载数据
     */
    reLoadData:function(){
    	var me = this;
		me.myfolderkpigrid.store.load();
    }


});
Ext.define('FHD.view.monitor.kpi.kpiCard', {
    extend: 'FHD.ux.CardPanel',
    
    activeItem: 0,
    backType:'',
    requires: [
               'FHD.view.kpi.kpi.KpiGatherForm',
               'FHD.view.kpi.kpi.KpiWarningSet'
              ],
    
    /**
     * 返回按钮事件
     */
    undo:function(){
    	var me = this;
    	me.gotopage();
    },
    
    /**
     * 下一步按钮事件
     */
    last:function(){
    	var me = this;
        var activePanel = me.getActiveItem();
        if (activePanel.last) {
            activePanel.last(me);
        }
        //me.lastSetBtnState(me, activePanel);
    },
    
    /**
     * 完成按钮事件
     */
    finish:function(){
    	var me = this;
    	me.setBtnState(0);
        var activePanel = me.getActiveItem();
        if (activePanel.last) {
            activePanel.last(me, true);
        }
    },
    
    gotopage:function(){
    	var me = this;
    	var scorecardtab = Ext.getCmp('scorecardtab');
    	var activeItem = me.monitorContainer.getActiveTree();
    	
    	if(me.backType=="sc"){
        	var scorecardmainpanel = Ext.getCmp('scorecardmainpanel');
        	var scorecardkpigrid = Ext.getCmp('scorecardkpigrid');
        	//导航刷新
        	scorecardmainpanel.navReRender();
        	//当点击添加或编辑指标时,在点击记分卡节点时,返回时需要设置记分卡scorecardmainpanel主面板在右侧
        	if(activeItem.id=='sctree'){
        		me.monitorContainer.reRightLayout(scorecardmainpanel);
        	}
        	scorecardtab.setActiveTab(0);
        	//刷新度量指标grid
        	scorecardkpigrid.reLoadData(scorecardtab.paramObj.categoryid);
        	
    	}else if(me.backType=="allkpi"){
    		var allmetricmainpanel = Ext.getCmp('allmetricmainpanel');
    		if(activeItem.id=='scorecardtree'){//当点击添加或编辑指标时,在点击记分卡节点时,返回时需要设置记分卡scorecardmainpanel主面板在右侧
        		Ext.getCmp('metriccentercardpanel').setActiveItem(allmetricmainpanel);
        		allmetricmainpanel.reLoadData();
        	}
    	}else if(me.backType=="mykpi"){
    		var myfoldertab = Ext.getCmp('myfoldertab');
    		if(activeItem.id=='myfoldertree'){//当点击添加或编辑指标时,在点击记分卡节点时,返回时需要设置记分卡scorecardmainpanel主面板在右侧
        		Ext.getCmp('metriccentercardpanel').setActiveItem(myfoldertab);
        		myfoldertab.reLoadData();
        	}
    	}else if(me.backType=="kpitype"){
    		var selectedNode = {};
    		var nodeItems = Ext.getCmp('kpitypetree').getSelectionModel().selected.items;
            if (nodeItems.length > 0) {
                selectedNode = nodeItems[0];
            }
    		var kpitypemainpanel = Ext.getCmp('kpitypemainpanel');
            if (activeItem.id == 'kpitypetree') { //当点击添加或编辑指标时,在点击战略节点时,需要设置记分卡kpitypemainpanel主面板在右侧
                Ext.getCmp('metriccentercardpanel').setActiveItem(kpitypemainpanel);
            }
            kpitypemainpanel.reLoadData(selectedNode);
    	}else if(me.backType=="sm"){
    		var selectedNode = {};
    		var nodeItems = Ext.getCmp('strategyobjectivetree').getSelectionModel().selected.items;
            if (nodeItems.length > 0) {
                selectedNode = nodeItems[0];
            }
            var strategyobjectivemainpanel = Ext.getCmp('strategyobjectivemainpanel');
            if (activeItem.id == 'strategyobjectivetree') { //当点击添加或编辑指标时,在点击战略节点时,需要设置记分卡strategyobjectivemainpanel主面板在右侧
                Ext.getCmp('metriccentercardpanel').setActiveItem(strategyobjectivemainpanel);
            }
            strategyobjectivemainpanel.reLoadData(selectedNode);
    	}
    	
    },
    /**
     * 点击下一步按钮时要设置导航按钮的选中或不选中状态
     * @param {panel} cardPanel cardpanel面板
     * @param {panel} activePanel 激活面板
     */
    lastSetBtnState: function (cardpanel, activePanel) {
        var items = cardpanel.items.items;
        var index = Ext.Array.indexOf(items, activePanel) + 1;
        this.setBtnState(index);
    },
    
    /**
    * 设置导航按钮的事件函数
    * @param {panel} cardPanel cardpanel面板
    * @param index 面板索引值
    */
    navBtnHandler: function (cardPanel, index) {
	   	var me = this;
	    cardPanel.getLayout().setActiveItem(index);
	    me.navBtnState();
       
    },
    
    /**
     * 设置上一步和下一步按钮的状态
     */
    navBtnState:function(){
    	var me = this;
    	var layout = me.getLayout();
    	Ext.getCmp('kpi_kpi_move-prev' ).setDisabled(!layout.getPrev());
        Ext.getCmp('kpi_kpi_move-next' ).setDisabled(!layout.getNext());
        Ext.getCmp('kpi_kpi_move-prev_top' ).setDisabled(!layout.getPrev());
        Ext.getCmp('kpi_kpi_move-next_top' ).setDisabled(!layout.getNext());
    },
    
    /**
     * 上一步按钮事件
     */
    back:function(){
    	var me = this;
        me.pageMove("prev");
        var activePanel = me.getActiveItem();
        me.navBtnState();
        me.preSetBtnState(me, activePanel);
        var kpimainpanel = Ext.getCmp('kpimainpanel');
        //kpimainpanel.paramObj.editflag = true;
        
    },
    /**
     * 点击上一步按钮时要设置导航按钮的选中或不选中状态
     * @param {panel} cardPanel cardpanel面板
     * @param {panel} activePanel 激活面板
     */
    preSetBtnState: function (cardpanel, activePanel) {
        var items = cardpanel.items.items;
        var index = Ext.Array.indexOf(items, activePanel)
        this.setBtnState(index);
    },
    /**
     * 设置导航按钮的选中或不选中状态
     * @param index,要激活的面板索引
     */
    setBtnState: function (index) {
        var k = 0;
        var topbar = Ext.getCmp('kpi_kpi_topbar');
        var btns = topbar.items.items;
        for (var i = 0; i < btns.length; i++) {
            var item = btns[i];
            if (item.pressed != undefined) {
                if (k == index) {
                    item.toggle(true);
                } else {
                    item.toggle(false);
                }
                k++;
            }
        }
        k = 0;
        var bbar = Ext.getCmp('kpi_kpi_bbar');
        btns = bbar.items.items;
        for (var i = 0; i < btns.length; i++) {
            var item = btns[i];
            if (item.pressed != undefined) {
                if (k == index) {
                    item.toggle(true);
                } else {
                    item.toggle(false);
                }
                k++;
            }
        }
    },
    
    /**
     * 设置首项为激活状态
     * 
     */
    setFirstItemFoucs:function(disable){
    	var kpicardpanel = Ext.getCmp('kpicardpanel');
    	kpicardpanel.setBtnState(0);
    	kpicardpanel.navBtnHandler(kpicardpanel, 0);
    	Ext.getCmp('kpi_kpi_caculate_btn_top').setDisabled(disable);
        Ext.getCmp('kpi_kpi_caculate_btn').setDisabled(disable);
        Ext.getCmp('kpi_kpi_alarmset_btn').setDisabled(disable);
        Ext.getCmp('kpi_kpi_alarmset_btn_top').setDisabled(disable);
    },
    
    /**
     * 使导航按钮为enable状态
     */
    setNavBtnEnable:function(v,first){
    	if(first){
    		Ext.getCmp('kpi_kpi_details_btn').setDisabled(v);
            Ext.getCmp('kpi_kpi_details_btn_top').setDisabled(v);
    	}
        Ext.getCmp('kpi_kpi_caculate_btn_top').setDisabled(v);
        Ext.getCmp('kpi_kpi_caculate_btn').setDisabled(v);
        Ext.getCmp('kpi_kpi_alarmset_btn').setDisabled(v);
        Ext.getCmp('kpi_kpi_alarmset_btn_top').setDisabled(v);
    },
    
    tbar: {

        id: 'kpi_kpi_topbar' ,
        items: [

        {
            text: FHD.locale.get('fhd.common.details'),//基本信息导航按钮
            iconCls: 'icon-001',
            id: 'kpi_kpi_details_btn_top' ,
            handler: function () {
            	Ext.getCmp('kpicardpanel').setBtnState(0);
            	Ext.getCmp('kpicardpanel').navBtnHandler(this.up('panel'), 0);
            }
        },
        '<img src="'+__ctxPath+'/images/icons/show_right.gif">',
         {
            text: FHD.locale.get("fhd.kpi.kpi.toolbar.caculate"),//采集计算报告导航按钮
            iconCls: 'icon-002',
            id: 'kpi_kpi_caculate_btn_top' ,
            handler: function () {
            	Ext.getCmp('kpicardpanel').setBtnState(1);
            	Ext.getCmp('kpicardpanel').navBtnHandler(this.up('panel'), 1);
            }
        },
        '<img src="'+__ctxPath+'/images/icons/show_right.gif">',
         {
            text: FHD.locale.get("fhd.kpi.kpi.toolbar.alarmset"),//告警设置导航按钮
            iconCls: 'icon-003',
            id: 'kpi_kpi_alarmset_btn_top' ,
            handler: function () {
            	Ext.getCmp('kpicardpanel').setBtnState(2);
            	Ext.getCmp('kpicardpanel').navBtnHandler(this.up('panel'), 2);
            }
        },
            '->', 
            {
                id: 'kpi_kpi_move-undo_top' ,
                text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.undo'),//返回按钮
                iconCls: 'icon-arrow-undo',
                handler: function () {
                	Ext.getCmp('kpicardpanel').undo();
                }
            },
            {
            id: 'kpi_kpi_move-prev_top' ,
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.back"),//上一步按钮
            iconCls: 'icon-control-rewind-blue',
            handler: function () {
            	Ext.getCmp('kpicardpanel').back();
            }
        }, {
            id: 'kpi_kpi_move-next_top' ,
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.last"),//下一步按钮
            iconCls: 'icon-control-fastforward-blue',
            handler: function () {
            	Ext.getCmp('kpicardpanel').last();
            }
        }, {
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.save"),//保存按钮
            id: 'kpi_kpi_finish_btn_top' ,
            iconCls: 'icon-control-stop-blue',
            handler: function () {
            	Ext.getCmp('kpicardpanel').finish();
            }
        }]
    },
    bbar: {
        id: 'kpi_kpi_bbar' ,
        items: [


        {
            text: FHD.locale.get('fhd.common.details'),//基本信息导航按钮
            iconCls: 'icon-001',
            id: 'kpi_kpi_details_btn' ,
            handler: function () {
            	Ext.getCmp('kpicardpanel').setBtnState(0);
            	Ext.getCmp('kpicardpanel').navBtnHandler(this.up('panel'), 0);
            }
        },
        '<img src="'+__ctxPath+'/images/icons/show_right.gif">',
         {
            text: FHD.locale.get("fhd.kpi.kpi.toolbar.caculate"),//采集计算报告导航按钮
            iconCls: 'icon-002',
            id: 'kpi_kpi_caculate_btn' ,
            handler: function () {
            	Ext.getCmp('kpicardpanel').setBtnState(1);
            	Ext.getCmp('kpicardpanel').navBtnHandler(this.up('panel'), 1);
            }
        },
        '<img src="'+__ctxPath+'/images/icons/show_right.gif">',
         {
            text: FHD.locale.get("fhd.kpi.kpi.toolbar.alarmset"),//告警设置导航按钮
            iconCls: 'icon-003',
            id: 'kpi_kpi_alarmset_btn' ,
            handler: function () {
            	Ext.getCmp('kpicardpanel').setBtnState(2);
            	Ext.getCmp('kpicardpanel').navBtnHandler(this.up('panel'), 2);
            }
        },
        '->', 
        {
            id: 'kpi_kpi_move-undo' ,
            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.undo'),//返回按钮
            iconCls: 'icon-arrow-undo',
            handler: function () {
            	Ext.getCmp('kpicardpanel').undo();
            }
        },
            
        {
            id: 'kpi_kpi_move-prev' ,
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.back"),//上一步按钮
            iconCls: 'icon-control-rewind-blue',
            handler: function () {
            	Ext.getCmp('kpicardpanel').back();

            }
        }, {
            id: 'kpi_kpi_move-next' ,
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.last"),//下一步按钮
            iconCls: 'icon-control-fastforward-blue',
            handler: function () {
            	Ext.getCmp('kpicardpanel').last();
            }
        }, {
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.save"),//保存按钮
            id: 'kpi_kpi_finish_btn' ,
            iconCls: 'icon-control-stop-blue',
            handler: function () {
            	Ext.getCmp('kpicardpanel').finish();
                
            }
        }]
    },
    // 初始化方法
    initComponent: function() {
        var me = this;
        if(!me.kpibasicform){
        	me.kpibasicform = Ext.create('FHD.view.monitor.kpi.basicForm',{id:'kpibasicform'});
        }
        //me.kpigatherform = Ext.widget('kpigatherform',{id:'kpigatherform'});
        //me.kpiwarningset = Ext.widget('kpiwarningset',{id:'kpiwarningset'});
        Ext.apply(me, {
            items: [
                    me.kpibasicform,
                    //me.kpigatherform,
                    //me.kpiwarningset
                   ]
        });

        me.callParent(arguments);
        
    
    },
    
    setInitBtnState:function(){
    	var me = this;
    	var kpimainpanel = Ext.getCmp('kpimainpanel');
        if(!kpimainpanel.paramObj.editflag){
        	//添加指标
        	me.setNavBtnEnable(true,false);
        }else{
        	//编辑指标
        	me.setNavBtnEnable(false,true);
        }
    }

});
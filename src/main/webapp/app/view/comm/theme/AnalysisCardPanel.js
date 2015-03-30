Ext.define('FHD.view.comm.theme.AnalysisCardPanel', {
    extend: 'FHD.ux.CardPanel',
    alias: 'widget.analysiscardpanel',
    
    activeItem: 0,
    
    requires: [
               'FHD.view.comm.theme.AnalysisLayoutForm',
               'FHD.view.comm.theme.AnalysisLayoutPanel',
               'FHD.view.comm.theme.AnalysisLayoutPanel2',
               'FHD.view.comm.theme.AnalysisLayoutPanel3',
               'FHD.view.comm.theme.LayoutDataPanel'
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
        if(activePanel.id=='analysislayoutpanel'){
        	me.gotopage();
        }
        if(activePanel.id=='analysislayoutpanel2'){
        	me.gotopage();
        }
        if(activePanel.id=='analysislayoutpanel3'){
        	me.gotopage();
        }
    },
    
    gotopage:function(){
    	var themetab = Ext.getCmp('themetab');
    	var themetreecardpanel = Ext.getCmp('themetreecardpanel');
    	var thememainpanel = Ext.getCmp('thememainpanel');
    	var themerecordgrid = Ext.getCmp('themerecordgrid');
//    	thememainpanel.navigationBar.renderHtml('scorecardtabcontainer', thememainpanel.paramObj.categoryid , '', 'sc');
    	var activeItem = themetreecardpanel.getActiveItem();
    	if(activeItem.id=='themetree'){//当点击添加或编辑指标时,在点击记分卡节点时,返回时需要设置记分卡scorecardmainpanel主面板在右侧
    		Ext.getCmp('themecentercardpanel').setActiveItem(thememainpanel);
    	}
    	themetab.setActiveTab(0);
    	//刷新度量指标grid
    	themerecordgrid.store.proxy.extraParams.id = themetab.paramObj.categoryid;
		themerecordgrid.store.load();
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
    	Ext.getCmp('theme_move-prev' ).setDisabled(!layout.getPrev());
        Ext.getCmp('theme_move-next' ).setDisabled(!layout.getNext());
        Ext.getCmp('theme_move-prev_top' ).setDisabled(!layout.getPrev());
        Ext.getCmp('theme_move-next_top' ).setDisabled(!layout.getNext());
    },
    
    /**
     * 上一步按钮事件
     */
    back:function(){
    	var me = this;
        var  analysislayoutform = Ext.getCmp('analysislayoutform');
        Ext.getCmp('analysiscardpanel').setActiveItem(analysislayoutform);
        Ext.getCmp('theme_move-next_top' ).setDisabled(false);
        Ext.getCmp('theme_move-prev_top' ).setDisabled(true);
        Ext.getCmp('theme_move-next' ).setDisabled(false);
        Ext.getCmp('theme_move-prev' ).setDisabled(true);
        
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
        var topbar = Ext.getCmp('theme_topbar');
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
        var bbar = Ext.getCmp('theme_bbar');
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
    	var analysiscardpanel = Ext.getCmp('analysiscardpanel');
    	analysiscardpanel.setBtnState(0);
    	analysiscardpanel.navBtnHandler(analysiscardpanel, 0);
    	Ext.getCmp('theme_caculate_btn_top').setDisabled(disable);
        Ext.getCmp('theme_caculate_btn').setDisabled(disable);
    },
    
    /**
     * 使导航按钮为enable状态
     */
    setNavBtnEnable:function(v,first){
    	if(first){
    		Ext.getCmp('theme_details_btn').setDisabled(v);
            Ext.getCmp('theme_details_btn_top').setDisabled(v);
    	}
        Ext.getCmp('theme_caculate_btn_top').setDisabled(v);
        Ext.getCmp('theme_caculate_btn').setDisabled(v);
    },
    
    tbar: {

        id: 'theme_topbar' ,
        items: [

        {
            text: '页面布局',
            iconCls: 'icon-001',
            id: 'theme_details_btn_top' ,
            handler: function () {
            	Ext.getCmp('analysiscardpanel').setBtnState(0);
            	Ext.getCmp('analysiscardpanel').navBtnHandler(this.up('panel'), 0);
            }
        },
        '<img src="'+__ctxPath+'/images/icons/show_right.gif">',
         {
            text: '基础设置',
            iconCls: 'icon-002',
            id: 'theme_caculate_btn_top' ,
            handler: function () {
            	Ext.getCmp('analysiscardpanel').last();
            }
        },'->', 
        
            {
                id: 'theme_move-undo_top' ,
                text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.undo'),//返回按钮
                iconCls: 'icon-arrow-undo',
                handler: function () {
                	Ext.getCmp('analysiscardpanel').undo();
                }
            },
            {
            id: 'theme_move-prev_top' ,
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.back"),//上一步按钮
            iconCls: 'icon-control-rewind-blue',
            handler: function () {
            	Ext.getCmp('analysiscardpanel').back();
            }
        }, {
            id: 'theme_move-next_top' ,
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.last"),//下一步按钮
            iconCls: 'icon-control-fastforward-blue',
            handler: function () {
            	Ext.getCmp('analysiscardpanel').last();
            }
        }, {
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.save"),//保存按钮
            id: 'theme_finish_btn_top' ,
            iconCls: 'icon-control-stop-blue',
            handler: function () {
            	Ext.getCmp('analysiscardpanel').finish();
            }
        }]
    },
    bbar: {
        id: 'theme_bbar' ,
        items: [


        {
            text: '页面布局',
            iconCls: 'icon-001',
            id: 'theme_details_btn' ,
            handler: function () {
            	Ext.getCmp('analysiscardpanel').setBtnState(0);
            	Ext.getCmp('analysiscardpanel').navBtnHandler(this.up('panel'), 0);
            }
        },
        '<img src="'+__ctxPath+'/images/icons/show_right.gif">',
         {
            text: '基础设置',
            iconCls: 'icon-002',
            id: 'theme_caculate_btn' ,
            handler: function () {
            	Ext.getCmp('analysiscardpanel').last();
            }
        },'->', 
        
        {
            id: 'theme_move-undo' ,
            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.undo'),//返回按钮
            iconCls: 'icon-arrow-undo',
            handler: function () {
            	Ext.getCmp('analysiscardpanel').undo();
            }
        },
            
        {
            id: 'theme_move-prev' ,
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.back"),//上一步按钮
            iconCls: 'icon-control-rewind-blue',
            handler: function () {
            	Ext.getCmp('analysiscardpanel').back();

            }
        }, {
            id: 'theme_move-next' ,
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.last"),//下一步按钮
            iconCls: 'icon-control-fastforward-blue',
            handler: function () {
            	Ext.getCmp('analysiscardpanel').last();
            }
        }, {
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.save"),//保存按钮
            id: 'theme_finish_btn' ,
            iconCls: 'icon-control-stop-blue',
            handler: function () {
            	Ext.getCmp('analysiscardpanel').finish();
//            	Ext.getCmp('analysiscardpanel').undo();
                
            }
        }]
    },
    // 初始化方法
    initComponent: function() {
        var me = this;
        me.analysislayoutform = Ext.widget('analysislayoutform',{id:'analysislayoutform'});
        me.analysislayoutpanel = Ext.widget('analysislayoutpanel',{id:'analysislayoutpanel'});
        me.analysislayoutpanel2 = Ext.widget('analysislayoutpanel2',{id:'analysislayoutpanel2'});
        me.analysislayoutpanel3 = Ext.widget('analysislayoutpanel3',{id:'analysislayoutpanel3'});
        Ext.apply(me, {
            items: [
                    me.analysislayoutform,
                    me.analysislayoutpanel,
                    me.analysislayoutpanel2,
                    me.analysislayoutpanel3
                   ]
        });

        me.callParent(arguments);
        
    
    },
    
    setInitBtnState:function(){
    	var me = this;
    	var analysismainpanel = Ext.getCmp('analysismainpanel');
        if(!analysismainpanel.paramObj.editflag){
        	//添加指标
        	me.setNavBtnEnable(true,false);
        }else{
        	//编辑指标
        	me.setNavBtnEnable(false,true);
        }
    }

});
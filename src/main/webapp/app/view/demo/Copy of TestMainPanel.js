Ext.define('FHD.view.demo.TestMainPanel', {
    extend: 'FHD.ux.CardPanel',
    alias: 'widget.formPanel',
    requires: [
               'FHD.view.kpi.scorecard.ScorecardBasicForm',
               'FHD.view.kpi.scorecard.ScorecardWarningSet'
           ],
    activeItem: 0,
    
    /**
     * 设置上一步和下一步按钮的状态
     */
    navBtnState:function(){
    	var me = this;
    	var layout = me.getLayout();
    	me.preTop.setDisabled(!layout.getPrev());
        me.nextTop.setDisabled(!layout.getNext());
        me.preBottom.setDisabled(!layout.getPrev());
        me.nextBottom.setDisabled(!layout.getNext());
    },
    
    /**
     * 设置导航按钮的选中或不选中状态
     * @param index,要激活的面板索引
     */
    navPanelState: function (index) {
    	var me = this;
    	
    	//top
    	me.basicTop.toggle(false);
    	me.listTop.toggle(false);
    	if(index == 0){
    		me.basicTop.toggle(true);
    	}else{
    		me.listTop.toggle(true);
    	}
    	
    	//bottom
    	me.basicBottom.toggle(false);
    	me.listBottom.toggle(false);
    	if(index == 0){
    		me.basicBottom.toggle(true);
    	}else{
    		me.listBottom.toggle(true);
    	}
    },
    
    /**
     * 返回按钮事件
     */
    undo:Ext.emptyFn(),
    
    /**
     * 上一步按钮事件
     */
    back:function(){
    	var me = this;
        me.pageMove("prev");
        me.navBtnState();
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
        
    	me.pageMove("next");
        me.navBtnState();
    },
    
    /**
     * 完成按钮事件
     */
    finish:function(){
    	var me = this;
        var activePanel = me.getActiveItem();
        if (activePanel.last) {
            activePanel.last(me, true);
        }
    },
    
    /**
     * 初始化页面组件
     */
    initComponent: function () {
    	var me = this;
        me.scorecardbasicform = Ext.widget('scorecardbasicform',{id : 'scorecardbasicform'});
        me.scorecardwarningset = Ext.widget('scorecardwarningset',{id : 'scorecardwarningset'});
        
        //top工具条
        me.basicTop = Ext.create('Ext.button.Button',{
            text: FHD.locale.get('fhd.common.details'),//基本信息按钮
            iconCls: 'icon-001',
            handler: function () {
                me.navPanelState(0);
                me.getLayout().setActiveItem(0);
                me.navBtnState();
            }
        });
        me.listTop = Ext.create('Ext.button.Button',{
            text: FHD.locale.get("fhd.kpi.kpi.toolbar.alarmset"),//告警设置按钮
            iconCls: 'icon-002',
            handler: function () {
            	me.navPanelState(1);
            	me.getLayout().setActiveItem(1);
                me.navBtnState();
            }
        });
        me.undoTop = Ext.create('Ext.button.Button',{
            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.undo'),//返回按钮
            iconCls: 'icon-arrow-undo',
            handler: function () {
            	me.undo();
            }
        });
        me.preTop = Ext.create('Ext.button.Button',{
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.back"),//上一步按钮
            iconCls: 'icon-control-rewind-blue',
            handler: function () {
            	me.back();
            }
        });
        me.nextTop = Ext.create('Ext.button.Button',{
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.last"),//下一步按钮
            iconCls: 'icon-control-fastforward-blue',
            handler: function () {
            	me.last();
            }
        });
        me.finishTop = Ext.create('Ext.button.Button',{
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.save"),//保存按钮
            iconCls: 'icon-control-stop-blue',
            handler: function () {
            	me.finish();
            }
        });
        
        //bottom工具条
        me.basicBottom = Ext.create('Ext.button.Button',{
            text: FHD.locale.get('fhd.common.details'),//基本信息按钮
            iconCls: 'icon-001',
            handler: function () {
                me.navPanelState(0);
                me.getLayout().setActiveItem(0);
                me.navBtnState();
            }
        });
        me.listBottom = Ext.create('Ext.button.Button',{
            text: FHD.locale.get("fhd.kpi.kpi.toolbar.alarmset"),//告警设置按钮
            iconCls: 'icon-002',
            handler: function () {
            	me.navPanelState(1);
            	me.getLayout().setActiveItem(1);
                me.navBtnState();
            }
        });
        me.undoBottom = Ext.create('Ext.button.Button',{
            text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.undo'),//返回按钮
            iconCls: 'icon-arrow-undo',
            handler: function () {
            	me.undo();
            }
        });
        me.preBottom = Ext.create('Ext.button.Button',{
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.back"),//上一步按钮
            iconCls: 'icon-control-rewind-blue',
            handler: function () {
            	me.back();
            }
        });
        me.nextBottom = Ext.create('Ext.button.Button',{
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.last"),//下一步按钮
            iconCls: 'icon-control-fastforward-blue',
            handler: function () {
            	me.last();
            }
        });
        me.finishBottom = Ext.create('Ext.button.Button',{
            text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.save"),//保存按钮
            iconCls: 'icon-control-stop-blue',
            handler: function () {
            	me.finish();
            }
        });
        
        Ext.apply(me, {
        	tbar: {
        		items:[me.basicTop,'<img src="'+__ctxPath+'/images/icons/show_right.gif">',me.listTop,'->',me.undoTop,me.preTop,me.nextTop,me.finishTop]
        	},
        	bbar: {
        		items:[me.basicBottom,'<img src="'+__ctxPath+'/images/icons/show_right.gif">',me.listBottom,'->',me.undoBottom,me.preBottom,me.nextBottom,me.finishBottom]
        	},
            items: [
                    me.scorecardbasicform,me.scorecardwarningset
                   ]
        });
        
        me.callParent(arguments);
      
    }
});
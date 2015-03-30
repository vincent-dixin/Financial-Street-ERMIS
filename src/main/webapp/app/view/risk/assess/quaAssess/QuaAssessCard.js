/**
 * 
 * 定性评估卡片面板
 */

Ext.define('FHD.view.risk.assess.quaAssess.QuaAssessCard',{
	extend: 'FHD.ux.CardPanel',
    alias: 'widget.quaAssessCard',
    
    requires: [
				 'FHD.view.risk.assess.quaAssess.QuaAssessOpe',
                 'FHD.view.risk.assess.quaAssess.QuaAssessGrid',
				 'FHD.view.risk.assess.quaAssess.QuaAssessShowGrid',
				 'FHD.view.risk.assess.quaAssess.QuaAssessSubmit'
              ],
              
    showQuaAssessOpe : function(){
  		var me = this;
  		me.getLayout().setActiveItem(me.items.items[1]);
  	},
  	
  	showQuaAssessGrid : function(){
  		var me = this;
  		me.getLayout().setActiveItem(me.items.items[0]);
  	},
  	
  	showQuaAssessShowGrid : function(){
  		var me = this;
  		me.getLayout().setActiveItem(me.items.items[2]);
  	},
  	
  	getQuaAssessGridRiskDatas : function(){
  		var me = this;
    	return me.quaAssessGrid.riskDatas;
    },
  	
    getAssessPlanNaem : function(){
    	var me = this;
    	
    	FHD.ajax({
            url: 'findAssessPlanName.f',
            callback: function (data) {
                if (data && data.success) {
                	me.assessPlanName = data.assessPlanName;
                }
                
            }
        });
    },
    
    initComponent: function () {
        var me = this;
        
        debugger;
        me.id = 'quaAssessCardId';
        me.quaAssessOpe = Ext.widget('quaAssessOpe');
        me.quaAssessGrid = Ext.widget('quaAssessGrid',{url:'findAssessGrid.f'});
        me.quaAssessGrid.store.load();
        
//        var searchField = Ext.create('Ext.ux.form.SearchField', {
//			width : 150,
//			paramName:me.searchParamName,
//			store:me.quaAssessGrid.store,
//			emptyText : FHD.locale.get('searchField.emptyText')
//		});
        
        Ext.apply(me, {
        	border:false,
        	activeItem : 0,
            items: [me.quaAssessGrid, me.quaAssessOpe],
			bbar : {
				items : [ '->',
				{
					text : '浏览',
					id : 'showId',
					iconCls : 'icon-operator-submit',
					handler : function() {
						me.quaAssessShowGrid = Ext.widget('quaAssessShowGrid',{url:'findAssessShowGrid.f'});
						me.quaAssessShowGrid.store.load();
						
						me.formwindow = new Ext.Window({
							layout:'fit',
							iconCls: 'icon-edit',//标题前的图片
							modal:true,//是否模态窗口
							collapsible:true,
							width:800,
							height:500,
							maximizable:true,//（是否增加最大化，默认没有）
							constrain:true,
							items : [me.quaAssessShowGrid]
						});
						me.formwindow.show();
					}
				},{
						text : '上一页',
						id : 'upId',
						iconCls : 'icon-operator-submit',
						handler : function() {
							me.quaAssessOpe.oper = 'up';
							if(me.quaAssessOpe.isActivate){
								//问卷上一页
								me.quaAssessOpe.load(me.getQuaAssessGridRiskDatas());
							}
						}
					},{
						text : '下一页',
						iconCls : 'icon-operator-submit',
						id : 'nextId',
						handler : function() {
							me.quaAssessOpe.oper = 'next';
							if(me.quaAssessOpe.isActivate){
								//问卷下一页
								me.quaAssessOpe.load(me.getQuaAssessGridRiskDatas());
							}else{
								//下一步问卷
								me.showQuaAssessOpe();
								me.quaAssessOpe.load(me.getQuaAssessGridRiskDatas());
							}
						}
					},{
						text : '提交',
						iconCls : 'icon-operator-submit',
						handler : function() {
							me.quaAssessSubmit = Ext.widget('quaAssessSubmit',{riskDatas : me.getQuaAssessGridRiskDatas()});
							
							me.formwindow = new Ext.Window({
								layout:'fit',
								iconCls: 'icon-edit',//标题前的图片
								modal:true,//是否模态窗口
								collapsible:true,
								width:800,
								height:500,
								maximizable:true,//（是否增加最大化，默认没有）
								constrain:true,
								items : [me.quaAssessSubmit]
							});
							me.formwindow.show();
							me.quaAssessSubmit.formwindow = me.formwindow;
						}
					}
				]
			}
        });
        
        me.callParent(arguments);
        
        Ext.getCmp('upId').disable();
        
        me.on('resize',function(p){
        	me.setHeight(FHD.getCenterPanelHeight() - 40);
    	});
    }

});
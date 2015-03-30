/**
 * 
 * 风险整理上下面板
 */

Ext.define('FHD.view.risk.assess.quaAssess.QuaAssessSubmit', {
    extend: 'Ext.form.Panel',
    alias: 'widget.quaAssessSubmit',
    
    requires: [
               'FHD.view.risk.assess.quaAssess.QuaAssessShowGrid',
              ],
    
    // 初始化方法
    initComponent: function() {
        var me = this;
        
        me.quaAssessShowGrid = Ext.widget('quaAssessShowGrid',{url:'findAssessShowGrid.f'});
		me.quaAssessShowGrid.store.load();
        
		me.submitPanel = Ext.create('Ext.form.Panel',{
			html:'组件'
			});
		
        Ext.apply(me, {
        	border:false,
        	region:'center',
        	layout:{
                align: 'stretch',
                type: 'vbox'
    		},
            items: [me.quaAssessShowGrid, me.submitPanel],
            buttons: [{
    			text: '提交',
    				handler:function(){
    					FHD.ajax({
    			            url: 'approvalColl.f',//'submitAssess.f',
    			            params: {
    			            	params : Ext.JSON.encode(me.riskDatas)
    			            },
    			            callback: function (data) {
    			                if (data && data.success) {
    			                	alert('提交完成');
    			                	me.formwindow.close();
    			                }
    			            }
    			        });
    				}
    			},{
    				text: '取消',
    				handler:function(){
    					me.formwindow.close();
    				}
    		}],
        });

        me.callParent(arguments);
    }

});
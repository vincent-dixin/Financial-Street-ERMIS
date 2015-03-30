/**
 * 
 * 定性评估操作面板
 */

Ext.define('FHD.view.risk.assess.quaAssess.QuaAssessOpe', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.quaAssessOpe',
    
    requires: [
				'FHD.view.risk.assess.quaAssess.QuaAssessEdit'
              ],
    
    page : 0,
    count : 0,
    showCount : 5,
    isActivate : false,
    oper : '',    
    
    operFun : function (riskDatas){
    	var me = this;
    	
    	me.isActivate = true; 
    	
    	if(me.oper == 'up'){
    		me.count = me.count - me.showCount;
    		me.page--;
    	}if(me.oper == 'next'){
    		me.count = me.count + me.showCount;
			me.page++;
    	}
    	
    	if(me.page == 1){
    		me.count = 0;	
    	}
    	
    	if(me.count < 0){
    		Ext.getCmp('quaAssessPanelId').infoNav.load(5, me.page + 1, riskDatas);
    		Ext.getCmp('quaAssessCardId').showQuaAssessGrid();
    		me.isActivate = false;
    		Ext.getCmp('upId').disable();
    		Ext.getCmp('nextId').enable();
    		return false;
    	}
    	Ext.getCmp('upId').enable();
    	return true;
    },
    
    load : function(riskDatas) {
		var me = this;
		
		if(me.items.length !=0){
			me.removeAll();
		}
		
		me.body.mask("读取中...","x-mask-loading");
		
		FHD.ajax({
            url: 'assessFindRiskInfoByIds.f',
            params: {
            	params : Ext.JSON.encode(riskDatas)
            },
            callback: function (data) {
                if (data && data.success) {
                	
                	if(!me.operFun(riskDatas)){
                		return;
                	}
                	
                	Ext.getCmp('quaAssessPanelId').infoNav.load(5, me.page + 1, riskDatas);
                	
                	for(var i = me.count; i < me.showCount * me.page; i++){
                		if(data.data[i] != null){
	                		me.datas = {
	                        		'parentRiskName' : data.data[i].parentRiskName,
	                        		'riskName' : data.data[i].riskName,
	                        		'code' : data.data[i].code,
	                        		'respDeptName' : data.data[i].respDeptName,
	                        		'relaDeptName' : data.data[i].relaDeptName,
	                        		'influKpiName' : data.data[i].influKpiName,
	                        		'influProcessureName' : data.data[i].influProcessureName
	                        	};
	                		
	                		me.fieldSetShow = {
	                				xtype : 'fieldset',
	                				width : FHD.getCenterPanelWidth() - 25,
	                				title : '<font size="3">' +(i + 1) + '、' +  me.datas.riskName + '</font>',
	                				defaultType : 'textfield',
	                				margin : '10 40 40 40',
	                				items : [me.quaAssessEdit.getFieldSetShow(me.datas), me.quaAssessEdit.getFieldSetAssess(data.result[i])]
	                			};
	                		
	                		me.add(me.fieldSetShow);
                		}else{
                    		Ext.getCmp('nextId').disable();
                			break;
                		}
                	}
                	
                	me.body.unmask();
                }
                
            }
        });
	},
              
    // 初始化方法
    initComponent: function() {
        var me = this;
        
        me.quaAssessEdit = Ext.widget('quaAssessEdit');
        
        Ext.apply(me, {
        	layout:{
                align: 'stretch',
                type: 'vbox'
    		},
    		autoScroll:true
        });

        me.callParent(arguments);
    }

});
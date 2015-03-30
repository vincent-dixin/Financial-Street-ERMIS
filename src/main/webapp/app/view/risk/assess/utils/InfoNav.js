/**
 * 
 * 操作导航
 */

Ext.define('FHD.view.risk.assess.utils.InfoNav', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.infoNav',
    
    setInfo : function(assessPlanName, isAssessCount, isNotAssessCount, totalCount, pageCount, totalPageCount){
    	var me = this;
    	me.info = '<div>　</div>' +
	     '<div  align="left">　评估计划:' + assessPlanName + '　已评估/未评估:<font color="#00CC66">' + isAssessCount + '</font>/<font color="#FF0000">' + isNotAssessCount + '</font>　　评估总数:' + totalCount + '　　当前页/总页:' + pageCount + '/' + totalPageCount + 　　　
	     '</div>'
    	me.body.update(me.info)
	    return me.info;
    },
    
    load : function(pageSize, pageCount, riskDatas){
    	var me = this;
    	
    	FHD.ajax({
            url: 'findAssessCount.f?pageSize=' + pageSize,
            params: {
            	params : Ext.JSON.encode(riskDatas)
            },
            callback: function (data) {
                if (data && data.success) {
                	var assessPlanName = data.assessPlanName;
                	var isAssessCount = data.isAssessCount;
                	var isNotAssessCount = data.isNotAssessCount;
                	var totalCount = data.totalCount;
                	var totalPageCount = data.totalPageCount;
                	totalPageCount = Number(totalPageCount + 1);
                	
                	me.setInfo(
                			assessPlanName, 
                			isAssessCount, 
                			isNotAssessCount, 
                			totalCount, 
                			pageCount, 
                			totalPageCount
                			);
                }
                
            }
        });
    },
    
    // 初始化方法
    initComponent: function() {
        var me = this;;
        
        Ext.apply(me, {
        	border : false
        });

        me.callParent(arguments);
    }

});
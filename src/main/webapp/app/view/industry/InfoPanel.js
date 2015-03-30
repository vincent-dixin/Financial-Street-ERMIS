/**
 * 
 * 行业主体内容面板
 */

Ext.define('FHD.view.industry.InfoPanel', {
    extend: 'Ext.form.Panel',
    alias: 'widget.infoPanel',

    load : function(id){
    	var me = this;
//    	alert(id);
//    	me.form.load({
//	        url:url,
//	        params:{id:id},
//	        failure:function(form,action) {
//	            alert("err 155");
//	        },
//	        success:function(form,action){
//		        
//	        }
//	    });
    },
    
    // 初始化方法
    initComponent: function() {
        var me = this;
        
        var name = Ext.widget('displayfield', {
            xtype: 'displayfield',
            rows: 1,
            fieldLabel: '行业名称',
            margin: '7 30 3 30', 
            name: 'name',
            columnWidth: .5
        });
        
        var kpiNumber = Ext.widget('displayfield', {
            xtype: 'displayfield',
            rows: 1,
            fieldLabel: '指标总数',
            margin: '7 30 3 30', 
            name: 'number',
            columnWidth: .5
        });
        
        var processNumber = Ext.widget('displayfield', {
            xtype: 'displayfield',
            rows: 1,
            fieldLabel: '流程总数',
            margin: '7 30 3 30', 
            name: 'number',
            columnWidth: .5
        });
        
        var riskNumber = Ext.widget('displayfield', {
            xtype: 'displayfield',
            rows: 1,
            fieldLabel: '风险总数',
            margin: '7 30 3 30', 
            name: 'number',
            columnWidth: .5
        });
        
        var info = {
                xtype:'fieldset',
                title: '基础信息',
                collapsible: true,
                defaultType: 'textfield',
                margin: '5 5 0 5',
                layout: {
         	        type: 'column'
         	    },
         	    items : [name, kpiNumber, processNumber, riskNumber]
        };
        
        me.id = "infoPanelId";
        
        Ext.apply(me, {
        	border:false,
            items: [info],
            url: '<%=request.getContextPath()%>/formsort.do?method=saveOrUpdateFormSort'
        });

        me.callParent(arguments);
    }

});
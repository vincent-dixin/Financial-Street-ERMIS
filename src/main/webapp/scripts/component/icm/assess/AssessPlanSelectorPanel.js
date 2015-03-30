Ext.define('FHD.ux.icm.assess.AssessPlanSelectorPanel', {
    extend: 'Ext.container.Container',
    alias: 'widget.assessplanselectorpanel',
    
    requires: [
       'FHD.ux.icm.assess.AssessPlanSelectorCardPanel'
    ],
    autoScroll:true,
    
    initComponent: function() {
        var me = this;
        
        me.flowImage = Ext.widget('image',{
        	src : __ctxPath + '/images/icm/assess/steps51.jpg',
            width: 850
        });
        
        me.assessplanselectorcardpanel = Ext.widget('assessplanselectorcardpanel',{
        	flex:1,
        	onSubmit:function(paramObj){
        		me.onSubmit(paramObj);
			}
        });

        Ext.applyIf(me, {
        	layout:{
        		align: 'stretch',
    			type: 'vbox'
        	},
    		items:[{
            	xtype:'container',
            	height: 50,
            	style:'border-bottom: 1px  #99bce8 solid !important;',
            	layout:{
            		align: 'stretch',
        			type: 'hbox'
            	},
    		    items:[
    		        {
	    		    	xtype:'image',
		        		src : __ctxPath + '/images/wp/zuo.jpg',
		        		flex:1
		        	},me.flowImage,{
		        		xtype:'image',
		        		src : __ctxPath + '/images/wp/you.jpg',
		        		flex:1
		        	}
		        ]
        	},me.assessplanselectorcardpanel]
        });
    
        me.callParent(arguments);
    },
    onSubmit:Ext.emptyFn(),
    reloadData:function(){
    	var me=this;
    	
    }
});
Ext.define('FHD.view.demo.TestMainPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.myPanel',
    
    /**
     * 初始化页面组件
     */
    initComponent: function () {
        var me = this;
    	
        //人员
        var p1 = Ext.create('Ext.panel.Panel',{
        	title:'第一步',
        	html:'aa',
        	border:'0 0 0 0',
        	bodyStyle: {
			    background: '#ffc',
			    //padding: '10px'
			},
        	last:function(){
        		alert('save1');
        	}
        });
        var p2 = Ext.create('Ext.panel.Panel',{
        	title:'第二步',
        	html:'bb',
        	border:'0 0 0 0',
        	last:function(){
        		alert('save2');
        	}
        });
        var p3 = Ext.create('Ext.panel.Panel',{
        	title:'第三步',
        	html:'cc',
        	last:function(){
        		alert('save3');
        	}
        });
        var basicPanel = Ext.create('FHD.ux.layout.NavigatorLayout',{
        	items:[p1,p2,p3],
        	undo : function(){
        		alert('返回');
        	}
        });

        Ext.applyIf(me, {
            autoScroll: true,
            border: false,
            bodyPadding: "5 5 5 5",
            items: [basicPanel]
            
        });
        
        me.callParent(arguments);
      
    }
});
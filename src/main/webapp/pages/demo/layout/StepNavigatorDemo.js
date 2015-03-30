Ext.define('FHD.demo.layout.StepNavigatorDemo', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.stepnavigatordemo',
    
    /**
     * 初始化页面组件
     */
    initComponent: function () {
        var me = this;
    
        var p1 = Ext.create('Ext.panel.Panel',{
        	navigatorTitle:'基本信息',	//步骤导航的名称，必须
        	border: false,
        	html:'aa',
        	//height: FHD.getCenterPanelHeight()-50,
        	last:function(){
        		alert('save1');
        	}
        });
        var p2 = Ext.create('Ext.panel.Panel',{
        	navigatorTitle:'额外信息',
        	border: false,
        	html:'bb',
        	last:function(){
        		alert('save2');
        	}
        });
        var p3 = Ext.create('Ext.panel.Panel',{
        	navigatorTitle:'更多信息',
        	border: false,
        	html:'cc',
        	last:function(){
        		alert('save3');
        	}
        });

        var basicPanel = Ext.create('FHD.ux.layout.StepNavigator',{
        	//hiddenTop:true,
        	//hiddenBottom:true,
        	//hiddenUndo:true,
        	items:[p1,p2,p3],
        	btns:[{
        		text:'自定义',
        		handler:function(){
        			alert();
        		}
        	}],
        	undo : function(){
        		alert('返回');
        	}
        });

        Ext.applyIf(me, {
            autoScroll: true,
            border: false,
            items: [basicPanel]
            
        });
        
        me.callParent(arguments);
      
    }
});
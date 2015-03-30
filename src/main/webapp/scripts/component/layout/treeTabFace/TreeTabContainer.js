Ext.define('FHD.ux.layout.treeTabFace.TreeTabContainer',{
    extend: 'Ext.panel.Panel',
    alias: 'widget.treetabcontainer',
    requires: [
               
              ],
              
    /**
	 * public
	 * 接口属性
	 */
    tabpanel:null,   	//内部放的tabpanel

    initComponent: function () {
        var me = this;

        me.navigationBar = Ext.create('Ext.scripts.component.NavigationBars');
        
        Ext.apply(me, {
        	border : true,
        	layout:{
                align: 'stretch',
                type: 'vbox'
    		},
    		items:[
	    		Ext.create('Ext.panel.Panel',{
					border : false,
					html:'<div id="'+me.id+'DIV" class="navigation" ></div>',
		            listeners : {
		            	afterrender: function(){
		            		me.navigationBar.renderHtml(me.id +　'DIV', 'eda8ffeab0da4159be0ff924108e3883MB0011', '', 'sm');
	
		            	}
		            }
	    		}),me.tabpanel
    		]
        });
        me.callParent(arguments);
    }

});

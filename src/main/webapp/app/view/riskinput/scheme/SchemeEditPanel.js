/**
 * 
 * 风险上报标准
 * 使用card布局
 * 
 * 下级有两个组件 潜在风险上报标准、历史风险上报标准
 * 
 * @author 宋佳
 */
Ext.define('FHD.view.riskinput.scheme.SchemeEditPanel', {
    extend: 'Ext.form.Panel',
    alias: 'widget.schemeeditpanel',
    requires: [
    	'FHD.view.riskinput.form.SchemeForm'
    ],
    border:false,
	autoHeight: true,
	autoScroll:true,
//	layout: {
//                type : 'column'
//            },
//            defaults : {
//                columnWidth : 1 / 1
//            },
    bodyPadding:'10 60 3 60',
    initParam : function(paramObj){
         var me = this;
    	 me.paramObj = paramObj;
	},
	initComponent: function() {
		/*定性 qualitative  定量quantification*/
		var me = this;
		
		me.bbar=['->',{
        		iconCls : 'icon-control-play-blue',
        		text:'保存',
        		handler: function(){
            	   	var schemeeditcardpanel = me.up('schemeeditcardpanel');
            	   	if(schemeeditcardpanel==undefined){
            	   		var riskeventeditcardpanel = me.up('riskeventeditcardpanel');
            	   		riskeventeditcardpanel.navBtnHandler(0);
            	   	}else{
            	   		schemeeditcardpanel.navBtnHandler(0);
            	   	}
    				
                }
        	},'-',{
        		iconCls : 'icon-control-fastforward-blue',
        		text:'提交',
        		handler: me.onSubmit
        }]
		//定性
        me.schemeformContainer = Ext.widget('schemeform');
        me.addBtn = Ext.widget('label',{
        	margin: '0 10 0 20',
			html : "<a href='javascript:void(0)' onclick='Ext.getCmp(\""+me.id+"\").addDx()'>增加</a>"
        });
        Ext.apply(me, {
            items: [
               me.schemeformContainer,
               me.addBtn
              
            ]
        },me.bbar);

        me.callParent(arguments);
    },
    addDx : function(){
    	var schemeeditpanel = this;
    	var schemeformContainer = Ext.widget('schemeform');
    	schemeeditpanel.insert(schemeeditpanel.items.length-1,schemeformContainer);
    	schemeeditpanel.doLayout();
    	}
});
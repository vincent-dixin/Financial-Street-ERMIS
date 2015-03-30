Ext.define('FHD.view.riskinput.SelectRiskPanel', {
    extend: 'Ext.Window',
    alias: 'widget.selectriskpanel',
	autoScroll:true,
	requires: [
 	 	'FHD.ux.risk.RiskStrategyMapSelector'
    ],
    initParam : function(paramObj){
         var me = this;
    	 me.paramObj = paramObj;
	},
	border:false,
    initComponent: function() {
        var me = this;
       
        var formButtons = [ //表单按钮
            {
                text: FHD.locale.get('fhd.common.confirm'),
                handler: function(){
                	var riskeventeditcardpanel = me.pr.up('riskeventeditcardpanel');
                	if(riskeventeditcardpanel!=undefined){
	               		riskeventeditcardpanel.initParam({
			    		editFlag : false,
			    		businessId : ''
			    		});
			    		riskeventeditcardpanel.reloadData();
	    				riskeventeditcardpanel.navBtnHandler(1);
	    				me.close();
                	}
                	var eventeditcardpanel = me.pr.up('eventeditcardpanel');
                	if(eventeditcardpanel!=undefined){
	               		eventeditcardpanel.initParam({
			    		editFlag : false,
			    		businessId : ''
			    		});
			    		eventeditcardpanel.reloadData();
	    				eventeditcardpanel.navBtnHandler(1);
	    				me.close();
                	}
               		
                }
                
            }, {
                text: FHD.locale.get('fhd.common.cancel'),
                handler: function () {
                	
                }
            }
        ];
 		var describe = {
            xtype : 'textareafield',
            fieldLabel:'描述',
            labelAlign : 'left',
            margin: '7 10 0 30',
            row : 5,
            columnWidth : 1,
            name : 'requirement',
            labelWidth : 80
        };
        
        var risk=Ext.widget('riskstrategymapselector', {
		    margin: '7 30 0 0',
		    name:'kpiStrategyMapIds',
		    single:false,
		    height :40,
		    value:'3,4',
		    labelText:'风险',
		    labelAlign: 'right',
		    labelWidth: 80
		});
 

        //
        var basicfieldSet = Ext.widget('fieldset', {
            xtype: 'fieldset', 
            collapsible: true,
            autoHeight: true,
            autoWidth: true,
            layout: {
                type: 'auto'
            },
            title: FHD.locale.get('fhd.kpi.kpi.form.basicinfo'),
            items: [risk]
        });

       Ext.applyIf(me, {
        	buttons: formButtons,
        	items:[basicfieldSet]
        });
    
        me.callParent(arguments);
    },
    save: function () {
    	var me = this;
    	var riskeventeditcardpanel = me.up('riskeventeditcardpanel');
    	riskeventeditcardpanel.initParam({
    		editFlag : false,
    		businessId : ''
    	});
    	riskeventeditpanel.reloadData();
    	riskeventeditpanel.navBtnHandler(1);
    },
    
    reloadData:function(){
    	var me=this;
    	
    }
});
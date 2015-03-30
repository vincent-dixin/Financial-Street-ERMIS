Ext.define('FHD.view.bpm.processinstance.ProcessInstanceTabInfo', {
    extend: 'Ext.panel.Panel',
	alias: 'widget.ProcessInstanceTabInfo',
	
	title:"工作流日志",
	jbpmHistProcinstId:"",
	width:'auto',
    model:"show",
    processInstanceFieldSet:null,
	jbpmHistProcinstGrid:null,
    resize:function(me){
		if(me.jbpmHistProcinstGrid.getHeight()!=me.getHeight()-me.processInstanceFieldSet.getHeight()-10){
	    	me.jbpmHistProcinstGrid.setHeight(me.getHeight()-me.processInstanceFieldSet.getHeight()-10);
		}
    },
    
    initComponent: function() {
        var me = this;
        var JbpmHistProcinstForm=Ext.create("FHD.view.bpm.processinstance.JbpmHistProcinstForm",{
        	jbpmHistProcinstId:me.jbpmHistProcinstId,
        	model:me.model
        });
        me.processInstanceFieldSet=Ext.create('Ext.form.FieldSet',{
			layout:'fit',
			title:'流程实例',
			margin:'5 5 5 5',
			items:[
				JbpmHistProcinstForm
			]
		});
		me.jbpmHistProcinstGrid=Ext.create('FHD.view.bpm.processinstance.JbpmHistActinstPage',{
			jbpmHistProcinstId:me.jbpmHistProcinstId,
			model:me.model
		});
        Ext.applyIf(me, {
            items: [
            	me.processInstanceFieldSet,
            	me.jbpmHistProcinstGrid
            ],
           	listeners:{
           		resize:function(me,width,height,oldWidth,oldHeight){
           			me.resize(me);
           		},
           		afterlayout:function(me){
           			me.resize(me);
           		}
           	}
        });
        me.callParent(arguments);
		
    }
});
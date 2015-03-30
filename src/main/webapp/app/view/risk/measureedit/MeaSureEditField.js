Ext.define('FHD.view.risk.measureedit.MeaSureEditField', {
	extend: 'Ext.form.FieldSet',
	alias: 'widget.measureeditfield',
    layout: {
        type: 'column'
    },
    defaults :{
    	margin: '7 30 3 30',
    	cloumnsWidth : .5
    },
	frame: false,
	autoHeight : true,
	checkboxToggle: true,
	title : '控制措施',
    initComponent: function() {
        var me = this;
		me.name = Ext.widget('textfield',{
			name : 'name',
			value : '',
			fieldLabel : '名称',
			columnWidth : 1
		});
		//流程选择组建
        me.processSelector = Ext.create('FHD.ux.process.processSelector', {
		 	labelWidth: 95,
		 	gridHeight:25,
		 	btnHeight:25,
		 	btnWidth:25,
		 	single : false,
		 	fieldLabel: '选择流程',
            margin: '7 30 3 30', 
            name: 'process',
            multiSelect:false,
            columnWidth : .5
        });
		//流程选择组建
        me.radiofield1 = Ext.widget('radiofield', {
             boxLabel: '日常持续执行',
             name : 'frequntly',
             columnWidth : .85
        });
		var blankLabel = {
			xtype : 'hiddenfield',
			columnWidth : .15
		}
        //流程选择组建
        me.radiofield2 = Ext.widget('radiofield', {
             boxLabel: '定期执行',
             name : 'frequntly',
             columnWidth : .2	
        });
        me.regExecuted = Ext.widget('textfield',{
        	name : 'regExecuted',
        	columnWidth : .65
        });
		//流程选择组建
        me.radiofield3 = Ext.widget('radiofield', {
		 	 fieldLabel: '',
             boxLabel: '按条件启动',
             name : 'frequntly',
             columnWidth : .2
        });
        me.conExecuted = Ext.widget('textfield',{
        	name : 'conExecuted',
        	columnWidth : .65
        });
        me.processNote = new Ext.form.field.ComboBox({
			fieldLabel : '对应的流程节点',
			name : 'processNote',
//			store :me.processStore,
			valueField : 'id',
			displayField : 'name',
			allowBlank : false,
			multiSelect : true,
			editable : false,
			columnWidth : .5,
			listeners: {
				collapse: function(){
				if(this.value!=''){
					this.value = this.value.toString();
					}
				}
 			}
		});
		//流程节点选择组建
		me.desc = Ext.widget('textareafield', {
			height:60,
			rows : 3,
			fieldLabel : '控制措施内容',
			value : '',
			allowBlank: false,
			name : 'meaSureDesc',
			columnWidth : 1
        });
    	me.quantificationContainer = Ext.widget('quantification');
        me.addQuantitative = Ext.widget('label',{
			html : "<a href='javascript:void(0)' onclick='Ext.getCmp(\""+me.id+"\").addDx()'>增加</a>"
        });
    	me.fieldsetQuantification = Ext.widget('fieldset', {
            layout : {
                type: 'vbox',
                algin: 'stretch'
            },
            border : false,
            columnWidth : .85,
            items : [me.quantificationContainer,me.addQuantitative]
        });
        //责任人
		me.noteradio = Ext.create('FHD.ux.org.CommonSelector', {
			fieldLabel : '责任人',
			labelAlign : 'left',
			name:'meaSureempId',
			type : 'emp',
			allowBlank: false,
			multiSelect : false,
			value : '',
			columnWidth : .5
		});
        Ext.applyIf(me, {
            items: [
                me.name,
                me.processSelector,
                me.processNote,
               	me.desc,
               	{
					xtype : 'label',
					text : '频率:',
					columnWidth : .15
				},me.radiofield1,
               	{
					xtype : 'hiddenfield',
					columnWidth : .15
               	},me.radiofield2,me.regExecuted,
               	{
					xtype : 'hiddenfield',
					columnWidth : .15
				},me.radiofield3,me.conExecuted,
				{
					xtype : 'hiddenfield',
					columnWidth : .15
				},me.fieldsetQuantification,
				me.noteradio
            ]
        });

        me.callParent(arguments);
    }

});
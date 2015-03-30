/**
 * 
 * 风险上报标准
 * 使用card布局
 * 
 * 下级有两个组件 潜在风险上报标准、历史风险上报标准
 * 
 * @author 宋佳
 */
Ext.define('FHD.view.risk.effective.QuantificationEffectFirstLevel', {
    extend: 'Ext.form.FieldContainer',
    alias: 'widget.quantificationeffectfirstlevel',
    requires: [
    	'FHD.ux.kpi.KpiSelector'
    ],
    autoHeight: true,
    autoWidth : true,
	layout: {
        type: 'table',
        columns : 6
    },
    kpiname : '',
	initComponent: function() {
		/*定性 qualitative  定量quantification*/
		var me = this;
		//定性
		//定量
		me.quantification = Ext.widget('kpiselector', {
			name : 'quantification', 
			value: me.kpiname,
			labelText : '指标',
			width:300,
			colspan : 2
		});
        me.delQuantification = Ext.widget('label',{
			html : "<a href='javascript:void(0)' onclick='Ext.getCmp(\""+me.id+"\").delSelf()'>删除</a>",
			colspan : 2
        });
        var redLabel = {
			margin: '7 30 3 30',
			xtype : 'label',
			text : '红',
			colspan : 1
        };  
        var yellowLabel = {
        	margin: '7 30 3 30',
			xtype : 'label',
			text : '黄',
			colspan : 1
        };  
        var greenLabel = {
        	margin: '7 30 3 30',
			xtype : 'label',
			text : '绿',
			colspan : 1
        };
        var formula1 = {
        	margin: '7 30 3 30',
        	xtype : 'label',
			text : '0<=x<1000',
			colspan : 1
        };
        var formula2 = {
        	margin: '7 30 3 30',
        	xtype : 'label',
			text : '1000 <= x < 2000',
			colspan : 1
        };
        var formula3 = {
        	margin: '7 30 3 30',
        	xtype : 'label',
			text : '2000 <= x < 5000',
			colspan : 1
        };
        me.num = Ext.widget('textfield',{
        	fieldLabel : '发生次数',
        	value : '',
        	labelWidth : 50,
        	name : 'num',
        	width : 100,
        	emptyText : '数量',
        	colspan : 2
        });
        me.num1 = Ext.widget('textfield',{
        	fieldLabel : '发生次数',
        	value : '',
        	labelWidth : 50,
        	name : 'num',
        	width : 100,
        	emptyText : '数量',
        	colspan : 2
        });
        me.num2 = Ext.widget('textfield',{
        	fieldLabel : '发生次数',
        	value : '',
        	labelWidth : 50,
        	name : 'num',
        	width : 100,
        	emptyText : '数量',
        	colspan : 2
        });
        me.latitudeTimeStore = Ext.create('Ext.data.Store',{
			fields : ['id', 'name'],
			data : [
				{'id' : 0,'name' : '过去一周'},
				{'id' : 1,'name' : '过去一月'},
				{'id' : 2,'name' : '过去一季'},
				{'id' : 3,'name' : '过去半年'},
				{'id' : 4,'name' : '过去一年'}
			]
		});
		me.latitudeTime= Ext.create('Ext.form.ComboBox',{
			fieldLabel: '时间纬度',
			labelWidth : 50,
			store :me.latitudeTimeStore,
			emptyText:'请选择',
			valueField : 'id',
			name:'latitudeTime',
			displayField : 'name',
			colspan : 2
		});
		me.latitudeTime1= Ext.create('Ext.form.ComboBox',{
			fieldLabel: '时间纬度',
			labelWidth : 50,
			store :me.latitudeTimeStore,
			emptyText:'请选择',
			valueField : 'id',
			name:'latitudeTime',
			displayField : 'name',
			colspan : 2
		});
		me.latitudeTime2= Ext.create('Ext.form.ComboBox',{
			fieldLabel: '时间纬度',
			labelWidth : 50,
			store :me.latitudeTimeStore,
			emptyText:'请选择',
			valueField : 'id',
			name:'latitudeTime',
			displayField : 'name',
			colspan : 2
		});
        Ext.apply(me, {
            items : [
            	me.quantification,
            	{
                    xtype: 'hiddenfield',
                    width: 125,
                    fieldLabel: 'Label',
                    colspan : 4
                },
            	{
                    xtype: 'hiddenfield',
                    width: 125,
                    fieldLabel: 'Label',
                    colspan : 1
                },redLabel,formula1,
                me.num,me.latitudeTime,
                {
                    xtype: 'hiddenfield',
                    width: 125,
                    fieldLabel: 'Label',
                    colspan : 1
                },yellowLabel,formula2,
                me.num1,me.latitudeTime1,
                {
                    xtype: 'hiddenfield',
                    width: 125,
                    fieldLabel: 'Label',
                    colspan : 1
                },greenLabel,formula3,
                me.num2,me.latitudeTime2,
            	]
        });
        me.callParent(arguments);
    },
    delSelf : function(){
    	var self = this.up('fieldcontainer');
    	upPanel = self.up('fieldset');
    	upPanel.remove(self);
    }
});
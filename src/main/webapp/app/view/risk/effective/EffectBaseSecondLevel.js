/**
 * 
 * 风险上报标准
 * 使用card布局
 * 
 * 下级有两个组件 潜在风险上报标准、历史风险上报标准
 * 
 * @author 宋佳
 */
Ext.define('FHD.view.risk.effective.EffectBaseSecondLevel', {
    extend: 'Ext.form.FieldContainer',
    alias: 'widget.effectbasesecondlevel',
    requires: [
    ],
	autoHeight: true,
	autoWidth : true,
	layout: {
        columns: 10,
        type: 'table'
    },
	initComponent: function() {
		//定量
		var me = this;
		//定量fieldset
        me.levelStandard = Ext.widget('textfield',{
        	value : '',
        	name : 'levelStandard',
        	emptyText : '分类标准',
        	width : 200
        });
        me.desc = Ext.widget('textfield',{
        	value : '',
        	name : 'desc',
        	emptyText : '描述',
        	width : 200
        });
        me.num = Ext.widget('textfield',{
        	fieldLabel : '发生次数',
        	value : '',
        	labelWidth : 50,
        	name : 'num',
        	width : 100,
        	emptyText : '数量'
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
			displayField : 'name'
		});
        Ext.apply(me, {
            items: [
               {
               		xtype : 'image',
               		src : __ctxPath+'/images/makegrid.png'
               },
               me.levelStandard,
               me.desc,
               me.num,
               me.delMySelfBtn,
               me.latitudeTime
            ]
        });
        me.callParent(arguments);
    },
    delMySelf : function(){
    	this.up('fieldcontainer').up('fieldcontainer').remove(this.up('fieldcontainer'));
    }
});
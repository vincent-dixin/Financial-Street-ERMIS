Ext.define('FHD.ux.collection.CollectionSelector', {
	extend : 'Ext.form.field.Picker',
	alias : 'widget.collectionSelector',
	triggerCls: Ext.baseCSSPrefix + 'form-search-trigger',
	valueCron : '',//CRON定时任务规则
	valueDictType : '',//字典类型
	valueRadioType : '',//单选项第几个
	parentId : '',//父组件ID
	label: FHD.locale.get('fhd.collectionSelector.labelText'),//标示名称
	selectorWindow:null,//弹出窗口
	locationX : 260,
	locationY : 100,
	
	reset : function(){
		this.valueText = '';//表现在文本框中文字
		this.valueCron = '';//CRON格式定时规则
		this.periodText = '';//每X期间文字
		this.valueDictType = '';//字典类型
		this.parentId = '';//父组件ID
		this.valueRadioType = '';//单选项第几个
		this.parameters = '';//输入参数
	},
	
	getCollectionWindow: function() {
		var me = this;
		me.selectorWindow = Ext.create('FHD.ux.collection.CollectionWindow',{
			parentId : me.parentId,
			valueDictType : me.valueDictType,
			valueRadioType : me.valueRadioType,
			onSubmit:function(values){
				var valuesStr = values.split('---');
				me.setValue(valuesStr[0]);
				me.valueCron = valuesStr[1]; 
				me.valueDictType = valuesStr[2];
				me.valueRadioType = valuesStr[3];
			}
		});
		return me.selectorWindow;
	},
	
	//初始化方法
    initComponent: function() {
        var me = this;
        me.fieldLabel = me.label;
		me.callParent(arguments);
    },
    
    onTriggerClick: function() { 
        var me = this; 
        
        me.getCollectionWindow().showAt(me.locationX, me.locationY);;
    },
    //获得值
    getValue: function() {
        return this.value; 
    }
});


Ext.define('FHD.ux.org.EmpSelectList', { 
	extend: 'Ext.form.ComboBox', 
	alias: 'widget.empselectlist',
	
	
	cascadeDept:'',

	editable: false,
	
	listConfig :{
		resizable :true,
		resizeHandles :'e'
	},
	
	fieldLabel:$locale('fhd.emp'),
	displayField:'empname',
	valueField:'id',
	
	initComponent:function(){
		var me = this;
		
		var store = Ext.create('Ext.data.Store', {
		    pageSize: 1000000,
        	idProperty: 'id',
        	
        	fields:['id', 'empno', 'empname'],
        	proxy: {
        		url: __ctxPath + '/sys/emp/findempsbyorgid/',
		        type: 'ajax',
		        reader: {
		            type : 'json',
		            root : 'datas',
		            totalProperty :'totalCount'
		        }
		    }
		});
		
		Ext.apply(me,{
			store:store
		});
		
		me.callParent(arguments);
		if(me.cascadeDept != ''){
		
			var dept = Ext.getCmp(me.cascadeDept);
			dept.on('change',function(field,nValue,oValue){
				me.setValue('');
				store.proxy.url = __ctxPath + '/sys/emp/findempsbyorgid/' + nValue;
				store.load();
			});
			
		}else{
			store.proxy.url = __ctxPath + '/sys/emp/findempsbyorgid/' + __user.companyId;
			store.load();
		}
	}
});
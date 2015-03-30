
Ext.define('FHD.ux.org.DeptSelectList', { 
	extend: 'FHD.ux.TreeCombox', 
	alias: 'widget.deptselectlist',
	
	
	cascadeCompany:'',
	
	fieldLabel:$locale('fhd.dept'),
	displayField:'text',
	rootVisible:false,
	
	initComponent:function(){
		var me = this;
		if(__user.companyId == 'null') {
        	__user.companyId = 'root';
        	__user.companyName = $locale('depttreepanel.__user.companyName');
        }
		
		var store = Ext.create('Ext.data.TreeStore', {
			fields : ['text', 'id', 'dbid','code','leaf','iconCls','cls'],
    	    proxy: {
    	    	url: __ctxPath + '/sys/org/cmp/depttreeloader.f',
    	        type: 'ajax',
    	        reader: {
    	            type: 'json'
    	        },
    	        extraParams: {
    	        	checkable: false,
    	        	subCompany: false,
    	        	chooseId:''
    	        }
    	    },
    	    root: {
    	        text: __user.companyName,
    	        id: '',
    	        expanded: true
    	    }
    	});
		
		Ext.apply(me,{
			store:store
		});
		
		
		me.callParent(arguments);
		if(me.cascadeCompany != ''){
			
			var company = Ext.getCmp(me.cascadeCompany);
			company.on('change',function(field,nValue,oValue){
				me.setValue('');
				store.setRootNode({id:nValue,expanded: true});
			});
		}else{
			store.setRootNode({id:__user.companyId,expanded: true});
		}
	}
});
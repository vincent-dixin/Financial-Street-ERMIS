Ext.define('FHD.view.demo.TestMainPanel', {
    extend: 'Ext.form.Panel',
    alias: 'widget.formPanel',
    
    /**
     * 初始化页面组件
     */
    initComponent: function () {
        var me = this;

        me.grid = Ext.create('FHD.ux.treeselector.TreeSelectorGrid',{
        	region:'center',
			entityName:'com.fhd.sys.entity.orgstructure.SysOrganization',
    		queryKey:'orgname',
    		parentKey:'parentOrg.id',
    		relationKey:'orgseq',
    		multiSelect:true,
    		mycolumns : [{
				            header: "部门编号",
				            dataIndex: 'orgcode',
				            width: 60
				        },
				        {
				            header: '部门名称',
				            dataIndex: 'orgname',
				            isPrimaryName:true,
				            flex:1,
				            align: 'right'
				        }],
			mydata : [{
						id : 'XD00',
						orgcode : 'XD00',
						orgname : '凤凰公司'
					 },{
						id : 'eda8ffeab0da4159be0ff924108e3883',
						orgcode : 'SSGS01',
						orgname : '上市公司'
					 }]
	    });
	    
	    me.grid2 = Ext.create('FHD.ux.treeselector.TreeSelectorGrid',{
	    	region:'north',
			entityName:'com.fhd.sys.entity.orgstructure.SysOrganization',
    		queryKey:'orgname',
    		parentKey:'parentOrg.id',
    		relationKey:'orgseq',
    		multiSelect:true,
    		mycolumns : [{
				            header: "部门编号",
				            dataIndex: 'orgcode',
				            width: 60
				        },
				        {
				            header: '部门名称',
				            dataIndex: 'orgname',
				            isPrimaryName:true,
				            flex:1,
				            align: 'right'
				        }],
			mydata : [{
						id : 'XD00',
						orgcode : 'XD00',
						orgname : '凤凰公司'
					 },{
						id : 'eda8ffeab0da4159be0ff924108e3883',
						orgcode : 'SSGS01',
						orgname : '上市公司'
					 }]
	    });
 
        //菜单项
    	function save(){
    		var window = Ext.create('FHD.ux.treeselector.TreeSelectorWindow',{
    			title:'地区选择',
    			columns: [{dataIndex: 'title',text: '拼音'},{dataIndex: 'name',text: '名称',isPrimaryName:true}],
    			treeUrl: '/demo/findFristtreeTree.f',
    			listUrl: '/demo/findFristtreeById.f',
				multiSelect:true,
				choose:'1,2',
				onSubmit:function(win){
					var values = win.selectedgrid.store;
					var valueStr = '';
			    	values.each(function(value){
			    		valueStr += value.data.id + ',';
			    	});
			    	if(valueStr!=''){
			    		valueStr = valueStr.substring(0,valueStr.length-1);
			    	}
					alert('你选择的值id串是：'+valueStr);
				}
			}).show();
    		//赋值
    		var selects = [{id:1,title:'liaoning',name:'辽宁省',parentId:null,idSeq:'.1.'},{id:2,title:'beijing',name:'北京市',parentId:null,idSeq:'.2.'}];
    		window.setValue(selects);
    	}
        var tbar =['->',{text : "选择地区",iconCls: 'icon-save', handler:save, scope : me}];
        
        Ext.applyIf(me, {
            autoScroll: true,
            border: false,
            bodyPadding: "5 5 5 5",
            tbar:tbar,
            layout:'border',
            items: [me.grid,me.grid2]
            
        });
        
        me.callParent(arguments);
      
    }
});
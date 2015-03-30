Ext.define('FHD.view.demo.TestMainPanel', {
    extend: 'Ext.form.Panel',
    alias: 'widget.formPanel',
    
    /**
     * 初始化页面组件
     */
    initComponent: function () {
        var me = this;
    	
        //人员
        me.emp = Ext.create('FHD.ux.org.CommonSelector',{
        	fieldLabel : '人员',
        	labelAlign: 'left',
        	type : 'emp',
            multiSelect : false,
            name: 'emp'
        });

        me.empFirst = Ext.create('FHD.ux.treelistselector.TreeListSelector',{
        	title:'请您选择员工',
        	noteField:'code',
			columns: [{dataIndex: 'code',header: '员工编号'},{dataIndex: 'name',header: '姓名',isPrimaryName:true}],
			//treeUrl: '/demo/findFristtreeTree.f',
			//listUrl: '/demo/findFirstlistByTreeId',
			//listUrl:'/components/findListByTreeId',
			treeEntityName:'com.fhd.demo.entity.Fristtree',
			entityName:'com.fhd.demo.entity.Firstlist',
			initUrl: '/components/initListByIds',
        	fieldLabel : '组件2',
        	labelAlign: 'left',
            multiSelect : true,
            value:'[{id:2},{id:3}]',
            name: 'empFirst'
        });
        
        //菜单项
    	function save(){
    		var window = Ext.create('FHD.view.demo.OneListSelectorWindow',{
    			title:'省份选择',
    			columns: [{dataIndex: 'idSeq',text: '拼音'},{dataIndex: 'name',text: '名称',isPrimaryName:true}],
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
					//me.grid.store.removeAll();
					//me.setValueFromStore(win.selectedgrid.store);
				}
			}).show();
    		//赋值
    		var selects = [{id:1,title:'liaoning',name:'辽宁省',parentId:null,idSeq:'.1.'},{id:2,title:'beijing',name:'北京市',parentId:null,idSeq:'.2.'}];
    		window.setValue(selects);
    	}
        var tbar =['->',{text : "弹出window",iconCls: 'icon-save', handler:save, scope : me}];
        
        Ext.applyIf(me, {
            autoScroll: true,
            border: false,
            bodyPadding: "5 5 5 5",
            tbar:tbar,
            items: [{
                xtype: 'fieldset',//基本信息fieldset
                collapsible: true,
                defaults: {
                	margin: '7 30 3 30',
                	columnWidth:.5
                },
                layout: {
                    type: 'column'
                },
                title: "基础信息",
                items:[me.emp,me.empFirst]
            }],
            
        });
        
        me.callParent(arguments);
      
    }
});
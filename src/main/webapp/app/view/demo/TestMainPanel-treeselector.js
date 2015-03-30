Ext.define('FHD.view.demo.TestMainPanel', {
    extend: 'Ext.form.Panel',
    alias: 'widget.formPanel',
    
    /**
     * 初始化页面组件
     */
    initComponent: function () {
        var me = this;

        me.region = Ext.create('FHD.ux.treeselector.TreeSelector',{
//        	title:'请您选择地区',
//        	noteField:'title',//orgcode
//			columns: [{dataIndex: 'title',header: '编号'},{dataIndex: 'name',header: '名称',isPrimaryName:true}],
////			treeUrl: '/demo/findFristtreeTree.f',
////			initUrl: '/demo/findFristtreeByIds.f',
//			entityName:'com.fhd.demo.entity.Fristtree',
//			value:'[{id:3},{id:4}]',
//        	fieldLabel : '地区',n bhn bbb  
        	title:'请您选择部门',
        	noteField:'orgcode',
			columns: [{dataIndex: 'orgcode',header: '部门编号'},{dataIndex: 'name',header: '名称',isPrimaryName:true}],
			treeUrl:'/components/treeLoader',
			initUrl:'/components/initByIds',
			value:'[{id:"XD00"},{id:"eda8ffeab0da4159be0ff924108e3883"}]',
        	fieldLabel : '部门',
        	entityName:'com.fhd.sys.entity.orgstructure.Organization',
        	labelAlign: 'left',
            multiSelect : true,
            
            name: 'deptFirst'
        });
 
//        me.region2 = Ext.create('FHD.ux.treeselector.TreeSelector',{
//        	title:'请您选择地区',
//        	noteField:'title',
//			columns: [{dataIndex: 'title',header: '编号'},{dataIndex: 'name',header: '名称',isPrimaryName:true}],
//			value:'[{id:3},{id:4}]',
//        	fieldLabel : '地区',
//        	labelAlign: 'left',
//            multiSelect : true,
//            treeUrl: '/app/view/demo/tree.json',
//            dtype:'demo',
//            demoValue:[{id:1,title:'liaoning',name:'辽宁省',parentId:null,idSeq:'.1.'},{id:2,title:'beijing',name:'北京市',parentId:null,idSeq:'.2.'}],
//            name: 'deptFirst'
//        });
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
                items:[me.region]
            }]
            
        });
        
        me.callParent(arguments);
      
    }
});
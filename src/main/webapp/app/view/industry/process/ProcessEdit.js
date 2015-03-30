/**
 * 
 * 行业主体内容面板
 */

Ext.define('FHD.view.industry.process.ProcessEdit', {
    extend: 'Ext.form.Panel',
    alias: 'widget.processEdit',
    
    load : function(id){
    	var me = this;
//    	alert(id);
//    	me.form.load({
//	        url:url,
//	        params:{id:id},
//	        failure:function(form,action) {
//	            alert("err 155");
//	        },
//	        success:function(form,action){
//		        
//	        }
//	    });
    },
    
    // 初始化方法
    initComponent: function() {
        var me = this;
        
        var processLevel = Ext.widget('displayfield', {
            xtype: 'displayfield',
            rows: 1,
            fieldLabel: '级别流程',
            margin: '7 30 3 30',
            name: 'kpiLevel',
            columnWidth: .5
        });
        
        var processNumber = Ext.widget('displayfield', {
            xtype: 'displayfield',
            rows: 1,
            fieldLabel: '级别流程数量',
            margin: '7 30 3 30', 
            name: 'name',
            columnWidth: .5
        });
        
        var info = {
            xtype:'fieldset',
            title: '基础信息',
            collapsible: true,
            defaultType: 'textfield',
            margin: '5 5 0 5',
            layout: {
     	        type: 'column'
     	    },
     	    items : [processLevel, processNumber]
        };
        
        
        var number = Ext.widget('textfield', {
            xtype: 'textfield',
            rows: 1,
            fieldLabel: '编号',
            margin: '7 30 3 30',
            name: 'riskNumber',
            columnWidth: .5
        });

        var parent = Ext.widget('textfield', {
            xtype: 'textfield',
            rows: 1,
            fieldLabel: '上级流程',
            margin: '7 30 3 30', 
            name: 'parent',
            columnWidth: .5
        });

        var name = Ext.widget('textareafield', {
            xtype: 'textfield',
            rows: 2,
            fieldLabel: '流程名称',
            margin: '7 30 3 30', 
            name: 'name',
            columnWidth: .5
        });

        var processDescribe = Ext.widget('textareafield', {
            xtype: 'textfield',
            rows: 2,
            fieldLabel: '流程描述',
            margin: '7 30 3 30', 
            name: 'name',
            columnWidth: .5
        });

        var responsibilityDe = Ext.widget('textfield', {
            xtype: 'textfield',
            rows: 1,
            fieldLabel: '责任部门',
            margin: '7 30 3 30', 
            name: 'responsibilityDe',
            columnWidth: .5
        });

        var relatedDe = Ext.widget('textfield', {
            xtype: 'textfield',
            rows: 1,
            fieldLabel: '相关部门',
            margin: '7 30 3 30', 
            name: 'relatedRe',
            columnWidth: .5
        });
        
        var processInfo = {
            xtype:'fieldset',
            title: '流程信息',
            collapsible: true,
            defaultType: 'textfield',
            margin: '5 5 0 5',
            layout: {
     	        type: 'column'
     	    },
     	    items : [number, parent, name, processDescribe, responsibilityDe, relatedDe]
        };
        
//        Ext.create('Ext.data.Store', {
//            storeId:'simpsonsStore',
//            fields:['name', 'email', 'phone'],
//            data:{'items':[
//                { 'name': 'Lisa',  "email":"lisa@simpsons.com",  "phone":"555-111-1224"  },
//                { 'name': 'Bart',  "email":"bart@simpsons.com",  "phone":"555-222-1234" },
//                { 'name': 'Homer', "email":"home@simpsons.com",  "phone":"555-222-1244"  },
//                { 'name': 'Marge', "email":"marge@simpsons.com", "phone":"555-222-1254"  }
//            ]},
//            proxy: {
//                type: 'memory',
//                reader: {
//                    type: 'json',
//                    root: 'items'
//                }
//            }
//        });
//
//        var grid = Ext.create('Ext.grid.Panel', {
//            title: 'Simpsons',
//            store: Ext.data.StoreManager.lookup('simpsonsStore'),
//            columns: [
//                { header: 'Name',  dataIndex: 'name' },
//                { header: 'Email', dataIndex: 'email', flex: 1 },
//                { header: 'Phone', dataIndex: 'phone' }
//            ]
//        });
        
        var grid = Ext.create('FHD.ux.treeselector.TreeSelectorGrid',{
		    entityName:'com.fhd.sys.entity.orgstructure.SysOrganization',
			queryKey:'orgname',
			parentKey:'parentOrg.id',
			relationKey:'orgseq',
			multiSelect:true,
			mycolumns : [{
				header: "部门编号",
				dataIndex: 'orgcode',
			    flex : 1
			},
			{
			    header: '部门名称',
			    dataIndex: 'orgname',
				isPrimaryName:true,
				flex:1
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
        
        var gridFieldSet = {
			 xtype:'fieldset',
	         title: '相关风险',
	         collapsible: true,
	         defaultType: 'textfield',
	         margin: '5 5 0 5',
	         layout: {
	  	        type: 'fit'
	  	     },
	  	     items : [grid]
        };
        
        Ext.apply(me, {
        	autoScroll:true,
        	border:false,
            items : [info, processInfo, gridFieldSet],
            tbar : {
				items : [ 
					'->',{
						text : '返回',
						iconCls : 'icon-arrow-undo',
						handler : function() {
							Ext.getCmp('industryCardId').showIndustryTab();
						}
					},{
						text : '保存',
						iconCls : 'icon-page-save',
						handler : function() {
							alert('保存');
							Ext.getCmp('industryCardId').showIndustryTab();
						}
					} 
				]
			}
        });

        me.callParent(arguments);
        me.on('resize',function(p){
    		me.setHeight(FHD.getCenterPanelHeight() - 20);
    	});
    }

});
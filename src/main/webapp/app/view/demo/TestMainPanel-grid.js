Ext.define('FHD.view.demo.TestMainPanel', {
    extend: 'Ext.form.Panel',
    alias: 'widget.formPanel',
    
    /**
     * 初始化页面组件
     */
    initComponent: function () {
        var me = this;

       var cols = [
		{
			dataIndex:'id',
			hidden:true
		},
        {
            header: "编号",
            dataIndex: 'code',
            sortable: true,
            width: 40
        },
        {
            header: "名称",
            dataIndex: 'name',
            sortable: true,
            width:40,
            flex:1
        }
        ];
        
        var btnAdd = Ext.create('Ext.Button', {
		    tooltip: '添加',
		    text:'添加',
            iconCls: 'icon-add',
		    handler: function() {
		        
		    }
		});
		var btnEdit = Ext.create('Ext.Button', {
		    tooltip: '修改',
		    text:'修改',
            iconCls: 'icon-edit',
		    handler: function() {
		        
		    }
		});
		var btnDel = Ext.create('Ext.Button', {
		    tooltip: '删除',
		    text:'删除',
            iconCls: 'icon-del',
		    handler: function() {
		        
		    }
		});
		var btnExport = Ext.create('Ext.Button', {
		    tooltip: '导出',
		    text:'导出',
            iconCls: 'icon-del',
		    handler: function() {
		        
		    }
		});
		var menu = Ext.create('Ext.menu.Menu', {
            margin: '0 0 10 0',
            items: [{
	            text:'导出1',
	            iconCls: 'icon-del',
			    handler: function() {
			        
			    }
		    },{
	            text:'导出2',
	            iconCls: 'icon-del',
			    handler: function() {
			        
			    }
		    }]
        });
		var btnOpe = Ext.create('Ext.Button', {
		    tooltip: '操作',
		    text:'操作',
            iconCls: 'icon-del',
            menu:menu
		});
        var tbarItems = [btnAdd,'-', btnEdit,'-', btnDel,'-', btnExport,'-', btnOpe];
        
        var grid = Ext.create("FHD.view.component.GridPanel",{
        	region:'center',
        	url : __ctxPath + "/app/view/component/list.json",
            extraParams:{
            	riskId:1
            },
        	cols:cols,
        	tbarItems:tbarItems,
        	btns:[{
        			btype:'add',
        			handler:function(){
        				alert('add');
        			}
    			},{
        			btype:'edit',
        			handler:function(){
        				alert('edit');
        			}
    			},{
        			btype:'delete',
        			handler:function(){
        				alert('del');
        			}
    			},{
        			btype:'export',
        			handler:function(){
        				alert('export');
        			}
    			},{
        			tooltip: '操作1',
				    text:'操作1',
		            iconCls: 'icon-del',
				    handler: function() {
				        
				    }
    			},{
        			tooltip: '操作2',
				    text:'操作2',
		            iconCls: 'icon-del',
				    handler: function() {
				        
				    }
    			}],
        	title:'列表',
		    border: true,
		    checked: true
        });
        
        Ext.applyIf(me,{
        	layout:'border',
        	items:[grid]
        });
        
        me.callParent(arguments);
      
    }
});
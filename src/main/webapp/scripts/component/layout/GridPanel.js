Ext.define('FHD.ux.layout.GridPanel',{
	extend: 'Ext.grid.Panel',
	alias: 'widget.fhdgridpanel',
	
	/**
	 * public
	 * 接口属性
	 */
	url:'',
	checked:true,
	searchable:true,
	searchParamName:'query',
	pagable : true,
	extraParams:{},
	cols:[],
	btns:[],
	/**
	 * public
	 * ext属性
	 */
	
	/**
	 * private
	 * 自定义的属性
	 */
	buttonArray:[],
	/**
	 * public
	 * 接口方法
	 */
	
	/**
	 * private
	 * 自定义的方法
	 */
	/** 创建操作按钮方法*/
	handleButton:function() {// 操作处理方法
		var me = this;
		
		var hasOpeBtn = false;	//是否有操作按钮
		var opeBtns = new Array();//操作按钮数组
		me.btns = me.btns || new Array();
		for ( var i = 0; i < me.btns.length; i++) {// 遍历
			var buttonObj = me.btns[i];

			/** 操作预定义类型处理 */
			if (buttonObj["btype"] == "add") {
				Ext.applyIf(buttonObj,{
					tooltip: '添加',
				    text:'添加',
		            iconCls: 'icon-add',
		            scope:me
				});
				me.buttonArray.push(buttonObj);
				me.buttonArray.push('-');
			} else if (buttonObj["btype"] == "edit") {
				Ext.applyIf(buttonObj,{
					tooltip: '修改',
				    text:'修改',
		            iconCls: 'icon-edit',
		            scope:me
				});
				me.buttonArray.push(buttonObj);
				me.buttonArray.push('-');
			} else if (buttonObj["btype"] == "delete") {
				Ext.applyIf(buttonObj,{
					tooltip: '删除',
				    text:'删除',
		            iconCls: 'icon-del',
		            scope:me
				});
				me.buttonArray.push(buttonObj);
				me.buttonArray.push('-');
			} else if (buttonObj["btype"] == "save") {
				Ext.applyIf(buttonObj,{
					tooltip: '保存',
				    text:'保存',
		            iconCls: 'icon-save',
		            scope:me
				});
				me.buttonArray.push(buttonObj);
				me.buttonArray.push('-');
			} else if (buttonObj["btype"] == "export") {
				Ext.applyIf(buttonObj,{
					tooltip: '导出',
				    text:'导出',
		            iconCls: 'icon-page-white-excel',
		            scope:me
				});
				me.buttonArray.push(buttonObj);
				me.buttonArray.push('-');
			}else if (buttonObj["btype"] == "custom"){
				Ext.applyIf(buttonObj,{
					tooltip: buttonObj.tooltip||'',
				    text:buttonObj.text||'自定义',
		            iconCls: buttonObj.iconCls,
		            scope:me
				});
				me.buttonArray.push(buttonObj);
				me.buttonArray.push('-');
			}else{
				hasOpeBtn = true;
				Ext.applyIf(buttonObj,{
		            iconCls: 'icon-del',
		            scope:me
				});
				opeBtns.push(buttonObj);
			}
		}
		/** 添加操作menu */
		if(hasOpeBtn){
			var opeMenuBtn = Ext.create('Ext.Button', {
			    tooltip: '操作',
			    text:'操作',
	            iconCls: 'icon-cog',
	            menu:opeBtns
			});
			me.buttonArray.push(opeMenuBtn);
		}

	},
	
	/**
	 * private
	 * 构造函数
	 */
	initComponent: function(){
		var me = this;var b

		//处理buttons
		me.buttonArray = [];	//清空，解决重复问题
		me.handleButton();
		
		//处理列Model
		var fields = new Array();
		fields.push('id');
		for(i in me.cols) {
			fields.push(me.cols[i].dataIndex);
		}
		
		var cols = new Array();
		//cols.push({xtype: 'rownumberer',resizable:true});
		cols = cols.concat(me.cols);
		
		//数据源
		var pageSize = FHD.pageSize;
		if(!me.pagable){
			pageSize = 100000;
		}
		me.store = Ext.create('Ext.data.Store',{
			pageSize: pageSize,
        	idProperty: 'id',
        	fields:fields,
        	remoteSort:true,
        	proxy: {
		        type: 'ajax',
		        url:me.url,
		        extraParams:me.extraParams,
		        reader: {
		            type : 'json',
		            root : 'datas',
		            totalProperty :'totalCount'
		        }
		    },
		    autoLoad:true
		});

		// 添加操作工具栏
		if(me.buttonArray.length > 0 || me.searchable) {
			// 头菜单
			me.tbar = Ext.create('Ext.toolbar.Toolbar');
			me.tbar.add(me.buttonArray);
		}
		
		//添加搜索框
		var searchField = Ext.create('Ext.ux.form.SearchField', {
			width : 150,
			paramName:me.searchParamName,
			store:me.store,
			emptyText : FHD.locale.get('searchField.emptyText')
		});
		if(me.searchable){
			me.tbar.add('->',searchField);
		}
		
		
		//添加复选框
		if(me.checked){
			me.selModel = Ext.create('Ext.selection.CheckboxModel');
		}
		
		//添加分页
		if(me.pagable){
			me.bbar = Ext.create('Ext.PagingToolbar', {
	            pageSize: FHD.pageSize,
	            store: me.store,
	            displayInfo: true,
	            xtype: 'pagingtoolbar',
             	displayMsg: $locale('fhd.common.display')+'{0} - {1} '+$locale('fhd.common.count')+','+$locale('fhd.common.totalcount')+' {2}'+$locale('fhd.common.count'),
              	emptyMsg: $locale('fhd.common.norecord'),
				items:[{
					xtype:'combobox',
					store: Ext.create('Ext.data.ArrayStore',{
						fields: ['psize'],
        				data : [[10],[20],[30],[50],[100],[200]]
					}),
					displayField:'psize',
			        typeAhead: true,
			        mode: 'local',
			        width:75,
			        forceSelection: true,
			        triggerAction: 'all',
			        emptyText:$locale('FHDGrid.paging.comb.emptyText'),
			        selectOnFocus:true,
			        listeners:{
			        	select:function(c,r,o){
			        		me.store.pageSize = r[0].data.psize;
			        		me.store.proxy.extraParams.limit = r[0].data.psize;
			        		me.store.load();
			        	}
			        }
				}]
	        });
		}
		
		Ext.apply(me,{
			columns:cols
		});
		me.callParent(arguments);
	}
});
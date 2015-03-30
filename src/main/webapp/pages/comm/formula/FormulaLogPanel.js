Ext.define('pages.comm.formula.FormulaLogPanel', {
	extend: 'Ext.grid.Panel',
    alias: 'widget.formulalogpanel',
    url: '', // kpi列表Url地址
    border: true, // 默认不显示border
    checked: true, // 是否可以选中
    searchable:true,
	searchParamName:'query',
	extraParams:{},
	cols:[],
	tbarItems:[],
	bbarItems:[],
	bbar:null,
	searchField:null,
    pagable: true,
    layout:{
        type: 'fit'
    },
    //autoScroll: false,
    overflowX:'hidden',
    
    initComponent: function () {
        var me = this;
        
        var groupingFeature = Ext.create('Ext.grid.feature.Grouping', {
            groupHeaderTpl: '{columnName}: {name} ({rows.length} Item{[values.rows.length > 1 ? "s" : ""]})',
            //startCollapsed: true,
            hideGroupedHeader: true
        });
        
        me.features = groupingFeature;
        	
        // 头菜单
        var tbar = Ext.create('Ext.toolbar.Toolbar');
        // 底菜单
        me.bbar = Ext.create('Ext.toolbar.Toolbar');
        
        var fields = new Array();
		fields.push('id');
		for(i in me.cols) {
			fields.push(me.cols[i].dataIndex);
		}
		
		var pageSize = FHD.pageSize;
		if(!me.pagable){
			pageSize = 100000;
		}

		// 数据源
		me.store = Ext.create('Ext.data.Store',{
			pageSize: pageSize,
        	idProperty: 'id',
        	fields:fields,
        	remoteSort:true,
        	groupField: 'objectColumn',
            sorters: ['calculateDate','objectName'],
        	proxy: {
		        type: 'ajax',
		        url: me.url,
		        extraParams:me.extraParams,
		        reader: {
		            type : 'json',
		            root : 'datas',
		            totalProperty :'totalCount'
		        }
		    }
		});
		
		var cols = new Array();
		cols.push({xtype: 'rownumberer',resizable:true,flex:1});
		cols = cols.concat(me.cols);
		
		if(me.pagable){
			bbar = Ext.create('Ext.PagingToolbar', {
	            pageSize: FHD.pageSize,
	            store: me.store,
	            displayInfo: true,
				//plugins: Ext.create('Ext.ux.ProgressBarPager', {}),
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
			        		me.store.proxy.extraParams = {limit:r[0].data.psize};
			        		me.store.load();
			        	}
			        }
				}]
	        });
	        Ext.apply(me,{
				columns:cols,
				store:me.store,
				tbar:tbar,
				bbar:bbar
			});
		}else{
			Ext.apply(me,{
				columns:cols,
				store:me.store,
				tbar:tbar
			})
		}
		
		if(me.checked){
			Ext.applyIf(me,{
				selModel:Ext.create('Ext.selection.CheckboxModel')
			});
		}
		
		me.callParent(arguments);
		
		me.store.load({
		    scope: this,
		    callback: function(records, operation, success) {
		    	var groups = me.store.getGroups();
		        for(var i=0;i<groups.length;i++){
		        	if(i==0){
		        		groupingFeature.expand(groups[i].name, true);
		        	}else{
		        		groupingFeature.collapse(groups[i].name, true);
		        	}
		        }
		    }
		});
		searchField = Ext.create('Ext.ux.form.SearchField', {
			width : 150,
			paramName:me.searchParamName,
			store:me.store,
			emptyText : FHD.locale.get('searchField.emptyText')
		});
		
		// 添加头菜单
		tbar.add(me.tbarItems);
		// 添加底菜单
		if(me.pagable){
			bbar.insert(12,'-');
			var i = 13;
			Ext.each(me.bbarItems,function(item,index){
				bbar.insert(i+index,item);
			});
		}
		
		if(me.searchable){
			tbar.add('->',searchField);
		}
    }
});

Ext.define('FHD.ux.GroupGridPanel',{
	extend: 'Ext.grid.Panel',
	alias: 'widget.fhdgroupgrid',
	
	url:'',
	checked:true,
	searchable:true,
	searchParamName:'query',
	pagable : true,
	extraParams:{},
	cols:[],
	tbarItems:[],
	bbarItems:[],
	bbar:null,
	searchField:null,
	storeGroupField:'',
	sorter:[],
	
	initComponent: function(){
		var me = this,tbar,bbar;
		// 添加头菜单
		if(me.tbarItems.length > 0 || me.searchable) {
			// 头菜单
			me.tbar = Ext.create('Ext.toolbar.Toolbar');
			me.tbar.add(me.tbarItems);
			tbar = me.tbar;
		}
		
		/*if(me.bbarItems.length > 0) {
			// 底菜单
			me.bbar = Ext.create('Ext.toolbar.Toolbar');
		}*/
		
		var fields = new Array();
		fields.push('id');
		for(i in me.cols) {
			fields.push(me.cols[i].dataIndex);
		}
		var pageSize = FHD.pageSize;
		if(!me.pagable){
			pageSize = 100000;
		}
		
		if(me.storeGroupField!=''){
			me.features=Ext.create('Ext.grid.feature.Grouping',{
		        groupHeaderTpl: '{columnName}: {"name"} ({rows.length} Item{[values.rows.length > 1 ? "s" : ""]})',
		        hideGroupedHeader: true
		    });
		}
		// 数据源
		me.store = Ext.create('Ext.data.Store',{
			pageSize: pageSize,
        	idProperty: 'id',
        	fields:fields,
        	remoteSort:true,
        	groupField:me.storeGroupField,
        	proxy: {
		        type: 'ajax',
		        url:me.url,
		        extraParams:me.extraParams,
		        reader: {
		            type : 'json',
		            root : 'datas',
		            totalProperty :'totalCount'
		        }
		    }
		});
		if(me.sorter!=null&&me.sorter.length>0){
			var n=me.sorter.length;
			for (var i = 0; i < n; i++) {
				me.store.sorters.add(new Ext.util.Sorter({property:me.sorter[i].property,direction: me.sorter[i].direction}));
			}
		}
		var cols = new Array();
		if(me.storeGroupField==''){
			cols.push({xtype: 'rownumberer',resizable:true});
		}
		cols = cols.concat(me.cols);
		
		
		if(me.pagable){
			me.bbar = Ext.create('Ext.PagingToolbar', {
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
			bbar = me.bbar;
	        Ext.apply(me,{
				columns:cols,
				store:me.store,
				tbar:me.tbar,
				bbar:me.bbar
			});
		}else{
			Ext.apply(me,{
				columns:cols,
				store:me.store,
				tbar:me.tbar
			})
		}
		
		if(me.checked){
			Ext.applyIf(me,{
				selModel:Ext.create('Ext.selection.CheckboxModel')
			});
		}
		
		me.callParent(arguments);
		
		me.store.load();
		searchField = Ext.create('Ext.ux.form.SearchField', {
			width : 150,
			paramName:me.searchParamName,
			store:me.store,
			emptyText : FHD.locale.get('searchField.emptyText')
		});
		
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
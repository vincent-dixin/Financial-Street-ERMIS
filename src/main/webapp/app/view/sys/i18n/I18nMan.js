/**
 * 国际化
 * 
 * @author 金鹏祥
 */
Ext.define('FHD.view.sys.i18n.I18nMan', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.i18nMan',
    
    // 初始化方法
    initComponent: function() {
    	var me = this;
    	var objectType='';
    	var loadable=true;//解决重复load冲突的问题
    	var i18nTree, i18nqueryUrl = 'sys/i18n/findI18nAll.f';
    	var i18nGridQueryUrl = 'sys/i18n/findI18nPage.f';
    	me.i18nSaveUrl = 'sys/i18n/saveI18n.f';
    	me.i18nDelUrl = 'sys/i18n/deleteI18n.f';
    	var i18nFormPanel;
    	var i18nManPanel;
    	
    	Ext.define('Test', {//定义model
    	    extend: 'Ext.data.Model',
    	   	fields: [{name: 'id', type: 'string'},
    		        {name: 'objectType', type: 'string'},
    		        {name: 'objectCn', type:'string'},
    		        {name: 'objectEn', type:'string'}]
    	});
    	
    	i18nTree = Ext.create('FHD.ux.TreePanel',{
    		useArrows: true,
            rootVisible: false,
            split: true,
            width:220,
            collapsible : true,
            region: 'west',
            multiSelect: true,
            rowLines:false,
            singleExpand: false,
            checked: false,
    		url: i18nqueryUrl,//调用后台url
    		height:FHD.getCenterPanelHeight(),
    		listeners : {
     			'itemclick' : function(view,re){
    				var form = i18nFormPanel.getForm();
    				me.objectType = re.data.id;
    				me.i18nGrid.store.url = i18nGridQueryUrl + "?objectType=" + re.data.id;
    				me.i18nGrid.down('searchfield').setValue("");
    				me.i18nGrid.down('searchfield').triggerCell.item(0).setDisplayed(false);
    				me.i18nGrid.down('searchfield').onTrigger1Click();
    	  			me.i18nGrid.store.load();
      			}
     		}
    	});
    	
    	me.i18nGrid = Ext.create('FHD.ux.EditorGridPanel',{//创建可编辑的grid列表，注释同不可编辑的gird
    		url: i18nGridQueryUrl,
    		cols:[
    			{header: '所属模块', dataIndex: 'objectType', sortable: true, flex : 1,editor: {allowBlank: false}},
    			{header: '关键值', dataIndex: 'objectKey', sortable: true, flex : 1,editor: {allowBlank: false}},
    			{header: '中文', dataIndex: 'objectCn', sortable: true, flex : 1,editor:{}},
    			{header: '英文', dataIndex: 'objectEn', sortable: true, flex : 1,editor:{}}
    			],
    		tbarItems:[
    				{text : FHD.locale.get('fhd.common.add'),iconCls: 'icon-add', handler:function(){me.add2(me)}, scope : this},{xtype : 'tbspacer'},
    				{text : FHD.locale.get('fhd.common.save'),iconCls: 'icon-save',id:'i18nManSaveId',handler:function(){me.save(me)}, disabled : true, scope : this},{xtype : 'tbspacer'},
    				{text : FHD.locale.get('fhd.common.delete'),iconCls: 'icon-del',id:'i18nMaDelId', handler:function(){me.del(me)}, disabled : true, scope : this}  
    			]
    	});
    	
    	me.i18nGrid.on('selectionchange',function(){me.onchange(me)});//选择记录发生改变时改变按钮可用状态

    	me.i18nGrid.store.on('beforeload', function (store, options) {
    		var new_params = {objectType:me.objectType};
    	    Ext.apply(me.i18nGrid.store.proxy.extraParams, new_params);
        });

        i18nFormPanel = Ext.create('Ext.form.Panel',{
        	region:'center',
        	layout: {
    	        type: 'fit'
    	    },
        	items: [ 
    			me.i18nGrid
        	]
    	});
    	
        Ext.apply(me, {
            height:FHD.getCenterPanelHeight(),
            border:false,
    		layout: {
    	        type: 'border',
    	        padding: '0 0 5	0'
    	    },
    	    defaults: {
                border:true
            },
    	    items:[i18nTree,i18nFormPanel]
        });

        me.callParent(arguments);
	},
	
	add2: function(me){//新增方法
		var r = Ext.create('Test');
		if(me.objectType == 'quanbu_1'){
			me.objectType = '';
		}
		r.data.objectType = me.objectType;
		me.i18nGrid.store.insert(0, r);
		me.i18nGrid.editingPlugin.startEditByPosition({row:0,column:0});
	},
	
	save : function(me){//保存方法
		var rows = me.i18nGrid.store.getModifiedRecords();
		var jsonArray=[];
		Ext.each(rows,function(item){
			jsonArray.push(item.data);
		});
		FHD.ajax({
			url : me.i18nSaveUrl,
			params : {
				modifiedRecord:Ext.encode(jsonArray)
			},
			callback : function(data){
				if(data){
					Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
					me.i18nGrid.store.load();
				}
			}
		})
		me.i18nGrid.store.commitChanges();
	},
	
	del : function(me){//删除方法
		var selection = me.i18nGrid.getSelectionModel().getSelection();
		Ext.MessageBox.show({
			title : FHD.locale.get('fhd.common.delete'),
			width : 260,
			msg : FHD.locale.get('fhd.common.makeSureDelete'),
			buttons : Ext.MessageBox.YESNO,
			icon : Ext.MessageBox.QUESTION,
			fn : function(btn) {
				if (btn == 'yes') {
					var ids = [];
					for(var i=0;i<selection.length;i++){
						ids.push(selection[i].get('id'));
					}
					FHD.ajax({
						url : me.i18nDelUrl,
						params : {
							ids:ids.join(',')
						},
						callback : function(data){
							if(data){
								Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
								me.i18nGrid.store.load();
							}
						}
					});
				}
			} 
		});
	},

	onchange : function(me){//设置你按钮可用状态
		me.i18nGrid.down('#i18nManSaveId').setDisabled(me.i18nGrid.getSelectionModel().getSelection().length === 0);
		me.i18nGrid.down('#i18nMaDelId').setDisabled(me.i18nGrid.getSelectionModel().getSelection().length === 0);
	}
});
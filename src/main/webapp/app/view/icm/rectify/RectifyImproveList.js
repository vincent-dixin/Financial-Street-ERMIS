/*整改计划制定*/
Ext.define('FHD.view.icm.rectify.RectifyImproveList',{
	extend: 'Ext.container.Container',
    alias: 'widget.rectifyimprovelist',
    layout: 'fit',
    requires: [
       'FHD.view.icm.rectify.form.RectifyView',
       'FHD.view.icm.defect.component.DefectRelaImproveGrid',
       'FHD.view.icm.rectify.component.ImprovePlanViewGrid'
    ],
	initComponent:function(){
		var me=this;
		me.rectifyImproveGrid = Ext.create('FHD.ux.GridPanel', { //实例化一个grid列表
            border: false,
            region: 'center',
            url:__ctxPath + '/icm/improve/findImproveListBypage.f?companyId='+__user.companyId+'&dealStatus=N', //调用后台url
            cols: [{header : 'ID',dataIndex : 'id',hidden:true,sortable: false},
            	{header : '所属公司',dataIndex : 'companyName',hidden:true, sortable : false}, 
				{header : '编号',dataIndex : 'code',sortable: false,flex : 1}, 
				{header : '名称',dataIndex : 'name',sortable: false,flex : 3, renderer : function(value, metaData, record, colIndex, store, view) { 
						/*
							data-qtip:设置提示正文内容。
							data-qtitle:设置提示的标题。
							data-qwidth:设置提示的宽度。
							data-qalign:表示用提示的一个基准点，对应到原件的哪个基准点。例如：tl-br表示用提示的左上角，对应到原件的右下角。
						*/
						metaData.tdAttr = 'data-qtip="'+value+'"'; 
				    	return "<a href=\"javascript:void(0);\" onclick=\"Ext.getCmp('"+me.id+"').viewObject('" + record.get('id') + "')\">"+value+"</a>";  
						//return value;
					}
				},              
				{header : '计划开始日期',dataIndex :'planStartDate',sortable: false}, 
				{header : '计划完成日期',dataIndex : 'planEndDate',sortable: false}, 
				{header : '处理状态',dataIndex : 'dealStatus',sortable: false, renderer : function(value, metaData, record, colIndex, store, view) { 
						if('N' == value){
							return '未开始';
						}else if('H' == value){
							return '处理中';
						}else if('F' == value){
							return '已完成';
						}else{
							return '';
						}
					}
				},
				{header : '状态',dataIndex : 'status',sortable: false, renderer : function(value, metaData, record, colIndex, store, view) { 
						if('S' == value){
							return '已保存';
						}else if('P' == value){
							return '已提交';
						}else{
							return '';
						}
					}
				},
				{header : '创建日期',dataIndex :'createTime',sortable: false} 
			],
			tbarItems:[{iconCls : 'icon-add', tooltip:'创建一条新的计划', text:'添加', handler :me.addImprove, scope : this},
				'-',
				{iconCls : 'icon-edit', id: 'editImprove', tooltip:'选择一条状态为“已保存”的计划，修改', text:'修改', handler :me.editImprove, disabled:true, scope : this}, 
				'-',
				{iconCls : 'icon-del', id: 'deleteImprove', tooltip:'选择一条或多条状态为“已保存”的计划，删除', text:'删除', handler :me.deleteImprove, disabled:true, scope : this}
			]
        });
		me.rectifyImproveGrid.store.on('load', function () {
            me.setstatus()
        });
        me.rectifyImproveGrid.on('selectionchange', function () {
            me.setstatus()
        });
		me.callParent(arguments);
		me.add(me.rectifyImproveGrid);
	},
	setCenterContainer:function(compent){
		this.removeAll(true);
		this.add(compent);
	},
	setstatus: function(){
		var me = this;
		var length = me.rectifyImproveGrid.getSelectionModel().getSelection().length;
		if(me.rectifyImproveGrid.down('#deleteImprove')){
			me.rectifyImproveGrid.down('#deleteImprove').setDisabled(length === 0);
		}
		if(me.rectifyImproveGrid.down('#editImprove')){
			var row = me.rectifyImproveGrid.getSelectionModel()[0];
			if(row){
				if(row.get('status') != 'D' && length == 1){
					me.rectifyImproveGrid.down('#editImprove').setDisabled(false);
				}else{
					me.rectifyImproveGrid.down('#editImprove').setDisabled(true);
				}
			}else{
				me.rectifyImproveGrid.down('#editImprove').setDisabled(length != 1);
			}
		}
	},
	//添加
	addImprove:function(){
		//var me = this;
		//this.setCenterContainer(Ext.create('FHD.view.icm.rectify.form.RectifyParent',{parameter:{type:'no'},upCompent:me}));
		var me=this;
    	var rectifyimprovemainpanel = this.up('rectifyimprovemainpanel');
    	if(rectifyimprovemainpanel){
    		rectifyimprovemainpanel.paramObj.editflag=false;
    		rectifyimprovemainpanel.paramObj.improveId='';
    		//激活新增面板
    		rectifyimprovemainpanel.navBtnHandler(1);
    	}
	},
	//修改
	editImprove: function(){
		//var me = this;
		//var selection=me.rectifyGrid.getSelectionModel().getSelection()[0];//得到选中的记录
		//me.setCenterContainer(Ext.create('FHD.view.icm.rectify.form.RectifyParent',{parameter:{improveId:selection.get('id'),type:'yes'},upCompent:me}));
		var me=this;
    	
    	var selection = me.rectifyImproveGrid.getSelectionModel().getSelection();//得到选中的记录
    	var improveId=selection[0].get('id');
    	var isSubmit=selection[0].get('status');
    	if(isSubmit=='P'){
    		Ext.Msg.alert(FHD.locale.get('fhd.common.prompt'),'对不起,您不能修改已提交的数据!');
    		return false;
    	}
    	
    	var rectifyimprovemainpanel = me.up('rectifyimprovemainpanel');
    	if(rectifyimprovemainpanel){
    		rectifyimprovemainpanel.paramObj.editflag=true;
    		rectifyimprovemainpanel.paramObj.improveId=improveId;//业务ID为整改计划ID
    		//激活编辑面板
    		rectifyimprovemainpanel.navBtnHandler(1);
    	}
	},
    //删除
    deleteImprove:function(){
    	var me = this;
    	var selection = me.rectifyImproveGrid.getSelectionModel().getSelection();//得到选中的记录
    	var flag = true;
    	for ( var i = 0; i < selection.length; i++) {
			if(selection[i].get('status')=='P'){
				flag = false;
			}
		}
		if(flag){
			Ext.MessageBox.show({
				title : FHD.locale.get('fhd.common.delete'),
				width : 260,
				msg : FHD.locale.get('fhd.common.makeSureDelete'),
				buttons : Ext.MessageBox.YESNO,
				icon : Ext.MessageBox.QUESTION,
				fn : function(btn) {
					if (btn == 'yes') {//确认删除
						var ids = [];
						for ( var i = 0; i < selection.length; i++) {
							ids.push(selection[i].get('id'));
						}
						FHD.ajax({//ajax调用
							url : __ctxPath+ '/icm/improve/removeImproveByIdBatch.f',
							params : {
								improveIds : ids
							},
							callback : function(data) {
								if (data) {//删除成功！
									Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
									me.rectifyImproveGrid.store.load();
								}
							}
						});
					}
				}
			});
		}else{
			Ext.Msg.alert(FHD.locale.get('fhd.common.prompt'),'对不起,您不能删除已提交的数据!');
			return false;
		}
		
    },
    viewObject: function(id){
    	var me = this;
    	var rectifyView = Ext.widget('rectifyview');
    	var improveplanviewgrid = Ext.widget('improveplanviewgrid',{border: false});
    	rectifyView.loadData(id);
    	improveplanviewgrid.loadData(id);
    	Ext.create('FHD.ux.Window',{
			title:'预览',
			iconCls: 'icon-view',//标题前的图片
			items: [Ext.create('Ext.form.Panel',{
				defaults : {
					columnWidth : 1
				},//每行显示一列，可设置多列
				layout : {
					type : 'column'
				},
				autoScroll: true,
				bodyPadding:'0 3 3 3',
				items:[
					rectifyView,
					{
						xtype : 'fieldset',
						layout : {
							type : 'column'
						},
						defaults:{
							columnWidth:1
						},
						collapsed : false,
						collapsible : false,
						title : '方案列表',
						items:[improveplanviewgrid]
					}
				]
			})], 
			maximizable:true,//（是否增加最大化，默认没有）
			listeners:{
				close : function(){
					me.reloadData();
				},
				maximize: function(win){
					var targetPanel = win.down('panel');
					var d = targetPanel.body.dom;
					d.scrollTop = d.scrollHeight - d.offsetHeight;
				}
			}
		}).show();
    },
	reloadData:function(){
		var me=this;
		me.rectifyImproveGrid.store.proxy.url=__ctxPath + '/icm/improve/findImproveListBypage.f';
		me.rectifyImproveGrid.store.on('beforeload', function (store, options) {
			var new_params = {companyId: __user.companyId,dealStatus:'N'};
	        Ext.apply(store.proxy.extraParams, new_params);
    	});
		me.rectifyImproveGrid.store.load();
	}
});
 
 
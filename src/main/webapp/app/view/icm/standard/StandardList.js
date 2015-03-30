/**
 *    @description 展示列表
 *    列表展示： /standard/standardGrid/findStandardByPage.f
 *    编辑明细： /standard/standardTree/findStandardByIdToJson.do
 *    @author 宋佳
 *    @since 2013-3-5
 */
Ext.define('FHD.view.icm.standard.StandardList', {
	extend : 'FHD.ux.GridPanel',
	alias : 'widget.standardlist',
	
	url :__ctxPath+ '/standard/standardGrid/findStandardByPage.f',
	extraParams: {
		id: '',
		editflag: ''
	},
	
	initComponent : function() {
		var me = this;
		
		me.tbarItems = [{
			name : 'standard_list_add',
			text : '添加',
			tooltip: '添加内控要求',
			iconCls : 'icon-add',
			handler : me.addStandard,
			scope : this
		}, '-', {
			name : 'standard_list_edit',
			text : '修改',
			tooltip: '修改内控要求',
			iconCls : 'icon-edit',
			handler : me.editStandard,
			scope : this
		}, '-', {
			name : 'standard_list_del',
			text : '删除',
			tooltip: '删除内控要求',
			iconCls : 'icon-del',
			handler : me.delStandard,
			scope : this
		}];
		
		me.cols = [{
			header : '编号',
			dataIndex : 'code',
			sortable : true,
			flex : 1
		}, {
			header : '名称',
			dataIndex : 'name',
			sortable : true,
			flex : 3,
			renderer : function(value, metaData, record, colIndex, store, view) {
				metaData.tdAttr = 'data-qtip="'+value+'"';
				return value;
			}
		},{
			header : '管理责任部门',
			dataIndex : 'dept',
			sortable : true,
			flex : 1
		}, {
			header : '执行状态',
			dataIndex : 'dealStatus',
			sortable : true,
			flex : 1,
			renderer : function(value, metaData, record, colIndex, store, view) { 
				if('N' == value){
					return '未开始';
				}else if('H' == value){
					return '处理中';
				}else if('ic_control_standard_estatus_1' == value){
					return '待更新';
				}else if('ic_control_standard_estatus_2' == value){
					return '体系建设计划阶段';
				}else if('ic_control_standard_estatus_3' == value){
					return '体系更新阶段';
				}else if('ic_control_standard_estatus_4' == value){
					return '缺陷整改阶段';
				}else if('ic_control_standard_estatus_5' == value){
					return '已纳入内控手册运转';
				}
				return value;
			}
		}];
        Ext.apply(me,{ 
        	layout : {
        		type : 'fit'
        	}
		});
        
		me.callParent(arguments);
        
        me.store.on('load', function () {
            me.setstatus()
        });
        me.on('selectionchange', function () {
            me.setstatus()
        });
	},
	setstatus: function(){
    	var me = this;
    	
        var length = me.getSelectionModel().getSelection().length;
        me.down('[name=standard_list_del]').setDisabled(length === 0);
        if(length != 1){
        	me.down('[name=standard_list_edit]').setDisabled(true);
        }else{
        	me.down('[name=standard_list_edit]').setDisabled(false);
        }
    },
	// 添加 type=0的standard
	addStandard : function() {
		var me=this;
		
		var standardtab = me.up('standardtab');
		var standardtree = me.up('standardtab').up('standardmainpanel').up('standardmanage').standardTree;
		standardtab.remove(standardtab.standardedit);
		standardtab.standardedit = Ext.widget('standardedit');
		standardtab.add(standardtab.standardedit);
		var nodeItems = standardtree.getSelectionModel().selected.items;
		var standardedit = standardtab.standardedit;
		var form = standardedit.getForm();
		form.setValues({
			'upName' : nodeItems[0].data.text,
			'id'     : standardtree.selectId
		});
		standardtree.paramObj = {};
		standardtree.paramObj.standardId = standardtree.selectId;
		standardtree.paramObj.controlType='';
		standardtree.paramObj.isLeaf = true;
		standardtree.paramObj.idSeq = nodeItems[0].data.idSeq;
		standardtree.paramObj.addType = "0";
		standardedit.initParam(standardtree.paramObj);
		standardtab.setActiveTab(standardedit);
		//standardedit.standardCreateCodeButton.setDisabled(false);
	},
	// 编辑
	editStandard : function() {
		var me=this;
		
		var selection = me.getSelectionModel().getSelection();// 得到选中的记录
		if(0 == selection.length){
    		Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), '请选择一条内控要求!');
	    	return ;
		}
		var standardtab = me.up('standardtab');
		var standardtree = me.up('standardtab').up('standardmainpanel').up('standardmanage').standardTree;
		standardtree.paramObj = {};
		standardtree.paramObj.controlType='';
		standardtab.standardedit.initParam(standardtree.paramObj);
		
		standardtab.setActiveTab(standardtab.standardedit);
		standardtab.addType = "0";
		standardtab.standardedit.controlType = 'listEdit';
		standardtab.standardedit.standardId = selection[0].get('id');
		var form = standardtab.standardedit.getForm();
		form.reset();		
		form.load({
			waitMsg : '信息读取中.....',
			url : __ctxPath + '/standard/standardTree/findStandardByIdToJson.do',
			params : {
				standardId : selection[0].get('id')
			},
			success : function(form, action) {
				return true;
			},
			failure : function(form, action) {
			}
		});
	},
	// 删除type=0的数据
	delStandard : function() {
		var me=this;
		
		var selection = me.getSelectionModel().getSelection();// 得到选中的记录
		Ext.MessageBox.show({
			title : FHD.locale.get('fhd.common.delete'),
			width : 260,
			msg : FHD.locale.get('fhd.common.makeSureDelete'),
			buttons : Ext.MessageBox.YESNO,
			icon : Ext.MessageBox.QUESTION,
			fn : function(btn) {
				if (btn == 'yes') {// 确认删除
					var ids = [];
					for (var i = 0; i < selection.length; i++) {
						ids.push(selection[i].get('id'));
					}
					FHD.ajax({// ajax调用
						url : __ctxPath
								+ '/standard/standardTree/removeStandards.do',
						params : {
							standardIds : ids
						},
						callback : function(data) {
							if (data) {// 删除成功！
								Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
								me.store.load();
							}
						}
					});
				}
			}
		});
	},
	reloadData :function(){
		var me = this;
		//me.gridPanel.store.proxy.extraParams.id
        me.store.proxy.extraParams.clickedNodeId = me.up('standardtab').up('standardmainpanel').up('standardmanage').standardTree.selectId;
        me.store.proxy.extraParams.isLeaf = '1';
		me.store.load();
	}
});
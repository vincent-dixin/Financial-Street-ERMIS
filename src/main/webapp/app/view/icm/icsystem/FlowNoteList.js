/**
 *    @description 展示列表
 *    @author 宋佳
 *    @since 2013-3-10
 */
Ext.define('FHD.view.icm.icsystem.FlowNoteList', {
	extend : 'FHD.ux.GridPanel',
	requires: [
    	'FHD.view.icm.icsystem.form.NoteEditForm'
    ],
	alias : 'widget.flownotelist',
	cols : [],
	tbar : [],
	url: __ctxPath + '/process/findprocesspointlistbypage.f',
	tbarItems : [],
	idSeq : '',
	upName : '',
	paramObj:[],
	selectId : '',
	initComponent : function() {
		var me = this;
		me.on('selectionchange',me.onchange);//选择记录发生改变时改变按钮可用状态
		me.tbarItems = [{
					iconCls : 'icon-add',
					id : 'flownote_add',
					text: '添加',tooltip: '添加流程节点',
					handler : me.addNote,
					scope : this
				}, '-', {
					iconCls : 'icon-edit',
					text: '修改',tooltip: '修改流程节点',
					id : 'flownote_edit',
					handler : me.editNote,
					scope : this
					//disabled : true
				}, '-', {
					iconCls : 'icon-del',
					text: '删除',tooltip: '删除流程节点',
					id : 'flownote_del',
					handler : me.delNote,
					scope : this
				}];
		me.cols = [{
					header : '编号',
					dataIndex : 'code',
					sortable : false,
					flex : 1
				}, {
					header : '名称',
					dataIndex : 'name',
					sortable : false,
					flex : 1
				}, {
					header : '责任部门',
					dataIndex : 'orgName',
					sortable : false
				}, {
					header : '责任人',
					dataIndex : 'responsilePersionId',
					sortable : false
				}];
        Ext.apply(me,{ 
        			extraParams: {
               			id: undefined,
                		editflag: undefined
            		}});
		me.callParent(arguments);
	},
	// 
	addNote : function(){
	    var me = this;
	    me.selectId = '';
	    var upPanel = me.up('flownotemainpanel');
	    upPanel.remove(upPanel.noteeditform);
	    upPanel.noteeditform = Ext.widget('noteeditform',{id:'noteeditform'});
	    upPanel.add(upPanel.noteeditform);
	    upPanel.getLayout().setActiveItem(1);
	    upPanel.noteeditform.initParam({
	    	processId : me.paramObj.processId,
			processPointId : me.selectId
	    });
	    upPanel.noteeditform.reloadData();
	},
	// 编辑
	editNote : function() {
		var me = this;
		var selection = me.getSelectionModel().getSelection();// 得到选中的记录
			if(selection == '' || null == selection){
		    	Ext.Msg.alert(FHD.locale.get('fhd.common.message'), '对叶子节点无法进行编辑！');
		    	return ;
			}
		me.selectId  = selection[0].get('id');
	    var upPanel = me.up('flownotemainpanel');
	    upPanel.remove(upPanel.noteeditform);
	    upPanel.noteeditform = Ext.widget('noteeditform',{id:'noteeditform',dataType : 'sc',typeTitle : '记分卡'});
	    upPanel.add(upPanel.noteeditform);
	    upPanel.getLayout().setActiveItem(1);
	    upPanel.noteeditform.initParam({
	    	processId : me.paramObj.processId,
			processPointId : me.selectId
	    });
		upPanel.noteeditform.reloadData();
	},
	// 删除节点
	delNote : function() {
		var me = this;
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
								+ '/ProcessPoint/ProcessPoint/removeProcessPoint.f',
						params : {
							ProcessPointID : ids
						},callback: function (data) {
                            if (data) {
                            	me.reloadData();
                                Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
                            }else{
                            	Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateFailure'));
                            }
                        }
					});
				}
			}
		});
	},
	reloadData :function(){
		var me = this;
        me.store.proxy.extraParams.processId = me.paramObj.processId;
		me.store.load();
	},
	onchange :function(){//设置你按钮可用状态
		var me = this;
		me.down('#flownote_edit').setDisabled(me.getSelectionModel().getSelection().length === 0);
		me.down('#flownote_del').setDisabled(me.getSelectionModel().getSelection().length === 0);
	}
});
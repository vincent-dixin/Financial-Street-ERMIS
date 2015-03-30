/**
 *    @description 展示列表
 *    @author 宋佳
 *    @since 2013-3-10
 */
Ext.define('FHD.view.icm.icsystem.FlowRiskList', {
	extend : 'FHD.ux.GridPanel',
	requires: [
    	'FHD.view.icm.icsystem.form.RiskEditForm'
    ],
	alias : 'widget.flowrisklist',
	cols : [],
	tbar : [],
	url: __ctxPath + '/processrisk/findProcessRiskListByPage.f',
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
					id : 'flowrisk_add',
					text: '添加',tooltip: '添加风险信息',
					handler : me.addNote,
					scope : this
				}, '-', {
					iconCls : 'icon-edit',
					id : 'flowrisk_edit',
					text: '修改',tooltip: '修改风险信息',
					handler : me.editNote,
					scope : this
					//disabled : true
				}, '-', {
					iconCls : 'icon-del',
					id : 'flowrisk_del',
					text: '删除',tooltip: '删除风险信息',
					handler : me.delNote,
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
					flex : 1
				},{
					header : '风险描述',
					dataIndex : 'desc',
					sortable : true
				},{
					header : '控制措施数量',
					dataIndex : 'measureNum',
					sortable : true
				}];
        Ext.apply(me,{ 
        			extraParams: {
               			id: undefined,
                		editflag: undefined
            		}})
		me.callParent(arguments);
	},
	// 
	addNote : function(){
	    var me = this;
	    me.selectId = '';
	    var upPanel = me.up('riskmeasuremainpanel');
	    upPanel.remove(upPanel.riskeditform,true);
	    upPanel.riskeditform = Ext.widget('riskeditform',{id:'riskeditform',processId:me.paramObj.processId});
	    upPanel.riskeditform.measureeditform = [];
	    upPanel.add(upPanel.riskeditform);
	    upPanel.getLayout().setActiveItem(1);
	    upPanel.riskeditform.initParam({
	    	processId : me.paramObj.processId,
			processRiskId : me.selectId
	    });
	    upPanel.riskeditform.reloadData();
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
	    var upPanel = me.up('riskmeasuremainpanel');
	    upPanel.remove(upPanel.riskeditform);
	    upPanel.riskeditform = Ext.widget('riskeditform',{id:'riskeditform',dataType : 'sc',typeTitle : '记分卡'});
		if(me.up('planprocessedittabpanel')){
			me.up('planprocessedittabpanel').autoHeight=true;
		}
	    upPanel.add(upPanel.riskeditform);
	    upPanel.getLayout().setActiveItem(1);
	    upPanel.riskeditform.initParam({
	    	processId : me.paramObj.processId,
			processRiskId : me.selectId
	    });
	    upPanel.riskeditform.getInitData();
		upPanel.riskeditform.reloadData();
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
								+ '/processrisk/removeprocessrisk.f',
						params : {
							riskId : ids
						},callback: function (data) {
                            if (data.data) {
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
		me.down('#flowrisk_edit').setDisabled(me.getSelectionModel().getSelection().length === 0);
		me.down('#flowrisk_del').setDisabled(me.getSelectionModel().getSelection().length === 0);
	}
});
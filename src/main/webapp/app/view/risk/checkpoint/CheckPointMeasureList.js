/**
 *    @description 展示列表
 *    @author 宋佳
 *    @since 2013-3-10
 */
Ext.define('FHD.view.risk.checkpoint.CheckPointMeasureList', {
	extend : 'FHD.ux.GridPanel',
	alias : 'widget.checkpointmeasurelist',
	requires: [
    	'FHD.view.riskinput.form.SchemeForm'
    ],
	cols : [],
	tbar : [],
	idSeq : '',
	upName : '',
	paramObj:[],
	selectId : '',
	height : 450,
	checked : false,
	initComponent : function() {
		var me = this;
		me.cols = [{
    			header:'控制措施',dataIndex:'parentProcessName',flex:3,
    			renderer : function(value, metaData, record, colIndex, store, view) {
					metaData.tdAttr = 'data-qtip="'+value+'"'; 
		 			return value;
				}
    		},{
    			header:'责任人',dataIndex:'processName',flex:3,
    			renderer : function(value, metaData, record, colIndex, store, view) {
					metaData.tdAttr = 'data-qtip="'+value+'"'; 
		 			return value;
				}
    		},{
    			header:'工作内容',dataIndex:'parentProcessName',flex:3,
    			renderer : function(value, metaData, record, colIndex, store, view) {
					metaData.tdAttr = 'data-qtip="'+value+'"'; 
		 			return value;
				}
    		},{
    			header:'工作类别',dataIndex:'parentProcessName',flex:3,
    			renderer : function(value, metaData, record, colIndex, store, view) {
					metaData.tdAttr = 'data-qtip="'+value+'"'; 
		 			return value;
				}
    		},{
    			header:'计划完成时间',dataIndex:'parentProcessName',flex:3,
    			renderer : function(value, metaData, record, colIndex, store, view) {
					metaData.tdAttr = 'data-qtip="'+value+'"'; 
		 			return value;
				}
    		}];
        Ext.apply(me);
		me.callParent(arguments);
	},
	// 
	addMeasure : function(){
		var me = this;
		me.schemeform = Ext.widget('schemeform');
	    var popWin = Ext.create('FHD.ux.Window',{
					title:'添加控制措施',
					//modal:true,//是否模态窗口
					collapsible:false,
					maximizable:true,//（是否增加最大化，默认没有）
					items:[me.schemeform]
	    		}).show();
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
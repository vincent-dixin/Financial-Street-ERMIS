/*
 * 内控评价列表页面 
 * */
Ext.define('FHD.view.icm.standard.bpm.StandardBpmList',{
	extend: 'Ext.container.Container',
    alias: 'widget.standardbpmlist',
    	requires : [
		'FHD.view.icm.standard.bpm.StandardBpmOne'
	],
    pagable:true,
    layout: 'fit',
    
    initComponent: function(){
    	var me = this;
    	
		//评价计划列表
		me.standardGrid = Ext.create('FHD.ux.GridPanel', {
	        border: false,
	        region: 'center',
	        url: __ctxPath + '/icm/standard/findStandardByPage.f',
	        cols: [{
					dataIndex : 'id',
					hidden: true
				},{
					header : '编号',
					dataIndex : 'code',
					sortable : true,
					flex : 1
				}, {
					header : '名称',
					dataIndex : 'name',
					sortable : true,
					flex : 1
				},{header : '执行状态',dataIndex : 'dealstatus',sortable: false, 			
					renderer:function(value){
						if(value=='N'){
							return '未开始';
						}else if(value=='H'){
							return '处理中';
						}else if(value=='F'){
							return '已完成';
						}else if(value=='A'){
							return '逾期';
						}
					return value;
				}},{header : '状态',dataIndex : 'status',sortable: false, renderer : function(value, metaData, record, colIndex, store, view) { 
					if('S' == value){
						return '已保存';
					}else if('P' == value){
						return '已提交';
					}else if('D' == value){
						return '已处理';
					}else{
						return '';
					}
				}},{header : '创建日期',
					dataIndex : 'createTime',
					sortable: false
				}],
            tbarItems: [{
					iconCls : 'icon-add',
					id : 'add',
					handler : me.addStandard,
					text : '添加',
					scope : this
				}, '-', {
					iconCls : 'icon-edit',
					id : 'edit',
					handler : me.editStandard,
					text : '修改',
					scope : this
				}, '-', {
					iconCls : 'icon-del',
					id : 'del',
					handler : me.delStandard,
					text : '删除',
					scope : this
				}]
		});
		me.standardGrid.store.on('load', function () {
            me.setstatus()
        });
        me.standardGrid.on('selectionchange', function () {
            me.setstatus()
        });
        //评价计划列表
        me.callParent(arguments);
        me.add(me.standardGrid);
    },
   // 添加 type=0的standard
	addStandard : function() {
		var me = this.up('standardplancenterpanel');
		var standardBpmOne = Ext.widget('standardbpmone');
		me.removeAll();
		me.add(standardBpmOne);
	},
	// 编辑
	editStandard : function() {
		var me = this;
		var selection = me.standardGrid.getSelectionModel().getSelection();// 得到选中的记录
		if(selection[0].get('status') != 'S'){
			Ext.Msg.alert('提示', '只有已保存的数据可以编辑！');
		}else{
			var standardId = selection[0].get('id');
			var me = this.up('standardplancenterpanel');
			var standardBpmOne = Ext.widget('standardbpmone',{businessId : standardId});
			me.removeAll();
			me.add(standardBpmOne);
		}
	},
	// 删除type=0的数据
	delStandard : function() {
		var me = this;
		var selection = me.standardGrid.getSelectionModel().getSelection();// 得到选中的记录
    	var flag = true;
    	for ( var i = 0; i < selection.length; i++) {
			if(selection[i].get('status')!='S'){
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
									me.standardGrid.store.load();
								}
							}
						});
					}
				}
			});
		}else{
			Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'对不起,您不能删除已提交的数据!');
			return false;
		}
	},
    setstatus: function(){
    	var me = this;
    	
        var length = me.standardGrid.getSelectionModel().getSelection().length;
        me.standardGrid.down('#del').setDisabled(length === 0);
        if(length != 1){
        	me.standardGrid.down('#edit').setDisabled(true);
        }else{
        	me.standardGrid.down('#edit').setDisabled(false);
        }
    },
	reloadData:function(){
		var me=this;
		me.standardGrid.store.load();
	}
});
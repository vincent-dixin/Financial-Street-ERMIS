/**
 * 我的数据
 * 我的制度
 * @author 邓广义
 */
Ext.define('FHD.view.icm.statics.IcmMyRuleInfo', {
    alias: 'widget.icmmyruleinfo',
 	extend: 'Ext.container.Container',
 	padding:10,
 	overflowX: 'hidden',
	overflowY: 'auto',
	flex:1,
	layout: 'fit',
    // 初始化方法
    initComponent: function() {
    	 var me = this;
		 me.gird = Ext.create('FHD.ux.GridPanel', {
				cols: [
					{dataIndex:'id',width:0},
					{ header: '制度分类', dataIndex: 'classify', flex: 1
					},
					{ header: '制度编号',  dataIndex: 'code' ,flex: 1 ,
						renderer:function(value,metaData,record,colIndex,store,view) {
								return value ;
						}
					},
					{ header: '制度名称', dataIndex: 'name', flex: 2 ,
						renderer:function(value,metaData,record,colIndex,store,view) {
							metaData.tdAttr = 'data-qtip="'+value+'" data-qwidth="'+100+'"'; 
								return value; 
						}
					},
					{header:'责任部门',dataIndex:'org',flex:.6}
				],
				url: __ctxPath+'/icm/statics/icmmyinstitutiondatas.f',
				tbarItems:['制度信息'],
				extraParams:{pagable:true,orgid:me.orgid},
				checked:false,
				searchable:true,
				pagable : true
			});			 
		
				 
	
        


        me.callParent(arguments);
        me.add(me.gird);
    },
    reloadData:function(orgid){
    	var me=this;

    },
    showProcessView:function(processid){
    	alert(processid);
    }
});
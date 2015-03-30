/**
 * 我的数据
 * 我的控制措施
 * @author 邓广义
 */
Ext.define('FHD.view.icm.statics.IcmMyControlInfo', {
    alias: 'widget.icmmycontrolinfo',
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
					{ header: '所属风险', dataIndex: 'risk', flex: 2  ,
						renderer:function(value,metaData,record,colIndex,store,view) {
							metaData.tdAttr = 'data-qtip="'+value+'" data-qwidth="'+100+'"'; 
							return  value ; 
					}
					},
					{ header: '控制措施编号',  dataIndex: 'code' ,flex: 1 ,
						renderer:function(value,metaData,record,colIndex,store,view) {
								return value; 
						}
					},
					{ header: '控制措施名称', dataIndex: 'name', flex: 2 ,
						renderer:function(value,metaData,record,colIndex,store,view) {
							metaData.tdAttr = 'data-qtip="'+value+'" data-qwidth="'+100+'"'; 
								return value; 
						}
					},
					{header:'控制方式',dataIndex:'controlMode',width:70},
					{header:'是否关键控制点',dataIndex:'isKeyPoint',flex:.7}
				],
				url: __ctxPath+'/icm/statics/icmmycontroldatas.f',
				tbarItems:['控制措施信息'],
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
    	//alert(processid);
    }
});
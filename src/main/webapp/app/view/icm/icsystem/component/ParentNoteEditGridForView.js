/*
 * 评价产生的缺陷的可编辑列表 
 * */
 Ext.define('ProcessPointPre', {
		    extend: 'Ext.data.Model',
		    fields:['id','processId','pointId', 'pointName', 'pointPreId','contition']
		});
 Ext.define('FHD.view.icm.icsystem.component.ParentNoteEditGridForView',{
    	extend:'FHD.ux.EditorGridPanel',
    	alias: 'widget.parentnoteeditgridforview',
    	url: __ctxPath + '/process/findParentListByPointId.f',
    	region:'center',
    	objectType:{},
    	pagable : false,
    	searchable:false,
    	layout: 'fit',
    	
    	initComponent:function(){
	    	var me=this;
	    	Ext.apply(me,{
	    		extraParams:{
	    			processPointId : me.processPointId,
	    			processId : me.processId
	    		}
	    	});
	    	me.cols=[ {header:'父节点',dataIndex:'pointPreId'},
				      {header:'入口条件', dataIndex: 'contition'}
				      ];
	    	me.callParent(arguments);
	    },
	    reloadData :function(){
			var me = this;
			me.store.load();
		}
});
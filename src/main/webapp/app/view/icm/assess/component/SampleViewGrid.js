/*
 * 流程样本列表
 */
 Ext.define('FHD.view.icm.assess.component.SampleViewGrid',{
	extend:'FHD.ux.GridPanel',
	alias: 'widget.sampleviewgrid',
	
	url: __ctxPath+'/icm/assess/findAssessSampleListByAssessPlanIdAndProcessId.f',
	extraParams:{
		assessPlanId:'',
		type:'',
		processId:''
	},
	
	cols:[],
	tbar:[],
	tbarItems:[],
	pagable:false,
	checked:false,
	
	initComponent:function(){
    	var me=this;
    	
    	me.extraParams.assessPlanId=me.assessPlanId;
    	me.extraParams.type=me.type;
    	me.extraParams.processId=me.processId;
    	
    	me.isQualifiedStore = Ext.create('Ext.data.Store',{
			fields : ['id', 'name'],
			data : [
			    {'id' : 'Y','name' : '是'},
			    {'id' : 'N','name' : '否'},
			    {'id':'NAN','name':'不适用'}
			]
		});

    	me.cols=[
			{header:'评价点', dataIndex: 'assessPointDesc', sortable: false,flex:1},
			{header:'实施证据', dataIndex: 'assessSampleName', sortable: false,flex:1},
		    {header:'状态', dataIndex: 'type', sortable: false,flex:1},
		    {header:'样本编号', dataIndex: 'code', sortable: false,flex:1},
		    {header:'样本名称', dataIndex: 'name',sortable: false,flex:1},
		    {header:'是否合格', dataIndex: 'isQualified', sortable: false,
		    	renderer:function(value){
		    		var index = me.isQualifiedStore.find('id',value);
		    		var record = me.isQualifiedStore.getAt(index);
		    		if(record!=null){
		    			return record.data.name;
		    		}
	    			return value;
		    	}
		    },
		    {header:'说明', dataIndex: 'comment',sortable: false,flex:1},
		    {header:'补充样本的来源样本', dataIndex: 'sourceSample',sortable: false,flex:2},
		    {header:'附件', dataIndex: 'file',sortable: false,flex:3,
			    renderer : function(value, metaData, record, colIndex, store, view) { 
			    	if(value){
			    		metaData.tdAttr = 'data-qtip="'+value+'"';
			    		var valueNew = value.substring(0,value.length>10?10:value.length);
						if(value.length>10){
							valueNew +="...";
						}
			    		return "<a href='javascript:void(0)' onclick=\"Ext.getCmp('"+me.id+"').downloadFile('" + record.data.fileId + "')\" >"+valueNew+" </a> ";
			    		/*
			    		return "<a href='javascript:void(0)' onclick=\"Ext.getCmp('"+me.id+"').downloadFile('" + record.data.fileId + "')\" >"+value+" </a> "
			    			+" <a href='javascript:void(0)' onclick=\"Ext.getCmp('"+me.id+"').delFile('" + record.data.assessSampleId + "')\" ><font class='icon-close' style='cursor:pointer;'>&nbsp&nbsp&nbsp&nbsp</font></a>";
			    		*/
			    	}
			    	return value;
			    }
			},
			{dataIndex:'fileId',hidden:true},
			{dataIndex:'isHasSupplement',hidden:true},
			{dataIndex:'assessSampleId',hidden:true}
		];
    	
    	me.callParent(arguments);
    },
	downloadFile:function(){
		var me=this;
		
		var selection=me.getSelectionModel().getSelection();
		var assessSampleId = selection[0].get('assessSampleId');
        var fileId = selection[0].get('fileId');
        
        if(fileId != ''){
        	window.location.href=__ctxPath+"/sys/file/download.do?id="+fileId;
        }else{
        	Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),'该样本没有上传附件!');
        }
	},
	delFile:function(){
		var me=this;
		
		var selection=me.getSelectionModel().getSelection();
		var assessSampleId=selection[0].get('assessSampleId');
		FHD.ajax({
			url:__ctxPath+'/icm/assess/removeAssessSampleRelaFile.f',
			params:{
				assessSampleId:assessSampleId
			},
			callback:function(data){
				if(data){
					Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
					selection[0].set("file", "");
					selection[0].set("fileId", "");
					me.getStore().commitChanges();
				}
			}
		}); 
	},
	reloadData:function(){
		var me=this;
		
		me.store.load();
	}
});
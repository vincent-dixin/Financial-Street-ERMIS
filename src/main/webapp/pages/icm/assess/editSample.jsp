<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
var sampleGrid;
/***function start***/

var sampletext={
		xtype:'textfield',
        fieldLabel: '编辑前缀',
        name: 'sampletext',
        allowBlank: false
    }
var startNum={
        xtype: 'numberfield',
        anchor: '100%',
        name: 'startNum',
        fieldLabel: '编号期间',
        minValue: 0,
        columnWidth:1/4
    }
var stopNum={
        xtype: 'numberfield',
        anchor: '100%',
        name: 'stopNum',
        fieldLabel: '至',
        minValue: 0,
        columnWidth:1/4
    }
 var isQualifiedStore = Ext.create('Ext.data.Store',{//myLocale的store
	fields : ['id', 'name'],
	data : [{'id' : 'Y','name' : '是'},{'id' : 'N','name' : '否'},{'id':'NAN','name':'不适用'}]
});
var assessSampleId;
var fileId;
function upload(){
	Ext.create('FHD.ux.fileupload.FileUploadWindow',{
		multiSelect: false,
		callBack:function(value){
			if(value!=null&&value.length>0){
				FHD.ajax({
					url:__ctxPath+'/icm/assess/mergeAssessSampleRelaFile.f',
					params:{
						fileId:value, 
						assessSampleId:assessSampleId
						},
					callback:function(data){
						if(data){
							Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
							sampleGrid.store.load();
						}
					}
				}); 
			}
		}
	}).show();
}
function download(){
	downloadFile(fileId);
}
function addSample(){
	var selection=sampleGrid.getSelectionModel().getSelection();
	var assessSampleId=selection[0].get('assessSampleId');
	FHD.ajax({
		url:__ctxPath+'/icm/assess/mergeSample.f',
		params:{
			assessSampleId:assessSampleId
			},
		callback:function(data){
			if(data){
				//Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
				sampleGrid.store.load();
			}
		}
	}); 
}
function delFile(){
	FHD.ajax({
		url:__ctxPath+'/icm/assess/removeAssessSampleRelaFile.f',
		params:{
			assessSampleId:assessSampleId 
			},
		callback:function(data){
			if(data){
				Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
				sampleGrid.store.load();
			}
		}
	}); 
} 
/***function end***/

/***Ext.onReady start***/
Ext.onReady(function(){
	/***sampleGrid start***/
	sampleGrid = Ext.create('FHD.ux.EditorGridPanel',{//创建可编辑的grid列表，注释同不可编辑的gird
		//renderTo: 'assessResult${param._dc}', 
		multiSelect:false,
		pagable:false,
		checked:true,
		url: 'icm/assess/findAssessSampleListBySome.f?assessResultId=${param.assessResultId}&isQualified=${param.isQualified}',
		height:300,
		columnWidth:1/1,
		cols:[{dataIndex:'assessSampleId',hidden:true},
		      {dataIndex:'fileId',hidden:true},
		      {header:'状态', dataIndex: 'type', sortable: false,flex:1,editor: {allowBlank: false}},
		      {header:'样本编号', dataIndex: 'code', sortable: false,flex:1,editor: {allowBlank: false}},
		      {header:'样本名称', dataIndex: 'name',sortable: false,flex:1,editor: {allowBlank: false}},
		      {header:'是否合格', dataIndex: 'isQualified', sortable: false,
		    	  editor:new Ext.form.ComboBox({
		    		  store :isQualifiedStore,
		    		  valueField : 'id',
		    		  displayField : 'name',
		    		  selectOnTab: true,
		    		  lazyRender: true,
		    		  typeAhead: true,
		    		  allowBlank : false,
		    		  editable : false
		    	  }),
		    	  renderer:function(value){
		    		  var index = isQualifiedStore.find('id',value);
		    		  var record = isQualifiedStore.getAt(index);
		    		  if(record!=null){
		    			  return record.data.name;
		    		  }else{
		    			  return value;
		    		  }
		    	  }
		      },
		      
		      	{header:'说明', dataIndex: 'comment',sortable: false,flex:1,editor: {allowBlank: false}},
		      	{header:'补充样本的来源样本', dataIndex: 'sourceSample',sortable: false,flex:1},
		      	{header:'附件', dataIndex: 'file',sortable: false,
			    	  renderer:function(value,p,record){
			    		  if(value!=''){
			    			  return "<a href='javascript:void(0);'onclick='download()'>"+value+"</a>"+"<a href='javascript:void(0);'onclick='delFile()'><img src='images/icons/del.png'></a>";
			    		  }else{
			    			  return  "<a href='javascript:void(0);'onclick='upload()'>选择文件</a>";
			    		  }
					 }
			      }
		      ],
		      listeners:{
		    	  select:function(){
		    		  var selectionDate=sampleGrid.getSelectionModel().getSelection();
		    			if(null!=selectionDate[0].get('assessSampleId')){
		    				assessSampleId=selectionDate[0].get('assessSampleId');	
		    			}
		    			if(null!=selectionDate[0].get('fileId')){
		    				fileId=selectionDate[0].get('fileId');	
		    			}
		    	  }
		      },
		      tbarItems:[{iconCls: 'icon-add',id:'sampleAdd${param._dc}',handler:addSample, disabled : true, scope : this}]
		
	});
	var form=Ext.create('Ext.form.Panel', {
	    layout: 'column',
	    height:FHD.getCenterPanelHeight()-8,
	    defaults:{
         	 columnWidth:1/1
           },
	    items: [{
	    	
	    	xtype:'fieldset',
            title:'样本生成',
            layout:{
           	 type:'column'
            },
            defaults:{
             	 columnWidth:1/2,
             	 margin:'0 10 0 0'
            },
            items:[sampletext,startNum,stopNum,
            {xtype:'button',
            	text: '生成',
            	handler:function(){
            		FHD.ajax({
        				url:__ctxPath +'/icm/assess/saveSample.f',
                        params: {
                        	sampletext:form.getValues().sampletext,
                        	startNum:form.getValues().startNum,
                        	stopNum:form.getValues().stopNum,
                        	assessResultId:'${param.assessResultId}',
                        	assessPlanId:'${param.assessPlanId}'
                        },
                        callback: function (data) {
                        	if(data){
                        		sampleGrid.store.load();
                        	}
                        }
        			});		
            	},
            	columnWidth:1/8
            	}
            ]
           
	    },{
	    	
	    	xtype:'fieldset',
            title:'样本列表',
            layout:{
           	 type:'column'
            },
            defaults:{
          	 columnWidth:1/1
            }, 
            items:[sampleGrid]
	    }],
	    
	    renderTo: 'assessResult${param._dc}'
	});
	sampleGrid.on('selectionchange',function(m) {
	var len=sampleGrid.getSelectionModel().getSelection().length; 
	 if (len== 0) {
	   		Ext.getCmp('sampleAdd${param._dc}').setDisabled(true);
	   } else if (len == 1) {
	   		Ext.getCmp('sampleAdd${param._dc}').setDisabled(false);//删除可用
	   } else if (len > 1){
		  	Ext.getCmp('sampleAdd${param._dc}').setDisabled(true);   
	   } 
	 });
})

/***Ext.onReady end***/
</script>
</head>
<body>
	<div id='assessResult${param._dc}'></div>
</body>
</html>
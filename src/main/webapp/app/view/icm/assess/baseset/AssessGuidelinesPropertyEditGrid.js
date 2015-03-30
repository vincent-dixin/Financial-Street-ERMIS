/**
 * @author denggy
 * @describe 评价标准模板动态列表
 */
 Ext.define('AssessGuidelinesPropertyDataMapping', {
		    extend: 'Ext.data.Model',
		    fields:['id','content','minValue','maxValue','judgeValue','sort', 'dictype','parentId','parentName']
		});
 Ext.define('FHD.view.icm.assess.baseset.AssessGuidelinesPropertyEditGrid',{
    	extend:'FHD.ux.EditorGridPanel',
    	alias: 'widget.assessguidelinespropertyeditgrid',
    	findUrl: __ctxPath + "/icm/assess/baseset/findAssessGuidelinesPropertiesByAGId.f",//查询
    	region:'center',
    	objectType:{},
    	pagable : false,
    	searchable:false,
//    	height: 240,
    	flex: 1,
    	layout: 'fit',
        margin: '2 0 0 0',
    	addGrid:function(){//新增方法
			var me = this;
			var count = me.store.data.length;
			var maxSort = me.getStore().getAt(count-1) && me.getStore().getAt(count-1).get("sort") || 0;
			var r = Ext.create('AssessGuidelinesPropertyDataMapping',{
				//新增时初始化参数
				id:'',
				parentId : me.parentId,
				parentName : me.parentName,
				minValue: 1,
				maxValue: 1,
				judgeValue: 1,
				sort: maxSort+1,
				dictype :'ca_defect_level_0',
				content : '描述'
				
			});
			me.store.insert(count, r);
			me.editingPlugin.startEditByPosition({row:count,column:0});
		},
		saveGrid:function(){//保存方法
			var me = this;
			var rows = me.store.getModifiedRecords();
			var jsonArray=[];
			Ext.each(rows,function(item){
				jsonArray.push(item.data);
			});
		//	if(!this.validate(jsonArray)) return ;
			
			FHD.ajax({
				url: __ctxPath + '/icm/assess/baseset/saveAssessGuidelinesProperty.f',//保存
				params : {
					modifyRecords:Ext.encode(jsonArray)
				},
				callback : function(data){
					if(data){
						Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
						me.store.load();
					}
				}
			})
			me.store.commitChanges();
		},
		delGrid:function(){//删除方法
			var me = this;
			var selection = me.getSelectionModel().getSelection();
			Ext.MessageBox.show({
				title : FHD.locale.get('fhd.common.delete'),
				width : 260,
				msg : FHD.locale.get('fhd.common.makeSureDelete'),
				buttons : Ext.MessageBox.YESNO,
				icon : Ext.MessageBox.QUESTION,
				fn : function(btn) {
					if (btn == 'yes') {
						var ids = [];
						for(var i=0;i<selection.length;i++){
							var delId = selection[i].get('id');
							if(delId){
									ids.push(delId)
							}else {
									return Ext.Msg.alert('提示','没有保存的记录无法删除!')
							       }
						}
						FHD.ajax({
							url : __ctxPath + '/icm/assess/baseset/delAssessGuidelinesPropertyById.f',//删除
							params : {
								ids : ids.join(',')
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
		setstatus: function(){
	    	var me = this;
	        var length = me.getSelectionModel().getSelection().length;
	        var rows = me.store.getModifiedRecords();
			var jsonArray=[];
			Ext.each(rows,function(item){
				jsonArray.push(item.data);
			});
			me.down('#icm_assessguidelinesprop_save').setDisabled(jsonArray.length === 0);
	        me.down('#icm_assessguidelinesprop_del').setDisabled(length === 0);
	    },
    	initComponent:function(){//初始化
    		var me=this;
    		
	    	var assessStore = Ext.create('Ext.data.Store', {
	    	    fields : ['id', 'name'],
	    		proxy : {type : 'ajax',url : 'sys/dic/findDictEntryBySome.f?typeId=ca_defect_level',
	    		reader:{type:'json',root:'children'}},
	    		autoload:false
	    	});
	    	assessStore.load();
	    	me.tbarItems = ['&nbsp;模板项列表&nbsp;','-',{
	    				text:'添加',
						iconCls : 'icon-add',
						id : 'icm_assessguidelinesprop_add',
						handler : me.addGrid,
						disabled :true,
						scope : this
					}, '-', {
						text:'删除',
						iconCls : 'icon-del',
						id : 'icm_assessguidelinesprop_del',
						handler : me.delGrid,
						disabled :true,
						scope : this
					}, '-', {
						text:'保存',
						iconCls : 'icon-save',
						id : 'icm_assessguidelinesprop_save',
						handler : me.saveGrid,
						disabled :true,
						scope : this
					}];
	    	
	    	me.cols=[ 
'sample_assessment_standard'!=me.as_tmlt_type?{header:'缺陷等级 '+'<font color=red>*</font>',dataIndex:'dictype',hidden:false,width:100
			      ,editor:new FHD.ux.dict.DictSelectForEditGrid({
					    dictTypeId:'ca_defect_level',fieldLabel:'',editable:false,
					}),allowBlank:false,
			      renderer:function(value){ 
					    var curModel = assessStore.findRecord("id",value);
					    if(curModel!=null){
					    	return curModel.raw.name;
						}
				  }}:{dataIndex:'dictype',width:0},
	    			  //{header:'所属评价标准模板',dataIndex:'parentName',hidden:false,editor:false,flex:1},
	    			  {dataIndex:'parentId',width:0},
				      {header:'描述 '+'<font color=red>*</font>',dataIndex:'content',hidden:false,editor: {allowBlank: false},flex:2},
			      'sample_assessment_standard'==me.as_tmlt_type?{header:'最小值 '+'<font color=red>*</font>',dataIndex:'minValue',hidden:false,editor:{
							xtype:'numberfield',
							allowBlank:false,
							minValue: 1,  
							allowDecimals: true, // 允许小数点 
							nanText: FHD.locale.get('fhd.risk.baseconfig.inputInteger'),  
							//hideTrigger: true,  //隐藏上下递增箭头
							keyNavEnabled: true,  //键盘导航
							mouseWheelEnabled: true,  //鼠标滚轮
							step:1
			      },width:60}:{dataIndex:'minValue',width:0},
			      //根据模板类型 决定是否显示列
			      'sample_assessment_standard'==me.as_tmlt_type?{header:'最大值 '+'<font color=red>*</font>',dataIndex:'maxValue',hidden:false,editor:{
							xtype:'numberfield',
							allowBlank:false,
							minValue: 1,  
							allowDecimals: true, // 允许小数点 
							nanText: FHD.locale.get('fhd.risk.baseconfig.inputInteger'),  
							//hideTrigger: true,  //隐藏上下递增箭头
							keyNavEnabled: true,  //键盘导航
							mouseWheelEnabled: true,  //鼠标滚轮
							step:1
			      },width:60}:{dataIndex:'maxValue',width:0},
			      'sample_assessment_standard'==me.as_tmlt_type?{header:'判定值 '+'<font color=red>*</font>',dataIndex:'judgeValue',hidden:false,editor:{
							xtype:'numberfield',
							allowBlank:false,
							minValue: 1,  
							allowDecimals: true, // 允许小数点 
							nanText: FHD.locale.get('fhd.risk.baseconfig.inputInteger'),  
							//hideTrigger: true,  //隐藏上下递增箭头
							keyNavEnabled: true,  //键盘导航
							mouseWheelEnabled: true,  //鼠标滚轮
							step:1
			      },width:60}:{dataIndex:'judgeValue',width:0},
				      {header:'排序',dataIndex:'sort',hidden:false,editor:{
								xtype:'numberfield',
								allowBlank:false,
								minValue: 1,  
								allowDecimals: false, // 允许小数点 
								nanText: FHD.locale.get('fhd.risk.baseconfig.inputInteger'),  
								//hideTrigger: true,  //隐藏上下递增箭头
								keyNavEnabled: true,  //键盘导航
								mouseWheelEnabled: true,  //鼠标滚轮
								step:1
				      },width:80}
				      ];
	        me.on('selectionchange', function () {
	            me.setstatus()
	        });
	        me.on('edit', function () {
	            me.setstatus()
	        });
	    	me.callParent(arguments);
	    },
	    reloadData :function(id){//刷新grid
			var me = this;
			me.store.proxy.extraParams.assessGuidelinesId =  id || me.store.proxy.extraParams.assessGuidelinesId ;
			me.store.proxy.url = me.findUrl;
			me.store.load();
		}
//	    validate : function(arr){//判断是否为空
//	    	var flag = true;
//	    	if(!arr.length)flag = false;
//	    	for(var p in arr){
//	    		delete(arr[p]['']);
//		    	for(var k in arr[p]){
//		    		if('id'!=k){
//		    			if(!(arr[p][k].toString().replace(/(^\s*)|(\s*$)/g, ""))){
//		    				flag = false;
//		    			}
//		    		}
//		    	}
//	    	}
//	    	return flag;
//	    }
});
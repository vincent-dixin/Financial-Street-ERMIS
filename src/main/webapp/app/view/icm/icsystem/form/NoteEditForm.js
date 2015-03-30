/**
 * 流程基本信息编辑页面
 * 
 * @author 宋佳
 */
Ext.define('FHD.view.icm.icsystem.form.NoteEditForm', {
	extend: 'Ext.form.Panel',
	alias: 'widget.noteeditform',
	requires: [
		'FHD.view.icm.icsystem.component.ParentNoteEditGrid',
		'FHD.view.icm.icsystem.component.AssessPointEditGrid'
	],
	frame: false,
	border : false,
	autoScroll : true,
	initParam:function(paramObj){
		var me = this;
		me.paramObj = paramObj;
	},
	addComponent: function () {
    	var me = this;
    	//基本信息fieldset
        me.basicinfofieldset = Ext.widget('fieldset', {
            flex:1,
            collapsible: false,
            autoHeight: true,
            autoWidth: true,
            defaults: {
                columnWidth : 1 / 2,
                margin: '7 30 3 30',
                labelWidth: 95
            },
            layout: {
                type: 'column'
            },
            title: '节点信息'
        });
        me.add(me.basicinfofieldset);
        //维护上级节点
//        me.parentNotefieldset = Ext.widget('fieldset', {
//            flex:1,
//            collapsible: true,
//            autoHeight: true,
//            autoWidth: true,
//            defaults: {
//                columnWidth : 1,
//                labelWidth: 95
//            },
//            layout: {
//                type: 'column'
//            },
//            title: '维护上级节点',
//            collapsed : false
//        });
//        me.parentnoteeditgrid =  Ext.widget('parentnoteeditgrid');    //将展示父节点组件创建
//        me.parentNotefieldset.add(me.parentnoteeditgrid);
//        me.add(me.parentNotefieldset);
        //维护评价店
        me.assesspointfieldset = Ext.widget('fieldset', {
            flex:1,
            autoHeight: true,
            collapsible: false,
            autoWidth: true,
            defaults: {
                columnWidth : 1,
                labelWidth: 95
            },
            layout: {
                type: 'column'
            },
            title: '评价点列表'+ '<font color=red>*</font>',
            collapsed : false
        });
        me.add(me.assesspointfieldset);
        me.assesspointeditgrid =  Ext.widget('assesspointeditgrid');
        me.assesspointfieldset.add(me.assesspointeditgrid);
        //父级流程id
        me.parentid = Ext.widget('textfield', {
            name : 'parentid',
            value: '',
            columnWidth: .5,
            hidden : true
        });
        //存放可编辑列表的数据 
        me.editGridJson = Ext.widget('textfield', {
            name : 'editGridJson',
            value: '',
            columnWidth: .5,
            hidden : true
        });
        //存放评价点保存信息
        me.assessEditGridJson = Ext.widget('textfield', {
            name : 'assessEditGridJson',
            value: '',
            columnWidth: .5,
            hidden : true
        });
        //父级流程id
        me.basicinfofieldset.add(me.parentid,me.editGridJson,me.assessEditGridJson);
        me.editProcessPointId = Ext.widget('textfield', {name: 'editProcessPointId',hidden : true });
        me.basicinfofieldset.add(me.editProcessPointId);
        me.code = Ext.widget('textfield', {
            name : 'code',
            fieldLabel : '节点编号' + '<font color=red>*</font>',
            value: '',
            allowBlank: false,
            columnWidth: .4
        });
	     // 初始化方法
	    me.standardCreateCodeButton = {
             xtype: 'button',
             margin: '7 30 3 3',
             id:'standardCreateCodeButtonId',
             text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.autoCode'),
             handler: function(){
       			 FHD.ajax({
	                 url:__ctxPath+'/ProcessPoint/ProcessPoint/ProcessPointCode.f',
	                 params: {
	                 	processId : me.processId,
	                 	ProcessPointId : me.processPointId
                 	 },
	                 callback: function (data) {
	                 	 me.getForm().setValues({'code':data.data.code});//给code表单赋值
	                 }
                 	 });
	             },
	             columnWidth: .1
         	 };
		me.basicinfofieldset.add(me.code,me.standardCreateCodeButton);
		//名称
		me.notename = Ext.widget('textfield', {
			fieldLabel : '节点名称' + '<font color=red>*</font>',
			allowBlank : false,
			value : '',
			name : 'name'
		});
        me.basicinfofieldset.add(me.notename);
        /*节点类型*/
		me.notestyle = Ext.create('FHD.ux.dict.DictSelect', {
			name : 'pointType',
			dictTypeId : 'ca_point_type',
			multiSelect : false,
			labelWidth: 100,
			labelAlign : 'left',
			fieldLabel : '节点类型'
		});
        /*流程接口*/
//		me.flowinterface = Ext.create('FHD.ux.process.processSelector', {
//			name : 'relaProcess',
//			multiSelect : false,
//	        fieldLabel: '选择流程'
//		});
        /*责任部门  */
		me.noteDepart = Ext.create('FHD.ux.org.CommonSelector', {
			fieldLabel : '责任部门' + '<font color=red>*</font>',
			name:'orgId',
			type : 'dept',
			allowBlank : false,
			multiSelect : false
		});
        me.basicinfofieldset.add(me.noteDepart);
        /*员工单选 */
		me.noteradio = Ext.create('FHD.ux.org.CommonSelector', {
			fieldLabel : '责&nbsp;&nbsp;任&nbsp;人'+ '<font color=red>*</font>',
			labelAlign : 'left',
			name:'empId',
			allowBlank : false,
			type : 'emp',
			multiSelect : false
		});
        me.basicinfofieldset.add(me.noteradio);
         /*配合部门  */
		me.noteCrdDepart = Ext.create('FHD.ux.org.CommonSelector', {
			fieldLabel : '配合部门',      
			height : 50,
			name:'CrdorgId',
			type : 'dept',
			multiSelect : true
		});
		me.basicinfofieldset.add(me.noteCrdDepart);
		//节点描述
        me.desc = Ext.widget('textareafield', {
			height:50,
			fieldLabel : '节点描述',
			value : '',
			name : 'desc'
        });
        me.basicinfofieldset.add(me.desc);
		//排序
		me.notesort = Ext.widget('numberfield', {
			fieldLabel : '排&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;序',
			name : 'sort'
		});
		me.basicinfofieldset.add(me.notesort);
	    },
	reloadData: function() {
        var me = this;
        me.load({
            waitMsg: FHD.locale.get('fhd.kpi.kpi.prompt.waiting'),
            url: __ctxPath + '/ProcessPoint/ProcessPoint/editProcessPoint.f',
            params: {
                processId: me.paramObj.processId,
                processEditID: me.paramObj.processPointId
            },
            success: function (form, action) {
                return true;
            }
	    });
//	    me.parentnoteeditgrid.initParam({
//			processId: me.paramObj.processId,
//            processPointId: me.paramObj.processPointId
//	    });
//	    me.parentnoteeditgrid.reloadData();
	    me.assesspointeditgrid.initParam({
	    	processId: me.paramObj.processId,
            processPointId: me.paramObj.processPointId
	    });
	    me.assesspointeditgrid.reloadData();
	},
    // 初始化方法
    initComponent: function() {
       var me = this;
       Ext.applyIf(me, {
    	   bbar: {
               items: ['->',	
	               {   
	            	   text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.undo'),
	                   iconCls: 'icon-operator-home',
	                   handler: me.cancel
	               },
	               	{   
	            	   text: FHD.locale.get("fhd.common.save"),
	                   iconCls: 'icon-control-stop-blue',
	                   handler: me.save
	               }
               ]
           }
       });
       me.callParent(arguments);
       //向form表单中添加控件
	   me.addComponent();
    },
	save: function() {
	   	var me = this.up('noteeditform');
	   	var processPointForm = me.getForm();
    	me.noteDepart.renderBlankColor(me.noteDepart);
    	me.noteradio.renderBlankColor(me.noteradio);
    	if(processPointForm.isValid()) {
		   	processPointForm.setValues({//paramObj
		               parentid: me.paramObj.processId,
		               editProcessPointId : me.paramObj.processPointId
		               }); //editGridJson
//		    var rows = me.parentnoteeditgrid.store.getModifiedRecords();
//		    var jsonArray=[];
//				Ext.each(rows,function(item){
//					jsonArray.push(item.data);
//				});
//			processPointForm.setValues({
//	               editGridJson : Ext.encode(jsonArray)
//	               }); //editGridJson
    		var allRows = me.assesspointeditgrid.store.getCount();
    		if(allRows == 0){
	    		Ext.Msg.alert(FHD.locale.get('fhd.common.prompt'), '评价点列表不能为空!');
	    		return false;
	    	}
		    var assessrows = me.assesspointeditgrid.store.getModifiedRecords();
		    var jsonArray=[];
				Ext.each(assessrows,function(item){
					jsonArray.push(item.data);
				});
			processPointForm.setValues({
                assessEditGridJson : Ext.encode(jsonArray)
            }); //editGridJson
    		FHD.submit({
				form : processPointForm,
				url : __ctxPath + '/processpoint/processpoint/saveprocesspoint.f',
				callback: function (data) {
					me.cancel();
				}
			});
		}
	   },
	cancel : function(){
   	    me = this.up('flownotemainpanel');
   	    me.getLayout().setActiveItem(0);
   	    me.flownotelist.reloadData();
     }
});
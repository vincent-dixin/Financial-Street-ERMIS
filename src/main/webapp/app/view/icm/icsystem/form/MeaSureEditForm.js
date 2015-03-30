/**
 * 控制措施基本信息
 * 
 * @author 宋佳
 */
Ext.define('FHD.view.icm.icsystem.form.MeaSureEditForm', {
	extend: 'Ext.form.Panel',
	alias: 'widget.measureeditform',
	requires: [
		'FHD.view.icm.icsystem.component.AssessPointEditGrid'
	],
	frame: false,
	border : false,
	paramObj : {
		measureId : ""
	},
	autoScroll : false,
	autoHeight : true,
	initParam:function(paramObj){
  	var me = this;
		me.paramObj = paramObj;
	},
    addComponent: function () {
		var me = this;
		//存放可编辑列表的数据 
		me.editGridJson = Ext.widget('textfield', {
		    name : 'editGridJson',
		    value: '',
		    columnWidth: .5,
		    hidden : true
		});
		//控制信息
		me.mainfieldset = Ext.widget('fieldset', {
			flex:1,
			collapsible: false,
			autoHeight: true,
			autoWidth: true,
			defaults: {
			    columnWidth : 1
			},
			layout: {
			    type: 'column'
			},
			title: '<input id="c1c"  type="checkbox" onclick="Ext.getCmp(\'measureeditform'+me.num+'\').select1(this.checked)"/></img>控制措施信息'
		});
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
		    title: '基本信息'
		});
    	//基本信息fieldset
        me.moreinfofieldset = Ext.widget('fieldset', {
            flex:1,
            collapsible: true,
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
            title: '更多信息',
            collapsed : true
        });
//	        me.add(me.basicinfofieldset);
        me.code = Ext.widget('textfield', {
            name : 'measurecode',
            fieldLabel : '控制措施编号' + '<font color=red>*</font>',
            value: '',
            allowBlank: false,
            columnWidth: .4
        });
	     // 初始化方法
	    me.measureCreateCodeBtn = {
             xtype: 'button',
             margin: '7 30 3 3',
             text: FHD.locale.get('fhd.strategymap.strategymapmgr.form.autoCode'),
             handler: function(){
       			 FHD.ajax({
	                 url:__ctxPath+'/ProcessPoint/ProcessPoint/ProcessPointCode.f',
	                 params: {
	                 	processId : me.processId,
	                 	processRiskId : me.processRiskId
                 	 },
                 callback: function (data) {
                 	 me.getForm().setValues({'measurecode':data.data.code});//给code表单赋值
                 }
                 });
	             },
	             columnWidth: .1
         	};
        me.basicinfofieldset.add(me.code,me.measureCreateCodeBtn,me.editGridJson);
        //控制措施内容
        me.desc = Ext.widget('textareafield', {
			height:60,
			rows : 3,
			fieldLabel : '控制措施内容'+ '<font color=red>*</font>',
			value : '',
			allowBlank: false,
			name : 'meaSureDesc',
			columnWidth: .5
        });
        me.basicinfofieldset.add(me.desc);
        /*责任部门  */
		me.noteDepart = Ext.create('FHD.ux.org.CommonSelector', {
			fieldLabel : '责任部门' + '<font color=red>*</font>',
			name:'meaSureorgId',
			type : 'dept',
			multiSelect : false,
			allowBlank: false,
			value : '[{"id":"'+me.measureInitOrgId+'"}]'
		});
        me.basicinfofieldset.add(me.noteDepart);
       	/*员工单选 */
		me.noteradio = Ext.create('FHD.ux.org.CommonSelector', {
			fieldLabel : '责&nbsp;&nbsp;任&nbsp;&nbsp;人'+ '<font color=red>*</font>',
			labelAlign : 'left',
			name:'meaSureempId',
			type : 'emp',
			allowBlank: false,
			multiSelect : false,
			value : '[{"id":"'+me.measureInitEmpId+'"}]'
		});
        me.basicinfofieldset.add(me.noteradio);
        //me.extraParams.processPointId = Ext.getCmp('flownotelist').selectId;
    	me.processStore=Ext.create('Ext.data.Store',{//myLocale的store
			fields : ['id', 'name'],
			proxy : {
				type : 'ajax',
				url : __ctxPath + '/processpoint/findallprocesspointbyprocessid.f',
				extraParams:{
	    			processId : me.processId
	    		}
			}
		});
		me.processStore.load();
		me.pointNote = new Ext.form.field.ComboBox({
			fieldLabel : '对应的流程节点'+ '<font color=red>*</font>',
			name : 'pointNote',
			store :me.processStore,
			valueField : 'id',
			displayField : 'name',
			allowBlank : false,
			multiSelect : true,
			editable : false,
			listeners: {
				collapse: function(){
				if(this.value!=''){
					this.value = this.value.toString();
					}
				}
 			}
		});
    	me.basicinfofieldset.add(me.pointNote);
        /*节点类型*/
		/*me.notestyle = Ext.create('FHD.ux.dict.DictSelect', {
			name : 'pointType',
			dictTypeId : 'ca_point_type',
			multiSelect : false,
			fieldLabel : '节点类型'
		});
		me.basicinfofieldset.add(me.notestyle);*/
		/* 是否关键节点 */
		me.isKeyPoint = Ext.create('FHD.ux.dict.DictRadio',
		{
		 	margin : '7 5 5 30',
			name:'isKeyPoint',
			dictTypeId:'0yn',
			lableWidth:100,labelAlign:'left',
			fieldLabel : '是否关键控制点'+ '<font color=red>*</font>'
		});
		me.basicinfofieldset.add(me.isKeyPoint);
		me.controlTarget = Ext.widget('textfield', {
            name : 'controlTarget',
            fieldLabel : '控制目标',
            value: '',
            columnWidth: .5
        });
        me.moreinfofieldset.add(me.controlTarget);
		//实施证据
		me.measureControl = Ext.widget('textfield', {
            name : 'implementProof',
            fieldLabel : '实施证据',
            value: '',
            columnWidth: .5
        });
        me.moreinfofieldset.add(me.measureControl);
        //控制点 
		me.controlPoint = Ext.widget('textfield', {
            name : 'controlPoint',
            fieldLabel : '控制点' ,
            value: '',
            columnWidth: .5
        });
        me.moreinfofieldset.add(me.controlPoint);
        /*控制频率  dict */
		me.controlFrequency = Ext.create('FHD.ux.dict.DictSelect',
		{
			name:'controlFrequency',
			dictTypeId:'ic_control_frequency',
			labelAlign : 'left',
			fieldLabel : '控制频率',
			columnWidth: .5
		});
		me.moreinfofieldset.add(me.controlFrequency);
		/* 控制方式 */
		me.controlMeasure = Ext.create('FHD.ux.dict.DictSelect', {
			name : 'controlMeasure',
			dictTypeId : 'ic_control_measure',
			multiSelect : false,
			labelAlign : 'left',
			fieldLabel : '控制方式',
			columnWidth: .5
		});
		me.moreinfofieldset.add(me.controlMeasure);
		//维护评价店
        me.assesspointfieldset = Ext.widget('fieldset', {
            flex:1,
            autoHeight : true,
            collapsible : false,
            collapsed : false,
            autoWidth: true,
            defaults: {
                columnWidth : 1,
                labelWidth: 95
            },
            layout: {
                type: 'column'
            },
            title: '评价点列表'+'<font color=red>*</font>'
        });
        me.assesspointeditgrid =  Ext.widget('assesspointeditgrid',{
			type : 'E'
	    });    //将展示父节点组件创建
	    me.assesspointeditgrid.initParam({
	    	processId : me.processId,
			measureId : me.measureId
	    });
        me.assesspointfieldset.add(me.assesspointeditgrid);
        me.mainfieldset.add(me.basicinfofieldset);
        me.mainfieldset.add(me.moreinfofieldset);
        me.mainfieldset.add(me.assesspointfieldset);
        me.add(me.mainfieldset);
//	        me.basicinfofieldset.add(me.assesspointfieldset);
        },
	    reloadData: function() {
	        var me = this;
	        me.load({
	            waitMsg: FHD.locale.get('fhd.kpi.kpi.prompt.waiting'),
	            url: __ctxPath + '/processrisk/loadmeasureeditformdata.f',
	            params: {
	                measureId : me.paramObj.measureId
	            },
	            success: function (form, action) {
	                return true;
	            }
	        });
	        me.assesspointeditgrid.reloadData();
	    },
	    select1 : function(tt){
		    var me = this;
		   	if(tt){
		   		me.up('riskeditform').selectArray.push(me.num);
		   	}else{
		   		Ext.Array.remove(me.up('riskeditform').selectArray,me.num);
		   	}
	    },
       // 初始化方法
       initComponent: function() {
           var me = this;
           Ext.applyIf(me);
           me.callParent(arguments);
           //向form表单中添加控件
		   me.addComponent();
       }
});
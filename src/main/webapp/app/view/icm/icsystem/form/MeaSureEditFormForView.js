/**
 * 控制措施基本信息只读
 * @author 宋佳
 */
Ext.define('FHD.view.icm.icsystem.form.MeaSureEditFormForView', {
    extend: 'Ext.form.Panel',
    alias: 'widget.measureeditformforview',
    requires: [
		'FHD.view.icm.icsystem.component.AssessPointEditGridForView'
   	],
   	frame: false,
   	border : false,
   	paramObj : {
   	   	measureId : ""
   	},
   	autoScroll : true,
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
			title: '控制措施信息'
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
            title: '更多信息'
        });
//	        me.add(me.basicinfofieldset);
        me.code = Ext.widget('displayfield',{
            name : 'measurecode',
            fieldLabel : '控制措施编号',
            value: '',
            allowBlank: false
        });
        me.basicinfofieldset.add(me.code);
        //控制措施内容
        me.desc = Ext.widget('textareafield', {
			fieldLabel : '控制措施内容',
			readOnly : true,
			name : 'meaSureDesc',
			columnWidth: .5
        });
        me.basicinfofieldset.add(me.desc);
        /*责任部门  */
		me.noteDepart = Ext.widget('displayfield',{
			fieldLabel : '责任部门' ,
			name:'orgName',
			type : 'dept',
			multiSelect : false,
			allowBlank: false
		});
        me.basicinfofieldset.add(me.noteDepart);
       	/*员工单选 */
		me.noteradio = Ext.widget('displayfield',{
			fieldLabel : '责&nbsp;&nbsp;任&nbsp;&nbsp;人',
			name:'empName'
		});
        me.basicinfofieldset.add(me.noteradio);
		me.pointNote = Ext.widget('displayfield',{
			fieldLabel : '对应的流程节点',
			name : 'pointNote'
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
		me.isKeyPoint = Ext.widget('displayfield',{
		 	margin : '7 5 5 30',
			name:'isKeyPoint',
			fieldLabel : '是否关键控制点'
		});
		me.basicinfofieldset.add(me.isKeyPoint);
		me.controlTarget = Ext.widget('displayfield', {
            name : 'controlTarget',
            fieldLabel : '控制目标',
            readOnly : true
        });
        me.moreinfofieldset.add(me.controlTarget);
		//实施证据
		me.measureControl = Ext.widget('displayfield', {
            name : 'implementProof',
            fieldLabel : '实施证据',
            columnWidth: .5
        });
        me.moreinfofieldset.add(me.measureControl);
        //控制点 
		me.controlPoint = Ext.widget('displayfield', {
            name : 'controlPoint',
            fieldLabel : '控制点'
        });
        me.moreinfofieldset.add(me.controlPoint);
        /*控制频率  dict */
		me.controlFrequency = Ext.widget('displayfield', {
			name:'controlFrequency',
			fieldLabel : '控制频率'
		});
		me.moreinfofieldset.add(me.controlFrequency);
		/* 控制方式 */
		me.controlMeasure = Ext.widget('displayfield', {
			name : 'controlMeasure',
			fieldLabel : '控制方式'
		});
		me.moreinfofieldset.add(me.controlMeasure);
        /* 影响财报科目 */
		me.relaSubject = Ext.widget('displayfield', {
			name : 'relaSubject',
			fieldLabel : '影响财报科目'
		});
//			me.moreinfofieldset.add(me.relaSubject);       
			 //维护评价店
        me.assesspointfieldset = Ext.widget('fieldset', {
            flex:1,
            autoHeight: true,
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
            title: '评价点列表'
        });
        me.assesspointeditgrid =  Ext.widget('assesspointeditgridforview',{
	    	processId : me.processId,
			measureId : me.measureId,
			type : 'E'
	    });    //将展示父节点组件创建
        me.assesspointfieldset.add(me.assesspointeditgrid);
        me.mainfieldset.add(me.basicinfofieldset);
        me.mainfieldset.add(me.moreinfofieldset);
        me.mainfieldset.add(me.assesspointfieldset);
        me.add(me.mainfieldset);
        },
	    reloadData: function() {
	        var me = this;
	        me.load({
	            waitMsg: FHD.locale.get('fhd.kpi.kpi.prompt.waiting'),
	            url: __ctxPath + '/processrisk/loadmeasureeditformdataforview.f',
	            params: {
	                measureId : me.paramObj.measureId
	            },
	            success: function (form, action) {
	                return true;
	            }
	         });
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
/**
 * 风险预览页面
 * 
 * @author 宋佳
 */
Ext.define('FHD.view.icm.icsystem.form.RiskEditFormForView', {
   	extend: 'Ext.form.Panel',
   	alias: 'widget.riskeditformforview',
  	requires: [
      	'FHD.view.icm.icsystem.form.MeaSureEditFormForView'	
   	],
   	frame: false,
   	border : false,
   	bodyPadding: "0 3 3 3",
   	paramObj : {
   		processId : "",
   		processRiskId : "",
   		measureId : ""
   	},
   	selectArray : [],
   	measureeditform : [],
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
            title: '风险信息'
	        });
        me.add(me.basicinfofieldset);
		//风险编号 
		me.code = Ext.widget('displayfield', {
            name : 'code',
            fieldLabel : '风险编号'
        });
        me.basicinfofieldset.add(me.code);
        //风险名称
        me.desc = Ext.widget('displayfield', {
			fieldLabel : '风险名称',
			name : 'name'
        });
        me.basicinfofieldset.add(me.desc);
		//风险描述
        me.desc = Ext.widget('displayfield', {
			fieldLabel : '风险描述',
			name : 'desc'
        });
        me.basicinfofieldset.add(me.desc);
        me.add(me.basicinfofieldset);
	},
	reloadData: function() {
		var me = this;
		me.load({
		    waitMsg: FHD.locale.get('fhd.kpi.kpi.prompt.waiting'),
		    url: __ctxPath + '/processrisk/loadriskeditformdata.f',
		    params: {
		        processRiskId: me.paramObj.processRiskId
		    },
		    success: function (form, action) {
		        return true;
		    }
        });
    },
    // 初始化方法
    initComponent: function() {
        var me = this;
        Ext.applyIf(me);
        me.callParent(arguments);
        //向form表单中添加控件
	    me.addComponent();
    },
	getInitData : function(){
   	    var me = this;
   		me.measureeditform = [];
   		FHD.ajax({
			url:__ctxPath+'/processrisk/findmeasureidbyriskid.f',
			params: {
				processId: me.paramObj.processId,
				processRiskId : me.paramObj.processRiskId
			},
	     	callback: function (data) {
				me.paramObj.measureId = data.data;
				for(var i = 0;i<me.paramObj.measureId.length;i++){
					me.editform = Ext.widget('measureeditformforview',{id:'measureeditformforview'+me.measureeditform.length,processId:me.paramObj.processId,measureId:me.paramObj.measureId[i],num:me.measureeditform.length});
					me.measureeditform.push(me.editform);
					me.editform.initParam({
						measureId : me.paramObj.measureId[i]
					});
					me.measureeditform[i].reloadData();
					me.add(me.measureeditform[i]);
	             }
	         }
         });
	   }
});
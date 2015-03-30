/**
 * 编辑样本
 */
Ext.define('FHD.view.icm.assess.form.SampleTestForm',{
	extend: 'Ext.form.Panel',
    alias: 'widget.sampletestform',
    
    autoScroll:true,
    
	initComponent : function() {
	    var me = this;
	    
	    me.isQualifiedStore = Ext.create('Ext.data.Store',{
			fields : ['id', 'name'],
			data : [
			    {'id' : 'Y','name' : '是'},
			    {'id' : 'N','name' : '否'},
			    {'id':'NAN','name':'不适用'}
			]
		});
	    
	    me.basicInfo=Ext.create('FHD.view.icm.assess.form.AssessResultPreviewForm',{
			columnWidth:1/1,
			assessResultId:me.assessResultId
		});
	    
	    if('sampling' == me.type){
	    	//抽样测试
	    	me.sampletext={
	    		margin: '0 10 0 30',
    			xtype:'textfield',
    	        fieldLabel: '编号前缀',
    	        name: 'sampletext',
            	columnWidth:1/4
        	};
    		me.startNum={
    			margin: '0 10 0 0',
            	xtype: 'numberfield',
            	name: 'startNum',
            	fieldLabel: '编号期间',
            	columnWidth:1/4,
		        minValue: 1,
		        listeners: {
		        	change: function(field, value) {  
		        		value = parseInt(value,10);  
		        		field.setValue(value);  
		        	}
		        }
        	};
    		me.stopNum={
    			margin: '0 10 0 0',
            	xtype: 'numberfield',
            	name: 'stopNum',
            	fieldLabel: '至',
            	columnWidth:1/4,
		        minValue: 1,
		        listeners: {
		        	change: function(field, value) {  
		        		value = parseInt(value,10);  
		        		field.setValue(value);  
		        	}
		        }
        	};
    		me.button={
            	xtype:'button',
            	margin: '0 30 0 0',
            	text: '生成',
            	tooltip: '生成抽样测试样本',
            	columnWidth:1/8,
            	handler:function(){
            		if(me.validateParams()){
            			FHD.ajax({
            				url:__ctxPath +'/icm/assess/saveSample.f',
                            params: {
                            	sampletext:me.getValues().sampletext,
                            	startNum:me.getValues().startNum,
                            	stopNum:me.getValues().stopNum,
                            	assessResultId:me.assessResultId,
                            	assessPlanId:me.businessId,
                            	processId:me.processId
                            },
                            callback: function (data) {
                            	if(data){
                            		me.sampleEditGrid.store.load();
                            	}
                            }
            			});
            		}
            	}
    	    };
	    	me.samplingFieldSet={
    			xtype : 'fieldset',
    			margin: '0 10 0 10',
    			layout:'column',
    			collapsed : false,
    			collapsible : true,
    			title: '生成样本',
    			items :[me.sampletext,me.startNum,me.stopNum,me.button]
    		};
	    }

	    me.sampleEditGrid=Ext.create('FHD.view.icm.assess.component.SampleTestEditGrid',{
	    	//id:'icm_assess_sampletesteditgrid',
	    	//type:me.type,
    		//businessId:me.businessId,
    		assessResultId:me.assessResultId
    	});

		me.callParent(arguments);
		
		//评价点信息
		me.add(me.basicInfo);
		if('sampling' == me.type){
			//生成样本
			me.add(me.samplingFieldSet);
			
			me.sampleFieldset = {
				xtype : 'fieldset',
				margin: '10 10 0 10',
				collapsed : false,
				layout:'anchor',
				collapsible : true,
				title: '样本列表',
				items :[me.sampleEditGrid]
			};
			//样本列表
			me.add(me.sampleFieldset);
		}else{
			me.sampleFieldset = {
				xtype : 'fieldset',
				margin: '10 10 10 10',
				collapsed : false,
				collapsible : true,
				title: '样本列表',
				items :[me.sampleEditGrid]
			};
			//样本列表
			me.add(me.sampleFieldset);
		}
	},
	validateParams:function(){
		var me=this;
		
		var sampletext = me.getValues().sampletext;
    	var startNum = me.getValues().startNum;
    	var stopNum = me.getValues().stopNum;
    	
    	var validateFlag=false;
		var message = '';
    	if(sampletext=='' || sampletext==null || sampletext==undefined){
    		message += "'编号前缀'不能为空!<br/>";
			validateFlag=true;
    	}
    	if(startNum=='' || startNum==null || startNum==undefined || stopNum=='' || stopNum==null || stopNum==undefined){
    		message += "'编号期间'不能为空!<br/>";
			validateFlag=true;
    	}
    	if(validateFlag){
 			Ext.Msg.alert(FHD.locale.get('fhd.common.prompt'), message);
 			return false;
 		}else{
 			return true;
 		}
	},
	loadData:function(assessResultId){
		var me=this;
		
		//评价点load
		me.basicInfo.getForm().load({
            url: __ctxPath+'/icm/assess/findAssessPointViewByAssessResultId.f?assessResultId='+assessResultId,
            success: function (form, action) {
         	   return true;
            },
            failure: function (form, action) {
         	   return false;
            }
		});
		
		if('sampling' == me.type){
	    	//抽样测试,编号前缀，编号期间清空
			me.clearValues();
		}
		
		me.sampleEditGrid.store.proxy.extraParams.assessResultId = assessResultId;
		me.sampleEditGrid.store.load();
	},
	clearValues:function(){
		var me=this;
		me.getForm().reset();
	},
	reloadData:function(){
		var me=this;
		
	}
});
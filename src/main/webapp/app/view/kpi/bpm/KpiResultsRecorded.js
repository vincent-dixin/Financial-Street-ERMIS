
Ext.define('FHD.view.kpi.bpm.KpiResultsRecorded',{
    extend: 'Ext.panel.Panel',
    alias: 'widget.kpiresultsrecorded',
	
	hiddenStart:1,
    
    initComponent : function() {
    	var me = this;
    	
    	me.totalLabel = Ext.widget('label' ,{ text: FHD.locale.get('fhd.kpi.kpi.kpiTotal')+':'+'0'+FHD.locale.get('fhd.common.units.ge') });
    	
    	me.needLabel = Ext.widget('label' ,{ text: FHD.locale.get('fhd.kpi.kpi.needCount')+':'+'0'+FHD.locale.get('fhd.common.units.ge') });
    	
    	me.finishLabel = Ext.widget('label' ,{ text: FHD.locale.get('fhd.kpi.kpi.finishCount')+':'+'0'+FHD.locale.get('fhd.common.units.ge') });
    	
    	me.preBtn = Ext.widget('button' ,{ 
    		text: FHD.locale.get('fhd.common.previousPage'),
    		iconCls: 'icon-control-rewind-blue',
    		handler: function () {
    			var start = me.hiddenStart - 1;
    			me.hiddenStart = start;
    			me.saveKpiGatherResult(true,start,3);
            }
    	});
    	
    	me.nextBtn = Ext.widget('button' ,{ 
    		text: FHD.locale.get('fhd.common.nextPage'),
    		iconCls: 'icon-control-fastforward-blue',
    		handler: function () {
    			var start = me.hiddenStart +1;
    			me.hiddenStart = start;
    			me.saveKpiGatherResult(true,start,3);
            }
    	});
    	
    	me.saveBtn = Ext.widget('button' ,{ 
    		text: FHD.locale.get('fhd.common.save'),
    		iconCls: 'icon-control-fastforward-blue',
    		handler: function () {
    			me.saveKpiGatherResult(true,me.hiddenStart,3);
            }
    	});
    	
    	me.submitBtn = Ext.widget('button' ,{ 
    		text: FHD.locale.get('fhd.common.submit'),
    		iconCls: 'icon-control-stop-blue',
    		handler: function () {
             	Ext.MessageBox.show({
    	            title: FHD.locale.get('fhd.common.prompt'),
    	            width: 260,
    	            msg: FHD.locale.get('fhd.common.submitPrompt'),
    	            buttons: Ext.MessageBox.YESNO,
    	            icon: Ext.MessageBox.QUESTION,
    	            fn: function (btn) {
    	                if (btn == 'yes') {
    	                	me.saveKpiGatherResult(false,1,3);
    	                }
    	            }
    	        });
            }
    	});
    	Ext.apply(me,{
    		tbar : [
    			'->',me.totalLabel,'-',me.needLabel,'-',me.finishLabel,'-',me.preBtn,me.nextBtn,me.saveBtn,me.submitBtn
    		],
    		autoScroll:true
    	});

    	FHD.ajax({
            url: __ctxPath + '/kpi/kpi/findTargetOrGatherKpiByEmpId.f?limit=3&start=1',
            params: {
            	executionId: me.executionId
            },
            callback: function (response) {
                if (response.datas && '' != response.datas) {
                	//设置指标总数
                	me.totalLabel.setText(FHD.locale.get('fhd.kpi.kpi.kpiTotal')+':'+response.totalCount+FHD.locale.get('fhd.common.units.ge'));
                	//设置指标需录入数
                	me.needLabel.setText(FHD.locale.get('fhd.kpi.kpi.needCount')+':'+response.needCount+FHD.locale.get('fhd.common.units.ge'));
                	//设置指标已录入数
                	me.finishLabel.setText(FHD.locale.get('fhd.kpi.kpi.finishCount')+':'+response.finishCount+FHD.locale.get('fhd.common.units.ge'));
                	//获得start,isNext设置按钮状态
                	me.setButtonStatus(me.hiddenStart, response.isNext);
                	Ext.each(response.datas, function (item, index){
	                	//js动态生成需要数据
	                	var dymicFieldSet = me.createFiledSet(item, index);
                        	
                        me.add(dymicFieldSet);
	                });
                }else{
                	me.preBtn.setDisabled(true);
                    me.nextBtn.setDisabled(true);
                    me.submitBtn.setDisabled(true);
                }
            }
    	});
    	
        me.callParent(arguments);
        
    },
    // 创建行
    createTr: function (type,title,content,name,unit){
    	var me = this;
    	
		var tr = $('<tr />',{
    		
    	});
    	
		var txt1 = me.createTxt(title);
		var td1 = me.createTdTitle(txt1);
		td1.appendTo(tr);
		
		if('txt' == type || false == type){
			var txt2 = me.createTxt(content);
			var td2 = me.createTdContent();
			if(undefined!=unit){
				var label =  $('<label/>');
				label.html(unit);
			}
			txt2.appendTo(td2);
			if(content!=''&&undefined!=unit){
				label.appendTo(td2);
			}
			td2.appendTo(tr);
		}else if('img' == type){
			var img = me.createImg(content,"16","16");
			var td2 = me.createTdContent();
			img.appendTo(td2);
			td2.appendTo(tr);
		}else if(true == type){
			var text = me.createTdText('text',content,name);
			var td3 = me.createTdContent();
			if(undefined!=unit){
				var label =  $('<label/>');
				label.html(unit);
			}
			text.appendTo(td3);
			if(undefined!=unit){
				label.appendTo(td3);
			}
			td3.appendTo(tr);
		}
    	
    	return tr;
    },
    //创建列标题
    createTdTitle: function (title){
		var td = $('<td />',{
			'class':"alt",
			width:"20%",
			html:title
    	});
		return td;
 	},
 	//创建列内容
 	createTdContent: function (){
 		var td = $('<td />',{
			width:"50%"
    	});
		return td;
 	},
 	//创建文本
 	createTxt: function (text){
 		var txt = $('<text />',{
			html:text
    	});
 		return txt;
 	},
 	//创建图片
 	createImg: function (imgPath,width,height){
		var img = $('<img />',{
    		src:__ctxPath + imgPath,
    		width:width,
    		height:height
    	});
 		return img;
 	},
 	//创建文本框
 	createTdText: function (type,value,name){
		var text = $('<input />',{
			type:type,
			name:name,
			value:value
    	});
 		return text;
 	},
 	//创建fieldSet
 	createFiledSet: function (item,index){
    	var me = this;
    	
   		//item.datas[index].kid
   		//console.log(item.kid);
   		var div1 = $('<div />',{
   			style:'width:60%;margin:5px 0px 0px 5px;'
   		});
   		
   		var table = $('<table />',{
   			'class':"fhd_add",
			width:"100%",
			height:"100%",
			border:"1",
			cellSpacing:"0", 
			cellPadding:"0" 
   		}).appendTo(div1);
   		
   		me.createTr("txt",FHD.locale.get('fhd.kpi.kpi.form.targetdept')+":",item.targetOrgEmp).appendTo(table);
		me.createTr("txt",FHD.locale.get('fhd.kpi.kpi.form.gatherdept')+":",item.gatherOrgEmp).appendTo(table);
		if(null != item.preFinishValue){
			me.createTr("txt",FHD.locale.get('fhd.kpi.kpi.form.prefinishValue')+":",item.preFinishValue+item.units).appendTo(table);
		}else{
			me.createTr("txt",FHD.locale.get('fhd.kpi.kpi.form.prefinishValue')+":","").appendTo(table);
		}
		me.createTr("img",FHD.locale.get('fhd.kpi.kpi.previousPeriodStatus')+":",item.assessmentStatus).appendTo(table);
		if('' != item.direction){
			me.createTr("img",FHD.locale.get('fhd.kpi.kpi.previousPeriodTrend')+":",item.direction).appendTo(table);
		}else{
			me.createTr("txt",FHD.locale.get('fhd.kpi.kpi.previousPeriodTrend')+":","").appendTo(table);
		}
		if(item.targetValueEdit){
			me.createTr(item.targetValueEdit,FHD.locale.get('fhd.kpi.kpi.form.targetValue')+":",item.targetValue,'targetValue',item.units).appendTo(table);
		}else{
			me.createTr(item.targetValueEdit,FHD.locale.get('fhd.kpi.kpi.form.targetValue')+":",item.targetValue,'targetValue',item.units).appendTo(table);
			me.createTdText('hidden',"",'targetValue').appendTo(table);
		}
		if(item.finishValueEdit){
			me.createTr(item.finishValueEdit,FHD.locale.get('fhd.kpi.kpi.form.finishValue')+":",item.finishValue,'finishValue',item.units).appendTo(table);
		}else{
			me.createTr(item.finishValueEdit,FHD.locale.get('fhd.kpi.kpi.form.finishValue')+":",item.finishValue,'finishValue',item.units).appendTo(table);
			me.createTdText('hidden',"",'finishValue').appendTo(table);
		}
		me.createTdText('hidden',item.id,'rid').appendTo(table);
   		
   		var historyChart = Ext.create('FHD.ux.FusionChartPanel',{
       		border:true,
       		height : 183,
   			chartType:'MSColumnLine3D',
   			flex:2,
   			xmlData : item.xml,
   			margin: '0 17 0 0'
   		});
   		
       	var tablePanel = Ext.create('Ext.panel.Panel', {
       		flex:3,
            html:div1.html(),
            border:false,
            margin: '0 10 0 0'
        });
   		
       	var myFieldSet = Ext.create('Ext.form.FieldSet',{
       		title : item.kpiName,
       		height:'200',
       		items : [tablePanel,historyChart],
       		margin: '3 8 25 8',
       		layout: {
       			type: 'hbox'
       	    }
       	});
       	
       	return myFieldSet;
 	},
 	//设置按钮状态
 	setButtonStatus: function (start,isNext){
 		var me=this;
 		
 		//判断上一页
    	if(1 == start){
        	me.preBtn.setDisabled(true);
    	}else{
        	me.preBtn.setDisabled(false);
    	}
        
    	//判断下一页
    	if(isNext){
    		me.nextBtn.setDisabled(false);
    	}else{
    		me.nextBtn.setDisabled(true);
    	}
 	},
 	//保存指标采集结果目标值/实际值
 	saveKpiGatherResult: function (isCallback,start,limit){
 		var me=this;
 		
 		var paramArray = [];
 		
 		var ridParams='',targetValueParams='',finishValueParams='';
 		
        var ridArray = me.getEl().dom.document.getElementsByName('rid');
        var targetValueArray = me.getEl().dom.document.getElementsByName('targetValue');
        var finishValueArray = me.getEl().dom.document.getElementsByName('finishValue');
        
        for (var i = 0; i < ridArray.length; i++) {
        	var vobj = {};
        	vobj.id = ridArray[i].value;
        	vobj.targetValue = targetValueArray[i].value;
        	vobj.finishValue =  finishValueArray[i].value;
        	paramArray.push(vobj);
        }
        
        FHD.ajax({
        	url: __ctxPath + '/kpi/kpi/saveKpiGatherReulst.f',
            params: {
            	paramArray:Ext.JSON.encode(paramArray)
            },
            callback: function (data) {
            	if(data && data.success) {
            		me.removeAll(true);
    				if(isCallback){
    					FHD.ajax({
            	            url: __ctxPath + '/kpi/kpi/findTargetOrGatherKpiByEmpId.f',
            	            params: {
            	            	executionId: me.executionId,
            	            	start:start,
	                        	limit:limit
	                        },
            	            callback: function (response) {
            	                if (response.datas && '' != response.datas) {
            	                   	//设置指标总数
            	                	me.totalLabel.setText(FHD.locale.get('fhd.kpi.kpi.kpiTotal')+':'+response.totalCount+FHD.locale.get('fhd.common.units.ge'));
            	                	//设置指标需录入数
            	                	me.needLabel.setText(FHD.locale.get('fhd.kpi.kpi.needCount')+':'+response.needCount+FHD.locale.get('fhd.common.units.ge'));
            	                	//设置指标已录入数
            	                	me.finishLabel.setText(FHD.locale.get('fhd.kpi.kpi.finishCount')+':'+response.finishCount+FHD.locale.get('fhd.common.units.ge'));
            	                	//获得start,isNext
            	                	me.setButtonStatus(response.start,response.isNext);
            	                	Ext.each(response.datas, function (item, index){
            		                	//js动态生成需要数据
            		                	var dymicFieldSet = me.createFiledSet(item, index);
            	                        	
            	                        me.add(dymicFieldSet);
            		                });
            	                }else{
            	                	me.preBtn.setDisabled(true);
            	                    me.nextBtn.setDisabled(true);
            	                    me.submitBtn.setDisabled(true);
            	                }
            	            }
            	    	});
    				}else{
    					FHD.ajax({
            	            url: __ctxPath + '/kpi/kpi/submitKpiGatherReulst.f',
            	            callback: function (data) {
            	            	if(data && data.success){
            	            		me.preBtn.setDisabled(true);
               	                    me.nextBtn.setDisabled(true);
               	                    me.submitBtn.setDisabled(true);
            	            		FHD.ajax({
                        	            url: __ctxPath + '/kpi/kpi/jbpmKpiGatherResultSubmit.f',
                        	            params: {
            	        	            	executionId:me.executionId
            	        	            },
                        	            callback: function () {
                        	            	//提交成功，关闭窗口
                        	            	Ext.getCmp(me.winId).close();
                        	            }
                        	    	});
            	            	}
            	            }
            	    	});
    				}
                }
            }
        });
 	},
 	//重新加载数据
 	reloadData: function (){
    	var me = this;
    	
    	
    }
});

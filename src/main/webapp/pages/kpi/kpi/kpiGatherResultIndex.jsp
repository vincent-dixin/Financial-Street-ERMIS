<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/include-tagsOnly.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>指标数据收集</title>
    <script type="text/javascript">
    	/**
    	 * 创建行
    	 */
	    function createTr(type,title,content,name){
			var tr = $('<tr />',{
	    		
	    	});
	    	
			var txt1 = createTxt(title);
			var td1 = createTdTitle(txt1);
			td1.appendTo(tr);
			
			if('txt' == type || false == type){
				var txt2 = createTxt(content);
				var td2 = createTdContent();
				txt2.appendTo(td2);
				td2.appendTo(tr);
			}else if('img' == type){
				var img = createImg(content,"16","16");
				var td2 = createTdContent();
				img.appendTo(td2);
				td2.appendTo(tr);
			}else if(true == type){
				var text = createTdText('text',content,name);
				var td3 = createTdContent();
				text.appendTo(td3);
				td3.appendTo(tr);
			}
	    	
	    	return tr;
	    }
	    /**
    	 * 创建列标题
    	 */
	 	function createTdTitle(title){
			var td = $('<td />',{
				'class':"alt",
				width:"20%",
				html:title
	    	});
			return td;
	 	}
	 	/**
    	 * 创建列内容
    	 */
	 	function createTdContent(){
	 		var td = $('<td />',{
				width:"40%"
	    	});
			return td;
	 	}
	 	/**
    	 * 创建文本
    	 */
	 	function createTxt(text){
	 		var txt = $('<text />',{
				html:text
	    	});
	 		return txt;
	 	}
	 	/**
    	 * 创建图片
    	 */
	 	function createImg(imgPath,width,height){
			var img = $('<img />',{
	    		src:__ctxPath + imgPath,
	    		width:width,
	    		height:height
	    	});
	 		return img;
	 	}
	 	/**
    	 * 创建文本框
    	 */
	 	function createTdText(type,value,name){
			var text = $('<input />',{
				type:type,
				name:name,
				value:value
	    	});
	 		return text;
	 	}
	 	/**
	 	 * 创建fieldSet
	 	 */
	 	function createFiledSet(item,index){
       		//item.datas[index].kid
       		//console.log(item.kid);
       		//debugger;
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
       		
       		createTr("txt","所属部门/人员：",item.targetOrgEmp).appendTo(table);
			createTr("txt","采集部门/人员：",item.gatherOrgEmp).appendTo(table);
			if(null != item.preFinishValue){
				createTr("txt","上期值：",item.preFinishValue+item.units).appendTo(table);
			}else{
				createTr("txt","上期值：","").appendTo(table);
			}
			createTr("img","上期状态：",item.assessmentStatus).appendTo(table);
			if('' != item.direction){
				createTr("img","上期趋势：",item.direction).appendTo(table);
			}else{
				createTr("txt","上期趋势：","").appendTo(table);
			}
			if(item.targetValueEdit){
				createTr(item.targetValueEdit,"目标值：",item.targetValue,'targetValue').appendTo(table);
			}else{
				createTr(item.targetValueEdit,"目标值：","",'targetValue').appendTo(table);
				createTdText('hidden',"",'targetValue').appendTo(table);
			}
			if(item.finishValueEdit){
				createTr(item.finishValueEdit,"实际值：",item.finishValue,'finishValue').appendTo(table);
			}else{
				createTr(item.finishValueEdit,"实际值：","",'finishValue').appendTo(table);
				createTdText('hidden',"",'finishValue').appendTo(table);
			}
			createTdText('hidden',item.id,'rid').appendTo(table);
       		
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
	 	}
	 	/**
	 	 * 设置按钮状态
	 	 */
	 	function setButtonStatus(start,isNext){
	 		//判断上一页
        	if(1 == start){
            	Ext.getCmp('kpigatherresutlInput-pre' + ${param._dc}).setDisabled(true);
        	}else{
            	Ext.getCmp('kpigatherresutlInput-pre' + ${param._dc}).setDisabled(false);
        	}
            
        	//判断下一页
        	if(isNext){
        		Ext.getCmp('kpigatherresutlInput-next' + ${param._dc}).setDisabled(false);
        	}else{
        		Ext.getCmp('kpigatherresutlInput-next' + ${param._dc}).setDisabled(true);
        	}
	 	}
	 	/**
	 	 * 保存指标采集结果目标值/实际值
	 	 */
	 	function saveKpiGatherResult(kpiGatherResultInputPanel,isCallback,start,limit){
	 		var paramArray = [];
	 		
	 		
	 		var ridParams='',targetValueParams='',finishValueParams='';
	 		
            var ridArray = kpiGatherResultInputPanel.getEl().dom.document.getElementsByName('rid');
            var targetValueArray = kpiGatherResultInputPanel.getEl().dom.document.getElementsByName('targetValue');
            var finishValueArray = kpiGatherResultInputPanel.getEl().dom.document.getElementsByName('finishValue');
            
            for (var i = 0; i < ridArray.length; i++) {
            	var vobj = {};
            	vobj.id = ridArray[i].value;
            	vobj.targetValue = targetValueArray[i].value;
            	vobj.finishValue =  finishValueArray[i].value;
            	paramArray.push(vobj);
            }
            
            //alert(ridParams+"\t"+targetValueParams+"\t"+finishValueParams+"\t"+start+"\t"+limit);
            
            FHD.ajax({
            	url: __ctxPath + '/kpi/kpi/saveKpiGatherReulst.f',
                params: {
                	paramArray:Ext.JSON.encode(paramArray)
                },
                callback: function (data) {
                	if(data && data.success) {
                		kpiGatherResultInputPanel.removeAll(true);
        				if(isCallback){
        					//alert('保存成功,回调');
        					FHD.ajax({
                	            url: __ctxPath + '/kpi/kpi/findTargetOrGatherKpiByEmpId.f',
                	            params: {
                	            	start:start,
    	                        	limit:limit
    	                        },
                	            callback: function (response) {
                	                if (response.datas && '' != response.datas) {
                	                	//设置指标总数
                                    	Ext.getCmp('kpigatherresutlInput-total' + ${param._dc}).setText('指标总数:'+response.totalCount+'个');
                	                	//设置需录入值
                	                	Ext.getCmp('kpigatherresutlInput-need' + ${param._dc}).setText('需录入值:'+response.needCount+'个');
                	                	//设置已录入值
                	                	Ext.getCmp('kpigatherresutlInput-finish' + ${param._dc}).setText('已录入值:'+response.finishCount+'个');
                	                	//获得start,isNext
                	                	setButtonStatus(response.start,response.isNext);
                	                	Ext.each(response.datas, function (item, index){
                		                	//js动态生成需要数据
                		                	var dymicFieldSet = createFiledSet(item, index);
                	                        	
                	                        kpiGatherResultInputPanel.add(dymicFieldSet);
                		                });
                	                }else{
                	                	Ext.getCmp('kpigatherresutlInput-pre' + ${param._dc}).setDisabled(true);
                	                    Ext.getCmp('kpigatherresutlInput-next' + ${param._dc}).setDisabled(true);
                	                    Ext.getCmp('kpigatherresutlInput-submit' + ${param._dc}).setDisabled(true);
                	                }
                	            }
                	    	});
        				}else{
        					FHD.ajax({
                	            url: __ctxPath + '/kpi/kpi/submitKpiGatherReulst.f',
                	            callback: function (data) {
                	            	if(data && data.success){
                	            		Ext.getCmp('kpigatherresutlInput-pre' + ${param._dc}).setDisabled(true);
	               	                    Ext.getCmp('kpigatherresutlInput-next' + ${param._dc}).setDisabled(true);
	               	                    Ext.getCmp('kpigatherresutlInput-submit' + ${param._dc}).setDisabled(true);
                	            		FHD.ajax({
                            	            url: __ctxPath + '/kpi/kpi/jbpmKpiGatherResultSubmit.f',
                            	            params: {
                	        	            	executionId:'${param.executionId}'
                	        	            },
                            	            callback: function () {
                            	            	//提交成功，关闭窗口
                            	            	//alert("窗口关闭");
                            	            	Ext.getCmp('${param.winId}').close();
                            	            }
                            	    	});
                	            	}
                	            }
                	    	});
        				}
                    }else {
                       //alert('保存失败');
                    }
                }
            });
	 	}
	 	
	    Ext.onReady(function () {
	    	Ext.QuickTips.init();
	    	
	    	var kpiGatherResultInputPanel = Ext.create('Ext.panel.Panel',{
	    		tbar: {
                   items: [
	                	'->',
	                	{
	                		id:'kpigatherresutlInput-total' + ${param._dc},
	                    	text: '指标总数:0个',
	                    	xtype:'label'
	                    },'-',
	                    {
	                		id:'kpigatherresutlInput-need' + ${param._dc},
	                    	text: '需录入值:0个',
	                    	xtype:'label'
	                    },'-',
	                    {
	                		id:'kpigatherresutlInput-finish' + ${param._dc},
	                    	text: '已录入值:0个',
	                    	xtype:'label'
	                    },'-',
	                	{
	                		id:'kpigatherresutlInput-pre' + ${param._dc},
	                    	text: '上一页',
	                        iconCls: 'icon-control-rewind-blue',
	                        handler: function () {
	                        	var start = parseInt($("#start").attr("value"))-1;
	                        	//alert("pre="+start);
	                        	$("#start").attr("value",parseInt($("#start").attr("value"))-1);
	                        	saveKpiGatherResult(kpiGatherResultInputPanel,true,start,3);
	                        }
	                    },
	                    {
	                    	id:'kpigatherresutlInput-next' + ${param._dc},
	                        text: '下一页',
	                        iconCls: 'icon-control-fastforward-blue',
	                        handler: function () {
	                         	var start = parseInt($("#start").attr("value"))+1;
	                         	//alert("next="+start);
	                         	$("#start").attr("value",parseInt($("#start").attr("value"))+1);
	                         	saveKpiGatherResult(kpiGatherResultInputPanel,true,start,3);
	                        }
	                    },
	                    {
	                    	id:'kpigatherresutlInput-submit' + ${param._dc},
	                        text: '提交',
	                        iconCls: 'icon-control-stop-blue',
	                        handler: function () {
	                         	//alert("submit");
	                         	var me = this;
	                         	Ext.MessageBox.show({
	                	            title: '提示',
	                	            width: 260,
	                	            msg: '提交后不能再处理，你确认提交吗?',
	                	            buttons: Ext.MessageBox.YESNO,
	                	            icon: Ext.MessageBox.QUESTION,
	                	            fn: function (btn) {
	                	                if (btn == 'yes') {
	                	                	saveKpiGatherResult(kpiGatherResultInputPanel,false,1,3);
	                	                }
	                	            }
	                	        });
	                        }
                  	 	}
              		]
              	},
              	renderTo:'${param._dc}',
	    		border:true,
	    		overflowX: 'hidden',
	    		overflowY: 'auto',
	            height: FHD.getCenterPanelHeight()
	    	});
	    	
	    	
	    	FHD.ajax({
	            url: __ctxPath + '/kpi/kpi/findTargetOrGatherKpiByEmpId.f?limit=3&start=1',
	            params: {
	            	executionId:'${param.executionId}'
	            },
	            callback: function (response) {
	                if (response.datas && '' != response.datas) {
	                	//设置指标总数
                    	Ext.getCmp('kpigatherresutlInput-total' + ${param._dc}).setText('指标总数:'+response.totalCount+'个');
	                	//设置需录入值
	                	Ext.getCmp('kpigatherresutlInput-need' + ${param._dc}).setText('需录入值:'+response.needCount+'个');
	                	//设置已录入值
	                	Ext.getCmp('kpigatherresutlInput-finish' + ${param._dc}).setText('已录入值:'+response.finishCount+'个');
	                	//alert('init='+$("#start").attr("value"));
	                	//获得start,isNext设置按钮状态
	                	setButtonStatus($("#start").attr("value"),response.isNext);
	                	Ext.each(response.datas, function (item, index){
		                	//js动态生成需要数据
		                	var dymicFieldSet = createFiledSet(item, index);
	                        	
	                        kpiGatherResultInputPanel.add(dymicFieldSet);
		                });
	                }else{
	                	Ext.getCmp('kpigatherresutlInput-pre' + ${param._dc}).setDisabled(true);
	                    Ext.getCmp('kpigatherresutlInput-next' + ${param._dc}).setDisabled(true);
	                    Ext.getCmp('kpigatherresutlInput-submit' + ${param._dc}).setDisabled(true);
	                }
	            }
	    	});
	    	
	    	FHD.componentResize(kpiGatherResultInputPanel, 0, 0);
	    });
   	</script>
</head>
<body>
	<div id='${param._dc}'><input type="hidden" id="start" name="start" value="1"/></div>
</body>
</html>
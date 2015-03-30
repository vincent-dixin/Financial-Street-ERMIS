Ext.define('FHD.view.icm.rectify.RectifyImproveDashboard', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.rectifyimprovedashboard',
    
    layout: {
        align: 'stretch',
        type: 'vbox'
    },
    bodyPadding: '3 3 3 3',
    border: false,
    requires:[
    	'FHD.view.icm.rectify.form.ImproveViewForm'
    ],
    initComponent: function() {
        var me = this;
        
		me.finishRateChart = Ext.create('FHD.ux.FusionChartPanel',{
    		id:'improve_finish_rate',
    		chartType:'AngularGauge',
    		flex:1,
			width:105,
			height:108,
			border:false,
			//title:'计划完成率',
    		xmlData:me.finishRateXml
		});
		var tbspacer = {
            xtype:'tbspacer',
            flex:1
        };
        var finishRateTitle;
        if(me.finishRate<50){
        	finishRateTitle = '计划完成率&nbsp;&nbsp;<font color="FF6600" size="5px">'+me.finishRate+'%</font>';
        }else if(me.finishRate>=50 && me.finishRate<75){
        	finishRateTitle = '计划完成率&nbsp;&nbsp;<font color="CC9900" size="5px">'+me.finishRate+'%</font>';
        }else{
        	finishRateTitle = '计划完成率&nbsp;&nbsp;<font color="339900" size="5px">'+me.finishRate+'%</font>';
        }
        
		me.upRegion = Ext.widget('panel',{
        	layout: {
		        type: 'vbox'
		    },
		    overflowX: 'hidden',
		    overflowY: 'auto',
		    margin: '1 1 1 1',
		    border:false,
    		flex:4,
        	items:[
        		tbspacer,
        	    me.encapsulateChart(me.finishRateChart,finishRateTitle,'反映当前整改方案完成情况的指标'),
        	    tbspacer
        	]
        });
		
		me.defectLevelChart = Ext.create('FHD.ux.FusionChartPanel',{
    		id:'assessplan_defect_level_chart',
    		chartType:'Column2D',
			flex:4,
			border:false,
			//title:'缺陷等级分布图',
			margin: '1 1 1 1',
    		xmlData:me.defectLevelXml
		});
        
        me.orgDefectChart = Ext.create('FHD.ux.FusionChartPanel',{
    		id:'assessplan_org_defect_chart',
    		chartType:'Column2D',
			flex:4,
			border:false,
			//title:'部门缺陷分布图',
			margin: '1 1 1 1',
    		xmlData:me.orgDefectXml
		});
        
        me.rightChart = Ext.create('Ext.container.Container',{
        	layout: {
				type: 'hbox',
	        	align:'stretch'
	        },
	        border:false,
	        flex:8,
        	items:[
	        	me.defectLevelChart,
	        	me.orgDefectChart
        	]
		});
		
        // 计划进度列表
        me.fhdgrid = Ext.widget('fhdgrid',{
        	checked: false,
			searchable: true,
			pagable: true,
			flex: 4,
			margin: '1 1 1 1',
        	url:__ctxPath + '/icm/improve/findImproveListBypage.f?companyId='+__user.companyId+'&dealStatus=H,F', //调用后台url
            cols: [{header: 'ID',dataIndex : 'id',hidden:true,sortable: false},
            	{header: '所属公司',dataIndex : 'companyName',hidden:true, sortable : false}, 
				{header: '编号',dataIndex : 'code',sortable: false,flex : 1}, 
				{header: '名称',dataIndex : 'name',sortable: false,flex : 3, renderer : function(value, metaData, record, colIndex, store, view) { 
						/*
							data-qtip:设置提示正文内容。
							data-qtitle:设置提示的标题。
							data-qwidth:设置提示的宽度。
							data-qalign:表示用提示的一个基准点，对应到原件的哪个基准点。例如：tl-br表示用提示的左上角，对应到原件的右下角。
						*/
						metaData.tdAttr = 'data-qtip="'+value+'"'; 
				    	return "<a href=\"javascript:void(0);\" onclick=\"Ext.getCmp('"+me.id+"').viewObject('" + record.get('id') + "')\">"+value+"</a>";  
						//return value;
					}
				},              
				/*{header: '计划开始日期',dataIndex :'planStartDate',sortable: false}, 
				{header: '计划完成日期',dataIndex : 'planEndDate',sortable: false}, */
				{header:'计划进度', sortable: false,dataIndex: 'schedule',flex:1,
					renderer:function(value,metaData,record,colIndex,store,view) {
						if(value){
							value = value +'%';
						}
						return value;
					}
				},
				{header: '实际进度', dataIndex: 'actualProgress',sortable: false, 
					renderer:function(value,metaData,record,colIndex,store,view) {
						if(record.data.dealStatus!='N'){
							return "<a href='javascript:void(0)' onclick=\"Ext.getCmp('"+me.id+"').showProcessViewList('" + record.data.id +"')\" >" + value +'%' + "</a>"; 
						}
						if(value){
							value = value +'%';
						}
						return value;
					}
				},
				{header: '处理状态',dataIndex : 'dealStatus',sortable: false, renderer : function(value, metaData, record, colIndex, store, view) { 
						if('N' == value){
							return '未开始';
						}else if('H' == value){
							return '处理中';
						}else if('F' == value){
							return '已完成';
						}else{
							return '';
						}
					}
				},
				
				{header: '创建日期',dataIndex :'createTime',sortable: false} 
			]
        	
        });
        
        me.fhdgrid.on('select',function(t,record,index,e){
        	FHD.ajax({
                url: __ctxPath + '/icm/rectify/findImproveChartXmlByImproveId.f',
                params: {
                	improveId: record.data.id
                },
                callback: function (data) {
                    if (data){
                    	me.generateChartXml(data,record.data.id);
                    }
                }
        	});
        });
        
		Ext.applyIf(me, {
        	layout: {
				type: 'hbox',
	        	align:'stretch'
	        },
	        items:[
	    		Ext.create('Ext.container.Container',{
		        	layout: {
						type: 'hbox',
			        	align:'stretch'
			        },
			        border:false,
		            flex:4,
		        	items:[
			        	me.upRegion,
			        	me.rightChart
		        	]
				}),
	    		me.fhdgrid
	    	]
        });
        me.callParent(arguments);
        
    },
    removeItems: function(){
    	var me=this;
    		//1.先删除
    	me.upRegion.removeAll();
    	
    	if(FusionCharts("improve_finish_rate-chart") != undefined){
 		   	FusionCharts("improve_finish_rate-chart").dispose();
     	}
    },
    generateChartXml:function(data,id){
    	var me=this;

    	me.removeItems();
    	
    	me.rightChart.removeAll();
    	
		//2.再创建
    	me.finishRateChart = Ext.create('FHD.ux.FusionChartPanel',{
    		id:'improve_finish_rate',
    		chartType:'AngularGauge',
    		flex:1,
			width:105,
			height:108,
			border:false,
			//title:'计划完成率',
    		xmlData:data.finishRateXml
		});
    	/*
    	me.defectLevelChart = Ext.create('FHD.ux.FusionChartPanel',{
    		id:'assessplan_defect_level_chart',
    		chartType:'Column2D',
			flex:4,
			border:true,
			//title:'缺陷等级分布图',
			margin: '1 1 1 1',
    		xmlData:data.defectLevelXml
		});
        
        me.orgDefectChart = Ext.create('FHD.ux.FusionChartPanel',{
    		id:'assessplan_org_defect_chart',
    		chartType:'Column2D',
			flex:4,
			border:true,
			//title:'部门缺陷分布图',
			margin: '1 1 1 1',
    		xmlData:data.orgDefectXml
		});
    	*/
    	me.defectLevelChart = Ext.create('FHD.view.icm.statics.AssessResultCountChart',{
    		toolRegion:'north',
    		flex:4,
    		extraParams:{
    			iPlanId:id
    		}
    	});
    	
    	me.orgDefectChart = Ext.create('FHD.view.icm.statics.DefectCountChart',{
    		toolRegion:'north',
    		flex:4,
    		extraParams:{
    			iPlanId:id
    		}
    	});
    	
        var tbspacer = {
            xtype:'tbspacer',
            flex:1
        };
        
        var finishRateTitle;
        if(data.finishRate<50){
        	finishRateTitle = '计划完成率&nbsp;&nbsp;<font color="FF6600" size="5px">'+data.finishRate+'%</font>';
        }else if(data.finishRate>=50 && data.finishRate<75){
        	finishRateTitle = '计划完成率&nbsp;&nbsp;<font color="CC9900" size="5px">'+data.finishRate+'%</font>';
        }else{
        	finishRateTitle = '计划完成率&nbsp;&nbsp;<font color="339900" size="5px">'+data.finishRate+'%</font>';
        }
        
        me.upRegion.add(tbspacer);
        me.upRegion.add(me.encapsulateChart(me.finishRateChart,finishRateTitle,'反映当前整改方案完成情况的指标'));
        me.upRegion.add(tbspacer);
        
        me.rightChart.add(me.defectLevelChart);
		me.rightChart.add(me.orgDefectChart);
    },
    encapsulateChart:function(singleChart,title,subtitle){
    	var me=this;
    	var encapsulateChart = Ext.widget('panel',{
			width: 348,
			height: 80,
			layout: {
                type: 'table',
                columns: 2
            },
            border:false,
            defaults: {frame:true},
			items:[
				singleChart,
				Ext.create('Ext.container.Container',{
					layout:'column',
					defaults:{
						columnWidth: 1
					},
					height: 80,
					items:[
						Ext.widget('label',{
							margin : '3 0 0 10',
							html: '<font size="3">'+title+'</font>'
						}),
						Ext.widget('label',{
							margin : '5 0 0 10',
							html: '<font color="#7E8877">'+subtitle+'</font>'
						})
					]
				})
			]
		});
    	
    	return encapsulateChart;
    },
    viewObject: function(id){
    	var me = this;
    	var improveviewform = Ext.widget('improveviewform',{improveId:id});
    	improveviewform.reloadData();
    	Ext.create('FHD.ux.Window',{
			title:'预览',
			iconCls: 'icon-view',//标题前的图片
			layout : {
				type : 'fit'
			},
			items: [Ext.widget('panel',{
				autoScroll: true,
				bodyPadding:'0 3 3 3',
				items:improveviewform
			})], 
			maximizable: true,
			resizable: false,
			autoScroll: true,
			listeners:{
				close : function(){
					me.reloadData();
				}
			}
		}).show();
    },
	showProcessViewList:function(id){
		var me=this;
		//判断此条记录是否发起工作流
		FHD.ajax({
			url : __ctxPath + '/jbpm/processInstance/findJbpmHistProcinstIfExistByBusinessId.f',//判断是否发起工作流
			params : {
				businessId : id
			},
			callback: function (data) {
	           	if(!data.success){
	           		return Ext.Msg.alert('提示','此条记录并未发起工作流!');
	           	}
	           	me.assessPlanDetailPanel = Ext.create('FHD.view.icm.assess.AssessPlanDetailsPopContainer',{
					businessId:id
				});
		
	    		var popWin = Ext.create('FHD.ux.Window',{
					title:'整改计划进度信息',
					//modal:true,//是否模态窗口
					collapsible:false,
					maximizable:true,//（是否增加最大化，默认没有）
					items:[me.assessPlanDetailPanel]
	    		}).show();
	        }
		});
	},
	reloadData:function(){
    	var me=this;
    	
    }
});
Ext.define('FHD.view.wp.WorkPlanDashboard', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.workplandashboard',
    
    layout: {
        align : 'stretch',
        type : 'vbox'
    },
    bodyPadding: '0 3 3 3',
    border : false,
    autoScroll: true,
    
    initComponent: function() {
        var me = this;

        me.callParent(arguments);

        FHD.ajax({
        	url : __ctxPath + '/wp/dashboard/findworkplanexecutionstatus.f',
        	callback : function(responseText){
        		Ext.each(responseText,function(data){
        			console.log(data);
        			var fieldset = Ext.widget('fieldset',{
        				title: data.workPlan,
        				layout:{
        					type : 'hbox',
        					align : 'stretch'
        				},
        				height: 250
        			});
        			me.add(fieldset);
        			
        			var grid = Ext.widget('grid',{
        				flex:1,
        				store: Ext.create('Ext.data.Store',{
        					fields: ['id','orgName','status','attainmentRate','contributeAmount','contributeLevel'],
        					data: data,
						    proxy: {
						        type: 'memory',
						        reader: {
						            type: 'json',
						            root: 'enforcementOrgs'
						        }
						    }
        				}),
        				columns: [
					        {header: '单位', sortable: false,  dataIndex: 'orgName', flex:1 },
					        {header: '完成情况', sortable: false, dataIndex: 'status', flex:.5 }			// 当前公司或者部门下里程碑关联计划的进度汇总
							//{header: '综合达标率', sortable: false, dataIndex: 'attainmentRate', flex:1 },
							//{header: '贡献额', sortable: false, dataIndex: 'contributeAmount', flex:1 },
							//{header: '贡献水平', sortable: false, dataIndex: 'contributeLevel', flex:1 }
					    ]
        			});
        			
        			fieldset.add(grid);
        			var xmlData = '<chart caption="计划完成情况" bgAlpha="30,100" bgAngle="45" startingAngle="175"  smartLineColor="7D8892" smartLineThickness="2">';
        			if(data.executeStatus){
        				if(data.executeStatus.finish){
        					xmlData+='<set label="完成" value="'+ data.executeStatus.finish +'"  />';
        				}else {
        					xmlData+='<set label="完成" value="0"  />';
        				}
        				if(data.executeStatus.unfinish){
        					xmlData+='<set label="未完成" value="'+ data.executeStatus.unfinish +'"  />';
        				}else {
        					xmlData+='<set label="未完成" value="0"  />';
        				}
        				if(data.executeStatus.overdue_finish){
        					xmlData+='<set label="逾期完成" value="'+ data.executeStatus.overdue_finish +'"  />';
        				}else {
        					xmlData+='<set label="逾期完成" value="0"  />';
        				}
        				if(data.executeStatus.overdue_unfinish){
        					xmlData+='<set label="预期未完成" value="'+ data.executeStatus.overdue_unfinish +'"  />';
        				}else {
        					xmlData+='<set label="预期未完成" value="0"  />';
        				}
        			}else {
        				xmlData+='<set label="完成" value="0"  />';
        				xmlData+='<set label="未完成" value="0"  />';
        				xmlData+='<set label="逾期完成" value="0"  />';
        				xmlData+='<set label="预期未完成" value="0"  />';
        			}
        			xmlData+='<styles><definition><style name="CaptionFont" type="FONT" face="Verdana" size="15" color="7D8892" bold="1" /><style name="LabelFont" type="FONT" color="7D8892" bold="1"/></definition><application><apply toObject="DATALABELS" styles="LabelFont" /><apply toObject="CAPTION" styles="CaptionFont" /></application></styles>';
        			xmlData+='</chart>';
        			
        			var chart = Ext.widget('fusionchartpanel',{
        				chartType :'Doughnut3D',
        				chartNoDataText : '没有一项计划开始进行！',
        				border:false,
		        		xmlData:xmlData,
		        		width: 500
		        	})
		        	
		        	fieldset.add(chart);
        			
        		});
        		
        	}
        });
        
    }
});
/**
 * 
 * 定性评估表格
 */

Ext.define('FHD.view.risk.assess.quaAssess.QuaAssessGrid', {
    extend: 'FHD.view.component.EditorGridPanel',
    alias: 'widget.quaAssessGrid',
    
    edit : function(riskId, templateId){
    	Ext.getCmp('quaAssessCardId').quaAssessOpe.quaAssessEdit.load(riskId, templateId);
    },
    
    riskDatas : null,
    
    getColsShow : function(){
    	var me = this;
    	var cols = [
    				{
    					dataIndex:'riskId',
    					hidden:true
    				},
//    				{
//    		            header: "目标名称",
//    		            dataIndex: 'strategyMapName',
//    		            sortable: true,
//    		            align: 'center',
//    		            flex:1
//    		        }, {
//    		            header: "衡量指标",
//    		            dataIndex: 'kpiName',
//    		            sortable: true,
//    		            align: 'center',
//    		            flex:2
//    		        },
    		        {
    		            header: "上级风险",
    		            dataIndex: 'parentRiskName',
    		            sortable: true,
    		            align: 'center',
    		            flex:1
    		        },{
    		            header: "风险名称",
    		            dataIndex: 'riskName',
    		            sortable: true,
    		            align: 'center',
    		            flex:2,
    		            renderer:function(value,metaData,record,colIndex,store,view) {
//    	     				return "<a href=\"javascript:void(0);\" " +
//    	     						"onclick=\"Ext.getCmp('" + me.id + "').edit('" + record.get('riskId') + "'," +
//    	     								" '" + record.get('templateId') + "')\">"+value+"</a>";
    		            	var value = {};
    		            	value['riskId'] = record.get('riskId');
    		            	value['templateId'] = record.get('templateId');
    		            	value['rangObjectDeptEmpId'] = record.get('rangObjectDeptEmpId');
    		            	me.riskDatas.push(value);
    		            	return record.get('riskName');
    	     			}
    		        },
    		        {
    		            header: "风险水平",
    		            dataIndex: 'riskIcon',
    		            sortable: true,
    		            align: 'center',
    		            flex:.5
    		        },
    		        
    		        {
    					dataIndex:'templateId',
    					hidden:true
    				},
    				{
    					dataIndex : 'rangObjectDeptEmpId',
    					hidden:true
    				}
    	        ];
    	
    	return cols;
    },
    
    getColsInit : function(){
    	var me = this;
    	var cols = [
    				{
    					dataIndex:'riskId',
    					hidden:true
    				},
//    				{
//    		            header: "目标名称",
//    		            dataIndex: 'strategyMapName',
//    		            sortable: true,
//    		            align: 'center',
//    		            flex:1
//    		        }, {
//    		            header: "衡量指标",
//    		            dataIndex: 'kpiName',
//    		            sortable: true,
//    		            align: 'center',
//    		            flex:2
//    		        },
    		        {
    		            header: "上级风险",
    		            dataIndex: 'parentRiskName',
    		            sortable: true,
    		            align: 'center',
    		            flex:1
    		        },{
    		            header: "风险名称",
    		            dataIndex: 'riskName',
    		            sortable: true,
    		            align: 'center',
    		            flex:2,
    		            renderer:function(value,metaData,record,colIndex,store,view) {
//    	     				return "<a href=\"javascript:void(0);\" " +
//    	     						"onclick=\"Ext.getCmp('" + me.id + "').edit('" + record.get('riskId') + "'," +
//    	     								" '" + record.get('templateId') + "')\">"+value+"</a>";
    		            	var value = {};
    		            	value['riskId'] = record.get('riskId');
    		            	value['templateId'] = record.get('templateId');
    		            	value['rangObjectDeptEmpId'] = record.get('rangObjectDeptEmpId');
    		            	me.riskDatas.push(value);
    		            	return record.get('riskName');
    	     			}
    		        },
//    		        {
//    		            header: "风险水平",
//    		            dataIndex: 'riskLevel',
//    		            sortable: true,
//    		            align: 'center',
//    		            flex:.5
//    		        },
    		        
    		        {
    					dataIndex:'templateId',
    					hidden:true
    				},
    				{
    					dataIndex : 'rangObjectDeptEmpId',
    					hidden:true
    				}
    	        ];
    	
    	return cols;
    },
    
    getSubmit : function(){
    	var me = this;
    	
    },
    
    // 初始化方法
    initComponent: function() {
        var me = this;
        me.riskDatas = [];
        
        var cols = null;
        
        if(me.url != 'findAssessShowGrid.f'){
        	cols = me.getColsInit();
        }else{
        	cols = me.getColsShow();
        }
        
        Ext.apply(me,{
        	region:'center',
        	url : me.url,
        	cols:cols,
		    border: true,
		    checked: false,
		    pagable : false,
		    searchable : false,
		    columnLines: true
        });
        
        me.callParent(arguments);
        me.store.on('load',function(){
        	Ext.widget('gridCells').mergeCells(me, [1,2]);
        	if(me.url != 'findAssessShowGrid.f'){
        		Ext.getCmp('quaAssessPanelId').infoNav.load(5, 1, me.riskDatas);
        	}
        });
    }

});
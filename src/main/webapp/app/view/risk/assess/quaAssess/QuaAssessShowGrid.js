/**
 * 
 * 定性评估表格
 */

Ext.define('FHD.view.risk.assess.quaAssess.QuaAssessShowGrid', {
    extend: 'FHD.view.component.EditorGridPanel',
    alias: 'widget.quaAssessShowGrid',
    
    edit : function(riskId, templateId){
    	Ext.getCmp('quaAssessCardId').quaAssessOpe.quaAssessEdit.load(riskId, templateId);
    },
    
    getColsShow : function(){
    	var me = this;
    	var cols = [
    				{
    					dataIndex:'riskId',
    					hidden:true
    				},
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
    		            flex:2
    		        },
    		        {
    		            header: "风险水平",
    		            dataIndex: 'riskIcon',
    		            sortable: true,
    		            align: 'center',
    		            flex:.5,
    		            renderer:function(value,metaData,record,colIndex,store,view) {
    		            	return "<div style='width: 32px; height: 19px; background-repeat: no-repeat;background-position: center top;' class='" + record.get('riskIcon') + "'/>";
    	     			}
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
    
    // 初始化方法
    initComponent: function() {
        var me = this;
        
        var cols = cols = me.getColsShow();
        
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
        });
    }

});
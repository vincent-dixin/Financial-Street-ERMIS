Ext.define('FHD.view.risk.assess.formulatePlan.FormulateApproverSubmitGridPanel', {
    extend: 'FHD.ux.layout.EditorGridPanel',
    alias: 'widget.formulateApproverSubmitGridPanel',
    flex:1,
    style: {
        borderColor: '#99bce8',
        borderStyle: 'solid',
        borderWidth:'1px'
    },
   
    // 初始化方法
    initComponent: function() {
        var me = this;
        //目标名称 目标责任人 衡量指标 权重  指标说明  指标责任人
        me.cols = [ {dataIndex : 'id', hidden : true}, 
         			{header : "上级风险", dataIndex : 'parentRiskName', sortable : true, flex : 1},
                    {header : "风险名称", dataIndex : 'riskName', sortable : true, flex : 2},
                    {header : "责任部门", dataIndex : 'mainOrgName', sortable : true, flex : 1}, 
                    {header : "相关部门", dataIndex : 'relaOrgName', sortable : true, flex : 1 },
                    {header : "参与部门", dataIndex : '', sortable : true, flex : 1 }
        Ext.apply(me, {
        	region:'center',
        	url : '',
            cols:me.cols,
            btns:me.tbar,
		    border: false,
		    checked: true,
		    pagable : false,
		    searchable : true
        });

        me.callParent(arguments);
        
    	/*me.on('resize',function(p){
    		me.setHeight(FHD.getCenterPanelHeight()-120);
		});*/
    }

});
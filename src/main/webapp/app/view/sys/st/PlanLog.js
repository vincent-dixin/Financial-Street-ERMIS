/**
 * 计划任务
 * 
 * @author 金鹏祥
 */
Ext.define('FHD.view.sys.st.PlanLog', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.planLog',
    
    // 初始化方法
    initComponent: function() {
    	var me = this;
    	var planLogGrid;
    	var queryUrl = 'sys/st/queryPlanDealLogPage.f?planEmpId=' + me.planManId.planId; //查询分页url
    	var viewUrl = 'sys/st/viewPlan.f'//查看url
    	var planLogGridColums =[
        	{header: FHD.locale.get('fhd.sys.planEdit.name'), dataIndex: 'planName', sortable: true, flex : 1},
        	{header: FHD.locale.get('fhd.sys.planDealLogMan.log'), dataIndex: 'dealMeasure', sortable: true, flex : 1},
        	{header: FHD.locale.get('fhd.sys.planDealLogMan.time'), dataIndex: 'dealTime', sortable: true, flex : 1},
        	{header: FHD.locale.get('fhd.sys.planEdit.status'), dataIndex: 'estatus', sortable: true, flex : 1,
        		renderer:function(value,metaData,record,colIndex,store,view) { 
        		if(record.get('status') == 1){
        			//启用
        			return "<image src='images/icons/start.ico' width='16' height='16'/></a>";
        					
        		}else{
        			//停用
        			return "<image src='images/icons/stop.ico' width='16' height='16'/></a>";
        		}
        	 }},
        	{header: FHD.locale.get('fhd.sys.planEdit.createBy'), dataIndex: 'planTempName', sortable: true, flex : 1}
        ];
    	
    	planLogGrid = Ext.create('FHD.ux.GridPanel',{//实例化一个grid列表
    		url: queryUrl,//调用后台url
    		cols:planLogGridColums,//cols:为需要显示的列
    		checked:false
    	});
    	
        Ext.apply(me, {
    		layout: {
    	        type: 'fit'
    	    },
    	    items:[planLogGrid]
        });

        me.callParent(arguments);
	}
});
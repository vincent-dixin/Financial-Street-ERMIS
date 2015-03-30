/**
 * 
 * 审批人
 */

Ext.define('FHD.view.risk.assess.formulatePlan.FormulateApproverEdit', {
    extend: 'Ext.form.Panel',
    alias: 'widget.formulateapproveredit',
    
    // 初始化方法
    initComponent: function() {
        var me = this;
      //审批人
        me.approver = Ext.create('FHD.ux.org.CommonSelector',{
        	fieldLabel: '审批人',
        	name : 'approver',
        	id : 'approverId',
            type:'emp',
            multiSelect:false,
            margin: '10 10 30 10',
            //columnWidth: .5
        });
        Ext.apply(me, {
        	autoScroll:false,
        	border:false,
        	region:'center',
            items : [me.approver],
        });

       me.callParent(arguments);
    }

});
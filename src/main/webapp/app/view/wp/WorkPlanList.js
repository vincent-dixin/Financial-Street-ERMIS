/**
 * 
 * 工作计划列表
 * 
 * @author 胡迪新
 */
Ext.define('FHD.view.wp.WorkPlanList', {
    extend: 'FHD.ux.GridPanel',
    alias: 'widget.workplanlist',
    
    url : __ctxPath + '/wp/findworkplanlist.f',
    
    border : false,
    
    
    
    // 初始化方法
    initComponent: function() {
        var me = this;
       	
        
       	me.addBtn = Ext.widget('button',{
       		iconCls : 'icon-add',
    		text : '添加',
    		handler:function(){
    			var workplancenterpanel = me.up('panel');
	            workplancenterpanel.removeAll();
	            workplancenterpanel.add(Ext.widget('workplanform'));
    		}
       	});
       	
       	me.editBtn = Ext.widget('button',{
       		iconCls : 'icon-edit',
    		text : '修改',
    		handler:function(){
    			var selection = me.getSelectionModel().getSelection()[0];
    			if(selection.get('status') != '已保存') {
    				Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), '已进入流程的计划不能修改');
    				return;
    			}
    			var workplancenterpanel = me.up('panel');
    			workplancenterpanel.removeAll();
    			
    			var workplanform = Ext.widget('workplanform',{workPlanId:selection.get('id')});
	            workplancenterpanel.add(workplanform);
	            workplanform.reloadData();
    		}
       	});
       	
       	me.delBtn = Ext.widget('button',{
    		iconCls : 'icon-del',
    		text : '删除',
    		handler:function(){
    			Ext.MessageBox.show({
		            title: FHD.locale.get('fhd.common.delete'),
		            width: 260,
		            msg: FHD.locale.get('fhd.common.makeSureDelete'),
		            buttons: Ext.MessageBox.YESNO,
		            icon: Ext.MessageBox.QUESTION,
		            fn: function (btn) {
		                var selections = me.getSelectionModel().getSelection();
		    			var delIds = new Array();
		    			Ext.each(selections,function(selection){
		    				if(selection.get('status') != '已保存') {
			    				Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), 
			    					'<font color="blue">' + selection.get('name') + '</font> 已进入流程的计划不能删除');
			    			}else {
			    				delIds.push(selection.get('id'));
			    			}
		    			});
		    			if(delIds.length > 0) {
		    				FHD.ajax({
			    				url: __ctxPath + '/wp/removeworkplan.f',
			    				params : {
			    					ids : delIds.join(',')
			    				},
			    				callback : function(responseText){
			    					me.reloadData();
			    				}
			    			});
		    			}	
		            }
		        });
    		}
    	});
       	
        Ext.apply(me, {
        	tbarItems:[me.addBtn,me.editBtn,me.delBtn],
        	cols:[
        		{header: '计划编号', dataIndex: 'code', width: 60,flex : 1},
        		{header: '计划名称', dataIndex: 'name', width: 60,flex : 1},
        		{header: '计划开始日期', dataIndex: 'startDate', width: 60,flex : 1},
        		{header: '计划完成日期', dataIndex: 'endDate', width: 60,flex : 1},
        		{header: '状态', dataIndex: 'status', width: 60,flex : 1,
        			renderer:function(v){
        				var color;
        				switch(v) {
        					case '已保存':
        						color = 'black';
        						break;
        					case '已提交': 
        						color = 'blue';
        						break;
        				}
        				return '<font color="' + color + '">' + v + '</font>';
        			}
        		},
        		{header: '执行状态', dataIndex: 'status', width: 60,flex : 1,
        			renderer:function(v){
        				var color;
        				switch(v) {
        					case '已保存':
        						color = 'black';
        						break;
        					case '已提交': 
        						color = 'blue';
        						break;
        				}
        				return '<font color="' + color + '">' + v + '</font>';
        			}
        		}
        	],
        	listeners : {
        		selectionchange : function() {
        			me.editBtn.setDisabled(me.getSelectionModel().getSelection().length != 1);
					me.delBtn.setDisabled(me.getSelectionModel().getSelection().length == 0);
        		}
        	}
        });
		
        me.callParent(arguments);
        
        me.store.on('load',function(){
			me.editBtn.setDisabled(me.getSelectionModel().getSelection().length != 1);
			me.delBtn.setDisabled(me.getSelectionModel().getSelection().length == 0);
		})
    },
    
    reloadData: function() {
    	var me = this;
    	me.store.load();
    }

});
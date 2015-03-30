/**
 * 
 * 风险整理表格
 */

Ext.define('FHD.view.risk.assess.riskTidy.RiskTidyGrid', {
    extend: 'FHD.view.component.GridPanel',
    alias: 'widget.riskTidyGrid',
    
    edit : function(){
		//Ext.getCmp('riskTidyCardId').showRiskTidyRiskEdit();
    },
    
    // 初始化方法
    initComponent: function() {
        var me = this;
        
        var states = Ext.create('Ext.data.Store', {
            fields: ['abbr', 'name'],
            data : [
                {"abbr":"AL", "name":"因果分析"},
                {"abbr":"AK", "name":"层次分析"}
            ]
        });

        var cols = [
			{
				dataIndex:'id',
				hidden:true
			},
			
			{
	            header: "目标名称",
	            dataIndex: 'name',
	            sortable: true,
	            align: 'center',
	            flex:.6
	        },
	        
	        {
	            header: "衡量指标",
	            dataIndex: 'name',
	            sortable: true,
	            align: 'center',
	            flex:.6
	        },
			
	        {
	            header: "上级风险",
	            dataIndex: 'name',
	            sortable: true,
	            align: 'center',
	            flex:.6
	        }
	        
//	        {
//	            header: "衡量指标",
//	            dataIndex: 'kpiName',
//	            sortable: true,
//	            width:40,
//	            flex:1,
//	            renderer:function(value,metaData,record,colIndex,store,view) { 
//     				metaData.tdAttr = 'data-qtip="'+value+'" data-qwidth="'+100+'"'; 
//     				metaData.tdAttr = 'data-qtip="'+value+'" data-qwidth="'+100+'"'; 
//     				if(record.get('kpiName') == '周转率'){
//     					return "<a href=\"javascript:void(0);\" onclick=\"Ext.getCmp('riskTidyCardId').showAssessFishbone()\">"+value+"</a>";
//     				}else{
//     					return "<a href=\"javascript:void(0);\" onclick=\"Ext.getCmp('riskTidyCardId').showAssessTreeStr()\">"+value+"</a>";
//     				}
//     			}
//	        }
			
			,{
	            header: "风险定义",
	            dataIndex: 'riskName',
	            sortable: true,
	            hidden : true,
	            align: 'center',
	            flex:1
	        }
	        
	        ,{
	        	header : "",
	        	align: 'center',
	        	id:'approval',
	        	dataIndex : 'approval',
	        	sortable : true,
	        	flex:.1,
	        	renderer:function(value,metaData,record,colIndex,store,view) {
     				return "<input name=" + record.get('riskName') + " type='checkbox'/>";
     			}
	        },{
	            header: "风险名称",
	            dataIndex: 'riskName',
	            sortable: true,
	            align: 'center',
	            flex:1,
	            renderer:function(value,metaData,record,colIndex,store,view) {
     				return "<a href=\"javascript:void(0);\" onclick=\"Ext.getCmp('riskTidyGridId').edit()\">"+value+"</a>";
     			}
	        },
	        
//	        {
//	            header: "责任部门",
//	            dataIndex: 'definition',
//	            sortable: true,
//	            align: 'center',
//	            flex:.8
//	        },{
//	            header: "相关部门",
//	            dataIndex: 'kpiName',
//	            sortable: true,
//	            align: 'center',
//	            flex:.8
//	        },{
//	            header: "评估人",
//	            dataIndex: 'assessPerson',
//	            sortable: true,
//	            align: 'center',
//	            flex:.8,
//	            renderer:function(value,metaData,record,colIndex,store,view) {
//	            	if(value.indexOf(',') != -1){
//	            		return "<a href=\"javascript:void(0);\" onclick=\"Ext.getCmp('riskTidyGridId').edit()\">"+value+"</a>";
//	            	}else{
//	            		return value;
//	            	}
//     				
//     			}
//	        }
	        
	        {
	            header: "风险水平",
	            dataIndex: 'riskLevel',
	            sortable: true,
	            align: 'center',
	            flex:.3
	        }
	        
//	        ,{header: '操作' ,dataIndex: 'name', sortable: true, flex : .2,align: 'center',
//     			renderer:function(value,metaData,record,colIndex,store,view) {
//     				return "<a href=\"javascript:void(0);\" name= " + record.get('name') +" onclick=\"Ext.getCmp('quaAssessGridId').edit()\">" + "添加</a>" ;
//     			}
//     		}
	        
        ];
        
        Ext.apply(me,{
        	region:'center',
        	url : __ctxPath + "/app/view/risk/assess/riskTidy/list.json",
            extraParams:{
            	riskId:1
            },
        	cols:cols,
        	autoScroll:true,
        	border: true,
		    checked: false,
		    pagable : false,
		    searchable : false,
        });
        
//        var buttons = Ext.create('Ext.Button', {
//            text: 'grid添加',
//            handler: function() {
//            	Ext.getCmp('riskTidyCardId').quaAssessTab.getPanel(
//   						Ext.getCmp('riskTidyCardId').quaAssessTab.quaAssessRiskEditContainerId, 'quaAssessRiskEdit');
//   				Ext.getCmp('riskTidyCardId').showQuaAssessTab();
//            }
//        });
//        
//        var buttons2 = Ext.create('Ext.Button', {
//            text: '鱼骨图',
//            handler: function() {
//            	Ext.getCmp('riskTidyCardId').showRiskTidyPanel();
//            }
//        });
//        
//        Ext.apply(me, {
//        	border:false,
//            items: [buttons, buttons2]
//        });

        me.callParent(arguments);
        me.store.on('load',function(){
        	Ext.widget('gridCells').mergeCells(me, [2]);
        });
//        me.on('resize',function(p){
//    		me.setHeight(FHD.getCenterPanelHeight() - 40);
//    	});
    }

});
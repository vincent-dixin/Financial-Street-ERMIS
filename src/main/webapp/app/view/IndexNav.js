/**
 * 
 * 首页导航面板
 */
Ext.define('FHD.view.IndexNav', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.indexNav',
    
    onMenuClick : function(url,title,businessId){
		var url = url;
		var text = title;//FHD.titleJs[url];
		var centerPanel = Ext.getCmp('center-panel');
		var tab = centerPanel.getComponent(url);
		
		if(tab){
			centerPanel.setActiveTab(tab);
		}else{
			if(url.startWith('FHD')){
				var p = centerPanel.add(Ext.create(url,{
					id:url,
					businessId:businessId,
					title: text,
					tabTip: text,
					closable:true,
				}));
				centerPanel.setActiveTab(p);
			} else if (url.startWith('/pages')){
				var p = centerPanel.add({
					id:url,
					title: text,
					tabTip: text,
					layout:'fit',
					autoWidth:true,
					border:false,
					//iconCls: 'tabs',
					closable:true,
					autoLoad :{ url: __ctxPath+url,scripts: true}
				});
				centerPanel.setActiveTab(p);
			}else{
				var p = centerPanel.add({
					id:url,
					title: menu.text,
					tabTip:menu.text,
					layout:'fit',
					autoWidth:true,
					border:false,
					//iconCls: 'tabs',
					closable:true,
					html : '<iframe width=\'100%\' height=\'100%\' frameborder=\'0\' src=\''+__ctxPath+url+'\'></iframe>'
					//autoLoad :{ url: 'pages/icon.jsp',scripts: true}
					//items:[{xtype:'dictTypelist'}]
				});
				centerPanel.setActiveTab(p);
			}
		}
	},
    
    onMouseMoveFun : function(){
    	$(document).ready(function(){
        	$(".wrap div").hover(function() {
        		$(this).animate({"top": "-80px"}, 300, "swing");
        	},function() {
        		$(this).stop(true,false).animate({"top": "0px"}, 300, "swing");
        	});

        	});
    },
    
    // 初始化方法
    initComponent: function() {
        var me = this;
        
        me.id ='indexNavId'
        
        Ext.apply(me, {
        	margin: '30 150 3 200', 
        	border : false,
        	width:950,
        	height:400,
        	html:   '<div style="width:1024; height:768">' + 
			        '<div class="frame" onMouseMove="Ext.getCmp(\'indexNavId\').onMouseMoveFun();">' + 
			        '<a href="javascript:Ext.getCmp(\'indexNavId\').onMenuClick(\'FHD.view.bpm.mywork.MyTask\',\'我的待办\');">' + 
			        '<div class="wrap">' + 
			        '<img src=\'images/grgzt.png\' border="0" style="width:48; height:48; padding:10px"/>' + 
			        '<div>' + 
			        '<b></b>' + 
			        '<span>' +  
			        '<h1>个人工作台</h1>' +    
			        '<p>个人工作平台包括个人待办工作和个人的风险、指标、流程等相关信息。</p>' +        
			        '</span>' + 
			        '</div>' + 
			        '</div>' + 
			        '</a>' + 
			        '</div>' + 
			
					'<a href="javascript:Ext.getCmp(\'indexNavId\').onMenuClick(\'FHD.view.kpi.Metric\',\'度量标准\');">' + 
			        '<div class="frame" onMouseMove="Ext.getCmp(\'indexNavId\').onMouseMoveFun();">' + 
			        '<div class="wrap">' + 
			        '<img src=\'images/zlgl.png\' border="0" style="width:48; height:48; padding:10px"/>' + 
			        '<div >' + 
			        '<b></b>' + 
			        '<span> ' + 
			        '<h1>战略管理</h1>   ' + 
			        '<p>管理企业组织、目标、制度、流程等与风险相关的信息，建立风险管理基本工作流程和制度。</p>  ' + 
			        '</span>' + 
			        '</div>' + 
			        '</div>' + 
			        '</div>' + 
			        '</a>' + 
			
					'<a href="javascript:Ext.getCmp(\'indexNavId\').onMenuClick(\'FHD.view.risk.assess.AssessNav\',\'风险评估\');">' + 
			        '<div class="frame" onMouseMove="Ext.getCmp(\'indexNavId\').onMouseMoveFun();">' + 
			        '<div class="wrap">' + 
			        '<img src=\'images/fxpg.png\' border="0" style="width:48; height:48; padding:10px"/>' + 
			        '<div>' + 
			        '<b></b>' + 
			        '<span> ' + 
			        '<h1>风险评估</h1>   ' + 
			        '<p>通过持续的风险辨识、分析和评价工作，对新发生或需持续关注的风险行为进行跟踪。</p>  ' + 
			        '</span>' + 
			        '</div>' + 
			        '</div>' + 
			        '</div>' + 
			        '</a>' + 
			        
			        '<a href="javascript:Ext.getCmp(\'indexNavId\').onMenuClick(\'FHD.view.icm.statics.IcmMyDatas\',\'内部控制\');">' + 
			        '<div class="frame" onMouseMove="Ext.getCmp(\'indexNavId\').onMouseMoveFun();">' + 
			        '<div class="wrap">' + 
			        '<img src=\'images/nkkz.png\' border="0" style="width:48; height:48; padding:10px"/>' + 
			        '<div>' + 
			        '<b></b>' + 
			        '<span> ' + 
			        '<h1>内控控制</h1>   ' + 
			        '<p>明确重大风险管理策略和解决方案，以统一公司的风险管理行为，动态监控风险和风险管理的效果。</p>  ' + 
			        '</span>' + 
			        '</div>' + 
			        '</div>' + 
			        '</div>' + 
			        '</a>' + 
			        '<div style="width:1024; height:768">' + 
					//'<a href="javascript:Ext.getCmp(\'indexNavId\').onMenuClick(\'FHD.view.kpi.Metric\',\'度量标准\');">' +
			        '<a href="#">' +
			        '<div class="frame" onMouseMove="Ext.getCmp(\'indexNavId\').onMouseMoveFun();">           ' + 
			        '<div class="wrap">' + 
			        '<img src=\'images/hjxx.png\' border="0" style="width:48; height:48; padding:10px"/>' + 
			        '<div>' + 
			        '<b></b>	' + 
			        '<span> ' + 
			        '<h1>环境信息</h1>  ' +  
			        '<p>明确风险监控指标，制定预警方案和监控计划，监控重大突发事件的发生，触发执行应急预案。</p>  ' +     
			        '</span>' + 
			        '</div>' + 
			        '</div>' + 
			        '</div>' + 
			        '</a>' + 
			
			        '<div class="frame" onMouseMove="Ext.getCmp(\'indexNavId\').onMouseMoveFun();">' + 
			        '<div class="wrap">' + 
			        '<img src=\'images/khsp.png\' border="0" style="width:48; height:48; padding:10px"/>' + 
			        '<div>' + 
			        '<b></b>' + 
			        '<span> ' + 
			        '<h1>考核评审</h1>   ' + 
			        '<p>对全面风险管理与内部控制制度的健全性、合理性和有效性进行监督检查与评审。</p>  	' + 
			        '</span>' + 
			        '</div>' + 
			        '</div>' + 
			        '</div>' + 
			
			        '<div class="frame" onMouseMove="Ext.getCmp(\'indexNavId\').onMouseMoveFun();">' + 
			        '<div class="wrap">' + 
			        '<img src=\'images/xxgt.png\' border="0" style="width:48; height:48; padding:10px"/>' + 
			        '<div>' + 
			        '<b></b>' + 
			        '<span> ' + 
			        '<h1>信息沟通</h1>   ' + 
			        '<p>支持公司各层级的风险管理报告模板，定期的公司风险评估报告，满足管理层和监管机构的管理要求。</p>  	' + 	
			        '</span>' + 
			        '</div>' + 
			        '</div>' + 
			        '</div>' + 
			        
			        '<div class="frame" onMouseMove="Ext.getCmp(\'indexNavId\').onMouseMoveFun();">' + 
			        '<div class="wrap">' + 
			        '<img src=\'images/xtgl.png\' border="0" style="width:48; height:48; padding:10px"/>' + 
			        '<div>' + 
			        '<b></b>' + 
			        '<span>' + 
			        '<h1>系统管理</h1>' + 
			        '<p>管理系统基础信息包括：组织机构、用户、访问权限等。</p>' + 
			        '</span>' + 
			        '</div>' + 
			        '</div>' + 
			        '</div>'

        });
        
        
        me.callParent(arguments);
        
    }

});
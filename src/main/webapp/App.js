Ext.Loader.setConfig({
	enabled : true
});

Ext.Loader.setPath({
	'Ext.ux' : 'scripts/ext-4.1/ux',
	'Ext.app' : 'scripts/ext-4.1/app',
	'FHD.ux' : 'scripts/component',
	'FHD.demo' : 'pages/demo'
});

Ext.require(['Ext.app.Portlet', 'Ext.app.PortalColumn', 'Ext.app.PortalPanel','Ext.ux.form.SearchField',
		'Ext.app.PortalDropZone', 'Ext.ux.TabReorderer','FHD.ux.TreeCombox','Ext.ux.grid.FiltersFeature',
		'Ext.ux.Toast','Ext.ux.TabCloseMenu','FHD.ux.dict.DictSelect','FHD.ux.org.EmpTree',
		'FHD.ux.dict.DictRadio','FHD.ux.org.EmpSelector','FHD.ux.org.EmpSelectorWindow','FHD.ux.GridPanel',
		'FHD.ux.EditorGridPanel','FHD.ux.dict.DictCheckbox','FHD.ux.kpi.KpiStrategyMapSelector',
		'FHD.ux.fileupload.FileUpload','FHD.ux.kpi.KpiSelectorWindow','FHD.ux.kpi.KpiSelector','FHD.ux.kpi.KpiTree','FHD.ux.kpi.KpiStrategyMapTree','FHD.ux.kpi.KpiStrategyMapSelectorWindow']);
Ext.onReady(function() {
	/**
	 * 登录有好提示
	 */
	setTimeout(function() {
		Ext.get('loading').remove();
		Ext.get('loading-mask').fadeOut({
			remove : true
		});
		document.getElementById('app-header').style.display = 'block';
	}, 100);	
	
	/**
	 * 检查session 是否超时
	 */
	Ext.Ajax.on("requestcomplete",function(conn,response,options){
		var sessionStatus = response.getResponseHeader["sessionstatus"];   
        if(typeof(sessionStatus) != "undefined"){       
            Ext.Msg.alert('提示', '会话超时，请重新登录!', function(btn, text){     
                if (btn == 'ok'){     
                    var redirect = __ctxPath + '/login.do';     
                    window.location = redirect;     
                }     
            });     
         }  
	},this);
	
var createStore = function(id) { // 创建树面板数据源
	return Ext.create('Ext.data.TreeStore', {
		defaultRootId : id, // 默认的根节点id
		model : 'TreeModel',
		proxy : {
			type : 'ajax', // 获取方式
			url : 'sys/menu/authorityTreeLoaderByParentId.do' // 获取树节点的地址
		},
		clearOnLoad : true,
		nodeParam : 'id'// 设置传递给后台的参数名,值是树节点的id属性
	});
};
function loadWestPanel(data,removeAll) {
	var westPanel1;
	if(removeAll){
		Ext.getCmp('vip').remove(Ext.getCmp('west-panel'));
		westPanel1 = westPanel();
		Ext.getCmp('vip').add(westPanel1);
	}else{
		westPanel1 = Ext.getCmp('west-panel')
	}
	var panel = Ext.create('Ext.tree.Panel', {
		title : data.title,
		iconCls : data.iconCls,
		useArrows: true,
		autoScroll : true,
		rootVisible : false,
		layout : 'fit',
		animate : true, // 开启动画效果
		viewConfig : {
			loadingText : FHD.locale.get('fhd.common.loading')
		},
		store : createStore(data.id),
		listeners : {
			afterlayout : function() {
				if (this.getView().el) {
					var el = this.getView().el;
					var table = el
							.down('table.x-grid-table');
					if (table) {
						table.setWidth(el.getWidth());
					}
				}
			},
			itemclick : function(view,node){
				if (node.isLeaf()&&node.data.url!='') { //判断是否是根节点
					if(Ext.getCmp(node.data.id)!=null){
						centerPanel.remove(Ext.getCmp(node.data.id),true);
					}
					var menuname =  node.data.url;
					if(menuname.startWith('/pages')){
						var panel = Ext.create('Ext.panel.Panel',{
							id:node.data.id,
							title : node.data.text,
							closable : true,
							iconCls : node.data.iconCls,
							autoLoad :{ url: __ctxPath+node.data.url,scripts: true}
						});
						centerPanel.add(panel);
						centerPanel.setActiveTab(panel);
					}else{
						var panel = Ext.create('Ext.panel.Panel',{
							id:node.data.id,
							title : node.data.text,
							closable : true,
							iconCls : node.data.iconCls,
							html : '<iframe width=\'100%\' height=\'100%\' frameborder=\'0\' src=\''+__ctxPath+node.data.url+'\'></iframe>'
							//autoLoad :{ url: 'pages/icon.jsp',scripts: true}
							//items:[{xtype:'dictTypelist'}]
						});
						centerPanel.add(panel);
						centerPanel.setActiveTab(panel);
					}
				}
			}
		}
	});
	westPanel1.add(panel);
}
Ext.define('TreeModel', { // 定义树节点数据模型
	extend : 'Ext.data.Model',
	fields : [{name : 'id',type : 'string'},
			{name : 'text',type : 'string'},
			{name : 'iconCls',type : 'string'},
			{name : 'url',type : 'string'},
			{name : 'leaf',type : 'boolean'},
			{name : 'expended',type : 'boolean'},
			{name : 'type'}]
});
var centerPanel = Ext.create('Ext.tab.Panel', {
		id : 'center-panel',
		activeTab : 0,
		enableTabScroll : true,
		animScroll : true,
		border : true,
		autoScroll : true,
		region : 'center',
		split : true,
		items : [{
			title : FHD.locale.get('fhd.common.homePage'),
			xtype:'portalpanel',
			layout:'column',
			items : [/*{
					xtype : 'portalcolumn',
					columnWidth : 0.7,
	                items:[{ title: '新闻动态',height : 400,iconCls : 'icon-news',items:[{xtype:'textpanel'}]},
	                	{title: '最新通知',height : 150, iconCls : 'icon-notice' ,
	                	items:[{
	                		xtype:'fhdeditorgrid',
	                		//searchable:false,
	                		url:__ctxPath + '/sys/dic/dictEntryQuery.do',
	                		cols:[{header: '类型名称', dataIndex: 'dictType', sortable: true, width: 60,editor:true},
	        					{header: '名称', dataIndex: 'dictEntryName', sortable: true, width: 60},
	        					{header: '条目值', dataIndex: 'dictEntryValue', sortable: true, width: 60},
	        					{header: '状态', dataIndex: 'status', sortable: true, width: 60,renderer:function(val){
	        						if(val=='0')
	        							return '无效';
	        						else
	        							return '有效';
	        						}
	        					},
	        					{header: '上一级名称',dataIndex: 'parentname', sortable: true, width: 60}],
	        					tbarItems:[{text:'ssss'}]
	                		
	                	}]},
	                	{title: '业绩报表',height : 150, iconCls : 'icon-chart'}]
	            },{
	            	xtype : 'portalcolumn',
	            	columnWidth : 0.3,
	                items:[{ title: '功能链接', height : 150, iconCls : 'icon-link'},
	                	{title: '待办事项',height : 150,iconCls : 'icon-note' },
	                	{title: '邮件列表', height : 150,iconCls : 'icon-email-list'}]
	            }*/]
		}],
		plugins: [Ext.create('Ext.ux.TabReorderer'),
		  Ext.create('Ext.ux.TabCloseMenu',{
		  	closeTabText: FHD.locale.get('fhd.common.closePanel'),
		  	closeOthersTabsText: FHD.locale.get('fhd.common.closeOther'),
		  	closeAllTabsText: FHD.locale.get('fhd.common.closeAll')
		  })]
	});
var westPanel = function(){
	return Ext.create('Ext.panel.Panel', {
		id:'west-panel',
		region : 'west',
		title : FHD.locale.get('fhd.common.navigation'),
		width : 250,
		iconCls : 'icon-navigation',
		autoScroll : false,
		layout : 'accordion',
		layoutConfig: {
           titleCollapse:   true ,
           animate:   true ,
           activeOnTop:   true
       }, 
		collapsible : true,
		split : true
	});
}
		
var northPanel = Ext.create('Ext.panel.Panel', {
		id : 'north-panel',
		region : 'north',
		contentEl : 'app-header',
		height : 58
	});
var southPanel = Ext.create('Ext.panel.Panel', {
		id : 'south-panel',
		region : 'south',
		height : 28,
		border : false,
		bbar : [
		'->',
		{
			xtype : 'tbfill'
		},
		{ xtype: 'tbtext', text: FHD.locale.get('fhd.common.technicalSupport')+'<a href=http://www.firsthuida.com target="_blank">'+FHD.locale.get('fhd.common.firsthuida.com')+'</a>' },
		{
			xtype : 'tbseparator'
		}, {	
			pressed : false,
			text : FHD.locale.get('fhd.common.contectUs'),
			handler : function() {
				Ext.ux.Toast.msg(FHD.locale.get('fhd.common.contectUs'),FHD.locale.get('fhd.common.telephone')+'：010-84477330<br/>'+FHD.locale.get('fhd.common.web')+'：http://www.firsthuida.com');
			}
		}, '-', {
			pressed : false,
			text : FHD.locale.get('fhd.common.expand'),
			iconCls : 'icon-expand',
			handler : function() {
				var a = Ext.getCmp('north-panel');
				if (a.collapsed) {
					a.expand(true);
				} else {
					a.collapse(Ext.Component.DIRECTION_TOP,true);
				}
			}
		}, '-', {
			xtype : 'combo',
			mode : 'local',
			editable : false,
			value : FHD.locale.get('fhd.common.skinChange'),
			width : 100,
			triggerAction : 'all',
			store : [['ext-all', FHD.locale.get('fhd.common.skinDefault')]
					,['ext-all-gray', FHD.locale.get('fhd.common.skinGray')]
				    ,['ext-all-access', FHD.locale.get('fhd.common.skinHighlight')]],
			listeners : {
				scope : this,
				'select' : function(d, b, c) {
					if (d.value != '') {
						var a = new Date();
						a.setDate(a.getDate() + 300);
						FHD.setCookie('theme', d.value, a, __ctxPath);
						Ext.util.CSS.swapStyleSheet('theme', __ctxPath
										+ '/scripts/ext-4.1/resources/css/'
										+ d.value + '.css');
					}
				}
			}
		}]
	});
var headerNavPanel = Ext.create('Ext.tab.Panel', {
		id : 'header-nav-panel',
		width : 350,
		deferredRender : true,
		enableTabScroll : true,
		frame : false,
		border : false,
		plain : true,
		margin:'4 0 0 0',
		activeTab:1,
		renderTo : 'header-nav',
		height:20,
		defaults : {
			autoScroll : false,
			closable : false,
			bodyStyle : 'padding-top: 12px;'
		},
		items : [],
		listeners : {
			'tabchange' : function(d, currentActiveTab) {
				loadWestPanel(currentActiveTab,true);
			}
		}
	});
	Ext.create('Ext.container.Viewport',{
		layout : 'border',
		id:'vip',
		items : [northPanel,centerPanel,southPanel,
			Ext.create('Ext.panel.Panel', {id:'west-panel',
			region : 'west',title : FHD.locale.get('fhd.common.navigation'),
			width : 250,
			iconCls : 'icon-navigation',layout : 'accordion',
			layoutConfig: {
	           titleCollapse:   true ,
	           animate:   true ,
	           activeOnTop:   true
			}, 
			collapsible : true,
			split : true})
		],
		listeners : {
			afterrender : function(){
				FHD.ajax({
					url : 'sys/menu/findFirstAuthority.do',// 获取面板的地址
					callback : function(data){
						headerNavPanel.add(data);
						Ext.each(data,function(record,index){
							loadWestPanel(record,false);
			        	});
					}
				});
			}
		}
	});
});


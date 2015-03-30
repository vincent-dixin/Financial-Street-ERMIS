
//分析url
function parseURL(url) {
    var a = document.createElement('a');
    a.href = url;
    return {
        source: url,
        protocol: a.protocol.replace(':', ''),
        host: a.hostname,
        port: a.port,
        query: a.search,
        params: (function () {
            var ret = {},
            seg = a.search.replace(/^\?/, '').split('&'),
            len = seg.length, i = 0, s;
            for (; i < len; i++) {
                if (!seg[i]) { continue; }
                s = seg[i].split('=');
                ret[s[0]] = s[1];
            }
            return ret;
 
        })(),
        file: (a.pathname.match(/\/([^\/?#]+)$/i) || [, ''])[1],
        hash: a.hash.replace('#', ''),
        path: a.pathname.replace(/^([^\/])/, '/$1'),
        relative: (a.href.match(/tps?:\/\/[^\/]+(.+)/) || [, ''])[1],
        segments: a.pathname.replace(/^\//, '').split('/')
    };
}
 
//替换myUrl中的同名参数值
function replaceUrlParams(myUrl, newParams) {
 
    for (var x in newParams) {
        var hasInMyUrlParams = false;
        for (var y in myUrl.params) {
            if (x.toLowerCase() == y.toLowerCase()) {
                myUrl.params[y] = newParams[x];
                hasInMyUrlParams = true;
                break;
            }
        }
        //原来没有的参数则追加
        if (!hasInMyUrlParams) {
            myUrl.params[x] = newParams[x];
        }
    }
    var _result =  myUrl.path + "?";
 
    for (var p in myUrl.params) {
        _result += (p + "=" + myUrl.params[p] + "&");
    }
 
    if (_result.substr(_result.length - 1) == "&") {
        _result = _result.substr(0, _result.length - 1);
    }
 
    if (myUrl.hash != "") {
        _result += "#" + myUrl.hash;
    }
    return _result;
}

Ext.Loader.setConfig({
	enabled : true
});
Ext.Loader.setPath({
	'Ext.ux' : 'scripts/ext-4.1/ux',
	'Ext.app' : 'scripts/ext-4.1/app',
	'FHD.ux' : 'scripts/component',
	'FHD.view' : 'app/view',
	'FHD.demo' : 'pages/demo'
	
});

Ext.require(['Ext.app.Portlet', 'Ext.app.PortalColumn', 'Ext.app.PortalPanel','Ext.ux.form.SearchField',
		'Ext.app.PortalDropZone', 'Ext.ux.TabReorderer','FHD.ux.TreeCombox','Ext.ux.grid.FiltersFeature',
		'Ext.ux.Toast','Ext.ux.TabCloseMenu','FHD.ux.dict.DictSelect','FHD.ux.org.EmpTree','FHD.ux.CheckCombo',
		'FHD.ux.dict.DictRadio','FHD.ux.org.EmpSelectorWindow','FHD.ux.GridPanel',
		'FHD.ux.EditorGridPanel','FHD.ux.dict.DictCheckbox','FHD.ux.kpi.KpiStrategyMapSelector',
		'FHD.ux.fileupload.FileUpload','FHD.ux.kpi.KpiSelectorWindow','FHD.ux.kpi.KpiSelector','FHD.ux.kpi.KpiTree','FHD.ux.kpi.KpiGridPanel',
		'FHD.ux.kpi.KpiStrategyMapTree','FHD.ux.kpi.KpiStrategyMapSelectorWindow','FHD.ux.dict.DictSelectForEditGrid',
		'FHD.ux.risk.RiskStrategyMapSelector',
		'FHD.ux.risk.RiskStrategyMapSelectorWindow',
		'FHD.ux.risk.RiskStrategyMapTree',
		'FHD.ux.collection.CollectionSelector',
		'FHD.ux.collection.CollectionWindow',
		'FHD.ux.timestamp.TimestampSelector',
		'FHD.ux.timestamp.TimestampWindow',
		'FHD.ux.CardPanel',
		'FHD.ux.FusionChartPanel',
		'FHD.ux.TipColumn',
		'FHD.ux.Window',
		'FHD.ux.rule.RuleSelector',
		'FHD.ux.rule.RuleSelectorWindow',
		'FHD.ux.rule.RuleTree',
		'FHD.ux.category.CategoryStrategyMapTree',
		'FHD.ux.kpi.opt.KpiSelector',
		'FHD.ux.kpi.opt.KpiSelectorWindow',
		'FHD.ux.kpi.opt.KpiSelectTree',
		'FHD.ux.kpi.opt.StrategymapTree',
		'FHD.ux.kpi.opt.KpiMyFolderTree',
		'FHD.ux.kpi.KpiTypeTree',
		'Ext.scripts.component.NavigationBars',
		'FHD.view.kpi.Metric',
		'FHD.view.comm.formula.FormulaLog',
		'FHD.view.kpi.bpm.KpiResultsRecorded',
		'FHD.view.wp.WorkPlan',
		'FHD.view.bpm.mywork.MyTask',
		'FHD.ux.MenuPanel',
		'FHD.view.icm.standard.StandardManage',
		'FHD.view.icm.icsystem.FlowMainManage',
		'FHD.ux.kpi.opt.KpiTypeSelect','FHD.view.kpi.result.RealTimeMain',
		'FHD.view.monitor.MainContainer']);

function chartAndListManage(manPanel ,chartPanelId, tablePanelId, xml, html){
	manPanel.items.items[0].removeAll();
	manPanel.items.items[0].add(
			getFunsionChartPanel(chartPanelId, xml));
	manPanel.items.items[0].add(
			getTablePanel(tablePanelId, html));
	manPanel.items.items[0].doLayout();
}

function getFunsionChartPanel(panelId, xml){
	var funsionChartPanel = Ext.create('FHD.ux.FusionChartPanel',{
		id : panelId,
		chartType:'MSColumnLine3D',
		xmlData:xml
	});
	
	return funsionChartPanel;
}

function getTablePanel(panelId, tableHtml){
	var tablePanel = Ext.create('Ext.panel.Panel', {
		id : 'tablePanelId${param._dc}',
        html : tableHtml,
        autoScroll: true,
        bodyStyle: 'border-bottom: 1px solid #bec0c0 !important;'
    });
	
	return tablePanel;
}

Ext.onReady(function() {
	setTimeout(function() {
		Ext.get('loading').remove();
		Ext.get('loading-mask').fadeOut({
			remove : true
		});
		document.getElementById('app-header2').style.display = 'block';
	}, 100);	
	
	
		var dataString ='<chart caption="Top 5 Sales Person" numberPrefix="$" useRoundEdges="1" bgColor="FFFFFF,FFFFFF" showBorder="0">\n\
	<set label="Alex" value="25000"  /> \n\
	<set label="Mark" value="35000" /> \n\
	<set label="David" value="42300" /> \n\
	<set label="Graham" value="35300" /> \n\
	<set label="John" value="31300" />\n\
\n\
</chart>';

var centerPanel = Ext.create('Ext.tab.Panel', {
		id : 'center-panel',
		activeTab : 0,
		enableTabScroll : true,
		animScroll : true,
		border : false,
		split: false,
		//plain:true,
		minTabWidth: 85,
		tabWidth:135,
		tabBar : {
	    height : 24
		},
		resizeTabs: true,
		minSize: 100,
        maxSize: 200,
		autoScroll : true,
		region : 'center',
		/*items : [{
			title : FHD.locale.get('fhd.common.homePage'),
			xtype:'portalpanel',
			layout:'column',
			border:true,
			items : [{
				xtype : 'portalcolumn',
				columnWidth : 0.7,
                items:[
                       	Ext.create('FHD.view.IndexNav'),
                       
                       //Ext.create('FHD.view.IndexNavPanel'),
                       //{title: '我的待办', iconCls : 'icon-link',items: Ext.create('FHD.view.bpm.mywork.MyTask',{height:500})}
	                { title: '公式编辑器',height : 150,iconCls : 'icon-vcard',items:[
						**
						Ext.create('FHD.ux.kpi.FormulaSelector',{
							type:'category',//strategy
							column:'assessmentValueFormula',
							showType:'categoryType',//strategyType
							targetId:'eda8ffeab0da4159be0ff924108e3883JFK13',
							targetName:'产品研发',
							formulaTypeName:'radioName', 
							formulaName:'formulaName',
							fieldLabel:'公式定义'
						})
						
						Ext.create('FHD.ux.kpi.FormulaTrigger',{
					        cols: 20,
				    		rows: 3,
				    		fieldLabel:'公式定义',
					        name:'formulaName',
					        type:'category',
					        showType:'categoryType',
					        column:'assessmentValueFormula',
					        targetId:'eda8ffeab0da4159be0ff924108e3883JFK13',
					        targetName:'产品研发'
					    })
					    
  	                ]},
                	{title: '我的风险',height : 150, iconCls : 'icon-risk'
                		,xtype:'cardpanel',
  	                	activeItem:0,
  	                	tbar:[{
  	                			text:"prev",
  	                			handler:function(){
  	                				this.up('panel').pageMove("prev");
  	                			}
  	                		},{
  	                			text:"next",
  	                			handler:function(){
  	                				this.up('panel').pageMove("next");
  	                			}
  	                		}],
  	                	items:[
  	                	       {title:'面板1'},
  	                	       {title:'面板2'},
  	                	       {title:'面板3'},
  	                	       {title:'面板4'},
  	                	       {title:'面板5'}
  	                	]},
                	{title: '我的指标',height : 400, iconCls : 'icon-asterisk-orange'
                		,items:[{xtype:'fusionchartpanel',xmlData:dataString}]
                	}]
            },{
            	xtype : 'portalcolumn',
            	columnWidth : 0.3,
                items:[{title: '我的待办', height : 350, iconCls : 'icon-link',
                	items:{xtype:'mytask'}},
                	{title: '我的流程',height : 150,iconCls : 'icon-note' },
                	{title: '最新通知', height : 150,iconCls : 'icon-newspaper'}
                ]
            }]
		}],*/
		plugins: [Ext.create('Ext.ux.TabReorderer'),
		  Ext.create('Ext.ux.TabCloseMenu',{
		  	closeTabText: FHD.locale.get('fhd.common.closePanel'),
		  	closeOthersTabsText: FHD.locale.get('fhd.common.closeOther'),
		  	closeAllTabsText: FHD.locale.get('fhd.common.closeAll')
		  })]
	});
	
var northPanel = Ext.create('Ext.panel.Panel', {
		id : 'north-panel',
		region : 'north',
		contentEl : 'app-header2',
		border:false
	});
var toolbar = Ext.create('Ext.toolbar.Toolbar',{
			dock:'top',
			height:28
		});
var headerNavPanel = Ext.create('Ext.panel.Panel', {
		renderTo : 'header-nav2',
		height:35,
		tbar:toolbar
	});
	
	Ext.create('Ext.container.Viewport',{
		layout: {
	        type: 'border',
	        padding: '0 3 0 3'
	    },
	    border:false,
		items : [northPanel,centerPanel],
		listeners : {
			afterrender : function(){
				northPanel.setHeight(71);
				FHD.ajax({
					url : 'sys/menu/authorityTreeLoader.f',
					callback : function(data){
						toolbar.add(data);
						toolbar.add('->',{
                        	id:'timeId',
                        	text : '最新的值',
                        	iconCls : 'icon-dateControl',
                        	handler:function(){
                        		var me = this;
        						this.selectorWindow= Ext.create('FHD.ux.timestamp.TimestampWindow',{
        							onSubmit:function(values){
        								var valuesStr = values.split(',');
        								data.yearId = valuesStr[0];
        								data.quarterId = valuesStr[1];
        								data.monthId = valuesStr[2];
        								data.weekId = valuesStr[3];
        								if(data.yearId == null || data.yearId == ''){
        									FHD.data.yearId = '';
        								}if(data.quarterId == null || data.quarterId == ''){
        									FHD.data.quarterId = '';
        								}if(data.monthId == null || data.monthId == ''){
        									FHD.data.monthId = '';
        								}if(data.weekId == null || data.weekId == ''){
        									FHD.data.weekId = '';
        								}
        								FHD.data.yearId = data.yearId;
        								var gridPanelArray = Ext.ComponentQuery.query('gridpanel');
        								
        								var new_params = {year:FHD.data.yearId,
        										month:FHD.data.monthId,
        										quarter:FHD.data.quarterId,
        										week:FHD.data.weekId,
        										isNewValue : FHD.data.isNewValue,
        										eType : FHD.data.eType
        								};
        								var null_params = {year:'',month:'',quarter:'',week:''};
        								for(var i = 0; i < toolbar.items.items.length; i++){
        									if(toolbar.items.items[i].id == 'timeId'){
        										if(FHD.data.newValue != ''){
        											toolbar.items.items[i].setText(FHD.data.newValue);
        										}
        										break;
        									}
        								}
        								
        								if(gridPanelArray != null){
        									for(var i = 0; i < gridPanelArray.length; i++){
        										/*console.log("id : " + gridPanelArray[i].id + "; url : " + gridPanelArray[i].url)
        										var oldurl = gridPanelArray[i].url;
        										var myURL = parseURL(oldurl);
        										var _newUrl = replaceUrlParams(myURL,new_params);
        										gridPanelArray[i].url = _newUrl;
        										gridPanelArray[i].store.proxy.url = gridPanelArray[i].url;
        										gridPanelArray[i].store.load();*/
        										
        										
        										if(gridPanelArray[i].store.proxy.extraParams){
        											gridPanelArray[i].store.proxy.extraParams.year=FHD.data.yearId;
            										gridPanelArray[i].store.proxy.extraParams.month=FHD.data.monthId;
            										gridPanelArray[i].store.proxy.extraParams.quarter=FHD.data.quarterId;
            										gridPanelArray[i].store.proxy.extraParams.week=FHD.data.weekId;
            										gridPanelArray[i].store.proxy.extraParams.isNewValue=FHD.data.isNewValue;
            										gridPanelArray[i].store.proxy.extraParams.eType = FHD.data.eType;
        										}
        										gridPanelArray[i].store.load();
        									}
        								}
        								
        								//图表、列表
        								if(Ext.getCmp("resultCardPanel") != null){
        									var resultCardPanel = Ext.getCmp('resultCardPanel');
        									//var resultParam = Ext.getCmp('resultParam');
	    									var paraobj = {};
	    									paraobj.eType = FHD.data.eType;
	    									paraobj.kpiname = FHD.data.kpiName;
	    									paraobj.timeId = "";
	    									paraobj.year = FHD.data.yearId;
	        								FHD.ajax({
	        				                    url: __ctxPath + '/kpi/kpi/createtable.f?edit=' + FHD.data.edit,
	        				                    params: {
	        				                    	condItem: Ext.JSON.encode(paraobj)
	        				                    },
	        				                    callback: function (data) {
	        				                        if (data && data.success) {
	        				                        	if(resultCardPanel.items.items.length != 0){
	        				                        		resultCardPanel.removeAll();
	        				        					}
	        				                        	resultCardPanel.add(Ext.widget('funsionChartPanel', {xml : data.xml}));
	        				                        	resultCardPanel.add(Ext.widget('tablePanel', {html : data.tableHtml}));
	        				                        	resultCardPanel.doLayout();
        				                            	Ext.getCmp('tbarChartsButtonId').toggle(true);
        				                            	Ext.getCmp('tbarListButtonId').toggle(false);
        				                            	//Ext.getCmp('tableEditId').hide();
	        				                        	
	        				        					if(resultCardPanel.resultParam.paraobj.isNewValue){
	        				        						FHD.data.yearId = data.year;
	        				        					}
	        				                        }
	        				                    }
	        				                });
        								}
        							}
        						}).show();
        					}
                        });
						
					}
				});
			},
			resize:function(p){
				headerNavPanel.setWidth(p.getWidth()-6);
			}
		}
	});
});


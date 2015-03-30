<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%>
    <%@ include file="/WEB-INF/jsp/commons/include-tagsOnly.jsp" %>
        <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
        <html>
            
            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <title></title>
                <script type="text/javascript">
					
                <%@ include file="/pages/kpi/kpi/opt/kpicategorytrees.jsp" %>
                <%@ include file="/pages/kpi/strategyMap/strategymtree.jsp" %>
                <%@ include file="/pages/kpi/strategyMap/strategymap.jsp" %>
                <%@ include file="/pages/kpi/kpi/opt/kpitypetrees.jsp" %>
                <%@ include file="/pages/kpi/myfolder/myfoldertree.jsp" %>
                    
					
					 var kpiActivePanelflag = '';
                     function panelSort(p) {
                        // 获得所有关闭的面板
                        var panels = fhd_kpi_kpiaccordion_view.accordion.query('>panel');
                        var items = new Array();
                        for (i = 0; i < panels.length; i++) {
                            if (panels[i].title != p.title) {
                                items.push(panels[i]);
                            }
                        }
                        // 先移除他们
                        for (i = 0; i < items.length; i++) {
                            fhd_kpi_kpiaccordion_view.accordion.remove(items[i], false);
                        }
                        // 冒泡排序
                        for (i = 0; i < items.length; i++) {
                            for (j = 0; j < items.length; j++) {
                                var temp;
                                if (items[i].index < items[j].index) {
                                    temp = items[j];
                                    items[j] = items[i];
                                    items[i] = temp;
                                }
                            }
                        }
                        // 按排序的顺序添加
                        for (i = 0; i < items.length; i++) {
                            fhd_kpi_kpiaccordion_view.accordion.insert(i + 1, items[i]);
                        }
                    }

                    var fhd_kpi_kpiaccordion_view;


                    Ext.define('rightPanel', {
                        extend: 'Ext.panel.Panel',
                        layout: 'fit',
                        border: false,
                        autoScroll: false,
                        region: 'center',
                        listeners: {
                        	destroy:function(component, eOpts){
                        		//销毁grid
                        		var gridPanelArray = Ext.ComponentQuery.query("gridpanel[destoryflag='true']");
                        		if(gridPanelArray != null){
                        			for(var i = 0; i < gridPanelArray.length; i++){
                        				gridPanelArray[i].destroy();
                        			}
                        		}
                        		//销毁panel
                        		/* var panelArray = Ext.ComponentQuery.query("panel[destoryflag='true']");
                        		if(panelArray != null){
                        			for(var i = 0; i < panelArray.length; i++){
                        				panelArray[i].destroy();
                        			}
                        		} */
                        		
                        	}
                        }
                    });

                    var renderTpl = [
                                '<em id="{id}-btnWrap"<tpl if="splitCls"> class="{splitCls}"</tpl>>',
                                '<button id="{id}-btnEl" type="{type}" class="{btnCls}" hidefocus="true"',
                                    // the autocomplete="off" is required to prevent Firefox from remembering
                                    // the button's disabled state between page reloads.
                                    '<tpl if="tabIndex"> tabIndex="{tabIndex}"</tpl>',
                                    '<tpl if="disabled"> disabled="disabled"</tpl>',
                                    ' role="button" autocomplete="off">',
                                    '<span id="{id}-btnInnerEl" class="{baseCls}-inner" style="{innerSpanStyle}">',
                                        '{text}',
                                    '</span>',
                                    '<span id="{id}-btnIconEl" class="{baseCls}-icon {iconCls}"<tpl if="iconUrl"> style="background-image:url({iconUrl})"</tpl>></span>',
                                    '<span id="{id}-btnIconEl1" style="background-image:url(\'${ctx}/scripts/ext-4.1/resources/themes/images/default/tools/tool-sprites.gif\');width: 15px;height: 15px;top: 7px;left: auto;bottom: 0;right: 5px;position:absolute;background-repeat:no-repeat;background-position-x:0px;background-position-y:-240px;"></span>',
                                '</button>',
                        '</em>'
                    ];
                    
                    // 左侧风琴式控件
                    Ext.define('FHD.kpi.kpiAccordion.view', {


                        initRightPanel: function (url) {
                            var me = this;
                            me.container.remove(this.container.rightpanel, true);
                            me.rightpanel = Ext.create('rightPanel', {
                                autoLoad: {
                                    url: url,
                                    scripts: true
                                }
                            });
                            me.container.rightpanel = me.rightpanel;
                            me.container.add(me.rightpanel);
                        },

                        
                        
                        
                        init: function () {
                            var me = this;
                            me.cardpanel = Ext.create('FHD.ux.CardPanel',{
      						   border:false,
     						   style : 'border-bottom: 1px  #99bce8 solid;',
     						   flex:1,
     						   items:[
     						    fhd_myfolder_tree_view.tree,
     						    
     						    fhd_strategymap_tree_view.tree,
                                
                                fhd_kpi_sm_tree_view.tree,
                                
                                fhd_kpi_kpitypetree_view.tree,
                                
                                kpi_category_tree_view.tree
                                ]
     					   }),
                            
	                            me.leftPanel = Ext.create('Ext.panel.Panel',{
	                          	   title:'我的文件夹',
	                         	   layout:{
	                         		   type:'vbox',
	                         		   align:'stretch'
	                         	   },
	                         	   border:true,
	                         	   renderTo: 'FHD.kpi.kpiAccordion.view${param._dc}',
	                               split: true,
	                               collapsible: true,
	                               region: 'west',
	                               width: 265,
	                               maxWidth: 300,
	     						   height: FHD.getCenterPanelHeight(),
	     						  items:[me.cardpanel,{
	   							   	id:'group-view${param._dc}',
	                                  xtype:'button',
	                                  iconCls: 'icon-ibm-new-group-view',
	                                  textAlign :'left',
	                                  height:30,
	                                  style:'border-bottom: 1px  #99bce8 solid !important;',
	                                  cls:'aaa-btn aaa-selected-btn',
	                                  text: '我的文件夹',
	                                  handler:function(){
	                                  	this.addCls('aaa-selected-btn');
	                                  	Ext.getCmp('metrictypes${param._dc}').removeCls('aaa-selected-btn');
	                                      Ext.getCmp('strategy${param._dc}').removeCls('aaa-selected-btn');
	                                      Ext.getCmp('reports${param._dc}').removeCls('aaa-selected-btn');
	                                      Ext.getCmp('scorecards${param._dc}').removeCls('aaa-selected-btn');
	                                	   me.cardpanel.setActiveItem(fhd_myfolder_tree_view.tree);
	                                	   me.leftPanel.setTitle(this.text);
	                                	   me.leftPanel.setIconCls(this.iconCls);
	                                	   fhd_myfolder_tree_view.tree.clicked();
	                                   }
	                              },{
	                              	id:'reports${param._dc}',
	                                  xtype:'button',
	                                  iconCls: 'icon-ibm-icon-reports',
	                                  textAlign :'left',
	                                  text: '战略地图',
	                                  style:'border-top: 1px  #f3f7fb solid !important;border-bottom: 1px  #99bce8 solid !important;',
	                                  height:30,
	                                  cls:'aaa-btn',
	                                  handler:function(){
	                                  	this.addCls('aaa-selected-btn');
	                                  	Ext.getCmp('metrictypes${param._dc}').removeCls('aaa-selected-btn');
	                                      Ext.getCmp('strategy${param._dc}').removeCls('aaa-selected-btn');
	                                      Ext.getCmp('group-view${param._dc}').removeCls('aaa-selected-btn');
	                                      Ext.getCmp('scorecards${param._dc}').removeCls('aaa-selected-btn');
	                               	   me.cardpanel.setActiveItem(fhd_strategymap_tree_view.tree);
	                               	   me.leftPanel.setTitle(this.text);
	                               	   me.leftPanel.setIconCls(this.iconCls);
	                                  }
	                              },{
	                              	id:'strategy${param._dc}',
	                                  xtype:'button',
	                                  iconCls: 'icon-strategy',
	                                  textAlign :'left',
	                                  text: '战略目标',
	                                  style:'border-top: 1px  #f3f7fb solid !important;border-bottom: 1px  #99bce8 solid !important;',
	                                  height:30,
	                                  cls:'aaa-btn',
	                                  handler:function(){
	                                  	this.addCls('aaa-selected-btn');
	                                  	Ext.getCmp('metrictypes${param._dc}').removeCls('aaa-selected-btn');
	                                      Ext.getCmp('reports${param._dc}').removeCls('aaa-selected-btn');
	                                      Ext.getCmp('group-view${param._dc}').removeCls('aaa-selected-btn');
	                                      Ext.getCmp('scorecards${param._dc}').removeCls('aaa-selected-btn');
	                                	   me.cardpanel.setActiveItem(fhd_kpi_sm_tree_view.tree);
	                                	   me.leftPanel.setTitle(this.text);
	                                	   me.leftPanel.setIconCls(this.iconCls);
	                                	   fhd_kpi_sm_tree_view.tree.clicked();
	                                   }
	                              },{
	                              	id:'metrictypes${param._dc}',
	                                  xtype:'button',
	                                  iconCls: 'icon-ibm-icon-metrictypes',
	                                  textAlign :'left',
	                                  text: '指标类型',
	                                  style:'border-top: 1px  #f3f7fb solid !important;border-bottom: 1px  #99bce8 solid !important;',
	                                  height:30,
	                                  cls:'aaa-btn',
	                                  handler:function(){
	                                  	this.addCls('aaa-selected-btn');
	                                      Ext.getCmp('strategy${param._dc}').removeCls('aaa-selected-btn');
	                                      Ext.getCmp('reports${param._dc}').removeCls('aaa-selected-btn');
	                                      Ext.getCmp('group-view${param._dc}').removeCls('aaa-selected-btn');
	                                      Ext.getCmp('scorecards${param._dc}').removeCls('aaa-selected-btn');
	                                  	
	                                	   me.cardpanel.setActiveItem(fhd_kpi_kpitypetree_view.tree);
	                                	   me.leftPanel.setTitle(this.text);
	                                	   me.leftPanel.setIconCls(this.iconCls);
	                                	   fhd_kpi_kpitypetree_view.tree.clicked();
	                                   }
	                              },{
	                              	id:'scorecards${param._dc}',
	                                  xtype:'button',
	                                  iconCls: 'icon-ibm-icon-scorecards',
	                                  textAlign :'left',
	                                  text: '记分卡',
	                                  height:30,
	                                  style:'border-top: 1px  #f3f7fb solid !important;',
	                                  cls:'aaa-btn',
	                                  handler:function(){
	                                     this.addCls('aaa-selected-btn');
	                                     Ext.getCmp('metrictypes${param._dc}').removeCls('aaa-selected-btn');
	                                     Ext.getCmp('strategy${param._dc}').removeCls('aaa-selected-btn');
	                                     Ext.getCmp('reports${param._dc}').removeCls('aaa-selected-btn');
	                                     Ext.getCmp('group-view${param._dc}').removeCls('aaa-selected-btn');
	                                     
	                                	   me.cardpanel.setActiveItem(kpi_category_tree_view.tree);
	                                	   me.leftPanel.setTitle(this.text);
	                                	   me.leftPanel.setIconCls(this.iconCls);
	                                	   kpi_category_tree_view.tree.clicked();
	                                	   
	                                   }
	                              }]
	                            });
                            
	                            me.container = Ext.create('Ext.container.Container', {
	                                renderTo: 'FHD.kpi.kpiAccordion.view${param._dc}',
	                                border: false,
	                                accordion:me.leftPanel,
	                                height: FHD.getCenterPanelHeight(),
	                                layout: {
	                                    type: 'border'
	                                },
	                                items: [me.leftPanel]
	                            });
                        }
                    });

                    var kpitype_container;
                    var kpicategoryMainPanel;
                    var kpiMainPanel;
                    var smcontainer;

                    Ext.onReady(function () {
                        fhd_kpi_kpiaccordion_view = Ext.create('FHD.kpi.kpiAccordion.view');
                        fhd_kpi_kpiaccordion_view.init();
                        FHD.componentResize(fhd_kpi_kpiaccordion_view.container, 0, 0);


                          fhd_kpi_kpiaccordion_view.leftPanel.on('collapse', function (p) {
                        	
                            var setwidth = fhd_kpi_kpiaccordion_view.container.getWidth() - 29;
                            if(FHD.panel.basicPanel != null){
                            	FHD.panel.basicPanel.setWidth(setwidth);
                            }if(FHD.panel.basicInputPanel != null){
                            	FHD.panel.basicInputPanel.setWidth(setwidth);
                            }
                            if (kpiActivePanelflag=='kpitype') {
                                kpitype_container.setWidth(setwidth);
                            }
                            if (kpiActivePanelflag=='kpicategory') {
                                kpicategoryMainPanel.setWidth(setwidth);
                            }
                            if (kpiActivePanelflag=="kpi") {
                                kpiMainPanel.setWidth(setwidth);
                            }
                            if (kpiActivePanelflag=='sm') {
                                smcontainer.setWidth(setwidth);
                            }

                        });

                        fhd_kpi_kpiaccordion_view.leftPanel.on('expand', function (p) {
                            var setwidth = fhd_kpi_kpiaccordion_view.container.getWidth() - p.getWidth() - 5;
                            if(FHD.panel.basicPanel != null){
                            	FHD.panel.basicPanel.setWidth(setwidth);
                            }if(FHD.panel.basicInputPanel != null){
                            	FHD.panel.basicInputPanel.setWidth(setwidth);
                            }
                            if (kpiActivePanelflag=='kpitype') {
                                kpitype_container.setWidth(setwidth);
                            }
                            if (kpiActivePanelflag=='kpicategory') {
                                kpicategoryMainPanel.setWidth(setwidth);
                            }
                            if (kpiActivePanelflag=="kpi") {
                                kpiMainPanel.setWidth(setwidth);
                            }
                            if (kpiActivePanelflag=='sm') {
                                smcontainer.setWidth(setwidth);
                            }
                        });

                        fhd_kpi_kpiaccordion_view.container.on('resize', function (p) {
                        	 var treepanel = p.accordion;
                             if (treepanel.collapsed==false) {
                                var setwidth = p.getWidth() - treepanel.getWidth() - 5;
                                var setheight = p.getHeight()-16;
                                if (kpiActivePanelflag=='kpitype') {
                                    kpitype_container.setWidth(setwidth);
                                    kpitype_container.setHeight(setheight);
                                }
                                if (kpiActivePanelflag=='kpicategory') {
                                    kpicategoryMainPanel.setWidth(setwidth);
                                    kpicategoryMainPanel.setHeight(setheight);
                                }
                                if (kpiActivePanelflag=="kpi") {
                                    kpiMainPanel.setWidth(setwidth);
                                    kpiMainPanel.setHeight(p.getHeight()-35);
                                } 
                                if (kpiActivePanelflag=='sm') {
                                    smcontainer.setWidth(setwidth);
                                    smcontainer.setHeight(p.getHeight());
                                }
                            }else{
                            	var setwidth = fhd_kpi_kpiaccordion_view.container.getWidth()-29;
                            	var setheight = p.getHeight()-16;
                            	if (kpiActivePanelflag=='kpitype') {
                                    kpitype_container.setWidth(setwidth);
                                    kpitype_container.setHeight(setheight);
                                }
                                if (kpiActivePanelflag=='kpicategory') {
                                    kpicategoryMainPanel.setWidth(setwidth);
                                    kpicategoryMainPanel.setHeight(setheight);
                                }
                                if (kpiActivePanelflag=="kpi") {
                                    kpiMainPanel.setWidth(setwidth);
                                    kpiMainPanel.setHeight(p.getHeight()-35);
                                } 
                                if (kpiActivePanelflag=='sm') {
                                    smcontainer.setWidth(setwidth);
                                    smcontainer.setHeight(p.getHeight());
                                }
                            } 

                        });
                        
                        fhd_kpi_kpiaccordion_view.leftPanel.on('resize', function (p) {
                        	var setwidth = fhd_kpi_kpiaccordion_view.container.getWidth() - p.getWidth() - 5;
                            var setheight = p.getHeight()-16;
                        	if (p.collapsed==false) {
                        		if (kpiActivePanelflag=='kpitype') {
                                    kpitype_container.setWidth(setwidth);
                                    kpitype_container.setHeight(setheight);
                                    fhd_kpi_kpitypetree_view.tree.setWidth(fhd_kpi_kpiaccordion_view.container.getWidth() - kpitype_container.getWidth() - 7);
                                }
                                if (kpiActivePanelflag=='kpicategory') {
                                    kpicategoryMainPanel.setWidth(setwidth);
                                    kpicategoryMainPanel.setHeight(setheight);
                                    kpi_category_tree_view.tree.setWidth(fhd_kpi_kpiaccordion_view.container.getWidth() - kpicategoryMainPanel.getWidth() - 7);
                                }
                                if (kpiActivePanelflag=="kpi") {
                                    kpiMainPanel.setWidth(setwidth);
                                    kpiMainPanel.setHeight(p.getHeight()-35);
                                    kpi_category_tree_view.tree.setWidth(fhd_kpi_kpiaccordion_view.container.getWidth() - kpiMainPanel.getWidth() - 7);
                                } 
                                if (kpiActivePanelflag=='sm') {
                                    smcontainer.setWidth(setwidth);
                                    smcontainer.setHeight(p.getHeight());
                                    fhd_kpi_sm_tree_view.tree.setWidth(fhd_kpi_kpiaccordion_view.container.getWidth() - smcontainer.getWidth() - 7);
                                }
                        	}
                        	
                        });  

                    });
                </script>
            </head>
            
            <body>
                <div id="FHD.kpi.kpiAccordion.view${param._dc}"></div>
            </body>
        
        </html>
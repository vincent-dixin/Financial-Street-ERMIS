Ext.define('FHD.ux.treeChar.TreeCharExt', {
	extend : 'Ext.panel.Panel',
	alias : 'widget.treeCharExt',
	
	graph:null,
	renderDiv:null,
	path:null,
	width:"auto",
	height:"auto",
	treeCharModel:null,
	root:null,
	open:null,
	add:null,
	del:null,
	
	addOverlays:function (graph, cell, addDeleteIcon){
		var me=this;
		if(typeof(me.open)=="function"){
			var overlay = new mxCellOverlay(new mxImage(me.path+'/scripts/component/treeChar/images/open.png', 24, 24), '展开');
			overlay.cursor = 'hand';
			overlay.align = mxConstants.ALIGN_CENTER;
			overlay.addListener(mxEvent.CLICK, mxUtils.bind(me, function(sender, evt){
				if(sender.image.src==me.path+'/scripts/component/treeChar/images/open.png'){
					graph.removeCellOverlay(cell, sender);
					var nodes=me.open(cell);
					if(nodes!=null){
						var length=nodes.length;
						for (var i = 0; i < length; i++) {
							me.addChild(graph, cell,nodes[i]);
						}
					}
					sender.image.src=me.path+'/scripts/component/treeChar/images/close.png';
					sender.tooltip="收起";
					
					graph.addCellOverlay(cell, sender);
				}else if(sender.image.src==me.path+'/scripts/component/treeChar/images/close.png'){
					me.deleteSubtree(graph, cell,false);
					graph.removeCellOverlay(cell, sender);
					sender.image.src=me.path+'/scripts/component/treeChar/images/open.png';
					sender.tooltip="展开";
					graph.addCellOverlay(cell, sender);
				}
			}));
			graph.addCellOverlay(cell, overlay);
		}
		//删除按钮
		/*if (addDeleteIcon)
		{
			overlay = new mxCellOverlay(new mxImage(me.path+'/scripts/component/treeChar/images/del.png', 30, 30), '删除');
			overlay.cursor = 'hand';
			overlay.offset = new mxPoint(-4, 8);
			overlay.align = mxConstants.ALIGN_RIGHT;
			overlay.verticalAlign = mxConstants.ALIGN_TOP;
			overlay.addListener(mxEvent.CLICK, mxUtils.bind(me, function(sender, evt){
				me.deleteSubtree(graph, cell);
			}));
		
			graph.addCellOverlay(cell, overlay);
		}*/
	},
	
	addChild:function (graph, cell,node){
		var me=this;
		var model = graph.getModel();
		var parent = graph.getDefaultParent();
	
		model.beginUpdate();
		try
		{
			var id="";
			var value="新建节点";
			var image="";
			if(node){
				if(node.id){
					id=node.id;
				}
				if(node.value){
					value=node.value;
				}
				if(node.image){
					image=node.image;
				}
			}
			var style = graph.getStylesheet().getDefaultVertexStyle();
			style[mxConstants.STYLE_IMAGE] = image;
			var vertex = graph.insertVertex(parent, id, value,50,50);
			var geometry = model.getGeometry(vertex);
	
			var size = graph.getPreferredSizeForCell(vertex);
			geometry.width = size.width;
			geometry.height = size.height;
	
			var edge = graph.insertEdge(parent, null, '', cell, vertex);
	
			edge.geometry.x = 1;
			edge.geometry.y = 0;
			edge.geometry.offset = new mxPoint(0, -20);
	
			me.addOverlays(graph, vertex, true);
		}
		finally
		{
			model.endUpdate();
		}
	},
	
	deleteSubtree:function (graph, cell,doIt){
		var me=this;
		var cells = [];
		graph.traverse(cell, true, function(vertex){
			if(doIt==false&&cell.id==vertex.id){
				return true;
			}
			cells.push(vertex);
			return true;
		});
		if(doIt!=false){
			if(typeof(me.del)=="function"){
				var flag=me.del(cell);
				if(typeof(flag)!="undefined"&&flag==false){
					return false;
				}
			}
		}
		graph.removeCells(cells);
	},
	
	initComponent: function(){
		var me=this;
		if (!mxClient.isBrowserSupported()){
			Ext.Msg.alert(FHD.locale.get('fhd.common.prompt'),"浏览器可能不支持该显示方式！");
		}
		if(!me.path){
			Ext.Msg.alert(FHD.locale.get('fhd.common.prompt'),"引用有误请确定引用代码");
			return; 
		}
		var oHead = document.getElementsByTagName('HEAD').item(0); 
		if(jQuery("[href='"+me.path+"/scripts/mxgraph-1.10/css/common.css']").length==0){
		    var oStyle= document.createElement("link"); 
		    oStyle.type = "text/css"; 
		    oStyle.rel = "stylesheet"; 
		    oStyle.href=me.path+"/scripts/mxgraph-1.10/css/common.css"; 
		    oHead.appendChild(oStyle);
		}
		if(jQuery("[href='"+me.path+"/scripts/mxgraph-1.10/css/explorer.css']").length==0){
		    var oStyle= document.createElement("link"); 
		    oStyle.type = "text/css"; 
		    oStyle.rel = "stylesheet"; 
		    oStyle.href=me.path+"/scripts/mxgraph-1.10/css/explorer.css"; 
		    oHead.appendChild(oStyle);
		}
		if(jQuery("[href='"+me.path+"/scripts/component/treeChar/treeChar.css']").length==0){
		    var oStyle= document.createElement("link"); 
		    oStyle.type = "text/css"; 
		    oStyle.rel = "stylesheet"; 
		    oStyle.href=me.path+"/scripts/component/treeChar/treeChar.css"; 
		    oHead.appendChild(oStyle);
		}
		if(me.height=="auto"||isNaN(me.height)){
			me.height=jQuery("#"+me.renderTo).height();
		}
		Ext.applyIf(me,{
			listeners:{
				height:me.height,
				afterrender : function() {
					me.init();
				}
			}
		});
		
		me.callParent(arguments);
	},
	init:function(){
		var me=this;
		me.renderDiv=jQuery("#"+me.id + '-body')[0];
		jQuery("#"+me.id).parent().resize(function(){
			me.width=jQuery("#"+me.id).parent().width();
			me.height=jQuery("#"+me.id).parent().height();
			me.setWidth(me.width);
			me.setHeight(me.height);
		});
		jQuery(me.renderDiv).addClass("treeCharDiv");
		//图表主体附着对象
		var container=jQuery("<div></div>")[0];
		jQuery(container).addClass("container");
		jQuery(me.renderDiv).append(container);
		//鹰视图附着对象
		var outline=jQuery("<div></div>")[0];
		jQuery(outline).addClass("outline");
		jQuery(me.renderDiv).append(outline);
		//工具栏附着对象
		var toolBar=jQuery("<div></div>")[0];
		jQuery(toolBar).addClass("toolBar");
		jQuery(me.renderDiv).append(toolBar);
		var content=jQuery("<div></div>")[0];
		jQuery(me.renderDiv).append(content);
		content.style.padding = '4px'; 
		//未知
		mxEvent.disableContextMenu(container);
		//设定背景图
	    if (mxClient.IS_GC || mxClient.IS_SF){
	    	container.style.background = '-webkit-gradient(linear, 0 0%, 0% 100%, from(#FFFFFF), to(#E7E7E7))';
	    }else if (mxClient.IS_NS){
	    	container.style.background = '-moz-linear-gradient(top, #FFFFFF, #E7E7E7)';  
	    }else if (mxClient.IS_IE){   
			new mxDivResizer(container);
			new mxDivResizer(outline);
	    	container.style.filter = 'progid:DXImageTransform.Microsoft.Gradient('+'StartColorStr=\'#FFFFFF\', EndColorStr=\'#E7E7E7\', GradientType=0)';
	    }
	
		//创建
		var graph = new mxGraph(container);
	
		//鹰视图附着对象
		var outln = new mxOutline(graph, outline);
		
		graph.setTooltips(!mxClient.IS_TOUCH);
	
		//节点样式
		var style = graph.getStylesheet().getDefaultVertexStyle();
		style[mxConstants.STYLE_SHAPE] = 'label';
		
		style[mxConstants.STYLE_VERTICAL_ALIGN] = mxConstants.ALIGN_MIDDLE;
		style[mxConstants.STYLE_ALIGN] = mxConstants.ALIGN_LEFT;
		style[mxConstants.STYLE_SPACING_LEFT] = 54;
		
		style[mxConstants.STYLE_GRADIENTCOLOR] = '#7d85df';
		style[mxConstants.STYLE_STROKECOLOR] = '#5d65df';
		style[mxConstants.STYLE_FILLCOLOR] = '#adc5ff';
		
		style[mxConstants.STYLE_FONTCOLOR] = '#1d258f';
		style[mxConstants.STYLE_FONTFAMILY] = 'Verdana';
		style[mxConstants.STYLE_FONTSIZE] = '12';
		style[mxConstants.STYLE_FONTSTYLE] = '1';
		
		style[mxConstants.STYLE_SHADOW] = '1';
		style[mxConstants.STYLE_ROUNDED] = '1';
		style[mxConstants.STYLE_GLASS] = '1';
	
		style = graph.getStylesheet().getDefaultEdgeStyle();
		style[mxConstants.STYLE_ROUNDED] = true;
		style[mxConstants.STYLE_STROKEWIDTH] = 3;
		style[mxConstants.STYLE_EXIT_X] = 0.5; 
		style[mxConstants.STYLE_EXIT_Y] = 1.0; 
		style[mxConstants.STYLE_EXIT_PERIMETER] = 0; 
		style[mxConstants.STYLE_ENTRY_X] = 0.5; 
		style[mxConstants.STYLE_ENTRY_Y] = 0;
		style[mxConstants.STYLE_ENTRY_PERIMETER] = 0; 
		
		style[mxConstants.STYLE_EDGE] = mxEdgeStyle.TopToBottom;
		
		
		
		// 节点设置
		graph.setCellsMovable(false);
		graph.setAutoSizeCells(false);
		graph.setPanning(true);
		graph.panningHandler.useLeftButtonForPanning = true;
		
	
		var keyHandler = new mxKeyHandler(graph);
	
		var layout = new mxCompactTreeLayout(graph, false);
		layout.useBoundingBox = false;
		layout.edgeRouting = false;
		layout.levelDistance = 60;
		layout.nodeDistance = 16;
	
		//新节点布局
		layout.isVertexMovable = function(cell){
			return true;
		};
		var layoutMgr = new mxLayoutManager(graph);
		layoutMgr.getLayout = function(cell){
			if (cell.getChildCount() > 0){
				return layout;
			}
		};
		
		// 添加右键菜单
		graph.panningHandler.factoryMethod = function(menu, cell, evt){
			var model = graph.getModel();
			if(graph.treeCharModel=="edit"){
				if (cell != null){
					if (model.isVertex(cell)){
						menu.addItem('添加子节点', graph.path+'/scripts/component/treeChar/images/check.png', function(){
							if(typeof(me.add)=="function"){
								me.add(cell);
								var node=me.add(cell);
								if(typeof(node)!="undefined"&&node==false){
									return false;
								}
							}
							me.addChild(graph, cell,node);
						});
					}
			
					menu.addItem('编辑内容', graph.path+'/scripts/component/treeChar/images/text.gif', function(){
						graph.startEditingAtCell(cell);
					});
			
					if (cell.id != 'treeRoot' &&model.isVertex(cell)){
						menu.addItem('删除', graph.path+'/scripts/component/treeChar/images/delete.gif', function(){
							me.deleteSubtree(graph, cell);
						});
					}
			
					menu.addSeparator();
				}
			}
		
			menu.addItem('放大', graph.path+'/scripts/component/treeChar/images/zoom.gif', function(){
				graph.fit();
			});
		
			menu.addItem('默认大小', graph.path+'/scripts/component/treeChar/images/zoomactual.gif', function()
			{
				graph.zoomActual();
			});
		};
	
		//增加根节点
		var parent = graph.getDefaultParent();
		var w = graph.container.offsetWidth;
		var id='root';
		var value="root";
		var image="";
		if(me.root){
			if(me.root.id){
				id=me.root.id;
			}
			if(me.root.value){
				value=me.root.value;
			}
			if(me.root.image){
				image=me.root.image;
			}
		}
		var v1 = graph.insertVertex(parent, id,value, w/2 - 30, 20, 140, 60, 'image='+image);
		graph.updateCellSize(v1);
	
		me.addOverlays(graph, v1, false);
		
		//工具栏
	
		var tb = new mxToolbar(content);
	
		tb.addItem('放大', me.path+'/scripts/component/treeChar/images/zoom_in32.png',function(evt)
		{
			graph.zoomIn();
		});
	
		tb.addItem('缩小', me.path+'/scripts/component/treeChar/images/zoom_out32.png',function(evt)
		{
			graph.zoomOut();
		});
		
		tb.addItem('默认', me.path+'/scripts/component/treeChar/images/view_1_132.png',function(evt)
		{
			graph.zoomActual();
		});
	
		wnd = new mxWindow('工具栏', content, 0, 0, 130, 66, false,null,null,null,toolBar);
		wnd.setMaximizable(false);
		wnd.setScrollable(false);
		wnd.setResizable(false);
		wnd.setVisible(true);
		
		graph.renderDiv=me.renderDiv;
		graph.path=me.path;
		graph.treeCharModel=me.treeCharModel;
		graph.open=me.open;
		graph.add=me.add;
		graph.del=me.del;
		graph.addOverlays=me.addOverlays;
		graph.addChild=me.addChild;
		graph.deleteSubtree=me.deleteSubtree;
		me.graph=graph;
	}
});
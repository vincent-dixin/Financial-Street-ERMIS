function treeChar(config){
	//兼容性验证
	if (!mxClient.isBrowserSupported()){
		Ext.MessageBox.confirm(FHD.locale.get('fhd.common.prompt'),"浏览器可能不支持该显示方式！是否允许系统尝试操作？", function showResult(btn){
	        if("no"==btn){
	        	return null;
	        }
	    });
	}
	if(!config.render||!config.path){
		Ext.Msg.alert(FHD.locale.get('fhd.common.prompt'),"引用有误请确定引用代码");
		return null;
	}
	this.render=config.render;
	this.path=config.path;
	this.treeCharModel=config.treeCharModel;
	this.root=config.root;
	this.open=config.open;
	this.add=config.add;
	this.del=config.del;
	this.addOverlays=function (graph, cell, addDeleteIcon){
		if(typeof(this.open)=="function"){
			var overlay = new mxCellOverlay(new mxImage(this.path+'/scripts/component/treeChar/images/open.png', 24, 24), '展开');
			overlay.cursor = 'hand';
			overlay.align = mxConstants.ALIGN_CENTER;
			overlay.addListener(mxEvent.CLICK, mxUtils.bind(this, function(sender, evt){
				if(sender.image.src==this.path+'/scripts/component/treeChar/images/open.png'){
					graph.removeCellOverlay(cell, sender);
					var nodes=this.open(cell);
					if(nodes!=null){
						var length=nodes.length;
						for (var i = 0; i < length; i++) {
							this.addChild(graph, cell,nodes[i]);
						}
					}
					sender.image.src=this.path+'/scripts/component/treeChar/images/close.png';
					sender.tooltip="收起";
					
					graph.addCellOverlay(cell, sender);
				}else if(sender.image.src==this.path+'/scripts/component/treeChar/images/close.png'){
					this.deleteSubtree(graph, cell,false);
					graph.removeCellOverlay(cell, sender);
					sender.image.src=this.path+'/scripts/component/treeChar/images/open.png';
					sender.tooltip="展开";
					graph.addCellOverlay(cell, sender);
				}
			}));
			graph.addCellOverlay(cell, overlay);
		}
		//删除按钮
		/*if (addDeleteIcon)
		{
			overlay = new mxCellOverlay(new mxImage(this.path+'/scripts/component/treeChar/images/del.png', 30, 30), '删除');
			overlay.cursor = 'hand';
			overlay.offset = new mxPoint(-4, 8);
			overlay.align = mxConstants.ALIGN_RIGHT;
			overlay.verticalAlign = mxConstants.ALIGN_TOP;
			overlay.addListener(mxEvent.CLICK, mxUtils.bind(this, function(sender, evt){
				this.deleteSubtree(graph, cell);
			}));
		
			graph.addCellOverlay(cell, overlay);
		}*/
	};
		
	this.addChild=function (graph, cell,node){
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
	
			// Updates the geometry of the vertex with the
			// preferred size computed in the graph
			var size = graph.getPreferredSizeForCell(vertex);
			geometry.width = size.width;
			geometry.height = size.height;
	
			// Adds the edge between the existing cell
			// and the new vertex and executes the
			// automatic layout on the parent
			var edge = graph.insertEdge(parent, null, '', cell, vertex);
	
			// Configures the edge label "in-place" to reside
			// at the end of the edge (x = 1) and with an offset
			// of 20 pixels in negative, vertical direction.
			edge.geometry.x = 1;
			edge.geometry.y = 0;
			edge.geometry.offset = new mxPoint(0, -20);
	
			this.addOverlays(graph, vertex, true);
		}
		finally
		{
			model.endUpdate();
		}
	};
		
	this.deleteSubtree=function (graph, cell,doIt){
		var cells = [];
		graph.traverse(cell, true, function(vertex){
			if(doIt==false&&cell.id==vertex.id){
				return true;
			}
			cells.push(vertex);
			return true;
		});
		if(doIt!=false){
			if(typeof(this.del)=="function"){
				var flag=this.del(cell);
				if(typeof(flag)!="undefined"&&flag==false){
					return false;
				}
			}
		}
		graph.removeCells(cells);
	};
	var width=config.width;
	if(!width){
		width="100%";
	}
	jQuery(this.render).width(width);
	var height=config.height;
	if(!height){
		height="100%";
	}
	jQuery(this.render).height(height);
	jQuery(this.render).addClass("treeCharDiv");
	//图表主体附着对象
	var container=jQuery("<div></div>")[0];
	jQuery(container).addClass("container");
	jQuery(this.render).append(container);
	//鹰视图附着对象
	var outline=jQuery("<div></div>")[0];
	jQuery(outline).addClass("outline");
	jQuery(this.render).append(outline);
	//工具栏附着对象
	var toolBar=jQuery("<div></div>")[0];
	jQuery(toolBar).addClass("toolBar");
	jQuery(this.render).append(toolBar);
	var content=jQuery("<div></div>")[0];
	jQuery(this.render).append(content);
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
	
	// Disables tooltips on touch devices
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

	// Sets the default style for edges
	style = graph.getStylesheet().getDefaultEdgeStyle();
	style[mxConstants.STYLE_ROUNDED] = true;
	style[mxConstants.STYLE_STROKEWIDTH] = 3;
	style[mxConstants.STYLE_EXIT_X] = 0.5; // center
	style[mxConstants.STYLE_EXIT_Y] = 1.0; // bottom
	style[mxConstants.STYLE_EXIT_PERIMETER] = 0; // disabled
	style[mxConstants.STYLE_ENTRY_X] = 0.5; // center
	style[mxConstants.STYLE_ENTRY_Y] = 0; // top
	style[mxConstants.STYLE_ENTRY_PERIMETER] = 0; // disabled
	
	// Disable the following for straight lines
	style[mxConstants.STYLE_EDGE] = mxEdgeStyle.TopToBottom;
	
	
	
	// 节点设置
	graph.setCellsMovable(false);
	graph.setAutoSizeCells(false);
	graph.setPanning(true);
	graph.panningHandler.useLeftButtonForPanning = true;
	

	// Displays a popupmenu when the user clicks
	// on a cell (using the left mouse button) but
	// do not select the cell when the popup menu
	// is displayed
	/* graph.panningHandler.selectOnPopup = false; */

	// Stops editing on enter or escape keypress
	var keyHandler = new mxKeyHandler(graph);

	// Enables automatic layout on the graph and installs
	// a tree layout for all groups who's children are
	// being changed, added or removed.
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
						if(typeof(this.add)=="function"){
							this.add(cell);
							var node=this.add(cell);
							if(typeof(node)!="undefined"&&node==false){
								return false;
							}
						}
						this.addChild(graph, cell,node);
					});
				}
		
				menu.addItem('编辑内容', graph.path+'/scripts/component/treeChar/images/text.gif', function(){
					graph.startEditingAtCell(cell);
				});
		
				if (cell.id != 'treeRoot' &&model.isVertex(cell)){
					menu.addItem('删除', graph.path+'/scripts/component/treeChar/images/delete.gif', function(){
						this.deleteSubtree(graph, cell);
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
	if(this.root){
		if(this.root.id){
			id=this.root.id;
		}
		if(this.root.value){
			value=this.root.value;
		}
		if(this.root.image){
			image=this.root.image;
		}
	}
	var v1 = graph.insertVertex(parent, id,value, w/2 - 30, 20, 140, 60, 'image='+image);
	graph.updateCellSize(v1);

	this.addOverlays(graph, v1, false);
	
	//工具栏

	var tb = new mxToolbar(content);

	tb.addItem('放大', this.path+'/scripts/component/treeChar/images/zoom_in32.png',function(evt)
	{
		graph.zoomIn();
	});

	tb.addItem('缩小', this.path+'/scripts/component/treeChar/images/zoom_out32.png',function(evt)
	{
		graph.zoomOut();
	});
	
	tb.addItem('默认', this.path+'/scripts/component/treeChar/images/view_1_132.png',function(evt)
	{
		graph.zoomActual();
	});

	wnd = new mxWindow('工具栏', content, 0, 0, 130, 66, false,null,null,null,toolBar);
	wnd.setMaximizable(false);
	wnd.setScrollable(false);
	wnd.setResizable(false);
	wnd.setVisible(true);
	
	graph.render=this.render;
	graph.path=this.path;
	graph.treeCharModel=this.treeCharModel;
	/*graph.root=this.root;*/
	graph.open=this.open;
	graph.add=this.add;
	graph.del=this.del;
	graph.addOverlays=this.addOverlays;
	graph.addChild=this.addChild;
	graph.deleteSubtree=this.deleteSubtree;
	return graph;
}
GraphEditor = {};
var graph;
var library;
function main()
{
    Ext.QuickTips.init();

	// Disables browser context menu
	mxEvent.disableContextMenu(document.body);	
	
	// Makes the connection are smaller
	mxConstants.DEFAULT_HOTSPOT = 0.3;
    
	// Creates the graph and loads the default stylesheet
    graph = new mxGraph();
    
    // Inverts the elbow edge style without removing existing styles
    graph.flipEdge = function(edge)
    {
		if (edge != null)
		{
			var state = this.view.getState(edge);
			var style = (state != null) ? state.style : this.getCellStyle(edge);
			
			if (style != null)
			{
				var elbow = mxUtils.getValue(style, mxConstants.STYLE_ELBOW,
					mxConstants.ELBOW_HORIZONTAL);
				var value = (elbow == mxConstants.ELBOW_HORIZONTAL) ?
					mxConstants.ELBOW_VERTICAL : mxConstants.ELBOW_HORIZONTAL;
				this.setCellStyles(mxConstants.STYLE_ELBOW, value, [edge]);
			}
		}
    };
    
    // Creates the command history (undo/redo)
    var history = new mxUndoManager();

    // Loads the default stylesheet into the graph
    var node = mxUtils.load('resources/default-style.xml').getDocumentElement();
		var dec = new mxCodec(node.ownerDocument);
		dec.decode(node, graph.getStylesheet());
	
	// Sets the style to be used when an elbow edge is double clicked
	graph.alternateEdgeStyle = 'vertical';
	
	// Creates the main containers
	var mainPanel = new MainPanel(graph, history);
	library = new LibraryPanel();
	library.title="图库";
	var store = new Ext.data.ArrayStore({
	    fields: ['name']
	});
    store.loadData([['test'], ['test2']]);
    
    var updateHandler = function()
    {
		var data = [];
		var names = DiagramStore.getNames();
		
		for (var i = 0; i < names.length; i++)
		{
			data.push([names[i]]);
		}
		
		store.loadData(data);
    };
    
    DiagramStore.addListener('put', updateHandler);
    DiagramStore.addListener('remove', updateHandler);
    updateHandler();
    
	var diagramPanel = new DiagramPanel(store, mainPanel);
	
	diagramPanel.on('dblclick', function(view, index, node, e)
	{
		var name = store.getAt(index).get('name');
		mainPanel.openDiagram(name);
	});
	
	var tabItems = (DiagramStore.isAvailable()) ? [library, diagramPanel] : [library];
	
    // Creates the container for the outline
	var tabPanel = new Ext.TabPanel(
	{
		id: 'tabPanel',
		region: 'center',
		activeTab: 0,
		width: 180,
		items: tabItems
    });

	
    // Creates the container for the outline
	var mainTabPanel = new Ext.TabPanel(
	{
		id: 'mainTabPanel',
		region: 'center',
		activeTab: 0,
		items: [mainPanel]
    });
	
    // Creates the container for the outline
	var outlinePanel = new Ext.Panel(
	{
		id: 'outlinePanel',
		layout: 'fit',
		split: true,
		height: 200,
        region:'south'
    });
    library.symbols.text="符号";
    library.templates.text="形状"
    library.images.text="图片"
	// Creates the enclosing viewport
    var viewport = new Ext.Viewport(
    {
    	layout:'border',
    	items:
        [{
	        xtype: 'panel',
	       	margins: '5 5 5 5',
	        region: 'center',
	        layout: 'border',
	        border: false,
        	items:
        	[
	            new Ext.Panel(
	            {
			        region: 'west',
			        layout: 'border',
			        split: true,
			        width: 180,
			        border: false,
			        items:
			        [
			         	tabPanel,
			        	outlinePanel
					]
		    	}),
		    	mainTabPanel
        	]
       	  } // end master panel
       	] // end viewport items
    }); // end of new Viewport

    // Enables scrollbars for the graph container to make it more
    // native looking, this will affect the panning to use the
    // scrollbars rather than moving the container contents inline
   	mainPanel.graphPanel.body.dom.style.overflow = 'auto';

    // Installs the command history after the initial graph
    // has been created
	var listener = function(sender, evt)
	{
		history.undoableEditHappened(evt.getProperty('edit'));
	};
	
	graph.getModel().addListener(mxEvent.UNDO, listener);
	graph.getView().addListener(mxEvent.UNDO, listener);

	// Keeps the selection in sync with the history
	var undoHandler = function(sender, evt)
	{
		var changes = evt.getProperty('edit').changes;
		graph.setSelectionCells(graph.getSelectionCellsForChanges(changes));
	};
	
	history.addListener(mxEvent.UNDO, undoHandler);
	history.addListener(mxEvent.REDO, undoHandler);

	// Initializes the graph as the DOM for the panel has now been created	
    graph.init(mainPanel.graphPanel.body.dom);
    graph.setConnectable(true);
	graph.setDropEnabled(true);
    graph.setPanning(true);
    graph.setTooltips(true);
    graph.connectionHandler.setCreateTarget(true);
    
    // Sets the cursor
    graph.container.style.cursor = 'default';

	// Creates rubberband selection
    var rubberband = new mxRubberband(graph);

	// Adds some example cells into the graph
	mainPanel.newDiagram();
	/***************************************************************************
    var parent = graph.getDefaultParent();
	graph.getModel().beginUpdate();
	try
	{
		var v1 = graph.insertVertex(parent, null, 'Hello,', 20, 20, 80, 40);
		var v2 = graph.insertVertex(parent, null, 'World!', 200, 150, 80, 40);
		var e1 = graph.insertEdge(parent, null, 'Hello, World!', v1, v2);
	}
	finally
	{
		// Updates the display
		graph.getModel().endUpdate();
	}
    ***************************************************************************/
	// Toolbar object for updating buttons in listeners
	var toolbarItems = mainPanel.graphPanel.getTopToolbar().items;
	
	// Hides the buttons which are only used if we have client-side storage
	if (!DiagramStore.isAvailable())
	{
		
		toolbarItems.get('saveButton').setVisible(false);
		toolbarItems.get('saveAsButton').setVisible(false);
	}
	
    // Updates the states of all buttons that require a selection
    var selectionListener = function()
    {
    	var selected = !graph.isSelectionEmpty();
    	
    	toolbarItems.get('cut').setDisabled(!selected);
    	toolbarItems.get('copy').setDisabled(!selected);
    	toolbarItems.get('delete').setDisabled(!selected);
    	toolbarItems.get('italic').setDisabled(!selected);
    	toolbarItems.get('bold').setDisabled(!selected);
    	toolbarItems.get('underline').setDisabled(!selected);
    	toolbarItems.get('fillcolor').setDisabled(!selected);
    	toolbarItems.get('fontcolor').setDisabled(!selected);
    	toolbarItems.get('linecolor').setDisabled(!selected);
    	toolbarItems.get('align').setDisabled(!selected);
    };
    
    graph.getSelectionModel().addListener(mxEvent.CHANGE, selectionListener);

    // Updates the states of the undo/redo buttons in the toolbar
    var historyListener = function()
    {
    	toolbarItems.get('undo').setDisabled(!history.canUndo());
    	toolbarItems.get('redo').setDisabled(!history.canRedo());
    };

	history.addListener(mxEvent.ADD, historyListener);
	history.addListener(mxEvent.UNDO, historyListener);
	history.addListener(mxEvent.REDO, historyListener);
	
	// Updates the button states once
	selectionListener();
	historyListener();
	
    // Installs outline in outlinePanel
	var outline = new mxOutline(graph, outlinePanel.body.dom);
	outlinePanel.body.dom.style.cursor = 'move';
	
    // Adds the entries into the library
    insertVertexTemplate(library, graph, '纵向容器', 'images/swimlane.gif', 'swimlane', 200, 200, '容器');//Container
	insertVertexTemplate(library, graph, '横向容器', 'images/swimlane_left.gif', 'swimlane2', 200, 200, '容器');//Container
    insertVertexTemplate(library, graph, '流程', 'images/rectangle.gif', null, 80, 60);
	insertVertexTemplate(library, graph, '预先定义的流程', 'images/preflow.gif', "shape=preflow", 80, 60);
	insertVertexTemplate(library, graph, '文字', 'images/text.gif', 'text', 70, 20, "文字");
//	insertVertexTemplate(library, graph, '图片', 'images/image.gif', 'image', 81, 47);
//  insertVertexTemplate(library, graph, '圆角矩形', 'images/rounded.gif', 'rounded=1', 100, 40);
    insertVertexTemplate(library, graph, '终结符', 'images/ellipse.gif', 'ellipse', 80, 60);
//    insertVertexTemplate(library, graph, '双椭圆', 'images/doubleellipse.gif', 'ellipse;shape=doubleEllipse', 60, 60);
    insertVertexTemplate(library, graph, '三角', 'images/triangle.gif', 'triangle', 80, 60);
    insertVertexTemplate(library, graph, '判定', 'images/rhombus.gif', 'rhombus', 80, 60);
//	insertVertexTemplate(library, graph, '水平线', 'images/hline.gif', 'line', 120, 10);
    insertVertexTemplate(library, graph, '准备', 'images/hexagon.gif', 'shape=hexagon', 80, 60);
	insertVertexTemplate(library, graph, '循环界限', 'images/circulation.gif', 'shape=circulation', 100, 50);
	insertVertexTemplate(library, graph, '卡', 'images/card.gif', 'shape=card', 100, 50);
	insertVertexTemplate(library, graph, '内部存储器', 'images/internalstorage.gif', 'shape=internalstorage', 100, 50);
	
//create shape by ryan 
	insertVertexTemplate(library, graph, '文档', 'images/ryan.gif', 'shape=ryan', 80, 60);
	insertVertexTemplate(library, graph, '数据', 'images/data.gif', 'shape=data', 80, 60);
	insertVertexTemplate(library, graph, '存储数据', 'images/storage.gif', 'shape=storage', 80, 60);
	insertVertexTemplate(library, graph, '直接数据', 'images/record.gif', 'shape=record', 80, 60);
	insertVertexTemplate(library, graph, '纸带', 'images/tape.gif', 'shape=tape', 80, 60);
	insertVertexTemplate(library, graph, '手动输入', 'images/handinput.gif', 'shape=handinput', 100, 50);
	insertVertexTemplate(library, graph, '手动操作', 'images/handoperator.gif', 'shape=handoperator', 80, 60);
	
//    insertVertexTemplate(library, graph, '数据库', 'images/cylinder.gif', 'shape=cylinder', 60, 80);
//    insertVertexTemplate(library, graph, '活动者', 'images/actor.gif', 'shape=actor', 40, 60);
//    insertVertexTemplate(library, graph, '云朵', 'images/cloud.gif', 'ellipse;shape=cloud', 80, 60);


    CustomImageLibrary();
    CustomSymbolLibrary();

	insertEdgeTemplate(library, graph, '直线连接器', 'images/straight.gif', 'straight', 100, 100);
	insertEdgeTemplate(library, graph, '水平连接器', 'images/connect.gif', null, 100, 100);
    insertEdgeTemplate(library, graph, '垂直连接器', 'images/vertical.gif', 'vertical', 100, 100);
    insertEdgeTemplate(library, graph, '实体关系', 'images/entity.gif', 'entity', 100, 100);
	insertEdgeTemplate(library, graph, '大箭头', 'images/arrow.gif', 'arrow', 100, 100);
    
    // Overrides createGroupCell to set the group style for new groups to 'group'
    var previousCreateGroupCell = graph.createGroupCell;
    
    graph.createGroupCell = function()
    {
    	var group = previousCreateGroupCell.apply(this, arguments);
    	group.setStyle('group');
    	
    	return group;
    };

    graph.connectionHandler.factoryMethod = function()
    {
		if (GraphEditor.edgeTemplate != null)
		{
    		return graph.cloneCells([GraphEditor.edgeTemplate])[0];
    	}
		
		return null;
    };

    // Uses the selected edge in the library as a template for new edges
    library.getSelectionModel().on('selectionchange', function(sm, node)
    {
    	if (node != null &&
    		node.attributes.cells != null)
    	{
    		var cell = node.attributes.cells[0];
    		
    		if (cell != null &&
    			graph.getModel().isEdge(cell))
    		{
    			GraphEditor.edgeTemplate = cell;
    		}
    	}
    });

    // Redirects tooltips to ExtJs tooltips. First a tooltip object
    // is created that will act as the tooltip for all cells.
  	var tooltip = new Ext.ToolTip(
	{
        target: graph.container,
        html: ''
    });
    
    // Disables the built-in event handling
    tooltip.disabled = true;
    
    // Installs the tooltip by overriding the hooks in mxGraph to
    // show and hide the tooltip.
    graph.tooltipHandler.show = function(tip, x, y)
    {
    	if (tip != null &&
    		tip.length > 0)
    	{
    		// Changes the DOM of the tooltip in-place if
    		// it has already been rendered
	    	if (tooltip.body != null)
	    	{
	    		// TODO: Use mxUtils.isNode(tip) and handle as markup,
	    		// problem is dom contains some other markup so the
	    		// innerHTML is not a good place to put the markup
	    		// and this method can also not be applied in
	    		// pre-rendered state (see below)
	    		//tooltip.body.dom.innerHTML = tip.replace(/\n/g, '<br>');
				tooltip.body.dom.firstChild.nodeValue = tip;
	    	}
	    	
	    	// Changes the html config value if the tooltip
	    	// has not yet been rendered, in which case it
	    	// has no DOM nodes associated
	    	else
	    	{
	    		tooltip.html = tip;
	    	}
	    	
	    	tooltip.showAt([x, y + mxConstants.TOOLTIP_VERTICAL_OFFSET]);
	    }
    };
    
    graph.tooltipHandler.hide = function()
    {
    	tooltip.hide();
    };

    // Updates the document title if the current root changes (drilling)
	var drillHandler = function(sender)
	{
		var model = graph.getModel();
		var cell = graph.getCurrentRoot();
		var title = '';
		
		while (cell != null &&
			  model.getParent(model.getParent(cell)) != null)
		{
			// Append each label of a valid root
			if (graph.isValidRoot(cell))
			{
				title = ' > ' +
				graph.convertValueToString(cell) + title;
			}
			
			cell = graph.getModel().getParent(cell);
		}
		
		document.title = 'Graph Editor' + title;
	};
		
	graph.getView().addListener(mxEvent.DOWN, drillHandler);
	graph.getView().addListener(mxEvent.UP, drillHandler);

	// Transfer initial focus to graph container for keystroke handling
	graph.container.focus();
	    
    // Handles keystroke events
    var keyHandler = new mxKeyHandler(graph);
    
    // Ignores enter keystroke. Remove this line if you want the
    // enter keystroke to stop editing
    keyHandler.enter = function() {};
    
    keyHandler.bindKey(8, function()
    {
    	graph.foldCells(true);
    });
    
    keyHandler.bindKey(13, function()
    {
    	graph.foldCells(false);
    });
    
    keyHandler.bindKey(33, function()
    {
    	graph.exitGroup();
    });
    
    keyHandler.bindKey(34, function()
    {
    	graph.enterGroup();
    });
    
    keyHandler.bindKey(36, function()
    {
    	graph.home();
    });

    keyHandler.bindKey(35, function()
    {
    	graph.refresh();
    });
    
    keyHandler.bindKey(37, function()
    {
    	graph.selectPreviousCell();
    });
        
    keyHandler.bindKey(38, function()
    {
    	graph.selectParentCell();
    });

    keyHandler.bindKey(39, function()
    {
    	graph.selectNextCell();
    });
    
    keyHandler.bindKey(40, function()
    {
    	graph.selectChildCell();
    });
    
    keyHandler.bindKey(46, function()
    {
    	graph.removeCells();
    });
    
    keyHandler.bindKey(107, function()
    {
    	graph.zoomIn();
    });
    
    keyHandler.bindKey(109, function()
    {
    	graph.zoomOut();
    });
    
    keyHandler.bindKey(113, function()
    {
    	graph.startEditingAtCell();
    });
  
    keyHandler.bindControlKey(65, function()
    {
    	graph.selectAll();
    });

    keyHandler.bindControlKey(89, function()
    {
    	history.redo();
    });
    
    keyHandler.bindControlKey(90, function()
    {
    	history.undo();
    });
    
    keyHandler.bindControlKey(88, function()
    {
    	mxClipboard.cut(graph);
    });
    
    keyHandler.bindControlKey(67, function()
    {
    	mxClipboard.copy(graph);
    });
    
    keyHandler.bindControlKey(86, function()
    {
    	mxClipboard.paste(graph);
    });
    
    keyHandler.bindControlKey(71, function()
    {
    	graph.setSelectionCell(graph.groupCells(null, 20));
    });
    
    keyHandler.bindControlKey(85, function()
    {
    	graph.setSelectionCells(graph.ungroupCells());
    });
    graph.setHtmlLabels(true);
    graph.refresh();
}; // end of main

function insertSymbolTemplate(panel, graph, name, icon, rhombus)
{
    var imagesNode = panel.symbols;
    var style = (rhombus) ? 'rhombusImage' : 'roundImage';
    return insertVertexTemplate(panel, graph, name, icon, style+';image='+icon, 50, 50, '', imagesNode);
};

function insertImageTemplate(panel, graph, name, icon, round)
{
    var imagesNode = panel.images;
    var style = (round) ? 'roundImage' : 'image';
    return insertVertexTemplate(panel, graph, name, icon, style+';image='+icon, 50, 50, name, imagesNode);
};

function insertVertexTemplate(panel, graph, name, icon, style, width, height, value, parentNode)
{
		var cells = [new mxCell((value != null) ? value : '', new mxGeometry(0, 0, width, height), style)];
		cells[0].vertex = true;
		
		var funct = function(graph, evt, target)
		{
			cells = graph.getImportableCells(cells);
			
			if (cells.length > 0)
			{
				var validDropTarget = (target != null) ?
					graph.isValidDropTarget(target, cells, evt) : false;
				var select = null;
				
				if (target != null &&
					!validDropTarget &&
					graph.getModel().getChildCount(target) == 0 &&
					graph.getModel().isVertex(target) == cells[0].vertex)
				{
					graph.getModel().setStyle(target, style);
					select = [target];
				}
				else
				{
					if (target != null &&
						!validDropTarget)
					{
						target = null;
					}
					
					var pt = graph.getPointForEvent(evt);
					
					// Splits the target edge or inserts into target group
					if (graph.isSplitEnabled() &&
						graph.isSplitTarget(target, cells, evt))
					{
						graph.splitEdge(target, cells, null, pt.x, pt.y);
						select = cells;
					}
					else
					{
						cells = graph.getImportableCells(cells);
						
						if (cells.length > 0)
						{
							select = graph.importCells(cells, pt.x, pt.y, target);
						}
					}
				}
				
				if (select != null &&
					select.length > 0)
				{
					graph.scrollCellToVisible(select[0]);
					graph.setSelectionCells(select);
				}
			}
		};
		
		// Small hack to install the drag listener on the node's DOM element
		// after it has been created. The DOM node does not exist if the parent
		// is not expanded.
		var node = panel.addTemplate(name, icon, parentNode, cells);
		var installDrag = function(expandedNode)
		{
			if (node.ui.elNode != null)
			{
				// Creates the element that is being shown while the drag is in progress
				var dragPreview = document.createElement('div');
				dragPreview.style.border = 'dashed black 1px';
				dragPreview.style.width = width+'px';
				dragPreview.style.height = height+'px';
				
				mxUtils.makeDraggable(node.ui.elNode, graph, funct, dragPreview, 0, 0,
						graph.autoscroll, true);
			}
		};
		
		if (!node.parentNode.isExpanded())
		{
			panel.on('expandnode', installDrag);
		}
		else
		{
			installDrag(node.parentNode);
		}
		
		return node;
};

function insertEdgeTemplate(panel, graph, name, icon, style, width, height, value, parentNode)
{
		var cells = [new mxCell((value != null) ? value : '', new mxGeometry(0, 0, width, height), style)];
		cells[0].geometry.setTerminalPoint(new mxPoint(0, height), true);
		cells[0].geometry.setTerminalPoint(new mxPoint(width, 0), false);
		cells[0].edge = true;
		
		var funct = function(graph, evt, target)
		{
			cells = graph.getImportableCells(cells);
			
			if (cells.length > 0)
			{
				var validDropTarget = (target != null) ?
					graph.isValidDropTarget(target, cells, evt) : false;
				var select = null;
				
				if (target != null &&
					!validDropTarget)
				{
					target = null;
				}
				
				var pt = graph.getPointForEvent(evt);
				var scale = graph.view.scale;
				
				pt.x -= graph.snap(width / 2);
				pt.y -= graph.snap(height / 2);
				
				select = graph.importCells(cells, pt.x, pt.y, target);
				
				// Uses this new cell as a template for all new edges
				GraphEditor.edgeTemplate = select[0];
				
				graph.scrollCellToVisible(select[0]);
				graph.setSelectionCells(select);
			}
		};
		
		// Small hack to install the drag listener on the node's DOM element
		// after it has been created. The DOM node does not exist if the parent
		// is not expanded.
		var node = panel.addTemplate(name, icon, parentNode, cells);
		var installDrag = function(expandedNode)
		{
			if (node.ui.elNode != null)
			{
				// Creates the element that is being shown while the drag is in progress
				var dragPreview = document.createElement('div');
				dragPreview.style.border = 'dashed black 1px';
				dragPreview.style.width = width+'px';
				dragPreview.style.height = height+'px';
				
				mxUtils.makeDraggable(node.ui.elNode, graph, funct, dragPreview, -width / 2, -height / 2,
						graph.autoscroll, true);
			}
		};
		
		if (!node.parentNode.isExpanded())
		{
			panel.on('expandnode', installDrag);
		}
		else
		{
			installDrag(node.parentNode);
		}
		
		return node;
};

// Defines a global functionality for displaying short information messages
Ext.example = function(){
    var msgCt;

    function createBox(t, s){
        return ['<div class="msg">',
                '<div class="x-box-tl"><div class="x-box-tr"><div class="x-box-tc"></div></div></div>',
                '<div class="x-box-ml"><div class="x-box-mr"><div class="x-box-mc"><h3>', t, '</h3>', s, '</div></div></div>',
                '<div class="x-box-bl"><div class="x-box-br"><div class="x-box-bc"></div></div></div>',
                '</div>'].join('');
    }
    return {
        msg : function(title, format){
            if(!msgCt){
                msgCt = Ext.DomHelper.append(document.body, {id:'msg-div'}, true);
            }
            msgCt.alignTo(document, 't-t');
            var s = String.format.apply(String, Array.prototype.slice.call(arguments, 1));
            var m = Ext.DomHelper.append(msgCt, {html:createBox(title, s)}, true);
            m.slideIn('t').pause(1).ghost("t", {remove:true});
        }
    };
}();

/*
 * $Id: MainPanel.js,v 1.83 2010-05-01 15:09:59 gaudenz Exp $
 * Copyright (c) 2006-2010, JGraph Ltd
 */
MainPanel = function(graph, history)
{
	var executeLayout = function(layout, animate, ignoreChildCount)
	{
		var cell = graph.getSelectionCell();
		
		if (cell == null ||
			(!ignoreChildCount &&
			graph.getModel().getChildCount(cell) == 0))
		{
			cell = graph.getDefaultParent();
		}

		graph.getModel().beginUpdate();
		try
		{
			layout.execute(cell);
		}
		catch (e)
		{
			throw e;
		}
		finally
		{
			// Animates the changes in the graph model except
			// for Camino, where animation is too slow
			if (animate && navigator.userAgent.indexOf('Camino') < 0)
			{
				// New API for animating graph layout results asynchronously
				var morph = new mxMorphing(graph);
				morph.addListener(mxEvent.DONE, function()
				{
					graph.getModel().endUpdate();
				});
				
				morph.startAnimation();
			}
			else
			{
				graph.getModel().endUpdate();
			}
		}
        
	};
	
	// 定义了各种不同颜色的彩色菜单
    var fillColorMenu = new Ext.menu.ColorMenu(
    {
    	items: [
    	{
    		text: 'None',
    		handler: function()
    		{
    			graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, mxConstants.NONE);
    		}
    	},
    	'-'
    	],
        handler : function(cm, color)
        {
    		if (typeof(color) == "string")
    		{
				graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, '#'+color);
			}
        }
    });

    var gradientColorMenu = new Ext.menu.ColorMenu(
    {
		items: [
        {
            text: 'North',
            handler: function()
            {
                graph.setCellStyles(mxConstants.STYLE_GRADIENT_DIRECTION, mxConstants.DIRECTION_NORTH);
            }
        },
        {
            text: 'East',
            handler: function()
            {
                graph.setCellStyles(mxConstants.STYLE_GRADIENT_DIRECTION, mxConstants.DIRECTION_EAST);
            }
        },
        {
            text: 'South',
            handler: function()
            {
                graph.setCellStyles(mxConstants.STYLE_GRADIENT_DIRECTION, mxConstants.DIRECTION_SOUTH);
            }
        },
        {
            text: 'West',
            handler: function()
            {
                graph.setCellStyles(mxConstants.STYLE_GRADIENT_DIRECTION, mxConstants.DIRECTION_WEST);
            }
        },
        '-',
		{
			text: 'None',
			handler: function()
			{
        		graph.setCellStyles(mxConstants.STYLE_GRADIENTCOLOR, mxConstants.NONE);
        	}
		},
		'-'
		],
        handler : function(cm, color)
        {
    		if (typeof(color) == "string")
    		{
    			graph.setCellStyles(mxConstants.STYLE_GRADIENTCOLOR, '#'+color);
			}
        }
    });

    var fontColorMenu = new Ext.menu.ColorMenu(
    {
    	items: [
    	{
    		text: 'None',
    		handler: function()
    		{
    			graph.setCellStyles(mxConstants.STYLE_FONTCOLOR, mxConstants.NONE);
    		}
    	},
    	'-'
    	],
        handler : function(cm, color)
        {
    		if (typeof(color) == "string")
    		{
    			graph.setCellStyles(mxConstants.STYLE_FONTCOLOR, '#'+color);
			}
        }
    });

    var lineColorMenu = new Ext.menu.ColorMenu(
    {
    	items: [
		{
			text: 'None',
			handler: function()
			{
				graph.setCellStyles(mxConstants.STYLE_STROKECOLOR, mxConstants.NONE);
			}
		},
		'-'
		],
        handler : function(cm, color)
        {
    		if (typeof(color) == "string")
    		{
				graph.setCellStyles(mxConstants.STYLE_STROKECOLOR, '#'+color);
			}
        }
    });

    var labelBackgroundMenu = new Ext.menu.ColorMenu(
    {
		items: [
		{
			text: 'None',
			handler: function()
			{
				graph.setCellStyles(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, mxConstants.NONE);
			}
		},
		'-'
		],
        handler : function(cm, color)
        {
    		if (typeof(color) == "string")
    		{
    			graph.setCellStyles(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, '#'+color);
    		}
        }
    });

    var labelBorderMenu = new Ext.menu.ColorMenu(
    {
		items: [
		{
			text: 'None',
			handler: function()
			{
				graph.setCellStyles(mxConstants.STYLE_LABEL_BORDERCOLOR, mxConstants.NONE);
			}
		},
		'-'
		],
        handler : function(cm, color)
        {
    		if (typeof(color) == "string")
    		{
    			graph.setCellStyles(mxConstants.STYLE_LABEL_BORDERCOLOR, '#'+color);
			}
        }
    });
    
    // 定义字体菜单
    var fonts = new Ext.data.SimpleStore(
    {
        fields: ['label', 'font'],
        data : [ ['Helvetica', 'Helvetica'], ['Verdana', 'Verdana'],
        	['Times New Roman', 'Times New Roman'], ['Garamond', 'Garamond'],
        	['Courier New', 'Courier New'],['宋体', '宋体'],['黑体', '黑体'],['隶书', '隶书'],['楷体', '楷体'],['方正舒体', '方正舒体'],

            ['华文中宋', '华文中宋'],['华文行楷', '华文行楷'],

            ['华文新魏', '华文新魏'],['华文细黑', '华文细黑'],

            ['华文宋体', '华文宋体'],['华文隶书', '华文隶书'],

            ['华文楷体', '华文楷体'],['华文琥珀', '华文琥珀'],

            ['华文仿宋', '华文仿宋'],['华文彩云', '华文彩云']]
    });
    
    var fontCombo = new Ext.form.ComboBox(
    {
        store: fonts,
        displayField:'label',
        mode: 'local',
        width:120,
        triggerAction: 'all',
        emptyText:'Select a font...',
        selectOnFocus:true,
        onSelect: function(entry)
        {
        	if (entry != null)
        	{
				graph.setCellStyles(mxConstants.STYLE_FONTFAMILY, entry.data.font);
				this.collapse();
        	}
        }
    });
    
	//处理输入字体名称后回车应用字体
    fontCombo.on('specialkey', function(field, evt)
    {
    	if (evt.keyCode == 10 ||
    		evt.keyCode == 13)
    	{
    		var family = field.getValue();
    		
    		if (family != null &&
    			family.length > 0)
    		{
    			graph.setCellStyles(mxConstants.STYLE_FONTFAMILY, family);
    		}
    	}
    });

    // 定义菜单的字体大小
    var sizes = new Ext.data.SimpleStore({
        fields: ['label', 'size'],
        data : [['6pt', 6], ['8pt', 8], ['9pt', 9], ['10pt', 10], ['12pt', 12],
        	['14pt', 14], ['18pt', 18], ['24pt', 24], ['30pt', 30], ['36pt', 36],
        	['48pt', 48],['60pt', 60]]
    });
    
    var sizeCombo = new Ext.form.ComboBox(
    {
        store: sizes,
        displayField:'label',
        mode: 'local',
        width:50,
        triggerAction: 'all',
        emptyText:'12pt',
        selectOnFocus:true,
        onSelect: function(entry)
        {
        	if (entry != null)
        	{
				graph.setCellStyles(mxConstants.STYLE_FONTSIZE, entry.data.size);
				this.collapse();
        	}
        }
    });
    
	// 处理输入字体大小后回车应用字体
    sizeCombo.on('specialkey', function(field, evt)
    {
    	if (evt.keyCode == 10 ||
    		evt.keyCode == 13)
    	{
    		var size = parseInt(field.getValue());
    		
    		if (!isNaN(size) &&
    			size > 0)
    		{
    			graph.setCellStyles(mxConstants.STYLE_FONTSIZE, size);
    		}
    	}
    });
    
    var sizeCombo = new Ext.form.ComboBox(
    {
        store: sizes,
        displayField:'label',
        mode: 'local',
        width:50,
        triggerAction: 'all',
        emptyText:'12pt',
        selectOnFocus:true,
        onSelect: function(entry)
        {
        	if (entry != null)
        	{
				graph.setCellStyles(mxConstants.STYLE_FONTSIZE, entry.data.size);
				this.collapse();
        	}
        }
    });
    
    // 简化处理文件和修改后的状态
    this.filename = null;
    this.modified = false;

	var updateTitle = mxUtils.bind(this, function()
	{
		this.setTitle((this.filename || '新绘图') + ((this.modified) ? ' *' : '') + ' ')
	});
    
	var changeHandler = mxUtils.bind(this, function(sender, evt)
	{
		this.modified = true;
		updateTitle();
	});
	
	graph.getModel().addListener(mxEvent.CHANGE, changeHandler);
    
    this.saveDiagram = function(forceDialog)
    {
    	var name = this.filename;
    	
    	if (name == null ||
    		forceDialog)
    	{
        	var defaultValue = this.filename;
        	
        	if (defaultValue == null)
        	{
        		defaultValue = "MyDiagram";
	        	var current = defaultValue;
	        	
	        	// Finds unused filename
	        	var i = 2;
	        	
	        	while (DiagramStore.get(current) != null)
	        	{
	        		current = defaultValue + i++;
	        	}
	        	
	        	defaultValue = current;
        	}
    		
    		do
    		{
	    		name = mxUtils.prompt('Enter filename', defaultValue);
	    		
	    		if (name != null)
	    		{
		    		if (name.length > 0)
		    		{
		    			if (name != this.filename && DiagramStore.get(name) != null)
		    			{
		    				alert('File exists, please choose a different name');
		    				defaultValue = name;
		    				name = '';
		    			}
		    		}
		    		else
		    		{
		    			alert('Please choose a name');
		    		}
	    		}
    		}
    		while (name != null && name.length == 0);
    	}
    	
    	if (name != null)
    	{
    		var enc = new mxCodec(mxUtils.createXmlDocument());
			var node = enc.encode(graph.getModel());
			var xml = mxUtils.getXml(node);
			DiagramStore.put(name, xml);
			this.filename = name;
			this.modified = false;
			updateTitle();
			mxUtils.alert('Saved "'+name+'": '+xml.length+' byte(s)');
    	}
    	else
    	{
    		mxUtils.alert('Not saved');
    	}
    };
    
    this.openDiagram = function(name)
    {
    	if (!this.modified ||
    		mxUtils.confirm('建立新图将丢失当前绘图中的更改，您确认建立新图吗？'))
   		{
			var xml = DiagramStore.get(name);
			
			if (xml != null && xml.length > 0)
			{
				var doc = mxUtils.parseXml(xml); 
				var dec = new mxCodec(doc); 
				dec.decode(doc.documentElement, graph.getModel());
				history.clear();
				this.filename = name;
				this.modified = false;
				updateTitle();
				mxUtils.alert('Opened "'+name+'": '+xml.length+' byte(s)');
			}
   		}
    };

    //开始绘图面板
	this.graphPanel = new Ext.Panel(
    {
    	region: 'center',
    	border:false,
/*        tbar:[     
        {
        	id: 'print',
            text:'',
            iconCls: 'print-icon',
            tooltip: '打印预览',
            handler: function()
            {
        		var preview = new mxPrintPreview(graph);
        		preview.autoOrigin = false;
        		preview.open();
            },
            scope:this
        },
        {
        	id: 'posterprint',
            text:'',
            iconCls: 'press-icon',
            tooltip: '海报式打印预览',
            handler: function()
            {
	        	try
	        	{
	        		var pageCount = mxUtils.prompt('请输入要打印的最大页数:', '1');
					
					if (pageCount != null)
					{
						var scale = mxUtils.getScaleForPageCount(pageCount, graph);
						var preview = new mxPrintPreview(graph, scale);
						preview.open();
					}
	        	}
	        	catch (e)
	        	{
	        		// ignore
	        	}
            },
            scope:this
        },
        {
        	id: 'view',
            text:'',
            iconCls: 'preview-icon',
            tooltip: '预览绘图',
            handler: function()
            {
        		mxUtils.show(graph, null, 10, 10);
            },
            scope:this
        }],
		*/
        bbar:[
        {
            text:'缩放',
            iconCls: 'zoom-icon',
            handler: function(menu) { },
            menu:
            {
                items: [
                {
		            text:'自定义',
		            scope:this,
		            handler: function(item)
		            {
		            	var value = mxUtils.prompt('请输入图形间距（单位像素）', parseInt(graph.getView().getScale() * 100));
										            	
		            	if (value != null)
		            	{
			            	graph.getView().setScale(parseInt(value) / 100);
			            }
		            }
		        },
		        '-',
                {
		            text:'400%',
		            scope:this,
		            handler: function(item)
		            {
						graph.getView().setScale(4);
		            }
		        },
                {
		            text:'200%',
		            scope:this,
		            handler: function(item)
		            {
						graph.getView().setScale(2);
		            }
		        },
                {
		            text:'150%',
		            scope:this,
		            handler: function(item)
		            {
						graph.getView().setScale(1.5);
		            }
		        },
		        {
		            text:'100%',
		            scope:this,
		            handler: function(item)
		            {
		                graph.getView().setScale(1);
		            }
		        },
                {
		            text:'75%',
		            scope:this,
		            handler: function(item)
		            {
						graph.getView().setScale(0.75);
		            }
		        },
                {
		            text:'50%',
		            scope:this,
		            handler: function(item)
		            {
						graph.getView().setScale(0.5);
		            }
		        },
                {
		            text:'25%',
		            scope:this,
		            handler: function(item)
		            {
						graph.getView().setScale(0.25);
		            }
		        },
                '-',
                {
		            text:'放大',
		            iconCls: 'zoomin-icon',
		            scope:this,
		            handler: function(item)
		            {
						graph.zoomIn();
		            }
		        },
		        {
		            text:'缩小',
		            iconCls: 'zoomout-icon',
		            scope:this,
		            handler: function(item)
		            {
		                graph.zoomOut();
		            }
		        },
		        '-',
		        {
		            text:'实际尺寸',
		            iconCls: 'zoomactual-icon',
		            scope:this,
		            handler: function(item)
		            {
		                graph.zoomActual();
		            }
		        },
		        {
		            text:'适合窗口',
		            iconCls: 'fit-icon',
		            scope:this,
		            handler: function(item)
		            {
		                graph.fit();
		            }
		        }]
            }
        },
        '-',
        {
            text:'选项',
            iconCls: 'preferences-icon',
            handler: function(menu) { },
            menu:
            {
                items: [
/*                {
                    text:'网格',
                    handler: function(menu) { },
                    menu:
                    {
                    	items: [
        		        {
        		            text:'网格大小',
        		            scope:this,
        		            handler: function()
        		            {
        						var value = mxUtils.prompt('请输入网格大小 （单位像素）', graph.gridSize);
        										            	
        		            	if (value != null)
        		            	{
        			            	graph.gridSize = value;
        			            }
        		            }
        		        },
        		        {
        		            checked: true,
        		            text:'可用网格',
        		            scope:this,
        		            checkHandler: function(item, checked)
        		            {
        		                graph.setGridEnabled(checked);
        		            }
        		        }
        		        ]
                    }
                },
                {
	                text:'样式',
	                handler: function(menu) { },
	                menu:
	                {
	                	items: [
	                	{
				            text:'基本样式',
				            scope:this,
				            handler: function(item)
				            {
							    var node = mxUtils.load('resources/basic-style.xml').getDocumentElement();
								var dec = new mxCodec(node.ownerDocument);
								dec.decode(node, graph.getStylesheet());    
								graph.refresh();
				            }
				        },
				        {
				            text:'默认样式',
				            scope:this,
				            handler: function(item)
				            {
							    var node = mxUtils.load('resources/default-style.xml').getDocumentElement();
								var dec = new mxCodec(node.ownerDocument);
								dec.decode(node, graph.getStylesheet());    
								graph.refresh();
				            }
				        }]
	                }
                },
                {
                    text:'标签',
                    handler: function(menu) { },
                    menu:
                    {
                    	items: [
        		        {
        		            checked: true,
        		            text:'显示标签',
        		            scope:this,
        		            checkHandler: function(item, checked)
        		            {
        		            	graph.labelsVisible = checked;
        		            	graph.refresh();
        		            }
        		        },
        		        {
        		            checked: true,
        		            text:'移动连线标签',
        		            scope:this,
        		            checkHandler: function(item, checked)
        		            {
        		            	graph.edgeLabelsMovable = checked;
        		            }
        		        },
        		        {
        		            checked: false,
        		            text:'移动形状标签',
        		            scope:this,
        		            checkHandler: function(item, checked)
        		            {
        		           		graph.vertexLabelsMovable = checked;
        		            }
        		        },
        		        '-',
        		        {
        		            checked: true,
        		            text:'HTML标签',
        		            scope:this,
        		            checkHandler: function(item, checked)
        		            {
        		           		graph.setHtmlLabels(checked);
        		           		graph.refresh();
        		            }
        		        }
            	        ]
                    }
                },
                '-',
                {
                    text:'连接线',
                    handler: function(menu) { },
                    menu:
                    {
                    	items: [
                        {
        		            checked: true,
        		            text:'可连接',
        		            scope:this,
        		            checkHandler: function(item, checked)
        		            {
        		                graph.setConnectable(checked);
        		            }
        		        },
        		        {
        		            checked: false,
        		            text:'连线可连接',
        		            scope:this,
        		            checkHandler: function(item, checked)
        		            {
        		                graph.setConnectableEdges(checked);
        		            }
        		        },
        		        '-',
                        {
        		            checked: true,
        		            text:'创建目标',
        		            scope:this,
        		            checkHandler: function(item, checked)
        		            {
        		                graph.connectionHandler.setCreateTarget(checked);
        		            }
        		        },
        		        {
        		            checked: true,
        		            text:'断开移动',
        		            scope:this,
        		            checkHandler: function(item, checked)
        		            {
        		                graph.setDisconnectOnMove(checked);
        		            }
        		        },
        		        '-',
        		        {
        		        	checked: false,
        		        	text:'添加/删除弯头',
        		        	scope:this,
        		        	checkHandler: function(item, checked)
        		        	{
        		        		mxEdgeHandler.prototype.addEnabled = checked;
        		        		mxEdgeHandler.prototype.removeEnabled = checked;
        		        	}
        		        }
            	        ]
                    }
                },
                {
                    text:'验证',
                    handler: function(menu) { },
                    menu:
                    {
                    	items: [
        		        {
        		            checked: true,
        		            text:'允许连接线端独立',
        		            scope:this,
        		            checkHandler: function(item, checked)
        		            {
        		                graph.setAllowDanglingEdges(checked);
        		            }
        		        },
        		        {
        		            checked: false,
        		            text:'克隆无效的边缘',
        		            scope:this,
        		            checkHandler: function(item, checked)
        		            {
        		                graph.setCloneInvalidEdges(checked);
        		            }
        		        },
        		        '-',
        		        {
        		            checked: false,
        		            text:'允许循环连接',
        		            scope:this,
        		            checkHandler: function(item, checked)
        		            {
        		                graph.setAllowLoops(checked);
        		            }
        		        },
        		        {
        		            checked: true,
        		            text:'多绘图',
        		            scope:this,
        		            checkHandler: function(item, checked)
        		            {
        		                graph.setMultigraph(checked);
        		            }
        		        }
            	        ]
                    }
                },
                '-',
		        {
		            checked: false,
		            text:'页面布局',
		            scope:this,
		            checkHandler: function(item, checked)
		            {
						graph.pageVisible = checked;
						graph.preferPageSize = graph.pageBreaksVisible;
						graph.view.validate();
						graph.sizeDidChange();
		            }
		        },
		        {
		            checked: false,
		            text:'分页符',
		            scope:this,
		            checkHandler: function(item, checked)
		            {
						graph.pageBreaksVisible = checked;
						graph.preferPageSize = graph.pageBreaksVisible;
						graph.sizeDidChange();
		            }
		        },
                '-',
		        {
		            checked: true,
		            text:'严格滚动区域',
		            scope:this,
		            checkHandler: function(item, checked)
		            {
						graph.useScrollbarsForPanning = checked;
		            }
		        },
		        {
		            checked: true,
		            text:'允许负坐标',
		            scope:this,
		            checkHandler: function(item, checked)
		            {
						graph.setAllowNegativeCoordinates(checked);
		            }
		        },
                '-',*/
		        {
		            text:'显示XML代码',
		            scope:this,
		            handler: function(item)
		            {
						var enc = new mxCodec(mxUtils.createXmlDocument());
						var node = enc.encode(graph.getModel());
						
						mxUtils.popup(mxUtils.getPrettyXml(node));
		            }
		        },
		        {
		            text:'解析XML代码',
		            scope:this,
		            handler: function(item)
		            {
		        		var xml = mxUtils.prompt('请输入要解析的XML代码:', '');
		        		
		        		if (xml != null && xml.length > 0)
		        		{
		        			var doc = mxUtils.parseXml(xml); 
		        			var dec = new mxCodec(doc); 
		        			dec.decode(doc.documentElement, graph.getModel()); 
		        		}
		            }
		        },
		        {
		            text:'上传XML代码',
		            scope:this,
		            handler: function(item)
		            {
		                //得到XML文档
		                var enc = new mxCodec(mxUtils.createXmlDocument());
						var node = enc.encode(graph.getModel());
						//得到HTML内容

						 var html = '<html>';

                        html += '<head>';

                        var base = document.getElementsByTagName('base');

                        for (var i = 0; i < base.length; i++) {

                            html += base[i].outerHTML;

                        }

                        html += '<style>';

                        for (var i = 0; i < document.styleSheets.length; i++) {

                            try {

                                html += document.styleSheets(i).cssText;

                            } 

                            catch (e) {

                            }

                        }

                        html += '</style>';

                        html += '</head>';

                        html += '<body>';

                        html += graph.container.innerHTML;

                        html += '</body>';

                        html += '<html>';
						
		        		UploadXmlHtmlDocument(node.xml,html);
		            }
		        }/*,
		        '-',
		        {
		            text:'控制台',
		            scope:this,
		            handler: function(item)
		            {
		            	mxLog.setVisible(!mxLog.isVisible());
		            }
		        }*/]
            }
        }],

        onContextMenu : function(node, e)
        {
    		var selected = !graph.isSelectionEmpty();

    		this.menu = new Ext.menu.Menu(
    		{
                items: [
/*				{
                    text:'撤销',
                    iconCls:'undo-icon',
                    disabled: !history.canUndo(),
                    scope: this,
                    handler:function()
                    {
                        history.undo();
                    }
                },'-',{
                    text:'剪切',
                    iconCls:'cut-icon',
                    disabled: !selected,
                    scope: this,
                    handler:function()
                    {
                    	mxClipboard.cut(graph);
                    }
                },{
                    text:'复制',
                    iconCls:'copy-icon',
                    disabled: !selected,
                    scope: this,
                    handler:function()
                    {
                    	mxClipboard.copy(graph);
                    }
                },{
                    text:'粘贴',
                    iconCls:'paste-icon',
                    disabled: mxClipboard.isEmpty(),
                    scope: this,
                    handler:function()
                    {
                    	mxClipboard.paste(graph);
                    }
                },
                '-',
                {
                    text:'删除',
                    iconCls:'delete-icon',
                    disabled: !selected,
                    scope: this,
                    handler:function()
                    {
                    	graph.removeCells();
                    }
                },
              	'-',

              	{
		            text:'格式',
		            disabled: !selected,
		            handler: function() { },
		            menu:
		            {
		            	items: [
		            	{
		            		text:'背景',
				            disabled: !selected,
				            handler: function() { },
				            menu:
				            {
				            	items: [
				                {
						            text: '填充色',
						            iconCls:'fillcolor-icon',
						            menu: fillColorMenu
						        },
				                {
						            text: '渐变色',
						            menu: gradientColorMenu
						        },
						        '-',
						        {
						            text: '图片',
						            handler: function()
						            {
						            	var value = '';
						            	var state = graph.getView().getState(graph.getSelectionCell());
						            	
						            	if (state != null)
						            	{
						            		value = state.style[mxConstants.STYLE_IMAGE] || value;
						            	}

					            		value = mxUtils.prompt('请输入图片的URL地址:', value);
						            	
						            	if (value != null)
						            	{
							            	graph.setCellStyles(mxConstants.STYLE_IMAGE, value);
							            }
						            }
						        },
						        {
						            text:'阴影',
						            scope:this,
						            handler: function()
						            {
						                graph.toggleCellStyles(mxConstants.STYLE_SHADOW);
						            }
						        },
						        '-',
						        {
						            text:'透明度',
						            scope:this,
						            handler: function()
						            {
						            	var value = mxUtils.prompt('请输入透明度 (0-100%)', '100');
						            	
						            	if (value != null)
						            	{
							            	graph.setCellStyles(mxConstants.STYLE_OPACITY, value);
							            }
						            }
						        }]
				            }
		            	},
			            {
		            		text:'标签',
				            disabled: !selected,
				            handler: function() { },
				            menu:
				            {
				            	items: [
								{
						            text: '前景色',
						            iconCls:'fontcolor-icon',
						            menu: fontColorMenu
						        },
						        '-',
				                {
						            text: '标签填充色',
						            menu: labelBackgroundMenu
						        },
				                {
						            text: '标签边框颜色',
						            menu: labelBorderMenu
						        },
						        '-',
								{
						            text:'旋转标签',
						            scope:this,
						            handler: function()
						            {
						                graph.toggleCellStyles(mxConstants.STYLE_HORIZONTAL, true);
						            }
						        },
						        {
						            text:'文本透明度',
						            scope:this,
						            handler: function()
						            {
						            	var value = mxUtils.prompt('请输入文本透明度 (0-100%)', '100');
						            	
						            	if (value != null)
						            	{
							            	graph.setCellStyles(mxConstants.STYLE_TEXT_OPACITY, value);
							            }
						            }
						        },
						        '-',
					            {
				            		text:'位置',
						            disabled: !selected,
						            handler: function() { },
						            menu:
						            {
						            	items: [
					            		{
								            text: '顶端',
								            scope:this,
								            handler: function()
								            {
					            				graph.setCellStyles(mxConstants.STYLE_VERTICAL_LABEL_POSITION, mxConstants.ALIGN_TOP);
					            				graph.setCellStyles(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_BOTTOM);
								            }
								        },
					            		{
								            text: '垂直居中',
								            scope:this,
								            handler: function()
								            {
					            				graph.setCellStyles(mxConstants.STYLE_VERTICAL_LABEL_POSITION, mxConstants.ALIGN_MIDDLE);
					            				graph.setCellStyles(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
								            }
								        },
					            		{
								            text: '底端',
								            scope:this,
								            handler: function()
								            {
					            				graph.setCellStyles(mxConstants.STYLE_VERTICAL_LABEL_POSITION, mxConstants.ALIGN_BOTTOM);
					            				graph.setCellStyles(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_TOP);
								            }
								        },
								        '-',
					            		{
								            text: '左对齐',
								            scope:this,
								            handler: function()
								            {
					            				graph.setCellStyles(mxConstants.STYLE_LABEL_POSITION, mxConstants.ALIGN_LEFT);
					            				graph.setCellStyles(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_RIGHT);
								            }
								        },
					            		{
								            text: '居中',
								            scope:this,
								            handler: function()
								            {
				            				graph.setCellStyles(mxConstants.STYLE_LABEL_POSITION, mxConstants.ALIGN_CENTER);
				            				graph.setCellStyles(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
								            }
								        },
					            		{
								            text: '右对齐',
								            scope:this,
								            handler: function()
								            {
				            				graph.setCellStyles(mxConstants.STYLE_LABEL_POSITION, mxConstants.ALIGN_RIGHT);
				            				graph.setCellStyles(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_LEFT);
								            }
								        }]
						            }
					            },
						        '-',
								{
						            text:'隐藏',
						            scope:this,
						            handler: function()
						            {
						                graph.toggleCellStyles(mxConstants.STYLE_NOLABEL, false);
						            }
						        }]
				            }
			            },
			            '-',
			            {
		            		text:'线条',
				            disabled: !selected,
				            handler: function() { },
				            menu:
				            {
				            	items: [
			            		{
						            text: '线条颜色',
						            iconCls:'linecolor-icon',
						            menu: lineColorMenu
						        },
						        '-',
						        {
						            text:'虚线',
						            scope:this,
						            handler: function()
						            {
						                graph.toggleCellStyles(mxConstants.STYLE_DASHED);
						            }
						        },
								{
						            text: '线条宽度',
						            handler: function()
						            {
						            	var value = '0';
						            	var state = graph.getView().getState(graph.getSelectionCell());
						            	
						            	if (state != null)
						            	{
						            		value = state.style[mxConstants.STYLE_STROKEWIDTH] || 1;
						            	}
	
					            		value = mxUtils.prompt('Enter Linewidth (Pixels)', value);
							            	
						            	if (value != null)
						            	{
							            	graph.setCellStyles(mxConstants.STYLE_STROKEWIDTH, value);
							            }
						            }
						        }]
				            }
			            },
		            	{
		            		text:'连接线',
		            		menu:
		            		{
		            			items: [
		            			{
						            text: '直线连接',
						            icon: 'images/straight.gif',
						            handler: function()
						            {
						            	graph.setCellStyle('straight');
						            }
						        },
						        '-',
						        {
						            text: '水平连接',
						            icon: 'images/connect.gif',
						            handler: function()
						            {
						            	graph.setCellStyle(null);
						            }
						        },
						        {
						            text: '垂直连接',
						            icon: 'images/vertical.gif',
						            handler: function()
						            {
						            	graph.setCellStyle('vertical');
						            }
						        },
						        '-',
						        {
						            text: '实体关系',
						           	icon: 'images/entity.gif',
						            handler: function()
						            {
						            	graph.setCellStyle('edgeStyle=entityRelationEdgeStyle');
						            }
						        },
						        {
						            text: '箭头',
						            icon: 'images/arrow.gif',
						            handler: function()
						            {
						            	graph.setCellStyle('arrow');
						            }
						        },
						        '-',
						        {
						            text: '平面',
						            handler: function()
						            {
						        		graph.toggleCellStyles(mxConstants.STYLE_NOEDGESTYLE, false);
						            }
						        }]
		            		}
		            	},
				        '-',
		            	{
							text:'线条开始',
							menu:
							{
		            			items: [
		            			{
		            				text: '打开',
						            icon: 'images/open_start.gif',
						            handler: function()
						            {
						            	graph.setCellStyles(mxConstants.STYLE_STARTARROW, mxConstants.ARROW_OPEN);
						            }
						        },
						        {
						            text: '经典',
						            icon: 'images/classic_start.gif',
						            handler: function()
						            {
						            	graph.setCellStyles(mxConstants.STYLE_STARTARROW, mxConstants.ARROW_CLASSIC);
						            }
						        },
						        {
						            text: '盒状',
						            icon: 'images/block_start.gif',
						            handler: function()
						            {
						            	graph.setCellStyles(mxConstants.STYLE_STARTARROW, mxConstants.ARROW_BLOCK);
						            }
						        },
						        '-',
						        {
						            text: '方形',
						            icon: 'images/diamond_start.gif',
						            handler: function()
						            {
						            	graph.setCellStyles(mxConstants.STYLE_STARTARROW, mxConstants.ARROW_DIAMOND);
						            }
						        },
						        {
						            text: '椭圆',
						            icon: 'images/oval_start.gif',
						            handler: function()
						            {
						            	graph.setCellStyles(mxConstants.STYLE_STARTARROW, mxConstants.ARROW_OVAL);
						            }
						        },
						        '-',
				                {
						            text: '无',
						            handler: function()
						            {
						            	graph.setCellStyles(mxConstants.STYLE_STARTARROW, mxConstants.NONE);
						            }
						        },
				                {
						            text: '大小',
						            handler: function()
						            {
						            	var size = mxUtils.prompt('请输入大小', mxConstants.DEFAULT_MARKERSIZE);
						            	
						            	if (size != null)
						            	{
							            	graph.setCellStyles(mxConstants.STYLE_STARTSIZE, size);
							            }
						            }
				                }]
							}
						},
						{
							text:'线条结束',
							menu:
							{
								items: [
								{
						            text: '打开',
						            icon: 'images/open_end.gif',
						            handler: function()
						            {
						            	graph.setCellStyles(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_OPEN);
						            }
						        },
						        {
						            text: '经典',
						            icon: 'images/classic_end.gif',
						            handler: function()
						            {
						            	graph.setCellStyles(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
						            }
						        },
						        {
						            text: '盒状',
						            icon: 'images/block_end.gif',
						            handler: function()
						            {
						            	graph.setCellStyles(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_BLOCK);
						            }
						        },
						        '-',
						        {
						            text: '方形',
						            icon: 'images/diamond_end.gif',
						            handler: function()
						            {
						            	graph.setCellStyles(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_DIAMOND);
						            }
						        },
						        {
						            text: '椭圆',
						            icon: 'images/oval_end.gif',
						            handler: function()
						            {
						            	graph.setCellStyles(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_OVAL);
						            }
						        },
						        '-',
				                {
						            text: '无',
						            handler: function()
						            {
						            	graph.setCellStyles(mxConstants.STYLE_ENDARROW, 'none');
						            }
				                },
				                {
				                	text: '大小',
				                	handler: function()
						            {
						            	var size = mxUtils.prompt('请输入大小', mxConstants.DEFAULT_MARKERSIZE);
						            	
						            	if (size != null)
						            	{
							            	graph.setCellStyles(mxConstants.STYLE_ENDSIZE, size);
							            }
						            }
						        }]
							}
						},
						'-',
						{
							text:'间距',
							menu:
							{
				                items: [
							    {
						            text: '顶端',
						            handler: function()
						            {
						            	var value = '0';
						            	var state = graph.getView().getState(graph.getSelectionCell());
						            	
						            	if (state != null)
						            	{
						            		value = state.style[mxConstants.STYLE_SPACING_TOP] || value;
						            	}

					            		value = mxUtils.prompt('请输入顶端间距 （单位像素）', value);
							            	
						            	if (value != null)
						            	{
							            	graph.setCellStyles(mxConstants.STYLE_SPACING_TOP, value);
							            }
						            }
						        },
						        {
						            text: '右端',
						            handler: function()
						            {
						            	var value = '0';
						            	var state = graph.getView().getState(graph.getSelectionCell());
						            	
						            	if (state != null)
						            	{
						            		value = state.style[mxConstants.STYLE_SPACING_RIGHT] || value;
						            	}

					            		value = mxUtils.prompt('请输入右端间距 （单位像素）', value);
							            	
						            	if (value != null)
						            	{
							            	graph.setCellStyles(mxConstants.STYLE_SPACING_RIGHT, value);
							            }
						            }
						        },
						        {
						            text: '底端',
						            handler: function()
						            {
						            	var value = '0';
						            	var state = graph.getView().getState(graph.getSelectionCell());
						            	
						            	if (state != null)
						            	{
						            		value = state.style[mxConstants.STYLE_SPACING_BOTTOM] || value;
						            	}

					            		value = mxUtils.prompt('请输入底端间距 （单位像素）', value);
							            	
						            	if (value != null)
						            	{
							            	graph.setCellStyles(mxConstants.STYLE_SPACING_BOTTOM, value);
							            }
						            }
						        },
						        {
						            text: '左端',
						            handler: function()
						            {
						            	var value = '0';
						            	var state = graph.getView().getState(graph.getSelectionCell());
						            	
						            	if (state != null)
						            	{
						            		value = state.style[mxConstants.STYLE_SPACING_LEFT] || value;
						            	}

					            		value = mxUtils.prompt('请输入左端间距 （单位像素）', value);
							            	
						            	if (value != null)
						            	{
							            	graph.setCellStyles(mxConstants.STYLE_SPACING_LEFT, value);
							            }
						            }
						        },
						        '-',
				                {
						            text: '全局',
						            handler: function()
						            {
						            	var value = '0';
						            	var state = graph.getView().getState(graph.getSelectionCell());
						            	
						            	if (state != null)
						            	{
						            		value = state.style[mxConstants.STYLE_SPACING] || value;
						            	}

					            		value = mxUtils.prompt('请输入间距 （单位像素）', value);
							            	
						            	if (value != null)
						            	{
							            	graph.setCellStyles(mxConstants.STYLE_SPACING, value);
							            }
						            }
				                },
				                '-',
						        {
						            text: '资源间距',
						            handler: function()
						            {
						            	var value = '0';
						            	var state = graph.getView().getState(graph.getSelectionCell());
						            	
						            	if (state != null)
						            	{
						            		value = state.style[mxConstants.STYLE_SOURCE_PERIMETER_SPACING] || value;
						            	}
	
					            		value = mxUtils.prompt('请输入资源间距 （单位像素）', value);
							            	
						            	if (value != null)
						            	{
							            	graph.setCellStyles(mxConstants.STYLE_SOURCE_PERIMETER_SPACING, value);
							            }
						            }
						        },
								{
						            text: '目标间距',
						            handler: function()
						            {
						            	var value = '0';
						            	var state = graph.getView().getState(graph.getSelectionCell());
						            	
						            	if (state != null)
						            	{
						            		value = state.style[mxConstants.STYLE_TARGET_PERIMETER_SPACING] || value;
						            	}
	
					            		value = mxUtils.prompt('请输入目标间距 （单位像素）', value);
							            	
						            	if (value != null)
						            	{
							            	graph.setCellStyles(mxConstants.STYLE_TARGET_PERIMETER_SPACING, value);
							            }
						            }
						        },
						        '-',
						        {
						            text: '周边',
						            handler: function()
						            {
						            	var value = '0';
						            	var state = graph.getView().getState(graph.getSelectionCell());
						            	
						            	if (state != null)
						            	{
						            		value = state.style[mxConstants.STYLE_PERIMETER_SPACING] || value;
						            	}

					            		value = mxUtils.prompt('请输入周长间距 （单位像素）', value);
							            	
						            	if (value != null)
						            	{
							            	graph.setCellStyles(mxConstants.STYLE_PERIMETER_SPACING, value);
							            }
						            }
						        }]
							}
						},
						{
							text:'方向',
							menu:
							{
				                items: [
				                {
						            text: '北向',
						            scope:this,
						            handler: function()
						            {
						                graph.setCellStyles(mxConstants.STYLE_DIRECTION, mxConstants.DIRECTION_NORTH);
						            }
						        },
						        {
						            text: '东向',
						            scope:this,
						            handler: function()
						            {
						                graph.setCellStyles(mxConstants.STYLE_DIRECTION, mxConstants.DIRECTION_EAST);
						            }
						        },
						        {
						            text: '南向',
						            scope:this,
						            handler: function()
						            {
						                graph.setCellStyles(mxConstants.STYLE_DIRECTION, mxConstants.DIRECTION_SOUTH);
						            }
						        },
						        {
						            text: '西向',
						            scope:this,
						            handler: function()
						            {
						                graph.setCellStyles(mxConstants.STYLE_DIRECTION, mxConstants.DIRECTION_WEST);
						            }
						        },
						        '-',
						        {
						            text:'旋转',
						            scope:this,
						            handler: function()
						            {
						            	var value = '0';
						            	var state = graph.getView().getState(graph.getSelectionCell());
						            	
						            	if (state != null)
						            	{
						            		value = state.style[mxConstants.STYLE_ROTATION] || value;
						            	}

					            		value = mxUtils.prompt('输入旋转角度 (0-360)', value);
							            	
						            	if (value != null)
						            	{
							            	graph.setCellStyles(mxConstants.STYLE_ROTATION, value);
							            }
						            }
						        }]
							}
						},
				        '-',
				        {
				            text:'圆角',
				            scope:this,
				            handler: function()
				            {
				               graph.toggleCellStyles(mxConstants.STYLE_ROUNDED);
				            }
				        },
				       	{
				            text:'样式',
				            scope:this,
				            handler: function()
				            {
				        		var cells = graph.getSelectionCells();

								if (cells != null &&
									cells.length > 0)
								{
									var model = graph.getModel();
									var style = mxUtils.prompt('请输入样式', model.getStyle(cells[0]) || '');
				
									if (style != null)
									{
										graph.setCellStyle(style, cells);
									}
								}
				            }
				        }]
		            }
              	},
              	{
              		split:true,
		            text:'形状',
		            handler: function() { },
		            menu:
		            {
		                items: [
		                {
		                    text:'主视图',
		                    iconCls: 'home-icon',
		                    disabled: graph.view.currentRoot == null,
		                    scope: this,
		                    handler:function()
		                    {
		                    	graph.home();
		                    }
		              	},
		              	'-',
		                {
		                    text:'退出组合',
		                    iconCls:'up-icon',
		                    disabled: graph.view.currentRoot == null,
		                    scope: this,
		                    handler:function()
		                    {
		                    	graph.exitGroup();
		                    }
		              	},
		                {
		                    text:'进入组合',
		                    iconCls:'down-icon',
		                    disabled: !selected,
		                    scope: this,
		                    handler:function()
		                    {
		                    	graph.enterGroup();
		                    }
		              	},
				        '-',
                        {
				            text:'组合',
				            icon: 'images/group.gif',
				            disabled: graph.getSelectionCount() <= 1,
				            scope:this,
				            handler: function()
				            {
				                graph.setSelectionCell(graph.groupCells(null, 20));
				            }
				        },
				        {
				            text:'取消组合',
				            icon: 'images/ungroup.gif',
				            scope:this,
				            handler: function()
				            {
				        		graph.setSelectionCells(graph.ungroupCells());
				            }
				        },
				        '-',
				       	{
				            text:'从组合中移除',
				            scope:this,
				            handler: function()
				            {
				                graph.removeCellsFromParent();
				            }
				        },
				       	{
				            text:'更新组合边界',
				            scope:this,
				            handler: function()
				            {
				        		graph.updateGroupBounds(null, 20);
				            }
				        },
		              	'-',
						{
		                    text:'收缩',
		                    iconCls:'collapse-icon',
		                    disabled: !selected,
		                    scope: this,
		                    handler:function()
		                    {
		                    	graph.foldCells(true);
		                    }
		              	},
		                {
		                    text:'展开',
		                    iconCls:'expand-icon',
		                    disabled: !selected,
		                    scope: this,
		                    handler:function()
		                    {
		                    	graph.foldCells(false);
		                    }
		              	},
		              	'-',
		                {
				            text:'置于底层',
				            icon: 'images/toback.gif',
				            scope:this,
				            handler: function()
				            {
				                graph.orderCells(true);
				            }
				        },
				        {
				            text:'置于顶层',
				            icon: 'images/tofront.gif',
				            scope:this,
				            handler: function()
				            {
				                graph.orderCells(false);
				            }
				        },
				        '-',
				        
				        
				        {
							text:'对齐',
							menu:
							{
								items: [
								{
						            text: '左对齐',
						            icon: 'images/alignleft.gif',
						            handler: function()
						            {
										graph.alignCells(mxConstants.ALIGN_LEFT);
						            }
						        },
						        {
						            text: '居中对齐',
						            icon: 'images/aligncenter.gif',
						            handler: function()
						            {
						        		graph.alignCells(mxConstants.ALIGN_CENTER);
						            }
						        },
						        {
						            text: '右对齐',
						            icon: 'images/alignright.gif',
						            handler: function()
						            {
						        		graph.alignCells(mxConstants.ALIGN_RIGHT);
						            }
						        },
						        '-',
						        {
						            text: '顶端对齐',
						            icon: 'images/aligntop.gif',
						            handler: function()
						            {
						        		graph.alignCells(mxConstants.ALIGN_TOP);
						            }
						        },
						        {
						            text: '垂直对齐',
						            icon: 'images/alignmiddle.gif',
						            handler: function()
						            {
						        		graph.alignCells(mxConstants.ALIGN_MIDDLE);
						            }
						        },
						        {
						            text: '底端对齐',
						            icon: 'images/alignbottom.gif',
						            handler: function()
						            {
						        		graph.alignCells(mxConstants.ALIGN_BOTTOM);
						            }
						        }]
							}
						},
				        '-',
				       	{
				            text:'自动大小',
				            scope:this,
				            handler: function()
				            {
				            	if (!graph.isSelectionEmpty())
				            	{
				            		graph.updateCellSize(graph.getSelectionCell());
				            	}
				            }
				        }]
		            }
			    },
			    '-',
		       	{
		            text:'编辑',
		            scope:this,
		            handler: function()
		            {
		                graph.startEditing();
		            }
		        },
				{
				text:'显示风险',
				scope:this,
				handler: function()
				{
					 
					alert('选择的节点的id='+graph.getSelectionCells()[0].id+'；value='+graph.getSelectionCells()[0].value);
				}
		        },
			    '-',
                {
                    text:'选择形状',
                    scope: this,
                    handler:function()
                    {
			    		graph.selectVertices();
                    }
              	},
              	{
                    text:'选择连接线',
                    scope: this,
                    handler:function()
                    {
              			graph.selectEdges();
                    }
              	},
              	'-',
              	{
                    text:'选择全部',
                    scope: this,
                    handler:function()
                    {
                    	graph.selectAll();
                    }
              	}*/
				{
				text:'查看详细',
				scope:this,
				handler: function()
				{
					 
					alert('选择的节点的id='+graph.getSelectionCells()[0].id+'；value='+graph.getSelectionCells()[0].value);
				}
				}]
            });
	            
            this.menu.on('hide', this.onContextHide, this);
            this.menu.showAt([e.clientX, e.clientY]);
	    },
	
	    onContextHide : function()
	    {
	        if(this.ctxNode)
	        {
	            this.ctxNode.ui.removeClass('x-node-ctx');
	            this.ctxNode = null;
	        }
	    }
    });

    MainPanel.superclass.constructor.call(this,
    {
        region:'center',
        layout: 'fit',
        items: this.graphPanel
    });

    // Redirects the context menu to ExtJs menus
    graph.panningHandler.popup = mxUtils.bind(this, function(x, y, cell, evt)
    {
    	this.graphPanel.onContextMenu(null, evt);
    });

    graph.panningHandler.hideMenu = mxUtils.bind(this, function()
    {
		if (this.graphPanel.menuPanel != null)
    	{
			this.graphPanel.menuPanel.hide();
    	}
    });

    // Fits the SVG container into the panel body
    this.graphPanel.on('resize', function()
    {
        graph.sizeDidChange();
    });
};

Ext.extend(MainPanel, Ext.Panel);

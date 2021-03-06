/*
 * $Id: DiagramPanel.js,v 1.2 2010-01-02 09:45:15 gaudenz Exp $
 * Copyright (c) 2006-2010, JGraph Ltd
 */
DiagramPanel = function(store, mainPanel)
{
	DiagramPanel.superclass.constructor.call(this,
    {
		// TODO: Set initial sorting column and order to name, desc
        title: '示例流程图',
        store: store,
		hideHeaders: false,
		columnSort: true,
		singleSelect: true,
		reserveScrollOffset: true,
        emptyText: 'No diagrams',

        columns: [{
            header: '名称',
            width: 1,
            dataIndex: 'name'
        }],
        
        onContextMenu: function(e, node, scope)
        {
			var idx = this.getSelectedIndexes();
			
			if (idx.length > 0)
			{
				var name = store.getAt(idx[0]).get('name');
				
				if (name != null)
				{
		    		var menu = new Ext.menu.Menu(
		    		{
		                items: [{
		                    text:'Open',
		                    iconCls:'open-icon',
		                    scope: this,
		                    handler:function()
		                    {
	                			mainPanel.openDiagram(name);
		                    }
		                },'-',{
		                    text:'Delete',
		                    iconCls:'delete-icon',
		                    scope: this,
		                    handler:function()
		                    {
		                		if (mxUtils.confirm('Delete "'+name+'"?'))
		                		{
		                			DiagramStore.remove(name);
		                		}
		                    }
		                }]
		            });
		
		            menu.showAt([e.browserEvent.clientX, e.browserEvent.clientY]);
				}
			}
        }
    });
};

Ext.extend(DiagramPanel, Ext.ListView);


/** 
 * 下拉ComboBoxGrid 
 * @extend Ext.form.ComboBox
 * @xtype 'combogrid' 
 * @author hudixin 
 */

FHD.ComboboxGrid = Ext.extend(Ext.form.ComboBox, {  
	passName:'comGrid',
    gridHeight : 180,  
    //listAlign : 'tr-br',  
    listWidth : 300,  
    store : new Ext.data.SimpleStore({  
        fields : [],  
        data : [[]]  
    }),  
   // resizable : false,  
    // Default  
    editable : false,   
    mode : 'local',  
    triggerAction : 'all',  
    maxHeight : 500,  
    onSelect : Ext.emptyFn,  
    emptyText : '\u8bf7\u9009\u62e9...',  
    gridClk:false,
    /** 
     * ----------------------------------  
     * 单击GRID事件 
     * ---------------------------------- 
     */  
    setComboboxGridValue:function(valuesStr){
    	//TODO初始化
    },
    gridClk : function(grid, rowIndex, r) { 
        this.setRawValue(grid.store.getAt(rowIndex).data[this.displayField]);  
        this.setValue(grid.store.getAt(rowIndex).data[this.valueField]);  
        this.collapse();
        this.fireEvent('gridselected', grid.store.getAt(rowIndex));
    },  
    gridSelectionchange : function(selectionModel) { 
    	var rows =selectionModel.getSelections();
    	var ids="";
    	var values="";
		for(var i=0;i<rows.length;i++){
			if(i>0){
				ids+=",";
				values+=",";
			}
			ids+=rows[i].get(this.valueField);
			values+=rows[i].get(this.displayField);
		}
		this.setValue(ids);
		this.setRawValue(values);
		this.setPassValue(ids);
    	this.gridClk=true;
    },  
  
    initLayout : function(){  
        this.grid.autoScroll = true;  
        this.grid.height = this.gridHeight;  
        this.grid.containerScroll = false;  
        this.grid.border=false;  
          
        this.listWidth = this.grid.width+3;  
    },  
    /** 
     * Init 
     */  
    initComponent : function() {  
        FHD.ComboboxGrid.superclass.initComponent.call(this);  
        this.initLayout();  
        this.tplId = Ext.id();  
        // overflow:auto"  
        this.tpl = '<div id="' + this.tplId + '" style="height:' + this.gridHeight  
                + ';overflow:hidden;"></div>';  
          
        //Add Event  
         this.addEvents('gridselected');  
    },  
    /** 
     * 设置传值 
     * @param passvalue 
     */  
    setPassValue: function(passvalue){  
        if (this.passField)  
            this.passField.value = passvalue;  
    }, 
    /** 
     * ------------------  
     *  Listener  
     * ------------------ 
     */  
    listeners : {  
        'expand' : {  
            fn : function() {  
                if (!this.grid.rendered && this.tplId) { 
                    //this.initComponent();  
                    this.initLayout();  
                    this.grid.render(this.tplId);  
                    this.store = this.grid.store;  
                      
                    //this.store.reload();  
                    if(this.store.getCount()==0){  
                        this.store.add(new Ext.data.Record([{}]));    
                    }  
                    //this.grid.store.reload();  
                    if(this.selectMod=='mutiple'){
                    	this.grid.getSelectionModel().on("selectionchange",this.gridSelectionchange,this);
                    }else{
                    	this.grid.on('rowclick', this.gridClk, this);    
                    }
                   
                }  
                  
                this.grid.show();  
            }  
            //single : true  
        },  
  
        'render' : {  
            fn : function() {  
            	if (this.passName) {  
                    this.passField = this.getEl().insertSibling({  
                        tag : 'input',  
                        type : 'hidden',  
                        name : this.passName,  
                        id : this.passId || Ext.id()  
                    }, 'before', true)  
                }  
  
                this.passField.value = this.passValue !== undefined  
                        ? this.passValue  
                        : (this.value !== undefined ? this.value : '');  
  
                this.el.dom.removeAttribute('name');  
            }  
        },  
        'beforedestroy' : {  
            fn : function(cmp) {  
                this.purgeListeners();  
                this.grid.purgeListeners();  
            }  
        },  
        'collapse' : {  
            fn : function(cmp) {  
                /** 
                 *  防止当store的记录为0时不出现下拉的状况 
                 */  
                if(this.grid.store.getCount()==0){  
                    this.store.add(new Ext.data.Record([{}]));  
                }
                if(this.selectMod=='mutiple'&&this.gridClk){
                	this.expand();
                	this.gridClk=false;
                }
            }
        }  
    }  
});

/** 
 * ---------------------------------  
 * register:'combogrid'  
 * --------------------------------- 
 */  
Ext.reg('combogrid', FHD.ComboboxGrid);  
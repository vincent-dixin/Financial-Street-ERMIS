/**
 * @class Ext.ux.TreeCheckNodeUI
 * @extends Ext.tree.TreeNodeUI
 * 
 * �� Ext.tree.TreeNodeUI ����checkbox���ܵ���չ,��̨���صĽ����Ϣ���÷�Ҫ��checked����
 * 
 * ��չ�Ĺ��ܵ��У�
 * һ��֧��ֻ������Ҷ�ӽ���ѡ��
 *    ֻ�е����ص����������leaf = true ʱ��������checkbox��ѡ
 * 	  ʹ��ʱ��ֻ����������ʱ���������� onlyLeafCheckable: true �ȿɣ�Ĭ����false
 * 
 * ����֧�ֶ����ĵ�ѡ
 *    ֻ����ѡ��һ�����
 * 	  ʹ��ʱ��ֻ����������ʱ���������� checkModel: "single" �ȿ�
 * 
 * ����֧�ֶ����ļ�����ѡ 
 *    ��ѡ����ʱ���Զ�ѡ��ý���µ������ӽ�㣬��ý������и���㣨������⣩���ر���֧���첽�����ӽ�㻹û��ʾʱ����Ӻ�̨ȡ���ӽ�㣬Ȼ����ѡ��/ȡ��ѡ��
 *    ʹ��ʱ��ֻ����������ʱ���������� checkModel: "cascade" ��"parentCascade"��"childCascade"�ȿ�
 * 
 * �����"check"�¼�
 *    ���¼�����������checkbox����ı�ʱ����
 *    ʹ��ʱ��ֻ�����ע���¼�,�磺
 *    tree.on("check",function(node,checked){...});
 * 
 * Ĭ������£�checkModelΪ'multiple'��Ҳ���Ƕ�ѡ��onlyLeafCheckableΪfalse�����н�㶼��ѡ
 * 
 * ʹ�÷�������loader����� baseAttrs:{uiProvider:Ext.ux.TreeCheckNodeUI} �ȿ�.
 * ���磺
 *   var tree = new Ext.tree.TreePanel({
 *		el:'tree-ct',
 *		width:568,
 *		height:300,
 *		checkModel: 'cascade',   //�����ļ�����ѡ
 *		onlyLeafCheckable: false,//�������н�㶼��ѡ
 *		animate: false,
 *		rootVisible: false,
 *		autoScroll:true,
 *		loader: new Ext.tree.DWRTreeLoader({
 *			dwrCall:Tmplt.getTmpltTree,
 *			baseAttrs: { uiProvider: Ext.ux.TreeCheckNodeUI } //��� uiProvider ����
 *		}),
 *		root: new Ext.tree.AsyncTreeNode({ id:'0' })
 *	});
 *	tree.on("check",function(node,checked){alert(node.text+" = "+checked)}); //ע��"check"�¼�
 *	tree.render();
 * 
 */

Ext.ux.TreeCheckNodeUI = function() {
	//��ѡ: 'multiple'(Ĭ��)
	//��ѡ: 'single'
	//������ѡ: 'cascade'(ͬʱѡ������);'parentCascade'(ѡ��);'childCascade'(ѡ��)
	this.checkModel = 'multiple';
	
	//only leaf can checked
	this.onlyLeafCheckable = false;
	this.collapsible=false;
	this.animate=false;
	
	Ext.ux.TreeCheckNodeUI.superclass.constructor.apply(this, arguments);
};

Ext.extend(Ext.ux.TreeCheckNodeUI, Ext.tree.TreeNodeUI, {

    renderElements : function(n, a, targetNode, bulkRender){
    	var tree = n.getOwnerTree();
		this.checkModel = tree.checkModel || this.checkModel;
		this.onlyLeafCheckable = tree.onlyLeafCheckable || false;
    	
        // add some indent caching, this helps performance when rendering a large tree
        this.indentMarkup = n.parentNode ? n.parentNode.ui.getChildIndent() : '';

        //var cb = typeof a.checked == 'boolean';
		var cb = (!this.onlyLeafCheckable || a.leaf);
        var href = a.href ? a.href : Ext.isGecko ? "" : "#";
        var buf = ['<li class="x-tree-node"><div ext:tree-node-id="',n.id,'" class="x-tree-node-el x-tree-node-leaf x-unselectable ', a.cls,'" unselectable="on">',
            '<span class="x-tree-node-indent">',this.indentMarkup,"</span>",
            '<img src="', this.emptyIcon, '" class="x-tree-ec-icon x-tree-elbow" />',
            '<img src="', a.icon || this.emptyIcon, '" class="x-tree-node-icon',(a.icon ? " x-tree-node-inline-icon" : ""),(a.iconCls ? " "+a.iconCls : ""),'" unselectable="on" />',
            cb ? ('<input class="x-tree-node-cb" type="checkbox" ' + (a.checked ? 'checked="checked" />' : '/>')) : '',
            '<a hidefocus="on" class="x-tree-node-anchor" href="',href,'" tabIndex="1" ',
             a.hrefTarget ? ' target="'+a.hrefTarget+'"' : "", '><span unselectable="on">',n.text,"</span></a></div>",
            '<ul class="x-tree-node-ct" style="display:none;"></ul>',
            "</li>"].join('');

        var nel;
        if(bulkRender !== true && n.nextSibling && (nel = n.nextSibling.ui.getEl())){
            this.wrap = Ext.DomHelper.insertHtml("beforeBegin", nel, buf);
        }else{
            this.wrap = Ext.DomHelper.insertHtml("beforeEnd", targetNode, buf);
        }
        
        this.elNode = this.wrap.childNodes[0];
        this.ctNode = this.wrap.childNodes[1];
        var cs = this.elNode.childNodes;
        this.indentNode = cs[0];
        this.ecNode = cs[1];
        this.iconNode = cs[2];
        var index = 3;
        if(cb){
            this.checkbox = cs[3];
            Ext.fly(this.checkbox).on('click', this.check.createDelegate(this,[null]));
            //alert("tree="+this.canCollapse);
            index++;
        }
        this.anchor = cs[index];
        this.textNode = cs[index].firstChild;
    },
    
    // private
    click : function(checked){
        var n = this.node;
		var tree = n.getOwnerTree();
		
		tree.fireEvent('click', n, checked);
		n.expand();
		alert("dddddddd");
	},

    
    // private
    check : function(checked){
        var n = this.node;
		var tree = n.getOwnerTree();
		this.checkModel = tree.checkModel || this.checkModel;
		
		if( checked === null ) {
			checked = this.checkbox.checked;
		} else {
			this.checkbox.checked = checked;
		}
		
		n.attributes.checked = checked;
		tree.fireEvent('check', n, checked);
		
		if(this.checkModel == 'single'){
			var checkedNodes = tree.getChecked();
			for(var i=0;i<checkedNodes.length;i++){
				var node = checkedNodes[i];
				if(node.id != n.id){
					node.getUI().checkbox.checked = false;
					node.attributes.checked = false;
					tree.fireEvent('check', node, false);
				}
			}
		} else if(!this.onlyLeafCheckable){
			if(this.checkModel == 'cascade' || this.checkModel == 'parentCascade'){
				var parentNode = n.parentNode;
				if(parentNode !== null) {
					this.parentCheck(parentNode,checked);
				}
			}
			if(this.checkModel == 'cascade' || this.checkModel == 'childCascade'){
				if( !n.expanded && !n.childrenRendered ) {
					n.expand(false,false,this.childCheck);
				}else {
					this.childCheck(n);  
				}
			}
		}
	},

    
    // private
	childCheck : function(node){
		var a = node.attributes;
		if(!a.leaf) {
			var cs = node.childNodes;
			var csui;
			for(var i = 0; i < cs.length; i++) {
				csui = cs[i].getUI();
				if(csui.checkbox.checked ^ a.checked)
					csui.check(a.checked);
			}
		}
	},
	
	// private
	parentCheck : function(node ,checked){
		var checkbox = node.getUI().checkbox;
		if(typeof checkbox == 'undefined')return ;
		if(!(checked ^ checkbox.checked))return;
		if(!checked && this.childHasChecked(node))return;
		checkbox.checked = checked;
		node.attributes.checked = checked;
		node.getOwnerTree().fireEvent('check', node, checked);
		
		var parentNode = node.parentNode;
		if( parentNode !== null){
			this.parentCheck(parentNode,checked);
		}
	},
	
	// private
	childHasChecked : function(node){
		var childNodes = node.childNodes;
		if(childNodes || childNodes.length>0){
			for(var i=0;i<childNodes.length;i++){
				if(childNodes[i].getUI().checkbox.checked)
					return true;
			}
		}
		return false;
	},
	
    toggleCheck : function(value){
    	var cb = this.checkbox;
        if(cb){
            var checked = (value === undefined ? !cb.checked : value);
            this.check(checked);
        }
    }
});
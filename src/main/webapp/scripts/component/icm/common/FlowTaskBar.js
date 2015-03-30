Ext.define('FHD.ux.icm.common.FlowTaskBar',{
	extend : 'Ext.container.Container',
	alias : 'widget.flowtaskbar',
	layout:{
		align: 'stretch',
		type: 'vbox'
	},
	initComponent : function() {
		var me = this;
		var upContainer = Ext.create('Ext.container.Container',{
        	height: 25,
        	layout:{
        		//align: 'stretch',
    			type: 'hbox'
        	}
    	});
    	var downContainer = Ext.create('Ext.container.Container',{
        	height: 25,
        	style:'border-bottom: 1px  #bec0c0 solid !important;',
        	layout:{
        		//align: 'stretch',
    			type: 'hbox'
        	}
    	});
    	var doneImageUrl = __ctxPath + '/images/icm/flow/done.png';
    	var currentImageUrl = __ctxPath + '/images/icm/flow/current.png';
    	var undoImageUrl = __ctxPath + '/images/icm/flow/undo.png';
    	
    	var doneFontColor = '15498b';
    	var currentFontColor = '3366FF';
    	var undoFontColor = 'bec0c0';
    	
    	var normalStyle = 'margin-top:5px;';
    	var normalLineStyle = 'margin-top:15px;';
    	
    	var firstImageStyle = 'margin-left:35px;margin-top:5px;';
    	var lastImageStyle = 'margin-right:35px;margin-top:5px;';
    	
    	var firstTextStyle = 'margin-left:15px;margin-top:5px;';
    	var lastTextStyle = 'margin-right:15px;margin-top:5px;';
    	
    	var doneLineStyle = 'border-bottom: 3px  #99bce8 solid !important;margin-top:15px;';
    	var undoLineStyle = 'border-bottom: 3px  #eeeeee solid !important;margin-top:15px;';
    	
    	/*var jsonArray = [
    		{index: 1, context:'1.计划制定',status:'done'},
    		{index: 2, context:'2.计划审核',status:'done'},
    		{index: 3, context:'3.计划审批',status:'done'},
    		{index: 4, context:'4.计划发布',status:'current'},
    		{index: 5, context:'5.计划发布',status:'undo'},
    		{index: 6, context:'6.计划发布',status:'undo'}
    	];*/
    	var jsonArray = me.jsonArray;
    	Ext.each(jsonArray,function(item,index){
    		var imageUrl,fontColor;
    		if(item.status == 'done'){
    			imageUrl = doneImageUrl;
    			fontColor = doneFontColor;
    		}else if(item.status == 'current'){
    			imageUrl = currentImageUrl;
    			fontColor = currentFontColor;
    		}else{
    			imageUrl = undoImageUrl;
    			fontColor = undoFontColor;
    		}
    		
    		if(item.index == 1){
    			upContainer.add({
			    	xtype:'image',
			    	height:20,
			    	width:20,
		    		src : imageUrl,
		    		style:firstImageStyle
		    	});
		    	downContainer.add({
			    	xtype:'label',
			    	html:'<font color='+fontColor+'>'+item.context+'</font>',
		    		style:firstTextStyle
		    	});
    		}else if(item.index == jsonArray.length){
    			if(item.status != 'undo'){
    				upContainer.add({
		            	xtype:'container',
		            	flex:1,
		            	style:doneLineStyle
	            	});
	            	downContainer.add({
		            	xtype:'container',
		            	flex:1,
		            	style:normalStyle
	            	});
    			}else {
    				upContainer.add({
		            	xtype:'container',
		            	flex:1,
		            	style:undoLineStyle
	            	});
	            	downContainer.add({
		            	xtype:'container',
		            	flex:1,
		            	style:normalStyle
	            	});
    			}
    			upContainer.add({
			    	xtype:'image',
			    	height:20,
			    	width:20,
		    		src : imageUrl,
		    		style:lastImageStyle
		    	});
		    	downContainer.add({
			    	xtype:'label',
			    	html:'<font color='+fontColor+'>'+item.context+'</font>',
		    		style:lastTextStyle
		    	});
    		}else{
    			if(item.status != 'undo'){
    				upContainer.add({
		            	xtype:'container',
		            	flex:1,
		            	style:doneLineStyle
	            	});
	            	downContainer.add({
		            	xtype:'container',
		            	flex:1,
		            	style:normalStyle
	            	});
    			}else {
    				upContainer.add({
		            	xtype:'container',
		            	flex:1,
		            	style:undoLineStyle
	            	});
	            	downContainer.add({
		            	xtype:'container',
		            	flex:1,
		            	style:normalStyle
	            	});
    			}
    			upContainer.add({
			    	xtype:'image',
			    	height:20,
			    	width:20,
		    		src : imageUrl,
		    		style:normalStyle
		    	});
		    	downContainer.add({
			    	xtype:'label',
			    	html:'<font color='+fontColor+'>'+item.context+'</font>',
		    		style:normalStyle
		    	});
    		}
    	});
		Ext.applyIf(me, {
            items: [
            	upContainer,downContainer
            ]
        });
        me.callParent(arguments);
	}
});
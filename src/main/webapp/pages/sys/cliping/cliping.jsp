<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>功能裁剪</title>
<script type="text/javascript">
 
var curRecord;
Ext.onReady(function() {
    
 
var tabs = Ext.create('Ext.tab.Panel', {
	border:false,
	plain: true,
    autoWidth:true
});
var main=Ext.create('Ext.panel.Panel',{ 
	border:false
});
var frame=Ext.create('Ext.panel.Panel',{
    renderTo : 'gncj',
    border:false,
    items:[tabs,main],
    height:FHD.getCenterPanelHeight(),
    width:FHD.getCenterPanelWidth()
    
});



	FHD.ajax({
		url : 'sys/cliping/findClipingTab.f',// 获取面板的地址
		callback : function(data){
			Ext.each(data,function(record,index){
			    if(index==0)
				{
				curRecord=record;
			    frame.remove(main);
				   main= Ext.create('Ext.panel.Panel',{
				       autoScroll:true,
				       border:false,
				       loader: { url:'pages/sys/cliping/clipingedit.jsp?category='+encodeURI(record), loadMask: 'loading...', autoLoad: true, scripts: true },
				       height:FHD.getCenterPanelHeight()-40,
				       width:FHD.getCenterPanelWidth()
					});
				   frame.add(main);
				}
			   var cc= Ext.create('Ext.panel.Panel',{
				    title:record,
				    id:'pan'+index,
				    height:0,
				    border:false,
				    listeners: { 
					activate: function(tab){ 
					    frame.remove(main);
					     eval('curRecord="'+record+'"');
					   main= Ext.create('Ext.panel.Panel',{
					       autoScroll:true,
					       loader: { url:'pages/sys/cliping/clipingedit.jsp?category='+encodeURI(record), loadMask: 'loading...', autoLoad: true, scripts: true },
					       height:FHD.getCenterPanelHeight()-40,
					       width:FHD.getCenterPanelWidth()
						});
						
					   frame.add(main);
					} 
					}
				});
	
			    
			    var tab = tabs.add(cc); 
			   
			});
		 tabs.setActiveTab(tabs.items.items[0]);
		}
	});
	
	FHD.componentResize(frame,0,0);
	frame.on('resize',function(x,w,h){
	    frame.remove(main);
	    main= Ext.create('Ext.panel.Panel',{
		       autoScroll:true,
		       loader: { url:'pages/sys/cliping/clipingedit.jsp?category='+encodeURI(curRecord), loadMask: 'loading...', autoLoad: true, scripts: true },
		       height:FHD.getCenterPanelHeight()-40,
		       width:FHD.getCenterPanelWidth()
			});
	    frame.add(main);
	});

	
});


</script>
</head>
<body>
	<div id='gncj'></div>
	
</body>
</html>
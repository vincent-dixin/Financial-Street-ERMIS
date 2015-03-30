<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/include-tagsOnly.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>文件上传择</title>
		
		<script type="text/javascript">
			Ext.onReady(function(){
				var data=[
					{
						id:"root",
						text:"root",
						isLeftOpen:true,
						isRightOpen:true,
						leftChilds:[
							{
								id:"leftChild1",
								text:"leftChild1",
							},{
								id:"leftChild2",
								text:"leftChild2",
							},{
								id:"leftChild3",
								text:"leftChild3",
							},{
								id:"leftChild4",
								text:"leftChild4",
								isLeftOpen:true,
								leftChilds:[
									{
										id:"leftChild41",
										text:"leftChild41",
									},{
										id:"leftChild42",
										text:"leftChild42",
									}
								]
							}
						],
						rightChilds:[
							{
								id:"rightChild1",
								text:"rightChild1",
							},{
								id:"rightChild2",
								text:"rightChild2",
							},{
								id:"rightChild3",
								text:"rightChild3",
							},{
								id:"rightChild4",
								text:"rightChild4",
								isRightOpen:true,
								rightChilds:[
									{
										id:"rightChild41",
										text:"rightChild41",
										isRightOpen:true,
										rightChilds:[
											{
												id:"rightChild411",
												text:"rightChild411",
												isRightOpen:true,
												rightChilds:[
													{
														id:"rightChild4111",
														text:"rightChild4111",
													},{
														id:"rightChild4112",
														text:"rightChild4112",
													},{
														id:"rightChild4113",
														text:"rightChild4113",
													}
												]
											}
										]
									}
								]
							}
						]
					}
				];
				meshStructureChart({
					render:jQuery("#meshStructureChartDiv"),
					canLeftOpen:false,
					canRightOpen:false,
					nodeCanRightMenu:true,
					lineWidth:20,
					model:"edit",
					delFun:function(node){
						node.delNode();
					},
					addLeftChildFun:function(node){
						newNode={
							parent:node,
							childType:"leftChild",
							id:"newNode",
							text:"newNode"
						};
						node.mainChar.cNode(newNode);
					},
					addRightChildFun:function(node){
						newNode={
							parent:node,
							childType:"rightChild",
							id:"newNode",
							text:"newNode"
						};
						node.mainChar.cNode(newNode);
					},
					openLeftFun:function(openButton){
						var nodes=[
							{
								parent:openButton.node,
								childType:"leftChild",
								id:"leftChild41",
								text:"leftChild41"
							},{
								parent:openButton.node,
								childType:"leftChild",
								id:"leftChild42",
								text:"leftChild42"
							}
						];
						for ( var i = 0; i < nodes.length; i++) {
							openButton.node.mainChar.cNode(nodes[i]);
						}
					},
					openRightFun:function(openButton){
						var nodes=[
							{
								parent:openButton.node,
								childType:"RightChild",
								id:"RightChild",
								text:"RightChild"
							},{
								parent:openButton.node,
								childType:"RightChild",
								id:"RightChild1",
								text:"RightChild1"
							}
						];
						for ( var i = 0; i < nodes.length; i++) {
							openButton.node.mainChar.cNode(nodes[i]);
						}
					},
					nodeClickFun:function(node){
						Ext.Msg.alert(FHD.locale.get('fhd.common.prompt'),node.text);
					},
					nodeProperty:{
						isCanViewMessage:false,
						addLeftChild:true,
						addRightChild:true,
						isCanDel:false,
						canLeftOpen:true,
						canRightOpen:true
					},
					data:data
				});
			});
		</script>
	</head>
	<body>
		<div id='meshStructureChartDiv'></div>
	</body>
</html>
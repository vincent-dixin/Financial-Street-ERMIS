/**
 * 图表转化钻取方法
 * @param config {dataType:"数据类型:baseData,hGridData",datas:"数据集",multiColumns:"列参数",cols:"关键列参数"}
 * @return {}
 */
Ext.define('FHD.view.comm.chart.ChartData', {
	dataTypes:{
		baseData:"baseData",
		hGridData:"hGridData",
		vGridData:"vGridData"
	},
	/**
	 * 初始化配置
	 * @type 
	 */
	config:null,
	/**
	 * 特殊列声明: 
	 * @type [{header:中文列名,dataIndex:英文标示}]
	 */
	cols:null,
	/**
	 * 基本表结构
	 * @type {datas:[{英文标示:值}]}
	 */
	baseData:null,
	/**
	 * 横向表结构
	 * @type 
	 */
	hGridData:null,
	/**
	 * 纵向表结构
	 * @type 
	 */
	vGridData:null,
	
	/**
	 * 一维数据源
	 * @type 
	 */
	chartTotal:null,
	/**
	 * 二维数据源
	 * @type 
	 */
	chartSum:null,
	/**
	 * 三维数据源
	 * @type 
	 */
	chartDetail:null,
	/**
	 * 比较主列信息
	 * @param {} cols1
	 * @param {} cols2
	 * @return {}
	 */
	compareCols:function(cols1,cols2){
		var flag=true;
		if(typeof(cols1)!=typeof(cols2)){
			flag=false;
		}else if(cols1&&cols2&&typeof(cols1.xCol)!=typeof(cols2.xCol)){
			flag=false;
		}else if(cols1&&cols2&&cols1.xCol&&cols2.xCol&&cols1.xCol.header==cols2.xCol.header&&cols1.xCol.dataIndex==cols2.xCol.dataIndex){
			flag=false;
		}else if(cols1&&cols2&&typeof(cols1.yCol)!=typeof(cols2.yCol)){
			flag=false;
		}else if(cols1&&cols2&&cols1.yCol&&cols2.yCol&&cols1.yCol.header==cols2.yCol.header&&cols1.yCol.dataIndex==cols2.yCol.dataIndex){
			flag=false;
		}else if(cols1&&cols2&&typeof(cols1.valueCol)!=typeof(cols2.valueCol)){
			flag=false;
		}else if(cols1&&cols2&&cols1.valueCol&&cols2.valueCol&&cols1.valueCol.header==cols2.valueCol.header&&cols1.valueCol.dataIndex==cols2.valueCol.dataIndex){
			flag=false;
		}
		return flag;
	},
	/**
	 * 复制cols对象
	 * @param {} cols1
	 * @param {} cols2
	 */
	copyCols:function(cols1){
		var cols2={};
		if(cols1){
			if(cols1.xCol){
				cols2.xCol={};
				cols2.xCol.header=cols1.xCol.header;
				cols2.xCol.dataIndex=cols1.xCol.dataIndex;
				cols2.xCol.flex=cols1.xCol.flex;
			}
			if(cols1.yCol){
				cols2.yCol={};
				cols2.yCol.header=cols1.yCol.header;
				cols2.yCol.dataIndex=cols1.yCol.dataIndex;
				cols2.yCol.flex=cols1.yCol.flex;
				cols2.yCol.values=cols1.yCol.values;
			}
			if(cols1.valueCol){
				cols2.valueCol={};
				cols2.valueCol.header=cols1.valueCol.header;
				cols2.valueCol.dataIndex=cols1.valueCol.dataIndex;
				cols2.valueCol.flex=cols1.valueCol.flex;
				cols2.valueCol.sumMethod=cols1.valueCol.sumMethod;
			}
		}
		return cols2;
	},
	/**
	 * 设定特殊列
	 * @param {} cols
	 */
	setCols:function(cols){
		var me=this;
		if(cols){
			me.cols=cols;
			if(me.cols.xCol){
				me.cols.xCol.flex=1;
			}
			if(me.cols.yCol){
				me.cols.yCol.flex=1;
			}
			if(me.cols.valueCol){
				me.cols.valueCol.flex=1;
			}
		}
	},
	/**
	 * 累计计算方法
	 * @param {} value1 累计值
	 * @param {} value2 新值
	 * @param {} weight 累计值权重
	 * @param {} valueColSumMethod 累计方式。
	 * @return {} 累计结果
	 */
	/**
	 * 累计计算方法
	 * @param {} value1 值1
	 * @param {} value2 值2
	 * @param {} weight1 值1权重
	 * @param {} weight2 值2权重
	 * @param {} valueColSumMethod 累计方法：multiply:累乘,average:加权平均,max:最大值,min:最小值,add:累加
	 * @return {} 累计结果
	 */
	computeValue:function(value1,value2,weight1,weight2,valueColSumMethod){
		if(isNaN(value1)){
			value=value2;
		}else{
			var value=value1;
			if(!isNaN(value2)){
				if(valueColSumMethod=="multiply"){
					value*=value2;
				}else if(valueColSumMethod=="average"){
					value=Math.round((value1*weight1+value2*weight2)/(weight1+weight2)*100)/100;
				}else if(valueColSumMethod=="max"){
					if(value<value2){
						value=value2;
					}
				}else if(valueColSumMethod=="min"){
					if(value>value2){
						value=value2;
					}
				}else{
					value+=value2;
				}
			}
		}
		return value
	},
	/**
	 * 获得一维数据源
	 * @return {}
	 */
	getChartTotal:function(){
		var me=this;
		if(me.chartTotal==null){
			me.chartTotal={
				value:0,
				weight:0
			};
			me.chartTotal.cols=me.copyCols(me.cols);
			var valueColDataIndex=me.cols.valueCol.dataIndex;
			var valueColSumMethod=me.cols.valueCol.sumMethod;
			var baseData=me.getBaseData().datas;
			var n=baseData.length;
			for (var i = 0; i < n; i++) {
				var num=baseData[i][valueColDataIndex];
				if(!isNaN(num)){
					var weight=1;
					me.chartTotal.value=me.computeValue(me.chartTotal.value,Number(num),me.chartTotal.weight,weight,valueColSumMethod);
					me.chartTotal.weight+=weight;
				}
			}
		}
		return me.chartTotal;
		datas.push({})
	},
	/**
	 * 获得二维数据源
	 * @return {}
	 */
	getChartSum:function(){
		var me=this;
		if(me.chartSum==null||!me.compareCols(me.chartSum.cols,me.cols)){
			me.chartSum={
				sets:new Array()
			};
			me.chartSum.cols=me.copyCols(me.cols);
			var xColDataIndex=me.cols.xCol.dataIndex;
			var valueColDataIndex=me.cols.valueCol.dataIndex;
			var valueColSumMethod=me.cols.valueCol.sumMethod;
			var baseData=me.getBaseData().datas;
			var n=baseData.length;
			forI:for (var i = 0; i < n; i++) {
				var n1=me.chartSum.sets.length;
				for (var j = 0; j < n1; j++) {
					if(me.chartSum.sets[j].label==baseData[i][xColDataIndex]){
						var num=baseData[i][valueColDataIndex];
						if(!isNaN(num)){
							var weight=1;
							me.chartSum.sets[j].value=me.computeValue(me.chartSum.sets[j].value,Number(num),me.chartSum.sets[j].weight,weight,valueColSumMethod);
							me.chartSum.sets[j].weight+=weight;
						}
						continue forI;
					}
				}
				var setTemp={};
				setTemp.label=baseData[i][xColDataIndex];
				setTemp.value=Number(baseData[i][valueColDataIndex]);
				setTemp.weight=1;
				me.chartSum.sets.push(setTemp);
			}
		}
		return me.chartSum;
	},
	/**
	 * 获得三维数据源
	 * @return {}
	 */
	getChartDetail:function(){
		var me=this;
		
		if(me.chartDetail==null||!me.compareCols(me.chartDetail.cols,me.cols)){
			me.chartDetail={
				sets:new Array()
			};
			var xColDataIndex=me.cols.xCol.dataIndex;
			var yColDataIndex=me.cols.yCol.dataIndex;
			var valueColDataIndex=me.cols.valueCol.dataIndex;
			var valueColSumMethod=me.cols.valueCol.sumMethod;
			var yColValues=me.cols.yCol.values;
			var baseData=me.getBaseData().datas;
			var n=baseData.length;
			yColValues=new Array();
			forI:for (var i = 0; i < n; i++) {
				var yColValue=baseData[i][yColDataIndex];
				var n1=yColValues.length;
				for (var j = 0; j < n1; j++) {
					if(yColValues[j]==yColValue){
						continue forI;
					}
				}
				yColValues.push(yColValue);
			}
			me.cols.yCol.values=yColValues;
			var n3=yColValues.length;
			forI:for (var i = 0; i < n; i++) {
				var n1=me.chartDetail.sets.length;
				for (var j = 0; j < n1; j++) {
					if(me.chartDetail.sets[j].label==baseData[i][xColDataIndex]){
						var n2=me.chartDetail.sets[j].sets.length;
						for (var m = 0; m < n2; m++) {
							if(me.chartDetail.sets[j].sets[m].label==baseData[i][yColDataIndex]){
								var num=baseData[i][valueColDataIndex];
								if(!isNaN(num)){
									var weight=1;
									me.chartDetail.sets[j].sets[m].value=me.computeValue(me.chartDetail.sets[j].sets[m].value,Number(num),me.chartDetail.sets[j].sets[m].weight,weight,valueColSumMethod);
									me.chartDetail.sets[j].sets[m].weight+=weight;
								}
								continue forI;
							}
						}
						var setTemp1={
							label:baseData[i][yColDataIndex],
							value:Number(baseData[i][valueColDataIndex]),
							weight:1
						};
						me.chartDetail.sets[j].sets.push(setTemp1);
						continue forI;
					}
					
				}
				var setsTemp=new Array();
				for (var j = 0; j < n3; j++) {
					var setTemp={
						label:yColValues[j],
						value:0,
						weight:0
					}
					if(yColValues[j]==baseData[i][yColDataIndex]){
						setTemp.value=baseData[i][valueColDataIndex];
						setTemp.weight=1;
					}
					setsTemp.push(setTemp);
				}
				var setTemp1={
					label:baseData[i][xColDataIndex],
					sets:setsTemp
				};
				me.chartDetail.sets.push(setTemp1);
			}
		}
		me.chartDetail.cols=me.copyCols(me.cols);
		return me.chartDetail;
	},
	/**
	 * 获得基础数据源
	 * @return {}
	 */
	getBaseData:function(){
		var me=this;
		return me.baseData;
	},
	/**
	 * 获得横向表
	 * @return {}
	 */
	getHGridData:function(){
		var me=this;
		if(me.hGridData==null||!me.compareCols(me.hGridData.cols,me.cols)){
			me.baseDataToHGridData();
		}
		return me.hGridData;
	},
	/**
	 * 获得纵向表
	 * @return {}
	 */
	getVGridData:function(){
		var me=this;
		if(!me.vGridData||!me.compareCols(me.vGridData.cols,me.cols)){
			me.baseDataToVGridData();
		}
		return me.vGridData;
	},
	
	/**
	 * 将横向表转为基础数据
	 */
	hGridDataToBaseData:function(){
		var me=this;
		me.baseData={
			multiColumns:new Array(),
			datas:new Array(),
			type:me.dataTypes.baseData
		};
		var multiColumns=me.hGridData.multiColumns;
		var datas=me.hGridData.datas;
		var xColHeader=me.cols.xCol.header;
		var xColDataIndex=me.cols.xCol.dataIndex;
		var yColHeader=me.cols.yCol.header;
		var yColDataIndex=me.cols.yCol.dataIndex;
		var valueColHeader=me.cols.valueCol.header;
		var valueColDataIndex=me.cols.valueCol.dataIndex;
		var n1=multiColumns.length;
		for (var i = 0; i < n1; i++) {
			if(multiColumns[i].dataIndex==xColDataIndex){
				me.baseData.multiColumns.push(multiColumns[i]);
			}
		}
		var multiColumn={
			header: yColHeader, 
			dataIndex: yColDataIndex,
			flex:1
		};
		me.baseData.multiColumns.push(multiColumn);
		var multiColumn={
			header: valueColHeader, 
			dataIndex: valueColHeader,
			flex:1
		};
		me.baseData.multiColumns.push(multiColumn);
		
		var n1=datas.length;
		var n2=multiColumns.length;
		for (var i = 0; i < n1; i++) {
			var data=datas[i];
			for (var j = 0; j < n2; j++) {
				if(multiColumns[j].dataIndex!=xColDataIndex){
					var newData={};
					newData[xColDataIndex]=data[xColDataIndex];
					newData[yColDataIndex]=multiColumns[j].dataIndex;
					newData[valueColDataIndex]=data[multiColumns[j].dataIndex];
					me.baseData.datas.push(newData);
				}
			}
		}
	},
	/**
	 * 将基础数据转为横向表
	 */
	baseDataToHGridData:function(){
		var me=this;
		me.hGridData={
			multiColumns:new Array(),
			datas:new Array(),
			type:me.dataTypes.hGridData
		};
		var multiColumns=me.baseData.multiColumns;
		var datas=me.baseData.datas;
		var xColHeader=me.cols.xCol.header;
		var xColDataIndex=me.cols.xCol.dataIndex;
		var yColHeader=me.cols.yCol.header;
		var yColDataIndex=me.cols.yCol.dataIndex;
		var valueColHeader=me.cols.valueCol.header;
		var valueColDataIndex=me.cols.valueCol.dataIndex;
		var valueColSumMethod=me.cols.valueCol.sumMethod;
		var newDatas=new Array();
		var n1=datas.length;
		var yColValues=me.cols.yCol.values;
		var newYColDataIndexMap={};
		me.cols.xCol.flex=1;
		me.hGridData.multiColumns.push(me.cols.xCol);
		yColValues=new Array();
		forI:for (var i = 0; i < n1; i++) {
			var yColValue=datas[i][yColDataIndex];
			var n2=yColValues.length;
			for (var j = 0; j < n2; j++) {
				if(yColValues[j]==yColValue){
					continue forI;
				}
			}
			yColValues.push(yColValue);
			newYColDataIndexMap[yColValue]="yColDataIndex"+i;
			me.hGridData.multiColumns.push({header:yColValue,dataIndex:"yColDataIndex"+i,flex:1});
		}
		if(!valueColSumMethod||valueColSumMethod=="add"){
			me.hGridData.multiColumns.push({header:"合计",dataIndex:"合计",flex:1});
		}
		me.cols.yCol.values=yColValues;
		var n3=yColValues.length;
		forI:for (var i = 0; i < n1; i++) {
			var newYCol=newYColDataIndexMap[datas[i][yColDataIndex]];
			var newData={};
			var n2=newDatas.length;
			for (var j = 0; j < n2; j++) {
				if(datas[i][xColDataIndex]==newDatas[j][xColDataIndex]){
					var num=datas[i][valueColDataIndex];
					if(!isNaN(num)){
						var weight=1;
						if(isNaN(newDatas[j][newYCol])){
							newDatas[j][newYCol]=Number(num);
							newDatas[j][newYCol+"权"]++;
						}else{
							newDatas[j][newYCol]=me.computeValue(newDatas[j][newYCol],Number(num),newDatas[j][newYCol+"权"],weight,valueColSumMethod);
							newDatas[j][newYCol+"权"]+=weight;
						}
						newDatas[j]["合计"]=me.computeValue(newDatas[j]["合计"],Number(num),newDatas[j]["合计权"],weight,valueColSumMethod);
						newDatas[j]["合计权"]+=weight;
					}
					continue forI;
				}
			}
			newData[xColDataIndex]=datas[i][xColDataIndex];
			for (var j = 0; j < n3; j++) {
				newData[newYColDataIndexMap[yColValues[j]]]="-";
				newData[newYColDataIndexMap[yColValues[j]]+"权"]=0;
			}
			newData[newYCol]=datas[i][valueColDataIndex];
			newData[newYCol+"权"]=1;
			newData["合计"]=datas[i][valueColDataIndex];
			newData["合计权"]=1;
			newDatas.push(newData);
		}
		me.hGridData.datas=newDatas;
		me.hGridData.cols=me.copyCols(me.cols);
	},
	/**
	 * 将基础数据转为纵向表
	 */
	baseDataToVGridData:function(){
		var me=this;
		me.vGridData={
			multiColumns:new Array(),
			datas:new Array(),
			type:me.dataTypes.vGridData
		};
		me.vGridData.cols=me.copyCols(me.cols);
		var xColDataIndex=null;
		if(me.cols.xCol){
			xColDataIndex=me.cols.xCol.dataIndex;
			me.cols.xCol.flex=1;
			me.vGridData.multiColumns.push(me.cols.xCol);
		}
		var yColHeader=null;
		var yColDataIndex=null;
		if(me.cols.yCol){
			me.cols.yCol.flex=1;
			me.vGridData.multiColumns.push(me.cols.yCol);
			yColDataIndex=me.cols.yCol.dataIndex;
		}
		if(me.cols.valueCol){
			me.cols.valueCol.flex=1;
			me.vGridData.multiColumns.push(me.cols.valueCol);
		}
		var datas=me.baseData.datas;
		var valueColDataIndex=me.cols.valueCol.dataIndex;
		var valueColSumMethod=me.cols.valueCol.sumMethod;
		var newDatas=new Array();
		var n1=datas.length;
		forI:for (var i = 0; i < n1; i++) {
			var newData={};
			var n2=newDatas.length;
			for (var j = 0; j < n2; j++) {
				if(xColDataIndex==null||datas[i][xColDataIndex]==newDatas[j][xColDataIndex]){
					if(yColDataIndex==null||datas[i][yColDataIndex]==newDatas[j][yColDataIndex]){
						var num=datas[i][valueColDataIndex];
						if(!isNaN(num)){
							var weight=1;
							newDatas[j][valueColDataIndex]=me.computeValue(newDatas[j][valueColDataIndex],Number(num),newDatas[j][valueColDataIndex+"权"],weight,valueColSumMethod);
							newDatas[j][valueColDataIndex+"权"]+=weight;
							continue forI;
						}
					}
				}
			}
			newData[xColDataIndex]=datas[i][xColDataIndex];
			if(yColDataIndex){
				newData[yColDataIndex]=datas[i][yColDataIndex];
			}
			newData[valueColDataIndex]=datas[i][valueColDataIndex];
			newData[valueColDataIndex+"权"]=1;
			newDatas.push(newData);
		}
		me.vGridData.datas=newDatas;
	},
	/**
	 * 初始化方法
	 * @param {} dataType
	 * @param {} datas
	 * @param {} multiColumns
	 */
	init:function(config){
		var me=this;
		me.config=config;
		var dataType=me.config.dataType;
		var datas=me.config.datas;
		var multiColumns=me.config.multiColumns;
		if(me.config.cols){
			me.setCols(me.config.cols);
		}
		var cols=me.cols;
		if(dataType==me.dataTypes.baseData){
			me.baseData={
				datas:datas,
				multiColumns:multiColumns,
				dataType:dataType
			};
		}else if(dataType==me.dataTypes.hGridData){
			me.hGridData={
				datas:datas,
				multiColumns:multiColumns,
				dataType:dataType,
				cols:cols
			};
			me.hGridDataToBaseData();
		}
	}
})
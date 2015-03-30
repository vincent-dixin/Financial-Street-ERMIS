package com.fhd.comm.entity.chart;


/**
 * FusionCharts的官方四大分类:
 * 1.FusionCharts
 * 2.FusionMaps
 * 3.FusionWidgets
 * 4.PowerCharts
 * 
 * FusionCharts的FusionCharts分类有很多种。
 * 按数据类型分类有:
 * 1.单组数据类型图表(Single Data Charts)
 * 2.多组数据类型图表(Multi-Data Charts)
 * 按图的展示类型分类(也是官方分类)有:
 * 1.单系列图表(Single Series Charts)
 * 2.多系列图表(Multi-Series Charts)
 * 3.堆积式图表(Stacked Charts)
 * 4.组合图表(Combination Charts)
 * 5.分布图(XY Plot Charts)
 * 6.滚动图(Scroll Charts)

 * FusionCharts 的 XML标签属性有一下四种数据类型:
 * <1>Boolean - 布尔类型，只能为1或者0。例如：<chart showNames=’1′ >
 * <2>Number - 数字类型，只能为数字。例如：<chart yAxisMaxValue=’200′ >
 * <3>String - 字符串类型，只能为字符串。例如： <chart caption=’My Chart’ >
 * <4>Hex Color Code - 十六进制颜色代码，前边没有’#’.例如： <chart bgColor=’FFFFDD’ >
 * @author 吴德福
 * @since 2013-1-7 11:40:00
 */
public class Chart implements java.io.Serializable{
	
	private static final long serialVersionUID = -2914041562936689777L;
	
	//1.chart:
	/*
	 * 1.Functional Attributes(功能属性)
	 */
	//设置图形的显示是否是动画显示:0/1
	private String animation;
	//5种默认的调色板风格任你选:1-5
	private String palette;
	//手动设置调色板的颜色paletteColors='FF0000,0372AB,FF5904...'
	private String paletteColors;
	//在图形上点击右键时是否显示about链接:0/1
	private String showAboutMenuItem;
	//about链接的具体名字:String
	private String aboutMenuItemLabel;
	//about链接的具体链接地址:String
	private String aboutMenuItemLink;
	//是否显示x轴的坐标值:0/1
	private String showLabels;
	//x轴坐标值的具体展现形式:WRAP/STAGGER/ROTATE/NONE
	private String labelDisplay;
	//是否旋转x轴的坐标值:0/1
	private String rotateLabels;
	//将x轴坐标值旋转为倾斜的还是完全垂直的:0/1
	private String slantLabels;
	//x轴坐标值的步长，即可以设置隔几个柱子显示一个值:1 or above
	private String labelStep;
	//如果labelDisplay设置为STAGGER,则此属性是控制一个展示周期:2 or above
	private String staggerLines;
	//是否在图形上显示每根柱子具体的值:0/1
	private String showValues;
	//是否旋转图形上显示的柱子的值:0/1
	private String rotateValues;
	//图形上柱子的值是否显示在柱子里面:0/1
	private String placeValuesInside;
	//是否显示Y轴的值:0/1
	private String showYAxisValues;
	//是否显示极值:0/1
	private String showLimits;
	//是否在divline处显示值:0/1
	private String showDivLineValues;
	//Y轴值的步长:1 or above
	private String yAxisValuesStep;
	//是否显示阴影:0/1
	private String showShadow;
	//是否自动调整divlines:0/1
	private String adjustDiv;
	//是否旋转Y轴的名字:0/1
	private String rotateYAxisName;
	//Y轴名字的宽度:Number
	private String yAxisNameWidth;
	//点击的链接地址:String
	private String clickURL;
	//是否使用默认动画:0/1
	private String defaultAnimation;
	//Y轴的最小值:Number
	private String yAxisMinValue;
	//Y轴的最大值:Number
	private String yAxisMaxValue;
	//自动设置Y轴的最小值:0/1
	private String setAdaptiveYMin;
	/*
	 * 2.Titles and Axis Names(标题和坐标抽名字)
	 */
	//主标题名字:String
	private String caption;
	//副标题名字:String
	private String subCaption;
	//X轴名字:String
	private String xAxisName;
	//Y轴名字:String
	private String yAxisName;
	/*
	 * 3.Charts Cosmetics(图表美容属性)
	 */
	//图表的背景色:Color
	private String bgColor;
	//背景色的透明度:0-100
	private String bgAlpha;
	//如果背景色有两个，该属性设置差异的比例:1-100
	private String bgRatio;
	//转变背景颜色的角度，设置一个倾斜度:0-360
	private String bgAngle;
	//用做背景的swf路径:String
	private String bgSWF;
	//背景swf的透明度:0-100
	private String bgSWFAlpha;
	//画板背景颜色:Color
	private String canvasBgColor;
	//画板背景透明度:0-100
	private String canvasBgAlpha;
	//不同画板背景色的比率:Number
	private String canvasBgRatio;
	//画布背景色显示角度:Number
	private String canvasBgAngle;
	//画板边框的颜色:Color
	private String canvasBorderColor;
	//画板边框的宽度:Number
	private String canvasBorderThickness;
	//画板边框的透明度:0-100
	private String canvasBorderAlpha;
	//是否显示图表边框:0/1
	private String showBorder;
	//边框颜色:Color
	private String borderColor;
	//图表边框的粗细:Number
	private String borderThickness;
	//边框透明度:0-100
	private String borderAlpha;
	//是否显示垂直线label的宽度:0/1
	private String showVLineLabelBorder;
	//在图表上加上logo,logo图片的地址:String
	private String logoURL;
	//logo的位置:TL/TR/BL/BR/CC
	private String logoPosition;
	//logo的透明度:0-100
	private String logoAlpha;
	//控制logo放大缩小的倍数:1-300
	private String logoScale;
	//logo的链接地址:String
	private String logoLink;
	/*
	 * 4.Divisional Lines/Grids(分区线/网格 属性)
	 */
	//水平网格线的数量>0
	private String numDivLines;
	//网格线颜色:Color
	private String divLineColor;
	//网格线粗细:1-5
	private String divLineThickness;
	//网格线透明度:0-100
	private String divLineAlpha;
	//网格线是否显示为虚线:0/1
	private String divLineIsDashed;
	//每个虚线的长度:Number
	private String divLineDashLen;
	//每个虚线间的间隔长度:Number
	private String divLineDashGap;
	//0值处网格线颜色:Color
	private String zeroPlaneColor;
	//0值处网格线粗细:Number
	private String zeroPlaneThickness;
	//0值处网格线透明度:0-100
	private String zeroPlaneAlpha;
	//是否交替显示网格颜色:0/1
	private String showAlternateHGridColor;
	//水平网格颜色:Color
	private String alternateHGridColor;
	//水平网格透明度:Number
	private String alternateHGridAlpha;
	/*
	 * 5.Tool-tip(工具提示属性)
	 */
	//是否显示气泡提示:0/1
	private String showToolTip;
	//气泡提示的背景颜色:Color
	private String toolTipBgColor;
	//汽包提示的边框颜色:Color
	private String toolTipBorderColor;
	//气泡提示的分隔符:String
	private String toolTipSepChar;
	//是否使气泡提示带有阴影效果:0/1
	private String showToolTipShadow;
	/**
	 * 6.Paddings and Margins(填充和边距属性)
	 */
	//Canvas上边线离图表标题的距离:Number
	private String captionPadding;
	//画板与x轴标题之间的距离:Number
	private String xAxisNamePadding;
	//画板与y轴标题之间的距离:Number
	private String yAxisNamePadding;
	//画板与y轴值之间的距离:Number
	private String yAxisValuesPadding;
	//画板离label之间的距离:Number
	private String labelPadding;
	//柱子离值之间的距离:Number
	private String valuePadding;
	//两个bar之间的距离:0-80
	private String plotSpacePercent;
	//距左边框的距离:Number
	private String chartLeftMargin;
	//距右边框的距离:Number
	private String chartRightMargin;
	//距上边框的距离:Number
	private String chartTopMargin;
	//距下边框的距离:Number
	private String chartBottomMargin;
	//画板离左边的距离:Number
	private String canvasLeftMargin;
	//画板离右边的距离:Number
	private String canvasRightMargin;
	//画板离上边的距离:Number
	private String canvasTopMargin;
	//画板离下边的距离:Number
	private String canvasBottomMargin;
	
	//2.Data:
	/*
	 * <1><set> element(set元素属性)
	 */
	//具体的值:Number
	private String value;
	//链接地址:link
	private String link;
	//是否显示标签:0/1
	private String showLabel;
	//是否显示此柱子的值:0/1
	private String showValue;

	/*
	 * <2>Plot Cosmetics(节点美容属性)
	 */
	//是否显示光滑边缘:0/1
	private String useRoundEdges;
	//是否显示柱子的边框:0/1
	private String showPlotBorder;
	//柱子边框的颜色:Color
	private String plotBorderColor;
	//柱子边框的厚度:0-5
	private String plotBorderThickness;
	//柱子边框的透明度:0-100
	private String plotBorderAlpha;
	//柱子边框是否显示为虚线:0/1
	private String plotBorderDashed;
	//虚线的长度:Number
	private String plotBorderDashLen;
	//虚线的间隔:Number
	private String plotBorderDashGap;
	//数据填充色角度:0-360
	private String plotFillAngle;
	//数据填充色比率:0-100
	private String plotFillRatio;
	//数据填充色透明度:0-100
	private String plotFillAlpha;
	//数据的有坡度颜色方案:Color
	private String plotGradientColor;

	//3.Others:
	/*
	 * <1>Number Formatting(数字格式化属性)
	 */
	//是否格式化数值:0-1
	private String formatNumber;
	//是否对大数值以k,M方式表示:0-1
	private String formatNumberScale;
	//默认的数字格式化:String
	private String defaultNumberScale;
	//设置进位规则对应的单位eg:k,m,b:String
	private String numberScaleUnit;
	//设置进位的规则eg:1000,1000,1000:String
	private String numberScaleValue;
	//数值前缀:String
	private String numberPrefix;
	//数值后缀:String
	private String numberSuffix;
	//设置小数点的分隔符的表示形式,|.:String
	private String decimalSeparator;
	//设置3位数值之间的分隔符的表示形式,|.:String
	private String thousandSeparator;
	//设置小数分隔符:String
	private String inDecimalSeparator;
	//设置千位分隔符:String
	private String inThousandSeparator;
	//小数点后保留几位:0-10
	private String decimals;
	//小数点后位数不够的，是否强制补0:0/1
	private String forceDecimals;
	//y轴值保留几位小数:0-10
	private String yAxisValueDecimals;

	/* 
	 * <2>Font Properties(字体属性)
	 */
	//字体:String
	private String baseFont;
	//字体大小:0-72
	private String baseFontSize;
	//字体颜色:Color
	private String baseFontColor;
	//画板外的字体:String
	private String outCnvBaseFont;
	//画板外的字体大小:0-72
	private String outCnvBaseFontSize;
	//画板外的字体颜色:Color
	private String outCnvBaseFontColor;

	/*
	 * <3>Vertical Lines(垂直线属性)
	 */
	//是否显示label的边框:0/1
	private String showLabelBorder;
	//line的位置:0/1
	private String linePosition;
	//label的位置:0/1
	private String labelPosition;
	//水平线label的位置:left/center/right
	private String labelHAlign;
	//垂直线label的位置:top/middle/bottom
	private String labelVAlign;

	/*
	 * <4>Trend Lines(趋势线属性)
	 */
	//开始值:Number
	private String startValue;
	//结束值:Number
	private String endValue;
	//是否显示趋势线:0/1
	private String isTrendZone;
	//趋势线是否显示在上面:0/1
	private String showOnTop;
	//趋势线的标记是否在右边:0/1
	private String valueOnRight;


	/*
	 * 共有的属性
	 */
	//颜色:Color
	private String color;
	//厚度:Number
	private String thickness;
	//透明度:0-100
	private String alpha;
	//是否使用虚线:0/1
	private String dashed;
	//虚线的长度:Number
	private String dashLen;
	//虚线间隔的长度:Number
	private String dashGap;
	//此垂直线的名字:String
	private String label;
	//显示的值:String
	private String displayValue;
	//提示时显示的值:String
	private String toolText;
	
	public Chart() {
		super();
	}

	public Chart(String animation, String palette, String paletteColors,
			String showAboutMenuItem, String aboutMenuItemLabel,
			String aboutMenuItemLink, String showLabels, String labelDisplay,
			String rotateLabels, String slantLabels, String labelStep,
			String staggerLines, String showValues, String rotateValues,
			String placeValuesInside, String showYAxisValues,
			String showLimits, String showDivLineValues,
			String yAxisValuesStep, String showShadow, String adjustDiv,
			String rotateYAxisName, String yAxisNameWidth, String clickURL,
			String defaultAnimation, String yAxisMinValue,
			String yAxisMaxValue, String setAdaptiveYMin, String caption,
			String subCaption, String xAxisName, String yAxisName,
			String bgColor, String bgAlpha, String bgRatio, String bgAngle,
			String bgSWF, String bgSWFAlpha, String canvasBgColor,
			String canvasBgAlpha, String canvasBgRatio, String canvasBgAngle,
			String canvasBorderColor, String canvasBorderThickness,
			String canvasBorderAlpha, String showBorder, String borderColor,
			String borderThickness, String borderAlpha,
			String showVLineLabelBorder, String logoURL, String logoPosition,
			String logoAlpha, String logoScale, String logoLink,
			String numDivLines, String divLineColor, String divLineThickness,
			String divLineAlpha, String divLineIsDashed, String divLineDashLen,
			String divLineDashGap, String zeroPlaneColor,
			String zeroPlaneThickness, String zeroPlaneAlpha,
			String showAlternateHGridColor, String alternateHGridColor,
			String alternateHGridAlpha, String showToolTip,
			String toolTipBgColor, String toolTipBorderColor,
			String toolTipSepChar, String showToolTipShadow,
			String captionPadding, String xAxisNamePadding,
			String yAxisNamePadding, String yAxisValuesPadding,
			String labelPadding, String valuePadding, String plotSpacePercent,
			String chartLeftMargin, String chartRightMargin,
			String chartTopMargin, String chartBottomMargin,
			String canvasLeftMargin, String canvasRightMargin,
			String canvasTopMargin, String canvasBottomMargin, String value,
			String link, String showLabel, String showValue,
			String useRoundEdges, String showPlotBorder,
			String plotBorderColor, String plotBorderThickness,
			String plotBorderAlpha, String plotBorderDashed,
			String plotBorderDashLen, String plotBorderDashGap,
			String plotFillAngle, String plotFillRatio, String plotFillAlpha,
			String plotGradientColor, String formatNumber,
			String formatNumberScale, String defaultNumberScale,
			String numberScaleUnit, String numberScaleValue,
			String numberPrefix, String numberSuffix, String decimalSeparator,
			String thousandSeparator, String inDecimalSeparator,
			String inThousandSeparator, String decimals, String forceDecimals,
			String yAxisValueDecimals, String baseFont, String baseFontSize,
			String baseFontColor, String outCnvBaseFont,
			String outCnvBaseFontSize, String outCnvBaseFontColor,
			String showLabelBorder, String linePosition, String labelPosition,
			String labelHAlign, String labelVAlign, String startValue,
			String endValue, String isTrendZone, String showOnTop,
			String valueOnRight, String color, String thickness, String alpha,
			String dashed, String dashLen, String dashGap, String label,
			String displayValue, String toolText) {
		super();
		this.animation = animation;
		this.palette = palette;
		this.paletteColors = paletteColors;
		this.showAboutMenuItem = showAboutMenuItem;
		this.aboutMenuItemLabel = aboutMenuItemLabel;
		this.aboutMenuItemLink = aboutMenuItemLink;
		this.showLabels = showLabels;
		this.labelDisplay = labelDisplay;
		this.rotateLabels = rotateLabels;
		this.slantLabels = slantLabels;
		this.labelStep = labelStep;
		this.staggerLines = staggerLines;
		this.showValues = showValues;
		this.rotateValues = rotateValues;
		this.placeValuesInside = placeValuesInside;
		this.showYAxisValues = showYAxisValues;
		this.showLimits = showLimits;
		this.showDivLineValues = showDivLineValues;
		this.yAxisValuesStep = yAxisValuesStep;
		this.showShadow = showShadow;
		this.adjustDiv = adjustDiv;
		this.rotateYAxisName = rotateYAxisName;
		this.yAxisNameWidth = yAxisNameWidth;
		this.clickURL = clickURL;
		this.defaultAnimation = defaultAnimation;
		this.yAxisMinValue = yAxisMinValue;
		this.yAxisMaxValue = yAxisMaxValue;
		this.setAdaptiveYMin = setAdaptiveYMin;
		this.caption = caption;
		this.subCaption = subCaption;
		this.xAxisName = xAxisName;
		this.yAxisName = yAxisName;
		this.bgColor = bgColor;
		this.bgAlpha = bgAlpha;
		this.bgRatio = bgRatio;
		this.bgAngle = bgAngle;
		this.bgSWF = bgSWF;
		this.bgSWFAlpha = bgSWFAlpha;
		this.canvasBgColor = canvasBgColor;
		this.canvasBgAlpha = canvasBgAlpha;
		this.canvasBgRatio = canvasBgRatio;
		this.canvasBgAngle = canvasBgAngle;
		this.canvasBorderColor = canvasBorderColor;
		this.canvasBorderThickness = canvasBorderThickness;
		this.canvasBorderAlpha = canvasBorderAlpha;
		this.showBorder = showBorder;
		this.borderColor = borderColor;
		this.borderThickness = borderThickness;
		this.borderAlpha = borderAlpha;
		this.showVLineLabelBorder = showVLineLabelBorder;
		this.logoURL = logoURL;
		this.logoPosition = logoPosition;
		this.logoAlpha = logoAlpha;
		this.logoScale = logoScale;
		this.logoLink = logoLink;
		this.numDivLines = numDivLines;
		this.divLineColor = divLineColor;
		this.divLineThickness = divLineThickness;
		this.divLineAlpha = divLineAlpha;
		this.divLineIsDashed = divLineIsDashed;
		this.divLineDashLen = divLineDashLen;
		this.divLineDashGap = divLineDashGap;
		this.zeroPlaneColor = zeroPlaneColor;
		this.zeroPlaneThickness = zeroPlaneThickness;
		this.zeroPlaneAlpha = zeroPlaneAlpha;
		this.showAlternateHGridColor = showAlternateHGridColor;
		this.alternateHGridColor = alternateHGridColor;
		this.alternateHGridAlpha = alternateHGridAlpha;
		this.showToolTip = showToolTip;
		this.toolTipBgColor = toolTipBgColor;
		this.toolTipBorderColor = toolTipBorderColor;
		this.toolTipSepChar = toolTipSepChar;
		this.showToolTipShadow = showToolTipShadow;
		this.captionPadding = captionPadding;
		this.xAxisNamePadding = xAxisNamePadding;
		this.yAxisNamePadding = yAxisNamePadding;
		this.yAxisValuesPadding = yAxisValuesPadding;
		this.labelPadding = labelPadding;
		this.valuePadding = valuePadding;
		this.plotSpacePercent = plotSpacePercent;
		this.chartLeftMargin = chartLeftMargin;
		this.chartRightMargin = chartRightMargin;
		this.chartTopMargin = chartTopMargin;
		this.chartBottomMargin = chartBottomMargin;
		this.canvasLeftMargin = canvasLeftMargin;
		this.canvasRightMargin = canvasRightMargin;
		this.canvasTopMargin = canvasTopMargin;
		this.canvasBottomMargin = canvasBottomMargin;
		this.value = value;
		this.link = link;
		this.showLabel = showLabel;
		this.showValue = showValue;
		this.useRoundEdges = useRoundEdges;
		this.showPlotBorder = showPlotBorder;
		this.plotBorderColor = plotBorderColor;
		this.plotBorderThickness = plotBorderThickness;
		this.plotBorderAlpha = plotBorderAlpha;
		this.plotBorderDashed = plotBorderDashed;
		this.plotBorderDashLen = plotBorderDashLen;
		this.plotBorderDashGap = plotBorderDashGap;
		this.plotFillAngle = plotFillAngle;
		this.plotFillRatio = plotFillRatio;
		this.plotFillAlpha = plotFillAlpha;
		this.plotGradientColor = plotGradientColor;
		this.formatNumber = formatNumber;
		this.formatNumberScale = formatNumberScale;
		this.defaultNumberScale = defaultNumberScale;
		this.numberScaleUnit = numberScaleUnit;
		this.numberScaleValue = numberScaleValue;
		this.numberPrefix = numberPrefix;
		this.numberSuffix = numberSuffix;
		this.decimalSeparator = decimalSeparator;
		this.thousandSeparator = thousandSeparator;
		this.inDecimalSeparator = inDecimalSeparator;
		this.inThousandSeparator = inThousandSeparator;
		this.decimals = decimals;
		this.forceDecimals = forceDecimals;
		this.yAxisValueDecimals = yAxisValueDecimals;
		this.baseFont = baseFont;
		this.baseFontSize = baseFontSize;
		this.baseFontColor = baseFontColor;
		this.outCnvBaseFont = outCnvBaseFont;
		this.outCnvBaseFontSize = outCnvBaseFontSize;
		this.outCnvBaseFontColor = outCnvBaseFontColor;
		this.showLabelBorder = showLabelBorder;
		this.linePosition = linePosition;
		this.labelPosition = labelPosition;
		this.labelHAlign = labelHAlign;
		this.labelVAlign = labelVAlign;
		this.startValue = startValue;
		this.endValue = endValue;
		this.isTrendZone = isTrendZone;
		this.showOnTop = showOnTop;
		this.valueOnRight = valueOnRight;
		this.color = color;
		this.thickness = thickness;
		this.alpha = alpha;
		this.dashed = dashed;
		this.dashLen = dashLen;
		this.dashGap = dashGap;
		this.label = label;
		this.displayValue = displayValue;
		this.toolText = toolText;
	}

	public String getAnimation() {
		return animation;
	}

	public void setAnimation(String animation) {
		this.animation = animation;
	}

	public String getPalette() {
		return palette;
	}

	public void setPalette(String palette) {
		this.palette = palette;
	}

	public String getPaletteColors() {
		return paletteColors;
	}

	public void setPaletteColors(String paletteColors) {
		this.paletteColors = paletteColors;
	}

	public String getShowAboutMenuItem() {
		return showAboutMenuItem;
	}

	public void setShowAboutMenuItem(String showAboutMenuItem) {
		this.showAboutMenuItem = showAboutMenuItem;
	}

	public String getAboutMenuItemLabel() {
		return aboutMenuItemLabel;
	}

	public void setAboutMenuItemLabel(String aboutMenuItemLabel) {
		this.aboutMenuItemLabel = aboutMenuItemLabel;
	}

	public String getAboutMenuItemLink() {
		return aboutMenuItemLink;
	}

	public void setAboutMenuItemLink(String aboutMenuItemLink) {
		this.aboutMenuItemLink = aboutMenuItemLink;
	}

	public String getShowLabels() {
		return showLabels;
	}

	public void setShowLabels(String showLabels) {
		this.showLabels = showLabels;
	}

	public String getLabelDisplay() {
		return labelDisplay;
	}

	public void setLabelDisplay(String labelDisplay) {
		this.labelDisplay = labelDisplay;
	}

	public String getRotateLabels() {
		return rotateLabels;
	}

	public void setRotateLabels(String rotateLabels) {
		this.rotateLabels = rotateLabels;
	}

	public String getSlantLabels() {
		return slantLabels;
	}

	public void setSlantLabels(String slantLabels) {
		this.slantLabels = slantLabels;
	}

	public String getLabelStep() {
		return labelStep;
	}

	public void setLabelStep(String labelStep) {
		this.labelStep = labelStep;
	}

	public String getStaggerLines() {
		return staggerLines;
	}

	public void setStaggerLines(String staggerLines) {
		this.staggerLines = staggerLines;
	}

	public String getShowValues() {
		return showValues;
	}

	public void setShowValues(String showValues) {
		this.showValues = showValues;
	}

	public String getRotateValues() {
		return rotateValues;
	}

	public void setRotateValues(String rotateValues) {
		this.rotateValues = rotateValues;
	}

	public String getPlaceValuesInside() {
		return placeValuesInside;
	}

	public void setPlaceValuesInside(String placeValuesInside) {
		this.placeValuesInside = placeValuesInside;
	}

	public String getShowYAxisValues() {
		return showYAxisValues;
	}

	public void setShowYAxisValues(String showYAxisValues) {
		this.showYAxisValues = showYAxisValues;
	}

	public String getShowLimits() {
		return showLimits;
	}

	public void setShowLimits(String showLimits) {
		this.showLimits = showLimits;
	}

	public String getShowDivLineValues() {
		return showDivLineValues;
	}

	public void setShowDivLineValues(String showDivLineValues) {
		this.showDivLineValues = showDivLineValues;
	}

	public String getyAxisValuesStep() {
		return yAxisValuesStep;
	}

	public void setyAxisValuesStep(String yAxisValuesStep) {
		this.yAxisValuesStep = yAxisValuesStep;
	}

	public String getShowShadow() {
		return showShadow;
	}

	public void setShowShadow(String showShadow) {
		this.showShadow = showShadow;
	}

	public String getAdjustDiv() {
		return adjustDiv;
	}

	public void setAdjustDiv(String adjustDiv) {
		this.adjustDiv = adjustDiv;
	}

	public String getRotateYAxisName() {
		return rotateYAxisName;
	}

	public void setRotateYAxisName(String rotateYAxisName) {
		this.rotateYAxisName = rotateYAxisName;
	}

	public String getyAxisNameWidth() {
		return yAxisNameWidth;
	}

	public void setyAxisNameWidth(String yAxisNameWidth) {
		this.yAxisNameWidth = yAxisNameWidth;
	}

	public String getClickURL() {
		return clickURL;
	}

	public void setClickURL(String clickURL) {
		this.clickURL = clickURL;
	}

	public String getDefaultAnimation() {
		return defaultAnimation;
	}

	public void setDefaultAnimation(String defaultAnimation) {
		this.defaultAnimation = defaultAnimation;
	}

	public String getyAxisMinValue() {
		return yAxisMinValue;
	}

	public void setyAxisMinValue(String yAxisMinValue) {
		this.yAxisMinValue = yAxisMinValue;
	}

	public String getyAxisMaxValue() {
		return yAxisMaxValue;
	}

	public void setyAxisMaxValue(String yAxisMaxValue) {
		this.yAxisMaxValue = yAxisMaxValue;
	}

	public String getSetAdaptiveYMin() {
		return setAdaptiveYMin;
	}

	public void setSetAdaptiveYMin(String setAdaptiveYMin) {
		this.setAdaptiveYMin = setAdaptiveYMin;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getSubCaption() {
		return subCaption;
	}

	public void setSubCaption(String subCaption) {
		this.subCaption = subCaption;
	}

	public String getxAxisName() {
		return xAxisName;
	}

	public void setxAxisName(String xAxisName) {
		this.xAxisName = xAxisName;
	}

	public String getyAxisName() {
		return yAxisName;
	}

	public void setyAxisName(String yAxisName) {
		this.yAxisName = yAxisName;
	}

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	public String getBgAlpha() {
		return bgAlpha;
	}

	public void setBgAlpha(String bgAlpha) {
		this.bgAlpha = bgAlpha;
	}

	public String getBgRatio() {
		return bgRatio;
	}

	public void setBgRatio(String bgRatio) {
		this.bgRatio = bgRatio;
	}

	public String getBgAngle() {
		return bgAngle;
	}

	public void setBgAngle(String bgAngle) {
		this.bgAngle = bgAngle;
	}

	public String getBgSWF() {
		return bgSWF;
	}

	public void setBgSWF(String bgSWF) {
		this.bgSWF = bgSWF;
	}

	public String getBgSWFAlpha() {
		return bgSWFAlpha;
	}

	public void setBgSWFAlpha(String bgSWFAlpha) {
		this.bgSWFAlpha = bgSWFAlpha;
	}

	public String getCanvasBgColor() {
		return canvasBgColor;
	}

	public void setCanvasBgColor(String canvasBgColor) {
		this.canvasBgColor = canvasBgColor;
	}

	public String getCanvasBgAlpha() {
		return canvasBgAlpha;
	}

	public void setCanvasBgAlpha(String canvasBgAlpha) {
		this.canvasBgAlpha = canvasBgAlpha;
	}

	public String getCanvasBgRatio() {
		return canvasBgRatio;
	}

	public void setCanvasBgRatio(String canvasBgRatio) {
		this.canvasBgRatio = canvasBgRatio;
	}

	public String getCanvasBgAngle() {
		return canvasBgAngle;
	}

	public void setCanvasBgAngle(String canvasBgAngle) {
		this.canvasBgAngle = canvasBgAngle;
	}

	public String getCanvasBorderColor() {
		return canvasBorderColor;
	}

	public void setCanvasBorderColor(String canvasBorderColor) {
		this.canvasBorderColor = canvasBorderColor;
	}

	public String getCanvasBorderThickness() {
		return canvasBorderThickness;
	}

	public void setCanvasBorderThickness(String canvasBorderThickness) {
		this.canvasBorderThickness = canvasBorderThickness;
	}

	public String getCanvasBorderAlpha() {
		return canvasBorderAlpha;
	}

	public void setCanvasBorderAlpha(String canvasBorderAlpha) {
		this.canvasBorderAlpha = canvasBorderAlpha;
	}

	public String getShowBorder() {
		return showBorder;
	}

	public void setShowBorder(String showBorder) {
		this.showBorder = showBorder;
	}

	public String getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}

	public String getBorderThickness() {
		return borderThickness;
	}

	public void setBorderThickness(String borderThickness) {
		this.borderThickness = borderThickness;
	}

	public String getBorderAlpha() {
		return borderAlpha;
	}

	public void setBorderAlpha(String borderAlpha) {
		this.borderAlpha = borderAlpha;
	}

	public String getShowVLineLabelBorder() {
		return showVLineLabelBorder;
	}

	public void setShowVLineLabelBorder(String showVLineLabelBorder) {
		this.showVLineLabelBorder = showVLineLabelBorder;
	}

	public String getLogoURL() {
		return logoURL;
	}

	public void setLogoURL(String logoURL) {
		this.logoURL = logoURL;
	}

	public String getLogoPosition() {
		return logoPosition;
	}

	public void setLogoPosition(String logoPosition) {
		this.logoPosition = logoPosition;
	}

	public String getLogoAlpha() {
		return logoAlpha;
	}

	public void setLogoAlpha(String logoAlpha) {
		this.logoAlpha = logoAlpha;
	}

	public String getLogoScale() {
		return logoScale;
	}

	public void setLogoScale(String logoScale) {
		this.logoScale = logoScale;
	}

	public String getLogoLink() {
		return logoLink;
	}

	public void setLogoLink(String logoLink) {
		this.logoLink = logoLink;
	}

	public String getNumDivLines() {
		return numDivLines;
	}

	public void setNumDivLines(String numDivLines) {
		this.numDivLines = numDivLines;
	}

	public String getDivLineColor() {
		return divLineColor;
	}

	public void setDivLineColor(String divLineColor) {
		this.divLineColor = divLineColor;
	}

	public String getDivLineThickness() {
		return divLineThickness;
	}

	public void setDivLineThickness(String divLineThickness) {
		this.divLineThickness = divLineThickness;
	}

	public String getDivLineAlpha() {
		return divLineAlpha;
	}

	public void setDivLineAlpha(String divLineAlpha) {
		this.divLineAlpha = divLineAlpha;
	}

	public String getDivLineIsDashed() {
		return divLineIsDashed;
	}

	public void setDivLineIsDashed(String divLineIsDashed) {
		this.divLineIsDashed = divLineIsDashed;
	}

	public String getDivLineDashLen() {
		return divLineDashLen;
	}

	public void setDivLineDashLen(String divLineDashLen) {
		this.divLineDashLen = divLineDashLen;
	}

	public String getDivLineDashGap() {
		return divLineDashGap;
	}

	public void setDivLineDashGap(String divLineDashGap) {
		this.divLineDashGap = divLineDashGap;
	}

	public String getZeroPlaneColor() {
		return zeroPlaneColor;
	}

	public void setZeroPlaneColor(String zeroPlaneColor) {
		this.zeroPlaneColor = zeroPlaneColor;
	}

	public String getZeroPlaneThickness() {
		return zeroPlaneThickness;
	}

	public void setZeroPlaneThickness(String zeroPlaneThickness) {
		this.zeroPlaneThickness = zeroPlaneThickness;
	}

	public String getZeroPlaneAlpha() {
		return zeroPlaneAlpha;
	}

	public void setZeroPlaneAlpha(String zeroPlaneAlpha) {
		this.zeroPlaneAlpha = zeroPlaneAlpha;
	}

	public String getShowAlternateHGridColor() {
		return showAlternateHGridColor;
	}

	public void setShowAlternateHGridColor(String showAlternateHGridColor) {
		this.showAlternateHGridColor = showAlternateHGridColor;
	}

	public String getAlternateHGridColor() {
		return alternateHGridColor;
	}

	public void setAlternateHGridColor(String alternateHGridColor) {
		this.alternateHGridColor = alternateHGridColor;
	}

	public String getAlternateHGridAlpha() {
		return alternateHGridAlpha;
	}

	public void setAlternateHGridAlpha(String alternateHGridAlpha) {
		this.alternateHGridAlpha = alternateHGridAlpha;
	}

	public String getShowToolTip() {
		return showToolTip;
	}

	public void setShowToolTip(String showToolTip) {
		this.showToolTip = showToolTip;
	}

	public String getToolTipBgColor() {
		return toolTipBgColor;
	}

	public void setToolTipBgColor(String toolTipBgColor) {
		this.toolTipBgColor = toolTipBgColor;
	}

	public String getToolTipBorderColor() {
		return toolTipBorderColor;
	}

	public void setToolTipBorderColor(String toolTipBorderColor) {
		this.toolTipBorderColor = toolTipBorderColor;
	}

	public String getToolTipSepChar() {
		return toolTipSepChar;
	}

	public void setToolTipSepChar(String toolTipSepChar) {
		this.toolTipSepChar = toolTipSepChar;
	}

	public String getShowToolTipShadow() {
		return showToolTipShadow;
	}

	public void setShowToolTipShadow(String showToolTipShadow) {
		this.showToolTipShadow = showToolTipShadow;
	}

	public String getCaptionPadding() {
		return captionPadding;
	}

	public void setCaptionPadding(String captionPadding) {
		this.captionPadding = captionPadding;
	}

	public String getxAxisNamePadding() {
		return xAxisNamePadding;
	}

	public void setxAxisNamePadding(String xAxisNamePadding) {
		this.xAxisNamePadding = xAxisNamePadding;
	}

	public String getyAxisNamePadding() {
		return yAxisNamePadding;
	}

	public void setyAxisNamePadding(String yAxisNamePadding) {
		this.yAxisNamePadding = yAxisNamePadding;
	}

	public String getyAxisValuesPadding() {
		return yAxisValuesPadding;
	}

	public void setyAxisValuesPadding(String yAxisValuesPadding) {
		this.yAxisValuesPadding = yAxisValuesPadding;
	}

	public String getLabelPadding() {
		return labelPadding;
	}

	public void setLabelPadding(String labelPadding) {
		this.labelPadding = labelPadding;
	}

	public String getValuePadding() {
		return valuePadding;
	}

	public void setValuePadding(String valuePadding) {
		this.valuePadding = valuePadding;
	}

	public String getPlotSpacePercent() {
		return plotSpacePercent;
	}

	public void setPlotSpacePercent(String plotSpacePercent) {
		this.plotSpacePercent = plotSpacePercent;
	}

	public String getChartLeftMargin() {
		return chartLeftMargin;
	}

	public void setChartLeftMargin(String chartLeftMargin) {
		this.chartLeftMargin = chartLeftMargin;
	}

	public String getChartRightMargin() {
		return chartRightMargin;
	}

	public void setChartRightMargin(String chartRightMargin) {
		this.chartRightMargin = chartRightMargin;
	}

	public String getChartTopMargin() {
		return chartTopMargin;
	}

	public void setChartTopMargin(String chartTopMargin) {
		this.chartTopMargin = chartTopMargin;
	}

	public String getChartBottomMargin() {
		return chartBottomMargin;
	}

	public void setChartBottomMargin(String chartBottomMargin) {
		this.chartBottomMargin = chartBottomMargin;
	}

	public String getCanvasLeftMargin() {
		return canvasLeftMargin;
	}

	public void setCanvasLeftMargin(String canvasLeftMargin) {
		this.canvasLeftMargin = canvasLeftMargin;
	}

	public String getCanvasRightMargin() {
		return canvasRightMargin;
	}

	public void setCanvasRightMargin(String canvasRightMargin) {
		this.canvasRightMargin = canvasRightMargin;
	}

	public String getCanvasTopMargin() {
		return canvasTopMargin;
	}

	public void setCanvasTopMargin(String canvasTopMargin) {
		this.canvasTopMargin = canvasTopMargin;
	}

	public String getCanvasBottomMargin() {
		return canvasBottomMargin;
	}

	public void setCanvasBottomMargin(String canvasBottomMargin) {
		this.canvasBottomMargin = canvasBottomMargin;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getShowLabel() {
		return showLabel;
	}

	public void setShowLabel(String showLabel) {
		this.showLabel = showLabel;
	}

	public String getShowValue() {
		return showValue;
	}

	public void setShowValue(String showValue) {
		this.showValue = showValue;
	}

	public String getUseRoundEdges() {
		return useRoundEdges;
	}

	public void setUseRoundEdges(String useRoundEdges) {
		this.useRoundEdges = useRoundEdges;
	}

	public String getShowPlotBorder() {
		return showPlotBorder;
	}

	public void setShowPlotBorder(String showPlotBorder) {
		this.showPlotBorder = showPlotBorder;
	}

	public String getPlotBorderColor() {
		return plotBorderColor;
	}

	public void setPlotBorderColor(String plotBorderColor) {
		this.plotBorderColor = plotBorderColor;
	}

	public String getPlotBorderThickness() {
		return plotBorderThickness;
	}

	public void setPlotBorderThickness(String plotBorderThickness) {
		this.plotBorderThickness = plotBorderThickness;
	}

	public String getPlotBorderAlpha() {
		return plotBorderAlpha;
	}

	public void setPlotBorderAlpha(String plotBorderAlpha) {
		this.plotBorderAlpha = plotBorderAlpha;
	}

	public String getPlotBorderDashed() {
		return plotBorderDashed;
	}

	public void setPlotBorderDashed(String plotBorderDashed) {
		this.plotBorderDashed = plotBorderDashed;
	}

	public String getPlotBorderDashLen() {
		return plotBorderDashLen;
	}

	public void setPlotBorderDashLen(String plotBorderDashLen) {
		this.plotBorderDashLen = plotBorderDashLen;
	}

	public String getPlotBorderDashGap() {
		return plotBorderDashGap;
	}

	public void setPlotBorderDashGap(String plotBorderDashGap) {
		this.plotBorderDashGap = plotBorderDashGap;
	}

	public String getPlotFillAngle() {
		return plotFillAngle;
	}

	public void setPlotFillAngle(String plotFillAngle) {
		this.plotFillAngle = plotFillAngle;
	}

	public String getPlotFillRatio() {
		return plotFillRatio;
	}

	public void setPlotFillRatio(String plotFillRatio) {
		this.plotFillRatio = plotFillRatio;
	}

	public String getPlotFillAlpha() {
		return plotFillAlpha;
	}

	public void setPlotFillAlpha(String plotFillAlpha) {
		this.plotFillAlpha = plotFillAlpha;
	}

	public String getPlotGradientColor() {
		return plotGradientColor;
	}

	public void setPlotGradientColor(String plotGradientColor) {
		this.plotGradientColor = plotGradientColor;
	}

	public String getFormatNumber() {
		return formatNumber;
	}

	public void setFormatNumber(String formatNumber) {
		this.formatNumber = formatNumber;
	}

	public String getFormatNumberScale() {
		return formatNumberScale;
	}

	public void setFormatNumberScale(String formatNumberScale) {
		this.formatNumberScale = formatNumberScale;
	}

	public String getDefaultNumberScale() {
		return defaultNumberScale;
	}

	public void setDefaultNumberScale(String defaultNumberScale) {
		this.defaultNumberScale = defaultNumberScale;
	}

	public String getNumberScaleUnit() {
		return numberScaleUnit;
	}

	public void setNumberScaleUnit(String numberScaleUnit) {
		this.numberScaleUnit = numberScaleUnit;
	}

	public String getNumberScaleValue() {
		return numberScaleValue;
	}

	public void setNumberScaleValue(String numberScaleValue) {
		this.numberScaleValue = numberScaleValue;
	}

	public String getNumberPrefix() {
		return numberPrefix;
	}

	public void setNumberPrefix(String numberPrefix) {
		this.numberPrefix = numberPrefix;
	}

	public String getNumberSuffix() {
		return numberSuffix;
	}

	public void setNumberSuffix(String numberSuffix) {
		this.numberSuffix = numberSuffix;
	}

	public String getDecimalSeparator() {
		return decimalSeparator;
	}

	public void setDecimalSeparator(String decimalSeparator) {
		this.decimalSeparator = decimalSeparator;
	}

	public String getThousandSeparator() {
		return thousandSeparator;
	}

	public void setThousandSeparator(String thousandSeparator) {
		this.thousandSeparator = thousandSeparator;
	}

	public String getInDecimalSeparator() {
		return inDecimalSeparator;
	}

	public void setInDecimalSeparator(String inDecimalSeparator) {
		this.inDecimalSeparator = inDecimalSeparator;
	}

	public String getInThousandSeparator() {
		return inThousandSeparator;
	}

	public void setInThousandSeparator(String inThousandSeparator) {
		this.inThousandSeparator = inThousandSeparator;
	}

	public String getDecimals() {
		return decimals;
	}

	public void setDecimals(String decimals) {
		this.decimals = decimals;
	}

	public String getForceDecimals() {
		return forceDecimals;
	}

	public void setForceDecimals(String forceDecimals) {
		this.forceDecimals = forceDecimals;
	}

	public String getyAxisValueDecimals() {
		return yAxisValueDecimals;
	}

	public void setyAxisValueDecimals(String yAxisValueDecimals) {
		this.yAxisValueDecimals = yAxisValueDecimals;
	}

	public String getBaseFont() {
		return baseFont;
	}

	public void setBaseFont(String baseFont) {
		this.baseFont = baseFont;
	}

	public String getBaseFontSize() {
		return baseFontSize;
	}

	public void setBaseFontSize(String baseFontSize) {
		this.baseFontSize = baseFontSize;
	}

	public String getBaseFontColor() {
		return baseFontColor;
	}

	public void setBaseFontColor(String baseFontColor) {
		this.baseFontColor = baseFontColor;
	}

	public String getOutCnvBaseFont() {
		return outCnvBaseFont;
	}

	public void setOutCnvBaseFont(String outCnvBaseFont) {
		this.outCnvBaseFont = outCnvBaseFont;
	}

	public String getOutCnvBaseFontSize() {
		return outCnvBaseFontSize;
	}

	public void setOutCnvBaseFontSize(String outCnvBaseFontSize) {
		this.outCnvBaseFontSize = outCnvBaseFontSize;
	}

	public String getOutCnvBaseFontColor() {
		return outCnvBaseFontColor;
	}

	public void setOutCnvBaseFontColor(String outCnvBaseFontColor) {
		this.outCnvBaseFontColor = outCnvBaseFontColor;
	}

	public String getShowLabelBorder() {
		return showLabelBorder;
	}

	public void setShowLabelBorder(String showLabelBorder) {
		this.showLabelBorder = showLabelBorder;
	}

	public String getLinePosition() {
		return linePosition;
	}

	public void setLinePosition(String linePosition) {
		this.linePosition = linePosition;
	}

	public String getLabelPosition() {
		return labelPosition;
	}

	public void setLabelPosition(String labelPosition) {
		this.labelPosition = labelPosition;
	}

	public String getLabelHAlign() {
		return labelHAlign;
	}

	public void setLabelHAlign(String labelHAlign) {
		this.labelHAlign = labelHAlign;
	}

	public String getLabelVAlign() {
		return labelVAlign;
	}

	public void setLabelVAlign(String labelVAlign) {
		this.labelVAlign = labelVAlign;
	}

	public String getStartValue() {
		return startValue;
	}

	public void setStartValue(String startValue) {
		this.startValue = startValue;
	}

	public String getEndValue() {
		return endValue;
	}

	public void setEndValue(String endValue) {
		this.endValue = endValue;
	}

	public String getIsTrendZone() {
		return isTrendZone;
	}

	public void setIsTrendZone(String isTrendZone) {
		this.isTrendZone = isTrendZone;
	}

	public String getShowOnTop() {
		return showOnTop;
	}

	public void setShowOnTop(String showOnTop) {
		this.showOnTop = showOnTop;
	}

	public String getValueOnRight() {
		return valueOnRight;
	}

	public void setValueOnRight(String valueOnRight) {
		this.valueOnRight = valueOnRight;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getThickness() {
		return thickness;
	}

	public void setThickness(String thickness) {
		this.thickness = thickness;
	}

	public String getAlpha() {
		return alpha;
	}

	public void setAlpha(String alpha) {
		this.alpha = alpha;
	}

	public String getDashed() {
		return dashed;
	}

	public void setDashed(String dashed) {
		this.dashed = dashed;
	}

	public String getDashLen() {
		return dashLen;
	}

	public void setDashLen(String dashLen) {
		this.dashLen = dashLen;
	}

	public String getDashGap() {
		return dashGap;
	}

	public void setDashGap(String dashGap) {
		this.dashGap = dashGap;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDisplayValue() {
		return displayValue;
	}

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}

	public String getToolText() {
		return toolText;
	}

	public void setToolText(String toolText) {
		this.toolText = toolText;
	}
}
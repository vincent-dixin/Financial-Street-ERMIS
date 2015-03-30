package com.fhd.comm.business.formula;

import org.apache.commons.math.stat.StatUtils;
import org.apache.commons.math.stat.descriptive.moment.Kurtosis;
import org.apache.commons.math.stat.descriptive.moment.Skewness;
import org.apache.commons.math.stat.descriptive.moment.Variance;
import org.apache.commons.math.stat.descriptive.summary.Sum;

/**
 * 此类是对一些常用统计方法的封装，有如下处理原则；（为了与其它运算整合时的算法更简单）
 * 参数是两个数组的方法：只传入一个偶数长度的数组，在方法内部split成两个数组
 * 参数是一个数组与一个数的方法：只传入一个数组，数组的最后一个值为那个单个的参数；
 * 参数只是一个数组：传入一下数组； 
 * @author 吴德福
 * @version  
 * @since    Ver 1.1
 * @Date	 2012-11-9
 */
public class StatisticFunctionCalculateBO {
	
	/**
	 * 最大值计算.
	 * @author 吴德福
	 * @param params
	 * @return double
	 */
	public static double max(double[] params){
		return StatUtils.max(params);
	}
	/**
	 * 最小值计算.
	 * @author 吴德福
	 * @param params
	 * @return double
	 */
	public static double min(double[] params){
		return StatUtils.min(params);
	}
	/**
	 * 移动平均.
	 * @author 吴德福
	 * @param params
	 * @return double
	 */
	public static double ma(double[] params){
		Sum sum = new Sum();
		double result=sum.evaluate(params);
		return result/params.length;
	}
	/**
	 * 方差.
	 * @author 吴德福
	 * @param params
	 * @return double
	 */
	public static double var(double[] params){
		return StatUtils.variance(params);
	}
	/**
	 * 标准差.
	 * @author 吴德福
	 * @param params
	 * @return double
	 */
	public static double dtdev(double[] params){
		Variance variance = new Variance();
		return Math.sqrt(variance.evaluate(params));
	}
	/**
	 * 标准正态累积分布函数.
	 * @param x
	 * @return double
	 */
	public static double fai(double x) {
		if (x < -3.9) {
			return 0;
		} else if (x > 3.9) {
			return 1;
		}
		double f = 0;
		double pc = -5;
		double step = 0.00001;
		for (double i = pc; i < x; i += step)
			f += fx(i) * step;
		return f;
	}
	/**
     * 标准正态概率密度函数.
     * @param x
     * @return double
     */
    private static double fx(double x){
        double ret = 0.0;
        double a = 1.0 / Math.sqrt(Math.PI * 2);
        ret  = a * Math.pow(Math.E, -0.5 * Math.pow(x, 2));
        return ret;
    }
	/**
	 * 偏度.
	 * @author 吴德福
	 * @param params
	 * @return double
	 */
	public static double skeness(double[] params){
		Skewness skewness = new Skewness();
		return skewness.evaluate(params);
	}
	/**
	 * 峰度.
	 * @author 吴德福
	 * @param params
	 * @return double
	 */
	public static double kurt(double[] params){
		Kurtosis kurtosis = new Kurtosis();
		return kurtosis.evaluate(params);
	}
	/**
	 * 协方差计算.
	 * @author 吴德福
	 * @param source
	 * @return double
	 * @throws Exception 
	 */
	public static  double cov(double[] source) throws Exception{
		double[] x=new double[source.length/2];
		double[] y=new double[source.length/2];
		StatisticFunctionCalculateBO.splitArray(source, x, y);
		Sum sum = new Sum();
		double avgX = sum.evaluate(x)/x.length;
		double avgY = sum.evaluate(y)/y.length;
		double total = 0;
		for(int i=0;i<x.length;i++){
			total += ((x[i]-avgX)*(y[i]-avgY));
		}
		return total/x.length;
	}
	/**
	 * 波动率计算.
	 * 参数数组的最后一个值为T值.
	 * @author 吴德福
	 * @param source
	 * @return double
	 */
	public static double vol(double[] source){
		double[] params=new double[source.length-1];
		for(int i=0;i<params.length;i++){
			params[i]=source[i];
		}
		int t=new Double(source[source.length-1]).intValue();
		Variance variance=new Variance();
		double result=Math.sqrt(t)*Math.sqrt(variance.evaluate(params));
		return result;
	}
	/**
	 * 同比与环比-变动率.
	 * @author 吴德福
	 * @param source:source[0]为本期值；source[1]为与之比较的值；
	 * @return double
	 * @throws Exception 
	 */
	public static double chr(double[] source) throws Exception{
		if(source.length!=2){
			throw new Exception("变动率的参数必须是两个值!");
		}
		/*
		if(0 == source[1]){
			throw new Exception("除数为'0'，不能进行计算!");
		}
		*/
		return source[0]/source[1]-1;
	}
	/**
	 * 皮尔逊系数
	 * @author 吴德福
	 * @param source
	 * @return double
	 * @throws Exception 
	 */
	public static double corr(double[] source) throws Exception{
		double[] x=new double[source.length/2];
		double[] y=new double[source.length/2];
		StatisticFunctionCalculateBO.splitArray(source, x, y);
		double cov=StatisticFunctionCalculateBO.cov(source);
		double varX=StatisticFunctionCalculateBO.var(x);
		double varY=StatisticFunctionCalculateBO.var(y);
		return cov/(Math.sqrt(varX*varY));
	}
	/**
	 * 拆分数组.
	 * @author 吴德福
	 * @param source
	 * @param x
	 * @param y
	 * @throws Exception
	 */
	public static void splitArray(double[] source, double[] x, double[] y) throws Exception{
		int length=source.length;
		if(length%2!=0){
			throw new Exception("参数个数不合法");
		}
		for(int i=0;i<length;i++){
			if(i<=length/2-1){
				x[i]=source[i];
			}else{
				y[i-length/2]=source[i];
			}
		}
	}
}

package com.jf.common.util;  

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * 
 * Title:  NumberUtil<br>
 * Description: 数字工具类<br>
 * @author sw
 * @Modified by 
 * @CreateDate 2016年6月4日下午2:12:01
 * @Version 
 * @Revision 
 * @ModifiedDate 
 * @since JDK 1.7
 */
public class NumberUtil {
	/**
	 * 默认保留有效位数
	 */
	 private static final int DEF_DIV_SCALE = 10;  
     
	 /**
	  * 
	  * Title:  doubleAdd<br>
	  * Description: 两个Double数相加<br>
	  * @param v1 double参数
	  * @param v2 double参数
	  * @return Double
	  *	
	  * @author sw
	  * @Modified by 
	  * @CreateDate 2016年6月4日 下午2:12:46
	  * @Version 
	  * @since JDK 1.7
	  */
	 public static Double doubleAdd(String v1, String v2) {  
		 BigDecimal b1 = new BigDecimal(v1.toString());  
		 BigDecimal b2 = new BigDecimal(v2.toString());  
		 return new Double(b1.add(b2).doubleValue());  
	 }
     /**
      * 
      * Title:  doubleAdd<br>
      * Description: 两个Double数相加<br>
      * @param v1 double参数
      * @param v2 double参数
      * @return Double
      *	
      * @author sw
      * @Modified by 
      * @CreateDate 2016年6月4日 下午2:12:46
      * @Version 
      * @since JDK 1.7
      */
     public static Double doubleAdd(Double v1, Double v2) {  
        BigDecimal b1 = new BigDecimal(v1.toString());  
        BigDecimal b2 = new BigDecimal(v2.toString());  
        return new Double(b1.add(b2).doubleValue());  
     }
     /**
      * 
      * Title:  doubleAdds<br>
      * Description: 多个Double数相加(可变参数)<br>
      * @param doubles Double参数数组
      * @return
      *	
      * @author sw
      * @Modified by 
      * @CreateDate 2016年6月4日 下午3:21:17
      * @Version 
      * @since JDK 1.7
      */
     public static String doubleAdds(String...doubles) {
    	 String v1 = "0.00"; //初始化数值
    	 for(int i=0;i<doubles.length;i++){
    		 String v2 = doubles[i];  
    		 if(v2!=null && v2.length()>0){
    			 v1 = String.valueOf(doubleAdd(v1, v2));
    		 }
    	 }
    	 return v1;  
     }
     /**
      * 
      * Title:  doubleAdds<br>
      * Description: 多个Double数相加(可变参数)<br>
      * @param doubles Double参数数组
      * @return
      *	
      * @author sw
      * @Modified by 
      * @CreateDate 2016年6月4日 下午3:21:17
      * @Version 
      * @since JDK 1.7
      */
     public static Double doubleAdds(Double...doubles) {
    	 Double v1 = 0.00; //初始化数值
    	 for(int i=0;i<doubles.length;i++){
    		 Double v2 = doubles[i];  
    		 v1 = doubleAdd(v1, v2);
    	 }
         return v1;  
      }
       
     /**
      * 
      * Title:  doubleSub<br>
      * Description: 两个Double数相减<br>
      * @param v1 double参数
      * @param v2 double参数
      * @return Double
      *	
      * @author sw
      * @Modified by 
      * @CreateDate 2016年6月4日 下午2:12:46
      * @Version 
      * @since JDK 1.7
      */
     public static Double doubleSub(String v1, String v2) {  
    	 BigDecimal b1 = new BigDecimal(v1.toString());  
    	 BigDecimal b2 = new BigDecimal(v2.toString());  
    	 return new Double(b1.subtract(b2).doubleValue());  
     }  
     /**
      * 
      * Title:  doubleSub<br>
      * Description: 两个Double数相减<br>
      * @param v1 double参数
      * @param v2 double参数
      * @return Double
      *	
      * @author sw
      * @Modified by 
      * @CreateDate 2016年6月4日 下午2:12:46
      * @Version 
      * @since JDK 1.7
      */
     public static Double doubleSub(Double v1, Double v2) {  
        BigDecimal b1 = new BigDecimal(v1.toString());  
        BigDecimal b2 = new BigDecimal(v2.toString());  
        return new Double(b1.subtract(b2).doubleValue());  
     }  
       
     /**
      * 
      * Title:  doubleMul<br>
      * Description: 两个Double数相乘<br>
      * @param v1 double参数
      * @param v2 double参数
      * @return Double
      *	
      * @author sw
      * @Modified by 
      * @CreateDate 2016年6月4日 下午2:12:46
      * @Version 
      * @since JDK 1.7
      */
     public static Double doubleMul(String v1, String v2) {  
    	 BigDecimal b1 = new BigDecimal(v1.toString());  
    	 BigDecimal b2 = new BigDecimal(v2.toString());  
    	 return new Double(b1.multiply(b2).doubleValue());  
     }  
     /**
      * 
      * Title:  doubleMul<br>
      * Description: 两个Double数相乘<br>
      * @param v1 double参数
      * @param v2 double参数
      * @return Double
      *	
      * @author sw
      * @Modified by 
      * @CreateDate 2016年6月4日 下午2:12:46
      * @Version 
      * @since JDK 1.7
      */
     public static Double doubleMul(Double v1, Double v2) {  
        BigDecimal b1 = new BigDecimal(v1.toString());  
        BigDecimal b2 = new BigDecimal(v2.toString());  
        return new Double(b1.multiply(b2).doubleValue());  
     }  
       
     
     /**
      * 
      * Title:  doubleDiv<br>
      * Description: 两个Double数相除(实际值)<br>
      * @param v1 double参数(分子)
      * @param v2 double参数(分母)
      * @return Double
      *	
      * @author sw
      * @Modified by 
      * @CreateDate 2016年6月4日 下午2:12:46
      * @Version 
      * @since JDK 1.7
      */
     public static Double doubleDiv(String v1, String v2) {  
    	 BigDecimal b1 = new BigDecimal(v1.toString());  
    	 BigDecimal b2 = new BigDecimal(v2.toString());  
    	 return new Double(b1.divide(b2, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP)  
    			 .doubleValue());  
     }  
     /**
      * 
      * Title:  doubleDiv<br>
      * Description: 两个Double数相除(实际值)<br>
      * @param v1 double参数(分子)
      * @param v2 double参数(分母)
      * @return Double
      *	
      * @author sw
      * @Modified by 
      * @CreateDate 2016年6月4日 下午2:12:46
      * @Version 
      * @since JDK 1.7
      */
     public static Double doubleDiv(Double v1, Double v2) {  
        BigDecimal b1 = new BigDecimal(v1.toString());  
        BigDecimal b2 = new BigDecimal(v2.toString());  
        return new Double(b1.divide(b2, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP)  
          .doubleValue());  
     }  
       
     /**
      * 
      * Title:  doubleDiv<br>
      * Description:  两个Double数相除，并保留scale位小数<br>
      * @param v1 double参数(分子)
      * @param v2 double参数(分母)
      * @param scase 保留有效数字个数
      * @return Double
      *	
      * @author sw
      * @Modified by 
      * @CreateDate 2016年6月4日 下午2:12:46
      * @Version 
      * @since JDK 1.7
      */
     public static Double doubleDiv(String v1, String v2, int scale) {  
    	 if (scale < 0) {  
    		 throw new IllegalArgumentException(  
    				 "The scale must be a positive integer or zero");  
    	 }  
    	 BigDecimal b1 = new BigDecimal(v1);  
    	 BigDecimal b2 = new BigDecimal(v2);  
    	 return new Double(b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue());  
     } 
     /**
      * 
      * Title:  doubleDiv<br>
      * Description:  两个Double数相除，并保留scale位小数<br>
      * @param v1 double参数(分子)
      * @param v2 double参数(分母)
      * @param scase 保留有效数字个数
      * @return Double
      *	
      * @author sw
      * @Modified by 
      * @CreateDate 2016年6月4日 下午2:12:46
      * @Version 
      * @since JDK 1.7
      */
     public static Double doubleDiv(Double v1, Double v2, int scale) {  
        if (scale < 0) {  
         throw new IllegalArgumentException(  
           "The scale must be a positive integer or zero");  
        }  
        BigDecimal b1 = new BigDecimal(v1.toString());  
        BigDecimal b2 = new BigDecimal(v2.toString());  
        return new Double(b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue());  
     } 
     /**
 	 * 
 	 * Title:  isNumeric<br>
 	 * Description: 正则表达式判断是否为数字字符串<br>
 	 * @param str
 	 * @return
 	 *	
 	 * @author sw
 	 * @Modified by 
 	 * @CreateDate 2016年6月4日 下午2:07:52
 	 * @Version 
 	 * @since JDK 1.7
 	 */
 	public static boolean isNumeric(String str){
 		//字符串不能为空
 		if(str==null){
 			return false;
 		}
 	    Pattern pattern = Pattern.compile("^[+-]?\\d+(\\.\\d+)?$"); 
 	    return pattern.matcher(str).matches();    
 	 } 
 	/**
 	 * 
 	 * Title:  Compare<br>
 	 * Description: 比较两个Double值的大小<br>
 	 * @param v1 double参数
 	 * @param v2 double参数
 	 * @return true：v1>v2;false:v1小于等于v2
 	 *	
 	 * @author sw
 	 * @Modified by 
 	 * @CreateDate 2016年6月4日 下午2:38:14
 	 * @Version 
 	 * @since JDK 1.7
 	 */
 	public static boolean Compare(String v1,String v2){
 		BigDecimal b1 = new BigDecimal(v1);
 		BigDecimal b2 = new BigDecimal(v2);
 		if(b1.compareTo(b2)>0){
 			return true;
 		}
 		return false;
 	}
 	/**
 	 * 
 	 * Title:  Compare<br>
 	 * Description: 比较两个Double值的大小<br>
 	 * @param v1 double参数
 	 * @param v2 double参数
 	 * @return 1：v1>v2;-1:v1小于v2;0-v1等于v2
 	 *	
 	 * @author sw
 	 * @Modified by 
 	 * @CreateDate 2016年6月4日 下午2:38:14
 	 * @Version 
 	 * @since JDK 1.7
 	 */
 	public static int CompareStr(String v1,String v2){
 		BigDecimal b1 = new BigDecimal(v1);
 		BigDecimal b2 = new BigDecimal(v2);
 		if(b1.compareTo(b2)>0){
 			return 1;
 		}else if(b1.compareTo(b2)<0){
 			return -1;
 		}
 		return 0;
 	}
 	/**
 	 * 
 	 * Title:  Compare<br>
 	 * Description: 比较两个Double值的大小<br>
 	 * @param v1 double参数
 	 * @param v2 double参数
 	 * @return true：v1>v2;false:v1小于等于v2
 	 *	
 	 * @author sw
 	 * @Modified by 
 	 * @CreateDate 2016年6月4日 下午2:38:14
 	 * @Version 
 	 * @since JDK 1.7
 	 */
 	public static boolean Compare(Double v1,Double v2){
 		BigDecimal b1 = new BigDecimal(v1.toString());
 		BigDecimal b2 = new BigDecimal(v2.toString());
 		if(b1.compareTo(b2)>0){
 			return true;
 		}
 		return false;
 	}
 	/**
 	 * 
 	 * Title:  CompareGE<br>
 	 * Description: 比较两个Double值的大小<br>
 	 * @param v1 double参数
 	 * @param v2 double参数
 	 * @return true：v1>=v2;false:v1小于v2
 	 *	
 	 * @author sw
 	 * @Modified by 
 	 * @CreateDate 2016年6月4日 下午2:38:14
 	 * @Version 
 	 * @since JDK 1.7
 	 */
 	public static boolean CompareGE(Double v1,Double v2){
 		BigDecimal b1 = new BigDecimal(v1.toString());
 		BigDecimal b2 = new BigDecimal(v2.toString());
 		if(b1.compareTo(b2)>=0){
 			return true;
 		}
 		return false;
 	}
 	/**
 	 * 
 	 * Title:  CompareGE<br>
 	 * Description: 比较两个Double值的大小<br>
 	 * @param v1 double参数
 	 * @param v2 double参数
 	 * @return true：v1>=v2;false:v1小于v2
 	 *	
 	 * @author sw
 	 * @Modified by 
 	 * @CreateDate 2016年6月4日 下午2:38:14
 	 * @Version 
 	 * @since JDK 1.7
 	 */
 	public static boolean CompareGE(String v1,String v2){
 		BigDecimal b1 = new BigDecimal(v1);
 		BigDecimal b2 = new BigDecimal(v2);
 		if(b1.compareTo(b2)>=0){
 			return true;
 		}
 		return false;
 	}
 	/**
 	 * 
 	 * Title:  getFloor<br>
 	 * Description: 向下取整<br>
 	 * @param v1
 	 * @return
 	 *	
 	 * @author sw
 	 * @Modified by 
 	 * @CreateDate 2016年6月12日 上午9:26:27
 	 * @Version 
 	 * @since JDK 1.7
 	 */
 	public static int getFloor(Double v1){
 		return (int) Math.floor(v1);
 	}
 	/**
 	 * 
 	 * Title:  getCeil<br>
 	 * Description: 向上取整<br>
 	 * @param v1
 	 * @return
 	 *	
 	 * @author sw
 	 * @Modified by 
 	 * @CreateDate 2016年6月12日 上午9:26:27
 	 * @Version 
 	 * @since JDK 1.7
 	 */
 	public static int getCeil(Double v1){
 		return (int) Math.ceil(v1);
 	}
 	/**
 	 * 
 	 * Title:  getRInt<br>
 	 * Description: 四舍五入<br>
 	 * @param v1
 	 * @return
 	 *	
 	 * @author sw
 	 * @Modified by 
 	 * @CreateDate 2016年6月12日 上午9:26:27
 	 * @Version 
 	 * @since JDK 1.7
 	 */
 	public static int getRInt(Double v1){
 		return (int) Math.rint(v1);
 	}
}

package com.github.wulf;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

public class MethodUtil {
	 private static final String REFLACT_OP_ERROR="反射操作异常";
	 private static final String NO_SUCH_METHOD="未找到该类型的方法";
	 private static final String LINE_BREAK=System.getProperty("line.separator","\n");

    private static void checkMethodA(String[] pMethodNames){
        if(pMethodNames==null||pMethodNames.length==0)
            throw new IllegalArgumentException("至少需要一个方法名");
    }


    /**
     * 获取方法,无视参数
     * 
     * @param pClazz
     *            类
     * @param pMethodName
     *            方法名
     * @param pDeclared
     *            是否只检索该类定义的方法而不检索父类的方法
     * @return 符合的方法,非空
     */
    public static ArrayList<Method> getMethodIgnoreParam(Class<?> pClazz,String pMethodName,boolean pDeclared){
        ArrayList<Method> tMethods=new ArrayList<>();
        do{
            for(Method sMethod : pClazz.getDeclaredMethods()){
                if(sMethod.getName().equals(pMethodName)){
                    tMethods.add(sMethod);
                }

            }
        }while(!pDeclared&&(pClazz=pClazz.getSuperclass())!=null);

        if(!tMethods.isEmpty()){
            return tMethods;
        }

        throw new IllegalStateException(NO_SUCH_METHOD+LINE_BREAK
                +"\t类: "+pClazz.getName()+LINE_BREAK
                +"\t方法名: "+pMethodName,new NoSuchMethodException());
    }

    
    /**
     * 获取无参方法
     * 
     * @param pClazz
     *            类
     * @param pMethodName
     *            方法名
     * @param pDeclared
     *            是否只检索该类定义的方法而不检索父类的方法
     * @return 方法
     * @throws IllegalStateException
     *             没有匹配的方法
     */
    public static Method getMethod(Class<?> pClazz,String pMethodName,boolean pDeclared){
        return MethodUtil.getMethod(pClazz,pMethodName,new Class<?>[0],pDeclared);
    }

    /**
     * 获取方法
     * 
     * @param pClazz
     *            类
     * @param pMethodName
     *            方法名
     * @param pParamsTypes
     *            参数类型
     * @param pDeclared
     *            是否只检索该类定义的方法而不检索父类的方法
     * @return 方法
     * @throws IllegalStateException
     *             没有匹配的方法
     */
    public static Method getMethod(Class<?> pClazz,String pMethodName,Class<?>[] pParamsTypes,boolean pDeclared){
        do{
            for(Method sMethod : pClazz.getDeclaredMethods()){
                if(sMethod.getName().equals(pMethodName)&&Arrays.equals(sMethod.getParameterTypes(),pParamsTypes)){
                    return sMethod;
                }
            }
        }while(!pDeclared&&(pClazz=pClazz.getSuperclass())!=null);

        throw new IllegalStateException(NO_SUCH_METHOD+LINE_BREAK
                +"\t类: "+pClazz.getName()+LINE_BREAK
                +"\t方法名: "+pMethodName+LINE_BREAK
                +"\t方法参数: "+Arrays.toString(pParamsTypes),new NoSuchMethodException());
    }
    
    /**
     * 执行方法,并返回结果
     * 
     * @param pObj
     *            要执行方法的实例,如果方法为静态,可以为null
     * @param pMethod
     *            方法
     * @param pParams
     *            参数
     * @return 方法返回值
     */
    public static Object invokeMethod(Method pMethod,Object pObj,Object...pParams){
        try{
            pMethod.setAccessible(true);
            return pMethod.invoke(pObj,pParams);
        }catch(IllegalAccessException|IllegalArgumentException|InvocationTargetException|SecurityException exp){
            throw new IllegalStateException(REFLACT_OP_ERROR,exp);
        }
    }

}

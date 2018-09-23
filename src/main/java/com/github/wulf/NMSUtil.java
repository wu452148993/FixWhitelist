package com.github.wulf;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.Server;

public class NMSUtil {
	
	public static String mTestVersion="v1_13_R1";
    /**
     * MC版本,请勿直接获取,请使用{@link ClassUtil#getServerVersion()}来获取
     */
    @Deprecated
    private static String mMCVersion;

    private static final String REFLACT_OP_ERROR="反射操作异常";
    /**
     * NMS类
     */
	public static final Class<?> clazz_PlayerList;
	public static final Class<?> clazz_CraftServer;
	public static final Class<?> clazz_WhiteListEntry;
	public static final Class<?> clazz_JsonList;
	
	public static final Method method_CraftServer_getHandle;
	public static final Method  method_PlayerList_getWhitelist;
	public static final Method  method_JsonList_add;
	
	static{
		clazz_PlayerList=NMSUtil.getNMSClass("PlayerList");
		clazz_WhiteListEntry=NMSUtil.getNMSClass("WhiteListEntry");
		clazz_CraftServer=NMSUtil.getCBTClass("CraftServer");
		clazz_JsonList=NMSUtil.getNMSClass("JsonList");
		
		method_CraftServer_getHandle=MethodUtil.getMethod(clazz_CraftServer,"getHandle",true);
		method_PlayerList_getWhitelist=MethodUtil.getMethod(clazz_PlayerList,"getWhitelist",true);
		method_JsonList_add=MethodUtil.getMethodIgnoreParam(clazz_JsonList,"add",true).get(0);
	}
	
	   /**
     * 获取NMS类的全名
     * 
     * @param pName
     *            短名字
     * @return 完整名字
     */
    public static String getNMSName(String pName){
        return "net.minecraft.server."+NMSUtil.getServerVersion()+"."+pName;
    }
    

    /**
     * 获取org.bukkit.craftbukkit类的全名
     * 
     * @param pName
     *            短名字
     * @return 完整名字
     */
    public static String getCBTName(String pName){
        return "org.bukkit.craftbukkit."+NMSUtil.getServerVersion()+"."+pName;
    }

    
    /**
     * 获取NMS类的{@link Class}对象
     * 
     * @param pClazz
     *            NMS类短名字
     */
    public static Class<?> getNMSClass(String pClazz){
        return NMSUtil.getClass(getNMSName(pClazz));
    }

    
    /**
     * 获取craftbukkit类的{@link Class}对象
     * 
     * @param pClazz
     *            craftbukkit类短名字,(org.bukkit.craftbukkit.version后的名字)
     */
    public static Class<?> getCBTClass(String pClazz){
        return NMSUtil.getClass(getCBTName(pClazz));
    }
    
    /**
     * 获取类
     * 
     * @param pClazz
     *            类全限定名
     * @return 类
     */
    public static Class<?> getClass(String pClazz){
        try{
            return Class.forName(pClazz);
        }catch(ClassNotFoundException exp){
            throw new IllegalStateException(REFLACT_OP_ERROR,exp);
        }
    }

    
    /**
     * 获取服务的Bukkit版本
     */
    public static String getServerVersion(){
        if(mMCVersion==null){
            if(Bukkit.getServer()!=null){
                String className=Bukkit.getServer().getClass().getPackage().getName();
                mMCVersion=className.substring(className.lastIndexOf('.')+1);
            }else mMCVersion=mTestVersion;
        }
        return mMCVersion;
    }
    
    /**
     * 获取NMS的server的PlayerList
     * 
     * @param pServer
     *            Bukkit的server实例
     * @return NMS的server的PlayerList实例,类型一般为PlayerList
     */
    public static Object getNMSPlayerList(Server pServer){
        if(pServer!=null&&NMSUtil.method_CraftServer_getHandle.getDeclaringClass().isInstance(pServer)){
            return MethodUtil.invokeMethod(NMSUtil.method_CraftServer_getHandle,pServer);
        }
        return null;
    }
    
    /**
     * 使用单参构造函数实例化类
     * 
     * @param pClazz
     *            类
     * @param pParamType
     *            参数类型
     * @param pParam
     *            参数
     * @return 实例
     */
    public static <T> T newInstance(Class<? extends T> pClazz,Class<?> pParamType,Object pParam){
        return NMSUtil.newInstance(pClazz,new Class<?>[]{pParamType},new Object[]{pParam});
    }

    /**
     * 实例化类
     * 
     * @param pClazz
     *            类
     * @param pParamTypes
     *            参数类型
     * @param pParams
     *            参数
     * @return 实例
     */
    public static <T> T newInstance(Class<? extends T> pClazz,Class<?>[] pParamTypes,Object[] pParams){
        try{
            Constructor<? extends T> tcons;
            if(pParamTypes==null||pParamTypes.length==0)
                tcons=pClazz.getDeclaredConstructor();
            else tcons=pClazz.getDeclaredConstructor(pParamTypes);
            tcons.setAccessible(true);
            return tcons.newInstance(pParams);
        }catch(NoSuchMethodException|SecurityException|InstantiationException|IllegalAccessException|IllegalArgumentException|InvocationTargetException exp){
            throw new IllegalStateException(REFLACT_OP_ERROR,exp);
        }
    }

}

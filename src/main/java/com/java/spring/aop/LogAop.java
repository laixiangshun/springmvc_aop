package com.java.spring.aop;

import java.lang.reflect.Method;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.java.spring.annotation.Log;
import com.java.spring.annotation.ServiceLog;
import com.java.spring.entity.User;

/**
 * 定义aop切面
 * @author lailai
 *
 */
@Aspect //标明这是一个切面类
@Component //注入到IOC容器中去
public class LogAop {

	ThreadLocal<Long> time=new ThreadLocal<>();
	ThreadLocal<String> tag=new ThreadLocal<>();
	
	/**
	 * 在所有使用注解Log的地方切入
	 */
	//controller层的层切点
	@Pointcut("@annotation(com.java.spring.annotation.Log)")
	public void log(){
		System.out.println("我是一个切入点");
	}
	//service层的层切点
	@Pointcut("@annotation(com.java.spring.annotation.ServiceLog)")
	public void serviceAspect(){}
	
	//在controller核心方法执行前执行
	@Before("log()")
	public void beforeExec(JoinPoint joinPoint)throws Exception{
		time.set(System.currentTimeMillis());
		tag.set(UUID.randomUUID().toString());
		info(joinPoint);
		MethodSignature ms=(MethodSignature)joinPoint.getSignature();
		Method m=ms.getMethod();
		//System.out.println(m.getAnnotation(Log.class).name()+"\t标志"+tag.get());
		System.out.println(getControllerMethodDesciption(joinPoint)+"\t标志"+tag.get());
	}
	//service层的前置通知
	@Before("serviceAspect()")
	public void beforeExecService(JoinPoint joinPoint)throws Exception{
		time.set(System.currentTimeMillis());
		tag.set(UUID.randomUUID().toString());
		info(joinPoint);
		MethodSignature ms=(MethodSignature)joinPoint.getSignature();
		Method m=ms.getMethod();
		//System.out.println(m.getAnnotation(Log.class).name()+"\t标志"+tag.get());
		HttpServletRequest request=((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session=request.getSession();
		User user=(User)session.getAttribute("user");
		String ip=request.getRemoteAddr();//ip
		String params="";
		//将参数转换为json格式字符串
		if(joinPoint.getArgs()!=null && joinPoint.getArgs().length>0){
			for(int i=0;i<joinPoint.getArgs().length;i++){
				params+=JSON.toJSONString(joinPoint.getArgs()[i])+";";
			}
		}
		System.out.println("ip地址:\t"+ip);
		System.out.println("请求参数：\t"+params);
		System.out.println("请求人:\t"+user.getName());
		System.out.println(getServiceMthodDescription(joinPoint)+"\t标志"+tag.get());
	}
	//后置通知
	@After("log()")
	public void afterExec(JoinPoint joinPoint){
		MethodSignature ms=(MethodSignature)joinPoint.getSignature();
		Method method=ms.getMethod();
		System.out.println("标志为"+tag.get()+"的方法"+method.getName()+"运行消耗"+(System.currentTimeMillis()-time.get())+"ms");
	}
	//环绕通知
	@Around("log()")
	public void aroundExec(ProceedingJoinPoint pjp) throws Throwable{
		System.out.println("我是around===========");
		pjp.proceed();
	}
	//获取返回后通知
	@AfterReturning(value="log()",returning="result")
	public void doAfterReturning(JoinPoint joinPoint,Object result){
		System.out.println("获取返回后通知，获取参数：\t"+result);
		String methodname=joinPoint.getSignature().getName();
		System.out.println("方法\t"+methodname+"\t返回结果\t"+result);
	}
	
	//异常通知：拦截service层的异常
	@AfterThrowing(value="serviceAspect()",throwing="ex")
	public void afterThrowing(JoinPoint joinPoint,Exception ex) throws Exception{
		System.out.println("抛出异常通知-----------------------------");
		System.out.println("异常代码:\t"+ex.getClass().getName());
		System.out.println("异常信息：\t"+ex.getMessage());
		System.out.println("异常方法：\t"+joinPoint.getTarget().getClass().getName()+"."+joinPoint.getSignature().getName()+"()");
		System.out.println("方法描述：\t"+getServiceMthodDescription(joinPoint));
		System.out.println("异常通知结束----------------------------------");
	}
	private void info(JoinPoint joinPoint){
		System.out.println("---------------------------------------------");
		System.out.println("King:\t"+joinPoint.getKind());
		System.out.println("Target:\t"+joinPoint.getTarget().toString());
		Object[] os=joinPoint.getArgs();//获取所有参数
		System.out.println("Args:");
		for(int i=0;i<os.length;i++){
			System.out.println("\t==>参数["+i+"]:\t"+os[i].toString());
		}
		System.out.println("Signature:\t"+joinPoint.getSignature());
		System.out.println("SourceLocation:\t"+joinPoint.getSourceLocation());
		System.out.println("StaticPart:\t"+joinPoint.getStaticPart());
		System.out.println("-------------------------------------------------");
	}
	
	//获取注解中用于描述的信息 用于service层
	public static String getServiceMthodDescription(JoinPoint joinPoint) throws ClassNotFoundException{
		String targetName=joinPoint.getTarget().getClass().getName();
		String methodName=joinPoint.getSignature().getName();
		Object[] arguments=joinPoint.getArgs();
		Class targetClass=Class.forName(targetName);
		Method[] methods=targetClass.getMethods();
		String name="";
		for(Method method:methods){
			if(method.getName().equals(methodName)){
				Class[] clazzs=method.getParameterTypes();
				if(clazzs.length==arguments.length){
					name=method.getAnnotation(ServiceLog.class).name();
					break;
				}
			}
		}
		return name;
	}
	
	/**
	 * 获取controller中用于描述的信息 用于controller层
	 * @param joinPoint
	 * @return
	 * @throws Exception
	 */
	public static String getControllerMethodDesciption(JoinPoint joinPoint)throws Exception{
		String targetName=joinPoint.getTarget().getClass().getName();//获取类名
		String methodName=joinPoint.getSignature().getName();//获取方法名称
		Object[] arguments=joinPoint.getArgs();//获取所有参数
		Class targetClass=Class.forName(targetName);
		Method[] methos=targetClass.getMethods();
		String name="";
		for (Method method : methos) {
			if(method.getName().equals(methodName)){
				Class[] clazzs=method.getParameterTypes();
				if(clazzs.length==arguments.length){
					name=method.getAnnotation(Log.class).name();
					break;
				}
			}
		}
		return name;
	}
}

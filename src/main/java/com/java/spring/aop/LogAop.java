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
 * ����aop����
 * @author lailai
 *
 */
@Aspect //��������һ��������
@Component //ע�뵽IOC������ȥ
public class LogAop {

	ThreadLocal<Long> time=new ThreadLocal<>();
	ThreadLocal<String> tag=new ThreadLocal<>();
	
	/**
	 * ������ʹ��ע��Log�ĵط�����
	 */
	//controller��Ĳ��е�
	@Pointcut("@annotation(com.java.spring.annotation.Log)")
	public void log(){
		System.out.println("����һ�������");
	}
	//service��Ĳ��е�
	@Pointcut("@annotation(com.java.spring.annotation.ServiceLog)")
	public void serviceAspect(){}
	
	//��controller���ķ���ִ��ǰִ��
	@Before("log()")
	public void beforeExec(JoinPoint joinPoint)throws Exception{
		time.set(System.currentTimeMillis());
		tag.set(UUID.randomUUID().toString());
		info(joinPoint);
		MethodSignature ms=(MethodSignature)joinPoint.getSignature();
		Method m=ms.getMethod();
		//System.out.println(m.getAnnotation(Log.class).name()+"\t��־"+tag.get());
		System.out.println(getControllerMethodDesciption(joinPoint)+"\t��־"+tag.get());
	}
	//service���ǰ��֪ͨ
	@Before("serviceAspect()")
	public void beforeExecService(JoinPoint joinPoint)throws Exception{
		time.set(System.currentTimeMillis());
		tag.set(UUID.randomUUID().toString());
		info(joinPoint);
		MethodSignature ms=(MethodSignature)joinPoint.getSignature();
		Method m=ms.getMethod();
		//System.out.println(m.getAnnotation(Log.class).name()+"\t��־"+tag.get());
		HttpServletRequest request=((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session=request.getSession();
		User user=(User)session.getAttribute("user");
		String ip=request.getRemoteAddr();//ip
		String params="";
		//������ת��Ϊjson��ʽ�ַ���
		if(joinPoint.getArgs()!=null && joinPoint.getArgs().length>0){
			for(int i=0;i<joinPoint.getArgs().length;i++){
				params+=JSON.toJSONString(joinPoint.getArgs()[i])+";";
			}
		}
		System.out.println("ip��ַ:\t"+ip);
		System.out.println("���������\t"+params);
		System.out.println("������:\t"+user.getName());
		System.out.println(getServiceMthodDescription(joinPoint)+"\t��־"+tag.get());
	}
	//����֪ͨ
	@After("log()")
	public void afterExec(JoinPoint joinPoint){
		MethodSignature ms=(MethodSignature)joinPoint.getSignature();
		Method method=ms.getMethod();
		System.out.println("��־Ϊ"+tag.get()+"�ķ���"+method.getName()+"��������"+(System.currentTimeMillis()-time.get())+"ms");
	}
	//����֪ͨ
	@Around("log()")
	public void aroundExec(ProceedingJoinPoint pjp) throws Throwable{
		System.out.println("����around===========");
		pjp.proceed();
	}
	//��ȡ���غ�֪ͨ
	@AfterReturning(value="log()",returning="result")
	public void doAfterReturning(JoinPoint joinPoint,Object result){
		System.out.println("��ȡ���غ�֪ͨ����ȡ������\t"+result);
		String methodname=joinPoint.getSignature().getName();
		System.out.println("����\t"+methodname+"\t���ؽ��\t"+result);
	}
	
	//�쳣֪ͨ������service����쳣
	@AfterThrowing(value="serviceAspect()",throwing="ex")
	public void afterThrowing(JoinPoint joinPoint,Exception ex) throws Exception{
		System.out.println("�׳��쳣֪ͨ-----------------------------");
		System.out.println("�쳣����:\t"+ex.getClass().getName());
		System.out.println("�쳣��Ϣ��\t"+ex.getMessage());
		System.out.println("�쳣������\t"+joinPoint.getTarget().getClass().getName()+"."+joinPoint.getSignature().getName()+"()");
		System.out.println("����������\t"+getServiceMthodDescription(joinPoint));
		System.out.println("�쳣֪ͨ����----------------------------------");
	}
	private void info(JoinPoint joinPoint){
		System.out.println("---------------------------------------------");
		System.out.println("King:\t"+joinPoint.getKind());
		System.out.println("Target:\t"+joinPoint.getTarget().toString());
		Object[] os=joinPoint.getArgs();//��ȡ���в���
		System.out.println("Args:");
		for(int i=0;i<os.length;i++){
			System.out.println("\t==>����["+i+"]:\t"+os[i].toString());
		}
		System.out.println("Signature:\t"+joinPoint.getSignature());
		System.out.println("SourceLocation:\t"+joinPoint.getSourceLocation());
		System.out.println("StaticPart:\t"+joinPoint.getStaticPart());
		System.out.println("-------------------------------------------------");
	}
	
	//��ȡע����������������Ϣ ����service��
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
	 * ��ȡcontroller��������������Ϣ ����controller��
	 * @param joinPoint
	 * @return
	 * @throws Exception
	 */
	public static String getControllerMethodDesciption(JoinPoint joinPoint)throws Exception{
		String targetName=joinPoint.getTarget().getClass().getName();//��ȡ����
		String methodName=joinPoint.getSignature().getName();//��ȡ��������
		Object[] arguments=joinPoint.getArgs();//��ȡ���в���
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

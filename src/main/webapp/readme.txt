Spring MVC �� Spring ���ϵ�ʱ��SpringMVC��springmvc.xml�ļ��� ����ɨ�������Ҫ���� service��ע�⣬
Spring��applicationContext.xml�ļ��� ����ɨ���ʱ����Ҫ����controller��ע�⣬������ʾ��
SpringMVC��xml���ã�
<context:component-scan base-package="com.xxx">
  <context:exclude-filter type="annotation" expression="org.springframework.stereotype.Service"/>
</context:component-scan>
Spring MVC����ʱ�������ļ����������ɨ�衢urlӳ���Լ�����freemarker��������spring��ɨ�����@Serviceע����ࡣΪʲôҪ�������ã�
��Ϊspringmvc.xml��applicationContext.xml����ͬʱ���أ�������������������ã���ô��
spring�ͻὫ���д�@Serviceע����඼ɨ�赽�����У��ȵ�����applicationContext.xml��ʱ��
����Ϊ�����Ѿ�����Service�࣬ʹ��cglib������Service���д���ֱ�ӵ��µĽ��������applicationContext �е��������ò������ã�
�����쳣ʱ���޷������ݽ��лع������Ͼ���ԭ�����ڡ�
ͬ������Spring��xml�������£�
<context:component-scan base-package="com.insigma">           
 <context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
</context:component-scan>
ɨ���·������ɨ�����@Controllerע����ࡣ��Ϊ��Щ���Ѿ�����������ʱ����springmvc.xml��ɨ���һ����;


���������������Ƕ�controller��������ʱ�����ã�����Ƕ�service�������棬��ô

<context:component-scan base-package="com.java1234.aop" />
<aop:aspectj-autoproxy />

������ע�;�Ҫ����ApplicationContext.xml�����ˣ�ע���ʱ��Ҫ����aop��cglib����ģʽ��

1.SpringMVC�����ܺܺ��ã�û���⣬����ע��Ҳ���˺ܶ�����ã�������ע��@Controller��@Service����SpringMVC��ܰ�����ģ�
Ҳ����˵����Щ���ʵ�����Լ�ע��Ҳ����SpringMVC��������ɵģ�ȷ�е���˵���������Լ��е������ĵ�IoC������ɵģ�
2.��AOP�������֧����Spring��ܱ�����ɵģ���Spring��ܵ�Ӧ����������ɨ�貢�����


Spring�е��������ȻҪ��@Aspect��ע����Ҳ��Ҫ������@Componet��ע���������ܱ�ע�ᵽ������

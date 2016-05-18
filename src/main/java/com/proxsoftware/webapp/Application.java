package com.proxsoftware.webapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = {"com.proxsoftware.webapp.*"})
@EnableTransactionManagement
//@EntityScan(basePackageClasses = new Clas)
public class Application /*implements EmbeddedServletContainerCustomizer*/ {


    @Value(value = "${file_uri}")
    private static String FILE_URI;

    public static void main(String[] args) {
        System.out.println("from run " + FILE_URI);
        SpringApplication.run(Application.class, args);
    }

    /*@Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        System.out.println("from costomize");
        container.setPort(8083);
    }*/

    /*@Configuration
    public static class BeanPostProcessorConfiguration {
        @Autowired
        ApplicationContext context;
        int count = 0;

        @Bean
        public BeanPostProcessor beanPostProcessor() {
            Environment environment = context.getEnvironment();
            environment.acceptsProfiles("dev");

            return new BeanPostProcessor() {
                @Override
                public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//                    System.out.println("Active Profile is: " + Arrays.toString(environment.getActiveProfiles()));
                    return bean;
                }

                @Override
                public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                    return bean;
                }

            };

        }
    }*/
}

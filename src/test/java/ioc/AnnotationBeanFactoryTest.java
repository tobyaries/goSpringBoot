package ioc;

import beans.cfgRd.annotation.UserRepository;
import beans.cfgRd.annotation.impl.UserServiceImpl;
import beans.cfgRd.annotation.interfaces.UserService;
import core.cfgRd.annotation.AnnotationBeanFactory;
import core.ioc.DefaultListableBeanFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AnnotationBeanFactoryTest {

    @Test
    public void testGetAnnotationBean() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        AnnotationBeanFactory annotationBeanFactory = new AnnotationBeanFactory("beans.cfgRd.annotation", beanFactory);
        UserService userService = (UserService) beanFactory.getBean("userService");

        Assertions.assertNotNull(userService);
        Assertions.assertTrue(userService instanceof UserServiceImpl);
    }

    @Test
    public void testGetAnnotationBeanWithDependency() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        AnnotationBeanFactory annotationBeanFactory = new AnnotationBeanFactory("beans.cfgRd.annotation", beanFactory);
        UserServiceImpl userService = (UserServiceImpl) beanFactory.getBean("userService");

        Assertions.assertNotNull(userService.getUserRepository());
        Assertions.assertTrue(userService.getUserRepository() instanceof UserRepository);
    }
}
/**
 * Created by m91snik on 19.01.14.
 */
package com.m91snik.smartfilesync.core.test;

import com.m91snik.smartfilesync.core.providers.DirectoryNameProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.config.SourcePollingChannelAdapterFactoryBean;
import org.springframework.integration.endpoint.SourcePollingChannelAdapter;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.config.FileReadingMessageSourceFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;

import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/test/core-root-test.xml"})
public class MainTest implements ApplicationContextAware {

    @Autowired
    private DirectoryNameProvider directoryNameProvider;

    private ApplicationContext applicationContext;

    @Test
    public void test() throws Exception {
        when(directoryNameProvider.getDirectoryName()).thenReturn("/home/m91snik/tmp/12345");

        String directoryName = directoryNameProvider.getDirectoryName();

        ConfigurableApplicationContext configContext = (ConfigurableApplicationContext) applicationContext;
        ConfigurableListableBeanFactory beanFactory = configContext.getBeanFactory();

        FileReadingMessageSourceFactoryBean fileReadingMessageSourceFactoryBean = new
                FileReadingMessageSourceFactoryBean();
        fileReadingMessageSourceFactoryBean.setDirectory(new File(directoryName));
        fileReadingMessageSourceFactoryBean.setAutoCreateDirectory(true);
        //fileReadingMessageSourceFactoryBean.setFilter(new AcceptOnceFileListFilter<File>());
        FileReadingMessageSource fileReadingMessageSource = fileReadingMessageSourceFactoryBean.getObject();

        beanFactory.registerSingleton("fileReadingMessageSource", fileReadingMessageSource);

        SourcePollingChannelAdapterFactoryBean sourcePollingChannelAdapterFactoryBean = new
                SourcePollingChannelAdapterFactoryBean();
        sourcePollingChannelAdapterFactoryBean.setSource(fileReadingMessageSource);
        sourcePollingChannelAdapterFactoryBean.setAutoStartup(true);
        sourcePollingChannelAdapterFactoryBean.setBeanClassLoader(Thread.currentThread().getContextClassLoader());
        sourcePollingChannelAdapterFactoryBean.setBeanFactory(beanFactory);
        sourcePollingChannelAdapterFactoryBean.setBeanName("sourcePollingChannelAdapter");
        sourcePollingChannelAdapterFactoryBean.setOutputChannel((MessageChannel) applicationContext.getBean
                ("filesChannel"));

        SourcePollingChannelAdapter sourcePollingChannelAdapter = sourcePollingChannelAdapterFactoryBean.getObject();

        beanFactory.registerSingleton("sourcePollingChannelAdapter", sourcePollingChannelAdapter);

//        SourcePollingChannelAdapter sourcePollingChannelAdapter1 = (SourcePollingChannelAdapter) applicationContext.getBean(
//                "sourcePollingChannelAdapter");
        sourcePollingChannelAdapter.start();

//        SourcePollingChannelAdapter filesInCh = (SourcePollingChannelAdapter) applicationContext.getBean(
//                "filesInCh");
        Thread.sleep(100000);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

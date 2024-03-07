package com.yomahub.liteflow.test;

import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.FlowBus;
import com.yomahub.liteflow.flow.LiteflowResponse;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StopWatch;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import javax.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * springboot环境下的nacos配置源功能测试
 * nacos存储数据的格式为xml文件
 * @author mll
 * @since 2.9.0
 */
@ExtendWith(SpringExtension.class)
@TestPropertySource(value = "classpath:/markdown/application-xml.properties")
@SpringBootTest(classes = MarkdownWithLocalMDSpringbootTest.class)
@EnableAutoConfiguration
@ComponentScan({ "com.yomahub.liteflow.test.cmp" })
public class MarkdownWithLocalMDSpringbootTest extends BaseTest {

    @Resource
    private FlowExecutor flowExecutor;

    @AfterEach
    public void after(){
        FlowBus.cleanCache();
    }

    @Test
    public void testForCase3() throws Exception {
        LiteflowResponse response = flowExecutor.execute2Resp("测试编排003", "D");
        Assertions.assertEquals("A==>B==>C==>D", response.getExecuteStepStrWithoutTime());
    }

    @Test
    public void testForCase4() throws Exception {
        LiteflowResponse response = flowExecutor.execute2Resp("测试编排004", "D");
        Assertions.assertEquals("A==>B==>C==>D==>E==>F", response.getExecuteStepStrWithoutTime());
    }

    @Test
    public void testForCase5() throws Exception {
        LiteflowResponse response = flowExecutor.execute2Resp("测试编排003", "E");
        Assertions.assertEquals("A==>B==>C==>E", response.getExecuteStepStrWithoutTime());
    }

    @Test
    public void testForCase6() throws Exception {
        LiteflowResponse response = flowExecutor.execute2Resp("测试编排005", "D", com.yomahub.liteflow.slot.DefaultContext.class);
        Assertions.assertEquals("A==>B==>C==>D==>D1==>G==>D2==>H==>D3", response.getExecuteStepStrWithoutTime());
    }

    @Test
    public void testForCase7() throws Exception {
        LiteflowResponse response = flowExecutor.execute2Resp("测试编排005", "E");
        Assertions.assertEquals("A==>B==>C==>E==>I==>G", response.getExecuteStepStrWithoutTime());
    }

    @Test
    public void testForCase006_1() throws Exception {
        LiteflowResponse response = flowExecutor.execute2Resp("测试编排006", "E");
        Assertions.assertEquals("A==>B==>C==>E==>I==>G", response.getExecuteStepStrWithoutTime());
    }

    @Test
    public void testForCase006_2() throws Exception {
        LiteflowResponse response = flowExecutor.execute2Resp("测试编排006", "F");
        // 因为是ANY, F21--F22流程来不及执行完就会被提前结束
        Assertions.assertEquals("A==>B==>C==>F==>F11==>F21==>G", response.getExecuteStepStrWithoutTime());
    }

    @Test
    public void testForCase006_3() throws Exception {
        LiteflowResponse response = flowExecutor.execute2Resp("测试编排006", "D");
        // G节点实现了Once语义。只跑一次
        String actual = response.getExecuteStepStrWithoutTime();
        Assertions.assertTrue(
                "A==>B==>C==>D==>D1==>D2==>D3==>H==>G".equals(actual)
                        || "A==>B==>C==>D==>D1==>D3==>D2==>H==>G".equals(actual)
                ,">>>"
        );
    }
    @Test
    public void testForCase007() throws Exception {
        LiteflowResponse response = flowExecutor.execute2Resp("测试编排007", "D");
        // G节点实现了Once语义。只跑一次
        String actual = response.getExecuteStepStrWithoutTime();
        Assertions.assertTrue(
                "A==>B==>D==>D1==>D2==>D3==>G==>E==>F".equals(actual)
                ,">>>"
        );
    }

    @Test
    public void testForCase008() throws Exception {
        LiteflowResponse response = flowExecutor.execute2Resp("测试编排008", "D");
        // G节点实现了Once语义。只跑一次
        String actual = response.getExecuteStepStrWithoutTime();
        Assertions.assertTrue(
                "A==>B==>D==>D1==>F11==>D2==>D3==>I==>E==>F==>I==>H".equals(actual)
                || "A==>B==>D==>D1==>D2==>D3==>F11==>I==>E==>F==>I==>H".equals(actual)
                || "A==>B==>D==>D1==>D3==>D2==>F11==>I==>E==>F==>I==>H".equals(actual),
                ">>>"
        );
    }

    @Test
    public void testForCase008Benchmark() throws Exception {
        StopWatch stopWatch = new StopWatch();

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(100, 100, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

        final int loop = 10000;
        stopWatch.start();
        for (int i = 0; i < loop; i++) {
            threadPoolExecutor.submit(() -> {
                LiteflowResponse response = flowExecutor.execute2Resp("测试编排008", "D");
            });
        }
        threadPoolExecutor.shutdown();
        threadPoolExecutor.awaitTermination(1, TimeUnit.HOURS);
        stopWatch.stop();
        // G节点实现了Once语义。只跑一次

        System.out.println(stopWatch.toString());
    }


}

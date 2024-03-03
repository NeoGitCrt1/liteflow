package com.yomahub.liteflow.test;

import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.FlowBus;
import com.yomahub.liteflow.flow.LiteflowResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * springboot环境下的nacos配置源功能测试
 * nacos存储数据的格式为xml文件
 * @author mll
 * @since 2.9.0
 */
@RunWith(SpringRunner.class)
@TestPropertySource(value = "classpath:/markdown/application-xml.properties")
@SpringBootTest(classes = MarkdownWithLocalMDSpringbootTest.class)
@EnableAutoConfiguration
@ComponentScan({"com.yomahub.liteflow.test.cmp"})
public class MarkdownWithLocalMDSpringbootTest extends BaseTest {

    @Resource
    private FlowExecutor flowExecutor;

    @After
    public void after(){
        FlowBus.cleanCache();
    }

    @Test
    public void testForCase3() throws Exception {
        LiteflowResponse response = flowExecutor.execute2Resp("测试编排003", "D");
        Assert.assertEquals("A==>B==>C==>D", response.getExecuteStepStrWithoutTime());
    }

    @Test
    public void testForCase4() throws Exception {
        LiteflowResponse response = flowExecutor.execute2Resp("测试编排004", "D");
        Assert.assertEquals("A==>B==>C==>D==>E==>F", response.getExecuteStepStrWithoutTime());
    }

    @Test
    public void testForCase5() throws Exception {
        LiteflowResponse response = flowExecutor.execute2Resp("测试编排003", "E");
        Assert.assertEquals("A==>B==>C==>E", response.getExecuteStepStrWithoutTime());
    }

    @Test
    public void testForCase6() throws Exception {
        LiteflowResponse response = flowExecutor.execute2Resp("测试编排005", "D", com.yomahub.liteflow.slot.DefaultContext.class);
        Assert.assertEquals("A==>B==>C==>D==>D1==>G==>D2==>H==>D3", response.getExecuteStepStrWithoutTime());
    }

    @Test
    public void testForCase7() throws Exception {
        LiteflowResponse response = flowExecutor.execute2Resp("测试编排005", "E");
        Assert.assertEquals("A==>B==>C==>E==>I==>G", response.getExecuteStepStrWithoutTime());
    }

    @Test
    public void testForCase006_1() throws Exception {
        LiteflowResponse response = flowExecutor.execute2Resp("测试编排006", "E");
        Assert.assertEquals("A==>B==>C==>E==>I==>G", response.getExecuteStepStrWithoutTime());
    }

    @Test
    public void testForCase006_2() throws Exception {
        LiteflowResponse response = flowExecutor.execute2Resp("测试编排006", "F");
        // 因为是ANY, F21--F22流程来不及执行完就会被提前结束
        Assert.assertEquals("A==>B==>C==>F==>F11==>F21==>G", response.getExecuteStepStrWithoutTime());
    }

    @Test
    public void testForCase006_3() throws Exception {
        LiteflowResponse response = flowExecutor.execute2Resp("测试编排006", "D");
        // G节点实现了Once语义。只跑一次
        String actual = response.getExecuteStepStrWithoutTime();
        Assert.assertTrue(">>>",
                "A==>B==>C==>D==>D1==>D2==>D3==>H==>G".equals(actual)
                        || "A==>B==>C==>D==>D1==>D3==>D2==>H==>G".equals(actual)
        );
    }
    @Test
    public void testForCase007() throws Exception {
        LiteflowResponse response = flowExecutor.execute2Resp("测试编排007", "D");
        // G节点实现了Once语义。只跑一次
        String actual = response.getExecuteStepStrWithoutTime();
        Assert.assertTrue(">>>",
                "A==>B==>D==>D1==>D2==>D3==>G==>E==>F".equals(actual)
        );
    }

    @Test
    public void testForCase008() throws Exception {
        LiteflowResponse response = flowExecutor.execute2Resp("测试编排008", "D");
        // G节点实现了Once语义。只跑一次
        String actual = response.getExecuteStepStrWithoutTime();
        Assert.assertTrue(">>>",
                "A==>B==>D==>D1==>D2==>D3==>I==>E==>F==>I==>H".equals(actual)
        );
    }

}

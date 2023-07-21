package com.yomahub.liteflow.test.monitorFile;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.CharsetUtil;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.core.FlowExecutorHolder;
import com.yomahub.liteflow.flow.LiteflowResponse;
import com.yomahub.liteflow.property.LiteflowConfig;
import com.yomahub.liteflow.test.BaseTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

public class LiteflowMonitorFileTest extends BaseTest {

	private static FlowExecutor flowExecutor;

	@BeforeAll
	public static void init() {
		LiteflowConfig config = new LiteflowConfig();
		config.setRuleSource("monitorFile/flow.el.xml");
		config.setEnableMonitorFile(true);
		flowExecutor = FlowExecutorHolder.loadInstance(config);
	}

	@Test
	public void testMonitor() throws InterruptedException {
		String absolutePath = new ClassPathResource("classpath:/monitorFile/flow.el.xml").getAbsolutePath();
		String content = FileUtil.readUtf8String(absolutePath);
		String newContent = content.replace("THEN(a, b, c);", "THEN(a, c, b);");
		FileUtil.writeString(newContent, new File(absolutePath), CharsetUtil.CHARSET_UTF_8);
		Thread.sleep(3000);
		LiteflowResponse response = flowExecutor.execute2Resp("chain1", "arg");
		Assertions.assertEquals("a==>c==>b", response.getExecuteStepStr());
	}

	@AfterEach
	public void afterEach(){
		String absolutePath = new ClassPathResource("classpath:/monitorFile/flow.el.xml").getAbsolutePath();
		FileUtil.writeString("<?xml version=\"1.0\" encoding=\"UTF-8\"?><flow><chain name=\"chain1\">THEN(a, b, c);</chain></flow>", new File(absolutePath), CharsetUtil.CHARSET_UTF_8);
	}

}

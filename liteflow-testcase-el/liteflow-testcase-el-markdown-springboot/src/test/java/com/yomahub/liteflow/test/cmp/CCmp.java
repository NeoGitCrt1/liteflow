/**
 * <p>Title: liteflow</p>
 * <p>Description: 轻量级的组件式流程框架</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2020/4/1
 */
package com.yomahub.liteflow.test.cmp;

import com.yomahub.liteflow.core.NodeSwitchComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("C")
public class CCmp extends NodeSwitchComponent {
	Logger log = LoggerFactory.getLogger(this.getClass());
	@Override
	public String processSwitch() throws Exception {
		log.info(" executed! > switch to:" + this.getRequestData());
		return this.getRequestData();
	}

}

/**
 * <p>Title: liteflow</p>
 * <p>Description: 轻量级的组件式流程框架</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2020/4/1
 */
package com.yomahub.liteflow.test.cmp;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component("A")
public class ACmp extends AbstractTestCmp {


    @Override
    public void process() throws Exception {
        MDC.put("traceId", "556");
        super.process();
        log.info("MDC trans {}", MDC.get("traceId"));
    }
}

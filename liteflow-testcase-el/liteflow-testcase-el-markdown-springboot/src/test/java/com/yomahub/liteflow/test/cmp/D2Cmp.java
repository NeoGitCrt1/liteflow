/**
 * <p>Title: liteflow</p>
 * <p>Description: 轻量级的组件式流程框架</p>
 *
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2020/4/1
 */
package com.yomahub.liteflow.test.cmp;

import org.springframework.stereotype.Component;

@Component("D2")
public class D2Cmp extends AbstractTestCmp {


    @Override
    public void process() throws Exception {
        Thread.sleep(200);
        super.process();
    }
}

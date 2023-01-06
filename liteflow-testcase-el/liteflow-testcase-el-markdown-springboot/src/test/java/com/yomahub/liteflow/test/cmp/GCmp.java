/**
 * <p>Title: liteflow</p>
 * <p>Description: 轻量级的组件式流程框架</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2020/4/1
 */
package com.yomahub.liteflow.test.cmp;

import com.yomahub.liteflow.slot.DefaultContext;
import org.springframework.stereotype.Component;

@Component("G")
public class GCmp extends AbstractTestCmp {

    @Override
    public boolean isAccess() {

        DefaultContext context = this.getContextBean(DefaultContext.class);
        if (context.hasData("GAccessed")) {
            return false;
        } else {
            context.setData("GAccessed", Boolean.TRUE);
            return true;
        }
    }
}

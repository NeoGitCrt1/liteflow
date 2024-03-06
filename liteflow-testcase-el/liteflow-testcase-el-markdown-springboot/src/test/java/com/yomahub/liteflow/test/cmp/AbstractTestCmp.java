package com.yomahub.liteflow.test.cmp;

import com.yomahub.liteflow.core.NodeComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTestCmp extends NodeComponent {
    Logger log = LoggerFactory.getLogger(this.getClass());
    @Override
    public void process() throws Exception {
        String tag = this.getTag();
        log.info("[{}] {} executed! > tag: {}", this.getSlot().getRequestId(), this.getNodeId(), tag);
    }
}

package com.yomahub.liteflow.test.subflow.cmp1;

import com.yomahub.liteflow.annotation.LiteflowCmpDefine;
import com.yomahub.liteflow.annotation.LiteflowMethod;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.enums.LiteFlowMethodEnum;
import org.springframework.stereotype.Component;


@Component("e")
@LiteflowCmpDefine
public class ECmp{

    @LiteflowMethod(LiteFlowMethodEnum.PROCESS)
    public void process(NodeComponent bindCmp) throws Exception {
        System.out.println("Ecomp executed!");
    }
}
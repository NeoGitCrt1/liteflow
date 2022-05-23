package com.yomahub.liteflow.test.asyncNode.cmp;

import com.yomahub.liteflow.annotation.LiteflowCmpDefine;
import com.yomahub.liteflow.annotation.LiteflowMethod;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.slot.Slot;
import com.yomahub.liteflow.enums.LiteFlowMethodEnum;
import org.springframework.stereotype.Component;


@Component("d")
@LiteflowCmpDefine
public class DCmp{
    @LiteflowMethod(LiteFlowMethodEnum.PROCESS)
    public void process(NodeComponent bindCmp) throws Exception {
        Thread.sleep(1000);
        Slot slot = bindCmp.getSlot();
        synchronized (NodeComponent.class){
            if (slot.hasData("check")){
                String str = slot.getData("check");
                str += bindCmp.getNodeId();
                slot.setData("check", str);
            }else{
                slot.setData("check", bindCmp.getNodeId());
            }
        }
        System.out.println("Dcomp executed!");
    }
}
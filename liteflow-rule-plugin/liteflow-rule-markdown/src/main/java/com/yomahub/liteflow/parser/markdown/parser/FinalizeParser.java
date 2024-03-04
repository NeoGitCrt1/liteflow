package com.yomahub.liteflow.parser.markdown.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class FinalizeParser implements Parser{
    @Override
    public void parse(ParseContext context) {
        StringJoiner main = new StringJoiner(",", "THEN(", ")");
        join(context.head, main, context);
        context.el = main.toString();
    }

    private void join(FlowChartNode root, StringJoiner main, ParseContext context) {
        if (root == null || root.equals(context.skipParallelJoinPoint)) {
            return;
        }

        String nid = root.nid;
        // 为了兼容节点组件复用, 去掉#后面的部分取组件id.例如G#2 则得G,
        int numFeildMark = nid.indexOf('#');
        if (numFeildMark > 0) {
            nid = nid.substring(0, numFeildMark);
        }
        switch (root.type) {
            case COMMON:
                main.add("node(\"" + nid + "\").tag(\"" + root.tag + "\")");

                if (root.next.isEmpty()) {
                    return;
                }

                if (root.next.size() == 1) {
                    join(root.next.get(0), main, context);
                } else {
                    StringJoiner when = new StringJoiner(",", "WHEN(", ").any(" + String.valueOf(root.anyForParallel) + ")");
                    Map<FlowChartNode, Integer> nodeCounter = new HashMap<>();

                    // 计算引用
                    joinPointDiscover(root, nodeCounter);
                    int maxRef = 0;
                    FlowChartNode joinPoint = null;
                    for (Map.Entry<FlowChartNode, Integer> elm : nodeCounter.entrySet()) {
                        if (elm.getValue() > maxRef) {
                            joinPoint = elm.getKey();
                            maxRef = elm.getValue();
                        }
                    }
                    context.skipParallelJoinPoint = joinPoint;

                    for (FlowChartNode n : root.next) {
                        StringJoiner subMain = new StringJoiner(",", "THEN(", ")");
                        join(n, subMain, context);
                        when.add(subMain.toString());
                    }
                    main.add(when.toString());

                    context.skipParallelJoinPoint = null;
                    join(joinPoint, main, context);
                }

                break;
            case SWITCH:
                StringJoiner sw = new StringJoiner(",", "SWITCH(node(\"" + nid + "\")).to(", ")");
                for (FlowChartNode n : root.next) {
                    StringJoiner subMain = new StringJoiner(",", "THEN(", ").id(\"" + n.nid + "\")");
                    join(n, subMain, context);
                    sw.add(subMain.toString());
                }
                main.add(sw.toString());
                break;
            default:
                throw new IllegalStateException("FIXME unknown type:" + root.type);
        }


    }

    void joinPointDiscover(FlowChartNode root, Map<FlowChartNode, Integer> nodeCounter) {
        if (root.next != null && root.next.size() == 1) {
            nodeCounter.compute(root.next.get(0), (k , v) -> {
                if (v == null) {
                    return 1;
                }
                v += 1;
                return v;
            });
            return;
        }

        for (FlowChartNode flowChartNode : root.next) {
            joinPointDiscover(flowChartNode, nodeCounter);
        }
    }
}

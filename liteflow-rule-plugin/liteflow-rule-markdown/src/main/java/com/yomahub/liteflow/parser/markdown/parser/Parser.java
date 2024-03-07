package com.yomahub.liteflow.parser.markdown.parser;

import com.yomahub.liteflow.enums.NodeTypeEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public interface Parser {

    void parse(ParseContext context);

    class ParseContext {

        public String[] lines;

        int chartStartPos;

        public final String rawText;

        public String chainId;

        FlowChartNode head;
        FlowChartNode skipParallelJoinPoint = null;

        public String el;

        public ParseContext(String rawText) {
            this.rawText = rawText;
        }
    }

    class FlowChartNode {
        final String nid;

        final String desc;

        final NodeTypeEnum type;

        final String tag;

        boolean anyForParallel;

        /**
         * FlowChartNodeType.STEP when has only one next element, then use THEN(x,x,x) flow; or when multiple, use WHEN(x,x,x) flow
         * FlowChartNodeType.SWITCH has multiple next element
         */
        final List<FlowChartNode> next = new ArrayList<>();

        FlowChartNode(String nid, String desc, NodeTypeEnum type, String tag) {
            this.nid = nid;
            this.desc = desc;
            this.type = type;
            this.tag = tag == null ? "" : tag;
        }

        FlowChartNode setAny(boolean any) {
            anyForParallel = any;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FlowChartNode that = (FlowChartNode) o;
            return nid.equals(that.nid);
        }

        @Override
        public int hashCode() {
            return Objects.hash(nid);
        }

        @Override
        public String toString() {
            return "FlowChartNode{" +
                    "nid='" + nid + '\'' +
                    ", tag='" + tag + '\'' +
                    ", desc='" + desc + '\'' +
                    ", type=" + type +
                    ", next.size=" + next.size() +
                    '}';
        }
    }
}
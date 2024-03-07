package com.yomahub.liteflow.parser.markdown.parser;

import cn.hutool.core.io.resource.ResourceUtil;
import com.yomahub.liteflow.parser.markdown.MarkdownMermaidParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ParseTests {
    private final List<Parser> parsers = MarkdownMermaidParser.parsers;

    @Test
    public void testFlowNodesParser() throws Exception{
        String text = ResourceUtil.readUtf8Str("classpath://CASE1.md");
        Parser.ParseContext parseContext = new Parser.ParseContext(text);

        parsers.forEach(p ->p.parse(parseContext));
        System.out.println(parseContext.el);

        Assertions.assertTrue( parseContext.chainId.equals("asd"));

        Assertions.assertTrue(parseContext.head.nid.equals("A"));
        Assertions.assertTrue( parseContext.head.next.size() == 3);
        Assertions.assertTrue( parseContext.head.next.get(0).nid.equals("E"));
        Assertions.assertTrue( parseContext.head.next.get(1).nid.equals("C"));
        Assertions.assertTrue( parseContext.head.next.get(2).nid.equals("G"));
        printGraph(parseContext.head, new HashSet<>());

    }

    @Test
    public void testFlowNodesParser2() throws Exception{
        String text = ResourceUtil.readUtf8Str("classpath://CASE2.md");
        Parser.ParseContext parseContext = new Parser.ParseContext(text);

        parsers.forEach(p -> p.parse(parseContext));
        printGraph(parseContext.head, new HashSet<>());
        System.out.println(parseContext.el);
        Assertions.assertTrue(parseContext.chainId.equals("CASE2"));
        Assertions.assertTrue(parseContext.head.nid.equals("A"));
        Assertions.assertTrue( parseContext.head.next.size() == 1);

        Parser.FlowChartNode b = parseContext.head.next.get(0);
        Assertions.assertTrue(b.nid.equals("B"));

    }

    @Test
    public void testFlowNodesParser2_2() throws Exception {
        String text = ResourceUtil.readUtf8Str("classpath://CASE2_2.md");
        Parser.ParseContext parseContext = new Parser.ParseContext(text);

        parsers.forEach(p -> p.parse(parseContext));
        System.out.println(parseContext.el);
        Assertions.assertTrue(parseContext.chainId.equals("CASE2"));
        Assertions.assertTrue( parseContext.head.nid.equals("A"));
        Assertions.assertTrue( parseContext.head.next.size() == 1);

        Parser.FlowChartNode b = parseContext.head.next.get(0);
        Assertions.assertTrue( b.nid.equals("B"));

        printGraph(parseContext.head, new HashSet<>());
    }
    @Test
    public void testFlowNodesParser2_e1() throws Exception {
        String text = ResourceUtil.readUtf8Str("classpath://CASE2_e1.md");
        Parser.ParseContext parseContext = new Parser.ParseContext(text);

        parsers.forEach(p -> p.parse(parseContext));
        System.out.println(parseContext.el);
        Assertions.assertTrue(parseContext.chainId.equals("CASE2"));
        Assertions.assertTrue(!parseContext.head.nid.equals("A"));
        printGraph(parseContext.head, new HashSet<>());
    }

    @Test
    public void testFlowNodesParser3() throws Exception {
        String text = ResourceUtil.readUtf8Str("classpath://CASE3.md");
        Parser.ParseContext parseContext = new Parser.ParseContext(text);

        parsers.forEach(p -> p.parse(parseContext));

        Set<String> cache = new HashSet<>();
        printGraph(parseContext.head, cache);
        System.out.println(parseContext.el);
        Assertions.assertTrue(!cache.contains("FB"));
        Assertions.assertTrue( !cache.contains("FA"));
        Assertions.assertTrue(!cache.contains("FABEND"));
    }


    @Test
    public void testFlowNodesParser4() throws Exception{
        String text = ResourceUtil.readUtf8Str("classpath://CASE4.md");
        Parser.ParseContext parseContext = new Parser.ParseContext(text);

        parsers.forEach(p ->p.parse(parseContext));

        Set<String> cache = new HashSet<>();
        printGraph(parseContext.head, cache);
        System.out.println(parseContext.el);
        Assertions.assertTrue(!cache.contains("FB"));
        Assertions.assertTrue(!cache.contains("FA"));
        Assertions.assertTrue( !cache.contains("FABEND"));
    }

    private void printGraph(Parser.FlowChartNode head, Set<String> printedCache) {
        if (printedCache.contains(head.nid)) {
            return;
        }
        System.out.println(head);
        printedCache.add(head.nid);
        head.next.forEach(n -> printGraph(n, printedCache));


    }

    @Test
    public void testFlowNodesParser5() throws Exception {
        String text = ResourceUtil.readUtf8Str("classpath://CASE6.md");
        Parser.ParseContext parseContext = new Parser.ParseContext(text);

        parsers.forEach(p -> p.parse(parseContext));

        Set<String> cache = new HashSet<>();
        printGraph(parseContext.head, cache);
        System.out.println(parseContext.el);
        Assertions.assertTrue( !cache.contains("FB"));
        Assertions.assertTrue( !cache.contains("FA"));
        Assertions.assertTrue( !cache.contains("FABEND"));
        Assertions.assertTrue( parseContext.el.contains(".any(true)"));
        Assertions.assertTrue( parseContext.el.contains(".any(false)"));
    }
    @Test
    public void testFlowNodesParser7() throws Exception {
        String text = ResourceUtil.readUtf8Str("classpath://CASE7.md");
        Parser.ParseContext parseContext = new Parser.ParseContext(text);

        parsers.forEach(p -> p.parse(parseContext));

        Set<String> cache = new HashSet<>();
        printGraph(parseContext.head, cache);
        System.out.println(parseContext.el);
    }
    @Test
    public void testFlowNodesParser8() throws Exception {
        String text = ResourceUtil.readUtf8Str("classpath://CASE8.md");
        Parser.ParseContext parseContext = new Parser.ParseContext(text);

        parsers.forEach(p -> p.parse(parseContext));

        Set<String> cache = new HashSet<>();
        printGraph(parseContext.head, cache);
        System.out.println(parseContext.el);
    }
}

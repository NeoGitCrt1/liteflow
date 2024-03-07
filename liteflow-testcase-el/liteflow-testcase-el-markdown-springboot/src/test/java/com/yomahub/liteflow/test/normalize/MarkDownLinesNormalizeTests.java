package com.yomahub.liteflow.test.normalize;

import com.yomahub.liteflow.parser.markdown.parser.ChainIdParser;
import com.yomahub.liteflow.parser.markdown.parser.FlowNodesParser;
import com.yomahub.liteflow.parser.markdown.parser.NormalizeParser;
import com.yomahub.liteflow.parser.markdown.parser.Parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import java.util.List;

public class MarkDownLinesNormalizeTests {

    @Test
    public void testEmptyText() throws Exception{
        Parser.ParseContext parseContext = new Parser.ParseContext("");
        NormalizeParser normalizeParser = new NormalizeParser();

        normalizeParser.parse(parseContext);

        Assertions.assertTrue(parseContext.lines.length == 0);
    }

    @Test
    public void testTitleOnly() throws Exception{
        Parser.ParseContext parseContext = new Parser.ParseContext("```mermaid\n" +
                "--- \r\n" +
                "   \n" +
                "\r\n" +
                "\n" +
                "title: asd    \n" +
                "   \n" +
                ";\n" +
                ";   \n" +
                "---\n```");
        NormalizeParser normalizeParser = new NormalizeParser();

        normalizeParser.parse(parseContext);

        Assertions.assertTrue(parseContext.lines.length == 5,">>" + parseContext.lines.length);
        Assertions.assertTrue(parseContext.lines[1].startsWith("---"));
        Assertions.assertTrue(parseContext.lines[2].startsWith("title: "));
        Assertions.assertTrue(parseContext.lines[3].startsWith("---"));
    }


    @Test
    public void testChainIdParser() throws Exception{
        Parser.ParseContext parseContext = new Parser.ParseContext("```mermaid\n" +
                "--- \r\n" +
                "   \n" +
                "\r\n" +
                "\n" +
                "title: asd   \n" +
                "\n" +
                "\n" +
                "---\n```");
        NormalizeParser normalizeParser = new NormalizeParser();

        normalizeParser.parse(parseContext);

        ChainIdParser chainIdParser = new ChainIdParser();

        chainIdParser.parse(parseContext);

        Assertions.assertTrue( parseContext.chainId.equals("asd"), ">>>" + parseContext.chainId);
    }

    @Test
    public void getScriptEngineFactory()
    {
        ScriptEngineManager manager = new ScriptEngineManager();
        List<ScriptEngineFactory> factories = manager.getEngineFactories();
        for (ScriptEngineFactory factory : factories)
        {
            System.out.println(factory.getNames());
        }
    }


}

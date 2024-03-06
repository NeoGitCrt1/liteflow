package com.yomahub.liteflow.parser.markdown;

import cn.hutool.core.collection.ListUtil;
import com.yomahub.liteflow.builder.el.LiteFlowChainELBuilder;
import com.yomahub.liteflow.parser.el.ClassJsonFlowELParser;
import com.yomahub.liteflow.parser.markdown.parser.ChainIdParser;
import com.yomahub.liteflow.parser.markdown.parser.FinalizeParser;
import com.yomahub.liteflow.parser.markdown.parser.FlowNodesParser;
import com.yomahub.liteflow.parser.markdown.parser.GlobalCheckParser;
import com.yomahub.liteflow.parser.markdown.parser.NormalizeParser;
import com.yomahub.liteflow.parser.markdown.parser.Parser;
import com.yomahub.liteflow.property.LiteflowConfig;
import com.yomahub.liteflow.property.LiteflowConfigGetter;
import com.yomahub.liteflow.spi.holder.PathContentParserHolder;

import java.util.Arrays;
import java.util.List;

public class MarkdownMermaidParser extends ClassJsonFlowELParser {



    @Override
    public void parseMain(List<String> pathList) throws Exception {

        LiteflowConfig liteflowConfig = LiteflowConfigGetter.get();

        parse(PathContentParserHolder.loadContextAware().parseContent(
                ListUtil.toList(liteflowConfig.getRuleSourceExtData().split(",|;")))
        );
    }

    @Override
    public String parseCustom() {
        return null;
    }


    public static final List<Parser> parsers = Arrays.asList(new NormalizeParser(), new GlobalCheckParser(), new ChainIdParser(), new FlowNodesParser(), new FinalizeParser());

    /**
     *  samples of content , see README.md
     *
     * @param contentList
     * @throws Exception
     */
    @Override
    public void parse(List<String> contentList) throws Exception {

        contentList.forEach( chainContent -> {

            Parser.ParseContext context = new Parser.ParseContext(chainContent);
            parsers.forEach(p -> p.parse(context));

            LiteFlowChainELBuilder.createChain().setChainId(context.chainId).setEL(context.el).build();

        });

    }
}

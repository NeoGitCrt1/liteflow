package com.yomahub.liteflow.parser.markdown.parser;

public class ChainIdParser implements Parser{

    static final String TITLE_PREFIX = "title: ";

    @Override
    public void parse(ParseContext context) {
        String[] lines = context.lines;
        if (lines[1].endsWith("]") && lines[1].contains(">")) {
            context.chainId = lines[1].substring(0, lines[1].indexOf(">"));
            context.chartStartPos = 2;
        } else if (lines[0].startsWith("---")
                && lines[1].startsWith(ChainIdParser.TITLE_PREFIX)
                && lines[2].startsWith("---")) {
            context.chainId = lines[1].substring(TITLE_PREFIX.length());
            context.chartStartPos = 4;
        } else {
            throw new IllegalArgumentException("should has title for specifying chainId");
        }
    }
}

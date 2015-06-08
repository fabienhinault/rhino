package org.mozilla.javascript.ast;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstNode.DebugPrintVisitor;

public class DebugHtmlVisitor extends DebugPrintVisitor {
    private int depth = -1; // old node depth
    private String source;

    public DebugHtmlVisitor(StringBuilder buf, String source) {
        super(buf);
        this.source = source;
        buffer.append("<!DOCTYPE html>\n<html>\n<head></head>\n<body>");
    }

    /*
     * * When nodeDepth == this.depth + 1 nothing to do * When nodeDepth ==
     * this.depth then close the previous div * When nodeDepth == this.depth - 1
     * then close 2 divs etc.
     */
    private void closeDivs(int nodeDepth) {
        for (int i = this.depth; i > nodeDepth - 1; i--) {
            buffer.append(makeIndent(i));
            buffer.append("</div>");
        }
    }

    public void finalize() {
        closeDivs(0);
        buffer.append("</body>\n</html>\n");
    }

    private void appendMarkup(String markup, String content, String attributes) {
        buffer.append("<" + markup + " " + attributes + ">");
        buffer.append(content);
        buffer.append("</" + markup + ">");
    }

    public boolean visit(AstNode node) {
        int nodeDepth = node.depth();
        closeDivs(nodeDepth);
        this.depth = nodeDepth;
        buffer.append(makeIndent(this.depth));
        int tt = node.getType();
        String name = Token.typeToName(tt);
        buffer.append("<div style=\"display:inline-block; "
                + "border-style: solid; border-width: 1px\">");
        this.appendMarkup("p", name, "");
        if (tt == Token.NAME) {
            buffer.append(" ").append(((Name) node).getIdentifier());
        }
        buffer.append("\n");
        return true;
    }

}

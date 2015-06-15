package org.mozilla.javascript.ast;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.TreeMap;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstNode.DebugPrintVisitor;

public class DebugHtmlVisitor extends DebugPrintVisitor {
    private Stack<Integer> offsets;
    private Integer offset = 0;
    private Buffer buffer;

    public DebugHtmlVisitor(String source) {
        super(new StringBuilder());
        buffer = new Buffer(source);
        buffer.insert(0, "<!DOCTYPE html>\n<html>\n<head></head>\n<body>");
        buffer.append("</body>\n</html>\n");
        offsets = new Stack<>();
    }

    public boolean visit(AstNode node) {
        int tt = node.getType();
        String name = Token.typeToName(tt);
        buffer.insert(offset + node.getPosition(), "<div style=\"display:inline-block; "
                + "border-style: solid; border-width: 1px\">" + 
                "\n" + makeIndent(node.depth()) + name + "<br>");
//        if (tt == Token.NAME) {
//            buffer.insert(offset + node.getPosition(), ((Name) node).getIdentifier() + "<br>");
//        }
        buffer.insert(offset + node.getPosition() + node.getLength(),
                "\n" + makeIndent(node.depth()) + "</div>");
        return true;
    }
    
    public void pushOffset(int offset){
        offsets.push(offset);
        this.offset += offset;
    }
    
    public void popOffset(){
        offset -= offsets.pop();
    }

    @Override
    public String toString() {
        return buffer.toString();
    }
    
    private class Buffer {
        private StringBuilder builder;
        private Map<Integer, Integer> inserts = new TreeMap<>();
        public Buffer (String source){
            builder = new StringBuilder(source);
        }
        public void insert(int dstOffset, String string){
            Iterator<Entry<Integer, Integer>> iterator = inserts.entrySet().iterator();
            int realOffset = dstOffset;
            while(iterator.hasNext()){
                Entry<Integer, Integer> entry = iterator.next();
                if (entry.getKey() <= dstOffset){
                    realOffset += entry.getValue();
                } else {
                    //break;
                }
            }
            builder.insert(realOffset, string);
            if(inserts.containsKey(dstOffset)){
                inserts.put(dstOffset, string.length() + inserts.get(dstOffset));
            } else {
                inserts.put(dstOffset, string.length());
            }
        }
        public void append(String string){
            builder.append(string);
        }
        public String toString(){
            return builder.toString();
        }
    }
    
}

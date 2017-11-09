
package bipartitegraph;

/**
 *
 * @author markova
 */
public class Edge {
    private int nodeStart;
    private int nodeEnd;
    private boolean direct;//прямое направление или обратное
    
    public Edge(int start, int end, boolean isDirect) {
        this.nodeStart = start;
        this.nodeEnd = end;
        this.direct = isDirect;
    }

    public int getNodeStart() {
        return nodeStart;
    }

    public void setNodeStart(int nodeStart) {
        this.nodeStart = nodeStart;
    }

    public int getNodeEnd() {
        return nodeEnd;
    }

    public void setNodeEnd(int nodeEnd) {
        this.nodeEnd = nodeEnd;
    }

    public boolean isDirect() {
        return direct;
    }

    public void setIsDirect(boolean isDirect) {
        this.direct = isDirect;
    }

    @Override
    public String toString() {
        return "Edge{ " + "(" + nodeStart + "," + nodeEnd + "), direct=" + direct + '}';
    }
    
    
    
}

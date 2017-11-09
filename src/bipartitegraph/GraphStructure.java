package bipartitegraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 *
 * @author markova
 */
public class GraphStructure {
    private int size;
    private int coverMatrix[][];
    private boolean occupiedMatrix[][];
    private int s[];//начало графа
    private int t[];//конец графа
    private List<Edge> pathEdges;//row -> col
    private List<Edge> totalEdges;
    GraphStructure(int size) {
        this.size = size;
        this.coverMatrix = new int[size][size];
        occupiedMatrix = new boolean[size][size];
        this.s = new int[size];
        this.t = new int[size];
        pathEdges = new ArrayList<>();
        totalEdges = new ArrayList<>();
        Arrays.fill(s,1);
        Arrays.fill(t,1);
        buildGraph();
        //initCoverMatrix();
    }
    
    private void initCoverMatrix() {
        for(int i=0; i < size; i++)
            Arrays.fill(coverMatrix[i],0);
    }
    
    //строим процедуру по матрице
    private void buildGraph(/*int[][] matrix*/) {
        coverMatrix = new int[][]
        {
            {1,0,1,0,1},
            {0,0,1,0,0},
            {0,0,0,0,1},
            {1,0,0,1,0},
            {0,1,0,0,0}
        };
    }
    
    void independentZeros() {
        findWay(1, true, true);
        
        for(int i=0; i < size; i++) {
            if (s[i] == 1) {
                findWay(i, true, true);
                printWay(i);
                //обращаем дуги
                reverseEdges();
                pathEdges.clear();
                //clearOccupiedMatrix();
                s[i] = 0;//закрываем вход
            }               
        }
    }
    
    void clearOccupiedMatrix() {
        occupiedMatrix = new boolean[size][size];
    }
    void findWay(int index, boolean isRow, boolean isDistrict) {
        //работаем со строкой
        if (isRow) {         
            //выход уже есть
            if (t[index] == 1 && !isDistrict) {
                t[index] = 0;//закрываем выход
            }
            else {
                //ищем в строке колонку со значением 1 (или -1)
                int col = findNodeInRow(index, 1);
                //нашли - прямая дуга
                if (col >= 0) {
                    pathEdges.add(new Edge(index, col, true));
                    occupiedMatrix[index][col] = true;
                    findWay(col, false, true);
                }
                //не нашли - ищем обратную дугу
                else {
                    col = findNodeInRow(index, -1);
                    //нашли обратную дугу
                    if (col >=0) {
                        pathEdges.add(new Edge(col, index, false));
                        occupiedMatrix[index][col] = true;
                        findWay(col, false, false);
                    }
                }                
            }
        }
        //работаем с колонкой
        else {
            int row = findNodeInCol(index, 1);
            //нашли - прямая дуга
            if (row >=0) {
                pathEdges.add(new Edge(row, index, true));
                occupiedMatrix[row][index] = true;
                findWay(row, true, true);
            }
            //не нашли - ищем обратнуюу дугу
            else {
                row = findNodeInCol(index, -1);
                if (row >= 0) {
                    pathEdges.add(new Edge(index, row, false));
                    occupiedMatrix[row][index] = true;
                    findWay(row, true, false);
                }   
            }
        }
    }
    
    private void printWay(int row) {
        System.out.printf("\n***ПУТЬ ИЗ ВЕРШИНЫ %d***\n", row);
        for(Edge edge: pathEdges) {
           System.out.println(edge.toString());
        }
    }
    
    private int findNodeInRow(int row, int val) {
        for(int i=0; i < size; i++) {
            if (coverMatrix[row][i] == val && !occupiedMatrix[row][i]) {
                return i;
            }
        }
        return -1;
    }
    
    private int findNodeInCol(int col, int val) {
        for(int i=0; i < size; i++) {
            if (coverMatrix[i][col] == val && !occupiedMatrix[i][col]) {
                return i;
            }
        }
        return -1;
    }
    
    //обращение ребер
    private void reverseEdges() {
        pathEdges.stream().forEach((edge) -> {
            int row = edge.getNodeStart();
            int col = edge.getNodeEnd();
            coverMatrix[row][col] = -1*coverMatrix[row][col]; 
        });       
    }
    
    private void mergeEdgesToResult(List<Edge> path) {
        path.stream().forEach((edge) -> {
            //подумать над этим
            if (!filterOppositeEdges(edge))
                   totalEdges.add(edge);
        });
    }
    
    private boolean filterOppositeEdges(Edge backEdge) {
        int row = backEdge.getNodeStart();
        int col = backEdge.getNodeEnd();
        int oldSize = totalEdges.size();
        totalEdges.stream().filter(edge -> edge.getNodeStart() != col && edge.getNodeEnd() != row);
        int newSize = totalEdges.size();
        return (oldSize != newSize); //true -> если фильтрация произошла
    }
  
}

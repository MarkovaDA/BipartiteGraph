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
    private int[][] coverXMatrix, coverYMatrix;
    private boolean occupiedMatrix[][];
    private int s[];//начало графа
    private int t[];//конец графа
    private List<Edge> pathEdges;//row -> col
    private List<Edge> totalEdges;
    GraphStructure(int size) {
        this.size = size;
        this.coverXMatrix = new int[size][size];
        this.coverYMatrix = new int[size][size];
        occupiedMatrix = new boolean[size][size];
        this.s = new int[size];
        this.t = new int[size];
        pathEdges = new ArrayList<>();
        totalEdges = new ArrayList<>();
        Arrays.fill(s,1);
        Arrays.fill(t,1);
        buildGraph();
        //две матрицы связности - XY и YX
        //initCoverMatrix();
    }
    
    private void initCoverMatrix() {
        for(int i=0; i < size; i++)
            Arrays.fill(coverXMatrix[i],0);
    }
    
    //строим процедуру по матрице
    private void buildGraph(/*int[][] matrix*/) {
        //s = new int[]{0,1,0,0,0};
        //t = new int[]{0,1,0,0,0};
        //как связаны X и Y
        coverXMatrix = new int[][]
        {
            {1,0,1,0,1},
            {0,0,1,0,0},
            {0,0,0,0,1},
            {1,0,0,1,0},
            {0,1,0,0,0}
        };
        //как связаны Y и X
        coverYMatrix = new int[][]
        {
            {0,0,0,0,0},
            {0,0,0,0,0},
            {0,0,0,0,0},
            {0,0,0,0,0},
            {0,0,0,0,0}
        };
    }
    
    void independentZeros() {
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
    boolean IsThereExit(int index) {
        if (t[index] == 1) {
            t[index] = 0;
            return true;
        }
        return false;
    }
    
    void findWay(int index, boolean isRow, boolean isDistrict) {
        //работаем со строкой
        if (isRow) {         
            int col = findStrictEdge(index, true);
            //нашли - прямая дуга
            if (col >= 0) {
                pathEdges.add(new Edge(index, col, true));
                occupiedMatrix[index][col] = true;
                if  (!IsThereExit(col)) 
                    findWay(col, false, true);
            }
            //не нашли - ищем обратную дугу
            else {
                col = findBackEdge(index, true);
                //нашли обратную дугу
                if (col >=0) {
                    pathEdges.add(new Edge(col, index, false));
                    occupiedMatrix[index][col] = true;
                    findWay(col, false, false);
                }
            }                

        }
        //работаем с колонкой
        else {
            int row = findStrictEdge(index, false);
            //нашли - прямая дуга
            if (row >=0) {
                pathEdges.add(new Edge(row, index, true));
                occupiedMatrix[row][index] = true;
                if  (!IsThereExit(row))
                    findWay(row, true, true);
            }
            //не нашли - ищем обратнуюу дугу 
            else {
                row = findBackEdge(index, false);
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
    
    private int findStrictEdge(int index, boolean isRow) {
       if (isRow) {
            for(int i=0; i < size; i++) {
                if (coverXMatrix[index][i] == 1 && !occupiedMatrix[index][i]) {
                    return i;
                }
            }
       }
       else {
            for(int i=0; i < size; i++) {
                if (coverXMatrix[i][index] == 1 && !occupiedMatrix[i][index]) {
                    return i;
                }
            }
       }
       return -1;
    }
    
    private int findBackEdge(int index, boolean isRow) {
       if (isRow) {
            for(int i=0; i < size; i++) {
                if (coverYMatrix[index][i] == 1 && !occupiedMatrix[index][i]) {
                    return i;
                }
            }
       }
       else {
            for(int i=0; i < size; i++) {
                if (coverYMatrix[i][index] == 1 && !occupiedMatrix[i][index]) {
                    return i;
                }
            }
       }
       return -1;
    }
    //обращение ребер
    private void reverseEdges() {
        pathEdges.stream().forEach((edge) -> {
            int row = edge.getNodeStart();
            int col = edge.getNodeEnd();
            if (edge.isDirect()) {
                coverXMatrix[row][col] = 0; 
                coverYMatrix[row][col] = 1;
            }
                
            else {
                coverYMatrix[col][row] = 0; 
                coverXMatrix[col][row] = 1;
            }
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

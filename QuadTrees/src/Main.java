import java.util.*;
import java.io.*;

class imageClass{
    int numRows;
    int numCols;
    int minVal;
    int maxVal;
    int squareSize;
    int imgAry[][];

    //constructor
    public imageClass(int numRows, int numCols, int minVal, int maxVal){
        this.numRows = numRows;
        this.numCols = numCols;
        this.minVal = minVal;
        this.maxVal = maxVal;
    }

    //computes the square
    public int computeSquare(){
        int square = Math.max(numRows, numCols);
        int power2 = 1;
        while(square >= power2){
            power2 *= 2;
        }
        squareSize = power2;
        return power2;
    }


    public void zero2DAry(int[][] imgAry){
        for(int i = 0; i < imgAry.length; i++){
            for(int j = 0; j < imgAry[i].length; j++){
                imgAry[i][j] = 0;
            }
        }
    }

    public void loadImage(Scanner scan, int [][] imgAry){
        for(int i = 0; i < imgAry.length; i++){
            for(int j = 0; j < imgAry[i].length; j++){
                if(scan.hasNext()) {
                    imgAry[i][j] = Integer.parseInt(scan.next());
                }
            }
        }
    }
}

class QtTreeNode{
    int color;
    int upperR;
    int upperC;
    int squareSize;

    QtTreeNode NWkid = null;
    QtTreeNode NEkid = null;
    QtTreeNode SWkid = null;
    QtTreeNode SEkid = null;

    QtTreeNode(int upperR, int upperC, int color, int squareSize, QtTreeNode NWkid, QtTreeNode NEkid, QtTreeNode SWkid, QtTreeNode SEkid){
        this.upperR = upperR;
        this.upperC = upperC;
        this.squareSize = squareSize;
        this.NWkid = NWkid;
        this.NEkid = NEkid;
        this.SWkid = SWkid;
        this.SEkid = SEkid;
    }

    public boolean isLeaf(){
        if(this.NWkid == null && this.NEkid == null && this.SWkid == null && this.SEkid == null){
            return true;
        }
        else{
            return false;
        }
    }

    public void printQtNode(QtTreeNode node, File outFile) throws IOException {
        FileWriter writer = new FileWriter(outFile, true);
        if (node.NWkid == null && node.NEkid == null && node.SWkid == null && node.SEkid == null) {
            writer.write("Color:" + node.color + ", upperC:" + node.upperC + ", upperR: " + node.upperR + ", NWkid Color:" + null + ", NEkid Color:" + null + ", SWkid Color:" + null + ", SEkid Color:" + null + "\n");
        }
        else{
            writer.write("Color:" + node.color + ", upperC:" + node.upperC + ", upperR: " + node.upperR + ", NWkid Color:" + node.NWkid.color + ", NEkid Color:" + node.NEkid.color + ", SWkid Color:" + node.SEkid.color + ", SEkid Color:" + node.SWkid.color + "\n");
        }
        writer.close();
    }
}

class buildQuadTree{
    QtTreeNode QtRoot;

    buildQuadTree(QtTreeNode QtRoot){
        this.QtRoot = QtRoot;
    }

    public QtTreeNode buildQuadTree(int [][] imgAry, int upR, int upC, int size){
        QtTreeNode newQtNode = new QtTreeNode (upR, upC, -1, size, null, null, null, null);

        if(size ==1){
            newQtNode.color = imgAry[upR][upC];
        }
        else{
            int halfSize = size / 2;
            newQtNode.NWkid = buildQuadTree(imgAry, upR, upC, halfSize);
            newQtNode.NEkid = buildQuadTree(imgAry, upR, upC + halfSize, halfSize);
            newQtNode.SWkid = buildQuadTree(imgAry, upR + halfSize, upC, halfSize);
            newQtNode.SEkid = buildQuadTree(imgAry, upR + halfSize, upC + halfSize, halfSize);

            int sumColor = newQtNode.NWkid.color + newQtNode.NEkid.color + newQtNode.SWkid.color + newQtNode.SEkid.color;
            if(sumColor == 0){
                newQtNode.color = 0;
                newQtNode.NWkid = null;
                newQtNode.NEkid = null;
                newQtNode.SWkid = null;
                newQtNode.SEkid = null;
            }
            else if (sumColor ==4){
                newQtNode.color = 1;
                newQtNode.NWkid = null;
                newQtNode.NEkid = null;
                newQtNode.SWkid = null;
                newQtNode.SEkid = null;
            }
            else{
                newQtNode.color = 5;
            }
        }
        return newQtNode;
    }

    public void preOrder(QtTreeNode Qt, File outFile) throws IOException {
        if(Qt.isLeaf()){
            Qt.printQtNode(Qt, outFile);
            return;
        }
        else{
            Qt.printQtNode(Qt, outFile);
            preOrder(Qt.NWkid, outFile);
            preOrder(Qt.NEkid, outFile);
            preOrder(Qt.SWkid, outFile);
            preOrder(Qt.SEkid, outFile);


        }
    }

    public void postOrder(QtTreeNode Qt, File outFile) throws IOException {
        if(Qt.isLeaf()){
            Qt.printQtNode(Qt, outFile);
            return;
        }
        else{
            preOrder(Qt.NWkid, outFile);
            preOrder(Qt.NEkid, outFile);
            preOrder(Qt.SWkid, outFile);
            preOrder(Qt.SEkid, outFile);
            Qt.printQtNode(Qt, outFile);

        }
    }
}

public class Main {

    public static void main(String[] args) throws IOException {
        File inFile = new File(args[0]);
        File outFile1 = new File (args[1]);
        File outFile2 = new File (args[2]);

        if(outFile1.exists()){
            outFile1.delete();
        }
        if(outFile2.exists()){
            outFile2.delete();
        }
        outFile1.createNewFile();
        outFile2.createNewFile();

        Scanner sc = new Scanner(inFile);
        int numRows = Integer.parseInt(sc.next());
        int numCols = Integer.parseInt(sc.next());
        int minVal = Integer.parseInt(sc.next());
        int maxVal = Integer.parseInt(sc.next());
        imageClass newimage = new imageClass(numRows, numCols, minVal, maxVal);
        int squareSize = newimage.computeSquare();
        newimage.imgAry = new int[squareSize][squareSize];
        newimage.zero2DAry(newimage.imgAry);
        newimage.loadImage(sc, newimage.imgAry);
        buildQuadTree QtRoot = new buildQuadTree(null);
        QtRoot.QtRoot = QtRoot.buildQuadTree(newimage.imgAry, 0, 0, squareSize);
        QtRoot.preOrder(QtRoot.QtRoot, outFile1);
        QtRoot.postOrder(QtRoot.QtRoot, outFile1);

    }

}




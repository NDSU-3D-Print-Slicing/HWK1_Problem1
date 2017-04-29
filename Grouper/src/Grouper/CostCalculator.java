package Grouper;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * NDSU-JPL 3D Printing
 * Problem 1
 * Algorithm to calculate full cost of a single most-efficient tree.
 * 4-28-17
 * @author Ryan
 */
public class CostCalculator {
    
    /**
     * Calculates the cost of a single tree.
     * @param W width in grid points
     * @param H height in grid points
     * @return cost of tree structure
     */
    public static double treeCost(int W, int H){
        
        int pointsFullGroup = (int) Math.floor(W/2) + 1; //in points
        int costStem = H - pointsFullGroup; //in points
        double costFullGroup;
        
        if(W < 5){
            
            costFullGroup = groupCost(W);
        }
        else{
   
            costFullGroup = subtreeCost(W,pointsFullGroup);
        }
        
        return costStem + costFullGroup;
    }
    
    /**
     * Recursive helper function to calculate cost of full grouping of subtree.
     * @param W width in grid points
     * @param H height in grid points of full grouping of subtree
     * @return cost of subtree full grouping
     */
    public static double subtreeCost(int W, int H){
        
        //base case
        if(W < 5){
            return groupCost(W);
        }
        //recursive case
        else{
            
            double totalLeft = 0;
            double totalRight = 0;
            double totalLeftover = 0;
            
            //calculate height in points of full grouping
            int fullGroupHeight = (int) Math.floor(W/2) + 1;
            
            //subdivide subtree into two smaller subtrees
            int [] subtrees;
            subtrees = getSubtrees(W);
           
            //determine cost of each of the smaller subtrees
            totalLeft = subtreeCost(subtrees[0], fullGroupHeight);
            totalRight = subtreeCost(subtrees[1], fullGroupHeight);
            
            //calculate cost of leftover
            totalLeftover = costLeftover(W,fullGroupHeight,subtrees);
            
            
            //return cost of full grouping of this subtree
            return totalLeft + totalRight + totalLeftover;
        }//end if
    }
    
    /**
     * Helper function to calculate the cost of the leftover structure in given subtree.
     * @param W width in grid points of given subtree
     * @param H height in grid points of full grouping of given subtree
     * @param subtrees integer array represents two subtrees of this subtree
     * @return cost of leftover structure
     */
    public static double costLeftover(int W, int H, int [] subtrees){
        
        double total = 0;
        int leftWidth = subtrees[0];
        int rightWidth = subtrees[1];
       
        //calculate height of each subgroup in grid points
        int leftGroupHeight = (int) Math.floor(leftWidth/2) + 1;
        int rightGroupHeight = (int) Math.floor(rightWidth/2) + 1;
        
        //calculate height of each diagonal in units
        int leftDiagonalHeight = H - leftGroupHeight;
        int rightDiagonalHeight = H - rightGroupHeight;
        
        
        if(W % 2 == 0){ //one diagonal has a cant (left always?)
            
            total += (leftDiagonalHeight - 1) * Math.sqrt(2) + 1;
            total += rightDiagonalHeight * Math.sqrt(2);
        }
        else{ //neither diagonal has a cant
            
            total += rightDiagonalHeight * Math.sqrt(2);
            total += leftDiagonalHeight * Math.sqrt(2);
        }
        
        
        return total;
    }
    
    /**
     * Helper function to subdivide tree into subtrees.
     * @param W width in points of tree to subdivide
     * @return array of two subtrees with left as index 0
     */
    public static int [] getSubtrees(int W){
        
        int leftWidth = (int) Math.floor(W/2);
        int rightWidth = W - leftWidth;
        
        //assume it is more efficient to subdivide tree into (odd,odd) or (odd,even) groups
        //rather than (even,even)
        if(leftWidth % 2 == 0 && rightWidth % 2 == 0){
            leftWidth -= 1;
            rightWidth = W - leftWidth;
        }
        
        int [] values = {leftWidth, rightWidth};
        
        return values;
    }
    
    /**
     * Helper function to get cost of base step subgroups.
     * @param W width in points of tree
     * @return cost of subgrouping
     */
    public static double groupCost(int W){
        
        double cost = 0;
        
        switch (W) {
            case 1:
                cost = 1;
                break;
            case 2:
                cost = Math.sqrt(2) + 1;
                break;
            case 3:
                cost = 2 * Math.sqrt(2) + 1;
                break;
            case 4:
                cost = 3 * Math.sqrt(2) + 3;
                break;
        }
        
        return cost;
    }

    //testing
    public static void main(String[] args) {
        System.out.println(treeCost(15,13));
    }
}



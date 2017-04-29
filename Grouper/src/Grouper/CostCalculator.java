package Grouper;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ryan
 */
public class CostCalculator {
    
    /**
     * Calculates the cost of a single tree that has a stem.
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
 
    
    
    
    
    
    
    
    
    
    
    /*
    
    //old version
    public static double total(int W, int H){
        
        //note: does not yet work for trees of height 3 pts only!!
        //base case
        if(W < 5){
            switch(W){
                case 1:
                    return 1;
                case 2:
                    return Math.sqrt(2)+1;   
                case 3:
                    return 2*Math.sqrt(2)+1;    
                case 4:
                    return 3*Math.sqrt(2)+3;   
                default:
                    return 0; //should never occur
            }//end switch
        }//end if
        else { //recursive step
            
            double sub1 = 0;
            double sub2 = 0;
            double sub3 = 0;
            
            //calculate height of full grouping
            int fullGroupHeight = (int) Math.floor(W/2) + 1;
            
            //subdivide width into two subtrees
            int leftSubWidth = (int) Math.floor(W/2);
            int rightSubWidth = W - leftSubWidth;
            
            //check for even + even and change to odd + odd or odd + even
            if(leftSubWidth % 2 == 0 && rightSubWidth % 2 == 0){
                leftSubWidth -= 1;
                rightSubWidth = W - leftSubWidth;
            }
            
            //System.out.println("left: " + leftSubWidth + ", right: " + rightSubWidth);
            
            //calculate leftover
            sub1 = leftover(W,H, leftSubWidth, rightSubWidth);
            
            //continue subdividing
            sub2 = total(leftSubWidth, fullGroupHeight);
            sub3 = total(rightSubWidth, fullGroupHeight);
            //System.out.println("sub1: " + sub1 + "\nsub2: " + sub2 + "\nsub3: " + sub3 + "\n\n");
            return sub1 + sub2 + sub3;
        }    
    }//end total
    
    
    
    
    
    
    public static double leftover(int W, int H, int leftWidth, int rightWidth){

        int leftFullGroup = (int) Math.floor(leftWidth/2) + 1;
        int leftRemainder = H - leftFullGroup;
        
        int rightFullGroup = (int) Math.floor(rightWidth/2) + 1;
        int rightRemainder = H - rightFullGroup;
        
        double total = 0;
        
        //cases to consider:
        //stem later
        //diagonal vee (even)
        //diagonal vee (uneven)
        //diagonal cant (even)
        //diagonal cant (uneven)
        
        //Case 1: W is even (we have a cant on the one side) //?? left only??
        
        if(W % 2 == 0){
            int rightRemainderHeight = rightRemainder; //square
            int leftRemainderHeight = leftRemainder - 1; //square
            
            total = rightRemainderHeight * Math.sqrt(2) +
                    leftRemainderHeight * Math.sqrt(2) + 1;
            
        }
        else{ //W is odd (no cant on either side)
            int rightRemainderHeight = rightRemainder;
            int leftRemainderHeight = leftRemainder;
            
            total = rightRemainderHeight * Math.sqrt(2) +
                    leftRemainderHeight * Math.sqrt(2);
        }     
        return total;  
    }   
*/
    
    public static void main(String[] args) {
        System.out.println(treeCost(15,14));
    }
}



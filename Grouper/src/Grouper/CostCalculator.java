package Grouper;

import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * NDSU-JPL 3D Printing Problem 1 Algorithm to calculate full cost of a single
 * most-efficient tree. 4-28-17
 *
 * @author Ryan
 */
public class CostCalculator {

    //public methods 
    /**
     * Subdivides given width into most-efficient n-trees based on given height, the calculates and returns total cost.
     * @param W width in grid units
     * @param H height in grid units
     * @return cost in length units
     */
    public static double divideAndCost(int W, int H){
        List<Integer> trees = divideIntoTrees(W, H);
        return listCost(trees, H);
    }

    //private methods
    /**
     * Subdivides given width into most-efficient n-trees based on given height, then
     * returns the list of n-trees. H > 1 because no tree can be formed by a single point.
     *
     * @param W width to be supported in grid points
     * @param H height to be supported in grid points
     * @return List of integer subtrees representing the width in grid points of all n-trees required to support W
     */
    private static List<Integer> divideIntoTrees(int W, int H) {

        List<Integer> trees = new ArrayList<>();

        int maxWidth = 2 * H - 3;    //max points efficiently supported by single stem for given H

        //base step
        if (W <= maxWidth) {

            trees.add(W); //entire structure should be supported by 1 stem
        } //recursive step
        else {
            //divide W into 2 halves
            int left = (int) Math.floor(W / 2);
            int right = W - left;

            //check whether each half needs to be further subdivided
            List<Integer> leftTrees = divideIntoTrees(left, H);
            List<Integer> rightTrees = divideIntoTrees(right, H);

            //merge lists of trees
            trees.addAll(leftTrees);
            trees.addAll(rightTrees);
        }
        return trees;
    }
    
    /**
     * Calculates the cost of all n-trees in a given list.
     * Assumes all trees have the same height.
     * @param trees List of integer subtrees representing n-trees
     * @param H height for every tree to be supported in grid points
     * @return cost in length units of all tree structures in list
     */
    private static double listCost(List<Integer> trees, int H) {

        double total = 0;

        for (int n : trees) {
            total += treeCost(n, H);
        }

        return total;
    }

    /**
     * Calculates the cost of a single tree, where cost is measured as length.
     *
     * @param W width of n-tree in grid points
     * @param H height of n-tree in grid points
     * @return cost in length units of given tree structure
     */
    private static double treeCost(int W, int H) {

        double costFullGroup, costStem;

        if (W == 1) {
            costFullGroup = 1;  //cost to connect two grid points vertically
            costStem = H - 2;
        } else {

            //determine the height in grid points of the full grouping
            //use result to calculate cost of the stem
            int pointsFullGroup = (int) Math.floor(W / 2) + 1; //in points  
            costStem = H - pointsFullGroup; //result in length units

            //calculate the cost of the full grouping
            if (W < 5) {
                costFullGroup = groupCost(W);
            } else {
                costFullGroup = subtreeCost(W, pointsFullGroup);
            } //end if
        }//end if

        return costStem + costFullGroup;
    }

    /**
     * Calculates cost of full grouping of subtree recursively.
     *
     * @param W width of n-subtree in grid points
     * @param H height of full grouping of n-subtree in grid points
     * @return cost of n-subtree full grouping in length units
     */
    private static double subtreeCost(int W, int H) {

        //base case
        if (W < 5) {
            return groupCost(W);
        } //recursive case
        else {

            double costLeft, costRight, costLeftover;

            //calculate height in points of full grouping
            int fullGroupHeight = (int) Math.floor(W / 2) + 1;

            //subdivide subtree into two smaller subtrees
            int[] subtrees = getSubtrees(W);

            //determine cost of each of the smaller subtrees
            costLeft = subtreeCost(subtrees[0], fullGroupHeight);
            costRight = subtreeCost(subtrees[1], fullGroupHeight);

            //calculate cost of leftover
            costLeftover = costLeftover(W, fullGroupHeight, subtrees);

            //return cost of full grouping of this subtree
            return costLeft + costRight + costLeftover;
        }//end if
    }

    /**
     * Calculate the cost of the leftover structure in given subtree.
     *
     * @param W width in grid points of given n-subtree
     * @param H height in grid points of full grouping of given n-subtree
     * @param subtrees integer array represents two smaller subtrees of this
     * n-subtree
     * @return cost of leftover structure in length units
     */
    private static double costLeftover(int W, int H, int[] subtrees) {

        double cost = 0;
        
        //get width in grid points of each subtree
        int leftWidth = subtrees[0];    
        int rightWidth = subtrees[1];   

        //calculate height of each subgroup in grid points
        int leftGroupHeight = (int) Math.floor(leftWidth / 2) + 1;
        int rightGroupHeight = (int) Math.floor(rightWidth / 2) + 1;

        //calculate vertical height of each leftover diagonal in length units
        int leftDiagonalHeight = H - leftGroupHeight;
        int rightDiagonalHeight = H - rightGroupHeight;
        int combinedHeight = leftDiagonalHeight + rightDiagonalHeight;

        //add up cost of diagonals
        if (W % 2 == 0) { //one diagonal has a cant

            cost += ((combinedHeight - 1) * Math.sqrt(2) + 1); //one less diagonal member, has one vertical member
            
        } else { //neither diagonal has a cant

            cost += (combinedHeight * Math.sqrt(2));
        }

        return cost;
    }

    /**
     * Subdivides n-tree into subtrees.
     * Assumes greater tree structure is more efficient with subgroups divided
     * into odd, odd or odd, even pairs, rather than even, even pairs.     *
     * @param W width in points of n-tree to subdivide
     * @return integer array of two n-subtrees with left subtree as index 0
     */
    private static int[] getSubtrees(int W) {

        int leftWidth = (int) Math.floor(W / 2); //in grid points
        int rightWidth = W - leftWidth;

        //ensures pair of subtrees is (odd,odd) or (odd,even), not (even, even)
        if (leftWidth % 2 == 0 && rightWidth % 2 == 0) {
            
            //alter subtree widths to remove (even, even) pairing
            leftWidth -= 1;
            rightWidth = W - leftWidth;
        }

        //store subtrees in integer array
        int[] subtrees = {leftWidth, rightWidth};

        return subtrees;
    }

    /**
     * Calculates cost of full grouping of indivisible n-groups.
     * These include 1-, 2-, 3-, and 4-groups.
     *
     * @param W width in points of n-group
     * @return cost of n-group in length units
     */
    private static double groupCost(int W) {

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
        System.out.println(divideIntoTrees(10, 4));
        System.out.println(divideAndCost(10, 4));
        //System.out.println(divideAndCost(2000000000, 1000));
        
        System.out.println("Check against manually calculated square trees");
        for(int i = 2; i < 16; i++){
            System.out.println(i +":" + treeCost(i,i));
        }
        System.out.println("Check if works for minimum height");
        System.out.println(divideIntoTrees(100, 2));
        System.out.println(divideAndCost(100, 2));
        
    }
}

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
 
    
    public static double total(int W, int H){
        
        //note: does not yet work for trees of height 3 pts only!!
        //base case
        if(W < 5){
            switch(W){
                case 1:
                    return 1;
                case 2:
                    return 2*Math.sqrt(2)+1;    //includes extra sqrt(2) as single diagonal connection
                case 3:
                    return 3*Math.sqrt(2)+1;    //includes extra sqrt(2) as single diagonal connection
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
    
    public static void main(String[] args) {
        System.out.println(total(9,5));
    }
}



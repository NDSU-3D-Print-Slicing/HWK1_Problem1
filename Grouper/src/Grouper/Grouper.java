package Grouper;



import java.util.ArrayList;
import java.util.List;

/**
 * 4-24-17
 * 
 * Program is designed to take as input a structure height and width, then determine the n-groups to construct the structure.
 * @author Ryan
 */
public class Grouper {
    
    
    public static List<Integer> subgroup(int H, int W){
        
        List<Integer> groups = new ArrayList<>();
        List<Integer> sub1, sub2;
        
       //base case
       if(W < 5){
           groups.add(W);
       }
       else{
           sub1 = subgroup(H, (int) Math.floor(W/2));
           sub2 = subgroup(H, W - (int) Math.floor(W/2));
           
           groups.addAll(sub1);
           groups.addAll(sub2);
       }
       return groups;
    }
    
    public static List<Integer> subgroup2(int H, int W){
        List<Integer> groups = new ArrayList<>();
        List<Integer> sub1, sub2;
       //base case
       if(W < 5){
           groups.add(W);
       }
       else{
           int lower = (int) Math.floor(W/2);
           int upper = W - lower;
           
           //odd + odd or odd + even is more efficient than even + even
           if(lower % 2 == 0 && upper % 2 == 0){
               lower -= 1;
               upper = W - lower;
           }
           System.out.print("\n middle {" + lower + "," + upper + "} becomes");
           sub1 = subgroup2(H, lower);
           sub2 = subgroup2(H, upper);
           
           groups.addAll(sub1);
           groups.addAll(sub2);
       }
       return groups;
    }
    
    public static void printSubgroups(int H, List<Integer> list){
        System.out.print("{");
        List<Integer> current;
        
        for(int i = 0; i < list.size(); i++)
        {
            current = subgroup2(H, list.get(i));
            print(current);
            
            if(i < list.size() - 1){
                System.out.print(",");
            }
        }
        System.out.print("}");
    }
    
    
    public static List<Integer> group(int H, int W){
        
        List<Integer> groups = new ArrayList<>();
        int maxWidth = 2*H - 3;    //max points efficiently supported by single stem
        
        if(W <= maxWidth){ //entire structure supported by 1 stem
            groups.add(W);
        }
        else {  //structure must be subdivided into multiple trees

            //if maxWidth not divisor of W, final grouping of maxWidth
            //added to remainder and divided in half
            int groups_max, extra, group1, group2;
            
            if(W % maxWidth == 0){  //maxWidth divisor of W
                groups_max = W/maxWidth; //divide evenly into groups of maxWidth each
            }
            else{ 
                groups_max = W/maxWidth - 1; //last group is added to remainder of W % maxWidth
                extra = W - (maxWidth * groups_max);
                
                group1 = (int) Math.floor(extra/2);
                group2 = extra - group1;
                
                groups.add(group1);
                groups.add(group2);
            }
            
            for(int i = 0; i < groups_max; i++){
                groups.add(maxWidth);
            }
        }
        return groups;
    }
    
    public static int stemHeight(int H, int D){
        return H - (int) Math.floor(D/2); 
    }
    
    public static void printStems(int H, List<Integer> list){
        System.out.print("Stems: {");
        for(int i = 0; i < list.size(); i++)
        {
            int current = stemHeight(H, list.get(i));
            System.out.print(current);
            
            if(i < list.size() - 1){
                System.out.print(",");
            }
        }
        System.out.print("}");
    }

    
    public static void print(List<Integer> list){
        System.out.print("{");
        for(int i = 0; i < list.size(); i++)
        {
            System.out.print(list.get(i));
            
            if(i < list.size() - 1){
                System.out.print(",");
            }
        }
        System.out.print("}");
    }
    
    public static void main(String[] args) {
        int height = 15;
        int width = 60;
        
        System.out.println("Height Limit: " + height + " pts");
        System.out.println("Width: " + width + " pts");
        System.out.print("n-Groups: ");
        print(group(height,width));
        System.out.println("");
        System.out.println("Number of groups: " + group(height,width).size());
        
        System.out.println("\nStem Heights (in points):");
        printStems(height, group(height,width));
        
        System.out.println("\n\nSubgroups:");
        printSubgroups(height, group(height, width));
        System.out.println("");
    }
}

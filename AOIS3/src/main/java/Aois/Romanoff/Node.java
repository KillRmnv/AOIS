package Aois.Romanoff;

import lombok.Data;
import lombok.Setter;

@Data
public class Node {
    @Setter
    private Integer left;
    private Integer right;
    private Integer up;
    private Integer down;
    private Integer rightUp;
    private Integer rightDown;
    private Integer leftUp;
    private Integer leftDown;
    private int data;
    String greyCode;

    public Node(int data,String greyCode) {
        this.data = data;
        this.left = -10;
        this.right = -10;
        this.up = -10;
        this.down = -10;
        this.rightUp = -10;
        this.rightDown = -10;
        this.leftUp = -10;
        this.leftDown = -10;
        this.greyCode = greyCode;
    }
    public Node(Node node) {
        this.left = node.left;
        this.right = node.right;
        this.up = node.up;
        this.down = node.down;
        this.rightUp = node.rightUp;
        this.rightDown = node.rightDown;
        this.leftUp = node.leftUp;
        this.leftDown = node.leftDown;
        this.data = node.data;
        this.greyCode = node.greyCode;

    }
    int[] getDirections(){
        int[] directions=new int[8];
        directions[0]=left;
        directions[1]=right;
        directions[2]=up;
        directions[3]=down;
        directions[4]=rightUp;
        directions[5]=rightDown;
        directions[6]=leftUp;
        directions[7]=leftDown;

        return directions;
    }
    public  void setDirections(int[] directions){
        left=directions[0];
        right=directions[1];
        up=directions[2];
        down=directions[3];
        rightUp=directions[4];
        rightDown=directions[5];
        leftUp=directions[6];
        leftDown=directions[7];

    }
}
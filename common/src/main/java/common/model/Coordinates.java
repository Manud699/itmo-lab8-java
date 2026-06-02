package common.model;

import java.io.Serializable;

/**
 * class Coordinates 
 * @author manu_d699
 */
public class Coordinates implements Serializable {

    private static final long serialVersionUID = 1L;

    private float x;
    private Double y;


    public Coordinates(float x, Double y) {
        setX(x);
        setY(y);
    } 


    public float getX() {
        return x; 
    } 
    

    public Double getY() {
        return y; 
    } 


    public void setX(float x){
        if(x < -497){
            throw new IllegalArgumentException("error.val.xCoord");
        }
        this.x = x;
    }


    public void setY(double y){
        if(y < -764){
            throw new IllegalArgumentException("error.val.yCoord");
        }
        this.y =y;
    }
}

package common.model;


import java.io.Serializable;

/**
 * class Organization 
 * @author manu_d699
 */
public class Organization implements Serializable {

    private String fullName; //Длина строки не должна быть больше 694, Поле не может быть null
    private float annualTurnover; //Значение поля должно быть больше 0
    private int employeesCount; //Значение поля должно быть больше 0


    public Organization(String fullName, float anuualTurnover, int employeesCount) {
        setFullName(fullName);
        setAnnualTurnover(anuualTurnover);
        setEmployeesCount(employeesCount);
    } 


    public String getFullName() {
        return fullName; 
    } 


    public float getAnnualTurnover() {
        return annualTurnover; 
    } 


    public int getEmployeesCount() {
        return employeesCount;
    }


    @Override
    public String toString() {
        return "Organization{" +
                "fullName='" + fullName + '\'' +
                ", annualTurnover=" + annualTurnover +
                ", employeesCount=" + employeesCount +
                '}';
    }

    public void setFullName(String fullName){
        if(fullName.length() > 694) {
            throw new IllegalArgumentException("error.val.orgAnnual");
        }
        this.fullName = fullName;
    }


    public void setAnnualTurnover(float annualTurnover){
        if(annualTurnover <= 0 ){
            throw new IllegalArgumentException("error.val.orgAnnual");
        }
        this.annualTurnover = annualTurnover;
    }

    public void setEmployeesCount(int employeesCount){
        if(employeesCount <= 0){
            throw new IllegalArgumentException("error.val.orgEmployeesCount");
        }
        this.employeesCount = employeesCount;
    }
}

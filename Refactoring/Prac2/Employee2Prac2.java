package n2_a;

public class Employee2Prac2 {
    public static int STANDARD_WORK_HOURS_PER_DAY = 8;
    double overworkPayment = 1.5;
    int paymentPerHour = 1000;
    public double getPayment(int workingHours) {
        double overWorkingHours = calculateoverWorkingHours(workingHours);
        double paymentForoverWorkingHours = this.paymentPerHour * overworkPayment * overWorkingHours;
        double standardWorkingHours = workingHours - overWorkingHours;
        double paymentForStandardWorkHours = this.paymentPerHour * standardWorkingHours;
        return paymentForoverWorkingHours + paymentForStandardWorkHours;
    }
    public static double calculateoverWorkingHours(double workingHours){
        if(workingHours < STANDARD_WORK_HOURS_PER_DAY){
            return 0;
        }
        else{
            return workingHours - STANDARD_WORK_HOURS_PER_DAY;
        }
    }
}

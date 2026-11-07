import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args){

Scanner input=new Scanner(System.in);
CarPark park=null;

while(input.hasNextLine()){
    String line= input.nextLine().trim();

    if(line.isEmpty()){
        continue;
    }

    String[] field=line.split(" ");
    String command=field[0];

    if(command.equals("END")){
        break;
    }
try{
    if(command.equals("SLOTS")){
        int bikeSlots=Integer.parseInt(field[1]);
        int regularSlots=Integer.parseInt(field[2]);
        int largerSlots=Integer.parseInt(field[3]);

    }
    else if(command.equals("MAXSTAY")){
        int hours=Integer.parseInt(field[1]);
        park.setMaxStay(hours);
    }
    else if(command.equals("BIKE")){
        Bike bike=new Bike(field[1],field[2]);

        park.parkVehicle(bike);
    }
else if(command.equals("CAR")){
    Car car=new Car(field[1],field[2]);
    park.vehicle(car);
    }
    else if(command.equals("TRUCK")){
        Truck truck=new Truck(field[1],field[2] );
        park.parkVehicle(truck);
    }

    else if(command.equals("PASSTIME")){
        int hours= Integer.parseInt(field[1]);
    }
    else if(command.equals("COUNT")){
        System.out.println(park.getVehicleCount());
    }

    else if (command.equals("EARNED")) {

        System.out.println(
                park.getEarned()
        );
    }


    else if (command.equals("REFUSED")) {

        System.out.println(
                park.getRefused()
        );
    }

}
catch(ParkingException e) {
    if (park!= null) {
        park.addRefusal();
    }
    }
    }
    }
}
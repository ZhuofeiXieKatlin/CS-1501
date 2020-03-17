import java.util.Scanner;
import java.io.*;


public class CarTracker{
    public static String VIN = ""; 
    public static String Make =  ""; 
    public static String Model = "";
    public static int price = 0;
    public static int Mileage = 0; 
    public static String color = "";  

    public static int input = 0; 
    public static Scanner scanner = new Scanner(System.in); 
    public static PriorityQueue PQ = new PriorityQueue(); 

    public static void openTXTfile(){
        try{
            File file = new File("cars.txt"); 
            Scanner reader = new Scanner(file); 
            String input = reader.nextLine(); 
            while(reader.hasNextLine()){
                input = reader.nextLine(); 
                String[] components = input.split(":"); 
                VIN = components[0].toUpperCase(); 
                Make = components[1].toUpperCase(); 
                Model = components[2].toUpperCase(); 
                price = Integer.parseInt(components[3]); 
                Mileage = Integer.parseInt(components[4]); 
                color = components[5].toUpperCase(); 
                PriorityQueue.add(new CarInfo(VIN, Make, Model, price, Mileage,color));         
            }
            reader.close();
        }
        catch(FileNotFoundException e){
            System.out.println("Got the error" + e); 
        }
        
    }
    public static void printMainMenu(){
        System.out.println("1. Add a Car");
        System.out.println("2. Update a car information");
        System.out.println("3. Removing a specific car from your consideration");
        System.out.println("4. Retrieve the lowest price car");
        System.out.println("5. Retrieve the lowest mileage car");
        System.out.println("6. Retrieve the lowest price car by make and model");
        System.out.println("7. Retrieve the lowest milage car by make and model");
        System.out.println("\nEnter '8' to exit the program"); 
        System.out.println("----------------------------------------------------");

        System.out.print("Please enter your selection(from 1 to 7)");
    }

    public static void find_Car_With_VIN(){
        System.out.print("Please enter the VIN number here in order to continue : "); 
        VIN = scanner.next().toUpperCase(); 
        boolean search; 
        search = PriorityQueue.search_with_VIN(VIN); 
        while(!search){
            System.out.println("Sorry! We cannot find the car associate with the VIN number that you input!"); 
            System.out.print("Please enter the VIN number of the car that you want to update: "); 
            VIN = scanner.next().toUpperCase();  
            search = PriorityQueue.search_with_VIN(VIN); 
        }
    }

    public static void add_a_Car(){
        try{
            System.out.println("******* Add a Car *********"); 
            System.out.print("Please enter the VIN Number: ");
            VIN = scanner.next().toUpperCase(); 
            System.out.print("Please enter the Make: ");
            Make = scanner.next().toUpperCase(); 
            System.out.print("Please enter the Model: "); 
            Model = scanner.next().toUpperCase(); 
            System.out.print("Please enter the Price: ");
            price = scanner.nextInt(); 
            System.out.print("Please enter the Mileage: "); 
            Mileage = scanner.nextInt(); 
            System.out.print("Please enter the Color: ");
            color = scanner.next().toUpperCase(); 
            PriorityQueue.add(new CarInfo(VIN, Make, Model, price, Mileage,color)); 
            System.out.println();
        }
        catch (Exception InputMismatchException){
            System.out.println("Catching exception: " + InputMismatchException);
            System.out.println("Exiting the program" + "\n......"); 
            System.exit(0);
        }
    }

    public static boolean update_the_price(String VINnumber, int price){
        if(PriorityQueue.update_Price(VINnumber, price)){
            return true; 
        } 
        return false; 
    }

    public static boolean update_the_mileage(String VINnumber, int miles){
        if(PriorityQueue.update_Mileage(VINnumber, miles)){
            return true; 
        }
        return false; 
    }

    public static boolean update_the_color(String VINnumber, String color){
        if(PriorityQueue.update_Color(VINnumber,color)){
            return true; 
        }
        return false; 
    }

    public static void update_a_car(){
        find_Car_With_VIN();
        System.out.println("********** update a car info **********"); 
        /*else{
            System.out.println("Found a car corrsponds with the VIN !!!!"); 
        }*/
        System.out.println("  What would you like to update?"); 
        System.out.println("    <1> The price of the car"); 
        System.out.println("    <2> The mileage of the car"); 
        System.out.println("    <3> The color of the car"); 
        System.out.print("  Your choice (1 to 3): ");
        int choice; 
        boolean success; 
        choice = scanner.nextInt(); 
        while(choice>3 || choice < 1){
            System.out.println("Input out of range, please enter again!"); 
            System.out.print("Please enter your choice again: "); 
            choice = scanner.nextInt(); 
        }
        switch(choice){
            case 1: choice = 1; 
                try{
                    System.out.print("Please enter the price of the car: ");
                    int price = scanner.nextInt(); 
                    success = update_the_price(VIN, price); 
                    if(success){
                        System.out.println("You have successfully update a price for the car!!!!"); 
                    }else{
                        System.out.println("There are something wrong inside the program...."); 
                    }
                }
                catch( Exception InputMismatchException){
                    System.out.println("Catching exception: " + InputMismatchException);
                    System.out.println("Exiting the program" + "\n......"); 
                    System.exit(0);
                }
                break; 
            case 2: choice = 2; 
                try{
                    System.out.print("Please enter the mileage of the car: ");
                    int mileage = scanner.nextInt(); 
                    success = update_the_mileage(VIN, mileage);
                    if(success){
                        System.out.println("You have successfully update a mileage for the car!!!!"); 
                    }else{
                        System.out.println("There are something wrong inside the program...."); 
                    } 
                }
                catch( Exception InputMismatchException){
                    System.out.println("Catching exception: " + InputMismatchException);
                    System.out.println("Exiting the program" + "\n......"); 
                    System.exit(0);
                }
                break; 
            case 3: choice = 3; 
                System.out.print("Please enter the color of the car you want to update: ");
                String color = scanner.next(); 
                success = update_the_color(VIN, color); 
                if(success){
                    System.out.println("You have already update the color of the car successfully! "); 
                } else{
                    System.out.println("There are something wrong inside the program...."); 
                }
                break; 
        }

    }

    public static void remove_a_car(){
        System.out.println("******* Remove a Car *********"); 
        find_Car_With_VIN(); 
        System.out.print("Are you sure to remove the car?(enter 'y' to continue) ");
        String input = scanner.next();  
        //System.out.println("The input is " + input); 
        if(input.toLowerCase().equals("y")){
            boolean has_been_removed; 
            has_been_removed = PriorityQueue.remove_Car(VIN); 
            if(has_been_removed){
                System.out.println("The car has been removed successfully!"); 
            }
            else{
                System.out.println("There are something wrong inside the program....");
            }
        }
        else{
            System.out.println("You didn't enter 'YES'. Sorry the car cannot be removed >_<"); 
        }
    }

    public static CarInfo retrieve_lowest_price_car(){
        System.out.println("******* Retrieve the Car with lowest price *********"); 
        CarInfo lowest_price_car = PriorityQueue.find_lowest_price_car(); 
        return lowest_price_car; 
    }

    public static CarInfo retrieve_lowest_mileage_car(){
        System.out.println("******* Retrieve the Car with lowest mileage *********"); 
        CarInfo lowest_mileage_car = PriorityQueue.find_lowest_mileage_car(); 
        return lowest_mileage_car; 
    }

    
    public static CarInfo retrieve_the_price_with_make_and_model(){
        System.out.println("******* Retrieve the Car with lowest price with make and model *********"); 
        String make; 
        String model; 
        boolean price = true; 
        System.out.print("Please enter the make of the car: ");
        make = scanner.next(); 
        make = make.toUpperCase(); 
        System.out.print("Please enter the model of the car: ");
        model = scanner.next(); 
        model = model.toUpperCase(); 
        CarInfo MM_Car_price = PriorityQueue.retrive_priceCar_with_Make_Model(price,make,model); 
        if(MM_Car_price == null){
            System.out.println("Cannot find a suitable price based on your input! -_-"); 
            return null; 
        }
        return MM_Car_price; 
    }

    public static CarInfo retrieve_the_mileage_with_make_and_model(){
        System.out.println("******* Retrieve the Car with lowest mileage with make and model *********"); 
        String make; 
        String model; 
        boolean price = false; 
        System.out.print("Please enter the make of the car: ");
        make = scanner.next(); 
        make = make.toUpperCase(); 
        System.out.print("Please enter the model of the car: ");
        model = scanner.next(); 
        model = model.toUpperCase(); 
        CarInfo MM_Car_price = PriorityQueue.retrive_priceCar_with_Make_Model(price,make,model); 
        if(MM_Car_price == null){
            System.out.println("Cannot find a suitable price based on your input! -_-"); 
            return null; 
        }
        return MM_Car_price;
    }

    public static void print_car_prices(){
        int size = PriorityQueue.get_size(); 
        CarInfo[] prices = PriorityQueue.return_car_with_prices(); 
        String information; 
        for(int i=0; i<size; i++){
            information =  prices[i].toString(); 
            System.out.print(information); 
        }
    }


    public static void print_car_mileage(){
        int size = PriorityQueue.get_size(); 
        CarInfo[] prices = PriorityQueue.return_car_with_mileage(); 
        String information; 
        for(int i=0; i<size; i++){
            information =  prices[i].toString(); 
            System.out.print(information); 
        }
    }

    public static void continue_the_display_menu(int input){
        //System.out.println("The input number is $" + input);
        switch(input){
            case 1: input = 1; 
                // print_car_prices(); 
                // System.out.println(); 
                // System.out.println("*************************************"); 
                // print_car_mileage();
                add_a_Car();
                // print_car_prices(); 
                // System.out.println("*************************************");
                // print_car_mileage();  
                // System.out.println();
                
                break;  
            case 2: input = 2;
                update_a_car();
                // print_car_prices(); 
                // System.out.println();
                // System.out.println("*************************************"); 
                // print_car_mileage();
                // System.out.println();  
                break; 
            case 3: input = 3;
        
                remove_a_car(); 
                //System.out.println("The size is "+ PriorityQueue.get_size());
                //print_car_prices(); 
                //System.out.println("*************************************"); 
                //print_car_mileage();  
                break; 
            case 4: input = 4;
                CarInfo lowest_price_car; 
                lowest_price_car=retrieve_lowest_price_car();
                if(lowest_price_car != null){
                    System.out.println("$$ This is the car with the lowest price $$"); 
                    System.out.println("The VIN number is " + lowest_price_car.getVin()); 
                    System.out.println("The make is " + lowest_price_car.getMake()); 
                    System.out.println("The model is " + lowest_price_car.getModel()); 
                    System.out.println("The price is $" + lowest_price_car.getPrice()); 
                    System.out.println("The mileage is " + lowest_price_car.getMideage()); 
                    System.out.println("The color is " + lowest_price_car.getColor());  
                    System.out.println();
                }
                break; 
            case 5: input = 5;
                CarInfo lowest_mileage_car; 
                lowest_mileage_car = retrieve_lowest_mileage_car();
                if(lowest_mileage_car != null){
                    System.out.println("$$ This is the car with the lowest mileage $$"); 
                    System.out.println("The VIN number is " + lowest_mileage_car.getVin()); 
                    System.out.println("The make is " + lowest_mileage_car.getMake()); 
                    System.out.println("The model is " + lowest_mileage_car.getModel()); 
                    System.out.println("The price is $" + lowest_mileage_car.getPrice()); 
                    System.out.println("The mileage is " + lowest_mileage_car.getMideage()); 
                    System.out.println("The color is " + lowest_mileage_car.getColor()); 
                    System.out.println();
                } 
                break; 
            case 6: input = 6;
                CarInfo lowest_price_with_make_model; 
                lowest_price_with_make_model = retrieve_the_price_with_make_and_model(); 
                if(lowest_price_with_make_model != null){
                    System.out.println("$$ This is the car with the lowest price with make and model $$"); 
                    System.out.println("The VIN number is " + lowest_price_with_make_model.getVin()); 
                    System.out.println("The make is " + lowest_price_with_make_model.getMake()); 
                    System.out.println("The model is " + lowest_price_with_make_model.getModel()); 
                    System.out.println("The price is $" + lowest_price_with_make_model.getPrice()); 
                    System.out.println("The mileage is " + lowest_price_with_make_model.getMideage()); 
                    System.out.println("The color is " + lowest_price_with_make_model.getColor()); 
                    System.out.println();
                }
                break; 
            case 7: input = 7;
                CarInfo lowest_mileage_with_make_model; 
                lowest_mileage_with_make_model = retrieve_the_mileage_with_make_and_model(); 
                if(lowest_mileage_with_make_model != null){
                    System.out.println("$$ This is the car with the lowest price with make and model $$"); 
                    System.out.println("The VIN number is " + lowest_mileage_with_make_model.getVin()); 
                    System.out.println("The make is " + lowest_mileage_with_make_model.getMake()); 
                    System.out.println("The model is " + lowest_mileage_with_make_model.getModel()); 
                    System.out.println("The price is $" + lowest_mileage_with_make_model.getPrice()); 
                    System.out.println("The mileage is " + lowest_mileage_with_make_model.getMideage()); 
                    System.out.println("The color is " + lowest_mileage_with_make_model.getColor()); 
                    System.out.println();
                }
                break; 
            case 8: input = 8;
                System.out.println("Exit the program!"); 
                System.out.println("Bye !!!"); 
                System.exit(0);
                break; 
        }        
    }
    public static void main(String[] args) {
        System.out.println("Welcome to CarTracker by Zhuofei(zhx34): "); 
        
        openTXTfile();

        //System.out.println("The size is "+ PriorityQueue.get_size());

        /*
        print_car_prices(); 
        System.out.println(); 
        System.out.println("*************************************"); 
        print_car_mileage(); 
        System.out.println(); 
        */


        printMainMenu(); 

        input = scanner.nextInt();

       
        //System.out.println("The input is " + input);

        while(input!=8){
            //input = scanner.nextInt();
            if(input > 8){
                System.out.println("\nThe input is out of bound please enter again"); 
                System.out.print("Please enter your selection(from 1 to 7) ");
            }
            continue_the_display_menu(input); 
            printMainMenu();
            input = scanner.nextInt();
        }

        //System.out.println(PriorityQueue.get_size());

        
    }
}
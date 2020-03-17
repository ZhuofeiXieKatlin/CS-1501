import java.util.Arrays;
import java.util.HashMap;


public class PriorityQueue{
    public static final int sizeOfTXT = 14;
    public static final int MORE_SPACE = 1000;  
    private static CarInfo[] carsWithPrice; 
    private static CarInfo[] carsWithMileage; 
    private static int size; 
    private static HashMap<String, Integer> indexPrice;
    private static HashMap<String, Integer> indexMileage;  

    public PriorityQueue(){
        carsWithPrice = new CarInfo[sizeOfTXT]; 
        carsWithMileage = new CarInfo[sizeOfTXT]; 
        size = 0; 
        indexPrice = new HashMap<String, Integer>(); 
        indexMileage = new HashMap<String, Integer>(); 
    }

    public static int get_size(){
        return size; 
    }

    public static CarInfo[] return_car_with_prices(){
        return carsWithPrice; 
    }

    public static CarInfo[] return_car_with_mileage(){
        return carsWithMileage; 
    }

    public static void resizeArray(){
        carsWithPrice = Arrays.copyOf(carsWithPrice, 2*carsWithPrice.length); 
        carsWithMileage = Arrays.copyOf(carsWithMileage, 2*carsWithMileage.length); 
    }

    public static void add(CarInfo newCar){
        if(indexPrice.containsKey(newCar.getVin())){
            System.out.println("The car with the input VIN number has already existed");
            return; 
        } 
        if(size == carsWithPrice.length){
            resizeArray(); 
        }
        carsWithPrice[size] = newCar; 
        carsWithMileage[size] = newCar; 
        indexPrice.put(newCar.getVin(), size); 
        indexMileage.put(newCar.getVin(), size);
        swim_with_price(size); 
        swim_with_mileage(size); 
        size = size + 1;  
    }

    public static boolean search_with_VIN(String VIN){
        int index = -1;  
        if(indexPrice.containsKey(VIN)){
            Integer indexAT = indexPrice.get(VIN);
            index = indexAT.intValue();
            return true;
        }
        return false; 
    }

    public static boolean update_Price(String VINnumber, int price){
        Integer position = indexPrice.get(VINnumber); 
        Integer position2 = indexMileage.get(VINnumber);  
        int index = position.intValue(); 
        int index2 = position2.intValue(); 
        //System.out.println("At index:" + index); 
        boolean setPrice = carsWithPrice[index].setPrice(price); 
        boolean setPrice_in_Mileage = carsWithMileage[index2].setPrice(price); 
        if(!setPrice){
            System.out.println("Something goes wrong, you cannot update a price for a car!"); 
            return false; 
        }
        swim_with_price(index);
        sink_with_price(index); 
        return true; 
    }

    public static boolean update_Mileage(String VINnumber, int milesage){
        Integer position = indexMileage.get(VINnumber); 
        Integer position2 = indexPrice.get(VINnumber); 
        int index = position.intValue(); 
        int index2 = position2.intValue(); 
        boolean setMileage = carsWithMileage[index].setMileage(milesage); 
        boolean setMileage_for_price = carsWithPrice[index2].setMileage(milesage); 
        if(!setMileage){
            System.out.println("Something goes wrong, you cannot update a price for a car!"); 
            return false; 
        }
        swim_with_mileage(index);
        sink_with_mileage(index);
        return true; 
    }

    public static boolean update_Color(String VINnumber, String color){
        Integer position = indexMileage.get(VINnumber); 
        int index = position.intValue(); 
        boolean setMileage = carsWithMileage[index].setColor(color); 
        if(!setMileage){
            System.out.println("Something goes wrong, you cannot update a color for a car!"); 
            return false; 
        }
        boolean setPriceColor = carsWithPrice[index].setColor(color); 
        if(!setPriceColor){
            System.out.println("Something goes wrong, you cannot update a color for a car!"); 
            return false; 
        }
        return true; 
    }

    public static void change_the_last(String VIN){
        Integer position = indexPrice.get(VIN); 
        int index = position.intValue(); 
        changePosition(carsWithPrice, indexPrice, index, size -1);
        carsWithPrice[size -1] = null; 

        position = indexMileage.get(VIN); 
        index = position.intValue(); 
        changePosition(carsWithMileage, indexMileage, index, size -1);
        carsWithMileage[size -1] = null; 

        size--; 

    }

    public static boolean remove_Car_price(String VIN){
        Integer position = indexPrice.get(VIN); 
        int index = position.intValue(); 
        if(index == size){
            indexPrice.remove(VIN); 
            carsWithPrice[size] = null; 
            return true; 
        }
        changePosition(carsWithPrice, indexPrice, index, size);
        carsWithPrice[size] = null; 

        indexPrice.remove(VIN); 
        swim_with_price(index);
        sink_with_price(index);
        return true; 
    }

    public static boolean remove_Car_mileage(String VIN){
        Integer position = indexMileage.get(VIN); 
        int index = position.intValue();
        if(index == size){
            indexMileage.remove(VIN); 
            carsWithMileage[size] = null; 
            return true; 
        } 
        changePosition(carsWithMileage, indexMileage, index, size);
        carsWithMileage[size] = null; 
        // pull up the last one into the position
        indexMileage.remove(VIN); 
        swim_with_mileage(index);
        sink_with_mileage(index);
        return true; 
    }

    public static boolean remove_Car(String VINnumber){
        size --;
        boolean remove_from_pricecars = remove_Car_price(VINnumber);
        boolean remove_from_mileages = remove_Car_mileage(VINnumber); 
        if(remove_from_pricecars && remove_from_mileages){
            return true;  
        } 
        else{
            System.out.println("Something goes wrong, you cannot update a color for a car!"); 
            return false; 
        }
    }

    public static CarInfo find_lowest_price_car(){
        return carsWithPrice[0]; 
    }

    
    public static CarInfo find_lowest_mileage_car(){
        return carsWithMileage[0]; 
    }

    public static CarInfo retrive_priceCar_with_Make_Model(boolean is_price, String make, String model){
        int Beginning = 0; 
        if(is_price){
            return retrive_priceCar_with_Make_Model(Beginning, make, model); 
        }
        else{
            return retrive_mileCar_with_Make_Model(Beginning, make,model); 
        }
    }

    
    public static CarInfo retrive_priceCar_with_Make_Model(int position, String make, String model){
        if(position >= size){
            return null; 
        }
        if(carsWithPrice[position] == null){
            return null;
        }
        if(carsWithPrice[position].getMake().equals(make) && carsWithPrice[position].getModel().equals(model)){
            return carsWithPrice[position]; 
        }
        else{
            CarInfo left = retrive_priceCar_with_Make_Model(2*position + 1, make, model); 
            CarInfo right = retrive_priceCar_with_Make_Model(2*position + 2, make, model); 
            if(left == null && right == null) return null; 
            else if(right == null) return left; 
            else if(left == null) return right; 
            else{
                if(left.getPrice() < right.getPrice()){
                    return left; 
                }
                return right; 
            }
        }
    }
    
    
    public static CarInfo retrive_mileCar_with_Make_Model(int position, String make, String model){
        if(position >= size){
            return null; 
        }
        if(carsWithPrice[position] == null){
            return null;
        }
        if(carsWithMileage[position].getMake().equals(make) && carsWithMileage[position].getModel().equals(model)){
            return carsWithMileage[position]; 
        }
        else{
            CarInfo left = retrive_mileCar_with_Make_Model(2*position + 1, make, model); 
            CarInfo right = retrive_mileCar_with_Make_Model(2*position + 2, make, model); 
            if(left == null && right == null) return null; 
            else if(left == null) return right; 
            else if(right == null) return left; 
            else{
                if(left.getMideage() < right.getMideage()){
                    return left; 
                }
                return right; 
            }
        }
    }
    
    public static boolean lowerPrice(int children, int parent){
        if(carsWithPrice[children].getPrice() >= carsWithPrice[parent].getPrice()){
            return false;
        }
        return true;
    }

    public static boolean lowerMileage(int children, int parent){
        if(carsWithMileage[children].getMideage() >= carsWithMileage[parent].getMideage()){
            return false; 
        }
        return true; 
    }


    public static void changePosition(CarInfo[] Cars, HashMap<String, Integer> indexCars, int children, int Parent){
        CarInfo current = Cars[children];
        Cars[children] = Cars[Parent]; 
        Cars[Parent] = current; 
        indexCars.replace(Cars[children].getVin(), Integer.valueOf(children)); 
        indexCars.replace(Cars[Parent].getVin(), Integer.valueOf(Parent));  
    }

    public static boolean compareChildPrice(int child1, int child2){
        if(carsWithPrice[child1].getPrice()> carsWithPrice[child2].getPrice()){
            return true; 
        }
        return false; 
    }

    public static boolean compareChildMileage(int child1, int child2){
        if(carsWithMileage[child1].getMideage()>carsWithMileage[child2].getMideage()){
            return true; 
        }
        return false; 
    }

    public static void swim_with_price(int index){
        while(index > 0 && lowerPrice(index, (index-1)/2)){
            changePosition(carsWithPrice, indexPrice, index, (index-1)/2); 
            index = (index-1)/2; 
        }
    }

    public static void swim_with_mileage(int index){
        while(index>0 && lowerMileage(index, (index-1)/2)){
            changePosition(carsWithMileage, indexMileage, index, (index-1)/2);
            index = (index-1)/2; 
        }
    }

    public static void sink_with_price(int index){
        while( (2*index + 1) < size){
            //System.out.println("here"); 
            int childONE = 2*index + 1; 
            int childTWO = 2*index + 2; 
            boolean child_is_lower; 
            if(childTWO < size){
                boolean childOne_greater_than_carTWO = compareChildPrice(childONE, childTWO); 
                if(childOne_greater_than_carTWO){
                    child_is_lower = lowerPrice(childTWO, index); 
                    if(child_is_lower){
                        //System.out.println("SINK WITH PRICE"); 
                        changePosition(carsWithPrice, indexPrice, childTWO, index);
                        index = childTWO; 
                        continue; 
                    }
                    else break; 
                }
            }
            child_is_lower = lowerPrice(childONE, index); 
            if(child_is_lower){
                changePosition(carsWithPrice, indexPrice, childONE, index);
            }
            if(!child_is_lower) break; 
            //changePosition(carsWithPrice, indexPrice, childONE, index);
            index = childONE; 
            
        }
    }

    public static void sink_with_mileage(int index){
        while( (2*index +1 ) < size){
            int childONE = 2*index + 1; 
            int childTWO = 2*index + 2; 
            boolean child_is_lower; 
            if(childTWO<size){
                boolean childOne_greater_than_carTWO = compareChildMileage(childONE, childTWO); 
                if(childOne_greater_than_carTWO){
                    //System.out.println("HERE"); 
                    child_is_lower = lowerMileage(childTWO, index); 
                    if(child_is_lower){
                        changePosition(carsWithMileage, indexMileage, childTWO, index);
                        index = childTWO; 
                        continue; 
                    }
                    else break; 
                }
            }
            child_is_lower = lowerMileage(childONE, index); 
            if(child_is_lower){
                changePosition(carsWithMileage, indexMileage, childONE, index);
            }
            if(!child_is_lower) break; 
            index = childONE; 
        }
    }
}
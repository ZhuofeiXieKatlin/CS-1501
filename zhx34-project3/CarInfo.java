class CarInfo{
    private String Vin;
    private String Make; 
    private String Model; 
    private int Price; 
    private int Mileage; 
    private String Color; 
    
    public CarInfo (String Vin, String Make,String Model, int Price, int Mileage, String Color){
        this.Vin = Vin; 
        this.Make = Make; 
        this.Model = Model; 
        this.Price = Price; 
        this.Mileage = Mileage; 
        this.Color = Color; 
    }

    public String getVin(){
        return Vin; 
    }

    public String getMake(){
        return Make; 
    }

    public String getModel(){
        return Model; 
    }

    public int getPrice(){
        return Price; 
    }

    public int getMideage(){
        return Mileage; 
    }

    public String getColor(){
        return Color; 
    }

    public boolean setVin(String vin){
        Vin = vin; 
        return true; 
    }

    public boolean setMake(String make){
        Make = make;
        return true;
    }

    public boolean setModel(String model){
        Model = model; 
        return true; 
    }

    public boolean setPrice(int price){
        Price = price; 
        return true; 
    }

    public boolean setMileage(int mileage){
        Mileage = mileage; 
        return true; 
    }

    public boolean setColor(String color){
        Color = color; 
        return true; 
    }

    public String toString(){
        StringBuilder info = new StringBuilder(); 
        info.append("\n" + Vin + " "); 
        info.append(Make + " "); 
        info.append(Model + " ");
        info.append(Price + " ");
        info.append(Mileage + " ");
        info.append(Color + " ");    
        return info.toString(); 
    }
}
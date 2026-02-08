class Food {
    private int foodID;
    private String name;
    private String type;
    private double stock;
    private String expiryDate;
    private double costPerUnit;

    public Food(int foodID, String name, String type, double stock, String expiryDate, double costPerUnit) {
        this.foodID = foodID;
        this.name = name;
        this.type = type;
        this.stock = stock;
        this.expiryDate = expiryDate;
        this.costPerUnit = costPerUnit;
    }

    public double getStockStatus() {
        return stock;
    }
    public String getName() {
        return name;
    }   

    public void setStock(double stock) {
        if (stock >= 0) {
            this.stock = stock;
        } else {
            System.out.println("Stock cannot be negative.");
        }
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Food{");
        sb.append("foodID=").append(foodID);
        sb.append(", name=").append(name);
        sb.append(", type=").append(type);
        sb.append(", stock=").append(stock);
        sb.append(", expiryDate=").append(expiryDate);
        sb.append(", costPerUnit=").append(costPerUnit);
        sb.append('}');
        return sb.toString();
    }


    
    

}
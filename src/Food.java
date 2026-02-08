class Food {
    int foodID;
    String name;
    String type;
    boolean inStock = true;
    double stock;
    String expiryDate;

    Food(int foodID, String name, String type, double stock, String expiryDate) {
        this.foodID = foodID;
        this.name = name;
        this.type = type;
        this.stock = stock;
        this.expiryDate = expiryDate;
    }

    @Override
    public String toString() {
        return "Food [foodID=" + foodID + ", name=" + name + ", type=" + type + ", inStock=" + inStock + ", stock=" + stock
                + ", expiryDate=" + expiryDate + "]";
    }
    

}
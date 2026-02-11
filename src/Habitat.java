import java.util.Objects;

public class Habitat {
    private String id;
    private String species;
    private Food food;
    private double amountFood;
    private Schedule feedingTime;
    private Animal[] animals;
    private int count = 0;

    public Habitat(String id, String species, double amountFood, Schedule feedingTime, Food food) {
        this.id = id;
        this.species = species;
        setAmountFood(amountFood);
        setFeedingTime(feedingTime,feedingTime.getAssignedStaff());
        this.animals = new Animal[10];
        this.food = food;
    }

    public String getHabitatID() {return id;}
    public String getSpecies() {return species;}
    public double getAmountFood() {return amountFood;}
    public Schedule getFeedingTime() {return feedingTime;}
    public Animal[] getAnimals() {return animals;}

    public void setAmountFood(double amountFood) {
        if (amountFood > 0) {
            this.amountFood = amountFood;
        } else {
            System.out.println("Amount of food must be positive.");
        }
    }

    public void setFeedingTime(Schedule feedingTime, Staff s) {
        if (!s.getRole().equals("Manager")) return;
        if (feedingTime != null) {
            this.feedingTime = feedingTime;
        }else {
            System.out.println("Feeding time is invalid or null.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Habitat habitat = (Habitat) o;
        return Double.compare(amountFood, habitat.amountFood) == 0 && count == habitat.count
                && Objects.equals(id, habitat.id) && Objects.equals(species, habitat.species)
                && Objects.equals(food, habitat.food) && Objects.equals(feedingTime, habitat.feedingTime)
                && Objects.deepEquals(animals, habitat.animals);
    }

}
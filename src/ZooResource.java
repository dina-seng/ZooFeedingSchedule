class ZooResource {

    // ===== ARRAYS + PRIMITIVE COUNTERS =====
    static Animal[] animals = new Animal[100];
    static int animalCount = 0;

    static Food[] foods = new Food[50];
    static int foodCount = 0;

    static Habitat[] habitats = new Habitat[30];
    static int habitatCount = 0;

    static Staff[] staffs = new Staff[20];
    static int staffCount = 0; 

    static Schedule currentSchedule;

    // ===== HELPER METHODS =====
    static void addAnimal(Animal a) {
        animals[animalCount++] = a;
    }

    static void addFood(Food f) {
        foods[foodCount++] = f;
    }

    static void addHabitat(Habitat h) {
        habitats[habitatCount++] = h;
    }
    
    static void addStaff(Staff s) {
        staffs[staffCount++] = s;
    }

    static void setSchedule(Schedule sch) {
        currentSchedule = sch;
    }

    static {
        // ===== FOODS =====
        Food lettuce  = new Food(2, "Lettuce", "Plant", 50.0, "2026-07-15");
        Food carrot   = new Food(3, "Carrot", "Plant", 60.0, "2026-06-20");
        Food spinach  = new Food(4, "Spinach", "Plant", 40.0, "2026-05-30");
        Food banana   = new Food(5, "Banana", "Fruit", 80.0, "2026-05-15");
        Food apple    = new Food(6, "Apple", "Fruit", 70.0, "2026-06-01");
        Food mango    = new Food(7, "Mango", "Fruit", 50.0, "2026-04-30");
        Food orange   = new Food(8, "Orange", "Fruit", 60.0, "2026-06-10");
        Food steak    = new Food(9, "Steak", "Meat", 30.0, "2026-06-30");
        Food chicken  = new Food(10, "Chicken", "Meat", 40.0, "2026-05-10");
        Food fish     = new Food(11, "Fish", "Meat", 50.0, "2026-04-20");
        Food peanuts  = new Food(12, "Peanuts", "Snack", 20.0, "2027-01-01");
        Food almonds  = new Food(13, "Almonds", "Snack", 15.0, "2027-01-01");
        Food corn     = new Food(14, "Corn", "Grain", 60.0, "2026-09-30");
        Food oats     = new Food(15, "Oats", "Grain", 40.0, "2026-08-20");

        addFood(new Food(1, "Bamboo", "Plant", 100.0, "2026-12-31")); 
        addFood(lettuce); addFood(carrot); addFood(spinach);
        addFood(banana); addFood(apple); addFood(mango); addFood(orange);
        addFood(steak); addFood(chicken); addFood(fish);
        addFood(peanuts); addFood(almonds); addFood(corn); addFood(oats);

        addFood(new Food(16, "KOK", "Grain", 20.0, "2026-08-30"));
        
        addFood(new Food(17, "BakSa", "Grain", 10.0, "2026-07-01"));
        addFood(new Food(16, "KOK", "Grain", 20.0, "2026-08-30"));

        // ===== FEEDING TIMES =====
        String[] forestTimes  = {"08:00", "12:30", "17:00"};
        String[] jungleTimes  = {"08:30", "12:00", "16:30"};
        String[] savannaTimes = {"09:00", "13:00", "18:00"};
        String[] oceanTimes   = {"07:00", "11:30", "19:00"};

        // ===== HABITATS + ANIMALS ===== 


        Habitat FR001 = new Habitat("FR001", "Panda", foods[0].stock, forestTimes, new Animal[5], foods[0]);
        addHabitat(FR001);
        addAnimal(new Animal(12, "P1", 6, FR001, "Panda"));
        addAnimal(new Animal(31, "P2", 5, FR001, "Panda"));
        addAnimal(new Animal(4,  "P3", 4, FR001, "Panda"));

        Habitat JG001 = new Habitat("JG001", "Monkey", banana.stock, jungleTimes, new Animal[5], banana);
        addHabitat(JG001);
        addAnimal(new Animal(14, "M1", 4, JG001, "Monkey"));
        addAnimal(new Animal(26, "M2", 3, JG001, "Monkey"));
        addAnimal(new Animal(5,  "M3", 5, JG001, "Monkey"));

        Habitat SV001 = new Habitat("SV001", "Lion", steak.stock, savannaTimes, new Animal[5], steak);
        addHabitat(SV001);
        addAnimal(new Animal(30, "L1", 7, SV001, "Lion"));
        addAnimal(new Animal(6,  "L2", 6, SV001, "Lion"));
        addAnimal(new Animal(19, "L3", 8, SV001, "Lion"));

        Habitat OC001 = new Habitat("OC001", "Shark", fish.stock, oceanTimes, new Animal[5], fish);
        addHabitat(OC001);
        addAnimal(new Animal(25, "S1", 9, OC001, "Shark"));
        addAnimal(new Animal(17, "S2", 10, OC001, "Shark"));

        /* ===== STAFF ===== */
        Staff alice = new Staff("st1", "Alice", "Keeper-Forest");
        Staff john = new Staff("st2", "John", "Keeper-Savanna");
        Staff dara = new Staff("st3", "Dara", "Keeper-Jungle");
        Staff mary = new Staff("st4", "Mary", "Keeper-Ocean");
        Staff bob   = new Staff("st5", "Bob", "Customer Service");
        addStaff(alice);
        addStaff(john);
        addStaff(dara);
        addStaff(mary);
        addStaff(bob);

        /*===Schedule===*/

        Schedule weeklySchedule = new Schedule(staffs[0].staffID, habitats[0].habitatID, "2026-06-01", "08:00");
        setSchedule(weeklySchedule);
    }


}
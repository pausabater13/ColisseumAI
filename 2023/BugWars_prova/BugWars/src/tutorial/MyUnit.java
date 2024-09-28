package tutorial;

import bugwars.user.*;

//Custom generic unit class. It contains all methods and attributes used by (mostly) all units.
public abstract class MyUnit {

    //Beginning of the shared array slots used to store food locations.
    final int FOOD_START_INDEX = 20;

    //Beginning of the shared array slots used to store the total number of units of this type.
    final int COUNTER_INDEX;

    //Since locations can range from (0,0) to (1000,1000), we communicate the position of other objects relative to a common reference location.
    final Location referenceLocation;

    //Each unit should have an instance of each of these
    UnitController uc;
    Pathfinding pathfinding;
    FoodTracker foodTracker;
    Counter counters;
    Micro micro;

    MyUnit(UnitController unitController) {
        uc = unitController;
        COUNTER_INDEX = Helper.getCounterIndex(uc.getType());

        // this makes sure that the reference location is common to all units
        referenceLocation = uc.getTeam().getInitialLocations()[0];

        pathfinding = new Pathfinding(uc);
        foodTracker = new FoodTracker(uc, FOOD_START_INDEX, referenceLocation);
        counters = new Counter(uc);
        micro = new Micro(uc);
    }

    /*
    Play is an abstract method, which means that each unit should implement its own play(). This allows different types of units to
    have different behaviors.
     */

    abstract void play();


    /*
    Methods that can be used by all units. This is useful to avoid having duplicate code.
     */

    //Method that updates the counter of the given unit type.
    void countMe(){
        counters.increaseValueByOne(COUNTER_INDEX);
    }

    public void reportFood() {
        FoodInfo[] foodSeen = uc.senseFood();
        for(FoodInfo foodInfo: foodSeen) {
            Location foodLocation = foodInfo.getLocation();
            if(!uc.isObstructed(uc.getLocation(), foodLocation)) {
                foodTracker.saveFoodSeen(foodInfo.getLocation());
            }
        }
    }
}

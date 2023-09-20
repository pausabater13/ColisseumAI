package tutorial;

import bugwars.user.*;

public class Ant extends MyUnit {

    Location[] myFood= {null, null, null};
    int firstUnassignedIndex = 0;

    Ant(UnitController unitController){
        super(unitController);
    }

    public void play() {
        //We want to claim at most 3 adjacent food tiles for ourselves and keep mining them
        assignFood();

        //We broadcast that those food tiles are ours to avoid having other ants mining from them as well
        claimFood();

        tryCollectFood();
        move();
        tryCollectFood();
        micro.tryGenericAttack();
    }

    public void tryCollectFood() {

        FoodInfo bestFood = null;

        FoodInfo[] foodsInfo = uc.senseFood();
        for(FoodInfo currentFoodInfo: foodsInfo) {
            // ignore locations with less than 3 food
            if (currentFoodInfo.getFood() < 3) continue;

            // we keep track of the best food location with our custom comparison method
            if (isBetterFoodAThanB(currentFoodInfo, bestFood)) bestFood = currentFoodInfo;
        }

        if(bestFood != null && uc.canMine(bestFood)) {
            uc.mine(bestFood);
        }
    }

    public boolean isBetterFoodAThanB(FoodInfo a, FoodInfo b) {
        // first we try with our assigned food
        // if we can't, we then try to get any food

        // check if one is null and return the other
        if(a == null) return false;
        if(b == null) return true;

        Location locationA = a.getLocation();
        Location locationB = b.getLocation();

        // if one is mine and the other isn't, we return the one that is mine no matter what
        if(foodTracker.isMine(locationA) && !foodTracker.isMine(locationB)) {
            return true;
        }

        if(!foodTracker.isMine(locationA) && foodTracker.isMine(locationB)) {
            return false;
        }

        // else, we get from the one that has more
        return a.getFood() > b.getFood();
    }

    public void assignFood() {
        if (firstUnassignedIndex >= myFood.length) return;

        //if we discover any available food, we claim it and try again!
        myFood[firstUnassignedIndex] = foodTracker.getNearestUnclaimedDiscoveredFood(uc.getLocation());
        if (myFood[firstUnassignedIndex] != null){
            foodTracker.claimMine(myFood[firstUnassignedIndex++]);
            assignFood();
        }
    }

    public void claimFood(){
        for (Location foodLocation: myFood) {
            if (foodLocation != null) {
                foodTracker.claimMine(foodLocation);
            }
        }
    }

    public void move() {
        UnitInfo[] enemies = uc.senseUnits(uc.getOpponent());

        if(enemies.length == 0) {
            /* if there are no enemies we go to our objective location  */
            pathfinding.moveTo(myFood[0]);
        }
        else {
            micro.doMicro();
        }
    }

}

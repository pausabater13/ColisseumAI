package tutorial;

import bugwars.user.*;

/*
This class will track the food that we have seen during the game. It basically consists in
a queue of locations where we store food locations and a map of each location to who claimed
that location and at what round it was last claimed. For that, each element of the queue takes
1 int (only the encoded location using the helper locationToInt) and each element of the map
takes 2 ints, one for at which round it was claimed and the another id for the unit that
claimed it. We store who claimed it so units can check if it's still their food.

Note: To know more about the map location encoding look at the Helper class.

A graphic representation of how this class stores the data:

[FOOD_SEEN, FOOD_MAP_LAST_ROUND_CLAIMED_0, FOOD_MAP_ID_CLAIMED_0,...,
FOOD_MAP_LAST_ROUND_CLAIMED_16383 FOOD_MAP_ID_CLAIMED_16383, ]

 */

public class FoodTracker {
    // max map length
    final int MAX_MAP_LENGTH = 127;

    // we implement a "queue" that stores all the food seen, the maximum number of
    // food we track is 64
    final int MAX_QUEUE_ELEMENTS = 64;

    // index where the food map starts, we store a map with all the locations, indicating
    // if the location has food or if it doesn't
    final int FOOD_START_MEMORY_INDEX; // takes MAX_MAP_LENGTH * MAX_MAP_LENGTH * 2 * 2 space

    // index where the food queue starts
    final int QUEUE_START_INDEX; // takes MAX_QUEUE_ELEMENTS space

    // index where we store the number of total food seen, every time we see new food
    // (we are able to check if it is new or not thanks to the food map) we add one
    // to the value
    final int FOOD_SEEN_INDEX;

    // index that stores the end of the queue (i.e. its last value), each time we push a seen food
    // we add one to this value, it goes from 0 to MAX_QUEUE_ELEMENTS - 1
    final int QUEUE_COUNTER_INDEX;

    Location referenceLocation;

    UnitController uc;
    FoodTracker(UnitController unitController, int foodStartMemoryIndex, Location rl){
        // initialize each index, we must be careful to leave enough space between them to avoid conflicts!
        FOOD_SEEN_INDEX = foodStartMemoryIndex;
        FOOD_START_MEMORY_INDEX = FOOD_SEEN_INDEX + 1;
        QUEUE_COUNTER_INDEX = FOOD_START_MEMORY_INDEX + MAX_MAP_LENGTH * 2 + 1;
        QUEUE_START_INDEX = QUEUE_COUNTER_INDEX + 1;

        uc = unitController;
        referenceLocation = rl;
    }

    // method that given a location returns the index where we stored the food on the food map
    public int getFoodIndex(int intLocation) {
        return FOOD_START_MEMORY_INDEX + intLocation * 2 + 1;
    }

    public void saveFoodSeen(Location loc) {
        int intLocation = Helper.locationToInt(loc, referenceLocation);

        // get id claimed, -1 if not claimed but food already there,
        // 0 if first time we see it
        int previous = uc.read(getFoodIndex(intLocation) + 1);

        if(previous == 0) {
            // mark that the location has food but no one claimed
            uc.write(getFoodIndex(intLocation) + 1, -1);

            // insert to the queue of foods
            int currentQueueIndex = uc.read(QUEUE_COUNTER_INDEX);
            uc.write(QUEUE_START_INDEX  + currentQueueIndex, intLocation);

            // add +1 to the insert index
            uc.write(QUEUE_COUNTER_INDEX, (currentQueueIndex + 1)%MAX_QUEUE_ELEMENTS);

            // add +1 to food seen
            int foodSeen = uc.read(FOOD_SEEN_INDEX);
            uc.write(FOOD_SEEN_INDEX, foodSeen + 1);
        }
    }

    public void claimMine(Location loc) {
        int intLocation = Helper.locationToInt(loc, referenceLocation);
        int index = getFoodIndex(intLocation);
        uc.write(index, uc.getRound());
        uc.write(index + 1, uc.getInfo().getID());
    }

    public boolean isMine(Location loc) {
        int intLocation = Helper.locationToInt(loc, referenceLocation);
        return uc.read(getFoodIndex(intLocation) + 1) == uc.getInfo().getID();
    }

    public Location getNearestUnclaimedDiscoveredFood(Location loc) {
        return getNearestUnclaimedDiscoveredFood(loc, Integer.MAX_VALUE);
    }

    public Location getNearestUnclaimedDiscoveredFood(Location loc, int maxDistance) {

        int bestIntLocation = -1;
        int closest = Integer.MAX_VALUE;

        for(int i = 0; i < MAX_QUEUE_ELEMENTS; i++) {
            int nextIntLocation = uc.read(QUEUE_START_INDEX + i);

            int index = getFoodIndex(nextIntLocation);

            int lastRoundClaimed = uc.read(index);
            int lastIdClaimed = uc.read(index + 1);

            if(lastIdClaimed == -1 || (lastIdClaimed > 0 && uc.getRound() - lastRoundClaimed > 20)){
                int distance = Helper.intToLocation(nextIntLocation, referenceLocation).distanceSquared(loc);
                if((bestIntLocation == -1 || distance < closest) && distance < maxDistance) {
                    bestIntLocation = nextIntLocation;
                    closest = distance;
                }
            }
        }

        if(bestIntLocation == -1) return null;
        return Helper.intToLocation(bestIntLocation, referenceLocation);
    }

    public Location getAdjacentUnclaimedDiscoveredFood(Location loc) {
        Direction[] directions = Direction.values();
        for (Direction dir: directions) {
            Location newLoc = loc.add(dir);
            int intLocation = Helper.locationToInt(newLoc, referenceLocation);
            int index = getFoodIndex(intLocation);

            int lastRoundClaimed = uc.read(index);
            int lastIdClaimed = uc.read(index + 1);

            if(lastIdClaimed == -1 || (lastIdClaimed > 0 && uc.getRound() - lastRoundClaimed > 20)){
                return newLoc;
            }
        }

        return null;
    }

    public int getSeenCookies() {
        return uc.read(FOOD_SEEN_INDEX);
    }
}

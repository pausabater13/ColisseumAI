package tutorial;

import bugwars.user.*;

// this class is for small utility methods that can be called from anywhere in the code
public class Helper {

    static public Direction getRandomDirection() {
        int randomNumber = (int)(Math.random()*Direction.values().length);
        return Direction.values()[randomNumber];
    }

    static public int getCounterIndex(UnitType type){
        return 4*type.ordinal();
    }

    /* functions that encode/decode a location to an int.
       The encode uses the simple idea of assigning each cell a value,
       for example we can number the tiles of a 3x3 map like this:
         0 1 2
         3 4 5
         6 7 8

       The formula is simple: x*col_size + y

         location (0,0) -> 0
         location (1,2) -> 5
         location (2,2) -> 8

       The problem is that locations in this game have an offset, so we can't assume that locations will go
       from 0 to 2 like in the example above. To solve that we will pick a random base location and calculate
       a new location that represents the offset relative position to the base location.

       Following the example above, the new possible values for x and y are [-2,2]. For our formula to work we
       we need positive values so we shift the numbers to [0, 4] adding the col size to them.
       Now we can use the formula above to encode our new locations (5x5 map). Notice that now
       we are using 2 times the space we were using before, since now we may have negative values.
     */

    static public int locationToInt(Location loc, Location base) {
        int MAX_MAP_SIZE = 64;
        int x = (loc.x - base.x) + MAX_MAP_SIZE - 1;
        int y = (loc.y - base.y) + MAX_MAP_SIZE - 1;
        return x * MAX_MAP_SIZE*2 + y;
    }

    static public Location intToLocation(int loc, Location base) {
        int MAX_MAP_SIZE = 64;

        int x = loc/(MAX_MAP_SIZE*2) - MAX_MAP_SIZE + 1 + base.x;
        int y = loc%(MAX_MAP_SIZE*2) - MAX_MAP_SIZE + 1 + base.y;

        return new Location(x, y);
    }
}

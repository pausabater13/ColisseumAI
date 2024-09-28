package cookiemonster;

import bugwars.user.*;

/**
 * This is the class that contains the main communication methods for food management.
 * All units report all cookies found by the end of their turns. Ants claim some of these cookies
 * so that no other ant attempts to go there ot to mine them. Queens only spawn ants if there are cookies
 * that are not claimed.
 */

/**
 * Information about cookies is spread among three segments of the communication array.
 *
 * The first segment (starting at 0) is a list of all visible cookies. Each element of the list
 * contains the x and the y coordinate of a cookie. The element at 9999 is the current length of the list.
 *
 * The second segment (starting at 10000) is a map from [x,y] to the index
 * of the cookie at [x,y] in the previous list. We hash (x,y) using the formula
 * h(x,y) = (x*M + y)%(M^2), where M is the maximum size of the map. It is easy to check
 * that if h(x,y) = h(x', y') then x = x' mod M and y = y' mod M, therefore there are no collisions
 * between different tiles of the map. Note that this hash is not invertible
 * (unless we somehow compute the map boundaries).
 *
 * The third segment (starting at 20000) is also a list. The element at position i (array[20000 + i])
 * has the information about which ant is currently claiming the cookie. It encodes two numbers (ID and r),
 * where the ID is the ID of the ant, and r is the round in which the ant claims the cookie (ants will claim
 * their cookies every turn, that way the cookies become available for other ants whenever their owner dies).
 */


public class CookieManager {

    UnitController uc;
    int myID;

    CookieManager(UnitController uc){
        this.uc = uc;
        this.myID = uc.getInfo().getID();
    }

    /*Segment constants*/
    final int FIRST_SEGMENT_BEGIN = 0;
    final int LIST_SIZE_LOC = 9999;
    final int SECOND_SEGMENT_BEGIN = 10000;
    final int THIRD_SEGMENT_BEGIN = 20000;

    final int SAFETY_TURNS = 3; // If an ant hasn't reported in 3 turns her cookies become available

    /*Map size constants*/
    final int max_map_size = GameConstants.MAX_MAP_SIZE;
    final int max_map_size2 = max_map_size*max_map_size;

    /**
     * Our hash method.
     */
    int getLocationHash(Location loc){
        return (loc.x*max_map_size + loc.y)%max_map_size2;
    }


    final int LOCATION_CODE_MULTIPLIER = 10000;

    /**
     * We encode x and y using x*10000 + y. We can easily get x and y back from the code by performing
     * code/10000 and code%10000.
     */
    int getLocationCode(Location loc){
        return loc.x*LOCATION_CODE_MULTIPLIER + loc.y;
    }

    /**
     * Inverse of the encoding.
     */
    Location inverseLocationCode(int code){
        return new Location(code/LOCATION_CODE_MULTIPLIER, code%LOCATION_CODE_MULTIPLIER);
    }

    /**
     * Method in which a unit reports all cookies that it sees and updates the first two lists.
     */
    void reportCookies(){

        /*Sense all cookies*/
        FoodInfo[] cookies = uc.senseFood();

        for (FoodInfo cookie : cookies){
            /*We really don't want to mess up the synchronization between the three arrays.
            This guarantees that we are not going to run out of bytecode in between. Probably
            we can use a way smaller number.*/
            if (uc.getEnergyLeft() < 1500) return;

            /*Check that we haven't appended this element yet*/
            int locationHash = getLocationHash(cookie.getLocation());
            if (uc.read(SECOND_SEGMENT_BEGIN + locationHash) > 0) continue; //We already worked on this element

            /*Append it to the first list*/
            int listEndpoint = uc.read(LIST_SIZE_LOC);
            int locationCode = getLocationCode(cookie.getLocation());
            uc.write(listEndpoint, locationCode);
            uc.write(LIST_SIZE_LOC, listEndpoint+1); //update size.

            /*Update the second list. Note that we write the index+1, since the default
             value 0 is reserved for uninteresting tiles.*/
            uc.write(SECOND_SEGMENT_BEGIN + locationHash, listEndpoint+1);

            /*Note that we do not touch the third list yet because no one is claiming the cookie.*/
        }

    }

    /**
     * This method returns the index of the cookie in the first/third list given its location.
     * Recall that since we added 1 we have to subtract it now. If this returns -1 it means there is no cookie.
     */
    int getCookieIndex(Location cookieLoc){
        int locationHash = getLocationHash(cookieLoc);
        return uc.read(SECOND_SEGMENT_BEGIN + locationHash) - 1;
    }

    /**
     * This method returns the location of the cookie given its index in the first list.
     */
    Location getCookieLocation(int cookieIndex){
        int code = uc.read(FIRST_SEGMENT_BEGIN + cookieIndex);
        return inverseLocationCode(code);
    }

    final int bitsRound = 12;
    final int roundMask = 0xFFF; // this is just 12 ones in binary.

    /**
     * The 'receipt' of claiming a tile is encoding our ID and the current round on
     * the corresponding position on the third list. The input is the index in the first list.
     * Note that we can encode it similarly as the location encoding, however, we use
     * bitmasks for exposition.
     */
    void claimCookie(int cookieIndex){
        int claimReceipt = (myID << bitsRound) | uc.getRound();
        uc.write(THIRD_SEGMENT_BEGIN + cookieIndex, claimReceipt);
    }

    /**
     * Returns the owner of a cookie given its index. Returns -1 if no owner.
     */
    int owner (int cookieIndex){
        /*Get the receipt*/
        int claimReceipt = uc.read(THIRD_SEGMENT_BEGIN + cookieIndex);

        /*If claimed a long time ago it is up for grabs. Note that if it was never claimed its round is 0.*/
        int claimRound = claimReceipt & roundMask;
        if (claimRound < uc.getRound() - SAFETY_TURNS) return -1;

        /*Return the ID of the owner*/
        return (claimReceipt >>> bitsRound);
    }

    /**
     * Returns the owner of a cookie given its location. Returns -1 if no owner.
     */
    int owner(Location cookieLocation){
        return owner(getCookieIndex(cookieLocation));
    }

    /**
     * Returns I'm the owner of the cookie.
     */
    boolean isMine(Location cookieLocation){
        return owner(cookieLocation) == myID;
    }

    /*=====================SEARCH METHODS==========================*/

    /**
     * Whenever we search for cookies, we begin with the first cookie of the list. We go through
     * the elements of the list until we find a free cookie, reach the end (we go back to 0), or
     * until we run out of bytecode. This index keeps track of where we are.
     */
    int myCookieIndex = 0;

    /**
     * Returns the index of the first cookie available on the list. Returns -1 if couldn't find any.
     */
    int findCookie(){
        int cookieListSize = uc.read(LIST_SIZE_LOC);

        for (int i = 0; i < cookieListSize; ++i){
            /*Check for bytecode*/
            if (uc.getEnergyLeft() < 500) return -1;

            /*If no owner, we set our return value to actualIndex and break the loop*/
            if (owner(myCookieIndex) < 0) return myCookieIndex;

            /*update myCookieIndex. Note that we go back to the first element if we finish the list.*/
            myCookieIndex = (myCookieIndex + 1)%cookieListSize;

        }

        return -1;
    }






}

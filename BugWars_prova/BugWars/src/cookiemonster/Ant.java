package cookiemonster;

import bugwars.user.*;

public class Ant extends MyUnitClass{

    /**
     * This ant claims up to three cookies. These are their indices and their locations.
     */
    final int MAX_COOKIES = 3;
    int cookieCount; //how many cookies have I claimed.
    int[] myCookieIndex = new int[MAX_COOKIES]; //Indices of the cookies
    Location[] myCookieLocation = new Location[MAX_COOKIES]; // Location of the cookies


    /**
     * Inherited constructor.
     */
    Ant(UnitController uc){
        super(uc);
    }

    /**
     * An ant will move to the first cookie available (note that it may not be the one with the shortest
     * distance (although it is very easy to change the method to do so). It will also claim up to
     * two adjacent cookies since an ant can mine up to three cookies without any of them running out of food.
     */
    void play(){
        /*Check for available cookies*/
        findTarget();

        /*If we claimed a cookie, we go to the first one we claimed. Otherwise move randomly.*/
        if (myCookieLocation[0] != null) path.moveTo(myCookieLocation[0]);
        else moveRandomly();

        /*Claim all cookies we have claimed so far (we have to claim our cookies every round!)*/
        claimCookies();

        /*Mine one of our cookies*/
        mine();
    }

    /**
     * Checks for a cookie to claim
     */
    void findTarget(){
        if (cookieCount > 0) return;
        int cookieIndex = cookieManager.findCookie();
        if (cookieIndex >= 0) updateClaimed(cookieIndex);
    }

    /**
     * Updates our claimed cookies with a new entry.
     */
    void updateClaimed(int cookieIndex){
        myCookieIndex[cookieCount] = cookieIndex;
        myCookieLocation[cookieCount] = cookieManager.getCookieLocation(cookieIndex);
        ++cookieCount;
    }

    /**
     * Claims (again) all currently claimed cookies, and also tries to claim up to two adjacent ones.
     */
    void claimCookies(){

        /*Try to claim adjacents. Of course this only applies if I have claimed at least a cookie.*/
        if (cookieCount > 0) {

            /*Check all directions*/
            for (Direction dir : directions) {

                /*Maybe we claimed too many cookies?*/
                if (cookieCount >= MAX_COOKIES) break;

                /*Check if this adjacent cookie is available*/
                Location loc = myCookieLocation[0].add(dir);
                int cookieIndex = cookieManager.getCookieIndex(loc);
                if (cookieIndex < 0) continue; //This isn't even a cookie.
                int owner = cookieManager.owner(cookieIndex);

                /*If it is, not it is ours!*/
                if (owner < 0) {
                    updateClaimed(cookieIndex);
                }
            }
        }

        /*Claim all my cookies*/
        for (int i = 0; i < cookieCount; ++i) cookieManager.claimCookie(myCookieIndex[i]);
    }

    /**
     * Mine the claimed cookie with the most food.
     */
    void mine(){

        /*Best MiningSpot around me so far*/
        FoodInfo bestMiningSpot = null;

        /*Check all adjacents*/
        FoodInfo[] adjacentFood = uc.senseFood(2);
        for (FoodInfo food : adjacentFood){

            /*...But only those that are mine!*/
            if (!cookieManager.isMine(food.getLocation())) continue;

            /*If it is better than what I got so far, update*/
            if (bestMiningSpot == null || food.getFood() > bestMiningSpot.getFood()) bestMiningSpot = food;
        }

        /*Try to mine the best spot*/
        if (bestMiningSpot != null){
            if (uc.canMine(bestMiningSpot.getLocation())) uc.mine(bestMiningSpot.getLocation());
        }
    }


}

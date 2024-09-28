package cookiemonster;

import bugwars.user.*;

/**
 * Abstract unit class, it is implemented by every unit type.
 */
public abstract class MyUnitClass {

    /**
     * Pathfinding class.
     */
    Bugpath path;

    /**
     * CookieManager class.
     */
    CookieManager cookieManager;

    /**
     * Our UnitController.
     */
    UnitController uc;

    /**
     * Direction values.
     */
    Direction[] directions = Direction.values();

    /**
     * My id.
     */
    int myID;

    /**
     * Constructor
     */
    MyUnitClass(UnitController uc){
        this.uc = uc;
        this.myID = uc.getInfo().getID();
        this.path = new Bugpath(uc);
        this.cookieManager = new CookieManager(uc);
    }

    /**
     * Play method. It is implemented by each unit type.
     */
    abstract void play();

    void endTurn(){
        cookieManager.reportCookies();
    }

    /**
     * Generic attack method.
     */
    void attack(){

        /*Sense all enemies*/
        UnitInfo[] units = uc.senseUnits(uc.getOpponent());

        /*Attack any of them*/
        for (UnitInfo unit : units){
            if (uc.canAttack(unit.getLocation())) uc.attack(unit.getLocation());
        }
    }

    /**
     * Orders the unit to move in a random direction (if possible).
     */
    void moveRandomly(){
        /*Generate a random number from 0 to 7, both included*/
        int randomNumber = (int)(Math.random()*8);

        /*Get corresponding direction*/
        Direction dir = Direction.values()[randomNumber];

        /*move in direction dir if possible*/
        if (uc.canMove(dir)) uc.move(dir);
    }

    /**
     * Generic spawn method. It tries to spawn one unit of a given type in every direction (but only one in total).
     * It returns true iff it successfully creates the given type.
     */
    boolean spawn(UnitType type){
        for(Direction dir : directions){

            /*try spawning*/
            if (uc.canSpawn(dir, type)){
                uc.spawn(dir, type);
                return true;
            }
        }
        return false;
    }

}

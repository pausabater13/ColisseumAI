package cookiemonster;

import bugwars.user.*;

public class Beetle extends MyUnitClass {

    /**
     * Inherited constructor.
     */
    Beetle(UnitController uc){
        super(uc);
    }

    /**
     * It tries to move towards the first enemy queen. It doesn't attack.
     */
    void play(){
        path.moveTo(uc.getEnemyQueensLocation()[0]);
    }

}

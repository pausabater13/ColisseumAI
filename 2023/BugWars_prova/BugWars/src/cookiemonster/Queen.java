package cookiemonster;

import bugwars.user.*;

public class Queen extends MyUnitClass {

    /**
     * Inherited constructor.
     */
    Queen(UnitController uc){
        super(uc);
    }

    /*If I have already built a beetle or not*/
    boolean beetleBuilt = false;

    /*Turns until I can build an ant again*/
    final int MIN_ANT_TURNS = 15;

    /*Last round an ant was built*/
    int antBuiltRound = -MIN_ANT_TURNS;

    /**
     * It builds an ant if there's a free cookie and it hasn't built an ant for 15 rounds.
     * If we build all ants at once we might build more than we need, since they can't claim
     * territory while in construction (it is probably better to do it anyways, this is just a demo).
     * In maps with two queens they might still produce extra ants since they are not in synchronization.
     *
     * It also tries to build a single beetle.
     */
    void play(){
        if (cookieManager.findCookie() >= 0){
            if (uc.getRound() >= antBuiltRound + MIN_ANT_TURNS){
                if (spawn(UnitType.ANT)){
                    antBuiltRound = uc.getRound();
                }
            }
        }
        if (!beetleBuilt){
            if (spawn(UnitType.BEETLE)) beetleBuilt = true;
        }
    }
}

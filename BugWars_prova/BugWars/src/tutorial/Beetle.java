package tutorial;

import bugwars.user.*;

public class Beetle extends MyUnit {

    Beetle(UnitController unitController){
        super(unitController);
    }

    public void play() {
        boolean attackedThisTurn = micro.tryGenericAttack();
        move(attackedThisTurn);
        micro.tryGenericAttack();
    }

    public void move(boolean attackedThisTurn) {
        // aim for the first enemy queen
        Location targetLocation = uc.getQueensLocation(uc.getOpponent())[0];

        // now we move, how we do so depends in if we attacked or we haven't
        if(!attackedThisTurn) {
            // we don't see enemies or we see them but we can't attack them so
            // we continue to our path
            // alternatively we could try to avoid them since they may attack us
            pathfinding.moveTo(targetLocation);
        }
        else {
            micro.doMicro();
        }
    }
}

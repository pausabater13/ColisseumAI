package cookiemonster;

import bugwars.user.*;

public class UnitPlayer {

    public void run(UnitController uc) {
	/*Insert here the code that should be executed only at the beginning of the unit's lifespan*/

        /*We instantiate MyUnitClass to whatever type we are - note that we only create units of these types*/
        MyUnitClass myUnitClass;
        if (uc.getType() == UnitType.QUEEN) myUnitClass = new Queen(uc);
        else if (uc.getType() == UnitType.ANT) myUnitClass = new Ant(uc);
        else myUnitClass = new Beetle(uc);

        while (true){
			/*Insert here the code that should be executed every round*/

            /*play one turn*/
            myUnitClass.play();

            /*Do this with the free bytecode at the end of every turn*/
            myUnitClass.endTurn();

            uc.yield(); //End of turn
        }

    }
}

package tutorial;

import bugwars.user.*;

public class UnitPlayer {

    public void run(UnitController uc) {

        // custom generic unit
        MyUnit me;

        // instantiate the generic class according to our unit type
        UnitType myType = uc.getType();
        if(myType == UnitType.ANT) me = new Ant(uc);
        else if(myType == UnitType.QUEEN) me = new Queen(uc);
        else if(myType == UnitType.BEETLE) me = new Beetle(uc);
        else if(myType == UnitType.BEE) me = new Bee(uc);
        else me = new Spider(uc);

        while(true){
            /*
                Every unit should run each of these methods every turn (note that some of these are abstract (i.e. play()),
                which means that they have to be implemented for each different instance of MyUnit).
             */
            me.countMe();
            me.play();
            me.reportFood();

            uc.yield(); //End of turn
        }

    }
}

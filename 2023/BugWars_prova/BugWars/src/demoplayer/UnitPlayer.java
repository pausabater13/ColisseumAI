package demoplayer;

import bugwars.user.*;

public class UnitPlayer {

	public void run(UnitController uc) {
		// Code to be executed only at the beginning of the unit's lifespan

		Team opponent = uc.getOpponent(); // Enemy team
		while (true) {
			// Code to be executed every round

			// Generate a random number from 0 to 7, both included
			int randomNumber = (int)(Math.random() * 8);
			// Get corresponding direction
			Direction dir = Direction.values()[randomNumber];
			// Move in direction dir if possible
			if (uc.canMove(dir)) uc.move(dir);
			// If this unit is a queen, try spawning a beetle at direction dir
			if (uc.getType() == UnitType.QUEEN) {
				if (uc.canSpawn(dir, UnitType.BEETLE)) {
					uc.spawn(dir, UnitType.BEETLE);
				}
			}
			// Else, attack the first enemy you see
			else {
				UnitInfo[] visibleEnemies = uc.senseUnits(opponent);
				for (UnitInfo enemy : visibleEnemies) {
					if (uc.canAttack(enemy)) uc.attack(enemy);
				}
			}
			uc.yield(); // End of turn
		}
	}

}

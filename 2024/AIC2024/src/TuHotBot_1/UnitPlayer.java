package TuHotBot_1;

import aic2024.user.*;

public class UnitPlayer {

    //This array will be useful
    Direction[] directions = Direction.values();

    public void run(UnitController uc) {
        // Code to be executed only at the beginning of the unit's lifespan
        /*
        // Print each direction
        for (Direction direction : directions) {
            System.out.println(direction);
        }
        */
        while (true) {
            // Code to be executed every round
            System.out.println(getOpponent());


            //Pau



            //Joan
            if (els astronautes){
                hotzones = new Location[];
                hotzones = senseObjects(MapObject HOT_ZONE, float 25);

                plantes = new Location[];
                oxigens = new Location[];
                domes = new Location[];
                jumps = new Location[];
                radios = new Location[];
                settlements = new Location[];
                suits = new Location[];
                kits = new Location[];

                CarePackageInfo[] coses = senseCarePackages(float 25);
                for (CarePackageInfo cosa : coses) {
                    if(cosa.getCarePackageType() == CarePackage.PLANTS){
                        plantes.add(cosa.getLocation());
                    }
                    else if(cosa.getCarePackageType() == CarePackage.OXYGEN_TANK){
                        oxigen.add(cosa.getLocation());
                    }
                    else if(cosa.getCarePackageType() == CarePackage.DOME){
                        domes.add(cosa.getLocation());
                    }
                    else if(cosa.getCarePackageType() == CarePackage.HYPERJUMP){
                        jumps.add(cosa.getLocation());
                    }
                    else if(cosa.getCarePackageType() == CarePackage.RADIO){
                        radios.add(cosa.getLocation());
                    }                    
                    else if(cosa.getCarePackageType() == CarePackage.REINFORCED_SUIT){
                        suits.add(cosa.getLocation());
                    }                    
                    else if(cosa.getCarePackageType() == CarePackage.SETTLEMENT){
                        settlements.add(cosa.getLocation());
                    }                    
                    else if(cosa.getCarePackageType() == CarePackage.SURVIVAL_KIT){
                        kits.add(cosa.getLocation());
                    }
                }

            }





            '''//Case in which we are a HQ
            if (uc.isStructure() && uc.getType() == StructureType.HQ){
                //Spawn exactly one astronaut with 30 oxygen, if possible
                for (Direction dir : directions){
                    if (uc.canEnlistAstronaut(dir, 30, null)){
                        uc.enlistAstronaut(dir, 30, null);
                        break;
                    }
                }
            }

            //Case in which we are an astronaut
            else if (!uc.isStructure()){
                //move randomly, turning right if we cant move.
                int dirIndex = (int)(uc.getRandomDouble()*8.0);
                Direction randomDir = directions[dirIndex];
                for (int i = 0; i < 8; ++i){
                    //Note that the value of the following command is irrelevant.
                    if (uc.canPerformAction(ActionType.MOVE, randomDir, 0)){
                        uc.performAction(ActionType.MOVE, randomDir, 0);
                        break;
                    }
                    randomDir = randomDir.rotateRight();

                }
                //Check if there are Care Packages at an adjacent tile. If so, retrieve them.
                for (Direction dir : directions){
                    Location adjLocation = uc.getLocation().add(dir);
                    if (!uc.canSenseLocation(adjLocation)) continue;
                    CarePackage cp = uc.senseCarePackage(adjLocation);
                    if (cp != null){
                        if (uc.canPerformAction(ActionType.RETRIEVE, dir, 0)){
                            uc.performAction(ActionType.RETRIEVE, dir, 0);
                            break;
                        }
                    }
                }

                //If we have 1 or 2 oxygen left, terraform my tile (alternatively, terraform a random tile)
                if (uc.getAstronautInfo().getOxygen() <= 2) {
                    if (uc.canPerformAction(ActionType.TERRAFORM, Direction.ZERO, 0)){
                        uc.performAction(ActionType.TERRAFORM, Direction.ZERO, 0);
                    }
                    else {
                        dirIndex = (int) (uc.getRandomDouble() * 8.0);
                        randomDir = directions[dirIndex];
                        for (int i = 0; i < 8; ++i) {
                            //Note that the value of the following command is irrelevant.
                            if (uc.canPerformAction(ActionType.TERRAFORM, randomDir, 0)) {
                                uc.performAction(ActionType.TERRAFORM, randomDir, 0);
                                break;
                            }
                            randomDir = randomDir.rotateRight();
                        }
                    }
                }
            }'''
            uc.yield(); // End of turn
        }
    }
}
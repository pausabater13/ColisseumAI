package TuHotBot_1;

import aic2024.user.*;

public class UnitPlayer {

    //This array will be useful
    Direction[] directions = Direction.values();

    public void run(UnitController uc) {
        // Code to be executed only at the beginning of the unit's lifespan
        
        while (true) {
            // Code to be executed every round
            

            //Pau
            //System.out.println(uc.getTeam());
            //System.out.println(uc.getOpponent());
            //System.out.println(uc.getMapHeight());  // Altura del mapa
            //System.out.println(uc.getMapWidth());   // Amplada del mapa
            /* // Print each direction
            for (Direction direction : directions) {
                System.out.println(direction);
            } */

            //Joan


            //Case in which we are a HQ
            if (uc.isStructure() && uc.getType() == StructureType.HQ){
                //NEW (PAU)---------------------------------
                /*//Spawn exactly one astronaut with 30 oxygen, if possible
                for (Direction dir : directions){
                    CarePackage cp;
                    for (CarePackage c : CarePackage.values()){
                        //System.out.println(c);
                        if(uc.getStructureInfo().getCarePackagesOfType(c)>0){
                            cp = c;
                            break;
                        }
                    }
                    if (uc.canEnlistAstronaut(dir, 30, cp)){
                        uc.enlistAstronaut(dir, 30, cp);
                        break;
                    }
                    else {
                        if (uc.canEnlistAstronaut(dir, 30, null)){
                        uc.enlistAstronaut(dir, 30, null);
                        break;
                    }
                    }
                }
                */
                //------------------------------------
                //OLD:-------------------------------
                //Spawn exactly one astronaut with 30 oxygen, if possible
                for (Direction dir : directions){
                    if (uc.canEnlistAstronaut(dir, 30, null)){
                        uc.enlistAstronaut(dir, 30, null);
                        break;
                    }
                }
                //-------------------------------
            }

            //Case in which we are an astronaut
            else if (!uc.isStructure()){
                // Get the hot zones (assuming hotzones are used somewhere)
                Location[] hotzones = uc.senseObjects(MapObject.HOT_ZONE, 25);

                Location[] plantes = new Location[10];
                Location[] oxigens = new Location[10];
                Location[] domes = new Location[10];
                Location[] jumps = new Location[10];
                Location[] radios = new Location[10];
                Location[] settlements = new Location[10];
                Location[] suits = new Location[10];
                Location[] kits = new Location[10];

                CarePackageInfo[] coses = uc.senseCarePackages(25);
                for (CarePackageInfo cosa : coses) {
                    if (cosa.getCarePackageType() == CarePackage.PLANTS) {
                        boolean afegit = false;
                        for (int i = 0; i < plantes.length; i++) {
                            if (plantes[i] == null) {
                                plantes[i] = cosa.getLocation();
                                afegit = true;
                                System.out.println("Element afegit a la posició: " + i);
                                break;
                            }
                        }
                        if (!afegit) {
                            System.out.println("No hi ha espai per més plantes.");
                        }
                    } else if (cosa.getCarePackageType() == CarePackage.OXYGEN_TANK) {
                        boolean afegit = false;
                        for (int i = 0; i < oxigen.length; i++) {
                            if (oxigen[i] == null) {
                                oxigen[i] = cosa.getLocation();
                                afegit = true;
                                System.out.println("Oxygen afegit a la posició: " + i);
                                break;
                            }
                        }
                        if (!afegit) {
                            System.out.println("No hi ha espai per més oxigen.");
                        }
                    } else if (cosa.getCarePackageType() == CarePackage.DOME) {
                        boolean afegit = false;
                        for (int i = 0; i < domes.length; i++) {
                            if (domes[i] == null) {
                                domes[i] = cosa.getLocation();
                                afegit = true;
                                System.out.println("Dome afegit a la posició: " + i);
                                break;
                            }
                        }
                        if (!afegit) {
                            System.out.println("No hi ha espai per més domes.");
                        }
                    } else if (cosa.getCarePackageType() == CarePackage.HYPERJUMP) {
                        boolean afegit = false;
                        for (int i = 0; i < jumps.length; i++) {
                            if (jumps[i] == null) {
                                jumps[i] = cosa.getLocation();
                                afegit = true;
                                System.out.println("Hyperjump afegit a la posició: " + i);
                                break;
                            }
                        }
                        if (!afegit) {
                            System.out.println("No hi ha espai per més hyperjumps.");
                        }
                    } else if (cosa.getCarePackageType() == CarePackage.RADIO) {
                        boolean afegit = false;
                        for (int i = 0; i < radios.length; i++) {
                            if (radios[i] == null) {
                                radios[i] = cosa.getLocation();
                                afegit = true;
                                System.out.println("Radio afegida a la posició: " + i);
                                break;
                            }
                        }
                        if (!afegit) {
                            System.out.println("No hi ha espai per més radios.");
                        }
                    } else if (cosa.getCarePackageType() == CarePackage.REINFORCED_SUIT) {
                        boolean afegit = false;
                        for (int i = 0; i < suits.length; i++) {
                            if (suits[i] == null) {
                                suits[i] = cosa.getLocation();
                                afegit = true;
                                System.out.println("Reinforced Suit afegit a la posició: " + i);
                                break;
                            }
                        }
                        if (!afegit) {
                            System.out.println("No hi ha espai per més reinforced suits.");
                        }
                    } else if (cosa.getCarePackageType() == CarePackage.SETTLEMENT) {
                        boolean afegit = false;
                        for (int i = 0; i < settlements.length; i++) {
                            if (settlements[i] == null) {
                                settlements[i] = cosa.getLocation();
                                afegit = true;
                                System.out.println("Settlement afegit a la posició: " + i);
                                break;
                            }
                        }
                        if (!afegit) {
                            System.out.println("No hi ha espai per més settlements.");
                        }
                    } else if (cosa.getCarePackageType() == CarePackage.SURVIVAL_KIT) {
                        boolean afegit = false;
                        for (int i = 0; i < kits.length; i++) {
                            if (kits[i] == null) {
                                kits[i] = cosa.getLocation();
                                afegit = true;
                                System.out.println("Survival Kit afegit a la posició: " + i);
                                break;
                            }
                        }
                        if (!afegit) {
                            System.out.println("No hi ha espai per més survival kits.");
                        }
                    }
                }


                //---------------------------------
                //NEW--------------------------------------
                CarePackageInfo[] cpInfo = uc.senseCarePackages(GameConstants.ASTRONAUT_VISION_RANGE);
                Location userLocation = uc.getLocation();
                /*Arrays.sort(cpInfo, new Comparator<CarePackageInfo>() {
                    @Override
                    public int compare(CarePackageInfo a, CarePackageInfo b) {
                        int distA = a.getLocation().distanceSquared(userLocation);
                        int distB = b.getLocation().distanceSquared(userLocation);
                        return Integer.compare(distA, distB); // Compare distances, sorting in ascending order
                    }
                });*/
                /*System.out.println("CP distances:");
                for (CarePackageInfo cp : cpInfo){
                    System.out.println(cp.getLocation().distanceSquared(userLocation));
                }
                System.out.println("------------");*/
                //------------------------------------
                /*
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
                            //Note that the 'value' of the following command is irrelevant.
                            if (uc.canPerformAction(ActionType.TERRAFORM, randomDir, 0)) {
                                uc.performAction(ActionType.TERRAFORM, randomDir, 0);
                                break;
                            }
                            randomDir = randomDir.rotateRight();
                        }
                    }
                }
                */
            }
            uc.yield(); // End of turn
        }
    }
}
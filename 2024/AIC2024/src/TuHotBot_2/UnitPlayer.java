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
                // Get the hot zones
                Location[] hotzones = uc.senseObjects(MapObject.HOT_ZONE, 25);

                // Initialize arrays for each care package type
                Location[] plantes = new Location[0];
                Location[] oxigens = new Location[0];
                Location[] domes = new Location[0];
                Location[] jumps = new Location[0];
                Location[] radios = new Location[0];
                Location[] settlements = new Location[0];
                Location[] suits = new Location[0];
                Location[] kits = new Location[0];

                // Sense care packages
                CarePackageInfo[] coses = uc.senseCarePackages(25);
                
                // Process each care package
                for (CarePackageInfo cosa : coses) {
                    Location location = cosa.getLocation(); // Get location once

                    if(cosa.getCarePackageType() == CarePackage.PLANTS){
                        plantes = appendLocation(plantes, location);
                        System.out.println("Plants Location: " + location);
                    }
                    if(cosa.getCarePackageType() == CarePackage.OXYGEN_TANK){
                            oxigens = appendLocation(oxigens, location);
                            System.out.println("Oxygen Tank Location: " + location);
                    }
                    if(cosa.getCarePackageType() == CarePackage.DOME){
                            domes = appendLocation(domes, location);
                            System.out.println("Dome Location: " + location);
                    }
                    if(cosa.getCarePackageType() == CarePackage.HYPERJUMP){
                            jumps = appendLocation(jumps, location);
                            System.out.println("Hyperjump Location: " + location);
                    }
                    if(cosa.getCarePackageType() == CarePackage.RADIO){
                            radios = appendLocation(radios, location);
                            System.out.println("Radio Location: " + location);
                    }
                    if(cosa.getCarePackageType() == CarePackage.REINFORCED_SUIT){
                            suits = appendLocation(suits, location);
                            System.out.println("Reinforced Suit Location: " + location);
                    }
                    if(cosa.getCarePackageType() == CarePackage.SETTLEMENT){
                            settlements = appendLocation(settlements, location);
                            System.out.println("Settlement Location: " + location);
                    }
                    if(cosa.getCarePackageType() == CarePackage.SURVIVAL_KIT){
                            kits = appendLocation(kits, location);
                            System.out.println("Survival Kit Location: " + location);
                    }
                }
                /*Location[] hotzones = uc.senseObjects(MapObject.HOT_ZONE, 25);

                Location[] plantes = new Location[0];
                Location[] oxigens = new Location[0];
                Location[] domes = new Location[0];
                Location[] jumps = new Location[0];
                Location[] radios = new Location[0];
                Location[] settlements = new Location[0];
                Location[] suits = new Location[0];
                Location[] kits = new Location[0];

                CarePackageInfo[] coses = uc.senseCarePackages(25);
                for (CarePackageInfo cosa : coses) {
                    if(cosa.getCarePackageType() == CarePackage.PLANTS){
                        plantes.add(cosa.getLocation());
                        System.out.println(cosa.getLocation());
                    }
                    else if(cosa.getCarePackageType() == CarePackage.OXYGEN_TANK){
                        oxigen.add(cosa.getLocation());
                        System.out.println(cosa.getLocation());
                    }
                    else if(cosa.getCarePackageType() == CarePackage.DOME){
                        domes.add(cosa.getLocation());
                        System.out.println(cosa.getLocation());
                    }
                    else if(cosa.getCarePackageType() == CarePackage.HYPERJUMP){
                        jumps.add(cosa.getLocation());
                        System.out.println(cosa.getLocation());
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
                }*/

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

    // Method to append a location to an array
    private Location[] appendLocation(Location[] array, Location newLocation) {
        Location[] newArray = new Location[array.length + 1]; // Create a new array
        System.arraycopy(array, 0, newArray, 0, array.length); // Copy existing elements
        newArray[array.length] = newLocation; // Add the new location
        return newArray; // Return the new array
    }
}
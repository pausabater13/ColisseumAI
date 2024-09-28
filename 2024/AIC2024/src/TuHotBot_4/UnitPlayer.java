package TuHotBot_4;

import aic2024.user.*;

public class UnitPlayer {

    //This array will be useful
    Direction[] directions = Direction.values();

    public void run(UnitController uc) {
        // Code to be executed only at the beginning of the unit's lifespan

        while (true) {
            // Code to be executed every round

            //Case in which we are a HQ
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
                            if (plantes[i] == null) {plantes[i] = cosa.getLocation();afegit = true; break;}
                        }
                        if (!afegit) {}
                    } else if (cosa.getCarePackageType() == CarePackage.OXYGEN_TANK) {
                        boolean afegit = false;
                        for (int i = 0; i < oxigens.length; i++) {
                            if (oxigens[i] == null) {oxigens[i] = cosa.getLocation();afegit = true;System.out.println(i);break;}
                        }
                        if (!afegit) {}
                    } else if (cosa.getCarePackageType() == CarePackage.DOME) {
                        boolean afegit = false;
                        for (int i = 0; i < domes.length; i++) {
                            if (domes[i] == null) { domes[i] = cosa.getLocation(); afegit = true;break;}
                        }
                        if (!afegit) {}
                    } else if (cosa.getCarePackageType() == CarePackage.HYPERJUMP) {
                        boolean afegit = false;
                        for (int i = 0; i < jumps.length; i++) {
                            if (jumps[i] == null) {jumps[i] = cosa.getLocation(); afegit = true; break;}
                        }
                        if (!afegit) {}
                    } else if (cosa.getCarePackageType() == CarePackage.RADIO) {
                        boolean afegit = false;
                        for (int i = 0; i < radios.length; i++) {
                            if (radios[i] == null) {radios[i] = cosa.getLocation();afegit = true;break;}
                        }
                        if (!afegit) {}
                    } else if (cosa.getCarePackageType() == CarePackage.REINFORCED_SUIT) {
                        boolean afegit = false;
                        for (int i = 0; i < suits.length; i++) {
                            if (suits[i] == null) {suits[i] = cosa.getLocation();afegit = true;break;}
                        }
                        if (!afegit) {}
                    } else if (cosa.getCarePackageType() == CarePackage.SETTLEMENT) {
                        boolean afegit = false;
                        for (int i = 0; i < settlements.length; i++) {
                            if (settlements[i] == null) {settlements[i] = cosa.getLocation(); afegit = true; break;}
                        }
                        if (!afegit) {
                        }
                    } else if (cosa.getCarePackageType() == CarePackage.SURVIVAL_KIT) {
                        boolean afegit = false;
                        for (int i = 0; i < kits.length; i++) {
                            if (kits[i] == null) { kits[i] = cosa.getLocation();afegit = true;break;}
                        }
                        if (!afegit) {}
                    }
                }


                
            }
            uc.yield(); // End of turn
        }
    }
}
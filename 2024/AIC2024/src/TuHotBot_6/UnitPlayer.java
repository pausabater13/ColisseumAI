package TuHotBot_6;

import aic2024.user.*;

public class UnitPlayer {

    //This array will be useful
    Direction[] directions = Direction.values();

    public void run(UnitController uc) {
        // Code to be executed only at the beginning of the unit's lifespan

        while (true) {
            // Code to be executed every round

            //Case in which we are a HQ
            if (uc.isStructure() && uc.getType() == StructureType.HQ && uc.getRound()%4==0){
                //Spawn exactly one astronaut with 30 oxygens, if possible
                for (Direction dir : directions){
                    int o2 = 30;//70 - (1000-uc.getRound())/1000*50;
                    //New-----------
                    CarePackage cp = null;
                    for (CarePackage c : CarePackage.values()){
                        //System.out.println(c);
                        boolean found = false;
                        if(uc.getStructureInfo().getCarePackagesOfType(c)>0) {
                            cp = c; found = true;
                            if (uc.canEnlistAstronaut(dir, o2, cp)) uc.enlistAstronaut(dir, o2, cp);
                        }
                        if (found) break;

                    }
                    
                    if (uc.canEnlistAstronaut(dir, o2, cp)) uc.enlistAstronaut(dir, o2, cp);
                    //else 
                    //-------------------
                    //if (uc.canEnlistAstronaut(dir, 02, null)) uc.enlistAstronaut(dir, 02, null);
                }
            }

            //Case in which we are an astronaut
            else if (!uc.isStructure()){
                // Get the hot zones (assuming hotzones are used somewhere)
                Location[] hotzones = uc.senseObjects(MapObject.HOT_ZONE, 25);

                Location[] plants = new Location[10];
                Location[] oxygens = new Location[10];
                Location[] domes = new Location[10];
                Location[] jumps = new Location[10];
                Location[] radios = new Location[10];
                Location[] settlements = new Location[10];
                Location[] suits = new Location[10];
                Location[] kits = new Location[10];
                Location[] other = new Location[10];

                CarePackageInfo[] coses = uc.senseCarePackages(25);
                Location userLocation = uc.getLocation();
                for (CarePackageInfo cosa : coses) {
                    if (cosa.getCarePackageType() == CarePackage.PLANTS) {
                        boolean afegit = false;
                        //System.out.println(CarePackage.PLANTS);
                        for (int i = 0; i < plants.length; i++) {
                            if (plants[i] == null) {plants[i] = cosa.getLocation();afegit = true; break;}
                            //System.out.println(userLocation.distanceSquared(plants[i]));
                        }
                        if (!afegit) {}
                    } else if (cosa.getCarePackageType() == CarePackage.OXYGEN_TANK) {
                        boolean afegit = false;
                        for (int i = 0; i < oxygens.length; i++) {
                            if (oxygens[i] == null) {oxygens[i] = cosa.getLocation();afegit = true; break;}
                        }
                        if (!afegit) {}
                    } else if (cosa.getCarePackageType() == CarePackage.DOME) {
                        boolean afegit = false;
                        //for (int i = 0; i < domes.length; i++) {
                            //if (domes[i] == null) { domes[i] = cosa.getLocation(); afegit = true;break;}
                        for (int i = 0; i < other.length; i++) {
                            if (other[i] == null) { other[i] = cosa.getLocation(); afegit = true;break;}
                        }
                        if (!afegit) {}
                    } else if (cosa.getCarePackageType() == CarePackage.HYPERJUMP) {
                        boolean afegit = false;
                        //for (int i = 0; i < jumps.length; i++) {
                            //if (jumps[i] == null) {jumps[i] = cosa.getLocation(); afegit = true; break;}
                        for (int i = 0; i < other.length; i++) {
                            if (other[i] == null) { other[i] = cosa.getLocation(); afegit = true;break;}
                        }
                        if (!afegit) {}
                    } else if (cosa.getCarePackageType() == CarePackage.RADIO) {
                        boolean afegit = false;
                        //for (int i = 0; i < radios.length; i++) {
                            //if (radios[i] == null) {radios[i] = cosa.getLocation();afegit = true;break;}
                        for (int i = 0; i < other.length; i++) {
                            if (other[i] == null) { other[i] = cosa.getLocation(); afegit = true;break;}
                        }
                        if (!afegit) {}
                    } else if (cosa.getCarePackageType() == CarePackage.REINFORCED_SUIT) {
                        boolean afegit = false;
                        //for (int i = 0; i < suits.length; i++) {
                        //    if (suits[i] == null) {suits[i] = cosa.getLocation();afegit = true;break;}
                        for (int i = 0; i < other.length; i++) {
                            if (other[i] == null) { other[i] = cosa.getLocation(); afegit = true;break;}
                        }
                        if (!afegit) {}
                    } else if (cosa.getCarePackageType() == CarePackage.SETTLEMENT) {
                        boolean afegit = false;
                        //for (int i = 0; i < settlements.length; i++) {
                        //    if (settlements[i] == null) {settlements[i] = cosa.getLocation(); afegit = true; break;}
                        for (int i = 0; i < other.length; i++) {
                            if (other[i] == null) { other[i] = cosa.getLocation(); afegit = true;break;}
                        }
                        if (!afegit) {
                        }
                    } else if (cosa.getCarePackageType() == CarePackage.SURVIVAL_KIT) {
                        boolean afegit = false;
                        //for (int i = 0; i < kits.length; i++) {
                        //    if (kits[i] == null) { kits[i] = cosa.getLocation();afegit = true;break;}
                        for (int i = 0; i < other.length; i++) {
                            if (other[i] == null) { other[i] = cosa.getLocation(); afegit = true;break;}
                        }
                        if (!afegit) {}
                    }
                }

                Direction dir = Direction.ZERO;
                if (uc.getAstronautInfo().getOxygen() <= 2) {
                    if (uc.canPerformAction(ActionType.TERRAFORM, Direction.ZERO, 0)){
                        uc.performAction(ActionType.TERRAFORM, Direction.ZERO, 0);
                    }
                    else {
                        int dirIndex = (int)(uc.getRandomDouble()*8.0);
                        dir = directions[dirIndex];
                        while(!uc.canPerformAction(ActionType.TERRAFORM, dir, 0)){
                            dir = dir.rotateRight();
                        }
                    }
                } 
                if(plants[0]!=null){
                    //Si estem al costat, recollim
                    if (userLocation.distanceSquared(plants[0])<2){
                        /*if(uc.getOxygen()>2 and uc.canPerformAction(ActionType.TRANSFER_OXYGEN, )){
                        }*/
                        if(uc.canPerformAction(ActionType.RETRIEVE, userLocation.directionTo(plants[0]),0))
                            uc.performAction(ActionType.RETRIEVE, userLocation.directionTo(plants[0]),0);//Recollim
                    } else {
                        dir = userLocation.directionTo(plants[0]);
                        while(!uc.canPerformAction(ActionType.MOVE, dir, 0))
                            dir = dir.rotateRight();
                        uc.performAction(ActionType.MOVE, dir,0);
                    }
                } else if(oxygens[0]!=null){
                    //Si estem al costat, recollim
                    if (userLocation.distanceSquared(oxygens[0])<2){
                        if(uc.canPerformAction(ActionType.RETRIEVE, userLocation.directionTo(oxygens[0]),0))
                            uc.performAction(ActionType.RETRIEVE, userLocation.directionTo(oxygens[0]),0);//Recollim
                    } else {
                        dir = userLocation.directionTo(oxygens[0]);
                        while(!uc.canPerformAction(ActionType.MOVE, dir, 0))
                            dir = dir.rotateRight();
                        uc.performAction(ActionType.MOVE, dir,0);
                    }
                } else if(other[0]!=null){
                    //Si estem al costat, recollim
                    if (userLocation.distanceSquared(other[0])<2){
                        if(uc.canPerformAction(ActionType.RETRIEVE, userLocation.directionTo(other[0]),0))
                            uc.performAction(ActionType.RETRIEVE, userLocation.directionTo(other[0]),0);//Recollim
                    } else {
                        dir = userLocation.directionTo(other[0]);
                        while(!uc.canPerformAction(ActionType.MOVE, dir, 0))
                            dir = dir.rotateRight();
                        uc.performAction(ActionType.MOVE, dir,0);
                    }
                } /*else if(hotzones[0]!=null){
                    //Si estem al costat, recollim
                    if(uc.canPerformAction(ActionType.MOVE, userLocation.directionTo(hotzones[0]),0))
                        uc.performAction(ActionType.MOVE, userLocation.directionTo(hotzones[0]),0);
                }  */
                //If we have 1 or 2 oxygens left, terraform my tile (alternatively, terraform a random tile)
                else {
                    int id = uc.getID()%5;
                    Direction direction = Direction.ZERO;
                    if (id == 1) {          direction = userLocation.directionTo(new Location(uc.getMapWidth()-5, uc.getMapHeight()-5));
                    } else if (id == 2) {   direction = userLocation.directionTo(new Location(5, uc.getMapHeight()-5));
                    } else if (id == 3) {   direction = userLocation.directionTo(new Location(uc.getMapWidth()-5, 5));
                    } else if (id == 4) {   direction = userLocation.directionTo(new Location(5, 5));
                    } else if (id == 0) {   direction = userLocation.directionTo(new Location(uc.getMapWidth()/2, uc.getMapHeight()/2));
                    }


                    while(!uc.canPerformAction(ActionType.MOVE, direction, 0))
                        direction = direction.rotateRight();
                    uc.performAction(ActionType.MOVE, direction,0);
                }
            }
            uc.yield(); // End of turn
        }
    }
}
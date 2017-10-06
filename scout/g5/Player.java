package scout.g5;

import scout.sim.*;

import java.util.*;


//Read scout.sim.Player for more information!
public class Player extends scout.sim.Player {
    List<Point> enemyLocations;
    List<Point> safeLocations;
    Random gen;
    int t,n;
    int x = -1;
    int y = -1;
    int dx = 0, dy = 0;
    int seed;
    int id;

    /**
     * better to use init instead of constructor, don't modify ID or simulator will error
     */
    public Player(int id) {
        super(id);
        seed=id;
        this.id = id;
    }

    /**
     *   Called at the start
     */
    @Override
    public void init(String id, int s, int n, int t, List<Point> landmarkLocations) {
        enemyLocations = new ArrayList<>();
        safeLocations = new ArrayList<>();
        gen = new Random(seed);
        this.t = t;
        this.n = n;
    }

    /**
     * nearby IDs is a 3 x 3 grid of nearby IDs with you in the center (1,1) position. A position is null if it is off the board.
     * Enemy IDs start with 'E', Player start with 'P', Outpost with 'O' and landmark with 'L'.
     *
     */
    @Override
    public Point move(ArrayList<ArrayList<ArrayList<String>>> nearbyIds, List<CellObject> concurrentObjects) {
        //System.out.println("I'm at " + x + " " + y);
        for(int i = 0 ; i < 3; ++ i) {
            for(int j = 0 ; j < 3 ; ++ j) {
                boolean safe = true;
                if(nearbyIds.get(i).get(j) == null) continue;
                for(String ID : nearbyIds.get(i).get(j)) {
                    if(ID.charAt(0) == 'E') {
                        safe = false;
                    }
                }

                if(x != -1) {
                    Point consideredLocation = new Point(x + i - 1, y + j - 1);
                    if(safe) {
                        if(!safeLocations.contains(consideredLocation)) {
                            safeLocations.add(consideredLocation);
                        }
                    } else {
                        if(!enemyLocations.contains(consideredLocation)) {
                            enemyLocations.add(consideredLocation);
                        }
                    }
                }
            }
        }
        for(CellObject obj : concurrentObjects) {
            if (obj instanceof Player) {
                //communicate using custom methods?
                ((Player) obj).stub();
            } else if (obj instanceof Enemy) {

            } else if (obj instanceof Landmark) {
                x = ((Landmark) obj).getLocation().x;
                y = ((Landmark) obj).getLocation().y;
            } else if (obj instanceof Outpost) {
                Object data = ((Outpost) obj).getData();
                if(data == null) {
                    ((Outpost) obj).setData((Object)"yay!!");
                }
                for(Point safe : safeLocations) {
                    ((Outpost) obj).addSafeLocation(safe);
                }
                for(Point unsafe : enemyLocations) {
                    ((Outpost) obj).addEnemyLocation(unsafe);
                }
            }
        }


        int moveX = 0, moveY = 0;

        if (id % 4 == 0 || id % 4 == 1) {
            if (nearbyIds.get(0).get(1) != null) {
                moveX = -1;
                setX(moveX);
            }
            if (id % 4 == 0) {
                if (nearbyIds.get(1).get(0) != null) {
                    moveY = -1;
                    setY(moveY);
                }
            }
            else {
                if (nearbyIds.get(1).get(2) != null) {
                    moveY = 1;
                    setY(moveY);
                }
            }
        }
        else {
            if (nearbyIds.get(2).get(1) != null) {
                moveX = 1;
                setX(moveX);
            }
            if (id % 4 == 2) {
                if (nearbyIds.get(1).get(0) != null) {
                    moveY = -1;
                    setY(moveY);
                }
            }
            else {
                if (nearbyIds.get(1).get(2) != null) {
                    moveY = 1;
                    setY(moveY);
                }
            }
        }

        // System.out.println("id: " + id + " movex: " + moveX + " movey: " + moveY);
        return new Point(moveX, moveY);
    }

    private void setX(int move) {
        if (dx != -1)
            dx = move;
    }

    private void setY(int move) {
        if (dy != -1)
            dy = move;
    }

    public void stub() {
        ;
    }

    @Override
    public void communicate(ArrayList<ArrayList<ArrayList<String>>> nearbyIds, List<CellObject> concurrentObjects) {
        --t;
        System.out.println("communicate");
    }

    @Override
    public void moveFinished() {
        x += dx;
        y += dy;
        dx = dy = 0;
    }
}

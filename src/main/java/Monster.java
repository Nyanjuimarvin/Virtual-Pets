import org.sql2o.Connection;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public abstract class Monster{
    private String name;
    private int personId;
    private int id;
    private int playLevel;
    private int foodLevel;
    private int sleepLevel;
    private Timestamp birthday;
    private Timestamp lastSlept;
    private Timestamp lastAte;
    private Timestamp lastPlayed;

    private Timer timer;//Timer class
    //Constants
    public static final int MAX_FOOD_LEVEL = 3;
    public static final int MAX_SLEEP_LEVEL = 8;
    public static final int MAX_PLAY_LEVEL = 12;
    public static final int MIN_ALL_LEVELS = 0;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Monster)) return false;
        Monster monster = (Monster) o;
        return personId == monster.personId && Objects.equals(name, monster.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, personId);
    }

    public Monster(String name, int personId){
        this.name =name;
        this.personId = personId;
        this.playLevel = MAX_PLAY_LEVEL / 2;
        this.foodLevel = MAX_FOOD_LEVEL / 2;
        this.sleepLevel = MAX_SLEEP_LEVEL / 2;
        timer = new Timer();//initialize timer
    }

    public String getName() {
        return name;
    }

    public int getPersonId() {
        return personId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getPlayLevel() {
        return playLevel;
    }

    public int getSleepLevel() {
        return sleepLevel;
    }

    public int getFoodLevel() {
        return foodLevel;
    }


    //Time


    public Timestamp getBirthday() {
        return birthday;
    }

    public Timestamp getLastAte() {
        return lastAte;
    }

    public Timestamp getLastPlayed() {
        return lastPlayed;
    }

    public Timestamp getLastSlept() {
        return lastSlept;
    }


    //Timer method

    public void startTimer(){
        Monster currentMonster = this;//Curr Object
        TimerTask timerTask = new TimerTask(){
            @Override
            public void run() {
                if (!currentMonster.isAlive()){
                    cancel();
                }
                depleteLevels();
            }
        };
        this.timer.schedule(timerTask, 0, 600);
    }


    //Life Check
    public boolean isAlive() {
        return foodLevel > MIN_ALL_LEVELS &&
                playLevel > MIN_ALL_LEVELS &&
                sleepLevel > MIN_ALL_LEVELS;
    }
//Deplete Health
    public void depleteLevels(){
        playLevel--;
        foodLevel--;
        sleepLevel--;
    }

    public void play(){
        if (playLevel >= MAX_PLAY_LEVEL){
            throw new UnsupportedOperationException("Monster is Tired..Can't play more");
        }
        try(Connection con = Db.sql2o.open()) {
            String sql = "UPDATE monsters SET lastplayed = now() WHERE id = :id";
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        }
        playLevel++;
    }

    public void sleep(){
        if (sleepLevel >= MAX_SLEEP_LEVEL){
            throw new UnsupportedOperationException("Monster is well rested..Can't sleep more");
        }try(Connection con = Db.sql2o.open()) {
            String sql = "UPDATE monsters SET lastslept = now() WHERE id = :id";
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        }
        sleepLevel++;
    }

    public void feed(){
        if(foodLevel >= MAX_FOOD_LEVEL){
        throw new UnsupportedOperationException("Monster is Full..Can't eat more");
        }
        try(Connection con = Db.sql2o.open()) {
            String sql = "UPDATE monsters SET lastate = now() WHERE id = :id";
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        }
        foodLevel++;
    }

    public void save(){
        try(Connection conn = Db.sql2o.open() ){
            String sql = "INSERT INTO monsters (name,personID,birthday) VALUES (:name,:personID,now())";
            this.id = (int)conn.createQuery(sql,true)
                    .addParameter("name",this.name)
                    .addParameter("personID",this.personId)
                    .executeUpdate()
                    .getKey();
        }
    }
}

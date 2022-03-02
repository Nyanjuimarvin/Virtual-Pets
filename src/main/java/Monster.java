import org.sql2o.Connection;

import java.util.List;
import java.util.Objects;

public class Monster {
    private String name;
    private int personId;
    private int id;
    private int playLevel;
    private int foodLevel;
    private int sleepLevel;

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
        playLevel++;
    }

    public void sleep(){
        if (sleepLevel >= MAX_SLEEP_LEVEL){
            throw new UnsupportedOperationException("Monster is well rested..Can't sleep more");
        }
        sleepLevel++;
    }

    public void feed(){
        if(foodLevel >= MAX_FOOD_LEVEL){
        throw new UnsupportedOperationException("Monster is Full..Can't eat more");
        }
        foodLevel++;
    }

    public void save(){
        try(Connection conn = Db.sql2o.open() ){
            this.id = (int)conn.createQuery("INSERT INTO monsters (name,personID) VALUES (:name,:personID)",true)
                    .addParameter("name",this.name)
                    .addParameter("personID",this.personId)
                    .executeUpdate()
                    .getKey();
        }
    }

    public static List <Monster> all(){
        try(Connection conn = Db.sql2o.open()){
            return conn.createQuery("SELECT * FROM monsters")
                    .executeAndFetch(Monster.class);
        }
    }

    public static Monster find(int id){
        try(Connection conn = Db.sql2o.open()) {
            return conn.createQuery("SELECT * FROM monsters WHERE id = :id")
                    .addParameter("id",id)
                    .executeAndFetchFirst(Monster.class);
        }
    }
}

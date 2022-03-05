import org.sql2o.Connection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Person {

    private String name;
    private String email;
    private int id;

    public Person(String name, String email){
        this.name = name;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return Objects.equals(name, person.name)
                && Objects.equals(email, person.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email);
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void save() {
        try(Connection con = Db.sql2o.open()) {
            String sql = "INSERT INTO persons (name, email) VALUES (:name, :email)";
            this.id = (int) con.createQuery(sql, true)
                    .addParameter("name", this.name)
                    .addParameter("email", this.email)
                    .executeUpdate()
                    .getKey();
        }
    }

    public static Person find(int id) {
        try(Connection con = Db.sql2o.open()) {
            String sql = "SELECT * FROM persons where id=:id";
            Person person = con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Person.class);
            return person;
        }
    }

    public static List<Person> all() {
        String sql = "SELECT * FROM persons";
        try(Connection con = Db.sql2o.open()) {
            return con.createQuery(sql).executeAndFetch(Person.class);
        }
    }

    //Previous getMonsters
//    public List<Monster> getMonsters() {
//        try(Connection con = Db.sql2o.open()) {
//            String sql = "SELECT * FROM monsters where personId=:id";
//            return con.createQuery(sql)
//                    .addParameter("id", this.id)
//                    .executeAndFetch(Monster.class);
//        }
//    }

    public List<Object> getMonsters() {
        List<Object> allMonsters = new ArrayList<Object>();

        try(Connection con = Db.sql2o.open()) {
            String sqlFire = "SELECT * FROM monsters WHERE personId=:id AND type='fire';";
            List<FireMonster> fireMonsters = con.createQuery(sqlFire)
                    .addParameter("id", this.id)
                    .throwOnMappingFailure(false)
                    .executeAndFetch(FireMonster.class);
            allMonsters.addAll(fireMonsters);//Add fire monsters contents to allMonsters

            String sqlWater = "SELECT * FROM monsters WHERE personId=:id AND type='water';";
            List<WaterMonster> waterMonsters = con.createQuery(sqlWater)
                    .addParameter("id", this.id)
                    .throwOnMappingFailure(false)
                    .executeAndFetch(WaterMonster.class);

            allMonsters.addAll(waterMonsters);//Add water monsters contents to allMonsters
        }

        return allMonsters;
    }
}

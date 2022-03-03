import org.joda.time.DateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class FireMonsterTest {
    @ExtendWith(DatabaseRule.class)
    public DatabaseRule databaseRule = new DatabaseRule();


    @Test
    public void FireMonster_instantiatesCorrectly_true() {
        FireMonster testFireMonster = new FireMonster("Bubbles", 1);
        assertTrue(testFireMonster instanceof FireMonster);
    }

    @Test
    public void FireMonster_instantiatesWithName_String() {
        FireMonster testFireMonster = new FireMonster("Bubbles", 1);
        assertEquals("Bubbles", testFireMonster.getName());
    }

    @Test
    public void FireMonster_instantiatesWithPersonId_int() {
        FireMonster testFireMonster = new FireMonster("Bubbles", 1);
        assertEquals(1, testFireMonster.getPersonId());
    }

    @Test
    public void equals_returnsTrueIfNameAndPersonIdAreSame_true() {
        FireMonster testFireMonster = new FireMonster("Bubbles", 1);
        FireMonster anotherFireMonster = new FireMonster("Bubbles", 1);
        assertTrue(testFireMonster.equals(anotherFireMonster));
    }
    @Test
    public void save_returnsTrueIfDescriptionsAretheSame() {
        FireMonster testFireMonster = new FireMonster("Bubbles", 1);
        testFireMonster.save();
        assertTrue(FireMonster.all().get(0).equals(testFireMonster));
    }


    @Test
    public void save_assignsIdToFireMonster() {
        FireMonster testFireMonster = new FireMonster("Bubbles", 1);
        testFireMonster.save();
        FireMonster savedFireMonster = FireMonster.all().get(0);
        assertEquals(savedFireMonster.getId(), testFireMonster.getId());
    }

    @Test
    public void all_returnsAllInstancesOfFireMonster_true() {
        FireMonster firstFireMonster = new FireMonster("Bubbles", 1);
        firstFireMonster.save();
        FireMonster secondFireMonster = new FireMonster("Spud", 1);
        secondFireMonster.save();
        assertEquals(true, FireMonster.all().get(0).equals(firstFireMonster));
        assertEquals(true, FireMonster.all().get(1).equals(secondFireMonster));
    }

    @Test
    public void find_returnsFireMonsterWithSameId_secondFireMonster() {
        FireMonster firstFireMonster = new FireMonster("Bubbles", 1);
        firstFireMonster.save();
        FireMonster secondFireMonster = new FireMonster("Spud", 3);
        secondFireMonster.save();
        assertEquals(FireMonster.find(secondFireMonster.getId()), secondFireMonster);
    }

    @Test
    public void save_savesPersonIdIntoDB_true() {
        Person testPerson = new Person("Henry", "henry@henry.com");
        testPerson.save();
        FireMonster testFireMonster = new FireMonster("Bubbles", testPerson.getId());
        testFireMonster.save();
        FireMonster savedFireMonster = FireMonster.find(testFireMonster.getId());
        assertEquals(savedFireMonster.getPersonId(), testPerson.getId());
    }

    //TESTS FOR CONSTANT INITIALIZATION
    @Test
    public void FireMonster_instantiatesWithHalfFullPlayLevel(){
        FireMonster testFireMonster = new FireMonster("Bubbles", 1);
        assertEquals(testFireMonster.getPlayLevel(), (FireMonster.MAX_PLAY_LEVEL / 2));
    }

    @Test
    public void FireMonster_instantiatesWithHalfFullSleepLevel(){
        FireMonster testFireMonster = new FireMonster("Bubbles", 1);
        assertEquals(testFireMonster.getSleepLevel(), (FireMonster.MAX_SLEEP_LEVEL / 2));
    }

    @Test
    public void FireMonster_instantiatesWithHalfFullFoodLevel(){
        FireMonster testFireMonster = new FireMonster("Bubbles", 1);
        assertEquals(testFireMonster.getFoodLevel(), (FireMonster.MAX_FOOD_LEVEL / 2));
    }

    @Test
    public void isAlive_confirmsFireMonsterIsAliveIfAllLevelsAboveMinimum_true(){
        FireMonster testFireMonster = new FireMonster("Bubbles", 1);
        assertEquals(testFireMonster.isAlive(), true);
    }

    //Reduce
    @Test
    public void depleteLevels_reducesAllLevels(){
        FireMonster testFireMonster = new FireMonster("Bubbles", 1);
        testFireMonster.depleteLevels();
        assertEquals(testFireMonster.getFoodLevel(), (FireMonster.MAX_FOOD_LEVEL / 2) - 1);
        assertEquals(testFireMonster.getSleepLevel(), (FireMonster.MAX_SLEEP_LEVEL / 2) - 1);
        assertEquals(testFireMonster.getPlayLevel(), (FireMonster.MAX_PLAY_LEVEL / 2) - 1);
    }

    //Is FireMonster alive
    @Test
    public void isAlive_recognizesFireMonsterIsDeadWhenLevelsReachMinimum_false(){
        FireMonster testFireMonster = new FireMonster("Bubbles", 1);
        for(int i = FireMonster.MIN_ALL_LEVELS; i <= FireMonster.MAX_FOOD_LEVEL; i++){
            testFireMonster.depleteLevels();
        }
        assertFalse(testFireMonster.isAlive());
    }

    //Increase
    @Test
    public void play_increasesFireMonsterPlayLevel(){
        FireMonster testFireMonster = new FireMonster("Bubbles", 1);
        testFireMonster.play();
        assertTrue(testFireMonster.getPlayLevel() > (FireMonster.MAX_PLAY_LEVEL / 2));
    }

    @Test
    public void sleep_increasesFireMonsterSleepLevel(){
        FireMonster testFireMonster = new FireMonster("Bubbles", 1);
        testFireMonster.sleep();
        assertTrue(testFireMonster.getSleepLevel() > (FireMonster.MAX_SLEEP_LEVEL / 2));
    }


    @Test
    public void feed_increasesFireMonsterFoodLevel(){
        FireMonster testFireMonster = new FireMonster("Bubbles", 1);
        testFireMonster.feed();
        assertTrue(testFireMonster.getFoodLevel() > (FireMonster.MAX_FOOD_LEVEL / 2));
    }

    //Food
    @Test()
    public void feed_throwsExceptionIfFoodLevelIsAtMaxValue(){
        FireMonster testFireMonster = new FireMonster("Bubbles", 1);
        Throwable exception = assertThrows(UnsupportedOperationException.class,()->{
            for(int i = FireMonster.MIN_ALL_LEVELS; i <= (FireMonster.MAX_FOOD_LEVEL); i++){
                testFireMonster.feed();
            }
        },"Monster is Full..Can't eat more");
        assertEquals("Monster is Full..Can't eat more",exception.getMessage());
    }

    @Test
    public void FireMonster_foodLevelCannotGoBeyondMaxValue(){
        FireMonster testFireMonster = new FireMonster("Bubbles", 1);
        for(int i = FireMonster.MIN_ALL_LEVELS; i <= FireMonster.MAX_FOOD_LEVEL; i++){
            try {
                testFireMonster.feed();
            } catch (UnsupportedOperationException exception){ }
        }
        assertTrue(testFireMonster.getFoodLevel() <= FireMonster.MAX_FOOD_LEVEL);
    }


    //Play

    @Test()
    public void play_throwsExceptionIfPlayLevelIsAtMaxValue(){
        FireMonster testFireMonster = new FireMonster("Bubbles", 1);
        Throwable exception = assertThrows(UnsupportedOperationException.class,()->{
            for(int i = FireMonster.MIN_ALL_LEVELS; i <= (FireMonster.MAX_PLAY_LEVEL); i++){
                testFireMonster.play();
            }
        },"Monster is Tired..Can't play more");
        assertEquals("Monster is Tired..Can't play more",exception.getMessage());
    }

    @Test
    public void FireMonster_playLevelCannotGoBeyondMaxValue(){
        FireMonster testFireMonster = new FireMonster("Bubbles", 1);
        for(int i = FireMonster.MIN_ALL_LEVELS; i <= (FireMonster.MAX_PLAY_LEVEL); i++){
            try {
                testFireMonster.play();
            } catch (UnsupportedOperationException exception){ }
        }
        assertTrue(testFireMonster.getPlayLevel() <= FireMonster.MAX_PLAY_LEVEL);
    }


    //Sleep
    @Test()
    public void sleep_throwsExceptionIfSleepLevelIsAtMaxValue(){
        FireMonster testFireMonster = new FireMonster("Bubbles", 1);
        Throwable exception = assertThrows(UnsupportedOperationException.class,()->{
            for(int i = FireMonster.MIN_ALL_LEVELS; i <= (FireMonster.MAX_SLEEP_LEVEL); i++){
                testFireMonster.sleep();
            }
        },"Monster is well rested..Can't sleep more");
        assertEquals("Monster is well rested..Can't sleep more",exception.getMessage());
    }

    @Test
    public void FireMonster_sleepLevelCannotGoBeyondMaxValue(){
        FireMonster testFireMonster = new FireMonster("Bubbles", 1);
        for(int i = FireMonster.MIN_ALL_LEVELS; i <= (FireMonster.MAX_SLEEP_LEVEL); i++){
            try {
                testFireMonster.sleep();
            } catch (UnsupportedOperationException exception){ }
        }
        assertTrue(testFireMonster.getSleepLevel() <= FireMonster.MAX_SLEEP_LEVEL);
    }

    @Test
    public void save_recordsTimeOfCreationInDatabase() {
        FireMonster testFireMonster = new FireMonster("Bubbles", 1);
        testFireMonster.save();
        Timestamp savedFireMonsterBirthday = FireMonster.find(testFireMonster.getId()).getBirthday();
        Timestamp rightNow = new Timestamp(new Date().getTime());
        DateTime saved = new DateTime(savedFireMonsterBirthday);
        DateTime now = new DateTime(rightNow);
        System.out.println(saved);
        System.out.println(now);
        System.out.println(now.toDateTime());
        assertEquals(now.dayOfWeek(), saved.dayOfWeek());
    }

    @Test
    public void sleep_recordsTimeLastSleptInDatabase() {
        FireMonster testFireMonster = new FireMonster("Bubbles", 1);
        testFireMonster.save();
        testFireMonster.sleep();
        Timestamp savedFireMonsterLastSlept = FireMonster.find(testFireMonster.getId()).getLastSlept();
        Timestamp rightNow = new Timestamp(new Date().getTime());
        assertEquals(DateFormat.getDateTimeInstance().format(rightNow), DateFormat.getDateTimeInstance().format(savedFireMonsterLastSlept));
    }


    @Test
    public void feed_recordsTimeLastAteInDatabase() {
        FireMonster testFireMonster = new FireMonster("Bubbles", 1);
        testFireMonster.save();
        testFireMonster.feed();
        Timestamp savedFireMonsterLastAte = FireMonster.find(testFireMonster.getId()).getLastAte();
        Timestamp rightNow = new Timestamp(new Date().getTime());
        assertEquals(DateFormat.getDateTimeInstance().format(rightNow), DateFormat.getDateTimeInstance().format(savedFireMonsterLastAte));
    }

    @Test
    public void play_recordsTimeLastPlayedInDatabase() {
        FireMonster testFireMonster = new FireMonster("Bubbles", 1);
        testFireMonster.save();
        testFireMonster.play();
        Timestamp savedFireMonsterLastPlayed = FireMonster.find(testFireMonster.getId()).getLastPlayed();
        Timestamp rightNow = new Timestamp(new Date().getTime());
        assertEquals(DateFormat.getDateTimeInstance().format(rightNow), DateFormat.getDateTimeInstance().format(savedFireMonsterLastPlayed));
    }

    @Test
    public void timer_executesDepleteLevelsMethod() {
        FireMonster testFireMonster = new FireMonster("Bubbles", 1);
        int firstPlayLevel = testFireMonster.getPlayLevel();
        testFireMonster.startTimer();
        try {
            Thread.sleep(6000);
        } catch (InterruptedException exception){}
        int secondPlayLevel = testFireMonster.getPlayLevel();
        assertTrue(firstPlayLevel > secondPlayLevel);
    }


}
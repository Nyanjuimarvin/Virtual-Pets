import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class WaterMonsterTest {

    @ExtendWith(DatabaseRule.class)
    public DatabaseRule databaseRule = new DatabaseRule();


    @Test
    public void WaterMonster_instantiatesCorrectly_true() {
        WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
        assertTrue(testWaterMonster instanceof WaterMonster);
    }

    @Test
    public void WaterMonster_instantiatesWithName_String() {
        WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
        assertEquals("Bubbles", testWaterMonster.getName());
    }

    @Test
    public void WaterMonster_instantiatesWithPersonId_int() {
        WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
        assertEquals(1, testWaterMonster.getPersonId());
    }

    @Test
    public void equals_returnsTrueIfNameAndPersonIdAreSame_true() {
        WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
        WaterMonster anotherWaterMonster = new WaterMonster("Bubbles", 1);
        assertTrue(testWaterMonster.equals(anotherWaterMonster));
    }
    @Test
    public void save_returnsTrueIfDescriptionsAretheSame() {
        WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
        testWaterMonster.save();
        assertTrue(WaterMonster.all().get(0).equals(testWaterMonster));
    }


    @Test
    public void save_assignsIdToWaterMonster() {
        WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
        testWaterMonster.save();
        WaterMonster savedWaterMonster = WaterMonster.all().get(0);
        assertEquals(savedWaterMonster.getId(), testWaterMonster.getId());
    }

    @Test
    public void all_returnsAllInstancesOfWaterMonster_true() {
        WaterMonster firstWaterMonster = new WaterMonster("Bubbles", 1);
        firstWaterMonster.save();
        WaterMonster secondWaterMonster = new WaterMonster("Spud", 1);
        secondWaterMonster.save();
        assertEquals(true, WaterMonster.all().get(0).equals(firstWaterMonster));
        assertEquals(true, WaterMonster.all().get(1).equals(secondWaterMonster));
    }

    @Test
    public void find_returnsWaterMonsterWithSameId_secondWaterMonster() {
        WaterMonster firstWaterMonster = new WaterMonster("Bubbles", 1);
        firstWaterMonster.save();
        WaterMonster secondWaterMonster = new WaterMonster("Spud", 3);
        secondWaterMonster.save();
        assertEquals(WaterMonster.find(secondWaterMonster.getId()), secondWaterMonster);
    }

    @Test
    public void save_savesPersonIdIntoDB_true() {
        Person testPerson = new Person("Henry", "henry@henry.com");
        testPerson.save();
        Monster testWaterMonster = new WaterMonster("Bubbles", testPerson.getId());
        testWaterMonster.save();
        Monster savedWaterMonster = WaterMonster.find(testWaterMonster.getId());
        assertEquals(savedWaterMonster.getPersonId(), testPerson.getId());
    }

    //TESTS FOR CONSTANT INITIALIZATION
    @Test
    public void WaterMonster_instantiatesWithHalfFullPlayLevel(){
        WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
        assertEquals(testWaterMonster.getPlayLevel(), (WaterMonster.MAX_PLAY_LEVEL / 2));
    }

    @Test
    public void WaterMonster_instantiatesWithHalfFullSleepLevel(){
        WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
        assertEquals(testWaterMonster.getSleepLevel(), (WaterMonster.MAX_SLEEP_LEVEL / 2));
    }

    @Test
    public void WaterMonster_instantiatesWithHalfFullFoodLevel(){
        WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
        assertEquals(testWaterMonster.getFoodLevel(), (WaterMonster.MAX_FOOD_LEVEL / 2));
    }

    @Test
    public void isAlive_confirmsWaterMonsterIsAliveIfAllLevelsAboveMinimum_true(){
        WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
        assertEquals(testWaterMonster.isAlive(), true);
    }

    //Reduce
    @Test
    public void depleteLevels_reducesAllLevels(){
        WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
        testWaterMonster.depleteLevels();
        assertEquals(testWaterMonster.getFoodLevel(), (WaterMonster.MAX_FOOD_LEVEL / 2) - 1);
        assertEquals(testWaterMonster.getSleepLevel(), (WaterMonster.MAX_SLEEP_LEVEL / 2) - 1);
        assertEquals(testWaterMonster.getPlayLevel(), (WaterMonster.MAX_PLAY_LEVEL / 2) - 1);
    }

    //Is WaterMonster alive
    @Test
    public void isAlive_recognizesWaterMonsterIsDeadWhenLevelsReachMinimum_false(){
        WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
        for(int i = WaterMonster.MIN_ALL_LEVELS; i <= WaterMonster.MAX_FOOD_LEVEL; i++){
            testWaterMonster.depleteLevels();
        }
        assertFalse(testWaterMonster.isAlive());
    }

    //Increase
    @Test
    public void play_increasesWaterMonsterPlayLevel(){
        WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
        testWaterMonster.play();
        assertTrue(testWaterMonster.getPlayLevel() > (WaterMonster.MAX_PLAY_LEVEL / 2));
    }

    @Test
    public void sleep_increasesWaterMonsterSleepLevel(){
        WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
        testWaterMonster.sleep();
        assertTrue(testWaterMonster.getSleepLevel() > (WaterMonster.MAX_SLEEP_LEVEL / 2));
    }


    @Test
    public void feed_increasesWaterMonsterFoodLevel(){
        WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
        testWaterMonster.feed();
        assertTrue(testWaterMonster.getFoodLevel() > (WaterMonster.MAX_FOOD_LEVEL / 2));
    }

    //Food
    @Test()
    public void feed_throwsExceptionIfFoodLevelIsAtMaxValue(){
        WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
        Throwable exception = assertThrows(UnsupportedOperationException.class,()->{
            for(int i = WaterMonster.MIN_ALL_LEVELS; i <= (WaterMonster.MAX_FOOD_LEVEL); i++){
                testWaterMonster.feed();
            }
        },"Monster is Full..Can't eat more");
        assertEquals("Monster is Full..Can't eat more",exception.getMessage());
    }

    @Test
    public void WaterMonster_foodLevelCannotGoBeyondMaxValue(){
        WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
        for(int i = WaterMonster.MIN_ALL_LEVELS; i <= WaterMonster.MAX_FOOD_LEVEL; i++){
            try {
                testWaterMonster.feed();
            } catch (UnsupportedOperationException exception){ }
        }
        assertTrue(testWaterMonster.getFoodLevel() <= WaterMonster.MAX_FOOD_LEVEL);
    }


    //Play

    @Test()
    public void play_throwsExceptionIfPlayLevelIsAtMaxValue(){
        WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
        Throwable exception = assertThrows(UnsupportedOperationException.class,()->{
            for(int i = WaterMonster.MIN_ALL_LEVELS; i <= (WaterMonster.MAX_PLAY_LEVEL); i++){
                testWaterMonster.play();
            }
        },"Monster is Tired..Can't play more");
        assertEquals("Monster is Tired..Can't play more",exception.getMessage());
    }

    @Test
    public void WaterMonster_playLevelCannotGoBeyondMaxValue(){
        WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
        for(int i = WaterMonster.MIN_ALL_LEVELS; i <= (WaterMonster.MAX_PLAY_LEVEL); i++){
            try {
                testWaterMonster.play();
            } catch (UnsupportedOperationException exception){ }
        }
        assertTrue(testWaterMonster.getPlayLevel() <= WaterMonster.MAX_PLAY_LEVEL);
    }


    //Sleep
    @Test()
    public void sleep_throwsExceptionIfSleepLevelIsAtMaxValue(){
        WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
        Throwable exception = assertThrows(UnsupportedOperationException.class,()->{
            for(int i = WaterMonster.MIN_ALL_LEVELS; i <= (WaterMonster.MAX_SLEEP_LEVEL); i++){
                testWaterMonster.sleep();
            }
        },"Monster is well rested..Can't sleep more");
        assertEquals("Monster is well rested..Can't sleep more",exception.getMessage());
    }

    @Test
    public void WaterMonster_sleepLevelCannotGoBeyondMaxValue(){
        WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
        for(int i = WaterMonster.MIN_ALL_LEVELS; i <= (WaterMonster.MAX_SLEEP_LEVEL); i++){
            try {
                testWaterMonster.sleep();
            } catch (UnsupportedOperationException exception){ }
        }
        assertTrue(testWaterMonster.getSleepLevel() <= WaterMonster.MAX_SLEEP_LEVEL);
    }

    @Test
    public void save_recordsTimeOfCreationInDatabase() {
        WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
        testWaterMonster.save();
        Timestamp savedWaterMonsterBirthday = WaterMonster.find(testWaterMonster.getId()).getBirthday();
        Timestamp rightNow = new Timestamp(new Date().getTime());
        DateTime saved = new DateTime(savedWaterMonsterBirthday);
        DateTime now = new DateTime(rightNow);
        System.out.println(saved);
        System.out.println(now);
        System.out.println(now.toDateTime());
        assertEquals(now.dayOfWeek(), saved.dayOfWeek());
    }

    @Test
    public void sleep_recordsTimeLastSleptInDatabase() {
        WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
        testWaterMonster.save();
        testWaterMonster.sleep();
        Timestamp savedWaterMonsterLastSlept = WaterMonster.find(testWaterMonster.getId()).getLastSlept();
        Timestamp rightNow = new Timestamp(new Date().getTime());
        assertEquals(DateFormat.getDateTimeInstance().format(rightNow), DateFormat.getDateTimeInstance().format(savedWaterMonsterLastSlept));
    }


    @Test
    public void feed_recordsTimeLastAteInDatabase() {
        WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
        testWaterMonster.save();
        testWaterMonster.feed();
        Timestamp savedWaterMonsterLastAte = WaterMonster.find(testWaterMonster.getId()).getLastAte();
        Timestamp rightNow = new Timestamp(new Date().getTime());
        assertEquals(DateFormat.getDateTimeInstance().format(rightNow), DateFormat.getDateTimeInstance().format(savedWaterMonsterLastAte));
    }

    @Test
    public void play_recordsTimeLastPlayedInDatabase() {
        WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
        testWaterMonster.save();
        testWaterMonster.play();
        Timestamp savedWaterMonsterLastPlayed = WaterMonster.find(testWaterMonster.getId()).getLastPlayed();
        Timestamp rightNow = new Timestamp(new Date().getTime());
        assertEquals(DateFormat.getDateTimeInstance().format(rightNow), DateFormat.getDateTimeInstance().format(savedWaterMonsterLastPlayed));
    }

    @Test
    public void timer_executesDepleteLevelsMethod() {
        WaterMonster testWaterMonster = new WaterMonster("Bubbles", 1);
        int firstPlayLevel = testWaterMonster.getPlayLevel();
        testWaterMonster.startTimer();
        try {
            Thread.sleep(6000);
        } catch (InterruptedException exception){}
        int secondPlayLevel = testWaterMonster.getPlayLevel();
        assertTrue(firstPlayLevel > secondPlayLevel);
    }

}
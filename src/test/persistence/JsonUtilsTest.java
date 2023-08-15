package persistence;

import model.CardSet;
import model.CardSetsMenu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonUtilsTest {
    private final String CSM_DATA = "./data/tests/csmutil.cpsc";
    private final String CARDSET_FOLDER = "./data/tests/";
    private final String SUFFIX = ".cpsc";
    private final String CARDSET_DATA = CARDSET_FOLDER + "csutil" + SUFFIX;
    private JsonUtils util;

    @BeforeEach
    public void setup() {
        this.util = new JsonUtils(CSM_DATA, CARDSET_FOLDER, SUFFIX);
    }

    @Test
    public void noFileSaveCsmTest(){
        File f = new File(CSM_DATA);
        f.delete();
        CardSetsMenu csm = new CardSetsMenu();
        try {
            util.saveCardSetMenu(csm);
        } catch (IOException e) {
            fail("No exception should be thrown");
        }
        try {
            JsonReader reader = new JsonReader(CSM_DATA);
            CardSetsMenu rCsm = reader.readCardSetMenu();
            // pass
            assertEquals(0 , rCsm.getCardSetLength());
        } catch (IOException e) {
            fail("No exception should be thrown");
        }
    }

    @Test
    public void ioExceptionSaveCsmTest(){
        this.util = new JsonUtils("./data/tests/csmutilinvw23(%$alidw wd-?.cpsc", CARDSET_FOLDER, SUFFIX);
        CardSetsMenu csm = new CardSetsMenu();
        try {
            util.saveCardSetMenu(csm);
            fail("Io exception expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void throwExceptionSaveCsmTest(){
        this.util = new JsonUtils("./data/tests/c:\\78fe9lk", CARDSET_FOLDER, SUFFIX);
        CardSetsMenu csm = new CardSetsMenu();
        try {
            util.saveCardSetMenu(csm);
            fail("IO exception expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void saveCsmTest(){
        try {
            JsonReader reader = new JsonReader(CSM_DATA);
            CardSetsMenu rCsm = reader.readCardSetMenu();
            // pass
            assertEquals(0 , rCsm.getCardSetLength());
        } catch (IOException e) {
            fail("No exception should be thrown");
        }
        CardSetsMenu csm = new CardSetsMenu();
        csm.addCardSet(new CardSet("test"));
        try {
            util.saveCardSetMenu(csm);
        } catch (IOException e) {
            fail("No exception should be thrown");
        }
        try {
            JsonReader reader = new JsonReader(CSM_DATA);
            CardSetsMenu rCsm = reader.readCardSetMenu();
            // pass
            assertEquals(1 , rCsm.getCardSetLength());
        } catch (IOException e) {
            fail("No exception should be thrown");
        }
        File f = new File(CSM_DATA);
        f.delete();
    }

    @Test
    public void noFileSaveCsTest() {
        File f = new File(CARDSET_DATA);
        f.delete();
        CardSet cs = new CardSet("csutil");
        try {
            util.saveCardSet(cs);
            JsonReader reader = new JsonReader(CARDSET_DATA);
            CardSet rCsm = reader.readCardSet();
            // pass
            assertEquals(0 , rCsm.getCardSetLength());
        } catch (IOException e) {
            fail("No exception should be thrown");
        }
    }

    @Test
    public void IOExceptionSaveCsTest() {
        this.util = new JsonUtils("./data/tests/csmutilinvw23(%$alidw wd-?.cpsc", CARDSET_FOLDER, SUFFIX);
        CardSet cs = new CardSet("csu?)09&*&5(*(0til");
        try {
            util.saveCardSet(cs);
            fail("IO exception expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void saveCsTest() {
        try {
            JsonReader reader = new JsonReader(CARDSET_DATA);
            CardSet rCsm = reader.readCardSet();
            // pass
            assertEquals(0 , rCsm.getCardSetLength());
        } catch (IOException e) {
            fail("No exception should be thrown");
        }
        CardSet csm = new CardSet("csutil");
        csm.incrementUseCount();
        try {
            util.saveCardSet(csm);
        } catch (IOException e) {
            fail("No exception should be thrown");
        }
        try {
            JsonReader reader = new JsonReader(CARDSET_DATA);
            CardSet rCsm = reader.readCardSet();
            // pass
            assertEquals(1 , rCsm.getUseCount());
        } catch (IOException e) {
            fail("No exception should be thrown");
        }
        File f = new File(CARDSET_DATA);
        f.delete();
    }

    @Test
    public void readEmptyCsmTest() {
        File f = new File(CSM_DATA);
        f.delete();
        CardSetsMenu nCsm;
        try {
            nCsm = util.readCardSetMenu();
            assertEquals(0, nCsm.getCardSetLength());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void readInvalidCsmTest() {
        this.util = new JsonUtils("./data/tests/csmutilinvw23(%$alidw wd-?.cpsc", CARDSET_FOLDER, SUFFIX);
        try {
            CardSetsMenu nCsm = util.readCardSetMenu();
            fail("RunTimeException expected!!");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void readCsmDirectoryNotFoundTest() {
        util = new JsonUtils("./data/tests/c:\\78fe9lk", CARDSET_FOLDER, SUFFIX);
        try {
            CardSetsMenu nCsm = util.readCardSetMenu();
            fail("exception expected");
        } catch (IOException e) {
            //pass
        }
        File f = new File("./data/tests/c:\\78fe9lk");
        f.delete();
    }


    @Test
    public void readCsmTest() {
        CardSetsMenu csm = new CardSetsMenu();
        csm.addCardSet(new CardSet("test"));
        try {
            util.saveCardSetMenu(csm);
            CardSetsMenu nCsm = util.readCardSetMenu();
            assertEquals(csm.getCardSet(0), nCsm.getCardSet(0));
        } catch (IOException e) {
            fail("No exception should be thrown");
        }
        File f = new File(CSM_DATA);
        f.delete();
    }

    @Test
    public void readEmptyCsTest() {
        CardSet csm = new CardSet("csutilrr");
        util.deleteCardSet(csm);
        try {
            util.saveCardSet(csm);
        } catch (IOException e) {
            fail();
        }
        try {
            CardSet nCsm = util.readCardSet("csutilrr");
            assertEquals("csutilrr", nCsm.getTitle());
        } catch(IOException e) {
            fail("No exception expected");
        }
        util.deleteCardSet(csm);
    }

    @Test
    public void readInvalidCsTest() {
        this.util = new JsonUtils("./data/tests/csmuvw23(%lidw wd-?.cpsc", CARDSET_FOLDER, SUFFIX);
        try {
            CardSet nCsm = util.readCardSet("dakwido&*^(u01??92hjk)");
            fail("RunTimeException expected!!");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void readEmptyCsTest2() {
        try {
            CardSet nCsm = util.readCardSet("doesnt exist fr");
            assertEquals(0, nCsm.getUseCount());
            util.deleteCardSet(nCsm);
        } catch (IOException e) {
            fail();
        }
    }
}

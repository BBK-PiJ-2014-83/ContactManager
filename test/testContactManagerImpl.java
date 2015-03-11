package test;

import main.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
public class testContactManagerImpl {
    ContactManagerImpl testContactManager;
    @Before
    public void before() {
        testContactManager = new ContactManagerImpl();
    }


}

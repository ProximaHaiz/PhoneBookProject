package service;

import com.proxsoftware.webapp.entity.AccountEntity;
import com.proxsoftware.webapp.repository.AccountRepository;
import com.proxsoftware.webapp.service.AccountServiceIml1;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@WebAppConfiguration
public class AccountServiceIml1Test extends AbstractTest {
    private Long id;
    public static String userName;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountServiceIml1 accountService;


    @Before
    public void setUp() throws Exception {
        AccountEntity register = accountService.register(new AccountEntity("testUser2", "makaroshka11", "testUser2",
                "testLastName2", "testMiddle2", "testuser@gmail.com"));
        id = register.getId();
    }

    @After
    public void tearDown() throws Exception {
        accountService.delete(id);
    }

    @Test
    public void testFindOneByName() {
        AccountEntity testUser2 = accountRepository.findOneByUserName("testUser2");
        Assert.assertNotNull("failure - expected not null", testUser2);
    }

    @Test
    public void testFindOneByEmail() {
        AccountEntity testUser2 = accountRepository.findOneByEmail("testuser@gmail.com");
        Assert.assertNotNull("failure - expected not null", testUser2);
    }


    @Test
    public void testRegister() throws Exception {
        AccountEntity register = accountService.register(new AccountEntity("testUser", "makaroshka11", "testUser",
                "testLastName", "testMiddle", "testUser.gmail.com"));
        AccountEntity foundUser = accountRepository.findOneByUserName("testUser");
        logger.info("Found user: " + foundUser);
        Assert.assertNotNull("failure - expected not null", foundUser);
        accountService.delete(foundUser.getId());
    }

    @Test
    public void testDelete() throws Exception {
        accountService.register(new AccountEntity("testUser", "makaroshka11", "testUser",
                "testLastName", "testMiddle", "testUser.gmail.com"));
        AccountEntity one = accountRepository.findOneByUserName("testUser");
        System.out.println(one);
        accountRepository.delete(one.getId());
        AccountEntity testUser2 = accountRepository.findOneByUserName("testUser");
        Assert.assertNull("failure - expected null", testUser2);

    }

    @Test
    public void testUpdateUser() throws Exception {
        AccountEntity testUser2 = accountRepository.findOneByUserName("testUser2");
        Assert.assertNotNull("failure - expected not null", testUser2);
        testUser2.setFirstName("UpdatedFirstName");
        testUser2.setLastName("UpdatedLastName");
        testUser2.setMiddleName("UpdatedMiddleName");
        accountService.updateUser(testUser2);
        AccountEntity updatedUser2 = accountRepository.findOneByUserName("testUser2");
        Assert.assertNotNull("failure - expected not null", updatedUser2);
        Assert.assertEquals("failure - expected equal", "UpdatedFirstName", updatedUser2.getFirstName());
    }


}
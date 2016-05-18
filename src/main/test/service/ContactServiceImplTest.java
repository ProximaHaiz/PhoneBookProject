package service;

import com.proxsoftware.webapp.entity.AccountEntity;
import com.proxsoftware.webapp.entity.ContactEntity;
import com.proxsoftware.webapp.repository.AccountRepository;
import com.proxsoftware.webapp.service.AccountServiceIml1;
import com.proxsoftware.webapp.service.IContactService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;

@Transactional
@WebAppConfiguration
public class ContactServiceImplTest extends AbstractTest {
    private long accountId;
    private long contact1_Id;
    private long contact2_Id;
    private AccountEntity account;

    @Autowired
    private IContactService contactService;
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountServiceIml1 accountService;

    @Autowired
    HttpSession session;

    @Before
    public void setUp() throws Exception {
        AccountEntity register = accountService.register(new AccountEntity("testUser", "makaroshka11", "testUser2",
                "testLastName2", "testMiddle2", "testuser@gmail.com"));
        AccountEntity testUser = accountRepository.findOneByUserName("testUser");
        account = register;
        accountId = register.getId();
        contactService.findAllByAccount(register);
        ContactEntity contact1 = new ContactEntity("Ivan", "Ivanov", "Ivanovich", "+38(063) 456-23-43", "(044) 456-43-22", "ivanov@gmail.com", "KievCity", register);
        ContactEntity contact2 = new ContactEntity("Petr", "Petrov", "Petrovich", "+38(063) 222-11-33", "(044) 456-44-11", "petrov@gmail.com", "KievCity", register);
        contact1_Id = contactService.save(contact1).getId();
        contact2_Id = contactService.save(contact2).getId();
    }

    @After
    public void tearDown() throws Exception {
        accountService.delete(accountId);
    }


    @Test
    public void testFindOne() throws Exception {
        ContactEntity first = contactService.findOne(contact1_Id);
        ContactEntity second = contactService.findOne(contact2_Id);
        Assert.assertNotNull("failure - expected not null", first);
        Assert.assertNotNull("failure - expected not null", second);
        Assert.assertEquals("failure - expected id attribute match", contact1_Id, first.getId());
        Assert.assertEquals("failure - expected id attribute match", contact2_Id, second.getId());
    }


    @Test
    public void testSave() throws Exception {
        ContactEntity savedContact = contactService.save(new ContactEntity("Alexandr", "TheGreat",
                "Makedonsky", "+38(067) 452-33-11", "(044) 235-22-11", "alexandr@gmail.com", "Alexandria"));
        ContactEntity one = contactService.findOne(savedContact.getId());
        Assert.assertNotNull("failure - expected not null", one);
        Assert.assertEquals("failure - expected id attribute match", savedContact.getId(), one.getId());
        contactService.delete(one.getId());
    }

    @Test
    public void testDelete() throws Exception {
        ContactEntity savedContact = contactService.save(new ContactEntity("Alexandr", "TheGreat",
                "Makedonsky", "+38(067) 452-33-11", "(044) 235-22-11", "alexandr@gmail.com", "Alexandria"));
        contactService.delete(savedContact.getId());
        List<ContactEntity> allByAccount = contactService.findAllByAccount(account);
        Assert.assertNotNull("failure - expected not null");
        Assert.assertTrue("failure - expected size not 2", allByAccount.size() == 2);
    }

    @Test
    public void testFindAllByAccount() throws Exception {
        List<ContactEntity> allByAccount = contactService.findAllByAccount(account);
        Assert.assertNotNull("failure - expected not null", allByAccount);
        Assert.assertTrue("failure - expected size not 2", allByAccount.size() == 2);
        contactService.delete(5L);
    }
}
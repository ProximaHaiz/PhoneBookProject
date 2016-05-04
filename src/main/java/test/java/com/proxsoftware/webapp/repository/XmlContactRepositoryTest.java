package com.proxsoftware.webapp.repository;

import com.proxsoftware.webapp.entity.AccountEntity;
import com.proxsoftware.webapp.entity.ContactEntity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Proxima on 24.04.2016.
 */
public class XmlContactRepositoryTest {
    private final static String PATH = "data.json";
    private static XmlRepository repoXml = new XmlRepository();

    Map<String, AccountEntity> acMap = new HashMap<>();




    private static ContactEntity createContact(int i, int key) {
        return new ContactEntity("contact" + i, "last" + i, "middle" + i, key,
                321312, "prox" + i + "@gmail.com", "address", i);
    }

    private static AccountEntity createAccount(String userName) {
        HashMap<Long, ContactEntity> contactMap = new HashMap<>();
        AccountEntity account = new AccountEntity(
                1L, userName, "546595qq", "user", "userLast", "userskiy");
        for (int i = 0; i < 5; i++) {
            ContactEntity contact = createContact(i, new Random().nextInt(10000000));
            contactMap.put(contact.getIdForXml(), contact);
        }
        account.setContactMap(contactMap);
        return account;
    }

    {
        /*AccountEntity account = createAccount("proxima");
        AccountEntity account1 = createAccount("admin");
        AccountEntity account2 = createAccount("visitor");*/
      /*  acMap.put(account.getUserName(), account);
        acMap.put(account1.getUserName(), account1);
        acMap.put(account2.getUserName(), account2);*/



    }

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void saveAccount() throws Exception {
        try {
            repoXml.addAccounts();
//            repoXml.addAccount(prox);
            System.out.println(repoXml.getAllAccounts().size());
        } catch (Exception e) {
            System.out.println("exeption");
            e.printStackTrace();
        }
    }

    @Test
    public void saveContact() throws Exception {
        AccountEntity account = createAccount("user666");
        AccountEntity account1 = createAccount("admin777");
        AccountEntity account2 = createAccount("visitor888");
        AccountEntity account3 = createAccount("visit2");
        AccountEntity prox = createAccount("prox");
        repoXml.addAccount(createAccount("proxima"));
        repoXml.addAccount(account);
        repoXml.addAccount(account1);
        repoXml.addAccount(account2);
        repoXml.addAccount(account3);
        repoXml.addAccount(prox);

        System.out.println(repoXml.getAllAccounts());

    }

    @Test
    public void readFromFile() throws Exception {
        Map<String, AccountEntity> allContacts = repoXml.getAllAccounts();
        System.out.println(allContacts);
        assert (allContacts != null);
    }


    @Test
    public void getAccount() throws Exception {
        assert (repoXml.getAccount("proxima") != null);

    }

}
package com.proxsoftware.webapp.repository;

import com.proxsoftware.webapp.entity.AccountEntity;
import com.proxsoftware.webapp.entity.ContactEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Proxima on 25.04.2016.
 */
@PropertySource("${classpath:app.properties}")
@Component
public class Test {
    @Autowired
    private ApplicationContext context;
    private static long idForXml = 0;

    public ApplicationContext getContext() {
        return context;
    }

    public static void main(String[] args) throws IOException {
        ApplicationContext context = new Test().getContext();
        System.out.println(context);
       /* XmlRepository repo = new XmlRepository();
        AccountEntity account = createAccount("user666");
        System.out.println(account);
        repo.getUtils().setFILE_URI("test1.json");
        repo.addAccount(account);

        Map<String, AccountEntity> allAccounts = repo.getAllAccounts();
        System.out.println(allAccounts);*/

       /* AccountEntity account1 = createAccount("admin777");
        AccountEntity account2 = createAccount("visitor888");
        AccountEntity account3 = createAccount("visitor882");
        String uuid = UUID.randomUUID().toString();
        AccountEntity prox = new AccountEntity(10L, "Vladimir", "Vladimirovich", "Karpov",
                "546595qq", "user", uuid, "proxima@gmail.com", "proxima");
        prox.addContactToMap(new ContactEntity("user", "user", "user", 4232312, 273122, "ss@gmail.com",
                "kiev", 234L));


        repo.addAccount(account);
        repo.addAccount(account1);
        repo.addAccount(account2);
        repo.addAccount(account3);
        repo.addAccount(prox);

        Map<String, AccountEntity> allContacts = repo.getAllAccounts();
        System.out.println(allContacts);
        AccountEntity prox1 = repo.getAccount("proxima");

        System.out.println(repo.getAccount("proxima"));
        ContactEntity contactById = repo.findContactById(prox, 234L);
        long idForXml = contactById.getIdForXml();
        System.out.println("contact id = " + contactById);

        repo.deleteContact(prox1, idForXml);

        System.out.println(repo.getAllAccounts());

        System.out.println("getAllContactsInList");

        System.out.println(repo.getAllAccounts(account1));


        AccountEntity accountByEmail = repo.findAccountByEmail("proxima@gmail.com");
        System.out.println("found next account: " + accountByEmail);*/

    }

    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        else return "";
    }

    private static AccountEntity createAccount(String userName) {
        HashMap<Long, ContactEntity> contactMap = new HashMap<>();
        Random r = new Random();

        AccountEntity account = new AccountEntity(r.nextLong(), "firstName", "middleName",
                "lastName", "546595qq", "user", UUID.randomUUID().toString(), "user@gmail.com", userName);
        for (int i = 0; i < 5; i++) {
            long id = r.nextLong();
            idForXml = id;
//            account.setOneXmlId();
            ContactEntity contact = createContact(i, id);
            contactMap.put(contact.getIdForXml(), contact);
        }
        account.setContactMap(contactMap);
        return account;
    }

    private static ContactEntity createContact(int i, long id) {
        return new ContactEntity("contact" + i, "last" + i, "middle" + i, i,
                321312, "prox" + i + "@gmail.com", "address", id);
    }
}

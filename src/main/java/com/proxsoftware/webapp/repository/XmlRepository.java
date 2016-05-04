package com.proxsoftware.webapp.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.proxsoftware.webapp.entity.AccountEntity;
import com.proxsoftware.webapp.entity.ContactEntity;
import com.proxsoftware.webapp.exceptions.FileEmptyException;
import com.proxsoftware.webapp.util.FileStoreEnum;
import com.proxsoftware.webapp.util.XmlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Proxima on 22.04.2016.
 */
@Repository
public class XmlRepository {
    public XmlRepository() {

    }

    private boolean fileIsEmpty;
    @Autowired
    private XmlUtils utils = new XmlUtils();

    public XmlUtils getUtils() {
        return utils;
    }

    private String accountUserName;


  /*  public XmlRepository(String FILE_URI) {
        utils = new XmlUtils(FILE_URI);
    }*/

    private Logger log = LoggerFactory.getLogger(XmlRepository.class);

    private Map<String, AccountEntity> accountContainer = new HashMap<>();

    AccountEntity addAccount(AccountEntity account) {
        getAllAccounts();
        accountContainer.put(account.getUserName(), account);
        System.out.println(accountContainer.size());
        doWriteToFile();
        return account;
    }

    public void addAccounts() {
        doWriteToFile();
    }

    public ContactEntity addContact(AccountEntity account, ContactEntity contact) {
        if (!isFileEmpty()) {
            accountContainer.clear();
            getAllAccounts();
            System.out.println("Contact from AddContact:" + contact);
            if (contact.getIdForXml() == 0) {
                contact.setIdForXml(contactIdGenerator());
            }
            accountContainer.get(account.getUserName()).getContactMap().put(contact.getIdForXml(), contact);
            addAccounts();
        } else {
            throw new FileEmptyException("File " + utils.getFILE_URI() + " is empty");
        }
        return contact;
    }

    private long contactIdGenerator() {
        return new Random().nextLong();
    }

    public Map<String, AccountEntity> getAllAccounts() {
        return doReadFromFile();
    }

    public AccountEntity getAccount(String userName) {
        try {
            if (!isFileEmpty()) {
                Map<String, AccountEntity> accountMap = getAllAccounts();
                return accountMap.get(userName);
            }
        } catch (FileEmptyException e) {
            e.getMessage();
        }
        return null;
    }

    public AccountEntity findAccountByEmail(String email) {
        if (!isFileEmpty()) {
            Map<String, AccountEntity> accounts = getAllAccounts();
            return accounts.values().stream().filter(account -> account.getEmail().equals(email)).findFirst().get();
        } else {
            return null;
        }
    }

    public ContactEntity getContact(AccountEntity account, long id) {
        ContactEntity contact = account.getContactMap().get(id);
        return contact != null ? contact : null;
    }

    public void deleteContact(AccountEntity account, long contactKey) {
        account.getContactMap().remove(contactKey);
        addAccount(account);
    }

    public List<ContactEntity> getAllAccounts(AccountEntity account) {
        Map<Long, ContactEntity> contactMap = account.getContactMap();
        return contactMap.values().stream().collect(Collectors.toList());
    }

    public ContactEntity findContactById(AccountEntity account, long id) {
        return account.getContactMap().values().stream().filter(entity -> entity.getIdForXml() == id).findFirst().get();
    }

    public AccountEntity findAccountByUserNameOrEmail(String userName, String email) {
        Map<String, AccountEntity> accounts = getAllAccounts();
        Collection<AccountEntity> values = accounts.values();
        AccountEntity accountEntity = values.stream().filter(account -> account.getEmail().equals(email) || account.getUserName().equals(userName)).
                findFirst().get();
        return (AccountEntity) accountEntity;
    }

    public AccountEntity findOneByToken(String token) {
        Map<String, AccountEntity> accounts = getAllAccounts();
        Collection<AccountEntity> values = accounts.values();
        return values.stream().filter(account -> account.getRole().equals(token)).findFirst().get();
    }

    public void deleteAccount(Long id) {
        Map<String, AccountEntity> accounts = getAllAccounts();
        accounts.remove(accounts.values().stream().filter(account -> account.getId() == id).findFirst().get().getUserName());
    }

    public void deleteAccount(AccountEntity account) {
        Map<String, AccountEntity> accounts = getAllAccounts();
        accounts.remove(account.getUserName());
    }

    public void updateAccount(String userName, String firstName, String lastName, String middleName) {
        Map<String, AccountEntity> accounts = getAllAccounts();
        AccountEntity account = accounts.remove(getAccountUserName());
        account.setFirstName(firstName);
        account.setUserName(userName);
        account.setLastName(lastName);
        account.setMiddleName(middleName);
        accounts.put(account.getUserName(), account);

        accountContainer.clear();
        accountContainer.putAll(accounts);
        doWriteToFile();
        accountContainer.clear();
    }

    public void updateContact(ContactEntity contact) {

    }

    private Map<String, AccountEntity> doReadFromFile() {
        FileStoreEnum storage = utils.getStore();
        try {
            fileIsEmpty = isFileEmpty();
            if (!fileIsEmpty) {
                switch (storage) {
                    case XML:
                        accountContainer.putAll((Map<String, AccountEntity>) utils.getxStream().fromXML(
                                new FileInputStream(utils.getFile())));
                        break;
                    case TXT:
                        accountContainer.putAll((Map<String, AccountEntity>) utils.getxStream().fromXML(new FileInputStream(utils.getFile())));
                        break;
                    case JSON:
                        accountContainer.putAll(utils.getMapper().readValue(utils.getFile(), new TypeReference<HashMap<String, AccountEntity>>() {
                        }));
                        break;
                }
            } /*else {
                return accountContainer;
            }*/
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return accountContainer;
    }

    private boolean doWriteToFile() {
        try {
            FileStoreEnum storage = utils.getStore();
            switch (storage) {
                case XML:
                    utils.getxStream().toXML(accountContainer, new FileWriter(utils.getFile()));
                    break;
                case TXT:
                    utils.getxStream().toXML(accountContainer, new FileWriter(utils.getFile()));
                    break;
                case JSON:
                    utils.getMapper().writeValue(utils.getFile(), accountContainer);
                    break;
            }
            accountContainer.clear();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isFileEmpty() {
//        File file2 = new File(utils.getFILE_URI());
//        long length = file2.length();
//        System.out.println("file length is " + length);
        Path path = Paths.get(utils.getFILE_URI());
        if (!Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (FileInputStream file = new FileInputStream(utils.getFILE_URI())) {
            int available = file.available();
            System.out.println("avalaible" + available);
            return available == 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    //<editor-fold desc="get\set">
    private ObjectOutputStream getObjectOutputStream() throws IOException {
        return utils.getxStream().createObjectOutputStream(
                new FileOutputStream(utils.getFILE_URI()));
    }

    private ObjectInputStream getObjectIntputStream() throws IOException {
        return utils.getxStream().createObjectInputStream(
                new FileInputStream(utils.getFILE_URI()));
    }

    public String getAccountUserName() {
        return accountUserName;
    }

    public void setAccountUserName(String accountUserName) {
        this.accountUserName = accountUserName;
    }
    //</editor-fold>

}

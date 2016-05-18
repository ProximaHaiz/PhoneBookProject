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

import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


@Repository
public class FileRepository {

    private boolean fileIsEmpty;
    @Autowired
    private XmlUtils utils = new XmlUtils();

    public XmlUtils getUtils() {
        return utils;
    }

    @Autowired
    HttpSession session;

    private Logger log = LoggerFactory.getLogger(FileRepository.class);

    private Map<String, AccountEntity> accountContainer = new HashMap<>();

    AccountEntity addAccount(AccountEntity account) {
        Map<String, AccountEntity> allAccounts = getAllAccounts();
        allAccounts.put(account.getUserName(), account);
//        accountContainer.putAll(allAccounts);
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
            if (contact.getId() == 0) {
                contact.setId(contactIdGenerator());
            }
            accountContainer.get(account.getUserName()).getContactMap().put(contact.getId(), contact);
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

    public void deleteContact(AccountEntity account, long contactKey) {
        account.getContactMap().remove(contactKey);
        addAccount(account);
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

    public List<ContactEntity> findAllContacts(AccountEntity account) {
        account = getAccount(account.getUserName());
        Map<Long, ContactEntity> contactMap = account.getContactMap();
        Collection<ContactEntity> values = contactMap.values();
        if (!values.isEmpty()) {
            return values.stream().collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    public void deleteAccount(Long id) {
        Map<String, AccountEntity> accounts = getAllAccounts();
        AccountEntity removed = accounts.remove(accounts.values().stream().filter(account -> account.getId() == id).findFirst().get().getUserName());
        accountContainer.putAll(accounts);
        doWriteToFile();
    }

    public void updateAccount(String userName, String firstName, String lastName, String middleName) {
        Map<String, AccountEntity> accounts = getAllAccounts();
        AccountEntity account = accounts.remove(/*SecurityContextHolder.getContext().getAuthentication().getName()*/userName);
        account.setFirstName(firstName);
        account.setUserName(userName);
        account.setLastName(lastName);
        account.setMiddleName(middleName);
        accounts.put(account.getUserName(), account);

        accountContainer.clear();
        accountContainer.put(account.getUserName(), account);
        doWriteToFile();
        accountContainer.clear();
    }

    /**
     * This method used only for testing, because can't invoke SecurityContextHolder.getContext().getAuthentication().getName()
     * when testing
     *
     * @param oldUSerName - name of current logged user
     */
    public void updateAccount(String userName, String firstName, String lastName, String middleName, String oldUSerName) {
        Map<String, AccountEntity> accounts = getAllAccounts();
        AccountEntity account = accounts.remove("testUser2");
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
            }
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
}

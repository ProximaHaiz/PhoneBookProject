package com.proxsoftware.webapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name = "account")
@XStreamAlias("Account")
@JsonIgnoreProperties({"contacts"})
public class AccountEntity extends AbstractEntity {

    @XStreamAlias(value = "contacts_id")
    private List<Integer> idForContact = new ArrayList<>();

    public void setOneXmlId(Integer id) {
        idForContact.add(id);
    }

    @Transient
    public List<Integer> getIdForContact() {
        return idForContact;
    }

    public void setIdForContact(List<Integer> idForContact) {
        this.idForContact = idForContact;
    }

    private String password;
    private String role = "ROLE_USER";
    private String token;
    private String email;
    private String userName;


    @Basic
    @Column(name = "user_name", length = 45)
//    @Size(min = 3)

    @NotNull(message = "blabla")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        this.userName = name;
    }

    private boolean admin;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public AccountEntity() {
    }

    @XStreamOmitField()//do not write to XML\JSON
    private Set<ContactEntity> contacts = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public Set<ContactEntity> getContacts() {
        return contacts;
    }

    public void setContacts(Set<ContactEntity> contacts) {
        this.contacts = contacts;
    }


    @XStreamAlias(value = "contactsMapppp")
//    @XStreamConverter(value = MapConverter.class)
    private Map<Long, ContactEntity> contactMap = new HashMap();

    @Transient
    public Map<Long, ContactEntity> getContactMap() {
        return contactMap;
    }

    public void setContactMap(HashMap<Long, ContactEntity> contactMap) {
        this.contactMap = contactMap;
    }

    public void addContactToMap(ContactEntity contact) {
        contactMap.put(contact.getIdForXml(), contact);
    }

    public AccountEntity(long id, String firstName, String middleName,
                         String lastName, String password, String role,
                         String token, String email, String userName) {
        super(firstName, middleName, lastName);
        this.id = id;
        this.password = password;
        this.role = role;
        this.token = token;
        this.email = email;
        this.userName = userName;

    }

    public AccountEntity(Long id, String userName, String password,
                         String firstName, String lastName, String middleName) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
    }

    public AccountEntity(String userName, String password, String firstName, String lastName, String middleName) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
    }


    @Basic
    @Column(name = "password", length = 45)
//    @Size(min = 5)
    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean isAdmin() {
        return this.role.equals("ROLE_ADMIN");
    }

    public void setAdmin(boolean admin) {

    }

    public void addContact(ContactEntity contact) {
        contacts.add(contact);
    }

    public void addContact(List<ContactEntity> contactList) {
        contacts.addAll(contactList);
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountEntity)) return false;
        if (!super.equals(o)) return false;
        AccountEntity that = (AccountEntity) o;
        return Objects.equals(password, that.password) &&
                Objects.equals(email, that.email) &&
                Objects.equals(userName, that.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), password, email, userName);
    }

    @Override
    public String toString() {
        return "AccountEntity{" +
                "idForContact=" + idForContact +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", token='" + token + '\'' +
                ", email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", contactMap=" + contactMap +
                ", admin=" + admin +
                '}';
    }
}

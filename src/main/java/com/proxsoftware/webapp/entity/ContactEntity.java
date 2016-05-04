package com.proxsoftware.webapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "contacts", indexes = {@Index(name = "fk_contact_idx", columnList =
        "id")})
@XStreamAlias("contact")
@JsonIgnoreProperties({"user"})
public class ContactEntity extends AbstractEntity {


    private int mobile_number;
    private int home_number;
    private String email;
    private String address;
    private AccountEntity user;


    @XStreamAlias(value = "account_id")
    @Transient
    private long idForXml;

    public long getIdForXml() {
        return idForXml;
    }

    public void setIdForXml(long idForXml) {
        this.idForXml = idForXml;
    }

    @ManyToOne()
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_contacts"))
    public AccountEntity getUser() {
        return user;
    }

    public void setUser(AccountEntity user) {
        this.user = user;
    }
    /*private AddressEntity address;*/

    public ContactEntity(String firstName, String lastName
            , String middle_name, int mobile_number, int home_number
            , String email, String address, long idForXml) {
        super(firstName, middle_name, lastName);
        this.mobile_number = mobile_number;
        this.home_number = home_number;
        this.email = email;
        this.address = address;
        this.idForXml = idForXml;
    }

    public ContactEntity(String firstName,
                         String lastName,
                         String middle_name,
                         int mobile_number,
                         int home_number,
                         String email,
                         String address,
                         AccountEntity user) {
        super(firstName, middle_name, lastName);
        this.mobile_number = mobile_number;
        this.home_number = home_number;
        this.email = email;
        this.address = address;
        this.user = user;
    }

    public ContactEntity() {
    }


    @Basic
    @Column(name = "mobile_number")
    public int getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(int mobile_number) {
        this.mobile_number = mobile_number;
    }

    @Basic
    @Column(name = "home_number")
    public int getHome_number() {
        return home_number;
    }

    public void setHome_number(int home_number) {
        this.home_number = home_number;
    }


    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContactEntity)) return false;
        if (!super.equals(o)) return false;
        ContactEntity that = (ContactEntity) o;
        return mobile_number == that.mobile_number &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), mobile_number, email);
    }

    @Override
    public String toString() {
        return "\n      contact[" +
                "id=" + id +
                "XmlId='"+idForXml+'\''+
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", midle_name='" + middleName + '\'' +
                ", mobile_number=" + mobile_number +
                ", home_number=" + home_number +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                "]," /*{" + user.getId() + "," + user.getName() + ", " + user.getFirstName() + "," +
                user.getMiddleName() + ", " + user.getLastName()*/;
    }

    public String myToString() {
        return "\n      contact[" +
                "id=" + idForXml +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", midle_name='" + middleName + '\'' +
                ", mobile_number=" + mobile_number +
                ", home_number=" + home_number +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ']';
    }


}

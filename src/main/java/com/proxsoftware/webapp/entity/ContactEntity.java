package com.proxsoftware.webapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.proxsoftware.webapp.annotation.phone.MobileNumber;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity(name = "contactEntity")
@Table(name = "contacts", indexes = {@Index(name = "fk_contact_idx", columnList =
        "id")})
@XStreamAlias("contact")
@JsonIgnoreProperties({"user"})
public class ContactEntity extends AbstractEntity {
    private String home_number;
    private String email;
    private String address;
    private AccountEntity user;
    private String mobile_number;

    @Basic
    @Column(name = "mobile_number")
    @MobileNumber(message = "support only ukrainian operators")
    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = addPrefix(mobile_number);
    }

    private String addPrefix(String number) {
        return number.contains("+38") ? number : "+38" + number;
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
            , String middle_name, String mobile_number, String home_number
            , String email, String address) {
        super(firstName, middle_name, lastName);
        this.mobile_number = mobile_number;
        this.home_number = home_number;
        this.email = email;
        this.address = address;
    }

    public ContactEntity(String firstName,
                         String lastName,
                         String middle_name,
                         String mobile_number,
                         String home_number,
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
    @Column(name = "home_number")
    @Size(min = 15,message = "incorrect number")
    @NotNull(message = "can not be empty")
    public String getHome_number() {
        return home_number;
    }

    public void setHome_number(String home_number) {
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
    @Size(min = 4,message = "length must be more than 4 characters")
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
        return Objects.equals(getMobile_number(), that.mobile_number) &&
                Objects.equals(getEmail(), that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getMobile_number(), getEmail());
    }

    @Override
    public String toString() {
        return "\n      contact[" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", midle_name='" + middleName + '\'' +
                ", mobile_number=" + mobile_number +
                ", home_number=" + home_number +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phoneString= " + mobile_number +
                "],";
    }
}

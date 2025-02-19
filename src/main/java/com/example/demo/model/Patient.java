package com.example.demo.model;

import java.util.Objects;

public class Patient {
    private String email;
    private String password;
    private Long idCardNo;
    private String firstName;
    private String lastName;
    private Long phoneNumber;
    private String birthday;

    public Patient(String email, String password, Long idCardNo, String firstName, String lastName, Long phoneNumber, String birthday) {
        this.email = email;
        this.password = password;
        this.idCardNo = idCardNo;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(Long idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Patient patient)) return false;
        return Objects.equals(email, patient.email) && Objects.equals(password, patient.password) && Objects.equals(idCardNo, patient.idCardNo) && Objects.equals(firstName, patient.firstName) && Objects.equals(lastName, patient.lastName) && Objects.equals(phoneNumber, patient.phoneNumber) && Objects.equals(birthday, patient.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password, idCardNo, firstName, lastName, phoneNumber, birthday);
    }

    @Override
    public String toString() {
        return "Patient{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", idCardNo=" + idCardNo +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", birthday='" + birthday + '\'' +
                '}';
    }
}

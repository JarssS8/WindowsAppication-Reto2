/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.beans;

import java.io.Serializable;
import java.util.Date;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableSet;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class is an entity.
 *
 * @author aimar
 */
@XmlRootElement
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public User () {
        this.id = new SimpleLongProperty();
        this.login = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.fullName = new SimpleStringProperty();
        this.status = new SimpleObjectProperty<Status>();
        this.privilege = new SimpleObjectProperty<Privilege>();
        this.password = new SimpleStringProperty();
        this.lastAccess = new SimpleObjectProperty<Date>();
        this.lastPasswordChange = new SimpleObjectProperty<Date>();
    }
    
    /**
     * The Id for the user.
     */
    private SimpleLongProperty id;
    /**
     * The login value for the user.
     */
    private SimpleStringProperty login;
    /**
     * The email value for the user.
     */
    private SimpleStringProperty email;
    /**
     * The full name for the user.
     */
    private SimpleStringProperty fullName;
    /**
     * The status for the users account.
     */
    private SimpleObjectProperty<Status> status;
    /**
     * The privilege for the user.
     */
    private SimpleObjectProperty<Privilege> privilege;
    /**
     * The password value for the user.
     */
    private SimpleStringProperty password;
    /**
     * The date when the user last acceded to the applicacion.
     */
    private SimpleObjectProperty<Date> lastAccess;
    /**
     * The date when the user last changed password.
     */
    private SimpleObjectProperty<Date> lastPasswordChange;
    /**
     * A collection with all the ratings given by the user.
     */
    private SimpleSetProperty<Rating> ratings;
    /**
     * A collection with all the documents uploaded by the user.
     */
    private SimpleSetProperty<Document> documents;
    /**
     * A collection with all the groups for the user.
     */
    private SimpleSetProperty<Group> groups;

    public Long getId() {
        return this.id.get();
    }

    public void setId(Long id) {
        this.id.set(id);
    }

    public String getLogin() {
        return this.login.get();
    }

    public void setLogin(String login) {
        this.login.set(login);
    }

    public String getEmail() {
        return this.email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getFullName() {
        return this.fullName.get();
    }

    public void setFullName(String fullName) {
        this.fullName.set(fullName);
    }

    public Status getStatus() {
        return this.status.get();
    }

    public void setStatus(Status status) {
        this.status.set(status);
    }

    public Privilege getPrivilege() {
        return this.privilege.get();
    }

    public void setPrivilege(Privilege privilege) {
        this.privilege.set(privilege);
    }

    public String getPassword() {
        return this.password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public Date getLastAccess() {
        return this.lastAccess.get();
    }

    public void setLastAccess(Date lastAccess) {
        this.lastAccess.set(lastAccess);
    }

    public Date getLastPasswordChange() {
        return this.lastPasswordChange.get();
    }

    public void setLastPasswordChange(Date lastPasswordChange) {
        this.lastPasswordChange.set(lastPasswordChange);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "serverapplication.entities.User[ id=" + id + " ]";
    }

}
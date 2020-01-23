/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.beans;

import java.io.Serializable;
import java.util.Date;
import javafx.beans.property.SimpleObjectProperty;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Extends of class User for users that are going to control the platform
 *
 * @author Adrian
 */
@XmlRootElement
public class Admin extends User implements Serializable {
    
        public Admin() {
        super();
        this.adminDate = new SimpleObjectProperty<Date>();
    }
    
    public Admin(User user) {
        super();
        this.setId(user.getId());
        this.setLogin(user.getLogin());
        this.setEmail(user.getEmail());
        this.setFullName(user.getFullName());
        this.setStatus(user.getStatus());
        this.setPrivilege(user.getPrivilege());
        this.setLastAccess(user.getLastAccess());
        this.setLastPasswordChange(user.getLastPasswordChange());
        this.setPassword(user.getPassword());
        this.adminDate = new SimpleObjectProperty<Date>();
    }

    private static final long serialVersionUID = 1L;
    /**
     * A timestamp with the date of one user become admin
     */
    private SimpleObjectProperty<Date> adminDate;

    public Date getAdminDate() {
        return adminDate.get();
    }

    public void setAdminDate(Date adminDate) {
        this.adminDate.set(adminDate);
    }

    /**
     * Return an int calculated from id for the User
     *
     * @return an int representating the instance of this entity
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    /**
     * Compares two instances of Users
     *
     * @param object the other User instance to compare to
     * @return true if instances are equal
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    /**
     * Obtains a String representation including id value and classes full Name
     *
     * @return a String of an User id
     */
    @Override
    public String toString() {
        return "serverapplication.entities.User[ id=" + getId() + " ]";
    }
}

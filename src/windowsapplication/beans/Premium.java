/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.beans;

import java.io.Serializable;
import java.util.Date;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Extends of class User for users that access paying to our platform
 *
 * @author Adrian
 */
@XmlRootElement
public class Premium extends User implements Serializable {
    private static final long serialVersionUID = 1L;

    public Premium() {
        super();
        this.autorenovation = new SimpleBooleanProperty();
        this.beginSub = new SimpleObjectProperty<Date>();
        this.cardNumber = new SimpleLongProperty();
        this.cvc = new SimpleIntegerProperty();
        this.endSub = new SimpleObjectProperty<Date>();
        this.expirationMonth = new SimpleIntegerProperty();
        this.expirationYear = new SimpleIntegerProperty();
    }

    public Premium(User user) {
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
        this.setDocuments(user.getDocuments());
        this.setRatings(user.getRatings());
        this.setGroups(user.getGroups());
        this.autorenovation = new SimpleBooleanProperty();
        this.beginSub = new SimpleObjectProperty<Date>();
        this.cardNumber = new SimpleLongProperty();
        this.cvc = new SimpleIntegerProperty();
        this.endSub = new SimpleObjectProperty<Date>();
        this.expirationMonth = new SimpleIntegerProperty();
        this.expirationYear = new SimpleIntegerProperty();
    }

    /**
     * Boolean true if the autorenovation is active
     */
    private SimpleBooleanProperty autorenovation;
    /**
     * Timestamp with the date when the User starts being premium
     */

    private SimpleObjectProperty<Date> beginSub;
    /**
     * A long with the number of the user's credit card
     */
    private SimpleLongProperty cardNumber;
    /**
     * A int with the CVC of the user's credit card
     */
    private SimpleIntegerProperty cvc;
    /**
     * Timestamp with the date when the User should finish his premium period
     */

    private SimpleObjectProperty<Date> endSub;
    /**
     * A int with the month expiration of the user's credit card
     */
    private SimpleIntegerProperty expirationMonth;
    /**
     * A int with the year expiration of the user's credit card
     */
    private SimpleIntegerProperty expirationYear;

    public boolean isAutorenovation() {
        return autorenovation.get();
    }

    public void setAutorenovation(boolean autorenovation) {
        this.autorenovation.set(autorenovation);
    }

    public Date getBeginSub() {
        return beginSub.get();
    }

    public void setBeginSub(Date beginSub) {
        this.beginSub.set(beginSub);
    }

    public Long getCardNumber() {
        return cardNumber.get();
    }

    public void setCardNumber(Long cardNumber) {
        this.cardNumber.set(cardNumber);
    }

    public int getCvc() {
        return cvc.get();
    }

    public void setCvc(int cvc) {
        this.cvc.set(cvc);
    }

    public Date getEndSub() {
        return endSub.get();
    }

    public void setEndSub(Date endSub) {
        this.endSub.set(endSub);
    }

    public int getExpirationMonth() {
        return expirationMonth.get();
    }

    public void setExpirationMonth(int expirationMonth) {
        this.expirationMonth.set(expirationMonth);
    }

    public int getExpirationYear() {
        return expirationYear.get();
    }

    public void setExpirationYear(int expirationYear) {
        this.expirationYear.set(expirationYear);
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
        return "windowsapplication.controller.User[ id=" + getId() + " ]";
    }
}

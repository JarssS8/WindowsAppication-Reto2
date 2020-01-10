/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.beans;

import java.io.Serializable;
import java.util.Date;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entity class for rating. 
 * @author Gaizka Andrés
 */

@XmlRootElement
public class Rating implements Serializable{
    private static final long serialVersionUID=1L;
    /**
     * Id to indentificate the rating
     */
    private SimpleObjectProperty<RatingId> id;
    /**
     * The rating given to the document
     */
  
    private SimpleIntegerProperty rating;
    /**
     * The rating given to the document
     */
    private SimpleStringProperty review;
    /**
     * The date the review has been done
     */
    private SimpleObjectProperty<Date> ratingDate;
    /**
     * The document were the rating has been done
     */
   
    private SimpleObjectProperty<Document> document;
    /**
     * The user who rates the document
     */
    private SimpleObjectProperty<User> user;

    public RatingId getId() {
        return id.get();
    }

    public void setId(RatingId id) {
        this.id.set(id);
    }

    public int getRating() {
        return rating.get();
    }

    public void setRating(int rating) {
        this.rating.set(rating);
    }

    public String getReview() {
        return review.get();
    }

    public void setReview(String review) {
        this.review.set(review);
    }

    public Date getRatingDate() {
        return ratingDate.get();
    }

    public void setRatingDate(Date ratingDate) {
        this.ratingDate.set(ratingDate);
    }

    @XmlTransient
    public Document getDocument() {
        return document.get();
    }

    public void setDocument(Document document) {
        this.document.set(document);
    }
    @XmlTransient
    public User getUser() {
        return user.get();
    }

    public void setUser(User user) {
        this.user.set(user);
    }
    
    /**
     * Return an int calculated from id for the User
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
     * @param object the other User instance to compare to
     * @return true if instances are equal
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rating)) {
            return false;
        }
        Rating other = (Rating) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    /**
     * Obtains a String representation including id value and classes full Name
     * @return a String of an User id
     */
    @Override
    public String toString() {
        return "serverapplication.entities.Rating[ id=" + getId() + " ]";
    }

    
}

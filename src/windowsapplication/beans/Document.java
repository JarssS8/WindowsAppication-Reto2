/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableSet;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entity class for Document.
 * @author Gaizka Andrés
 */
@XmlRootElement
public class Document implements Serializable{
    private static final long serialVersionUID=1L;
    /**
     * Id to identificate the document
     */
    private SimpleLongProperty id;
    /**
     * The name of the document
     */
    private SimpleStringProperty name;
    /**
     * The date when the document has been upload
     */
    private SimpleObjectProperty<Date> uploadDate;
    /**
     * The total rating of the document
     */
    private SimpleIntegerProperty totalRating;
    /**
     * The total of reviews the document has
     */
    private SimpleIntegerProperty ratingCount;
    /**
     * The file itself
     */
    private SimpleObjectProperty<byte[]> file;
    /**
     * The collection of rating the document has been given
     */
    private SimpleSetProperty<Rating> ratings;
    /**
     * The author of the document
     */

    private SimpleObjectProperty<User> user;
    /**
     * The category of the document
     */
    
    private SimpleObjectProperty<Category> category;
    /**
     * The author group of the document
     */
    private SimpleObjectProperty<Group> group;
    
    public Document(){
    }
    
    public Document(Long id,String name, String author, Date uploadDate, int totalRating, int ratingCount){
        this.id.set(id);
        this.name.set(name);
        this.user.set(new User());
        this.user.get().setLogin(author);
        this.uploadDate.set(uploadDate);
        this.totalRating.set(totalRating);
        this.ratingCount.set(ratingCount);
        
    }
    
    public Long getId() {
        return this.id.get();
    }

    public void setId(Long id) {
        this.id.set(id);
    }

    public String getName() {
        return this.name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public Date getUploadDate() {
        return this.uploadDate.get();
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate.set(uploadDate);
    }

    public int getTotalRating() {
         return this.totalRating.get();
    }

    public void setTotalRating(int totalRating) {
        this.totalRating.set(totalRating);
    }

    public int getRatingCount() {
        return this.ratingCount.get();
    }

    public void setRatingCount(int ratingAccount) {
        this.ratingCount.set(ratingAccount);
    }

    public byte[] getFile() {
        return file.get();
    }

    public void setFile(byte[] file) {
        this.file.set(file);
    }

    public ObservableSet<Rating> getRatings() {
        return ratings.get();
    }

    public void setRatings(ObservableSet<Rating> ratings) {
        this.ratings.set(ratings);
    }

    public void setUser(User user) {
        this.user.set(user);
    }
    
    @XmlTransient
       public User getUser() {
        return user.get();
    }
    
    public Category getCategory() {
        return category.get();
    }

    public void setCategory(Category category) {
        this.category.set(category);
    }
 
 
    public Group getGroup() {
        return group.get();
    }

    public void setGroup(Group group) {
        this.group.set(group);
    }
    
    /**
     * Return an int calculated from id for the User
     * @return an int representating the instance of this entity
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
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
        if (!(object instanceof Document)) {
            return false;
        }
        Document other = (Document) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
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
        return "serverapplication.entities.Document[ id=" + id + " ]";
    }

  

    
}

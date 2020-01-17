/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.beans;

import java.io.Serializable;
import java.util.Set;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableSet;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
/**
 * Class category, with the different categories for our application. Every
 * document must have a category
 *
 * @author Adrian
 */
@XmlRootElement
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * A long with the identifier of the category
     */
    private SimpleLongProperty id;
    /**
     * A String with the name of the category
     */
    private SimpleStringProperty name;
    /**
     * A collection with the documents of this category
     */
    private SimpleSetProperty<Document> documents;

    public Long getId() {
        return this.id.get();
    }

    public void setId(Long id) {
        this.id.set(id);
    }

    public String getName() {
         return this.name.get();
    }

    public void setName(String Name) {
        this.name.set(Name);
    }

    public void setDocuments(ObservableSet<Document> documents) {
        this.documents.set(documents);
    }
    
    public Category(Long id, String name, ObservableSet document){
        this.id = new SimpleLongProperty(id);
        this.name = new SimpleStringProperty(name);
        this.documents = new SimpleSetProperty<>(document);
    }
    
    public Category(){
        this.id = new SimpleLongProperty();
        this.name = new SimpleStringProperty();
        this.documents = new SimpleSetProperty<>();
    }
    /**
     * Return an int calculated from id for the Category
     *
     * @return an int representating the instance of this entity
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * Compares two instances of Category
     *
     * @param object the other Category instance to compare to
     * @return true if instances are equal
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Category)) {
            return false;
        }
        Category other = (Category) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
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
        return this.name.get();    
    }

}

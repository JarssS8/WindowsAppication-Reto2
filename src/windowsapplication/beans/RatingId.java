/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windowsapplication.beans;

import java.io.Serializable;
import javafx.beans.property.SimpleLongProperty;


/**
 *
 * @author Gaizka Andres
 */
public class RatingId implements Serializable{
    
    private SimpleLongProperty idDocument;
    private SimpleLongProperty idUser;

    public void ratinId(Long idDocument,Long idUser){
        this.idDocument.set(idDocument);
        this.idUser.set(idUser);
    }
    
    public Long getIdDocument() {
        return idDocument.get();
    }

    public void setIdDocument(Long idDocument) {
        this.idDocument.set(idDocument);
    }

    public Long getIdUser() {
        return idUser.get();
    }

    public void setIdUser(Long idUser) {
        this.idUser.set(idUser);
    }
    
    /**
     * Return an int calculated from id for the User
     * @return an int representating the instance of this entity
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDocument != null ? idDocument.hashCode() : 0);
        hash += (idUser != null ? idUser.hashCode() : 0);
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
        if (!(object instanceof RatingId)) {
            return false;
        }
        RatingId other = (RatingId) object;
        if ((this.idDocument == null && other.idDocument != null) || 
            (this.idDocument != null && !this.idDocument.equals(other.idDocument))) {
            return false;
        }
        if ((this.idUser == null && other.idUser != null) || 
            (this.idUser != null && !this.idUser.equals(other.idUser))) {
            return false;
        }
        return true;
    }
}

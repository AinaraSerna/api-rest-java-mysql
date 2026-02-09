/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jpa_discografia;

import jakarta.json.bind.annotation.JsonbTransient;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 *
 * @author ainar
 */
@Entity
@Table(name = "albumes")
@NamedQueries({
    @NamedQuery(name = "Albumes.findAll", query = "SELECT a FROM Albumes a"),
    @NamedQuery(name = "Albumes.findByIDalbum", query = "SELECT a FROM Albumes a WHERE a.iDalbum = :iDalbum"),
    @NamedQuery(name = "Albumes.findByInterprete", query = "SELECT a FROM Albumes a WHERE a.interprete = :interprete"),
    @NamedQuery(name = "Albumes.findByNombre", query = "SELECT a FROM Albumes a WHERE a.nombre = :nombre"),
    @NamedQuery(name = "Albumes.findByFechapublicacion", query = "SELECT a FROM Albumes a WHERE a.fechapublicacion = :fechapublicacion"),
    @NamedQuery(name = "Albumes.findByDuracion", query = "SELECT a FROM Albumes a WHERE a.duracion = :duracion"),
    @NamedQuery(name = "Albumes.findByDescripcion", query = "SELECT a FROM Albumes a WHERE a.descripcion = :descripcion"),
    @NamedQuery(name = "Albumes.findByCuriosidades", query = "SELECT a FROM Albumes a WHERE a.curiosidades = :curiosidades")})
public class Albumes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_album")
    private Integer iDalbum;
    @Basic(optional = false)
    @Column(name = "Interprete")
    private String interprete;
    @Basic(optional = false)
    @Column(name = "Nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "Fecha_publicacion")
    @Temporal(TemporalType.DATE)
    private Date fechapublicacion;
    @Basic(optional = false)
    @Column(name = "Duracion")
    @Temporal(TemporalType.TIME)
    private Date duracion;
    @Basic(optional = false)
    @Column(name = "Descripcion")
    private String descripcion;
    @Column(name = "Curiosidades")
    private String curiosidades;
    @OneToMany(mappedBy = "album")
//    @JsonbTransient
    private Collection<Canciones> cancionesCollection;

    public Albumes() {
    }

    public Albumes(Integer iDalbum) {
        this.iDalbum = iDalbum;
    }

    public Albumes(Integer iDalbum, String interprete, String nombre, Date fechapublicacion, Date duracion, String descripcion) {
        this.iDalbum = iDalbum;
        this.interprete = interprete;
        this.nombre = nombre;
        this.fechapublicacion = fechapublicacion;
        this.duracion = duracion;
        this.descripcion = descripcion;
    }

    public Integer getIDalbum() {
        return iDalbum;
    }

    public void setIDalbum(Integer iDalbum) {
        this.iDalbum = iDalbum;
    }

    public String getInterprete() {
        return interprete;
    }

    public void setInterprete(String interprete) {
        this.interprete = interprete;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechapublicacion() {
        return fechapublicacion;
    }

    public void setFechapublicacion(Date fechapublicacion) {
        this.fechapublicacion = fechapublicacion;
    }

    public Date getDuracion() {
        return duracion;
    }

    public void setDuracion(Date duracion) {
        this.duracion = duracion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCuriosidades() {
        return curiosidades;
    }

    public void setCuriosidades(String curiosidades) {
        this.curiosidades = curiosidades;
    }

    public Collection<Canciones> getCancionesCollection() {
        return cancionesCollection;
    }

    public void setCancionesCollection(Collection<Canciones> cancionesCollection) {
        this.cancionesCollection = cancionesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDalbum != null ? iDalbum.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Albumes)) {
            return false;
        }
        Albumes other = (Albumes) object;
        if ((this.iDalbum == null && other.iDalbum != null) || (this.iDalbum != null && !this.iDalbum.equals(other.iDalbum))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpaeminem.Albumes[ iDalbum=" + iDalbum + " ]";
    }
    
}

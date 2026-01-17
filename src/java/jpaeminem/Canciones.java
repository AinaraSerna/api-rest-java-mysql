/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jpaeminem;

import jakarta.json.bind.annotation.JsonbTransient;
import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 *
 * @author ainar
 */
@Entity
@Table(name = "canciones")
@NamedQueries({
    @NamedQuery(name = "Canciones.findAll", query = "SELECT c FROM Canciones c"),
    @NamedQuery(name = "Canciones.findByIDsong", query = "SELECT c FROM Canciones c WHERE c.iDsong = :iDsong"),
    @NamedQuery(name = "Canciones.findByInterprete", query = "SELECT c FROM Canciones c WHERE c.interprete = :interprete"),
    @NamedQuery(name = "Canciones.findByNombre", query = "SELECT c FROM Canciones c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "Canciones.findByArtistasinvitados", query = "SELECT c FROM Canciones c WHERE c.artistasinvitados = :artistasinvitados"),
    @NamedQuery(name = "Canciones.findByNumerodepista", query = "SELECT c FROM Canciones c WHERE c.numerodepista = :numerodepista"),
    @NamedQuery(name = "Canciones.findByDuracion", query = "SELECT c FROM Canciones c WHERE c.duracion = :duracion"),
    @NamedQuery(name = "Canciones.findBySingle", query = "SELECT c FROM Canciones c WHERE c.single = :single"),
    @NamedQuery(name = "Canciones.findByDescripcion", query = "SELECT c FROM Canciones c WHERE c.descripcion = :descripcion"),
    @NamedQuery(name = "Canciones.findByCompositores", query = "SELECT c FROM Canciones c WHERE c.compositores = :compositores"),
    @NamedQuery(name = "Canciones.findByProductores", query = "SELECT c FROM Canciones c WHERE c.productores = :productores"),
    @NamedQuery(name = "Canciones.findBySamples", query = "SELECT c FROM Canciones c WHERE c.samples = :samples"),
    @NamedQuery(name = "Canciones.findByAlbum", query = "SELECT c FROM Canciones c WHERE c.album = :album")})
public class Canciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_song")
    private Integer iDsong;
    @Basic(optional = false)
    @Column(name = "Interprete")
    private String interprete;
    @Basic(optional = false)
    @Column(name = "Nombre")
    private String nombre;
    @Column(name = "Artistas_invitados")
    private String artistasinvitados;
    @Basic(optional = false)
    @Column(name = "Numero_de_pista")
    private int numerodepista;
    @Basic(optional = false)
    @Column(name = "Duracion")
    @Temporal(TemporalType.TIME)
    private Date duracion;
    @Basic(optional = false)
    @Column(name = "Single")
    private boolean single;
    @Basic(optional = false)
    @Column(name = "Descripcion")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "Compositores")
    private String compositores;
    @Column(name = "Productores")
    private String productores;
    @Column(name = "Samples")
    private String samples;
    @JoinColumn(name = "Album", referencedColumnName = "Nombre")
    @ManyToOne
    @JsonbTransient
    private Albumes album;
    @Column(name = "album", updatable = false, insertable = false)
    private String alb;

    public String getAlb() {
        return alb;
    }

    public Canciones() {
    }

    public Canciones(Integer iDsong) {
        this.iDsong = iDsong;
    }

    public Canciones(Integer iDsong, String interprete, String nombre, int numerodepista, Date duracion, boolean single, String descripcion, String compositores) {
        this.iDsong = iDsong;
        this.interprete = interprete;
        this.nombre = nombre;
        this.numerodepista = numerodepista;
        this.duracion = duracion;
        this.single = single;
        this.descripcion = descripcion;
        this.compositores = compositores;
    }

    public Integer getIDsong() {
        return iDsong;
    }

    public void setIDsong(Integer iDsong) {
        this.iDsong = iDsong;
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

    public String getArtistasinvitados() {
        return artistasinvitados;
    }

    public void setArtistasinvitados(String artistasinvitados) {
        this.artistasinvitados = artistasinvitados;
    }

    public int getNumerodepista() {
        return numerodepista;
    }

    public void setNumerodepista(int numerodepista) {
        this.numerodepista = numerodepista;
    }

    public Date getDuracion() {
        return duracion;
    }

    public void setDuracion(Date duracion) {
        this.duracion = duracion;
    }

    public boolean getSingle() {
        return single;
    }

    public void setSingle(boolean single) {
        this.single = single;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCompositores() {
        return compositores;
    }

    public void setCompositores(String compositores) {
        this.compositores = compositores;
    }

    public String getProductores() {
        return productores;
    }

    public void setProductores(String productores) {
        this.productores = productores;
    }

    public String getSamples() {
        return samples;
    }

    public void setSamples(String samples) {
        this.samples = samples;
    }

    public Albumes getAlbum() {
        return album;
    }

    public void setAlbum(Albumes album) {
        this.album = album;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDsong != null ? iDsong.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Canciones)) {
            return false;
        }
        Canciones other = (Canciones) object;
        if ((this.iDsong == null && other.iDsong != null) || (this.iDsong != null && !this.iDsong.equals(other.iDsong))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpaeminem.Canciones[ iDsong=" + iDsong + " ]";
    }

}

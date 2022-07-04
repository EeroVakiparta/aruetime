package com.arue.aruetime.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ArueMan.
 */
@Entity
@Table(name = "arue_man")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ArueMan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name_tag", nullable = false)
    private String nameTag;

    @Column(name = "score")
    private Long score;

    @OneToMany(mappedBy = "participants")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "participants", "games" }, allowSetters = true)
    private Set<GamingSession> gamingSessions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ArueMan id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameTag() {
        return this.nameTag;
    }

    public ArueMan nameTag(String nameTag) {
        this.setNameTag(nameTag);
        return this;
    }

    public void setNameTag(String nameTag) {
        this.nameTag = nameTag;
    }

    public Long getScore() {
        return this.score;
    }

    public ArueMan score(Long score) {
        this.setScore(score);
        return this;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public Set<GamingSession> getGamingSessions() {
        return this.gamingSessions;
    }

    public void setGamingSessions(Set<GamingSession> gamingSessions) {
        if (this.gamingSessions != null) {
            this.gamingSessions.forEach(i -> i.setParticipants(null));
        }
        if (gamingSessions != null) {
            gamingSessions.forEach(i -> i.setParticipants(this));
        }
        this.gamingSessions = gamingSessions;
    }

    public ArueMan gamingSessions(Set<GamingSession> gamingSessions) {
        this.setGamingSessions(gamingSessions);
        return this;
    }

    public ArueMan addGamingSession(GamingSession gamingSession) {
        this.gamingSessions.add(gamingSession);
        gamingSession.setParticipants(this);
        return this;
    }

    public ArueMan removeGamingSession(GamingSession gamingSession) {
        this.gamingSessions.remove(gamingSession);
        gamingSession.setParticipants(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArueMan)) {
            return false;
        }
        return id != null && id.equals(((ArueMan) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArueMan{" +
            "id=" + getId() +
            ", nameTag='" + getNameTag() + "'" +
            ", score=" + getScore() +
            "}";
    }
}

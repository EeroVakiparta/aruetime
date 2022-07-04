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
 * A Game.
 */
@Entity
@Table(name = "game")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "game_name", nullable = false)
    private String gameName;

    @Column(name = "popularity")
    private Long popularity;

    @OneToMany(mappedBy = "games")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "participants", "games" }, allowSetters = true)
    private Set<GamingSession> gamingSessions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Game id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGameName() {
        return this.gameName;
    }

    public Game gameName(String gameName) {
        this.setGameName(gameName);
        return this;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public Long getPopularity() {
        return this.popularity;
    }

    public Game popularity(Long popularity) {
        this.setPopularity(popularity);
        return this;
    }

    public void setPopularity(Long popularity) {
        this.popularity = popularity;
    }

    public Set<GamingSession> getGamingSessions() {
        return this.gamingSessions;
    }

    public void setGamingSessions(Set<GamingSession> gamingSessions) {
        if (this.gamingSessions != null) {
            this.gamingSessions.forEach(i -> i.setGames(null));
        }
        if (gamingSessions != null) {
            gamingSessions.forEach(i -> i.setGames(this));
        }
        this.gamingSessions = gamingSessions;
    }

    public Game gamingSessions(Set<GamingSession> gamingSessions) {
        this.setGamingSessions(gamingSessions);
        return this;
    }

    public Game addGamingSession(GamingSession gamingSession) {
        this.gamingSessions.add(gamingSession);
        gamingSession.setGames(this);
        return this;
    }

    public Game removeGamingSession(GamingSession gamingSession) {
        this.gamingSessions.remove(gamingSession);
        gamingSession.setGames(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Game)) {
            return false;
        }
        return id != null && id.equals(((Game) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Game{" +
            "id=" + getId() +
            ", gameName='" + getGameName() + "'" +
            ", popularity=" + getPopularity() +
            "}";
    }
}

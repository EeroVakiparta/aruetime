package com.arue.aruetime.domain;

import com.arue.aruetime.domain.enumeration.Theme;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A GamingSession.
 */
@Entity
@Table(name = "gaming_session")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GamingSession implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "session_name")
    private String sessionName;

    @Column(name = "start_time")
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @Column(name = "success")
    private Boolean success;

    @Enumerated(EnumType.STRING)
    @Column(name = "theme")
    private Theme theme;

    @ManyToOne
    @JsonIgnoreProperties(value = { "gamingSessions" }, allowSetters = true)
    private ArueMan participants;

    @ManyToOne
    @JsonIgnoreProperties(value = { "gamingSessions" }, allowSetters = true)
    private Game games;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GamingSession id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSessionName() {
        return this.sessionName;
    }

    public GamingSession sessionName(String sessionName) {
        this.setSessionName(sessionName);
        return this;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public Instant getStartTime() {
        return this.startTime;
    }

    public GamingSession startTime(Instant startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return this.endTime;
    }

    public GamingSession endTime(Instant endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Boolean getSuccess() {
        return this.success;
    }

    public GamingSession success(Boolean success) {
        this.setSuccess(success);
        return this;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Theme getTheme() {
        return this.theme;
    }

    public GamingSession theme(Theme theme) {
        this.setTheme(theme);
        return this;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public ArueMan getParticipants() {
        return this.participants;
    }

    public void setParticipants(ArueMan arueMan) {
        this.participants = arueMan;
    }

    public GamingSession participants(ArueMan arueMan) {
        this.setParticipants(arueMan);
        return this;
    }

    public Game getGames() {
        return this.games;
    }

    public void setGames(Game game) {
        this.games = game;
    }

    public GamingSession games(Game game) {
        this.setGames(game);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GamingSession)) {
            return false;
        }
        return id != null && id.equals(((GamingSession) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GamingSession{" +
            "id=" + getId() +
            ", sessionName='" + getSessionName() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", success='" + getSuccess() + "'" +
            ", theme='" + getTheme() + "'" +
            "}";
    }
}

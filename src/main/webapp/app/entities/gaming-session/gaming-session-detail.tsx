import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './gaming-session.reducer';

export const GamingSessionDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const gamingSessionEntity = useAppSelector(state => state.gamingSession.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="gamingSessionDetailsHeading">
          <Translate contentKey="aruetimeApp.gamingSession.detail.title">GamingSession</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{gamingSessionEntity.id}</dd>
          <dt>
            <span id="sessionName">
              <Translate contentKey="aruetimeApp.gamingSession.sessionName">Session Name</Translate>
            </span>
          </dt>
          <dd>{gamingSessionEntity.sessionName}</dd>
          <dt>
            <span id="startTime">
              <Translate contentKey="aruetimeApp.gamingSession.startTime">Start Time</Translate>
            </span>
          </dt>
          <dd>
            {gamingSessionEntity.startTime ? (
              <TextFormat value={gamingSessionEntity.startTime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="endTime">
              <Translate contentKey="aruetimeApp.gamingSession.endTime">End Time</Translate>
            </span>
          </dt>
          <dd>
            {gamingSessionEntity.endTime ? <TextFormat value={gamingSessionEntity.endTime} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="success">
              <Translate contentKey="aruetimeApp.gamingSession.success">Success</Translate>
            </span>
          </dt>
          <dd>{gamingSessionEntity.success ? 'true' : 'false'}</dd>
          <dt>
            <span id="theme">
              <Translate contentKey="aruetimeApp.gamingSession.theme">Theme</Translate>
            </span>
          </dt>
          <dd>{gamingSessionEntity.theme}</dd>
          <dt>
            <Translate contentKey="aruetimeApp.gamingSession.participants">Participants</Translate>
          </dt>
          <dd>{gamingSessionEntity.participants ? gamingSessionEntity.participants.id : ''}</dd>
          <dt>
            <Translate contentKey="aruetimeApp.gamingSession.games">Games</Translate>
          </dt>
          <dd>{gamingSessionEntity.games ? gamingSessionEntity.games.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/gaming-session" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/gaming-session/${gamingSessionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default GamingSessionDetail;

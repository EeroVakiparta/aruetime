import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './game.reducer';

export const GameDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const gameEntity = useAppSelector(state => state.game.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="gameDetailsHeading">
          <Translate contentKey="aruetimeApp.game.detail.title">Game</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{gameEntity.id}</dd>
          <dt>
            <span id="gameName">
              <Translate contentKey="aruetimeApp.game.gameName">Game Name</Translate>
            </span>
          </dt>
          <dd>{gameEntity.gameName}</dd>
          <dt>
            <span id="popularity">
              <Translate contentKey="aruetimeApp.game.popularity">Popularity</Translate>
            </span>
          </dt>
          <dd>{gameEntity.popularity}</dd>
        </dl>
        <Button tag={Link} to="/game" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/game/${gameEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default GameDetail;

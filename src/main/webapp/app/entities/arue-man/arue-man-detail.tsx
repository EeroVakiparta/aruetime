import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './arue-man.reducer';

export const ArueManDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const arueManEntity = useAppSelector(state => state.arueMan.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="arueManDetailsHeading">
          <Translate contentKey="aruetimeApp.arueMan.detail.title">ArueMan</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{arueManEntity.id}</dd>
          <dt>
            <span id="nameTag">
              <Translate contentKey="aruetimeApp.arueMan.nameTag">Name Tag</Translate>
            </span>
          </dt>
          <dd>{arueManEntity.nameTag}</dd>
          <dt>
            <span id="score">
              <Translate contentKey="aruetimeApp.arueMan.score">Score</Translate>
            </span>
          </dt>
          <dd>{arueManEntity.score}</dd>
        </dl>
        <Button tag={Link} to="/arue-man" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/arue-man/${arueManEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ArueManDetail;

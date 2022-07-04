import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IGamingSession } from 'app/shared/model/gaming-session.model';
import { getEntities } from './gaming-session.reducer';

export const GamingSession = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const gamingSessionList = useAppSelector(state => state.gamingSession.entities);
  const loading = useAppSelector(state => state.gamingSession.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="gaming-session-heading" data-cy="GamingSessionHeading">
        <Translate contentKey="aruetimeApp.gamingSession.home.title">Gaming Sessions</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="aruetimeApp.gamingSession.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/gaming-session/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="aruetimeApp.gamingSession.home.createLabel">Create new Gaming Session</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {gamingSessionList && gamingSessionList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="aruetimeApp.gamingSession.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="aruetimeApp.gamingSession.sessionName">Session Name</Translate>
                </th>
                <th>
                  <Translate contentKey="aruetimeApp.gamingSession.startTime">Start Time</Translate>
                </th>
                <th>
                  <Translate contentKey="aruetimeApp.gamingSession.endTime">End Time</Translate>
                </th>
                <th>
                  <Translate contentKey="aruetimeApp.gamingSession.success">Success</Translate>
                </th>
                <th>
                  <Translate contentKey="aruetimeApp.gamingSession.theme">Theme</Translate>
                </th>
                <th>
                  <Translate contentKey="aruetimeApp.gamingSession.participants">Participants</Translate>
                </th>
                <th>
                  <Translate contentKey="aruetimeApp.gamingSession.games">Games</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {gamingSessionList.map((gamingSession, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/gaming-session/${gamingSession.id}`} color="link" size="sm">
                      {gamingSession.id}
                    </Button>
                  </td>
                  <td>{gamingSession.sessionName}</td>
                  <td>
                    {gamingSession.startTime ? <TextFormat type="date" value={gamingSession.startTime} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {gamingSession.endTime ? <TextFormat type="date" value={gamingSession.endTime} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{gamingSession.success ? 'true' : 'false'}</td>
                  <td>
                    <Translate contentKey={`aruetimeApp.Theme.${gamingSession.theme}`} />
                  </td>
                  <td>
                    {gamingSession.participants ? (
                      <Link to={`/arue-man/${gamingSession.participants.id}`}>{gamingSession.participants.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{gamingSession.games ? <Link to={`/game/${gamingSession.games.id}`}>{gamingSession.games.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/gaming-session/${gamingSession.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/gaming-session/${gamingSession.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/gaming-session/${gamingSession.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="aruetimeApp.gamingSession.home.notFound">No Gaming Sessions found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default GamingSession;

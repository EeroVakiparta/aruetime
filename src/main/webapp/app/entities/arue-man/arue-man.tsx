import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IArueMan } from 'app/shared/model/arue-man.model';
import { getEntities } from './arue-man.reducer';

export const ArueMan = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const arueManList = useAppSelector(state => state.arueMan.entities);
  const loading = useAppSelector(state => state.arueMan.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="arue-man-heading" data-cy="ArueManHeading">
        <Translate contentKey="aruetimeApp.arueMan.home.title">Arue Men</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="aruetimeApp.arueMan.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/arue-man/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="aruetimeApp.arueMan.home.createLabel">Create new Arue Man</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {arueManList && arueManList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="aruetimeApp.arueMan.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="aruetimeApp.arueMan.nameTag">Name Tag</Translate>
                </th>
                <th>
                  <Translate contentKey="aruetimeApp.arueMan.score">Score</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {arueManList.map((arueMan, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/arue-man/${arueMan.id}`} color="link" size="sm">
                      {arueMan.id}
                    </Button>
                  </td>
                  <td>{arueMan.nameTag}</td>
                  <td>{arueMan.score}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/arue-man/${arueMan.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/arue-man/${arueMan.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/arue-man/${arueMan.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="aruetimeApp.arueMan.home.notFound">No Arue Men found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default ArueMan;

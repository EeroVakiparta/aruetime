import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IArueMan } from 'app/shared/model/arue-man.model';
import { getEntities as getArueMen } from 'app/entities/arue-man/arue-man.reducer';
import { IGame } from 'app/shared/model/game.model';
import { getEntities as getGames } from 'app/entities/game/game.reducer';
import { IGamingSession } from 'app/shared/model/gaming-session.model';
import { Theme } from 'app/shared/model/enumerations/theme.model';
import { getEntity, updateEntity, createEntity, reset } from './gaming-session.reducer';

export const GamingSessionUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const arueMen = useAppSelector(state => state.arueMan.entities);
  const games = useAppSelector(state => state.game.entities);
  const gamingSessionEntity = useAppSelector(state => state.gamingSession.entity);
  const loading = useAppSelector(state => state.gamingSession.loading);
  const updating = useAppSelector(state => state.gamingSession.updating);
  const updateSuccess = useAppSelector(state => state.gamingSession.updateSuccess);
  const themeValues = Object.keys(Theme);
  const handleClose = () => {
    props.history.push('/gaming-session');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getArueMen({}));
    dispatch(getGames({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.startTime = convertDateTimeToServer(values.startTime);
    values.endTime = convertDateTimeToServer(values.endTime);

    const entity = {
      ...gamingSessionEntity,
      ...values,
      participants: arueMen.find(it => it.id.toString() === values.participants.toString()),
      games: games.find(it => it.id.toString() === values.games.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          startTime: displayDefaultDateTime(),
          endTime: displayDefaultDateTime(),
        }
      : {
          theme: 'HARDCORE',
          ...gamingSessionEntity,
          startTime: convertDateTimeFromServer(gamingSessionEntity.startTime),
          endTime: convertDateTimeFromServer(gamingSessionEntity.endTime),
          participants: gamingSessionEntity?.participants?.id,
          games: gamingSessionEntity?.games?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="aruetimeApp.gamingSession.home.createOrEditLabel" data-cy="GamingSessionCreateUpdateHeading">
            <Translate contentKey="aruetimeApp.gamingSession.home.createOrEditLabel">Create or edit a GamingSession</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="gaming-session-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('aruetimeApp.gamingSession.sessionName')}
                id="gaming-session-sessionName"
                name="sessionName"
                data-cy="sessionName"
                type="text"
              />
              <ValidatedField
                label={translate('aruetimeApp.gamingSession.startTime')}
                id="gaming-session-startTime"
                name="startTime"
                data-cy="startTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('aruetimeApp.gamingSession.endTime')}
                id="gaming-session-endTime"
                name="endTime"
                data-cy="endTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('aruetimeApp.gamingSession.success')}
                id="gaming-session-success"
                name="success"
                data-cy="success"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('aruetimeApp.gamingSession.theme')}
                id="gaming-session-theme"
                name="theme"
                data-cy="theme"
                type="select"
              >
                {themeValues.map(theme => (
                  <option value={theme} key={theme}>
                    {translate('aruetimeApp.Theme.' + theme)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="gaming-session-participants"
                name="participants"
                data-cy="participants"
                label={translate('aruetimeApp.gamingSession.participants')}
                type="select"
              >
                <option value="" key="0" />
                {arueMen
                  ? arueMen.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="gaming-session-games"
                name="games"
                data-cy="games"
                label={translate('aruetimeApp.gamingSession.games')}
                type="select"
              >
                <option value="" key="0" />
                {games
                  ? games.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/gaming-session" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default GamingSessionUpdate;

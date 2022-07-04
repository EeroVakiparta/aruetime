import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import GamingSession from './gaming-session';
import GamingSessionDetail from './gaming-session-detail';
import GamingSessionUpdate from './gaming-session-update';
import GamingSessionDeleteDialog from './gaming-session-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GamingSessionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GamingSessionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GamingSessionDetail} />
      <ErrorBoundaryRoute path={match.url} component={GamingSession} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={GamingSessionDeleteDialog} />
  </>
);

export default Routes;

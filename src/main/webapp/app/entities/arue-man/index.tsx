import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ArueMan from './arue-man';
import ArueManDetail from './arue-man-detail';
import ArueManUpdate from './arue-man-update';
import ArueManDeleteDialog from './arue-man-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ArueManUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ArueManUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ArueManDetail} />
      <ErrorBoundaryRoute path={match.url} component={ArueMan} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ArueManDeleteDialog} />
  </>
);

export default Routes;

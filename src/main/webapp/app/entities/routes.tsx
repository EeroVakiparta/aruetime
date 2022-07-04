import React from 'react';
import { Switch } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ArueMan from './arue-man';
import GamingSession from './gaming-session';
import Game from './game';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default ({ match }) => {
  return (
    <div>
      <Switch>
        {/* prettier-ignore */}
        <ErrorBoundaryRoute path={`${match.url}arue-man`} component={ArueMan} />
        <ErrorBoundaryRoute path={`${match.url}gaming-session`} component={GamingSession} />
        <ErrorBoundaryRoute path={`${match.url}game`} component={Game} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </Switch>
    </div>
  );
};

import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/arue-man">
        <Translate contentKey="global.menu.entities.arueMan" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/gaming-session">
        <Translate contentKey="global.menu.entities.gamingSession" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/game">
        <Translate contentKey="global.menu.entities.game" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu as React.ComponentType<any>;

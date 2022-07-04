import dayjs from 'dayjs';
import { IArueMan } from 'app/shared/model/arue-man.model';
import { IGame } from 'app/shared/model/game.model';
import { Theme } from 'app/shared/model/enumerations/theme.model';

export interface IGamingSession {
  id?: number;
  sessionName?: string | null;
  startTime?: string | null;
  endTime?: string | null;
  success?: boolean | null;
  theme?: Theme | null;
  participants?: IArueMan | null;
  games?: IGame | null;
}

export const defaultValue: Readonly<IGamingSession> = {
  success: false,
};

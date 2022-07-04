import { IGamingSession } from 'app/shared/model/gaming-session.model';

export interface IGame {
  id?: number;
  gameName?: string;
  popularity?: number | null;
  gamingSessions?: IGamingSession[] | null;
}

export const defaultValue: Readonly<IGame> = {};

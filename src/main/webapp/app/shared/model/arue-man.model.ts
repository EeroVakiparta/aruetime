import { IGamingSession } from 'app/shared/model/gaming-session.model';

export interface IArueMan {
  id?: number;
  nameTag?: string;
  score?: number | null;
  gamingSessions?: IGamingSession[] | null;
}

export const defaultValue: Readonly<IArueMan> = {};

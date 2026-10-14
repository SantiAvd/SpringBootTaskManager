
export type Status = 'NOT_STARTED' | 'IN_PROGRESS' | 'DONE';

export type Priority =
    | 'NOT_ASSIGNED'
    | 'LOW'
    | 'MEDIUM'
    | 'HIGH'
    | 'URGEN';

export interface Task {
    id: number;
    title: string;
    description?: string | null;
    status?: string | null;
    priority?: string | null;
}

export interface TaskPayload {
    title: string;
    description: string;
    status: Status;
    priority: Priority;
}

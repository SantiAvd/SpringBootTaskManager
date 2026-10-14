
import type { Task, TaskPayload } from './types.ts';

const BASE = (
    import.meta.env.VITE_API_URL || 'http://localhost:8080'
).replace(/\/$/, '');

const USER_ID = 2;

async function request<T>(
    path: string,
    init?: RequestInit,
): Promise<T> {
  let res: Response;

  try {
    res = await fetch(`${BASE}${path}`, {
      ...init,
      headers: {
        ...(init?.body
            ? { 'Content-Type': 'application/json' }
            : {}),
        ...init?.headers,
      },
    });
  } catch {
    throw new Error(
        'Не удалось подключиться к Spring Boot. Проверь, запущен ли сервер.',
    );
  }

  if (!res.ok) {
    const body = await res.text().catch(() => '');
    let message = `Ошибка сервера (${res.status})`;

    if (body) {
      try {
        const json = JSON.parse(body);
        message =
            json.message ||
            json.detail ||
            Object.values(json.errors || {}).join(', ') ||
            message;
      } catch {
        message = body.slice(0, 180);
      }
    }

    throw new Error(message);
  }

  if (res.status === 204) {
    return undefined as T;
  }

  const text = await res.text();
  return (text ? JSON.parse(text) : undefined) as T;
}

export async function getTasks(): Promise<Task[]> {
  const value = await request<unknown>('/tasks');

  if (Array.isArray(value)) {
    return value as Task[];
  }

  if (value && typeof value === 'object') {
    const result = value as {
      content?: unknown;
      tasks?: unknown;
      data?: unknown;
    };

    for (const key of ['content', 'tasks', 'data'] as const) {
      if (Array.isArray(result[key])) {
        return result[key] as Task[];
      }
    }
  }

  return [];
}

export const createTask = (body: TaskPayload) =>
    request<Task>('/tasks', {
      method: 'POST',
      body: JSON.stringify({
        title: body.title,
        description: body.description,
        status: body.status,
        userId: USER_ID,
      }),
    });

export function updateTask(
    id: number,
    payload: TaskPayload,
): Promise<Task> {
  return request<Task>(`/tasks/${id}`, {
    method: 'PUT',
    body: JSON.stringify(payload),
  });
}

export function deleteTask(id: number): Promise<void> {
  return request<void>(`/tasks/${id}`, {
    method: 'DELETE',
  });
}

import { useEffect, useMemo, useState } from 'react';
import type { FormEvent, ReactNode } from 'react';
import {
  Activity, Check, CheckCheck, ChevronDown, CirclePlus, ClipboardList,
  Clock3, Filter, LayoutDashboard, ListTodo, LoaderCircle, Pencil, Plus,
  RefreshCw, Search, Sparkles, Trash2, X, AlertCircle,
} from 'lucide-react';

import { createTask, deleteTask, getTasks, updateTask } from './api';
import type { Task, TaskPayload, Status, Priority } from './types';

const statuses: Record<Status, string> = {
  NOT_STARTED: 'К выполнению',
  IN_PROGRESS: 'В работе',
  DONE: 'Готово',
};

const priorities: Record<Priority, string> = {
  NOT_ASSIGNED: 'Не назначен',
  LOW: 'Низкий',
  MEDIUM: 'Средний',
  HIGH: 'Высокий',
  URGEN: 'Срочный',
};

const statusOf = (value: unknown): Status => {
  const status = String(value ?? '').toUpperCase();
  if (['DONE', 'COMPLETED', 'FINISHED'].includes(status)) return 'DONE';
  if (['IN_PROGRESS', 'IN PROGRESS', 'IN-PROGRESS', 'DOING'].includes(status)) {
    return 'IN_PROGRESS';
  }
  return 'NOT_STARTED';
};

const priorityOf = (value: unknown): Priority => {
  const priority = String(value ?? '').toUpperCase();
  if (priority === 'NOT_ASSIGNED') return 'NOT_ASSIGNED';
  if (priority === 'LOW') return 'LOW';
  if (priority === 'HIGH') return 'HIGH';
  if (priority === 'URGEN') return 'URGEN';
  return 'MEDIUM';
};

const normalize = (task: Task): Task => ({
  ...task,
  status: statusOf(task.status),
  priority: priorityOf(task.priority),
});

const empty: TaskPayload = {
  title: '',
  description: '',
  status: 'NOT_STARTED',
  priority: 'NOT_ASSIGNED',
};

export default function App() {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [loading, setLoading] = useState(true);
  const [busy, setBusy] = useState(false);
  const [error, setError] = useState('');
  const [notice, setNotice] = useState('');
  const [query, setQuery] = useState('');
  const [filter, setFilter] = useState<'ALL' | Status>('ALL');
  const [priorityFilter, setPriorityFilter] = useState<'ALL' | Priority>('ALL');

  const [modal, setModal] = useState(false);
  const [editing, setEditing] = useState<Task | null>(null);
  const [form, setForm] = useState<TaskPayload>(empty);
  const [deleting, setDeleting] = useState<Task | null>(null);

  async function reload() {
    setLoading(true);
    setError('');
    try {
      setTasks((await getTasks()).map(normalize));
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Не удалось загрузить задачи');
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    void reload();
  }, []);

  const visible = useMemo(() => {
    const search = query.trim().toLowerCase();
    return tasks.filter((task) => {
      const matchesSearch =
        !search ||
        `${task.title} ${task.description ?? ''}`.toLowerCase().includes(search);
      const matchesStatus = filter === 'ALL' || statusOf(task.status) === filter;
      const matchesPriority =
        priorityFilter === 'ALL' || priorityOf(task.priority) === priorityFilter;
      return matchesSearch && matchesStatus && matchesPriority;
    });
  }, [tasks, query, filter, priorityFilter]);

  const done = tasks.filter((task) => statusOf(task.status) === 'DONE').length;
  const active = tasks.length - done;
  const progress = tasks.filter((task) => statusOf(task.status) === 'IN_PROGRESS').length;

  function resetFilters() {
    setFilter('ALL');
    setPriorityFilter('ALL');
    setQuery('');
  }

  function create() {
    setEditing(null);
    setForm(empty);
    setError('');
    setModal(true);
  }

  function edit(task: Task) {
    setEditing(task);
    setForm({
      title: task.title,
      description: task.description ?? '',
      status: statusOf(task.status),
      priority: priorityOf(task.priority),
    });
    setError('');
    setModal(true);
  }

  async function save(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    setBusy(true);
    setError('');
    setNotice('');
    const payload: TaskPayload = {
      ...form,
      title: form.title.trim(),
      description: form.description.trim(),
    };

    try {
      if (editing) {
        const updated = await updateTask(editing.id, payload);
        setTasks((old) =>
          old.map((task) =>
            task.id === editing.id
              ? normalize({ ...task, ...payload, ...(updated ?? {}) })
              : task,
          ),
        );
        setNotice('Задача обновлена');
      } else {
        const created = await createTask(payload);
        if (created && typeof created === 'object' && 'id' in created) {
          setTasks((old) => [normalize({ ...created, ...payload }), ...old]);
        } else {
          await reload();
        }
        setNotice('Задача создана');
      }
      setModal(false);
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Не удалось сохранить задачу');
    } finally {
      setBusy(false);
    }
  }

  async function changeStatus(task: Task, status: Status) {
    const payload: TaskPayload = {
      title: task.title,
      description: task.description ?? '',
      status,
      priority: priorityOf(task.priority),
    };
    try {
      const updated = await updateTask(task.id, payload);
      setTasks((old) =>
        old.map((item) =>
          item.id === task.id
            ? normalize({ ...item, ...payload, ...(updated ?? {}) })
            : item,
        ),
      );
      setError('');
      setNotice('Статус изменён');
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Не удалось изменить статус');
    }
  }

  async function remove() {
    if (!deleting) return;
    try {
      await deleteTask(deleting.id);
      setTasks((old) => old.filter((task) => task.id !== deleting.id));
      setDeleting(null);
      setNotice('Задача удалена');
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Не удалось удалить задачу');
      setDeleting(null);
    }
  }

  return (
    <div className="layout">
      <aside className="sidebar">
        <div className="brand">
          <div className="brand-icon"><CheckCheck size={22} /></div>
          <div><b>Taskflow</b><small>WORKSPACE</small></div>
        </div>

        <div className="workspace">
          <div className="workspace-avatar">TM</div>
          <div><b>Мой проект</b><small>Личное пространство</small></div>
          <ChevronDown size={16} />
        </div>

        <div className="nav-label">РАБОЧЕЕ ПРОСТРАНСТВО</div>
        <nav>
          <button className="nav active" onClick={resetFilters}>
            <LayoutDashboard />Обзор
          </button>
          <button className="nav" onClick={resetFilters}>
            <ListTodo />Все задачи <span>{tasks.length}</span>
          </button>
          <button className="nav" onClick={() => {
            setFilter('IN_PROGRESS'); setPriorityFilter('ALL'); setQuery('');
          }}>
            <Activity />В работе
          </button>
          <button className="nav" onClick={() => {
            setFilter('DONE'); setPriorityFilter('ALL'); setQuery('');
          }}>
            <Check />Завершённые
          </button>
        </nav>

        <div className="side-bottom">
          <div className="tip">
            <Sparkles size={19} />
            <b>Маленький шаг — большой результат</b>
            <p>Отмечай завершённые задачи и двигайся вперёд.</p>
          </div>
          <div className="profile">
            <div className="user-avatar">A</div>
            <div><b>Мой аккаунт</b><small>Рабочее пространство</small></div>
          </div>
        </div>
      </aside>

      <main className="main-content">
        <header className="topbar">
          <div>Рабочее пространство <span>/</span> <b>Обзор</b></div>
          <button className="icon-button" title="Обновить" onClick={() => void reload()}>
            <RefreshCw size={17} />
          </button>
        </header>

        <div className="content">
          <div className="heading">
            <div>
              <div className="eyebrow">✦ ТВОЁ ПРОСТРАНСТВО</div>
              <h1>Обзор задач<span>.</span></h1>
              <p>Всё важное — в одном месте. Что будем делать сегодня?</p>
            </div>
            <button className="primary" onClick={create}><Plus size={18} /> Новая задача</button>
          </div>

          {error && <div className="alert error">
            <AlertCircle size={17} /><span>{error}</span>
            <button onClick={() => setError('')}><X size={16} /></button>
          </div>}
          {notice && !error && <div className="alert success">
            <Check size={17} /><span>{notice}</span>
            <button onClick={() => setNotice('')}><X size={16} /></button>
          </div>}

          <section className="stats">
            <Stat icon={<ClipboardList />} label="Всего задач" value={tasks.length} color="purple" foot="В твоём списке" />
            <Stat icon={<Clock3 />} label="Активные" value={active} color="blue" foot="Ждут выполнения" />
            <Stat icon={<Activity />} label="В работе" value={progress} color="orange" foot="Уже в процессе" />
            <Stat icon={<CheckCheck />} label="Завершено" value={done} color="green" foot={tasks.length ? `${Math.round((done / tasks.length) * 100)}% от всех задач` : 'Начни с первой задачи'} />
          </section>

          <section className="panel">
            <div className="panel-head">
              <div><h2>Твои задачи</h2><p>Следи за прогрессом и держи фокус на главном.</p></div>
              <button className="secondary reset" onClick={resetFilters}><Filter size={15} /> Сбросить</button>
            </div>

            <div className="tools">
              <label className="search">
                <Search size={17} />
                <input value={query} onChange={(e) => setQuery(e.target.value)} placeholder="Поиск по задачам..." />
              </label>
              <label className="filter">
                <Filter size={15} />
                <select value={filter} onChange={(e) => setFilter(e.target.value as 'ALL' | Status)}>
                  <option value="ALL">Все статусы</option>
                  <option value="NOT_STARTED">К выполнению</option>
                  <option value="IN_PROGRESS">В работе</option>
                  <option value="DONE">Готово</option>
                </select>
              </label>
              <label className="filter">
                <Filter size={15} />
                <select value={priorityFilter} onChange={(e) => setPriorityFilter(e.target.value as 'ALL' | Priority)}>
                  <option value="ALL">Все приоритеты</option>
                  {Object.entries(priorities).map(([key, value]) => (
                    <option key={key} value={key}>{value}</option>
                  ))}
                </select>
              </label>
            </div>

            <div className="table-head">
              <span>ЗАДАЧА</span><span>СТАТУС</span><span>ПРИОРИТЕТ</span><span>ДЕЙСТВИЯ</span>
            </div>

            {loading ? (
              <div className="empty"><LoaderCircle className="spin" /><p>Загружаем задачи...</p></div>
            ) : visible.length === 0 ? (
              <div className="empty">
                <div className="empty-icon"><CirclePlus size={24} /></div>
                <h3>{tasks.length ? 'Ничего не найдено' : 'Пока нет задач'}</h3>
                <p>{tasks.length
                  ? 'Попробуй изменить поиск или фильтр.'
                  : 'Создай первую задачу — и начни превращать планы в результат.'}</p>
                {tasks.length ? (
                  <button className="secondary" onClick={resetFilters}>Сбросить фильтры</button>
                ) : (
                  <button className="primary" onClick={create}><Plus size={17} /> Создать задачу</button>
                )}
              </div>
            ) : (
              <div className="rows">
                {visible.map((task) => (
                  <article className="task-row" key={task.id}>
                    <div className="task-main">
                      <button className={`task-check ${statusOf(task.status) === 'DONE' ? 'checked' : ''}`}
                        onClick={() => void changeStatus(task, statusOf(task.status) === 'DONE' ? 'NOT_STARTED' : 'DONE')}
                        title="Изменить выполнение">
                        {statusOf(task.status) === 'DONE' && <Check size={13} />}
                      </button>
                      <div className="task-copy">
                        <b>{task.title}</b>
                        <p>{task.description || 'Описание не добавлено'}</p>
                        <small>ID: {task.id}</small>
                      </div>
                    </div>
                    <div>
                      <select className={`status status-${statusOf(task.status).toLowerCase()}`}
                        value={statusOf(task.status)}
                        onChange={(e) => void changeStatus(task, e.target.value as Status)}>
                        {Object.entries(statuses).map(([key, value]) => <option key={key} value={key}>{value}</option>)}
                      </select>
                    </div>
                    <div>
                      <span className={`priority priority-${priorityOf(task.priority).toLowerCase()}`}>
                        {priorities[priorityOf(task.priority)]}
                      </span>
                    </div>
                    <div className="row-actions">
                      <button className="row-action" title="Редактировать" onClick={() => edit(task)}><Pencil size={16} /></button>
                      <button className="row-action delete" title="Удалить" onClick={() => setDeleting(task)}><Trash2 size={16} /></button>
                    </div>
                  </article>
                ))}
              </div>
            )}

            <div className="panel-foot">
              Показано <b>{visible.length}</b> из <b>{tasks.length}</b> задач
              <span className="api-live"><i /> Spring Boot API</span>
            </div>
          </section>

          <footer>Сделано с фокусом на важное. <span>Taskflow · Task Manager</span></footer>
        </div>
      </main>

      {modal && <div className="backdrop" onMouseDown={(e) => {
        if (e.target === e.currentTarget) setModal(false);
      }}>
        <section className="modal" role="dialog" aria-modal="true">
          <div className="modal-head">
            <div>
              <small>{editing ? 'ИЗМЕНИТЬ ЗАДАЧУ' : 'НОВАЯ ЗАДАЧА'}</small>
              <h2>{editing ? 'Редактирование' : 'Добавить задачу'}</h2>
              <p>Заполни детали, остальное можно изменить позже.</p>
            </div>
            <button className="icon-button" onClick={() => setModal(false)}><X size={18} /></button>
          </div>

          <form onSubmit={save}>
            <label>Название задачи <em>*</em>
              <input autoFocus required minLength={3} maxLength={100} value={form.title}
                onChange={(e) => setForm((current) => ({ ...current, title: e.target.value }))}
                placeholder="Например, подготовить резюме" />
            </label>
            <label>Описание <small>Необязательно</small>
              <textarea rows={4} maxLength={200} value={form.description}
                onChange={(e) => setForm((current) => ({ ...current, description: e.target.value }))}
                placeholder="Добавь детали или следующий шаг..." />
            </label>
            <div className="form-grid">
              <label>Статус
                <select value={form.status} onChange={(e) => setForm((current) => ({ ...current, status: e.target.value as Status }))}>
                  {Object.entries(statuses).map(([key, value]) => <option key={key} value={key}>{value}</option>)}
                </select>
              </label>
              <label>Приоритет
                <select value={form.priority} onChange={(e) => setForm((current) => ({ ...current, priority: e.target.value as Priority }))}>
                  {Object.entries(priorities).map(([key, value]) => <option key={key} value={key}>{value}</option>)}
                </select>
              </label>
            </div>
            <div className="modal-actions">
              <button type="button" className="secondary" onClick={() => setModal(false)}>Отмена</button>
              <button type="submit" className="primary" disabled={busy}>
                {busy ? <LoaderCircle className="spin" size={17} /> : <Check size={17} />}
                {editing ? 'Сохранить' : 'Создать задачу'}
              </button>
            </div>
          </form>
        </section>
      </div>}

      {deleting && <div className="backdrop" onMouseDown={(e) => {
        if (e.target === e.currentTarget) setDeleting(null);
      }}>
        <section className="modal confirm" role="dialog" aria-modal="true">
          <div className="delete-icon"><Trash2 size={22} /></div>
          <button className="close-confirm" onClick={() => setDeleting(null)}><X size={17} /></button>
          <h2>Удалить задачу?</h2>
          <p>«{deleting.title}» будет удалена. Это действие нельзя отменить.</p>
          <div className="modal-actions">
            <button className="secondary" onClick={() => setDeleting(null)}>Отмена</button>
            <button className="danger" onClick={() => void remove()}><Trash2 size={16} /> Удалить</button>
          </div>
        </section>
      </div>}
    </div>
  );
}

function Stat({
  icon, label, value, color, foot,
}: {
  icon: ReactNode;
  label: string;
  value: number;
  color: string;
  foot: string;
}) {
  return (
    <article className="stat">
      <div className={`stat-icon ${color}`}>{icon}</div>
      <div className="stat-label">{label}</div>
      <strong>{value}</strong>
      <small>{foot}</small>
    </article>
  );
}

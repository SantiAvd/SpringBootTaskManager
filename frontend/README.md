# Task Manager frontend

React + TypeScript + Vite UI for the Spring Boot Task Manager.

## Run

```bash
npm install
npm install lucide-react
npm run dev
```

Open http://localhost:5173.

Default API: `http://localhost:8080`. Override with `VITE_API_URL` in `.env`.

Assumed endpoints: `GET /tasks`, `POST /tasks`, `PUT /task/{id}`, `DELETE /delete/{id}`. Task fields: `id`, `title`, optional `description`, `status`, `priority`. If your DTO differs, send me the controller and DTO and we'll align the adapter.

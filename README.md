# Task Manager — менеджер задач

Full-stack приложение для управления задачами, разработанное с использованием Spring Boot, PostgreSQL, React и TypeScript.

## Возможности

- Создание, просмотр, редактирование и удаление задач.
- Фильтрация задач по статусу и приоритету.
- Поиск задач по названию и описанию.
- REST API на Spring Boot.
- Хранение данных в PostgreSQL.
- Управление миграциями базы данных с помощью Liquibase.
- Пользовательский интерфейс на React и TypeScript.
- Запуск backend и базы данных с помощью Docker Compose.

## Технологический стек

**Backend**
- Java
- Spring Boot
- Spring Data JPA / Hibernate
- PostgreSQL
- Liquibase
- Maven

**Frontend**
- React
- TypeScript
- Vite

**Инфраструктура**
- Docker
- Docker Compose

## Требования

Перед запуском установите:

- Docker Desktop
- Git
- Node.js и npm — для запуска frontend

## Запуск проекта

### 1. Клонирование репозитория

```bash
git clone YOUR_REPOSITORY_URL
cd TaskManagerSpringBoot
```

Замените `YOUR_REPOSITORY_URL` на ссылку вашего GitHub-репозитория.

### 2. Настройка переменных окружения

Создайте файл `.env` в корневой директории проекта:

```dotenv
POSTGRES_USER=alex
POSTGRES_PASSWORD=1234
POSTGRES_DB=task_manager
```

Укажите собственный пароль для локальной базы данных. Файл `.env` не следует добавлять в Git.

### 3. Запуск backend и базы данных

Из корневой директории выполните:

```bash
docker compose up --build
```

Docker Compose соберёт backend и запустит приложение вместе с PostgreSQL.

### 4. Запуск frontend

Откройте второй терминал и выполните:

```bash
cd frontend
npm ci
npm run dev
```

После запуска Vite выведет локальный адрес frontend в терминале. Откройте его в браузере.

Backend API доступен по адресу `http://localhost:8080`.

## Остановка приложения

Остановите Docker Compose сочетанием клавиш `Ctrl+C`, затем выполните:

```bash
docker compose down
```

Данные PostgreSQL сохраняются в Docker volume и не удаляются при обычной остановке контейнеров.

## Миграции базы данных

Для управления изменениями структуры базы данных используется Liquibase.

Файлы миграций находятся в директории:

`src/main/resources/db/changelog/`

## Запуск тестов

Для запуска тестов backend выполните:

```bash
./mvnw test
```

Для успешного запуска тестов необходимо настроить тестовое окружение в соответствии с конфигурацией проекта.

## Структура проекта

```text
TaskManagerSpringBoot/
├── frontend/       # Клиентское приложение React + TypeScript
├── src/            # Backend на Spring Boot и тесты
├── Dockerfile      # Сборка backend-образа
├── compose.yaml    # Конфигурация Docker Compose
├── pom.xml         # Конфигурация Maven
└── README.md       # Документация проекта
```

# Task Manager

A full-stack task management application built with Spring Boot, PostgreSQL, React, and TypeScript.

## Features

- Create, view, update, and delete tasks.
- Filter tasks by status and priority.
- Search tasks by title and description.
- REST API built with Spring Boot.
- PostgreSQL database with Liquibase migrations.
- React frontend built with Vite and TypeScript.
- Docker Compose setup for the backend and database.

## Tech Stack

**Backend**
- Java
- Spring Boot
- Spring Data JPA / Hibernate
- PostgreSQL
- Liquibase
- Maven

**Frontend**
- React
- TypeScript
- Vite

**Infrastructure**
- Docker
- Docker Compose

## Prerequisites

Install the following tools:

- Docker Desktop
- Git

## Getting Started

### 1. Clone the repository

```bash
git clone YOUR_REPOSITORY_URL
cd TaskManagerSpringBoot
```

Replace `YOUR_REPOSITORY_URL` with the repository URL from GitHub.

### 2. Configure environment variables

Create a `.env` file in the project root:

```dotenv
POSTGRES_USER=alex
POSTGRES_PASSWORD=1234
POSTGRES_DB=task_manager
```

Use your own local database password. Do not commit the `.env` file.

### 3. Start the application

```bash
docker compose up --build
```

Docker Compose starts the PostgreSQL database and Spring Boot backend.

### 4. Start the frontend

Open a second terminal:

```bash
cd frontend
npm ci
npm run dev
```

Open the local URL printed by Vite in the terminal.

The backend API is available at `http://localhost:8080`.

## Stopping the Application

Press `Ctrl+C` in the terminal running Docker Compose, then run:

```bash
docker compose down
```

The PostgreSQL data volume is preserved when the containers are stopped.

## Database Migrations

Liquibase manages database schema migrations. Migration changelogs are located in:

`src/main/resources/db/changelog/`

## Running Tests

Run the backend tests with:

```bash
./mvnw test
```

The test environment must be configured according to the project's test properties.

## Project Structure

```text
TaskManagerSpringBoot/
├── frontend/       # React + TypeScript application
├── src/             # Spring Boot backend and tests
├── Dockerfile
├── compose.yaml
├── pom.xml
└── README.md
```
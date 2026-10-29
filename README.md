# UniConnect

A part-time employment, internship and alumni referral platform for the University of Kelaniya.

UniConnect connects **students**, **alumni**, **lecturers** and **employers** in one verified university community. Jobs are matched to a student's real free time, skills and location, and opportunities come from people the university already knows and trusts.

> Final year project, University of Kelaniya. *(Add your degree program, year and supervisor.)*

---

## The Problem

- Finding genuine part-time jobs and internships is hard for undergraduates.
- Many vacancies are ghost jobs: interviews are held but the position is already filled.
- Global job sites make students compete with thousands of applicants and ignore lecture timetables.
- Internship placement is a compulsory module, yet students often search alone.

## Our Solution

One platform where:

- Students enter their **lecture timetable** and are matched only with jobs that fit their free time.
- **Lecturers** publish vacancies from their industry contacts to a chosen department, degree or batch, then send shortlisted CVs to the company.
- **Alumni** refer suitable students to their company, and the student must accept before their CV is shared.
- Every match shows **why** it was suggested (skills, schedule, pay).
- Admins verify users and moderate vacancies to keep the platform safe.

## User Roles

| Role | Who | What they do |
|---|---|---|
| Job Seeker | Students | Build profile and timetable, search and apply, post "Available for Work", accept referrals |
| Job Giver | Alumni, lecturers, employers | Publish vacancies, refer or shortlist students, send CVs to companies |
| Administrator | University staff | Verify users, moderate content, resolve complaints |

## Key Features

- Role-based sign up with verification
- Student profile, CV and timetable-based availability
- Vacancy publishing with mandatory pay, hours and location details
- Targeted vacancies for a department, degree or batch (lecturers)
- "Available for Work" posts and employer invitations
- Explainable matching (for example: "85% match: skills 90%, schedule 100%, salary 60%")
- Consent-based alumni referrals
- Notifications, reviews, reports and admin moderation

## How It Works

1. Users join and are verified.
2. Students add skills, CV and timetable.
3. Employers and lecturers post vacancies; alumni refer students.
4. The system matches and notifies students.
5. Students apply, or accept or decline a referral.
6. Job givers review and shortlist; lecturers send the best CVs to the company.
7. The company interviews and decides.
8. The student is hired and can leave a review.
9. The graduate becomes an alumnus and helps the next students.

## Tech Stack

| Part | Technology |
|---|---|
| Frontend | React |
| Backend | Spring Boot (Java) |
| Database | MongoDB |
| Authentication | Spring Security with JWT *(change if you use something else)* |

## Project Structure

```
UniConnect/
├── frontend/    # React user interface
├── backend/     # Spring Boot REST API
├── docs/        # Diagrams and documents
└── README.md
```

## Getting Started

### Prerequisites

- Java JDK 17 or later
- Maven *(or use the included `mvnw` wrapper)*
- Node.js and npm
- MongoDB (local installation or a free MongoDB Atlas cluster)
- Git

### 1. Clone the repository

```bash
git clone <your-repository-url>
cd UniConnect
```

### 2. Set up the database

Start MongoDB locally (default `mongodb://localhost:27017`) or create a MongoDB Atlas cluster and copy its connection string.

### 3. Run the backend (Spring Boot)

Open `backend/src/main/resources/application.properties` and set:

```properties
server.port=8080
spring.data.mongodb.uri=mongodb://localhost:27017/uniconnect
jwt.secret=change-this-secret
```

Then run:

```bash
cd backend
./mvnw spring-boot:run
```

The API starts at `http://localhost:8080`.

### 4. Run the frontend (React)

```bash
cd frontend
npm install
npm start
```

The app opens at `http://localhost:3000`. If your project was created with Vite, use `npm run dev` instead (it runs on `http://localhost:5173`).

Set the backend address in `frontend/.env`:

```
REACT_APP_API_URL=http://localhost:8080
```

*(For Vite, the variable is `VITE_API_URL`.)*

### Notes

- Never commit real secrets (database passwords, JWT secret) to the repository. Add `.env` to `.gitignore` and keep production values out of `application.properties`.
- If you use Gradle instead of Maven, run `./gradlew bootRun`.

## Documentation

- Use case diagram and project flow: see the `docs/` folder
- *(Add links to your report, SRS or design documents.)*

## Team

| Name | Role |
|---|---|
| *Your name* | *e.g. Backend* |
| *Team member* | *e.g. Frontend* |

**Supervisor:** *name*

## License

*(Choose one, for example MIT, or write "For academic use only".)*
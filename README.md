# DeliverFlow — Delivery Management System

A full-stack delivery management application built with Spring Boot and Next.js.

## 🌐 Live Demo
- **Frontend:** https://delivery-frontend-fawn.vercel.app
- **API:** https://spring-production-903d.up.railway.app
- **API Docs:** https://spring-production-903d.up.railway.app/swagger-ui/index.html

## 👤 Test Accounts
| Role | Email | Password |
|------|-------|----------|
| Admin | admin@delivery.com | admin123 |
| Register as Customer/Driver/Business | /register | — |

## 🚀 Features

### 3 User Roles
- **Business** — Create delivery requests, track shipments, export CSV, share tracking links
- **Driver** — Browse available jobs, accept deliveries, update status live, track earnings
- **Customer** — Track incoming deliveries, rate drivers, view history

### Core Features
- ✅ JWT Authentication with auto-refresh
- ✅ Public delivery tracking (no login required)
- ✅ Live map tracking with driver location
- ✅ Real-time notifications (in-app + email)
- ✅ PWA — installable on mobile
- ✅ Admin panel with PDF/Excel export
- ✅ Driver earnings breakdown with charts

## 🛠 Tech Stack

### Backend
- Java 17 + Spring Boot 3.5
- Spring Security + JWT
- Spring Data JPA + Hibernate
- PostgreSQL
- Spring Mail (Gmail SMTP)
- Swagger / OpenAPI

### Frontend
- Next.js 14 (App Router)
- Tailwind CSS
- Leaflet.js (maps)
- Axios
- react-hot-toast
- xlsx + jspdf (exports)

## 🏃 Running Locally

### Backend
```bash
# Prerequisites: Java 17, PostgreSQL
cd Spring
cp src/main/resources/application.properties.example src/main/resources/application.properties
# Edit application.properties with your DB credentials
mvnw.cmd spring-boot:run
```

### Frontend
```bash
cd delivery-frontend
cp .env.example .env.local
# Edit .env.local
npm install
npm run dev
```

## 📁 Project Structure
Backend (Spring Boot):
├── controller/     — REST API endpoints
├── service/        — Business logic
├── repository/     — Database access
├── entity/         — JPA entities
├── dto/            — Data transfer objects
├── security/       — JWT + Spring Security
├── mapper/         — Entity ↔ DTO conversion
└── exception/      — Global error handling
Frontend (Next.js):
├── app/
│   ├── business/   — Business dashboard
│   ├── drivers/    — Driver dashboard
│   ├── customers/  — Customer dashboard
│   ├── dashboard/  — Admin panel
│   └── track/      — Public tracking
├── components/     — Shared components
└── lib/            — Axios config

## 🔒 API Endpoints
See full documentation: `/swagger-ui/index.html`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | /api/auth/register | Public | Register |
| POST | /api/auth/login | Public | Login |
| GET | /api/deliveries/track/{code} | Public | Track delivery |
| GET | /api/deliveries/my-business | Business | My deliveries |
| GET | /api/deliveries/available | Driver | Available jobs |
| PATCH | /api/deliveries/{id}/accept | Driver | Accept delivery |
| GET | /api/deliveries/my-deliveries | Customer | My deliveries |
| GET | /api/users/all | Admin | All users |
| GET | /api/deliveries/admin/all | Admin | All deliveries |
Also create delivery-frontend/README.md:
markdown# DeliverFlow Frontend

Next.js frontend for the DeliverFlow delivery management system.

## Setup

```bash
npm install
cp .env.example .env.local
npm run dev
```

## Environment Variables
NEXT_PUBLIC_API_URL=http://localhost:8080

## Live
https://delivery-frontend-fawn.vercel.app
Also create delivery-frontend/.env.example:
NEXT_PUBLIC_API_URL=http://localhost:8080
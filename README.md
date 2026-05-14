# Namma Pustaka
**Smart Library for Every Rural School**
Namma Pustaka is a digital library management system built for rural schools in India. It enables students to d## Features
### For Students
- **No-password login** — sign in with just name, register number, and class
- **Browse & search** — explore the catalog by category or search by title/author
- **Self-borrow/return** — borrow books with one tap; return earns points
- **Reading progress** — track books read, points earned, and leaderboard ranking
- **Profile** — view reading stats and progress toward goals
### For Teachers
- **Book management** — add, edit, and manage book inventory (copies tracking)
- **QR-based issuance** — scan student/book QR codes to issue or return
- **Student management** — view complete student details and reading history
- **Reports** — full transaction history with student and book details
- **Overdue tracking** — identify overdue books at a glance
### For Admins
- **Dashboard** — real-time stats on books, students, teachers, and transactions
- **Full CRUD** — manage books, teachers, and students with cascade-safe deletion
- **Password reset** — reset any teacher's password directly from the console
- **Transaction history** — view all borrow/return activity across the school
## Screenshots
| Splash | Student Home | Book Catalog | Borrowed Books |
|--------|-------------|--------------|----------------|
| | | | |
| Admin Dashboard | Teacher Dashboard | Profile | Reports |
| | | | |
## Tech Stack
| Layer | Technology |
|-------|-----------|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM (Model-View-ViewModel) |
| Database | Room (SQLite) |
| DI | Manual (AppContainer) |
| Build | Gradle KTS + AGP 8.7.2 |
| Minimum SDK | 24 |
| Target SDK | 34 |
## Architecture
```
■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
■ UI Layer (Compose) ■
■ Screens · ViewModels · Navigation ■
■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
■ Repository Layer ■
■ Auth · Book · Student · Review ■
■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
■ Data Layer (Room) ■
■ DAOs · Entities · AppDatabase ■
■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
```
### Key Design Decisions
- **Offline-first**: All data is stored locally via Room. No network dependency — works in areas with limited c- **No Hilt/Dagger**: Manual DI via `AppContainer` avoids build complexity with AGP 9.x while keeping the archi- **Student-first auth**: Students authenticate by name + register number (no email or password needed), making- **Copies-based inventory**: Books are tracked by `copies` and `availableCopies` instead of page count, reflec- **Cascade deletion**: Deleting a book or student automatically removes associated transactions to maintain re## Project Structure
```
app/src/main/java/com/nammapusthakaa/
■■■ data/
■ ■■■ local/
■ ■ ■■■ dao/ # Room DAOs
■ ■ ■■■ entity/ # Room entities
■ ■■■ model/ # Enums (BookCategory, UserRole)
■ ■■■ repository/ # Repository implementations
■■■ di/
■ ■■■ AppContainer.kt # Manual dependency injection
■■■ ui/
■ ■■■ admin/ # Admin screens & ViewModel
■ ■■■ auth/ # Login/Signup screens & ViewModel
■ ■■■ common/ # Shared components & illustrations
■ ■■■ navigation/ # NavGraph & Screen routes
■ ■■■ student/ # Student screens & ViewModel
■ ■■■ teacher/ # Teacher screens & ViewModel
■ ■■■ theme/ # Colors, Typography, Theme
■■■ MainActivity.kt
■■■ NammaPusthakaaApp.kt # Application class
```
## Getting Started
### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 34
### Setup
1. Clone the repository:
 ```bash
 git clone https://github.com/your-username/NammaPusthakaa.git
 ```
2. Open the project in Android Studio.
3. Sync Gradle and let dependencies resolve.
4. Run on an emulator or physical device.
### Default Admin Credentials
- **Email**: admin@nammapustaka.com
- **Password**: admin123
*The admin account is auto-seeded on first launch.*
## Role Workflows
### Student Flow
```
Login (Name + Reg No + Class)
 → Student Home (stats, categories, recent books, quick actions)
 → Book Catalog (browse/filter by category)
 → Book Detail (borrow/return)
 → My Borrowed Books (return books, overdue badges)
 → Leaderboard
 → Profile (stats, reading progress, logout)
```
### Teacher Flow
```
Login (Email + Password)
 → Teacher Dashboard (recent activity, quick actions)
 → Add Book / Manage Books
 → Scan QR (issue/return via QR)
 → Student Management
 → Reports
```
### Admin Flow
```
Login (Email + Password — admin role enforced)
 → Admin Dashboard (stats overview)
 → Management (4 tabs: Books, Teachers, Students, History)
 → Delete entities with cascade-safe transaction cleanup
 → Reset teacher passwords
```
## License
```
MIT License
Copyright (c) 2026 Namma Pustaka
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
## Contributing
Contributions are welcome! Please open an issue or submit a pull request for any improvements.

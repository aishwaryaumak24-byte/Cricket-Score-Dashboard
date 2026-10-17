# CricPulse — Real-Time Cricket Score Management & Simulation Platform

A scalable, real-time cricket scoring platform built with **Spring Boot** and a responsive **React + TypeScript** frontend dashboard. CricPulse processes live cricket match events, computes dynamic run rates and statistics, broadcasts ball-by-ball updates via STOMP WebSockets, and offers a simulation engine with manual umpiring controls.

---

## 🌟 Key Features

### 1. Real-Time Cricket Engine (Spring Boot & STOMP WebSockets)
- **STOMP over WebSocket**: Live match channel `/topic/match/{id}` and general tournament channel `/topic/matches` for instant push updates.
- **Dual-Sync Reliability**: Dual-mode data fetching with WebSocket broadcast and background polling synchronization.
- **Match Simulation Service**: Probabilistic ball-by-ball engine simulating realistic T20/ODI match flows (dots, singles, boundaries, maximums, wickets, wides, and no-balls).
- **Strike & Bowler Rotation**:
  - Automatically alternates batsman strike on odd runs (1s, 3s).
  - Swaps strike at the end of each over.
  - Automatically rotates bowlers at the end of each over (bowlers cannot bowl back-to-back overs).
- **Dynamic Metrics**:
  - Current Run Rate (CRR).
  - Required Run Rate (RRR) during 2nd innings chase.
  - Projected final score.
  - Extras breakdown (wides, no balls, byes, leg byes).

### 2. Rich Domain Architecture & Persistence
- **Matches**: T20, ODI, and Test matches with status transitions (`SCHEDULED`, `LIVE`, `INNINGS_BREAK`, `COMPLETED`).
- **Innings**: Accurate legal balls count, over formatting (e.g. `16.4`), target calculation, and innings conclusions.
- **Scorecards**: Comprehensive batting scorecard (runs, balls faced, 4s, 6s, strike rate, dismissal text) and bowling scorecard (overs, maidens, runs conceded, wickets, economy).
- **Ball Events & Commentary**: Rich, realistic broadcast commentary for every ball with ball badges (`4`, `6`, `W`, `1wd`, `0`).
- **In-Memory H2 Database**: Pre-seeded with realistic international teams (India, Australia, England, South Africa, Pakistan, New Zealand) and star players.

### 3. Responsive Sports Dashboard (React + TypeScript)
- **Stadium Hero Scorecard**:
  - Live broadcast score widget with team flags, color palettes, bold live score typography, and target equations.
  - Recent balls in over carousel (`[ 1 | 4 | 0 | 6 | W | 1 ]`).
  - Active batting pair (striker highlighted with 🏏, non-striker) and active bowler spell figures.
- **Simulation Control Bar**:
  - Auto-play / pause toggle.
  - Speed selector: `1s (Turbo)`, `2.5s (Standard)`, `4s (Slow)`.
  - "Next Ball" single-step manual simulation.
  - "Manual Scorer" umpire pad to record custom runs, extras, or wickets.
  - "Reset Match" button to re-run from ball one.
- **Tabbed Views**:
  - **Live Center & Commentary**: Real-time ball-by-ball timeline filterable by (All, Boundaries, Wickets).
  - **Full Scorecard**: Complete batting and bowling tables for Innings 1 and Innings 2.
  - **Match Summary & Analytics**: Winner badge, Player of the Match, head-to-head comparison, and over-by-over worm progression chart.
  - **Tournament Leaderboard**: Orange Cap (Top Run Scorers) and Purple Cap (Leading Wicket Takers).
- **Create Match Modal**: Custom match creation with team selection, overs (5, 10, 20, 50), toss winner, toss decision, and venue.

---

## 🏗️ Architecture & Tech Stack

```
cricketscore-30/
├── backend/                             # Spring Boot 3 Java Application
│   ├── src/main/java/com/cricketscore/
│   │   ├── config/                      # WebSocketConfig, WebMvcConfig, DataInitializer
│   │   ├── controller/                  # MatchController, SimulationController, PlayerController, TeamController
│   │   ├── dto/                         # LiveMatchUpdateDto, ScorecardResponseDto, MatchSummaryDto, etc.
│   │   ├── model/                       # Match, Innings, Team, Player, BattingScorecard, BowlingScorecard, BallEvent
│   │   ├── repository/                  # Spring Data JPA Repositories
│   │   └── service/                     # MatchSimulationService, MatchService, PlayerService, LiveScorePublisher
│   ├── src/main/resources/
│   │   ├── application.properties       # H2 DB, WebSocket, and simulation properties
│   │   └── static/                      # Production bundled React frontend assets
│   ├── pom.xml                          # Maven build dependencies
│   ├── mvnw & mvnw.cmd                  # Maven Wrapper
├── frontend/                            # React + TypeScript Frontend
│   ├── src/
│   │   ├── components/                  # Navbar, HeroScorecard, SimulationControlBar, LiveCommentaryFeed, etc.
│   │   ├── services/                    # api.ts (REST client), websocket.ts (STOMP client)
│   │   ├── types/                       # cricket.ts (Strict TypeScript definitions)
│   │   ├── App.tsx                      # Main dashboard controller
│   │   ├── index.css                    # Sports broadcast stylesheet & animations
│   │   └── main.tsx
│   ├── vite.config.ts                   # Vite config with backend proxy
│   └── package.json
```

---

## 🚀 Running the Application

### Option A: Unified Spring Boot Server (Single Port 8080)
The React TypeScript frontend has been compiled and bundled directly into Spring Boot's static resources:
```bash
# In backend directory
java -jar target/cricket-score-service-1.0.0.jar
```
Or with the Maven wrapper:
```bash
cd backend
./mvnw spring-boot:run
```
Open **`http://localhost:8080/`** in your browser!

### Option B: Concurrent Vite Development Mode
Run both servers concurrently for development and hot module replacement:
1. **Start Backend**:
   ```bash
   cd backend
   java -jar target/cricket-score-service-1.0.0.jar
   ```
2. **Start Frontend Dev Server**:
   ```bash
   cd frontend
   npm run dev
   ```
Open **`http://localhost:5173/`** in your browser. All API requests and WebSocket connections are automatically proxied to the backend at `http://localhost:8080`.

---

## 📡 REST API Reference

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/matches` | Get all matches (optional `?status=LIVE`) |
| `GET` | `/api/matches/{id}` | Get match by ID |
| `GET` | `/api/matches/{id}/live` | Get live score update snapshot |
| `GET` | `/api/matches/{id}/scorecard` | Get full detailed scorecard (Innings 1 & 2) |
| `GET` | `/api/matches/{id}/summary` | Get match summary, result, and over progression |
| `GET` | `/api/matches/{id}/commentary` | Get ball-by-ball commentary (`?page=0&size=30`) |
| `POST` | `/api/matches` | Create a new cricket match |
| `POST` | `/api/matches/{id}/reset` | Reset match back to ball 1 |
| `POST` | `/api/matches/{id}/start` | Start auto-simulation (`?speed=2500`) |
| `POST` | `/api/matches/{id}/pause` | Pause auto-simulation |
| `POST` | `/api/matches/{id}/step` | Simulate next single ball |
| `POST` | `/api/matches/{id}/score-ball` | Record manual ball scoring |
| `GET` | `/api/players/leaderboard` | Get Orange & Purple cap leaderboard |
| `GET` | `/api/teams` | Get all teams |
| `GET` | `/h2-console` | H2 Web Console (`jdbc:h2:mem:cricketdb`) |

---

## ⚡ WebSocket / STOMP Topics

- **Endpoint**: `/ws-cricket` (supports both SockJS and raw WebSockets)
- **Match Channel**: `/topic/match/{matchId}` — Publishes `LiveMatchUpdateDto` on every ball bowled.
- **Tournament Channel**: `/topic/matches` — Broadcasts real-time events to tournament dashboards.

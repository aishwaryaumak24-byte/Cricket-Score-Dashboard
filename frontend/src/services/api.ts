import {
  Match,
  LiveMatchUpdate,
  ScorecardResponse,
  MatchSummary,
  BallEvent,
  ScoreBallPayload,
  CreateMatchPayload,
  Team,
  Player
} from '../types/cricket';

const API_BASE = '/api';

export const cricketApi = {
  async fetchMatches(status?: string): Promise<Match[]> {
    const url = status ? `${API_BASE}/matches?status=${status}` : `${API_BASE}/matches`;
    const res = await fetch(url);
    if (!res.ok) throw new Error(`Failed to fetch matches: ${res.statusText}`);
    return res.json();
  },

  async fetchMatchById(id: number): Promise<Match> {
    const res = await fetch(`${API_BASE}/matches/${id}`);
    if (!res.ok) throw new Error(`Failed to fetch match ${id}`);
    return res.json();
  },

  async fetchLiveMatchUpdate(id: number): Promise<LiveMatchUpdate> {
    const res = await fetch(`${API_BASE}/matches/${id}/live`);
    if (!res.ok) throw new Error(`Failed to fetch live score for match ${id}`);
    return res.json();
  },

  async fetchScorecard(id: number): Promise<ScorecardResponse> {
    const res = await fetch(`${API_BASE}/matches/${id}/scorecard`);
    if (!res.ok) throw new Error(`Failed to fetch scorecard for match ${id}`);
    return res.json();
  },

  async fetchSummary(id: number): Promise<MatchSummary> {
    const res = await fetch(`${API_BASE}/matches/${id}/summary`);
    if (!res.ok) throw new Error(`Failed to fetch summary for match ${id}`);
    return res.json();
  },

  async fetchCommentary(id: number, page = 0, size = 30): Promise<BallEvent[]> {
    const res = await fetch(`${API_BASE}/matches/${id}/commentary?page=${page}&size=${size}`);
    if (!res.ok) throw new Error(`Failed to fetch commentary for match ${id}`);
    return res.json();
  },

  async startSimulation(id: number, speedMs = 2500): Promise<void> {
    const res = await fetch(`${API_BASE}/matches/${id}/start?speed=${speedMs}`, {
      method: 'POST',
    });
    if (!res.ok) throw new Error(`Failed to start simulation`);
  },

  async pauseSimulation(id: number): Promise<void> {
    const res = await fetch(`${API_BASE}/matches/${id}/pause`, {
      method: 'POST',
    });
    if (!res.ok) throw new Error(`Failed to pause simulation`);
  },

  async simulateStep(id: number): Promise<LiveMatchUpdate> {
    const res = await fetch(`${API_BASE}/matches/${id}/step`, {
      method: 'POST',
    });
    if (!res.ok) throw new Error(`Failed to step simulation`);
    return res.json();
  },

  async scoreBall(id: number, payload: ScoreBallPayload): Promise<LiveMatchUpdate> {
    const res = await fetch(`${API_BASE}/matches/${id}/score-ball`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    });
    if (!res.ok) throw new Error(`Failed to record ball`);
    return res.json();
  },

  async resetMatch(id: number): Promise<void> {
    const res = await fetch(`${API_BASE}/matches/${id}/reset`, {
      method: 'POST',
    });
    if (!res.ok) throw new Error(`Failed to reset match`);
  },

  async createMatch(payload: CreateMatchPayload): Promise<Match> {
    const res = await fetch(`${API_BASE}/matches`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    });
    if (!res.ok) throw new Error(`Failed to create match`);
    return res.json();
  },

  async fetchTeams(): Promise<Team[]> {
    const res = await fetch(`${API_BASE}/teams`);
    if (!res.ok) throw new Error(`Failed to fetch teams`);
    return res.json();
  },

  async fetchLeaderboard(): Promise<{ topRunScorers: Player[]; topWicketTakers: Player[] }> {
    const res = await fetch(`${API_BASE}/players/leaderboard`);
    if (!res.ok) throw new Error(`Failed to fetch leaderboard`);
    return res.json();
  },

  async fetchPlayers(): Promise<Player[]> {
    const res = await fetch(`${API_BASE}/players`);
    if (!res.ok) throw new Error(`Failed to fetch players`);
    return res.json();
  },
};

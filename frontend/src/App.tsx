import React, { useEffect, useState, useCallback } from 'react';
import {
  Match,
  LiveMatchUpdate,
  ScorecardResponse,
  MatchSummary,
  BallEvent,
  ScoreBallPayload,
  CreateMatchPayload,
  Team,
} from './types/cricket';
import { cricketApi } from './services/api';
import { wsService } from './services/websocket';
import { Navbar } from './components/Navbar';
import { HeroScorecard } from './components/HeroScorecard';
import { SimulationControlBar } from './components/SimulationControlBar';
import { LiveCommentaryFeed } from './components/LiveCommentaryFeed';
import { ScorecardTab } from './components/ScorecardTab';
import { MatchSummaryTab } from './components/MatchSummaryTab';
import { LeaderboardTab } from './components/LeaderboardTab';
import { ManualScoreModal } from './components/ManualScoreModal';
import { CreateMatchModal } from './components/CreateMatchModal';
import { Radio, FileText, BarChart3, Trophy, Calendar } from 'lucide-react';

export const App: React.FC = () => {
  const [matches, setMatches] = useState<Match[]>([]);
  const [selectedMatchId, setSelectedMatchId] = useState<number | null>(null);
  const [liveScore, setLiveScore] = useState<LiveMatchUpdate | null>(null);
  const [scorecard, setScorecard] = useState<ScorecardResponse | null>(null);
  const [summary, setSummary] = useState<MatchSummary | null>(null);
  const [commentary, setCommentary] = useState<BallEvent[]>([]);
  const [teams, setTeams] = useState<Team[]>([]);

  const [activeTab, setActiveTab] = useState<'live' | 'scorecard' | 'summary' | 'leaderboard'>('live');
  const [isManualModalOpen, setIsManualModalOpen] = useState<boolean>(false);
  const [isCreateModalOpen, setIsCreateModalOpen] = useState<boolean>(false);
  const [isWsConnected, setIsWsConnected] = useState<boolean>(true);
  const [loading, setLoading] = useState<boolean>(false);

  const selectedMatch = matches.find((m) => m.id === selectedMatchId) || null;
  const isSimulating = liveScore?.isSimulating || selectedMatch?.isSimulating || false;
  const isCompleted = liveScore?.status === 'COMPLETED' || selectedMatch?.status === 'COMPLETED';

  // Load matches & teams initial
  const loadInitialData = useCallback(async () => {
    try {
      setLoading(true);
      const [matchesRes, teamsRes] = await Promise.all([
        cricketApi.fetchMatches(),
        cricketApi.fetchTeams(),
      ]);
      setMatches(matchesRes);
      setTeams(teamsRes);

      if (matchesRes.length > 0 && selectedMatchId === null) {
        // Default to first match (or first live match)
        const liveOne = matchesRes.find((m) => m.status === 'LIVE') || matchesRes[0];
        setSelectedMatchId(liveOne.id);
      }
    } catch (err) {
      console.error('Error loading initial match data:', err);
    } finally {
      setLoading(false);
    }
  }, [selectedMatchId]);

  useEffect(() => {
    loadInitialData();
  }, [loadInitialData]);

  // Load detailed match data when selectedMatchId changes
  const loadMatchDetails = useCallback(async (matchId: number) => {
    try {
      const [liveRes, scorecardRes, summaryRes, commRes] = await Promise.all([
        cricketApi.fetchLiveMatchUpdate(matchId).catch(() => null),
        cricketApi.fetchScorecard(matchId).catch(() => null),
        cricketApi.fetchSummary(matchId).catch(() => null),
        cricketApi.fetchCommentary(matchId, 0, 40).catch(() => []),
      ]);

      if (liveRes) setLiveScore(liveRes);
      if (scorecardRes) setScorecard(scorecardRes);
      if (summaryRes) setSummary(summaryRes);
      if (commRes) setCommentary(commRes);
    } catch (err) {
      console.error('Failed to load match details:', err);
    }
  }, []);

  useEffect(() => {
    if (selectedMatchId !== null) {
      loadMatchDetails(selectedMatchId);

      // Subscribe to WebSocket updates for selected match
      wsService.subscribeToMatch(selectedMatchId, (update) => {
        setLiveScore(update);

        // Refresh commentary and scorecard in background
        cricketApi.fetchCommentary(selectedMatchId, 0, 40).then(setCommentary).catch(() => {});
        cricketApi.fetchScorecard(selectedMatchId).then(setScorecard).catch(() => {});
        if (update.status === 'COMPLETED') {
          cricketApi.fetchSummary(selectedMatchId).then(setSummary).catch(() => {});
          // Update match in list
          setMatches((prev) =>
            prev.map((m) => (m.id === update.matchId ? { ...m, status: 'COMPLETED', winMargin: update.winMargin } : m))
          );
        }
      });
    }
  }, [selectedMatchId, loadMatchDetails]);

  // Polling fallback to ensure dual reliability
  useEffect(() => {
    if (!selectedMatchId) return;

    const interval = setInterval(() => {
      if (selectedMatch?.status === 'LIVE' || isSimulating) {
        cricketApi.fetchLiveMatchUpdate(selectedMatchId)
          .then((live) => {
            setLiveScore(live);
          })
          .catch(() => {});
      }
    }, 2500);

    return () => clearInterval(interval);
  }, [selectedMatchId, selectedMatch?.status, isSimulating]);

  // Actions
  const handleToggleSimulation = async (speedMs: number) => {
    if (!selectedMatchId) return;
    try {
      if (isSimulating) {
        await cricketApi.pauseSimulation(selectedMatchId);
        setLiveScore((prev) => prev ? { ...prev, isSimulating: false } : null);
      } else {
        await cricketApi.startSimulation(selectedMatchId, speedMs);
        setLiveScore((prev) => prev ? { ...prev, isSimulating: true } : null);
      }
      // Refresh match list status
      const updatedMatches = await cricketApi.fetchMatches();
      setMatches(updatedMatches);
    } catch (err) {
      console.error('Toggle simulation error:', err);
    }
  };

  const handleSimulateStep = async () => {
    if (!selectedMatchId) return;
    try {
      const update = await cricketApi.simulateStep(selectedMatchId);
      setLiveScore(update);
      loadMatchDetails(selectedMatchId);
    } catch (err) {
      console.error('Simulate step error:', err);
    }
  };

  const handleScoreBall = async (payload: ScoreBallPayload) => {
    if (!selectedMatchId) return;
    try {
      const update = await cricketApi.scoreBall(selectedMatchId, payload);
      setLiveScore(update);
      loadMatchDetails(selectedMatchId);
    } catch (err) {
      console.error('Manual score ball error:', err);
    }
  };

  const handleResetMatch = async () => {
    if (!selectedMatchId) return;
    try {
      await cricketApi.resetMatch(selectedMatchId);
      loadMatchDetails(selectedMatchId);
      const updatedMatches = await cricketApi.fetchMatches();
      setMatches(updatedMatches);
    } catch (err) {
      console.error('Reset match error:', err);
    }
  };

  const handleCreateMatch = async (payload: CreateMatchPayload) => {
    try {
      const newMatch = await cricketApi.createMatch(payload);
      const updatedMatches = await cricketApi.fetchMatches();
      setMatches(updatedMatches);
      setSelectedMatchId(newMatch.id);
      setActiveTab('live');
    } catch (err) {
      console.error('Create match error:', err);
    }
  };

  return (
    <div style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column' }}>
      {/* Top Navbar */}
      <Navbar
        matches={matches}
        selectedMatchId={selectedMatchId}
        onSelectMatch={(id) => {
          setSelectedMatchId(id);
          setActiveTab('live');
        }}
        onOpenCreateModal={() => setIsCreateModalOpen(true)}
        activeTab={activeTab}
        setActiveTab={setActiveTab}
        isWsConnected={isWsConnected}
        onRefresh={() => selectedMatchId && loadMatchDetails(selectedMatchId)}
        loading={loading}
      />

      {/* Main Content Area */}
      <main className="container" style={{ flex: 1, padding: '1.5rem 1rem', display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
        {/* Navigation Tabs Header */}
        <div style={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          borderBottom: '1px solid var(--border-color)',
          paddingBottom: '0.75rem',
          flexWrap: 'wrap',
          gap: '0.75rem',
        }}>
          {/* Main Tabs */}
          <div style={{ display: 'flex', gap: '0.5rem', flexWrap: 'wrap' }}>
            <button
              onClick={() => setActiveTab('live')}
              className={`btn ${activeTab === 'live' ? 'btn-primary' : 'btn-secondary'}`}
            >
              <Radio size={16} />
              <span>Live Center & Commentary</span>
            </button>

            <button
              onClick={() => setActiveTab('scorecard')}
              className={`btn ${activeTab === 'scorecard' ? 'btn-primary' : 'btn-secondary'}`}
            >
              <FileText size={16} />
              <span>Full Scorecard</span>
            </button>

            <button
              onClick={() => setActiveTab('summary')}
              className={`btn ${activeTab === 'summary' ? 'btn-primary' : 'btn-secondary'}`}
            >
              <BarChart3 size={16} />
              <span>Match Summary & Analytics</span>
            </button>

            <button
              onClick={() => setActiveTab('leaderboard')}
              className={`btn ${activeTab === 'leaderboard' ? 'btn-primary' : 'btn-secondary'}`}
            >
              <Trophy size={16} />
              <span>Tournament Leaderboard</span>
            </button>
          </div>

          {/* Quick Matches Counter */}
          <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <Calendar size={14} />
            <span>{matches.length} Matches in Tournament</span>
          </div>
        </div>

        {/* Tab 1: Live Center */}
        {activeTab === 'live' && (
          <div style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
            {/* Stadium Hero Scorecard */}
            <HeroScorecard liveScore={liveScore} match={selectedMatch} />

            {/* Simulation Engine Controls */}
            <SimulationControlBar
              isSimulating={isSimulating}
              onToggleSimulation={handleToggleSimulation}
              onSimulateStep={handleSimulateStep}
              onOpenManualModal={() => setIsManualModalOpen(true)}
              onResetMatch={handleResetMatch}
              matchCompleted={isCompleted}
            />

            {/* Live Commentary Feed */}
            <LiveCommentaryFeed commentary={commentary} />
          </div>
        )}

        {/* Tab 2: Full Scorecard */}
        {activeTab === 'scorecard' && (
          <div style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
            <HeroScorecard liveScore={liveScore} match={selectedMatch} />
            <ScorecardTab scorecard={scorecard} />
          </div>
        )}

        {/* Tab 3: Match Summary & Analytics */}
        {activeTab === 'summary' && (
          <div style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
            <MatchSummaryTab summary={summary} />
          </div>
        )}

        {/* Tab 4: Tournament Leaderboard */}
        {activeTab === 'leaderboard' && (
          <div style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
            <LeaderboardTab />
          </div>
        )}
      </main>

      {/* Footer */}
      <footer style={{
        backgroundColor: 'var(--bg-secondary)',
        borderTop: '1px solid var(--border-color)',
        padding: '1.25rem 0',
        marginTop: '2rem',
        textAlign: 'center',
        fontSize: '0.8rem',
        color: 'var(--text-muted)',
      }}>
        <div className="container">
          <p>CricPulse • Scalable Real-Time Cricket Scoring Platform powered by Spring Boot & React TypeScript</p>
          <p style={{ marginTop: '0.25rem', fontSize: '0.75rem' }}>
            STOMP WebSockets • In-Memory H2 DB • Ball-by-Ball Simulation Engine • REST APIs
          </p>
        </div>
      </footer>

      {/* Modals */}
      <ManualScoreModal
        isOpen={isManualModalOpen}
        onClose={() => setIsManualModalOpen(false)}
        onSubmit={handleScoreBall}
      />

      <CreateMatchModal
        isOpen={isCreateModalOpen}
        onClose={() => setIsCreateModalOpen(false)}
        teams={teams}
        onSubmit={handleCreateMatch}
      />
    </div>
  );
};

export default App;

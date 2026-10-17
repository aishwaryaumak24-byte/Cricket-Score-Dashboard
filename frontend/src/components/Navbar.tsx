import React from 'react';
import { Activity, Radio, PlusCircle, Trophy, RefreshCw } from 'lucide-react';
import { Match } from '../types/cricket';

interface NavbarProps {
  matches: Match[];
  selectedMatchId: number | null;
  onSelectMatch: (id: number) => void;
  onOpenCreateModal: () => void;
  activeTab: 'live' | 'scorecard' | 'summary' | 'leaderboard';
  setActiveTab: (tab: 'live' | 'scorecard' | 'summary' | 'leaderboard') => void;
  isWsConnected: boolean;
  onRefresh: () => void;
  loading: boolean;
}

export const Navbar: React.FC<NavbarProps> = ({
  matches,
  selectedMatchId,
  onSelectMatch,
  onOpenCreateModal,
  activeTab,
  setActiveTab,
  isWsConnected,
  onRefresh,
  loading,
}) => {
  return (
    <header style={{
      backgroundColor: 'var(--bg-secondary)',
      borderBottom: '1px solid var(--border-color)',
      position: 'sticky',
      top: 0,
      zIndex: 100,
      backdropFilter: 'blur(8px)',
    }}>
      <div className="container" style={{
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        height: '68px',
        gap: '1rem',
      }}>
        {/* Brand */}
        <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', cursor: 'pointer' }}
             onClick={() => setActiveTab('live')}>
          <div style={{
            background: 'linear-gradient(135deg, #2563eb, #06b6d4)',
            width: '40px',
            height: '40px',
            borderRadius: '10px',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            boxShadow: '0 0 16px rgba(37, 99, 235, 0.4)',
          }}>
            <Radio size={22} color="#ffffff" />
          </div>
          <div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <span style={{ fontWeight: 800, fontSize: '1.25rem', letterSpacing: '-0.02em' }}>
                CRIC<span style={{ color: '#38bdf8' }}>PULSE</span>
              </span>
              <span className="badge badge-live" style={{ fontSize: '0.65rem', padding: '0.15rem 0.45rem' }}>
                <span className="live-indicator" style={{ width: '6px', height: '6px' }} /> LIVE
              </span>
            </div>
            <p style={{ fontSize: '0.7rem', color: 'var(--text-muted)' }}>
              Real-Time Score Engine & Simulation
            </p>
          </div>
        </div>

        {/* Center: Match Switcher */}
        <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', flex: 1, maxWidth: '420px' }}>
          <select
            value={selectedMatchId || ''}
            onChange={(e) => onSelectMatch(Number(e.target.value))}
            style={{
              width: '100%',
              backgroundColor: 'var(--bg-card)',
              color: 'var(--text-primary)',
              border: '1px solid var(--border-color)',
              borderRadius: '8px',
              padding: '0.5rem 0.85rem',
              fontSize: '0.875rem',
              fontWeight: 500,
              cursor: 'pointer',
              outline: 'none',
            }}
          >
            {matches.map((m) => (
              <option key={m.id} value={m.id}>
                {m.status === 'LIVE' ? '🔴 ' : m.status === 'COMPLETED' ? '🏆 ' : '⏳ '}
                {m.title} ({m.matchType})
              </option>
            ))}
          </select>
        </div>

        {/* Actions & Status */}
        <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
          {/* Refresh button */}
          <button
            onClick={onRefresh}
            className="btn btn-secondary"
            style={{ padding: '0.5rem' }}
            title="Refresh Match Data"
          >
            <RefreshCw size={16} className={loading ? 'spin-slow' : ''} />
          </button>

          {/* Leaderboard button */}
          <button
            onClick={() => setActiveTab('leaderboard')}
            className={`btn ${activeTab === 'leaderboard' ? 'btn-primary' : 'btn-secondary'}`}
          >
            <Trophy size={16} />
            <span>Leaderboard</span>
          </button>

          {/* Create Match */}
          <button
            onClick={onOpenCreateModal}
            className="btn btn-primary"
          >
            <PlusCircle size={16} />
            <span>New Match</span>
          </button>

          {/* WebSocket Status Indicator */}
          <div
            title={isWsConnected ? 'WebSocket Live: Real-time broadcast active' : 'WebSocket: Auto-reconnecting'}
            style={{
              display: 'flex',
              alignItems: 'center',
              gap: '0.4rem',
              fontSize: '0.75rem',
              fontWeight: 600,
              color: isWsConnected ? '#34d399' : '#f59e0b',
              backgroundColor: isWsConnected ? 'rgba(16, 185, 129, 0.1)' : 'rgba(245, 158, 11, 0.1)',
              padding: '0.35rem 0.65rem',
              borderRadius: '6px',
              border: `1px solid ${isWsConnected ? 'rgba(16, 185, 129, 0.3)' : 'rgba(245, 158, 11, 0.3)'}`,
            }}
          >
            <Activity size={12} className={isWsConnected ? '' : 'pulse-glow'} />
            <span>{isWsConnected ? 'WS LIVE' : 'SYNCING'}</span>
          </div>
        </div>
      </div>
    </header>
  );
};

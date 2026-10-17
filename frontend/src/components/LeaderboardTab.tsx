import React, { useEffect, useState } from 'react';
import { Player } from '../types/cricket';
import { cricketApi } from '../services/api';
import { Trophy, Award } from 'lucide-react';

export const LeaderboardTab: React.FC = () => {
  const [data, setData] = useState<{ topRunScorers: Player[]; topWicketTakers: Player[] } | null>(null);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    cricketApi.fetchLeaderboard()
      .then((res) => setData(res))
      .catch((err) => console.error('Failed to load leaderboard', err))
      .finally(() => setLoading(false));
  }, []);

  if (loading) {
    return (
      <div className="glass-card" style={{ padding: '3rem', textAlign: 'center', color: 'var(--text-secondary)' }}>
        Loading tournament leaderboard...
      </div>
    );
  }

  return (
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(360px, 1fr))', gap: '1.5rem' }}>
      {/* Orange Cap: Top Run Scorers */}
      <div className="glass-card" style={{ padding: '1.5rem', borderTop: '4px solid #f97316' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', marginBottom: '1.25rem' }}>
          <div style={{
            width: '36px',
            height: '36px',
            borderRadius: '8px',
            backgroundColor: '#ea580c',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
          }}>
            <Trophy size={18} color="#ffffff" />
          </div>
          <div>
            <h3 style={{ fontSize: '1.1rem', fontWeight: 800 }}>Top Run Scorers</h3>
            <p style={{ fontSize: '0.75rem', color: '#94a3b8' }}>Leading batsmen of the tournament</p>
          </div>
        </div>

        <div style={{ overflowX: 'auto' }}>
          <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left', fontSize: '0.85rem' }}>
            <thead>
              <tr style={{ backgroundColor: '#111827', color: 'var(--text-muted)' }}>
                <th style={{ padding: '0.6rem 0.75rem' }}>#</th>
                <th style={{ padding: '0.6rem 0.75rem' }}>Batter</th>
                <th style={{ padding: '0.6rem 0.5rem', textAlign: 'right' }}>Runs</th>
                <th style={{ padding: '0.6rem 0.5rem', textAlign: 'right' }}>HS</th>
                <th style={{ padding: '0.6rem 0.5rem', textAlign: 'right' }}>100s</th>
                <th style={{ padding: '0.6rem 0.5rem', textAlign: 'right' }}>50s</th>
                <th style={{ padding: '0.6rem 0.75rem', textAlign: 'right' }}>SR</th>
              </tr>
            </thead>
            <tbody>
              {data?.topRunScorers.map((p, idx) => (
                <tr key={p.id} style={{ borderBottom: '1px solid var(--border-color)' }}>
                  <td style={{ padding: '0.6rem 0.75rem', fontWeight: 700, color: idx === 0 ? '#f97316' : '#94a3b8' }}>
                    {idx + 1}
                  </td>
                  <td style={{ padding: '0.6rem 0.75rem' }}>
                    <div style={{ fontWeight: 700, color: '#ffffff' }}>{p.name}</div>
                    <div style={{ fontSize: '0.7rem', color: '#94a3b8' }}>{p.teamName}</div>
                  </td>
                  <td style={{ padding: '0.6rem 0.5rem', textAlign: 'right', fontWeight: 800, color: '#f97316' }}>
                    {p.totalRuns}
                  </td>
                  <td style={{ padding: '0.6rem 0.5rem', textAlign: 'right', color: '#cbd5e1' }}>
                    {p.highestScore}
                  </td>
                  <td style={{ padding: '0.6rem 0.5rem', textAlign: 'right', color: '#cbd5e1' }}>
                    {p.centuries}
                  </td>
                  <td style={{ padding: '0.6rem 0.5rem', textAlign: 'right', color: '#cbd5e1' }}>
                    {p.halfCenturies}
                  </td>
                  <td style={{ padding: '0.6rem 0.75rem', textAlign: 'right', color: '#38bdf8', fontWeight: 600 }}>
                    {p.careerStrikeRate}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Purple Cap: Top Wicket Takers */}
      <div className="glass-card" style={{ padding: '1.5rem', borderTop: '4px solid #a855f7' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', marginBottom: '1.25rem' }}>
          <div style={{
            width: '36px',
            height: '36px',
            borderRadius: '8px',
            backgroundColor: '#9333ea',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
          }}>
            <Award size={18} color="#ffffff" />
          </div>
          <div>
            <h3 style={{ fontSize: '1.1rem', fontWeight: 800 }}>Leading Wicket Takers</h3>
            <p style={{ fontSize: '0.75rem', color: '#94a3b8' }}>Most wickets in the tournament</p>
          </div>
        </div>

        <div style={{ overflowX: 'auto' }}>
          <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left', fontSize: '0.85rem' }}>
            <thead>
              <tr style={{ backgroundColor: '#111827', color: 'var(--text-muted)' }}>
                <th style={{ padding: '0.6rem 0.75rem' }}>#</th>
                <th style={{ padding: '0.6rem 0.75rem' }}>Bowler</th>
                <th style={{ padding: '0.6rem 0.5rem', textAlign: 'right' }}>Wkts</th>
                <th style={{ padding: '0.6rem 0.5rem', textAlign: 'right' }}>Mat</th>
                <th style={{ padding: '0.6rem 0.5rem', textAlign: 'right' }}>BBF</th>
                <th style={{ padding: '0.6rem 0.75rem', textAlign: 'right' }}>Econ</th>
              </tr>
            </thead>
            <tbody>
              {data?.topWicketTakers.map((p, idx) => (
                <tr key={p.id} style={{ borderBottom: '1px solid var(--border-color)' }}>
                  <td style={{ padding: '0.6rem 0.75rem', fontWeight: 700, color: idx === 0 ? '#a855f7' : '#94a3b8' }}>
                    {idx + 1}
                  </td>
                  <td style={{ padding: '0.6rem 0.75rem' }}>
                    <div style={{ fontWeight: 700, color: '#ffffff' }}>{p.name}</div>
                    <div style={{ fontSize: '0.7rem', color: '#94a3b8' }}>{p.teamName}</div>
                  </td>
                  <td style={{ padding: '0.6rem 0.5rem', textAlign: 'right', fontWeight: 800, color: '#a855f7' }}>
                    {p.totalWickets}
                  </td>
                  <td style={{ padding: '0.6rem 0.5rem', textAlign: 'right', color: '#cbd5e1' }}>
                    {p.matchesPlayed}
                  </td>
                  <td style={{ padding: '0.6rem 0.5rem', textAlign: 'right', color: '#34d399' }}>
                    {p.bestBowlingFigures || '-'}
                  </td>
                  <td style={{ padding: '0.6rem 0.75rem', textAlign: 'right', color: '#38bdf8', fontWeight: 600 }}>
                    {p.careerEconomy}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

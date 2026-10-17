import React from 'react';
import { MatchSummary } from '../types/cricket';
import { Trophy, Award, BarChart2 } from 'lucide-react';

interface MatchSummaryTabProps {
  summary: MatchSummary | null;
}

export const MatchSummaryTab: React.FC<MatchSummaryTabProps> = ({ summary }) => {
  if (!summary) {
    return (
      <div className="glass-card" style={{ padding: '3rem', textAlign: 'center', color: 'var(--text-secondary)' }}>
        No summary available yet.
      </div>
    );
  }

  // Calculate highest runs for chart scaling
  const maxRuns = Math.max(
    ...summary.innings1Progression.map((p) => p.cumulativeRuns),
    ...summary.innings2Progression.map((p) => p.cumulativeRuns),
    200
  );

  return (
    <div className="glass-card" style={{ padding: '1.5rem', display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
      {/* Result Banner */}
      <div style={{
        padding: '1.25rem 1.5rem',
        background: 'linear-gradient(135deg, rgba(37, 99, 235, 0.2), rgba(6, 182, 212, 0.2))',
        border: '1px solid rgba(59, 130, 246, 0.4)',
        borderRadius: '12px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        flexWrap: 'wrap',
        gap: '1rem',
      }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
          <div style={{
            width: '44px',
            height: '44px',
            borderRadius: '50%',
            backgroundColor: '#f59e0b',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            boxShadow: '0 0 16px rgba(245, 158, 11, 0.4)',
          }}>
            <Trophy size={22} color="#000000" />
          </div>
          <div>
            <h3 style={{ fontSize: '1.25rem', fontWeight: 800, color: '#ffffff' }}>
              {summary.resultText || 'Match in Progress'}
            </h3>
            <p style={{ fontSize: '0.85rem', color: '#94a3b8' }}>
              {summary.title}
            </p>
          </div>
        </div>

        {summary.manOfTheMatch && (
          <div style={{
            display: 'flex',
            alignItems: 'center',
            gap: '0.5rem',
            padding: '0.5rem 1rem',
            backgroundColor: 'rgba(245, 158, 11, 0.15)',
            border: '1px solid rgba(245, 158, 11, 0.4)',
            borderRadius: '8px',
          }}>
            <Award size={18} color="#f59e0b" />
            <div>
              <div style={{ fontSize: '0.7rem', color: '#f59e0b', fontWeight: 700, textTransform: 'uppercase' }}>
                Player of the Match
              </div>
              <div style={{ fontSize: '0.95rem', fontWeight: 800, color: '#ffffff' }}>
                {summary.manOfTheMatch}
              </div>
            </div>
          </div>
        )}
      </div>

      {/* Head-to-Head Innings Overview */}
      <div style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))',
        gap: '1rem',
      }}>
        {/* Team 1 Card */}
        <div style={{
          backgroundColor: '#111827',
          padding: '1.25rem',
          borderRadius: '10px',
          border: '1px solid var(--border-color)',
        }}>
          <div style={{ fontSize: '1.1rem', fontWeight: 700, color: '#38bdf8' }}>
            {summary.team1Name}
          </div>
          <div style={{ fontSize: '2rem', fontWeight: 900, color: '#ffffff', margin: '0.35rem 0' }}>
            {summary.team1Score}{' '}
            <span style={{ fontSize: '1rem', color: '#94a3b8', fontWeight: 600 }}>
              ({summary.team1Overs} ov)
            </span>
          </div>

          <div style={{ marginTop: '1rem', display: 'flex', flexDirection: 'column', gap: '0.5rem', fontSize: '0.85rem' }}>
            {summary.topBatter1 && (
              <div>
                <span style={{ color: 'var(--text-muted)' }}>Top Batter: </span>
                <strong style={{ color: '#ffffff' }}>{summary.topBatter1}</strong>{' '}
                <span style={{ color: '#c084fc' }}>{summary.topBatter1Stats}</span>
              </div>
            )}
            {summary.topBowler1 && (
              <div>
                <span style={{ color: 'var(--text-muted)' }}>Top Bowler: </span>
                <strong style={{ color: '#ffffff' }}>{summary.topBowler1}</strong>{' '}
                <span style={{ color: '#34d399' }}>{summary.topBowler1Stats}</span>
              </div>
            )}
          </div>
        </div>

        {/* Team 2 Card */}
        {summary.team2Name && (
          <div style={{
            backgroundColor: '#111827',
            padding: '1.25rem',
            borderRadius: '10px',
            border: '1px solid var(--border-color)',
          }}>
            <div style={{ fontSize: '1.1rem', fontWeight: 700, color: '#f59e0b' }}>
              {summary.team2Name}
            </div>
            <div style={{ fontSize: '2rem', fontWeight: 900, color: '#ffffff', margin: '0.35rem 0' }}>
              {summary.team2Score || '-'}{' '}
              <span style={{ fontSize: '1rem', color: '#94a3b8', fontWeight: 600 }}>
                ({summary.team2Overs || '-'} ov)
              </span>
            </div>

            <div style={{ marginTop: '1rem', display: 'flex', flexDirection: 'column', gap: '0.5rem', fontSize: '0.85rem' }}>
              {summary.topBatter2 && (
                <div>
                  <span style={{ color: 'var(--text-muted)' }}>Top Batter: </span>
                  <strong style={{ color: '#ffffff' }}>{summary.topBatter2}</strong>{' '}
                  <span style={{ color: '#c084fc' }}>{summary.topBatter2Stats}</span>
                </div>
              )}
              {summary.topBowler2 && (
                <div>
                  <span style={{ color: 'var(--text-muted)' }}>Top Bowler: </span>
                  <strong style={{ color: '#ffffff' }}>{summary.topBowler2}</strong>{' '}
                  <span style={{ color: '#34d399' }}>{summary.topBowler2Stats}</span>
                </div>
              )}
            </div>
          </div>
        )}
      </div>

      {/* Over-by-Over Run Progression Chart */}
      <div style={{
        backgroundColor: '#111827',
        padding: '1.25rem',
        borderRadius: '10px',
        border: '1px solid var(--border-color)',
      }}>
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '1.25rem' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <BarChart2 size={18} color="#38bdf8" />
            <h4 style={{ fontSize: '1rem', fontWeight: 700 }}>Over-by-Over Worm & Run Progression</h4>
          </div>

          <div style={{ display: 'flex', alignItems: 'center', gap: '1rem', fontSize: '0.8rem' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.4rem' }}>
              <div style={{ width: '12px', height: '12px', backgroundColor: '#38bdf8', borderRadius: '2px' }} />
              <span>{summary.team1Name}</span>
            </div>
            {summary.team2Name && (
              <div style={{ display: 'flex', alignItems: 'center', gap: '0.4rem' }}>
                <div style={{ width: '12px', height: '12px', backgroundColor: '#f59e0b', borderRadius: '2px' }} />
                <span>{summary.team2Name}</span>
              </div>
            )}
          </div>
        </div>

        {/* Visual Bar Chart */}
        <div style={{
          display: 'flex',
          alignItems: 'flex-end',
          gap: '8px',
          height: '180px',
          paddingTop: '20px',
          paddingBottom: '20px',
          borderBottom: '1px solid #334155',
          overflowX: 'auto',
        }}>
          {Array.from({ length: 20 }, (_, i) => i + 1).map((over) => {
            const inn1Point = summary.innings1Progression.find((p) => p.overNumber === over);
            const inn2Point = summary.innings2Progression.find((p) => p.overNumber === over);

            const h1 = inn1Point ? Math.max(10, (inn1Point.cumulativeRuns / maxRuns) * 140) : 0;
            const h2 = inn2Point ? Math.max(10, (inn2Point.cumulativeRuns / maxRuns) * 140) : 0;

            return (
              <div key={over} style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', minWidth: '28px' }}>
                <div style={{ display: 'flex', alignItems: 'flex-end', gap: '2px', height: '140px' }}>
                  {inn1Point && (
                    <div
                      title={`${summary.team1Name} Over ${over}: ${inn1Point.cumulativeRuns}/${inn1Point.wickets}`}
                      style={{
                        width: '10px',
                        height: `${h1}px`,
                        backgroundColor: '#38bdf8',
                        borderRadius: '2px 2px 0 0',
                        transition: 'height 0.3s',
                      }}
                    />
                  )}
                  {inn2Point && (
                    <div
                      title={`${summary.team2Name} Over ${over}: ${inn2Point.cumulativeRuns}/${inn2Point.wickets}`}
                      style={{
                        width: '10px',
                        height: `${h2}px`,
                        backgroundColor: '#f59e0b',
                        borderRadius: '2px 2px 0 0',
                        transition: 'height 0.3s',
                      }}
                    />
                  )}
                </div>
                <span style={{ fontSize: '0.65rem', color: '#64748b', marginTop: '6px' }}>
                  {over}
                </span>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
};

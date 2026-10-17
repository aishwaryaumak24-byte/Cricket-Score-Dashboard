import React from 'react';
import { LiveMatchUpdate, Match } from '../types/cricket';
import { Flame, Target, TrendingUp, Zap, MapPin } from 'lucide-react';

interface HeroScorecardProps {
  liveScore: LiveMatchUpdate | null;
  match: Match | null;
}

export const HeroScorecard: React.FC<HeroScorecardProps> = ({ liveScore, match }) => {
  if (!liveScore || !match) {
    return (
      <div className="glass-card" style={{ padding: '3rem', textAlign: 'center' }}>
        <p style={{ color: 'var(--text-secondary)' }}>Loading live scoreboard...</p>
      </div>
    );
  }

  const isCompleted = liveScore.status === 'COMPLETED';
  const isSecondInnings = liveScore.currentInningsNumber === 2;
  const runsRemaining = liveScore.target ? liveScore.target - liveScore.totalRuns : null;
  const ballsRemaining = liveScore.target ? (liveScore.totalOversLimit * 6) - liveScore.legalBalls : null;

  const getBallChipClass = (val: string) => {
    if (val === 'W') return 'ball-w';
    if (val === '4') return 'ball-4';
    if (val === '6') return 'ball-6';
    if (val.includes('Wd') || val.includes('Nb') || val.includes('B') || val.includes('Lb')) return 'ball-extra';
    if (val === '0') return 'ball-0';
    return 'ball-1';
  };

  return (
    <div className="glass-card" style={{ overflow: 'hidden', borderTop: '4px solid #2563eb' }}>
      {/* Top Match Meta Header */}
      <div style={{
        padding: '0.85rem 1.5rem',
        backgroundColor: 'rgba(15, 23, 42, 0.7)',
        borderBottom: '1px solid var(--border-color)',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        flexWrap: 'wrap',
        gap: '0.75rem',
      }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
          <span className={`badge ${isCompleted ? 'badge-completed' : 'badge-live'}`}>
            {!isCompleted && <span className="live-indicator" style={{ width: '6px', height: '6px' }} />}
            {liveScore.status}
          </span>
          <span style={{ fontSize: '0.875rem', fontWeight: 600, color: 'var(--text-primary)' }}>
            {match.title}
          </span>
          <span style={{
            fontSize: '0.75rem',
            padding: '0.15rem 0.5rem',
            borderRadius: '4px',
            backgroundColor: '#1e293b',
            color: '#38bdf8',
            fontWeight: 700,
          }}>
            {liveScore.matchType} • {liveScore.totalOversLimit} OVERS
          </span>
        </div>

        <div style={{ display: 'flex', alignItems: 'center', gap: '1rem', fontSize: '0.8rem', color: 'var(--text-muted)' }}>
          <span style={{ display: 'flex', alignItems: 'center', gap: '0.35rem' }}>
            <MapPin size={14} color="#64748b" /> {match.venue}, {match.city}
          </span>
        </div>
      </div>

      {/* Main Scoreboard Hero */}
      <div style={{ padding: '1.5rem' }}>
        <div style={{
          display: 'grid',
          gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))',
          gap: '1.5rem',
          alignItems: 'center',
        }}>
          {/* Batting Team Active Score */}
          <div style={{
            display: 'flex',
            alignItems: 'center',
            gap: '1.25rem',
            padding: '1.25rem',
            background: 'linear-gradient(135deg, rgba(30, 41, 59, 0.8), rgba(15, 23, 42, 0.9))',
            borderRadius: '12px',
            border: '1px solid rgba(59, 130, 246, 0.25)',
          }}>
            <div style={{ fontSize: '3rem', lineHeight: 1 }}>
              {liveScore.battingTeamFlag || '🏏'}
            </div>
            <div>
              <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                <h2 style={{ fontSize: '1.35rem', fontWeight: 800, letterSpacing: '-0.01em' }}>
                  {liveScore.battingTeamName}
                </h2>
                <span style={{
                  fontSize: '0.75rem',
                  fontWeight: 700,
                  padding: '0.15rem 0.4rem',
                  backgroundColor: '#0284c7',
                  borderRadius: '4px',
                  color: '#fff',
                }}>
                  {liveScore.battingTeamShort}
                </span>
                <span style={{ fontSize: '0.75rem', color: '#94a3b8' }}>
                  (Inn {liveScore.currentInningsNumber})
                </span>
              </div>

              <div style={{ display: 'flex', alignItems: 'baseline', gap: '0.75rem', marginTop: '0.35rem' }}>
                <span style={{
                  fontSize: '2.5rem',
                  fontWeight: 900,
                  color: '#ffffff',
                  letterSpacing: '-0.03em',
                  fontVariantNumeric: 'tabular-nums',
                }}>
                  {liveScore.totalRuns}/{liveScore.totalWickets}
                </span>
                <span style={{ fontSize: '1.2rem', color: '#94a3b8', fontWeight: 600 }}>
                  ({liveScore.oversFormatted} ov)
                </span>
              </div>
            </div>
          </div>

          {/* Opponent / Target Info */}
          <div style={{
            display: 'flex',
            flexDirection: 'column',
            gap: '0.75rem',
            padding: '1.25rem',
            background: 'rgba(15, 23, 42, 0.5)',
            borderRadius: '12px',
            border: '1px solid var(--border-color)',
          }}>
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                <span style={{ fontSize: '1.5rem' }}>{liveScore.bowlingTeamFlag || '🏏'}</span>
                <span style={{ fontWeight: 700, fontSize: '1rem', color: '#cbd5e1' }}>
                  {liveScore.bowlingTeamName} ({liveScore.bowlingTeamShort})
                </span>
              </div>
              {liveScore.target && (
                <div style={{
                  display: 'flex',
                  alignItems: 'center',
                  gap: '0.35rem',
                  fontSize: '0.85rem',
                  fontWeight: 700,
                  color: '#f59e0b',
                }}>
                  <Target size={16} /> Target: {liveScore.target}
                </div>
              )}
            </div>

            {/* Match Situation Equation */}
            {isCompleted ? (
              <div style={{
                padding: '0.65rem 1rem',
                backgroundColor: 'rgba(16, 185, 129, 0.15)',
                border: '1px solid rgba(16, 185, 129, 0.4)',
                borderRadius: '8px',
                color: '#34d399',
                fontWeight: 700,
                fontSize: '0.95rem',
                display: 'flex',
                alignItems: 'center',
                gap: '0.5rem',
              }}>
                <Flame size={18} color="#34d399" />
                <span>{liveScore.winMargin || 'Match Finished'}</span>
              </div>
            ) : isSecondInnings && runsRemaining !== null && ballsRemaining !== null ? (
              <div style={{
                padding: '0.65rem 1rem',
                backgroundColor: 'rgba(245, 158, 11, 0.1)',
                border: '1px solid rgba(245, 158, 11, 0.3)',
                borderRadius: '8px',
                color: '#fbbf24',
                fontWeight: 700,
                fontSize: '0.9rem',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'space-between',
              }}>
                <span>
                  {liveScore.battingTeamShort} needs <strong style={{ color: '#fff', fontSize: '1.05rem' }}>{runsRemaining > 0 ? runsRemaining : 0}</strong> runs in <strong style={{ color: '#fff', fontSize: '1.05rem' }}>{ballsRemaining > 0 ? ballsRemaining : 0}</strong> balls
                </span>
                <span style={{ fontSize: '0.8rem', color: '#f59e0b', fontWeight: 600 }}>
                  RRR: {liveScore.requiredRunRate ?? '-'}
                </span>
              </div>
            ) : (
              <div style={{ fontSize: '0.85rem', color: 'var(--text-muted)' }}>
                {match.tossWinner ? `${match.tossWinner.name} won the toss & chose to ${match.tossDecision?.toLowerCase()}` : 'Innings 1 in progress'}
              </div>
            )}
          </div>
        </div>

        {/* Live Metrics Row: CRR, RRR, Projected Score */}
        <div style={{
          display: 'grid',
          gridTemplateColumns: 'repeat(auto-fit, minmax(130px, 1fr))',
          gap: '0.75rem',
          marginTop: '1.25rem',
        }}>
          <div style={{
            backgroundColor: '#0f172a',
            padding: '0.75rem 1rem',
            borderRadius: '8px',
            border: '1px solid var(--border-color)',
          }}>
            <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)', display: 'block', fontWeight: 600 }}>
              CURRENT RUN RATE
            </span>
            <span style={{ fontSize: '1.25rem', fontWeight: 800, color: '#38bdf8' }}>
              {liveScore.currentRunRate}
            </span>
          </div>

          {liveScore.requiredRunRate !== undefined && liveScore.requiredRunRate !== null && (
            <div style={{
              backgroundColor: '#0f172a',
              padding: '0.75rem 1rem',
              borderRadius: '8px',
              border: '1px solid var(--border-color)',
            }}>
              <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)', display: 'block', fontWeight: 600 }}>
                REQ. RUN RATE
              </span>
              <span style={{ fontSize: '1.25rem', fontWeight: 800, color: '#f59e0b' }}>
                {liveScore.requiredRunRate}
              </span>
            </div>
          )}

          <div style={{
            backgroundColor: '#0f172a',
            padding: '0.75rem 1rem',
            borderRadius: '8px',
            border: '1px solid var(--border-color)',
          }}>
            <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)', display: 'block', fontWeight: 600 }}>
              PROJECTED SCORE
            </span>
            <span style={{ fontSize: '1.25rem', fontWeight: 800, color: '#34d399' }}>
              {liveScore.projectedScore}
            </span>
          </div>

          <div style={{
            backgroundColor: '#0f172a',
            padding: '0.75rem 1rem',
            borderRadius: '8px',
            border: '1px solid var(--border-color)',
            display: 'flex',
            flexDirection: 'column',
            justifyContent: 'center',
          }}>
            <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)', display: 'block', fontWeight: 600 }}>
              LEGAL BALLS
            </span>
            <span style={{ fontSize: '1.15rem', fontWeight: 700, color: '#cbd5e1' }}>
              {liveScore.legalBalls} / {liveScore.totalOversLimit * 6}
            </span>
          </div>
        </div>

        {/* Recent Balls in Current Over Strip */}
        <div style={{
          marginTop: '1.25rem',
          padding: '0.85rem 1.25rem',
          backgroundColor: '#0b1120',
          borderRadius: '10px',
          border: '1px solid var(--border-color)',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          flexWrap: 'wrap',
          gap: '1rem',
        }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.6rem' }}>
            <span style={{ fontSize: '0.8rem', fontWeight: 700, color: 'var(--text-secondary)', textTransform: 'uppercase' }}>
              Recent Balls:
            </span>
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.45rem', flexWrap: 'wrap' }}>
              {liveScore.recentBalls && liveScore.recentBalls.length > 0 ? (
                liveScore.recentBalls.map((b, idx) => (
                  <span key={idx} className={`ball-chip ${getBallChipClass(b)}`}>
                    {b}
                  </span>
                ))
              ) : (
                <span style={{ fontSize: '0.85rem', color: 'var(--text-muted)' }}>Over beginning...</span>
              )}
            </div>
          </div>

          {liveScore.latestBallDisplay && (
            <div style={{
              display: 'flex',
              alignItems: 'center',
              gap: '0.5rem',
              backgroundColor: 'rgba(30, 41, 59, 0.8)',
              padding: '0.35rem 0.75rem',
              borderRadius: '6px',
              fontSize: '0.8rem',
            }}>
              <span style={{ color: 'var(--text-muted)' }}>Last Event:</span>
              <span className={`ball-chip ${getBallChipClass(liveScore.latestBallDisplay)}`} style={{ width: '24px', height: '24px', fontSize: '0.75rem' }}>
                {liveScore.latestBallDisplay}
              </span>
            </div>
          )}
        </div>

        {/* Live Batsmen & Bowler Grid */}
        <div style={{
          display: 'grid',
          gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))',
          gap: '1rem',
          marginTop: '1.25rem',
        }}>
          {/* Active Batsmen Card */}
          <div style={{
            backgroundColor: '#111827',
            border: '1px solid var(--border-color)',
            borderRadius: '10px',
            padding: '1rem',
          }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.4rem', marginBottom: '0.75rem', fontSize: '0.8rem', fontWeight: 700, color: '#38bdf8' }}>
              <Zap size={14} /> LIVE BATSMEN
            </div>

            <div style={{ display: 'flex', flexDirection: 'column', gap: '0.6rem' }}>
              {/* Striker */}
              <div style={{
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'space-between',
                padding: '0.5rem 0.75rem',
                backgroundColor: 'rgba(37, 99, 235, 0.12)',
                border: '1px solid rgba(37, 99, 235, 0.3)',
                borderRadius: '6px',
              }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                  <span title="On Strike" style={{ fontSize: '1rem' }}>🏏</span>
                  <div>
                    <div style={{ fontWeight: 700, fontSize: '0.95rem', color: '#ffffff' }}>
                      {liveScore.strikerName || 'Striker'} *
                    </div>
                    <div style={{ fontSize: '0.75rem', color: '#94a3b8' }}>
                      4s: {liveScore.strikerFours} • 6s: {liveScore.strikerSixes}
                    </div>
                  </div>
                </div>

                <div style={{ textAlign: 'right' }}>
                  <span style={{ fontSize: '1.2rem', fontWeight: 800, color: '#ffffff' }}>
                    {liveScore.strikerRuns}
                  </span>
                  <span style={{ fontSize: '0.85rem', color: '#94a3b8' }}>
                    ({liveScore.strikerBalls})
                  </span>
                  <div style={{ fontSize: '0.7rem', color: '#38bdf8', fontWeight: 600 }}>
                    SR: {liveScore.strikerStrikeRate}
                  </div>
                </div>
              </div>

              {/* Non-Striker */}
              <div style={{
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'space-between',
                padding: '0.5rem 0.75rem',
                backgroundColor: '#1e293b',
                borderRadius: '6px',
              }}>
                <div>
                  <div style={{ fontWeight: 600, fontSize: '0.9rem', color: '#cbd5e1' }}>
                    {liveScore.nonStrikerName || 'Non-Striker'}
                  </div>
                  <div style={{ fontSize: '0.7rem', color: '#64748b' }}>
                    SR: {liveScore.nonStrikerStrikeRate}
                  </div>
                </div>

                <div style={{ textAlign: 'right' }}>
                  <span style={{ fontSize: '1.1rem', fontWeight: 700, color: '#e2e8f0' }}>
                    {liveScore.nonStrikerRuns}
                  </span>
                  <span style={{ fontSize: '0.85rem', color: '#94a3b8' }}>
                    ({liveScore.nonStrikerBalls})
                  </span>
                </div>
              </div>
            </div>
          </div>

          {/* Active Bowler Card */}
          <div style={{
            backgroundColor: '#111827',
            border: '1px solid var(--border-color)',
            borderRadius: '10px',
            padding: '1rem',
          }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.4rem', marginBottom: '0.75rem', fontSize: '0.8rem', fontWeight: 700, color: '#34d399' }}>
              <TrendingUp size={14} /> CURRENT BOWLER
            </div>

            <div style={{
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'space-between',
              padding: '0.75rem',
              backgroundColor: '#1e293b',
              borderRadius: '6px',
            }}>
              <div>
                <div style={{ fontWeight: 700, fontSize: '1rem', color: '#ffffff' }}>
                  {liveScore.bowlerName || 'Bowler'}
                </div>
                <div style={{ fontSize: '0.75rem', color: '#94a3b8', marginTop: '0.2rem' }}>
                  Economy: <strong style={{ color: '#34d399' }}>{liveScore.bowlerEconomy}</strong>
                </div>
              </div>

              <div style={{ textAlign: 'right' }}>
                <div style={{ fontSize: '1.2rem', fontWeight: 800, color: '#ffffff' }}>
                  {liveScore.bowlerWickets} - {liveScore.bowlerRunsConceded}
                </div>
                <div style={{ fontSize: '0.8rem', color: '#94a3b8' }}>
                  {liveScore.bowlerOvers} overs
                </div>
              </div>
            </div>

            {/* Commentary Snippet */}
            {liveScore.latestCommentary && (
              <div style={{
                marginTop: '0.75rem',
                padding: '0.5rem 0.75rem',
                backgroundColor: 'rgba(15, 23, 42, 0.6)',
                borderRadius: '6px',
                borderLeft: '3px solid #38bdf8',
                fontSize: '0.8rem',
                color: '#cbd5e1',
                lineHeight: 1.4,
              }}>
                <span style={{ color: '#38bdf8', fontWeight: 700 }}>Live: </span>
                {liveScore.latestCommentary}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

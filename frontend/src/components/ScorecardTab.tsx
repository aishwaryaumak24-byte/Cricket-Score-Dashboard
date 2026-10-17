import React, { useState } from 'react';
import { ScorecardResponse, InningsScorecard } from '../types/cricket';

interface ScorecardTabProps {
  scorecard: ScorecardResponse | null;
}

export const ScorecardTab: React.FC<ScorecardTabProps> = ({ scorecard }) => {
  const [selectedInningsNum, setSelectedInningsNum] = useState<number>(1);

  if (!scorecard || (!scorecard.innings1 && !scorecard.innings2)) {
    return (
      <div className="glass-card" style={{ padding: '3rem', textAlign: 'center', color: 'var(--text-secondary)' }}>
        No scorecard information available yet.
      </div>
    );
  }

  const activeInnings: InningsScorecard | undefined =
    selectedInningsNum === 1 ? scorecard.innings1 : (scorecard.innings2 || scorecard.innings1);

  const yetToBat = activeInnings?.batsmen.filter((b) => !b.isOut && b.ballsFaced === 0 && !b.isStriker && !b.isNonStriker) || [];

  return (
    <div className="glass-card" style={{ padding: '1.5rem' }}>
      {/* Innings Switcher Tabs */}
      <div style={{
        display: 'flex',
        gap: '0.75rem',
        marginBottom: '1.5rem',
        borderBottom: '1px solid var(--border-color)',
        paddingBottom: '0.75rem',
      }}>
        {scorecard.innings1 && (
          <button
            onClick={() => setSelectedInningsNum(1)}
            style={{
              display: 'flex',
              alignItems: 'center',
              gap: '0.5rem',
              padding: '0.6rem 1.25rem',
              borderRadius: '8px',
              backgroundColor: selectedInningsNum === 1 ? '#2563eb' : '#1e293b',
              color: '#ffffff',
              fontWeight: 700,
              fontSize: '0.9rem',
              border: 'none',
              cursor: 'pointer',
              transition: 'background 0.2s',
            }}
          >
            <span>{scorecard.innings1.teamFlag}</span>
            <span>{scorecard.innings1.teamName} (1st Inn)</span>
            <span style={{
              backgroundColor: 'rgba(255, 255, 255, 0.2)',
              padding: '0.15rem 0.5rem',
              borderRadius: '4px',
              fontSize: '0.8rem',
            }}>
              {scorecard.innings1.totalRuns}/{scorecard.innings1.totalWickets} ({scorecard.innings1.oversFormatted})
            </span>
          </button>
        )}

        {scorecard.innings2 && (
          <button
            onClick={() => setSelectedInningsNum(2)}
            style={{
              display: 'flex',
              alignItems: 'center',
              gap: '0.5rem',
              padding: '0.6rem 1.25rem',
              borderRadius: '8px',
              backgroundColor: selectedInningsNum === 2 ? '#2563eb' : '#1e293b',
              color: '#ffffff',
              fontWeight: 700,
              fontSize: '0.9rem',
              border: 'none',
              cursor: 'pointer',
              transition: 'background 0.2s',
            }}
          >
            <span>{scorecard.innings2.teamFlag}</span>
            <span>{scorecard.innings2.teamName} (2nd Inn)</span>
            <span style={{
              backgroundColor: 'rgba(255, 255, 255, 0.2)',
              padding: '0.15rem 0.5rem',
              borderRadius: '4px',
              fontSize: '0.8rem',
            }}>
              {scorecard.innings2.totalRuns}/{scorecard.innings2.totalWickets} ({scorecard.innings2.oversFormatted})
            </span>
          </button>
        )}
      </div>

      {activeInnings ? (
        <div>
          {/* Batting Table */}
          <div style={{ marginBottom: '2rem' }}>
            <h4 style={{ fontSize: '1rem', fontWeight: 700, color: '#38bdf8', marginBottom: '0.75rem' }}>
              BATTING • {activeInnings.teamName.toUpperCase()}
            </h4>

            <div style={{ overflowX: 'auto' }}>
              <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left', fontSize: '0.875rem' }}>
                <thead>
                  <tr style={{ backgroundColor: '#111827', borderBottom: '2px solid var(--border-color)', color: 'var(--text-muted)' }}>
                    <th style={{ padding: '0.75rem 1rem' }}>Batter</th>
                    <th style={{ padding: '0.75rem 0.5rem' }}>Dismissal</th>
                    <th style={{ padding: '0.75rem 0.75rem', textAlign: 'right' }}>R</th>
                    <th style={{ padding: '0.75rem 0.75rem', textAlign: 'right' }}>B</th>
                    <th style={{ padding: '0.75rem 0.75rem', textAlign: 'right' }}>4s</th>
                    <th style={{ padding: '0.75rem 0.75rem', textAlign: 'right' }}>6s</th>
                    <th style={{ padding: '0.75rem 1rem', textAlign: 'right' }}>SR</th>
                  </tr>
                </thead>
                <tbody>
                  {activeInnings.batsmen.map((b) => {
                    const isBatted = b.ballsFaced > 0 || b.isOut || b.isStriker || b.isNonStriker;
                    if (!isBatted) return null;

                    return (
                      <tr
                        key={b.id}
                        style={{
                          borderBottom: '1px solid var(--border-color)',
                          backgroundColor: b.isStriker ? 'rgba(37, 99, 235, 0.08)' : 'transparent',
                        }}
                      >
                        <td style={{ padding: '0.75rem 1rem', fontWeight: 600 }}>
                          <span style={{ color: '#ffffff' }}>
                            {b.player.name} {b.isStriker ? '🏏*' : b.isNonStriker ? '*' : ''}
                          </span>
                        </td>
                        <td style={{ padding: '0.75rem 0.5rem', color: b.isOut ? '#94a3b8' : '#34d399', fontSize: '0.8rem' }}>
                          {b.dismissalInfo}
                        </td>
                        <td style={{ padding: '0.75rem 0.75rem', textAlign: 'right', fontWeight: 800, color: '#ffffff' }}>
                          {b.runs}
                        </td>
                        <td style={{ padding: '0.75rem 0.75rem', textAlign: 'right', color: '#cbd5e1' }}>
                          {b.ballsFaced}
                        </td>
                        <td style={{ padding: '0.75rem 0.75rem', textAlign: 'right', color: '#c084fc' }}>
                          {b.fours}
                        </td>
                        <td style={{ padding: '0.75rem 0.75rem', textAlign: 'right', color: '#fde047' }}>
                          {b.sixes}
                        </td>
                        <td style={{ padding: '0.75rem 1rem', textAlign: 'right', color: '#38bdf8', fontWeight: 600 }}>
                          {b.strikeRate}
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>

            {/* Extras & Totals Strip */}
            <div style={{
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'space-between',
              padding: '0.85rem 1rem',
              backgroundColor: '#111827',
              borderBottom: '1px solid var(--border-color)',
              fontSize: '0.85rem',
            }}>
              <div>
                <strong style={{ color: '#ffffff' }}>Extras:</strong>{' '}
                <span style={{ color: '#94a3b8' }}>
                  {activeInnings.extrasTotal} (wd {activeInnings.wides}, nb {activeInnings.noBalls}, b {activeInnings.byes}, lb {activeInnings.legByes})
                </span>
              </div>
              <div>
                <strong style={{ color: '#ffffff', fontSize: '1rem' }}>
                  TOTAL: {activeInnings.totalRuns}/{activeInnings.totalWickets}
                </strong>{' '}
                <span style={{ color: '#94a3b8' }}>
                  ({activeInnings.oversFormatted} Ov, RR: {activeInnings.runRate})
                </span>
              </div>
            </div>

            {/* Yet to Bat */}
            {yetToBat.length > 0 && (
              <div style={{ padding: '0.75rem 1rem', fontSize: '0.8rem', color: '#64748b', backgroundColor: '#0b1120' }}>
                <strong>Yet to bat:</strong> {yetToBat.map((b) => b.player.name).join(', ')}
              </div>
            )}
          </div>

          {/* Bowling Table */}
          <div>
            <h4 style={{ fontSize: '1rem', fontWeight: 700, color: '#34d399', marginBottom: '0.75rem' }}>
              BOWLING
            </h4>

            <div style={{ overflowX: 'auto' }}>
              <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left', fontSize: '0.875rem' }}>
                <thead>
                  <tr style={{ backgroundColor: '#111827', borderBottom: '2px solid var(--border-color)', color: 'var(--text-muted)' }}>
                    <th style={{ padding: '0.75rem 1rem' }}>Bowler</th>
                    <th style={{ padding: '0.75rem 0.75rem', textAlign: 'right' }}>O</th>
                    <th style={{ padding: '0.75rem 0.75rem', textAlign: 'right' }}>M</th>
                    <th style={{ padding: '0.75rem 0.75rem', textAlign: 'right' }}>R</th>
                    <th style={{ padding: '0.75rem 0.75rem', textAlign: 'right' }}>W</th>
                    <th style={{ padding: '0.75rem 0.75rem', textAlign: 'right' }}>ECON</th>
                    <th style={{ padding: '0.75rem 1rem', textAlign: 'right' }}>DOTS</th>
                  </tr>
                </thead>
                <tbody>
                  {activeInnings.bowlers.map((bw) => {
                    if (bw.legalBalls === 0 && !bw.isCurrentBowler) return null;

                    return (
                      <tr
                        key={bw.id}
                        style={{
                          borderBottom: '1px solid var(--border-color)',
                          backgroundColor: bw.isCurrentBowler ? 'rgba(16, 185, 129, 0.08)' : 'transparent',
                        }}
                      >
                        <td style={{ padding: '0.75rem 1rem', fontWeight: 600, color: '#ffffff' }}>
                          {bw.player.name} {bw.isCurrentBowler ? '🎳 (current)' : ''}
                        </td>
                        <td style={{ padding: '0.75rem 0.75rem', textAlign: 'right', color: '#cbd5e1' }}>
                          {bw.oversFormatted}
                        </td>
                        <td style={{ padding: '0.75rem 0.75rem', textAlign: 'right', color: '#94a3b8' }}>
                          {bw.maidenOvers}
                        </td>
                        <td style={{ padding: '0.75rem 0.75rem', textAlign: 'right', fontWeight: 700, color: '#ffffff' }}>
                          {bw.runsConceded}
                        </td>
                        <td style={{ padding: '0.75rem 0.75rem', textAlign: 'right', fontWeight: 800, color: '#34d399' }}>
                          {bw.wicketsTaken}
                        </td>
                        <td style={{ padding: '0.75rem 0.75rem', textAlign: 'right', color: '#38bdf8', fontWeight: 600 }}>
                          {bw.economyRate}
                        </td>
                        <td style={{ padding: '0.75rem 1rem', textAlign: 'right', color: '#64748b' }}>
                          {bw.dotBalls}
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      ) : null}
    </div>
  );
};

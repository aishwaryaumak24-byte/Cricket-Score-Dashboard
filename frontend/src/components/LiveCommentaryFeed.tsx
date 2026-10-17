import React, { useState } from 'react';
import { BallEvent } from '../types/cricket';
import { MessageSquare, Filter } from 'lucide-react';

interface LiveCommentaryFeedProps {
  commentary: BallEvent[];
}

export const LiveCommentaryFeed: React.FC<LiveCommentaryFeedProps> = ({ commentary }) => {
  const [filter, setFilter] = useState<'ALL' | 'BOUNDARIES' | 'WICKETS'>('ALL');

  const filteredList = commentary.filter((event) => {
    if (filter === 'BOUNDARIES') {
      return event.runsScored === 4 || event.runsScored === 6;
    }
    if (filter === 'WICKETS') {
      return event.isWicket;
    }
    return true;
  });

  const getChipStyle = (display: string) => {
    if (display === 'W') return { bg: '#7f1d1d', text: '#fca5a5', border: '#ef4444' };
    if (display === '4') return { bg: '#4c1d95', text: '#c084fc', border: '#7c3aed' };
    if (display === '6') return { bg: '#78350f', text: '#fde047', border: '#f59e0b' };
    if (display.includes('Wd') || display.includes('Nb')) return { bg: '#713f12', text: '#fde047', border: '#eab308' };
    if (display === '0') return { bg: '#1e293b', text: '#94a3b8', border: '#334155' };
    return { bg: '#0f3957', text: '#38bdf8', border: '#0284c7' };
  };

  return (
    <div className="glass-card" style={{ padding: '1.5rem' }}>
      {/* Header and Filter */}
      <div style={{
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        flexWrap: 'wrap',
        gap: '1rem',
        marginBottom: '1.25rem',
        paddingBottom: '1rem',
        borderBottom: '1px solid var(--border-color)',
      }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
          <MessageSquare size={18} color="#38bdf8" />
          <h3 style={{ fontSize: '1.1rem', fontWeight: 700 }}>Ball-by-Ball Commentary</h3>
          <span style={{
            fontSize: '0.75rem',
            color: 'var(--text-muted)',
            backgroundColor: '#1e293b',
            padding: '0.15rem 0.5rem',
            borderRadius: '12px',
          }}>
            {filteredList.length} events
          </span>
        </div>

        {/* Filter Pills */}
        <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
          <Filter size={14} color="#64748b" />
          {(['ALL', 'BOUNDARIES', 'WICKETS'] as const).map((mode) => (
            <button
              key={mode}
              onClick={() => setFilter(mode)}
              style={{
                background: filter === mode ? '#2563eb' : '#1e293b',
                color: filter === mode ? '#ffffff' : '#94a3b8',
                border: '1px solid',
                borderColor: filter === mode ? '#3b82f6' : '#334155',
                padding: '0.3rem 0.75rem',
                borderRadius: '6px',
                fontSize: '0.75rem',
                fontWeight: 600,
                cursor: 'pointer',
              }}
            >
              {mode}
            </button>
          ))}
        </div>
      </div>

      {/* Commentary Items */}
      {filteredList.length === 0 ? (
        <div style={{ textAlign: 'center', padding: '3rem 1rem', color: 'var(--text-muted)' }}>
          No commentary events match the selected filter.
        </div>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '0.75rem' }}>
          {filteredList.map((event) => {
            const chip = getChipStyle(event.ballDisplay);
            const isEndOfOver = event.ballNumberInOver === 6;

            return (
              <React.Fragment key={event.id}>
                <div style={{
                  display: 'flex',
                  alignItems: 'flex-start',
                  gap: '1rem',
                  padding: '0.85rem 1rem',
                  backgroundColor: event.isWicket
                    ? 'rgba(239, 68, 68, 0.08)'
                    : event.runsScored >= 4
                    ? 'rgba(124, 58, 237, 0.08)'
                    : '#111827',
                  border: '1px solid',
                  borderColor: event.isWicket
                    ? 'rgba(239, 68, 68, 0.3)'
                    : event.runsScored >= 4
                    ? 'rgba(124, 58, 237, 0.3)'
                    : 'var(--border-color)',
                  borderRadius: '10px',
                  transition: 'background 0.2s',
                }}>
                  {/* Left: Over & Chip */}
                  <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', minWidth: '55px', gap: '0.35rem' }}>
                    <span style={{ fontSize: '0.85rem', fontWeight: 800, color: '#94a3b8' }}>
                      {event.overNumber}.{event.ballNumberInOver}
                    </span>
                    <span style={{
                      display: 'inline-flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      width: '30px',
                      height: '30px',
                      borderRadius: '50%',
                      backgroundColor: chip.bg,
                      color: chip.text,
                      border: `1px solid ${chip.border}`,
                      fontSize: '0.8rem',
                      fontWeight: 800,
                    }}>
                      {event.ballDisplay}
                    </span>
                  </div>

                  {/* Middle: Text Commentary */}
                  <div style={{ flex: 1 }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginBottom: '0.25rem' }}>
                      <span style={{ fontWeight: 700, fontSize: '0.9rem', color: '#ffffff' }}>
                        {event.bowler ? event.bowler.name : 'Bowler'} to {event.batsman ? event.batsman.name : 'Batsman'}
                      </span>
                      {event.isWicket && (
                        <span className="badge badge-live" style={{ fontSize: '0.65rem', padding: '0.1rem 0.4rem' }}>
                          WICKET ({event.wicketType})
                        </span>
                      )}
                      {event.runsScored === 4 && (
                        <span style={{
                          fontSize: '0.65rem',
                          fontWeight: 700,
                          backgroundColor: '#7c3aed',
                          color: '#fff',
                          padding: '0.1rem 0.4rem',
                          borderRadius: '4px',
                        }}>
                          FOUR
                        </span>
                      )}
                      {event.runsScored === 6 && (
                        <span style={{
                          fontSize: '0.65rem',
                          fontWeight: 700,
                          backgroundColor: '#f59e0b',
                          color: '#000',
                          padding: '0.1rem 0.4rem',
                          borderRadius: '4px',
                        }}>
                          SIX
                        </span>
                      )}
                    </div>

                    <p style={{ fontSize: '0.875rem', color: '#cbd5e1', lineHeight: 1.45 }}>
                      {event.commentaryText}
                    </p>
                  </div>

                  {/* Right: Score At That Ball */}
                  <div style={{ textAlign: 'right', minWidth: '70px' }}>
                    <span style={{ fontSize: '0.95rem', fontWeight: 800, color: '#f8fafc' }}>
                      {event.scoreAfterBall}/{event.wicketsAfterBall}
                    </span>
                  </div>
                </div>

                {/* Over Summary Separator if end of over */}
                {isEndOfOver && (
                  <div style={{
                    padding: '0.5rem 1rem',
                    backgroundColor: 'rgba(30, 41, 59, 0.6)',
                    borderRadius: '8px',
                    fontSize: '0.75rem',
                    fontWeight: 700,
                    color: '#94a3b8',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'space-between',
                    border: '1px dashed #334155',
                  }}>
                    <span>END OF OVER {event.overNumber + 1}</span>
                    <span>SCORE: {event.scoreAfterBall}/{event.wicketsAfterBall}</span>
                  </div>
                )}
              </React.Fragment>
            );
          })}
        </div>
      )}
    </div>
  );
};

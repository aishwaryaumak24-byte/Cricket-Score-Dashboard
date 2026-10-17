import React, { useState } from 'react';
import { ScoreBallPayload, ExtraType, WicketType } from '../types/cricket';
import { X, Check } from 'lucide-react';

interface ManualScoreModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (payload: ScoreBallPayload) => void;
}

export const ManualScoreModal: React.FC<ManualScoreModalProps> = ({
  isOpen,
  onClose,
  onSubmit,
}) => {
  const [runsScored, setRunsScored] = useState<number>(0);
  const [extraType, setExtraType] = useState<ExtraType>('NONE');
  const extraRuns = 1;
  const [isWicket, setIsWicket] = useState<boolean>(false);
  const [wicketType, setWicketType] = useState<WicketType>('BOWLED');
  const [customCommentary, setCustomCommentary] = useState<string>('');

  if (!isOpen) return null;

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit({
      runsScored,
      extraType,
      extraRuns: extraType !== 'NONE' ? extraRuns : 0,
      isWicket,
      wicketType: isWicket ? wicketType : 'NONE',
      customCommentary: customCommentary.trim() || undefined,
    });
    onClose();
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content" style={{ padding: '1.5rem' }}>
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '1.25rem' }}>
          <h3 style={{ fontSize: '1.2rem', fontWeight: 800 }}>Umpire / Manual Scoring Pad</h3>
          <button onClick={onClose} style={{ background: 'none', border: 'none', color: '#94a3b8', cursor: 'pointer' }}>
            <X size={20} />
          </button>
        </div>

        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '1.25rem' }}>
          {/* Runs Scored */}
          <div>
            <label style={{ fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', display: 'block', marginBottom: '0.5rem' }}>
              Runs Off Bat:
            </label>
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(6, 1fr)', gap: '0.5rem' }}>
              {[0, 1, 2, 3, 4, 6].map((r) => (
                <button
                  type="button"
                  key={r}
                  onClick={() => setRunsScored(r)}
                  style={{
                    padding: '0.75rem',
                    borderRadius: '8px',
                    border: '1px solid',
                    borderColor: runsScored === r ? '#3b82f6' : '#334155',
                    backgroundColor: runsScored === r ? '#2563eb' : '#1e293b',
                    color: '#ffffff',
                    fontWeight: 800,
                    fontSize: '1.1rem',
                    cursor: 'pointer',
                  }}
                >
                  {r}
                </button>
              ))}
            </div>
          </div>

          {/* Extras */}
          <div>
            <label style={{ fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', display: 'block', marginBottom: '0.5rem' }}>
              Extras:
            </label>
            <div style={{ display: 'flex', gap: '0.5rem', flexWrap: 'wrap' }}>
              {(['NONE', 'WIDE', 'NO_BALL', 'BYE', 'LEG_BYE'] as ExtraType[]).map((ext) => (
                <button
                  type="button"
                  key={ext}
                  onClick={() => setExtraType(ext)}
                  style={{
                    padding: '0.45rem 0.85rem',
                    borderRadius: '6px',
                    border: '1px solid',
                    borderColor: extraType === ext ? '#f59e0b' : '#334155',
                    backgroundColor: extraType === ext ? 'rgba(245, 158, 11, 0.2)' : '#1e293b',
                    color: extraType === ext ? '#fde047' : '#cbd5e1',
                    fontSize: '0.8rem',
                    fontWeight: 600,
                    cursor: 'pointer',
                  }}
                >
                  {ext.replace('_', ' ')}
                </button>
              ))}
            </div>
          </div>

          {/* Wicket Toggle */}
          <div style={{
            padding: '1rem',
            backgroundColor: isWicket ? 'rgba(239, 68, 68, 0.1)' : '#1e293b',
            border: `1px solid ${isWicket ? 'rgba(239, 68, 68, 0.4)' : '#334155'}`,
            borderRadius: '8px',
          }}>
            <label style={{ display: 'flex', alignItems: 'center', gap: '0.6rem', cursor: 'pointer', fontWeight: 700 }}>
              <input
                type="checkbox"
                checked={isWicket}
                onChange={(e) => setIsWicket(e.target.checked)}
                style={{ width: '18px', height: '18px', accentColor: '#ef4444' }}
              />
              <span style={{ color: isWicket ? '#f87171' : '#ffffff' }}>WICKET FELL THIS BALL</span>
            </label>

            {isWicket && (
              <div style={{ marginTop: '0.75rem', display: 'flex', gap: '0.5rem', flexWrap: 'wrap' }}>
                {(['BOWLED', 'CAUGHT', 'LBW', 'RUN_OUT', 'STUMPED'] as WicketType[]).map((wt) => (
                  <button
                    type="button"
                    key={wt}
                    onClick={() => setWicketType(wt)}
                    style={{
                      padding: '0.35rem 0.65rem',
                      borderRadius: '6px',
                      border: '1px solid',
                      borderColor: wicketType === wt ? '#ef4444' : '#475569',
                      backgroundColor: wicketType === wt ? '#991b1b' : '#334155',
                      color: '#ffffff',
                      fontSize: '0.75rem',
                      fontWeight: 600,
                      cursor: 'pointer',
                    }}
                  >
                    {wt}
                  </button>
                ))}
              </div>
            )}
          </div>

          {/* Custom Commentary */}
          <div>
            <label style={{ fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', display: 'block', marginBottom: '0.5rem' }}>
              Custom Commentary (Optional):
            </label>
            <input
              type="text"
              value={customCommentary}
              onChange={(e) => setCustomCommentary(e.target.value)}
              placeholder="e.g. Smashed over deep mid-wicket with thunderous authority!"
              style={{
                width: '100%',
                backgroundColor: '#1e293b',
                color: '#ffffff',
                border: '1px solid var(--border-color)',
                borderRadius: '8px',
                padding: '0.65rem 0.85rem',
                fontSize: '0.875rem',
              }}
            />
          </div>

          {/* Submit */}
          <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '0.75rem', marginTop: '0.5rem' }}>
            <button type="button" onClick={onClose} className="btn btn-secondary">
              Cancel
            </button>
            <button type="submit" className="btn btn-primary" style={{ minWidth: '120px' }}>
              <Check size={16} />
              <span>Record Ball</span>
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

import React, { useState } from 'react';
import { CreateMatchPayload, Team, MatchType, TossDecision } from '../types/cricket';
import { X, PlusCircle } from 'lucide-react';

interface CreateMatchModalProps {
  isOpen: boolean;
  onClose: () => void;
  teams: Team[];
  onSubmit: (payload: CreateMatchPayload) => void;
}

export const CreateMatchModal: React.FC<CreateMatchModalProps> = ({
  isOpen,
  onClose,
  teams,
  onSubmit,
}) => {
  const [team1Id, setTeam1Id] = useState<number>(teams[0]?.id || 1);
  const [team2Id, setTeam2Id] = useState<number>(teams[1]?.id || 2);
  const [matchType, setMatchType] = useState<MatchType>('T20');
  const [totalOversLimit, setTotalOversLimit] = useState<number>(20);
  const [tossWinnerId, setTossWinnerId] = useState<number>(teams[0]?.id || 1);
  const [tossDecision, setTossDecision] = useState<TossDecision>('BAT');
  const [venue, setVenue] = useState<string>('Melbourne Cricket Ground');
  const [city, setCity] = useState<string>('Melbourne');

  if (!isOpen) return null;

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const t1 = teams.find((t) => t.id === team1Id);
    const t2 = teams.find((t) => t.id === team2Id);

    onSubmit({
      title: `${t1?.name || 'Team 1'} vs ${t2?.name || 'Team 2'} (${matchType})`,
      team1Id,
      team2Id,
      tossWinnerId,
      tossDecision,
      matchType,
      totalOversLimit,
      venue,
      city,
    });
    onClose();
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content" style={{ padding: '1.5rem' }}>
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '1.25rem' }}>
          <h3 style={{ fontSize: '1.2rem', fontWeight: 800 }}>Create New Cricket Match</h3>
          <button onClick={onClose} style={{ background: 'none', border: 'none', color: '#94a3b8', cursor: 'pointer' }}>
            <X size={20} />
          </button>
        </div>

        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
          {/* Teams */}
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
            <div>
              <label style={{ fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-secondary)', display: 'block', marginBottom: '0.35rem' }}>
                Team 1:
              </label>
              <select
                value={team1Id}
                onChange={(e) => setTeam1Id(Number(e.target.value))}
                style={{
                  width: '100%',
                  backgroundColor: '#1e293b',
                  color: '#ffffff',
                  border: '1px solid var(--border-color)',
                  borderRadius: '8px',
                  padding: '0.6rem',
                  fontSize: '0.85rem',
                }}
              >
                {teams.map((t) => (
                  <option key={t.id} value={t.id}>{t.flagEmoji} {t.name}</option>
                ))}
              </select>
            </div>

            <div>
              <label style={{ fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-secondary)', display: 'block', marginBottom: '0.35rem' }}>
                Team 2:
              </label>
              <select
                value={team2Id}
                onChange={(e) => setTeam2Id(Number(e.target.value))}
                style={{
                  width: '100%',
                  backgroundColor: '#1e293b',
                  color: '#ffffff',
                  border: '1px solid var(--border-color)',
                  borderRadius: '8px',
                  padding: '0.6rem',
                  fontSize: '0.85rem',
                }}
              >
                {teams.map((t) => (
                  <option key={t.id} value={t.id}>{t.flagEmoji} {t.name}</option>
                ))}
              </select>
            </div>
          </div>

          {/* Format & Overs */}
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
            <div>
              <label style={{ fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-secondary)', display: 'block', marginBottom: '0.35rem' }}>
                Format:
              </label>
              <select
                value={matchType}
                onChange={(e) => {
                  const val = e.target.value as MatchType;
                  setMatchType(val);
                  setTotalOversLimit(val === 'ODI' ? 50 : 20);
                }}
                style={{
                  width: '100%',
                  backgroundColor: '#1e293b',
                  color: '#ffffff',
                  border: '1px solid var(--border-color)',
                  borderRadius: '8px',
                  padding: '0.6rem',
                  fontSize: '0.85rem',
                }}
              >
                <option value="T20">T20 (Twenty20)</option>
                <option value="ODI">ODI (One Day International)</option>
              </select>
            </div>

            <div>
              <label style={{ fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-secondary)', display: 'block', marginBottom: '0.35rem' }}>
                Overs Limit:
              </label>
              <select
                value={totalOversLimit}
                onChange={(e) => setTotalOversLimit(Number(e.target.value))}
                style={{
                  width: '100%',
                  backgroundColor: '#1e293b',
                  color: '#ffffff',
                  border: '1px solid var(--border-color)',
                  borderRadius: '8px',
                  padding: '0.6rem',
                  fontSize: '0.85rem',
                }}
              >
                <option value={5}>5 Overs (Super Fast)</option>
                <option value={10}>10 Overs (Quick Match)</option>
                <option value={20}>20 Overs (Standard T20)</option>
                <option value={50}>50 Overs (ODI)</option>
              </select>
            </div>
          </div>

          {/* Toss */}
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
            <div>
              <label style={{ fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-secondary)', display: 'block', marginBottom: '0.35rem' }}>
                Toss Winner:
              </label>
              <select
                value={tossWinnerId}
                onChange={(e) => setTossWinnerId(Number(e.target.value))}
                style={{
                  width: '100%',
                  backgroundColor: '#1e293b',
                  color: '#ffffff',
                  border: '1px solid var(--border-color)',
                  borderRadius: '8px',
                  padding: '0.6rem',
                  fontSize: '0.85rem',
                }}
              >
                <option value={team1Id}>Team 1</option>
                <option value={team2Id}>Team 2</option>
              </select>
            </div>

            <div>
              <label style={{ fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-secondary)', display: 'block', marginBottom: '0.35rem' }}>
                Elected To:
              </label>
              <select
                value={tossDecision}
                onChange={(e) => setTossDecision(e.target.value as TossDecision)}
                style={{
                  width: '100%',
                  backgroundColor: '#1e293b',
                  color: '#ffffff',
                  border: '1px solid var(--border-color)',
                  borderRadius: '8px',
                  padding: '0.6rem',
                  fontSize: '0.85rem',
                }}
              >
                <option value="BAT">Bat First</option>
                <option value="BOWL">Bowl First</option>
              </select>
            </div>
          </div>

          {/* Venue & City */}
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
            <div>
              <label style={{ fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-secondary)', display: 'block', marginBottom: '0.35rem' }}>
                Stadium / Venue:
              </label>
              <input
                type="text"
                value={venue}
                onChange={(e) => setVenue(e.target.value)}
                style={{
                  width: '100%',
                  backgroundColor: '#1e293b',
                  color: '#ffffff',
                  border: '1px solid var(--border-color)',
                  borderRadius: '8px',
                  padding: '0.6rem',
                  fontSize: '0.85rem',
                }}
              />
            </div>

            <div>
              <label style={{ fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-secondary)', display: 'block', marginBottom: '0.35rem' }}>
                City:
              </label>
              <input
                type="text"
                value={city}
                onChange={(e) => setCity(e.target.value)}
                style={{
                  width: '100%',
                  backgroundColor: '#1e293b',
                  color: '#ffffff',
                  border: '1px solid var(--border-color)',
                  borderRadius: '8px',
                  padding: '0.6rem',
                  fontSize: '0.85rem',
                }}
              />
            </div>
          </div>

          <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '0.75rem', marginTop: '0.75rem' }}>
            <button type="button" onClick={onClose} className="btn btn-secondary">
              Cancel
            </button>
            <button type="submit" className="btn btn-primary">
              <PlusCircle size={16} />
              <span>Create & Launch Match</span>
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

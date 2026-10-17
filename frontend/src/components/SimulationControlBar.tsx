import React, { useState } from 'react';
import { Play, Pause, FastForward, Edit3, RotateCcw, Zap } from 'lucide-react';

interface SimulationControlBarProps {
  isSimulating: boolean;
  onToggleSimulation: (speedMs: number) => void;
  onSimulateStep: () => void;
  onOpenManualModal: () => void;
  onResetMatch: () => void;
  matchCompleted: boolean;
}

export const SimulationControlBar: React.FC<SimulationControlBarProps> = ({
  isSimulating,
  onToggleSimulation,
  onSimulateStep,
  onOpenManualModal,
  onResetMatch,
  matchCompleted,
}) => {
  const [speed, setSpeed] = useState<number>(2500);

  const handleSpeedChange = (newSpeed: number) => {
    setSpeed(newSpeed);
    if (isSimulating) {
      onToggleSimulation(newSpeed);
    }
  };

  return (
    <div className="glass-card" style={{
      padding: '0.85rem 1.5rem',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'space-between',
      flexWrap: 'wrap',
      gap: '1rem',
      backgroundColor: '#111827',
      border: '1px solid var(--border-color)',
    }}>
      {/* Simulation Engine Status & Controls */}
      <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', flexWrap: 'wrap' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '0.4rem' }}>
          <Zap size={16} color="#38bdf8" />
          <span style={{ fontSize: '0.85rem', fontWeight: 700, textTransform: 'uppercase', color: '#94a3b8' }}>
            Match Engine:
          </span>
        </div>

        {/* Play / Pause Toggle */}
        <button
          onClick={() => onToggleSimulation(speed)}
          disabled={matchCompleted}
          className={`btn ${isSimulating ? 'btn-danger' : 'btn-success'}`}
          style={{ minWidth: '135px' }}
        >
          {isSimulating ? (
            <>
              <Pause size={16} />
              <span>Pause Sim</span>
            </>
          ) : (
            <>
              <Play size={16} />
              <span>{matchCompleted ? 'Finished' : 'Auto Play'}</span>
            </>
          )}
        </button>

        {/* Step 1 Ball */}
        <button
          onClick={onSimulateStep}
          disabled={isSimulating || matchCompleted}
          className="btn btn-secondary"
          title="Simulate Single Ball"
        >
          <FastForward size={16} />
          <span>Next Ball</span>
        </button>

        {/* Speed Selector */}
        <div style={{
          display: 'flex',
          alignItems: 'center',
          backgroundColor: '#1e293b',
          borderRadius: '8px',
          padding: '0.2rem',
          border: '1px solid #334155',
        }}>
          {[
            { label: '1s (Fast)', ms: 1000 },
            { label: '2.5s (Std)', ms: 2500 },
            { label: '4s (Slow)', ms: 4000 },
          ].map((item) => (
            <button
              key={item.ms}
              onClick={() => handleSpeedChange(item.ms)}
              style={{
                background: speed === item.ms ? '#2563eb' : 'transparent',
                color: speed === item.ms ? '#ffffff' : '#94a3b8',
                border: 'none',
                padding: '0.35rem 0.65rem',
                borderRadius: '6px',
                fontSize: '0.75rem',
                fontWeight: 600,
                cursor: 'pointer',
                transition: 'background 0.2s',
              }}
            >
              {item.label}
            </button>
          ))}
        </div>
      </div>

      {/* Manual Scoring and Match Reset */}
      <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
        <button
          onClick={onOpenManualModal}
          disabled={matchCompleted}
          className="btn btn-secondary"
        >
          <Edit3 size={15} />
          <span>Manual Score</span>
        </button>

        <button
          onClick={() => {
            if (window.confirm('Reset this match to the beginning of Innings 1?')) {
              onResetMatch();
            }
          }}
          className="btn btn-secondary"
          style={{ color: '#f87171' }}
          title="Reset Match"
        >
          <RotateCcw size={15} />
          <span>Reset</span>
        </button>
      </div>
    </div>
  );
};

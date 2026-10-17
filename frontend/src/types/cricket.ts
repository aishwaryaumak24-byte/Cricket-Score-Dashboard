export type MatchStatus = 'SCHEDULED' | 'LIVE' | 'INNINGS_BREAK' | 'COMPLETED' | 'ABANDONED';
export type MatchType = 'T20' | 'ODI' | 'TEST';
export type PlayerRole = 'BATSMAN' | 'BOWLER' | 'ALL_ROUNDER' | 'WICKET_KEEPER';
export type ExtraType = 'NONE' | 'WIDE' | 'NO_BALL' | 'BYE' | 'LEG_BYE' | 'PENALTY';
export type WicketType = 'NONE' | 'BOWLED' | 'CAUGHT' | 'LBW' | 'RUN_OUT' | 'STUMPED' | 'HIT_WICKET';
export type TossDecision = 'BAT' | 'BOWL';

export interface Team {
  id: number;
  name: string;
  shortName: string;
  flagEmoji: string;
  primaryColor: string;
  secondaryColor: string;
  country: string;
}

export interface Player {
  id: number;
  name: string;
  teamId: number;
  teamName: string;
  role: PlayerRole;
  battingStyle: string;
  bowlingStyle: string;
  jerseyNumber: number;
  matchesPlayed: number;
  totalRuns: number;
  highestScore: number;
  centuries: number;
  halfCenturies: number;
  totalWickets: number;
  bestBowlingFigures: string;
  careerEconomy: number;
  careerStrikeRate: number;
}

export interface Match {
  id: number;
  title: string;
  matchType: MatchType;
  status: MatchStatus;
  venue: string;
  city: string;
  matchDate: string;
  team1: Team;
  team2: Team;
  tossWinner?: Team;
  tossDecision?: TossDecision;
  totalOversLimit: number;
  currentInningsNumber: number;
  winner?: Team;
  winMargin?: string;
  manOfTheMatch?: string;
  isSimulating: boolean;
  simulationSpeedMs: number;
  target?: number;
}

export interface LiveMatchUpdate {
  matchId: number;
  title: string;
  status: MatchStatus;
  matchType: MatchType;
  currentInningsNumber: number;
  totalOversLimit: number;

  battingTeamName: string;
  battingTeamShort: string;
  battingTeamFlag: string;
  battingTeamColor: string;

  bowlingTeamName: string;
  bowlingTeamShort: string;
  bowlingTeamFlag: string;
  bowlingTeamColor: string;

  totalRuns: number;
  totalWickets: number;
  oversFormatted: string;
  legalBalls: number;
  currentScore: string;
  currentRunRate: number;
  requiredRunRate?: number;
  projectedScore: number;
  target?: number;

  strikerId?: number;
  strikerName?: string;
  strikerRuns: number;
  strikerBalls: number;
  strikerFours: number;
  strikerSixes: number;
  strikerStrikeRate: number;

  nonStrikerId?: number;
  nonStrikerName?: string;
  nonStrikerRuns: number;
  nonStrikerBalls: number;
  nonStrikerStrikeRate: number;

  bowlerId?: number;
  bowlerName?: string;
  bowlerOvers?: string;
  bowlerRunsConceded: number;
  bowlerWickets: number;
  bowlerEconomy: number;

  recentBalls: string[];
  latestCommentary?: string;
  latestBallDisplay?: string;

  winMargin?: string;
  winnerTeamName?: string;
  isSimulating: boolean;
}

export interface BattingScorecard {
  id: number;
  inningsId: number;
  player: Player;
  battingPosition: number;
  runs: number;
  ballsFaced: number;
  fours: number;
  sixes: number;
  strikeRate: number;
  isOut: boolean;
  dismissalType: WicketType;
  dismissalInfo: string;
  bowlerName?: string;
  catcherName?: string;
  isStriker: boolean;
  isNonStriker: boolean;
}

export interface BowlingScorecard {
  id: number;
  inningsId: number;
  player: Player;
  oversFormatted: string;
  legalBalls: number;
  maidenOvers: number;
  runsConceded: number;
  wicketsTaken: number;
  economyRate: number;
  wides: number;
  noBalls: number;
  dotBalls: number;
  isCurrentBowler: boolean;
}

export interface InningsScorecard {
  inningsId: number;
  inningsNumber: number;
  teamName: string;
  teamShort: string;
  teamFlag: string;
  totalRuns: number;
  totalWickets: number;
  oversFormatted: string;
  runRate: number;
  target?: number;
  extrasTotal: number;
  wides: number;
  noBalls: number;
  byes: number;
  legByes: number;
  batsmen: BattingScorecard[];
  bowlers: BowlingScorecard[];
}

export interface ScorecardResponse {
  match: Match;
  innings1?: InningsScorecard;
  innings2?: InningsScorecard;
}

export interface OverRunProgression {
  overNumber: number;
  cumulativeRuns: number;
  wickets: number;
}

export interface MatchSummary {
  matchId: number;
  title: string;
  status: MatchStatus;
  resultText?: string;
  manOfTheMatch?: string;

  team1Name: string;
  team1Score: string;
  team1Overs: string;

  team2Name?: string;
  team2Score?: string;
  team2Overs?: string;

  topBatter1?: string;
  topBatter1Stats?: string;
  topBowler1?: string;
  topBowler1Stats?: string;

  topBatter2?: string;
  topBatter2Stats?: string;
  topBowler2?: string;
  topBowler2Stats?: string;

  innings1Progression: OverRunProgression[];
  innings2Progression: OverRunProgression[];
}

export interface BallEvent {
  id: number;
  matchId: number;
  inningsId: number;
  overNumber: number;
  ballNumberInOver: number;
  totalLegalBallsAtPoint: number;
  runsScored: number;
  extraType: ExtraType;
  extraRuns: number;
  isWicket: boolean;
  wicketType: WicketType;
  batsman: Player;
  bowler: Player;
  nonStriker?: Player;
  commentaryText: string;
  ballDisplay: string;
  scoreAfterBall: number;
  wicketsAfterBall: number;
  timestamp: string;
}

export interface ScoreBallPayload {
  runsScored: number;
  extraType: ExtraType;
  extraRuns: number;
  isWicket: boolean;
  wicketType: WicketType;
  customCommentary?: string;
}

export interface CreateMatchPayload {
  title: string;
  team1Id: number;
  team2Id: number;
  tossWinnerId?: number;
  tossDecision?: TossDecision;
  matchType: MatchType;
  totalOversLimit: number;
  venue: string;
  city: string;
}

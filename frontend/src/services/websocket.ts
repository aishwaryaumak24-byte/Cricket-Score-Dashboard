import { Client, IMessage } from '@stomp/stompjs';
import { LiveMatchUpdate } from '../types/cricket';

export class CricketWebSocketService {
  private client: Client | null = null;
  private matchSubscription: any = null;
  private matchesSubscription: any = null;
  private currentMatchId: number | null = null;
  private onMatchUpdateCallback: ((update: LiveMatchUpdate) => void) | null = null;
  private onGeneralUpdateCallback: ((update: LiveMatchUpdate) => void) | null = null;
  public isConnected = false;

  constructor() {
    this.initClient();
  }

  private initClient() {
    try {
      if (typeof window === 'undefined') return;

      const isHttps = window.location.protocol === 'https:';
      const wsProto = isHttps ? 'wss:' : 'ws:';
      // In Vite dev (5173), proxy handles /ws-cricket, or connect directly to 8080
      const host = window.location.port === '5173' ? 'localhost:8080' : window.location.host;
      const brokerURL = `${wsProto}//${host}/ws-cricket`;

      this.client = new Client({
        brokerURL,
        reconnectDelay: 3000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
        debug: () => {},
      });

      this.client.onConnect = () => {
        this.isConnected = true;
        console.log('✅ Connected to Cricket STOMP WebSocket');

        if (this.currentMatchId) {
          this.subscribeToMatch(this.currentMatchId, this.onMatchUpdateCallback);
        }
        if (this.onGeneralUpdateCallback) {
          this.subscribeToAllMatches(this.onGeneralUpdateCallback);
        }
      };

      this.client.onDisconnect = () => {
        this.isConnected = false;
        console.log('ℹ️ Disconnected from Cricket STOMP WebSocket');
      };

      this.client.onStompError = (frame) => {
        console.warn('STOMP broker warning:', frame.headers['message']);
      };

      this.client.onWebSocketError = (err) => {
        console.warn('WebSocket connection error, falling back to REST sync:', err);
      };

      this.client.activate();
    } catch (e) {
      console.warn('Failed to initialize WebSocket client:', e);
    }
  }

  public subscribeToMatch(matchId: number, callback: ((update: LiveMatchUpdate) => void) | null) {
    this.currentMatchId = matchId;
    this.onMatchUpdateCallback = callback;

    if (!this.client || !this.isConnected) {
      return;
    }

    try {
      if (this.matchSubscription) {
        this.matchSubscription.unsubscribe();
        this.matchSubscription = null;
      }

      const destination = `/topic/match/${matchId}`;
      this.matchSubscription = this.client.subscribe(destination, (message: IMessage) => {
        try {
          const update: LiveMatchUpdate = JSON.parse(message.body);
          if (this.onMatchUpdateCallback) {
            this.onMatchUpdateCallback(update);
          }
        } catch (err) {
          console.error('Failed to parse match live update JSON', err);
        }
      });
    } catch (e) {
      console.warn('Subscription error for match', matchId, e);
    }
  }

  public subscribeToAllMatches(callback: ((update: LiveMatchUpdate) => void) | null) {
    this.onGeneralUpdateCallback = callback;

    if (!this.client || !this.isConnected) {
      return;
    }

    try {
      if (this.matchesSubscription) {
        this.matchesSubscription.unsubscribe();
        this.matchesSubscription = null;
      }

      this.matchesSubscription = this.client.subscribe('/topic/matches', (message: IMessage) => {
        try {
          const update: LiveMatchUpdate = JSON.parse(message.body);
          if (this.onGeneralUpdateCallback) {
            this.onGeneralUpdateCallback(update);
          }
        } catch (err) {
          console.error('Failed to parse general update', err);
        }
      });
    } catch (e) {
      console.warn('Subscription error for /topic/matches', e);
    }
  }

  public disconnect() {
    try {
      if (this.matchSubscription) {
        this.matchSubscription.unsubscribe();
        this.matchSubscription = null;
      }
      if (this.matchesSubscription) {
        this.matchesSubscription.unsubscribe();
        this.matchesSubscription = null;
      }
      if (this.client) {
        this.client.deactivate();
        this.client = null;
      }
    } catch (e) {
      console.warn('Error during disconnect:', e);
    }
    this.isConnected = false;
  }
}

export const wsService = new CricketWebSocketService();

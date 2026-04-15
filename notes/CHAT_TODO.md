# Chat Feature — TODO List

## 🏗️ Stack Context
- **Backend:** Spring Boot 3.5.6 + Java 21 + Spring Security + JWT + PostgreSQL
- **Frontend:** Angular 20 + RxJS + Angular Material
- **Infrastructure:** Docker Compose (3 services: db, backend, frontend)

---

## ⚡ Chosen Solution: STOMP over WebSocket

| Option | Verdict |
|---|---|
| **STOMP over WebSocket** | ✅ Best fit — full-duplex, pub/sub channels, Spring native support |
| Server-Sent Events (SSE) | ❌ One-way only (server → client) |
| Long Polling | ❌ Outdated, inefficient |
| External broker (Kafka, RabbitMQ) | ⚠️ Overkill for current scale |

---

## ✅ TODO

### 🔧 Phase 1 — Backend: WebSocket Infrastructure

- [ ] **Add Spring WebSocket dependency** to `pom.xml`
  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-websocket</artifactId>
  </dependency>
  ```
  📖 [Spring WebSocket docs](https://docs.spring.io/spring-framework/reference/web/websocket.html)

- [ ] **Configure STOMP WebSocket endpoint** — create a `WebSocketConfig` class annotated with `@EnableWebSocketMessageBroker`, register a `/ws` endpoint and message broker prefixes (`/topic`, `/app`)
  📖 [Configuring STOMP](https://docs.spring.io/spring-framework/reference/web/websocket/stomp/configuration.html)

- [ ] **Secure the WebSocket endpoint** — integrate existing JWT filter with the WebSocket handshake using `HandshakeInterceptor` or `ChannelInterceptor` to authenticate the token passed during connection
  📖 [WebSocket Security](https://docs.spring.io/spring-security/reference/servlet/integrations/websocket.html)

- [ ] **Create `Channel` entity + repository** — fields: `id`, `name`, `description`, `createdBy`, `createdAt`, `members` (ManyToMany with User)

- [ ] **Create `Message` entity + repository** — fields: `id`, `content`, `sentAt`, `sender` (ManyToOne User), `channel` (ManyToOne Channel)

- [ ] **Create REST endpoints for channel management**
  - `POST /api/channels` — create channel
  - `GET /api/channels` — list available channels
  - `POST /api/channels/{id}/join` — join channel
  - `GET /api/channels/{id}/messages` — load message history (paginated)

- [ ] **Create `@MessageMapping` controller** for real-time messaging
  - `@MessageMapping("/chat/{channelId}/send")` → broadcasts to `/topic/chat/{channelId}`
  - Persist message to DB before broadcasting

- [ ] **Add pagination to message history** using Spring Data's `Pageable`
  📖 [Spring Data JPA Pagination](https://docs.spring.io/spring-data/jpa/reference/repositories/query-methods-details.html)

---

### 🎨 Phase 2 — Frontend: Angular WebSocket + UI

- [ ] **Install STOMP client library**
  ```bash
  npm install @stomp/rx-stomp
  ```
  📖 [@stomp/rx-stomp docs](https://stomp-js.github.io/rx-stomp/injectables/RxStompService.html)

- [ ] **Create `ChatService`** — wraps `RxStompService`, handles:
  - Connecting with JWT token in headers
  - Subscribing to `/topic/chat/{channelId}`
  - Publishing to `/app/chat/{channelId}/send`
  - Reconnection logic

- [ ] **Implement JWT passing on WebSocket connect** — pass the token as a STOMP header (`Authorization: Bearer <token>`) during connection setup

- [ ] **Create Channel List component** — fetches channels via REST, allows creating/joining channels

- [ ] **Create Chat Window component** — displays messages, loads history on open, subscribes to live messages via STOMP, auto-scrolls to latest

- [ ] **Create Message Input component** — sends messages via STOMP publish, supports Enter key

- [ ] **Handle connection state in UI** — show "connecting…" / "disconnected" states using `RxStompService`'s `connectionState$` observable

---

### 🗄️ Phase 3 — Database

- [ ] **Add DB migration for `channels` and `messages` tables** — consider using Flyway or Liquibase for schema migrations
  📖 [Flyway with Spring Boot](https://docs.spring.io/spring-boot/how-to/data-initialization.html#howto.data-initialization.migration-tool.flyway)

- [ ] **Index `messages.channel_id` and `messages.sent_at`** for fast history queries

---

### 🔒 Phase 4 — Security & Polish

- [ ] **Authorize channel access** — only members of a channel can subscribe to its topic and send messages (check membership in the `@MessageMapping` handler)

- [ ] **Add CORS config for WebSocket** — ensure existing CORS config covers the `/ws` upgrade endpoint

- [ ] **Handle disconnection / presence** (optional) — track online users per channel using `SessionConnectEvent` / `SessionDisconnectEvent` Spring events
  📖 [Application Events](https://docs.spring.io/spring-framework/reference/web/websocket/stomp/application-context-events.html)

- [ ] **Rate limiting on messages** (optional) — prevent spam via a simple in-memory counter or using a `ChannelInterceptor`

---

### 🧪 Phase 5 — Testing

- [ ] **Backend:** Test `@MessageMapping` controllers using `StompClient` in integration tests with `SpringBootTest`
  📖 [Testing WebSocket STOMP](https://docs.spring.io/spring-framework/reference/web/websocket/stomp/testing.html)

- [ ] **Frontend:** Test `ChatService` using Jasmine + mock `RxStompService`

---

## 🔑 Key Concepts & Docs

| Topic | Link |
|---|---|
| STOMP Protocol | https://stomp.github.io/stomp-specification-1.2.html |
| Spring WebSocket + STOMP | https://docs.spring.io/spring-framework/reference/web/websocket/stomp.html |
| Spring WebSocket Security | https://docs.spring.io/spring-security/reference/servlet/integrations/websocket.html |
| @stomp/rx-stomp (Angular) | https://stomp-js.github.io/rx-stomp/ |
| Angular RxJS patterns | https://rxjs.dev/guide/overview |
| Spring Data JPA Pagination | https://docs.spring.io/spring-data/jpa/reference/ |

---

> 💡 **Tip:** Start with Phase 1 + 2 without WebSocket authentication first (just get messages flowing), then layer in JWT auth. This makes debugging much easier.
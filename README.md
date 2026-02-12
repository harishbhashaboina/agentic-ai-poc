# Agentic AI POC (Spring Boot)

This is a minimal Spring Boot project that demonstrates a master agent routing
requests to domain agents, with inter-agent communication.

Flow covered:
- MasterAgent routes to CancelAgent when the prompt contains "cancel".
- CancelAgent calls PaymentAgent to check for pending payments.

## Prerequisites
- Java 17+
- Maven 3.9+

## Run the application

```bash
mvn spring-boot:run
```

The app will start on `http://localhost:8080`.

## Example request (cancel membership)

```bash
curl -X POST http://localhost:8080/agentic/master \
  -H "Content-Type: application/json" \
  -d '{
        "prompt": "I want to cancel my membership.",
        "memberId": "M12349",
        "billingAccountId": "B88888"
      }'
```

## Direct endpoints

- `POST /agentic/master`
- `POST /agentic/cancel`
- `POST /agentic/payment`

## LM Studio integration (optional)

This project can call LM Studio using its OpenAI-compatible API.

1. Start LM Studio and enable the **Local Server** (OpenAI-compatible).
2. Note the base URL (default: `http://localhost:1234/v1`) and the model name.
3. Run the app with LLM support enabled:

```bash
AGENTIC_LLM_ENABLED=true \
AGENTIC_LLM_BASE_URL=http://localhost:1234/v1 \
AGENTIC_LLM_MODEL=your-model-name \
mvn spring-boot:run
```

The CancelAgent will ask the LLM to generate a one-sentence summary. If the LLM
is unreachable, the app falls back to the deterministic summary.

## Notes

- Pending payment check is deterministic for demo purposes:
  - Any `memberId` or `billingAccountId` ending with `9` is treated as pending.
- By default, LLM calls are disabled (`agentic.llm.enabled=false`).
